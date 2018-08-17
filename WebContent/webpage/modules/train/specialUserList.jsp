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
	
	/* 添加权限 */
	function addFzxRole(title,url) {
		window.location=url;
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
							action="${ctx}/train/specialUser/list" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<div class="form-group">
								<span>姓名：</span>
								<form:input path="name" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
								<span>手机号码：</span>
								<form:input path="mobile" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
								<span>用户类型：</span>
								<select name="type">
									<option value="" >请选择</option>
									<option value="yg" <c:if test="${user.type== 'yg' }">selected="selected"</c:if> >员工</option>
									<option value="pt" <c:if test="${user.type== 'pt'}">selected="selected"</c:if> >普通会员</option>
									<option value="syr" <c:if test="${user.type== 'syr'}">selected="selected"</c:if> >手艺人</option>
									<option value="qy" <c:if test="${user.type== 'qy'}">selected="selected"</c:if> >企业</option>
								</select>
							</div>
						</form:form>
						<br />
					</div>
				</div>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
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
							<th class="sort-column mobile">手机号</th>
							<th class="sort-column name">姓名</th>
							<th class="sort-column name">昵称</th>
							<th class="sort-column phone">用户类型</th>
							<th class="sort-column phone">版本类型</th>
							<th class="sort-column phone">注册时间</th>
<!-- 							<th class="sort-column phone">用户状态</th> -->
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="user">
							<tr>
								<td>${user.mobile}</td>
								<td>${user.name}</td>
								<td>${user.nickname}</td>
								<td>${user.type}</td>
								<td>${user.modelName}</td>
								<td><fmt:formatDate value="${user.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<%-- <td>${user.userStatus}</td> --%>
								<td>
									<shiro:hasPermission name="sys:user:auth">
										<a href="#" onclick="openDialog('重置密码', '${ctx}/train/specialUser/resetPassword?id=${user.id}','400px', '300px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i>重置密码</a>
										<c:if test="${user.delFlag == 1}">
										<a href="${ctx}/train/specialUser/freeze?id=${user.id}&opflag=1"  class="btn btn-success btn-xs" ><i class="fa fa-search-plus"></i>解冻</a>
										</c:if>
										<c:if test="${user.delFlag == 0}">
										<a href="#" onclick="openDialog('冻结', '${ctx}/train/specialUser/freezeReason?id=${user.id}&opflag=2','300px', '300px')" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i>冻结</a>
										</c:if>
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