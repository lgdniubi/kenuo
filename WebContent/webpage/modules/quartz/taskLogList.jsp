<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>定时任务</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		//刷新
		function refresh(){
			window.location="${ctx}/quartz/task/loglist";
		}
		
		//重置表单
		function resetnew(){
			$("#jobName").val("");
			reset();
		}
		
		//分页按钮
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
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
					<h5>定时任务列表</h5>
				</div>
				<div class="ibox-content">
					<div class="searcharea clearfix">
						<form:form id="searchForm" action="${ctx}/quartz/task/loglist" modelAttribute="taskLog" method="post" class="navbar-form navbar-left searcharea">
							<div class="form-group">
								<label>
									关键字：
									<form:input path="jobName" class="form-control"/>
									状态：
									<form:select path="status" class="form-control">
										<form:option value="-1">全部</form:option>
										<form:option value="0">正常</form:option>
										<form:option value="1">异常</form:option>
									</form:select>
								</label>
							</div>
							<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
								<i class="fa fa-search"></i> 搜索
							</button>
							<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="resetnew()" >
								<i class="fa fa-refresh"></i> 重置
							</button>
							<!-- 分页必要字段 -->
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						</form:form>
					</div>
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
						</div>
					</div>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<th style="text-align: center;">编号</th>
								<th style="text-align: center;">任务名称</th>
								<th style="text-align: center;">过程描述</th>
								<th style="text-align: center;">开始时间</th>
								<th style="text-align: center;">结束时间</th>
								<th style="text-align: center;">运行时间(秒)</th>
								<th style="text-align: center;">创建时间</th>
								<th style="text-align: center;">当前状态</th>
								<th style="text-align: center;">异常描述</th>
								<th style="text-align: center;">备注</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list }" var="tasklog">
								<tr>
									<td>${tasklog.id}</td>
									<td>${tasklog.jobName}</td>
									<td >${tasklog.jobDescription}</td>
									<td style="text-align: center;"><fmt:formatDate value="${tasklog.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td style="text-align: center;"><fmt:formatDate value="${tasklog.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td style="text-align: center;">${tasklog.runTime/1000}</td>
									<td style="text-align: center;"><fmt:formatDate value="${tasklog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td style="text-align: center;">${tasklog.status==0?"是":"否"}</td>
									<td><a href="#" onclick="openDialogView('查看异常信息', '${ctx}/quartz/task/findlogbyid?id=${tasklog.id}','1200px', '600px')">${tasklog.exceptionMsg}</a></td>
									<td>${tasklog.remarks}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<!-- 分页代码 -->
					<table:page page="${page}"></table:page>
				</div>
			</div>
		</div>
	</div>
</body>
</html>