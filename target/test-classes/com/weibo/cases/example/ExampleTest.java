package com.weibo.cases.example;

import static org.junit.Assert.*;

import org.junit.Test;
import static org.hamcrest.Matchers.is;

public class ExampleTest {

	@Test
	public void testExampleOne() {
		try {
			// 构造场景
			assertThat("abc", is("abc"));

		} catch (Exception e) {
			e.printStackTrace();
			fail("testExampleOne excepiton");
		} finally {
			//  清数据
		}
	}

	@Test
	public void testExampleTwo() {
		try {

			assertThat("abc", is("def"));

		} catch (Exception e) {
			e.printStackTrace();
			fail("testExampleTwo excepiton");
		} finally {

		}
	}
}
