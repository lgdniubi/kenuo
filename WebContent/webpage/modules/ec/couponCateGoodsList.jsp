<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>红包商品分类列表</title>
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
		
				
				
				
				 	<form:form id="inputFrom" modelAttribute="activityCoupon" action="#" method="get" class="form-inline">
						<table id="contentTable" class="table  table-bordered">
						<thead>
							<tr>
								<c:if test="${activityCoupon.usedType==1}">
									<th>商品名称</th>
								</c:if>
								<c:if test="${activityCoupon.usedType==2}">
									<th>分类名称</th>
								</c:if>
								<c:if test="${activityCoupon.usedType==3}">
									<th>商品名称</th>
								</c:if>
								
							</tr>
						</thead>
						<tbody>
								<c:if test="${activityCoupon.usedType==1}">
									<tr><td>全部商品</td></tr>
								</c:if>
								<c:if test="${activityCoupon.usedType==2}">
									<c:forEach items="${activityCoupon.catelist}" var="list" varStatus="status">
										<td>${list.categoryName}</td>
									</c:forEach>
								</c:if>
								<c:if test="${activityCoupon.usedType==3}">
									<c:forEach items="${activityCoupon.list}" var="list" varStatus="status">
										<td>${list.goodName}</td>
									</c:forEach>
								</c:if>
						
						</tbody>
					</table>
						
						
					</form:form>
			</div>
					
					
				
				
						
		
	
	
</body>
</html>