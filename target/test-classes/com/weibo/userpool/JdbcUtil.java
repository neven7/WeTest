package com.weibo.userpool;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbutils.DbUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * 
 * jdbc 使用C3PO连接池 获得Connection
 * @author hugang
 *
 */
public class JdbcUtil {
	private static Properties enc;
	private static InputStream in;
	private static ComboPooledDataSource ds = new ComboPooledDataSource();
	static {
		try {
			enc = new Properties();
			in = JdbcUtil.class.getResourceAsStream("../../../c3p0.properties");
			enc.load(in);
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		} 
		finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	static {
		try {
			ds.setDriverClass(enc.getProperty("c3p0.driverClass"));
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ds.setUser(enc.getProperty("c3p0.user"));
		ds.setPassword(enc.getProperty("c3p0.password"));
		ds.setJdbcUrl(enc.getProperty("c3p0.jdbcUrl"));
	}

	public static Connection getConnection() {

		try {
			
			return ds.getConnection();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void close(ResultSet rs, Statement st, Connection con) {
		DbUtils.closeQuietly(rs);
		DbUtils.closeQuietly(st);
		DbUtils.closeQuietly(con);
	}

}
