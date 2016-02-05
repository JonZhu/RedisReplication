/**
 * 
 */
package com.gridy.tool.redisrep.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhujun
 * @date 2015-4-20
 *
 */
public class SqlUtil {

	private final static Logger LOG = LoggerFactory.getLogger(SqlUtil.class);
	
	private final static SimpleDateFormat UtilDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 将值转换为sql 字符串值, 用于拼接sql
	 * 
	 * <p>
	 * eg: <br/>
	 * 字符串 abc -> 'abc' <br/>
	 * int 4 -> 4
	 * </p>
	 * 
	 * @param value
	 * @return
	 * @author zhujun
	 * @date 2015-4-20
	 */
	public static String stringValue(Object value) {
		if (value == null) {
			return "null";
		}
		
		if (value instanceof String) {
			// 字符串
			return "'" + escapeString((String)value) + "'";
		}
		
		if (value instanceof Number) {
			// 数字
			return ((Number)value).toString();
		}
		
		if (value instanceof Boolean) {
			// boolean
			return ((Boolean)value).toString();
		}
		
		if (value instanceof java.sql.Date || value instanceof Timestamp) {
			// sql日期
			return "'" + ((Object)value).toString() + "'";
		}
		
		if (value instanceof java.util.Date) {
			// util 日期
			return "'" + UtilDateFormat.format(value) + "'";
		}
		
		throw new RuntimeException("无法将类型: "+ value.getClass().getName() +" 转换为sql值");
	}

	/**
	 * @param value
	 * @return
	 * @author zhujun
	 * @date 2015-4-23
	 */
	private static String escapeString(String value) {
		return value.replace("'", "''")
				    .replace("\\", "\\\\");
	}
	
	public static void main(String[] args) {
		Object value = new java.util.Date(System.currentTimeMillis());
		System.out.println(stringValue(value));
	}

	/**
	 * 
	 * @author zhujun
	 * @date 2015-4-20
	 */
	public static void close(Connection conn, Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				LOG.error("关闭数据库Statement出错", e);
			}
		}
		
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				LOG.error("关闭数据库Connection出错", e);
			}
		}
		
	}

}
