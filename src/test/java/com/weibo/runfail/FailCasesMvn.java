package com.weibo.runfail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

/*
 * @author hugang
 */

public class FailCasesMvn extends FailCases {

	@Override
	public Map<Class<?>, List<String>> findFailedCases(String logPath) {
		// 1
		// @BeforeClass失败
		// BeforeClassTest.setUpBeforeClass:15 beforeclass fail
		// \s{2}(\w+).setUpBeforeClass:\d{1,}\s
		// group(1)为类
		// String failBeforeClassRegex = "^\\s{2}(^Run)(.*).setUpBeforeClass";
		String failBeforeClassRegex = "^\\s{2}(\\w+)\\.setUpBeforeClass:\\d{1,}";

		Pattern patternBeforeClass = Pattern.compile(failBeforeClassRegex);
		Matcher matchBeforeClass;

		// 2
		// @BeforeClass， @AfterClass 都失败
		// ﻿com.weibo.cases.maincase.XiaoyuGroupStatusStatusBVTTest.com.weibo.cases.maincase.XiaoyuGroupStatusStatusBVTTest
		// Run 1: XiaoyuGroupStatusStatusBVTTest.setUpBeforeClass:90 
		// Expected: is <3>
		//  but: was <0>
		//  
		// Run 2: XiaoyuGroupStatusStatusBVTTest.tearDownAfterClass:163->destroyPage:554 This is error,should destroy page success!
		// \s{2}Run\s1:\s(\w.+)\.setUpBeforeClass:\d{1,}
		// group(1)为类
		// Run 1: StatusGroupStatusTest.setUpBeforeClass:119->createPage:1066
		// This is error,should create page success!
		String failBeforeClassTwoRegex = "^\\s{2}Run\\s1:\\s(.*).setUpBeforeClass:\\d{1,}";
		Pattern patternBeforeClassTwo = Pattern
				.compile(failBeforeClassTwoRegex);
		Matcher matchBeforeClassTwo;

		// 3
		// TODO Auto-generated method stub
		// HugangV6ProfileBVTTest.testAddStatusUserTimeline:217
		// testAddStatusUserTimeline
		// 正常
		// e.g. LikeObjectRpcTest.testLikeStatus:122
		// group(1)类， group(2)方法
		String failRegex = "\\s{2}(\\w+)\\.(\\w+):\\d{1,}\\s.*";
		Pattern pattern = Pattern.compile(failRegex);
		Matcher match;

		// 4
		// 内嵌方法判断失败
		// StatusShoppingTitleStatusTest.testFriendsTimelineRecom:245->createPage:319
		// This is error,should create page success!
		// group(1) 类， group(2) 方法
		String failSubRegex = "\\s{2}(\\w+)\\.(\\w+):\\d{1,}->\\w+:\\d{1,}.*";
		Pattern patternSub = Pattern.compile(failSubRegex);
		Matcher matchSub;

		// 5
		// @Before失败
		// LingnaStratageOptiHotTimelineCommentBVTTest.setUp:114 NullPointer
		// \s{2}(\w+)\.(setUp):\d{1,}\sNullPointer
		// group(1)类， group(2)方法
		String failRegexBefore = "\\s{2}(\\w+)\\.(setUp):\\d{1,}\\sNullPointer";
		Pattern patternBefore = Pattern.compile(failRegexBefore);
		Matcher matchBefore;

		// 6
		// 待修改
		// @Before ,@After失败
		// com.weibo.cases.hugang.BeforeClassTest.test(com.weibo.cases.hugang.BeforeClassTest)
		// Run 1: BeforeClassTest.setUp:25 before
		// Run 2: BeforeClassTest.tearDown:30 after
		// (\\w.*\\.){1,}(\\w+)\\((\\w.*\\.){1,}(\\w.*)\\)
		// \\s{2}Run\\s1:\\sBeforeClassTest.setUp
		// group(2) 方法， group(4) 类
		String failRegexBeforeBoth = "(\\w+\\.){1,}(\\w+)\\((\\w+\\.){1,}(\\w+)\\)";
		Pattern patternBeforeBoth = Pattern.compile(failRegexBeforeBoth);
		Matcher matchBeforeBoth;

		// 7
		// @Test,@After失败
		// com.weibo.cases.hugang.BeforeClassTest.test(com.weibo.cases.hugang.BeforeClassTest)
		// Run 1: BeforeClassTest.test:37
		// Expected: is <2>
		// but: was <1>
		// Run 2: BeforeClassTest.tearDown:31 after
		// group(1) 类，group(2)方法
		// String failRegexAfter =
		// "\\s{2}Run\\s1:\\s(\\w+)\\.(\\w+):\\d{1,}\\s";
		// 防止和2冲突
		String failRegexAfter = "^\\s{2}Run\\s1:\\s(\\w+)\\.(^setUpBeforeClass):\\d{1,}\\s";
		Pattern patternAfter = Pattern.compile(failRegexAfter);
		Matcher matchAfter;

		// 8.@After; 不处理
		// BeforeClassTest.tearDown:30 after
		// 9.@AfterClass; 不处理
		// BeforeClassTest.tearDownAfterClass:20 afterclass fail
		// 10.@Test,@AfterClass(同正常的1)
		// BeforeClassTest.tearDownAfterClass:21 afterclass fail
		// BeforeClassTest.test:37
		// Expected: is <2>
		// but: was <1>

		// 处理1->2->3->4->5->6

		// 记录失败用例，key:类名, value:方法列表
		Map<Class<?>, List<String>> myClassMethodMap = new LinkedHashMap<Class<?>, List<String>>();
		// bitMap标记@BeforeClass, 如果存在该类，其他匹配不放到myClassMethodMap
		Map<String, Integer> bitMap = new LinkedHashMap();
		

		Reader re;
		BufferedReader bre;
		try {
			re = new FileReader(new File(logPath));
			bre = new BufferedReader(re);
			while (bre.ready()) {
				String str = bre.readLine();
				// 处理第1种
				matchBeforeClass = patternBeforeClass.matcher(str);
				// 部分匹配
				if (matchBeforeClass.find()) {
					String AClassName = matchBeforeClass.group(1);
					if (!bitMap.containsKey(AClassName)) {
						List<String> singleClassMethod = new ArrayList<String>();
						System.out.println("matchBeforeClass" + " : "
								+ AClassName);
						RealClassMvn rcm = new RealClassMvn();
						rcm.findClass(AClassName);
						String realClassName = rcm.getRealClassName();
						Class<?> failClass = Class.forName(realClassName);

						// 拿失败类中的方法
						Method[] met = failClass.getMethods();
						
						for (Method method : met) {
							// 只拿@Test 方法
							if (method.isAnnotationPresent(Test.class)
									&& (!method
											.isAnnotationPresent(Ignore.class))) {
								// method.getName() 返回的格式：testLikeVideo
								System.out.println(method.getName());
								singleClassMethod.add(method.getName());
							}
						}

						// 将类下所有的用例放到myClassMethodMap下
						myClassMethodMap.put(failClass, singleClassMethod);
						// 标记该类已通过“类”的方式进入失败用例集，无须进入“方法（类）”的方式进入失败用例集
						bitMap.put(AClassName, 1);
					}

				}
				// 处理第2种
				matchBeforeClassTwo = patternBeforeClassTwo.matcher(str);
				// 部分匹配
				// if (matchBeforeClassBoth.matches()) {
				if (matchBeforeClassTwo.find()) {
					List<String> singleClassMethodBoth = new ArrayList<String>();
					String ABothClassName = matchBeforeClassTwo.group(1);
					// 第1步没处理过的类，才处理
//					System.out.println(ABothClassName);
					if (!bitMap.containsKey(ABothClassName)) {
						System.out.println("matchBeforeClassBoth" + " : "
								+ ABothClassName);
						RealClassMvn rcmBoth = new RealClassMvn();
						rcmBoth.findClass(ABothClassName);
						String realClassNameBoth = rcmBoth.getRealClassName();
						Class<?> failClassBoth = Class
								.forName(realClassNameBoth);

						// 拿失败类中的方法
						Method[] metBoth = failClassBoth.getMethods();
						for (Method method : metBoth) {
							// 只拿@Test 方法
							if (method.isAnnotationPresent(Test.class)
									&& (!method
											.isAnnotationPresent(Ignore.class))) {
								// method.getName() 返回的格式：testLikeVideo
								System.out.println(method.getName());
								singleClassMethodBoth.add(method.getName());
							}
						}

						// 将类下所有的用例放到myClassMethodMap下
						myClassMethodMap.put(failClassBoth,
								singleClassMethodBoth);
						// 标记该类已通过“类”的方式进入失败用例集，无须进入“方法（类）”的方式进入失败用例集
						bitMap.put(ABothClassName, 1);

					}
				}

				// 处理第3种
				match = pattern.matcher(str);
				// 匹配后，group(1)为类名，group(2)为方法名
				if (match.matches()) {
					String className = match.group(1);
					String methodName = match.group(2);
					// 前面没有找到该类，才处理
					if (!bitMap.containsKey(className)) {
						System.out.println(className + ":" + methodName);
						// mvn执行结果中，失败用例只有单独类名，不是完全类名（包括包名）
						// 会导致ClassNotFoundException
						// RealClassMvn找所属的类名，返回完全类名
						RealClassMvn rcm = new RealClassMvn();
						rcm.findClass(className);
						String realClassName = rcm.getRealClassName();

						Class<?> failClass = Class.forName(realClassName);
						// 聚合 class-method 一对多
						if (myClassMethodMap.containsKey(failClass)) {
							// 拿到之前的class 对应的list, 并在该list下新增 method, 再放回map
							List<String> beforeFailMethod = myClassMethodMap
									.get(failClass);
							beforeFailMethod.add(methodName);
							myClassMethodMap.put(failClass, beforeFailMethod);
						} else {
							// 第一次添加该class时
							List<String> firstMethod = new ArrayList<String>();
							firstMethod.add(methodName);
							myClassMethodMap.put(failClass, firstMethod);
						}
					}

				}

				// 处理第4种
				matchSub = patternSub.matcher(str);
				// 匹配后，group(1)为类名，group(2)为方法名
				if (matchSub.matches()) {
					String classNameSub = matchSub.group(1);
					String methodNameSub = matchSub.group(2);
					// 前面没有找到该类，才处理
					if (!bitMap.containsKey(classNameSub)) {
						System.out.println(classNameSub + ":" + methodNameSub);
						// mvn执行结果中，失败用例只有单独类名，不是完全类名（包括包名）
						// 会导致ClassNotFoundException
						// RealClassMvn找所属的类名，返回完全类名
						RealClassMvn rcmSub = new RealClassMvn();
						rcmSub.findClass(classNameSub);
						String realClassNameSub = rcmSub.getRealClassName();

						Class<?> failClassSub = Class.forName(realClassNameSub);
						// 聚合 class-method 一对多
						if (myClassMethodMap.containsKey(failClassSub)) {
							// 拿到之前的class 对应的list, 并在该list下新增 method, 再放回map
							List<String> beforeFailMethodSub = myClassMethodMap
									.get(failClassSub);
							beforeFailMethodSub.add(methodNameSub);
							myClassMethodMap.put(failClassSub,
									beforeFailMethodSub);
						} else {
							// 第一次添加该class时
							List<String> firstMethod = new ArrayList<String>();
							firstMethod.add(methodNameSub);
							myClassMethodMap.put(failClassSub, firstMethod);
						}
					}
				}

				// 处理第5种
				matchBefore = patternBefore.matcher(str);
				// 匹配后，group(1)为类名，group(2)为方法名
				if (matchBefore.matches()) {
					String classNameBefore = matchBefore.group(1);
					String methodNameBefore = matchBefore.group(2);
					// 前面没有找到该类，才处理
					if (!bitMap.containsKey(classNameBefore)) {
						System.out.println(classNameBefore + ":"
								+ methodNameBefore);
						// mvn执行结果中，失败用例只有单独类名，不是完全类名（包括包名）
						// 会导致ClassNotFoundException
						// RealClassMvn找所属的类名，返回完全类名
						RealClassMvn rcmBefore = new RealClassMvn();
						rcmBefore.findClass(classNameBefore);
						String realClassNameBefore = rcmBefore
								.getRealClassName();

						Class<?> failClassBefore = Class
								.forName(realClassNameBefore);
						// 聚合 class-method 一对多
						if (myClassMethodMap.containsKey(failClassBefore)) {
							// 拿到之前的class 对应的list, 并在该list下新增 method, 再放回map
							List<String> beforeFailMethodBefore = myClassMethodMap
									.get(failClassBefore);
							beforeFailMethodBefore.add(methodNameBefore);
							myClassMethodMap.put(failClassBefore,
									beforeFailMethodBefore);
						} else {
							// 第一次添加该class时
							List<String> firstMethod = new ArrayList<String>();
							firstMethod.add(methodNameBefore);
							myClassMethodMap.put(failClassBefore, firstMethod);
						}
					}

				}

				// 处理第6种
				matchBeforeBoth = patternBeforeBoth.matcher(str);
				// 匹配后，group(4)为类名，group(2)为方法名
				if (matchBeforeBoth.matches()) {
					System.out.println("debug: " + str);
					String classNameBeforeBoth = matchBeforeBoth.group(4);
					String methodNameBeforeBoth = matchBeforeBoth.group(2);
					// 前面没有找到该类，才处理
					if (!bitMap.containsKey(classNameBeforeBoth)) {
						System.out.println(classNameBeforeBoth + ":"
								+ methodNameBeforeBoth);
						// mvn执行结果中，失败用例只有单独类名，不是完全类名（包括包名）
						// 会导致ClassNotFoundException
						// RealClassMvn找所属的类名，返回完全类名
						RealClassMvn rcmBeforeBoth = new RealClassMvn();
						rcmBeforeBoth.findClass(classNameBeforeBoth);
						String realClassNameBeforeBoth = rcmBeforeBoth
								.getRealClassName();

						Class<?> failClassBeforeBoth = Class
								.forName(realClassNameBeforeBoth);
						// 聚合 class-method 一对多
						if (myClassMethodMap.containsKey(failClassBeforeBoth)) {
							// 拿到之前的class 对应的list, 并在该list下新增 method, 再放回map
							List<String> beforeFailMethodBefore = myClassMethodMap
									.get(failClassBeforeBoth);
							beforeFailMethodBefore.add(methodNameBeforeBoth);
							myClassMethodMap.put(failClassBeforeBoth,
									beforeFailMethodBefore);
						} else {
							// 第一次添加该class时
							List<String> firstMethod = new ArrayList<String>();
							firstMethod.add(methodNameBeforeBoth);
							myClassMethodMap.put(failClassBeforeBoth,
									firstMethod);
						}
					}

				}

				// 处理第7种
				matchAfter = patternAfter.matcher(str);
				// 匹配后，group(1)为类名，group(2)为方法名
				if (matchAfter.matches()) {
					String classNameAfter = matchBeforeBoth.group(1);
					String methodNameAfter = matchBeforeBoth.group(2);
					// 前面没有找到该类，才处理
					if (!bitMap.containsKey(classNameAfter)) {
						System.out.println(classNameAfter + ":"
								+ classNameAfter);
						// mvn执行结果中，失败用例只有单独类名，不是完全类名（包括包名）
						// 会导致ClassNotFoundException
						// RealClassMvn找所属的类名，返回完全类名
						RealClassMvn rcmAfter = new RealClassMvn();
						rcmAfter.findClass(classNameAfter);
						String realClassNameAfter = rcmAfter.getRealClassName();

						Class<?> failClassAfter = Class
								.forName(realClassNameAfter);
						// 聚合 class-method 一对多
						if (myClassMethodMap.containsKey(failClassAfter)) {
							// 拿到之前的class 对应的list, 并在该list下新增 method, 再放回map
							List<String> beforeFailMethodBefore = myClassMethodMap
									.get(failClassAfter);
							beforeFailMethodBefore.add(methodNameAfter);
							myClassMethodMap.put(failClassAfter,
									beforeFailMethodBefore);
						} else {
							// 第一次添加该class时
							List<String> firstMethod = new ArrayList<String>();
							firstMethod.add(methodNameAfter);
							myClassMethodMap.put(failClassAfter, firstMethod);
						}
					}

				}

			}
			bre.close();
			re.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(myClassMethodMap.toString());
		return myClassMethodMap;

	}

}
