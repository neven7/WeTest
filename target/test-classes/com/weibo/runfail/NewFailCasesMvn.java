package com.weibo.runfail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;


/*
 * <pre>
 * 正则匹配出失败用例，maven-surefire-customresult自定义输出格式
 * 
 * CustomResult Fail@后面
 * 
 * CustomResult Error@后面
 * 
 * 1) 后面带（），com.weibo.cases.wanglei16.PublicMentionsStatusTest.testFilterType(com.weibo.cases.wanglei16.PublicMentionsStatusTest)
 * 2）后面不带（），com.weibo.cases.maincase.XiaoyuGroupStatusLikeBVTTest.com.weibo.cases.maincase.XiaoyuGroupStatusLikeBVTTest
 * 
 * 
 * </pre>
 * 
 * 
 * @author hugang
 */
public class NewFailCasesMvn extends FailCases {

	// 找出所有失败用例 每行字符串信息
	public Set<String> failCasesStr(String logPath){
		String failRegex = "^CustomResult Fail@(.*)";
		Pattern failPattern = Pattern.compile(failRegex);
		Matcher matchFail;
		
		String errorRegex = "^CustomResult Error@(.*)";
		Pattern errorPattern = Pattern.compile(errorRegex);
		Matcher matchError;
		
		// 去掉重复的
		Set<String> failSet = new HashSet<String>();
		
		try (BufferedReader bre = new BufferedReader(new FileReader(new File(logPath)));){
			while (bre.ready()) {
				String str = bre.readLine();
				matchFail = failPattern.matcher(str);
				matchError = errorPattern.matcher(str);
				if(matchFail.matches() || matchError.matches()){
					failSet.add(str);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return failSet;
	}
	
	// 处理字符串信息, 将信息转换成Map
	public Map<Class<?>, List<String>> getResultMap(Set<String> failSet) {
		Map<Class<?>, List<String>> resultMap = new LinkedHashMap<Class<?>, List<String>>();
		String className;
		List<String> methodList = new ArrayList<String>();
		Class<?> failClass = null;
		// 标记Class是否将全部方法放入Map中
		Map<Class<?>, Integer> bitMap = new HashMap<Class<?>, Integer>();
		for(String str : failSet){
			// 先处理不带(), 表示整个类失败
			if( ! str.contains("(")){
				className = str.substring(str.lastIndexOf("com"), str.length());
				try {
					failClass = Class.forName(className);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 拿失败类中的方法
				Method[] met = failClass.getMethods();
				for (Method method : met) {
					// 只拿@Test 方法
					if (method.isAnnotationPresent(Test.class)
							&& (!method
									.isAnnotationPresent(Ignore.class))) {
						// method.getName() 返回的格式：testLikeVideo
						System.out.println(method.getName());
						methodList.add(method.getName());
					}
				}
				resultMap.put(failClass, methodList);
				bitMap.put(failClass, 1);
			}else{
				// 处理单个测试方法
				className = str.substring(str.indexOf("(") + 1, str.length() - 1);
				try {
					failClass = Class.forName(className);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
				// 之后支持 添加JUnitCore格式
				int last = str.indexOf("(");
				String methodPart = str.substring(str.lastIndexOf(".", last) + 1, last);
				
				
				// 判断该类是否已通过“类”的方式进入错误用例集,不存在，则以“方法（类）”处理
				if(!bitMap.containsKey(failClass)){
					// 聚合 class-method 一对多
					if (resultMap.containsKey(failClass)) {
						// 拿到之前的class 对应的list, 并在该list下新增 method, 再放回map
						List<String> beforeFailMethod = resultMap
								.get(failClass);
						beforeFailMethod.add(methodPart);
						resultMap.put(failClass, beforeFailMethod);
					} else {
						// 第一次添加该class时
						List<String> firstMethod = new ArrayList<String>();
						firstMethod.add(methodPart);
						resultMap.put(failClass, firstMethod);
					}
				}	
			}
		}
	
		int casesNum = 0;
		List<String> myMethod = new ArrayList<String>();
		for(Map.Entry<Class<?>, List<String>> myCases: resultMap.entrySet()){
			
			myMethod = myCases.getValue();
			casesNum += myMethod.size();
			System.out.println(myCases.getKey() + ":" + myMethod + ":" + myMethod.size() );
		}
		System.out.println("mvn 找到用例数：" + casesNum);
		return resultMap;
	}
	
	@Override
	public Map<Class<?>, List<String>> findFailedCases(String logPath) {
		return getResultMap(failCasesStr(logPath));
	}

}
