/**
 * 
 */
package com.gridy.tool.redisrep.config.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.gridy.tool.redisrep.config.IConfig;
import com.gridy.tool.redisrep.config.IRedis;
import com.gridy.tool.redisrep.convertor.ConvertorFactory;
import com.gridy.tool.redisrep.replication.IDataMapping;
import com.gridy.tool.redisrep.replication.IDataMappingField;
import com.gridy.tool.redisrep.replication.IReplication;
import com.gridy.tool.redisrep.replication.impl.DataMappingFieldImpl;
import com.gridy.tool.redisrep.replication.impl.DataMappingImpl;
import com.gridy.tool.redisrep.replication.impl.ReplicationImpl;
import com.gridy.tool.redisrep.util.RedisDataTypeUtil;
import com.gridy.tool.redisrep.util.StringUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * @author zhujun
 * @date 2015-4-17
 *
 */
public class XmlConfigImpl implements IConfig {

	private File configFile;
	private Document configDoc;
	
	private final List<ReplicationImpl> replicationList = new ArrayList<>();
	private final List<IRedis> redisList = new ArrayList<>();
	
	@Override
	public void load(File config) {
		SAXReader reader = new SAXReader();
		try {
			// 解析配置
			Document doc = reader.read(config);
			Element reps = doc.getRootElement();
			
			final Map<String, RedisImpl> redisMap = new HashMap<>();
			final Map<String, RdbImpl> rdbMap = new HashMap<>();
			final Map<IReplication, String> repRedisIdMap = new HashMap<>();
			final Map<IReplication, String> repRdbIdMap = new HashMap<>();
			
			for (Element ele : (List<Element>)reps.elements()) {
				if ("redis".equals(ele.getName())) {
					RedisImpl redis = parseRedisEle(ele);
					redisList.add(redis);
					redisMap.put(redis.getId(), redis);
				} else if ("rdb".equals(ele.getName())) {
					RdbImpl rdb = parseRdbEle(ele);
					rdbMap.put(rdb.getId(), rdb);
				} else if ("replication".equals(ele.getName())) {
					ReplicationImpl rep = parseReplicationEle(ele);
					String repRedisId = ele.attributeValue("redis");
					repRedisIdMap.put(rep, repRedisId);
					String repRdbId = ele.attributeValue("rdb");
					repRdbIdMap.put(rep, repRdbId);
					replicationList.add(rep);
				}
			}
			
			// 校验配置
			if (replicationList.size() == 0) {
				throw new Exception("replication 数目不能为0");
			}
			for (ReplicationImpl rep : replicationList) {
				RedisImpl redis = redisMap.get(repRedisIdMap.get(rep));
				if (redis == null) {
					throw new Exception("未找到replication 的 redis");
				}
				rep.setRedis(redis);
				if (redis.getReplicationList() == null) {
					redis.setReplicationList(new ArrayList<IReplication>());
				}
				redis.getReplicationList().add(rep);
				
				RdbImpl rdb = rdbMap.get(repRdbIdMap.get(rep));
				if (rdb == null) {
					throw new Exception("未找到replication 的 rdb");
				}
				initDataSource(rdb); // 被使用的rdb在这里初始化连接池
				rep.setRdb(rdb);
			}
			
			// 配置文件加载完成
			this.configFile = config;
			this.configDoc = doc;
			
		} catch (Exception e) {
			throw new RuntimeException("加载配置文件失败", e);
		}
	}

	
	/**
	 * 初始化数据库连接池
	 * 
	 * @param rdb
	 * @author zhujun
	 * @date 2015-4-21
	 */
	private void initDataSource(RdbImpl rdb) throws Exception {
		try {
			ComboPooledDataSource dataSource = new ComboPooledDataSource();
			dataSource.setDriverClass(rdb.getDriver());
			dataSource.setJdbcUrl(rdb.getUrl());
			dataSource.setUser(rdb.getUser());
			dataSource.setPassword(rdb.getPass());
			dataSource.setInitialPoolSize(2);
			dataSource.setDescription("DataSource of rdb["+ rdb.getId() +"]");
			
			dataSource.setIdleConnectionTestPeriod(60);
			dataSource.setMaxIdleTime(60);
			dataSource.setAcquireRetryAttempts(5);
			
			rdb.setDataSource(dataSource);
			
		} catch (Exception e) {
			throw new Exception("初始化rdb["+ rdb.getId() +"]数据库连接池失败", e);
		}
	}


	/**
	 * @param ele
	 * @return
	 */
	private ReplicationImpl parseReplicationEle(Element ele) {
		ReplicationImpl rep = new ReplicationImpl();
		List<Element> mappingEles = ele.elements();
		if (mappingEles == null || mappingEles.isEmpty()) {
			throw new RuntimeException("replication的映射配置不能为空");
		}
		
		List<IDataMapping> dataMappingList = new ArrayList<>();
		for (Element mappingEle : mappingEles) {
			dataMappingList.add(parseDataMappingEle(mappingEle));
		}
		rep.setDataMappingList(dataMappingList);
		
		return rep;
	}
	
	/**
	 * 解析数据映射配置
	 * 
	 * @param mappingEle
	 * @return
	 * @author zhujun
	 * @date 2015-4-21
	 */
	private IDataMapping parseDataMappingEle(Element mappingEle) {
		DataMappingImpl mapping = new DataMappingImpl();
		String dataTypeName = mappingEle.getName();
		Integer dataType = RedisDataTypeUtil.getType(dataTypeName);
		if (dataType == null) {
			throw new RuntimeException("不支持的映射规则: " + dataTypeName);
		}
		mapping.setDataType(dataType);
		mapping.setKey(mappingEle.attributeValue("key"));
		if (StringUtil.isEmpty(mapping.getKey())) {
			throw new RuntimeException("Mapping["+dataTypeName+"] key不能为空");
		}
		mapping.setToTable(mappingEle.attributeValue("toTable"));
		if (StringUtil.isEmpty(mapping.getToTable())) {
			throw new RuntimeException("Mapping["+dataTypeName+"] toTable不能为空");
		}
		
		List<Element> fieldEleList = mappingEle.elements();
		if (fieldEleList == null || fieldEleList.isEmpty()) {
			throw new RuntimeException("映射字段不能为空");
		}
		
		// 解析FieldList
		List<IDataMappingField> fieldList = new ArrayList<>();
		mapping.setFieldList(fieldList);
		for (Element fieldEle : fieldEleList) {
			DataMappingFieldImpl field = new DataMappingFieldImpl();
			field.setIsId("id".equals(fieldEle.getName()));
			field.setName(fieldEle.attributeValue("name"));
			field.setTo(fieldEle.attributeValue("to"));
			
			if (StringUtil.isEmpty(field.getName())) {
				throw new RuntimeException("DataMappingField name不能为空");
			}
			
			if (StringUtil.isEmpty(field.getTo())) {
				throw new RuntimeException("DataMappingField["+ field.getName() +"] to不能为空");
			}
			
			String convertorName = fieldEle.attributeValue("convertor");
			if (StringUtil.isEmpty(convertorName)) {
				throw new RuntimeException("DataMappingField["+ field.getName() +"] convertor不能为空");
			}
			field.setConvertor(ConvertorFactory.getConvertor(convertorName));
			if (field.getConvertor() == null) {
				throw new RuntimeException("不能找到DataMappingField["+ field.getName() +"] convertor["+ convertorName +"]");
			}
			
			fieldList.add(field);
		}
		
		if (fieldList.isEmpty()) {
			throw new RuntimeException("Mapping["+dataTypeName+"] DataMappingField 为空");
		}
		
		return mapping;
	}


	/**
	 * @param ele
	 * @return
	 */
	private RdbImpl parseRdbEle(Element ele) {
		RdbImpl rdb = new RdbImpl();
		rdb.setId(ele.elementText("id"));
		rdb.setUrl(ele.elementText("url"));
		rdb.setDriver(ele.elementText("driver"));
		rdb.setUser(ele.elementText("user"));
		rdb.setPass(ele.elementText("pass"));
		
		if (rdb.getId() == null || "".equals(rdb.getId())) {
			throw new RuntimeException("rdb id不能为空");
		}
		
		if (rdb.getUrl() == null || "".equals(rdb.getUrl())) {
			throw new RuntimeException("rdb["+ rdb.getId() +"] url不能为空");
		}
		
		if (rdb.getDriver() == null || "".equals(rdb.getDriver())) {
			throw new RuntimeException("rdb["+ rdb.getId() +"] driver不能为空");
		}
		
		if (rdb.getUser() == null || "".equals(rdb.getUser())) {
			throw new RuntimeException("rdb["+ rdb.getId() +"] user不能为空");
		}
		
		return rdb;
	}
	
	/**
	 * @param ele
	 * @return
	 */
	private RedisImpl parseRedisEle(Element ele) {
		RedisImpl redis = new RedisImpl(ele);
		redis.setId(ele.elementText("id"));
		redis.setAof(ele.elementText("aof"));
		
		if (redis.getId() == null || "".equals(redis.getId())) {
			throw new RuntimeException("redis id不能为空");
		}
		
		if (redis.getAof() == null || "".equals(redis.getAof())) {
			throw new RuntimeException("redis["+ redis.getId() +"] aof不能为空");
		}
		
		try {
			redis.setOffset(Long.valueOf(ele.elementText("offset")));
		} catch (Exception e) {
			throw new RuntimeException("redis["+ redis.getId() +"] offset不正确");
		}
		
		return redis;
	}

	@Override
	public void save() {
		if (configFile != null && configDoc != null) {
			OutputStreamWriter writer = null;
			try {
				writer = new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8");
				configDoc.write(writer);
				writer.flush();
			} catch (Exception e) {
				throw new RuntimeException("保存配置数据失败", e);
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	@Override
	public List<? extends IReplication> getReplicationList() {
		return replicationList;
	}

	@Override
	public List<IRedis> getRedisList() {
		return redisList;
	}

}
