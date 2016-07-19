package com.weibo.runner;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Categories;
import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Runner;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.RunnerScheduler;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * 自定义runner, 并发执行
 * 
 * 
 * @author hugang
 *
 */
public final class ConcurrentSuite extends ClasspathSuite {

	public static Runner MulThread(Runner runner) {
		if (runner instanceof ParentRunner) {
			// setScheduler(RunnerScheduler scheduler):Sets a scheduler that
			// determines the order and parallelization of children
			// RunnerScheduler:Represents a strategy for scheduling when
			// individual test methods should be run (in serial or parallel)
			((ParentRunner) runner).setScheduler(new RunnerScheduler() {
				private final ExecutorService fService = Executors
						.newCachedThreadPool();

				// private final ExecutorService fService =
				// Executors.newFixedThreadPool(10);

				// Schedule a child statement to run
				public void schedule(Runnable childStatement) {
					this.fService.submit(childStatement);
				}

				// Override to implement any behavior that must occur after all
				// children have been scheduled
				public void finished() {
					try {
						this.fService.shutdown();
						this.fService.awaitTermination(9223372036854775807L,
								TimeUnit.NANOSECONDS);
					} catch (InterruptedException e) {
						e.printStackTrace(System.err);
					}
				}
			});
		}
		return runner;
	}

	public ConcurrentSuite(final Class<?> klass) throws InitializationError {
		// 调用父类ClasspathSuite构造函数
		// AllDefaultPossibilitiesBuilder根据不同的测试类定义（@RunWith的信息）返回Runner,使用职责链模式
		super(klass, new AllDefaultPossibilitiesBuilder(true) {
			@Override
			public Runner runnerForClass(Class<?> testClass) throws Throwable {
				List<RunnerBuilder> builders = Arrays
						.asList(new RunnerBuilder[] {
								// 创建Runner, 工厂类,
								// 自定义自己的Runner，找出注解为@Ignore,并输出@Ignore的类和方法名
								new RunnerBuilder() {
									@Override
									public Runner runnerForClass(
											Class<?> testClass)
											throws Throwable {
										// 获取类的所有方法
										Method[] methods = testClass
												.getMethods();

										// 如果类有@Ignore，则只输出类名，因为Junit最后计算结果时，会把@Ignore类记为1个用例，
										// 不是计算类下面的测试方法数（实验验证过）
										// 否则，遍历方法，如果方法有@Ignore，则输出该方法
										if (testClass
												.isAnnotationPresent(Ignore.class)) {

											System.out.println("Ignore: "
													+ testClass.getName());

										} else {
											for (Method method : methods) {
												if (method
														.isAnnotationPresent(Ignore.class)) {
													System.out.println("Ignore: "
															+ testClass
																	.getName()
															+ "."
															+ method.getName());
												}
											}
										}
										return null;
									}
								}, ignoredBuilder(), annotatedBuilder(),
								suiteMethodBuilder(), junit3Builder(),
								junit4Builder() });
				for (RunnerBuilder each : builders) {
					// 根据不同的测试类定义（@RunWith的信息）返回Runner
					Runner runner = each.safeRunnerForClass(testClass);
					if (runner != null)
						// 方法级别，多线程执行
						// return MulThread(runner);
						return runner;
				}
				return null;
			}
		});

		// 类级别，多线程执行
		setScheduler(new RunnerScheduler() {
			private final ExecutorService fService = Executors
					.newCachedThreadPool();

			public void schedule(Runnable paramRunnable) {
				// TODO Auto-generated method stub
				fService.submit(paramRunnable);
			}

			public void finished() {
				// TODO Auto-generated method stub
				try {
					fService.shutdown();
					fService.awaitTermination(Long.MAX_VALUE,
							TimeUnit.NANOSECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace(System.err);
				}
			}
			//
			// // ExecutorService executorService =
			// Executors.newFixedThreadPool(
			// // klass.isAnnotationPresent(Concurrent.class) ?
			// // klass.getAnnotation(Concurrent.class).threads() :
			// // (int) (Runtime.getRuntime().availableProcessors() * 1.5),
			// // new NamedThreadFactory(klass.getSimpleName()));
			// // CompletionService<Void> completionService = new
			// // ExecutorCompletionService<Void>(executorService);
			// // Queue<Future<Void>> tasks = new LinkedList<Future<Void>>();
			// //
			// // @Override
			// // public void schedule(Runnable childStatement) {
			// // tasks.offer(completionService.submit(childStatement, null));
			// // }
			// //
			// // @Override
			// // public void finished() {
			// // try {
			// // while (!tasks.isEmpty())
			// // tasks.remove(completionService.take());
			// // } catch (InterruptedException e) {
			// // Thread.currentThread().interrupt();
			// // } finally {
			// // while (!tasks.isEmpty())
			// // tasks.poll().cancel(true);
			// // executorService.shutdownNow();
			// // }
			//
		});
	}

	// static final class NamedThreadFactory implements ThreadFactory {
	// static final AtomicInteger poolNumber = new AtomicInteger(1);
	// final AtomicInteger threadNumber = new AtomicInteger(1);
	// final ThreadGroup group;
	//
	// NamedThreadFactory(String poolName) {
	// group = new ThreadGroup(poolName + "-"
	// + poolNumber.getAndIncrement());
	// }
	//
	// @Override
	// public Thread newThread(Runnable r) {
	// System.out.println(group.getName() + "-thread-");
	//
	// return new Thread(group, r, group.getName() + "-thread-"
	// + threadNumber.getAndIncrement(), 0);
	// }
	// }

}