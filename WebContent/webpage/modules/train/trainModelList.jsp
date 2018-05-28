<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> 批量删除 由于样式问题  取消 --%>
<html>
<head>
	<title>版本管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<script type="text/javascript">
		//页面加载
		/* $(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 1,column:1}).show();
	    }); */
		
		//重置表单
		function resetnew(){
			$("#name").val("");
			reset();
		}
		
		//刷新
		function refresh(){
			window.location="${ctx}/train/model/findalllist";
		}
		
		
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">
			<div class="ibox">
				<div class="ibox-title">
					<h5>版本管理</h5>
				</div>
				<div class="ibox-content">
					<%-- <div class="searcharea clearfix">
						<form:form id="searchForm" action="${ctx}/train/model/findalllist" method="post" class="navbar-form navbar-left searcharea">
							<div class="form-group">
								<label>关键字：<input id="name" name="modEname" maxlength="10" type="text" class="form-control" value="${trainModel.modEname}" placeholder="请输入版本英文名称"></label> 
							</div>
							<shiro:hasPermission name="train:model:findalllist">
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
									<i class="fa fa-search"></i> 搜索
								</button>
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="resetnew()" >
									<i class="fa fa-refresh"></i> 重置
								</button>
							</shiro:hasPermission>
						</form:form>
					</div> --%>
					<!-- 工具栏 -->
					<%-- <div class="row">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="train:model:addmodel">
									<table:addRow url="${ctx}/train/model/addmodel" width="430px" height="480px" title="版本类型"></table:addRow><!-- 增加按钮 -->
								</shiro:hasPermission>
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
							</div>
						</div>
					</div> --%>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<!-- <th width="40" style="text-align: center;"><input type="checkbox" class="i-checks"></th>  批量删除 由于样式问题  取消 -->
								<th width="120" style="text-align: center;">序号</th>
								<th style="text-align: center">版本名称</th>
								<th width="230" style="text-align: center;">版本类型</th>
								<th width="230" style="text-align: center;">英文名称</th>
								<th width="230" style="text-align: center;">备注</th>
								<th width="300" style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody><%-- src="${trainModel.coverPic}" --%>
							<c:forEach items="${list}" var="trainModel">
								<tr id="${trainModel.id}" >
									<td nowrap style="text-align: center;"><i class="icon-menu.icon"></i>${trainModel.id}</td>
									<td>${trainModel.modName}</td>
									<td>
										<c:if test="${trainModel.modType eq 'pt'}">普通会员</c:if>
										<c:if test="${trainModel.modType eq 'syr'}">手艺人</c:if>
										<c:if test="${trainModel.modType eq 'qy'}">企业</c:if>
										<c:if test="${trainModel.modType eq 'dy'}">登云</c:if>
									</td>
									<td>${trainModel.modEname}</td>
									<td>${trainModel.remark}</td>
									<td style="text-align: left;">
										<shiro:hasPermission name="train:model:view">
											<a href="#" onclick="openDialogView('查看', '${ctx}/train/model/form?id=${trainModel.id}&opflag=VIEW','430px', '480px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="train:model:auth">	
				    						<a href="#" onclick="openDialog('fzx端权限设置', '${ctx}/train/model/auth?id=${trainModel.id}&opflag=fzx','430px', '600px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>fzx端权限设置</a>
				    						<c:if test="${trainModel.modType eq 'qy'}">
				    						<a href="#" onclick="openDialog('pc端权限设置', '${ctx}/train/model/auth?id=${trainModel.id}&opflag=pc','430px', '600px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>pc端权限设置</a>
				    						<a href="#" onclick="openDialog('media权限设置', '${ctx}/train/model/auth?id=${trainModel.id}&opflag=md','430px', '600px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>自媒体权限设置</a>
				    						</c:if>
				    						<c:if test="${trainModel.modType eq 'dy'}">
				    						<a href="#" onclick="openDialog('media权限设置', '${ctx}/train/model/auth?id=${trainModel.id}&opflag=md','430px', '600px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>自媒体权限设置</a>
				    						</c:if>
					    				</shiro:hasPermission>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>