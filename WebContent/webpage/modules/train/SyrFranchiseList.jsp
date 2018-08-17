<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> 批量删除 由于样式问题  取消 --%>
<html>
<head>
	<title>手艺人商家列表</title>
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
			$("#mobile").val("");
			$("#modId").val("");
			$("#name").val("");
			reset();
		}
		
		//刷新 
		function refresh(){
			window.location="${ctx}/train/syr/list";
		}
		
		var pageNo = '${page.pageNo}';
		function isPermiss(id,userid,auditType){
			$.ajax({
	             type: "GET",
	             url: "${ctx}/train/userCheck/isPermiss",
	             data: {userid:userid,id:id},
	             dataType: "json",
	             success: function(data){
	            	 if(data){
	         			var urlPermiss = "${ctx}/train/syr/form?id="+id+"&userid="+userid+"&type="+auditType+"&pageNo="+pageNo+"&opflag=setPermiss";
	         			openDialog('权限设置', urlPermiss,'800px', '550px')
	            	 } else{
	            		 layer.msg('此认证信息不能授权'); 
	            	 }       
	        	 }
			});
		}
		
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">
			<div class="ibox">
				<div class="ibox-title">
					<h5>手艺人商家列表</h5>
				</div>
				<div class="ibox-content">
					<div class="searcharea clearfix">
						<form:form id="searchForm" modelAttribute="syrFranchise" action="${ctx}/train/syr/list" method="post" class="navbar-form navbar-left searcharea">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<div class="form-group">
								<label>手机号：<input id="mobile" name="mobile" maxlength="11" type="text" class="form-control" value="${syrFranchise.mobile}" placeholder="请输入手机号"></label> 
								<label>姓名：</label>
								<form:input path="name" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
								<label>版本类型：</label>
								<select name="modId" id="modId">
									<option value="" >请选择</option>
									<option value="3" <c:if test="${syrFranchise.modId== '3' }">selected="selected"</c:if> >手艺人免费</option>
									<option value="4" <c:if test="${syrFranchise.modId== '4'}">selected="selected"</c:if> >手艺人收费</option>
								</select>
							</div>
							<shiro:hasPermission name="train:syr:list">
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
							<tr>
								<th width="120" style="text-align: center;">序号</th>
								<th width="120" style="text-align: center;">手机号</th>
								<th width="230" style="text-align: center">姓名</th>
								<th width="230" style="text-align: center;">昵称</th>
								<th width="230" style="text-align: center;">版本类型</th>
								<th width="230" style="text-align: center;">到期时间</th>
								<th width="300" style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="syrFranchise">
								<tr id="${syrFranchise.id}" >
									<td>${syrFranchise.id}</td>
									<td>${syrFranchise.mobile}</td>
									<td>${syrFranchise.name}</td>
									<td>${syrFranchise.nickname}</td>
									<td>
										<c:if test="${syrFranchise.modId eq 3}">手艺人免费</c:if>
										<c:if test="${syrFranchise.modId eq 4}">手艺人收费</c:if>
									</td>
									<td><fmt:formatDate value="${syrFranchise.authEndDate}" pattern="yyyy-MM-dd"/> </td>
									<td style="text-align: left;">
									<shiro:hasPermission name="train:userCheck:update">
										<c:if test="${syrFranchise.status == 3}">
						    				<a href="#" onclick="isPermiss('${syrFranchise.id}','${syrFranchise.userid }','${syrFranchise.auditType}')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>权限设置</a>
										</c:if>
										<c:if test="${syrFranchise.status != 0}">
					    					<a href="#" onclick="openDialogView('手艺人信息', '${ctx}/train/userCheck/form?id=${syrFranchise.id}&userid=${syrFranchise.userid }&opflag=view','800px', '700px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>查看</a>
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
	</div>
	<div class="loading"></div>
</body>
</html>