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
    <title>tab_banner图管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>tab_banner图管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<form id="searchForm" action="${ctx}/ec/tab_banner/list" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					</form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="ec:tab_banner:add">
	                        		<table:addRow url="${ctx}/ec/tab_banner/form" title="背景图" width="700px" height="550px"></table:addRow>
	                        	</shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;">ID</th>
                			<th style="text-align: center;">标签组名称</th>
                			<th style="text-align: center;">背景图</th>
                			<th style="text-align: center;">创建者</th>
                			<th style="text-align: center;">创建时间</th>
                			<th style="text-align: center;">更新者</th>
                			<th style="text-align: center;">更新时间</th>
                			<th style="text-align: center;">状态</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="tabBackground">
						<tr>
							<td style="text-align: center;">${tabBackground.tabBackgroundId}</td>
							<td style="text-align: center;">${tabBackground.groupName}</td>
							<td style="text-align: center;" class="imgUrl" ><img alt="" src="${ctxStatic}/images/lazylode.png"  data-src="${tabBackground.tabBg}" style="width: 150px;height: 100px;border:1px solid black; "></td>
							<td style="text-align: center;">${tabBackground.createBy.name}</td>
							<td style="text-align: center;">
								<fmt:formatDate value="${tabBackground.createDate}"  pattern="yyyy-MM-dd HH:mm:ss" />
							</td>	
							<td style="text-align: center;">${tabBackground.updateBy.name}</td>
							<td style="text-align: center;">
								<fmt:formatDate value="${tabBackground.updateDate}"  pattern="yyyy-MM-dd HH:mm:ss" />
							</td>	
							<td style="text-align: center;" id="${tabBackground.tabBackgroundId}">
								<shiro:hasPermission name="ec:tab_banner:update">
									<c:if test="${tabBackground.isShow == '1'}">
										<a href="${ctx}/ec/tab_banner/updateType?tabBackgroundId=${tabBackground.tabBackgroundId}&flag=0" >
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" >
										</a>	
									</c:if>
									<c:if test="${tabBackground.isShow eq '0'}">
										<a href="${ctx}/ec/tab_banner/updateType?tabBackgroundId=${tabBackground.tabBackgroundId}&flag=1" >
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png">
										</a>
									</c:if>
								</shiro:hasPermission>
							</td>
							<td style="text-align: center;">
								<shiro:hasPermission name="ec:tab_banner:view">
									<a href="#" onclick="openDialogView('查看', '${ctx}/ec/tab_banner/togetherForm?tabBackgroundId=${tabBackground.tabBackgroundId}','600px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:tab_banner:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/ec/tab_banner/form?tabBackgroundId=${tabBackground.tabBackgroundId}','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:tab_banner:del">
									<a href="${ctx}/ec/tab_banner/del?tabBackgroundId=${tabBackground.tabBackgroundId}" onclick="return confirmx('确认要删除吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:tab_banner:add">
									<a href="#" onclick="openDialogView('增加tab_banner图', '${ctx}/ec/tab_banner/bannerList?tabBackgroundId=${tabBackground.tabBackgroundId}','700px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-edit"></i> 增加tab_banner图</a>
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