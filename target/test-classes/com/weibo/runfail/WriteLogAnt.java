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
 * @author hugang
 */
public class WriteLogAnt {
	public void writeLogAnt(String logPath, String resultPath)
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

		// 根据Result.txt , 正则表达式找失败用例,
		// 形如 方法（类） 格式
		// [GroupChatNotFrontTest(com.weibo.cases.xiaoyu.FrontAppStatusesTest):
		// should create success]
		String pattern = "\\[(\\w+)\\((.*)\\)";

		Pattern pt = Pattern.compile(pattern);
		Matcher mt;

		// 根据Result.txt , 正则表达式找失败用例（只提供失败类信息）
		// 形如：
		// [com.weibo.cases.xiaoyu.FrontAppStatusesTest: It should get success]
		String patternClass = "\\[((\\w+\\.)+\\w+):";
		Pattern ptClass = Pattern.compile(patternClass);
		Matcher mtClass;

		List<String> strList = new ArrayList<String>();
		// 行号
		List<Integer> flags = new ArrayList();
		int i = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 写时间戳
		writer.write(sdf.format(new Date()));
		writer.newLine();

		try {
			while (reader.ready()) {
				String str = reader.readLine();
				strList.add(str);
				mt = pt.matcher(str);
				mtClass = ptClass.matcher(str);
				// 匹配后，记录匹配的行号
				if (mt.find() || mtClass.find()) {
					flags.add(i);
				}	
				i++;
			}
			for (int k = 0; k < flags.size(); k++) {
				// 模拟 FindFailTest.java 截取的规则
				String failStr = "Testcase:" + strList.get(flags.get(k));
				writer.write(failStr);
				writer.newLine();
			}
//			System.out.println("--------------------------------------");
//			System.out.println("   Ant Model find failcases number: " + i);
//			System.out.println("--------------------------------------");
//			Utils.sleep(2000);		
		} finally {
			writer.close();
			reader.close();

		}
	}
}
