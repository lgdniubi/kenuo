<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/treetable.jsp" %>
<html>
	<head>
		<title>添加课程线下预约</title>
		<meta name="decorator" content="default" />
		<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
		<script type="text/javascript" src="${ctxStatic}/train/js/jquery-1.11.3.js"></script>
		<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.js"></script>
		<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js"></script>
		<script type="text/javascript">
			//一级分类改变事件，联动二级分类
			function categorychange(v){
				$(".loading").show();//打开展示层
				$.ajax({
					type : "POST",   
					url : "${ctx}/train/categorys/listtow?categoryId="+v,
					dataType: 'json',
					success: function(data) {
						$(".loading").hide(); //关闭加载层
						$("#s2").html("");
						$.each(data.listtow, function(index,item){
							if(item.categoryId == $("#categoryId").val()){
								$("#s2").prepend("<option selected='selected' value='"+item.categoryId+"'>"+item.name+"</option>");
							}else{
								$("#s2").prepend("<option value='"+item.categoryId+"'>"+item.name+"</option>");
							}
						});
					}
				});   
			}
			
			//添加时间段
			function numtime(v){
				$('#input-item').empty();
				for(var i = 1;i <= v ;i++){
					$('#input-item').append('<p>预约时间：<input id="subscribeDate" name="subscribeDate" type="text" class="datetimepicker form-control required"/></p>');
					$('.datetimepicker').datetimepicker({minDate:'-'+Date()});
				}
			}
			
			var validateForm;
			function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
				if(validateForm.form()){
					$("#inputForm").submit();
				  	return true;
			  	}
			  	return false;
			}
			
			//页面加载事件
			$(document).ready(function() {
				//默认加载第一个分类
				categorychange($("#parentId").val());
				
				//屏蔽分类选项卡
				$("#s1").attr({"disabled":"disabled"});//禁用分类选项
				$("#s2").attr({"disabled":"disabled"});//禁用分类选项
				
				//表单验证
				validateForm = $("#inputForm").validate({
					submitHandler: function(form){
						loading('正在提交，请稍等...');
						form.submit();
					},
					errorContainer: "#messageBox",
					errorPlacement: function(error, element) {
						$("#messageBox").text("输入有误，请先更正。");
						if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
							error.appendTo(element.parent().parent());
						} else {
							error.insertAfter(element);
						}
					}
				});
			});
			
		</script>
	</head>
	<body>
		<div class="ibox-content" style="height: 100%">
			<sys:message content="${message}"/>
			<div class="warpper-content">
				<ul id="myTab1" class="nav nav-tabs">
					<li class="active"><a href="#cooking" data-toggle="tab">线下预约</a></li>
				</ul>
				<div id="myTabContent1" class="tab-content">
					<form:form id="inputForm" modelAttribute="trainCategorys" action="${ctx}/train/subscribe/savesubscribe" method="post">
						<div class="input-item">
							<span>课程分类：</span> 
							<input type="hidden" id="categoryId" name="categoryId" value="${trainCategorys.categoryId}" />
							<input type="hidden" id="parentId" name="parentId" value="${trainCategorys.parentId}" />
							<select class="form-control" id="s1" name="s1" onchange="categorychange(this.options[this.options.selectedIndex].value)">
								<c:forEach items="${listone}" var="trainCategory">
									<c:if test="${trainCategory.categoryId == trainCategorys.parentId}">
										<option selected="selected" value="${trainCategory.categoryId}">${trainCategory.name}</option>
									</c:if>
									<c:if test="${trainCategory.categoryId != trainCategorys.parentId}">
										<option  value="${trainCategory.categoryId}">${trainCategory.name}</option>
									</c:if>
								</c:forEach>
							</select>
							<select class="form-control" id="s2" name="s2">
								
							</select> 
						</div>
						<div class="input-item">
							<span>添加时间段：</span> 
							<select id="lessontype" name="lessontype" class="form-control input-item-bootm" onchange="numtime(this.options[this.options.selectedIndex].value)">
								<c:forEach var="i" begin="1" end="${4 - findStatusList.size()}">
									<option value="${i}"><c:out value="${i}"/></option>
								</c:forEach>
							</select>
							<div id="input-item" style="padding-top: 12px;">
								<p>
									预约时间：<input name="subscribeDate" id="subscribeDate" type="text" class="datetimepicker form-control required" >
								</p>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
		<div class="loading"></div>
		<script type="text/javascript" src="${ctxStatic}/train/js/jquery.datetimepicker.js"></script>
		<script type="text/javascript">
			// 选取时间
	    	$('.datetimepicker').datetimepicker({
	    		minDate:'-'+Date()
	    	});
		</script>
	</body>
</html>