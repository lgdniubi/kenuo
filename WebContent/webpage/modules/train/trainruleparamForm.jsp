<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>商品品牌管理</title>
	<meta name="decorator" content="default"/>
	<%-- <script type="text/javascript" src="${ctxStatic}/train/js/jquery-1.11.3.js"></script> --%>
	<script type="text/javascript">
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
			
			//页面加载时，初始化规则类型
			ruletypechange($("#trainRuleParamTypeId").val());
			
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
		
		//根据规则类型id，查询其别名值
		function ruletypechange(ruletypeid){
			
			if(ruletypeid == -1){
				//当不选择分类时，则不显示分类
				$("#typealiaName").html("");
				$("#aliaName").val("");
			}else{
				$(".loading").show();//打开展示层
				$.ajax({
					type : "POST",   
					url : "${ctx}/train/ruleparam/getruletype?ruletypeid="+ruletypeid,
					dataType: 'json',
					success: function(data) {
						$(".loading").hide(); //关闭加载层
						if(data.STATUS == 'OK'){
							$("#typealiaName").html(data.ALIANAME);
							$("#aliaName").val(data.ALIANAME);
						}else{
							alert(data.MESSAGE);
						}
					}
				});  
			}
		}
		
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="trainRuleParam" action="${ctx}/train/ruleparam/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" />
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tr>
				<td class="active">
					<label class="pull-right">参数类型:</label>
				</td>
				<td>
					<select id="trainRuleParamTypeId" name="trainRuleParamType.id" class="form-control required" onchange="ruletypechange(this.options[this.options.selectedIndex].value)">
						<option value="-1"></option>
						<c:forEach items="${trainRuleParam.ruleparamtypelist}" var="tspType">
							<option ${trainRuleParam.trainRuleParamType.id==tspType.id?'selected="selected"':''} value="${tspType.id }">${tspType.typeName }</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td class="active">
					<label class="pull-right"><font color="red">*</font>参数Key:</label>
				</td>
				<td>
					<span id="typealiaName" style="color: red;"></span>
					<input type="text" id="paramKey" name="paramKey" maxlength="64" value="${trainRuleParam.paramKey }" class="form-control required" style="width: 400px;">
					<input type="hidden" id="aliaName" name="trainRuleParamType.aliaName" >
				</td>
			</tr>
			<tr>
				<td class="active">
					<label class="pull-right"><font color="red">*</font>参数值:</label>
				</td>
				<td>
					<form:input path="paramValue" htmlEscape="false" maxlength="250" class="form-control required" />
				</td>
			</tr>
			<tr>
				<td class="active">
					<label class="pull-right">上限:</label>
				</td>
				<td>
					<form:input path="paramUplimit" htmlEscape="false" maxlength="250" class="form-control" />
				</td>
			</tr>
			<tr>
				<td class="active">
					<label class="pull-right"><font color="red">*</font>参数说明:</label>
				</td>
				<td>
					<form:textarea path="paramExplain" htmlEscape="false" rows="3" maxlength="500" class="form-control required"/>
				</td>
			</tr>
			<tr>
				<td class="active">
					<label class="pull-right">排序:</label>
				</td>
				<td>
					<form:input path="sort" htmlEscape="false" maxlength="11" class="form-control" />
				</td>
			</tr>
			<tr>
				<td class="active">
					<label class="pull-right">备注:</label>
				</td>
				<td>
					<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/>
				</td>
			</tr>
		</table>
	</form:form>
	<div class="loading"></div>
</body>
</html>