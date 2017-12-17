<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
	<head>
		<title>部门管理</title>
		<meta name="decorator" content="default" />
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
	</head>
	<body>
		<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>部门列表</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="department"
							action="${ctx}/train/department/list" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span>部门名称：</span>
								<form:input path="name" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
							</div>
						</form:form>
						<br />
					</div>
				</div>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="train:department:add">
								<!-- 增加按钮 -->
								<table:addRow url="${ctx}/train/department/form" title="部门" width="800px" height="650px"></table:addRow>
							</shiro:hasPermission>
							<shiro:hasPermission name="train:department:del">
								<!-- 删除按钮 -->
								<table:delRow url="${ctx}/train/department/deleteAll" id="contentTable"></table:delRow>
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新">
								<i class="glyphicon glyphicon-repeat"></i> 刷新
							</button>
						</div>
						<div class="pull-right">
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()">
								<i class="fa fa-search"></i> 查询
							</button>
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()">
								<i class="fa fa-refresh"></i> 重置
							</button>
						</div>
					</div>
				</div>
				<!-- 主要内容展示 -->
				<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th><input type="checkbox" class="i-checks"></th>
							<th class="sort-column name">部门名称</th>
							<th class="sort-column name">归属商家</th>
							<th class="sort-column sort">排序</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="department">
							<tr>
								<td><input type="checkbox" id="${department.dId}" class="i-checks"></td>
								<td><a href="#" onclick="openDialogView('查看部门', '${ctx}/train/department/form?dId=${department.dId}','800px', '650px')">${department.name}</a></td>
								<td>${department.office.name}</td>
								<td>${department.sort}</td>
								<td>
									<shiro:hasPermission name="train:department:view">
										<a href="#" onclick="openDialogView('查看部门', '${ctx}/train/department/form?dId=${department.dId}','800px', '650px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
									</shiro:hasPermission> 
									<shiro:hasPermission name="train:department:edit">
										<a href="#" onclick="openDialog('修改部门', '${ctx}/train/department/form?dId=${department.dId}','800px', '650px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
									</shiro:hasPermission> 
									<shiro:hasPermission name="train:department:del">
										<c:if test="${department.delFlag == 0 }">
											<a href="${ctx}/train/department/deleteAll?ids=${department.dId}" onclick="return confirmx('确定要删除该部门吗？',this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
										</c:if>
									</shiro:hasPermission>
									<shiro:hasPermission name="train:position:view">
										<a href="#" onclick="openDialogView('职位管理', '${ctx}/train/department/positionList?dId=${department.dId}','800px', '650px')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i> 职位管理</a>
									</shiro:hasPermission>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<!-- 分页table -->
				<table:page page="${page}"></table:page>
			</div>
		</div>
	</div>
	</body>
</html>