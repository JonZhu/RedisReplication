/**
 * 
 */
package com.gridy.tool.redisrep.cmd;

import com.gridy.tool.redisrep.replication.IDataMapping;

/**
 * @author zhujun
 * @date 2015-4-17
 *
 */
public interface IRedisCommond {

	/**
	 * eg: HSET
	 * 
	 * @return
	 */
	String getName();
	
	/**
	 * 参数列表
	 * @return
	 */
	byte[][] getArgs();
	
	/**
	 * Crud标识
	 * @return
	 * @author zhujun
	 * @date 2015-4-20
	 */
	byte getCrudFlag();

	/**
	 * 获取命令操作的对象 字段值, 如果命令中未操作该字段, 返回null
	 * 
	 * @param name
	 * @return
	 * @author zhujun
	 * @date 2015-4-20
	 */
	byte[] getFieldValue(String name, IDataMapping mapping);
	
	/**
	 * 命令开始在文件中的位置
	 */
	long getStartOffset();
	
	/**
	 * 命令结束在文件中的位置 (不包括)
	 */
	long getEndOffset();
}
