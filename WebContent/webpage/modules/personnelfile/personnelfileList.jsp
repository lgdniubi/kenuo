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
			maxlength : 10,
			formType : 2
		//prompt风格，支持0-2  0 文本框  1 密码框 2 多行文本
		}, function(pass) {
			var nowhref = href + '&delRemarks=' + pass;
			confirmx(lable, nowhref, closed);
		});
		return false;
	}
	//重置表单
	function resetnew(){
		$("#officeIdId").val("");
		$("#officeIdName").val("");
		reset();
	}
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>人事档案</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="personnelFile"
							action="${ctx}/personnelfile/user/list" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span>姓名：</span>
								<form:input path="sName" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
								<span>手机号：</span>
								<form:input path="mobile" htmlEscape="false" maxlength="11" class=" form-control input-sm" />
									<form:select path="parentDel" cssClass="form-control">
										<form:option value="0">在职</form:option>
										<form:option value="1">已离职</form:option>
									</form:select>
								<form:select path="filing" cssClass="form-control">
									<form:option value="">所有</form:option>
									<form:option value="0">未建档</form:option>
									<form:option value="1">已建档</form:option>
								</form:select>
							</div>
							<span>归属机构：</span>
							<sys:treeselect id="officeId" name="officeId" value="${personnelFile.officeId}" labelName="oName" labelValue="${personnelFile.oName}" title="部门"
								url="/sys/office/treeData?type=2" cssClass=" form-control input-sm" allowClear="true"
								notAllowSelectRoot="false" notAllowSelectParent="false" />
						</form:form>
						<br />
					</div>
				</div>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-right">
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()">
								<i class="fa fa-search"></i> 查询
							</button>
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="resetnew()">
								<i class="fa fa-refresh"></i> 重置
							</button>
						</div>
					</div>
				</div>
				<!-- 主要内容展示 -->
				<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th style="background-color: #5553;"><input type="checkbox" class="i-checks"></th>
							<th style="background-color: #5553;">姓名</th>
							<th style="background-color: #5553;">性别</th>
							<th style="background-color: #5553;">角色名称</th>
							<th style="background-color: #5553;">电话</th>
							<th style="background-color: #5553;">入职日期</th>
							<th style="background-color: #5553;">归属机构</th>
							<th style="background-color: #5553;">在职状态</th>
							<th style="background-color: #5553;">是否有档案</th>
							<th style="background-color: #5553;">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="personnelfile">
							<tr>
								<td><input type="checkbox" id="${personnelfile.id}" class="i-checks"></td>
								<td>${personnelfile.sName}</td>
								<td>
									<c:if test="${personnelfile.sex == 0 }">男</c:if> 
									<c:if test="${personnelfile.sex == 1 }">女</c:if> 
								</td>
								<td>
									<c:forEach items="${personnelfile.rolelist}" var="role">
										${role.name}
									</c:forEach>
								</td>
								<td>${personnelfile.mobile }</td>
								<td><fmt:formatDate value="${personnelfile.inductionTime}" pattern="yyyy-MM-dd"/></td>
								<td>${personnelfile.oName}</td>
								<td>
									<c:if test="${personnelfile.delFlag == 0 }">在职</c:if> 
									<c:if test="${personnelfile.delFlag == 1 }">已离职</c:if>
								</td>
								<td>
									<c:if test="${personnelfile.filing == 0}">
										<font color="red">未建档</font>
									</c:if>
									<c:if test="${personnelfile.filing == 1}">
										已建档
									</c:if>
								</td>
								<td>
									<!-- 增加按钮 -->
									<c:if test="${empty personnelfile.probationStartDate}">
										<shiro:hasPermission name="sys:personnelfile:add">
											<a href="${ctx}/personnelfile/user/form?id=${personnelfile.id}" class="btn btn-danger btn-xs">
												<i class="fa fa-plus"></i> 建档
											</a>
										</shiro:hasPermission>
									</c:if>
									<c:if test="${not empty personnelfile.probationStartDate}">
										<%-- <shiro:hasPermission name="sys:personnelfile:edit">
											<a href="${ctx}/personnelfile/user/getPersonnelFileBefor?id=${personnelfile.id}&&type=1" class="btn btn-success btn-xs">
												<i class="fa fa-edit"></i> 修改
											</a>
										</shiro:hasPermission> --%>
										<shiro:hasPermission name="sys:personnelfile:view">
											<a href="${ctx}/personnelfile/user/getPersonnelFileBefor?id=${personnelfile.id}&&type=2" class="btn btn-info btn-xs">
												<i class="fa fa-search-plus"></i> 查看
											</a> 
										</shiro:hasPermission>
									</c:if>
									<%-- <c:if test="${personnelfile.delFlag == 0 }">
										<a href="${ctx}/personnelfile/user/delete?id=${personnelfile.id}" onclick="return promptx('请填写删除备注信息！不可为空！','确定要删除用户吗？',this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
									</c:if> --%>
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