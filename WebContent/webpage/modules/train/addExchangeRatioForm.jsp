<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>主题图</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	
	<script type="text/javascript">
	$(document).ready(function() {
		validateForm = $("#inputForm").validate();
		
		var exchangeRatioId = $("#exchangeRatioId").val();
		var exchangeType = $("#exchangeType").val();
		
		if(exchangeRatioId == 0){
			$("#exchangeType3").show();
		}else{
			$("#exchangeType3").hide();
		}
		
		if(exchangeType == '0'){
			$("#exchangePrice1").show();
			$("#exchangePrice2").hide();
		}else if(exchangeType == '1'){
			$("#exchangePrice1").hide();
			$("#exchangePrice2").show();
		} 
		 
		var exchangePrice = $("#exchangePrice22").val();
		var afterExchangePrice = parseInt(exchangePrice);
		$("#exchangePrice22").val(afterExchangePrice);
	});
	
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
	    	if(validateForm.form()){
	    		loading("正在提交，请稍候...");
				$("#inputForm").submit();
		    	return true;
	    	}
	    	return false;
	    }
		
		function change(value){
			if(value == 0){
				$("#exchangePrice1").show();
				$("#exchangePrice2").hide();
			}else if(value == 1){
				$("#exchangePrice1").hide();
				$("#exchangePrice2").show();
			}
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
						<form id="inputForm" action="${ctx}/train/ratio/saveExchangeRatio">
							<input id="exchangeRatioId" name="exchangeRatioId" type="hidden" value="${trainLiveExchangeRatio.exchangeRatioId}"/>
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<td><label class="pull-right"><font color="red">*</font>名称：</label></td>
									<td>
										<input class="form-control required" id="name" name="name" type="text" value="${trainLiveExchangeRatio.name }" style="width: 300px"/>
									</td>
								</tr>
								<tr id="exchangeType3">
									<td class="active" style="width:100px;">
										<label class="pull-right"><font color="red">*</font>兑换类型:</label>
									</td>
									<td>
										<select class="form-control required" id="exchangeType" name="exchangeType" onchange="change(this.value)">
											<option value=0 <c:if test="${trainLiveExchangeRatio.exchangeType == 0}">selected</c:if>>人民币换云币</option>
											<option value=1 <c:if test="${trainLiveExchangeRatio.exchangeType == 1}">selected</c:if>>佣金换云币</option>
										</select>
									</td>
								</tr>
								<tr id="exchangePrice1">
									<td><label class="pull-right"><font color="red">*</font>人民币：</label></td>
									<td>
										<input class="form-control required" id="exchangePrice11" name="exchangePrice" type="text" value="${trainLiveExchangeRatio.exchangePrice }" style="width: 300px"/>
									</td>
								</tr>
								<tr id="exchangePrice2">
									<td><label class="pull-right"><font color="red">*</font>佣金：</label></td>
									<td>
										<input class="form-control required" id="exchangePrice22" name="exchangePrice" type="text" value="${trainLiveExchangeRatio.exchangePrice}" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>云币：</label></td>
									<td>
										<input class="form-control required" id="integrals" name="integrals" type="text" value="${trainLiveExchangeRatio.integrals }" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>排序：</label></td>
									<td>
										<input class="form-control required" id="sort" name="sort" type="text" value="${trainLiveExchangeRatio.sort }" style="width: 300px"/>
									</td>
								</tr>
							</table>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>