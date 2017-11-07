<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<head>
<title>创建套卡订单</title>
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
			
			//新订单,业务员校验
			if($("#isNeworder").val() == 0){
				var sysUserId = $("#sysUserId").val(); 
				var belongUserId = $("#belongUserId").val(); 
				var orderamount = $("#orderamount").val();
				
				if(sysUserId == belongUserId){
					top.layer.alert('归属人和业务员不能是同一个人!', {icon: 0, title:'提醒'}); 
					return;
				}
				if(sysUserId == undefined){
					top.layer.alert('业务员信息不能为空!', {icon: 0, title:'提醒'}); 
					return;
				}else{
					var str = $("#strval").val();
					str = str.split(",");
					var sumPushMoney = 0;
					for(var i = 0; i < str.length-1; i++){
						$("[name='"+str[i]+"pushMoney']").each(function(){
							sumPushMoney += parseFloat($(this).val());
			    		});
						if(parseFloat(sumPushMoney)<0){
							top.layer.alert('营业总额必须大于0！', {icon: 0, title:'提醒'});
							return;
						}
						if(parseFloat(sumPushMoney) - parseFloat(orderamount) > 0){
							top.layer.alert('营业总额小于等于订单应付总额！', {icon: 0, title:'提醒'});
							return;
						}
						sumPushMoney = 0;
					}
				}
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
			    content: "${ctx}/ec/orders/addSuitCardOderGoods?isNeworder="+isNeworder,
			    btn: ['确定', '关闭'],
			    yes: function(index, layero){
			        var obj =  layero.find("iframe")[0].contentWindow;
					var goodselectId = obj.document.getElementById("goodselectId");		//商品id
			    	var goodselectName=obj.document.getElementById("goodselectName");	//商品名称
					var marketPrice=obj.document.getElementById("marketPrice");			//市场价格
					var goodsPrice=obj.document.getElementById("goodsPrice");			//优惠价格
					var costPrice=obj.document.getElementById("costPrice");				//系统价 
					var orderAmount = obj.document.getElementById("orderAmount"); 		//成交价
					var goodsNum=obj.document.getElementById("goodsNum");		  		//成交次数
					var debtMoney = obj.document.getElementById("debtMoney");			//欠款
					var spareMoney = obj.document.getElementById("spareMoney");			//余额
					var isNeworderSon = obj.document.getElementById("isNeworderSon");	//子弹出层的订单
					var actualPayment = obj.document.getElementById("actualPayment");	//实际付款
					var computType = obj.document.getElementById("computType");	//是否计算过费用
					var r =/^\d+\.?\d{0,2}$/;
					var reg=/^\+?[1-9]\d*$/;
					if($(goodselectName).val()==""){
						top.layer.alert('商品不能为空!', {icon: 0, title:'提醒'}); 
						  return;
					}
					if($(computType).val() == 1 || $(computType).val() == ""){
						top.layer.alert('请计算费用!', {icon: 0, title:'提醒'}); 
						return;
					}
					if($(orderAmount).val()<0){
						top.layer.alert('订单金额要大于等于0!', {icon: 0, title:'提醒'}); 
						return;
					}
					//通过子页面的订单来锁定父页面订单为不可变的
					$("#isNeworder").attr("disabled",true);
					$("#_isNeworder").val(isNeworder);
					var goodstolprice=changeTwoDecimal_f($(goodsNum).val()*$(goodsPrice).val()*discount);
					var invoiceContent = $("#invoiceContent").val();
					invoiceContent = invoiceContent + "商品名称："+$(goodselectName).val()+" 商品数量:1 商品价格："+$(goodsPrice).val()+";";
					$("#invoiceContent").val(invoiceContent);
					
					$.ajax({
						type:"post",
						async: false,
						url:"${ctx}/ec/orders/selectSuitCardSon",
						data:{
							goodsId:$(goodselectId).val(),
							goodsName:$(goodselectName).val(),
							marketPrice:$(marketPrice).val(),
							goodsPrice:$(goodsPrice).val(),
							orderAmount:$(orderAmount).val(),
							actualPayment:$(actualPayment).val(),
							costPrice:$(costPrice).val(),
							debtMoney:$(debtMoney).val(),
							spareMoney:$(spareMoney).val(),
							tail:$("#tail").val(),
							isNeworder:isNeworder
						 },
						success:function(date){
							
							$(date).appendTo($("#addZTD"));
							$("#tail").val(parseInt($("#tail").val())+1);
							
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
						},
						error:function(XMLHttpRequest,textStatus,errorThrown){
							
						}
					});
				
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
	//orderAmount 成交价
	//spareMoney 余额
	//afterPayment 实际付款
	//debtMoney 欠款
	function delFile(id,costPrice,orderAmount,spareMoney,afterPayment,debtMoney){
		$("#goodsprice").val(changeTwoDecimal_f(parseFloat($("#goodsprice").val())-parseFloat(costPrice))); //系统总额
		$("#orderamount").val(changeTwoDecimal_f(parseFloat($("#orderamount").val())-parseFloat(orderAmount))); //成交总价
		$("#orderBalance").val(changeTwoDecimal_f(parseFloat($("#orderBalance").val())-parseFloat(spareMoney))); //总余额
		$("#totalamount").val(changeTwoDecimal_f(parseFloat($("#totalamount").val())-parseFloat(afterPayment))); //实付总额
		$("#orderArrearage").val(changeTwoDecimal_f(parseFloat($("#orderArrearage").val())-parseFloat(debtMoney)));//总欠款 
		$("tr").remove(".suitCard"+id);
		var goodselectIds = $("#goodselectIds").val();
		if(goodselectIds == undefined){
		$("#isNeworder").attr("disabled",false);
		}
		var orderArrearage = $("#orderArrearage").val();
	   if(orderArrearage == 0){
		   $("#Ichecks").attr("disabled",false);
	   }
    }
    //删除业务员
	function deleteFile(obj,id){
		$(obj).parent().parent().remove();
		
		//删除业务员时,同时对比隐藏域是否需存在该值
		var idstr = id+"pushMoney";
		var strval = $("#strval").val();
		if($("[name='"+idstr+"']").val() == undefined){
			id = id+",";
			strval = strval.replace(id,"");
		} 
		$("#strval").val(strval);
	}
	//删除备注
	function deleteFileRemarks(obj){
		$(obj).parent().parent().remove();
	}
	
	$(document).ready(function(){
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
		
		validateForm = $("#inputForm").validate( {
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
		} );
	
		//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
		//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
		//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
		//$("#inputForm").validate().element($("#phone"));
		
		/* $("#belongOfficeButton").click(function(){
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
							
							$("#belongUserId").val("");
							$("#belongUserName").val("");
							
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
		}); */
		
	});
	function selectUser(){
		$("#username").val("");
		$("#userid").val("");
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
				top.layer.alert('昵称查询失败!', {icon: 0, title:'提醒'}); 
			}
		});
	}
	function getSysUserInfo(){
		var orderamount = $("#orderamount").val();
		var sysUserIds = document.getElementsByName("sysUserId");
		// 正常打开	
		top.layer.open({
		    type: 2, 
		    area: ['550px', '420px'],
		    title:"业务员选择",
		    ajaxData:{selectIds: $("#goodselectId").val()},
		    content: "${ctx}/ec/orders/getPushmoneyView",
		    btn: ['确定', '关闭']
    	    ,yes: function(index, layero){
    	    	var obj =  layero.find("iframe")[0].contentWindow;
    	    	var sysUserId = obj.document.getElementById("sysUserId").value; //员工id
    	    	var sysMobile = obj.document.getElementById("sysMobile").value; //员工电话
    	    	var sysName = obj.document.getElementById("sysName").value; //员工名称
    	    	var pushMoney = obj.document.getElementById("pushMoney").value; //营业额
    	    	
    	    	if(pushMoney==""){	
    	    		top.layer.alert('填写营业额！', {icon: 0, title:'提醒'});
     	    		return;
    	    	}else{
	   	    		var re = /^([+-]?)\d*\.?\d{0,2}$/; 
	   				if(!re.test(pushMoney)){
	   					top.layer.alert('请输入正确的营业额(最多两位小数)', {icon: 0, title:'提醒'});
	     	    		return;
	   				}
    	    	}
    	    	if(sysUserId == ""){
    	    		top.layer.alert('填写业务员！', {icon: 0, title:'提醒'});
     	    		return;
    	    	}else if(parseFloat(pushMoney) - parseFloat(orderamount) > 0){
    	    		top.layer.alert('营业额小于等于订单应付总额！', {icon: 0, title:'提醒'});
     	    		return;
    	    	}else{
    	    		/* if(sysUserIds.length > 0){
    	    			for(i=0;i<sysUserIds.length;i++){
         	    	        if(sysUserId == sysUserIds[i].value){
         	    	        	top.layer.alert('业务员不能相同！', {icon: 0, title:'提醒'});
         	     	    		return;
         	    	        }
         	    	    }
    	    		} */
    	    		
    	    		var pushMoneySum = 0;
    	    		$("[name='"+sysUserId+"pushMoney']").each(function(){
    	    			pushMoneySum += parseFloat($(this).val());
    	    		});
    	    		
    	    		var belongUserId = $("#belongUserId").val(); 
    				if(sysUserId == belongUserId){
    					top.layer.alert('归属人和业务员不能是同一个人!', {icon: 0, title:'提醒'}); 
    					return;
    				}
    	    		
    	    		if(parseFloat(pushMoneySum) + parseFloat(pushMoney) < 0){
    	    			top.layer.alert('营业总额必须大于0！', {icon: 0, title:'提醒'});
    	    			return;
    	    		}
    	    		if(parseFloat(pushMoneySum) + parseFloat(pushMoney) - parseFloat(orderamount) > 0){
    	    			top.layer.alert('营业总额小于等于订单应付总额！', {icon: 0, title:'提醒'});
    	    			return;
    	    		}
    	    		
	    	    	$("#sysUserInfo").append(
	    	    			"<tr>"+
	    					"<td>"+
	    						"<input id='sysUserId' name='sysUserId' type='hidden' value='"+sysUserId+"' class='form-control' readonly='readonly'>"+
	    						"<input id='sysName' name='sysName' type='text' value='"+sysName+"' class='form-control' readonly='readonly'>"+
	    					"</td>"+
	    					"<td><input id='sysMobile' name='sysMobile' type='text' value='"+sysMobile+"' class='form-control' readonly='readonly'></td>"+
	    					"<td><input id='pushMoney' name='pushMoney' value='"+pushMoney+"' readonly='readonly' class='form-control required' type='text' class='form-control'></td>"+
	    					"<input id='pushMoney' name='"+sysUserId+"pushMoney'  type='hidden' value='"+pushMoney+"' readonly='readonly' class='form-control required' type='text' class='form-control'>"+
	    					"<td><a href='#' class='btn btn-danger btn-xs' onclick='deleteFile(this,\""+sysUserId+"\")'><i class='fa fa-trash'></i> 删除</a></td>"+
	    					"</tr>"
	    			);
	    	    	//把获取到的sysUserId存放到隐藏域中
	    	    	var strval = $("#strval").val();
	    	    	var sysUserIdStr = sysUserId+",";
	    	    	//判断sysUserId是否存在strval中
	    	    	if(strval.indexOf(sysUserIdStr) < 0){
		    	    	strval += sysUserIdStr;
		    	    	$("#strval").val(strval);
	    	    	}
	    	    	
					top.layer.close(index);
    	    	}
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
    					"<td><a href='#' class='btn btn-danger btn-xs' onclick='deleteFileRemarks(this)'><i class='fa fa-trash'></i> 删除</a></td>"+
    					"</tr>"
    			);
				top.layer.close(index);
			}
		}); 
	}
	
	function choose(value){
		$("#belongOfficeId").val("");
		$("#belongOfficeName").val("");
		$("#belongUserId").val("");
		$("#belongUserName").val("");
		if(value == 1){
			
			$("#iType").hide();
			$("#personheadContent").hide();
			$("#invoiceRecipient").hide();
			$("#companyheadContent").hide();
			$("#fpinfo").hide();
			$("#Ichecks").attr("checked",false);
			$("#Ichecks").attr("disabled",true);
			$("#sysUserPush").hide();
			$("#sysUserInfo").empty();
			$("#belongUser").hide();
		}else{
			$("#sysUserPush").show();
			$("#belongUser").show();
		}
	}
	</script>
</head>
<body>
	<div class="ibox-content">
		<div class="clearfix">
			<form:form id="inputForm" modelAttribute="orders" action="${ctx}/ec/orders/saveSuitCardOrder" method="post" class="form-horizontal">
				<label><font color="red">*</font>手机号码：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="form-control required" style="width:200px"/>&nbsp;&nbsp;
				<a href="#" onclick="selectUser()">查询昵称</a>
				<label><font color="red">*</font>昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：</label>
				<input id="username" name="username" type="text" class="form-control required" readonly="true" style="width:200px" />
				<input type="hidden" name="userid" id="userid" />
				<input id="tail" name="tail" type="hidden" value="1"/>
				<input type="hidden" name="strval" id="strval" /><!-- 多个业务员校验用 -->
				<p></p>
				<label><font color="red">*</font>订单性质：</label>
				<form:select path="distinction"  class="form-control" style="width:200px">
						<form:option value="1">售前卖</form:option>
						<form:option value="2">售后卖</form:option>
						<form:option value="3">老带新</form:option>
				</form:select>&nbsp;&nbsp;
				<label><font color="red">*</font>新老订单：</label>
				<select id="isNeworder" class="form-control" style="width:200px" onchange="choose(this.value)">
					<option value=0>新订单</option>
					<option value=1>老订单</option>
				</select>
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
								<th style="text-align: center;">商品名称</th>
								<th style="text-align: center;">子项名称</th>
								<th style="text-align: center;">系统价</th>
								<th style="text-align: center;">市场价</th>
								<th style="text-align: center;">优惠价</th>
								<th style="text-align: center;">次(个)数</th>
								<th style="text-align: center;">实际次(个)数</th>
								<th style="text-align: center;">余额</th>
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
					<form:input id="goodsprice" path="goodsprice" htmlEscape="false" readonly="true" maxlength="11" class="form-control required" style="width:180px" />
					<label ><font color="red">*</font>成交总额：</label>
					<form:input id="orderamount" path="orderamount" htmlEscape="false" readonly="true"  maxlength="11" class="form-control required" style="width:180px" />
					<label ><font color="red">*</font>总&nbsp;&nbsp;余&nbsp;&nbsp;额：</label>
					<form:input id="orderBalance" path="orderBalance" htmlEscape="false" readonly="true"  maxlength="11" class="form-control required" style="width:180px" />
					<p></p>
					<label ><font color="red">*</font>实付总额：</label>
					<form:input id="totalamount" path="totalamount" htmlEscape="false" readonly="true"  maxlength="11" class="form-control required" style="width:180px" />
					<label ><font color="red">*</font>总&nbsp;&nbsp;欠&nbsp;&nbsp;款：</label>
					<form:input id="orderArrearage" path="orderArrearage" htmlEscape="false" readonly="true"  maxlength="11" class="form-control required" style="width:180px" />
					<p></p>
					<label ><font color="red">*</font>付款方式：</label>
					<form:select path="paycode" class="form-control" style="width:180px">
						<c:forEach items="${paylist}" var="payment">
							<form:option value="${payment.paycode}">${payment.paydesc}</form:option>
						</c:forEach>
					</form:select>
					<label ><font color="red">*</font>订单状态：</label>
					<form:select path="orderstatus"  class="form-control" style="width:180px">
							<form:option value="4">已完成</form:option>
					</form:select>
					<p></p>
					<label >留言备注：</label>
					<textarea name="usernote" rows="5" cols="60"></textarea>
				</div>
				<!-- <p></p>
				<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;">
				<table id="contentTable" class="table table-bordered table-condensed  dataTables-example dataTable no-footer">
					<tr>
						<td width="100px"><span><font color="red">*</font>归属机构：</span></td>
						<td width="300px">
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
						<td colspan="2" width="100px"></td>
					</tr>
					<tr id="belongUser">
						<td width="100px"><span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;归属人：</span></td>
						<td width="300px">
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
						<td colspan="2" width="100px"></td>
					</tr>
				</table>
				</div> -->
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
						<label class="active"><font color="red">*</font>公司名称：</label><input type="text" id="companyContent" name="companyheadContent" class="form-control required" style="width:180px" />
						<p></p>
					</div>
					<div id="fpinfo">
						<label class="active"><font color="red">*</font>纳税人识别号：</label><input type="text" name="taxNum" class="form-control required" style="width:180px" />
						<label class="active"><font color="red">*</font>银行账号：</label><input type="text" name="bankNo" class="form-control required" style="width:180px" />
						<p></p>
						<label class="active"><font color="red">*</font>开&nbsp;户&nbsp;银&nbsp;行：</label>
						<input type="text" name="bankName" class="form-control required" style="width:180px" />
						<label class="active"><font color="red">*</font>电&nbsp;&nbsp;话：</label>
						<input type="text" name="invoicePhone" class="form-control required" style="width:180px" />
						<label class="active"><font color="red">*</font>地&nbsp;&nbsp;址：</label>
						<input type="text" name="invoiceAddress" class="form-control required" style="width:180px" />
					</div>
					<div id="invoiceRecipient">
						<label class="active">发票收货地址：</label>
						<p></p>
						<label class="active"><font color="red">*</font>收货人：</label>
						<input type="text" name="recipientsName" class="form-control required" style="width:180px" />
						<label class="active"><font color="red">*</font>联系电话：</label>
						<input type="text" name="recipientsPhone" class="form-control required" maxlength="11" style="width:180px" />
						<label class="active"><font color="red">*</font>收货地址：</label>
						<input type="text" name="recipientsAddress" class="form-control required" maxlength="50" style="width:180px" />
					</div>
				</div>
				<p></p>
				<div style=" border: 1px solid #CCC;padding:10px 20px 20px 10px;" id="sysUserPush">
					<div class="pull-left">
						<h4><font color="red">*</font>业务员信息：</h4>
					</div>
					<div class="pull-right">
						<a href="#" onclick="getSysUserInfo()" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加业务员</a>
					</div>
					<p></p>
					<table id="contentTable" class="table table-bordered table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">业务员</th>
								<th style="text-align: center;">手机号</th>
								<th style="text-align: center;">营业额</th>
								<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody id="sysUserInfo" style="text-align:center;">	
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