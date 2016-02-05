/**
 * 
 */
package com.gridy.tool.redisrep.cmd.impl;

import com.gridy.tool.redisrep.cmd.IRedisCommond;

/**
 * @author zhujun
 * @date 2015-4-19
 *
 */
public abstract class AbstractCommond implements IRedisCommond {

	private String name;
	
	private byte[][] args;
	
	protected byte crudFlag;
	
	private long startOffset;
	private long endOffset;
	
	public AbstractCommond(String name, byte[][] args) {
		this.name = name;
		this.args = args;
		
		crudFlag = createCrudFlag();
	}
	
	/**
	 * 创建Crud flag
	 * 
	 * @return
	 * @author zhujun
	 * @date 2015-4-22
	 */
	abstract protected byte createCrudFlag();

	/**
	 * 名称
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * 命令参数, 包括名称
	 */
	@Override
	public byte[][] getArgs() {
		return args;
	}
	
	@Override
	public byte getCrudFlag() {
		return crudFlag;
	}

	public long getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(long startOffset) {
		this.startOffset = startOffset;
	}

	public long getEndOffset() {
		return endOffset;
	}

	public void setEndOffset(long endOffset) {
		this.endOffset = endOffset;
	}

}
