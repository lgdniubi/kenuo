<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>修改排序</title>
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
							<td><label class="pull-right"><font color="red">*</font>排序：</label></td>
							<td>
								<input class="form-control required" id="sort" name="sort" type="text" value="${sort}" style="width: 300px" onkeyup="this.value=this.value.replace(/[^\d]/g,'')"  onfocus="if(value == '0'){value=''}" onblur="if(value == ''){value='0'}"/>
							</td>
						</tr>
					</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>