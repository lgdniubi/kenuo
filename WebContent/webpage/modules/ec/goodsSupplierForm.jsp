<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>供应商管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if($("#name").val() == null || $("#name").val() == ""){
				   top.layer.alert('名称不可为空！', {icon: 0, title:'提醒'});
				   return false;
			}
	    	if(validateForm.form()){
	    		loading("正在提交，请稍候...");
				$("#inputForm").submit();
		    	return true;
	    	}
	    	return false;
	    }
		$(document).ready(function() {
			validateForm = $("#inputForm").validate();
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
						<form id="inputForm" action="${ctx}/ec/goodsSupplier/save">
							<input class="form-control" id="goodsSupplierId" name="goodsSupplierId" type="hidden" value="${goodsSupplier.goodsSupplierId }"/>
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<td><label class="pull-right"><font color="red">*</font>名称：</label></td>
									<td>
										<input class="form-control required" id="name" name="name" type="text" value="${goodsSupplier.name}" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>状态：</label></td>
									<td>
										<select class="form-control required" id="status" name="status">
											<c:if test="${goodsSupplier.status != 1}">
												<option value="0" selected="selected">商用</option>
												<option value="1">暂停</option>
											</c:if>
											<c:if test="${goodsSupplier.status == 1}">
												<option value="0">商用</option>
												<option value="1" selected="selected">暂停</option>
											</c:if>
											
										</select>
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