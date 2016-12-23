<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单列表</title>
	<meta name="decorator" content="default"/>
</head>

<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
</script>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
				<div class="ibox-content">
				<div class="clearfix">
				 <div class="pull-left">
				 	<form:form id="orderGoodsPrice" modelAttribute="orderGoodsPrice" action="#" method="get" class="form-inline">
						<div class=" pull-right">
							<label >订单编号：${orderGoodsPrice.orderid}</label>
						</div>
					</form:form>
				 </div>
					<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<th>订单类型</th>
								<th>商品属性</th>
								<th>商品名称</th>
								<th>规格</th> 
								<th>商品条形码</th>
								<th>商品编码</th>
								<th>价格</th>
								<th>数量</th>
								<th>总次数</th>
								<th>剩余次数</th>
								<th>服务时长</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${orderlist}" var="ordergoods">
							<tr>
								<c:if test="${ordergoods.actiontype==0}">
									<td>普通订单</td>
								</c:if>
								<c:if test="${ordergoods.actiontype==1}">
									<td>限时抢购</td>
								</c:if>
								<c:if test="${ordergoods.actiontype==2}">
									<td>团购</td>
								</c:if>
								<c:if test="${ordergoods.actiontype==3}"> 
									<td>促销优惠</td>
								</c:if>
								<c:if test="${ordergoods.isreal==0}">
									<td>实物</td>
								</c:if>
								<c:if test="${ordergoods.isreal==1}">
									<td>虚拟</td>
								</c:if>
								<td>${ordergoods.goodsname}</td>
								<td>${ordergoods.speckeyname}</td>
								<td>${ordergoods.goodBarCode}</td>
								<td>${ordergoods.goodsNo}</td>
								<td>${ordergoods.goodsprice}</td>
								<td>${ordergoods.goodsnum}</td>
								<td>${ordergoods.servicetimes }</td>
								<td>${ordergoods.remaintimes }</td>
								<td>${ordergoods.servicemin }</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
					<form:form id="orderGoodsPrice" modelAttribute="orderGoodsPrice" action="#" method="get" class="form-inline">
						<div class=" pull-right">
							<label >优惠合计: ￥${orderGoodsPrice.youprice} 元</label>
							<br/>
							 <label>成交价格: ￥${orderGoodsPrice.totlprice}元 </label>
						</div>
					</form:form>
				</div>
				
				
						
				
			</div>
		</div>
	</div>
	
</body>
</html>