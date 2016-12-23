<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学生预约详情</title>
	<meta name="decorator" content="default" />
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<script type="text/javascript" src="${ctxStatic}/train/js/jquery-1.11.3.js"></script>
	<script type="text/javascript">
		//分页按钮
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		//重置表单
		function resetnew(){
			$("#userName").val("");
		}
		//刷新
		function refresh(){
			$("#userName").val("");
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
					<h5>学生预约详情</h5>
				</div>
				<div class="ibox-content" style="width: 85%;">
					<div class="searcharea clearfix">
						<form:form id="searchForm" action="${ctx}/train/studentsubscribe/findlist" method="post">
							<div class="form-group">
								<input type="hidden" id="categoryId" name="categoryId" value="${trainOfflineTestSubscribe.categoryId}">
								<input type="hidden" id="subscribeId" name="subscribeId" value="${trainOfflineTestSubscribe.subscribeId}">
								<label>关键字：<input id="userName" name="userName" maxlength="10" type="text" class="form-control" value="${trainOfflineTestSubscribe.userName}"></label> 
								<shiro:hasPermission name="train:subscribe:listsubscribe">
									<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
										<i class="fa fa-search"></i>  搜索
									</button>
									<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="resetnew()" >
										<i class="fa fa-refresh"></i> 重置
									</button>
								</shiro:hasPermission>
							</div>
							<!-- 分页必要字段 -->
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						</form:form>
					</div>
					<!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
							</div>
						</div>
					</div>
					<table id="treeTable" class="table table-bordered table-hover table-striped">
						<thead>
							<tr>
								<th width="200" style="text-align: center;">课程分类</th>
								<th width="200" style="text-align: center;">考试时间</th>
								<th width="120" style="text-align: center;">用户</th>
								<th width="120" style="text-align: center;">联系电话</th>
								<th width="200" style="text-align: center;">预约时间</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="trainOfflineTestSubscribe">
								<tr>
									<td>${trainOfflineTestSubscribe.trainCategorys.name }</td>
									<td><fmt:formatDate value="${trainOfflineTestSubscribe.subscribeTime }" pattern="yyyy-MM-dd HH:mm"/></td>
									<td>${trainOfflineTestSubscribe.user.name }</td>
									<td>${trainOfflineTestSubscribe.user.mobile }</td>
									<td><fmt:formatDate value="${trainOfflineTestSubscribe.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
</body>
</html>