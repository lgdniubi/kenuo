<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> --%>
<%-- <%@ page import="com.training.modules.sys.utils.ParametersFactory"%> --%>
<html>
<head>
	<title>拒绝通过</title>
	<meta name="decorator" content="default"/>
		
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
	$(function(){
		validateForm = $("#inputForm").validate({ });
	});
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="modelFranchisee" action="${ctx}/train/userCheck/save" method="post" class="form-horizontal">
		<input name="userid" value="${userid}" type="hidden">
		<input name="id" value="${id}" type="hidden">
		<input name="status" value="${status}" type="hidden">
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
			    <tr>
			        <td> <textarea name="remarks" id="label-reason" style="width:100px,height:100px" class="required" maxlength="50" placeholder="请输入拒绝理由（最多50个字）"></textarea></td>
				</tr>
			</tbody>
		</table>  
	</form:form> 
</body>
</html>