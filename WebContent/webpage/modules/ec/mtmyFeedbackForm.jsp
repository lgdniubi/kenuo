<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
 	<meta name="decorator" content="default"/>
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
    <script src="${ctxStatic}/train/js/jquery-1.11.3.js"></script>
    <script type="text/javascript">
    	$(document).ready(function(){
    		var pictures = "${feedback.messageImg}";
    		if(pictures != null && pictures != ""){
				var arr = pictures.split(",");
    			for(var i=0;i<arr.length;i++){
    	        	   if(arr[i] != ''){
    	        		   $("#photo").append("<a class='img' href="+arr[i]+"><img width='150px' height='150px' style='padding:3px' src='"+arr[i]+"'></a>");
    	        	   }
    			}
    		}
    	});
    </script>
    <title>反馈详情</title>
</head>
<body>
   <div class="wrapper wrapper-content">
		<div class="ibox">
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
						<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
							<tr style="text-align: center;">
								<td><label>用户昵称</label></td>
								<td>
									${feedback.users.nickname}
								</td>
							</tr>
							<tr style="text-align: center;">
								<td><label>用户姓名</label></td>
								<td>
									${feedback.users.name}
								</td>
							</tr>
							<tr style="text-align: center;">
								<td><label>联系电话</label></td>
								<td>
									${feedback.mobile}
								</td>
							</tr>
							<tr style="text-align: center;">
								<td><label>反馈类型</label></td>
								<td>
									<c:if test="${feedback.msgType == 0}">功能异常</c:if>
			               			<c:if test="${feedback.msgType == 1}">使用建议</c:if>
			               			<c:if test="${feedback.msgType == 2}">功能需求</c:if>
			               			<c:if test="${feedback.msgType == 3}">系统优化</c:if>
			               			<c:if test="${feedback.msgType == 4}">其他</c:if>
								</td>
							</tr>
							<tr style="text-align: center;">
								<td><label>反馈描述</label></td>
								<td>
									${feedback.msgContent}
								</td>
							</tr>
							<tr style="text-align: center;">
								<td><label>反馈图片</label></td>
								<td style="text-align: center;">
									<div id="photo"></div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>