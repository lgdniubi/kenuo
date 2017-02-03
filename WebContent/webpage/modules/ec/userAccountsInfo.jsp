<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>用户账户详情</title>
	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<tbody style="text-align: center;">
						<tr>
						     <td style="text-align: center;">余额</td>
						     <td style="text-align: center;">欠款</td>
						</tr>
						<tr>
							<td>${usersAccounts.accountBalance}</td>
							<td>${usersAccounts.accountArrearage}</td>	
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>