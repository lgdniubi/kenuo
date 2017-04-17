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
									<c:if test="${coupon.couponType==1}">
										<p>商品详情页</p>
									</c:if>
									<c:if test="${coupon.couponType==2}">
										<p>活动页</p>
									</c:if>
									<c:if test="${coupon.couponType==3}">
										<p>新注册</p>
									</c:if>
									<c:if test="${coupon.couponType==4}">
										<p>通用红包</p>
									</c:if>
								</td>
								<td class="col-md-1">
									<c:if test="${coupon.usedType==1}">
										<p>全部商品</p>
									</c:if>
									<c:if test="${coupon.usedType==2}">
										<p>商品分类</p>
									</c:if>
									<c:if test="${coupon.usedType==3}">
										<p>指定商品</p>
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</table>
			</div>
		</div>
	</div>
</body>
</html>