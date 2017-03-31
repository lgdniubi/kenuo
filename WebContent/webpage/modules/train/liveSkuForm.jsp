<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>配置列表</title>
<meta name="decorator" content="default" />

<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	function editSku(id){
		top.layer.open({
		    type: 2, 
		    area: ['430px', '480px'],
		    title:"修改配置",
		    content: "${ctx}/train/live/editSku?trainLiveSkuId="+id,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		    	var obj =  layero.find("iframe")[0].contentWindow;
				var price = obj.document.getElementById("price");
				var num = obj.document.getElementById("num");
		    	if(!/^\d+(\.\d{1,2})?$/.test($(price).val())){
					top.layer.alert('价格的小数点后不可以超过2位!', {icon: 0, title:'提醒'});
					return;
				}
		    	
		        //异步保存配置
				$.ajax({
				type:"post",
				data:{
					price:$(price).val(),
					num:$(num).val()
				 },
				url:"${ctx}/train/live/saveSku?trainLiveSkuId="+id,
				success:function(date){
					if(date=="success"){
						top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
						window.location="${ctx}/train/live/liveSkuForm?auditId=${trainLiveSku.auditId}";
					}
					if(date=="error"){
						top.layer.alert('保存失败!', {icon: 2, title:'提醒'});
						window.location="${ctx}/train/live/liveSkuForm?auditId=${trainLiveSku.auditId}";
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




<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="trainLiveSku" action="${ctx}/train/live/liveSkuform" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					</form:form>
				</div>
				<p></p>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">编号</th>
							<th style="text-align: center;">价格</th>
							<th style="text-align: center;">有效期</th>
							<th style="text-align: center;">类型</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="sku">
							<tr>
								<td>${sku.trainLiveSkuId}</td>
								<td>${sku.price}</td>
								<td>${sku.num}</td>
								<td>
									<c:if test="${sku.type == 1}">直播</c:if>
									<c:if test="${sku.type == 2}">回放</c:if>
								</td>
								<td>
									<shiro:hasPermission name="train:live:editSku">
										<a href="#" onclick="editSku(${sku.trainLiveSkuId})" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>修改</a>
									</shiro:hasPermission>
								</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="20">
								<!-- 分页代码 -->
								<div class="tfoot">
									<table:page page="${page}"></table:page>
								</div>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</div>
</body>
</html>