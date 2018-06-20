<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>创建活动</title>
<meta name="decorator" content="default" />
<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
<!-- 内容上传 引用-->
<style type="text/css">
#one {
	width: 200px;
	height: 180px;
	float: left
}

#two {
	width: 50px;
	height: 180px;
	float: left
}

#three {
	width: 200px;
	height: 180px;
	float: left
}

.fabtn {
	width: 50px;
	height: 30px;
	margin-top: 10px;
	cursor: pointer;
}
</style>

<script type="text/javascript">
	var validateForm;
	
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			
			if($("#franchiseeId").val() == '0' || $("#franchiseeId").val() == '' || $("#franchiseeId").val() == null){
				top.layer.alert('归属商家必选！', {icon: 0, title:'提醒'}); 
				return;
			}
			loading("正在提交，请稍候...");
			$("#inputForm").submit();
			
			return true;
		}

		return false;
	}

	$(document).ready(function() {
		
				var start = {
					elem : '#startTime',
					format : 'YYYY-MM-DD hh:mm:ss',
					event : 'focus',
					max : $("#endTime").val(), //最大日期
					min :laydate.now(new Date().toLocaleString(),"YYYY-MM-DD hh"),
					istime : true, //是否显示时间
					isclear : false, //是否显示清除
					istoday : false, //是否显示今天
					issure : true, //是否显示确定
					festival : true, //是否显示节日
					choose : function(datas) {
						var time=datas.substr(0,13); 
						end.min = time; //开始日选好后，重置结束日的最小日期
						end.start = time; //将结束日的初始值设定为开始日
					}
				};
				var end = {
					elem : '#endTime',
					format : 'YYYY-MM-DD hh:mm:ss',
					event : 'focus',
					min : $("#startTime").val(),
					istime : true,
					isclear : false,
					istoday : false,
					issure : true,
					festival : true,
					choose : function(datas) {
						var time=datas.substr(0,13); 
						start.max = time; //结束日选好后，重置开始日的最大日期
						actiontime.min = time;
					}
				};
				var actiontime = {
						elem : '#expirationDate',
						format : 'YYYY-MM-DD hh:mm:ss',
						event : 'focus',
						min : $("#endTime").val(),
						istime : true,
						isclear : false,
						istoday : false,
						issure : true,
						festival : true,
						choose : function(datas) {
							var time=datas.substr(0,13); 
							end.max=time;
							//actiontime.max = datas; //结束日选好后，重置开始日的最大日期
						}
					};
				laydate(start);
				laydate(end);
				laydate(actiontime);
				
				validateForm = $("#inputForm").validate({
					rules : {
						

									},
						messages : {
							

							},

							submitHandler : function(form) {
							//	loading('正在提交，请稍等...');
								form.submit();
							},
							errorContainer : "#messageBox",
							errorPlacement : function(error, element) {
								$("#messageBox").text("输入有误，请先更正。");
							if (element.is(":checkbox")|| element.is(":radio")|| element.parent().is(".input-append")) {
									error.appendTo(element.parent().parent());
							} else {
								error.insertAfter(element);
						}
					}
				});

				//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
				//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
				//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
				//$("#inputForm").validate().element($("#reason"));

// 				laydate({
// 					elem : '#expirationDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
// 					event : 'focus' //响应事件。如果没有传入event，则按照默认的click
// 				});

				if("${activity.id}" != null && "${activity.id}" != ""){
					typeChange("${activity.expirationType}");
				}
			});
			
	function typeChange(value){
		if(value == '0'){
			$("#rad0").attr("checked",true);
			$("#expirationType").val(0);
			$("#expirationDay").val("");
			$("#expirationDay").attr("disabled",true);
			$("#expirationDate").attr("disabled",false);
		}else{
			$("#rad1").attr("checked",true);
			$("#expirationType").val(1);
			$("#expirationDate").val("");
			$("#expirationDate").attr("disabled",true);
			$("#expirationDay").attr("disabled",false);
		}
	}
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="inputForm" modelAttribute="activity" action="${ctx}/ec/activity/save" method="post" class="form-horizontal">
					<form:hidden path="id"/>
					<form:hidden path="expirationType"/>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>活动名称：</label></td>
								<td>
									<form:input path="name" htmlEscape="false" maxlength="200" style="width:200px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>所属商家：</label></td>
								<td>
									<form:select path="franchiseeId" class="form-control" style="width:200px;">
										<form:option value="0">请选择商家</form:option>
										<c:forEach items="${franList}" var="list" varStatus="status">
												<form:option value="${list.id}">${list.name}</form:option>
										</c:forEach>
									</form:select>
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>活动类型：</label></td>
								<td><form:select path="actionType"  class="form-control" style="width:200px;" >
										<form:option value="1">营销红包</form:option>
										<form:option value="2">生日红包</form:option>
										<form:option value="3">内部红包</form:option>
										<form:option value="4">团购红包</form:option>
										<form:option value="5">其它</form:option>
									</form:select></td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>领取时间：</label></td>
								<td><input id="startTime" name="startTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required"
									value="<fmt:formatDate value="${activity.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width: 200px;" placeholder="领取时间" readonly="readonly" /> </td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>截止时间：</label></td>
								<td>
									<input id="endTime" name="endTime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm required"
							value="<fmt:formatDate value="${activity.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width: 200px;" placeholder="截止时间" readonly="readonly" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>有效期：</label></td>
								<td colspan="2"><input type="radio" onchange="typeChange('0')" id="rad0" name="exTime">日期范围<input id="expirationDate" name="expirationDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required"
									value="<fmt:formatDate value="${activity.expirationDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width:200px;" placeholder="请选择日期" readonly="readonly" />
									<p></p>
									<br><input type="radio" onchange="typeChange('1')" id="rad1" name="exTime">固定天数<form:input path="expirationDay" htmlEscape="false" style="width:190px;" class="form-control required" placeholder="领取后到期天数"/>天
								</td>
							</tr>
						</table>
					
					</form:form>
				</div>
				
			</div>
		</div>
	</div>
	 <div class="loading"></div>
</body>
</html>