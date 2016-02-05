/**
 * 
 */
package com.gridy.tool.redisrep.util;

/**
 * @author zhujun
 * @date 2015-4-19
 *
 */
public class ThreadUtil {

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
