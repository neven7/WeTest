package com.weibo.cases.hugang;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.weibo.common.StatusCommon;
import com.weibo.runner.Retry;
import com.weibo.global.Constant;
import com.weibo.global.TestLog;
import com.weibo.model.Account;
import com.weibo.model.Status;
import com.weibo.runner.RetryRunner;
import com.weibo.userpool.UserPool;

/**
 * 用例负责人：
 * 
 * @author hugang
 *
 *	用例所属提案： WEIBOTOTEST
 * 
 */
@Retry
@RunWith(RetryRunner.class)
public class ShowBatchExposureStatusTest {

	// 测试方法使用的测试账号
	private static final int NUM = 3;
	static String[] users = new String[NUM];
	static String[] pwd = new String[NUM];
	static long[] uid = new long[NUM];
	static String[] nickname = new String[NUM];

	static List<Account> accountList = new ArrayList<Account>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	
	@Before
	public void setUp() throws Exception {
		// 从用户池中取NUM个账号
		accountList = UserPool.getAccounts(Constant.FREESTATE, NUM);
		UserPool.updateAccountState(accountList, Constant.BUSYSTATE);
		int i = 0;
		for (Account account : accountList) {
			users[i] = account.getEmail();
			pwd[i] = account.getPassword();
			uid[i] = account.getUid();
			i++;
		}
	}

	@After
	public void tearDown() throws Exception {
		// 每个测试方法执行结束后，释放账号
		UserPool.updateAccountState(accountList, Constant.FREESTATE);
	}

	/*
	*用例描述
	 */
	@Test
	public void testSingleExposureBizId() {
		StatusCommon statusCom = new StatusCommon();
		Status status1;
		try {
			// 上行构造场景
			
			// caseid:类名.测试方法:用例步数 steps
			// 每执行完一步，记录
			TestLog.Comment("caseid:ShowBatchExposureStatusTest.testSingleExposureBizId:7 steps");
			// 用户A发微博S1
			status1 = (Status) statusCom.updateStatusPublic(users[0], pwd[0],
					"接口params");
			// 上行操作，可以sleep下，接口响应时间，代码执行几毫秒
			long status1Id = status1.getId();
			TestLog.Comment("step1:用户A发微博S1");

			// 用例详情省略...
			TestLog.Comment("step2:设置S1曝光量为1，用户B不在曝光列表里");

			
			// 下行校验
			assertThat(status1.getText(), is(""));
			
			
			TestLog.Comment("step7:B查看S1(频次控制不生效)，返回S1");

		} catch (Exception e) {
			e.printStackTrace();
			fail("testSingleExposureBizId fail");		
		} finally {
			// 清除在try里构建的关系，本用例没有
		}

	}


}
