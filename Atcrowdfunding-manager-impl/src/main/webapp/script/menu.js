
function showMenu(){
	 var pathname = window.location.pathname;		//去除项目名后的页面路径
	 var alink = $(".list-group-item a[href*='"+pathname+"']");
	 alink.css("color","red");
	 alink.parent().parent().parent().removeClass("tree-closed"); //找到父块，去除样式
	 alink.parent().parent().show();		//展开列表	
            	           	 
       }
function showAssign(){
	 var pathname = "/user/index.htm";		
	 var alink = $(".list-group-item a[href*='"+pathname+"']");
	 alink.css("color","red");
	 alink.parent().parent().parent().removeClass("tree-closed"); //找到父块，去除样式
	 alink.parent().parent().show();		//展开列表	
           	           	 
      }
function showRole(){
	 var pathname = "/role/index.htm";		
	 var alink = $(".list-group-item a[href*='"+pathname+"']");
	 alink.css("color","red");
	 alink.parent().parent().parent().removeClass("tree-closed"); //找到父块，去除样式
	 alink.parent().parent().show();		//展开列表	
          	           	 
     }
function showPermission(){
	 var pathname = "/permission/index.htm";		
	 var alink = $(".list-group-item a[href*='"+pathname+"']");
	 alink.css("color","red");
	 alink.parent().parent().parent().removeClass("tree-closed"); //找到父块，去除样式
	 alink.parent().parent().show();		//展开列表	
         	           	 
    }
function showProcess(){
	 var pathname = "/process/index.htm";		
	 var alink = $(".list-group-item a[href*='"+pathname+"']");
	 alink.css("color","red");
	 alink.parent().parent().parent().removeClass("tree-closed"); //找到父块，去除样式
	 alink.parent().parent().show();		//展开列表	
        	           	 
   }