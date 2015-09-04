package com.weibo.global;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ParseProperties {

	private static final String SYSTEMPROPERTY = "../../../global.properties";
	private static  String HOST = null;
	private static  String PORT = null;
	private static  String SOURCE = null;
	// add retry 
	private static  String RETRY = null;
			
	private static Properties enc= null;
	private static InputStream ins = null;
	static {
		try {
			enc = new Properties();
			ins = ParseProperties.class
					.getResourceAsStream(SYSTEMPROPERTY);
			enc.load(ins);
			HOST = enc.getProperty("host");
			SOURCE = enc.getProperty("source");
			PORT = enc.getProperty("port");
			RETRY = enc.getProperty("retry");
		
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);

		}finally
		{
			try {
				ins.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 根据属性文件和key得到属性的value
	 * 
	 * @param fileRelativePath
	 * @param propertyName
	 * @return
	 * @throws Exception
	 */
	public static String getProperty(String fileRelativePath,
			String propertyName) throws Exception {
		if (fileRelativePath == null || fileRelativePath.equals("")
				|| propertyName == null || propertyName.equals(""))
			throw new Exception("传入参数为空！");
		InputStream ins = ParseProperties.class
				.getResourceAsStream(fileRelativePath);
		if (ins == null)
			throw new Exception("系统配置文件global.peroperties文件不存在！");
		Properties p = new Properties();
		String value = "";
		try {
			p.load(ins);
			value = p.getProperty(propertyName);
		} catch (Exception ex) {
			throw new Exception("读取属性文件异常");
		} finally {
			ins.close();
		}
		return value;

	}

	/**
	 * 根据key得到系统属性的value
	 * 
	 * @param propertyName
	 * @throws Exception
	 */
	public static String getSystemProperty(String propertyName)
			throws Exception {
		if (propertyName == null || propertyName.equals(""))
			throw new Exception("传入参数为空！");
		if(propertyName.equals("port"))
			return PORT;
		else if(propertyName.equals("host"))
			return HOST;
		else if(propertyName.equals("source"))
			return SOURCE;
		else if(propertyName.equals("retry"))
			return RETRY;
		return null;
	}

	public static void main(String args[]) {
		try {
			System.out.println("host======"+getSystemProperty("host"));
			System.out.println("port======"+getSystemProperty("port"));
			System.out.println("source===="+getSystemProperty("source"));
			System.out.println("retry====" + getSystemProperty("retry"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}