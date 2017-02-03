<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>会员列表</title>
<meta name="decorator" content="default" />

<script type="text/javascript"> 
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	
	
	
</script>
</head>




<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>会员列表</h5>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="trainLiveUser" action="${ctx}/train/liveUser/list" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							<label>直播申请编号：</label><input id="auditId"  name="auditId" value="${trainLiveUser.auditId}" maxlength="50" class=" form-control input-sm" />
							<label>会员姓名：</label><form:input path="name" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
							<label>手机号码：</label><form:input path="mobile" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
							<label>直播主题：</label><form:input path="title" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
						</div>
					</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top: 10px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="train:liveUser:add">
									<table:addRow url="${ctx}/train/liveUser/form" title="添加会员" width="600px" height="550px"></table:addRow><!-- 增加按钮 -->
								</shiro:hasPermission>
								<shiro:hasPermission name="train:liveUser:importPage">
									<!-- 导入数据 -->
									<a href="#" onclick="openDialog('导入会员', '${ctx}/train/liveUser/importPage','400px', '220px')" class="btn btn-white btn-sm"><i class="fa fa-folder-open-o"></i>导入会员</a>
								</shiro:hasPermission>
							</div>
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
							<th style="text-align: center;">直播申请编号</th>
							<th style="text-align: center;">直播标题</th>
							<th style="text-align: center;">直播时间</th>
							<th style="text-align: center;">姓名</th>
							<th style="text-align: center;">手机号</th>
							<th style="text-align: center;">职位</th>
							<th style="text-align: center;">购买金额</th>
							<th style="text-align: center;">支付方式</th>
							<th style="text-align: center;">有效期</th>
							<th style="text-align: center;">描述</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="live">
							<tr>
								<td>${live.id}</td>
								<td>${live.auditId}</td>
								<td>${live.title}</td>
								<td><fmt:formatDate value="${live.bengTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>${live.name}</td>			
								<td>${live.mobile}</td>
								<td>${live.position}</td>
								<td>${live.money}</td>
								<td>${live.payment}</td>
								<td><fmt:formatDate value="${live.validityDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>${live.remak}</td>
								<td>
									<shiro:hasPermission name="train:liveUser:view">
										<a href="#" onclick="openDialogView('查看直播会员', '${ctx}/train/liveUser/editform?id=${live.id}','600px','550px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>查看</a>
									</shiro:hasPermission> 
									<shiro:hasPermission name="train:liveUser:edit">
										<a href="#" onclick="openDialog('编辑直播会员','${ctx}/train/liveUser/editform?id=${live.id}','600px','550px')" 
											class="btn btn-success btn-xs"><i class="fa fa-edit"></i>修改</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="train:liveUser:del">
										<a href="${ctx}/train/liveUser/delete?id=${live.id}" onclick="return confirmx('确认要删除该会员吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
									</shiro:hasPermission>
									
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