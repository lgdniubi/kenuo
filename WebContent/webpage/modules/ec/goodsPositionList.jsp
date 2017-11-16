<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>项目部位</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<%@ include file="/webpage/include/treetable.jsp"%>
	
	<script type="text/javascript">
		//页面加载
		/* $(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 1,column:1}).show();
	    }); */
		
	    $(document).ready(function() {
			//树形table配置
			var option = {
                expandLevel : 1,
                column:1,
                beforeExpand : function($treeTable, id) {
                    //判断id是否已经有了孩子节点，如果有了就不再加载，这样就可以起到缓存的作用
                    if ($('.' + id, $treeTable).length) { 
                    	return; 
                    }
                    $(".loading").show();//打开展示层
                    $.ajax({
                    	type:"post",
   					 	url:"${ctx}/ec/goodsposition/getChildren",
   					  	data: {id:id},
   					 	success:function(data){
   							var dataObj = eval("("+data+")"); 
   							//文本内容
   						 	var html = ""; 
   						 for(var i=0;i<dataObj.length;i++){   
								var isChildren = dataObj[i].num>0?'hasChild=\"true\"':'';
								html += "<tr pid=\""+dataObj[i].parentId+"\" id=\""+dataObj[i].id+"\" "+isChildren+" >";
								html += "<td style=\"text-align: center;\">"+dataObj[i].id+"</td>";
								html += "<td nowrap><i class=\"icon-menu.icon\"></i>"+dataObj[i].name+"</td>";
								html += "<td style=\"text-align: center;\">"+dataObj[i].level+"</td>";
								html += "<td>";
								if(dataObj[i].id != 101){
									//操作权限-查看
									if($("#shiroView").val() == 1){
										html += "<a href=\"#\" onclick=\"openDialogView('查看项目部位', '${ctx}/ec/goodsposition/form?id="+dataObj[i].id+"&opflag=VIEW','800px', '500px')\" class=\"btn btn-info btn-xs\" ><i class=\"fa fa-search-plus\"></i> 查看</a>";
									}
									//操作权限-修改
									if($("#shiroEdit").val() == 1){
										html += "&nbsp;<a href=\"#\" onclick=\"openDialog('修改项目部位', '${ctx}/ec/goodsposition/form?id="+dataObj[i].id+"&opflag=UPDATE','800px', '500px')\" class=\"btn btn-success btn-xs\" ><i class=\"fa fa-edit\"></i> 修改</a>";
									}
									//操作权限-删除
									if($("#shiroDel").val() == 1){
										html += "&nbsp;<a href=\"${ctx}/ec/goodsposition/delete?id="+dataObj[i].id+"\" onclick=\"return confirmx('要删除该项目部位吗？', this.href)\" class=\"btn btn-danger btn-xs\" ><i class=\"fa fa-trash\"></i> 删除</a>";
									}
									if(dataObj[i].level == 1){
										//操作权限-添加
										if($("#shiroAdd").val() == 1){
											html += "&nbsp;<a href=\"#\" onclick=\"openDialog('添加下级项目部位', '${ctx}/ec/goodsposition/form?parent.id="+dataObj[i].id+"&opflag=ADD','800px', '500px')\" class=\"btn btn-primary btn-xs\" ><i class=\"fa fa-plus\"></i> 添加下级项目部位</a>";
										}
									}
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
	    
		//刷新
		function refresh(){
			window.location="${ctx}/ec/goodsposition/list";
		}
		
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">
			<div class="ibox">
				<div class="ibox-title">
					<h5>项目部位管理</h5>
				</div>
				<div class="ibox-content">
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<shiro:hasPermission name="ec:goodsposition:add">
								<table:addRow url="${ctx}/ec/goodsposition/form?parent.id=0&opflag=ADDPARENT" width="800px" height="500px" title="项目部位"></table:addRow><!-- 增加按钮 -->
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
						</div>
					</div>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">部位名称</th>
							<th style="text-align: center;">等级</th>
							<th style="text-align: center;">操作</th>
						</thead>
						<tbody>
							<c:forEach items="${list}" var="goodsPosition">
								<tr pid="${goodsPosition.parentId}" id="${goodsPosition.id}" style="text-align: center;" ${goodsPosition.num>0?'hasChild="true"':''}>
									<td>${goodsPosition.id}</td>
									<td nowrap style="text-align: left;"><i class="icon-menu.icon"></i>${goodsPosition.name}</td>
									<td>${goodsPosition.level}</td>
									<c:if test="${goodsPosition.id == 100}">
										<td style="text-align: left;"></td>
									</c:if>
									<c:if test="${goodsPosition.id != 100}">
										<td style="text-align: left;">
											<shiro:hasPermission name="ec:goodsposition:view">
												<a href="#" onclick="openDialogView('查看项目部位', '${ctx}/ec/goodsposition/form?id=${goodsPosition.id}&opflag=VIEW','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
											</shiro:hasPermission>
											<shiro:hasPermission name="ec:goodsposition:edit">
					    						<a href="#" onclick="openDialog('修改项目部位', '${ctx}/ec/goodsposition/form?id=${goodsPosition.id}&opflag=UPDATE','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
						    				</shiro:hasPermission>
						    				<shiro:hasPermission name="ec:goodsposition:del">
												<a href="${ctx}/ec/goodsposition/delete?id=${goodsPosition.id}" onclick="return confirmx('要删除该项目部位吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
											</shiro:hasPermission>
											<shiro:hasPermission name="ec:goodsposition:add">
												<a href="#" onclick="openDialog('添加下级项目部位', '${ctx}/ec/goodsposition/form?parent.id=${goodsPosition.id}&opflag=ADD','800px', '500px')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i> 添加下级项目部位</a>
											</shiro:hasPermission>
										</td>
									</c:if>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<shiro:hasPermission name="ec:goodscategory:view">
			<input type="hidden" id="shiroView" value="1">
		</shiro:hasPermission>
		<shiro:hasPermission name="ec:goodscategory:edit">
			<input type="hidden" id="shiroEdit" value="1">
		</shiro:hasPermission>
		<shiro:hasPermission name="ec:goodscategory:del">
			<input type="hidden" id="shiroDel" value="1">
		</shiro:hasPermission>
		<shiro:hasPermission name="ec:goodscategory:add">
			<input type="hidden" id="shiroAdd" value="1">
		</shiro:hasPermission>
		<div class="loading"></div>
	</div>
</body>
</html>