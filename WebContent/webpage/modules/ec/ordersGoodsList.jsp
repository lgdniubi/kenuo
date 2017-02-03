<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>订单列表</title>
<meta name="decorator" content="default" />
</head>

<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		$(document).ready(function() {
			
	        //外部js调用
	        laydate({
	            elem: '#begtime',   //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus'      //响应事件。如果没有传入event，则按照默认的click
	        });
	        
	        laydate({
	            elem: '#endtime',  //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus'     //响应事件。如果没有传入event，则按照默认的click
	        });

	    })
</script>


<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
					<div class="pull-left">
						<form:form id="orderGoodsPrice" modelAttribute="orderGoodsPrice"
							action="#" method="get" class="form-inline">
							<div class=" pull-right">
								<label>订单编号：${orderGoodsPrice.orderid}</label>
							</div>
						</form:form>
					</div>
					<table id="contentTable"
						class="table table-striped table-bordered  table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<th style="text-align: center;">订单类型</th>
								<th style="text-align: center;">商品属性</th>
								<th style="text-align: center;">商品名称</th>
								<th style="text-align: center;">规格</th>
								<th style="text-align: center;">价格</th>
								<th style="text-align: center;">数量</th>
								<th style="text-align: center;">总次数</th>
								<th style="text-align: center;">剩余次数</th>
								<th style="text-align: center;">服务时长</th>
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
									<td>${ordergoods.goodsprice}</td>
									<td>${ordergoods.goodsnum}</td>
									<td>${ordergoods.servicetimes }</td>
									<td>${ordergoods.remaintimes }</td>
									<td>${ordergoods.servicemin }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<form:form id="orderGoodsPrice" modelAttribute="orderGoodsPrice"
						action="#" method="get" class="form-inline">
						<div class=" pull-left">
							<label>运费价格: ￥${orderGoodsPrice.shippingPrice} 元</label>
						</div>
						<div class=" pull-right">
							<label>优惠合计: ￥${orderGoodsPrice.youprice} 元</label> <br /> <label>成交价格:
								￥${orderGoodsPrice.totlprice}元 </label>
						</div>
					</form:form>
					<div class=" pull-left">
						<hr>
						<h4>使用优惠明细</h4>
						<table id="coupTable"
							class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
							<thead>
								<tr>
									<th style="text-align: center;">活动id</th>
									<th style="text-align: center;">活动名称</th>
									<th style="text-align: center;">红包id</th>
									<th style="text-align: center;">红包名称</th>
									<th style="text-align: center;">红包类型</th>
									<th style="text-align: center;">使用范围</th>
									<th style="text-align: center;">满减金额</th>
									<th style="text-align: center;">红包金额</th>
									<th style="text-align: center;">操作</th>
								</tr>
							</thead>
							<tbody>

								<c:if test="${couplist != null}">
									<c:forEach items="${couplist}" var="couplist">
										<tr>
											<td style="text-align: center;">${couplist.actionId}</td>
											<td style="text-align: center;">${couplist.actionName}</td>
											<td style="text-align: center;">${couplist.counponId}</td>
											<td style="text-align: center;">${couplist.counponName}</td>
											<td style="text-align: center;">
											<c:if test="${couplist.type==1}">
												商品详情页
											</c:if> <c:if test="${couplist.type==2}">
												活动页
											</c:if> <c:if test="${couplist.type==3}">
												新注册
											</c:if> <c:if test="${couplist.type==4}"> 
												手工红包
											</c:if></td>
											<td style="text-align: center;"><c:if
													test="${couplist.usedType==1}">
												全部商品
											</c:if> <c:if test="${couplist.usedType==2}">
												指定分类
											</c:if> <c:if test="${couplist.usedType==3}">
												指定商品
											</c:if></td>
											<td style="text-align: center;">${couplist.baseAmount }</td>
											<td style="text-align: center;">${couplist.couponMoney }</td>
											<td style="text-align: center;"><a href="#"
												onclick="openDialogView('信息列表', '${ctx}/ec/activity/CateGoodsList?id=${couplist.counponId}&usedType=${couplist.usedType}','300px','400px')"
												class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>查看</a>
											</td>
										</tr>
									</c:forEach>
								</c:if>
							</tbody>
						</table>
					</div>

				</div>
			</div>
		</div>
	</div>

</body>
</html>