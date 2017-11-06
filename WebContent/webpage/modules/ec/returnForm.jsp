<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>退货录入</title>
	<meta name="decorator" content="default"/>
	<!-- 内容上传 引用-->

<script type="text/javascript">
		var isReal=0;//判断是否为实物
		var goodNum=0;
		var returnAmount=0;//退款金额
		var remaintimes=0;//剩余次数
		var singleRealityPrice=0;//服务单次价
		var orderArrearage=0;//订单欠款
		var totalAmount=0;//实付款
		var returnedGoodsNum = 0;//后台查询出来 "实物" 中正在退货的商品数量
		var validateForm;
		
		var advanceFlag;
		
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  var recid=$("#goodsMappingId").val();
			  if(recid==""){
				  top.layer.alert('请选择退货商品!', {icon: 0, title:'提醒'});
				  return;
			  }
			  if(orderArrearage>0){
				  top.layer.alert('当前订单有欠款,无法退款(请先补齐欠款)', {icon: 0, title:'提醒'});
				  return;
			  }
			  //remaintimes == 0   当时限卡时,才会有
			  if(servicetimes == 999 && remaintimes == 0){
				  top.layer.alert('该商品已申请过售后!', {icon: 0, title:'提醒'});
				  return;
			  }
			  if(returnAmount==-1){
				 top.layer.alert('该商品已申请过售后!', {icon: 0, title:'提醒'});
				 return;
			  }else if(returnAmount==-2){
				 top.layer.alert('该订单还没有进行分销，请稍后再试!', {icon: 0, title:'提醒'});
				 return;
			  }
			  //当是实物的时候,售后商品数量 <=goodNum实际购买量-returnedGoodsNum(从后台查出来的退货中的数量)
			  if(isReal == 0){
				  var rn=$("#returnNum").val();
				  if(parseInt(rn)<=0){
					  top.layer.alert('售后商品数量必须大于0，小于可申请的售后数量!', {icon: 0, title:'提醒'});
					  return;
				  }else if(parseInt(rn)>(parseInt(goodNum)-parseInt(returnedGoodsNum))){
					  top.layer.alert('售后商品数量必须大于0，小于可申请的售后数量!', {icon: 0, title:'提醒'});
					  return;
				  }
			  }
			  //除了实物之外,只要有预约金的,都需要做处理
			  if(isReal != 0){
				  if(advanceFlag == 1){
					  top.layer.alert('当前订单有预约金,无法退款(请先处理预约金)', {icon: 0, title:'提醒'});
					  return;
				  }
			  }
				  
			  //虚拟商品的售后次数校验,时限卡不进行此校验
			  if(isReal == 1 && servicetimes != 999){
				  var st=$("#serviceTimes").val();
				  if(parseInt(st)<=0){
					  top.layer.alert('售后次数必须大于0，小于剩余次数!', {icon: 0, title:'提醒'});
					  return;
				  }else if(parseInt(st)>parseInt(remaintimes)){
					  top.layer.alert('售后次数必须大于0，小于剩余次数!', {icon: 0, title:'提醒'});
					  return;
				  }
			  }
			  
			  var type=$("#applyType").val();
			  if(type == 0){
				  //虚拟商品的退款金额校验
				  var ra=$("#returnAmount").val();
				  if(parseFloat(ra)<0){
					  top.layer.alert('退款金额必须大于等于0，小于实付款金额!', {icon: 0, title:'提醒'});
					  return;
				  }else if(parseFloat(totalAmount) < parseFloat(ra)){
					  top.layer.alert('退款金额必须大于等于0，小于实付款金额!', {icon: 0, title:'提醒'});
					  return;
				  }
			  }
			  $("#returnAmount").val();
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		
		//选中某个商品时,查询数据
		function selectFunction(o){
			var id=$("#selectId:checked").val();
			var type="${orders.channelFlag}";
			var orderId=$("#orderId").val();
			isReal="${orders.isReal}";
			$("#goodsMappingId").val(id);
			goodNum=$("#"+id+"goodsnum").val();//购买的数量
			remaintimes=$("#"+id+"remaintimes").val();//虚拟商品剩余次数
			
			//时限卡售后添加servicetimes
			servicetimes=$("#"+id+"servicetimes").val();//为了判断是否为时限卡
			advanceFlag = $("#"+id+"advanceFlag").val();//判断时限卡是否为预约金,是:先处理预约金
			
			
			orderArrearage=$("#"+id+"orderArrearage").val();//订单欠款
			singleRealityPrice=$("#"+id+"singleRealityPrice").val();//实际服务单次价
			totalAmount=$("#"+id+"total").val();//实付款
			if(isReal==0){
				returnedGoodsNum = $("#"+id+"returnedGoodsNum").val();//实物中  从后台查出来的退货中的数量
				$("#returnNum").val(parseInt(goodNum)-parseInt(returnedGoodsNum));//为售后商品数量赋值
			}
			//除了实物之外的,当前是否为预约金,是:先处理预约金
			if(isReal != 0){
				if(advanceFlag == 1){
					top.layer.alert('当前订单有预约金,无法退款(请先处理预约金)', {icon: 0, title:'提醒'});
					return;
				}
			}
			
			//判断是否有无欠款
			if(orderArrearage>0){
				top.layer.alert('当前订单有欠款,无法退款(请先补齐欠款)', {icon: 0, title:'提醒'});
			}else{
				if(servicetimes == 999){//是否为时限卡,剩余次数赋值, 
					//是时限卡的时候, 当remaintimes == 0 ,说明时限卡已经售后过了
					if(remaintimes == 0){
						top.layer.alert('该商品已申请过售后!', {icon: 0, title:'提醒'});
						return;
					}
					$("#d1serviceTimes").hide();
					$("#serviceTimes").val(remaintimes);//时限卡内还剩下多少次就售后多少次
				}else{
					$("#d1serviceTimes").show();
				}
				if("bm"==type){//当时后台数据时
					var orderA=$("#"+id+"orderAmount").val();
					var totalA=$("#"+id+"total").val();
					$("#totalAmount").val(totalA);//实付款金额
					$("#orderAmount").val(orderA);//应付款金额
					if(isReal==0){//是实物
						if(orderA==totalA){
							$("#totalAmount").val(totalA);
							returnAmount=totalA/goodNum;//计算退款金额
							var retNum=$("#returnNum").val();//退货的商品数量
							var SumAmount=returnAmount*retNum;//退款总金额
							//$("#returnAmount").val(SumAmount.toFixed(2));//退款金额
						}else{
							$.ajax({
								type:"post",
								async:false,
								data:{
									id:id,
									isReal:isReal,
									orderid:orderId
								 },
								url:"${ctx}/ec/orders/getOrderGoodsIsFre",
								success:function(date){
									returnAmount=date;
									if(returnAmount==-1){
										top.layer.alert('该商品已申请过售后!', {icon: 0, title:'提醒'});
										return;
									}else if(returnAmount==-2){
										top.layer.alert('该订单还没有进行分销，请稍后再试!', {icon: 0, title:'提醒'});
										return;
									}
									
								},
								error:function(XMLHttpRequest,textStatus,errorThrown){
											    
								}
							});
							
						}
					}else if(isReal==1){//是虚拟
						$.ajax({
							type:"post",
							async:false,
							data:{
								id:id,
								isReal:isReal,
								orderid:orderId
							 },
							url:"${ctx}/ec/orders/getOrderGoodsIsFre",
							success:function(date){
								returnAmount=date;
								if(returnAmount==-1){
									top.layer.alert('该商品已申请过售后!', {icon: 0, title:'提醒'});
									return;
								}else if(returnAmount==-2){
									top.layer.alert('该订单还没有进行分销，请稍后再试!', {icon: 0, title:'提醒'});
									return;
								}
							},
							error:function(XMLHttpRequest,textStatus,errorThrown){
										    
							}
						});
					}
	
				}else{
					var totalA=$("#"+id+"total").val();
					var orderA=$("#"+id+"orderAmount").val();
					$("#totalAmount").val(totalA);
					$("#orderAmount").val(orderA);

					/* returnAmount=totalA/goodNum;
					var retNum=$("#returnNum").val();
					var SumAmount=returnAmount*retNum;//
					$("#returnAmount").val(SumAmount.toFixed(2)); */
					
				}
			}
			
		}
		
		//退款 换货 展示隐藏 退款数量
		function selectType(o){
			var type=$("#applyType").val();
			if(type==0){
				$("#hideandshow").show();
			}else if(type==1){
				$("#hideandshow").hide();
			}
		}
		
		//退款数量改变校验
		function returnChangeNum(){
			var num=$("#returnNum").val();
			if(parseInt(num)<=0){
				top.layer.alert('退货数量必须大于0，小于可申请的售后数量!', {icon: 0, title:'提醒'});
				return;
			}else if(parseInt(num)>parseInt(goodNum)){
				top.layer.alert('退货数量必须大于0，小于可申请的售后数量!', {icon: 0, title:'提醒'});
				return;
			}
		}
		
		 //虚拟商品的售后次数校验
		function returnChangeTimes(){
		   var st=$("#serviceTimes").val();
		   if(parseInt(st)<=0){
			   top.layer.alert('售后次数必须大于0，小于剩余次数!', {icon: 0, title:'提醒'});
			   return;
		   }else if(parseInt(st)>parseInt(remaintimes)){
			   top.layer.alert('售后次数必须大于0，小于剩余次数!', {icon: 0, title:'提醒'});
			   return;
		   }
		}
		 
		//虚拟商品的退款金额校验
		function returnChangeAmount(){
			var ra=$("#returnAmount").val();
			if(parseFloat(ra)<0){
				top.layer.alert('退款金额必须大于等于0，小于实付款金额!', {icon: 0, title:'提醒'});
				return;
			}else if(parseFloat(totalAmount)<parseFloat(ra)){
				top.layer.alert('退款金额必须大于等于0，小于实付款金额!', {icon: 0, title:'提醒'});
				return;
			}
		}
		$(document).ready(function(){
			validateForm = $("#inputForm").validate({
				rules: {
					returnmoney:{
						number:true,
						min:0
						},
					bankno:{
						digits:true
					}
				},
				messages: {
					returnmoney:{
						number:"输入合法的价格",
						min:"价格最小为0"
						},
						bankno:{
							digits:"输入合法的银行卡号"
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
			
// 			laydate({
// 	            elem: '#userinfo.birthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
// 	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
// 	        });
			
			$("#belongOfficeButton").click(function(){
				// 是否限制选择，如果限制，设置为disabled
				if ($("#belongOfficeButton").hasClass("disabled")){
					return true;
				}
				// 正常打开	
				top.layer.open({
				    type: 2, 
				    area: ['300px', '420px'],
				    title:"选择部门",
				    ajaxData:{selectIds: $("#belongOfficeId").val()},
				    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/sys/office/treeData?type=2")+"&module=&checked=&extId=&isAll=&selectIds=" ,
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
									ids.push(nodes[i].id);
									names.push(nodes[i].name);//
									break; // 如果为非复选框选择，则返回第一个选择  
								}
								$("#belongOfficeId").val(ids.join(",").replace(/u_/ig,""));
								$("#belongOfficeName").val(names.join(","));
								$("#belongOfficeName").focus();
								top.layer.close(index);
								$("#belongUserId").val("");
								$("#belongUserName").val("");
						    	       },
		    	cancel: function(index){ //或者使用btn2
		    	           //按钮【按钮二】的回调
		    	       }
				}); 
			
			});
			
			$("#belongUserButton").click(function(){
				var belongOfficeId = $("#belongOfficeId").val();
				// 是否限制选择，如果限制，设置为disabled
				if ($("#belongUserButton").hasClass("disabled")){
					return true;
				}
				
				if(belongOfficeId == null || belongOfficeId == ""){
					top.layer.alert('请先选择归属机构!', {icon: 0, title:'提醒'});
				}else{
					// 正常打开	
					top.layer.open({
					    type: 2, 
					    area: ['300px', '420px'],
					    title:"选择人员",
					    ajaxData:{belongOfficeId:belongOfficeId},
					    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/sys/user/officeUserTreeData?belongOfficeId="+belongOfficeId),
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
										ids.push(nodes[i].id);
										names.push(nodes[i].name);//
										break; // 如果为非复选框选择，则返回第一个选择  
									}
									$("#belongUserId").val(ids.join(",").replace(/u_/ig,""));
									$("#belongUserName").val(names.join(","));
									$("#belongUserName").focus();
									top.layer.close(index);
							    	       },
			    	cancel: function(index){ //或者使用btn2
			    	           //按钮【按钮二】的回调
			    	       }
					}); 
				}
			});
		});
</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="ibox">
		<div class="ibox-content">
			<div class="clearfix">
				<form:form id="inputForm" modelAttribute="returnedGoods" action="${ctx}/ec/orders/saveReturn" method="post" class="form-horizontal">
					<form:hidden path="userId"/>
					<form:hidden path="orderId" value="${orders.orderid}"/>
					<form:hidden path="goodsMappingId"/>
					<label>订单号：</label>${orders.orderid}
					<p></p>
					<label>选择售后商品</label>
					<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">选择</th>
								<th style="text-align: center;">商品名称</th>
								<th style="text-align: center;">商品规格</th>
								<th style="text-align: center;">商品类型</th>
								<th style="text-align: center;">市场价</th>
								<th style="text-align: center;">优惠价</th>
								<th style="text-align: center;">系统价</th>
								<th style="text-align: center;">成交价</th>
								<th style="text-align: center;">购买数量</th>
								<th style="text-align: center;">实付款</th>
								<c:if test="${orders.isReal==1}">
									<th style="text-align: center;">实际服务单价</th>
									<th style="text-align: center;">截止时间</th>
									<th style="text-align: center;">剩余次数</th>
								</c:if>
								<th style="text-align: center;">欠款</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${orderGoodList}" var="orderGood">
								<tr>
									<td><input id="selectId" name="selectId" type="radio" value="${orderGood.recid}"  class="form required"  onchange="selectFunction(this)"/></td>
									<td style="text-align: center;">${orderGood.goodsname}</td>
									<td style="text-align: center;">${orderGood.speckeyname }</td>
									<td  style="text-align: center;">
										<c:if test="${orderGood.isreal==0}">
											实物
										</c:if>
										<c:if test="${orderGood.isreal==1}">
											虚拟
										</c:if>
									</td>
									<td  style="text-align: center;">${orderGood.marketprice}</td>
									<td  style="text-align: center;">${orderGood.goodsprice}</td>
									<td  style="text-align: center;">${orderGood.costprice}</td>
									<td  style="text-align: center;">${orderGood.orderAmount}</td>
									<td  style="text-align: center;">${orderGood.goodsnum}</td>
									<td  style="text-align: center;">${orderGood.totalAmount}</td>
									<c:if test="${orders.isReal==1 && orderGood.servicetimes == 999}">
										<td  style="text-align: center;"></td>
										<td  style="text-align: center;">${orderGood.expiringdate}</td>
										<td  style="text-align: center;"></td>
									</c:if>
									<c:if test="${orders.isReal==1 && orderGood.servicetimes != 999}">
										<td  style="text-align: center;">${orderGood.singleRealityPrice}</td>
										<td  style="text-align: center;"></td>
										<td  style="text-align: center;">${orderGood.remaintimes}</td>
									</c:if>
									<td  style="text-align: center;">${orderGood.orderArrearage}</td>
								</tr>
								<input type="hidden" id="${orderGood.recid}goodsnum" name="${orderGood.recid}goodsnum" value="${orderGood.goodsnum}" />
								<input type="hidden" id="${orderGood.recid}total" name="${orderGood.recid}total" value="${orderGood.totalAmount}" />
								<input type="hidden" id="${orderGood.recid}singleRealityPrice" name="${orderGood.recid}singleRealityPrice" value="${orderGood.singleRealityPrice}" />
								<input type="hidden" id="${orderGood.recid}remaintimes" name="${orderGood.recid}remaintimes" value="${orderGood.remaintimes}" />
								<input type="hidden" id="${orderGood.recid}orderAmount" name="${orderGood.recid}orderAmount" value="${orderGood.orderAmount}" />
								<input type="hidden" id="${orderGood.recid}orderArrearage" name="${orderGood.recid}orderArrearage" value="${orderGood.orderArrearage}" />
								<c:if test="${orderGood.isreal==0}">
									<input type="hidden" id="${orderGood.recid}returnedGoodsNum" value="${orderGood.returnNum}" />
								</c:if>
								<!-- 时限卡售后添加字段  -->
								<input type="hidden" id="${orderGood.recid}servicetimes" name="${orderGood.recid}servicetimes" value="${orderGood.servicetimes}" />
								<input type="hidden" id="${orderGood.recid}advanceFlag" name="${orderGood.recid}advanceFlag" value="${orderGood.advanceFlag}" />
							</c:forEach>
						</tbody>						
					</table>
					<p></p>					
				  	<label><font color="red">*</font>售后原因：</label>
			       	<form:input path="returnReason" htmlEscape="false" maxlength="50" style="width: 300px;height:30px;" class="form-control required"/>
			       	<p></p>
			        <label><font color="red">*</font>问题描述：</label>
			        <form:textarea path="problemDesc" htmlEscape="false" rows="3"  style="width:300px;" maxlength="200" class="form-control required"/>
			        <p></p>
					<table id="contentTable" class="table-condensed dataTable no-footer">
						<tr>
							<td>
								<label><font color="red">*</font>归属机构：</label>
							</td>
							<td width="300px" height="30px">
								<input id="belongOfficeId" class=" form-control input-sm" name="belongOfficeId" value="" type="hidden">
								<div class="input-group">
									<input id="belongOfficeName" class=" form-control required input-sm" name="belongOfficeName" readonly="readonly" value="" data-msg-required="" style="" type="text">
										<span class="input-group-btn">
											<button id="belongOfficeButton" class="btn btn-sm btn-primary " type="button">
												<i class="fa fa-search"></i>
											</button>
										</span>
								</div>
								<label id="belongOfficeName-error" class="error" for="belongOfficeName" style="display:none"></label>
							</td>	
						</tr>
						<tr>
							<td>
								<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;归属人：</label>
							</td>
							<td width="300px" height="30px">
								<input id="belongUserId" class=" form-control input-sm" name="belongUserId" value="" type="hidden">
								<div class="input-group">
									<input id="belongUserName" class=" form-control input-sm" name="belongUserName" readonly="readonly" value="" data-msg-required="" style="" type="text">
										<span class="input-group-btn">
											<button id="belongUserButton" class="btn btn-sm btn-primary " type="button">
												<i class="fa fa-search"></i>
											</button>
										</span>
								</div>
								<label id="belongUserName-error" class="error" for="belongUserName" style="display:none"></label>
							</td>
						</tr>
					</table>
					<p></p>
			        <label><font color="red">*</font>申请类型：</label>
			        <c:if test="${orders.isReal==0}">
			        	<form:select path="applyType" class="form-control" style="width:185px;" onchange="selectType(this)">
							<form:option value="0">退货并退款</form:option>
							<form:option value="1">仅换货</form:option>
						</form:select>
			        </c:if>
			        <c:if test="${orders.isReal==1}">
			        	<form:select path="applyType" class="form-control" style="width:185px;" >
							<form:option value="0">退货并退款</form:option>
						</form:select>
			        </c:if>
					<p></p>
					<form:hidden path="orderAmount"/>
			        <label><font color="red">*</font>支付金额：</label>
			        <form:input path="totalAmount" htmlEscape="false" maxlength="10" style="width: 180px;height:30px;" class="form-control" readonly="true"/>
					<p></p>
					<c:if test="${orders.isReal==0}">
				        <label><font color="red">*</font>售后商品数量：</label>
				        <form:input path="returnNum" htmlEscape="false" maxlength="10" style="width: 180px;height:30px;" class="form-control"
				        onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
						onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
						onfocus="if(value == '0'){value=''}"
						onblur="if(value == ''){value='0'}"
				        onchange="returnChangeNum()"/>
			        </c:if>
					<c:if test="${orders.isReal==1}">
					<div id="d1serviceTimes">
						<p></p>
						<label><font color="red">*</font>售后次数：</label>
			        	<form:input path="serviceTimes" htmlEscape="false" maxlength="10" style="width: 180px;height:30px;" class="form-control" 
			        	onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
						onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
						onfocus="if(value == '0'){value=''}"
						onblur="if(value == ''){value='0'}"
			        	onchange="returnChangeTimes()"/>
			        </div>
			        </c:if>
					<p></p>
					<div id="hideandshow" style="display: display">
						<label><font color="red">*</font>退款金额：</label>
						<form:input path="returnAmount" htmlEscape="false" maxlength="10"  style="width:180px;" class="form-control required"
						onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
						onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
						onfocus="if(value == '0.0'){value=''}"
						onblur="if(value == ''){value='0.0'}"
						onchange="returnChangeAmount()"/>
					</div>
			       
			        <p></p>
					<label>客服备注：</label>
					<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" style="width:300px;" class="form-control"/>
				</form:form>
			</div>
		</div>
	</div>
</div>
</body>
</html>