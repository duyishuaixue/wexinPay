package com.wechatpay.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wechatpay.util.MPConfigUtils;

/**
 * 调用这个servlet用于支付
 * @author zhangWenchao
 *
 */
public class MainServlet extends HttpServlet {

	private static final long serialVersionUID = 3680084802350647841L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 第1步、 获取code
		 */
	    String getCodeUrl = MPConfigUtils.GETCODEURL
	    		.replace("APPID", MPConfigUtils.APPID)
	    		.replace("REDIRECT_URI",  URLEncoder.encode(MPConfigUtils.REDIRECT_URI, "UTF-8"));
	    String code = request.getParameter("code");
	    if(code==null){
	        System.out.println("code为空，跳转，url=  "+getCodeUrl);
	        response.sendRedirect(getCodeUrl);
	    }else{
	        System.out.println("已获取到code，值为："+code);
	    }
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
