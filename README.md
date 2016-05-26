### 一.简介
```
   WeTest是处理API接口测试的轻量级自动化测试框架，java语言实现，拓展JUnit4开源框架，支持Ant/Maven执行方式。

   工具特点：
       
   1.支持suite，根据JUnit4测试类名正则匹配，聚合相同模块的用例，运行单个聚合类即可实现运行一个模块所有的用例；
   2.支持测试类和方法级别用例并发执行，缩短执行时间(用例间需线程安全)；
   3.支持失败重试，包括执行中（@Retry注解）和结果跑完（根据Ant/Maven失败日志，扫失败日志，JUnitCore跑失败用例，多线程执行）； 
   4.邮件通知结果。
```
#### 框架图：
![这里写图片描述](http://img.blog.csdn.net/20151028220329877)
### 二. 开发环境

```    
   Java IDE，JDK6以上，JUnit4，Mysql，Ant/maven，Svn。
```
### 三.项目结构

```
1.包com.weibo.cases.hugang，可以新建多个以模块或者以人的维度管理的用例集，测试用例放在该包下。
2.包com.weibo.cases.suite，聚合相同模块测试用例的聚合类。
3.包com.weibo.common，封装不同接口为特定方法。
5.包com.weibo.global，接口http请求（get或post），HttpClient实现；http请求返回的String反序列化成Javabean对象，Jackson实现。
6.包com.weibo.model, 接口返回的结果对应的Javabean类。
7.包com.weibo.runfail, 用例结束后，重跑失败用例。
8.包com.weibo.runner, 自定义的runner, @Retry（运行中重试）,@ThreadRunner（单个类并发）,@ConcurrentSuite（Suite类和方法级别）等。
9.包com.weibo.userpool, 用例中申请测试账号，JDBC实现。

配置文件：
c3p0.properties 用户池数据库连接池配置， mysql地址，账号，密码；连接池最大，最小连接数。
global.properties 测试环境设置，ip, port, appkey, retry默认设置。

```

### 四.调度方式

```
以shell脚本进行调度，根据不同模块，执行对应的build.xml;
在build_***.xml中指定测试模块，shell脚本会根据你的选择，ant执行不同的xml，达到运行某个模块的功能。 
 
二期已经使用Taobao toast自动化测试框架二次开发，前端调度。  
http://blog.csdn.net/neven7/article/details/45022825
http://blog.csdn.net/neven7/article/details/44886011

```
### 五.测试用例填写规范

```
1.每个用例使用
try{
用例;
TestLogComment("用例描述");
}catch(Exception e){
fail("用例信息");
}finally{
清关系等；
}
结构；catch()能捕获非assertThat失败，如接口报错； finally里清try语句块构建的关系等;
2.每个用例之间不能有任何依赖，都是单独的;
3.每个测试Class名，后缀应该跟该相应模块相关，主要为聚合某一类功能，正则匹配用。
```

### 六. 使用方法


   团队成员，可以将自己添加的case上传到SVN上，协同工作。单个case可以直接在eclipse上运行。
   对于自动化回归测试时，可以根据不同模块，执行对应的build.xml。
   
1 运行shell脚本，运行不同模块用例。

![这里写图片描述](http://img.blog.csdn.net/20150904154245930)


2 邮件推送运行结果，并将本次运行的失败日志作为附件发送，将失败日志放到com.weibo.runfail下，运行RunFailedCases.java重试。

![这里写图片描述](http://img.blog.csdn.net/20150904115617809)
![这里写图片描述](http://img.blog.csdn.net/20150904115526196)


### 七.WeTest Features
![这里写图片描述](http://img.blog.csdn.net/20151028220407942)
![这里写图片描述](http://img.blog.csdn.net/20151028220419965)
![这里写图片描述](http://img.blog.csdn.net/20151028220527721)
![这里写图片描述](http://img.blog.csdn.net/20151028220616281)
![这里写图片描述](http://img.blog.csdn.net/20151028220559296)
![这里写图片描述](http://img.blog.csdn.net/20151028220643007)
![这里写图片描述](http://img.blog.csdn.net/20151028220659487)
### 八. 流程图

#### 1. 用例执行流程

![这里写图片描述](http://img.blog.csdn.net/20150904113558088)

#### 2.用户池获取用户

![这里写图片描述](http://img.blog.csdn.net/20150904113751032)

#### 3.接口调用流程   

![这里写图片描述](http://img.blog.csdn.net/20150904113815022)

#### 4.调度脚本流程

![这里写图片描述](http://img.blog.csdn.net/20150904113941095)

#### 5.初始化流程

![这里写图片描述](http://img.blog.csdn.net/20150904113955968)


相关内容介绍：

 [JUnit结果重跑失败用例（支持Mvn和Ant）](http://blog.csdn.net/neven7/article/details/45221685) 
 
 [JUnit4多线程执行测试用例](http://blog.csdn.net/neven7/article/details/45555687) 



