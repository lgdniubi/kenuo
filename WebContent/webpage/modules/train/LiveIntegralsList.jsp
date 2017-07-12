<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>云币贡献管理</title>
<meta name="decorator" content="default" />
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>云币贡献管理</h5>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="trainLiveAudit" action="${ctx}/train/live/liveIntegralsList" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							<label>用户名：</label><form:input path="userName" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
							<label>手机号：</label><form:input path="mobile" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
							<span>归属机构：</span>
							<sys:treeselect id="organization" name="organization.id" value="${trainLiveAudit.organization.id}" labelName="organization.name" labelValue="${trainLiveAudit.organization.name}" title="部门" url="/sys/office/treeData?type=2" cssClass=" form-control input-sm" allowClear="true" notAllowSelectRoot="false" notAllowSelectParent="false" />
							<label>直播主题：</label><form:input path="title" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
						</div>
					</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top: 10px;">
						<div class="col-sm-12">
							<div class="pull-right">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
							</div>
						</div>
					</div>
				</div>
				<p></p>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">编号</th>
							<th style="text-align: center;">主播</th>
							<th style="text-align: center;">直播id</th>
							<th style="text-align: center;">直播主题</th>
							<th style="text-align: center;">直播描述</th>
							<th style="text-align: center;">直播开始时间</th>
							<th style="text-align: center;">所属机构</th>
							<th style="text-align: center;">获得云币</th>
							<th style="text-align: center;">平台抽取比例</th>
							<th style="text-align: center;">平台抽取云币</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="live">
							<tr>
								<td>${live.id}</td>
								<td>${live.userName}</td>
								<td>${live.roomId}</td>
								<td>${live.title}</td>
								<td>${live.desc}</td>
								<td><fmt:formatDate value="${live.bengTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>${live.organization.name}</td>
								<td>${live.userEarnings}</td>
								<td><fmt:formatNumber type="percent" value="${live.earningsRatio }" /></td>
								<td>${live.platformEarnings}</td>
							</tr>
						</c:forEach>
						
					</tbody>
					<tfoot>
						<tr>
							<td colspan="20">
								<!-- 分页代码 -->
								<div class="tfoot">
									<table:page page="${page}"></table:page>
								</div>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</div>
</body>
</html>