<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
	<title>通知管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		
		function pushMsg(notifyId,pushType){
			 var flag = confirm('确认要推送吗？');
			 
			 if(flag){
				 $.ajax({
					 type : 'POST',
					 url : '${ctx}/ec/mtmyOaNotify/pushMsg',
					 data:{'notify_id':notifyId,'push_type':pushType},
					 success:function(data){
						 if(data.result == '200'){
							 alert(data.message);
							 sortOrRefresh();
						 }else{
							 alert(data.message);
						 }
					 }
				 })
			 }
		}
		
		function pushResult(notifyId,contentId){
			
			 var flag = confirm('确认要获取推送结果吗？');
			 
			 if(flag){
				 $.ajax({
					 type : 'POST',
					 url : '${ctx}/ec/mtmyOaNotify/getPushResult',
					 data:{'notify_id':notifyId,'content_id':contentId},
					 success:function(data){
						 if(data.result == '200'){
							 alert(data.message);
							 sortOrRefresh();
						 }else{
							 alert(data.message);
						 }
					 }
				 })
			 }
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>通知列表 </h5>
			</div>
		    <div class="ibox-content">
				<sys:message content="${message}"/>
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="mtmyOaNotify" action="${ctx}/ec/mtmyOaNotify/list" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="isSelf" name="isSelf" type="hidden" value="false"/>
						<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
						<div class="form-group">
							<span>标题：</span>
								<form:input path="title" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>
							<span>类型：</span>
								<form:select path="type"  class="form-control m-b">
								    <form:option value="" label=""/>
								    <form:options items="${fns:getDictList('mtmy_oa_notify_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							    </form:select>
							<span>状态：</span>
								<form:radiobuttons path="status" class="i-checks" items="${fns:getDictList('oa_notify_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</div>	
					</form:form>
					<br/>
					</div>
				</div>
				
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="ec:mtmyOaNotify:add">
								<table:addRow url="${ctx}/ec/mtmyOaNotify/form?status=0" title="通知"></table:addRow><!-- 增加按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="ec:mtmyOaNotify:edit">
							    <table:editRow url="${ctx}/ec/mtmyOaNotify/form" id="contentTable"  title="通知" width="800px" height="500px"></table:editRow><!-- 编辑按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="ec:mtmyOaNotify:del">
								<table:delRow url="${ctx}/ec/mtmyOaNotify/deleteAll" id="contentTable" isSelf="false"></table:delRow><!-- 删除按钮 -->
							</shiro:hasPermission>
					       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
							</div>
						<div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
					</div>
				</div>
				
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th> <input type="checkbox" class="i-checks"></th>
							<th>标题</th>
							<th>类型</th>
							<th>推送类型</th>
							<th>状态</th>
							<th>查阅状态</th>
							<th>任务开始</th>
							<th>任务结束</th>
							<th>更新时间</th>
							<th>推送结果</th>
							<th>有效下发</th>
							<th>消息回执</th>
							<th>用户点击</th>
							<th>推送数</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${page.list}" var="mtmyOaNotify">
						<tr>
							<td> <input type="checkbox" id="${mtmyOaNotify.id}" class="i-checks"></td>
							<td><a  href="#" onclick="openDialogView('查看通知', '${ctx}/ec/mtmyOaNotify/form?id=${mtmyOaNotify.id}','800px', '500px')">
								${fns:abbr(mtmyOaNotify.title,50)}
							</a></td>
							<td>
								${fns:getDictLabel(mtmyOaNotify.type, 'mtmy_oa_notify_type', '')}
							</td>
							<td>
								<c:if test="${mtmyOaNotify.pushType == 0}">群推</c:if>
								<c:if test="${mtmyOaNotify.pushType == 1}">列推</c:if>
								<c:if test="${mtmyOaNotify.pushType == 2}">组推</c:if>
								<%-- ${fns:getDictLabel(mtmyOaNotify.pushType, 'oa_push_type', '')} --%>
							</td>
							<td>
								${fns:getDictLabel(mtmyOaNotify.status, 'oa_notify_status', '')}
							</td>
							<td>
								${mtmyOaNotify.readNum} / ${mtmyOaNotify.readNum + mtmyOaNotify.unReadNum}
							</td>
							<td>
								<fmt:formatDate value="${mtmyOaNotify.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</td>
							<td>
								<fmt:formatDate value="${mtmyOaNotify.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</td>
							<td>
								<fmt:formatDate value="${mtmyOaNotify.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</td>
							<td>
								${fns:abbr(mtmyOaNotify.result,50)}
							</td>
							<td>
								${fns:abbr(mtmyOaNotify.msgTotal,50)}
							</td>
							<td>
								${fns:abbr(mtmyOaNotify.msgProcess,50)}
							</td>
							<td>
								${fns:abbr(mtmyOaNotify.clickNum,50)}
							</td>
							<td>
								${fns:abbr(mtmyOaNotify.pushNum,50)}
							</td>
							<td>
								<shiro:hasPermission name="ec:mtmyOaNotify:view">
									<a href="#" onclick="openDialogView('查看通知', '${ctx}/ec/mtmyOaNotify/form?id=${mtmyOaNotify.id}','800px', '500px')" class="btn btn-info btn-xs btn-circle" ><i class="fa fa-search-plus"></i></a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:mtmyOaNotify:edit">
			    					<a href="#" onclick="openDialog('修改通知', '${ctx}/ec/mtmyOaNotify/form?id=${mtmyOaNotify.id}','800px', '500px')" class="btn btn-success btn-xs btn-circle" ><i class="fa fa-edit"></i></a>
			    				</shiro:hasPermission>
			    				<shiro:hasPermission name="ec:mtmyOaNotify:del">
									<a href="${ctx}/ec/mtmyOaNotify/delete?id=${mtmyOaNotify.id}" onclick="return confirmx('确认要删除该通知吗？', this.href)"   class="btn btn-danger btn-xs btn-circle"><i class="fa fa-trash"></i></a>
								</shiro:hasPermission>
			
								<shiro:hasPermission name="ec:mtmyOaNotify:pushMsg">
									<c:if test="${mtmyOaNotify.status == 0}">
										<span onclick="pushMsg('${mtmyOaNotify.id}','${mtmyOaNotify.pushType}')" title="推送" class="btn btn-primary btn-xs btn-circle"><i class="fa fa-envelope-o"></i></span>
									</c:if>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:mtmyOaNotify:getPushResult">
									<c:if test="${mtmyOaNotify.status == 1 ||mtmyOaNotify.status == 2 }">
										<span onclick="pushResult('${mtmyOaNotify.id}','${mtmyOaNotify.contentId}')" title="获取推送结果" class="btn btn-warning btn-xs btn-circle"><i class="fa fa-search"></i></span>
									</c:if>
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
</body>
</html>