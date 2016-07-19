package com.weibo.runfail;
/*
 * @author hugang
 */
public class RealClassMvn {
	String[] packageName = {
		"com.weibo.cases.hugang", "com.weibo.cases.lingna", "com.weibo.cases.maincase",
		"com.weibo.cases.qiaoli", "com.weibo.cases.wanglei16", "com.weibo.cases.xiaoyu", 
		"com.weibo.cases.xuelian", "com.weibo.cases.beibei12", "com.weibo.cases.publicservice",
		"com.weibo.cases.yanlei3", "com.weibo.cases.guanglu"
	};
	
	int now = 0;
	int retryNum = packageName.length;
	
	String realClassName;
	
	
	public String getRealClassName() {
		return realClassName;
	}


	public void setRealClassName(String realClassName) {
		this.realClassName = realClassName;
	}

	// 由于， mvn执行结果中失败的用例只返回类名(ActivitiesTimelineSpActivitiesTest)，
	// 而不是完全类名
	// （包括包名，e.g.com.weibo.cases.xuelian.ActivitiesTimelineSpActivitiesTest）
	// 导致Class.forName(类名)抛异常
	// 使用递归加上不同包名，进行判断，找到正确完全类名
	public void findClass(String className) throws Throwable{
		try{
			realClassName = packageName[now++] + "." + className;
			Class.forName(realClassName);
			setRealClassName(realClassName);
		}catch(ClassNotFoundException e){
			if(now < retryNum){
				findClass(className);
			}else{
				throw e;
			}	
		}
	}

}
