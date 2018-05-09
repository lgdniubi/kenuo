<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> 批量删除 由于样式问题  取消 --%>
<html>
<head>
	<title>用户审核</title>
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
			window.location="${ctx}/train/userCheck/findalllist";
		}
		//已审核
		function hasCheck(){
			window.location="${ctx}/train/userCheck/findalllist?status=2";
		}
		//待审核
		function wait(){
			window.location="${ctx}/train/userCheck/findalllist?status=0";
		}
		//审核按钮
		function checkBtn(id,userid,type){
			top.layer.open({
			    type: 2, 
			    area: ['800px', '650px'],
			    title:"审核",
			    content: "${ctx}/train/userCheck/form?id="+id+"&userid="+userid ,
			    btn: ['通过','不通过'],
			    yes: function(index, layero){ //或者使用btn1
			    	window.location="${ctx}/train/userCheck/save?id="+id+"&userid="+userid+"&status=2";
					top.layer.close(index);
				},
				btn2: function(index, layero){
			    //按钮【不通过】的回调
			    	var url = "${ctx}/train/userCheck/refuseForm?id="+id+"&userid="+userid+"&status=1&auditType="+type;
			    	openDialog('拒绝', url,'300px', '300px')
			    	//window.location="${ctx}/train/userCheck/save?id="+id+"&userid="+userid+"&status=1";
					top.layer.close(index);
			    //return false 开启该代码可禁止点击该按钮关闭
			  	},
	    	cancel: function(){ //或者使用btn2
	    	           //按钮【按钮二】的回调
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
					<h5>用户审核</h5>
				</div>
				<div class="ibox-content">
					<div class="searcharea clearfix">
						<form:form id="searchForm" action="${ctx}/train/userCheck/findalllist" method="post" class="navbar-form navbar-left searcharea">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<div class="form-group">
								<label>关键字：<input id="name" name="mobile" maxlength="11" type="text" class="form-control" value="${userCheck.mobile}" placeholder="请输入手机号"></label> 
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
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="search()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="wait()" title="待审核">待审核</button>
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="hasCheck()" title="已审核">已审核</button>
							</div>
						</div>
					</div>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<th width="120" style="text-align: center;">手机号</th>
								<th width="230" style="text-align: center">姓名</th>
								<th width="230" style="text-align: center;">昵称</th>
								<th width="230" style="text-align: center;">会员类型</th>
								<th width="230" style="text-align: center;">认证类型</th>
								<th width="230" style="text-align: center;">申请时间</th>
								<th width="230" style="text-align: center;">状态</th>
								<th width="300" style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody><%-- src="${userCheck.coverPic}" --%>
							<c:forEach items="${page.list}" var="userCheck">
								<tr id="${userCheck.id}" >
									<td>${userCheck.mobile}</td>
									<td>${userCheck.name}</td>
									<td>${userCheck.nickname}</td>
									<td>
										<c:if test="${userCheck.type eq 'pt'}">普通会员</c:if>
										<c:if test="${userCheck.type eq 'syr'}">手艺人</c:if>
										<c:if test="${userCheck.type eq 'qy'}">企业</c:if>
									</td>
									<td>
										<c:if test="${userCheck.auditType eq 'syr'}">手艺人</c:if>
										<c:if test="${userCheck.auditType eq 'qy'}">企业</c:if>
									</td>
									<td><fmt:formatDate value="${userCheck.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
									<td>
										<c:if test="${userCheck.status == 0}">待审核</c:if>
										<c:if test="${userCheck.status == 1}">未通过</c:if>
										<c:if test="${userCheck.status == 2}">已通过</c:if>
										<c:if test="${userCheck.status == 4}">不能操作</c:if>
									</td>
									<td style="text-align: left;">
									<shiro:hasPermission name="train:userCheck:update">
										<c:if test="${userCheck.status == 2}">
					    						<a href="#" onclick="openDialog('权限设置', '${ctx}/train/userCheck/form?id=${userCheck.id}&userid=${userCheck.userid }&type=${userCheck.auditType}&opflag=setPermiss','800px', '550px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>权限设置</a>
										</c:if>
										<c:if test="${userCheck.status == 0}">
					    						<a href="#" onclick="checkBtn(${userCheck.id},'${userCheck.userid}','${userCheck.auditType}')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>审核</a>
										</c:if>
										<c:if test="${userCheck.status != 0}">
					    				<a href="#" onclick="openDialogView('查看审核信息', '${ctx}/train/userCheck/form?id=${userCheck.id}&userid=${userCheck.userid }&opflag=view','800px', '550px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>查看</a>
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