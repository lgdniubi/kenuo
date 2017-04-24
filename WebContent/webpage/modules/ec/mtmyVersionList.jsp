<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
   	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
    <link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
    <script>
	    function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	  	//重置表单
		function resetnew(){
			reset();
		}
    </script>
    <title>妃子校版本管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>版本管理管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<form:form id="searchForm" modelAttribute="mtmyVersion" action="${ctx}/ec/version/list" method="post" class="form-inline">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							<form:input path="versionCode" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="版本号"/>&nbsp;&nbsp;&nbsp;&nbsp;
		                	<label>类型：</label>
							<form:select path="type"  class="form-control" style="width:185px;">
									<form:option value="">全部</form:option>
									<form:option value="0">兼容版本</form:option>
									<form:option value="1">审核版本</form:option>
							</form:select>
		                	<label>客户端：</label>
							<form:select path="client"  class="form-control" style="width:185px;">
								<form:option value="">全部</form:option>
								<form:option value="wap">wap</form:option>
								<form:option value="ios">ios</form:option>
								<form:option value="android">android</form:option>
							</form:select>
		                	<label>状态：</label>
							<form:select path="Flag"  class="form-control" style="width:185px;">
								<form:option value="">全部</form:option>
								<form:option value="0">开启</form:option>
								<form:option value="1">关闭</form:option>
							</form:select>
							&nbsp;&nbsp;
							<shiro:hasPermission name="ec:version:list">
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
									<i class="fa fa-search"></i> 搜索
								</button>
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="resetnew()" >
									<i class="fa fa-refresh"></i> 重置
								</button>
							</shiro:hasPermission>
						</div>
					</form:form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="ec:version:add">
	                        		<table:addRow url="${ctx}/ec/version/form" title="添加" width="600px" height="550px"></table:addRow>
	                        	</shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;">ID</th>
                			<th style="text-align: center;">版本号</th>
                			<th style="text-align: center;">类型</th>
                			<th style="text-align: center;">客户端</th>
                			<th style="text-align: center;">备注</th>
                			<th style="text-align: center;">添加时间</th>
                			<th style="text-align: center;">状态</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="version">
						<tr>
							<td style="text-align: center;">${version.id}</td>
							<td style="text-align: center;">${version.versionCode}</td>
							<td style="text-align: center;">
									<c:if test="${version.type == 1}">
										审核版本
									</c:if>
									<c:if test="${version.type == 0}">
										兼容版本
									</c:if>
							</td>
							<td style="text-align: center;">${version.client}</td>
							<td style="text-align: center;">${version.remark}</td>
							<td style="text-align: center;"><fmt:formatDate value="${version.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td style="text-align: center;">
									<c:if test="${version.flag == 0}">
										开启
									</c:if>
									<c:if test="${version.flag == 1}">
										关闭
									</c:if>
							</td>
							<td style="text-align: center;">
								<shiro:hasPermission name="ec:version:view">
									<a href="#" onclick="openDialogView('查看', '${ctx}/ec/version/form?id=${version.id}','600px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:version:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/ec/version/form?id=${version.id}','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
							</td>
						</tr>
						</c:forEach>
                    </tbody>
                    <tfoot>
 						<tr>
                            <td colspan="20">
                                <!-- 分页代码 --> 
                                <div class="tfoot">
                                </div>
                               	<table:page page="${page}"></table:page>
                            </td>	
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
   </div>
   <div class="loading"></div>
</body>
</html>