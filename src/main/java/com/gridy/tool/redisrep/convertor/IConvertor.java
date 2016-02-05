/**
 * 
 */
package com.gridy.tool.redisrep.convertor;

/**
 * 数据转换器
 * 
 * <p>将redis byte[]数据转换成其它类型数据</p>
 * 
 * @author zhujun
 * @date 2015-4-17
 *
 */
public interface IConvertor<T> {

	/**
	 * 转换数据
	 * 
	 * @param value
	 * @return
	 * @author zhujun
	 * @date 2015-4-20
	 */
	T convert(byte[] value); 

}
