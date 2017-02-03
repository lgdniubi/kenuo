<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>日志列表</title>
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
	            elem: '#begtime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        
	        laydate({
	            elem: '#endtime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });

	       
	    })
</script>


<body>
    <sys:message content="${message}"/>


	
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
				
			<tr>
			<th>编号</th>
			<th>订单号</th>
			<th>操作</th>
			<th>支付编码</th>
			<th>支付名称</th>
			<th>时间</th>
			<th>ip地址</th>
			<th>支付流水号</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${orderslist}" var="list">
				<tr>
					<td>${list.olid}</td>
					<td>${list.orderid}</td>
					<td>${list.operator}</td>
					<td>${list.paycode}</td>
					<td>${list.payname}</td>
					<td><fmt:formatDate value="${list.addtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>${list.ipaddress}</td>
					<td>${list.temporderid}</td>
				</tr>
			</c:forEach>
			</tbody>
	</table>
	
	
</body>
</html>