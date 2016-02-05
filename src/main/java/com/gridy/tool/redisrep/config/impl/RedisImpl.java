/**
 * 
 */
package com.gridy.tool.redisrep.config.impl;

import java.util.List;

import org.dom4j.Element;

import com.gridy.tool.redisrep.config.IRedis;
import com.gridy.tool.redisrep.replication.IReplication;

/**
 * @author zhujun
 * @date 2015-4-20
 *
 */
public class RedisImpl implements IRedis {

	private Element ele;
	
	private String id;
	
	private String aof;
	
	private long offset;
	
	private List<IReplication> replicationList;
	
	/**
	 * @param ele 配置xml节点, 用于保存数据
	 */
	public RedisImpl(Element ele) {
		this.ele = ele;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getAof() {
		return aof;
	}

	@Override
	public long getOffset() {
		return offset;
	}

	@Override
	public void setOffset(long value) {
		this.offset = value;
		ele.element("offset").setText(String.valueOf(offset));
	}

	@Override
	public List<IReplication> getReplicationList() {
		return replicationList;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param aof the aof to set
	 */
	public void setAof(String aof) {
		this.aof = aof;
	}

	/**
	 * @param replicationList the replicationList to set
	 */
	public void setReplicationList(List<IReplication> replicationList) {
		this.replicationList = replicationList;
	}

}
