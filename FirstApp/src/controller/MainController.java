package controller;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.util.StatusPrinter;

public class MainController {
	public static void setLogger() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

		RollingFileAppender rfAppender = new RollingFileAppender();
		rfAppender.setContext(loggerContext);
		rfAppender.setFile(System.getProperty("log_file_loc") + "/log/" + "rapid" + ".log");
		FixedWindowRollingPolicy rollingPolicy = new FixedWindowRollingPolicy();
		rollingPolicy.setContext(loggerContext);
		// rolling policies need to know their parent
		// it's one of the rare cases, where a sub-component knows about its
		// parent
		rollingPolicy.setParent(rfAppender);
		rollingPolicy.setFileNamePattern(System.getProperty("log_file_loc") + "/log/" + "falcon.%i.log.zip");
		rollingPolicy.start();

		SizeBasedTriggeringPolicy triggeringPolicy = new ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy();
		triggeringPolicy.setMaxFileSize("5MB");
		triggeringPolicy.start();

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(loggerContext);
		encoder.setPattern("%d{yyyy-MM-dd_HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
		encoder.start();

		rfAppender.setEncoder(encoder);
		rfAppender.setRollingPolicy(rollingPolicy);
		rfAppender.setTriggeringPolicy(triggeringPolicy);

		rfAppender.start();

		Logger hibernateType = loggerContext.getLogger("org.hibernate.type");
		hibernateType.setLevel(Level.ERROR);
		hibernateType.addAppender(rfAppender);

		Logger hibernateAll = loggerContext.getLogger("org.hibernate");
		hibernateAll.setLevel(Level.ERROR);
		hibernateAll.addAppender(rfAppender);

		// attach the rolling file appender to the logger of your choice
		Logger logbackLogger = loggerContext.getLogger("MainController");
		logbackLogger.addAppender(rfAppender);

		// OPTIONAL: print logback internal status messages
		StatusPrinter.print(loggerContext);

		// log something
		logbackLogger.debug("hello");
	}
}
