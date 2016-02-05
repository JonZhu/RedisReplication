/**
 * 
 */
package com.gridy.tool.redisrep.util;

import com.gridy.tool.redisrep.cmd.IRedisDataType;

/**
 * @author zhujun
 * @date 2015-4-22
 *
 */
public class RedisDataTypeUtil {

	/**
	 * 根据名称获取数据类型, 不支持返回null
	 * @param name
	 * @return
	 * @author zhujun
	 * @date 2015-4-22
	 */
	public static Integer getType(String name) {
		if ("String".equalsIgnoreCase(name)) {
			return IRedisDataType.String;
		}
		
		if ("Hash".equalsIgnoreCase(name)) {
			return IRedisDataType.Hash;
		}
		
		if ("Set".equalsIgnoreCase(name)) {
			return IRedisDataType.Set;
		}
		
		if ("SortedSet".equalsIgnoreCase(name)) {
			return IRedisDataType.SortedSet;
		}
		
		if ("List".equalsIgnoreCase(name)) {
			return IRedisDataType.List;
		}
		
		return null;
	}
	
}
