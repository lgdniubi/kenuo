<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <script> 
    </script>
    <title>协议列表</title>
</head>
<body>
	<div class="wrapper-content">
	<sys:message content="${message}" />
        <div class="ibox">
            <div class="ibox-title">
                <h5>协议列表</h5>
            </div>
<%--             <sys:message content="${message}"/> --%>
            <div class="ibox-content">
                <div class="" id="reviewlists">
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">ID</th>
								<th style="text-align: center;">协议名称</th>
							    <th style="text-align: center;">状态</th>
							    <th style="text-align: center;">是否重新签订</th>
							    <th style="text-align: center;">创建时间</th>
							    <th style="text-align: center;">操作人</th>
						    	<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${modelList}" var="model">
								<tr>
									<td>${model.id }</td>
								  	<td>${model.name }</td>
								  	<td>
									  	<c:if test="${model.status ==1}">启用</c:if>
									  	<c:if test="${model.status ==2}">停用</c:if>
									  	<c:if test="${model.status ==3}">变更</c:if>
								  	</td>
								  	<td>
								  	<c:if test="${model.pid eq '0'}">否</c:if>
								  	<c:if test="${model.pid ne '0'}">是</c:if>
								  	</td>
								  	<td><fmt:formatDate value="${model.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								  	<td>${model.createBy.name}</td>
								    <td>
								    	<c:if test="${model.status !=3}">
										<a href="#" onclick="openDialog('修改', '${ctx}/train/protocolModel/modelForm?id=${model.id}','850px', '700px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i>修改</a> 
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