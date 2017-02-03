<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>免费订单备注信息</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<script type="text/javascript">
	var validateForm;
	function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if($("#contents").val() != ""){
			loading('正在提交，请稍等...');
			$("#inputForm").submit();
			return true;
		}else{
			top.layer.alert('备注不能为空!', {icon: 0, title:'提醒'}); 
		}
	}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="public-heade">
			<div class="ibox">
				<div class="ibox-title">
					<h5>免费订单备注信息</h5>
				</div>
				<div class="ibox-content">
					<c:forEach items="${remark }" var="remark">
						<div class="comment_areas clearfix">
						    <h4>备注人：${remark.user.name }&nbsp;&nbsp;&nbsp;&nbsp;备注时间：<fmt:formatDate value="${remark.addTime }" pattern="yyyy-MM-dd HH:mm:ss"/></h4>
						    <p>${remark.contents }</p>
						</div>
					</c:forEach>
					<form action="${ctx }/ec/freeorder/saveRemark" id="inputForm">
						<input type="hidden" value="${divId }" name="divId">
						<textarea id="contents" name="contents" class="form-control" rows="4" style="width:100%;margin-top: 10px"></textarea>
					</form>
				</div>
			</div>
		</div>
	</div>
	</body>
</html>
