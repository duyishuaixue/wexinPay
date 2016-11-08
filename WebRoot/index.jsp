<%@page import="java.net.URLEncoder"%>
<%@page import="com.wechatpay.util.MPConfigUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setAttribute("ctx", request.getContextPath());
String applyCashUrl = MPConfigUtils.GETCODEURL.replace("APPID", MPConfigUtils.APPID).replace("REDIRECT_URI",
					URLEncoder.encode(MPConfigUtils.REDIRECT_URI_CASH, "UTF-8"));
request.setAttribute("applyCashUrl", applyCashUrl);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<link rel="stylesheet" href="${ctx}/css/weui.css"/>
<title>微信支付与微信提现</title>
</head>
<body>
<div class="page">
	<div class="page__hd">
        <h3 class="page__title">微信支付与微信提现</h3>
        <p class="page__desc">&nbsp;</p>
    </div>
	<div class="page">
    <div class="page__bd page__bd_spacing">
        <a href="javascript:;" onclick="payMoney();" class="weui-btn weui-btn_primary">确认支付  0.01 元</a>
        <br/><br/>
        <a href="javascript:;" onclick="applyGetCast();" class="weui-btn weui-btn_primary">申请提现 1.00 元</a>
    </div>
    </div>
   
   <!-- 提示 --> 
   <div id="loadingToast" style="display:none;">
       <div class="weui-mask_transparent"></div>
       <div class="weui-toast">
           <i class="weui-loading weui-icon_toast"></i>
           <p class="weui-toast__content">加载中...</p>
       </div>
   </div>
</div>
    <script type="text/javascript">
    	//支付
    	function payMoney(){
    		loadS();
    		window.location.href="${ctx}/pay";
    	}
    	
    	//申请提现
    	function applyGetCast(){
    		loadS();
    		window.location.href="${applyCashUrl}";
    	}
    	
    	function loadS(){
    		var $loadingToast = $('#loadingToast');
    		if ($loadingToast.css('display') != 'none') return;
            $loadingToast.fadeIn(100);
            /* setTimeout(function () {
                $loadingToast.fadeOut(100);
            }, 5000); */
    	}
    </script>
</body>
</html>