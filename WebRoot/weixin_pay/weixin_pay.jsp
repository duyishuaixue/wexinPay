<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setAttribute("ctx", request.getContextPath()); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>微信确认支付</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<link rel="stylesheet" href="${ctx}/css/weui.css"/>
<script type="text/javascript">
	function callpay(){
	     WeixinJSBridge.invoke('getBrandWCPayRequest',{
		 	"appId" : "${param.appId}",
		 	"timeStamp" : "${param.timeStamp}", 
		 	"nonceStr" : "${param.nonceStr}", 
		 	"package" : "${param.package1}",
		 	"signType" : "MD5", 
		 	"paySign" : "${param.signature}"
			},function(res){
			/* WeixinJSBridge.log(res.err_msg);
            alert(res.err_code + res.err_desc + res.err_msg); */
            if(res.err_msg == "get_brand_wcpay_request:ok"){  
                alert("微信支付成功!");
                window.location.href="${ctx}/weixin_pay/paySuccess.jsp?PayStatus=SUCCESS";
            }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
                alert("用户取消支付!");
                window.location.href="${ctx}/index.jsp";
            }else{
               alert("支付失败!");
               window.location.href="${ctx}/index.jsp";
            }  
		});
	}

</script>
</head>
<body>
	<div class="page__hd">
        <h3 class="page__title">微信确认支付</h3>
        <p class="page__desc">&nbsp;</p>
    </div>
	<div class="page">
    <div class="page__bd page__bd_spacing">
        <a href="javascript:;" onclick="callpay();" class="weui-btn weui-btn_primary">确认支付 1 分钱</a>
        <br/><br/>
    </div>
    </div>
</body>
</html>