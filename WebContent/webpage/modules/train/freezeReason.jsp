<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> --%>
<%-- <%@ page import="com.training.modules.sys.utils.ParametersFactory"%> --%>
<html>
<head>
	<title>冻结</title>
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
	<form:form id="inputForm" modelAttribute="modelFranchisee" action="${ctx}/train/specialUser/freeze" method="post" class="form-horizontal">
		<input name="id" value="${id}" type="hidden">
		<input name="opflag" value="${opflag}" type="hidden">
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
			    <tr>
			        <td> <textarea name="delRemarks" id="label-reason" style="width:270px;height:170px" class="required" maxlength="200" placeholder="请输入冻结原因（最多200个字）"></textarea></td>
				</tr>
			</tbody>
		</table>  
	</form:form> 
</body>
</html>