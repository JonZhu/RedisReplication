/**
 * 
 */
package com.gridy.tool.redisrep.replication;

import java.util.List;

import com.gridy.tool.redisrep.cmd.IRedisCommond;
import com.gridy.tool.redisrep.config.IRdb;
import com.gridy.tool.redisrep.config.IRedis;

/**
 * 数据复制
 * 
 * @author zhujun
 * @date 2015-4-17
 *
 */
public interface IReplication {

	/**
	 * 来源redis
	 * @return
	 */
	IRedis getRedis();
	
	/**
	 * 目标 rdb数据库
	 * @return
	 */
	IRdb getRdb();
	
	/**
	 * 数据映射, 映射规则
	 * @return
	 */
	List<IDataMapping> getDataMappingList();
	
	/**
	 * 复制
	 * 
	 * @param cmd
	 */
	void replicate(IRedisCommond cmd);
	
	
}
