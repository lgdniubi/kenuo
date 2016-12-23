<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单详情</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<script type="text/javascript">
	 function lookAddress(num){
		 	$(".loading").show();
	    	$.ajax({
	    		type : 'post',
	    		url : '${ctx}/ec/mtmyuser/lookAddress?orderid='+num,
	    		dateType: 'text',
	    		success:function(date){
	    			$("#address").val(date.address);
	    			$("#consignee").val(date.consignee);
	    			$("#phone").val(date.phone);
	    			$("#mobile").val(date.mobile);
	    			$('#userAddress').modal("show");
	    			$(".loading").hide();
	    		}
	    	})
	    }
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>订单详情</h5>
			</div>
    		<sys:message content="${message}"/>
    		<div class="ibox-content">
				<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th>订单号</th>
							<th>商品名称</th>
							<th>订单类型</th> 
							<th>价格</th>
							<th>数量</th>
							<th>规格</th>
							<!-- <th>条形码</th> -->
							<th>订单类型</th>
							<th>收货地址</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${orderlist}" var="ordergoods">
						<tr>
							<td>${ordergoods.orderid}</td>
							<td>${ordergoods.goodsname}</td>
							<td>
								<c:if test="${ordergoods.isreal==0}">
									实物
								</c:if>
								<c:if test="${ordergoods.isreal==1}">
									虚拟
								</c:if>
							</td>
							<td>${ordergoods.goodsprice}</td>
							<td>${ordergoods.goodsnum}</td>
							<td>${ordergoods.speckeyname}</td>
							<%-- <td>${ordergoods.barcode}</td> --%>
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
							<td>
								<c:if test="${ordergoods.isreal==0}">
									<shiro:hasPermission name="ec:mtmyuser:lookAddress">
										<a href="#" class="infos" onclick="lookAddress('${ordergoods.orderid}')">查看</a>
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
	<!--订单收货详细信息-->
	<div class="modal fade bs-example-modal-lg in" id="userAddress" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" style="display: none; padding-right: 17px;">
   		<form id="mobileFrom" class="modal-dialog modal-lg">
     			<div class="modal-content">
       			<div class="modal-header">
       				<span>订单详细信息</span>
         			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
       			</div>
       			<div class="modal-body">
       				<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
					   <tbody>
							<tr>
								<td colspan="4" class="active"><label class="pull-left">基本信息</label></td>
							</tr>
							<!-- <tr>
								<td  class="width-15 active"><label class="pull-right">所在地区:</label></td>
					         	<td class="width-35" colspan="3">
					         		<input value="" class="form-control">
					         	</td>
							</tr> -->
							<tr>
						      	 <td  class="width-15 active"><label class="pull-right">收货地址:</label></td>
						         <td class="width-35">
						         	<textarea id="address" rows="3" cols="30" readonly="readonly"></textarea>
						         </td>
								<td class="width-15 active"><label class="pull-right">收货人姓名:</label></td>
					         	<td class="width-35">
					         		<input id="consignee" value="" class="form-control" readonly="readonly">
					         	</td>
						    </tr>
							<tr>
								<td  class="width-15 active"><label class="pull-right">手机号码:</label></td>
					         	<td class="width-35">
					         		<input id="phone" value="" class="form-control" readonly="readonly">
					         	</td>
					         	<td  class="width-15 active"><label class="pull-right">固定电话:</label></td>
					         	<td class="width-35">
					         		<input id="mobile" value="" class="form-control" readonly="readonly">
					         	</td>
							</tr>
							<!--
							<tr>
					         	 <td  class="width-15 active"><label class="pull-right">邮编:</label></td>
					         	<td class="width-35">
					         		<input value="" class="form-control">
					         	</td>
							</tr>
							<tr>
								<td  class="width-15 active"><label class="pull-right">电子邮件:</label></td>
					         	<td class="width-35">
					         		<input value="" class="form-control">
					         	</td>
							</tr> -->
				       </tbody>
				    </table>
       			</div>
       			<div class="modal-footer">
					<a href="#" class="btn btn-primary" data-dismiss="modal">关   闭</a>
       			</div>
     			</div>
   		</form>
	</div>
	<div class="loading"></div>
</body>
</html>