<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> --%>
<%-- <%@ page import="com.training.modules.sys.utils.ParametersFactory"%> --%>
<html>
<head>
	<title>账单审核</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">
		var validateForm;

		$(document).ready(function() {
			//表单验证
			validateForm = $("#inputForm").validate({ });
		});
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
        	  loading('正在提交，请稍等...');
        	   if(validateForm.form()){ 
		     		$("#inputForm").submit();
	     	   return true; 
        	   }
        	  return false; 
		}
		
		/* function checkValue(value){
			if(value == 3){
				$("#remarks").hide();
				$("#remarks [name='remarks']").removeClass("required");
			}else if(value == 4){
				$("#remarks").show();
				$("#remarks [name='remarks']").addClass("required");
			}
		} */
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="modelFranchisee" action="${ctx}/train/refundOrder/makeSureInAccount" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<input name="office_id" value="${office_id}" type="hidden">
		<input name="order_id" value="${order_id}" type="hidden">
		<input name="amount" value="${amount}" type="hidden">
		<input type="hidden" name="status" id="status" value="4"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
			    <!-- <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="3"><label class="pull-left">手艺人权益设置:</label></td>
				</tr> 
			    <tr>
			         <td class="active"><label class="pull-right">操作类型:</label></td>
			         <td><input id="mod_id1" class=" input-sm required" name="status" value="3" aria-required="true" type="radio" onclick="checkValue(this.value)">确认入账</td>
			         <td><input id="mod_id2" class=" input-sm required" name="status" value="4" aria-required="true" type="radio" onclick="checkValue(this.value)">审核驳回</td>
				</tr>-->
			    <tr id="remarks">
			         <td ><label class="pull-left">驳回原因:</label></td>
				</tr>
				<tr>
			         <td colspan="2">
			         	<textarea rows="5"  name="remarks" class="input-medium form-control required"></textarea>
			         </td>
				</tr>
			</tbody>
		</table>  
	</form:form> 
</body>
</html>