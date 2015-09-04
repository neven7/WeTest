package com.weibo.common;

import java.util.List;

import org.junit.Ignore;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.weibo.global.HttpClientBase;
import com.weibo.global.JsonCommon;
import com.weibo.model.Status;


public class StatusCommon {
	Object status;
	Object statusCount;
	Object statuses;
	Object interest;
	Object lastTime;
	Object statusesIds;
	Object error;
	Object exposure;
	Object allowComment;
	Object userReadCount;
	Object darwinTags;
	HttpClientBase statusTest = new HttpClientBase();
	String relativeurl;
	String statusInfo;
	Object objectList;
	Object midObject;
	Object StatusBean;
	Object recomm;
	Object getExposureTrigger;
	Object getSimilarity;
	Object atTimeline;
	Object readMetas;
	Object phototags;
	boolean flag;



	public Object commonResult(String statusInfo) throws Exception {
//		if (statusInfo.contains("error_code")) {
//			error = (ErrorInfo) JsonCommon.getJavabean(statusInfo,
//					ErrorInfo.class);
//			MbLogger.error(statusInfo, new ExceptionCommon(statusInfo));
//			return error;
//		} else {
			status = (Status) JsonCommon.getJavabean(statusInfo, Status.class);
			return status;
//		}	
	}
	


	/*
	 * status feed (statuses/friends_timeline)
	 * 
	 * @param username
	 * 
	 * @param password
	 * 
	 * @param parameters required
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public Object friendsTimeline_status(String username, String password,
			String parameters) throws Exception {
		relativeurl = "/2/statuses/friends_timeline.json";
		statusInfo = statusTest.doGet(username, password, relativeurl, parameters);
		statuses = commonResult(statusInfo);
		return statuses;
	}

	
	public Object updateStatusPublic(String username, String password,
			String parameters) throws Exception {
		relativeurl = "/2/statuses/update.json";
		if (parameters.isEmpty()) {
			parameters = "visible=0&status=test public ";
		}
		statusInfo = statusTest.doPost(username, password, relativeurl,
				parameters);
		status = commonResult(statusInfo);

		return status;
	}

	
	







	
	
	
	
	




	


	



}
