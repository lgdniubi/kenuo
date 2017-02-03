<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>新增设备</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
						<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
							<tr>
								<td class="width-15 active">
									<label class="pull-right"><font color="red">*</font>设备名称:</label>
								</td>
								<td>
									<sys:treeselect id="equipment" name="equipmentId" value="${ShopComEquipment.equipmentId}" labelName="name" labelValue="${ShopComEquipment.name}" title="通用设备" url="/ec/specEquipment/treeDataForCom?shopId=${shopComEquipment.shopId}" cssClass="form-control" notAllowSelectParent="true"/>
								</td>
							</tr>
							<tr>
								<td class="width-15 active">
									<label class="pull-right"><font color="red">*</font>设备数量:</label>
								</td>
								<td class="width-15 active">
									<input class="form-control required" id="sum" name="sum" type="text" value="${ShopComEquipment.sum }" style="width: 300px"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;个
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>