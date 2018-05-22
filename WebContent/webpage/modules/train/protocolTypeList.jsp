<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <script> 
    </script>
    <title>协议类型列表</title>
</head>
<body>
	<div class="wrapper-content">
	<sys:message content="${message}" />
        <div class="ibox">
            <div class="ibox-title">
                <h5>协议类型列表</h5>
            </div>
<%--             <sys:message content="${message}"/> --%>
            <div class="ibox-content">
                <div class="" id="reviewlists">
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">ID</th>
								<th style="text-align: center;">协议类型</th>
							    <th style="text-align: center;">状态</th>
						    	<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${typeList}" var="type">
								<tr>
									<td>${type.id }</td>
								  	<td>${type.name }</td>
								  	<td>
									  	<c:if test="${type.status ==1}">启用</c:if>
									  	<c:if test="${type.status ==2}">停用</c:if>
								  	</td>
								    <td>
				    					<a href="#" onclick="openDialogView('协议列表', '${ctx}/train/protocolModel/list?type=${type.id}','800px', '650px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>查看列表</a>
				    					<a href="#" onclick="openDialog('添加', '${ctx}/train/protocolModel/modelForm?type=${type.id}','850px', '700px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i>添加</a>
				    					<c:if test="${type.status ==2}">
				    					<a href="${ctx}/train/protocolModel/statusType?status=1&type=${type.id}" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>启用</a>
				    					</c:if>
										<c:if test="${type.status ==1}">
										<a href="${ctx}/train/protocolModel/statusType?status=2&type=${type.id}" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i>停用</a>
										</c:if>
								    </td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
	        </div>
	    </div>
    </div>
    <div class="loading"></div>
</body>
</html>