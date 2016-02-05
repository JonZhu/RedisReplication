/**
 * 
 */
package com.gridy.tool.redisrep.replication.impl;

import java.util.List;
import java.util.regex.Pattern;

import com.gridy.tool.redisrep.cmd.IRedisCommond;
import com.gridy.tool.redisrep.cmd.impl.KeyCommond;
import com.gridy.tool.redisrep.replication.IDataMapping;
import com.gridy.tool.redisrep.replication.IDataMappingField;
import com.gridy.tool.redisrep.util.CrudFlagUtil;

/**
 * 数据映射 规则
 * 
 * @author zhujun
 * @date 2015-4-21
 */
public class DataMappingImpl implements IDataMapping {

	/**
	 * 数据类型
	 */
	private int dataType;
	
	/**
	 * redis 对象 key
	 */
	private String key;
	
	/**
	 * 目标 表名
	 */
	private String toTable;
	
	/**
	 * 映射字段 列表
	 */
	private List<IDataMappingField> fieldList;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getToTable() {
		return toTable;
	}

	public void setToTable(String toTable) {
		this.toTable = toTable;
	}

	public List<IDataMappingField> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<IDataMappingField> fieldList) {
		this.fieldList = fieldList;
	}
	
	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	@Override
	public boolean match(IRedisCommond cmd) {
		if (!(cmd instanceof KeyCommond)) {
			// 不对key进行操作
			return false;
		}
		
		KeyCommond kCmd = (KeyCommond)cmd;
		if (kCmd.getDataType() != getDataType()) {
			// 操作的数据类型不同
			return false;
		}
		
		if (!CrudFlagUtil.isChange(kCmd.getCrudFlag())) {
			// 命令不会改变数据
			return false;
		}
		
		if (!Pattern.matches(getKey(), kCmd.getKey())) {
			// 规则key不匹配命令
			return false;
		}
		
		if (CrudFlagUtil.isDelete(kCmd.getCrudFlag())) {
			// 删除操作, 以上条件, 已经匹配
			return true;
		}
		
		// 更新操作需要判断命令是否操作该映射中的字段
		for (IDataMappingField field : getFieldList()) {
			if (!field.isId() && kCmd.containField(field.getName())) {
				return true;
			}
		}
		
		return false;
	}
	
}
