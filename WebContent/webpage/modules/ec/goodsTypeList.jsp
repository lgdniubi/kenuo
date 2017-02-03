<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>商品类型</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		//刷新
		function refresh(){
			window.location="${ctx}/ec/goodstype/list";
		}
		
		//重置表单
		function resetnew(){
			$("#name").val("");
			reset();
		}
		
		//分页按钮
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">
			<div class="ibox">
				<div class="ibox-title">
					<h5>商品类型管理</h5>
				</div>
				<div class="ibox-content">
					<div class="searcharea clearfix">
						<form:form id="searchForm" action="${ctx}/ec/goodstype/list" modelAttribute="goodsType" method="post" class="navbar-form navbar-left searcharea">
							<div class="form-group">
								<label>关键字：<input id="name" name="name" maxlength="10" type="text" class="form-control" value="${goodsType.name}"></label> 
							</div>
							<shiro:hasPermission name="ec:goodstype:list">
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
									<i class="fa fa-search"></i> 搜索
								</button>
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="resetnew()" >
									<i class="fa fa-refresh"></i> 重置
								</button>
							</shiro:hasPermission>
							<!-- 分页必要字段 -->
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						</form:form>
					</div>
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<shiro:hasPermission name="ec:goodstype:add">
								<table:addRow url="${ctx}/ec/goodstype/form?opflag=ADDPARENT" width="800px" height="300px" title="商品类型"></table:addRow><!-- 增加按钮 -->
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
						</div>
					</div>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">类型名称</th>
							<th style="text-align: center;">操作</th>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="goodsType">
								<tr style="text-align: center;">
									<td>${goodsType.id}</td>
									<td>${goodsType.name}</td>
									<td style="text-align: left;">
										<shiro:hasPermission name="ec:goodstype:view">
											<a href="#" onclick="openDialogView('查看商品类型', '${ctx}/ec/goodstype/form?id=${goodsType.id}&opflag=VIEW','800px', '300px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="ec:goodstype:edit">
				    						<a href="#" onclick="openDialog('修改商品类型', '${ctx}/ec/goodstype/form?id=${goodsType.id}&opflag=UPDATE','800px', '300px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
					    				</shiro:hasPermission>
					    				<shiro:hasPermission name="ec:goodstype:del">
											<a href="${ctx}/ec/goodstype/delete?id=${goodsType.id}" onclick="return confirmx('要删除该商品类型吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
										</shiro:hasPermission>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<!-- 分页代码 -->
					<table:page page="${page}"></table:page>
				</div>
			</div>
		</div>
	</div>
</body>
</html>