<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
     <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
    <title>协议列表</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>协议列表</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <table id="treeTable" class="table table-bordered table-hover table-striped">
	                 <thead> 
	                   	 <tr>
                   			<th style="text-align: center;">协议名称</th>
                   			<th style="text-align: center;">类型</th>
                   			<th style="text-align: center;">签约机构</th>
                   			<th style="text-align: center;">签约人</th>
                   			<th style="text-align: center;">状态</th>
                   			
                   		</tr>
	                 </thead>
                     <tbody>
                       	<c:forEach items="${protocalUser}" var="state">
							<tr style="text-align: center;">
                                <td style="text-align: center;">
                               		${state.protocolName}
                               	</td>
                               	<td style="text-align: center;">
                               		<c:if test="${state.typeId eq 1}">APP注册</c:if>
                               		<c:if test="${state.typeId eq 2}">手艺人认证</c:if>
                               		<c:if test="${state.typeId eq 3}">企业认证</c:if>
                               		<c:if test="${state.typeId eq 4}">报货</c:if>
                               	</td>
                               	<td style="text-align: center;">
                               		${state.officeName }
                               	</td>
                               	<td style="text-align: center;">
                               		${state.createBy.name}
                               	</td>
                               	<td style="text-align: center;">
                               		<c:if test="${state.status eq '1' }">履约中</c:if>
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