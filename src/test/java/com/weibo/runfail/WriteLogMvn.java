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
 * author hugang
 */
public class WriteLogMvn {
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
		// 根据Result.txt , 正则表达式找失败用例,
		// 形如 方法（类） 格式
		// [GroupChatNotFrontTest(com.weibo.cases.xiaoyu.FrontAppStatusesTest):
		// should create success]
		String pattern = "\\[(\\w+)\\((.*)\\)";
		Pattern pt = Pattern.compile(pattern);
		Matcher mt;

		List<String> strList = new ArrayList<String>();

		// 2
		// 根据Result.txt , 正则表达式找失败用例（只提供失败类信息）
		// 形如：
		// [com.weibo.cases.xiaoyu.FrontAppStatusesTest: It should get success]
		String patternClass = "\\[((\\w+\\.)+(\\w+)):";
		Pattern ptClass = Pattern.compile(patternClass);
		Matcher mtClass;

		// 行号
		 List<Integer> flags = new ArrayList();
		 int i = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		writer.write(sdf.format(new Date()));
		writer.newLine();
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
					// \\s{2}(\\w+)\\.(\\w+):\\d{1,}
					// 模拟MVN 日志，回写 \s\sclassname.methodname:\d{1,}
					// 为了与MVN日志统一（正则匹配一致, 循环跑），完全类名去掉包名
					// com.weibo.cases.wanglei16.LikesByMeBatchTest
					String failStr = "  " + className[size - 1] + "."
							+ methodName + ":1 ";
					writer.write(failStr);
					writer.newLine();
					i++;
				}
				// 类失败
				if (mtClass.find()) {
					String SingleclassName = mtClass.group(2);
					// 模拟MVN 日志，回写 BeforeClassTest.setUpBeforeClass:15
					// 为了与MVN日志统一（正则匹配一致, 循环跑），完全类名去掉包名
					// com.weibo.cases.wanglei16.LikesByMeBatchTest
					String SinglefailStr = "  " + SingleclassName + ".setUpBeforeClass:15";
					writer.write(SinglefailStr);
					writer.newLine();
					i++;
				}
			}
			// for (int k = 0; k < flags.size(); k++) {
			// // 模拟MVN 日志，回写 \s\sclassname.methodname:\d{1,}
			// String failStr = "Testcase:" + strList.get(flags.get(k));
			// writer.write(failStr);
			// writer.newLine();
			// }
		} finally {
			writer.close();
			reader.close();

		}

	}

}
