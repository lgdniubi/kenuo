<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>商品分类</title>
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
   					 	url:"${ctx}/ec/goodscategory/getChildren",
   					  	data: {id:id},
   					 	success:function(data){
   							var dataObj = eval("("+data+")"); 
   							//文本内容
   						 	var html = ""; 
   						 for(var i=0;i<dataObj.length;i++){   
								var isChildren = dataObj[i].num>0?'hasChild=\"true\"':'';
								html += "<tr pid=\""+dataObj[i].parentId+"\" id=\""+dataObj[i].categoryId+"\" "+isChildren+" >";
								html += "<td style=\"text-align: center;\">"+dataObj[i].categoryId+"</td>";
								html += "<td nowrap><i class=\"icon-menu.icon\"></i>"+dataObj[i].name+"</td>";
								html += "<td style=\"text-align: center;\">"+dataObj[i].mobileName+"</td>";
								
								if(dataObj[i].isHot == 0){
									html += "<td id=\"ISHOT"+dataObj[i].categoryId+"\" style=\"text-align: center;\"><img width=\"20\" height=\"20\" src=\"${ctxStatic}/ec/images/open.png\" onclick=\"changeTableVal('ISHOT','"+dataObj[i].categoryId+"','1')\"></td>";
								}else{
									html += "<td id=\"ISHOT"+dataObj[i].categoryId+"\" style=\"text-align: center;\"><img width=\"20\" height=\"20\" src=\"${ctxStatic}/ec/images/cancel.png\" onclick=\"changeTableVal('ISHOT','"+dataObj[i].categoryId+"','0')\"></td>";
								}
								
								if(dataObj[i].isShow == 0){
									html += "<td id=\"ISSHOW"+dataObj[i].categoryId+"\" style=\"text-align: center;\"><img width=\"20\" height=\"20\" src=\"${ctxStatic}/ec/images/open.png\" onclick=\"changeTableVal('ISSHOW','"+dataObj[i].categoryId+"','1')\"></td>";
								}else{
									html += "<td id=\"ISSHOW"+dataObj[i].categoryId+"\" style=\"text-align: center;\"><img width=\"20\" height=\"20\" src=\"${ctxStatic}/ec/images/cancel.png\" onclick=\"changeTableVal('ISSHOW','"+dataObj[i].categoryId+"','0')\"></td>";
								}
								html += "<td style=\"text-align: center;\">"+dataObj[i].sort+"</td>";
								html += "<td>";
								//操作权限-查看
								if($("#shiroView").val() == 1){
									html += "<a href=\"#\" onclick=\"openDialogView('查看商品分类', '${ctx}/ec/goodscategory/form?id="+dataObj[i].categoryId+"&opflag=VIEW','800px', '500px')\" class=\"btn btn-info btn-xs\" ><i class=\"fa fa-search-plus\"></i> 查看</a>";
								}
								//操作权限-修改
								if($("#shiroEdit").val() == 1){
									html += "&nbsp;<a href=\"#\" onclick=\"openDialog('修改商品分类', '${ctx}/ec/goodscategory/form?id="+dataObj[i].categoryId+"&opflag=UPDATE','800px', '500px')\" class=\"btn btn-success btn-xs\" ><i class=\"fa fa-edit\"></i> 修改</a>";
								}
								//操作权限-删除
								if($("#shiroDel").val() == 1){
									html += "&nbsp;<a href=\"${ctx}/ec/goodscategory/delete?id="+dataObj[i].categoryId+"\" onclick=\"return confirmx('要删除该分类及所有子分类项吗？', this.href)\" class=\"btn btn-danger btn-xs\" ><i class=\"fa fa-trash\"></i> 删除</a>";
								}
								if(dataObj[i].parentId == '0'){
									//操作权限-添加
									if($("#shiroAdd").val() == 1){
										html += "&nbsp;<a href=\"#\" onclick=\"openDialog('添加下级商品分类', '${ctx}/ec/goodscategory/form?parent.id="+dataObj[i].categoryId+"&opflag=ADD','800px', '500px')\" class=\"btn btn-primary btn-xs\" ><i class=\"fa fa-plus\"></i> 添加下级商品分类</a>";
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
			window.location="${ctx}/ec/goodscategory/list";
		}
		
		//是否推荐/是否显示 改变事件
		function changeTableVal(fromid,id,isyesno){
			
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",   
				url : "${ctx}/ec/goodscategory/updateisyesno?ID="+id+"&FROMID="+fromid+"&ISYESNO="+isyesno,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					var isyesno = data.ISYESNO;
					if("OK" == status){
						$("#"+fromid+id).html("");//清除DIV内容	
						if(isyesno == '0'){
							//当前状态为【是】，则取消
							$("#"+fromid+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+fromid+"','"+id+"','1')\">");
						}else if(isyesno == '1'){
							//当前状态为【否】，则打开
							$("#"+fromid+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+fromid+"','"+id+"','0')\">");
						}
					}else if("ERROR" == status){
						alert(data.MESSAGE);
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
					<h5>商品分类管理</h5>
				</div>
				<div class="ibox-content">
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<shiro:hasPermission name="ec:goodscategory:add">
								<table:addRow url="${ctx}/ec/goodscategory/form?parent.id=0&opflag=ADDPARENT" width="800px" height="500px" title="商品分类"></table:addRow><!-- 增加按钮 -->
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
						</div>
					</div>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">分类名称</th>
							<th style="text-align: center;">手机分类名称</th>
							<th style="text-align: center;">是否推荐</th>
							<th style="text-align: center;">是否显示</th>
							<th style="text-align: center;">排序</th>
							<th style="text-align: center;">操作</th>
						</thead>
						<tbody>
							<c:forEach items="${list}" var="goodsCategory">
								<tr pid="${goodsCategory.parentId}" id="${goodsCategory.id}" style="text-align: center;" ${goodsCategory.num>0?'hasChild="true"':''}>
									<td>${goodsCategory.id}</td>
									<td nowrap style="text-align: left;"><i class="icon-menu.icon"></i>${goodsCategory.name}</td>
									<td>${goodsCategory.mobileName}</td>
									<td style="text-align: center;" id="ISHOT${goodsCategory.id}">
										<c:if test="${goodsCategory.isHot == 0}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('ISHOT','${goodsCategory.id}','1')">
										</c:if>
										<c:if test="${goodsCategory.isHot == 1}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('ISHOT','${goodsCategory.id}','0')">
										</c:if>
									</td>
									<td style="text-align: center;" id="ISSHOW${goodsCategory.id}">
										<c:if test="${goodsCategory.isShow == 0}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('ISSHOW','${goodsCategory.id}','1')">
										</c:if>
										<c:if test="${goodsCategory.isShow == 1}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('ISSHOW','${goodsCategory.id}','0')">
										</c:if>
									</td>
									<td>${goodsCategory.sort}</td>
									<td style="text-align: left;">
										<shiro:hasPermission name="ec:goodscategory:view">
											<a href="#" onclick="openDialogView('查看商品分类', '${ctx}/ec/goodscategory/form?id=${goodsCategory.id}&opflag=VIEW','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="ec:goodscategory:edit">
				    						<a href="#" onclick="openDialog('修改商品分类', '${ctx}/ec/goodscategory/form?id=${goodsCategory.id}&opflag=UPDATE','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
					    				</shiro:hasPermission>
					    				<shiro:hasPermission name="ec:goodscategory:del">
											<a href="${ctx}/ec/goodscategory/delete?id=${goodsCategory.id}" onclick="return confirmx('要删除该分类及所有子分类项吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="ec:goodscategory:add">
											<a href="#" onclick="openDialog('添加下级商品分类', '${ctx}/ec/goodscategory/form?parent.id=${goodsCategory.id}&opflag=ADD','800px', '500px')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i> 添加下级商品分类</a>
										</shiro:hasPermission>
									</td>
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