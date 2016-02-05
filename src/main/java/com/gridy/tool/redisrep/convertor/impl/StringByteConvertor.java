/**
 * 
 */
package com.gridy.tool.redisrep.convertor.impl;

import com.gridy.tool.redisrep.convertor.IConvertor;

/**
 * byte[] -> string -> byte
 * 
 * @author zhujun
 * @date 2015-4-21
 *
 */
public class StringByteConvertor implements IConvertor<Byte> {

	@Override
	public Byte convert(byte[] value) {
		return Byte.valueOf(new String(value));
	}

}
