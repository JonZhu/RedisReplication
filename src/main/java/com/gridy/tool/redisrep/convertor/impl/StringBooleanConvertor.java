/**
 * 
 */
package com.gridy.tool.redisrep.convertor.impl;

import com.gridy.tool.redisrep.convertor.IConvertor;

/**
 * byte[] -> string -> boolean
 * 
 * @author zhujun
 * @date 2015-4-21
 *
 */
public class StringBooleanConvertor implements IConvertor<Boolean> {

	@Override
	public Boolean convert(byte[] value) {
		return "1".equals(new String(value));
	}

}
