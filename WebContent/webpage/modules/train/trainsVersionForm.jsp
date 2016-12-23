<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>版本控制图</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
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
						<form id="inputForm" action="${ctx}/trains/version/save">
							<input class="form-control" id="id" name="id" type="hidden" value="${version.id }"/>
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<td><label class="pull-right"><font color="red">*</font>版本号：</label></td>
									<td>
										<input class="form-control required" id="versionCode" name="versionCode" type="text" value="${version.versionCode }" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>类型：</label></td>
									<td>
										<select class="form-control required" id="type" name="type">
											<c:if test="${version.type != 1}">
												<option value="0" selected="selected">兼容版本</option>
												<option value="1">审核版本</option>
											</c:if>
											<c:if test="${version.type == 1 }">
												<option value="0">兼容版本</option>
												<option value="1" selected="selected">审核版本</option>
											</c:if>
											
										</select>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>客户端：</label></td>
									<td>
										<select class="form-control required" id="client" name="client">
											<c:if test="${version.client != 'android'}">
												<option value="ios">ios</option>
												<option value="android">android</option>
											</c:if>
											<c:if test="${version.client == 'android'}">
												<option value="ios">ios</option>
												<option value="android" selected="selected">android</option>
											</c:if>
										</select>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>状态：</label></td>
									<td>
										<select class="form-control required" id="flag" name="flag">
											<c:if test="${version.flag != 1}">
												<option value="0" selected="selected">开启</option>
												<option value="1">关闭</option>
											</c:if>
											<c:if test="${version.flag == 1 }">
												<option value="0">开启</option>
												<option value="1" selected="selected">关闭</option>
											</c:if>
											
										</select>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>备注：</label></td>
									<td>
										<textarea id="remark" name="remark" cols="35" rows="4" class="form-control required">${version.remark }</textarea>
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