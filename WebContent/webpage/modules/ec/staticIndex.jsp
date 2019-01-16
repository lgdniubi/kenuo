<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/common/training.js"></script>

<script type="text/javascript">
	function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		 loading('正在提交，请稍等...');
		$("#inputForm").submit();
    	  return true;
	}
</script>
<head>
<title>静态主页预览</title>
</head>
<body>
	<form:form id="inputForm" action="${ctx}/ec/staticIndex/publish" method="post">
		<c:import url="${goal}" charEncoding="utf-8"></c:import>
	</form:form>
</body>
</html>