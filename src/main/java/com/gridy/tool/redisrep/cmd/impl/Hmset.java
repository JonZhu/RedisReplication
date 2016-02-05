/**
 * 
 */
package com.gridy.tool.redisrep.cmd.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gridy.tool.redisrep.cmd.IRedisDataType;
import com.gridy.tool.redisrep.replication.IDataMapping;
import com.gridy.tool.redisrep.util.CrudFlagUtil;
import com.gridy.tool.redisrep.util.FunctionUtil;

/**
 * @author zhujun
 * @date 2015-4-21
 *
 */
public class Hmset extends KeyCommond {

	private final static Logger LOG = LoggerFactory.getLogger(Hmset.class);
	
	private final Map<String, byte[]> fieldMap = new HashMap<>();
	
	/**
	 * @param name
	 * @param args
	 */
	public Hmset(String name, byte[][] args) {
		super(name, args);
		
		for (int i = 2; i < args.length - 1; i+=2) {
			fieldMap.put(new String(args[i]), args[i+1]);
		}
	}

	@Override
	public int getDataType() {
		return IRedisDataType.Hash;
	}

	@Override
	public boolean containField(String name) {
		return fieldMap.containsKey(name);
	}

	@Override
	public byte[] getFieldValue(String name, IDataMapping mapping) {
		if (name.startsWith("$")) {
			String funValue = FunctionUtil.eval(name, this, mapping);
			return funValue == null ? null : funValue.getBytes();
		}
		
		return fieldMap.get(name);
	}


	@Override
	protected byte createCrudFlag() {
		return CrudFlagUtil.createFlag(true, false, true, false);
	}

}
