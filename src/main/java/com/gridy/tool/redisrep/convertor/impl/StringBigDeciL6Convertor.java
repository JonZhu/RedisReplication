/**
 * 
 */
package com.gridy.tool.redisrep.convertor.impl;

import java.math.BigDecimal;

import com.gridy.tool.redisrep.convertor.IConvertor;

/**
 * byte[] -> string -> BigDecimal -> movePointLeft(6)
 * 
 * @author zhujun
 * @date 2015-4-21
 *
 */
public class StringBigDeciL6Convertor implements IConvertor<BigDecimal> {

	@Override
	public BigDecimal convert(byte[] value) {
		return new BigDecimal(new String(value)).movePointLeft(6);
	}

}
