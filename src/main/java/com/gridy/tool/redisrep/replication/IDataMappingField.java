/**
 * 
 */
package com.gridy.tool.redisrep.replication;

import com.gridy.tool.redisrep.convertor.IConvertor;

/**
 * 数据映射 字段
 * 
 * @author zhujun
 * @date 2015-4-20
 *
 */
public interface IDataMappingField {

	/**
	 * 该字段是否为id标识
	 * @return
	 * @author zhujun
	 * @date 2015-4-20
	 */
	boolean isId();
	
	/**
	 * 字段名称
	 * @return
	 * @author zhujun
	 * @date 2015-4-20
	 */
	String getName();
	
	/**
	 * 映射目标
	 * @return
	 * @author zhujun
	 * @date 2015-4-20
	 */
	String getTo();
	
	/**
	 * 字段 转换器
	 * @return
	 * @author zhujun
	 * @date 2015-4-20
	 */
	IConvertor getConvertor();
}
