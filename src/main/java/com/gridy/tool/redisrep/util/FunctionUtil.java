/**
 * 
 */
package com.gridy.tool.redisrep.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gridy.tool.redisrep.cmd.IRedisCommond;
import com.gridy.tool.redisrep.cmd.impl.KeyCommond;
import com.gridy.tool.redisrep.replication.IDataMapping;

/**
 * 命令中支持的函数 工具
 * 
 * <p>如: $key(1)</p>
 * 
 * @author zhujun
 * @date 2015-5-6
 *
 */
public class FunctionUtil {

	private final static Logger LOG = LoggerFactory.getLogger(FunctionUtil.class);
	
	// $key(\d+)
	private final static Pattern keyFuncPattern = Pattern.compile("^\\$key\\((\\d+)\\)$");
	
	
	/**
	 * 计算函数值
	 * @param fun 函数内容
	 * @param cmd 当前执行的命令
	 * @param mapping 当前数据映射规则
	 * @return
	 * @author zhujun
	 * @date 2015-5-6
	 */
	public static String eval(String fun, IRedisCommond cmd, IDataMapping mapping) {
		
		if (keyFuncPattern.matcher(fun).matches()) {
			return evalKeyFun(fun, cmd, mapping);
		}
		
		throw new RuntimeException("不支持的函数: " + fun);
	}


	/**
	 * key函数
	 * 
	 * @param fun
	 * @param cmd
	 * @param mapping
	 * @return
	 * @author zhujun
	 * @date 2015-5-6
	 */
	private static String evalKeyFun(String fun, IRedisCommond cmd, IDataMapping mapping) {
		if (!(cmd instanceof KeyCommond)) {
			throw new RuntimeException("key函数只支持 KeyCommond");
		}
		KeyCommond kCmd = (KeyCommond)cmd;
		
		// 获取key
		Matcher funMatcher = keyFuncPattern.matcher(fun);
		funMatcher.matches();
		
		Integer groupId = Integer.valueOf(funMatcher.group(1));
		LOG.debug("key function arg: {}", groupId);
		
		
		Matcher cmdKeyMatcher = Pattern.compile(mapping.getKey()).matcher(kCmd.getKey());
		if (!cmdKeyMatcher.matches()) {
			throw new RuntimeException("key函数 "+fun+" 不匹配key: " + kCmd.getKey());
		}
		
		return cmdKeyMatcher.group(groupId);
	}
	
}
