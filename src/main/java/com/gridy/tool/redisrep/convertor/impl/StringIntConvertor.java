/**
 * 
 */
package com.gridy.tool.redisrep.convertor.impl;

import com.gridy.tool.redisrep.convertor.IConvertor;

/**
 * byte[] -> string -> int
 * 
 * @author zhujun
 * @date 2015-4-21
 *
 */
public class StringIntConvertor implements IConvertor<Integer> {

	@Override
	public Integer convert(byte[] value) {
		return Integer.valueOf(new String(value));
	}

}
