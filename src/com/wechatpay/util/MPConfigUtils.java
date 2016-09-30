package com.wechatpay.util;

public class MPConfigUtils {
	
	public static String APPID = "wxd225550ca5fc04db";
	
	public static String APPSECRET = "680990ae9ba761ffa09fd93f55cc3c61";
	
	public static String MCH_ID = "1254635501"; //商户号
	
	public static String API_KEY = "87F6C49ACBC3F8F253633014208F7756"; //API密钥
	
	//API密钥
	public static String OAUTH2_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	
	//获取code的URL
	public static String GETCODEURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
	
	//微信访问我们系统重定向的系统地址
	public static String REDIRECT_URI = "http://test.app88.cn/weixinPay/topayServlet";
	
	// notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
	public static String NOTIFY_URL = "http://test.app88.cn/weixinPay/weixin_pay/notify_url.jsp";
	
	public static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder"; //统一下单地址
	
}
