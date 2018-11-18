package com.renke.tool;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Z.R.K
 * @description
 * @create 2018-10-25 10:53:59
 **/
public class MyApacheClient {
	//	static PoolingHttpClientConnectionManager poolManage =
//			new PoolingHttpClientConnectionManager();
	public static String doGet(String url) {
		//创建默认的httpClient实例
		CloseableHttpClient httpClient = getHttpClient();
		try {
			//用get方法发送http请求
			HttpGet get = new HttpGet(url);
			CloseableHttpResponse httpResponse = null;
			//发送get请求
			httpResponse = httpClient.execute(get);
			try {
				//response实体
				HttpEntity entity = httpResponse.getEntity();
				if (null != entity) {
					return EntityUtils.toString(entity);
				}
			} finally {
				httpResponse.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				closeHttpClient(httpClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static HttpEntity getEntity(String url) {
		//创建默认的httpClient实例
		CloseableHttpClient httpClient = getHttpClient();
		try {
			//用get方法发送http请求
			HttpGet get = new HttpGet(url);
			CloseableHttpResponse httpResponse = null;
			//发送get请求
			httpResponse = httpClient.execute(get);
			try {
				//response实体
				StatusLine status = httpResponse.getStatusLine();
				HttpEntity entity = httpResponse.getEntity();
				if (null != entity && status.getStatusCode() == 200) {
					return entity;
				}
			} finally {
				httpResponse.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				closeHttpClient(httpClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * POST方式发起http请求
	 */
	public String doPost(String url, Map<String, String> param) {
		CloseableHttpClient httpClient = getHttpClient();
		try {
			HttpPost post = new HttpPost(url);          //这里用上本机的某个工程做测试
			//创建参数列表
			List<NameValuePair> list = new ArrayList<>();
			for (Map.Entry<String, String> entry : param.entrySet()) {
				list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			//url格式编码
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(uefEntity);
			//执行请求
			CloseableHttpResponse httpResponse = httpClient.execute(post);
			try {
				HttpEntity entity = httpResponse.getEntity();
				if (null != entity) {
					return EntityUtils.toString(entity);
				}
			} finally {
				httpResponse.close();
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				closeHttpClient(httpClient);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * POST方式发起http请求
	 */
	public static HttpEntity postEntity(String url, Map<String, String> param) {
		CloseableHttpClient httpClient = getHttpClient();
		try {
			HttpPost post = new HttpPost(url);          //这里用上本机的某个工程做测试
			//创建参数列表
			List<NameValuePair> list = new ArrayList<>();
			for (Map.Entry<String, String> entry : param.entrySet()) {
				list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			//url格式编码
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(uefEntity);
			//执行请求
			CloseableHttpResponse httpResponse = httpClient.execute(post);
			try {
				StatusLine status = httpResponse.getStatusLine();
				HttpEntity entity = httpResponse.getEntity();
				if (null != entity && status.getStatusCode() == 200) {
					return entity;
				}
			} finally {
				httpResponse.close();
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				closeHttpClient(httpClient);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static CloseableHttpClient getHttpClient() {
		return HttpClients.createDefault();
	}
	
	private static void closeHttpClient(CloseableHttpClient client) throws IOException {
		if (client != null) {
			client.close();
		}
	}
	
	public static String tryAccessUrl(HTTPMethod method, String url, Map<String, String> param) {
		if (HTTPMethod.GET.equals(method)) {
			for (int i = 0; i < 5; i++) {
				HttpEntity entity = getEntity(url);
				if (entity != null) {
					return entity.toString();
				}
			}
		} else {
			for (int i = 0; i < 5; i++) {
				HttpEntity entity = postEntity(url, param);
				if (entity != null) {
					return entity.toString();
				}
			}
		}
		return "fail";
	}
	
	public static void main(String[] args) {
		String url = "http://192.168.20.124:10601";
		Map<String, String> parameter = new HashMap<>();
		parameter.put("data", "{\"groupId\":123}");
		parameter.put("m", "getGroupInfo");
//		MyApacheClient client = new MyApacheClient();
//		String result = client.doPost(url, parameter);
		//
		HttpEntity result = MyApacheClient.postEntity(url, parameter);
		System.out.println(result.getContentType().getElements());
//		String params = parameter.toString();
//		params = params.substring(1, params.length() - 1).replace(", ", "&");
//		String result = HttpClient.doPost(url, params);
		System.out.println(result);
	}
}
