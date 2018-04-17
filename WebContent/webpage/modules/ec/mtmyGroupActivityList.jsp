<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>团购活动列表</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	function page(n,s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
        
	$(document).ready(function() {
		var activityStartDate = {
		    elem: '#activityStartDate',
		    format: 'YYYY-MM-DD',
		    event: 'focus',
		    istime: false,
		    isclear: true,
		    istoday: true,
		    issure: true,
		    festival: true,
		    choose: function(datas){
		    	
		    }
		};
		laydate(activityStartDate);
    });
</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<c:if test="${mtmyGroupActivity.parent == null or mtmyGroupActivity.parent.id == '0' or mtmyGroupActivity.parent.id == ''}">
					<h5>团购项目列表</h5>
				</c:if>
				<c:if test="${mtmyGroupActivity.parent != null and mtmyGroupActivity.parent.id != '0' and mtmyGroupActivity.parent.id != ''}">
					<h5>团购批次列表</h5>
				</c:if>
				<div class="ibox-tools">
					<c:if test="${mtmyGroupActivity.parent != null and mtmyGroupActivity.parent.id != '0' and mtmyGroupActivity.parent.id != ''}">
						<a href="${ctx}/ec/groupActivity/list" class="btn btn-info btn-xs" ><i class="fa fa-arrow-left"></i> 返回</a>
					</c:if>
				</div>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="mtmyGroupActivity" action="${ctx}/ec/groupActivity/list" method="post" class="form-inline">
						<input id="parent.id" name="parent.id" type="hidden" value="${mtmyGroupActivity.parent.id}"/>
						<div class="form-group">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<label>项目时间：</label>
							<input id="activityStartDate" name="activityStartDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${mtmyGroupActivity.activityStartDate}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="项目时间" readonly="readonly"/>
							<label>项目状态：</label>
							<form:select path="activityStatus" class="form-control">
								<form:option value="">全部</form:option>
								<form:option value="0">即将开启</form:option>
								<form:option value="1">进行中</form:option>
								<form:option value="2">已结束</form:option>
							</form:select>
							<label>项目名称：</label>
							<form:input path="name" cssClass="form-control"/>
						</div>
					</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top: 10px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="ec:groupActivity:add">
									<c:if test="${mtmyGroupActivity.parent == null or mtmyGroupActivity.parent.id == '0' or mtmyGroupActivity.parent.id == ''}">
										<a href="#" onclick="openDialog('新增团购项目', '${ctx}/ec/groupActivity/form?parent.id=${mtmyGroupActivity.parent.id}','700px','500px')" class="btn btn-white btn-sm"><i class="fa fa-plus"></i>新增团购项目</a>
									</c:if>
									<c:if test="${mtmyGroupActivity.parent != null and mtmyGroupActivity.parent.id != '0' and mtmyGroupActivity.parent.id != ''}">
										<a href="#" onclick="openDialog('新增团购批次', '${ctx}/ec/groupActivity/form?parent.id=${mtmyGroupActivity.parent.id}','700px','500px')" class="btn btn-white btn-sm"><i class="fa fa-plus"></i>新增团购批次</a>
									</c:if>
								</shiro:hasPermission>
							</div>
							<div class="pull-right">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
							</div>
						</div>
					</div>
				</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">项目名称</th>
							<th style="text-align: center;">项目状态</th>
							<th style="text-align: center;">项目有效时间</th>
							<th style="text-align: center;">项目创建时间</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="mtmyGroupActivity">
							<tr>
								<td>${mtmyGroupActivity.id}</td>
								<td>${mtmyGroupActivity.name}</td>
								<td>
									<c:if test="${mtmyGroupActivity.activityStatus == 0}">即将开启</c:if>
									<c:if test="${mtmyGroupActivity.activityStatus == 1}">进行中</c:if>
									<c:if test="${mtmyGroupActivity.activityStatus == 2}">已结束</c:if>
								</td>
								<td>
									<fmt:formatDate value="${mtmyGroupActivity.activityStartDate}" pattern="yyyy-MM-dd" /> 至 
									<fmt:formatDate value="${mtmyGroupActivity.activityEndDate}" pattern="yyyy-MM-dd" />
								</td>
								<td>
									<fmt:formatDate value="${mtmyGroupActivity.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />
								</td>	
								<td>
									<c:if test="${mtmyGroupActivity.parent.id == '0'}">
										<a href="${ctx}/ec/groupActivity/list?parent.id=${mtmyGroupActivity.id}" class="btn btn-info btn-xs" ><i class="fa fa-edit"></i> 团购批次</a>
									</c:if>
									<c:if test="${mtmyGroupActivity.parent.id != '0'}">
										<a href="${ctx}/ec/groupActivity/goodsList?mtmyGroupActivity.id=${mtmyGroupActivity.id}&mtmyGroupActivity.parent.id=${mtmyGroupActivity.parent.id}" class="btn btn-info btn-xs" ><i class="fa fa-edit"></i> 商品</a>
									</c:if>
									<%-- <shiro:hasPermission name="ec:groupActivity:edit"> --%>
										<a href="#" onclick="openDialog('修改团购项目', '${ctx}/ec/groupActivity/form?id=${mtmyGroupActivity.id}','700px', '500px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
									<%-- </shiro:hasPermission>
									<shiro:hasPermission name="ec:groupActivity:del"> --%>
										<a href="${ctx}/ec/groupActivity/delete?id=${mtmyGroupActivity.id}" onclick="return confirmx('要删除该团购项目吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
									<%-- </shiro:hasPermission> --%>
								</td>
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