<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
	<head>
		<title>权限设置</title>
		<meta name="decorator" content="default" />
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
		<script type="text/javascript">
			function refresh() {
				var userId = "${user.id}";
				window.location="${ctx}/sys/user/addFzxRole?id="+userId
			}
			
			//$("#addRoleButton").click(function(){
			function addRoleButton(){				
				top.layer.open({
				    type: 2, 
				    area: ['800px', '600px'],
				    title:"选择权限",
			        maxmin: true, //开启最大化最小化按钮
				    content: "${ctx}/train/fzxRole/addFzxRoleForm?fzxRoleIds=${fzxRoleIds}&userId=${user.id}",
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){
		    	       var fzxRoleId = layero.find("iframe")[0].contentWindow.$.fn.zTree.getZTreeObj("fzxRoleTree").getCheckedNodes(true);
						var officeTreeIds = layero.find("iframe")[0].contentWindow.$.fn.zTree.getZTreeObj("officeTree").getCheckedNodes(true);
						if (fzxRoleId != "") {
							var officeIds = [];
							for (var i = 0; i < officeTreeIds.length; i++) {
								officeIds.push(officeTreeIds[i].id); 
							}
							$("#addfzxRoleId").val(fzxRoleId[0].id);
							$("#addofficeIds").val(officeIds);
							var a = $("#addofficeIds").val();
					    	$('#saveFzxRoleFrom').submit();
						    top.layer.close(index);
						}else{
							alert("角色不能为空");
						}
					  },
					  cancel: function(index){ 
		    	       }
				}); 
					}
		
			function editButton(value){
				var userId = "${user.id}";
				top.layer.open({
				    type: 2, 
				    area: ['420px', '650px'],
				    title:"修改权限",
			        maxmin: true, //开启最大化最小化按钮
				    content: "${ctx}/sys/user/editOfficeFrom?userId=${user.id}&roleId="+value,
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){
		    	       var officeTreeIds = layero.find("iframe")[0].contentWindow.$.fn.zTree.getZTreeObj("editOffice").getCheckedNodes(true);
		    	       var returnId = layero.find("iframe")[0].contentWindow.document.getElementById("id").value;
						var officeIds = [];
						for (var i = 0; i < officeTreeIds.length; i++) {
							officeIds.push(officeTreeIds[i].id); 
						}
						$("#returnId").val(returnId);
						$("#officeIds").val(officeIds);
						$("#returnId").val();
				    	$("#editForm").submit();
					   top.layer.close(index);
					  },
					  cancel: function(index){ 
		    	       }
				}); 
			}
		</script>
	<body>
		<form action="${ctx}/sys/user/updateOffice" id="editForm" method="post">
			<input type="hidden" id="userId" name="userId" value="${user.id }">
			<input type="hidden" id="returnId" name="id">
			<input type="hidden" id="officeIds" name="officeIds" >
		</form>
		<div class="wrapper wrapper-content">
			<div class="ibox">
				<!-- 标题 -->
				<div class="ibox-title">
					<h5>权限设置</h5>
				</div>
				<!-- 主体内容 -->
				<div class="ibox-content">
					<sys:message content="${message}" />
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<div class="pull-left">
								<%-- <shiro:hasPermission name="sys:user:add"> --%>
									<!-- 增加按钮 -->
									<%-- <table:addRow url="${ctx}/train/fzxRole/addFzxRoleForm" title="添加角色" width="800px" height="600px"></table:addRow> --%>
									<form id="saveFzxRoleFrom" action="${ctx}/sys/user/saveFzxRoleOfficeById" method="post" class="hide">
										<input type="hidden" name="userId" value="${user.id}"/>
										<input type="hidden" name="fzxRoleId" id="addfzxRoleId"/>
										<input type="hidden" name="officeIds" id="addofficeIds"/>
									</form>
									<button id="addRoleButton" type="submit"  class="btn btn-outline btn-primary btn-sm" title="添加" onclick="addRoleButton()"><i class="fa fa-plus"></i> 添加</button>
									<%-- <a href="#" onclick="openDialog('添加权限', '${ctx}/train/fzxRole/addFzxRoleForm?fzxRoleIds=${fzxRoleIds}&userId=${user.id}','800px', '600px')"  class="btn btn-outline btn-primary btn-sm" ><i class="fa fa-plus"></i>添加</a>
								</shiro:hasPermission> --%>
								
								<!-- <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="onRefresh()" title="刷新">
									<i class="glyphicon glyphicon-repeat"></i> 刷新 -->
									<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
								<!-- </button> -->
							</div>
						</div>
					</div>
					<!-- 主要内容展示 -->
					<table id="contentTable" class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">角色</th>
								<th style="text-align: center;width: 300px">权限</th>
								<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody id="contentTbody">
							<form:form id="addFzxRoleForm" action="" method="post">
							<c:forEach items="${fzxRoleList}" var="fzxRole">
								<input type="hidden" id="${fzxRole.roleId }" value="${fzxRole.roleId }">
								<tr>
									<td style="text-align: center;">
										${fzxRole.name}</td>
									<c:if test="${not empty map }">
									<c:forEach items="${map}" var="map">
										<c:if test="${fzxRole.roleId == map.key }">
											<td style="text-align: center;">${map.value}</td>
										</c:if>
									</c:forEach>
									</c:if>
									<td style="text-align: center;">
										<shiro:hasPermission name="sys:user:edit">
											<%-- <a href="#" onclick="openDialog('修改权限', '${ctx}/sys/user/editOfficeFrom?userId=${user.id}&roleId=${fzxRole.roleId}','400px', '650px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a> --%>
											<button id="editOffice" type="button"  class="btn btn-success btn-xs" title="添加" onclick="editButton('${fzxRole.roleId}')"><i class="fa fa-edit"></i> 修改</button>
										</shiro:hasPermission>
										
										<shiro:hasPermission name="sys:user:del">
										<a href="${ctx}/sys/user/delFzxRoleByUser?userId=${user.id}&roleId=${fzxRole.roleId}" onclick=" return confirmx('确认要删除该角色以及权限吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
									</shiro:hasPermission>
									</td>
								</tr>
							</c:forEach>
							</form:form>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</body>
</html>