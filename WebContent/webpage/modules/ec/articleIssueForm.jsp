<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>发布界面</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
	    	if(validateForm.form()){
	    		loading("正在提交，请稍候...");
				$("#inputForm").submit();
		    	return true;
	    	}
	    	return false;
	    };
	    
		$(document).ready(function() {
			$("#mtmy").hide();
			validateForm = $("#inputForm").validate();
		});
		function changeType(type){
			if("1" == type){
				$("#trains").show();
				$("#mtmy").hide();
			}else if("2" == type){
				$("#trains").hide();
				$("#mtmy").show();
			}else{
				top.layer.alert('发布类型有误！', {icon: 0});
			}
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
					<shiro:hasPermission name="ec:articles:findLogs">
						<button class="btn btn-primary btn-xs" title="发布日志" onclick="openDialog('发布日志', '${ctx}/ec/articles/findLogs?articleId=${articleRepository.articleId }','650px', '500px')" data-placement="left" data-toggle="tooltip">
							<i class="fa fa-calendar-o"></i> 发布日志
						</button>
					</shiro:hasPermission>
	                <div class="tab-inner">
						<form:form id="inputForm"  action="${ctx}/ec/articles/sendArticles" method="post">
							<input type="hidden" id="articleId" name="articleId" value="${articleRepository.articleId }">
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<td width="100px;"><label class="pull-right"><font color="red">*</font>发布到：</label></td>
									<td>
										<select id="type" name="type" class="form-control required" onchange="changeType(this.value)">
											<option value="1">妃子校</option>
											<option value="2">每天美耶</option>
										</select>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>所属分类：</label></td>
									<td>
										<div id="trains">
											<select id="trainsCategoryId" name="trainsCategoryId" class="form-control required">
												<c:forEach items="${trainsCategoryList }" var="list">
													<option value="${list.categoryId}">${list.name}</option>
												</c:forEach>
											</select>
										</div>
										<div id="mtmy">
											<select id="mtmyCategoryId" name="mtmyCategoryId" class="form-control required">
												<c:forEach items="${mtmyCategoryList }" var="list">
													<option value="${list.id}">${list.name}</option>
												</c:forEach>
											</select>
										</div>
									</td>
								</tr>
							</table>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>