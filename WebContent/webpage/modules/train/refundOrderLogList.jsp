<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
     <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
    <title>账单操作日志</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>日志列表</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <table id="treeTable" class="table table-bordered table-hover table-striped">
	                 <thead> 
	                   	 <tr>
                   			<th style="text-align: center;">订单ID</th>
                   			<th style="text-align: center;">操作描述</th>
                   			<th style="text-align: center;">操作人</th>
                   			<th style="text-align: center;">操作时间</th>
                   		</tr>
	                 </thead>
                     <tbody>
                       	<c:forEach items="${log}" var="state">
							<tr style="text-align: center;">
                                <td style="text-align: center;">
                               		${state.orderId}
                               	</td>
                               	<td style="text-align: center;">
                               		${state.description }
                               	</td>
                               	<td style="text-align: center;">
                               		${state.createUsername }
                               	</td>
                               	<td style="text-align: center;">
                               		${state.createTime}
                               	</td>
							</tr>
						</c:forEach>
                     </tbody>
                </table>
	        </div>
	    </div>
    </div>
</body>
</html>