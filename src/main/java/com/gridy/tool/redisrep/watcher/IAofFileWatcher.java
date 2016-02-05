/**
 * 
 */
package com.gridy.tool.redisrep.watcher;

import java.io.IOException;
import java.util.List;

import com.gridy.tool.redisrep.config.IRedis;
import com.gridy.tool.redisrep.replication.IReplication;

/**
 * Aof文件监视器, 当文件中有数据时, 调用命令协议解码器, 解析命令, 
 * 最后将命令派发给复制器完成复制。
 * 
 * @author zhujun
 * @date 2015-4-17
 *
 */
public interface IAofFileWatcher {

	/**
	 * 添加复制器到该监视器上
	 * @param reps
	 * @author zhujun
	 */
	void addReplication(List<IReplication> reps);
	
	/**
	 * 开始监视redis aof文件
	 * @param redis
	 * @throws IOException
	 * @author zhujun
	 */
	void watch(IRedis redis) throws IOException;
	
}
