<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>商家列表</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<%@include file="/webpage/include/treetable.jsp" %>
	<script type="text/javascript">
		//页面加载
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 1,column:1}).show();
	    });
		
		//刷新
		function refresh(){
			window.location="${ctx}/sys/franchisee/list";
		}
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">
			<div class="ibox">
				<div class="ibox-title">
					<h5>商家管理</h5>
				</div>
				<div class="ibox-content">
					<div class="searcharea clearfix">
						<form:form id="searchForm" action="${ctx}/sys/franchisee/list" method="post" class="navbar-form navbar-left searcharea">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<input id="status" name="status" type="hidden" value="${status}" />
							<div class="form-group">
								<label>关键字：<input id="name" name="name" maxlength="11" type="text" class="form-control" value="${franchisee.name}" placeholder="请输入企业名称"></label> 
							</div>
							
							<shiro:hasPermission name="train:userCheck:findalllist">
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
									<i class="fa fa-search"></i> 搜索
								</button>
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="resetnew()" >
									<i class="fa fa-refresh"></i> 重置
								</button>
							</shiro:hasPermission>
						</form:form>
					</div>
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<div class="pull-left">
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
							</div>
						</div>
					</div>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<th style="text-align: center;display: none;"></th>
							<th style="text-align: center;">加盟商名称</th>
							<th style="text-align: center;">加盟商属性</th>
							<th style="text-align: center;">省份</th>
							<th style="text-align: center;">市区</th>
							<th style="text-align: center;">县</th>
							<th style="text-align: center;">邮政编码</th>
							<th style="text-align: center;">联系人</th>
							<th style="text-align: center;">电话</th>
							<th style="text-align: center;">详细地址</th>
							<th style="text-align: center;">操作</th>
						</thead>
						<tbody>
							<c:forEach items="${list}" var="franchisee">
								<tr pid="${franchisee.parentId}" id="${franchisee.id}">
									<td style="display: none;"></td>
									<td nowrap style="text-align: left;"><i class="icon-menu.icon"></i>${franchisee.name}</td>
									<td>${franchisee.type}</td>
									<td>${franchisee.province}</td>
									<td>${franchisee.city}</td>
									<td>${franchisee.district}</td>
									<td>${franchisee.zipcode}</td>
									<td>${franchisee.contacts}</td>
									<td>${franchisee.mobile}</td>
									<td>${franchisee.address}</td>
									<td style="text-align: left;">
										<shiro:hasPermission name="sys:franchisee:view">
											<a href="#" onclick="openDialogView('查看商家', '${ctx}/sys/franchisee/form?id=${franchisee.id}&opflag=VIEW','800px', '620px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="sys:franchisee:edit">
				    						<a href="#" onclick="openDialog('修改商家', '${ctx}/sys/franchisee/form?id=${franchisee.id}&opflag=UPDATE','800px', '620px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
					    				</shiro:hasPermission>
					    				
					    				<shiro:hasPermission name="sys:franchisee:permissEdit">  
					    					<a href="#" onclick="openDialog('权限设置', '${ctx}/sys/franchisee/permissform?companyId=${franchisee.id}&type=qy&opflag=setPermiss','800px', '550px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>权限设置</a>
					    				</shiro:hasPermission>
					    				
					    				<%-- <shiro:hasPermission name="sys:franchisee:del">
											<a href="${ctx}/sys/franchisee/delete?id=${franchisee.id}" onclick="return confirmx('确定要删除该商家吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
										</shiro:hasPermission> --%>
									<shiro:hasPermission name="sys:franchisee:description">
										<a onclick="openDialog('商家详情', '${ctx}/sys/franchisee/franchiseeDsecriptionForm?id=${franchisee.id}','800px','620px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>商家详情</a>
									</shiro:hasPermission>
										<%-- 
										<c:if test="${franchisee.code == '1'}">
											<shiro:hasPermission name="sys:franchisee:add">
												<a href="#" onclick="openDialog('添加下级商家', '${ctx}/sys/franchisee/form?parent.id=${franchisee.id}&opflag=ADD','800px', '620px')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i> 添加下级商家</a>
											</shiro:hasPermission>
										</c:if> --%>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>