<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>微信确认支付</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
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
			WeixinJSBridge.log(res.err_msg);
            alert(res.err_code + res.err_desc + res.err_msg);
            if(res.err_msg == "get_brand_wcpay_request:ok"){  
                alert("微信支付成功!");  
            }else if(res.err_msg == "get_brand_wcpay_request:cancel"){  
                alert("用户取消支付!");  
            }else{  
               alert("支付失败!");  
            }  
		});
	}

</script>
</head>
<body>
	<center>
		<h1>您应支付 [1] 分钱，请确认：<br/>
			<input type="button" onclick="callpay()" value="确认支付"/>
		</h1>
	</center>
</body>
</html>