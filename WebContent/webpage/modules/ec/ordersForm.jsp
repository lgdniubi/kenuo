<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>订单修改</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
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
			if($("#isReal").val() == '1'){
				$("#shipping").hide();
			}
			if($("#shippingtype").val()== 0){
				$("#shop").hide();
				$("#logistics").show();
			}else if($("#shippingtype").val()== 1){
				$("#shop").show();
				$("#logistics").show();
			}else{
				$("#shop").hide();
				$("#logistics").hide();
			}
			//物流类型来判断是否显示物流地址
			$("#shippingtype").change(function(){
				$("#consignee").val("");	
				$("#mobile").val("");	
				$("#address").val("");
				$("#bazaarId").val("");
				$("#bazaarName").val("");
				var shippingtype = $(this).val();
				if(shippingtype == 0){
					$("#logistics").show();
					$("#shop").hide();
				}else if(shippingtype == 1){
					$("#shop").show();
					$("#logistics").show();
				}else{
					$("#shop").hide();
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
// 					mobile:{
// 						digits:true,
							
// 					}
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
	  
			$("#bazaarButton").click(function(){
				$("#consignee").val("");	
				$("#mobile").val("");	
				$("#address").val("");
				$("#bazaarId").val("");
				$("#bazaarName").val("");
				// 是否限制选择，如果限制，设置为disabled
				if ($("#bazaarButton").hasClass("disabled")){
					return true;
				}
				// 正常打开	
				top.layer.open({
				    type: 2, 
				    area: ['300px', '420px'],
				    title:"选择店铺",
				    ajaxData:{selectIds: $("#bazaarId").val()},
				    content: "${ctx}/tag/treeselect?url="+encodeURIComponent("/sys/office/treeData")+"&module=&checked=&extId=&isAll=" ,
				    btn: ['确定', '关闭']
		    	       ,yes: function(index, layero){ //或者使用btn1
								var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
								var ids = [], names = [], nodes = [];
								if ("" == "true"){
									nodes = tree.getCheckedNodes(true);
								}else{
									nodes = tree.getSelectedNodes();
								}
								for(var i=0; i<nodes.length; i++) {//
									/* if (nodes[i].level == 0){
										//top.$.jBox.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
										top.layer.msg("不能选择根节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
										return false;
									}// */
									if (nodes[i].isParent){
										//top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
										//layer.msg('有表情地提示');
										top.layer.msg("不能选择父节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
										return false;
									}//
									ids.push(nodes[i].id);
									names.push(nodes[i].name);//
									break; // 如果为非复选框选择，则返回第一个选择  
								}
								$("#bazaarId").val(ids.join(",").replace(/u_/ig,""));
								$("#bazaarName").val(names.join(","));
								$("#bazaarName").focus();
								
								$.ajax({
									type:"post",
									data:{
										officeId:$("#bazaarId").val()
									},
									url:"${ctx}/ec/orders/getOfficeDetails",
									success:function(data){
										if(data){
											$("#consignee").val(data.shopName);	
											$("#mobile").val(data.shopPhone);	
											$("#address").val(data.address);
										}else{
											top.layer.alert('该店铺详情异常!', {icon: 0, title:'提醒'}); 
										}
									},
									error:function(XMLHttpRequest,textStatus,errorThrown){
									}
								});
								
								top.layer.close(index);
						    	       },
		    	cancel: function(index){ //或者使用btn2
		    	           //按钮【按钮二】的回调
		    	       }
				}); 
			
			});
	    });
			
	    	
		    function TopUp(recid,singleRealityPrice,singleNormPrice,orderArrearage,servicetimes,remaintimes,goodsBalance){
		    	var orderid = $("#orderid").val();
		    	var userid = $("#userid").val();
		    	var isReal = $("#isReal").val();
		    	var channelFlag = $("#channelFlag").val();
				top.layer.open({
				    type: 2, 
				    area: ['600px', '450px'],
				    title:"充值",
				    content: "${ctx}/ec/orders/addTopUp?orderid="+orderid+"&singleRealityPrice="+singleRealityPrice+"&userid="+userid+"&isReal="+isReal+"&goodsBalance="+goodsBalance+"&servicetimes="+servicetimes,
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){
				    	var orderid = $("#orderid").val();
				        var obj =  layero.find("iframe")[0].contentWindow;
						var accountBalance = obj.document.getElementById("accountBalance").value;
				    	var rechargeAmount = obj.document.getElementById("rechargeAmount").value;
						var topUpTotalAmount = obj.document.getElementById("topUpTotalAmount").value;
						var jsmoney = obj.document.getElementById("jsmoney").value;
						var loading = obj.document.getElementById("loading");
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
						if(servicetimes == 999){       //当充值的对象时时限卡时，充值时必须将尾款全部补齐
							if(topUpTotalAmount < orderArrearage){     //充值金额+使用的账户余额<欠款
								$(loading).hide();
								top.layer.alert('尾款剩余'+orderArrearage+'元，补齐尾款才能预约！', {icon: 0, title:'提醒'});
								return;
							}
						}
						
						//查询用户账户余额
						$.ajax({
							type:"post",
							data:{
								userid:userid
							 },
							url:"${ctx}/ec/orders/getAccount",
							success:function(date){
							//	var orderArrearage = $("#orderArrearage").val();
							//	var servicetimes = $("#servicetimes").val();
							//	var remaintimes = $("#remaintimes").val();
								var totalAmount = topUpTotalAmount/singleRealityPrice;
								var _totalAmount = totalAmount - parseInt(totalAmount);
								if(accountBalance > date){
									$(loading).hide(); 
									top.layer.alert('您输入的余额不能大于当前账户的余额！', {icon: 0, title:'提醒'});
								}else{
									top.layer.close(index);
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
											channelFlag:channelFlag
										 },
										url:"${ctx}/ec/orders/addOrderRechargeLog",
										success:function(date){
												top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
												window.location="${ctx}/ec/orders/orderform?orderid="+orderid;
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
	
	//修改提成人员信息
	function updateFileSysUserInfo(obj,pushmoneyRecordId){
		var orderamount = $("#orderamount").val();
		top.layer.open({
 			type: 2, 
 		    area: ['550px', '420px'],
 		    title:"提成人员选择",
 		    content: "${ctx}/ec/orders/getPushmoneyView?pushmoneyRecordId="+pushmoneyRecordId,
 		    btn: ['确定', '关闭']
     	    ,yes: function(index, layero){
     	    	var obj =  layero.find("iframe")[0].contentWindow;
     	    	var sysUserId = obj.document.getElementById("sysUserId").value; //员工id
     	    	var sysMobile = obj.document.getElementById("sysMobile").value; //员工电话
     	    	var sysName = obj.document.getElementById("sysName").value; //员工名称
     	    	var pushMoney = obj.document.getElementById("pushMoney").value; //提成金额
     	    	var orderid =  $("#orderid").val();
     	    	var isReal =  $("#isReal").val();
     	    	if(pushMoney==""){
     	    		top.layer.alert('填写提成金额！', {icon: 0, title:'提醒'});
     	    		return;
     	    	}else if(pushMoney < 0 || parseFloat(pushMoney) - parseFloat(orderamount) > 0){
    	    		top.layer.alert('提成金额必须大于等于0，小于等于订单应付总额！', {icon: 0, title:'提醒'});
     	    		return;
     	    	}else{
     	    		$.ajax({
         				type:"post",
         				data:{
         					orderId:orderid,
         					pushmoneyUserId:sysUserId,
         					pushMoney:pushMoney
         				 },
         				url:"${ctx}/ec/orders/saveOrderPushmoneyRecord?flag=edit"+"&pushmoneyRecordId="+pushmoneyRecordId,
         				success:function(date){
         					if(date == 'success'){
         						$(obj).parent().parent().remove();
         						top.layer.alert('修改提成人员成功', {icon: 0, title:'提醒'});
         						window.location="${ctx}/ec/orders/orderform?orderid="+orderid+"&isReal="+isReal+"&type=edit";
         	     				top.layer.close(index);
         					}else{
         						top.layer.alert('修改提成人员失败', {icon: 0, title:'提醒'});
         					}
         				},
         				error:function(XMLHttpRequest,textStatus,errorThrown){
         					
         				}
         			});
     	    	}
 			}
 		}); 
	}
     
	//删除提成人员
     function delFileSysUserInfo(obj,pushmoneyRecordId){
		$.ajax({
			type:"post",
			data:{
				pushmoneyRecordId:pushmoneyRecordId
			 },
			url:"${ctx}/ec/orders/deleteSysUserInfo",
			success:function(date){
				if(date == 'success'){
					$(obj).parent().parent().remove();
					top.layer.alert('删除提成人员成功', {icon: 0, title:'提醒'});
				}else{
					top.layer.alert('删除提成人员失败', {icon: 0, title:'提醒'});
				}
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
				
			}
		});
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
	
     function getSysUserInfo(){
    	var orderamount = $("#orderamount").val();
 		var sysUserIds = document.getElementsByName("sysUserId");
    	var operationName = $("#operationName").val();
 		if ($("#mtmyUserButton").hasClass("disabled")){
 			return true;
 		}
 		// 正常打开	
 		top.layer.open({
 			type: 2, 
 		    area: ['550px', '420px'],
 		    title:"提成人员选择",
 		    content: "${ctx}/ec/orders/getPushmoneyView",
 		    btn: ['确定', '关闭']
     	    ,yes: function(index, layero){
     	    	var obj =  layero.find("iframe")[0].contentWindow;
     	    	var sysUserId = obj.document.getElementById("sysUserId").value; //员工id
     	    	var sysMobile = obj.document.getElementById("sysMobile").value; //员工电话
     	    	var sysName = obj.document.getElementById("sysName").value; //员工名称
     	    	var pushMoney = obj.document.getElementById("pushMoney").value; //提成金额
     	    	var orderid =  $("#orderid").val();
     	    	var isReal =  $("#isReal").val();
     	    	if(pushMoney==""){
     	    		top.layer.alert('填写提成金额！', {icon: 0, title:'提醒'});
     	    		return;
     	    	}else if(sysUserId == ""){
    	    		top.layer.alert('填写提成人员！', {icon: 0, title:'提醒'});
     	    		return;
     	    	}else if(pushMoney < 0 || parseFloat(pushMoney) - parseFloat(orderamount) > 0){
    	    		top.layer.alert('提成金额必须大于等于0，小于订单应付总额！', {icon: 0, title:'提醒'});
     	    		return;
     	    	}else{
     	    		if(sysUserIds.length > 0){
    	    			for(i=0;i<sysUserIds.length;i++){
         	    	        if(sysUserId == sysUserIds[i].value){
         	    	        	top.layer.alert('提成人员不能相同！', {icon: 0, title:'提醒'});
         	     	    		return;
         	    	        }
         	    	    }
    	    		}
     	    		
     	    		$.ajax({
         				type:"post",
         				data:{
         					orderId:orderid,
         					pushmoneyUserId:sysUserId,
         					pushMoney:pushMoney
         				 },
         				url:"${ctx}/ec/orders/saveOrderPushmoneyRecord?flag=add",
         				success:function(date){
         					if(date == 'success'){
         						$(obj).parent().parent().remove();
         						top.layer.alert('添加提成人员成功', {icon: 0, title:'提醒'});
         						window.location="${ctx}/ec/orders/orderform?orderid="+orderid+"&isReal="+isReal+"&type=edit";
         	     				top.layer.close(index);
         					}else{
         						top.layer.alert('添加提成人员失败', {icon: 0, title:'提醒'});
         					}
         				},
         				error:function(XMLHttpRequest,textStatus,errorThrown){
         					
         				}
         			});
     	    	}
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
     						window.location="${ctx}/ec/orders/orderform?orderid="+orderid+"&isReal="+isReal+"&type=edit";
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

	function ToAdvance(recid,servicetimes,orderArrearage){
		var userid = $("#userid").val();
		var orderid = $("#orderid").val();
		var isReal = $("#isReal").val();
		var channelFlag = $("#channelFlag").val();
		top.layer.open({
		    type: 2, 
		    area: ['600px', '450px'],
		    title:"处理预约金",
		    content: "${ctx}/ec/orders/handleAdvanceFlagForm?recid="+recid+"&userid="+userid+"&servicetimes="+servicetimes+"&orderArrearage="+orderArrearage,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		    	var obj =  layero.find("iframe")[0].contentWindow;
     	    	var sum = obj.document.getElementById("sum").value; //是否使用了账户余额
				//异步处理预约金
				$.ajax({
					type:"post",
					data:{
						sum:sum,
						recid:recid, 
						userid:userid,
						servicetimes:servicetimes,
						orderArrearage:orderArrearage,
						channelFlag:channelFlag
					 },
					url:"${ctx}/ec/orders/handleAdvanceFlag?recid="+recid+"&userid="+userid+"&orderid="+orderid,
					success:function(date){
						if(date == 'success'){
							top.layer.alert('处理成功!', {icon: 1, title:'提醒'});
							window.location="${ctx}/ec/orders/orderform?orderid="+orderid;
							top.layer.close(index);	
						}else if(date == 'error'){
							top.layer.alert('处理失败!', {icon: 0, title:'提醒'});
							window.location="${ctx}/ec/orders/orderform?orderid="+orderid;
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
	
	function getInvoiceRelevancy(orderid){
		top.openTab("${ctx}/ec/invoice/list?orderId="+orderid,"发票列表", false);
		var index = parent.layer.getFrameIndex(window.name);
		top.layer.close(index);
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
					<form:form id="inputForm" modelAttribute="orders" action="${ctx}/ec/orders/updateVirtualOrder" method="post" class="form-horizontal">
						<input id="oldAddress" name="oldAddress" type="hidden" value="${orders.address}"/>
						<input id="orderamount" name="orderamount" type="hidden" value="${orders.orderamount}"/>
						<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
							<input type="hidden" id="channelFlag" value="${orders.channelFlag}" />
							<input type="hidden" id="isReal" value="${orders.isReal}" />
							<input type="hidden" id="operationName" value="${user.name}" />
							<h4>订单基本信息:&nbsp;&nbsp;<c:if test="${orders.isReal == 1}">虚拟订单</c:if>&nbsp;&nbsp;<c:if test="${orders.isReal == 0}">实物订单</c:if></h4><br>
							<label class="active">订&nbsp;&nbsp;单&nbsp;号:</label>&nbsp;&nbsp;${orders.orderid }
							<input type="hidden" id="orderid" name="orderid" value="${orders.orderid }" >
							<input type="hidden" id="userid" name="userid" value="${orders.userid }" >
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
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<label class="active">手机号码:</label>&nbsp;&nbsp;${orders.users.mobile }
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<label class="active">订单状态:</label>&nbsp;&nbsp;
							<form:hidden path="oldstatus"/>
							<form:select path="orderstatus"  class="form-control" style="width:180px">
								<c:if test="${orders.isReal == 0}">
									<c:if test="${orders.orderstatus == -2}">
										<form:option value="-2">取消订单</form:option>
									</c:if>
									<c:if test="${orders.orderstatus == -1}">
										<form:option value="-1">待付款</form:option>
									</c:if>
									<c:if test="${orders.orderstatus == 1 or orders.orderstatus == 2 or orders.orderstatus == 4}">
										<form:option value="1">待发货</form:option>
										<form:option value="2">待收货</form:option>
										<form:option value="4">已完成</form:option>
									</c:if>
									<c:if test="${orders.orderstatus == 3}">
										<form:option value="3">已退款</form:option>
									</c:if>
								</c:if>
								<c:if test="${orders.isReal == 1}">
									<c:if test="${orders.channelFlag != 'bm'}">
										<form:option value='${orders.orderstatus }'>
											<c:if test="${orders.orderstatus == -2}">
												取消订单
											</c:if>
											<c:if test="${orders.orderstatus == -1}">
												待付款
											</c:if>
											<c:if test="${orders.orderstatus == 1}">
												待发货
											</c:if>
											<c:if test="${orders.orderstatus == 2}">
												待收货
											</c:if>
											<c:if test="${orders.orderstatus == 3}">
												已退款
											</c:if>
											<c:if test="${orders.orderstatus == 4}">
												已完成
											</c:if>
										</form:option>
									</c:if>
									<c:if test="${orders.channelFlag == 'bm'}">
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
							<textarea name="usernote" rows="5" cols="60">${orders.userNote }</textarea>
						</div>
						<p></p>
						<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
							<h4>商品信息:</h4><br>
							<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
								<tr>
									<th style="text-align: center;">商品名称</th>
									<th style="text-align: center;">商品规格</th>
									<th style="text-align: center;">系统价</th>
									<th style="text-align: center;">市场价</th>
									<th style="text-align: center;">优惠价</th>
									<th style="text-align: center;">购买数量</th>
									<th style="text-align: center;">红包面值</th>
									<th style="text-align: center;">折扣率</th>
									<th style="text-align: center;">会员折扣</th>
									<th style="text-align: center;">应付金额</th>
									<th style="text-align: center;">实付金额</th>
									<th style="text-align: center;">预约金</th>
									<c:if test="${orders.isReal == 1 }">
										<th style="text-align: center;">实际次数</th>
									</c:if>
									<!-- <th style="text-align: center;">余款</th> -->
									<th style="text-align: center;">欠款</th>
									<th style="text-align: center;">操作</th>
								</tr>
								<c:forEach items="${orders.orderGoodList }" var="orderGood">
									<tr>
										<td align="center">${orderGood.goodsname }</td>
										<td align="center">${orderGood.speckeyname }</td>
										<td align="center">${orderGood.costprice }</td>
										<td align="center">${orderGood.marketprice }</td>
										<td align="center">${orderGood.goodsprice }</td>
										<td align="center">${orderGood.goodsnum }</td>
										<td align="center">${orderGood.couponPrice }</td>
										<td align="center">${orderGood.discount }</td>
										<td align="center">${orderGood.membergoodsprice }</td>
										<td align="center">${orderGood.orderAmount }</td>
										<td align="center">${orderGood.totalAmount}</td>
										<td align="center">
											<c:if test="${orders.isReal == 1 }">
												${orderGood.advancePrice}
											</c:if>
											<c:if test="${orders.isReal != 1 }">
												0.0
											</c:if>
										</td>
										<c:if test="${orders.isReal == 1 }">
											<td align="center">${orderGood.remaintimes }</td>
										</c:if>
										<%-- <td>${orderGood.orderBalance }</td> --%>
										<td align="center">${orderGood.orderArrearage }</td>
										<td align="center">
											<c:if test="${type != 'view' }">
												<c:if test="${orders.channelFlag == 'bm' || (orders.channelFlag != 'bm' && orders.isReal==1 && orderGood.advanceFlag != 1) || (orders.channelFlag != 'bm' && orders.isReal==0 && (orders.orderstatus == 1 || orders.orderstatus == 2 || orders.orderstatus == 4))}">
													<c:if test="${orderGood.orderArrearage != 0}">
														<a href="#" onclick="TopUp(${orderGood.recid},${orderGood.singleRealityPrice },${orderGood.singleNormPrice },${orderGood.orderArrearage },${orderGood.servicetimes },${orderGood.payRemaintimes },${orderGood.goodsBalance})"  class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>充值</a>
													</c:if>
													<c:if test="${orderGood.orderArrearage == 0}">
														<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-edit"></i>充值</a>
													</c:if>
												</c:if>
												<c:if test="${orders.channelFlag != 'bm' && orders.isReal==1 && orderGood.advanceFlag == 1}">
													<c:if test="${orders.orderstatus == 4 && orderGood.sumAppt == 1}">
														<a href="#" onclick="ToAdvance(${orderGood.recid},${orderGood.servicetimes},${orderGood.orderArrearage })"  class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>处理预约金</a>
													</c:if>
													<c:if test="${orders.orderstatus != 4 || orderGood.sumAppt == 0}">
														<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-edit"></i>处理预约金</a>
													</c:if>
													
												</c:if>
											</c:if>
											<a href="#" onclick="openDialogView('查看订单', '${ctx}/ec/orders/getMappinfOrderView?recid=${orderGood.recid}&orderid=${orders.orderid }&orderType=mapping','800px','600px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i>商品充值查看</a>
										</td>
									</tr>
									<input type="hidden" id="orderArrearage" name="orderArrearage" value="${orderGood.orderArrearage }" />
									<input type="hidden" id="servicetimes" name="servicetimes" value="${orderGood.servicetimes }" />
									<input type="hidden" id="remaintimes" name="remaintimes" value="${orderGood.remaintimes }" />
								</c:forEach>
							</table>
						</div>
						<p></p>
						<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
						<h4>订单支付信息:</h4>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
								<tr>
									<th style="text-align: center;">商品总价</th>
									<th style="text-align: center;">红包面值</th>
									<th style="text-align: center;">折扣率</th>
									<th style="text-align: center;">会员折扣</th>
									<th style="text-align: center;">邮费</th>
									<th style="text-align: center;">应付金额</th>
									<th style="text-align: center;">实付金额</th>
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
							<%-- <div style="padding-left: 90px;">
								<p></p>
								<label>商品总价：￥${orders.goodsprice }</label>
								<label>红包面值：￥${orders.couponprice }</label>
								<p></p>
								<label>折扣率：￥${orders.discount }</label>
								<label>会员折扣：￥${orders.memberGoodsPrice }</label>
								<p></p>
								<label>邮费：￥${orders.shippingprice }</label>
								<p></p>
								<label>应付金额：￥${orders.orderamount }</label>
								<label>实付金额：￥${orders.totalamount }</label>
								<form:input id="goodsprice" readonly="true" path="goodsprice" htmlEscape="false" maxlength="11" class="form-control required" style="width:180px" />
								<label>成交总额：</label>
								<form:input id="orderamount" readonly="true" path="orderamount" htmlEscape="false" maxlength="11" class="form-control required" style="width:180px" />
								<label >总&nbsp;&nbsp;余&nbsp;&nbsp;额：</label>
								<form:input id="orderBalance" readonly="true" path="orderBalance" htmlEscape="false" maxlength="11" class="form-control required" style="width:180px" />
								<p></p>
								<label>实付总额：</label>
								<form:input id="totalamount" readonly="true" path="totalamount" htmlEscape="false" maxlength="11" class="form-control required" style="width:180px" />
								<label>总&nbsp;&nbsp;欠&nbsp;&nbsp;款：</label>
								<form:input id="orderArrearage" readonly="true" path="orderArrearage" htmlEscape="false" maxlength="11111" class="form-control required" style="width:180px" />
							</div> --%>
						</div>
						<p></p>
						<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;" id="shipping">
							<label ><font color="red">*</font>物流类型：</label>
							<form:select path="shippingtype"  class="form-control" style="width:180px">
									<form:option value="0">快递发货</form:option>
									<form:option value="1">到店自取</form:option>
									<form:option value="2">无需发货</form:option>
							</form:select>
							<div class="input-group" id="shop">
								<p></p>
								<label><!-- <font color="red">*</font> -->选择店铺:</label>&nbsp;&nbsp;&nbsp;&nbsp;
								<div class="input-group" style="float:right">
									<input id="bazaarId" class=" form-control input-sm required" name="bazaarId" value="" aria-required="true" type="hidden">
									<input id="bazaarName" class="form-control input-sm valid" name="bazaarName" readonly="readonly" value="" data-msg-required="" style="" aria-required="true" aria-invalid="false" type="text">
									<span class="input-group-btn">
										<button id="bazaarButton" class="btn btn-sm btn-primary " type="button">
											<i class="fa fa-search"></i>
										</button>
									</span>
									<label id="bazaarName-error" class="error" for="bazaarName" style="display: none"></label>
								</div>
							</div>
							<div id="logistics">
								<p></p>
								<label ><font color="red">*</font>收&nbsp;&nbsp;货&nbsp;&nbsp;人：</label>
								<form:input path="consignee" htmlEscape="false" maxlength="10" class="form-control required" style="width:180px" />
								<label ><font color="red">*</font>联系电话：</label>
								<form:input path="mobile" htmlEscape="false" class="form-control required" style="width:180px" />
								<label ><font color="red">*</font>收货地址：</label>
								<form:input path="address" htmlEscape="false" maxlength="120" class="form-control required" style="width:180px" />
							</div>
						</div>
						<p></p>
						<c:if test="${orders.num > 0}">
								<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
									<div class="pull-left">
										<a href="#" onclick="getInvoiceRelevancy('${orders.orderid}')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>发票信息</a>
									</div>
									<p></p>
								<%-- <label class="active">索要发票</label>
								<p></p>
								<div id="iType">
									<label class="active">发票类型：</label>
									<form:select path="orderInvoice.invoiceType"  class="form-control" style="width:180px">
										<form:option value="1" disabled="true">个人普票</form:option>
										<form:option value="2" disabled="true">公司普票</form:option>
										<form:option value="3" disabled="true">公司专票</form:option>
									</form:select>
								</div>
								<p></p>
								<label class="active">发票抬头：</label>
								<input type="text" name="taxNum" class="form-control" style="width:180px" value="${orders.orderInvoice.headContent }"  readonly="readonly" />
								<c:if test="${orders.orderInvoice.invoiceType == 3}">
									<label class="active">纳税人识别号：</label>
									<input type="text" name="taxNum" class="form-control" style="width:180px" value="${orders.orderInvoice.taxNum }"  readonly="readonly" />
									<label class="active">银行账号：</label>
									<input type="text" name="bankNo" class="form-control" style="width:180px" value="${orders.orderInvoice.bankNo }"  readonly="readonly" />
									<p></p>
									<label class="active">开户银行：</label>
									<input type="text" name="bankName" class="form-control" style="width:180px" value="${orders.orderInvoice.bankName }"  readonly="readonly" />
									<label class="active">电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：</label>
									<input type="text" name="invoicePhone" class="form-control" style="width:180px" value="${orders.orderInvoice.phone }"  readonly="readonly" />
									<label class="active">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：</label>
									<input type="text" name="invoiceAddress" class="form-control" style="width:180px" value="${orders.orderInvoice.address }"  readonly="readonly" />
								</c:if>
								<p></p>
								<label class="active">收货人信息：</label>
								<p></p>
								<label class="active">收货人：</label>
								<input type="text" name="recipientsName" class="form-control" style="width:180px" value="${orders.orderInvoice.recipientsName }"  readonly="readonly" />
								<label class="active">联系电话：</label>
								<input type="text" name="recipientsPhone" class="form-control" style="width:180px" value="${orders.orderInvoice.recipientsPhone }"  readonly="readonly" />
								<label class="active">收货地址：</label>
								<input type="text" name="recipientsAddress" class="form-control" style="width:180px" value="${orders.orderInvoice.recipientsAddress }"  readonly="readonly" />--%>
							</div> 
						</c:if>
						<p></p>
						<c:if test="${orders.isNeworder == 0}">
							<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
								<div class="pull-left">
									<h4>人员提成信息：</h4>
								</div>
								<c:if test="${type != 'view' }">
									<div class="pull-right">
										<a href="#" onclick="getSysUserInfo()" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加业务员</a>
									</div>
								</c:if>
								<p></p>
								<table id="contentTable" class="table table-bordered table-condensed  dataTables-example dataTable no-footer">
									<thead>
										<tr>
											<th style="text-align: center;">业务员</th>
											<th style="text-align: center;">手机号</th>
											<th style="text-align: center;">提成金额</th>
											<th style="text-align: center;">操作人</th>
											<th style="text-align: center;">操作时间</th>
											<c:if test="${type != 'view' }">
												<th style="text-align: center;" colspan="2">操作</th>
											</c:if>
										</tr>
									</thead>
									<tbody id="sysUserInfo" style="text-align:center;">
										<c:forEach items="${orders.orderPushmoneyRecords }" var="orderPushmoneyRecord" varStatus="stauts">
											<tr>
												<td>
													<input type="hidden" id="sysUserId" name="sysUserId" value="${orderPushmoneyRecord.pushmoneyUserId }" />
													<input type="hidden" id="pushmoneyRecordId" name="pushmoneyRecordId" value="${orderPushmoneyRecord.pushmoneyRecordId }" />
													<input id="sysName" name="sysName" type="text" value="${orderPushmoneyRecord.pushmoneyUserName }" class='form-control' readonly='readonly'>
												</td>
												<td>
													<input id="sysMobile" name="sysMobile" type="text" value="${orderPushmoneyRecord.pushmoneyUserMobile }" class='form-control' readonly='readonly'>
												</td>
												<td>
													${orderPushmoneyRecord.pushMoney }
													<%-- <input class="form-control" type="text" id="pushMoney" class="form-control required" name="pushMoney" value="${orderPushmoneyRecord.pushMoney }" /> --%>
												</td>
												<td>
													${orderPushmoneyRecord.createBy.name }
												</td>
												<td>
													<fmt:formatDate value="${orderPushmoneyRecord.createDate }" pattern="yyyy-MM-dd HH:mm:ss" />
												</td>
												<c:if test="${type != 'view' }">
													<td colspan="2">
														<a href="#" class="btn btn-success btn-xs" onclick="updateFileSysUserInfo(this,${orderPushmoneyRecord.pushmoneyRecordId})"><i class='fa fa-edit'></i> 修改</a>
														<a href="#" class="btn btn-danger btn-xs" onclick="delFileSysUserInfo(this,${orderPushmoneyRecord.pushmoneyRecordId })"><i class='fa fa-trash'></i> 删除</a>
													</td>
												</c:if>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</c:if>
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
									<%-- 	<c:if test="${type != 'view' }">
											<th style="text-align: center;">操作</th>
										</c:if> --%>
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
											<%-- <c:if test="${type != 'view' }">
												<td>
													<a href='#' class='btn btn-danger btn-xs' onclick='delOrderRemarks(this)'><i class='fa fa-trash'></i> 删除</a>
												</td>
											</c:if> --%>
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
						<c:if test="${type != 'view' }">
							<c:if test="${orders.isReal == 0   and orders.orderstatus==1}">
								<a href="#" onclick="forcedCancel('${orders.orderid}')" class="btn btn-danger btn-xs"><i class="fa fa-save"></i>强制取消</a>
							</c:if>
							<c:if test="${orders.isReal == 0   and orders.orderstatus!=1}">
								<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-save"></i>强制取消</a>
							</c:if>
							<c:if test="${orders.isReal==1}">
								<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-save"></i>强制取消</a>
							</c:if>
						</c:if>	
					</shiro:hasPermission>
					<%-- <p></p>
					<label class="active">操作日志:</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<shiro:hasPermission name="ec:orders:edit">
							<a href="#" onclick="openDialogView('操作日志', '${ctx}/ec/orders/loglist?id=${orders.orderid}','500px','400px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>操作日志</a>
					</shiro:hasPermission>
					
					<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
							<th>操作者</th>
							<th>操作时间</th>
							<th>描述</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${acountlist}" var="acount">
								<tr>
									<td>${acount.operator}</td>
									<td><fmt:formatDate value="${acount.changetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${acount.logdesc}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table> --%>
				</div>
			</div>	
		</div>
	</div>	
    <div class="loading"></div>
</body>
</html>