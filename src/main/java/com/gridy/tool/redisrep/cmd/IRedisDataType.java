/**
 * 
 */
package com.gridy.tool.redisrep.cmd;

/**
 * redis数据对象类型
 * 
 * @author zhujun
 * @date 2015-4-21
 *
 */
public interface IRedisDataType {

	/**
	 * 字符串是一种最基本的Redis值类型
	 */
	int String = 1;
	
	/**
	 * Redis列表是简单的字符串列表，按照插入顺序排序
	 */
	int List = 2;
	
	/**
	 * Redis集合是一个无序的字符串合集
	 */
	int Set = 3;
	
	/**
	 * Redis Hashes是字符串字段和字符串值之间的映射
	 */
	int Hash = 4;
	
	/**
	 * 有序集合
	 */
	int SortedSet = 5;
	
}
