<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> 批量删除 由于样式问题  取消 --%>
<html>
<head>
	<title>自媒体菜单管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/webpage/include/treetable.jsp" %>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<script type="text/javascript">
		/* $(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 1,column:1}).show();
		}); */

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
   					 	url:"${ctx}/train/mdmenu/getChildren",
   					  	data: {id:id},
   					 	success:function(data){
   							var dataObj = eval("("+data+")"); 
   							//文本内容
   						 	var html = ""; 
							for(var i=0;i<dataObj.length;i++){   
								var isChildren = dataObj[i].num>0?'hasChild=\"true\"':'';
								html += "<tr pid=\""+dataObj[i].parentId+"\" id=\""+dataObj[i].id+"\" "+isChildren+" >";
								/* html += "<td><input type=\"checkbox\" id="+dataObj[i].id+" class=\"i-checks\"></td>"; 批量删除 由于样式问题  取消 */
								html += "<td nowrap><a href=\"#\" onclick=\"openDialogView('查看菜单', '${ctx}/train/mdmenu/form?id="+dataObj[i].id+"','800px', '620px')\">"+dataObj[i].name+"</a></td>";
								html += "<td>"+dataObj[i].href+"</td>";
								html +="<td style=\"text-align:center;\"><input name=\"sorts\" type=\"text\" value="+dataObj[i].sort+" class=\"form-control\" style=\"width:100px;margin:0;padding:0;text-align:center;\"></td>";
								if(dataObj[i].isShow == 0){
									html += "<td>显示</td>";
								}else{
									html += "<td>隐藏</td>";
								}
								/* html += "<td>"+dataObj[i].permission+"</td>"; */
								html += "<td>";
								//操作权限-查看
								if($("#shiroView").val() == 1){
									html += "<a href=\"#\" onclick=\"openDialogView('查看菜单', '${ctx}/train/mdmenu/form?id="+dataObj[i].id+"','800px', '500px')\" class=\"btn btn-info btn-xs\" ><i class=\"fa fa-search-plus\"></i> 查看</a>";
								}
								//操作权限-修改
								if($("#shiroEdit").val() == 1){
									html += "&nbsp;<a href=\"#\" onclick=\"openDialog('修改菜单', '${ctx}/train/mdmenu/form?id="+dataObj[i].id+"','800px', '500px')\" class=\"btn btn-success btn-xs\" ><i class=\"fa fa-edit\"></i> 修改</a>";
								}
								//操作权限-删除
								if($("#shiroDel").val() == 1){
									html += "&nbsp;<a href=\"${ctx}/train/mdmenu/delete?id="+dataObj[i].id+"\" onclick=\"return confirmx('要删除该菜单及所有子菜单项吗？', this.href)\" class=\"btn btn-danger btn-xs\" ><i class=\"fa fa-trash\"></i> 删除</a>";
								}
								//操作权限-添加
								if($("#shiroAdd").val() == 1){
									html += "&nbsp;<a href=\"#\" onclick=\"openDialog('添加下级菜单', '${ctx}/train/mdmenu/form?parent.id="+dataObj[i].id+"','800px', '500px')\" class=\"btn btn-primary btn-xs\" ><i class=\"fa fa-plus\"></i> 添加下级菜单</a>";
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
		
    	function updateSort() {
			loading('正在提交，请稍等...');
	    	$("#listForm").attr("action", "${ctx}/train/mdmenu/updateSort");
	    	$("#listForm").submit();
    	}
		function refresh(){//刷新
			window.location="${ctx}/train/mdmenu/";
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
			<h5>菜单列表 </h5>
			<div class="ibox-tools">
				<a class="collapse-link">
					<i class="fa fa-chevron-up"></i>
				</a>
				<a class="dropdown-toggle" data-toggle="dropdown" href="form_basic.html#">
					<i class="fa fa-wrench"></i>
				</a>
				<ul class="dropdown-menu dropdown-user">
					<li><a href="#">选项1</a>
					</li>
					<li><a href="#">选项2</a>
					</li>
				</ul>
				<a class="close-link">
					<i class="fa fa-times"></i>
				</a>
			</div>
	</div>
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
		
			<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="train:mdmenu:add">
				<table:addRow url="${ctx}/train/mdmenu/form" title="菜单"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<%-- <shiro:hasPermission name="train:menu:del">
				<table:delRow url="${ctx}/train/menu/deleteAll" id="treeTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission> 批量删除 由于样式问题  取消 --%>
			<shiro:hasPermission name="train:mdmenu:updateSort">
				<button id="btnSubmit" class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="updateSort()" title="保存排序"><i class="fa fa-save"></i> 保存排序</button>
			</shiro:hasPermission>
				<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		</div>
	</div>
	</div>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
			<thead>
				<tr>
					<th>名称</th>
					<th>链接</th>
					<th style="text-align:center;">排序</th>
					<th>可见</th>
					<shiro:hasPermission name="train:mdmenu:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list}" var="menu">
					<c:if test="${menu.parent.id == '1'}">
						<tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}" ${menu.num>0?'hasChild="true"':''}>
							<%-- <td><input type="checkbox" id="${menu.id}" class="i-checks"></td> 批量删除 由于样式问题  取消 --%>
							<td nowrap><i class="icon-${not empty menu.icon?menu.icon:' hide'}"></i><a  href="#" onclick="openDialogView('查看菜单', '${ctx}/train/mdmenu/form?id=${menu.id}','800px', '500px')">${menu.name}</a></td>
							<td title="${menu.href}">${fns:abbr(menu.href,30)}</td>
							<td style="text-align:center;">
								<input name="sorts" type="text" value="${menu.sort}" class="form-control" style="width:100px;margin:0;padding:0;text-align:center;">
							</td>
							<td>${menu.isShow eq '0'?'显示':'隐藏'}</td>
							<td nowrap>
								<shiro:hasPermission name="train:mdmenu:view">
									<a href="#" onclick="openDialogView('查看菜单', '${ctx}/train/mdmenu/form?id=${menu.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="train:mdmenu:edit">
			    					<a href="#" onclick="openDialog('修改菜单', '${ctx}/train/mdmenu/form?id=${menu.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
			    				</shiro:hasPermission>
			    				<shiro:hasPermission name="train:mdmenu:del">
									<a href="${ctx}/train/mdmenu/delete?id=${menu.id}" onclick="return confirmx('要删除该菜单及所有子菜单项吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="train:mdmenu:add">
									<a href="#" onclick="openDialog('添加下级菜单', '${ctx}/train/mdmenu/form?parent.id=${menu.id}','800px', '500px')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i> 添加下级菜单</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
	 </form>
	 <!-- 用于异步加载时，操作权限设置 -->
	 <shiro:hasPermission name="train:mdmenu:view">
	 	<input type="hidden" id="shiroView" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="train:mdmenu:edit">
		<input type="hidden" id="shiroEdit" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="train:mdmenu:del">
		<input type="hidden" id="shiroDel" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="train:mdmenu:add">
		<input type="hidden" id="shiroAdd" value="1">
	</shiro:hasPermission>
	 </div>
	</div>
	</div>
	<div class="loading"></div>
</body>
</html>