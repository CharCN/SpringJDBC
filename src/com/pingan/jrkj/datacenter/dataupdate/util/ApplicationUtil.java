package com.pingan.jrkj.datacenter.dataupdate.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

import com.pingan.jrkj.datacenter.dataload.Test;

public class ApplicationUtil {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationUtil.class);

	private static ApplicationContext ctx = null;

	public static void init() {
		if (ctx != null) {
			return;
		}
		try {
			ApplicationUtil.ctx = new FileSystemXmlApplicationContext("resource/applicationContext.xml");
//			Properties sysConfig = (Properties) ApplicationUtil.ctx.getBean("sysConfig");
			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
			loggerContext.reset();
			JoranConfigurator joranConfigurator = new JoranConfigurator();
			joranConfigurator.setContext(loggerContext);
			joranConfigurator.doConfigure("resource/logback.xml");
			joranConfigurator.registerSafeConfiguration();
			logger.info("Init Application SUCCESS 加载Spring应用成功");
		} catch (Exception e) {
			logger.error("Init Application ERROR[{}] E[{}]", e.getMessage(), e);
		}
	}

	public static void setCtx(ApplicationContext ctx) {
		ApplicationUtil.ctx = ctx;
	}

	public static Object getBean(String beanName) {
		return ctx.getBean(beanName);
	}

	public static void main(String[] args) {
		System.out.println(new Test().getClass().getResource("/").getPath());
	}

}
