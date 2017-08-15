<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>套卡商品充值记录</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
	                <c:if test="${orderType != 'order' }">
		                <label class="active">套卡商品充值记录:</label>
		                <table id="contentTable" 
		                	class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<th style="text-align: center;">充值日期</th>
									<th style="text-align: center;">实际付款</th>
									<th style="text-align: center;">欠款</th>
									<th style="text-align: center;">未使用充值金额</th>
									<th style="text-align: center;">操作人</th>
								</tr>
								<c:forEach items="${orderGoodsDetails }" var="orderGoodsDetail">
									<tr>
										<td align="center"><fmt:formatDate value="${orderGoodsDetail.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										<td>${orderGoodsDetail.totalAmount }</td>
										<td>${orderGoodsDetail.orderArrearage }</td>
										<td>${orderGoodsDetail.surplusAmount }</td>
										<td>${orderGoodsDetail.createByName }</td>
									</tr>
								</c:forEach>
						</table>
					</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>