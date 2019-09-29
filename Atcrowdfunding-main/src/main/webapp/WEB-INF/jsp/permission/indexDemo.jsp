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
	<link rel="stylesheet" href="${APP_PATH }/css/main.css">
	<link rel="stylesheet" href="${APP_PATH }/css/doc.min.css">
	<link rel="stylesheet" href="${APP_PATH }/ztree/zTreeStyle.css">
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
           <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 许可维护</a></div>
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

			<div class="panel panel-default">
              <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限菜单列表 <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
			  <div class="panel-body">
                  <ul id="treeDemo" class="ztree"></ul>
			  </div>
			</div>
        </div>
      </div>
    </div>
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">帮助</h4>
		  </div>
		  <div class="modal-body">
			<div class="bs-callout bs-callout-info">
				<h4>没有默认类</h4>
				<p>警告框没有默认类，只有基类和修饰类。默认的灰色警告框并没有多少意义。所以您要使用一种有意义的警告类。目前提供了成功、消息、警告或危险。</p>
			  </div>
			<div class="bs-callout bs-callout-info">
				<h4>没有默认类</h4>
				<p>警告框没有默认类，只有基类和修饰类。默认的灰色警告框并没有多少意义。所以您要使用一种有意义的警告类。目前提供了成功、消息、警告或危险。</p>
			  </div>
		  </div>
		  <!--
		  <div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			<button type="button" class="btn btn-primary">Save changes</button>
		  </div>
		  -->
		</div>
	  </div>
	</div>
    <script src="${APP_PATH }/jquery/jquery-2.1.1.min.js"></script>
    <script src="${APP_PATH }/bootstrap/js/bootstrap.min.js"></script>
	<script src="${APP_PATH }/script/docs.min.js"></script>
	<script src="${APP_PATH }/script/menu.js"></script>
	<script src="${APP_PATH }/ztree/jquery.ztree.all-3.5.min.js"></script>
	<script src="${APP_PATH }/jquery/layer/layer.js"></script>
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
			    showMenu();
								
            });
            
            var setting = {	
        	    view : {			//显示自定义图标
           	    	addDiyDom: function(treeId, treeNode){
           		    	var icoObj = $("#" + treeNode.tId + "_ico"); // tId = permissionTree_1, $("#permissionTree_1_ico")
           	    		if ( treeNode.icon ) {
           	    			icoObj.removeClass("button ico_docu ico_open").addClass(treeNode.icon).css("background","");
           	    			}
           	    		},
          	    	addHoverDom: function(treeId, treeNode){  
						var aObj = $("#" + treeNode.tId + "_a"); // tId = permissionTree_1, ==> $("#permissionTree_1_a")
						aObj.attr("href", "javascript:;");
						if (treeNode.editNameFlag || $("#btnGroup"+treeNode.tId).length>0) return;
						var s = '<span id="btnGroup'+treeNode.tId+'">';
						if ( treeNode.level == 0 ) {
							s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="#" title="添加权限信息"  onclick="window.location.href=\'${APP_PATH}/permission/add.htm?pid='+treeNode.id+'\'" >&nbsp;&nbsp;<i class="fa fa-fw fa-plus rbg "></i></a>';
						} else if ( treeNode.level == 1 ) {
							s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;"  href="#" title="修改权限信息" onclick="window.location.href=\'${APP_PATH}/permission/edit.htm?id='+treeNode.id+'\'" >&nbsp;&nbsp;<i class="fa fa-fw fa-edit rbg "></i></a>';
							if (treeNode.children.length == 0) {
								s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="#" title="删除权限信息" onclick="del('+treeNode.id+')" >&nbsp;&nbsp;<i class="fa fa-fw fa-times rbg "></i></a>';
							}
							s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="#" title="添加权限信息" onclick="window.location.href=\'${APP_PATH}/permission/add.htm?pid='+treeNode.id+'\'" >&nbsp;&nbsp;<i class="fa fa-fw fa-plus rbg "></i></a>';
						} else if ( treeNode.level == 2 ) {
							s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;"  href="#" title="修改权限信息" onclick="window.location.href=\'${APP_PATH}/permission/edit.htm?id='+treeNode.id+'\'">&nbsp;&nbsp;<i class="fa fa-fw fa-edit rbg "></i></a>';
							s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="#" title="删除权限信息"  onclick="del('+treeNode.id+')" >&nbsp;&nbsp;<i class="fa fa-fw fa-times rbg "></i></a>';
						}
		
						s += '</span>';
						aObj.after(s);
					},
					removeHoverDom: function(treeId, treeNode){
						$("#btnGroup"+treeNode.tId).remove();
					}
				}
            	   
            	};


    		var zNodes;
    		
    		$.ajax({
        		type : "POST",
        		data : {  },
        		url : "${APP_PATH}/permission/doIndex.do",
        		beforeSend : function(){        			
        			return true ;
        		},
        		success : function(result){ 
        			if(result.success){    				
        				var zNodes = result.data;	
    					$.fn.zTree.init($("#treeDemo"), setting, zNodes);
        			}else{
        				layer.msg("加载失败!", {time:1000, icon:5, shift:6}); 
        			}
        		},
        		error : function(){
        			layer.msg("加载失败!", {time:1000, icon:5, shift:6});   
        		}	
        	});
    		
    		function del(id) {
    			layer.confirm("确认要删除吗?",  {icon: 3, title:'提示'}, function(cindex){
            		layer.close(cindex);
            		$.ajax({
                		type : "POST",
                		data : {
                			"id" : id
                		},
                		url : "${APP_PATH}/permission/doDelete.do",
                		beforeSend : function() {   
                			return true ;
                		},
                		success : function(result){
                			if(result.success){
                				window.location.href="${APP_PATH}/permission/index.htm";
                			}else{
                				layer.msg("删除失败", {time:1000, icon:5, shift:6}); 
                			}
                		},
                		error : function(){
                			layer.msg("删除失败", {time:1000, icon:5, shift:6}); 
                		}
                	});
    			}, function(cindex){
    			    layer.close(cindex);
    			});        													
								
            }
    		
            
            
        </script>
  </body>
</html>
    