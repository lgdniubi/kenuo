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
    <title>妃子校角色管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>妃子校角色管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="fzxRole" action="${ctx}/train/fzxRole/list" method="post">
                    	<!-- 分页隐藏文本框 -->
						<form:hidden path="opflag"/>
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							<form:input path="name" htmlEscape="false" placeholder="搜索角色名称" class=" form-control input-sm" />
						</div>
                    </form:form>
                </div>
                <!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="train:fzxRole:add">
								<!-- 增加按钮 -->
								<table:addRow url="${ctx}/train/fzxRole/form?opflag=${fzxRole.opflag }" title="角色" width="800px" height="650px"></table:addRow>
							</shiro:hasPermission>
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
							    <th style="text-align: center;">英文名称</th>
							    <th style="text-align: center;">版本名称</th>
							    <th style="text-align: center;">备注</th>
						    	<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${page.list}" var="list">
								<tr>
									<td>${list.roleId }</td>
								  	<td>${list.name }</td>
								  	<td>${list.enname }</td>
								  	<td>${list.modName }</td>
								  	<td>${list.remarks }</td>
								    <td>
							    		<shiro:hasPermission name="train:fzxRole:view">
											<a href="#" onclick="openDialogView('查看角色', '${ctx}/train/fzxRole/form?roleId=${list.roleId}','800px', '650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
										</shiro:hasPermission>
										<c:choose>
											<c:when test="${list.modeid ==8}">
													<c:if test="${list.enamer ne 'dy'}">
													<shiro:hasPermission name="train:fzxRole:edit">
								    					<a href="#" onclick="openDialog('修改角色', '${ctx}/train/fzxRole/form?roleId=${list.roleId}','800px', '650px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
								    				</shiro:hasPermission>
								    				<shiro:hasPermission name="train:fzxRole:del">
														<a href="${ctx}/train/fzxRole/delete?roleId=${list.roleId}" onclick="return confirmx('要删除该角色吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
													</shiro:hasPermission>
													</c:if>
											</c:when>
											<c:otherwise>
													<c:if test="${list.isDefault eq '0' && list.enamer ne 'sjgly' && list.enamer ne 'dy'}">
													<shiro:hasPermission name="train:fzxRole:edit">
								    					<a href="#" onclick="openDialog('修改角色', '${ctx}/train/fzxRole/form?roleId=${list.roleId}','800px', '650px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
								    				</shiro:hasPermission>
								    				<shiro:hasPermission name="train:fzxRole:del">
														<a href="${ctx}/train/fzxRole/delete?roleId=${list.roleId}" onclick="return confirmx('要删除该角色吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
													</shiro:hasPermission>
													</c:if>
											</c:otherwise>
										</c:choose>
										<shiro:hasPermission name="train:fzxRole:auth"> 
										<c:choose>
											<c:when test="${list.modeid ==8}">
												<c:if test="${list.enamer != 'dy'}">
												<a href="#" onclick="openDialog('权限设置', '${ctx}/train/fzxRole/auth?roleId=${list.roleId}','350px', '700px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i> 权限设置</a> 
												</c:if>
											</c:when>
											<c:otherwise>
												<c:if test="${list.enamer != 'sjgly' && list.enamer != 'dy' && list.enamer ne 'syrsf' && list.enamer ne 'syrmf' && list.enamer ne 'pthy'}">
												<a href="#" onclick="openDialog('权限设置', '${ctx}/train/fzxRole/auth?roleId=${list.roleId}','350px', '700px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i> 权限设置</a> 
												</c:if>
											</c:otherwise>
										</c:choose>
											<c:if test="${list.isDefault eq '0' && list.enamer != 'sjgly' && list.modeid !=8}">
												<a href="${ctx}/train/fzxRole/setDefault?roleId=${list.roleId}&modeid=${list.modeid}"  class="btn btn-danger btn-xs" >默认</a>
											</c:if>
										</shiro:hasPermission>
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