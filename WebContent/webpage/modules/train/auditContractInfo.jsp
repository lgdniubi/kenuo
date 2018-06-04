<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> --%>
<%-- <%@ page import="com.training.modules.sys.utils.ParametersFactory"%> --%>
<html>
<head>
	<title>签约审核</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
        	  loading('正在提交，请稍等...');
		      $("#inputForm").submit();
	     	  return true;
		}
		
		function checkValue(value){
			if(value == 2){
				$("#remarks").hide();
			}else if(value == 3){
				$("#remarks").show();
			}
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="modelFranchisee" action="${ctx}/train/contractInfo/auditContractInfo" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<input name="office_id" value="${office_id}" type="hidden">
		
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="3"><label class="pull-left">手艺人权益设置:</label></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">操作类型:</label></td>
			         <td><input id="mod_id1" class=" input-sm required" name="status" value="2" aria-required="true" type="radio" onclick="checkValue(this.value)">审核通过</td>
			         <td><input id="mod_id2" class=" input-sm required" name="status" value="3" aria-required="true" type="radio" onclick="checkValue(this.value)">审核驳回</td>
				</tr>
			    <tr id="remarks">
			         <td class="active"><label class="pull-right">驳回原因:</label></td>
			         <td colspan="2">
			         	<textarea rows="4" cols="80" name="remarks"></textarea>
			         </td>
				</tr>
			</tbody>
		</table>  
	</form:form> 
</body>
</html>