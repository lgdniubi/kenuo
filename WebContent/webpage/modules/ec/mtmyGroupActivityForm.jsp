<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
	<title>新增团购项目</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if(validateForm.form()){
        	  loading('正在提交，请稍等...');
		      $("#inputForm").submit();
	     	  return true;
		  	}
		  return false;
		}
		
		$(document).ready(function(){
			validateForm = $("#inputForm").validate({
				rules: {
					
				},
				messages:{
					
				},
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
			
			var activityStartDate = {
			    elem: '#activityStartDate',
			    format: 'YYYY-MM-DD',
			    min: $("#parentStartDate").val(),   //最大日期
			    max: $("#parentEndDate").val(),   //最小日期
			    event: 'focus',
			    istime: false,
			    isclear: true,
			    istoday: true,
			    issure: true,
			    festival: true,
			    choose: function(datas){
			    	activityEndDate.min = datas;
			    	activityEndDate.start = datas;
			    }
			};
			var activityEndDate = {
			    elem: '#activityEndDate',
			    format: 'YYYY-MM-DD',
			    min: $("#parentStartDate").val(),   //最大日期
			    max: $("#parentEndDate").val(),   //最小日期
			    event: 'focus',
			    istime: false,
			    isclear: true,
			    istoday: true,
			    issure: true,
			    festival: true,
			    choose: function(datas){
			    	activityStartDate.max = datas;
			    }
			};
			laydate(activityStartDate);
			laydate(activityEndDate);
		});	
	</script>
</head>
	<body>
		<form:form id="inputForm" modelAttribute="mtmyGroupActivity" action="${ctx}/ec/groupActivity/save" method="post" class="form-horizontal">
		<form:hidden path="id" id="id"/>
		<form:hidden path="parent.id"/>
		<input value="<fmt:formatDate value="${mtmyGroupActivity.parent.activityStartDate}" pattern="yyyy-MM-dd"/>" id="parentStartDate" type="hidden"><!-- 父类的项目活动开始时间 -->
		<input value="<fmt:formatDate value="${mtmyGroupActivity.parent.activityEndDate}" pattern="yyyy-MM-dd"/>" id="parentEndDate" type="hidden"><!-- 父类的项目活动结束时间 -->
		<sys:message content="${message}"/>
			<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
				<tbody>
					<tr>
						<td class="width-15 active"><label class="pull-right"><font color="red">*</font>项目名称:</label></td>
						<td class="width-35">
							<form:input path="name" cssClass="form-control required"/>
						</td>
					</tr>
					<tr>
						<td class="active"><label class="pull-right"><font color="red">*</font>团购时间:</label></td>
						<td class="width-35">
							<input id="activityStartDate" name="activityStartDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required"
								value="<fmt:formatDate value="${mtmyGroupActivity.activityStartDate}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="项目开始时间" readonly="readonly"/>
							至
							<input id="activityEndDate" name="activityEndDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required"
								value="<fmt:formatDate value="${mtmyGroupActivity.activityEndDate}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="项目结束时间" readonly="readonly"/>
						</td>
					</tr>
					<c:if test="${mtmyGroupActivity.parent.id != '0'}">
					<tr>
						<td class="active"><label class="pull-right"><font color="red">*</font>团购时间:</label></td>
						<td class="width-35">
							<input id="activityStartTimeStr" name="activityStartTimeStr" value="<fmt:formatDate value="${mtmyGroupActivity.activityStartTime}" pattern="HH:mm"/>" class="datetimepicker form-control required" readonly="readonly" placeholder="项目开始时间">
				         	至
							<input id="activityEndTimeStr" name="activityEndTimeStr" value="<fmt:formatDate value="${mtmyGroupActivity.activityEndTime}" pattern="HH:mm"/>" class="datetimepicker form-control required" readonly="readonly" placeholder="项目结束时间">
						</td>
					</tr>
					</c:if>    
				</tbody>
			</table>   
		</form:form>
	<script type="text/javascript" src="${ctxStatic}/train/js/jquery.datetimepicker.js"></script>
	<script>
	    // 选取时间
	    $('.datetimepicker').datetimepicker({
			datepicker:false,
			format:'H:i',
			step:30
		});
    </script>
	</body>
</html>