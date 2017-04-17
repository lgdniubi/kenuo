<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
<title>特殊美容师管理</title>
<meta name="decorator" content="default" />
<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
<script type="text/javascript">
	//刷新
	function refresh(){
		window.location="${ctx}/sys/specBeautician/list";
	}
	
	function newReset(){
		$("#pageNo").val(0);
		$(".form-group").val("");
		$(".form-control").val("");
		$("#searchForm").submit();
			return false;
	}
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>特殊美容师管理</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="specBeautician" action="${ctx}/sys/specBeautician/list" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span>选择市场：</span>
								<sys:treeselect id="bazaar" name="bazaarId" value="${specBeautician.bazaarId}" labelName="bazaarName" labelValue="${specBeautician.bazaarName}" title="市场" url="/sys/office/treeData?isGrade=true" cssClass=" form-control input-sm" allowClear="true" notAllowSelectRoot="false" notAllowSelectParent="true"/>
							</div>
							<span>姓&nbsp;&nbsp;&nbsp;名：</span>
								<form:input path="userName" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
							<span>手机号码：</span>
								<form:input path="userPhone" htmlEscape="false" maxlength="50" class=" form-control input-sm" /> 
						</form:form>
						<br />
					</div>
				</div>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="sys:specBeautician:add">
								<!-- 增加按钮 -->
								<table:addRow url="${ctx}/sys/specBeautician/form" title="特殊美容师" width="800px" height="600px"></table:addRow>
							</shiro:hasPermission>
							
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新">
								<i class="glyphicon glyphicon-repeat"></i> 刷新
							</button>
						</div>
						<div class="pull-right">
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()">
								<i class="fa fa-search"></i> 查询
							</button>
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="newReset()">
								<i class="fa fa-refresh"></i> 重置
							</button>
						</div>
					</div>
				</div>
				<!-- 主要内容展示 -->
				<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th style="text-align: center;">姓名</th>
							<th style="text-align: center;">手机号码</th>
							<th style="text-align: center;">所在市场</th>
							<th style="text-align: center;">所在店铺</th>
							<th style="text-align: center;">在职状态</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="specBeautician">
							<tr>
								<td style="text-align: center;">${specBeautician.userName}</td>
								<td style="text-align: center;">${specBeautician.userPhone}</td>
								<td style="text-align: center;">${specBeautician.bazaarName}</td>
								<td style="text-align: center;">${specBeautician.shopName}</td>
								<td style="text-align: center;">
									<c:if test="${specBeautician.status == 0}" >在职</c:if>
									<c:if test="${specBeautician.status == 1}" >离职</c:if>
								</td>
								<td style="text-align: center;">
									<shiro:hasPermission name="sys:specBeautician:view">
										<a href="#" onclick="openDialogView('查看', '${ctx}/sys/user/form?id=${specBeautician.userId}','800px','650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
									</shiro:hasPermission>
									
									<shiro:hasPermission name="sys:specBeautician:del">
									<a href="${ctx}/sys/specBeautician/delete?userId=${specBeautician.userId}" onclick=" return confirmx('确认要删除该特殊美容师吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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