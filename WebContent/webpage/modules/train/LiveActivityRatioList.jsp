<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
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
    <title>兑换活动管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>兑换活动管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<form id="searchForm" action="${ctx}/train/ratio/list" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="trainLiveActivityRatioId" type="hidden" name="trainLiveActivityRatioId" value="${trainLiveActivityRatio.trainLiveActivityRatioId}">
					</form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="train:ratio:add">
	                        		<table:addRow url="${ctx}/train/ratio/form" title="兑换活动" width="700px" height="550px"></table:addRow>
	                        	</shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;">ID</th>
                			<th style="text-align: center;">名称</th>
                			<th style="text-align: center;">状态</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="trainLiveActivityRatio">
						<tr>
							<td style="text-align: center;">${trainLiveActivityRatio.trainLiveActivityRatioId}</td>
							<td style="text-align: center;">${trainLiveActivityRatio.name}</td>
							<td style="text-align: center;">
								<c:if test="${trainLiveActivityRatio.isShow == '1'}">
									<a href="${ctx}/train/ratio/updateIsShow?trainLiveActivityRatioId=${trainLiveActivityRatio.trainLiveActivityRatioId}&flag=0" >
										<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" >
									</a>	
								</c:if>
								<c:if test="${trainLiveActivityRatio.isShow == '0'}">
									<a href="${ctx}/train/ratio/updateIsShow?trainLiveActivityRatioId=${trainLiveActivityRatio.trainLiveActivityRatioId}&flag=1" >
										<img width="20" height="20" src="${ctxStatic}/ec/images/open.png">
									</a>
								</c:if>
							</td>
							<td style="text-align: center;">
								<shiro:hasPermission name="train:ratio:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/train/ratio/form?trainLiveActivityRatioId=${trainLiveActivityRatio.trainLiveActivityRatioId}','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="train:ratio:del">
									<a href="${ctx}/train/ratio/del?trainLiveActivityRatioId=${trainLiveActivityRatio.trainLiveActivityRatioId}" onclick="return confirmx('确认要删除吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="train:ratio:addExchangeRatio">
									<a href="#" onclick="openDialogView('增加兑换比例', '${ctx}/train/ratio/exchangeRatioList?trainLiveActivityRatioId=${trainLiveActivityRatio.trainLiveActivityRatioId}','700px', '650px')" class="btn btn-info btn-xs"><i class="fa fa-edit"></i>增加兑换比例</a>
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