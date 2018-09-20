<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>编辑信用额度</title>
	<meta name="decorator" content="default"/>
	<!-- 引入layui.css -->
	<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">
	
	<%-- <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css"> --%>
	<script type="text/javascript">
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			var creditLimit = $("#credit").val();
			var v = $("#creditLimit").val();
			var usedLimit = $("#usedLimit").val();
			var useLimit = $("#hasUsedCreditLimit").val();
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
			//compareVal(v);
			$("#usedLimit").val(v - useLimit);
			//$("#useLimit").val(useLimit);
			$("#inputForm").submit();
			return true;
		};

		function checkNum (vall) {
			//数字开头
			//只能输入数字与点
			//只能输入一个点
			//小数点后4位
			document.getElementById('creditLimit').value = vall.replace(/^\./g,"").replace(/[^\d.]/g,"").replace(".","$#$").replace(/\./g,"").replace("$#$",".").replace(/^(\-)*(\d+)\.(\d\d\d\d).*$/,'$1$2.$3');
		}; 
		function checkVal (v) {
			console.log(v)
			if(compareVal(v)){
				var hasUsedCreditLimit = $("#hasUsedCreditLimit").val();	//已经使用额度
				var usedLimit = parseFloat(v) - parseFloat(hasUsedCreditLimit);	//可用额度
			console.log(usedLimit)
				$("#usedLimit").val(usedLimit.toFixed(4));	
			}
		}
		
		function compareVal (v) {
			var hasUsedCreditLimit = $("#hasUsedCreditLimit").val();	//已经使用额度
			
			if (parseFloat(v) < parseFloat(hasUsedCreditLimit)){
				top.layer.alert('不能小于已用额度', {icon: 0, title:'提醒'});
				return false;
			}	
			return true;
		}

		var validateForm;
		$(function(){
			validateForm = $("#inputForm").validate({ });
		});
	</script>
</head>
<body>
	<div class="ibox-content">
		<div class="clearfix">
			<div class="nav">
				<ul class="layui-tab-title">
					<li  class="layui-this">
						<a href="${ctx}/sys/officeCredit/toEditCredit?office_id=${officeAcount.officeId}">额度调整</a>
					</li>
					<li >
						<a href="${ctx}/sys/officeCredit/creditLogList?officeId=${officeAcount.officeId}">额度日志</a>
					</li>
					
				</ul>
			</div>
		</div>
		<form:form id="inputForm" modelAttribute="officeAcount" action="${ctx}/sys/officeCredit/updateOfficeCreditLimit" method="post" class="form-horizontal">
			<input type="hidden" value="${officeAcount.officeId }" name="officeId">
			<%-- <input type="hidden" name="usedLimit" id="usedLimit" value="${officeAcount.usedLimit}"> --%>
			<input type="hidden" name="useLimit" id="useLimit" value="${officeAcount.usedLimit}">
			<input type="hidden" id="credit" name="oldCreditLimit" value="${officeAcount.creditLimit}">
			<sys:message content="${message}"/>
			<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-15" class="active"><label class="pull-right"><font color="red">*</font>总额度</label></td>
		         <td class="width-35"><input id="creditLimit" oninput="checkNum(this.value)" onblur="checkVal(this.value)" name="creditLimit" value="${officeAcount.creditLimit}" class="form-control required number">
<!-- 		         oninput="checkNum(this.value)" -->
		      </tr>
		      <tr>
		         <td  class="width-15" class="active"><label class="pull-right"><font color="red">*</font>已用额度</label></td>
		         <td class="width-35"><input id="hasUsedCreditLimit" readonly unselectable="on" type="text" value="${officeAcount.useLimit}" class="form-control required number">
		      </tr>
		      <tr>
		         <td  class="width-15" class="active"><label class="pull-right"><font color="red">*</font>当前可用额度</label></td>
		         <td class="width-35"><input id="usedLimit" readonly unselectable="on" name="usedLimit" type="text" value="${officeAcount.usedLimit}" class="form-control required number">
		      </tr>
		      <tr>
		         <td  class="width-15" ></td>
		         <td class="width-35"><input id="btn"  type="button" onclick="doSubmit()" value="确定" class="fa fa-edit">
		      </tr>
			</tbody>
			</table>
		</form:form>
	</div>
	
	
</body>

</html>