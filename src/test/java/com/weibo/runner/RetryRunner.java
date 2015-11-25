package com.weibo.runner;



import java.lang.annotation.Annotation;

import junit.framework.AssertionFailedError;

import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

import com.weibo.global.ParseProperties;

/**
 * 自定义runner, 运行中重试
 * @author hugang
 *
 */
public class RetryRunner extends BlockJUnit4ClassRunner {

	private int retryTime;
	private int now = 0;

	public RetryRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@Override
	protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
		Description description = describeChild(method);
		if (method.getAnnotation(Ignore.class) != null) {
			notifier.fireTestIgnored(description);
		} else
			try {
				if (shouldRetry(method)) { // 需要重试，走重试逻辑
					runLeafRetry(methodBlock(method), description, notifier);
				} else { // 不需要重试，走原来逻辑
					runLeaf(methodBlock(method), description, notifier);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private boolean shouldRetry(final FrameworkMethod method) throws Exception {
		Retry retry = null;
		// 类上有@Retry
		retry = getClassRetry(getTestClass().getJavaClass());
		// 方法有@Retry
		Retry annotation = method.getAnnotation(Retry.class);
		// 方法的@Retry替换类的@Retry
		if (annotation != null) {
			retry = annotation;
		}
		if (retry != null) {
			if (retry.value() > 0) {
				// 之前根据注解@Retry, 获取重试次数 
//				retryTime = retry.value();
				// 现在更改为，根据global.properties中retry判断次数， 为空不重试， 其他值为默认重试次数
				String retryProperty = ParseProperties.getSystemProperty("retry");
				if("".equals(retryProperty) || (1 == retry.value())){
					retryTime = 1;
				}else{
					retryTime = Integer.parseInt(retryProperty);
				}
//				System.out.println("test property get retry:  " + retryTime);
			} else {
				// retry.value()<=0时，为1
				retryTime = 1;
			}
			now = 0;
			return true;
		}
		return false;
	}

	private Retry getClassRetry(Class<?> mClass) {
		if (mClass == null || mClass == Object.class) {
			return null;
		}
		Retry retry = null;
		Annotation[] annotations = mClass.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof Retry) {
				retry = (Retry) annotation;
				break;
			}
		}
		// 判断父类
		if (null == retry) {
			retry = getClassRetry(mClass.getSuperclass());
		}
		return retry;
	}

	protected final void runLeafRetry(Statement statement,
			Description description, RunNotifier notifier) {
		EachTestNotifier eachNotifier = new EachTestNotifier(notifier,
				description);
		eachNotifier.fireTestStarted();
		try {
			retryRun(statement);
			//  标记成功用例信息
			System.out.println("SuccessCase: " + description);
		} catch (AssumptionViolatedException e) {
			eachNotifier.addFailedAssumption(e);
		} catch (Throwable e) {
			eachNotifier.addFailure(e);
		} finally {
			eachNotifier.fireTestFinished();
		}
	}

	private void retryRun(Statement statement) throws Throwable {
		try {
			now++;
			statement.evaluate();
			
		} catch (AssertionFailedError e) {
			if (now < retryTime) {
				retryRun(statement);
			} else {
				throw e;
			}
		} catch (MultipleFailureException e) {
			if (now < retryTime) {
				retryRun(statement);
			} else {
				throw e;
			}
		} catch (AssertionError e) {
			// assertThat断言失败，抛AssertionError
			if (now < retryTime) {
				retryRun(statement);
			} else {
				throw e;
			}
		} catch (Throwable e) {
			// Throwable 所有异常
			if (now < retryTime) {
				retryRun(statement);
			} else {
				throw e;
			}
		}
	}

}
