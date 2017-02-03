<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单列表</title>
	<meta name="decorator" content="default"/>
</head>

<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		$(document).ready(function() {
			
	        //外部js调用
	        laydate({
	            elem: '#begtime',   //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus'      //响应事件。如果没有传入event，则按照默认的click
	        });
	        
	        laydate({
	            elem: '#endtime',  //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus'     //响应事件。如果没有传入event，则按照默认的click
	        });

	    })
</script>


<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
				<div class="ibox-content">
				<div class="clearfix">
				 <div class="pull-left">
				 	<form:form id="buglog" modelAttribute="bugLog" action="#" method="get" class="form-inline">
						<div class=" pull-right">
							<label >${bugLog.exception}</label>
						</div>
					</form:form>
				 </div>
				</div>				
			</div>
		</div>
	</div>
	
</body>
</html>