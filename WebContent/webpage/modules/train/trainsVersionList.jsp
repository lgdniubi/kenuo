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
                	<form id="searchForm" action="${ctx}/trains/version/list" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					</form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="trains:version:add">
	                        		<table:addRow url="${ctx}/trains/version/form" title="添加" width="600px" height="550px"></table:addRow>
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
							<th style="text-align: center;">${version.client}</th>
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
								<shiro:hasPermission name="trains:version:view">
									<a href="#" onclick="openDialogView('查看', '${ctx}/trains/version/form?id=${version.id}','600px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="trains:version:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/trains/version/form?id=${version.id}','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
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