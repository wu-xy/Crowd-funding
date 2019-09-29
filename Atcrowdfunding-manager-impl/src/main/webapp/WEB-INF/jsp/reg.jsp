<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
	<link rel="stylesheet" href="${APP_PATH }/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="${APP_PATH }/css/font-awesome.min.css">
	<link rel="stylesheet" href="${APP_PATH }/css/login.css">
	<style>

	</style>
  </head>
  <body>
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <div><a class="navbar-brand" href="index.html" style="font-size:32px;">尚筹网-创意产品众筹平台</a></div>
        </div>
      </div>
    </nav>

    <div class="container">

      <form class="form-signin" role="form">
        <h2 class="form-signin-heading"><i class="glyphicon glyphicon-log-in"></i> 用户注册</h2>
		  <div class="form-group has-success has-feedback">
			<input type="text" class="form-control" id="floginacct" name="loginacct" placeholder="请输入登录账号" autofocus>
			<span class="glyphicon glyphicon-user form-control-feedback"></span>
		  </div>
		  <div class="form-group has-success has-feedback">
			<input type="password" class="form-control" id="fuserpswd" name="userpswd" placeholder="请输入登录密码" style="margin-top:10px;">
			<span class="glyphicon glyphicon-lock form-control-feedback"></span>
		  </div>
		  <div class="form-group has-success has-feedback">
			<input type="text" class="form-control" id="femail" name="email" placeholder="请输入邮箱地址" style="margin-top:10px;">
			<span class="glyphicon glyphicon glyphicon-envelope form-control-feedback"></span>
		  </div>
		  <div class="form-group has-success has-feedback">
			<select class="form-control" id="fusertype" name="usertype" >
                <option value="1">企业</option>
                <option value="0">个人</option>
            </select>
		  </div>
        <div class="checkbox">
          <label>
            忘记密码
          </label>
          <label style="float:right">
            <a href="${APP_PATH }/login.htm">我有账号</a>
          </label>
        </div>
        <a class="btn btn-lg btn-success btn-block" onclick="doReg()" > 注册</a>
      </form>
    </div>
    <script src="${APP_PATH }/jquery/jquery-2.1.1.min.js"></script>
    <script src="${APP_PATH }/bootstrap/js/bootstrap.min.js"></script>
     <script>
    function doReg() {
    	var floginacct = $("#floginacct");
    	var fuserpswd = $("#fuserpswd");
    	var femail = $("#femail");
    	var fusertype = $("#fusertype");

    	
    	//对于表单数据而言不能用null进行判断,如果文本框什么都不输入,获取的值是""
    	if($.trim(floginacct.val()) == ""){
    		alert("用户账号不能为空,请重新输入!");
    		floginacct.val("");		//清空输入框的值
    		floginacct.focus();		//把光标重新定位回这个输入框
    		return false ;
    	}
    	
    	if($.trim(fuserpswd.val()) == ""){
    		alert("用户密码不能为空,请重新输入!");
    		fuserpswd.val("");		//清空输入框的值
    		fuserpswd.focus();		//把光标重新定位回这个输入框
    		return false ;
    	}
    	
    	if($.trim(femail.val()) == ""){
    		alert("用户邮箱不能为空,请重新输入!");
    		femail.val("");		//清空输入框的值
    		femail.focus();		//把光标重新定位回这个输入框
    		return false ;
    	}
    	
    	
    	/* JQuery框架中使用AJAX实现异步请求 */
    	$.ajax({
    		type : "POST",
    		data : {
    			loginacct : floginacct.val(),
    			userpswd : fuserpswd.val(),
     			usertype : fusertype.val(), 
    			email : femail.val()
    		},
    		url : "${APP_PATH}/doReg.do",
    		beforeSend : function(){
    			//一般做表单数据校验.
    			return true ;
    		},
    		success : function(result){ //{"success":true}  或    {"success":false,"message":"登录失败!"}
    			if(result.success){    				
    				alert(result.message);
    				//跳转登录页面.
    				window.location.href="${APP_PATH}/login.htm";
    			}else{
    				alert(result.message);
    			}
    		},
    		error : function(){
    			alert("error");
    		}	
    	});
    	
    	
    }
    </script>
    
  </body>
</html>