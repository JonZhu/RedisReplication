/**
 * 
 */
package com.gridy.tool.redisrep;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gridy.tool.redisrep.config.IConfig;
import com.gridy.tool.redisrep.config.IRedis;
import com.gridy.tool.redisrep.config.impl.XmlConfigImpl;
import com.gridy.tool.redisrep.watcher.IAofFileWatcher;
import com.gridy.tool.redisrep.watcher.impl.AofFileWatcherImpl;

/**
 * 程序启动入口
 * 
 * @author zhujun
 * @date 2015-4-17
 *
 */
public class Startup {

	private final static Logger LOG = LoggerFactory.getLogger(Startup.class);
	
	/**
	 * @param args
	 * @author zhujun
	 * @date 2015-4-17
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			LOG.warn("Usage: Startup configFile");
			System.exit(1);
		}
		
		File configFile = new File(args[0]);
		if (!configFile.isFile()) {
			LOG.error("Config file is not exist");
			System.exit(1);
		}
		
		// Config
		IConfig config = new XmlConfigImpl();
		config.load(configFile);
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(config));

		// Start rep
		try {
			List<IRedis> redisList = config.getRedisList();
			for (IRedis redis : redisList) {
				if (redis.getReplicationList() != null && !redis.getReplicationList().isEmpty()) {
					IAofFileWatcher watcher = new AofFileWatcherImpl();
					watcher.addReplication(redis.getReplicationList());
					new AofFileWatchThread(watcher, redis).start();
				}
			}
		} catch (Exception e) {
			LOG.error("Startup error", e);
		}
		
		// 开始保存配置线程
		startSaveConfigTask(config);
		
	}
	
	
	/**
	 * 开启保存配置任务
	 * 
	 * @param config
	 * @author zhujun
	 * @date 2015-4-23
	 */
	private static void startSaveConfigTask(final IConfig config) {
		Timer timer = new Timer("SaveConfig", true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				config.save();
			}
		};
		timer.schedule(task, 1000, 10000);
	}


	private static class AofFileWatchThread extends Thread {
		private IAofFileWatcher watcher;
		private IRedis redis;
		
		public AofFileWatchThread(IAofFileWatcher watcher, IRedis redis) {
			super("Watcher-" + redis.getId());
			
			this.watcher = watcher;
			this.redis = redis;
		}
		
		@Override
		public void run() {
			try {
				watcher.watch(redis);
			} catch (IOException e) {
				LOG.error("Aof监视出错", e);
			}
		}
	}
	
	
	private static class ShutdownHook extends Thread {
		
		private IConfig config;
		
		public ShutdownHook(IConfig config) {
			this.config = config;
		}
		
		@Override
		public void run() {
			LOG.info("Shutdown...");
			LOG.info("Save config");
			config.save();
		}
		
	}

}
