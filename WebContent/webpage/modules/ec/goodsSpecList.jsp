<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>商品规格</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<link rel="stylesheet" href="${ctxStatic}/ec/css/base.css">
	<script type="text/javascript">
		//刷新
		function refresh(){
			window.location="${ctx}/ec/goodsspec/list";
		}
		
		//重置表单
		function resetnew(){
			reset();
		}
		
		//分页按钮
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		
		//删除规格项前,先判断是否有商品正在使用
		function deleteGoodsSpec(specId){
			$.ajax({
				type:"post",
				async:false,
				data:{
					specId:specId
				 },
				url:"${ctx}/ec/goodsspec/getIsGoodsUseSpec",
				success:function(obj){
					if(obj){
						top.layer.alert('商品正在使用此规格,无法删除.', {icon: 0, title:'提醒'});
					}else{
						if(confirm("要删除该商品规格吗？")){
							deleteSpec(specId);
						}
					}
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
							    
				}
			});
		}
		//删除操作
		function deleteSpec(specId){
			$.ajax({
				type:"post",
				async:false,
				data:{
					id:specId
				 },
				url:"${ctx}/ec/goodsspec/delete",
				success:function(obj){
					if(obj == "success"){
						top.layer.alert('删除成功!', {icon: 0, title:'提醒'});
					}else{
						top.layer.alert('删除失败!', {icon: 0, title:'提醒'});
					}					
					window.location = "${ctx}/ec/goodsspec/list"
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
							    
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
					<h5>商品规格管理</h5>
				</div>
				<div class="ibox-content">
					<div class="searcharea clearfix">
						<form:form id="searchForm" action="${ctx}/ec/goodsspec/list" modelAttribute="goodsSpec" method="post" class="navbar-form navbar-left searcharea">
							<div class="form-group" style="width: 500px;">
								商品类型：
								<select class="form-control" style="width: 30%;text-align: center;" id="typeId" name="typeId">
									<option value="-1">所有类型</option>
									<c:forEach items="${goodsTypeList}" var="goodsType">
										<option ${(goodsType.id == goodsSpec.typeId)?'selected="selected"':''} value="${goodsType.id}">${goodsType.name}</option>
									</c:forEach>
								</select>
								<shiro:hasPermission name="ec:goodsspec:list">
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
							</div>
						</form:form>
					</div>
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<shiro:hasPermission name="ec:goodsspec:add">
								<table:addRow url="${ctx}/ec/goodsspec/form?opflag=ADD" width="800px" height="500px" title="商品规格"></table:addRow><!-- 增加按钮 -->
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
						</div>
					</div>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">规格类型</th>
							<th style="text-align: center;">规格名称</th>
							<th style="text-align: center;">规格项</th>
							<th style="text-align: center;">排序</th>
							<th style="text-align: center;">操作</th>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="goodsSpec">
								<tr style="text-align: center;">
									<td>${goodsSpec.id}</td>
									<td>${goodsSpec.goodsType.name}</td>
									<td>${goodsSpec.name}</td>
									<td>
										<div class="textoverflow">
											${goodsSpec.specItems}
										</div>
									</td>
									<td>${goodsSpec.sort}</td>
									<td style="text-align: left;">
										<shiro:hasPermission name="ec:goodsspec:view">
											<a href="#" onclick="openDialogView('查看商品规格项', '${ctx}/ec/goodsspec/form?id=${goodsSpec.id}&opflag=VIEW','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="ec:goodsspec:edit">
				    						<a href="#" onclick="openDialog('修改商品规格项', '${ctx}/ec/goodsspec/form?id=${goodsSpec.id}&opflag=UPDATE','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
					    				</shiro:hasPermission>
					    				<shiro:hasPermission name="ec:goodsspec:del">
											<a href="#" onclick="deleteGoodsSpec(${goodsSpec.id})" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
											<%-- <a href="${ctx}/ec/goodsspec/delete?id=${goodsSpec.id}" onclick="return confirmx('要删除该商品规格吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a> --%>
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