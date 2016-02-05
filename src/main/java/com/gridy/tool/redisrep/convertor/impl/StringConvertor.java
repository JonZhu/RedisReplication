/**
 * 
 */
package com.gridy.tool.redisrep.convertor.impl;

import com.gridy.tool.redisrep.convertor.IConvertor;

/**
 * byte[] -> string
 * 
 * @author zhujun
 * @date 2015-4-21
 *
 */
public class StringConvertor implements IConvertor<String> {

	@Override
	public String convert(byte[] value) {
		return new String(value);
	}

}
