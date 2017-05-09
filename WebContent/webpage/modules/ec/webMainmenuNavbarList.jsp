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
    <!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
    <script>
	    function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		$(function(){
			$(".imgUrl img").each(function(){
				var $this = $(this),
					$src = $this.attr('data-src');  
				$this.attr({'src':$src})
			});
			
		});
		
		
    </script>
    <title>主菜单导航栏管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>主菜单导航栏管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<form id="searchForm" action="${ctx}/ec/webMainmenuNavbar/list" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					</form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="ec:webMainmenuNavbar:add">
	                        		<table:addRow url="${ctx}/ec/webMainmenuNavbar/form" title="导航图" width="700px" height="550px"></table:addRow>
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
                			<th style="text-align: center;">图片</th>
                			<th style="text-align: center;">类型</th>
                			<th style="text-align: center;">创建者</th>
                			<th style="text-align: center;">创建时间</th>
                			<th style="text-align: center;">更新者</th>
                			<th style="text-align: center;">更新时间</th>
                			<th style="text-align: center;">状态</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="webMainmenuNavbar">
						<tr>
							<td style="text-align: center;">${webMainmenuNavbar.webMainmenuNavbarId}</td>
							<td style="text-align: center;">${webMainmenuNavbar.name}</td>
							<td style="text-align: center;" class="imgUrl" ><img alt="" src="${ctxStatic}/images/lazylode.png"  data-src="${webMainmenuNavbar.imgUrl}" style="width: 150px;height: 100px;border:1px solid black; "></td>
							<td style="text-align: center;">
								<c:if test="${webMainmenuNavbar.type==1}">
									首页
								</c:if>
								<c:if test="${webMainmenuNavbar.type==2}">
									商城
								</c:if>		
								<c:if test="${webMainmenuNavbar.type==3}">
									生活美容
								</c:if>
							</td>
							<td style="text-align: center;">${webMainmenuNavbar.createBy.id}</td>
							<td style="text-align: center;">
								<fmt:formatDate value="${webMainmenuNavbar.createDate}"  pattern="yyyy-MM-dd HH:mm:ss" />
							</td>	
							<td style="text-align: center;">${webMainmenuNavbar.updateBy.id}</td>
							<td style="text-align: center;">
								<fmt:formatDate value="${webMainmenuNavbar.updateDate}"  pattern="yyyy-MM-dd HH:mm:ss" />
							</td>	
							<td style="text-align: center;" id="${webMainmenuNavbar.webMainmenuNavbarId}">
								<shiro:hasPermission name="ec:webMainmenuNavbar:update">
									<c:if test="${webMainmenuNavbar.isShou == '1'}">
										<a href="${ctx}/ec/webMainmenuNavbar/updateType?webMainmenuNavbarId=${webMainmenuNavbar.webMainmenuNavbarId}&type=${webMainmenuNavbar.type}&flag=0" >
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" >
										</a>	
									</c:if>
									<c:if test="${webMainmenuNavbar.isShou eq '0'}">
										<a href="${ctx}/ec/webMainmenuNavbar/updateType?webMainmenuNavbarId=${webMainmenuNavbar.webMainmenuNavbarId}&type=${webMainmenuNavbar.type}&flag=1" >
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png">
										</a>
									</c:if>
								</shiro:hasPermission>
							</td>
							<td style="text-align: center;">
								<shiro:hasPermission name="ec:webMainmenuNavbar:view">
									<a href="#" onclick="openDialogView('查看', '${ctx}/ec/webMainmenuNavbar/form?webMainmenuNavbarId=${webMainmenuNavbar.webMainmenuNavbarId}','600px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:webMainmenuNavbar:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/ec/webMainmenuNavbar/form?webMainmenuNavbarId=${webMainmenuNavbar.webMainmenuNavbarId}','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:webMainmenuNavbar:del">
									<a href="${ctx}/ec/webMainmenuNavbar/del?webMainmenuNavbarId=${webMainmenuNavbar.webMainmenuNavbarId}" onclick="return confirmx('确认要删除吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:webMainmenuNavbarContent:view">
									<a href="#" onclick="openDialogView('主菜单导航栏内容图', '${ctx}/ec/webMainmenuNavbarContent/list?webMainmenuNavbarId=${webMainmenuNavbar.webMainmenuNavbarId}','800px', '600px')" class="btn btn-info btn-xs"><i class="fa fa-edit"></i> 主菜单导航栏内容图</a>
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