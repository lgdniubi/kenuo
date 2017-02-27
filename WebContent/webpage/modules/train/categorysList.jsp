<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> 批量删除 由于样式问题  取消 --%>
<html>
<head>
	<title>课程分类管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<%@include file="/webpage/include/treetable.jsp" %>
	<script type="text/javascript">
		//页面加载
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
   					 	url:"${ctx}/train/categorys/getChildren",
   					  	data: {id:id},
   					 	success:function(data){
   							var dataObj = eval("("+data+")"); 
   							//文本内容
   						 	var html = ""; 
							for(var i=0;i<dataObj.length;i++){   
								var isChildren = dataObj[i].num>0?'hasChild=\"true\"':'';
								html += "<tr pid=\""+dataObj[i].parentId+"\" id=\""+dataObj[i].categoryId+"\" "+isChildren+" >";
								/* html += "<td><input type=\"checkbox\" id="+dataObj[i].categoryId+" class=\"i-checks\"></td>"; 批量删除 由于样式问题  取消*/
								html += "<td nowrap><i class=\"icon-menu.icon\"></i>"+dataObj[i].name+"</td>";
								html += "<td>"+dataObj[i].introduce+"</td>";
								if(dataObj[i].parentId != 0){
									html += "<td><img alt=\"images\" style=\"width: 220px;height: 120px;\" src=\""+dataObj[i].coverPic+"\"></td>";
								}else{
									html += "<td></td>";
								}
								if(dataObj[i].isShow == 1){
									html += "<td style='text-align: center;' id='isShow"+dataObj[i].categoryId+"'><img  width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeVal('isShow','"+dataObj[i].categoryId+"','0')\"></td>";
								}else{
									html += "<td style='text-align: center;' id='isShow"+dataObj[i].categoryId+"'><img  width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeVal('isShow','"+dataObj[i].categoryId+"','1')\"></td>";
								}
								html += "<td>"+dataObj[i].office.name+"</td>";
								html += "<td>"+dataObj[i].sort+"</td>";
								html += "<td style='text-align: left;'>";
								//操作权限-查看
								if($("#shiroView").val() == 1){
									html += "<a href=\"#\" onclick=\"openDialogView('查看菜单', '${ctx}/train/categorys/form?categoryId="+dataObj[i].categoryId+"&opflag=VIEW','430px', '480px')\" class=\"btn btn-info btn-xs\" ><i class=\"fa fa-search-plus\"></i> 查看</a>";
								}
								if(dataObj[i].cateType != '1' || dataObj[i].officeType == '1'){
									//操作权限-修改
									if($("#shiroEdit").val() == 1){
										html += "&nbsp;<a href=\"#\" onclick=\"openDialog('修改菜单', '${ctx}/train/categorys/form?categoryId="+dataObj[i].categoryId+"&opflag=UPDATE','430px', '480px')\" class=\"btn btn-success btn-xs\" ><i class=\"fa fa-edit\"></i> 修改</a>";
									}
									//操作权限-删除
									if($("#shiroDel").val() == 1){
										html += "&nbsp;<a href=\"${ctx}/train/categorys/deletecategorys?categoryId="+dataObj[i].categoryId+"\" onclick=\"return confirmx('要删除该菜单及所有子菜单项吗？', this.href)\" class=\"btn btn-danger btn-xs\" ><i class=\"fa fa-trash\"></i> 删除</a>";
									}
								}
								if(dataObj[i].parentId == '0'){
									//操作权限-添加
									if($("#shiroAdd").val() == 1){
										html += "&nbsp;<a href=\"#\" onclick=\"openDialog('添加下级菜单', '${ctx}/sys/menu/form?parent.id="+dataObj[i].categoryId+"','800px', '500px')\" class=\"btn btn-primary btn-xs\" ><i class=\"fa fa-plus\"></i> 添加下级菜单</a>";
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
		
		
		//重置表单
		function resetnew(){
			$("#name").val("");
			reset();
		}
		
		//刷新
		function refresh(){
			window.location="${ctx}/train/categorys/findalllist";
		}
		
		//是否推荐/是否显示 改变事件
		function changeVal(flag,id,isShow){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/train/categorys/updateIsShow?categoryId="+id+"&isShow="+isShow,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var STATUS = data.STATUS;
					var ISSHOW = data.ISSHOW;
					var CATEGORYIDS = data.CATEGORYIDS;
					if("OK" == STATUS){
						arr=CATEGORYIDS.split(',');//注split可以用字符或字符串分割
			            for(var i=0;i<arr.length;i++){
			            	$("#"+flag+arr[i]).html("");//清除DIV内容	
							if(ISSHOW == '1'){
								$("#"+flag+arr[i]).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeVal('"+flag+"','"+arr[i]+"','0')\">");
							}else if(ISSHOW == '0'){
								$("#"+flag+arr[i]).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeVal('"+flag+"','"+arr[i]+"','1')\">");
							} 
			            } 
					}else if("ERROR" == STATUS){
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
					<h5>课程分类管理</h5>
				</div>
				<div class="ibox-content">
					<div class="searcharea clearfix">
						<form:form id="searchForm" action="${ctx}/train/categorys/findalllist" method="post" class="navbar-form navbar-left searcharea">
							<div class="form-group">
								<label>关键字：<input id="name" name="name" maxlength="10" type="text" class="form-control" value="${trainCategorys.name}" ></label> 
							</div>
							<shiro:hasPermission name="train:categorys:findalllist">
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
									<i class="fa fa-search"></i> 搜索
								</button>
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="resetnew()" >
									<i class="fa fa-refresh"></i> 重置
								</button>
							</shiro:hasPermission>
						</form:form>
					</div>
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="train:categorys:addcategorys">
									<table:addRow url="${ctx}/train/categorys/addcategorys" width="430px" height="480px" title="添加课程分类"></table:addRow><!-- 增加按钮 -->
								</shiro:hasPermission>
								<%-- <shiro:hasPermission name="train:categorys:batchdeletecategorys">
									<table:delRow url="${ctx}/train/categorys/batchdeletecategorys" id="treeTable"></table:delRow><!-- 删除按钮 -->
								</shiro:hasPermission> 批量删除 由于样式问题  取消  --%>
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
							</div>
						</div>
					</div>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<!-- <th width="40" style="text-align: center;"><input type="checkbox" class="i-checks"></th>  批量删除 由于样式问题  取消 -->
								<th width="120" style="text-align: center;">名称</th>
								<th style="text-align: center">课程介绍</th>
								<th width="230" style="text-align: center;">图片</th>
								<th width="230" style="text-align: center;">是否显示</th>
								<th width="230" style="text-align: center;">权限范围</th>
								<th width="230" style="text-align: center;">排序</th>
								<th width="300" style="text-align: center;">课程操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${list}" var="trainCategory">
								<tr pid="${trainCategory.parentId}" id="${trainCategory.categoryId}" ${trainCategory.num>0?'hasChild="true"':''}>
								    <%-- <td><input type="checkbox" id="${trainCategory.categoryId}" class="i-checks"></td>  批量删除 由于样式问题  取消 --%>
									<td nowrap style="text-align: left;"><i class="icon-menu.icon"></i>${trainCategory.name}</td>
									<td>${trainCategory.introduce}</td>
									<td>
										<c:if test="${trainCategory.parentId != '0' }">
											<img alt="images" style="width: 220px;height: 120px;" src="${trainCategory.coverPic}">
										</c:if>
									</td>
									<td style="text-align: center;" id="isShow${trainCategory.categoryId}">
										<c:if test="${trainCategory.isShow == 1}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeVal('isShow','${trainCategory.categoryId}','0')">
										</c:if>
										<c:if test="${trainCategory.isShow == 0}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeVal('isShow','${trainCategory.categoryId}','1')">
										</c:if>
									</td>
									<td>${trainCategory.office.name}</td>
									<td>${trainCategory.sort}</td>
									<td style="text-align: left;">
										<shiro:hasPermission name="train:categorys:form">
											<a href="#" onclick="openDialogView('查看课程', '${ctx}/train/categorys/form?categoryId=${trainCategory.categoryId}&opflag=VIEW','430px', '480px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
										</shiro:hasPermission>
										<!-- 1、分类类型为：公共时，才能允许有修改与删除功能 
										   或者 2、登录用户角色的机构为1（可诺丹婷），才能允许有修改与删除功能
										-->
										<c:if test="${(trainCategory.cateType != '1') || (trainCategorys.officeType == '1')}">
											<shiro:hasPermission name="train:categorys:updatecategorys">
					    						<a href="#" onclick="openDialog('修改课程', '${ctx}/train/categorys/form?categoryId=${trainCategory.categoryId}&opflag=UPDATE','430px', '480px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
						    				</shiro:hasPermission>
						    				<shiro:hasPermission name="train:categorys:deletecategorys">
												<a href="${ctx}/train/categorys/deletecategorys?categoryId=${trainCategory.categoryId}" onclick="return confirmx('要删除该课程及所有子课程吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
											</shiro:hasPermission>
										</c:if>
										<c:if test="${trainCategory.parentId == '0' }">
											<shiro:hasPermission name="train:categorys:juniorcategorys">
												<a href="#" onclick="openDialog('添加下级课程', '${ctx}/train/categorys/addcategorys?categoryId=${trainCategory.categoryId}','430px', '480px')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i> 添加下级课程</a>
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
	</div>
	<div class="loading"></div>
	<!-- 用于异步加载时，操作权限设置 -->
	<shiro:hasPermission name="train:categorys:form">
		<input type="hidden" id="shiroView" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="train:categorys:updatecategorys">
		<input type="hidden" id="shiroEdit" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="train:categorys:deletecategorys">
		<input type="hidden" id="shiroDel" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="train:categorys:juniorcategorys">
		<input type="hidden" id="shiroAdd" value="1">
	</shiro:hasPermission>
</body>
</html>