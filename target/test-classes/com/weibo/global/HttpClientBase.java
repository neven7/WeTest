package com.weibo.global;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;



public class HttpClientBase {
	private static final String HTTP = "http";
	private static final String  HTTPHEADER = "http://";

	/**
	 * 
	 * @param username
	 * @param password
	 * @param relativeurl
	 * @return
	 * @throws Exception
	 */
	public String doGet(String username, String password, String relativeurl,
			String parameters)
			throws Exception {
		if(relativeurl == null || relativeurl.equals(""))
			return null;
		if(parameters.indexOf("source=") < 0) {
			//parameters += "&source=" + ParseProperties.getSystemProperty("source");
		parameters += "&source=*******";
		}
		String url = HTTPHEADER + ParseProperties.getSystemProperty("host") + ":" + ParseProperties.getSystemProperty("port") + relativeurl + "?" + parameters;
		
		HttpGet httpget = new HttpGet(url);
		HttpHost targetHost = new HttpHost(httpget.getURI().getHost(), httpget.getURI().getPort(), HTTP);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(targetHost.getHostName(),targetHost.getPort()), new UsernamePasswordCredentials(username, password));
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProvider).build();
		JSONObject json = null;
		String jsonstring = "", line = null;
		try {
			// Create AuthCache instance
			AuthCache authCache = new BasicAuthCache();
			// Generate BASIC scheme object and add it to the local
			// auth cache
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(targetHost, basicAuth);
			// Add AuthCache to the execution context
			HttpClientContext localContext = HttpClientContext.create();
			localContext.setAuthCache(authCache);
			for (int i = 0; i < 1; i++) {
				CloseableHttpResponse response = httpclient.execute(targetHost,
						httpget, localContext);
				try {
					HttpEntity entity = response.getEntity();

					if (entity != null) {
						InputStream in = (InputStream) entity.getContent();
						InputStreamReader ris = new InputStreamReader(in,
								"utf-8");
						BufferedReader br = new BufferedReader(ris);

						while ((line = br.readLine()) != null) {
							jsonstring += line;
						}
					}
					EntityUtils.consume(entity);
					System.out.println("curl -u \"" + username+ ":" + password + "\"  \"" + url + "\"");
					System.out.println(jsonstring);
					System.out.println("-----------------------------------------------");
				} finally {
					response.close();
				}
			}
		} finally {
			httpclient.close();
		}
		return jsonstring;
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @param relativeurl
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String username, String password, String relativeurl,
			String parameters,String host)
			throws Exception {
		if(relativeurl == null || relativeurl.equals(""))
			return null;
		if(parameters.indexOf("source=") < 0) {
			//parameters += "&source=" + ParseProperties.getSystemProperty("source");
			parameters += "&source=";
		}
		String url = HTTPHEADER +host + relativeurl + "?" + parameters;
		
		HttpGet httpget = new HttpGet(url);
		HttpHost targetHost = new HttpHost(httpget.getURI().getHost(), httpget
				.getURI().getPort(), HTTP);

		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(targetHost.getHostName(),
				targetHost.getPort()), new UsernamePasswordCredentials(
				username, password));
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProvider).build();
		JSONObject json = null;
		String jsonstring = "", line = null;
		try {
			// Create AuthCache instance
			AuthCache authCache = new BasicAuthCache();
			// Generate BASIC scheme object and add it to the local
			// auth cache
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(targetHost, basicAuth);
			// Add AuthCache to the execution context
			HttpClientContext localContext = HttpClientContext.create();
			localContext.setAuthCache(authCache);
			for (int i = 0; i < 1; i++) {
				CloseableHttpResponse response = httpclient.execute(targetHost,
						httpget, localContext);
				try {
					HttpEntity entity = response.getEntity();

					if (entity != null) {
						InputStream in = (InputStream) entity.getContent();
						InputStreamReader ris = new InputStreamReader(in,
								"utf-8");
						BufferedReader br = new BufferedReader(ris);

						while ((line = br.readLine()) != null) {
							jsonstring += line;
						}
					}
					EntityUtils.consume(entity);
					System.out.println("curl -u \"" + username+ ":" + password + "\"  \"" + url + "\"");
					System.out.println(jsonstring);
					System.out.println("-----------------------------------------------");
				} finally {
					response.close();
				}
			}
		} finally {
			httpclient.close();
		}
		return jsonstring;
	}
	/**
	 * 
	 * @param username
	 * @param password
	 * @param relativeurl
	 * @param parameters：post参数
	 * @return
	 * @throws Exception
	 */
	public String doPost(String username, String password, String relativeurl,
			String parameters) throws Exception {
		if(relativeurl == null || relativeurl.equals(""))
			return null;
		String source = null;
	
		if(parameters.indexOf("source=") < 0 ){
			source = "&source=" + ParseProperties.getSystemProperty("source");
		}
		 
		String localurl = HTTPHEADER + ParseProperties.getSystemProperty("host") + ":" + ParseProperties.getSystemProperty("port") + relativeurl + "?" + source;
		
		HttpPost httppost = new HttpPost(localurl);
		HttpHost targetHost = new HttpHost(httppost.getURI().getHost(), httppost
				.getURI().getPort(), HTTP);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(targetHost.getHostName(),
				targetHost.getPort()), new UsernamePasswordCredentials(
				username, password));
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProvider).build();
		String jsonstring = "", line = null;
		StringEntity reqEntity = new StringEntity(parameters,"UTF-8");
		reqEntity.setContentType("application/x-www-form-urlencoded");
		httppost.setEntity(reqEntity);
		try {
			// Create AuthCache instance
			AuthCache authCache = new BasicAuthCache();
			// Generate BASIC scheme object and add it to the local
			// auth cache
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(targetHost, basicAuth);
			// Add AuthCache to the execution context
			HttpClientContext localContext = HttpClientContext.create();
			localContext.setAuthCache(authCache);
			for (int i = 0; i < 1; i++) {
				CloseableHttpResponse response = httpclient.execute(targetHost,
						httppost, localContext);
				try {
					HttpEntity entity = response.getEntity();

					if (entity != null) {
						InputStream in = (InputStream) entity.getContent();
						InputStreamReader ris = new InputStreamReader(in,
								"utf-8");
						BufferedReader br = new BufferedReader(ris);

						while ((line = br.readLine()) != null) {
							jsonstring += line;
						}
						
					}
					EntityUtils.consume(entity);
					System.out.println("curl -u \"" + username+ ":" + password + "\"  \"" + localurl + "\" -d \"" + parameters + "\"");
					System.out.println(jsonstring);
					System.out
							.println("-----------------------------------------------");
				} finally {
					response.close();
				}
			}
		} finally {
			httpclient.close();
		}
		return jsonstring; 
	}
	/**
	 * 
	 * @param username
	 * @param password
	 * @param relativeurl
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public String doPost(String username, String password, String relativeurl,
			String parameters,String hosts) throws Exception {
		if(relativeurl == null || relativeurl.equals(""))
			return null;
		String source = null;
		if(parameters.indexOf("source=") < 0)
		 source = "&source=" + ParseProperties.getSystemProperty("source");
		
		String localurl = HTTPHEADER + hosts + relativeurl + "?" + source;
		HttpPost httppost = new HttpPost(localurl);
		HttpHost targetHost = new HttpHost(httppost.getURI().getHost(), httppost
				.getURI().getPort(), HTTP);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(targetHost.getHostName(),
				targetHost.getPort()), new UsernamePasswordCredentials(
				username, password));
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProvider).build();
		String jsonstring = "", line = null;
		StringEntity reqEntity = new StringEntity(parameters,"UTF-8");
		reqEntity.setContentType("application/x-www-form-urlencoded");
		httppost.setEntity(reqEntity);
		try {
			// Create AuthCache instance
			AuthCache authCache = new BasicAuthCache();
			// Generate BASIC scheme object and add it to the local
			// auth cache
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(targetHost, basicAuth);
			// Add AuthCache to the execution context
			HttpClientContext localContext = HttpClientContext.create();
			localContext.setAuthCache(authCache);
			for (int i = 0; i < 1; i++) {
				CloseableHttpResponse response = httpclient.execute(targetHost,
						httppost, localContext);
				try {
					HttpEntity entity = response.getEntity();

					if (entity != null) {
						InputStream in = (InputStream) entity.getContent();
						InputStreamReader ris = new InputStreamReader(in,
								"utf-8");
						BufferedReader br = new BufferedReader(ris);

						while ((line = br.readLine()) != null) {
							jsonstring += line;
						}
						
					}
					EntityUtils.consume(entity);
					System.out.println("curl -u \"" + username+ ":" + password + "\"  \"" + localurl + "\" -d \"" + parameters + "\"");
					System.out.println(jsonstring);
					System.out
							.println("-----------------------------------------------");
				} finally {
					response.close();
				}
			}
		} finally {
			httpclient.close();
		}
		return jsonstring; 
	}
}
