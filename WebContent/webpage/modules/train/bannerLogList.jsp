<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

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
    <title>banner图日志列表</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>banner图日志列表</h5>
            </div>
            <div class="ibox-content">
                <div class=" clearfix">
                	<form id="searchForm" modelAttribute="trainsBanner" action="${ctx}/trains/banner/bannerLoglist" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					</form>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;">ID</th>
                			<th style="text-align: center;">banner图名称</th>
                			<th style="text-align: center;">banner图</th>
                			<th style="text-align: center;">类型</th>
                			<th style="text-align: center;">状态</th>
                			<th style="text-align: center;">排序</th>
                			<th style="text-align: center;">备注</th>
                			<th style="text-align: center;">操作人</th>
                			<th style="text-align: center;">操作时间</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="trainsBanner">
						<tr>
							<td style="text-align: center;">${trainsBanner.adId}</td>
							<td style="text-align: center;">${trainsBanner.adName}</td>
							<td style="text-align: center;" class="imgUrl"><img alt="" src="${ctxStatic}/images/lazylode.png" data-src="${trainsBanner.adPic}" style="width: 150px;height: 100px;"></td>
							<td style="text-align: center;">
								<c:choose>
									<c:when test="${trainsBanner.adType == 0}">首页</c:when>
									<c:when test="${trainsBanner.adType == 1}">动态</c:when>
									<c:when test="${trainsBanner.adType == 2}">工作台</c:when>
								</c:choose>
							</td>
							<td style="text-align: center;" id="${trainsBanner.adId}">
								<c:if test="${trainsBanner.delFlag == '1'}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" >
								</c:if>
								<c:if test="${trainsBanner.delFlag eq '0'}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" >
								</c:if>
							</td>
							<td style="text-align: center;">${trainsBanner.sort}</td>
							<td style="text-align: center;">${trainsBanner.remark}</td>
							<td style="text-align: center;">${trainsBanner.createBy.name}</td>
							<td style="text-align: center;"><fmt:formatDate value="${trainsBanner.createDate}" pattern="yyyy-MM-dd"/></td>
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