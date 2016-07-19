package com.weibo.runfail;

import java.io.IOException;
/*
 * @author hugang
 */
public class WriteLogFactory {
	public void writeLog(String logPath, String logType, String resultPath)
			throws IOException {
		// 为了支持JDK6 ， case后用数字; JDK7可以直接String类型
		int typeNum = 2;
		if ("MVN".equals(logType)) {
			typeNum = 1;
		} else if ("ANT".equals(logType)) {
			typeNum = 2;
		}

		switch (typeNum) {
		case 1:
//			new WriteLogMvn().writeLogMvn(logPath, resultPath);
			new NewWriteLogMvn().writeLogMvn(logPath, resultPath);
			break;
		case 2:
			new WriteLogAnt().writeLogAnt(logPath, resultPath);
		}
	}
}
