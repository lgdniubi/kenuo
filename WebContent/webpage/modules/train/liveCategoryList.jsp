<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>机构管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<%@ include file="/webpage/include/treetable.jsp"%>
	<script type="text/javascript">
	
	    $(document).ready(function() {
			//树形table配置
			var option = {
                expandLevel : 1,
                column:0,
                beforeExpand : function($treeTable, id) {
                    //判断id是否已经有了孩子节点，如果有了就不再加载，这样就可以起到缓存的作用
                    if ($('.' + id, $treeTable).length) { 
                    	return; 
                    }
                    
                    $(".loading").show();//打开展示层
                    $.ajax({
                    	type:"post",
   					 	url:"${ctx}/train/category/getChildren",
   					  	data: {id:id},
   					 	success:function(data){
   							var dataObj = eval("("+data+")"); 
   							//文本内容
   						 	var html = ""; 
							for(var i=0;i<dataObj.length;i++){ 
								var isChildren = dataObj[i].num>0?'hasChild=\"true\"':'';
								html += "<tr pid=\""+dataObj[i].parentId+"\" id=\""+dataObj[i].trainLiveCategoryId+"\" "+isChildren+" >";
								html += "<td style='text-align: left;'>"+dataObj[i].name+"</td>";
								html += "<td>"+dataObj[i].type+"</td>";
								html += "<td>"+dataObj[i].sort+"</td>";
								html += "<td>";
								//操作权限-查看
								if($("#shiroView").val() == 1){
									html += "<a href=\"#\" onclick=\"openDialogView('查看分类', '${ctx}/train/category/form?trainLiveCategoryId="+dataObj[i].trainLiveCategoryId+"','430px', '4800px')\" class=\"btn btn-info btn-xs\" ><i class=\"fa fa-search-plus\"></i> 查看</a>";
								}
								//操作权限-修改
								if($("#shiroEdit").val() == 1){
									html += "&nbsp;<a href=\"#\" onclick=\"openDialog('修改分类', '${ctx}/train/category/form?trainLiveCategoryId="+dataObj[i].trainLiveCategoryId+"','430px', '480px')\" class=\"btn btn-success btn-xs\" ><i class=\"fa fa-edit\"></i> 修改</a>";
								}
								//操作权限-删除
								if($("#shiroDel").val() == 1){
									html += "&nbsp;<a href=\"#\" onclick=\"delCategory('"+dataObj[i].trainLiveCategoryId+"')\" class=\"btn btn-danger btn-xs\" ><i class=\"fa fa-trash\"></i> 删除</a>";
								}
								//操作权限-添加
								if($("#shiroAddChild").val() == 1 && dataObj[i].type != 3){
									html += "&nbsp;<a href=\"#\" onclick=\"openDialog('添加下级分类', '${ctx}/train/category/form?parentId="+dataObj[i].trainLiveCategoryId+"','430px', '480px')\" class=\"btn btn-primary btn-xs\" ><i class=\"fa fa-plus\"></i> 添加下级分类</a>";
								}
								//操作权限-转移分类
								if($("#shiroTransfer").val() == 1){
									html += "&nbsp;<a href=\"#\" onclick=\"openDialogView('直播列表', '${ctx}/train/live/transferForm?category.trainLiveCategoryId="+dataObj[i].trainLiveCategoryId+"','1400px', '800px')\" class=\"btn btn-success btn-xs\" ><i class=\"fa fa-edit\"></i> 转移直播</a>";
								}
								html += "</td>";
								html += "</tr>";
							} 
							$treeTable.addChilds(html);
							$(".loading").hide(); //关闭加载层     
   					 	}
   					});
                }
            };
			//树形table
			$("#treeTable").treeTable(option);
	    });
	    
		//刷新或者排序，页码不清零
		function refresh(){
			window.location="${ctx}/train/category/list";
    	} 
    	
    	function delCategory(id){
			$.post("${ctx}/train/live/delCheck",{"category.trainLiveCategoryId":id},function(data){
				if (data == "yes") {
					top.layer.confirm('确认要删除该分类吗?', {icon: 3, title:'提醒'}, function(index){
					window.location="${ctx}/train/category/delete?trainLiveCategoryId="+id;
				    top.layer.close(index);
					});
				}else if(data == "no"){
					top.layer.alert('删除失败!分类中还有直播，无法删除', {icon: 2, title:'提醒'});
				}
				
			})
    	}
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>直播分类管理</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<%-- <!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<!-- 无查询条件 -->
						<form:form id="searchForm" modelAttribute="" action="${ctx}/train/category/list" method="post" class="form-inline">
							<div class="form-group">
								<label>关键字：<input id="name" name="name" maxlength="10" type="text" class="form-control" value="${trainLiveCategory.name}"></label> 
							</div>
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
									<i class="fa fa-search"></i> 搜索
								</button>
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="refresh()" >
									<i class="fa fa-refresh"></i> 重置
								</button>
						</form:form>
					</div>
				</div> --%>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="train:category:add">
								<table:addRow url="${ctx}/train/category/form?flag=1" width="430px" height="480px" title="添加直播分类"></table:addRow><!-- 增加按钮 -->
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
						</div>
					</div>
				</div>
				<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th width="230" style="text-align: center;">名称</th>
							<th width="130" style="text-align: center;">级别</th>
							<th width="130" style="text-align: center;">排序</th>
							<th width="300" style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="row" >
						<tr id="${row.trainLiveCategoryId}" pid="${row.parentId}" ${row.num>0?'hasChild="true"':''}>
							<td style="text-align: left;">${row.name}</td>
							<td>${row.type}</td>
							<td>${row.sort}</td>
							<td>
								<shiro:hasPermission name="train:category:view">
									<a href="#" onclick="openDialogView('查看分类', '${ctx}/train/category/form?flag=2&trainLiveCategoryId=${row.trainLiveCategoryId}','430px', '480px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission> 
								<shiro:hasPermission name="train:category:edit">
									<a href="#" onclick="openDialog('修改分类', '${ctx}/train/category/form?flag=3&trainLiveCategoryId=${row.trainLiveCategoryId}','430px', '480px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<c:if test="${row.name != '其他' }">
									<shiro:hasPermission name="train:category:del">
										<a href="#" onclick="delCategory('${row.trainLiveCategoryId}')" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
									</shiro:hasPermission>
								</c:if>
								<c:if test="${row.name != '其他' }">
									<c:if test="${row.type != 3}">
										<shiro:hasPermission name="train:category:addChild">
											<a href="#" onclick="openDialog('添加下级分类', '${ctx}/train/category/form?parentId=${row.trainLiveCategoryId}','430px', '480px')" class="btn  btn-primary btn-xs"><i class="fa fa-plus"></i> 添加下级分类</a>
										</shiro:hasPermission>
									</c:if>
								</c:if>
								<shiro:hasPermission name="train:category:transfer">
									<a href="#" onclick="openDialogView('直播列表', '${ctx}/train/live/transferForm?category.trainLiveCategoryId=${row.trainLiveCategoryId}','1400px', '800px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 转移直播</a>
								</shiro:hasPermission>
							</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="loading"></div>
	 <!-- 用于异步加载时，操作权限设置 -->
	 <shiro:hasPermission name="train:category:view">
	 	<input type="hidden" id="shiroView" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="train:category:edit">
		<input type="hidden" id="shiroEdit" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="train:category:del">
		<input type="hidden" id="shiroDel" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="train:category:addChild">
		<input type="hidden" id="shiroAddChild" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="train:category:transfer">
		<input type="hidden" id="shiroTransfer" value="1">
	</shiro:hasPermission>
</body>
</html>