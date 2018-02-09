<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>套卡订单详情页</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			//  $(".loading").show();
			var oldpice='${orders.goodsprice}';
			var orderamount=$("#orderamount").val();
			if(orderamount<0){
				  top.layer.alert('订单成交价格不能小于0元!', {icon: 0, title:'提醒'}); 
				  return;
			}
			
			 $("#inputForm").submit();
			 return true;
				 
		  }
		
		  return false;
		}
		
		function initStatus(){
			$("#messageBox").show();
			$("#messageBox").css("background-color","#66CCCC");
			var status='${orders.orderstatus}';
			if(status>0){
				$("#orderamount").attr("readonly",true);
			}
			
		}
		
		jQuery.validator.addMethod("lrunlv", function(value, element) {     
		    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
		}, "小数位不能超过三位"); 
		
		$(document).ready(function() {
			/* var suitCardSons = $("#suitCardSons").val();
			$(suitCardSons).appendTo($("#addZTD")); */
			
			/* if($("#isReal").val() == '1'){
				$("#shipping").hide();
			} 
			if($("#shippingtype").val()== 0 || $("#shippingtype").val()== 1){
				$("#logistics").show();
			}else{
				$("#logistics").hide();
			}*/
			if($("#isNeworder").val() == 0){
				$("#beauticianMoney").show();
				$("#shopMoney").show();
			}else{
				$("#beauticianMoney").hide();
				$("#shopMoney").hide();
			}
			//物流类型来判断是否显示物流地址
			$("#shippingtype").change(function(){
				var shippingtype = $(this).val();
				if(shippingtype == 0 || $("#shippingtype").val()== 1){
					$("#logistics").show();
				}else{
					$("#logistics").hide();
				}
			});
			
			$("#orderamount").focus();
			validateForm = $("#inputForm").validate({
				rules:{
					orderamount:{
						number:true,
						min:0,
						lrunlv:true
						},
					shippingcode:{
						digits:true
					},
					postalcode:{
						digits:true	
					}
				},
				messages:{
					orderamount:{
						number:"输入合法的价格",
						min:"成交价最小为0",
						lrunlv:"请输入两位小数"
					},
					shippingcode:{
						digits:"输入合法的订单号"
					},
					postalcode:{
						digits:"输入合法邮编"
					}
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
			/* $("#inputForm").validate().element($("#orderamount")); */
	  	
	    });
			
	    	
		    function TopUp(recid,singleRealityPrice,singleNormPrice,orderArrearage,servicetimes,remaintimes,goodsBalance){
		    	var isCommitted = false;		//表单是否已经提交标识，默认为false
		    	var orderid = $("#orderid").val();
		    	var userid = $("#userid").val();
		    	var isReal = $("#isReal").val();
		    	var channelFlag = $("#channelFlag").val();
				top.layer.open({
				    type: 2, 
				    area: ['600px', '450px'],
				    title:"充值",
				    content: "${ctx}/ec/orders/addCardTopUp?orderid="+orderid+"&singleRealityPrice="+singleRealityPrice+"&userid="+userid+"&isReal="+isReal+"&goodsBalance="+goodsBalance+"&orderArrearage="+orderArrearage,
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){
				    	var orderid = $("#orderid").val();
				        var obj =  layero.find("iframe")[0].contentWindow;
						var accountBalance = obj.document.getElementById("accountBalance").value;
				    	var rechargeAmount = obj.document.getElementById("rechargeAmount").value;
						var topUpTotalAmount = obj.document.getElementById("topUpTotalAmount").value;
						var jsmoney = obj.document.getElementById("jsmoney").value;
						var loading = obj.document.getElementById("loading");
						var belongOfficeId = obj.document.getElementById("belongOfficeId").value;
						$(loading).show();
						if(accountBalance == ''){
							$(loading).hide();
							top.layer.alert('账户余额必填！', {icon: 0, title:'提醒'});
							return;
						}
						if(rechargeAmount == ''){
							$(loading).hide();
							top.layer.alert('充值金额必填！', {icon: 0, title:'提醒'});
							return;
						}
						if(topUpTotalAmount == ''){
							$(loading).hide(); 
							top.layer.alert('总付款必填！', {icon: 0, title:'提醒'});
							return;
						}
						if(jsmoney == 0){
							$(loading).hide();
							top.layer.alert('请计算费用！', {icon: 0, title:'提醒'});
							return;
						}
						if(belongOfficeId == ''){
							$(loading).hide();
							top.layer.alert('归属店铺必填！', {icon: 0, title:'提醒'});
							return;
						}
						
						//查询用户账户余额
						$.ajax({
							type:"post",
							data:{
								userid:userid
							 },
							url:"${ctx}/ec/orders/getAccount",
							success:function(date){
								var totalAmount = topUpTotalAmount/singleRealityPrice;
								var _totalAmount = totalAmount - parseInt(totalAmount);
								if(accountBalance > date){
									$(loading).hide(); 
									top.layer.alert('您输入的余额不能大于当前账户的余额！', {icon: 0, title:'提醒'});
								}else{
									top.layer.close(index);
									
									//防止表单多次提交
								    if(isCommitted == false){
								    	isCommitted = true;		//提交表单后，将表单是否已经提交标识设置为true
								   	}else{
								       	return false;			
									}
									
									$.ajax({
										type:"post",
										data:{
											mtmyUserId:userid,
											orderId:orderid,
											recid:recid,
											singleRealityPrice:singleRealityPrice,
											singleNormPrice:singleNormPrice,
											accountBalance:accountBalance,
											rechargeAmount:rechargeAmount,
											totalAmount:topUpTotalAmount,
											orderArrearage:orderArrearage,
											servicetimes:servicetimes,
											remaintimes:remaintimes,
											isReal:isReal,
											channelFlag:channelFlag,
											belongOfficeId:belongOfficeId,
											mtmyUserId:userid
										 },
										url:"${ctx}/ec/orders/addCardOrderRechargeLog",
										success:function(date){
												top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
												window.location="${ctx}/ec/orders/cardOrdersForm?orderid="+orderid;
										},
										error:function(XMLHttpRequest,textStatus,errorThrown){}
									});
								}
							},
							error:function(XMLHttpRequest,textStatus,errorThrown){}
						});
				},
				cancel: function(index){ //或者使用btn2
					    	           //按钮【按钮二】的回调
				}
			}); 
		}
		    
	//删除tr
     function delFile(obj){
         $(obj).parent().parent().remove();
     }
	
	
	//删除备注信息
     function delOrderRemarks(obj){
		var orderRemarksId = $("#orderRemarksId").val();
		$.ajax({
			type:"post",
			data:{
				orderRemarksId:orderRemarksId
			 },
			url:"${ctx}/ec/orders/deleteOrderRemarksLog",
			success:function(date){
				if(date == 'success'){
					$(obj).parent().parent().remove();
					top.layer.alert('删除备注成功', {icon: 0, title:'提醒'});
				}else{
					top.layer.alert('删除备注失败', {icon: 0, title:'提醒'});
				}
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
				
			}
		});
     }
	
     function editSysUserInfo(turnOverDetailsId){
    	 var orderid = $("#orderid").val();
    	 var isDecided = false;		//表单是否已经提交标识，默认为false
  		// 正常打开	
  		top.layer.open({
  			type: 2, 
  		    area: ['800px', '520px'],
  		    title:"新增/修改营业额：(提示：营业额数字不能修改，但可以插入正负数增减)",
  		    content: "${ctx}/ec/orders/editPushmoneyUser?turnOverDetailsId="+turnOverDetailsId,
  		    btn: ['确定', '关闭']
      	    ,yes: function(index, layero){
      	    	var map = {};
      	    	var obj =  layero.find("iframe")[0].contentWindow;
      	    	var amount = obj.document.getElementById("amount").value; //提成总金额
      	    	var orderId = obj.document.getElementById("orderId").value; //订单id
      	    	var pushmoneyUserId = obj.document.getElementsByName("pushmoneyUserId"); //提成人员id
      	    	var departmentId = obj.document.getElementsByName("departmentId"); //提成人员部门id
      	    	var departmentName = obj.document.getElementsByName("departmentName"); //提成人员部门
      	    	var pushMoney = obj.document.getElementsByName("pushMoney"); //提成金额
      	    	var changeValue = obj.document.getElementsByName("changeValue"); //加减额
      	    	var pushMoneyTotal = obj.document.getElementsByName("pushMoneySum"); //单个营业员该订单的提成总额，下单+充值-退款
      	    	for(i=0;i<pushmoneyUserId.length;i++){
					var pushMoneySum = (parseFloat(pushMoney[i].value)*100 + parseFloat(changeValue[i].value)*100)/100;
					if(pushMoneySum < 0){
						top.layer.alert('单个业务员的营业额不能小于0！', {icon: 0, title:'提醒'});
	 	    			return;
					}
					if(((parseFloat(pushMoneyTotal[i].value)*100 + parseFloat(changeValue[i].value)*100)/100) < 0){
						top.layer.alert('单个业务员的营业额不能小于其在该笔订单中的营业总额！', {icon: 0, title:'提醒'});
	 	    			return;
					}
					map[departmentName[i].value] = departmentId[i].value;
				}     
				
				for(var j in map){
					var sum = 0;
					var result = obj.document.getElementsByClassName(map[j]);
					for(k=0;k<result.length;k++){
						sum = (parseFloat(sum)*100 + parseFloat(result[k].value)*100)/100; 
					}
					if(((parseFloat(sum)*100 - parseFloat(amount)*100)/100) != 0){
      	    			top.layer.alert('每个部门的营业额之和必须等于分享营业额！', {icon: 0, title:'提醒'});
	 	    			return;
      	    		}
      	    	}
				
				//防止表单多次提交
			    if(isDecided == false){
			    	isDecided = true;		//提交表单后，将表单是否已经提交标识设置为true
			   	}else{
			       	return false;			
				}
   	    		$.ajax({
       				type:"post",
       				data:$(obj.document.getElementById("mosaic")).serialize(),
       				url:"${ctx}/ec/orders/savePushMoneyRecord",
       				success:function(date){
       					if(date == 'success'){
       						top.layer.alert('添加业务员成功', {icon: 0, title:'提醒'});
       						window.location="${ctx}/ec/orders/cardOrdersForm?orderid="+orderId+"&type=edit";
       	     				top.layer.close(index);
       					}else{
       						top.layer.alert('添加业务员失败', {icon: 0, title:'提醒'});
       					}
       				},
       				error:function(XMLHttpRequest,textStatus,errorThrown){
       					
       				}
       			});
  			}
  		});  
  	}
     
     function getRemarks(){
    	 var operationName = $("#operationName").val();
 		// 正常打开	
 		top.layer.open({
 		    type: 2, 
 		    area: ['550px', '420px'],
 		    title:"添加订单备注",
 		    ajaxData:{selectIds: $("#goodselectId").val()},
 		    content: "${ctx}/ec/orders/getRemarksView",
 		    btn: ['确定', '关闭']
     	    ,yes: function(index, layero){
     	    	var obj =  layero.find("iframe")[0].contentWindow;
     	    	var remarks = obj.document.getElementById("remark").value;
     	    	var orderid =  $("#orderid").val();
     	    	var isReal =  $("#isReal").val();
     	    	//保存备注信息
     	    	$.ajax({
     				type:"post",
     				data:{
     					orderid:orderid,
     					orderRemark:remarks
     				 },
     				url:"${ctx}/ec/orders/saveOrderRemarksLog",
     				success:function(date){
     					if(date == 'success'){
     						top.layer.alert('添加备注成功', {icon: 0, title:'提醒'});
     						window.location="${ctx}/ec/orders/cardOrdersForm?orderid="+orderid+"&isReal="+isReal+"&type=edit";
     						top.layer.close(index);
     					}else{
     						top.layer.alert('添加备注失败', {icon: 0, title:'提醒'});
     					}
     				},
     				error:function(XMLHttpRequest,textStatus,errorThrown){
     					
     				}
     			});
 				
 			}
 		}); 
 	}
    
	function getNowFormatDate() {
	    var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	            + " " + date.getHours() + seperator2 + date.getMinutes()
	            + seperator2 + date.getSeconds();
	    return currentdate;
	}
window.onload=initStatus;

	function ToAdvance(officeId,recid,servicetimes,orderArrearage){
		var userid = $("#userid").val();
		var orderid = $("#orderid").val();
		var isReal = $("#isReal").val();
		var channelFlag = $("#channelFlag").val();
		
		if(officeId == "" || officeId == null){
			top.layer.alert('该用户未绑定店铺,请在CRM中为该用户绑定！', {icon: 0, title:'提醒'});
	    	return;
		}
		
		var isHandled = false;		//表单是否已经提交标识，默认为false
		
		top.layer.open({
		    type: 2, 
		    area: ['600px', '450px'],
		    title:"处理预约金",
		    content: "${ctx}/ec/orders/handleCardAdvanceForm?recid="+recid+"&userid="+userid+"&servicetimes="+servicetimes+"&orderArrearage="+orderArrearage+"&isreal="+isReal,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		    	var obj =  layero.find("iframe")[0].contentWindow;
     	    	var sum = obj.document.getElementById("sum").value; //是否使用了账户余额
     	    	var loading = obj.document.getElementById("loading");
				$(loading).show();
				
				//防止表单多次提交
			    if(isHandled == false){
			    	isHandled = true;		//提交表单后，将表单是否已经提交标识设置为true
			   	}else{
			       	return false;			
				}
				
				//异步处理预约金
				$.ajax({
					type:"post",
					data:{
						sum:sum,
						recid:recid, 
						userid:userid,
						servicetimes:servicetimes,
						orderArrearage:orderArrearage,
						isReal:isReal,
						channelFlag:channelFlag,
						mtmyUserId:userid
					 },
					url:"${ctx}/ec/orders/handleCardAdvance?recid="+recid+"&userid="+userid+"&orderid="+orderid,
					success:function(date){
						$(loading).hide();
						if(date == 'success'){
							top.layer.alert('处理成功!', {icon: 1, title:'提醒'});
							window.location="${ctx}/ec/orders/cardOrdersForm?orderid="+orderid;
							top.layer.close(index);	
						}else if(date == 'error'){
							top.layer.alert('处理失败!', {icon: 0, title:'提醒'});
							window.location="${ctx}/ec/orders/cardOrdersForm?orderid="+orderid;
							top.layer.close(index);
						}
						
					},
					error:function(XMLHttpRequest,textStatus,errorThrown){}
				});
		},
		cancel: function(index){ //或者使用btn2
			    	           //按钮【按钮二】的回调
		}
	}); 
	}
	
	function viewCardOrders(recid,orderid,isReal){
		if(isReal == 2){  //套卡
			top.layer.open({
			    type: 2, 
			    area: ['800px', '600px'],
			    title:"商品充值查看",
			    content: "${ctx}/ec/orders/suitCardRechargeLog?recid="+recid+"&orderid="+orderid+"&orderType=mapping",
			    btn: ['关闭'],
				cancel: function(index){ //或者使用btn2
				    	           //按钮【按钮二】的回调
				}
			}); 			
		}else{
			top.layer.open({
			    type: 2, 
			    area: ['800px', '600px'],
			    title:"商品充值查看",
			    content: "${ctx}/ec/orders/getMappinfOrderView?recid="+recid+"&orderid="+orderid+"&orderType=mapping",
			    btn: ['关闭'],
				cancel: function(index){ //或者使用btn2
				    	           //按钮【按钮二】的回调
				}
			}); 
		}
		
	}
	
	function getInvoiceRelevancy(orderid){
		top.openTab("${ctx}/ec/invoice/list?orderId="+orderid,"发票列表", false);
		var index = parent.layer.getFrameIndex(window.name);
		top.layer.close(index);
	}
	
	function updateDetails(detailsId,orderId){
    	var isCommitted = false;		//表单是否已经提交标识，默认为false
		top.layer.open({
		    type: 2, 
		    area: ['500px', '350px'],
		    title:"选择归属店铺",
		    content: "${ctx}/ec/orders/addBelongOffice",
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		        var obj =  layero.find("iframe")[0].contentWindow;
		        var loading = obj.document.getElementById("loading");
				var belongOfficeId = obj.document.getElementById("belongOfficeId").value;
				$(loading).show();
				if(belongOfficeId == ''){
					$(loading).hide();
					top.layer.alert('归属店铺必填！', {icon: 0, title:'提醒'});
					return;
				} 
				
				//防止表单多次提交
			    if(isCommitted == false){
			    	isCommitted = true;		//提交表单后，将表单是否已经提交标识设置为true
			   	}else{
			       	return false;			
				}
			    
			    top.layer.close(index);
			    $.ajax({
					type:"post",
					data:{
						turnOverDetailsId:detailsId,
						belongOfficeId:belongOfficeId
					 },
					url:"${ctx}/ec/orders/saveBelongOffice",
					success:function(date){
						console.log(loading);
						top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
						window.location="${ctx}/ec/orders/cardOrdersForm?orderid="+orderId;
					},
					error:function(XMLHttpRequest,textStatus,errorThrown){}
				});
			},
			cancel: function(index){ //或者使用btn2
			    	           //按钮【按钮二】的回调
			}
		}); 
	}
	
	// 强制取消
	function forcedCancel(orderId){
		layer.confirm('确认强制取消订单吗？', {
			btn: ['确定','取消'], //按钮  可以设置多个按钮 具体可参考 http://layer.layui.com/api.html
			icon: 3, 
			title:'系统提示'
		},function(index){	// 点击确认事件
			layer.close(index);	// 关闭弹出框  若不关闭 则可以点击多次确认按钮
			$(".loading").show();
			window.location = "${ctx}/ec/orders/forcedCancel?orderid="+orderId;
	    }, function(){ // 点击取消事件
	    		
	    }); 
	}
</script>
</head>



<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="inputForm" modelAttribute="orders" action="${ctx}/ec/orders/updateCardOrder" method="post" class="form-horizontal">
						<input id="oldAddress" name="oldAddress" type="hidden" value="${orders.address}"/>
						<input id="orderamount" name="orderamount" type="hidden" value="${orders.orderamount}"/>
						<input id="shippingtype" name="shippingtype" type="hidden" value="${orders.shippingtype}"/>
						<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
							<input type="hidden" id="channelFlag" value="${orders.channelFlag}" />
							<input type="hidden" id="isReal" value="${orders.isReal}" />
							<input type="hidden" id="operationName" value="${user.name}" />
							<input type="hidden" id="isNeworder" name="isNeworder" value="${orders.isNeworder}" >
							<h4>订单基本信息:&nbsp;&nbsp;<c:if test="${orders.isReal == 2}">套卡订单</c:if>&nbsp;&nbsp;<c:if test="${orders.isReal == 3}">通用卡订单</c:if></h4><br>
							<label class="active">订&nbsp;&nbsp;单&nbsp;号:</label>&nbsp;&nbsp;${orders.orderid }
							<input type="hidden" id="orderid" name="orderid" value="${orders.orderid }" >
							<input type="hidden" id="userid" name="userid" value="${orders.userid }" >
							<input type="hidden" id="ordersGoodsprice" name="ordersGoodsprice" value="${orders.goodsprice}">
							<input type="hidden" id="couponprice" name="couponprice" value="${orders.couponprice}">
							<input type="hidden" id="memberGoodsPrice" name="memberGoodsPrice" value="${orders.memberGoodsPrice}">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<label class="active">新老订单:</label>&nbsp;&nbsp;
							<c:if test="${orders.isNeworder == 0}">新订单</c:if>
							<c:if test="${orders.isNeworder == 1}">老订单</c:if>
							<p></p>
							<label class="active">订单性质:</label>&nbsp;&nbsp;
							<c:if test="${orders.distinction == 0}">电商</c:if>
							<c:if test="${orders.distinction == 1}">售前卖</c:if>
							<c:if test="${orders.distinction == 2}">售后卖</c:if>
							<c:if test="${orders.distinction == 3}">老带新</c:if>
							<c:if test="${orders.distinction == 4}">售后转单</c:if>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<label class="active">手机号码:</label>&nbsp;&nbsp;${orders.users.mobile }
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<label class="active">订单状态:</label>&nbsp;&nbsp;
							<form:hidden path="oldstatus"/>
							<form:select path="orderstatus"  class="form-control" style="width:180px">
								<c:if test="${orders.channelFlag != 'bm'}">
									<c:if test="${orders.orderstatus == -2}">
										<form:option value="-2">取消订单</form:option>
									</c:if>
									<c:if test="${orders.orderstatus == -1}">
										<form:option value="-1">待付款</form:option>
									</c:if>
									<c:if test="${orders.orderstatus == 3}">
										<form:option value="3">已退款</form:option>
									</c:if>
									<c:if test="${orders.orderstatus == 4}">
										<form:option value="4">已完成</form:option>
									</c:if>
								</c:if>
								<c:if test="${orders.channelFlag == 'bm'}">
									<c:if test="${orders.orderstatus == -2}">
										<form:option value="-2">取消订单</form:option>
									</c:if>
									<c:if test="${orders.orderstatus == 3}">
										<form:option value="3">已退款</form:option>
									</c:if>
									<c:if test="${orders.orderstatus == 4}">
										<form:option value="4">已完成</form:option>
									</c:if>
								</c:if>
							</form:select>&nbsp;&nbsp;&nbsp;&nbsp;
							<label class="active">付款方式：</label>
							<form:select path="paycode" class="form-control" style="width:180px">
								<c:forEach items="${paylist}" var="payment">
									<form:option value="${payment.paycode}">${payment.paydesc}</form:option>
								</c:forEach>
							</form:select>
							<p></p>
							<label >留言备注：</label>
							<textarea name="userNote" rows="5" cols="60">${orders.userNote }</textarea>
						</div>
						<p></p>
						<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
							<h4>商品信息:</h4><br>
							<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
								<tr>
									<th style="text-align: center;">商品名称</th>
									<th style="text-align: center;">子项名称</th>
									<th style="text-align: center;">商品规格</th>
									<c:if test="${orders.isReal == 3}">
										<th style="text-align: center;">实际规格次</th>
									</c:if>
									<th style="text-align: center;">系统价</th>
									<c:if test="${orders.isReal == 2}">
										<th style="text-align: center;">异价比例</th>
										<th style="text-align: center;">异价价格</th>
									</c:if>
									<th style="text-align: center;">市场价</th>
									<th style="text-align: center;">优惠价</th>
									<c:if test="${orders.isReal == 3}">
										<th style="text-align: center;">异价比例</th>
									</c:if>
									<th style="text-align: center;">异价价格</th>
									<c:if test="${orders.isReal == 2}">
										<th style="text-align: center;">次(个)数</th>
									</c:if>
									<th style="text-align: center;">预约金</th>
									<th style="text-align: center;">购买数量</th>
									<c:if test="${(orders.isReal == 3) || (orders.isReal == 2 && orders.isNeworder == 1)}">
										<th style="text-align: center;">实际次数</th>
									</c:if>
									<th style="text-align: center;">红包面值</th>
									<th style="text-align: center;">折扣率</th>
									<th style="text-align: center;">会员折扣</th>
									<th style="text-align: center;">应付金额</th>
									<th style="text-align: center;">实付金额</th>
									<th style="text-align: center;">欠款</th>
									<th style="text-align: center;">操作</th>
								</tr>
								<tbody id="addZTD" style="text-align:center;">	
									<tr>${suitCardSons}</tr>
								</tbody>
							</table>
						</div>
						<p></p>
						<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
						<h4>订单支付信息:</h4>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
								<tr>
									<th style="text-align: center;">商品总额</th>
									<th style="text-align: center;">红包面值</th>
									<th style="text-align: center;">折扣率</th>
									<th style="text-align: center;">会员折扣</th>
									<th style="text-align: center;">邮费</th>
									<th style="text-align: center;">应付总额</th>
									<th style="text-align: center;">实付总额</th>
								</tr>
								<tr>
									<td align="center">${orders.goodsprice }</td>
									<td align="center">${orders.couponprice }</td>
									<td align="center">${orders.discount }</td>
									<td align="center">${orders.memberGoodsPrice }</td>
									<td align="center">${orders.shippingprice }</td>
									<td align="center">${orders.orderamount }</td>
									<td align="center">${(orders.totalamount*100 + orders.shippingprice*100)/100}</td>
								</tr>
						</table>
						</div>
						<p></p>
						<c:if test="${orders.num > 0}">
								<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
									<div class="pull-left">
										<a href="#" onclick="getInvoiceRelevancy('${orders.orderid}')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>发票信息</a>
									</div>
									<p></p>
							</div> 
						</c:if>
						<p></p>
						<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;" id="beauticianMoney">
						<h4>业务员营业额:</h4>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
								<tr>
									<th style="text-align: center;">时间</th>
									<th style="text-align: center;">类型</th>
									<th style="text-align: center;">金额</th>
									<th style="text-align: center;">业务员</th>
									<th style="text-align: center;">部门</th>
									<th style="text-align: center;">手机号</th>
									<th style="text-align: center;">营业额</th>
									<th style="text-align: center;">操作</th>
								</tr>
								<tbody style="text-align:center;">	
									<tr>${pushMoneryDetails}</tr>
								</tbody>
						</table>
						</div>
						<p></p>
						<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;" id="shopMoney">
						<h4>店营业额:</h4>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
								<tr>
									<th style="text-align: center;">时间</th>
									<th style="text-align: center;">类型</th>
									<th style="text-align: center;">金额</th>
									<th style="text-align: center;">归属店铺</th>
									<th style="text-align: center;">操作时间</th>
									<th style="text-align: center;">操作人</th>
									<th style="text-align: center;">操作</th>
								</tr>
								<c:forEach items="${turnOverDetailsList}" var="turnOverDetails">
									<tr>
										<td align="center">
											<fmt:formatDate value="${turnOverDetails.createDate}"  pattern="yyyy-MM-dd HH:mm:ss" />
										</td>
										<td align="center">
											<c:if test="${turnOverDetails.type == 1}">下单</c:if>
											<c:if test="${turnOverDetails.type == 2}">还款</c:if>
										</td>
										<td align="center">${turnOverDetails.amount}</td>
										<td align="center">${turnOverDetails.belongOfficeName}</td>
										<td align="center">
											<fmt:formatDate value="${turnOverDetails.settleDate}"  pattern="yyyy-MM-dd HH:mm:ss" />
										</td>
										<td align="center">${turnOverDetails.settleName}</td>
										<td align="center">
											<c:choose>
												<c:when test="${turnOverDetails.status == 2 && (turnOverDetails.belongOfficeId == '' || turnOverDetails.belongOfficeId == null)}">
													<a href="#" class="btn btn-success btn-xs" onclick="updateDetails('${turnOverDetails.turnOverDetailsId}','${turnOverDetails.orderId}')"><i class='fa fa-edit'></i>编辑</a>
												</c:when>
												<c:otherwise>
													<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-edit"></i>编辑</a>
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</c:forEach>
						</table>
						</div>
						<p></p>
						<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
						<h4>售后信息:</h4>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
								<tr>
									<th style="text-align: center;">时间</th>
									<th style="text-align: center;">商品名称</th>
									<th style="text-align: center;">商品规格</th>
									<th style="text-align: center;">售后次(个)数</th>
									<th style="text-align: center;">退款金额</th>
									<th style="text-align: center;">平欠款</th>
									<th style="text-align: center;">操作</th>
								</tr>
								<c:forEach items="${returnedList}" var="returnedGoods">
									<tr>
										<td align="center">
											<fmt:formatDate value="${returnedGoods.applyDate}"  pattern="yyyy-MM-dd HH:mm:ss" />
										</td>
										<td align="center">${returnedGoods.goodsName}</td>
										<td align="center">${returnedGoods.specName}</td>
										<td align="center">${returnedGoods.returnNum}</td>
										<td align="center">${returnedGoods.returnAmount}</td>
										<td align="center">${returnedGoods.floatArreageMoney}</td>
										<td align="center">
											<a href="#" onclick="openDialogView('售后详情', '${ctx}/ec/returned/returnform?id=${returnedGoods.id}&flag=view','850px','650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i>查看</a>
											<a href="#" onclick="openDialogView('查看转单', '${ctx}/ec/orders/returnedOrdersList?returnedId=${returnedGoods.id}','850px','650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i>查看转单</a>
										</td>
									</tr>
								</c:forEach>
						</table>
						</div>
						<p></p>
						<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
							<div class="pull-left">
								<h4>订单备注信息：</h4>
							</div>
							<c:if test="${type != 'view' }">
								<div class="pull-right">
									<a href="#" onclick="getRemarks()" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加备注</a>
								</div>
							</c:if>
							<p></p>
							<table id="contentTable" class="table table-bordered table-condensed  dataTables-example dataTable no-footer">
								<thead>
									<tr>
										<th style="text-align: center;">备注</th>
										<th style="text-align: center;">操作人</th>
										<th style="text-align: center;">操作时间</th>
									</tr>
								</thead>
								<tbody id="orderRemarks" style="text-align:center;">	
									<c:forEach items="${orders.orderRemarksLog }" var="orderRemark" varStatus="stauts">
										<tr>
											<td align="center">
												<div style='width:260px;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;'>${orderRemark.remarks }</div>
												<input type="hidden" id="orderRemark" name="orderRemark" value="${orderRemark.remarks }" />
												<input type="hidden" id="orderRemarksId" name="orderRemarksId" value="${orderRemark.orderRemarksId }" />
											</td>
											<td align="center">
												<div style='width:260px;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;'>${orderRemark.createBy.name }</div>
											</td>
											<td align="center">
												<div style='width:260px;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;'>
													<fmt:formatDate value="${orderRemark.createDate }" pattern="yyyy-MM-dd HH:mm:ss" />
												</div>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<p></p>
						<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
							<div class="pull-left">
								<h4>使用优惠明细：</h4>
							</div>
							<table id="contentTable" class="table table-bordered table-condensed  dataTables-example dataTable no-footer">
								<thead>
									<tr>
										<th style="text-align: center;">活动id</th>
										<th style="text-align: center;">活动名称</th>
										<th style="text-align: center;">红包id</th>
										<th style="text-align: center;">红包名称</th>
										<th style="text-align: center;">红包类型</th>
										<th style="text-align: center;">使用范围</th>
										<th style="text-align: center;">满减金额</th>
										<th style="text-align: center;">红包金额</th>
									</tr>
								</thead>
								<c:if test="${not empty orders.orderGoodsCoupons }">
									<c:forEach items="${orders.orderGoodsCoupons }" var="orderGoodsCoupon">
										<tr style="text-align: center;">
											<td>${orderGoodsCoupon.actionId }</td>
											<td>${orderGoodsCoupon.actionName }</td>
											<td>${orderGoodsCoupon.counponId }</td>
											<td>${orderGoodsCoupon.counponName }</td>
											<td>
												<c:if test="${orderGoodsCoupon.type==1}">商品详情页</c:if>
												<c:if test="${orderGoodsCoupon.type==2}">活动页</c:if>
												<c:if test="${orderGoodsCoupon.type==3}">新注册</c:if>
												<c:if test="${orderGoodsCoupon.type==4}">手工红包</c:if>
											</td>
											<td>
												<c:if test="${orderGoodsCoupon.usedType==1}">全部商品</c:if>
												<c:if test="${orderGoodsCoupon.usedType==2}">指定分类</c:if>
												<c:if test="${orderGoodsCoupon.usedType==3}">指定商品</c:if>
											</td>
											<td>${orderGoodsCoupon.baseAmount }</td>
											<td>${orderGoodsCoupon.couponMoney }</td>
										</tr>
									</c:forEach>
								</c:if>
							</table>
						</div>
					</form:form>
					<p></p>
					<shiro:hasPermission name="ec:orders:edit">
						<a href="#" onclick="openDialogView('操作日志', '${ctx}/ec/orders/orderlist?id=${orders.orderid}','900px','450px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>订单流程</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="ec:orders:edit">
						<!-- 1.app套卡、通用卡订单，未处理预约金且（未预约或预约了但预约状态不为等待服务、已完成、 已评价 、爽约的预约）；2.后台套卡、通用卡订单，未预约或预约了但预约状态不为等待服务、已完成、 已评价 、爽约的预约 -->
						<c:if test="${type != 'view' }">
							<c:choose>                                                                                                                                                                     
								<c:when test="${(orders.channelFlag != 'bm' && (orders.orderstatus ==1 || orders.orderstatus==2 || orders.orderstatus==4) && (((orders.advanceFlag > 0) || (orders.changeAdvanceFlag > 0)) && orders.returnedFlag == 0) && ((orders.sumAppt == 0) || (orders.sumAppt > 0 && orders.apptFlag == 0))) || (orders.channelFlag == 'bm' && (orders.orderstatus ==1 || orders.orderstatus==2 || orders.orderstatus==4) && (((orders.sumAppt == 0) || (orders.sumAppt > 0 && orders.apptFlag == 0)) && orders.returnedFlag == 0))}">
									<a href="#" onclick="forcedCancel('${orders.orderid}')" class="btn btn-danger btn-xs"><i class="fa fa-save"></i>强制取消</a>
								</c:when>
								<c:otherwise>
									<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-save"></i>强制取消</a>
								</c:otherwise>
							</c:choose>
						</c:if>
					</shiro:hasPermission>
					<shiro:hasPermission name="ec:orders:editLog">
						<a href="#" onclick="openDialogView('订单操作日志', '${ctx}/ec/orders/editLog?orderid=${orders.orderid}','1100px','650px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>订单操作日志</a>
					</shiro:hasPermission>
				</div>
			</div>	
		</div>
	</div>	
    <div class="loading"></div>
</body>
</html>