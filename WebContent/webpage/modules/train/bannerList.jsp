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
		function updateType(bannerId,flag){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/trains/banner/updateType?adId="+bannerId+"&flag="+flag,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					var FLAG = data.FLAG;
					if("OK" == status){
						$("#"+bannerId).html("");//清除DIV内容	
						if(FLAG == '0'){
							$("#"+bannerId).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"updateType('"+bannerId+"','1')\">");
						}else if(FLAG == '1'){
							$("#"+bannerId).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"updateType('"+bannerId+"','0')\">");
						}
					}else if("ERROR" == status){
						alert(data.MESSAGE);
					}
				}
			}); 
		}
    </script>
    <title>banner图管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>banner图管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<form id="searchForm" action="${ctx}/trains/banner/list" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					</form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="trains:banner:add">
	                        		<table:addRow url="${ctx}/trains/banner/form" title="banner图" width="600px" height="550px"></table:addRow>
	                        	</shiro:hasPermission>
	                        </div>
						</div>
					</div>
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
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="trainsBanner">
						<tr>
							<td style="text-align: center;">${trainsBanner.adId}</td>
							<td style="text-align: center;">${trainsBanner.adName}</td>
							<td style="text-align: center;" class="imgUrl"><img alt="" src="${ctxStatic}/images/lazylode.png" data-src="${trainsBanner.adPic}" style="width: 150px;height: 100px;"></td>
							<td style="text-align: center;">
								<%-- <c:if test="${trainsBanner.adType == 0}">
									首页
								</c:if>
								<c:if test="${trainsBanner.adType == 1}">
									提问
								</c:if> --%>
								${trainsBanner.dict.label }
							</td>
							<td style="text-align: center;" id="${trainsBanner.adId}">
								<shiro:hasPermission name="trains:banner:update">
									<c:if test="${trainsBanner.delFlag == '1'}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="updateType('${trainsBanner.adId}','0')">
									</c:if>
									<c:if test="${trainsBanner.delFlag eq '0'}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="updateType('${trainsBanner.adId}','1')">
									</c:if>
								</shiro:hasPermission>
							</td>
							<td style="text-align: center;">${trainsBanner.sort}</td>
							<td style="text-align: center;">
								<shiro:hasPermission name="trains:banner:view">
									<a href="#" onclick="openDialogView('查看', '${ctx}/trains/banner/form?adId=${trainsBanner.adId}','600px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="trains:banner:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/trains/banner/form?adId=${trainsBanner.adId}','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
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