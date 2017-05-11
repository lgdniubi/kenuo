<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>分配角色</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	
	<div class="wrapper wrapper-content">
	<div class="container-fluid breadcrumb">
		<div class="row-fluid span12">
			<span class="span4">角色名称: <b>${fzxRole.name}</b></span>
		</div>
	</div>
	<sys:message content="${message}"/>
	<div class="breadcrumb">
		<form id="assignRoleForm" action="${ctx}/train/fzxRole/assignrole" method="post" class="hide">
			<input type="hidden" name="roleId" value="${fzxRole.roleId}"/>
			<input type="hidden" name="name" value="${fzxRole.name}"/>
			<input id="idsArr" type="hidden" name="idsArr" value=""/>
		</form>
		<form:form id="searchForm" modelAttribute="user" action="${ctx}/train/fzxRole/assign" method="post" class="form-inline">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
			<input id="fzxRole.roleId" name="fzxRole.roleId" type="hidden" value="${user.fzxRole.roleId}" />
		</form:form>
		<button id="assignButton" type="submit"  class="btn btn-outline btn-primary btn-sm" title="添加人员"><i class="fa fa-plus"></i> 添加人员</button>
		<script type="text/javascript">
			$("#assignButton").click(function(){
				top.layer.open({
				    type: 2, 
				    area: ['800px', '600px'],
				    title:"选择用户",
			        maxmin: true, //开启最大化最小化按钮
				    content: "${ctx}/train/fzxRole/usertoFzxRole?roleId=${user.fzxRole.roleId}" ,
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){
		    	        var pre_ids = layero.find("iframe")[0].contentWindow.pre_ids;
						var ids = layero.find("iframe")[0].contentWindow.ids;
						if(ids[0]==''){
								ids.shift();
								pre_ids.shift();
							}
							if(pre_ids.sort().toString() == ids.sort().toString()){
								top.layer.alert("未给角色分配新成员！", {icon: 0, title:'提醒'});
								return false;
							};
					    	// 执行保存
					    	loading('正在提交，请稍等...');
					    	var idsArr = "";
					    	for (var i = 0; i<ids.length; i++) {
					    		idsArr = (idsArr + ids[i]) + (((i + 1)== ids.length) ? '':',');
					    	}
					    	$('#idsArr').val(idsArr);
					    	$('#assignRoleForm').submit();
						    top.layer.close(index);
					  },
					  cancel: function(index){ 
		    	       }
				}); 
			});
		</script>
	</div>
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead><tr><th>归属公司</th><th>归属部门</th><th>登录名</th><th>姓名</th><th>电话</th><th>手机</th><shiro:hasPermission name="sys:user:edit"><th>操作</th></shiro:hasPermission></tr></thead>
			<tbody>
				<c:forEach items="${page.list}" var="user">
					<tr>
						<td>${user.company.name}</td>
						<td>${user.office.name}</td>
						<td><a href="${ctx}/sys/user/form?id=${user.id}">${user.loginName}</a></td>
						<td>${user.name}</td>
						<td>${user.phone}</td>
						<td>${user.mobile}</td>
						<shiro:hasPermission name="train:fzxRole:edit"><td>
							<a href="${ctx}/train/fzxRole/outrole?userId=${user.id}&fzxRoleId=${fzxRole.roleId}" 
								onclick="return confirmx('确认要将用户<b>[${user.name}]</b>从<b>[${fzxRole.name}]</b>角色中移除吗？', this.href)">移除</a>
						</td></shiro:hasPermission>
					</tr>
				</c:forEach>
			</tbody>
	</table>
	<table:page page="${page}"></table:page>
	</div>
</body>
</html>
