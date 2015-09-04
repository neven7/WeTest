package com.weibo.global;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;

/*
 * 记录 测试步骤
 * @author hugang
 */

public class TestLog {

		public final static String PATH = "./logs/caseinfo.log";
		public final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		
		
		public static void Comment(String comment) {		
			try {	
				LOGGER.setLevel(Level.ALL);
				FileHandler fileHandler;
				fileHandler = new FileHandler(PATH);
				fileHandler.setLevel(Level.ALL);
				fileHandler.setFormatter(new LogFormatter());
				LOGGER.addHandler(fileHandler);
				LOGGER.info(comment);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
}


//public class TestLog {
//
//	
//	public final static Logger LOGGER = Logger.getLogger(TestLog.class);
//	public final static String LOG4JPATH = ".../../../log4j.properties";
//	
//	public static void Comment(String comment) {		
//		try {	
//			PropertyConfigurator.configure(LOG4JPATH);
//			LOGGER.info(comment);
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//}




