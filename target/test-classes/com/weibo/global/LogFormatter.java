package com.weibo.global;

//import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
	@Override
	public String format(LogRecord record) {
		//	Date date = new Date();
		//	String sDate = date.toString();
		// return "[" + sDate + "]" + "[" + record.getLevel() + "]"
		// + record.getClass() + record.getMessage() + "\n";
		return "[" + record.getMessage() + "]" + "\n";
	}
}
