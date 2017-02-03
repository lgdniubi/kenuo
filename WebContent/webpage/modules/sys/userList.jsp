<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
<title>用户管理</title>
<meta name="decorator" content="default" />
<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
<script type="text/javascript">
	function promptx(title, lable, href, closed) {
		top.layer.prompt({
			title : title,
			maxlength : 100,
			formType : 2
		//prompt风格，支持0-2  0 文本框  1 密码框 2 多行文本
		}, function(pass) {
			var nowhref = href + '&delRemarks=' + pass;
			confirmx(lable, nowhref, closed);
		});
		return false;
	}
	//给父页面传值
	function parentDelchange() {
		parent.$('#parentDel').val($('#parentDel').val());
	}
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>用户列表</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="user"
							action="${ctx}/sys/user/list" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span>归属商家：</span>
								<sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" 
									labelValue="${user.company.name}" title="公司" 
									url="/sys/franchisee/treeData" cssClass=" form-control input-sm" allowClear="true" />
								<span>登录名：</span>
								<form:input path="loginName" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
								<span>归属机构：</span>
								<sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}" title="部门"
									url="/sys/office/treeData?type=2" cssClass=" form-control input-sm" allowClear="true"
									notAllowSelectRoot="false" notAllowSelectParent="false" />
								<span>姓&nbsp;&nbsp;&nbsp;名：</span>
								<form:input path="name" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
								<shiro:hasPermission name="sys:user:state">
									<form:select path="parentDel" onchange="parentDelchange(this.options[this.options.selectedIndex].value)" cssClass="form-control">
										<form:option value="0">在职</form:option>
										<form:option value="1">已离职</form:option>
									</form:select>
								</shiro:hasPermission>
							</div>
						</form:form>
						<br />
					</div>
				</div>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="sys:user:add">
								<!-- 增加按钮 -->
								<table:addRow url="${ctx}/sys/user/form" title="用户" width="800px" height="650px"></table:addRow>
							</shiro:hasPermission>
							<shiro:hasPermission name="sys:user:edit">
								<!-- 编辑按钮 -->
								<table:editRow url="${ctx}/sys/user/form" id="contentTable" title="用户" width="800px" height="680px" target="officeContent"></table:editRow>
							</shiro:hasPermission>
							<shiro:hasPermission name="sys:user:del">
								<!-- 删除按钮 -->
								<table:delRow url="${ctx}/sys/user/deleteAll" id="contentTable"></table:delRow>
							</shiro:hasPermission>
							<!-- update 导入数据 -->
							<shiro:hasPermission name="sys:user:importPage">
								<a href="#" onclick="openDialog('导入数据', '${ctx}/sys/user/importPage','400px', '220px')" class="btn btn-white btn-sm"><i class="fa fa-folder-open-o"></i>导入</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="sys:user:export">
								<!-- 导出按钮 -->
								<table:exportExcel url="${ctx}/sys/user/export"></table:exportExcel>
							</shiro:hasPermission>
							<shiro:hasPermission name="sys:user:importPage">
								<!-- 导入删除数据 -->
								<a href="#" onclick="openDialog('导入删除数据', '${ctx}/sys/user/importDelete','400px', '220px')" class="btn btn-white btn-sm"><i class="fa fa-folder-open-o"></i>导入删除数据</a>
							</shiro:hasPermission>
							
							<!-- 特长管理 新选项卡打开-->
							<shiro:hasPermission name="sys:speciality:list">
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/sys/speciality/list","特长管理", false)'><i class="fa fa-plus"></i> 特长管理</button>	
							</shiro:hasPermission>
							
							<!--技能标签管理 -->
							<shiro:hasPermission name="sys:skill:list">
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/sys/skill/list","技能标签管理", false)'><i class="fa fa-plus"></i> 技能标签管理</button>	
							</shiro:hasPermission>
							
							<!--特殊美容师管理 -->
							<shiro:hasPermission name="sys:specBeautician:list">
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/sys/specBeautician/list","特殊美容师管理", false)'><i class="fa fa-plus"></i> 特殊美容师管理</button>	
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
							<th class="sort-column login_name">登录名</th>
							<th class="sort-column name">姓名</th>
							<th class="sort-column phone">电话</th>
							<th class="sort-column mobile">手机</th>
							<th class="sort-column c.name">归属商家</th>
							<th class="sort-column o.name">归属店铺</th>
							<th>用户状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="user">
							<tr>
								<td><input type="checkbox" id="${user.id}" class="i-checks"></td>
								<td><a href="#" onclick="openDialogView('查看用户', '${ctx}/sys/user/form?id=${user.id}','800px', '650px')">${user.loginName}</a></td>
								<td>${user.name}</td>
								<td>${user.phone}</td>
								<td>${user.mobile}</td>
								<td>${user.company.name}</td>
								<td>${user.office.name}</td>
								<td>
									<c:if test="${user.delFlag == 0 }">在职</c:if> 
									<c:if test="${user.delFlag == 1 }">已离职</c:if>
								</td>
								<td>
									<shiro:hasPermission name="sys:user:view">
										<a href="#" onclick="openDialogView('查看用户', '${ctx}/sys/user/form?id=${user.id}','800px', '650px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
									</shiro:hasPermission> 
									<shiro:hasPermission name="sys:user:edit">
										<a href="#" onclick="openDialog('修改用户', '${ctx}/sys/user/form?id=${user.id}','800px', '650px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
									</shiro:hasPermission> 
									<shiro:hasPermission name="sys:user:del">
										<%-- <a href="${ctx}/sys/user/delete?id=${user.id}" onclick="return confirmx('确认要删除该用户吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a> --%>
										<c:if test="${user.delFlag == 0 }">
											<a href="${ctx}/sys/user/delete?id=${user.id}" onclick="return promptx('请填写删除备注信息！不可为空！','确定要删除用户吗？',this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
										</c:if>
									</shiro:hasPermission>
									<shiro:hasPermission name="sys:user:auth">
										<a href="#" onclick="openDialog('权限设置', '${ctx}/sys/user/auth?id=${user.id}','800px', '650px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i> 权限设置</a>
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