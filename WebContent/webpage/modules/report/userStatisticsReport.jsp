<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="decorator" content="default" />
<title>用户统计</title>
</head>
<body>
	<div class="wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>用户统计</h5>
			</div>
			<sys:message content="${message}" />
			<div class="ibox-content">
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr><td colspan="3">会员购买率表</td></tr>
						<tr>
							 <th style="text-align: center;">注册用户数</th>
						     <th style="text-align: center;">有订单用户数</th>
						     <th style="text-align: center;">会员购买率</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<tr>
							<td>${userStatisticsReport.userNum }</td>
						  	<td>${userStatisticsReport.orderUserNum }</td>
						  	<td>${userStatisticsReport.buyRate }</td>
						</tr>
					</tbody>
				</table>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr><td colspan="3">会员平均订单数及购物额</td></tr>
						<tr>
							 <th style="text-align: center;">会员购买总额</th>
						     <th style="text-align: center;">每会员订单数</th>
						     <th style="text-align: center;">每会员购物额</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<tr>
							<td>￥${userStatisticsReport.amount }</td>
						  	<td>${userStatisticsReport.orderNum }</td>
						  	<td>￥${userStatisticsReport.userBuyRate }</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>