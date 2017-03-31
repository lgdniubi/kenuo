<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>会员管理</title>
<meta name="decorator" content="default" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css"
	type="text/css" rel="stylesheet">

<style type="text/css">
.modal-content {
	margin: 0 auto;
	width: 400px;
}
</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<tr>
							<th style="text-align: center;" class="col-md-1">红包名称</th>
							<th style="text-align: center;" class="col-md-1">红包金额</th>
							<th style="text-align: center;" class="col-md-1">到期时间</th>
							<th style="text-align: center;" class="col-md-1">红包类型</th>
							<th style="text-align: center;" class="col-md-1">使用类型</th>
						</tr>
						<c:forEach items="${coupon}" var="coupon">
							<tr style="text-align: center;">
								<td class="col-md-1">
									<p>${coupon.couponName}</p>
								</td>
								<td class="col-md-1">
									<p>${coupon.couponMoney}</p>
								</td>
								<td class="col-md-1">
									<p>
										<fmt:formatDate value="${coupon.expirationDate}" pattern="yyyy-MM-dd  HH:mm" />
									</p>
								</td>
								<td class="col-md-1">
								
								
								
									<p>${coupon.couponType}</p>
								
								
								</td>
								<td class="col-md-1">
									<p>${coupon.usedType}</p>
								</td>
							</tr>
						</c:forEach>
					</table>
			</div>
		</div>
	</div>
</body>
</html>