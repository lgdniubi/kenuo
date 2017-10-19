<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <!-- 引入treetable文件 -->
    <%@include file="/webpage/include/treetable.jsp" %>
    <!-- 引入css文件 -->
    <link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
    <!-- <script>
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
    </script> -->
    <title>妃子校菜单管理</title>
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
   					 	url:"${ctx}/train/fzxMenu/getChildren",
   					  	data: {menuId:id},
   					 	success:function(data){
   							var dataObj = eval("("+data+")"); 
   							//文本内容
   						 	var html = ""; 
							for(var i=0;i<dataObj.length;i++){   
								var isChildren = dataObj[i].num>0?'hasChild=\"true\"':'';
								html += "<tr pId=\""+dataObj[i].parentId+"\" id=\""+dataObj[i].menuId+"\" "+isChildren+" >";
								/* html += "<td><input type=\"checkbox\" id="+dataObj[i].id+" class=\"i-checks\"></td>"; 批量删除 由于样式问题  取消 */
								html += "<td nowrap><a href=\"#\" onclick=\"openDialogView('查看菜单', '${ctx}/train/fzxMenu/form?id="+dataObj[i].menuId+"','800px', '620px')\">"+dataObj[i].name+"</a></td>";
								html += "<td>"+dataObj[i].enname+"</td>";
								html += "<td class=\"imgUrl\"><img src=\""+dataObj[i].icon+"\" data-src=\""+dataObj[i].icon+"\" style=\"max-width: 100px;max-height: 100px;\"></td>";
								if(dataObj[i].isShow == 0){
									html += "<td>显示</td>";
								}else{
									html += "<td>隐藏</td>";
								}
								html +="<td style=\"text-align:center;\"><input name=\"sorts\" type=\"text\" value="+dataObj[i].sort+" class=\"form-control\" style=\"width:100px;margin:0;padding:0;text-align:center;\"></td>";
								/* html += "<td>"+dataObj[i].permission+"</td>"; */
								html += "<td>";
								//操作权限-查看
								if($("#shiroView").val() == 1){
									html += "<a href=\"#\" onclick=\"openDialogView('查看菜单', '${ctx}/train/fzxMenu/form?menuId="+dataObj[i].menuId+"','800px', '500px')\" class=\"btn btn-info btn-xs\" ><i class=\"fa fa-search-plus\"></i> 查看</a>";
								}
								//操作权限-修改
								if($("#shiroEdit").val() == 1){
									html += "&nbsp;<a href=\"#\" onclick=\"openDialog('修改菜单', '${ctx}/train/fzxMenu/form?menuId="+dataObj[i].menuId+"','800px', '500px')\" class=\"btn btn-success btn-xs\" ><i class=\"fa fa-edit\"></i> 修改</a>";
								}
								//操作权限-删除
								if($("#shiroDel").val() == 1){
									html += "&nbsp;<a href=\"${ctx}/train/fzxMenu/delete?menuId="+dataObj[i].menuId+"\" onclick=\"return confirmx('要删除该菜单及所有子菜单项吗？', this.href)\" class=\"btn btn-danger btn-xs\" ><i class=\"fa fa-trash\"></i> 删除</a>";
								}
								//操作权限-添加
								if($("#shiroAdd").val() == 1){
									html += "&nbsp;<a href=\"#\" onclick=\"openDialog('添加下级菜单', '${ctx}/train/fzxMenu/form?parent.menuId="+dataObj[i].menuId+"','800px', '500px')\" class=\"btn btn-primary btn-xs\" ><i class=\"fa fa-plus\"></i> 添加下级菜单</a>";
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
	    	$("#listForm").attr("action", "${ctx}/train/fzxMenu/updateSort");
	    	$("#listForm").submit();
    	}
		function refresh(){//刷新
			window.location="${ctx}/train/fzxMenu/";
		}
	</script>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>妃子校菜单管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
            <!-- 菜单展示不需要分页处理 -->
                <%-- <div class=" clearfix">
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="fzxMenu" action="${ctx}/train/fzxMenu/list" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							<form:input path="name" htmlEscape="false" placeholder="搜索菜单名称" class=" form-control input-sm" />
						</div>
                    </form:form>
                </div> --%>
                <!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="train:fzxMenu:add">
								<!-- 增加按钮 -->
								<table:addRow url="${ctx}/train/fzxMenu/form" title="新增菜单" width="800px" height="650px"></table:addRow>
							</shiro:hasPermission>
							<shiro:hasPermission name="train:fzxMenu:updateSort"> <!-- name="train:fzxMenu:updateSort" -->
								<button id="btnSubmit" class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="updateSort()" title="保存排序"><i class="fa fa-save"></i> 保存排序</button>
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
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
                <form id="listForm" method="post">
					<table id=treeTable class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">菜单名称</th>
							    <th style="text-align: center;">英文名称</th>
							    <th style="text-align: center;">图标</th>
							    <th style="text-align: center;">是否显示</th>
							    <th style="text-align: center;">排序</th>
						    	<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${list}" var="menu">
							<c:if test="${menu.parent.menuId == '1'}">
								<tr id="${menu.menuId}" pId="${menu.parent.menuId ne '1'?menu.parent.menuId:'0'}" ${menu.num>0?'hasChild="true"':''}>
									<%-- <td>${menu.menuId }</td> --%>
								  	<td nowrap>
								  	<input type="hidden" name="menuIds" value="${menu.menuId }">
								  	<i class="icon-${not empty menu.icon?menu.icon:' hide'}"></i><a  href="#" onclick="openDialogView('查看菜单', '${ctx}/train/fzxMenu/form?menuId=${menu.menuId}','800px', '500px')">${menu.name}</a></td>
								  	<td>${menu.enname }</td>
								  	<td class="imgUrl"><img alt="" src="${not empty menu.icon?menu.icon:' hide'}" data-src="${menu.icon }" style="max-width: 100px;max-height: 100px;"></td>  <%-- ${ctxStatic}/images/lazylode.png --%>
								  	<td>${menu.isShow == 0?'显示':'隐藏'}</td>
								  	<td style="text-align:center;">
										<input name="sorts" type="text" value="${menu.sort}" class="form-control" style="width:100px;margin:0;padding:0;text-align:center;">
									</td>
								    <td>
							    		<shiro:hasPermission name="train:fzxMenu:view">
											<a href="#" onclick="openDialogView('查看菜单', '${ctx}/train/fzxMenu/form?menuId=${menu.menuId}','800px', '650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="train:fzxMenu:edit">
					    					<a href="#" onclick="openDialog('修改菜单', '${ctx}/train/fzxMenu/form?menuId=${menu.menuId}','800px', '650px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
					    				</shiro:hasPermission>
					    				<shiro:hasPermission name="train:fzxMenu:del">
											<a href="${ctx}/train/fzxMenu/delete?menuId=${menu.menuId}" onclick="return confirmx('要删除该菜单吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="train:fzxMenu:add">
											<a href="#" onclick="openDialog('添加下级菜单', '${ctx}/train/fzxMenu/form?parent.menuId=${menu.menuId}','800px', '500px')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i> 添加下级菜单</a>
										</shiro:hasPermission>
								    </td>
								</tr>
								</c:if>
							</c:forEach>
						</tbody>
					</table>
				</form>
				 <!-- 用于异步加载时，操作权限设置 -->
				 <shiro:hasPermission name="train:fzxMenu:view">
				 	<input type="hidden" id="shiroView" value="1">
				</shiro:hasPermission>
				<shiro:hasPermission name="train:fzxMenu:edit">
					<input type="hidden" id="shiroEdit" value="1">
				</shiro:hasPermission>
				<shiro:hasPermission name="train:fzxMenu:del">
					<input type="hidden" id="shiroDel" value="1">
				</shiro:hasPermission>
				<shiro:hasPermission name="train:fzxMenu:add">
					<input type="hidden" id="shiroAdd" value="1">
				</shiro:hasPermission>
				</div>
               <%--  <table:page page="${page}"></table:page> --%>
	        </div>
	    </div>
    </div>
    <div class="loading"></div>
</body>
</html>