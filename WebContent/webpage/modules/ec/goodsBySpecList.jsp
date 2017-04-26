<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>商品规格价格列表</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<link rel="stylesheet" href="${ctxStatic}/ec/css/base.css">
	<script type="text/javascript">
		//刷新
		function refresh(){
			window.location="${ctx}/ec/goodsspec/list";
		}
		
		function updateByGoodsSpecPrice(goodsId,specKey){
			top.layer.open({
			    type: 2, 
			    area: ['350px', '550px'],
			    title:"编辑规格",
			    content: "${ctx}/ec/goods/goodsBySpecForm?goodsId="+goodsId+"&specKey="+specKey,
			    btn: ['确定', '关闭'],
			    yes: function(index, layero){
			    	
			        var obj = layero.find("iframe")[0].contentWindow;
					var price = obj.document.getElementById("price");
			    	var marketPrice = obj.document.getElementById("marketPrice");
					var barCode = obj.document.getElementById("barCode");
					var goodsNo = obj.document.getElementById("goodsNo");
					var goodsWeight = obj.document.getElementById("goodsWeight");
					var serviceTimes = obj.document.getElementById("serviceTimes");
					var expiringDate = obj.document.getElementById("expiringDate");
					
					//异步保存红包
					$.ajax({
						type:"post",
						data:{
							goodsId:goodsId,
							specKey:specKey,
							price:$(price).val(),
							marketPrice:$(marketPrice).val(),
							barCode:$(barCode).val(),
							goodsNo:$(goodsNo).val(),
							goodsWeight:$(goodsWeight).val(),
							serviceTimes:$(serviceTimes).val(),
							expiringDate:$(expiringDate).val()
						},
						url:"${ctx}/ec/goods/goodsBySpecSave",
						success:function(date){
							if(date=="success"){
								top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
								window.location="${ctx}/ec/goods/goodsBySpecList?goodsId="+goodsId;
							}
							if(date=="error"){
								top.layer.alert('保存失败!', {icon: 2, title:'提醒'});
								window.location="${ctx}/ec/goods/goodsBySpecList?goodsId="+goodsId;
							}
						},error:function(XMLHttpRequest,textStatus,errorThrown){
							alert();	    
						}
					});
					top.layer.close(index);
				},cancel: function(index){ //或者使用btn2
					//按钮【按钮二】的回调
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
					<h5>商品规格价格</h5>
				</div>
				<div class="ibox-content">
					<div class="searcharea clearfix">
						
					</div>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<th style="text-align: center;">规格ID</th>
							<th style="text-align: center;">规格名称</th>
							<th style="text-align: center;">优惠价</th>
							<th style="text-align: center;">市场价</th>
							<th style="text-align: center;">库存</th>
							<th style="text-align: center;">条形码</th>
							<th style="text-align: center;">规格编码</th>
							<th style="text-align: center;">商品重量（克）</th>
							<th style="text-align: center;">服务次数</th>
							<th style="text-align: center;">截止时间（月）</th>
							<th style="text-align: center;">操作</th>
						</thead>
						<tbody>
							<c:forEach items="${goodsspecpricelist}" var="goodsspecprice">
								<tr style="text-align: center;">
									<td>${goodsspecprice.specKey}</td>
									<td>${goodsspecprice.specKeyValue}</td>
									<td>${goodsspecprice.price}</td>
									<td>${goodsspecprice.marketPrice}</td>
									<td>${goodsspecprice.storeCount}</td>
									<td>${goodsspecprice.barCode}</td>
									<td>${goodsspecprice.goodsNo}</td>
									<td>${goodsspecprice.goodsWeight}</td>
									<td>${goodsspecprice.serviceTimes}</td>
									<td>${goodsspecprice.expiringDate}</td>
									<td style="text-align: left;">
										<shiro:hasPermission name="ec:goods:goodsBySpecForm">
											<a href="#" onclick="updateByGoodsSpecPrice('${goodsspecprice.goodsId}','${goodsspecprice.specKey}')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>修改</a>
										</shiro:hasPermission>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>