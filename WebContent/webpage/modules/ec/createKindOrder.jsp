<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<head>
<title>创建订单</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/jquery/jquery.alerts.js"></script>
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/jquery/jquery.alerts.css">
<style>
	#Ichecks{display: inline-block;width: 17px;height: 17px;background: none;vertical-align: middle;margin-top: -1px;margin-right: 2px;}
</style>
<!-- 内容上传 引用-->
<script type="text/javascript">
		var cateid = 0;  // 分类的id
		var areaId=0;
		var discount=1;
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			var goodselectIds = $("#goodselectIds").val(); 
			if(goodselectIds == undefined){
				top.layer.alert('商品信息不能为空!', {icon: 0, title:'提醒'}); 
				return;
			}
			$("#inputForm").submit();
			 return true;	
		  }
	
		  return false;
		}

		//添加商品
		function addActionGoods(){
			var isNeworder = $("#isNeworder option:selected").val();
			top.layer.open({
			    type: 2, 
			    area: ['800px', '550px'],
			    title:"添加商品",
			    content: "${ctx}/ec/orders/addKindOderGoods?isNeworder="+isNeworder,
			    btn: ['确定', '关闭'],
			    yes: function(index, layero){
			        var obj =  layero.find("iframe")[0].contentWindow;
					var goodselectId = obj.document.getElementById("goodselectId");		//商品id
			    	var goodselectName=obj.document.getElementById("goodselectName");	//商品名称
					var speckey=obj.document.getElementById("speckey");					//规格key
					var speckeyname=obj.document.getElementById("speckeyname");			//规格名称
					var marketPrice=obj.document.getElementById("marketPrice");			//市场价格
					var goodsPrice=obj.document.getElementById("goodsPrice");			//优惠价格
					var costPrice=obj.document.getElementById("costPrice");				//系统价
					var specgoods=obj.document.getElementById("specgoods");				//规格选择结果
					var orderAmount = obj.document.getElementById("orderAmount"); 		//成交价
					var goodsNum=obj.document.getElementById("goodsNum");		  		//购买数量
					var actualPayment = obj.document.getElementById("actualPayment");	//实际付款
					var oldactualNum = obj.document.getElementById("oldremaintimes");	//老订单本身付款享有剩余服务次数
					var debtMoney = obj.document.getElementById("debtMoney");			//欠款
					var spareMoney = obj.document.getElementById("spareMoney");			//余款
					var isNeworderSon = obj.document.getElementById("isNeworderSon");	//子弹出层的订单
					var computType = obj.document.getElementById("computType");	//计算费用 0计算过 1未计算
					var r =/^\d+\.?\d{0,2}$/;
					var reg=/^\+?[1-9]\d*$/;
					if($(computType).val() == 1){
						top.layer.alert('请计算费用!', {icon: 0, title:'提醒'}); 
						return;
					}
					if($(specgoods).val()==0){
						top.layer.alert('请选择规格!', {icon: 0, title:'提醒'}); 
						return;
					}
					if($(goodselectName).val()==""){
						top.layer.alert('商品不能为空!', {icon: 0, title:'提醒'}); 
						  return;
					}
					if($(orderAmount).val()<0){
						top.layer.alert('订单金额要大于等于0!', {icon: 0, title:'提醒'}); 
						return;
					}
					var goodstolprice=changeTwoDecimal_f($(goodsNum).val()*$(goodsPrice).val()*discount);
					var invoiceContent = $("#invoiceContent").val();
					invoiceContent = invoiceContent +"商品名称："+ $(goodselectName).val()+" 商品数量:1 商品价格："+$(goodsPrice).val()+";";
					$("#invoiceContent").val(invoiceContent);
					$("<tr> "+
						"<td> "+$(goodselectName).val()+"<input id='goodselectIds' name='goodselectIds' type='hidden' value='"+$(goodselectId).val()+"'></td> "+
						"<td> "+$(speckeyname).val()+"<input id='speckeys' name='speckeys' type='hidden' value='"+$(speckey).val()+"'></td> "+
						"<td> "+$(marketPrice).val()+"</td> "+
						"<td> "+$(goodsPrice).val()+"</td> "+
						"<td> "+$(costPrice).val()+"</td> "+
						"<td> "+$(orderAmount).val()+"<input id='orderAmounts' name='orderAmounts' type='hidden' value='"+$(orderAmount).val()+"'></td> "+
						"<td> "+$(goodsNum).val()+"<input id='goodsnum' name='kindgoodsnum' type='hidden' value='"+$(goodsNum).val()+"'></td> "+
						"<td> "+$(actualPayment).val()+"<input id='actualPayments' name='actualPayments' type='hidden' value='"+$(actualPayment).val()+"'></td> "+
						"<td> "+$(spareMoney).val()+"</td> "+
						"<td> "+$(debtMoney).val()+"</td> "+
						"<td> "+
							"<a href='#' class='btn btn-danger btn-xs' onclick='delFile(this,"+$(costPrice).val()+","+$(orderAmount).val()+","+$(spareMoney).val()+","+$(actualPayment).val()+","+$(debtMoney).val()+")'><i class='fa fa-trash'></i> 删除</a> "+
						"</td>"+
					"</tr>").appendTo($("#addZTD"));
					
				var goodsprice =parseFloat($("#goodsprice").val())+parseFloat($(costPrice).val());
				$("#goodsprice").val(changeTwoDecimal_f(goodsprice));
				
				var orderamount =parseFloat($("#orderamount").val())+parseFloat($(orderAmount).val());	//成交总额
				$("#orderamount").val(changeTwoDecimal_f(orderamount));
				
				var orderBalance =parseFloat($("#orderBalance").val())+parseFloat($(spareMoney).val()); //总余额
				$("#orderBalance").val(changeTwoDecimal_f(orderBalance));
				
				var totalamount =parseFloat($("#totalamount").val())+parseFloat($(actualPayment).val());	//实付总额
				$("#totalamount").val(changeTwoDecimal_f(totalamount));
				
				var orderArrearage =parseFloat($("#orderArrearage").val())+parseFloat($(debtMoney).val()); //总欠款
				$("#orderArrearage").val(changeTwoDecimal_f(orderArrearage));
				$("#invoiceAmount").val(changeTwoDecimal_f(totalamount));//发票总金额
				//通过子页面的订单来锁定父页面订单为不可变的
				$("#isNeworder").attr("disabled",true);
				$("#_isNeworder").val(isNeworder);
				top.layer.close(index);
				
				if(orderArrearage != 0 || isNeworder == 1){ //欠款或者为老订单时无法开发票
					$("#iType").hide();
					$("#personheadContent").hide();
					$("#invoiceRecipient").hide();
					$("#companyheadContent").hide();
					$("#fpinfo").hide();
					$("#Ichecks").attr("checked",false);
					$("#Ichecks").attr("disabled",true);
				}else{
					$("#Ichecks").attr("disabled",false);
				}
			},
			cancel: function(index){ //或者使用btn2
				    	           //按钮【按钮二】的回调
			}
		}); 

		}
		//数字计算保留2位小数
		changeTwoDecimal_f= function (floatvar){
			var f_x = parseFloat(floatvar);
			if (isNaN(f_x))
			{
				alert('function:changeTwoDecimal->parameter error');
				return false;
			}
			var f_x = Math.round(floatvar*100)/100;
			var s_x = f_x.toString();
			var pos_decimal = s_x.indexOf('.');
			if (pos_decimal < 0){
				pos_decimal = s_x.length;
				s_x += '.';
			}
			while (s_x.length <= pos_decimal + 2){
				s_x += '0';
			}
			return s_x;
		}
		
		
		//删除tr
	function delFile(obj,costPrice,orderAmount,spareMoney,actualPayment,debtMoney){
		$(obj).parent().parent().remove();
		$("#goodsprice").val($("#goodsprice").val()-costPrice); //系统总额
		$("#orderamount").val($("#orderamount").val()-orderAmount); //成交总价
		$("#orderBalance").val($("#orderBalance").val()-spareMoney); //总余额
		$("#totalamount").val($("#totalamount").val()-actualPayment); //实付总额
		$("#orderArrearage").val($("#orderArrearage").val()-debtMoney)//总欠款
		var speckey = $("#speckey").val();
		if(speckey == undefined){
			$("#isNeworder").attr("disabled",false);
		}
		var orderArrearage = $("#orderArrearage").val();
		if(orderArrearage == 0){
			$("#Ichecks").attr("disabled",false);
		}
	}
       
	$(document).ready(function(){
		$("#defaultRadio").click(function(){
			$("#recipientsName").val($("#consignee").val());
			$("#recipientsPhone").val($("#phone").val());
			$("#recipientsAddress").val($("#address").val());
		});
		$("#newRadio").click(function(){
			$("#recipientsName").val("");
			$("#recipientsPhone").val("");
			$("#recipientsAddress").val("");
		});
		//物流类型来判断是否显示物流地址
		$("#shippingtype").change(function(){
			var shippingtype = $(this).val();
			if(shippingtype == 0 || shippingtype == 1){
				$("#logistics").show();
			}else{
				$("#logistics").hide();
			}
		});
		
		//索要发票验证方法
		invoiceVerify();
		
		
		// 手机号码验证
		jQuery.validator.addMethod("isMobile", function(value, element) {
		    var length = value.length;
		    var mobile =  /^(133[0-9]{8})|(153[0-9]{8})|(180[0-9]{8})|(181[0-9]{8})|(189[0-9]{8})|(177[0-9]{8})|(130[0-9]{8})|(131[0-9]{8})|(132[0-9]{8})|(155[0-9]{8})|(156[0-9]{8})|(185[0-9]{8})|(186[0-9]{8})|(145[0-9]{8})|(170[0-9]{8})|(176[0-9]{8})|(134[0-9]{8})|(135[0-9]{8})|(136[0-9]{8})|(137[0-9]{8})|(138[0-9]{8})|(139[0-9]{8})|(150[0-9]{8})|(151[0-9]{8})|(152[0-9]{8})|(157[0-9]{8})|(158[0-9]{8})|(159[0-9]{8})|(182[0-9]{8})|(183[0-9]{8})|(184[0-9]{8})|(187[0-9]{8})|(188[0-9]{8})|(147[0-9]{8})|(178[0-9]{8})|(149[0-9]{8})|(173[0-9]{8})$/;           
		    return this.optional(element) || (length == 11 && mobile.test(value));
		}, "请正确填写您的手机号码");
		jQuery.validator.addMethod("lrunlv", function(value, element) {         
			   return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);         
		}, "小数位不能超过三位"); 
		validateForm = $("#inputForm").validate({
			/* rules:{
				mobile:{
					digits:true,
					minlength:11,
					isMobile : true,
					remote: "${ctx}/ec/mtmyuser/verifyPhone"
				},
				mobile1:{
					digits:true,
					minlength:11,
					isMobile : true
				}
			},
			messages:{
				mobile:{
					digits:"输入合法手机号",
					minlength:"手机号码要11位",
					isMobile :"请输入正确手机号",
					remote:"手机号不存在，不可创建订单"
				},
				mobile1:{
					digits:"输入合法手机号",
					minlength:"手机号码要11位",
					isMobile :"请输入正确手机号"
				}
			}, */
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
		//$("#inputForm").validate().element($("#phone"));
		
	});
	function selectUser(){
		var mobile = $("#mobile").val();
		
		if(mobile == ""){
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
				var user = date.users;
				if(user){
					top.layer.alert('昵称查询成功!', {icon: 0, title:'提醒'}); 
					$("#username").val(date.users.nickname);
					$("#userid").val(date.users.userid);
				}else{
					top.layer.alert('昵称查询失败!', {icon: 0, title:'提醒'}); 
				}
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
			}
		});
	}
	function getMtmyUserInfo(){
		if ($("#mtmyUserButton").hasClass("disabled")){
			return true;
		}
		// 正常打开	
		top.layer.open({
		    type: 2, 
		    area: ['550px', '420px'],
		    title:"提成人员选择",
		    ajaxData:{selectIds: $("#goodselectId").val()},
		    content: "${ctx}/ec/orders/getPushmoneyView",
		    btn: ['确定', '关闭']
    	    ,yes: function(index, layero){
    	    	var obj =  layero.find("iframe")[0].contentWindow;
    	    	var mtmyUserId = obj.document.getElementById("mtmyUserId").value; //员工id
    	    	var mtmyUserMobile = obj.document.getElementById("mtmyUserMobile").value; //员工电话
    	    	var mtmyUserName = obj.document.getElementById("mtmyUserName").value; //员工名称
    	    	var pushMoney = obj.document.getElementById("pushMoney").value; //提成金额
    	    	$("#mtmyUserInfo").append(
    	    			"<tr>"+
    					"<td>"+
    						"<input id='mtmyUserId' name='mtmyUserId' type='hidden' value='"+mtmyUserId+"' class='form-control' readonly='readonly'>"+
    						"<input id='mtmyUserName' name='mtmyUserName' type='text' value='"+mtmyUserName+"' class='form-control' readonly='readonly'>"+
    					"</td>"+
    					"<td><input id='mtmyUserMobile' name='mtmyUserMobile' type='text' value='"+mtmyUserMobile+"' class='form-control' readonly='readonly'></td>"+
    					"<td><input id='pushMoney' name='pushMoney' value='"+pushMoney+"' readonly='readonly' class='form-control required' type='text' class='form-control'></td>"+
    					"<td><a href='#' class='btn btn-danger btn-xs' onclick='delFile(this)'><i class='fa fa-trash'></i> 删除</a></td>"+
    					"</tr>"
    			);
				top.layer.close(index);
			}
		}); 
	}
	function getRemarks(){
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
    	    	$("#orderRemarks").append(
   	    			"<tr>"+
   					"<td align='center'>"+
   						"<div style='width:260px;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;'>"+remarks+"</div><input id='orderRemarks' name='orderRemarks' type='hidden' value='"+remarks+"' class='form-control' readonly='readonly'>"+
   					"</td>"+
   					"<td><a href='#' class='btn btn-danger btn-xs' onclick='delFile(this)'><i class='fa fa-trash'></i> 删除</a></td>"+
   					"</tr>"
    			);
				top.layer.close(index);
			}
		}); 
	}
	
	
	function invoiceVerify(){
		$("#Ichecks").attr("disabled",true);
		$("#Ichecks").val(0);
		$("#iType").hide();
		$("#personheadContent").hide();
		$("#companyheadContent").hide();
		$("#fpinfo").hide();
		$("#invoiceRecipient").hide();
		$("#Ichecks").click(function(){
			$("#invoiceRecipient").show();
			//选择索要发票以后 清空发票下所有文本框内容
			$("#fpinfo input").val("");
			$("#companyheadContent input").val("");
			$("#invoiceRecipient input").val("");
			if($(this).is(":checked")){
				$(this).val(1);
				$("#invoiceType").val(1);
				$("#invoiceType").attr("selected",true);
				var orderArrearage = $("#orderArrearage").val();
				if(orderArrearage == 0){
					$("#iType").show();  //发票类型
					if($("#invoiceType").val()==1){
						$("#personheadContent").show(); //发票个人抬头
						$("#companyContent").val(""); //发票公司抬头
					}
				}else{
					top.layer.alert('您当前有欠款不能打印发票!', {icon: 0, title:'提醒'}); 
					$(this).attr("checked",false);
				}
			}else{
				$(this).val(0);
				$("#iType").hide();
				$("#personheadContent").hide();
				$("#invoiceRecipient").hide();
				$("#fpinfo").hide();
				$("#companyheadContent").hide();
			}
		});
		//类型选择判断
		$("#invoiceType").change(function(){
			$("#invoiceRecipient").show();
			if($(this).val() == 1){
				$("#personheadContent").show();
				$("#companyheadContent").hide();
				$("#fpinfo").hide();
			}
			if($(this).val() == 2){
				$("#personheadContent").hide();
				$("#companyheadContent").show();
				$("#fpinfo").hide();
			}
			if($(this).val() == 3){
				$("#personheadContent").hide();
				$("#companyheadContent").show();
				$("#invoiceRecipient").show();
				$("#fpinfo").show();
			}
			$("#fpinfo input").val("");
			$("#companyheadContent input").val("");
			$("#invoiceRecipient input").val("");
		});
	}
	
	function choose(value){
		if(value == 1){
			$("#iType").hide();
			$("#personheadContent").hide();
			$("#invoiceRecipient").hide();
			$("#companyheadContent").hide();
			$("#fpinfo").hide();
			$("#Ichecks").attr("checked",false);
			$("#Ichecks").attr("disabled",true);
			$("#mtmyUserPush").hide();
			$("#mtmyUserInfo").empty();
		}else{
			$("#mtmyUserPush").show();
		}
	}
	</script>
</head>
<body>
	<div class="ibox-content">
		<div class="clearfix">
			<form:form id="inputForm" modelAttribute="orders" action="${ctx}/ec/orders/saveKindOrder" method="post" class="form-horizontal">
				<label><font color="red">*</font>手机号码：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="form-control required" style="width:200px" />&nbsp;&nbsp;
				<a href="#" onclick="selectUser()">查询昵称</a>
				<label><font color="red">*</font>昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：</label>
				<input id="username" name="username" type="text" class="form-control required" readonly="true" style="width:200px" />
				<input type="hidden" name="userid" id="userid" />
				<p></p>
				<label><font color="red">*</font>订单性质：</label>
				<form:select path="distinction"  class="form-control" style="width:200px">
						<form:option value="1">售前卖</form:option>
						<form:option value="2">售后卖</form:option>
						<form:option value="3">老带新</form:option>
				</form:select>&nbsp;&nbsp;
				<label><font color="red">*</font>新老订单：</label>
				<form:select id="isNeworder" path="isNeworder"  class="form-control" style="width:200px" onchange="choose(this.value)">
						<form:option value="0">新订单</form:option>
						<form:option value="1">老订单</form:option>
				</form:select>
				<input type="hidden" id="_isNeworder" name="isNeworder" />
				<p></p>
				<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
					<div class="pull-left">
						<h4>商品信息：</h4>
					</div>
					<div class="pull-right">
						<a href="#" onclick="addActionGoods()" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加商品</a>
					</div>
					<p></p>
					<table id="contentTable" class="table table-bordered table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<!-- <th style="text-align: center;">商品编号</th> -->
								<th style="text-align: center;">商品名称</th>
								<th style="text-align: center;">商品规格</th>
								<th style="text-align: center;">市场价</th>
								<th style="text-align: center;">优惠价</th>
								<th style="text-align: center;">系统价</th>
								<th style="text-align: center;">成交价</th>
								<th style="text-align: center;">购买数量</th>
								<th style="text-align: center;">实付款</th>
								<th style="text-align: center;">余款</th>
								<th style="text-align: center;">欠款</th>
								<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody id="addZTD" style="text-align:center;">	
						</tbody>
					</table>
				</div>
				<p></p>
				<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;"> 
					<label ><font color="red">*</font>系统总额：</label>
					<form:input id="goodsprice" path="goodsprice" readonly="true" htmlEscape="false" maxlength="11" class="form-control required" style="width:180px" />
					<label ><font color="red">*</font>成交总额：</label>
					<form:input id="orderamount" path="orderamount" readonly="true" htmlEscape="false" maxlength="11" class="form-control required" style="width:180px" />
					<label ><font color="red">*</font>总&nbsp;&nbsp;余&nbsp;&nbsp;额：</label>
					<form:input id="orderBalance" path="orderBalance" readonly="true" htmlEscape="false" maxlength="11" class="form-control required" style="width:180px" />
					<p></p>
					<label ><font color="red">*</font>实付总额：</label>
					<form:input id="totalamount" path="totalamount" readonly="true" htmlEscape="false" maxlength="11" class="form-control required" style="width:180px" />
					<label ><font color="red">*</font>总&nbsp;&nbsp;欠&nbsp;&nbsp;款：</label>
					<form:input id="orderArrearage" path="orderArrearage" readonly="true" htmlEscape="false" maxlength="11" class="form-control required" style="width:180px" />
					<p></p>
					<label ><font color="red">*</font>付款方式：</label>
					<form:select path="paycode" class="form-control" style="width:180px">
						<c:forEach items="${paylist}" var="payment">
							<form:option value="${payment.paycode}">${payment.paydesc}</form:option>
						</c:forEach>
					</form:select>
					<label ><font color="red">*</font>订单状态：</label>
					<form:select path="orderstatus"  class="form-control" style="width:180px">
							<form:option value="1">待发货</form:option>
							<form:option value="2">待收货</form:option>
							<form:option value="4">已完成</form:option>
					</form:select>
					<p></p>
					<label >留言备注：</label>
					<textarea name="usernote" rows="5" cols="60"></textarea>
				</div>
				<p></p>
				<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;"> 
					<label ><font color="red">*</font>物流类型：</label>
					<form:select path="shippingtype"  class="form-control" style="width:180px">
							<form:option value="0">快递发货</form:option>
						<%-- 	<form:option value="1">到店自取</form:option> --%>
							<form:option value="2">无需发货</form:option>
					</form:select>
					<div id="logistics">
						<p></p>
						<label ><font color="red">*</font>收&nbsp;&nbsp;货&nbsp;&nbsp;人：</label>
						<form:input path="consignee" htmlEscape="false" maxlength="11" class="form-control required" style="width:180px" />
						<label ><font color="red">*</font>联系电话：</label>
						<form:input path="phone" htmlEscape="false" maxlength="11" class="form-control required" style="width:180px" />
						<label ><font color="red">*</font>收货地址：</label>
						<form:input path="address" htmlEscape="false" maxlength="120" class="form-control required" style="width:180px" />
					</div>
				</div>
				<p></p>
				<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
					<input type="checkbox" id="Ichecks" name="Ichecks" /><label class="active">索要发票</label>
					<input type="hidden" name="invoiceContent" id="invoiceContent" />
					<input type="hidden" name="invoiceAmount" id="invoiceAmount" />
					<p></p>
					<div id="iType">
						<label class="active">发票类型：</label>
						<form:select path="invoiceType"  class="form-control" style="width:180px">
							<form:option value="1">个人普票</form:option>
							<form:option value="2">公司普票</form:option>
							<form:option value="3">公司专票</form:option>
						</form:select>
					</div>
					<p></p>
					<div id="personheadContent">
						<label class="active">发票抬头：</label>
						<label class="active">个人</label>
						<input type="hidden" name="personheadContent" value="个人" />
					</div>
					<div id="companyheadContent">
						<label class="active">发票抬头：</label>
						<label class="active">公司名称：</label><input type="text" name="companyheadContent" class="form-control" style="width:180px" />
						<p></p>
					</div>
					<div id="fpinfo">
						<label class="active">纳税人识别号：</label><input type="text" name="taxNum" class="form-control" style="width:180px" />
						<label class="active">银行账号：</label><input type="text" name="bankNo" class="form-control" style="width:180px" />
						<p></p>
						<label class="active">开&nbsp;户&nbsp;银&nbsp;行：</label>
						<input type="text" name="bankName" class="form-control" style="width:180px" />
						<label class="active">电&nbsp;&nbsp;话：</label>
						<input type="text" name="invoicePhone" class="form-control" style="width:180px" maxlength="11" />
						<label class="active">地&nbsp;&nbsp;址：</label>
						<input type="text" name="invoiceAddress" class="form-control" style="width:180px" maxlength="50" />
					</div>
					<P></P>
					<div id="invoiceRecipient">
						<label class="active">发票收货地址：</label>
						<p></p>
						<input type="radio" name="invoice" id="newRadio" value="2" />新增地址<input type="radio" id="defaultRadio" name="invoice" value="1" />默认使用商品收货地址  
						<p></p>
						<label class="active"><font color="red">*</font>收货人：</label>
						<input type="text" id="recipientsName" name="recipientsName" class="form-control" style="width:180px" />
						<label class="active"><font color="red">*</font>联系电话：</label>
						<input type="text" id="recipientsPhone" name="recipientsPhone" class="form-control" style="width:180px" />
						<label class="active"><font color="red">*</font>收货地址：</label>
						<input type="text" id="recipientsAddress" name="recipientsAddress" class="form-control" style="width:180px" />
					</div>
				</div>
				<p></p>
				<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;" id="mtmyUserPush">
					<div class="pull-left">
						<h4>人员提成信息：</h4>
					</div>
					<div class="pull-right">
						<a href="#" onclick="getMtmyUserInfo()" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加业务员</a>
					</div>
					<p></p>
					<table id="contentTable" class="table table-bordered table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">业务员</th>
								<th style="text-align: center;">手机号</th>
								<th style="text-align: center;">提成金额</th>
								<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody id="mtmyUserInfo" style="text-align:center;">	
						</tbody>
					</table>
				</div>
				<p></p>
				<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
					<div class="pull-left">
						<h4>订单备注信息：</h4>
					</div>
					<div class="pull-right">
						<a href="#" onclick="getRemarks()" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加备注</a>
					</div>
					<p></p>
					<table id="contentTable" class="table table-bordered table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">备注</th>
								<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody id="orderRemarks" style="text-align:center;">
						</tbody>
					</table>
				</div>
			</form:form>
		</div>
	</div>
</body>
</html>