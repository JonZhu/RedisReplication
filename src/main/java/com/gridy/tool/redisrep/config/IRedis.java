/**
 * 
 */
package com.gridy.tool.redisrep.config;

import java.util.List;

import com.gridy.tool.redisrep.replication.IReplication;

/**
 * @author zhujun
 * @date 2015-4-17
 *
 */
public interface IRedis {

	String getId();
	
	String getAof();
	
	long getOffset();
	void setOffset(long value);

	/**
	 * @return
	 */
	List<IReplication> getReplicationList();
	
}
