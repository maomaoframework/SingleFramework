package huxg.framework.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HttpUtils {
	public static String postJson(String url, JSONObject jo) throws Exception {
		String content = jo.toString();
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httpPost = null;
		try {

			httpPost = new HttpPost(url);
			StringEntity stringEntity = new StringEntity(content, "utf-8");
			httpPost.addHeader("Content-Type", "text/xml");
			httpPost.setEntity(stringEntity);

			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String returnVal = EntityUtils.toString(entity);
			return returnVal;
		} finally {
			// System.out.println("ok!");
			if (httpPost != null)
				httpPost.releaseConnection();
		}
	}

	/**
	 * 向一个URL提交字符串内容
	 * 
	 * @param url
	 * @param content
	 */
	public static String postString(String url, String content) throws Exception {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httpPost = null;
		try {

			httpPost = new HttpPost(url);
			StringEntity stringEntity = new StringEntity(content, "utf-8");
			httpPost.addHeader("Content-Type", "text/xml");
			httpPost.setEntity(stringEntity);

			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String returnVal = EntityUtils.toString(entity);
			return returnVal;
		} finally {
			// System.out.println("ok!");
			if (httpPost != null)
				httpPost.releaseConnection();
		}
	}

	/**
	 * post请求，返回JSONObject
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static JSONObject post4JsonObject(String url, Map<String, String> params) throws Exception {
		String returnVal = post4String(url, params);
		JSONObject jo = (JSONObject) JSONObject.parse(returnVal);
		return jo;
	}

	/**
	 * Post请求，返回JSONArray
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static JSONArray post4JsonArray(String url, Map<String, String> params) throws Exception {
		String returnVal = post4String(url, params);
		JSONArray jo = (JSONArray) JSONArray.parse(returnVal);
		return jo;
	}

	/**
	 * POST请求，返回String
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String post4String(String url, Map<String, String> params) throws Exception {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httpPost = null;
		try {

			httpPost = new HttpPost(url);
			if (params != null && params.size() > 0) {
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				String key, value;
				for (Iterator<String> keys = params.keySet().iterator(); keys.hasNext();) {
					key = keys.next();
					value = params.get(key);
					nvps.add(new BasicNameValuePair(key, value));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
			}

			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String returnVal = EntityUtils.toString(entity);
			return returnVal;
		} finally {
			// System.out.println("ok!");
			if (httpPost != null)
				httpPost.releaseConnection();
		}
	}

	/**
	 * GET请求，返回String
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static JSONObject get4JsonObject(String url, Map<String, String> params) throws Exception {
		String returnVal = get4String(url, params);
		JSONObject jo = (JSONObject) JSONObject.parse(returnVal);
		return jo;
	}

	/**
	 * Post请求，返回JSONArray
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static JSONArray get4JsonArray(String url, Map<String, String> params) throws Exception {
		String returnVal = get4String(url, params);
		JSONArray jo = (JSONArray) JSONArray.parse(returnVal);
		return jo;
	}

	/**
	 * GET请求，返回String
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String get4String(String url, Map<String, String> params) throws Exception {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpGet httpGet = null;
		try {

			String composedUrl = url;
			for (Iterator<String> keys = params.keySet().iterator(); keys.hasNext();) {
				String key = keys.next();
				String value = params.get(key);
				if (composedUrl.contains("?")) {
					composedUrl = composedUrl + "&" + key + "=" + value;
				} else {
					composedUrl = composedUrl + "?" + key + "=" + value;
				}
			}
			httpGet = new HttpGet(composedUrl);
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String returnVal = EntityUtils.toString(entity);
			return returnVal;
		} finally {
			if (httpGet != null)
				httpGet.releaseConnection();
		}
	}

	public static String fillUrlParams(String url, String[] values) {
		int i = 0;
		String s = url;
		int idx = 0;
		while ((idx = s.indexOf("[?]")) != -1) {
			s = s.substring(0, idx) + values[i] + s.substring(idx + 3);
			i++;
		}
		return s;
	}

	public static String post2WebService(String url, String xml) throws Exception {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httpPost = null;
		try {

			httpPost = new HttpPost(url);
			StringEntity stringEntity = new StringEntity(xml, "utf-8");
			httpPost.addHeader("Content-Type", "text/xml; charset=UTF-8");
			httpPost.addHeader("SOAPAction", "http://tempuri.org/SendSMS");
			httpPost.setEntity(stringEntity);

			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String returnVal = EntityUtils.toString(entity);
			return returnVal;
		} finally {
			if (httpPost != null)
				httpPost.releaseConnection();
		}
	}

	public static void main(String[] args) throws Exception {
		String url = "http://test2.ndchina.cn/api/bs3webservice.asmx";
		StringBuffer sb = new StringBuffer();
		sb.append("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><SendSMS xmlns=\"http://tempuri.org/\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><UserName>HJSystem</UserName><UserPsw>HJSystem</UserPsw><AccountType>2</AccountType><AccountInfo>恒江</AccountInfo><SchTime/><SMSMobile>13720003175</SMSMobile><SMSContent>【民生恒江】test</SMSContent></SendSMS></s:Body></s:Envelope>");
		String str = HttpUtils.post2WebService(url, sb.toString());
		System.out.println(str);
	}

}
