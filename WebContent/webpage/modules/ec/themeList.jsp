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
    <title>热门主题管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<form id="searchForm" action="${ctx}/ec/theme/list" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					</form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="ec:theme:add">
	                        		<table:addRow url="${ctx}/ec/theme/form" title="热门主题" width="700px" height="550px"></table:addRow>
	                        	</shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;">ID</th>
                			<th style="text-align: center;">主题名称</th>
                			<th style="text-align: center;">主标题</th>
                			<th style="text-align: center;">副标题</th>
							<th style="text-align: center;">创建时间</th>
                			<th style="text-align: center;">是否显示</th>
                			<th style="text-align: center;">是否推荐</th>
                			<th style="text-align: center;">排序</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="theme">
						<tr>
							<td style="text-align: center;">${theme.themeId}</td>
							<td style="text-align: center;">${theme.name}</td>
							<td style="text-align: center;">${theme.primaryName}</td>
							<td style="text-align: center;">${theme.deputyName}</td>
							<td style="text-align: center;">
								<fmt:formatDate value="${theme.createDate}"  pattern="yyyy-MM-dd HH:mm:ss" />
							</td>	
							<td style="text-align: center;" id="${theme.themeId}">
								<shiro:hasPermission name="ec:theme:isShow">
									<c:if test="${theme.isShow == '0'}">
										<a href="${ctx}/ec/theme/updateType?themeId=${theme.themeId}&isShow=1&flag=isShow" >
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" >
										</a>	
									</c:if>
									<c:if test="${theme.isShow eq '1'}">
										<a href="${ctx}/ec/theme/updateType?themeId=${theme.themeId}&isShow=0&flag=isShow" >
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png">
										</a>
									</c:if>
								</shiro:hasPermission>
							</td>
							<td style="text-align: center;" id="${theme.themeId}">
								<shiro:hasPermission name="ec:theme:isRecommend">
									<c:if test="${theme.isRecommend == '0'}">
										<a href="${ctx}/ec/theme/updateType?themeId=${theme.themeId}&isRecommend=1&flag=isRecommend" >
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" >
										</a>	
									</c:if>
									<c:if test="${theme.isRecommend eq '1'}">
										<a href="${ctx}/ec/theme/updateType?themeId=${theme.themeId}&isRecommend=0&flag=isRecommend" >
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png">
										</a>
									</c:if>
								</shiro:hasPermission>
							</td>
							<td style="text-align: center;">${theme.sort}</td>
							<td style="text-align: center;">
								<shiro:hasPermission name="ec:theme:view">
									<a href="#" onclick="openDialogView('查看', '${ctx}/ec/theme/form?themeId=${theme.themeId}','600px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:theme:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/ec/theme/form?themeId=${theme.themeId}','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:theme:del">
									<a href="${ctx}/ec/theme/del?themeId=${theme.themeId}" onclick="return confirmx('确认要删除吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:theme:addGoods">
									<a href="#" onclick="openDialogView('添加商品', '${ctx}/ec/theme/themeGoodsList?themeId=${theme.themeId}','700px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-edit"></i>添加商品</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:theme:addArticles">
									<a href="#" onclick="openDialogView('添加文章', '${ctx}/ec/theme/themeArticlesList?themeId=${theme.themeId}','700px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-edit"></i>添加文章</a>
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