package com.weibo.runner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.weibo.global.ParseProperties;

// 重试次数，默认2
// 方法会覆盖类，子类会覆盖父类

// 重试默认值改用配置文件global.properties中 retry 字段指定
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Retry {
   int value() default 2;
}
