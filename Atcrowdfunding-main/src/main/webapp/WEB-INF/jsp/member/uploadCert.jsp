<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
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
		  <li role="presentation" class="active"><a href="#"><span class="badge">2</span> 资质文件上传</a></li>
		  <li role="presentation"><a href="#"><span class="badge">3</span> 邮箱确认</a></li>
		  <li role="presentation"><a href="#"><span class="badge">4</span> 申请确认</a></li>
		</ul>
		<form id="certForm"  role="form" method="post" enctype="multipart/form-data" style="margin-top:20px;">
		  
		  <c:forEach items="${certByAccttype }" var="cert" varStatus="status">
		  	 <div class="form-group">
				<label for="name">${cert.name }</label>
				<input type="hidden" name="certimgs[${status.index}].certid" value="${cert.id}"> 				
				<input type="file" name="certimgs[${status.index}].imgfile"  class="form-control" >				
	            <br>
	           	<img src="" style="display:none;">
		  	</div>
		  </c:forEach>
		  		  
          <button type="button" onclick="window.location.href='basicinfo.htm'" class="btn btn-default">上一步</button>
		  <button type="button" id="nextBtn"  class="btn btn-success">下一步</button>
		</form>
		<hr>
    </div> <!-- /container -->
        
    <%@ include file="/WEB-INF/jsp/common/foot.jsp" %>     
        
    <script src="${APP_PATH }/jquery/jquery-2.1.1.min.js"></script>
    <script src="${APP_PATH }/bootstrap/js/bootstrap.min.js"></script>
	<script src="${APP_PATH }/script/docs.min.js"></script>
	<script src="${APP_PATH }/script/menu.js"></script>
	<script src="${APP_PATH }/jquery/layer/layer.js"></script>
	<script src="${APP_PATH }/jquery/jquery-form/jquery-form.min.js"></script>
	<script>
        $('#myTab a').click(function (e) {
          e.preventDefault()
          $(this).tab('show')
        });
        
        $(":file").change(function(event){	//图片预览
        	var files = event.target.files;
        	var file;
        	
        	if (files && files.length > 0) {
        		file = files[0];
        		
        		var URL = window.URL || window.webkitURL;
        		// 本地图片路径
        		var imgURL = URL.createObjectURL(file);
        		
        		var imgObj = $(this).next().next(); //获取同辈紧邻的下一个元素
        		//console.log(imgObj);
        		imgObj.attr("src", imgURL);
        		imgObj.show();
        	}
		});
		
        $("#nextBtn").click(function(){       //表单提交 	
        	var loadingIndex = -1;
		   	var options = {
		   		url : "${APP_PATH}/member/doUploadCertFile.do",
		   		beforeSubmit : function() {
		   			loadingIndex = layer.msg('数据保存中', {icon: 16});
		   		},
		   		success : function(result) {
		   			layer.close(loadingIndex);
		   			if ( result.success ) {
		   				layer.msg("资质文件上传成功", {time:1000, icon:6}, function(){
		   					window.location.href = "${APP_PATH}/member/checkEmail.htm";
		   				});
		   			} else {
		   				layer.msg("资质文件上传失败", {time:1000, icon:5, shift:6});
		   			}
		   		}
		   	};
		   	$("#certForm").ajaxSubmit(options);
		});

        
        
	</script>
  </body>
</html>