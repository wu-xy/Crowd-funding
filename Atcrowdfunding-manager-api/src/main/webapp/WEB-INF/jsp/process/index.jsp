<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	<link rel="stylesheet" href="${APP_PATH }/css/pagination.css">
	<style>
	.tree li {
        list-style-type: none;
		cursor:pointer;
	}
	table tbody tr:nth-child(odd){background:#F4F4F4;}
	table tbody td:nth-child(even){color:#C00;}
	</style>
  </head>

  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container-fluid">
        <div class="navbar-header">
          <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 流程管理</a></div>
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
			  <div class="panel-heading">
				<h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
			  </div>
			  <div class="panel-body">
<form class="form-inline" role="form" style="float:left;">
  <div class="form-group has-feedback">
    <div class="input-group">
      <div class="input-group-addon">查询条件</div>
      <input class="form-control has-success" type="text" placeholder="请输入查询条件">
    </div>
  </div>
  <button type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
</form>

<button type="button" id="uploadProcDefBtn" class="btn btn-primary" style="float:right;" ><i class="glyphicon glyphicon-upload"></i> 上传流程定义文件</button>
<br>

<!-- 隐藏的上传表单 -->
<form id="uploadForm" action="" method="post" enctype="multipart/form-data">
        <input style="display:none;" id="procDefFile" type="file" name="procDefFile">
</form>

 <hr style="clear:both;">
          <div class="table-responsive">
            <table class="table  table-bordered">
              <thead>
                <tr >
                  <th width="30">#</th>
                  <th width="30"><input id="allCheckbox" type="checkbox"></th>
                  <th>流程名称</th>
                  <th>流程版本</th>
                  <th>任务名称</th>
                  <th width="100">操作</th>
                </tr>
              </thead>
              <tbody>
                  
              </tbody>
			  <tfoot>
			     <tr >
				     <td colspan="6" align="center">
						<ul class="pagination">
								
						</ul>
					 </td>
				 </tr>

			  </tfoot>
            </table>
          </div>
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
	<script src="${APP_PATH}/jquery/pagination/jquery.pagination.js"></script>
	<script src="${APP_PATH }/jquery/jquery-form/jquery-form.min.js"></script>
	
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
			    queryPageUser(1);
			    showMenu();
            });
            
            var jsonObj = {
        			"pageno" : 1,
        			"pagesize" : 10 
        		};
            function pageChange(pageno){
            	queryPageUser(pageno);
            }
            
            var loadingIndex = -1 ;
            function queryPageUser(pageno){
            	jsonObj.pageno = pageno ;
            	$.ajax({
            		type : "POST",
            		data : jsonObj,
            		url : "${APP_PATH}/process/doIndex.do",
            		beforeSend : function(){
            			loadingIndex = layer.load(2, {time: 10*1000});
            			return true ;
            		},
            		success : function(result){
            			layer.close(loadingIndex);
            			if(result.success){
            				var page = result.page ;
            				var data = page.data ;
            				
            				var content = '';
            				
            				$.each(data,function(i,n){
            					content+='<tr>';
                				content+='  <td>'+(i+1)+'</td>';
                				content+='  <td><input type="checkbox" id="'+n.id+'" name="'+n.name+'"></td>';
                				content+='  <td>'+n.name+'</td>';
                				content+='  <td>'+n.version+'</td>';
                				content+='  <td>'+n.key+'</td>';
                				content+='  <td>';
                				content+='	  <button type="button"  onclick="window.location.href=\'${APP_PATH}/process/showimg.do?id='+n.id+'\'"  class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-eye-open"></i></button>';
                				content+='	  <button type="button"  onclick="deleteProcess(\''+n.id+'\',\''+n.name+'\')" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>';               			
                				content+='  </td>';
                				content+='</tr>';
            				});
            				
            				
            				$("tbody").html(content);
            				
            				var contentBar = '';
            				
            				if(page.pageno==1 ){
            					contentBar+='<li class="disabled"><a href="#">上一页</a></li>';
            				}else{
            					contentBar+='<li><a href="#" onclick="pageChange('+(page.pageno-1)+')">上一页</a></li>';
            				}
            				
            				for(var i = 1 ; i<= page.totalno ; i++ ){            					
            					contentBar+='<li';
									if(page.pageno==i){
										contentBar+=' class="active"';
									}
								contentBar+='><a href="#" onclick="pageChange('+i+')">'+i+'</a></li>';
            				}
							
							if(page.pageno==page.totalno ){
            					contentBar+='<li class="disabled"><a href="#">下一页</a></li>';
            				}else{
            					contentBar+='<li><a href="#" onclick="pageChange('+(page.pageno+1)+')">下一页</a></li>';
            				}
            				
            				$(".pagination").html(contentBar);
            				
            			}else{
            				layer.msg(result.message, {time:1000, icon:5, shift:6});
            			}
            		},
            		error : function(){
            			layer.msg("加载数据失败!", {time:1000, icon:5, shift:6});
            		}
            	});
            }
            
            $("#uploadProcDefBtn").click(function(){ //表示增加绑定事件  
            	$("#procDefFile").click(); //表示执行点击            
            });
           
            
            $("#procDefFile").change(function(){		//选择上传的文件即开始上传.
       		 var options = {
       				url:"${APP_PATH}/process/deploy.do",
      				beforeSubmit : function(){
      					loadingIndex = layer.msg('流程正在部署中', {icon: 6});
              			return true ; //必须返回true,否则,请求终止.
      				},
      				success : function(result){
           				layer.close(loadingIndex);
           				if(result.success){
           					layer.msg("流程部署成功", {time:1000, icon:6});
           			 		queryPageUser(1);
           				}else{
           					layer.msg("流程部署失败", {time:1000, icon:5, shift:6});
           				}	
           			}	
       			};
       		
       		$("#uploadForm").ajaxSubmit(options); //异步提交
       		return ;  
       		      		
       		/* $("#advertForm").attr("action","${APP_PATH}/advert/doAdd.do");
       		$("#advertForm").submit();  */
       		          	
           });	
            
			function deleteProcess(id,name){
            	
            	layer.confirm("确认要删除["+name+"]流程吗?",  {icon: 3, title:'提示'}, function(cindex){
            		layer.close(cindex);
            		$.ajax({
                		type : "POST",
                		data : {
                			"id" : id
                		},
                		url : "${APP_PATH}/process/doDelete.do",
                		beforeSend : function() {   
                			return true ;
                		},
                		success : function(result){
                			if(result.success){
                				 queryPageUser(1);
                			}else{
                				layer.msg("删除流程失败", {time:1000, icon:5, shift:6}); 
                			}
                		},
                		error : function(){
                			layer.msg("删除流程失败", {time:1000, icon:5, shift:6}); 
                		}
                	});
    			}, function(cindex){
    			    layer.close(cindex);
    			});            	
            	
            }  
            			
						
            
        </script>
  </body>
</html>
    