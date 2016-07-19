package com.weibo.runfail;

import java.util.List;
import java.util.Map;
/*
 * @author hugang
 */
public class FailCasesContext {
	FailCases fc = null;

	// 简单工厂模式和策略模式， 根据type声明，实例化不同实例
	public FailCasesContext(String type) {
		// 为了支持JDK6 ， case后用数字;  JDK7可以直接String类型
		// 默认为2， ant
		int typeNum =2;
		if("MVN".equals(type)){
			typeNum = 1;
		}else if("ANT".equals(type)){
			typeNum = 2;
		}
		switch (typeNum) {
		case 1:
//			FailCasesMvn fcm = new FailCasesMvn();
			// 重构匹配mvn test
			NewFailCasesMvn fcm = new NewFailCasesMvn();
			fc = fcm;
			break;
		case 2:
			FailCasesAnt fca = new FailCasesAnt();
			fc = fca;
			break;	
		}
	}
	
	public Map<Class<?>,List<String>> getFailCases(String logPath) {
		return fc.findFailedCases(logPath);
	}
	
}
