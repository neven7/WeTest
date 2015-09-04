package com.weibo.global;


import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


//import com.alibaba.fastjson.JSON;

public class JsonCommon {
	 // jackSon 反序列化 java对象
	public static Object getJavabean(String jsonString, Class classes)
			throws JsonParseException, JsonMappingException, IOException {
		if (jsonString == null || jsonString.equals("") || classes == null)
			return null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// System.out.println("---------------------------"+classes.getName());
		Object myObject = mapper.readValue(jsonString, classes);
		return myObject;
	}

	/** @author yanlei3 
	 * write for the JSONobject with unset key
	 */
	public static Map<String, Object> getMap(String jsonString)
			throws JsonParseException, JsonMappingException, IOException {
		if (jsonString == null || jsonString.equals(""))
			return null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// System.out.println("---------------------------"+classes.getName());
		Map<String,Object> myMap = mapper.readValue(jsonString, Map.class);
		return myMap;
	}

	@JsonIgnore
	public static Object[] getJavabeans(String jsonString, Class classes)
			throws JsonParseException, JsonMappingException, IOException {
		if (jsonString == null || jsonString.equals("") || classes == null)
			return null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		Object[] myObject = (Object[]) mapper.readValue(jsonString, classes);
		return myObject;
	}

////使用fastjson 反序列为java 对象
//	@SuppressWarnings("unchecked")
//	public static Object getJavabean(String jsonString, @SuppressWarnings("rawtypes") Class classes) {
//		if (jsonString == null || jsonString.equals("") || classes == null)
//			return null;
////		ObjectMapper mapper = new ObjectMapper();
////		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
////				false);
////		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
////		// System.out.println("---------------------------"+classes.getName());
////		Object myObject = mapper.readValue(jsonString, classes);
////		return myObject;
//		Object myObject = JSON.parseObject(jsonString, classes);		
//		return myObject;
//	}
//
//	@SuppressWarnings("unchecked")
//	@JsonIgnore
//	public static Object[] getJavabeans(String jsonString, @SuppressWarnings("rawtypes") Class classes) {
//		if (jsonString == null || jsonString.equals("") || classes == null)
//			return null;
////		ObjectMapper mapper = new ObjectMapper();
////		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
////				false);
////		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//		Object[] myObject = (Object[]) JSON.parseObject(jsonString, classes);
//		return myObject;
//	}


	public static String writeEntity2Json(Object object) throws IOException {
		String jsonResult = null;
		ObjectMapper objectMapper = new ObjectMapper();
		jsonResult = objectMapper.writeValueAsString(object);
		return jsonResult;
	}

}