<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>编辑实际服务时长</title>
	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
    <script type="text/javascript">
    function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
    	if(validateForm.form()){
       		$("#inputForm").submit();
   	    	return true; 
    	}
    	return false;
    } 
    $(document).ready(function(){
    	validateForm = $("#inputForm").validate({
			submitHandler: function(form){
				loading('正在提交，请稍等...');
				form.submit();
			},
			errorContainer: "#messageBox",
			errorPlacement: function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
   		});
   	})
    </script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
	    	<div class="ibox-content">
				<form:form id="inputForm" modelAttribute="reservation" action="${ctx }/ec/mtmyMnappointment/editServiceTime">
					<input type="hidden" value="${reservation.reservationId }" id="reservationId" name="reservationId"><!-- 预约id -->
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<tr>
							<td><label class="pull-right">选择服务时间：</label></td>
							<td>
								<input id="serviceStartTime" name="serviceStartTime" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" 
									value="<fmt:formatDate value="${reservation.serviceStartTime}" pattern="yyyy-MM-dd HH:mm"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'serviceEndTime\')}'})" readonly="readonly"/>
								<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
								<input id="serviceEndTime" name="serviceEndTime" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" 
									value="<fmt:formatDate value="${reservation.serviceEndTime}" pattern="yyyy-MM-dd HH:mm"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'serviceStartTime\')}'})" readonly="readonly"/>
							</td>
						</tr>
					</table>
				</form:form>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>