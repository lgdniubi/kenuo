<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>广告图配置</title>
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
   					 	url:"${ctx}/ec/adCategory/getChildren",
   					  	data: {id:id},
   					 	success:function(data){
   							var dataObj = eval("("+data+")"); 
   							//文本内容
   						 	var html = ""; 
							for(var i=0;i<dataObj.length;i++){ 
								var isChildren = dataObj[i].num>0?'hasChild=\"true\"':'';
								html += "<tr pid=\""+dataObj[i].parentId+"\" id=\""+dataObj[i].mtmyWebAdCategoryId+"\" "+isChildren+" >";
								html += "<td style='text-align: left;'>"+dataObj[i].name+"</td>";
								html += "<td>"+dataObj[i].level+"</td>";
								if(dataObj[i].positionType == '1'){
									html += "<td>首页</td>";
								}else if(dataObj[i].positionType == '2'){
									html += "<td>商城</td>";
								}else if(dataObj[i].positionType == '3'){
									html += "<td>生活美容</td>";
								}
								html += "<td>"+dataObj[i].sort+"</td>";
								html += "<td style='text-align: center;' id='isShow"+dataObj[i].mtmyWebAdCategoryId+"'>";
								if(dataObj[i].isShow == '1'){
									html += "<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+dataObj[i].mtmyWebAdCategoryId+"','0')\">"
								}else if(dataObj[i].isShow == '0'){
									html += "<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+dataObj[i].mtmyWebAdCategoryId+"','1')\">"
								}
								html += "</td>";
								html += "<td>"+dataObj[i].remarks+"</td>";
								html += "<td>";
								//操作权限-查看
								if($("#shiroView").val() == 1){
									html += "<a href=\"#\" onclick=\"openDialogView('查看分类', '${ctx}/ec/adCategory/form?mtmyWebAdCategoryId="+dataObj[i].mtmyWebAdCategoryId+"','430px', '480px')\" class=\"btn btn-info btn-xs\" ><i class=\"fa fa-search-plus\"></i> 查看</a>";
								}
								//操作权限-修改
								if($("#shiroEdit").val() == 1){
									html += "&nbsp;<a href=\"#\" onclick=\"openDialog('修改分类', '${ctx}/ec/adCategory/form?mtmyWebAdCategoryId="+dataObj[i].mtmyWebAdCategoryId+"','430px', '480px')\" class=\"btn btn-success btn-xs\" ><i class=\"fa fa-edit\"></i> 修改</a>";
								}
								//操作权限-删除
								if($("#shiroDel").val() == 1 && dataObj[i].mtmyWebAdCategoryId > 8){
									html += "&nbsp;<a href=\"${ctx}/ec/adCategory/delete?mtmyWebAdCategoryId="+dataObj[i].mtmyWebAdCategoryId+"\" onclick=\"return confirmx('要删除该分类及所有子分类吗？', this.href)\" class=\"btn btn-danger btn-xs\" ><i class=\"fa fa-trash\"></i> 删除</a>";
								}
								//操作权限-添加
								if($("#shiroAddChild").val() == 1 && dataObj[i].level == 1){
									html += "&nbsp;<a href=\"#\" onclick=\"openDialog('添加下级分类', '${ctx}/ec/adCategory/form?parentId="+dataObj[i].mtmyWebAdCategoryId+"','430px', '480px')\" class=\"btn btn-primary btn-xs\" ><i class=\"fa fa-plus\"></i> 添加下级分类</a>";
								}
								//操作权限-首页广告图管理
								if($("#shiroAddWebAd").val() == 1 && dataObj[i].level == 2){
									html += "&nbsp;<a href=\"#\" onclick=\"top.openTab('${ctx}/ec/webAd/list?categoryId="+dataObj[i].mtmyWebAdCategoryId+"','首页广告图管理', false)\" class=\"btn btn-primary btn-xs\" ><i class=\"fa fa-plus\"></i> 首页广告图管理</a>";
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
			window.location="${ctx}/ec/adCategory/list";
    	} 
    	
    	//是否显示
		function changeTableVal(id,isShow){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/ec/adCategory/updateIsShow?isShow="+isShow+"&mtmyWebAdCategoryId="+id,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					var isShow = data.ISSHOW;
					if("OK" == status){
						$("#isShow"+id).html("");//清除DIV内容
						if(isShow == '1'){
							$("#isShow"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+id+"','0')\">");
						}else if(isShow == '0'){
							$("#isShow"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+id+"','1')\">");
						}
					}else if("ERROR" == status){
						top.layer.alert(data.MESSAGE, {icon: 2, title:'提醒'});
					}
				}
			});   
		}
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="ec:adCategory:add">
								<table:addRow url="${ctx}/ec/adCategory/form?flag=1" width="430px" height="480px" title="添加广告图分类"></table:addRow><!-- 增加按钮 -->
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
						</div>
					</div>
				</div>
				<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th width="130" style="text-align: center;">名称</th>
							<th width="130" style="text-align: center;">等级</th>
							<th width="130" style="text-align: center;">位置类型</th>
							<th width="130" style="text-align: center;">排序</th>
							<th width="130" style="text-align: center;">是否显示</th>
							<th width="130" style="text-align: center;">备注</th>
							<th width="300" style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="row" >
						<tr id="${row.mtmyWebAdCategoryId}" pid="${row.parentId}" ${row.num>0?'hasChild="true"':''}>
							<td style="text-align: left;">${row.name}</td>
							<td>${row.level}</td>
							<td>
								<c:if test="${row.positionType == '0'}"></c:if>
								<c:if test="${row.positionType == '1'}">首页</c:if>
								<c:if test="${row.positionType == '2'}">商城</c:if>
								<c:if test="${row.positionType == '3'}">生活美容</c:if>
							</td>
							<td>${row.sort}</td>
							<td style="text-align: center;" id="isShow${row.mtmyWebAdCategoryId}">
								<c:if test="${row.isShow=='1'}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('${row.mtmyWebAdCategoryId}','0')">
								</c:if>
								<c:if test="${row.isShow=='0'}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('${row.mtmyWebAdCategoryId}','1')">
								</c:if>
							</td>
							<td>${row.remarks}</td>
							<td>
								<shiro:hasPermission name="ec:adCategory:view">
									<a href="#" onclick="openDialogView('查看分类', '${ctx}/ec/adCategory/form?flag=2&mtmyWebAdCategoryId=${row.mtmyWebAdCategoryId}','430px', '480px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission> 
								<shiro:hasPermission name="ec:adCategory:edit">
									<a href="#" onclick="openDialog('修改分类', '${ctx}/ec/adCategory/form?flag=3&mtmyWebAdCategoryId=${row.mtmyWebAdCategoryId}','430px', '480px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<c:if test="${row.mtmyWebAdCategoryId > 8}">
									<shiro:hasPermission name="ec:adCategory:del">
										<a href="${ctx}/ec/adCategory/delete?mtmyWebAdCategoryId=${row.mtmyWebAdCategoryId}" onclick="return confirmx('要删除该分类及所有子分类吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
									</shiro:hasPermission>
								</c:if>
								<c:if test="${row.level == 1}">
									<shiro:hasPermission name="ec:adCategory:addChild">
										<a href="#" onclick="openDialog('添加下级分类', '${ctx}/ec/adCategory/form?parentId=${row.mtmyWebAdCategoryId}','430px', '480px')" class="btn  btn-primary btn-xs"><i class="fa fa-plus"></i> 添加下级分类</a>
									</shiro:hasPermission>
								</c:if>
								<c:if test="${row.level == 2}">
									<shiro:hasPermission name="ec:adCategory:addWebAd">
										<a href="#" onclick='top.openTab("${ctx}/ec/webAd/list?categoryId=${row.mtmyWebAdCategoryId}","首页广告图管理", false)' class="btn  btn-primary btn-xs"><i class="fa fa-plus"></i> 首页广告图管理</a>
									</shiro:hasPermission>
								</c:if>
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
	 <shiro:hasPermission name="ec:adCategory:view">
	 	<input type="hidden" id="shiroView" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="ec:adCategory:edit">
		<input type="hidden" id="shiroEdit" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="ec:adCategory:del">
		<input type="hidden" id="shiroDel" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="ec:adCategory:addChild">
		<input type="hidden" id="shiroAddChild" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="ec:adCategory:addWebAd">
		<input type="hidden" id="shiroAddWebAd" value="1">
	</shiro:hasPermission>
</body>
</html>