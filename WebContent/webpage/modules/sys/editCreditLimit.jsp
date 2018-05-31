<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>编辑信用额度</title>
	<meta name="decorator" content="default"/>
	
	
	<%-- <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css"> --%>
	<script type="text/javascript">
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			var creditLimit = $("#credit").val();
			var v = $("#creditLimit").val();
			var usedLimit = $("#usedLimit").val();
			var useLimit = creditLimit - usedLimit;
			if(v < useLimit ){
				alert("信用额度不能小于已用额度");
				return;
			}
			if(!v){
				alert("请输入数字");
				return;
			}
			if(isNaN(v)){
				alert("请输入数字");
				return;
			}
			$("#usedLimit").val(v - useLimit);
			$("#useLimit").val(usedLimit);
			$("#inputForm").submit();
			return true;
		};

		function checkNum (vall) {
			//数字开头
			//只能输入数字与点
			//只能输入一个点
			//小数点后4位
			document.getElementById('creditLimit').value = vall.replace(/^\./g,"").replace(/[^\d.]/g,"").replace(/\.{2,}/g,".").replace(/^(\-)*(\d+)\.(\d\d\d\d).*$/,'$1$2.$3');
		}; 

	</script>
</head>
<body>
	<div class="ibox-content">
		<form:form id="inputForm" modelAttribute="office" action="${ctx}/sys/office/updateOfficeCreditLimit" method="post" class="form-horizontal">
			<input type="hidden" value="${officeAcount.officeId }" name="officeId">
			<%-- <input type="hidden" name="usedLimit" id="usedLimit" value="${officeAcount.usedLimit}"> --%>
			<input type="hidden" name="useLimit" id="useLimit">
			<input type="hidden" id="credit" value="${officeAcount.creditLimit}">
			<sys:message content="${message}"/>
			<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-15" class="active"><label class="pull-right"><font color="red">*</font>信用额度</label></td>
		         <td class="width-35"><input id="creditLimit" oninput="checkNum(this.value)" name="creditLimit" value="${officeAcount.creditLimit}" class="form-control required">
		      </tr>
		      <%-- <tr>
		         <td  class="width-15" class="active"><label class="pull-right"><font color="red">*</font>信用额度</label></td>
		         <td class="width-35"><input id="creditLimit" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" name="creditLimit" type="text" value="${officeAcount.creditLimit}" class="form-control required">
		      </tr> --%>
		      <tr>
		         <td  class="width-15" class="active"><label class="pull-right"><font color="red">*</font>可用额度</label></td>
		         <td class="width-35"><input id="usedLimit" readonly unselectable="on" name="usedLimit" type="text" value="${officeAcount.usedLimit}" class="form-control required">
		      </tr>
			</tbody>
			</table>
		</form:form>
	</div>
	
	
</body>

</html>