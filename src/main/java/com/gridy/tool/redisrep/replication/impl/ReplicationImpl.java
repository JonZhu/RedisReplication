/**
 * 
 */
package com.gridy.tool.redisrep.replication.impl;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gridy.tool.redisrep.cmd.IRedisCommond;
import com.gridy.tool.redisrep.config.IRdb;
import com.gridy.tool.redisrep.config.IRedis;
import com.gridy.tool.redisrep.exception.ReplicationException;
import com.gridy.tool.redisrep.replication.IDataMapping;
import com.gridy.tool.redisrep.replication.IDataMappingField;
import com.gridy.tool.redisrep.replication.IReplication;
import com.gridy.tool.redisrep.util.CrudFlagUtil;
import com.gridy.tool.redisrep.util.SqlUtil;

/**
 * @author zhujun
 * @date 2015-4-19
 *
 */
public class ReplicationImpl implements IReplication {

	private final static Logger LOG = LoggerFactory.getLogger(ReplicationImpl.class);
	
	private IRedis redis;
	private IRdb rdb;
	private List<IDataMapping> dataMappingList;
	
	@Override
	public IRedis getRedis() {
		return redis;
	}

	@Override
	public IRdb getRdb() {
		return rdb;
	}

	public void setRedis(IRedis redis) {
		this.redis = redis;
	}

	public void setRdb(IRdb rdb) {
		this.rdb = rdb;
	}

	@Override
	public void replicate(IRedisCommond cmd) {
		List<IDataMapping> mappings = getDataMappingList();
		for (IDataMapping mapping : mappings) {
			if (!mapping.match(cmd)) {
				continue;
			}
			
			replicateToMapping(cmd, mapping);
		}
	}

	/**
	 * @param cmd
	 * @param mapping
	 * @author zhujun
	 * @date 2015-4-20
	 */
	private void replicateToMapping(IRedisCommond cmd, IDataMapping mapping) {
		LOG.debug("开始复制命令 {} 到: {}", cmd.getName(), mapping.getToTable());
		
		String sql = generateSql(cmd, mapping);
		
		executeSql(sql);
	}

	/**
	 * 执行sql
	 * 
	 * @param sql
	 * @author zhujun
	 * @date 2015-4-20
	 */
	private void executeSql(String sql) {
		if (sql == null) {
			return;
		}
		
		LOG.debug("Start execute sql: {}", sql);
		
		long startTime = System.currentTimeMillis();
		
		Connection conn = null;
		Statement stat = null;
		try {
			conn = getRdb().getDataSource().getConnection();
			stat = conn.createStatement();
			stat.executeUpdate(sql);
		} catch (Exception e) {
			throw new ReplicationException("执行sql ["+ sql +"]出错", e);
		} finally {
			SqlUtil.close(conn, stat);
		}
		
		LOG.debug("Execute sql time: {}, sql: {}", System.currentTimeMillis() - startTime, sql);
	}

	/**
	 * 生成sql
	 * 
	 * @param cmd
	 * @param mapping
	 * @return
	 * @author zhujun
	 * @date 2015-4-20
	 */
	private String generateSql(IRedisCommond cmd, IDataMapping mapping) {
		if (CrudFlagUtil.isDelete(cmd.getCrudFlag())) {
			// 删除数据
			return generateDeleteSql(cmd, mapping);
		} else if (CrudFlagUtil.isCreate(cmd.getCrudFlag()) || CrudFlagUtil.isUpdate(cmd.getCrudFlag())) {
			// 更新数据
			return generateSaveOrUpdateSql(cmd, mapping);
		}
		
		return null;
	}

	/**
	 * @param cmd
	 * @param mapping
	 * @return
	 * @author zhujun
	 * @date 2015-4-20
	 */
	private String generateSaveOrUpdateSql(IRedisCommond cmd, IDataMapping mapping) {
		// insert test1(id, name) values (1, '55511113') on duplicate key update name = values(name);
		StringBuffer sql = new StringBuffer("insert ").append(mapping.getToTable());
		StringBuffer columns = new StringBuffer();
		StringBuffer values = new StringBuffer();
		StringBuffer updates = new StringBuffer();
		
		boolean isFirstField = true;
		byte[] fieldValue = null;
		for (IDataMappingField field : mapping.getFieldList()) {
			fieldValue = cmd.getFieldValue(field.getName(), mapping);
			if (fieldValue == null) {
				// 未操作该字段
				continue;
			}
			
			if (isFirstField) {
				isFirstField = false;
			} else {
				columns.append(",");
				values.append(",");
				updates.append(",");
			}
			
			columns.append(field.getTo());
			values.append(SqlUtil.stringValue(field.getConvertor().convert(fieldValue)));
			updates.append(field.getTo()).append("=values(").append(field.getTo()).append(")");
		}
		
		sql.append("(").append(columns).append(") values(").append(values).append(") ")
		   .append("on duplicate key update ").append(updates);
		
		return sql.toString();
	}

	/**
	 * @param cmd
	 * @param mapping
	 * @return
	 * @author zhujun
	 * @date 2015-4-20
	 */
	private String generateDeleteSql(IRedisCommond cmd, IDataMapping mapping) {
		StringBuffer sql = new StringBuffer("delete ");
		sql.append(mapping.getToTable()).append(" where ");
		boolean addAnd = false;
		for (IDataMappingField field : mapping.getFieldList()) {
			if (field.isId()) {
				if (addAnd) {
					sql.append(" and ");
				} else {
					addAnd = true;
				}
				
				
				sql.append(" ").append(field.getTo()).append("=")
				   .append(SqlUtil.stringValue(field.getConvertor().convert(cmd.getFieldValue(field.getName(), mapping))));
			}
		}
		
		return sql.toString();
	}

	@Override
	public List<IDataMapping> getDataMappingList() {
		return dataMappingList;
	}

	public void setDataMappingList(List<IDataMapping> dataMappingList) {
		this.dataMappingList = dataMappingList;
	}

}
