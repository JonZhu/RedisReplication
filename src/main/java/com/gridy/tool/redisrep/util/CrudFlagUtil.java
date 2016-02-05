/**
 * 
 */
package com.gridy.tool.redisrep.util;

/**
 * @author zhujun
 * @date 2015-4-20
 *
 */
public class CrudFlagUtil {

	public static byte createFlag(boolean create, boolean read, boolean update, boolean delete) {
		byte flag = 0;
		if (create) {
			flag = (byte)(flag | (1 << 3));
		}
		
		if (update) {
			flag = (byte)(flag | (1 << 2));
		}
		
		if (delete) {
			flag = (byte)(flag | (1 << 1));
		}
		
		if (read) {
			flag = (byte)(flag | 1);
		}
		
		return flag;
	}
	
	
	public static boolean isCreate(byte flag) {
		return ((flag >> 3) & 1) == 1;
	}
	
	public static boolean isRead(byte flag) {
		return (flag & 1) == 1;
	}
	
	public static boolean isUpdate(byte flag) {
		return ((flag >> 2) & 1) == 1;
	}
	
	public static boolean isDelete(byte flag) {
		return ((flag >> 1) & 1) == 1;
	}
	
	/**
	 * 是否导致数据改变
	 * @param flag
	 * @return
	 * @author zhujun
	 * @date 2015-4-22
	 */
	public static boolean isChange(byte flag) {
		return isCreate(flag) || isUpdate(flag) || isDelete(flag);
	}
	
}
