<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/treetable.jsp" %>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
	<title>课程评论管理</title>
	<meta name="decorator" content="default" />
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<script type="text/javascript">
		//分页按钮
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		function refresh(){
			$("#searchForm").submit();
	    	return false;
		}
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">	
			<div class="ibox">
				<div class="ibox-title">
					<h5>${trainLessonComments.trainLessons.name}</h5>-课程评论列表
				</div>
				<div class="ibox-content">
					<div class="searcharea clearfix">
						<form:form id="searchForm" modelAttribute="trainLessonComments" action="${ctx}/train/comments/listcomments" method="post">
							<input type="hidden" id="lessonId" name="lessonId" value="${trainLessonComments.trainLessons.lessonId}">
							<!-- 分页必要字段 -->
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						</form:form>
					</div>
					<!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="train:comments:batchdeletecomments">
									<table:delRow url="${ctx}/train/comments/batchdeletecomments" id="treeTable"></table:delRow><!-- 删除按钮 -->
								</shiro:hasPermission>
							</div>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
						</div>
					</div>
					<table id="treeTable" class="table table-bordered table-hover table-striped">
						<thead>
							<tr>
								<th width="120" style="text-align: center;"><label for="i-checks"><input type="checkbox" name="" id="i-checks" class="i-checks"></label></th>
								<th width="200" style="text-align: center;">课程名称</th>
								<th width="120" style="text-align: center;">用户名称</th>
								<th style="text-align: center;">评论内容</th>
								<th width="120" style="text-align: center;">评论星级</th>
								<th width="150" style="text-align: center;">评论时间</th>
								<th width="100" style="text-align: center;">评论管理</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="trainLessonComments">
								<tr>
									<td><input type="checkbox" class="i-checks" id="${trainLessonComments.commentId }" name="ids"></td>
									<td>${trainLessonComments.trainLessons.name }</td>
									<td>${trainLessonComments.user.name }</td>
									<td>${trainLessonComments.content }</td>
									<td><fmt:formatNumber value="${trainLessonComments.star }"/></td>
									<td><fmt:formatDate value="${trainLessonComments.createtime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>
										<shiro:hasPermission name="train:comments:deletecomments">
											<a href="${ctx}/train/comments/deletecomments?commentId=${trainLessonComments.commentId }" class="btn btn-danger btn-circle" 
												onclick="return confirmx('要删除该课程评论吗？', this.href)">
												<i class=" glyphicon glyphicon-trash"></i>
											</a>
										</shiro:hasPermission>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<!-- 分页代码 -->
					<table:page page="${page}"></table:page>
					<br/>
					<br/>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>