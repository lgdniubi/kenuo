<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>美容师评论管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<link href="${ctxStatic}/train/css/imgbox.css" rel="stylesheet" />
	<script src="${ctxStatic}/train/js/jquery.min.js"></script>
	<script type="text/javascript">
		var jq = $.noConflict();
	</script>
	<script src="${ctxStatic}/train/js/jquery.imgbox.pack.js"></script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>美容师评论管理</h5>
			</div>
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">编号</th>
						    <th style="text-align: center;">用户昵称</th>
						    <th style="text-align: center;">评论时间</th>
						    <th style="text-align: center;">美容师</th>
						    <th style="text-align: center;">星级</th>
						    <th style="text-align: center;">评论内容</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${comment}" var="beatuyComment">
							<tr>
							  	<td>${beatuyComment.commentId }</td>
							  	<td>${beatuyComment.users.name }</td>
							  	<td><fmt:formatDate value="${beatuyComment.addTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							  	<td>${beatuyComment.user.name }</td>
							  	<td>${beatuyComment.goodsRank }</td>
							  	<td>
							  		<!-- 当文本内容大于5个字符时，只显示其前五个字符 -->
								  	<c:choose>  
								         <c:when test="${fn:length(beatuyComment.contents) > 10}">  
								             <c:out value="${fn:substring(beatuyComment.contents, 0, 10)}..." />  
								         </c:when>  
								        <c:otherwise>  
								           <c:out value="${beatuyComment.contents}" />  
								        </c:otherwise>  
								    </c:choose>
							  	</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>