<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>定时任务</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<script type="text/javascript">
		//刷新
		function refresh(){
			window.location="${ctx}/quartz/task/list";
		}
		
		//重置表单
		function resetnew(){
			$("#name").val("");
			reset();
		}
		
		//分页按钮
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		
		//立即执行定时任务
		function updatetaskstatus(id,status){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",   
				url : "${ctx}/quartz/task/updatetaskstatus?id="+id+"&status="+status,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					if("OK" == data.STATUS){
						alert(data.MESSAGE);
						
						$("#JOBSTATUS"+id).html("");//清除DIV内容	
						if(status == '0'){
							//当前状态为【关闭】，则开启
							$("#JOBSTATUS"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"updatetaskstatus('"+id+"','1')\">");
						}else if(status == '1'){
							//当前状态为【开启】，则关闭
							$("#JOBSTATUS"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"updatetaskstatus('"+id+"','0')\">");
						}
						
					}else if("ERROR" == data.STATUS){
						alert(data.MESSAGE);
					}
				}
			});   
		}
		
		//立即执行定时任务
		function runJobNow(id){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",   
				url : "${ctx}/quartz/task/runjobnow?id="+id,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					if("OK" == data.STATUS){
						alert(data.MESSAGE);
					}else if("ERROR" == data.STATUS){
						alert(data.MESSAGE);
					}
				}
			});   
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
						<form:form id="searchForm" action="${ctx}/quartz/task/list" modelAttribute="task" method="post" class="navbar-form navbar-left searcharea">
							<div class="form-group">
								<label>关键字：<input id="jobName" name="jobName" maxlength="50" type="text" class="form-control" value="${task.jobName}"></label> 
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
							<shiro:hasPermission name="quartz:task:add">
								<table:addRow url="${ctx}/quartz/task/form?opflag=ADDPARENT" width="800px" height="450px" title="添加任务" ></table:addRow><!-- 增加按钮 -->
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
							<shiro:hasPermission name="quartz:task:loglist">
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/quartz/task/loglist","任务日志", false)' ><i class="fa fa-plus"></i> 定时任务日志</button>
							</shiro:hasPermission>
						</div>
					</div>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<th style="text-align: center;">编号</th>
								<th style="text-align: center;">名称</th>
								<th style="text-align: center;">分组</th>
								<th style="text-align: center;">状态</th>
								<!-- <th style="text-align: center;">是否并发</th> -->
								<th style="text-align: center;">执行时间</th>
								<th style="text-align: center;">描述</th>
								<th style="text-align: center;">实体类</th>
								<th style="text-align: center;">创建时间</th>
								<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list }" var="task">
								<tr>
									<td>${task.jobId}</td>
									<td>${task.jobName}</td>
									<td>${task.jobGroup}</td>
									<td style="text-align: center;" id="JOBSTATUS${task.jobId}">
										<c:if test="${task.jobStatus == 0}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="updatetaskstatus('${task.jobId}','1')">
										</c:if>
										<c:if test="${task.jobStatus == 1}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="updatetaskstatus('${task.jobId}','0')">
										</c:if>
									</td>
									<%-- <td style="text-align: center;">${task.isConcurrent==0?"是":"否"}</td> --%>
									<td>${task.cronExpression}</td>
									<td>${task.description}</td>
									<td>${task.beanClass}</td>
									<td><fmt:formatDate value="${task.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>
										<!-- 立即执行任务 -->
										<a href="#" onclick="runJobNow(${task.jobId})" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 立即执行</a>
										
										<shiro:hasPermission name="quartz:task:view">
				    						<a href="#" onclick="openDialogView('查看任务', '${ctx}/quartz/task/form?jobId=${task.jobId}&opflag=VIEW','800px', '450px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					    				</shiro:hasPermission>
										<shiro:hasPermission name="quartz:task:edit">
				    						<a href="#" onclick="openDialog('修改任务', '${ctx}/quartz/task/form?jobId=${task.jobId}&opflag=UPDATE','800px', '450px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
					    				</shiro:hasPermission>
					    				<shiro:hasPermission name="quartz:task:del">
											<a href="${ctx}/quartz/task/delete?jobId=${task.jobId}" onclick="return confirmx('要删除该任务吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
										</shiro:hasPermission>
									</td>
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
	<div class="loading"></div>
</body>
</html>