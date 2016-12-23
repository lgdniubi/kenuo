<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>预约详情</title>
	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
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
    	validateForm = $("#inputForm").validate({
			rules: {
				reservationStatus: "required",
				remarks: "required"
			},
			messages: {
				reservationStatus: "请输入正确预约状态！",
				remarks: "备注不可为空！"
			},
		});
	}); 
    </script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
	    	<div class="ibox-content">
				<form:form id="inputForm" modelAttribute="reservation" action="${ctx }/ec/mtmyMnappointment/updateMnappointment">
					<input type="hidden" value="${reservation.reservationId }" id="reservationId" name="reservationId"><!-- 预约id -->
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<tr>
							<td><label class="pull-right">预约时间：</label></td>
							<td>
								<fmt:formatDate value="${reservation.beginTime }" pattern="yyyy-MM-dd HH:mm"/>
								<label>&nbsp;&nbsp;--&nbsp;&nbsp;</label>
								<fmt:formatDate  value="${reservation.endTime }" pattern="yyyy-MM-dd HH:mm"/>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">店铺：</label></td>
							<td>
								<input type="text" class="form-control" value="${reservation.office.name }" readonly="readonly">
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">美容师：</label></td>
							<td>
								<input type="text" class="form-control" value="${reservation.user.name }" readonly="readonly">
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>服务状态：</label></td>
							<td>
									<form:select path="reservationStatus" class="form-control" id="eservationStatus">
										<form:option value="" label="服务状态"/>
										<c:forEach items="${fns:getDictList('reservation_status')}" var="reservation_status">
											<c:choose>
												<c:when test="${reservation_status.label == '已取消' or reservation_status.label == '已评价'}">
													<form:option value="${reservation_status.value }" label="${reservation_status.label }" disabled="true"/>
												</c:when>
												<c:otherwise>
													<form:option value="${reservation_status.value }" label="${reservation_status.label }"/>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</form:select>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>备注：</label></td>
							<td>
								<form:textarea path="remarks" cols="30" rows="7"/>
							</td>
						</tr>
					</table>
					<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
						<thead>
							 <tr>
							 	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;">
							 		<a href="#" onclick="openDialogView('查看日志', '${ctx}/ec/mtmyMnappointment/findListLog?reservationId=${reservation.reservationId }','700px', '550px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i>日志详情</a>
							 	</td>
					  			<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="2"><label>操作日志</label></td>
					  		</tr>
							<tr>
								<th style="text-align: center;width:150px;" class="active">操作者</th>
							    <th style="text-align: center;width:100px;" class="active">操作时间</th>
							    <th style="text-align: center;" class="active">相关内容</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${reservationLog }" var="reservationLog">
								<tr>
								  	<td align="center">${reservationLog.createBy.name }</td>
								  	<td align="center"><fmt:formatDate value="${reservationLog.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								  	<td>
								  		店铺名:${reservationLog.officeName }|美容师:${reservationLog.userName }|服务状态:${fns:getDictLabel(reservation.reservationStatus, 'reservation_status', '')}<br>
								  		备注：${reservationLog.remarks }
								  	</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</form:form>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>