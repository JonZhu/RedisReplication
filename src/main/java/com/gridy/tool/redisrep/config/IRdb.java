/**
 * 
 */
package com.gridy.tool.redisrep.config;

import javax.sql.DataSource;

/**
 * @author zhujun
 * @date 2015-4-17
 *
 */
public interface IRdb {

	String getId();
	String getUrl();
	
	String getDriver();
	String getUser();
	String getPass();
	
	DataSource getDataSource();
}
