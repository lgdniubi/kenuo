<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>添加通用设备</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->


<script type="text/javascript">
	function addComEquipment(id){
		top.layer.open({
		    type: 2, 
		    area: ['600px', '550px'],
		    title:"新增设备",
		    content: "${ctx}/ec/specEquipment/shopComEquipmentForm?shopId="+id,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		        var obj =  layero.find("iframe")[0].contentWindow;
				var equipmentId = obj.document.getElementById("equipmentId");
		    	var sum = obj.document.getElementById("sum");
		    	var name = obj.document.getElementById("equipmentName");
		        var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
		        var all = new Array(); //定义变量全部保存
				
		       if($(equipmentId).val()== null || $(equipmentId).val()==""){
					top.layer.alert('设备名称不能为空!', {icon: 0, title:'提醒'}); 
					return;
				}
		        if($(sum).val() == null || $(sum).val() == ""){
					top.layer.alert('设备数量不可为空！', {icon: 0, title:'提醒'});
					return;
				} 
		        
		        //异步添加店铺通用设备
				$.ajax({
				type:"post",
				data:{
					equipmentId:$(equipmentId).val(),
					name:$(name).val(),
					sum:$(sum).val()
				 },
				url:"${ctx}/ec/specEquipment/saveshopComEquipment?shopId="+id,
				success:function(date){
					if(date=="success"){
						top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
						window.location="${ctx}/ec/specEquipment/shopComEquipmentList?shopId=${shopComEquipment.shopId}";
					}
					if(date=="error"){
						top.layer.alert('保存失败!', {icon: 2, title:'提醒'});
						window.location="${ctx}/ec/specEquipment/shopComEquipmentList?shopId=${shopComEquipment.shopId}";
					}
								
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
							    
				}
							 
		});
		top.layer.close(index);
		},
		cancel: function(index){ //或者使用btn2
			    	           //按钮【按钮二】的回调
		  }
	}); 

	}
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<form id="searchForm" action="${ctx}/ec/specEquipment/shopComEquipmentList" method="post">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
					<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" /><!-- 支持排序 -->
					<input name="shopId" value="${shopComEquipment.shopId}" type="hidden">
					<div class="form-group">
						<input id="name" name="name" value="${shopComEquipment.name}" class=" form-control input-sm" style="width:200px;" placeholder="设备名称">
					</div>
				</form>
				<div>
					<a href="#" onclick="addComEquipment('${shopComEquipment.shopId}')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加设备</a>
					<div class="pull-right">
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
					</div>
				</div>
				<p></p>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">设备名称</th>
							<th style="text-align: center;">状态</th>
							<shiro:hasPermission name="ec:specEquipment:editLog">
								<th style="text-align: center;">日志</th>
							</shiro:hasPermission>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="page">
							<tr>
								<td style="text-align: center;">${page.shopComEquipmentId}</td>
								<td style="text-align: center;">${page.name}</td>
								<td style="text-align: center;" id="${page.shopComEquipmentId}">
									<c:if test="${page.isEnabled == '1'}">
										<a href="${ctx}/ec/specEquipment/updateType?shopComEquipmentId=${page.shopComEquipmentId}&flag=0&shopId=${page.shopId}&name=${page.name}" >
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" >
										</a>	
									</c:if>
									<c:if test="${page.isEnabled eq '0'}">
										<a href="${ctx}/ec/specEquipment/updateType?shopComEquipmentId=${page.shopComEquipmentId}&flag=1&shopId=${page.shopId}&name=${page.name}" >
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png">
										</a>
									</c:if>
								</td>
								<shiro:hasPermission name="ec:specEquipment:editLog">
									<td style="text-align: center;">
										<a href="#" onclick="openDialogView('设备操作日志', '${ctx}/ec/specEquipment/editLog?comEquipmentId=${page.shopComEquipmentId}','600px','550px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>操作日志</a>
									</td>
								</shiro:hasPermission>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<!-- 分页table -->
				<table:page page="${page}"></table:page>
			</div>
		</div>
	</div>
	
</body>
</html>