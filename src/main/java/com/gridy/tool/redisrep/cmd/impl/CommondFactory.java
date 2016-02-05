/**
 * 
 */
package com.gridy.tool.redisrep.cmd.impl;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.gridy.tool.redisrep.cmd.IRedisCommond;

/**
 * 命令工厂
 * 
 * @author zhujun
 * @date 2015-4-19
 *
 */
public class CommondFactory {

	private final static Map<String, Class<? extends AbstractCommond>> cmdClassMap = new HashMap<>();
	static {
		// 注册命令, 名称和aof文件中一致
		cmdClassMap.put("SET", Set.class);
		
		cmdClassMap.put("HMSET", Hmset.class);
		cmdClassMap.put("HSET", Hset.class);
		
		cmdClassMap.put("ZADD", Zadd.class);
	}
	
	private final static Class byteArrayClass = new byte[][]{}.getClass();
	
	/**
	 * @param cmdArgs 命令所有参数列表
	 * @return
	 */
	public static IRedisCommond createCmd(byte[][] cmdArgs) {
		if (cmdArgs == null || cmdArgs.length == 0) {
			return null;
		}
		
		String cmdName = new String(cmdArgs[0]);
		Class<? extends AbstractCommond> cmdClass = cmdClassMap.get(cmdName.toUpperCase());
		if (cmdClass == null) {
			cmdClass = OtherCommond.class;
		}
		
		Constructor<? extends AbstractCommond> constructor = null;
		try {
			constructor = cmdClass.getConstructor(String.class, byteArrayClass);
		} catch (Exception e) {
			throw new RuntimeException("获取命令构造器出错, cmdName: " + cmdName, e);
		}
		
		IRedisCommond cmd = null;
		try {
			cmd = constructor.newInstance(cmdName, cmdArgs);
		} catch (Exception e) {
			throw new RuntimeException("构造命令出错, cmdName: " + cmdName, e);
		}
		
		return cmd;
	}

}
