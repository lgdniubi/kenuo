<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <script>
	   function search(){//查询，页码清零
			$("#pageNo").val(0);
			$("#searchForm").submit();
	   		return false;
	   }
		function page(n,s){//翻页
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			return false;
		}
		$(function(){
			$(".imgUrl img").each(function(){
				var $this = $(this),
					$src = $this.attr('data-src');  
				$this.attr({'src':$src})
			});
			
		});
    </script>
    <title>自媒体角色管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>自媒体色管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="mediaRole" action="${ctx}/train/mdrole/list" method="post">
                    	<form:hidden path="opflag"/>
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							角色名称：<form:input path="name" htmlEscape="false" placeholder="搜索角色名称" class=" form-control input-sm" />
						</div>
                    </form:form>
                </div>
                <!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<c:if test="${mediaRole.opflag eq 'dy'}">
							<shiro:hasPermission name="train:mdrole:add">
								<!-- 增加按钮 -->
								<table:addRow url="${ctx}/train/mdrole/form?opflag=${mediaRole.opflag }" title="新增角色" width="800px" height="650px"></table:addRow>
							</shiro:hasPermission>
							</c:if>
						</div>
						<div class="pull-right">
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()">
								<i class="fa fa-search"></i> 查询
							</button>
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()">
								<i class="fa fa-refresh"></i> 重置
							</button>
						</div>
					</div>
				</div>
                <div class="" id="reviewlists">
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">ID</th>
								<th style="text-align: center;">角色名称</th>
							    <th style="text-align: center;">版本名称</th>
							    <th style="text-align: center;">角色类型</th>
							    <th style="text-align: center;">发布平台</th>
						    	<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${page.list}" var="list">
								<tr>
									<td>${list.roleId }</td>
								  	<td>${list.name }</td>
								  	<td>${list.modName }</td>
								  	<td>
								  		<c:if test="${list.type eq '0'}">写手</c:if>
								  		<c:if test="${list.type eq '1'}">管理员</c:if>
								  		<c:if test="${list.type eq '2'}">超级管理员</c:if>
								  	</td>
								  	<td>
								  		<c:forEach items="${list.publictoArr}" var="pub">
								  		<c:if test="${pub eq 'a'}">妃子笑</c:if>
								  		<c:if test="${pub eq 'b'}">每天美耶</c:if>
								  		</c:forEach>
								  	</td>
								    <td>
							    		<shiro:hasPermission name="train:mdrole:view">
											<a href="#" onclick="openDialogView('查看角色', '${ctx}/train/mdrole/form?roleId=${list.roleId}','800px', '650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
										</shiro:hasPermission>
										<c:if test="${list.modeid eq '8'}">
											<c:if test="${list.type ne '2'}">
											<shiro:hasPermission name="train:mdrole:edit">
						    					<a href="#" onclick="openDialog('修改角色', '${ctx}/train/mdrole/form?roleId=${list.roleId}&opflag=${mediaRole.opflag }','800px', '650px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
						    				</shiro:hasPermission>
						    				<shiro:hasPermission name="train:mdrole:del">
												<a href="${ctx}/train/mdrole/delete?roleId=${list.roleId}&opflag=${mediaRole.opflag }" onclick="return confirmx('要删除该角色吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
											</shiro:hasPermission>
											</c:if>
											<c:if test="${list.type eq '1'}">
											<shiro:hasPermission name="train:mdrole:auth"> 
												<a href="#" onclick="openDialog('权限设置', '${ctx}/train/mdrole/auth?roleId=${list.roleId}&opflag=${mediaRole.opflag }','350px', '700px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i> 权限设置</a> 
											</shiro:hasPermission> 
											</c:if>
										</c:if>
								    </td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
                <table:page page="${page}"></table:page>
	        </div>
	    </div>
    </div>
    <div class="loading"></div>
</body>
</html>