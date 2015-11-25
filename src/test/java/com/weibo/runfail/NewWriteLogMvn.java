package com.weibo.runfail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 将JUnitCore执行结果，回写到源日志
 * author hugang
 */
public class NewWriteLogMvn {
	
	
	// 格式转换，将JUnitCore装换成Mvn
	public List<String> FormatJUnitCoreToMvn(List<String> failStr){
		List<String> mvnFailList = new ArrayList<String>();
		
		
		
		
		
		
		
		return mvnFailList;
	}
	
	
	
	public void writeLogMvn(String logPath, String resultPath)
			throws IOException {
		BufferedWriter writer;
		BufferedReader reader;

		// 日志文件
		File sourceFile = new File(logPath);
		if (!sourceFile.exists()) {
			sourceFile.createNewFile();
		}
		writer = new BufferedWriter(new FileWriter(sourceFile));

		// 结果日志
		File readFile = new File(resultPath);
		if (!readFile.exists()) {
			readFile.createNewFile();
			System.out.println("read 文件不存在， Result.txt 不存在");
		} else {
			System.out.println("" + readFile.canRead() + " "
					+ readFile.length() + " " + readFile.getAbsolutePath()
					+ " " + readFile.getCanonicalPath());
		}
		reader = new BufferedReader(new FileReader(readFile));

		// 1
		// 根据Result.txt , 正则表达式找失败用例
		// 形如 方法（类） 格式
		// [GroupChatNotFrontTest(com.weibo.cases.xiaoyu.FrontAppStatusesTest):
		// should create success]
		String pattern = "\\[(\\w+)\\((.*)\\):(.*)\\]";
		Pattern pt = Pattern.compile(pattern);
		Matcher mt;

		List<String> strList = new ArrayList<String>();

		// 2
		// 根据Result.txt , 正则表达式找失败用例（只提供失败类信息）
		// 形如：
		// [com.weibo.cases.xiaoyu.FrontAppStatusesTest: It should get success]
		String patternClass = "\\[((\\w+\\.)+(\\w+)):(.*)\\]";
		Pattern ptClass = Pattern.compile(patternClass);
		Matcher mtClass;

		// 行号
		 List<Integer> flags = new ArrayList();
		 int i = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		writer.write(sdf.format(new Date()));
		writer.write("mvn 回写失败结果");
		writer.newLine();
	
		String failStackTrace = "CustomResult Failed StackTrace@";
		String failInfo = "CustomResult Fail@";
		String failMethodStackTraceStr;
		String failMethodInfoStr;
		
		String failClassStackTraceStr;
		String failClassInfoStr;
		try {
			while (reader.ready()) {
				String str = reader.readLine();
				strList.add(str);
				mt = pt.matcher(str);
				mtClass = ptClass.matcher(str);
				// 匹配1后，记录匹配的行号
				if (mt.find()) {
					String[] className = mt.group(2).split("\\.");
					int size = className.length;
					String methodName = mt.group(1);
					// 模拟MVN 日志
					// 1.错误栈信息，供前端解析
					// CustomResult Failed StackTrace@SassPart2ShortUrlStatusTest.testPublicRepost:88 testPublicRepost exception 
					failMethodStackTraceStr = failStackTrace + className[size - 1] + "."
							+ methodName + mt.group(3).replaceAll("\n", "");
					// 2.用例信息
					// CustomResult Fail@com.weibo.cases.wanglei16.SassPart2ShortUrlStatusTest.testPublicRepost(com.weibo.cases.wanglei16.SassPart2ShortUrlStatusTest)
					failMethodInfoStr = failInfo + mt.group(2) + "." + methodName + "(" + mt.group(2) + ")";
					writer.write(failMethodStackTraceStr);
					// 供前端解析
					System.out.println(failMethodStackTraceStr);
					writer.newLine();
					writer.write(failMethodInfoStr);
					writer.newLine();
					i++;
				}
				// 类失败
				if (mtClass.find()) {
					// 模拟MVN 日志
					// 1.错误栈信息, group(1) 全限类名， group(4)栈信息
					failClassStackTraceStr = failStackTrace + mtClass.group(1) + " " + mtClass.group(4).replaceAll("\n", "");
					// com.weibo.cases.maincase.XiaoyuGroupStatusLikeBVTTest.com.weibo.cases.maincase.XiaoyuGroupStatusLikeBVTTest
					failClassInfoStr = failInfo + mtClass.group(1) + "." + mtClass.group(1);
					writer.write(failClassStackTraceStr);
					// 供前端解析
					System.out.println(failClassStackTraceStr);
					writer.newLine();
					writer.write(failClassInfoStr);
					writer.newLine();
					i++;
				}
			}
		
		} finally {
			writer.close();
			reader.close();

		}

	}

}
