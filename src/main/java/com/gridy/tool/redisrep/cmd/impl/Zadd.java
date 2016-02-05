/**
 * 
 */
package com.gridy.tool.redisrep.cmd.impl;

import com.gridy.tool.redisrep.cmd.IRedisDataType;
import com.gridy.tool.redisrep.replication.IDataMapping;
import com.gridy.tool.redisrep.util.CrudFlagUtil;

/**
 * @author zhujun
 * @date 2015-4-21
 *
 */
public class Zadd extends KeyCommond {

	/**
	 * @param name
	 * @param args
	 */
	public Zadd(String name, byte[][] args) {
		super(name, args);
	}

	@Override
	public int getDataType() {
		return IRedisDataType.SortedSet;
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
		return CrudFlagUtil.createFlag(true, false, false, false);
	}

}
