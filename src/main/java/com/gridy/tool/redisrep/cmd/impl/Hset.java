/**
 * 
 */
package com.gridy.tool.redisrep.cmd.impl;

import java.security.InvalidParameterException;

import com.gridy.tool.redisrep.cmd.IRedisDataType;
import com.gridy.tool.redisrep.replication.IDataMapping;
import com.gridy.tool.redisrep.util.CrudFlagUtil;
import com.gridy.tool.redisrep.util.FunctionUtil;

/**
 * Hset命令
 * 
 * @author zhujun
 * @date 2015-5-6
 *
 */
public class Hset extends KeyCommond {

	private String field;
	private byte[] value;
	
	/**
	 * @param name
	 * @param args
	 */
	public Hset(String name, byte[][] args) {
		super(name, args);
		
		if (args.length < 4) {
			throw new InvalidParameterException("args数量不能小于4");
		}
		
		field = new String(args[2]);
		value = args[3];
		
		crudFlag = CrudFlagUtil.createFlag(true, false, true, false);
	}

	public String getField() {
		return field;
	}
	
	public byte[] getValue() {
		return value;
	}

	@Override
	public int getDataType() {
		return IRedisDataType.Hash;
	}

	@Override
	public boolean containField(String name) {
		return field.equals(name);
	}

	@Override
	public byte[] getFieldValue(String name, IDataMapping mapping) {
		if (name.startsWith("$")) {
			String funValue = FunctionUtil.eval(name, this, mapping);
			return funValue == null ? null : funValue.getBytes();
		}
		
		return field.equals(name) ? value : null;
	}

	@Override
	protected byte createCrudFlag() {
		return CrudFlagUtil.createFlag(true, false, true, false);
	}

	
}
