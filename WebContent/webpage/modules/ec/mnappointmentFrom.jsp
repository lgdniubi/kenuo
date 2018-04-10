<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>预约详情</title>
	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
    <script type="text/javascript">
    function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
    	var verifyForm = "true";

		if($("#apptStatus").val() == 0){	// 未修改预约状态时,
			top.layer.alert('预约状态不可修改为等待服务', {icon: 0, title:'提醒'});
    		return false;
		}
		if($("#apptStatus").val() == 1){	// 当预约状态修改为已完成时需校验美容师当前时间在当前店铺是否存在预约
			/* 日期控件做了禁选操作,由于用户可以不修改预约日期直接提交数据,在提交前需人为校验 */
			var date = new Date().getTime() - new Date(Date.parse($("#apptDate").val())).getTime();   //时间差的毫秒数  当前时间-预约日期
			var day=Math.floor(date/(24*3600*1000));//计算出相差天数
	      	if(day < 0 || day > 7){
	      		verifyForm = "false";
	      		top.layer.alert('预约时间必须为七天内', {icon: 0, title:'提醒'});
	      	}
	      	if(verifyForm == "true"){
				$.ajax({
					type : "POST",   
					url : "${ctx}/ec/mtmyMnappointment/verifyApptDate",
					data: $('#inputForm').serialize(), 
					async:false,
					success: function(data) {
						if("ERROR" == data.FLAG){
							verifyForm = "false";
							top.layer.alert(data.MESSAGE, {icon: 0, title:'提醒'}); 
						}
					}
				}); 
			}
		}
		if(verifyForm == "true"){
			if(validateForm.form()){
    			loading("正在提交，请稍候...");
        		$("#inputForm").submit();
    	    	return true;
	    	}
		}
		return false;
    } 
   	$(document).ready(function(){
   		changeStatus($("#oldStatus").val());
   		$("#apptStatus").val($("#oldStatus").val());
   		validateForm = $("#inputForm").validate({
   			rules: {
   				apptStartTime:{
					isStartTime:true
				},
				apptEndTime:{
					isEndTime:true
				}
			},
			messages:{
				apptStartTime:{
					isStartTime:"输入正确时间(整点/半点/小于结束时间)"
				},
				apptEndTime:{
					isEndTime:"输入合法时间(整点/半点/大于开始时间)"
				}
			},
			submitHandler: function(form){
				loading('正在提交，请稍等...');
				form.submit();
			}
   		});
   	})
   // 服务开始时间验证
	jQuery.validator.addMethod("isStartTime", function(value, element) {
	    var mobile = /^[0-2][0-9][:][0,3][0]$/;       
	    return this.optional(element) || (value < $("#apptEndTime").val() && mobile.test(value));
	}, "请正确填写开始时间");
 	// 服务结束时间验证
	jQuery.validator.addMethod("isEndTime", function(value, element) {
	    var mobile = /^[0-2][0-9][:][0,3][0]$/;     
	    return this.optional(element) || (value > $("#apptStartTime").val() && mobile.test(value));
	}, "请正确填写结束时间");
   	function changeStatus(str){
   		if(1 == str){
   			$("#isNo1,#isNo2").show();
   			$("#isYes").hide();
   		}else{
   			$("#isNo1,#isNo2").hide();
   			$("#isYes").show();
   		}
   	}
   	
    function checkEnter(e){
	    var code;  
	    if (!e) var  e = window.event;  
	    if (e.keyCode) code = e.keyCode;  
	    else if (e.which) code = e.which;  
	    if(code==13 && window.event){  
	        e.returnValue = false;  
	        top.layer.alert('禁止换行!', {icon: 0, title:'提醒'}); 
	    }else if(code==13){  
	        e.preventDefault();
	        top.layer.alert('禁止换行!', {icon: 0, title:'提醒'}); 
	    }  
    }
    </script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
	    	<div class="ibox-content">
				<form:form id="inputForm" modelAttribute="reservation" action="${ctx }/ec/mtmyMnappointment/updateMnappointment">
					<input type="hidden" value="${reservation.reservationId }" id="reservationId" name="reservationId"><!-- 预约id -->
					<input type="hidden" value="${reservation.apptStatus}" id="oldStatus" name="oldStatus"><!-- 修改前预约状态 -->
					<input type="hidden" value="<fmt:formatDate value="${reservation.apptDate}" pattern="yyyy-MM-dd"/>" id="oldApptDate" name="oldApptDate"><!-- 修改前预约时间 -->
					<input type="hidden" value="${reservation.beauticianId}" id=beauticianId name=beauticianId><!-- 预约美容师id -->
					<input type="hidden" value="${reservation.shopId}" id="shopId" name="shopId"><!-- 预约店铺id -->
					<input type="hidden" value="${reservation.userId}" id="userId" name="userId"><!-- 预约用户id -->
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<tr>
							<td width="100px"><label class="pull-right">店铺：</label></td>
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
							<td><label class="pull-right">渠道来源：</label></td>
							<td>
								<input type="text" class="form-control" value="${reservation.channelFlag }" readonly="readonly">
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>服务状态：</label></td>
							<td>
								<select class="form-control" id="apptStatus" name="apptStatus" onchange="changeStatus(this.value)">
									<c:forEach items="${fns:getDictList('reservation_status')}" var="reservation_status">
										<c:choose>
											<c:when test="${reservation_status.label == '已评价'}">
												<option value="${reservation_status.value }" disabled="true">${reservation_status.label }</option>
											</c:when>
											<c:otherwise>
												<option value="${reservation_status.value }">${reservation_status.label }</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr id="isYes">
							<td><label class="pull-right">预约时间：</label></td>
							<td>
								<fmt:formatDate value="${reservation.apptDate }" pattern="yyyy-MM-dd"/>&nbsp;${reservation.apptStartTime }
								<label>&nbsp;&nbsp;--&nbsp;&nbsp;</label>
								<fmt:formatDate value="${reservation.apptDate }" pattern="yyyy-MM-dd"/>&nbsp;${reservation.apptEndTime }
							</td>
						</tr>
						<tr id="isNo1">
							<td><label class="pull-right"><font color="red">*</font>预约日期：</label></td>
							<td>
								<input id="apptDate" name="apptDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text"
									value="<fmt:formatDate value="${reservation.apptDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-{%d-7}',maxDate:'%y-%M-%d'})" readonly="readonly"/>
							</td>
						</tr>
						<tr id="isNo2">
							<td><label class="pull-right"><font color="red">*</font>预约时间：</label></td>
							<td>
								<input id="apptStartTime" name="apptStartTime" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text"
									value="${reservation.apptStartTime}" onclick="WdatePicker({dateFmt:'HH:mm',maxDate:'#F{$dp.$D(\'apptEndTime\')}'})" readonly="readonly"/>
								<label>&nbsp;&nbsp;--&nbsp;&nbsp;</label>	
								<input id="apptEndTime" name="apptEndTime" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text"
									value="${reservation.apptEndTime}" onclick="WdatePicker({dateFmt:'HH:mm',minDate:'#F{$dp.$D(\'apptStartTime\')}'})" readonly="readonly"/>						
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">消费者备注：</label></td>
							<td>
								<textarea rows="7" cols="30" id="userNote" name="userNote" class="form-control" readonly="readonly">${reservation.userNote }</textarea>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>管理员备注：</label></td>
							<td>
								<textarea rows="7" cols="30" id="remarks" name="remarks" onkeydown="checkEnter(event)" class="form-control required">${reservation.remarks }</textarea>
							</td>
						</tr>
					</table>
				</form:form>
				<shiro:hasPermission name="ec:mtmyMnappointment:editLog">
					<a href="#" onclick="openDialogView('预约操作日志', '${ctx}/ec/mtmyMnappointment/findReservationLog?reservationId=${reservation.reservationId }','800px','650px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>预约操作日志</a>
				</shiro:hasPermission>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>