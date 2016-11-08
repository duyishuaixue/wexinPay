<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.wechatpay.util.MPConfigUtils"%>
<%request.setAttribute("ctx", request.getContextPath());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<link rel="stylesheet" href="${ctx}/css/weui.css"/>
<title>微信用户支付状态</title>
</head>
<body>
<div class="page">
    <div class="weui-msg">
    <c:if test="${param.PayStatus eq 'SUCCESS'}">
    	<div class="weui-msg__icon-area">
        	<i class="weui-icon-success weui-icon_msg"></i><!-- 成功提示图片 -->
        </div>
        <div class="weui-msg__text-area">
       		<h2 class="weui-msg__title">支付成功</h2>
            <p class="weui-msg__desc">
            	支付成功！
            </p>
        </div>
        <div class="weui-msg__opr-area">
            <p class="weui-btn-area">
                <a href="javascript:window.location.href='${ctx}/index.jsp;" class="weui-btn weui-btn_primary">完成</a>
            </p>
        </div>
    </c:if>
    
    <c:if test="${param.PayStatus ne 'SUCCESS'}">
    	<div class="weui-msg__icon-area">
        	<i class="weui-icon-warn weui-icon_msg"></i>
        </div>
        <div class="weui-msg__text-area">
       		<h2 class="weui-msg__title">支付失败</h2>
            <p class="weui-msg__desc">
            	支付失败
            </p>
        </div>
        <div class="weui-msg__opr-area">
            <p class="weui-btn-area">
                <a href="javascript:window.location.href='${ctx}/index.jsp';" class="weui-btn weui-btn_primary">返回重新支付</a>
            </p>
        </div>
    </c:if>
        <div class="weui-msg__extra-area">
            <div class="weui-footer">
                <p class="weui-footer__links">
                    <a href="javascript:void(0);" class="weui-footer__link">公司名称</a>
                </p>
                <p class="weui-footer__text">Copyright &copy; </p>
            </div>
        </div>
    </div>
</div>
</body>
</html>