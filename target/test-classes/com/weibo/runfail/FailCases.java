package com.weibo.runfail;

import java.util.List;
import java.util.Map;
/*
 * @author hugang
 */
public abstract class FailCases {
	public abstract Map<Class<?>, List<String>> findFailedCases(String logPath);
}
