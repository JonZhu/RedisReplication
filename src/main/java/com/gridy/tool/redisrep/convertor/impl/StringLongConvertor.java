/**
 * 
 */
package com.gridy.tool.redisrep.convertor.impl;

import com.gridy.tool.redisrep.convertor.IConvertor;

/**
 * byte[] -> string -> long
 * 
 * @author zhujun
 * @date 2015-7-29
 *
 */
public class StringLongConvertor implements IConvertor<Long> {

	@Override
	public Long convert(byte[] value) {
		return Long.valueOf(new String(value));
	}

}
