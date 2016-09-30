<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支付成功</title>
</head>
<body>
 <%
	 BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
	 String line = null;
	 StringBuilder sb = new StringBuilder();
	 while((line = br.readLine())!=null){
	     sb.append(line);
	 }
	 //sb为微信返回的xml
	 System.out.println(" notify_url.jsp界面， 微信返回的： "+sb.toString());
 %>
 <h1>
 	<center>
 		恭喜您，支付成功啦！
 	</center>
 </h1>
</body>
</html>