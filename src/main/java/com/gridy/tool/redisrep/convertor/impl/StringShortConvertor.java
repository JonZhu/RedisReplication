/**
 * 
 */
package com.gridy.tool.redisrep.convertor.impl;

import com.gridy.tool.redisrep.convertor.IConvertor;

/**
 * byte[] -> string -> short
 * 
 * @author zhujun
 * @date 2015-4-21
 *
 */
public class StringShortConvertor implements IConvertor<Short> {

	@Override
	public Short convert(byte[] value) {
		return Short.valueOf(new String(value));
	}

}
