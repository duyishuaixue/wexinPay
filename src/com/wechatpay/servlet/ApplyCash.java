package com.wechatpay.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wechatpay.util.CommonUtil;
import com.wechatpay.util.MPConfigUtils;

/**
 * 用户申请提现调用的
 * @author zhangWenchao
 * @createTime 2016-10-22 15:55
 */
public class ApplyCash extends HttpServlet {
	
	private static final long serialVersionUID = -5434591618203942088L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException  {
		    String code = request.getParameter("code"); //这个code是微信调用这个servlet传过来的
		    /**
			 * 第１步：通过code获取openId
			 */
			String openid = CommonUtil.getOpenIdByCode(code);
			String nonceStr = UUID.randomUUID().toString().replace("-",""); //随机字符串
			SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
	        parameters.put("mch_appid", MPConfigUtils.APPID);
	        parameters.put("mchid", MPConfigUtils.MCH_ID);
	         //随机字符串  随机字符串，不长于32位。推荐随机数生成算法
	        parameters.put("nonce_str", nonceStr);
	        //这个订单号用于和微信通信的订单号，可以是某个商品的订单号，可以是区分某一类的订单号，保证唯一就行
	        String orderNo = new Random().nextInt(10)+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	        parameters.put("openid", openid);
	        parameters.put("partner_trade_no", orderNo);
	        parameters.put("check_name", "FORCE_CHECK");//强制要求检察实名认证
	        parameters.put("re_user_name", "xxx");//收款用户名（这里必须要填写提现人的真实姓名）
	        String money = "100";
	        parameters.put("amount", money);//金额，以分为单位（提现至少为 1元）
	        parameters.put("desc", "用户申请提现");//操作信息说明
	        parameters.put("spbill_create_ip", CommonUtil.getIpAddr(request));//操作ip地址
	        String sign = CommonUtil.createSign("UTF-8", parameters);
	        parameters.put("sign", sign);
	        String xmlStr = CommonUtil.getHttpsTransfer(parameters);
			System.out.println(" 微信返回的参数：  =====>\r\n  "+xmlStr);
	        try {
	        	Map<String, String> map = CommonUtil.parseXml(xmlStr);
	        	if("SUCCESS".equals(map.get("return_code")) && "SUCCESS".equals(map.get("result_code"))){
	        		String wxOrderNo = map.get("payment_no"); //属于微信的订单号
	        		String partnerNo = map.get("partner_trade_no"); //商户交易订单号
	        		String succesTime = map.get("payment_time"); //微信企业付款成功时间
	        		String sucMoney = money; //微信企业付款成功时间
	        		request.setAttribute("partner_trade_no", partnerNo);
	        		request.setAttribute("payment_time", succesTime);
	        		request.setAttribute("sucMoney", sucMoney);
	        		request.setAttribute("PayStatus", "SUCCESS");
	        	}else{
	        		request.setAttribute("PayStatus", "FAIL");
	        		request.setAttribute("return_msg", map.get("return_msg"));
	        	}
			} catch (Exception e) {
				request.setAttribute("PayStatus", "FAIL");
				e.printStackTrace();
			}
	       request.getRequestDispatcher("/weixin_pay/apply_cash_suscces.jsp").forward(request, response);
	}
	
	/**
	 * 封装xml数据
	 * @param parameters
	 * @return
	 */
    public static String getRequestXml(SortedMap<Object,Object> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            sb.append("<"+k+">"+v+"</"+k+">");
        }
        sb.append("</xml>");
        return sb.toString();
    }
    
}
