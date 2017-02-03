<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>特长管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		//刷新
		function refresh(){
			window.location="${ctx}/sys/speciality/list";
		}
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>特长管理</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="speciality" action="${ctx}/sys/speciality/list" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
						</form:form>
						<br />
					</div>
				</div>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="sys:speciality:add">
								<table:addRow url="${ctx}/sys/speciality/form" title="特长" width="600px" height="200px" ></table:addRow><!-- 增加按钮 -->
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新">
								<i class="glyphicon glyphicon-repeat"></i> 刷新
							</button>
						</div>
					</div>
				</div>
				<!-- 主要内容展示 -->
				<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th class="sort-column name">特长描述</th>
							<th>操作</th>
						</tr>
					</thead>
					<c:forEach items="${page.list}" var="speciality">
						<tr>
							<td>
								${speciality.name}
							</td>
							<td>
								<shiro:hasPermission name="sys:speciality:view">
									<a href="#" onclick="openDialogView('查看用户', '${ctx}/sys/speciality/form?id=${speciality.id}','800px','200px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="sys:speciality:edit">
									<a href="#" onclick="openDialog('修改用户', '${ctx}/sys/speciality/form?id=${speciality.id}','800px','200px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="sys:speciality:del">
									<a href="${ctx}/sys/speciality/delete?id=${speciality.id}" onclick="return confirmx('确认要删除特长吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</c:forEach>
				</table>
				<!-- 分页table -->
				<table:page page="${page}"></table:page>
			</div>
		</div>
	</div>
</body>
</html>