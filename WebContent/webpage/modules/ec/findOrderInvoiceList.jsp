<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>订单发票选择</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<script type="text/javascript">
	 	$(function(){
	        // tab
	        $('#tab-navs button').click(function(){
	           var $this = $(this);
	           $this.addClass('active').siblings().removeClass('active');
	           $('#tab-content .tab-inner').eq($this.index()).removeClass('hidden').siblings().addClass('hidden');
	        });
	    });
	</script>
	<style>
		#ichecks{display: inline-block;width: 17px;height: 17px;background: rgb(255, 255, 255);vertical-align: middle;margin-top: -1px;margin-right: 2px;}
		.comment_areas{padding:10px;background:none;border-bottom:1px solid #c6c6c6;}
		.comment_areas{margin-bottom:10px;}
		.comment_areas .user_photo {width:70px;height:70px;}
		.comment_areas .user_photo img{width:100%;height:100%;border-radius:0;}
		.comment_areas .usersname,.comment_areas p{height:35px;line-height:35px;}
	</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
			<div class="ibox-content">
				<div class="clearfix">
					<div class="form-group">
			            <ul class="nav nav-tabs">
			                <li class="active"><a href="#tab_InvoiceCan" data-toggle="tab">未开票</a></li>
			                <li><a href="#tab_InvoiceOpen" data-toggle="tab">已开票</a></li>                        
			                <li><a href="#tab_InvoiceOvertime" data-toggle="tab">已过期</a></li>                        
			            </ul>
                	</div>
	                <div class="tab-content">
	                    <div class="tab-pane fade in active" id="tab_InvoiceCan">
							<c:forEach items="${orderInvoiceCan }" var="orderInvoiceCan">
								<c:if test= "${not empty orderInvoiceCan.orderGoodList }">
									<p style="font-weight:bold;"><input type="checkbox" id="ichecks" value="${orderInvoiceCan.orderid }"/>订单号：${orderInvoiceCan.orderid }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;总计:${orderInvoiceCan.goodsprice }</p>
									<div class="comment_areas clearfix">
		                   	  			<c:forEach items="${orderInvoiceCan.orderGoodList }" var="orderGoodsList">
		                   	  				<div style="overflow:hidden;margin-bottom:5px;">
			                   	  				<div class="user_photo"><img src="${orderGoodsList.originalimg }" alt="" class="img-responsive"></div>
			                   	  				<div class="comments_con">
				                   	  				<span class="usersname">${orderGoodsList.goodsname }</span><%-- <span class="times">${orderGoodsList.recid }</span> --%>
				                   	  				<p>￥${orderGoodsList.goodsprice }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;×${orderGoodsList.openNum }</p>
			                   	  				</div>
		                   	  				</div>
		                   	  			</c:forEach>
	                   	  			</div>
								</c:if>
							</c:forEach>
	                    </div>
	                    <div class="tab-pane fade" id="tab_InvoiceOpen">
							<c:forEach items="${ordersInvoiceOpen }" var="ordersInvoiceOpen">
								<c:if test= "${not empty ordersInvoiceOpen.orderGoodList }">
	                   	  			<p style="font-weight:bold;">订单号：${ordersInvoiceOpen.orderid }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;总计:${ordersInvoiceOpen.goodsprice }</p>
									<div class="comment_areas clearfix">
		                   	  			<c:forEach items="${ordersInvoiceOpen.orderGoodList }" var="orderGoodsList">
		                   	  				<div style="overflow:hidden;margin-bottom:5px;">
			                   	  				<div class="user_photo"><img src="${orderGoodsList.originalimg }" alt="" class="img-responsive"></div>
			                   	  				<div class="comments_con">
				                   	  				<span class="usersname">${orderGoodsList.goodsname }</span><span class="times">${orderGoodsList.recid }</span>
				                   	  				<p>￥${orderGoodsList.goodsprice }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;×${orderGoodsList.openNum }</p>
			                   	  				</div>
		                   	  				</div>
		                   	  			</c:forEach>
	                   	  			</div>
                   	  			</c:if>
							</c:forEach>
	                    </div>
	                    <div class="tab-pane fade" id="tab_InvoiceOvertime">
							<c:forEach items="${orderInvoiceOvertime }" var="orderInvoiceOvertime">
								<c:if test= "${not empty orderInvoiceOvertime.orderGoodList }">
	                   	  			<p style="font-weight:bold;">订单号：${orderInvoiceOvertime.orderid }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;总计:${orderInvoiceOvertime.goodsprice }</p>
									<div class="comment_areas clearfix">
		                   	  			<c:forEach items="${orderInvoiceOvertime.orderGoodList }" var="orderGoodsList">
		                   	  				<div style="overflow:hidden;margin-bottom:5px;">
			                   	  				<div class="user_photo"><img src="${orderGoodsList.originalimg }" alt="" class="img-responsive"></div>
			                   	  				<div class="comments_con">
				                   	  				<span class="usersname">${orderGoodsList.goodsname }</span><span class="times">${orderGoodsList.recid }</span>
				                   	  				<p>￥${orderGoodsList.goodsprice }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;×${orderGoodsList.openNum }</p>
			                   	  				</div>
		                   	  				</div>
		                   	  			</c:forEach>
	                   	  			</div>
                   	  			</c:if>
							</c:forEach>
	                    </div>
                     </div>
				</div>
			</div>	
		</div>
	</div>	
    <div class="loading"></div>
</body>
</html>