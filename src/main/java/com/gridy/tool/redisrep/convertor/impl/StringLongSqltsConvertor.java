/**
 * 
 */
package com.gridy.tool.redisrep.convertor.impl;

import java.sql.Timestamp;

import com.gridy.tool.redisrep.convertor.IConvertor;

/**
 * byte[] -> string -> long -> sql timestamp
 * 
 * @author zhujun
 * @date 2015-4-21
 *
 */
public class StringLongSqltsConvertor implements IConvertor<Timestamp> {

	@Override
	public Timestamp convert(byte[] value) {
		return new Timestamp(Long.valueOf(new String(value)));
	}

}
