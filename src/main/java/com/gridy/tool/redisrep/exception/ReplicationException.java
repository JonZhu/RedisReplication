/**
 * 
 */
package com.gridy.tool.redisrep.exception;

/**
 * @author zhujun
 * @date 2015-4-20
 *
 */
public class ReplicationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3290355540488631399L;

	/**
	 * 
	 */
	public ReplicationException() {
	}

	/**
	 * @param message
	 */
	public ReplicationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ReplicationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ReplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ReplicationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
