/**
 * 
 */
package com.gridy.tool.redisrep.cmd.impl;

import com.gridy.tool.redisrep.replication.IDataMapping;
import com.gridy.tool.redisrep.util.CrudFlagUtil;

/**
 * 无特殊实现的命令
 * 
 * @author zhujun
 * @date 2015-4-19
 *
 */
public class OtherCommond extends AbstractCommond {

	/**
	 * @param name
	 * @param args
	 */
	public OtherCommond(String name, byte[][] args) {
		super(name, args);
	}

	@Override
	public byte[] getFieldValue(String name, IDataMapping mapping) {
		return null;
	}
	
	@Override
	protected byte createCrudFlag() {
		return CrudFlagUtil.createFlag(false, false, false, false);
	}

}
