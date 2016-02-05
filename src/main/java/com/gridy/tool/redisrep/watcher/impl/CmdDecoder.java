/**
 * 
 */
package com.gridy.tool.redisrep.watcher.impl;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gridy.tool.redisrep.cmd.IRedisCommond;
import com.gridy.tool.redisrep.cmd.impl.AbstractCommond;
import com.gridy.tool.redisrep.cmd.impl.CommondFactory;

/**
 * Redis 命令解析器
 * 
 * @author zhujun
 * @date 2015-4-19
 *
 */
public class CmdDecoder {

	private final static Logger LOG = LoggerFactory.getLogger(CmdDecoder.class);
	
	/**
	 * Cmd开始 标识
	 */
	private final static byte[] FLAG_START = new byte[]{'*'};
	
	/**
	 * 参数长度开始 标识
	 */
	private final static byte[] FLAG_ARG_LENG_START = new byte[]{'$'};
	
	/**
	 * 参数分隔 标识
	 */
	private final static byte[] FLAG_SEPERATOR = new byte[]{'\r', '\n'};
	
	private static interface Phase {
		int UnStart = 0; // 命令未开始
		int Start = 1; // 命令开始
		int ARG_LIST = 2; // 参数列表阶段
		int COMPLETE = 3; // 命令解析完成
	}
	
	/**
	 * 数据缓冲
	 */
	private ReadBuffer readBuffer;
	
	/**
	 * 解析执行到数据缓冲中的位置
	 */
	private int offset = 0;
	
	/**
	 * 当前解析命令的阶段
	 */
	private int currentPhase = Phase.UnStart;
	
	/**
	 * 当前命令参数 总数量
	 */
	private int currentCmdArgCount = 0;
	
	/**
	 * 当前命令参数 列表
	 */
	private byte[][] currentCmdArgs = null;
	
	/**
	 * 已解析的命令 数量
	 */
	private int parsedArgCount = 0;
	
	// 记录当前命令的 起始 位置
	private long currentCmdStartOffset;
	private long currentCmdEndOffset;
	
	/**
	 * @param readBuffer
	 */
	public CmdDecoder(ReadBuffer readBuffer) {
		if (readBuffer == null) {
			throw new NullPointerException("ReadBuffer 不能为空");
		}
		this.readBuffer = readBuffer;
	}

	/**
	 * 解析命令
	 * 
	 * <p>
	 * eg cmd:<br/>
	 * 命令 SET msg hello 的协议为：<br/>
	 * *3\r\n$3\r\nSET\r\n$3\r\nmsg$5\r\nhello\r\n
	 * </p>
	 * 
	 * @return 如果返回null, 表示当前ReadBuffer中没有足够数据, 无法得到完整命令
	 */
	public IRedisCommond decode() {
		// 命令解码循环
		while (true) {
			
			if (currentPhase == Phase.UnStart) {
				// 查找命令起始 标识
				int index = findBytes(offset, FLAG_START);
				if (index > -1) {
					// 查找到
					offset = index + FLAG_START.length;
					currentPhase = Phase.Start;
					
					currentCmdStartOffset = readBuffer.convertToGlobalOffset(index);
					continue;
				} else {
					return null;
				}
			} else if (currentPhase == Phase.Start) {
				// 命令已经进入 FLAG_START, 开始解析命令参数个数
				int index = findBytes(offset, FLAG_SEPERATOR);
				if (index > -1) {
					// 找到参数数目结束标识
					byte[] argCountBytes = Arrays.copyOfRange(readBuffer.getData(), offset, index);
					currentCmdArgCount = Integer.valueOf(new String(argCountBytes));
					LOG.debug("currentCmdArgCount: {}", currentCmdArgCount);
					currentCmdArgs = new byte[currentCmdArgCount][];
					
					offset = index + FLAG_SEPERATOR.length;
					currentPhase = Phase.ARG_LIST;
					continue;
				} else {
					return null;
				}
			} else if (currentPhase == Phase.ARG_LIST) {
				// 开始解析参数列表
				for (int i = parsedArgCount; i < currentCmdArgCount; i++) {
					// 获取参数字节长度
					int lengStartIndex = findBytes(offset, FLAG_ARG_LENG_START);
					if (lengStartIndex < 0) {
						return null;
					}
					
					int lengthEndIndex = findBytes(lengStartIndex + FLAG_ARG_LENG_START.length, FLAG_SEPERATOR);
					if (lengthEndIndex < 0) {
						return null;
					}
					
					byte[] argLengthBytes = Arrays.copyOfRange(readBuffer.getData(), lengStartIndex + FLAG_ARG_LENG_START.length, lengthEndIndex);
					int argLength = Integer.valueOf(new String(argLengthBytes));
					LOG.debug("argLength: {}", argLength);
					
					// 参数参数值
					int argEndIndex = lengthEndIndex + FLAG_SEPERATOR.length + argLength; // 参数结束位置 (exclude)
					if (readBuffer.getDataSize() < argEndIndex) {
						// 缓冲中无完整的参数值
						return null;
					}
					
					// 取值
					currentCmdArgs[i] =  Arrays.copyOfRange(readBuffer.getData(), lengthEndIndex + FLAG_SEPERATOR.length, argEndIndex);
					
					parsedArgCount++;
					offset = argEndIndex;
					
				}
				
				// 参数列表解析完成
				currentPhase = Phase.COMPLETE;
				currentCmdEndOffset = readBuffer.convertToGlobalOffset(offset + FLAG_SEPERATOR.length);
			} else if (currentPhase == Phase.COMPLETE) {
				// 命令完成, 构建命令
				IRedisCommond cmd = CommondFactory.createCmd(currentCmdArgs);
				AbstractCommond aCmd = (AbstractCommond)cmd;
				aCmd.setStartOffset(currentCmdStartOffset);
				aCmd.setEndOffset(currentCmdEndOffset);
				
				// 释放buffer
				readBuffer.release(offset); 
				offset = 0;
				
				// 重置数据
				reset();
				
				return cmd;
			}
		}
		
	}
	
	
	/**
	 * @param start 搜索起始位置
	 * @param value
	 * @return 搜索值的起始位置, 无匹配返回-1
	 */
	private int findBytes(int start, byte[] value) {
		int bufferDataSize = readBuffer.getDataSize();
		byte[] bufferData = readBuffer.getData();
		for (int i = start; i < bufferDataSize - value.length + 1; i++) {
			int matchCount = 0;
			for (int j = 0; j < value.length; j++) {
				if (value[j] == bufferData[i + j]) {
					matchCount++;
				} else {
					break;
				}
			}
			
			if (matchCount == value.length) {
				return i;
			}
		}
		
		return -1;
	}

	/**
	 * 重置
	 * @author zhujun
	 * @date 2015年9月24日
	 */
	public void reset() {
		currentPhase = Phase.UnStart;
		currentCmdArgCount = 0;
		currentCmdArgs = null;
		parsedArgCount = 0;
		currentCmdStartOffset = 0;
		currentCmdEndOffset = 0;
	}

}
