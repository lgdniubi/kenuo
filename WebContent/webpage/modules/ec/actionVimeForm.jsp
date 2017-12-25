<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>创建活动</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
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
			
			var startTime=$("#startTime").val();
			var endTime=$("#endTime").val();
			var showTime=$("#showTime").val();
			var closeTime=$("#closeTime").val();
			var ceiling=$("#ceiling").val()
			
			if(endTime<startTime){
				top.layer.alert('结束时间要大于抢购时间!', {icon: 0, title:'提醒'}); 
				 return;
			}
			if(showTime>startTime){
				top.layer.alert('开启时间要小于抢购日期!', {icon: 0, title:'提醒'}); 
				 return;
			}
			if(closeTime<endTime){
				top.layer.alert('关闭时间要大于结束时间!', {icon: 0, title:'提醒'}); 
				  return;
			}
			var r = /^[1-9]+[0-9]*]*$/;
			if(!r.test(ceiling)){
				top.layer.alert('抢购数量输入正整数!', {icon: 0, title:'提醒'}); 
				  return;
			}
			if(ceiling<=0){
				top.layer.alert('抢购数量必须大于0!', {icon: 0, title:'提醒'}); 
				  return;
			}
			$("#inputForm").submit();
			return true;
		}

		return false;
	}


	$(document).ready(function() {

			/* var start = {
				    elem: '#startTime',
				    format: 'YYYY-MM-DD hh:mm:ss',
				    event: 'focus',
				    max: $("#endTime").val(),   //最大日期
				    istime: true,				//是否显示时间
				    isclear: true,				//是否显示清除
				    istoday: true,				//是否显示今天
				    issure: true,				//是否显示确定
				    festival: true,				//是否显示节日
				    choose: function(datas){
				         end.min = datas.substr(0,13); 		//开始日选好后，重置结束日的最小日期
				         end.start = datas.substr(0,13);		//将结束日的初始值设定为开始日
				         show.max=datas.substr(0,13);
				       
				    }
				};
			var end = {
				    elem: '#endTime',
				    format: 'YYYY-MM-DD hh:mm:ss',
				    event: 'focus',
				    min: $("#startTime").val(),
				    istime: true,
				    isclear: true,
				    istoday: true,
				    issure: true,
				    festival: true,
				    choose: function(datas){
				        start.max = datas.substr(0,13); //结束日选好后，重置开始日的最大日期
				        close.min=datas.substr(0,13);
				      
				    }
				};
			var show = {
				    elem: '#showTime',
				    format: 'YYYY-MM-DD hh:mm:ss',
				    event: 'focus',
				    max: $("#startTime").val(),   //最大日期
				    istime: true,				//是否显示时间
				    isclear: true,				//是否显示清除
				    istoday: true,				//是否显示今天
				    issure: true,				//是否显示确定
				    festival: true,				//是否显示节日
				    choose: function(datas){
				    	start.min = datas.substr(0,13);		//开始日选好后，重置结束日的最小日期
				    	start.start = datas.substr(0,13); 		//将结束日的初始值设定为开始日
				    }
				};
			var close = {
				    elem: '#closeTime',
				    format: 'YYYY-MM-DD hh:mm:ss',
				    event: 'focus',
				    min: $("#endTime").val(),
				    istime: true,
				    isclear: true,
				    istoday: true,
				    issure: true,
				    festival: true,
				    choose: function(datas){
				        end.max = datas.substr(0,13); //结束日选好后，重置开始日的最大日期
				    }
				};
				laydate(start);
				laydate(end);
				laydate(show);
				laydate(close); */
	       

				
				validateForm = $("#inputForm").validate({
					rules : {
						

									},
						messages : {
							

							},

							submitHandler : function(form) {
								loading('正在提交，请稍等...');
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

			});
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>创建抢购活动</h5>
			</div>
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="inputForm" modelAttribute="actionInfo" action="${ctx}/ec/action/save" method="post" class="form-horizontal">
						<form:hidden path="actionId"/>
						<label>活动名称：</label>
						<form:input path="actionName" htmlEscape="false" maxlength="50" style="width:200px;" class="form-control required" /><font color="red">*</font>
						<label>名称短标题：</label>
						<form:input path="actionTitle" htmlEscape="false" maxlength="50" style="width:200px;" class="form-control required" /><font color="red">*</font>
						<%-- <p></p>
						<label>抢购时间：</label>
						<input id="startTime" name="startTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required"
							value="<fmt:formatDate value="${actionInfo.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width: 200px;"  readonly="readonly" /><font color="red">*</font>
						<label>结束时间：&nbsp;</label>
						<input id="endTime" name="endTime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm required"
							value="<fmt:formatDate value="${actionInfo.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width: 200px;" readonly="readonly" /><font color="red">*</font>&nbsp;&nbsp;
						<p></p>
						<label>开启时间：</label>
						<input id="showTime" name="showTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required"
							value="<fmt:formatDate value="${actionInfo.showTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width: 200px;"  readonly="readonly" /><font color="red">*</font>
						<label>关闭时间：&nbsp;</label>
						<input id="closeTime" name="closeTime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm required"
							value="<fmt:formatDate value="${actionInfo.closeTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width: 200px;" readonly="readonly" /><font color="red">*</font>&nbsp;&nbsp; --%>
						<p></p>
						<label>抢购时间：</label>
						<input id="startTime" name="startTime" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" value="<fmt:formatDate value="${actionInfo.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}',minDate:'#F{$dp.$D(\'showTime\')}'})"/><font color="red">*</font> 
						<label>结束时间：&nbsp;</label>
						<input id="endTime" name="endTime" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" value="<fmt:formatDate value="${actionInfo.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}',maxDate:'#F{$dp.$D(\'closeTime\')}'})"/><font color="red">*</font>
						<p></p>
						<label>开启时间：</label>
						<input id="showTime" name="showTime" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" value="<fmt:formatDate value="${actionInfo.showTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'startTime\')}'})"/><font color="red">*</font> 
						<label>关闭时间：&nbsp;</label>
						<input id="closeTime" name="closeTime" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" value="<fmt:formatDate value="${actionInfo.closeTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'endTime\')}'})"/><font color="red">*</font>
						<p></p>
						<label>限购数量：</label>
						<input id="ceiling" name="ceiling" maxlength="10" style="width:200px;" class="form-control" value="${actionInfo.ceiling}"/><font color="red">*</font>
						<label>用户等级：&nbsp;</label>
						<form:select path="level" class="form-control required" style="width:200px;" >
							<form:option value="0">0级以上</form:option>
 							<form:option value="1">1级以上</form:option>
 							<form:option value="2">2级以上</form:option>
							<form:option value="3">3级以上</form:option>
							<form:option value="4">4级以上</form:option>
							<form:option value="5">5级以上</form:option>
							<form:option value="6">6级以上</form:option>
							<form:option value="7">7级以上</form:option>
							<form:option value="8">8级以上</form:option>
							<form:option value="9">9级以上</form:option>
							<form:option value="10">10级以上</form:option>
 						</form:select><font color="red">*</font>
 						<p></p>
 						<label>排&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;序：</label>
						<input id="sort" name="sort" maxlength="10" style="width:200px;" class="form-control" value="${actionInfo.sort}"/><font color="red">*</font>
					</form:form>
				</div>
				
			</div>
			<p></p>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">商品名称</th>
							<th style="text-align: center;">货号</th>
							<th style="text-align: center;">商品分类</th>
							<th style="text-align: center;">成本价</th>
							<th style="text-align: center;">总库存</th>
							<th style="text-align: center;">剩余库存</th>
							<th style="text-align: center;">抢购价</th>
							
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${list}" var="list">
							<tr>
								<td>${list.goodsId}</td>
								<td>${list.goodsName}</td>
								<td>${list.goodsSn}</td>
								<td>${list.goodsCategory.name}</td>
								<td>${list.costPrice}</td>
								<td>${list.totalStore}</td>
								<td>${list.storeCount}</td>
							 	<td>${list.shopPrice}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
		</div>
	</div>
	
</body>
</html>