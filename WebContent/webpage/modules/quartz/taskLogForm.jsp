<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="default"/>
</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
				<div class="ibox-content">
				<div class="clearfix">
				 <div class="pull-left">
				 	<form:form id="tasklog" modelAttribute="tasklog" class="form-inline">
						<div class=" pull-right">
							<label >${tasklog.exceptionMsg}</label>
						</div>
					</form:form>
				 </div>
				</div>				
			</div>
		</div>
	</div>
	
</body>
</html>