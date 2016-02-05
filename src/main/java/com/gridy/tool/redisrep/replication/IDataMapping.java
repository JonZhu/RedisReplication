/**
 * 
 */
package com.gridy.tool.redisrep.replication;

import java.util.List;

import com.gridy.tool.redisrep.cmd.IRedisCommond;

/**
 * 数据映射 规则
 * 
 * @author zhujun
 * @date 2015-4-19
 *
 */
public interface IDataMapping {

	/**
	 * redis数据类型
	 * @return
	 */
	int getDataType();
	
	/**
	 * 匹配redis数据库 key, 为正则表达式
	 * @return
	 */
	String getKey();
	
	/**
	 * 目标表名
	 * @return
	 */
	String getToTable();

	/**
	 * 是否为指定命令的映射规则
	 * @param cmd
	 * @return
	 */
	boolean match(IRedisCommond cmd);
	
	/**
	 * 获取字段列表
	 * @return
	 * @author zhujun
	 * @date 2015-4-20
	 */
	List<IDataMappingField> getFieldList();
	
}
