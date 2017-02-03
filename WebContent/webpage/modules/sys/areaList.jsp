<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>区域管理</title>
	<meta name="decorator" content="default" />
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<%@ include file="/webpage/include/treetable.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
			//树形table配置
			var option = {
                expandLevel : 2,
                beforeExpand : function($treeTable, id) {
                    //判断id是否已经有了孩子节点，如果有了就不再加载，这样就可以起到缓存的作用
                    if ($('.' + id, $treeTable).length) { 
                    	return; 
                    }
                    $(".loading").show();//打开展示层
                   
                    $.ajax({
                    	type:"post",
   					 	url:"${ctx}/sys/area/findByPidforChild",
   					  	data: {id:id},
   					 	success:function(data){
   							var dataObj = eval("("+data+")"); 
   							//文本内容
   						 	var html = ""; 
							for(var i=0;i<dataObj.length;i++){   
								var isChildren = dataObj[i].childrenNum>0?'hasChild=\"true\"':'';
								html += "<tr pid=\""+dataObj[i].parentId+"\" id=\""+dataObj[i].id+"\" "+isChildren+" >";
								html += "<td style=\"text-align: left;margin-left: 10px;\"><a href=\"#\" onclick=\"openDialogView('查看区域', '${ctx}/sys/area/form?id="+dataObj[i].id+"','800px', '380px')\">"+dataObj[i].name+"</a></td>";
								html += "<td>"+dataObj[i].code+"</td>";
								html += "<td>"+dataObj[i].typeName+"</td>";
								html += "<td>"+dataObj[i].remarks+"</td>";
								html += "<td>";
								if($("#shiroView").val() == 1){
									html += "<a href=\"#\" onclick=\"openDialogView('查看区域', '${ctx}/sys/area/form?id="+dataObj[i].id+"','800px', '380px')\" class=\"btn btn-info btn-xs\" ><i class=\"fa fa-search-plus\"></i> 查看</a>";
								}
								if($("#shiroEdit").val() == 1){
									html += "<a href=\"#\" onclick=\"openDialog('修改区域', '${ctx}/sys/area/form?id="+dataObj[i].id+"','800px', '380px')\" class=\"btn btn-success btn-xs\" ><i class=\"fa fa-edit\"></i> 修改</a>";
								}
								if($("#shiroDel").val() == 1){
									html += "<a href=\"${ctx}/sys/area/delete?id="+dataObj[i].id+"\" onclick=\"return confirmx('要删除该区域及所有子区域项吗？', this.href)\" class=\"btn btn-danger btn-xs\" ><i class=\"fa fa-trash\"></i> 删除</a>";
								}
								if($("#shiroAdd").val() == 1){
									html += "<a href=\"#\" onclick=\"openDialog('添加下级区域', '${ctx}/sys/area/form?parent.id="+dataObj[i].id+"','800px', '380px')\" class=\"btn btn-primary btn-xs\" ><i class=\"fa fa-plus\"></i> 添加下级区域</a>";
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
			window.location="${ctx}/sys/area/list";
		}
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>区域列表</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<!-- 无查询条件 -->
					</div>
				</div>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新">
								<i class="glyphicon glyphicon-repeat"></i> 刷新
							</button>
						</div>
					</div>
				</div>
				<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th>区域名称</th>
							<th>区域编码</th>
							<th>区域类型</th>
							<th>备注</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list }" var="row" varStatus="status">
							<c:if test="${status.index == '0'}">
								<tr id="${row.id}" pid="0" ${row.childrenNum>0?'hasChild="true"':''}>
							</c:if>
							<c:if test="${status.index != '0'}">
								<tr id="${row.id}" pid="${row.parent.id}" ${row.childrenNum>0?'hasChild="true"':''}>
							</c:if>
								<td style="text-align: left;margin-left: 10px;"><a href="#" onclick="openDialogView('查看区域', '${ctx}/sys/area/form?id=${row.id}','800px', '380px')">${row.name}</a></td>
								<td>${row.code}</td>
								<td>${fns:getDictLabel(row.type, 'sys_area_type', '')}</td>
								<td>${row.remarks}</td>
								<td>
									<shiro:hasPermission name="sys:area:view">
										<a href="#" onclick="openDialogView('查看区域', '${ctx}/sys/area/form?id=${row.id}','800px', '380px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i>  查看</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="sys:area:edit">
										<a href="#" onclick="openDialog('修改区域', '${ctx}/sys/area/form?id=${row.id}','800px', '380px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="sys:area:del">
										<a href="${ctx}/sys/area/delete?id=${row.id}" onclick="return confirmx('要删除该区域及所有子区域项吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="sys:area:add">
										<a href="#" onclick="openDialog('添加下级区域', '${ctx}/sys/area/form?parent.id=${row.id}','800px', '380px')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i> 添加下级区域</a>
									</shiro:hasPermission>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<!-- 用于异步加载时，操作权限设置 -->
				<shiro:hasPermission name="sys:area:view">
					<input type="hidden" id="shiroView" value="1">
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:area:edit">
					<input type="hidden" id="shiroEdit" value="1">
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:area:del">
					<input type="hidden" id="shiroDel" value="1">
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:area:add">
					<input type="hidden" id="shiroAdd" value="1">
				</shiro:hasPermission>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>