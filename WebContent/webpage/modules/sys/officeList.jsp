<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>机构管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<%@ include file="/webpage/include/treetable.jsp"%>
	<script type="text/javascript">
		//页面加载
		$(document).ready(function() {
			//树形table配置
			var option = {
                expandLevel : 1,
                beforeExpand : function($treeTable, id) {
                    //判断id是否已经有了孩子节点，如果有了就不再加载，这样就可以起到缓存的作用
                    if ($('.' + id, $treeTable).length) { 
                    	return; 
                    }
                    $(".loading").show();//打开展示层
                    $.ajax({
                    	type:"post",
   					 	url:"${ctx}/sys/office/findByPidforChild",
   					  	data: {id:id},
   					 	success:function(data){
   							var dataObj = eval("("+data+")"); 
   							//文本内容
   						 	var html = ""; 
							for(var i=0;i<dataObj.length;i++){   
								var isChildren = dataObj[i].num>0?'hasChild=\"true\"':'';
								html += "<tr pid=\""+dataObj[i].parentId+"\" id=\""+dataObj[i].id+"\" "+isChildren+" >";
								html += "<td nowrap style=\"text-align: left;\"><a href=\"#\" onclick=\"openDialogView('查看机构', '${ctx}/sys/office/form?id="+dataObj[i].id+"','800px', '620px')\">"+dataObj[i].name+"</a></td>";
								html += "<td>"+dataObj[i].area.name+"</td>";
								html += "<td>"+dataObj[i].code+"</td>";
								html += "<td>"+dataObj[i].typeName+"</td>";
								html += "<td>"+dataObj[i].remarks+"</td>";
								html += "<td>";
								//操作权限-查看
								if($("#shiroView").val() == 1){
									if(dataObj[i].parentId == '0' && dataObj[i].code != '10001' ){
										html += "<a href=\"#\" onclick=\"openDialogView('查看机构', '${ctx}/sys/franchisee/form?id="+dataObj[i].id+"&opflag=VIEW','800px', '620px')\" class=\"btn btn-info btn-xs\"><i class=\"fa fa-search-plus\"></i> 查看</a>";
									}else{
										html += "<a href=\"#\" onclick=\"openDialogView('查看机构', '${ctx}/sys/office/form?id="+dataObj[i].id+"&num="+dataObj[i].num+"','800px', '620px')\" class=\"btn btn-info btn-xs\"><i class=\"fa fa-search-plus\"></i> 查看</a>";
									}
								}
								if(dataObj[i].parent.id != '0'){
									//操作权限-修改
									if($("#shiroEdit").val() == 1){
										html += "<a href=\"#\" onclick=\"openDialog('修改机构', '${ctx}/sys/office/form?id="+dataObj[i].id+"&num="+dataObj[i].num+"','800px', '620px')\" class=\"btn btn-success btn-xs\"><i class=\"fa fa-edit\"></i> 修改</a>";
									}
									//操作权限-删除
									if($("#shiroDel").val() == 1){
										html += "<a href=\"${ctx}/sys/office/delete?id="+dataObj[i].id+"\" onclick=\"return confirmx('要删除该机构及所有子机构项吗？', this.href)\" class=\"btn btn-danger btn-xs\"><i class=\"fa fa-trash\"></i> 删除</a>";
									}
								}	
								if(dataObj[i].grade != '1'){
									//操作权限-添加
									if($("#shiroAdd").val() == 1){
										html += "<a href=\"#\" onclick=\"openDialog('添加下级机构', '${ctx}/sys/office/form?parent.id="+dataObj[i].id+"','800px', '620px')\" class=\"btn  btn-primary btn-xs\"><i class=\"fa fa-plus\"></i> 添加下级机构</a>";
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
	
		//刷新或者排序，页码不清零
		function refresh(){
			window.location="${ctx}/sys/office/list";
    	}    
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>机构列表</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<!-- 无查询条件 -->
						<form:form id="searchForm" modelAttribute="" action="${ctx}/sys/office/export" method="post" class="form-inline"></form:form>
					</div>
				</div>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<!-- update 导入数据 -->
							<shiro:hasPermission name="sys:office:importPage">
								<a href="#" onclick="openDialog('导入数据', '${ctx}/sys/office/importPage','400px', '220px')" class="btn btn-white btn-sm"><i class="fa fa-folder-open-o"></i> 导入</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="sys:office:export">
								<!-- 导出按钮 -->
								<table:exportExcel url="${ctx}/sys/office/export"></table:exportExcel>
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新">
								<i class="glyphicon glyphicon-repeat"></i> 刷新
							</button>
						</div>
					</div>
				</div>
				<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th>机构名称</th>
							<th>归属区域</th>
							<th>机构编码</th>
							<th>机构类型</th>
							<th>备注</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="row" varStatus="status">
							<c:if test="${status.index == 0}">
								<tr id="${row.id}" pid="0" ${row.num>0?'hasChild="true"':''}>
							</c:if>
							<c:if test="${status.index != 0}">
								<tr id="${row.id}" pid="${row.parent.id}" ${row.num>0?'hasChild="true"':''}>
							</c:if>
								<td nowrap style="text-align: left;">
									<a href="#" onclick="openDialogView('查看机构', '${ctx}/sys/office/form?id=${row.id}','800px', '620px')">${row.name}</a>
								</td>
								<td>${row.area.name}</td>
								<td>${row.code}</td>
								<td>${fns:getDictLabel(row.type, 'sys_office_type', '')}</td>
								<td>${row.remarks}</td>
								<td>
									<shiro:hasPermission name="sys:office:view">
										<c:choose>
											<c:when test="${row.parent.id =='0' || row.parent.id =='1'}">
												<%-- <a href="#" onclick="openDialogView('查看机构', '${ctx}/sys/franchisee/form?id=${row.id}&opflag=VIEW','800px', '620px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a> --%>
											</c:when>
											<c:otherwise>
												<a href="#" onclick="openDialogView('查看机构', '${ctx}/sys/office/form?id=${row.id}&&num=${row.num}','800px', '620px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
											</c:otherwise>
										</c:choose>
									</shiro:hasPermission> 
									<c:if test="${row.parent.id !='0' && row.parent.id !='1'}">
										<shiro:hasPermission name="sys:office:edit">
											<a href="#" onclick="openDialog('修改机构', '${ctx}/sys/office/form?id=${row.id}&&num=${row.num}','800px', '620px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="sys:office:del">
											<a href="${ctx}/sys/office/delete?id=${row.id}" onclick="return confirmx('要删除该机构及所有子机构项吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
										</shiro:hasPermission>
									</c:if> 
									<c:if test="${row.grade != '1' && row.code !='1'}">
										<shiro:hasPermission name="sys:office:add">
											<a href="#" onclick="openDialog('添加下级机构', '${ctx}/sys/office/form?parent.id=${row.id}','800px', '620px')" class="btn  btn-primary btn-xs"><i class="fa fa-plus"></i> 添加下级机构</a>
										</shiro:hasPermission>
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<!-- 用于异步加载时，操作权限设置 -->
				<shiro:hasPermission name="sys:office:view">
					<input type="hidden" id="shiroView" value="1">
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:office:edit">
					<input type="hidden" id="shiroEdit" value="1">
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:office:del">
					<input type="hidden" id="shiroDel" value="1">
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:office:add">
					<input type="hidden" id="shiroAdd" value="1">
				</shiro:hasPermission>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>