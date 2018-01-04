<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>tab_banner图</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	
	<!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<script type="text/javascript">
	  function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#inputForm").submit();
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
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
						<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
							<tr>
								<td><label class="pull-right"><font color="red">*</font>推荐组名称：</label></td>
								<td>
									<input class="form-control required" id="name" name="name" type="text" value="${officeRecommend.name }" style="width: 300px"/>
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>头图：</label></td>
								<td>
									<img id="img1" src="${officeRecommend.img }" alt="" style="width: 200px;height: 100px;"/>
									<input class="form-control" id="img" name="img" type="hidden" value="${officeRecommend.img }"/><!-- 图片隐藏文本框 -->
									<input type="file" name="file_img_upload" id="file_img_upload">
									<div id="file_img_queue"></div>
								</td>
						</table>
					</div>                                                              
					<form id="inputForm" action="${ctx}/ec/officeRecommend/togetherForm">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="officeRecommendId" name="officeRecommendId" value="${officeRecommend.officeRecommendId}" type="hidden">
						<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">序号</th>
								<th style="text-align: center;">店铺ID</th>
								<th style="text-align: center;">店铺名称</th>
								<th style="text-align: center;">排序</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${page.list}" var="page">
								<tr>
									<td style="text-align: center;">${page.officeRecommendMappingId}</td>
									<td style="text-align: center;">${page.officeId}</td>
									<td style="text-align: center;">${page.officeName}</td>
									<td style="text-align: center;">${page.sort}</td>
								</tr>
							</c:forEach>
						</tbody>
						<tfoot>
							<tr>
		                    	<td colspan="20">
		                        	<!-- 分页代码 --> 
		                            <table:page page="${page}"></table:page>
		                        </td>	
		                    </tr>
	                   </tfoot>
					</table>
				</form>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>