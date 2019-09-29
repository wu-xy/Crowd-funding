<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
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
	<link rel="stylesheet" href="${APP_PATH }/css/main.css">
	<link rel="stylesheet" href="${APP_PATH }/css/doc.min.css">
	<style>
	.tree li {
        list-style-type: none;
		cursor:pointer;
	}
	</style>
  </head>

  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container-fluid">
        <div class="navbar-header">
            <div><a class="navbar-brand" style="font-size:32px;" href="user.html">众筹平台 - 用户维护</a></div>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <%@ include file="/WEB-INF/jsp/common/top.jsp" %>
          </ul>
          <form class="navbar-form navbar-right">
            <input type="text" class="form-control" placeholder="Search...">
          </form>
        </div>
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
			<div class="tree">
				<%@ include file="/WEB-INF/jsp/common/menu.jsp" %>
			</div>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<ol class="breadcrumb">
				  <li><a href="#">首页</a></li>
				  <li><a href="#">数据列表</a></li>
				  <li class="active">认证信息</li>
				</ol>
			<div class="panel panel-default">
              <div class="panel-heading">表单数据<div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
			  <div class="panel-body">
				<form role="form">
				  <div class="form-group">
					<label for="exampleInputPassword1">真实名称</label>
					<p>${member.realname}</p>
				  </div>
				  <div class="form-group">
					<label for="exampleInputPassword1">身份证号码</label>
					<p>${member.cardnum}</p>					
				  </div>
				  <div class="form-group">
					<label for="exampleInputPassword1">电话号码</label>
					<p>${member.tel}</p>	
				  </div>
				  
				  <c:forEach  items="${certImgs}" var="certImg">
					  <div class="form-group">
						<label for="certname">${certImg.certname}</label>
						<br>
						<img src="${APP_PATH}/pic/certs/${certImg.iconpath}">
					  </div>		  
				  </c:forEach>
				  <button id="passBtn"   type="button"  class="btn btn-success"><i class="glyphicon glyphicon-plus"></i> 审核通过</button>
				  <button id="refuseBtn" type="button"  class="btn btn-danger"><i class="glyphicon glyphicon-refresh"></i> 审核拒绝</button>
				</form>

			  </div>
			</div>
        </div>
      </div>
    </div>
	
    <script src="${APP_PATH }/jquery/jquery-2.1.1.min.js"></script>
    <script src="${APP_PATH }/bootstrap/js/bootstrap.min.js"></script>
	<script src="${APP_PATH }/script/docs.min.js"></script>
	<script type="text/javascript" src="${APP_PATH }/jquery/layer/layer.js"></script>
	<script type="text/javascript" src="${APP_PATH }/script/menu.js"></script>
	
        <script type="text/javascript">
            $(function () {
			    $(".list-group-item").click(function(){
				    if ( $(this).find("ul") ) {
						$(this).toggleClass("tree-closed");
						if ( $(this).hasClass("tree-closed") ) {
							$("ul", this).hide("fast");
						} else {
							$("ul", this).show("fast");
						}
					}
				});
			   
            });
            
            
            $("#passBtn").click(function(){            	
                $.ajax({
        		type : "POST",
        		url  : "${APP_PATH}/authcert/pass.do",
        		data : {
        			taskId : "${param.taskid}",
        			memberid : "${param.memberid}"
        		},
        		success : function(result) {
        			window.location.href = "${APP_PATH}/authcert/index.htm";
        		}
        	});
        });
        
        $("#refuseBtn").click(function(){
        	$.ajax({
        		type : "POST",
        		url  : "${APP_PATH}/authcert/refuse.do",
        		data : {
        			taskId : "${param.taskid}",
        			memberid : "${param.memberid}"
        		},
        		success : function(result) {
        			window.location.href = "${APP_PATH}/authcert/index.htm";
        		}
        	});
        });

            
            
        </script>
  </body>
</html>
    