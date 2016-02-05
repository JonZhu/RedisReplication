/**
 * 
 */
package com.gridy.tool.redisrep.config.impl;

import javax.sql.DataSource;

import com.gridy.tool.redisrep.config.IRdb;

/**
 * @author zhujun
 * @date 2015-4-20
 *
 */
public class RdbImpl implements IRdb {

	private String id;
	
	private String url;
	
	private String driver;
	
	private String user;
	
	private String pass;
	
	private DataSource dataSource;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
}
