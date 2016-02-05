/**
 * 
 */
package com.gridy.tool.redisrep.replication.impl;

import com.gridy.tool.redisrep.convertor.IConvertor;
import com.gridy.tool.redisrep.replication.IDataMappingField;

/**
 * @author zhujun
 * @date 2015-4-21
 *
 */
public class DataMappingFieldImpl implements IDataMappingField {

	private boolean isId;
	
	private String name;
	
	private String to;
	
	private IConvertor convertor;

	public boolean isId() {
		return isId;
	}
	
	public void setIsId(boolean value) {
		this.isId = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public IConvertor getConvertor() {
		return convertor;
	}

	public void setConvertor(IConvertor convertor) {
		this.convertor = convertor;
	}
	
	
	
}
