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
    <title>首页广告图管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<form id="searchForm" action="${ctx}/ec/webAd/list" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="mtmyWebAdId" type="hidden" name="mtmyWebAdId" value="${mtmyWebAd.mtmyWebAdId}">
						<input id="categoryId" type="hidden" name="categoryId" value="${mtmyWebAd.categoryId}">
					</form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="	ec:webAd:add">
	                        		<table:addRow url="${ctx}/ec/webAd/form?categoryId=${mtmyWebAd.categoryId}" title="首页广告图" width="700px" height="550px"></table:addRow>
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
                			<th style="text-align: center;">位置</th>
                			<th style="text-align: center;">方向</th>
                			<th style="text-align: center;">原始图</th>
                			<th style="text-align: center;">头图</th>
                			<th width="130" style="text-align: center;">是否显示</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="mtmyWebAd">
						<tr>
							<td style="text-align: center;">${mtmyWebAd.mtmyWebAdId}</td>
							<td style="text-align: center;">${mtmyWebAd.name}</td>
							<td style="text-align: center;">
								<c:if test="${mtmyWebAd.positionType == '1'}">首页</c:if>
								<c:if test="${mtmyWebAd.positionType == '2'}">商城</c:if>
								<c:if test="${mtmyWebAd.positionType == '3'}">生活美容</c:if>							
							</td>
							<td style="text-align: center;">
								<c:if test="${mtmyWebAd.align == '0'}">居中</c:if>
								<c:if test="${mtmyWebAd.align == '1'}">左边</c:if>
								<c:if test="${mtmyWebAd.align == '2'}">右上</c:if>
								<c:if test="${mtmyWebAd.align == '3'}">右下</c:if>								
							</td>
							<td style="text-align: center;" class="imgUrl" ><img alt="" src="${ctxStatic}/images/lazylode.png"  data-src="${mtmyWebAd.suboriginalImg}" style="width: 150px;height: 100px;border:1px solid black; "></td>
							<td style="text-align: center;" class="imgUrl" ><img alt="" src="${ctxStatic}/images/lazylode.png"  data-src="${mtmyWebAd.headImg}" style="width: 150px;height: 100px;border:1px solid black; "></td>
							<td style="text-align: center;" id="${mtmyWebAd.mtmyWebAdId}">
								<c:if test="${mtmyWebAd.isShow == '1'}">
									<a href="${ctx}/ec/webAd/updateIsShow?mtmyWebAdId=${mtmyWebAd.mtmyWebAdId}&isShow=0" >
										<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" >
									</a>	
								</c:if>
								<c:if test="${mtmyWebAd.isShow eq '0'}">
									<a href="${ctx}/ec/webAd/updateIsShow?mtmyWebAdId=${mtmyWebAd.mtmyWebAdId}&isShow=1" >
										<img width="20" height="20" src="${ctxStatic}/ec/images/open.png">
									</a>
								</c:if>
							</td>
							<td style="text-align: center;">
								<shiro:hasPermission name="ec:webAd:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/ec/webAd/form?mtmyWebAdId=${mtmyWebAd.mtmyWebAdId}','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:webAd:del">
									<a href="${ctx}/ec/webAd/del?mtmyWebAdId=${mtmyWebAd.mtmyWebAdId}&categoryId=${mtmyWebAd.categoryId}" onclick="return confirmx('确认要删除吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:webAd:addGoods">
									<a href="#" onclick="openDialogView('增加商品', '${ctx}/ec/webAd/mtmyWebAdGoodsList?mtmyWebAdId=${mtmyWebAd.mtmyWebAdId}','700px', '650px')" class="btn btn-info btn-xs"><i class="fa fa-edit"></i> 增加商品</a>
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