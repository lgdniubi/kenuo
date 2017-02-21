<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>发布界面</title>
	<meta name="decorator" content="default"/>
	<!-- 日期控件 -->
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
		.layer-date{vertical-align: middle;}
	</style>
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
							<div class="row">
								<div class="col-xs-12 col-md-12 "style="height:35px;line-height:35px;" >
									<input type="checkbox" id="" name="" value="train"> 妃子校
								</div>		
								<div class="col-xs-6 col-md-6" style="height:35px;line-height:35px;">
									<span style="float:left;">分类：</span><select id="trainsCategoryId" name="trainsCategoryId" class="form-control required" style="width:200px;">
										<c:forEach items="${trainsCategoryList }" var="list">
											<option value="${list.categoryId}">${list.name}</option>
										</c:forEach>
									</select>
								</div>
								<div class="col-xs-6 col-md-6" style="height:35px;line-height:35px;">
									<span style="float:left;">发布时间：</span><input id="trainsTaskDate" name="trainsTaskDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" value="<fmt:formatDate value="${articles.taskDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
												onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s}'})"/>
								</div>
								
							</div>
							<div class="row">
								<div class="col-xs-12 col-md-12 "style="height:35px;line-height:35px;" >
									<input type="checkbox" id="" name="" value="mtmy"> 每天美耶
								</div>
								<div class="col-xs-6 col-md-6" style="height:35px;line-height:35px;">
									<span style="float:left;">分类：</span>
									<select id="mtmyCategoryId" name="mtmyCategoryId" class="form-control required" style="width:200px;">
										<c:forEach items="${mtmyCategoryList }" var="list">
											<option value="${list.id}">${list.name}</option>
										</c:forEach>
									</select>
								</div>
								<div class="col-xs-6 col-md-6" style="height:35px;line-height:35px;">
									<span style="float:left;">发布时间：</span>
									<input id="mtmyTaskDate" name="mtmyTaskDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" value="<fmt:formatDate value="${articles.taskDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
												onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s}'})"/>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>