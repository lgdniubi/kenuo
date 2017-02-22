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
    <title>主题图管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>主题图管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<form id="searchForm" action="${ctx}/ec/subject/list" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="subId" type="hidden" name="subId" value="${subject.subId}">
					</form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="	ec:subject:add">
	                        		<table:addRow url="${ctx}/ec/subject/form" title="主题图" width="700px" height="550px"></table:addRow>
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
                			<th style="text-align: center;">原始图</th>
                			<th style="text-align: center;">头图</th>
                			<th style="text-align: center;">创建时间</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="subject">
						<tr>
							<td style="text-align: center;">${subject.subId}</td>
							<td style="text-align: center;">${subject.subName}</td>
							<td style="text-align: center;">
								<c:if test="${subject.position == 0}">左上</c:if>
								<c:if test="${subject.position == 1}">左下</c:if>
								<c:if test="${subject.position == 2}">右边</c:if>							
							</td>
							<td style="text-align: center;" class="imgUrl" ><img alt="" src="${ctxStatic}/images/lazylode.png"  data-src="${subject.subOriginalImg}" style="width: 150px;height: 100px;border:1px solid black; "></td>
							<td style="text-align: center;" class="imgUrl" ><img alt="" src="${ctxStatic}/images/lazylode.png"  data-src="${subject.subHeadImg}" style="width: 150px;height: 100px;border:1px solid black; "></td>
							<td style="text-align: center;">
								<fmt:formatDate value="${subject.addTime}"  pattern="yyyy-MM-dd HH:mm:ss" />
							</td>	
							<td style="text-align: center;">
								<shiro:hasPermission name="ec:subject:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/ec/subject/form?subId=${subject.subId}','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:subject:del">
									<a href="${ctx}/ec/subject/del?subId=${subject.subId}" onclick="return confirmx('确认要删除吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:subject:addGoods">
									<a href="#" onclick="openDialogView('增加商品', '${ctx}/ec/subject/subjectGoodsList?subId=${subject.subId}','700px', '650px')" class="btn btn-info btn-xs"><i class="fa fa-edit"></i> 增加商品</a>
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