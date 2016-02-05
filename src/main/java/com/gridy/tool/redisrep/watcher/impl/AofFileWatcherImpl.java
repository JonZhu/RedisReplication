/**
 * 
 */
package com.gridy.tool.redisrep.watcher.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gridy.tool.redisrep.cmd.IRedisCommond;
import com.gridy.tool.redisrep.config.IRedis;
import com.gridy.tool.redisrep.replication.IReplication;
import com.gridy.tool.redisrep.util.ThreadUtil;
import com.gridy.tool.redisrep.watcher.IAofFileWatcher;

/**
 * 
 * @author zhujun
 * @date 2015-4-17
 *
 */
public class AofFileWatcherImpl implements IAofFileWatcher {

	private final static Logger LOG = LoggerFactory.getLogger(AofFileWatcherImpl.class);
	
	private List<IReplication> replicationList;
	

	@Override
	public void watch(IRedis redis) throws IOException {
		File aofFile = new File(redis.getAof());
		RandomAccessFile raFile = new RandomAccessFile(aofFile, "r");
		
		// read
		ReadBuffer readBuffer = new ReadBuffer();
		CmdDecoder cmdDecoder = new CmdDecoder(readBuffer);
		long aofFileLength = 0;
		while (true) {
			aofFileLength = aofFile.length();
			if (redis.getOffset() > aofFileLength) {
				LOG.warn("aof file[{}] may rewrited, old offset:{}, new length:{}", redis.getAof(), redis.getOffset(), aofFileLength);
				redis.setOffset(aofFileLength);
				
				// re open stream
				raFile.close();
				raFile = new RandomAccessFile(aofFile, "r");
				raFile.seek(aofFileLength);
				
				cmdDecoder.reset();
				readBuffer.clean();
			}
			
			IRedisCommond cmd = cmdDecoder.decode();
			if (cmd != null) {
				dispatch(cmd);
				redis.setOffset(cmd.getEndOffset()); // 记录偏移
				LOG.info("aof [{}] file offset: {}", redis.getAof(), cmd.getEndOffset());
			} else {
				int readSize = readBuffer.read(raFile, 512);
				if (readSize <= 0) {
					LOG.info("aof [{}] no more data, sleep 5 second, offset:{}", redis.getAof(), redis.getOffset());
					ThreadUtil.sleep(5000);
					continue;
				}
			}
		}
		
	}

	/**
	 * 分发命令到复制器
	 * 
	 * @param cmd
	 */
	private void dispatch(IRedisCommond cmd) {
		for (IReplication rep : replicationList) {
			rep.replicate(cmd);
		}
		
	}

	@Override
	public void addReplication(List<IReplication> reps) {
		if (replicationList == null) {
			replicationList = new ArrayList<>();
		}
		
		replicationList.addAll(reps);
	}

}
