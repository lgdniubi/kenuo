<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>订单修改</title>
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
			var status='${orders.orderstatus}';
			if(status>0){
				$("#orderamount").attr("readonly",true);
			}
			
		}
		
		jQuery.validator.addMethod("lrunlv", function(value, element) {     
		    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
		}, "小数位不能超过三位"); 
		
		$(document).ready(function() {

			var shippingtime = {
				    elem: '#shippingtime',
				    format: 'YYYY-MM-DD hh:mm:ss',
				    event: 'focus',
				    istime: true,				//是否显示时间
				    isclear: true,				//是否显示清除
				    istoday: true,				//是否显示今天
				    issure: true,				//是否显示确定
				    festival: true				//是否显示节日
				
				};
			
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
// 					mobile:{
// 						digits:"输入合法手机号",
// 						minlength:"手机号码要11位"
						
// 					}
				},
				submitHandler: function(form){
						//alert("提交")
						loading('正在提交，请稍等...');
					//	$(".loading").show();
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
			$("#inputForm").validate().element($("#orderamount"));
			laydate(shippingtime);
	  
	    });
			
	    window.onload=initStatus;	
</script>
	
</head>



<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
			<div class="ibox-content">
				<div class="clearfix">
				<form:form id="inputForm" modelAttribute="orders" action="${ctx}/ec/orders/save" method="post" class="form-horizontal">
						<form:hidden path="userid"/>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
						   <tbody>
						      <tr>
						         <td class="active" style="width:100px"><label class="pull-right">订单号:</label></td>
						         <td id="tdinput" style="width:300px">
									 <form:input path="orderid" htmlEscape="false" maxlength="50"  class="form-control"  readonly="true"/>
								 </td> 
							
						      	 <td class="active" style="width:100px"><label class="pull-right">用户名:</label></td>
						         <td style="width:300px"><form:input path="username" htmlEscape="false" maxlength="64" class="form-control" readonly="true"/></td>
						      	
						      	</tr>
						      	<tr>
						      	
						      	 <td class="active"><label class="pull-right">下单时间:</label></td>
						         <td><input id="addtime" name="addtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
										value="<fmt:formatDate value="${orders.addtime}" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly="true"/>
						         </td>

						      	 <td class="active"><label class="pull-right">支付时间:</label></td>
						         <td>
						        	 <input id="paytime" name="paytime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
										value="<fmt:formatDate value="${orders.paytime}" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly="true"/>
						     		</td>
						      	
						      	</tr>
						      	<tr>
						      	 <td class="active"><label class="pull-right">付款方式:</label></td>
						        <td>
						         		<form:select path="paycode" class="form-control">
						         			<c:if test="${orders.orderstatus==-1}">
							         			<c:if test="${orders.paycode==null}">
							         				<form:option value=" "> </form:option>
							         			</c:if>
						         			</c:if>
						         			<c:forEach items="${paylist}" var="payment">
						         			<c:if test="${orders.orderstatus!=-1}">
						         				<c:if test="${payment.paycode==orders.paycode}">
						         				<form:option value="${payment.paycode}">${payment.payname}</form:option>
						         				</c:if>
						         			</c:if>	
						         			<c:if test="${orders.orderstatus==-1}">
						         				<form:option value="${payment.paycode}">${payment.payname}</form:option>
						         			</c:if>
						         			</c:forEach>
				 		      			</form:select>
						       </td>
						 
						      	<td class="active" ><label class="pull-right"><font color="red">*</font>订单状态:</label></td>
						      	<td>
						      		<input id="oldstatus" name="oldstatus" type="hidden" value="${orders.orderstatus}">
						      		<c:if test="${orders.orderstatus<0}">
						      		<form:select path="orderstatus"  class="form-control">
										<form:option value="-2">取消订单</form:option>
										<form:option value="-1">未付款</form:option>
										<form:option value="1">已付款</form:option>
										<form:option value="2">已发货</form:option>
										<form:option value="4">已完成</form:option>
									</form:select>
									</c:if>
									<c:if test="${orders.orderstatus==3}">
						      		<form:select path="orderstatus"  class="form-control">
										<form:option value="3">已退款</form:option>
									</form:select>
									</c:if>
									<c:if test="${orders.orderstatus==5}">
						      		<form:select path="orderstatus"  class="form-control">
										<form:option value="5">申请退款</form:option>
									</form:select>
									</c:if>
									<c:if test="${orders.orderstatus==1}">
						      		<form:select path="orderstatus"  class="form-control">
										<form:option value="1">已付款</form:option>
										<form:option value="2">已发货</form:option>
										<form:option value="4">已完成</form:option>
									</form:select>
									</c:if>
									<c:if test="${orders.orderstatus==2}">
						      		<form:select path="orderstatus"  class="form-control">
										<form:option value="1">已付款</form:option>
										<form:option value="2">已发货</form:option>
										<form:option value="4">已完成</form:option>
									</form:select>
									</c:if>
									<c:if test="${orders.orderstatus==4}">
						      		<form:select path="orderstatus"  class="form-control">
										<form:option value="1">已付款</form:option>
										<form:option value="2">已发货</form:option>
										<form:option value="4">已完成</form:option>
									</form:select>
									</c:if>
						      	</td>
						      	</tr>
						      	<tr>
						      	<td class="active" style="width:100px"><label class="pull-right">支付订单号:</label></td>
						         <td id="tdinput" style="width:300px">
									 <form:input path="tempOrderId" htmlEscape="false" maxlength="50"  class="form-control"  readonly="true"/>
								 </td> 
						      	<td class="active"><label class="pull-right">优惠价格:</label></td>
						         <td>
						         	<form:input path="orderamount" htmlEscape="false" maxlength="10" class="form-control" /><font color="red" class="pull-right">订单总价：${orders.totalamount}</font>
						         </td>
						     	 </tr>
								
						       <tr>
						         <td class="active"><label class="pull-right">发货时间:</label></td>
						         <td><input id="shippingtime" name="shippingtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
										value="<fmt:formatDate value="${orders.shippingtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
								</td>
						  
						      	 <td class="active"><label class="pull-right">快递单号:</label></td>
						         <td><form:input path="shippingcode" htmlEscape="false" maxlength="30" class="form-control"/></td>
						      
						      </tr>
						      <tr>
						         <td class="active"><label class="pull-right">快递公司:</label></td>
						         <td><form:input path="shippingname" htmlEscape="false" maxlength="30" class="form-control"/></td>
						  
						      	 <td class="active"><label class="pull-right">地址信息</label></td>
						         <td>
						          <sys:treeselect id="qucity" name="city" value="${orders.city}" labelName="cityname" labelValue="${orders.cityname}"
											title="区域" url="/sys/area/treeData" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
									<font color="red">区域选择精确到县区级</font>
						         </td>
						      
						      </tr>
						      <tr>
						         
						         <td class="active"><label class="pull-right">收货人姓名:</label></td>
						         <td>
						         	<form:input path="consignee" htmlEscape="false" maxlength="10" class="form-control"/>
								 </td>
								 
						         <td class="active"><label class="pull-right">街道地址:</label></td>
						         <td>
						         	<form:input path="address" htmlEscape="false" maxlength="50" class="form-control"/>
								 </td>
						      </tr>
						    
						      <tr>
						      	<td class="active"><label class="pull-right">邮政编码：</label></td>
						      	<td>
						      		<form:input path="postalcode" htmlEscape="false" maxlength="20" class="form-control"/>
						      	</td>
						         <td class="active"><label class="pull-right">手机号码:</label></td>
						         <td>
						         	<form:input path="mobile" htmlEscape="false" maxlength="20" class="form-control"/>
								 </td>
						         
						      </tr>
						      
						      <tr>
						      
						         <td class="active"><label class="pull-right">固定电话:</label></td>
						         <td>
						         	<form:input path="phone" htmlEscape="false" maxlength="20" class="form-control"/>
								 </td>
								 <td class="active"><label class="pull-right">客户留言:</label></td>
						         <td><form:textarea path="usernote" htmlEscape="false" rows="3" maxlength="200" class="form-control" readonly="true"/></td>
						        
						     </tr>
						     <tr>
						     	 <td class="active"><label class="pull-right">备注:</label></td>
						         <td>
						         	<form:textarea path="adminnote"  htmlEscape="false" rows="3" maxlength="200" class="form-control" ></form:textarea>
						         </td>
						     </tr>
						  </tbody>
						  </table> 
						  <div>
						  </div> 
					</form:form>
					<label class="active">操作日志:</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<shiro:hasPermission name="ec:orders:edit">
							<a href="#" onclick="openDialogView('操作日志', '${ctx}/ec/orders/loglist?id=${orders.orderid}','500px','400px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>操作日志</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="ec:orders:edit">
							<a href="#" onclick="openDialogView('操作日志', '${ctx}/ec/orders/orderlist?id=${orders.orderid}','900px','450px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>订单流程</a>
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
					</table>
					</div>
				</div>	
		  </div>
	 </div>	
    <div class="loading"></div>
</body>
</html>