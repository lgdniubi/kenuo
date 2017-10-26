<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>退货录入</title>
	<meta name="decorator" content="default"/>
	<!-- 内容上传 引用-->

<script type="text/javascript">
		var orderArrearage=0;
		var validateForm;
		var flag;
		var goodsNum = 0;//实物售后数量校验用
		var flagNum = false;//实物售后数量校验用
		var totalAmount;
		var advanceFlag;
		
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  if(flag){
				  top.layer.alert('当前商品已申请售后!', {icon: 0, title:'提醒'});
				  return;
			  }
			  var recid=$("#goodsMappingId").val();
			  if(recid==""){
				  top.layer.alert('请选择退货商品!', {icon: 0, title:'提醒'});
				  return;
			  }
			  //订单存在欠款
			  if(orderArrearage>0){
				  top.layer.alert('当前订单有欠款,无法退款(请先补齐欠款)', {icon: 0, title:'提醒'});
				  return;
			  }
			  //订单存在预约金,先处理预约金
			  if(advanceFlag == 1){
				  top.layer.alert('当前订单有预约金,无法退款(请先处理预约金)', {icon: 0, title:'提醒'});
				  return;
			  }
			  
			  
			  //校验实物售后数量的准确
			  for(var i=0;i<goodsNum;i++){
				  var newNum = $("#returnNums"+i).val();
				  var oldNum = $("#oldreturnNums"+i).val();
				  if(parseInt(newNum) < 0){
					  flagNum = true;
				  }else if(parseInt(newNum) > parseInt(oldNum)){
					  flagNum = true;
				  }else{
					  flagNum = false;
				  }
				  if(flagNum){
					  top.layer.alert('售后数量必须大于等于0，小于等于购买数量!', {icon: 0, title:'提醒'});
					  return;
				  }
			  }
			  //退款金额校验
			  var ra=$("#returnAmount").val();
			  if(parseFloat(ra)<0){
				  top.layer.alert('退款金额必须大于等于0，小于等于支付金额!', {icon: 0, title:'提醒'});
				  return;
			  }else if(parseFloat(totalAmount) < parseFloat(ra)){
				  top.layer.alert('退款金额必须大于等于0，小于等于支付金额!', {icon: 0, title:'提醒'});
				  return;
			  }
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		
		//选中某个商品时,查询数据
		function selectFunction(o){
			var groupId;
			var recid=$("#selectId:checked").val();
			var orderId=$("#orderId").val();//订单id
			var orderAmount=$("#"+recid+"orderAmount").val();//应付款
			
			totalAmount=$("#"+recid+"totalAmount").val();//实付款
			advanceFlag=$("#"+recid+"advanceFlag").val();//是否预约金
			orderArrearage=$("#"+recid+"orderArrearage").val();//欠款
			$("#goodsMappingId").val(recid);//为goodsMappingId赋值
			
			//判断订单是否存在预约金
			if(advanceFlag == 1){
				top.layer.alert('当前订单有预约金,无法退款(请先处理预约金)', {icon: 0, title:'提醒'});
				return;
			}
			//判断是否存在欠款
			if(parseInt(orderArrearage)>0){
				top.layer.alert('当前订单有欠款,无法退款(请先补齐欠款)', {icon: 0, title:'提醒'});
			}else{
				//判断商品是否售后    (是:正在售后    或者   已经售后)
				$.ajax({
					type:"post",
					async:false,
					data:{
						orderId:orderId,
						goodsMappingId:recid,
					},
					url:"${ctx}/ec/orders/getReturnGoodsNum",
					success:function(obj){
						flag = obj;
						if(flag){//正在售后    或者   已经售后
							top.layer.alert('当前商品已申请售后', {icon: 0, title:'提醒'});
							return;
						}else{
							$("#addReal").empty();
							$.ajax({
								type:"post",
								async:false,
								data:{
									recid:recid,
								},
								url:"${ctx}/ec/orders/getOrderGoodsCard",
								success:function(date){
									if(date!=null && date!=""){
										for(var i in date){
											if(date[i].isreal == 0){
												$("<label><font color='red'>*</font>"+date[i].goodsname+"    售后数量：</label><input id='recIds' name='recIds' value='"+date[i].recid+"' type='hidden'/><input id='returnNums"+goodsNum+"' name='returnNums' value='"+date[i].goodsnum+"' style='width:180px;' class='form-control required' onblur='findReturnNum(this,"+date[i].goodsnum+")'/><input id='oldreturnNums"+goodsNum+"' value='"+date[i].goodsnum+"' type='hidden'/> <p></p>").appendTo($("#addReal"));
												goodsNum++;
											}
										}
									}
								},
								error:function(XMLHttpRequest,textStatus,errorThrown){
											    
								}
							});
						}
					},
					error:function(XMLHttpRequest,textStatus,errorThrown){
								    
					}
				});
				$("#orderAmount").val(orderAmount);
				$("#totalAmount").val(totalAmount);
			}
		}
		//实物售后数量校验
		function findReturnNum (id,num){
			var num1=$(id).val();
			if(parseInt(num1)<0){
				top.layer.alert('售后数量必须大于等于0，小于等于购买数量!', {icon: 0, title:'提醒'});
				return;
			}else if(parseInt(num1) > num){
				top.layer.alert('售后数量必须大于等于0，小于等于购买数量!', {icon: 0, title:'提醒'});
				return;
			}
		}
		//虚拟商品的退款金额校验
		function returnChangeAmount(){
			var ra=$("#returnAmount").val();
			if(parseFloat(ra)<0){
				top.layer.alert('退款金额必须大于等于0，小于等于支付金额!', {icon: 0, title:'提醒'});
				return;
			}else if(parseFloat(totalAmount) < parseFloat(ra)){
				top.layer.alert('退款金额必须大于等于0，小于等于支付金额!', {icon: 0, title:'提醒'});
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
					<form:hidden path="isReal" value="${orders.isReal}"/>
					<label>订单号：</label>${orders.orderid}
					<p></p>
					<label>选择售后商品</label>
					<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">选择</th>
								<th style="text-align: center;">商品名称</th>
								<th style="text-align: center;">商品类型</th>
								<c:if test="${orders.isReal == 3}">
									<th style="text-align: center;">商品规格</th>
								</c:if>
								<th style="text-align: center;">子项名称</th>
								<th style="text-align: center;">子项类型</th>
								<th style="text-align: center;">市场价</th>
								<th style="text-align: center;">优惠价</th>
								<th style="text-align: center;">成本价</th>
								<th style="text-align: center;">购买数量</th>
								<th style="text-align: center;">应付款</th>
								<th style="text-align: center;">实付款</th>
								<th style="text-align: center;">欠款</th>
							</tr>
						</thead>
						<tbody>
							${suitCardSons}
						</tbody>						
					</table>
					<p></p>					
				  	<label><font color="red">*</font>售后原因：</label>
			       	<form:input path="returnReason" htmlEscape="false" maxlength="50" style="width: 300px;height:30px;" class="form-control required"/>
			       	<p></p>
			        <label><font color="red">*</font>问题描述：</label>
			        <form:textarea path="problemDesc" htmlEscape="false" rows="3"  style="width:300px;" maxlength="200" class="form-control required"/>
			        <p></p>
			        <table>
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
		        	<form:select path="applyType" class="form-control" style="width:185px;" >
						<form:option value="0">退货并退款</form:option>
					</form:select>
					<p></p>
					<div id="addReal"></div>
					<p></p>
					<form:hidden path="orderAmount"/>
			        <label><font color="red">*</font>支付金额：</label>
			        <form:input path="totalAmount" htmlEscape="false" maxlength="10" style="width: 180px;height:30px;" class="form-control" readonly="true"/>
					<p></p>
					<label><font color="red">*</font>退款金额：</label>
					<form:input path="returnAmount" htmlEscape="false" maxlength="10"  style="width:180px;" class="form-control required"
					onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
					onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
					onfocus="if(value == '0.0'){value=''}"
					onblur="if(value == ''){value='0.0'}"
					onchange="returnChangeAmount()"/>
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