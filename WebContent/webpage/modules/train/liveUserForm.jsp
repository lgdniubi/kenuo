<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>添加直播会员</title>
<meta name="decorator" content="default" />


<script type="text/javascript">
	var validateForm;
	
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			
			$("#inputForm").submit();
			
			return true;
		}

		return false;
	}
	//查询用户折扣比例
    function fundName(){
 	   var mobile=$("#mobile").val();
 		
   	   if(mobile==""){
   		   top.layer.alert('请先填写用户手机号!', {icon: 0, title:'提醒'}); 
			return;
   	   }else{
	   		$.ajax({
				 type:"get",
				 dataType:"text",
				 url:"${ctx}/train/liveUser/getUserName?mobile="+mobile,
				 success:function(date){
					//alert(date);
					$("#name").val(date);
				 },
				 error:function(XMLHttpRequest,textStatus,errorThrown){
				    
				 }
				 
			});
 	   
   	   }
	
    }
	$(document).ready(function() {
		
		
		var actiontime = {
				elem : '#validityDate',
				format : 'YYYY-MM-DD hh:mm:ss',
				event : 'focus',
				//min : '',
				istime : true,
				isclear : false,
				istoday : false,
				issure : true,
				festival : true,
				choose : function(datas) {
					var time=datas; 
					//end.max=time;
					//actiontime.max = datas; //结束日选好后，重置开始日的最大日期
				}
			};
		
			laydate(actiontime);
		
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
						remote: "${ctx}/sys/user/verifyPhone"
						
						}

					},
				messages : {
					mobile:{
						digits:"输入合法手机号",
						minlength:"手机号码要11位",
						isMobile :"请输入正确手机号",
						remote:"手机号不存在"
						
						}

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

			});
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="inputForm" modelAttribute="trainLiveUser" action="${ctx}/train/liveUser/save" method="post" class="form-horizontal">
					<form:hidden path="id"/>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>手机号码：</label></td>
								<td>
									<form:input path="mobile" htmlEscape="false" maxlength="11" class="form-control required" style="width:200px" onblur="fundName()"/>
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>会员姓名：</label></td>
								<td>
									<form:input path="name" htmlEscape="false" maxlength="64" readonly="true"  style="width:200px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>直播申请编号：</label></td>
								<td>
									<form:input path="auditId" htmlEscape="false" maxlength="10" style="width:200px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>有效期：</label></td>
								<td>
									<input id="validityDate" name="validityDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required"
									value="<fmt:formatDate value="${trainLiveUser.validityDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width: 200px;" placeholder="有效期" readonly="readonly" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>购买金额：</label></td>
								<td>
									<form:input path="money" htmlEscape="false" maxlength="10" style="width:200px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>支付方式：</label></td>
								<td>
									<form:input path="payment" htmlEscape="false" maxlength="64" style="width:200px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" >备　　　　注：</label></td>
								<td>
									<form:textarea path="remak"  htmlEscape="false" rows="3" maxlength="200" class="form-control" ></form:textarea>
								</td>
							</tr>
						</table>
					</form:form>
				</div>
				
			</div>
		</div>
	</div>
	
</body>
</html>