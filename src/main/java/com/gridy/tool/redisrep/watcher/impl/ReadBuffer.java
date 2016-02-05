/**
 * 
 */
package com.gridy.tool.redisrep.watcher.impl;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidParameterException;

/**
 * 读缓冲
 * 
 * @author zhujun
 * @date 2015-4-19
 *
 */
public class ReadBuffer {

	private byte[] buffer;
	
	/**
	 * 缓冲中数据长度
	 */
	private int dataSize = 0;
	
	/**
	 * 已读数据全局偏移量
	 */
	private long globalOffset = 0;
	
	public int getDataSize() {
		return dataSize;
	}
	
	public int getFreeSize() {
		return (buffer == null ? 0 : buffer.length) - getDataSize();
	}
	
	public byte[] getData() {
		return buffer;
	}
	
	/**
	 * @param file
	 * @param length
	 * @return
	 * @throws IOException 
	 */
	public int read(RandomAccessFile file, int length) throws IOException {
		checkFree(length);
		int readSize = file.read(buffer, dataSize, length);
		if (readSize > 0) {
			dataSize += readSize;
			globalOffset = file.getFilePointer();
		}
		
		return readSize;
	}

	/**
	 * 检查Free空间
	 * 
	 * @param length
	 */
	private void checkFree(int length) {
		if (length > getFreeSize()) {
			// 无足够空间, 分配
			
			if (buffer == null) {
				buffer = new byte[length];
			} else {
				byte[] newBuffer = new byte[buffer.length + length];
				System.arraycopy(buffer, 0, newBuffer, 0, dataSize);
				buffer = newBuffer;
			}
		}
		
	}

	/**
	 * 释放buffer前部分数据
	 * @param size
	 */
	public void release(int size) {
		if (size > dataSize) {
			throw new InvalidParameterException("size不能大于dataSize");
		}
		int newDataSize = dataSize - size;
		System.arraycopy(buffer, size, buffer, 0, newDataSize);
		dataSize = newDataSize;
	}
	
	/**
	 * 清除缓存数据
	 * 
	 * @author zhujun
	 * @date 2015年9月24日
	 */
	public void clean() {
		buffer = null;
		dataSize = 0;
	}

	/**
	 * 计算相对位置的全局位置
	 * 
	 * @param index
	 * @return
	 * @author zhujun
	 * @date 2015-4-22
	 */
	public long convertToGlobalOffset(int index) {
		return globalOffset - dataSize + index;
	}
	
	
}
