package com.weibo.runfail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

/*
 * @atuhor hugang
 */
public class FailCasesAnt extends FailCases {

	@Override
	public Map<Class<?>, List<String>> findFailedCases(String logPath) {
		// TODO Auto-generated method stub
		// 文本每一行
		List<String> strList = new ArrayList();
		// 行号
		List<Integer> flags = new ArrayList();
		// 记录FAILED和ERROR
		Set<String> failSet = new TreeSet();
		// String regexStr =
		// "(Testcase:\\s\\w*([\\w]*.{3,}\\w*.):\\sFAILED)|(Testcase:\\s\\w*([\\w]*.{3,}\\w*.):\\sCaused\\san\\sERROR)";
		Pattern p = Pattern.compile("Testcase");
		Matcher m;
		int i = 0;

		try {
			Reader re = new FileReader(new File(logPath));
			BufferedReader bre = new BufferedReader(re);
			while (bre.ready()) {
				String str = bre.readLine();
				strList.add(str);
				m = p.matcher(str);
				// 匹配后，记录匹配的行号
				if (m.find()) {
					flags.add(i);
					System.out.println("find " + i);
				}
				i++;
			}
			for (int k = 0; k < flags.size(); k++) {
				// 去除SKIPPED, 只存 FAILED和ERROR
				if (!strList.get(flags.get(k)).contains("SKIPPED")) {
					// 从文本中取满足匹配的那行字符串
					failSet.add(strList.get(flags.get(k)));
				}
			}
			bre.close();
			re.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("找到失败用例数： " + failSet.size());
		// 存失败用例
		Map<Class<?>, List<String>> myClassMethodMap = new LinkedHashMap();

		// JUnitCore执行结果中，形如这种结果，某个方法初始化失败直接抛错，“[com.weibo.cases.xiaoyu.FrontAppStatusesTest:
		// It should get success]”
		// 有“方法（类）”和“类”， 运行其类下所有方法，不让其方法（类）再进myClassMethodMap：
		// [GroupChatNotFrontTest(com.weibo.cases.xiaoyu.FrontAppStatusesTest):
		// should create success]
		// [com.weibo.cases.xiaoyu.FrontAppStatusesTest: It should get success]

		// 标记该类是否通过“类”记录到myClassMethodMap
		Map<Class<?>, Integer> bitMap = new LinkedHashMap();

		List<String> className = new ArrayList<String>();
		List<String> methodName = new ArrayList<String>();

		for (Iterator<String> it = failSet.iterator(); it.hasNext();) {
			// System.out.println(it.next().toString());
			// Testcase:
			// testAPIRequest(com.weibo.cases.xuelian.FeedWithDarwinTagsForMovieStatusTest):
			// FAILED
			// 取出失败的行
			String str = it.next().toString();

			
			// 先处理只有类信息的失败用例，执行该类所有方法（无法判断该方法是什么）
			// “[com.weibo.cases.xiaoyu.FrontAppStatusesTest: It should get success]”格式的失败用例
			
			// 标记当个类
			Class<?> singleFailClass = null;
			
			if (!str.contains("(")) {
				// ant 生成的单个类失败
				// 处理单个失败类，形如：
				// Testcase:
				// com.weibo.cases.xiaoyu.TopicFrontAddHasNewAndTopStatusTopicTest:
				// FAILED

				// JUnitCore生成的，单个类报错, 形如：
				// Testcase:[com.weibo.cases.xiaoyu.StatusTopicTest: null]
				
				List<String> singleClassMethod = new ArrayList<String>();
				int singleClassLeft = 10;
				// 从singleClassLeft开始找
				int singleClassRight = str.indexOf(":", singleClassLeft);
				String singleFail = str.substring(singleClassLeft,
						singleClassRight);
				try {
					singleFailClass = Class.forName(singleFail);
					// 拿失败类中的方法
					Method[] met = singleFailClass.getMethods();
					for (Method method : met) {
						// 只拿@Test 方法
						if (method.isAnnotationPresent(Test.class)
								&& (!method.isAnnotationPresent(Ignore.class))) {
							// method.getName() 返回的格式：testLikeVideo
							System.out.println(method.getName());
							singleClassMethod.add(method.getName());
						}
					}
					// 将类下所有的用例放到myClassMethodMap下
					myClassMethodMap.put(singleFailClass, singleClassMethod);
					// 标记该类已通过“类”的方式进入失败用例集，无须进入“方法（类）”的方式进入失败用例集
					bitMap.put(singleFailClass, 1);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				// Ant生成的正常失败信息
				// 形如
				// Testcase:
				// testStatusWangYiGCard(com.weibo.cases.xiaoyu.StatusMusicStatusTest):
				// FAILED
				// JUnitCore 生成的正常失败信息
				// Testcase:[testFriendsTimelineMuti(com.weibo.cases.xiaoyu.IsReadStatusTest):
				// expected:<{"3859449779315109":[1]}> but
				// was:<{"3859449779315109":[0]}>]
				// 方法(类)
				int classBegin = str.indexOf("(");
				int classEnd = str.indexOf(")");
				// 类名
				String classPart = str.substring(classBegin + 1, classEnd);
				// 方法名，起始位置为10(固定)
				int beginFind = 10;
				String methodPart = str.substring(beginFind, classBegin);
				
				Class<?> failClass = null;
				try {
					failClass = Class.forName(classPart);
					// 判断该类是否已通过“类”的方式进入错误用例集,不存在，则以“方法（类）”处理
					if(!bitMap.containsKey(failClass)){
						// 聚合 class-method 一对多
						if (myClassMethodMap.containsKey(failClass)) {
							// 拿到之前的class 对应的list, 并在该list下新增 method, 再放回map
							List<String> beforeFailMethod = myClassMethodMap
									.get(failClass);
							beforeFailMethod.add(methodPart);
							myClassMethodMap.put(failClass, beforeFailMethod);
						} else {
							// 第一次添加该class时
							List<String> firstMethod = new ArrayList<String>();
							firstMethod.add(methodPart);
							myClassMethodMap.put(failClass, firstMethod);
						}
					}

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			
		}
		return myClassMethodMap;
	}
}
