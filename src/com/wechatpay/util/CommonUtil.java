package com.wechatpay.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import net.sf.json.JSONObject;

/**
 * 通用工具类
 * 
 * @author zhangwenchao
 * @CreateDate 2016-09-26:20:29
 */
public class CommonUtil {

	public static JSONObject httpsRequestToJsonObject(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		try {
			StringBuffer buffer = httpsRequest1(requestUrl, requestMethod,
					outputStr);
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			System.out.println("连接超时：" + ce.getMessage());
		} catch (Exception e) {
			System.out.println("https请求异常：" + e.getMessage());
		}
		return jsonObject;
	}

	private static StringBuffer httpsRequest1(String requestUrl,
			String requestMethod, String output)
			throws NoSuchAlgorithmException, NoSuchProviderException,
			KeyManagementException, MalformedURLException, IOException,
			ProtocolException, UnsupportedEncodingException {

		URL url = new URL(requestUrl);
		HttpsURLConnection connection = (HttpsURLConnection) url
				.openConnection();

		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod(requestMethod);
		if (null != output) {
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(output.getBytes("UTF-8"));
			outputStream.close();
		}

		// 从输入流读取返回内容
		InputStream inputStream = connection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		StringBuffer buffer = new StringBuffer();
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}

		bufferedReader.close();
		inputStreamReader.close();
		inputStream.close();
		inputStream = null;
		connection.disconnect();
		return buffer;
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param byteArray
	 * @return
	 */
	public static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	/**
	 * 将字节转换为十六进制字符串
	 * 
	 * @param btyes
	 * @return
	 */
	public static String byteToHexStr(byte bytes) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(bytes >>> 4) & 0X0F];
		tempArr[1] = Digit[bytes & 0X0F];

		String s = new String(tempArr);
		return s;
	}

	/**
	 * 获取ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			return request.getRemoteAddr();
		}
		byte[] ipAddr = addr.getAddress();
		String ipAddrStr = "";
		for (int i = 0; i < ipAddr.length; i++) {
			if (i > 0) {
				ipAddrStr += ".";
			}
			ipAddrStr += ipAddr[i] & 0xFF;
		}
		return ipAddrStr;
	}

	/**
	 * 第 ２步：同code获取openId
	 * 
	 * @param code
	 * @return
	 */
	public static String getOpenIdByCode(String code) {
		String openId = "";
		String oauth2_url = MPConfigUtils.OAUTH2_URL
				.replace("APPID", MPConfigUtils.APPID)
				.replace("SECRET", MPConfigUtils.APPSECRET)
				.replace("CODE", code);
		JSONObject jsonObject = CommonUtil.httpsRequestToJsonObject(oauth2_url,
				"POST", null);
		Object errorCode = jsonObject.get("errcode");
		if (errorCode != null) {
			System.out.println("code不合法");
		} else {
			openId = jsonObject.getString("openid");
			System.out.println("openId:" + openId);
		}
		return openId;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(String xml) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		List<Element> elementList = root.elements();
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}
		return map;
	}

	public static Map<String, String> httpsRequestToXML(String requestUrl,
			String requestMethod, String outputStr) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			StringBuffer buffer = httpsRequest1(requestUrl, requestMethod,
					outputStr);
			result = CommonUtil.parseXml(buffer.toString());
		} catch (ConnectException ce) {
			System.out.println("连接超时：" + ce.getMessage());
		} catch (Exception e) {
			System.out.println("https请求异常：" + e.getMessage());
		}
		return result;
	}

	/**
	 * /** 微信支付签名算法sign
	 * 
	 * @param characterEncoding
	 * @param parameters
	 * @return
	 */
	public static String createSign(String characterEncoding,
			SortedMap<Object, Object> parameters) {
		String API_KEY = MPConfigUtils.API_KEY;
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();// 所有参与传参的参数按照ASCII码从小到大排序（字典序）
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + API_KEY);
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding)
				.toUpperCase();
		return sign;
	}

	public static String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code
				+ "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}

	public static String getRequestXml(SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)
					|| "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 调用请求商户支付的接口,带密钥请求的,主要是用于商户提现
	 */
	public static String getHttpsTransfer(SortedMap<Object, Object> params) {
		String jsonStr = "";
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(new File(
					MPConfigUtils.CERT_PATH));
			keyStore.load(instream, MPConfigUtils.MCH_ID.toCharArray());//
			instream.close();
			SSLContext sslcontext = SSLContexts
					.custom()
					.loadKeyMaterial(keyStore,
							MPConfigUtils.MCH_ID.toCharArray()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslcontext,
					new String[] { "TLSv1" },
					null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom()
					.setSSLSocketFactory(sslsf).build();
			HttpPost httpost = new HttpPost(MPConfigUtils.TRANSFERS_URL);
			httpost.addHeader("Connection", "keep-alive");
			httpost.addHeader("Accept", "*/*");
			httpost.addHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=UTF-8");
			httpost.addHeader("Host", "api.mch.weixin.qq.com");
			httpost.addHeader("X-Requested-With", "XMLHttpRequest");
			httpost.addHeader("Cache-Control", "max-age=0");
			httpost.addHeader("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
			httpost.setEntity(new StringEntity(getRequestXml(params), "UTF-8"));
			CloseableHttpResponse res = httpclient.execute(httpost);
			HttpEntity entity = res.getEntity();
			jsonStr = EntityUtils.toString(res.getEntity(), "UTF-8");
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonStr;
	}
}