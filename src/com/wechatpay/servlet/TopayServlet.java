package com.wechatpay.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wechatpay.util.CommonUtil;
import com.wechatpay.util.MPConfigUtils;

import net.sf.json.JSONObject;

/**
 * 这个类是微信那边请求调用这个类
 * @author zhangwenchao
 * @CreateDate 2016-09-25 08:30
 *
 */
public class TopayServlet extends HttpServlet {

	private static final long serialVersionUID = -4946802691127283340L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			//网页授权后获取传递的参数
			String orderNo = request.getParameter("orderNo");
			String code = request.getParameter("code");
			System.out.println("获取到的code是：  "+code +"  订单号是： "+orderNo);
			
			/**
			 * 第２步：通过code获取openId
			 */
			String openId = CommonUtil.getOpenIdByCode(code);
			/**
			 * 第３步：获取签名，把签名与PayInfo中的其他数据转成XML格式，当做参数传递给统一下单地址
			 */
			String nonceStr = UUID.randomUUID().toString().replace("-",""); //随机字符串
			//String ip = CommonUtil.getIpAddr(request);
			orderNo = new Random().nextInt(10)+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//商品订单号
			
			SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
			//公众账号ID  微信分配的公众账号ID（企业号corpid即为此appId）
	        parameters.put("appid", MPConfigUtils.APPID);
	         //商户号    微信支付分配的商户号
	        parameters.put("mch_id", MPConfigUtils.MCH_ID);
	         //随机字符串  随机字符串，不长于32位。推荐随机数生成算法
	        parameters.put("nonce_str", nonceStr);
	         //商品描述  商品或支付单简要描述
	        parameters.put("body", "Iphone7");
	        parameters.put("out_trade_no", orderNo);
	        parameters.put("total_fee", "1");//订单总金额
	        parameters.put("spbill_create_ip",request.getRemoteHost());
	        parameters.put("notify_url", MPConfigUtils.NOTIFY_URL);
	        Calendar start=Calendar.getInstance();
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
	        String time_start =sdf.format(start.getTimeInMillis());
	        //交易起始时间  订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
	        parameters.put("time_start", time_start);
	        //订单失效时间 订单失效时间为下单后30分钟，30分钟未支付则一律作废，如果有需求可以调整时间
	        Calendar expire=Calendar.getInstance();
	        int cont = 30;
	        expire.add(Calendar.MINUTE,cont);
	        String time_expire=sdf.format(expire.getTimeInMillis());
	         //交易结束时间   订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则注意：最短失效时间间隔必须大于5分钟
	        parameters.put("time_expire", time_expire);
	        //交易类型    取值如下：JSAPI，NATIVE，APP，WAP,详细说明见参数规定
	        parameters.put("trade_type", "JSAPI");
	         //用户标识  trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。
	        //openid如何获取，可参考【获取openid】
	        parameters.put("openid", openId);
	        String sign = CommonUtil.createSign("UTF-8", parameters);
	        parameters.put("sign", sign);
	        String requestXML = CommonUtil.getRequestXml(parameters);
			System.out.println("生成的签名是： "+sign);
			/**
			 * 第４步：调用统一下单地址
			 */
			Map<String, String> map = CommonUtil.httpsRequestToXML(MPConfigUtils.UNIFIED_ORDER_URL, "POST", requestXML.replace("__", "_").replace("<![CDATA[", "").replace("]]>", ""));
			/**
			 * 第５步：获取 prepay_Id 预支付ID
			 */
			String return_code = map.get("return_code");
		    if(return_code!=null && return_code.length()>0 && "SUCCESS".equals(return_code)){
		    	String return_msg = map.get("return_msg");
			     if(return_msg!=null && return_msg.length()>0 && !return_msg.equals("OK")) {
			    	 System.out.println("统一下单错误！return_msg的值为："+return_msg);
			     }
			 }else{
				 System.out.println("统一下单错误！return_code 为空或不为SUCCESS "+return_code);
			 }
			 String prepay_id = map.get("prepay_id");
			 
			 /**
			  * 第６步：跳转到我们系统的支付界面
			  * 再次生成签名
			  */
			 String userAgent = request.getHeader("user-agent");
			 char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger")+15);  
			 JSONObject json = new JSONObject();
	         if (prepay_id!=""||prepay_id!=null) {
	             //生成时间戳
	             String timestamp=Long.toString(new Date().getTime()).substring(0, 10);
	             SortedMap<Object,Object> params = new TreeMap<Object,Object>();
	             params.put("appId", MPConfigUtils.APPID);
	             params.put("timeStamp", timestamp);
	             params.put("nonceStr",  nonceStr);
	             params.put("package", "prepay_id="+prepay_id);
	             params.put("signType", "MD5");
	             String paySign =  CommonUtil.createSign("UTF-8", params);
	             params.put("packageValue", "prepay_id="+prepay_id);    //这里用packageValue是预防package是关键字在js获取值出错
	             params.put("paySign", paySign);                     //paySign的生成规则和Sign的生成规则一致
	             params.put("agent", agent);//微信版本号，用于前面提到的判断用户手机微信的版本是否是5.0以上版本。
	             json = JSONObject.fromObject(params);
	             request.setAttribute("json", json);
	         }else {
	        	 request.setAttribute("json", json);
	         }
			 String params = "appId="+MPConfigUtils.APPID+"&timeStamp="+json.getString("timeStamp")+"&nonceStr="+nonceStr+"&signature="+json.getString("paySign")+"&prepay_id="+prepay_id+"&package1="+json.getString("package");
			 response.sendRedirect(request.getContextPath()+"/weixin_pay/weixin_pay.jsp?"+params);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath()+"/weixin_pay/weixin_pay.jsp");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
