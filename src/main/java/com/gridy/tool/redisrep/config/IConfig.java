/**
 * 
 */
package com.gridy.tool.redisrep.config;

import java.io.File;
import java.util.List;

import com.gridy.tool.redisrep.replication.IReplication;

/**
 * @author zhujun
 * @date 2015-4-17
 *
 */
public interface IConfig {

	void load(File config);
	
	void save();

	/**
	 * @return
	 */
	List<? extends IReplication> getReplicationList();
	
	List<IRedis> getRedisList();
	
}
