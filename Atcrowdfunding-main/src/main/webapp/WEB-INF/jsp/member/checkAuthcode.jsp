<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
	<link rel="stylesheet" href="${APP_PATH }/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="${APP_PATH }/css/font-awesome.min.css">
	<link rel="stylesheet" href="${APP_PATH }/css/theme.css">
	<style>
#footer {
    padding: 15px 0;
    background: #fff;
    border-top: 1px solid #ddd;
    text-align: center;
}
	</style>
  </head>
  <body>
 <div class="navbar-wrapper">
      <%@ include file="/WEB-INF/jsp/common/memberinfo.jsp" %>
    </div>

    <div class="container theme-showcase" role="main">
      <div class="page-header">
        <h1>实名认证 - 申请</h1>
      </div>

		<ul class="nav nav-tabs" role="tablist">
		  <li role="presentation" ><a href="#"><span class="badge">1</span> 基本信息</a></li>
		  <li role="presentation" ><a href="#"><span class="badge">2</span> 资质文件上传</a></li>
		  <li role="presentation" ><a href="#"><span class="badge">3</span> 邮箱确认</a></li>
		  <li role="presentation" class="active"><a href="#"><span class="badge">4</span> 申请确认</a></li>
		</ul>
        
		<form role="form" style="margin-top:20px;">
		  <div class="form-group">
			<label for="authcode">验证码</label>
			<input type="text" class="form-control" id="authcode" placeholder="请输入你邮箱中接收到的验证码">
		  </div>
          <button type="button" id="replayBtn" class="btn btn-primary">重新发送验证码</button>
		  <button type="button" id="finishBtn"  class="btn btn-success">申请完成</button>
		</form>
		<hr>
    </div> <!-- /container -->
        
    <%@ include file="/WEB-INF/jsp/common/foot.jsp" %>       
        
        
    <script src="${APP_PATH }/jquery/jquery-2.1.1.min.js"></script>
    <script src="${APP_PATH }/bootstrap/js/bootstrap.min.js"></script>
	<script src="${APP_PATH }/script/docs.min.js"></script>
	<script src="${APP_PATH }/script/menu.js"></script>
	<script src="${APP_PATH }/jquery/layer/layer.js"></script>
	<script>
        $('#myTab a').click(function (e) {
          e.preventDefault()
          $(this).tab('show')
        }); 
        
		var loadingIndex = -1 ;
        
        $("#finishBtn").click(function(){        	
        	$.ajax({
		   		type : "POST",
		   		url  : "${APP_PATH}/member/doCheckAuthcode.do",
		   		data : {		   			
		   			authcode  : $("#authcode").val()
		   		},
		   		beforeSend : function(){
	    			loadingIndex = layer.msg('处理中', {icon: 16});
	    			return true ;
	    		},
		   		success : function(result) {
		   			layer.close(loadingIndex);
		   			if ( result.success ) {
		   				layer.msg("验证成功", {time:1500, icon:6});	
		   				window.location.href = "${APP_PATH}/member.htm";
		   			} else {
		   				layer.msg("验证失败，请重新输入验证码", {time:1000, icon:5, shift:6});
		   			}
		   		},
		   		error : function(){
        			layer.msg("验证失败，请重新输入验证码", {time:1000, icon:5, shift:6}); 
        		}
	    	});
	    });
        
	</script>
  </body>
</html>