<%@page import="com.wechatpay.util.CommonUtil"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <%
	 BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
	 String line = null;
	 StringBuilder sb = new StringBuilder();
	 while((line = br.readLine())!=null){
	     sb.append(line);
	 }
	 //sb为微信返回的xml
	 System.out.println(" notify_url.jsp界面， 微信返回的： "+sb.toString());
	 
	//告诉微信服务器，我收到信息了，不要在调用回调 
	 response.getWriter().write(CommonUtil.setXML("SUCCESS", ""));
 %>
