package com.weibo.cases.suite;
import org.junit.extensions.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.runner.RunWith;

import com.weibo.runner.Concurrent;
import com.weibo.runner.ConcurrentSuite;

@RunWith(ConcurrentSuite.class)
@ClassnameFilters({"com.weibo.cases.hugang.*Test"})
@Concurrent
public interface IntegrationHugangModuleTests {

}
