<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>创建发票</title>
<meta name="decorator" content="default" />
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
			if($("#orderId").val().length > 0){
				$("#inputForm").submit();
				return true;
			}else{
				top.layer.alert('未选择可开发票订单！', {icon: 0, title:'提醒'}); 
				return false;
			}
		}
		return false;
	}
	function selectUser(obj){
		var mobile = $(obj).val();
		if(mobile == ""){
			top.layer.alert('手机号码不能为空！', {icon: 0, title:'提醒'}); 
			return;
		}
		$.ajax({
			type:"get",
			dataType:"json",
			data:{
				mobile:mobile
			},
			url:"${ctx}/ec/orders/getUser",
			success:function(date){
				$("#userName").val(date.users.nickname);
				$("#userId").val(date.users.userid);
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
			}
		});
	}
	function selectType(o){
		var type=$("#invoiceType").val();
		if(type==1){
			$("#compay").hide();
			$("#headContent").val("个人");
		}else if(type==2){
			$("#compay").hide();
			$("#headContent").val("");
		}else if(type==3){
			$("#compay").show();
			$("#headContent").val("");
		}
	}
	$(document).ready(function() {		
		// 手机号码验证
		jQuery.validator.addMethod("isMobile", function(value, element) {
		    var length = value.length;
		    var mobile =  /^(133[0-9]{8})|(153[0-9]{8})|(180[0-9]{8})|(181[0-9]{8})|(189[0-9]{8})|(177[0-9]{8})|(130[0-9]{8})|(131[0-9]{8})|(132[0-9]{8})|(155[0-9]{8})|(156[0-9]{8})|(185[0-9]{8})|(186[0-9]{8})|(145[0-9]{8})|(170[0-9]{8})|(176[0-9]{8})|(134[0-9]{8})|(135[0-9]{8})|(136[0-9]{8})|(137[0-9]{8})|(138[0-9]{8})|(139[0-9]{8})|(150[0-9]{8})|(151[0-9]{8})|(152[0-9]{8})|(157[0-9]{8})|(158[0-9]{8})|(159[0-9]{8})|(182[0-9]{8})|(183[0-9]{8})|(184[0-9]{8})|(187[0-9]{8})|(188[0-9]{8})|(147[0-9]{8})|(178[0-9]{8})|(149[0-9]{8})|(173[0-9]{8})$/;           
		    return this.optional(element) || (length == 11 && mobile.test(value));
		}, "请正确填写您的手机号码");
				
		validateForm = $("#inputForm").validate({
			rules : {
					mobile:{
						digits:true,
						minlength:11,
						isMobile : true,
						remote: "${ctx}/ec/mtmyuser/verifyPhone"
						}

					},
			messages : {
					mobile:{
						digits:"输入合法手机号",
						minlength:"手机号码要11位",
						isMobile :"请输入正确手机号",
						remote:"手机号不存在，不可创建发票"
						}

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
	});
	// 选择可开发票订单
	function findOrderInvoiceList(){
		var userId = $("#userId").val();
		if(userId.length > 0){
			top.layer.open({
			    type: 2, 
			    area: ['800px', '650px'],
			    title:"选择订单",
			    content: "${ctx}/ec/invoice/findOrderInvoiceList?userId="+userId,
			    btn: ['确定', '关闭'],
			    yes: function(index, layero){
			    	var obj = layero.find("iframe")[0].contentWindow;
			    	if(obj.document.querySelectorAll("input[type=checkbox]:checked").length > 0){
			    		$("#invoice").empty();
			    		$("#orderId").val("");
			    		var s = "";
						$(obj.document.querySelectorAll("input[type=checkbox]:checked")).each(function(){ 
							s = s + $(this).val() + ",";
						}); 
						s = s.substring(0,s.length-1);	// 截取最后一位 ， 
						$("#invoice").append(s);
			    		$("#orderId").val(s);
						top.layer.close(index);
			    	}else{
			    		top.layer.alert('未选择可开发票订单！', {icon: 0, title:'提醒'}); 
			    	}
			    }
			})
		}else{
			top.layer.alert('请核实用户信息！', {icon: 0, title:'提醒'}); 
		}
	}
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="inputForm" modelAttribute="orderInvoice" action="${ctx}/ec/invoice/save" method="post" class="form-horizontal">
						<label><font color="red">*</font>手机号码：</label>
						<form:input path="mobile" htmlEscape="false" maxlength="11" class="form-control required" style="width:200px" onchange="selectUser(this)"/>&nbsp;&nbsp;
						<label><font color="red">*</font>用户名：</label>
						<input id="userName" name="userName" type="text" class="form-control" style="width:200px;" readonly="readonly" />
						<input type="hidden" name="userId" id="userId" />
						<p></p>
						<div class="pull-left">
							<label style="padding:10px 0px 0px 0px;"><font color="red">*</font>选择订单：</label>
						</div>
						<div style="width:400px">
	 						<a href="#" onclick="findOrderInvoiceList()" class="btn btn-white btn-sm"><i class="fa fa-plus"></i>选择可开发票订单</a>
						</div>
						<p></p>
						<label><font color="red">*</font>已选开票订单：</label>
						<span id="invoice"></span><input id="orderId" name="orderId" class="form-control" type="hidden" value="" >
						<p></p>
						<label><font color="red">*</font>发票类型：</label>
						<form:select path="invoiceType"  class="form-control" style="width:185px;" onchange="selectType(this)">
							<form:option value="1">个人普票</form:option>
							<form:option value="2">公司普通</form:option>
							<form:option value="3">公司专票</form:option>
						</form:select>
						<p></p>
						<label><font color="red">*</font>发票抬头：</label>
						<form:input path="headContent" htmlEscape="false" maxlength="200" value="个人" class="form-control required" style="width:200px" />
						<p></p>
						<div id="compay" style="border: 1px solid #CCC;padding:10px 20px 20px 10px;display: none">
							<label><font color="red">*</font>纳税人税号：</label>
							<form:input path="taxNum" htmlEscape="false" maxlength="200" class="form-control required" style="width:200px" />
<!-- 							<label><font color="red">*</font>开户人：</label> -->
<%-- 							<form:input path="accountHolder" htmlEscape="false" maxlength="200" class="form-control required" style="width:200px" /> --%>
							<p></p>
							<label><font color="red">*</font>开户银行：</label>
							<form:input path="bankName" htmlEscape="false" maxlength="200" class="form-control required" style="width:200px" />
							<label><font color="red">*</font>电话：</label>
							<form:input path="phone" htmlEscape="false" maxlength="30" class="form-control required" style="width:200px" />
							<p></p>
							<label><font color="red">*</font>银行卡号：</label>
							<form:input path="bankNo" htmlEscape="false" maxlength="200" class="form-control required" style="width:200px" />
							<label><font color="red">*</font>地址：</label>
							<form:input path="address" htmlEscape="false" maxlength="200" class="form-control required" style="width:300px" />
						</div>
						<p></p>
						<label><font color="red">*</font>收货人姓名：</label>
						<form:input path="recipientsName" htmlEscape="false" maxlength="200" class="form-control required" style="width:200px" />
						<label><font color="red">*</font>联系电话：</label>
						<form:input path="recipientsPhone" htmlEscape="false" maxlength="11" class="form-control required" style="width:200px" />
						<p></p>
						<label><font color="red">*</font>收货人地址：</label>
						<form:input path="recipientsAddress" htmlEscape="false" maxlength="200" class="form-control required" style="width:400px" />
					</form:form>
				</div>
				
			</div>
		</div>
	</div>
	
</body>
</html>