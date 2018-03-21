<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>明星技师组</title>
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
	    };
	    
		$(document).ready(function() {
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
				}
			);
			var id = '${starBeauty.id}';
			if(id > 0){
				$("[name='isOpen']").attr("disabled",true);
			}
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
						<form:form id="inputForm" modelAttribute="starBeauty" action="${ctx}/ec/starBeauty/save">
							<form:hidden path="id"/>
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<td><label class="pull-right"><font color="red">*</font>明星技师组名称：</label></td>
									<td>
										<form:input path="name" class="form-control required" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right">备注：</label></td>
									<td>
										<form:input path="remarks" class="form-control" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>排序：</label></td>
									<td>
										<form:input path="sort" class="form-control required" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>选择商家:</label></td>	
									<td>
										<label><input type="radio" id="isOpen" name="isOpen" value="0" <c:if test="${starBeauty.isOpen == 0}">checked="checked"</c:if> class="form required">公开</label>
										<c:forEach items="${list}" var="franchisee">
											<label><input type="radio" id="isOpen" name="isOpen" <c:if test="${starBeauty.franchiseeId == franchisee.id}">checked="checked"</c:if> value="${franchisee.id}" class="form required">${franchisee.name}</label>
										</c:forEach>
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