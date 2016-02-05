/**
 * 
 */
package com.gridy.tool.redisrep.cmd.impl;

import java.security.InvalidParameterException;

/**
 * 对key值操作的命令
 * 
 * @author zhujun
 * @date 2015-4-19
 *
 */
public abstract class KeyCommond extends AbstractCommond {

	private String key;
	
	/**
	 * @param name
	 * @param args
	 */
	public KeyCommond(String name, byte[][] args) {
		super(name, args);
		if (args.length < 2) {
			throw new InvalidParameterException("args数量不能小于2");
		}
		
		key = new String(args[1]);
	}
	
	public String getKey() {
		return key;
	}
	
	/**
	 * 命令操作的数据类型
	 * @return
	 */
	abstract public int getDataType();

	/**
	 * 命令是否包含字段, 如果包含, 表示会对该字段
	 * @param name
	 * @return
	 * @author zhujun
	 * @date 2015-4-22
	 */
	abstract public boolean containField(String name);

}
