<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>开启红包</title>
	<meta name="decorator" content="default"/>
	<!-- 内容上传 引用-->
<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			 	 var begtime=$("#getBegintime").val();
				var endtime=$("#getEndtime").val();
				var expirationDate=$("#expirationDate").val();
				if(endtime<begtime){
					top.layer.alert('领取结束日期要小于开始日期!', {icon: 0, title:'提醒'}); 
					  return;
				}
				if(expirationDate<endtime){
					top.layer.alert('有效时间要大于领取结束日期!', {icon: 0, title:'提醒'}); 
					  return;
				}
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		
	
		$(document).ready(function(){
	
			var start = {
				    elem: '#getBegintime',
				    format: 'YYYY-MM-DD hh:mm:ss',
				    event: 'focus',
				    max: $("#getEndtime").val(),   //最大日期
				  //  min:'2016-08-31',
				    istime: true,				//是否显示时间
				    isclear: false,				//是否显示清除
				    istoday: false,				//是否显示今天
				    issure: true,				//是否显示确定
				    festival: true,				//是否显示节日
				    choose: function(datas){
				    	var time=datas.substr(0,13); 
				         end.min = time; 		//开始日选好后，重置结束日的最小日期
				         end.start = time; 		//将结束日的初始值设定为开始日
				    }
				};
			var end = {
				    elem: '#getEndtime',
				    format: 'YYYY-MM-DD hh:mm:ss',
				    event: 'focus',
				    min: $("#getBegintime").val(),
				    max:$("#expirationDate").val(),
				    istime: true,
				    isclear: false,
				    istoday: false,
				    issure: true,
				    festival: true,
				    choose: function(datas){
				    	var time=datas.substr(0,13); 
				        start.max = time; //结束日选好后，重置开始日的最大日期
				    }
				};
				laydate(start);
				laydate(end);
			
			
			validateForm = $("#inputForm").validate({
				rules: {
			
				},
				messages: {
				
					
				},
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
		
			//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
			//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
			//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
			//$("#inputForm").validate().element($("#reason"));
			
// 			laydate({
// 	            elem: '#userinfo.birthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
// 	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
// 	        });
			
			
		});

		
</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="coupon" action="${ctx}/ec/coupon/updateTime" method="post" class="form-horizontal">
		
		<div class="form-group" align="center" style="padding-top:20px">
			<form:hidden path="couponId"/>
			 <label>领取时间：</label>
		  	<input id="getBegintime" name="getBegintime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
			value="<fmt:formatDate value="${coupon.getBegintime}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width:185px;" placeholder="开始时间" readonly="readonly"/> 	 
		    一
			<input id="getEndtime" name="getEndtime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" 
			value="<fmt:formatDate value="${coupon.getEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"  style="width:185px;" placeholder="结束时间" readonly="readonly"/>&nbsp;&nbsp;
			<p></p>
			<input id="expirationDate" name="expirationDate" type="hidden" maxlength="20" class="laydate-icon form-control layer-date input-sm"
			value="<fmt:formatDate value="${coupon.expirationDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width:185px;" placeholder="开始时间" readonly="readonly"/>
		</div>
	</form:form>
</body>
</html>