/**
 * 
 */
package com.gridy.tool.redisrep.convertor;

import java.util.HashMap;
import java.util.Map;

import com.gridy.tool.redisrep.convertor.impl.StringBigDeciConvertor;
import com.gridy.tool.redisrep.convertor.impl.StringBigDeciL6Convertor;
import com.gridy.tool.redisrep.convertor.impl.StringBooleanConvertor;
import com.gridy.tool.redisrep.convertor.impl.StringByteConvertor;
import com.gridy.tool.redisrep.convertor.impl.StringConvertor;
import com.gridy.tool.redisrep.convertor.impl.StringIntConvertor;
import com.gridy.tool.redisrep.convertor.impl.StringLongConvertor;
import com.gridy.tool.redisrep.convertor.impl.StringLongSqldateConvertor;
import com.gridy.tool.redisrep.convertor.impl.StringLongSqltsConvertor;
import com.gridy.tool.redisrep.convertor.impl.StringShortConvertor;

/**
 * 转换器工厂
 * 
 * @author zhujun
 * @date 2015-4-21
 *
 */
public class ConvertorFactory {

	private final static Map<String, IConvertor> convertorMap = new HashMap<>();
	
	static {
		// 初始化convertor
		convertorMap.put("string", new StringConvertor());
		convertorMap.put("stringBoolean", new StringBooleanConvertor());
		convertorMap.put("stringByte", new StringByteConvertor());
		convertorMap.put("stringShort", new StringShortConvertor());
		convertorMap.put("stringInt", new StringIntConvertor());
		
		convertorMap.put("stringLong", new StringLongConvertor());
		convertorMap.put("stringLongSqldate", new StringLongSqldateConvertor());
		convertorMap.put("stringLongSqlts", new StringLongSqltsConvertor());
		
		convertorMap.put("stringBigdeci", new StringBigDeciConvertor());
		convertorMap.put("stringBigdeciL6", new StringBigDeciL6Convertor());
		
	}
	
	public static IConvertor getConvertor(String name) {
		return convertorMap.get(name);
	}
	
}
