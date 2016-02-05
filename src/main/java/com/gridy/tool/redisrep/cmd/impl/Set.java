/**
 * 
 */
package com.gridy.tool.redisrep.cmd.impl;

import java.security.InvalidParameterException;

import com.gridy.tool.redisrep.cmd.IRedisDataType;
import com.gridy.tool.redisrep.replication.IDataMapping;
import com.gridy.tool.redisrep.util.CrudFlagUtil;

/**
 * Set命令
 * 
 * @author zhujun
 * @date 2015-4-19
 *
 */
public class Set extends KeyCommond {

	private byte[] value;
	
	/**
	 * @param name
	 * @param args
	 */
	public Set(String name, byte[][] args) {
		super(name, args);
		
		if (args.length < 3) {
			throw new InvalidParameterException("args数量不能小于3");
		}
		
		value = args[2];
		
		crudFlag = CrudFlagUtil.createFlag(true, false, true, false);
	}

	public byte[] getValue() {
		return value;
	}

	@Override
	public int getDataType() {
		return IRedisDataType.Set;
	}

	@Override
	public boolean containField(String name) {
		return false;
	}

	@Override
	public byte[] getFieldValue(String name, IDataMapping mapping) {
		return null;
	}

	@Override
	protected byte createCrudFlag() {
		return CrudFlagUtil.createFlag(true, false, true, false);
	}

	
}
