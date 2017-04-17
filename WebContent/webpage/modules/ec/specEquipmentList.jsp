<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>设备表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		//刷新
		function refresh(){
			window.location="${ctx}/ec/specEquipment/list";
		}
		
		function newSearch(){
			$("#searchForm").submit();
		}
		
		$(document).ready(function() {
			$("#shopButton").click(function(){
				var bazaarId = $("#bazaarId").val();
				// 是否限制选择，如果限制，设置为disabled
				if ($("#shopButton").hasClass("disabled")){
					return true;
				}
				
				if(bazaarId == null || bazaarId == ""){
					top.layer.alert('请先选择市场!', {icon: 0, title:'提醒'});
				}else{
					// 正常打开	
					top.layer.open({
					    type: 2, 
					    area: ['300px', '420px'],
					    title:"选择所在店铺",
					    ajaxData:{selectIds: $("#shopId").val()},
					    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/ec/specEquipment/treeDataForShop?id="+bazaarId)+"&module=&checked=&extId=&isAll=" ,
					    	btn: ['确定', '关闭']
			    	       ,yes: function(index, layero){ //或者使用btn1
			    	    	   var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
								var ids = [], names = [], nodes = [];
								if ("" == "true"){
									nodes = tree.getCheckedNodes(true);
								}else{
									nodes = tree.getSelectedNodes();
								}
								for(var i=0; i<nodes.length; i++) {//
									ids.push(nodes[i].id);
									names.push(nodes[i].name);//
									break; // 如果为非复选框选择，则返回第一个选择  
								}
								$("#shopId").val(ids.join(",").replace(/u_/ig,""));
								$("#shopName").val(names.join(","));
								$("#shopName").focus();
								//预约管理-->> 修改预约地址时异步加载美容师   2016-06-29  咖啡
								if("shop" == "officeIdbeaut"){
									findBeauty();
								}
								top.layer.close(index);
						    	       },
			    	cancel: function(index){ //或者使用btn2
			    	           //按钮【按钮二】的回调
			    	       }
					}); 
			
				}
			});
		
			$("#bazaarButton").click(function(){
				$("#shopId").val("");
				$("#shopName").val("");
				// 是否限制选择，如果限制，设置为disabled
				if ($("#bazaarButton").hasClass("disabled")){
					return true;
				}
				// 正常打开	
				top.layer.open({
				    type: 2, 
				    area: ['300px', '420px'],
				    title:"选择市场",
				    ajaxData:{selectIds: $("#bazaarId").val()},
				    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/sys/office/treeData?isGrade=true")+"&module=&checked=&extId=&isAll=" ,
				    btn: ['确定', '关闭']
		    	       ,yes: function(index, layero){ //或者使用btn1
								var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
								var ids = [], names = [], nodes = [];
								if ("" == "true"){
									nodes = tree.getCheckedNodes(true);
								}else{
									nodes = tree.getSelectedNodes();
								}
								for(var i=0; i<nodes.length; i++) {//
									/* if (nodes[i].level == 0){
										//top.$.jBox.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
										top.layer.msg("不能选择根节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
										return false;
									}// */
									if (nodes[i].isParent){
										//top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
										//layer.msg('有表情地提示');
										top.layer.msg("不能选择父节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
										return false;
									}//
									ids.push(nodes[i].id);
									names.push(nodes[i].name);//
									break; // 如果为非复选框选择，则返回第一个选择  
								}
								$("#bazaarId").val(ids.join(",").replace(/u_/ig,""));
								$("#bazaarName").val(names.join(","));
								$("#bazaarName").focus();
								//预约管理-->> 修改预约地址时异步加载美容师   2016-06-29  咖啡
								if("bazaar" == "officeIdbeaut"){
									findBeauty();
								}
								top.layer.close(index);
						    	       },
		    	cancel: function(index){ //或者使用btn2
		    	           //按钮【按钮二】的回调
		    	       }
				}); 
			
			});
		});
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>设备表</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="equipment" action="${ctx}/ec/specEquipment/list" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
							
							<div class="form-group" >
								<span>设备名称：</span>
								<form:input path="name" htmlEscape="false" maxlength="50" class=" form-control input-sm" style="width:200px"/> 
								<span>选择市场：</span> 
								<input id="bazaarId" class=" form-control input-sm" name="bazaarId" value="${equipment.bazaarId}" type="hidden">
								<div class="input-group">
									<input id="bazaarName" class=" form-control input-sm" name="bazaarName" readonly="readonly" value="${equipment.bazaarName}" data-msg-required="" style="" type="text"> 
									<span class="input-group-btn">
										<button id="bazaarButton" class="btn btn-sm btn-primary " type="button">
											<i class="fa fa-search"></i>
										</button>
									</span>
								</div>
								<label id="bazaarName-error" class="error" for="bazaarName" style="display: none"></label>


								<%-- <sys:treeselect id="bazaar" name="bazaarId" value="${equipment.bazaarId}" labelName="bazaarName" labelValue="${equipment.bazaarName}" title="市场" url="/sys/office/treeData?isGrade=true" cssClass=" form-control input-sm" allowClear="true" notAllowSelectRoot="true" notAllowSelectParent="true"/> --%>
								
								<span>选择店铺：</span>
								<input id="shopId" class=" form-control input-sm" name="shopId" value="${equipment.shopId}" type="hidden">
									<div class="input-group">
										<input id="shopName" class=" form-control input-sm" name="shopName" readonly="readonly" value="${equipment.shopName}" data-msg-required="" style="" type="text">
											<span class="input-group-btn">
												<button id="shopButton" class="btn btn-sm btn-primary " type="button">
													<i class="fa fa-search"></i>
												</button>
											</span>
									</div>
									<label id="shopName-error" class="error" for="shopName" style="display:none"></label>
							</div>
						</form:form>
						<br />
					</div>
				</div>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="ec:specEquipment:add">
								<table:addRow url="${ctx}/ec/specEquipment/form?flag=1" title="设备" width="600px" height="400px" ></table:addRow><!-- 增加按钮 -->
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新">
								<i class="glyphicon glyphicon-repeat"></i> 刷新
							</button>
						</div>
						<div class="pull-right">
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="newSearch()">
								<i class="fa fa-search"></i> 查询
							</button>
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()">
								<i class="fa fa-refresh"></i> 重置
							</button>
						</div>
					</div>
				</div>
				<!-- 主要内容展示 -->
				<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th style="text-align: center;">设备编号</th>
							<th style="text-align: center;">设备名称</th>
							<th style="text-align: center;">所在市场</th>
							<th style="text-align: center;">所在店铺</th>
							<th style="text-align: center;">设备类型</th>
							<th style="text-align: center;">备注</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<c:forEach items="${page.list}" var="equipment">
						<tr>
							<td style="text-align: center;">${equipment.labelId}</td>
							<td style="text-align: center;">${equipment.name}</td>
							<td style="text-align: center;">${equipment.bazaarName}</td>
							<td style="text-align: center;">${equipment.shopName}</td>
							<td style="text-align: center;">
								<c:if test="${equipment.type == 1}" >特殊</c:if>
								<c:if test="${equipment.type == 2}" >通用</c:if>
							</td>
							<td style="text-align: center;">${equipment.remarks}</td>
							<td style="text-align: center;">
								<shiro:hasPermission name="ec:specEquipment:view">
									<a href="#" onclick="openDialogView('设备排班详情日志', '${ctx}/ec/specEquipment/equipmentLogsForm?equipmentId=${equipment.equipmentId}&flag=2','700px','550px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 详情</a>
								</shiro:hasPermission>
								<%-- <shiro:hasPermission name="ec:specEquipment:edit">
									<a href="#" onclick="openDialog('修改设备', '${ctx}/ec/specEquipment/form?equipmentId=${equipment.equipmentId}&flag=2','600px','400px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission> --%>
								<shiro:hasPermission name="ec:specEquipment:del">
									<a href="${ctx}/ec/specEquipment/delete?equipmentId=${equipment.equipmentId}" onclick=" return confirmx('确认要删除该设备吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</c:forEach>
				</table>
				<!-- 分页table -->
				<table:page page="${page}"></table:page>
			</div>
		</div>
	</div>
</body>
</html>