<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>A级用户</title>
	<meta name="decorator" content="default" />
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
	<script type="text/javascript">
		function nowReset(){
			$("#pageNo").val(0);
			$("#searchForm div.form-group input").val("");
			$("#searchForm div.form-group select").val("");
			$("#searchForm").submit();
		}
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>A级用户</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="mtmySaleRelieve" action="${ctx}/ec/mtmySale/findAllA" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<form:input path="user.name" htmlEscape="false" maxlength="50" placeholder="用户姓名" class=" form-control input-sm" />
								<form:input path="user.mobile" htmlEscape="false" maxlength="50" placeholder="用户手机号" class=" form-control input-sm" />
								<span>归属店铺：</span>
								<sys:treeselect id="office" name="office.id" value="${mtmySaleRelieve.office.id}" labelName="office.name" labelValue="${mtmySaleRelieve.office.name}" title="部门"
									url="/sys/office/treeData?type=2" cssClass=" form-control input-sm" allowClear="true"
									notAllowSelectRoot="false" notAllowSelectParent="false" />
							</div>
							<div class="pull-right" style="margin-left: 50px;">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="nowReset()" ><i class="fa fa-refresh"></i> 重置</button>
							</div>
						</form:form>
					</div>
				</div>
				<p></p>
				<!-- 主要内容展示 -->
				<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th>级别</th>
							<th>妃子校用户归属店铺</th>
							<th>妃子校用户名</th>
							<th>每天美耶用户名</th>
							<th class="sort-column r.ABnum">B级用户</th>
							<th>剩余B级用户</th>
							<th class="sort-column rc.ACnum">AB级用户</th>
							<th>剩余AB级用户</th>
							<th class="sort-column uu.user_balance">总余额</th>
							<th class="sort-column uu.user_integral">总云币</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="mtmySaleRelieve">
							<tr>
								<td>A</td>
								<td>${mtmySaleRelieve.office.name}</td>
								<td>${mtmySaleRelieve.user.name}</td>
								<td>${mtmySaleRelieve.users.nickname}</td>
								<td>${mtmySaleRelieve.ABnum}</td>
								<td>
									<c:if test="${mtmyRuleParam.paramValue-mtmySaleRelieve.ABnum < 0}">
										0
									</c:if>
									<c:if test="${mtmyRuleParam.paramValue-mtmySaleRelieve.ABnum >= 0}">
										${mtmyRuleParam.paramValue-mtmySaleRelieve.ABnum }
									</c:if>
								</td>
								<td>${mtmySaleRelieve.ACnum}</td>
								<td>
									<c:if test="${ACmtmyRuleParam.paramValue-mtmySaleRelieve.ACnum < 0}">
										0
									</c:if>
									<c:if test="${ACmtmyRuleParam.paramValue-mtmySaleRelieve.ACnum >= 0}">
										${ACmtmyRuleParam.paramValue-mtmySaleRelieve.ACnum }
									</c:if>
								</td>
								<td><fmt:formatNumber type="number" value="${mtmySaleRelieve.users.usermoney}" maxFractionDigits="2"/></td>
								<td><fmt:formatNumber type="number" value="${mtmySaleRelieve.users.paypoints}" maxFractionDigits="2"/></td>
								<td>
									<a href="#" onclick='top.openTab("${ctx}/ec/mtmySale/userFrom?users.userid=${mtmySaleRelieve.users.userid}","A用户详情", false)' class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
									<a href="${ctx}/ec/mtmySale/moveA?userId=${mtmySaleRelieve.users.userid}" onclick="return confirmx('确定要转移该用户吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-share"></i> 转移到</a>
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