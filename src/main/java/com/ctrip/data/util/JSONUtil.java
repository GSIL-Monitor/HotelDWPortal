package com.ctrip.data.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * JSON数据处理工具类
 */
public class JSONUtil {
	/**
	 * 实体类型的对象
	 */
	public static final int OBJECT_TYPE_BEAN = 1;

	/**
	 * 集合类型对象
	 */
	public static final int OBJECT_TYPE_LIST = 2;

	/**
	 * 方法名称：writeJsonToResponse 内容摘要：向客户端写入JSON字符串流
	 * 
	 * @param response
	 *            HTTP响应
	 * @param object
	 *            待写入的对象
	 * @param objType
	 *            待写入的对象类型
	 * @return
	 */
	public static boolean writeJsonToResponse(HttpServletResponse response, Object object, int objType) {
		boolean flag = true;
		if (object == null) {
			System.out.println("待写入的对象为空");
			return false;
		}
		response.setContentType("text/xml; charset=UTF-8");
		try {
			JSON json = null;
			if (objType == OBJECT_TYPE_BEAN) {
				json = JSONObject.fromObject(object);
			} else if (objType == OBJECT_TYPE_LIST) {
				json = JSONArray.fromObject(object);
			} else {
				System.out.println("待写入实体的对象类型不正确");
				return false;
			}
			String responseText = json.toString();
			// Debug.println(json.toString());
			response.getWriter().write(responseText);
		} catch (IOException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 方法名称：parseJsonObjectToBean 内容摘要：将一个JSON对象转换成指定类型的Bean
	 * 
	 * @param json
	 *            任意实体，包括Json格式字符串
	 * @param clazz
	 *            需要转换的bean的Class
	 * @return
	 */
	public static Object parseJsonObjectToBean(Object json, Class clazz) {
		Object bean = null;
		try {
			JSONObject jsonObject = JSONObject.fromObject(json);
			bean = JSONObject.toBean(jsonObject, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 方法名称：parseJsonArrayToBean 内容摘要：将一个JSON对象转换成指定类型的Bean集合
	 * 
	 * @param json
	 *            任意实体，包括Json格式字符串
	 * @param clazz
	 *            需要转换的bean的Class
	 * @return
	 */
	public static List<Object> parseJsonArrayToBean(Object json, Class clazz) {
		List<Object> list = new LinkedList<Object>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(json);

			Object[] beans = jsonArray.toArray();

			Object bean = null;
			for (int i = 0; i < beans.length; i++) {
				bean = parseJsonObjectToBean(beans[i], clazz);
				list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 方法名称：getJsonString 内容摘要：将对象转换为JSON字符串
	 * 
	 * @param object
	 * @param objType
	 * @return
	 */
	public static String getJsonString(Object object, int objType) {
		JSON json = null;
		try {
			json = null;

			if (objType == OBJECT_TYPE_BEAN) {
				json = JSONObject.fromObject(object);
			} else if (objType == OBJECT_TYPE_LIST) {
				json = JSONArray.fromObject(object);
			} else {
				return "待写入实体的对象类型不正确";
			}
		} catch (Exception e) {
			return "转换JSON字符串出错";
		}

		return json.toString();
	}
	public static void writeStirngJsonToResponse(HttpServletResponse response, String result){
		try {
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.println(result);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  @param jsonArr json数组
	 */
	public static JSONArray sort(JSONArray jsonArr){
		JSONArray sortedJsonArray = new JSONArray();

		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonArr.size(); i++) {
			jsonValues.add(jsonArr.getJSONObject(i));
		}
		Collections.sort( jsonValues, new Comparator<JSONObject>() {
			//You can change "Name" with "ID" if you want to sort by ID
			private static final String KEY_NAME = "table_name";
			public int compare(JSONObject a, JSONObject b) {
				String valA = new String();
				String valB = new String();

				try {
					valA = (String) a.get(KEY_NAME);
					valB = (String) b.get(KEY_NAME);
				}
				catch (JSONException e) {
					//do something
				}

				return valA.compareTo(valB);
				//if you want to change the sort order, simply use the following:
				//return -valA.compareTo(valB);
			}
		});
		//System.out.println(jsonArr);
		for (int i = 0; i < jsonArr.size(); i++) {
			sortedJsonArray.add(jsonValues.get(i));
		}
		//System.out.println(sortedJsonArray);
		return sortedJsonArray;
	}

	public static void main(String[] args) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", "succ");
		JSON json1 = JSONObject.fromObject(jsonObject);
		System.out.println(json1);
	}

}