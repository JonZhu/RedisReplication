/**
 * 
 */
package com.gridy.tool.redisrep.convertor.impl;

import java.sql.Date;

import com.gridy.tool.redisrep.convertor.IConvertor;

/**
 * byte[] -> string -> long -> sqldate
 * 
 * @author zhujun
 * @date 2015-4-21
 *
 */
public class StringLongSqldateConvertor implements IConvertor<Date> {

	@Override
	public Date convert(byte[] value) {
		return new Date(Long.valueOf(new String(value)));
	}

}
