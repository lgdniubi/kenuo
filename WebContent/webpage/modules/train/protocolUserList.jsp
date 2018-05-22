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
		<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="protocol"
							action="${ctx}/sys/user/list" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span>所属商家：</span>
								<sys:treeselect id="company" name="company.id" value="${protocol.franchiseeId}" labelName="company.name" 
									labelValue="${protocol.companyName}" title="公司" 
									url="/sys/franchisee/treeData" cssClass=" form-control input-sm" allowClear="true" />
								<span>所属机构：</span>
								<sys:treeselect id="office" name="office.id" value="${protocol.officeId}" labelName="office.name" labelValue="${protocol.officeName}" title="部门"
									url="/sys/office/treeData?type=2" cssClass=" form-control input-sm" allowClear="true"
									notAllowSelectRoot="false" notAllowSelectParent="false" />
								<span>签订人：</span>
								<form:input path="createBy.name" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
								<span>状态：</span>
								<form:input path="status" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
							</div>
						</form:form>
						<br />
					</div>
				</div>
        <div class="ibox">
            <div class="ibox-title">
                <h5>协议列表</h5>
            </div>
            <div class="ibox-content">
                <div class="" id="reviewlists">
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">ID</th>
								<th style="text-align: center;">所属商家</th>
							    <th style="text-align: center;">所属机构</th>
							    <th style="text-align: center;">协议名称</th>
							    <th style="text-align: center;">状态</th>
							    <th style="text-align: center;">签订人</th>
							    <th style="text-align: center;">有效期</th>
							    <th style="text-align: center;">签订时间</th>
						    	<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${protocolList}" var="protocol">
								<tr>
									<td>${protocol.id}</td>
								  	<td>${protocol.companyName}</td>
								  	<td>${protocol.officeName}</td>
								  	<td>${protocol.protocolName}</td>
								  	<td>
									  	<c:if test="${protocol.status ==1}">启用</c:if>
									  	<c:if test="${protocol.status ==2}">停用</c:if>
								  	</td>
								  	<td>${protocol.createBy.name}</td>
								  	<td>
								  	<fmt:formatDate value="${protocol.authStartDate}" pattern="yyyy-MM-dd"/>--
								  	<fmt:formatDate value="${protocol.authEndDate}" pattern="yyyy-MM-dd"/>
								  	</td>
								  	<td><fmt:formatDate value="${protocol.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								    <td>
								    	<c:if test="${protocol.status ==2}">
				    					<a href="${ctx}/train/protocolModel/status?status=1&id=${protocol.id}&type=${protocol.type}" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>启用</a>
				    					</c:if>
										<c:if test="${protocol.status ==1}">
										<a href="${ctx}/train/protocolModel/status?status=2&id=${protocol.id}&type=${protocol.type}" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i>停用</a>
										</c:if>
										<a href="#" onclick="openDialog('修改', '${ctx}/train/protocolModel/modelForm?id=${protocol.id}','850px', '700px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i>修改</a> 
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