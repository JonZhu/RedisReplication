/**
 * 
 */
package com.gridy.tool.redisrep.watcher.impl;

import java.io.File;

/**
 * @author zhujun
 * @date 2015年9月24日
 *
 */
public class AofFileWatcherImplTest {

	public static void main(String[] args) {
		try {
			//RandomAccessFile file = new RandomAccessFile("/usr/local/redis/data/appendonly-6401.aof", "r");
			File file = new File("/usr/local/redis/data/appendonly-6401.aof");
			while (true) {
				System.out.println(file.length());
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
