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
          <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 实名认证审核</a></div>
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
				<%@include file="/WEB-INF/jsp/common/menu.jsp" %>
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
      <input id="queryText" class="form-control has-success" type="text" placeholder="请输入查询条件">
    </div>
  </div>
  <button id="queryBtn"  type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
</form>
<br>
 <hr style="clear:both;">
          <div class="table-responsive">
            <table class="table  table-bordered">
              <thead>
                <tr >
                  <th width="30">#</th>
                  <th>流程名称</th>
                  <th>流程版本</th>
                  <th>任务名称</th>
                  <th>申请会员</th>
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
	<script src="${APP_PATH}/jquery/layer/layer.js"></script>
	<script src="${APP_PATH }/script/menu.js"></script>
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

            function pageChange(pageno){
            	queryPageUser(pageno);
            }
            
            
            var jsonObj = {
        			"pageno" : 1,
        			"pagesize" : 10 
        		};
            
            
            var loadingIndex = -1 ;
            function queryPageUser(pageno){
            	jsonObj.pageno = pageno ;
            	$.ajax({
            		type : "POST",
            		data : jsonObj,
            		url : "${APP_PATH}/authcert/doIndex.do",
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
            				
            				$.each(data,function(i,task){
            					content+= '  <tr>';
                				content+= '  <td>'+(i+1)+'</td>';
                				content+= '  <td>'+task.procDefName+'</td>';	                
                				content+= '  <td>'+task.procDefVersion+'</td>';
             	                content+= '  <td>'+task.name+'</td>';
             	                content+= '  <td>'+task.memberName+'</td>';
                				content+= '  <td>';
                				content+= '  <button type="button" onclick="window.location.href=\'${APP_PATH}/authcert/show.htm?taskid='+task.id+'&memberid='+task.memberid+'\'" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>';		       
                				content+= '  </td>';
            	                content+= '  </tr>';

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
            
            
            
            $("#queryBtn").click(function(){
            	var queryText = $("#queryText").val();
            	jsonObj.queryText = queryText ;
            	queryPageUser(1);
            });

            
        </script>
  </body>
</html>
    