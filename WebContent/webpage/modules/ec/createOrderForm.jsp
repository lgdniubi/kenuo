<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<head>
<title>创建订单</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/jquery/jquery.alerts.js"></script>
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/jquery/jquery.alerts.css">
<!-- 内容上传 引用-->
<script type="text/javascript">
		var cateid = 0;  // 分类的id
		var areaId=0;
		var isRel=1;
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  var val=jQuery("#specgoods").val();
			  var orderamount=$("#orderamount").val();
			  var price=$("#couponprice").val();
			 
			  if(orderamount<=0){
				  top.layer.alert('订单成交价格要大于0元!', {icon: 0, title:'提醒'}); 
				  return;
			  }else{
				  //alert(numprice);
				  if(isRel==1){
					  if(orderamount>price){
						  top.layer.alert('订单成交价格要小于订单价格!', {icon: 0, title:'提醒'});
						  return;
					  }
				  }else{
					  var  num=$("#goodsnum").val();
					  var numprice=num*price;
					  if(orderamount>numprice){
						  top.layer.alert('订单成交价格要小于订单价格!', {icon: 0, title:'提醒'});
						  return;
					  } 
				  }
				 
			  }

			  
			  
				//var vv=$('select');
				if(val==0){
					if(confirm("没有选择规格确定提交吗？","提示框")){
						$("#inputForm").submit();
						 return true;
 					}
// 					layer.msg('没有选择规格确定提交吗？', {
// 						  time: 0 //不自动关闭
// 						  ,btn: ['确认', '取消']
// 						  ,yes: function(index){
// 							 layer.close(index);
// 						    $("#inputForm").submit();
// 							 return true;	
							 
// 						  }
// 						});
// 					jConfirm('你确定这么做吗?', '确认对话框', function(r) {
// 						$("#inputForm").submit();
// 						 return true;
// 					});
					
				}else{
					if(confirm("确定提交吗？","提示框")){
						$("#inputForm").submit();
						 return true;	
					}
// 					layer.msg('确定提交吗？', {
// 						  time: 0 //不自动关闭
// 						  ,btn: ['确认', '取消']
// 						  ,yes: function(index){
// 							 layer.close(index);
// 						    $("#inputForm").submit();
// 							 return true;	
							 
// 						  }
// 						});
				}
			 
		  }
	
		  return false;
		}
		//根据一级分类查询二级分类
		function select(o){
			var val=jQuery("#onecate").val();
			$("#twocate").val(0);
			$("#twocate").empty();
			$("#twocate").empty().append("<option value='0'>二级分类</option>");
		//	alert(val);
			if(val!=0){
				$.ajax({
					 type:"get",
					 dataType:"json",
					 url:"${ctx}/ec/orders/catetwolist?id="+val,
					 success:function(date){
						
					      $.each(date,function(i,e){
					     $("#twocate").append("<option value='"+e.id+"'>"+e.name+"</option>");
							//<option value='"+e.id+"'>"+e.name+"</option>
					   })
					 },
					 error:function(XMLHttpRequest,textStatus,errorThrown) {
					    
					  }
					 
					});
			}
			
		}
		
		
		
		
		//根据商品id 查询商品信息
		function selectgood(){
			var val=jQuery("#goodselectId").val();
			//alert(val);
			
			if(val!=0){
				$.ajax({
					 type:"get",
					 dataType:"json",
					 url:"${ctx}/ec/orders/Getgood?id="+val,
					 success:function(date){
						//console.log(date);
						isRel=date.isReal;
						if(date.isReal==1){
							$("#service").show();
							//$("#idnum").hide();
							$("#goodsnum").attr("readonly",true);
							$("#shippingtype").val(2);
							$("#address").hide();
							$("#address1").hide();
							$("#address2").hide();
							$("#address3").hide();
							$("#address4").hide();
							$("#goodsnum").val(1);
							areaId=0;
						}else{
							//$("#idnum").show();
							$("#goodsnum").attr("readonly",false);
							$("#shippingtype").val(0);
							$("#consignee").val("");
							$("#mobile1").val("");
							$("#qucityId").val("");
							$("#qucityName").val("");
							$("#address").val("");
							$("#officeId").val("");
							$("#officeName").val("");
							areaId=0;
							$("#service").hide();
							$("#address").show();
							$("#address1").show();
							$("#address2").show();
							$("#address3").show();
							$("#goodsnum").val(1);
						}
					   $("#marketprice").val(date.marketPrice);
					   $("#couponprice").val(date.shopPrice);
					   $("#orderamount").val(date.shopPrice);
		    		//	$("#newMobile").val(date.mobile);
					     
					 },
					 error:function(XMLHttpRequest,textStatus,errorThrown) {
					    
					  }
					 
					});
			}
			
		}
		
		
		//根据商品 查询商品的规格
		function selecSpecgood(){
			var val=jQuery("#goodselectId").val();
			//	alert(val)
			$("#specgoods").val(0);
			$("#specgoods").empty().append("<option value='0'>规格选择</option>");
			if(val!=0){
				$.ajax({
					 type:"get",
					 dataType:"json",
					 url:"${ctx}/ec/orders/specgoods?id="+val,
					 success:function(date){
					      $.each(date,function(i,e){
					     $("#specgoods").append("<option value='"+e.id+"'>"+e.name+"</option>");
							//<option value='"+e.id+"'>"+e.name+"</option>
					   })
					 },
					 error:function(XMLHttpRequest,textStatus,errorThrown) {
					    
					  }
					 
					});
			}
			
		}
		
		
		//根据规格查询商品
		function selectspecprice(o){
			var val=jQuery("#specgoods").val();
			var goodid=$("#goodselectId").val();
			//	alert(val)
			if(val!=0){
				$.ajax({
					 type:"get",
					 dataType:"json",
					 url:"${ctx}/ec/orders/getSpecPrce?id="+val+"&goodid="+goodid,
					 success:function(date){
						//console.log(date);
					   $("#marketprice").val(0);
					   $("#couponprice").val(date.price);
					   $("#orderamount").val(date.price);
					   $("#speckey").val(date.specKey);
					   $("#speckeyname").val(date.specKeyValue);
		    		//	$("#newMobile").val(date.mobile);
					     
					 },
					 error:function(XMLHttpRequest,textStatus,errorThrown){
					    
					 }
					 
					});
			}
			
		}
		
		//计算成交价格
		function numPrice(o){
		
			var  num=$("#goodsnum").val();
			var price=$("#couponprice").val();
			var numprice=num*price;
			$("#orderamount").val(numprice);
		
		}
		
		function selectshipping(o){
			//alert("lalal");
			var select=$("#shippingtype").val();
			if(isRel==1){
				$("#address").hide();
				$("#address1").hide();
				$("#address2").hide();
				$("#address3").hide();
				$("#address4").hide();
			}else{
				$("#consignee").val("");
				$("#mobile1").val("");
				$("#qucityId").val("");
				$("#qucityName").val("");
				$("#address").val("");
				$("#officeId").val("");
				$("#officeName").val("");
				areaId=0;
				if(select==0){
					$("#address").show();
					$("#address1").show();
					$("#address2").show();
					$("#address3").show();
					$("#address4").hide();
				}else if(select==1){
					$("#address").hide();
					$("#address1").hide();
					$("#address2").show();
					$("#address3").hide();
					$("#address4").show();
				}else{
					$("#address").hide();
					$("#address1").hide();
					$("#address2").hide();
					$("#address3").hide();
					$("#address4").hide();
				}
			}
			
		}
		
	
		$(document).ready(function(){
		
//			$("#goodselectButton, #goodselectName").click(function(){	    增加  , #goodselectName  文本框有点击事件
				$("#goodselectButton").click(function(){
				// 是否限制选择，如果限制，设置为disabled
				if ($("#goodselectButton").hasClass("disabled")){
					return true;
				}
				if(cateid == "0" || cateid == ""){
					top.layer.alert('请先选择商品分类!', {icon: 0, title:'提醒'});
				}else{
					// 正常打开	
					top.layer.open({
					    type: 2, 
					    area: ['300px', '420px'],
					    title:"商品选择",
					    ajaxData:{selectIds: $("#goodselectId").val()},
					    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/ec/goods/treeGoodsData?goodsCategory="+cateid)+"&module=&checked=&extId=&isAll=",
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
									
									$("#goodselectId").val(ids.join(",").replace(/u_/ig,""));
									$("#goodselectName").val(names.join(","));
									$("#goodselectName").focus();
									ceateid=$("#goodselectId").val();
									selectgood();
									selecSpecgood();
									top.layer.close(index);
							    	       },
			    	cancel: function(index){ //或者使用btn2
			    	           //按钮【按钮二】的回调
			    	       }
					}); 
				}
			});
	
	
				$("#goodsCategoryIdButton").click(function(){
					// 是否限制选择，如果限制，设置为disabled
					if ($("#goodsCategoryIdButton").hasClass("disabled")){
						return true;
					}
					// 正常打开	
					top.layer.open({
					    type: 2, 
					    area: ['300px', '420px'],
					    title:"选择商品分类",
					    ajaxData:{selectIds: $("#goodsCategoryIdId").val()},
					    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/ec/goodscategory/treeData")+"&module=&checked=&extId=&isAll=" ,
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
									$("#goodsCategoryIdId").val(ids.join(",").replace(/u_/ig,""));
									$("#goodsCategoryIdName").val(names.join(","));
									$("#goodsCategoryIdName").focus();
									
									$("#goodselectId").val("");
									$("#goodselectName").val("");
									$("#consignee").val("");
									$("#mobile1").val("");
									$("#qucityId").val("");
									$("#qucityName").val("");
									$("#address").val("");
									$("#officeId").val("");
									$("#officeName").val("");
									areaId=0;
									$("#service").hide();
									$("#address").hide();
									$("#address1").hide();
									$("#address2").hide();
									$("#address3").hide();
									$("#address4").hide();
									$("#specgoods").empty().append("<option value='0'>规格选择</option>");
									$("#specgoods").val(0);
									$("#speckey").val("");
									$("#speckeyname").val("");
									$("#goodsnum").val("");
									$("#marketprice").val("");
									$("#couponprice").val("");
									$("#orderamount").val("");
									
									cateid=$("#goodsCategoryIdId").val();
									top.layer.close(index);
							    	       },
			    	cancel: function(index){ //或者使用btn2
			    	           //按钮【按钮二】的回调
			    	       }
					}); 
				
				});
				
				$("#qucityButton").click(function(){
					// 是否限制选择，如果限制，设置为disabled
					if ($("#qucityButton").hasClass("disabled")){
						return true;
					}
					// 正常打开	
					top.layer.open({
					    type: 2, 
					    area: ['300px', '420px'],
					    title:"选择区域",
					    ajaxData:{selectIds: $("#qucityId").val()},
					    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/sys/area/treeData")+"&module=&checked=&extId=&isAll=" ,
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
									$("#qucityId").val(ids.join(",").replace(/u_/ig,""));
									$("#qucityName").val(names.join(","));
									$("#qucityName").focus();
									areaId=$("#qucityId").val();
									top.layer.close(index);
							    	       },
			    	cancel: function(index){ //或者使用btn2
			    	           //按钮【按钮二】的回调
			    	       }
					}); 
				
				});
			
				$("#officeButton").click(function(){
					// 是否限制选择，如果限制，设置为disabled
					if ($("#officeButton").hasClass("disabled")){
						return true;
					}
					if(areaId == "0"){
						top.layer.alert('请先选择省市区!', {icon: 0, title:'提醒'});
						return;
					}
					
					// 正常打开	
					top.layer.open({
					    type: 2, 
					    area: ['300px', '420px'],
					    title:"选择实体店",
					    ajaxData:{selectIds: $("#officeId").val()},
					    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/sys/office/storeTreeDate?areaId="+areaId+"")+"&module=&checked=&extId=&isAll=" ,
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
									$("#officeId").val(ids.join(",").replace(/u_/ig,""));
									$("#officeName").val(names.join(","));
									$("#officeName").focus();
									
									
									top.layer.close(index);
							    	       },
			    	cancel: function(index){ //或者使用btn2
			    	           //按钮【按钮二】的回调
			    	       }
					}); 
				
				});
			
			$("#phone").focus();
			
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
			
				rules:{
					mobile:{
							digits:true,
							minlength:11,
							isMobile : true,
							remote: "${ctx}/ec/mtmyuser/verifyPhone"
							
						},
					goodsnum:{
						  digits:true,
			              min:1
						},
					orderamount:{
						number:true,
						min:0,
						lrunlv:true
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
				goodsnum:{
							digits:"输入合法的数量",
				            min:"不能小于0"
						},
				orderamount:{
					number:"输入合法的价格",
					min:"成交价最小为0",
					lrunlv:"请输入两位小数"
						},
				mobile1:{
							digits:"输入合法手机号",
							minlength:"手机号码要11位",
							isMobile :"请输入正确手机号"
	
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
			$("#inputForm").validate().element($("#phone"));
			
			
		});



			
		
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="orders" action="${ctx}/ec/orders/saveOrder" method="post" class="form-horizontal">

		<table style="width: 700px"
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="active" style="width: 200px"><label
						class="pull-right"><font color="red">*</font>用户手机号码:</label></td>
					<td><form:input path="mobile" htmlEscape="false"
							maxlength="11" class="form-control required" style="width:200px" /></td>
				</tr>
				<tr>
					<td class="active"><label class="pull-right"><font
							color="red">*</font>商品分类:</label></td>
					<td><input id="goodsCategoryIdId" class="form-control required" type="hidden" value="" name="goodsCategoryId" aria-required="true">
						<div class="input-group">
							<input id="goodsCategoryIdName" class="form-control required" type="text" style="" data-msg-required="" value="" readonly="readonly" name="goodsCategory.name" aria-required="true"> <span class="input-group-btn">
								<button id="goodsCategoryIdButton" class="btn btn-primary " type="button">
									<i class="fa fa-search"></i>
								</button>
							</span>
						</div> <label id="goodsCategoryIdName-error" class="error" style="display: none" for="goodsCategoryIdName"></label></td>
				</tr>
				<tr>
					<td class="active" style="width: 200px"><label class="pull-right"><font color="red">*</font>商品选择:</label></td>
					<td><input id="goodselectId" name="goodsid" class="form-control required" type="hidden" value="" aria-required="true">
						<div class="input-group">
							<input id="goodselectName" name="goodsname" readonly="readonly" type="text" value="" data-msg-required="" class="form-control required" style="" aria-required="true">
							<span class="input-group-btn">
								<button type="button" id="goodselectButton" class="btn   btn-primary  ">
									<i class="fa fa-search"></i>
								</button>
							</span>
						</div> <label id="goodselectName-error" class="error" for="goodselectName" style="display: none"></label></td>
				</tr>
				<tr>
					<td class="active"><label class="pull-right"><font
							color="red">*</font>规格选择:</label></td>
					<td><select id="specgoods" onchange="selectspecprice(this)" style="width: 120px;height:30px;">
							<option value="0">规格选择</option>
					</select><font color="red">如果商品有规格必选项</font> 
					<input id="speckey" name="speckey" type="hidden" value="" class="form-control">
					<input id="speckeyname" name="speckeyname" type="hidden" value="">
					</td>
				</tr>
				<tr id="service" style="display: none;">
					<td class="active"><label class="pull-right"><font
							color="red">*</font>剩余服务次数:</label></td>
					<td>
						<form:input path="remaintimes" htmlEscape="false"
							maxlength="5" class="form-control required" />
					</td>
				
				</tr>
				<tr>
					<td class="active"><label class="pull-right">
							<font color="red">*</font>购买数量:</label></td>
					<td><input id="goodsnum" name="goodsnum" maxlength="10" class="form-control required" readonly="true" onblur="numPrice(this)" /></td>

				</tr>
				<tr>
					<td class="active"><label class="pull-right">市场价格:</label></td>
					<td><input id="marketprice" name="marketprice" name="marketprice" maxlength="10" class="form-control" readonly="true"/></td>
				</tr>
				<tr>
					<td class="active"><label class="pull-right">优惠价格:</label></td>
					<td><input id="couponprice" name="couponprice" maxlength="10" class="form-control required" readonly="true" /></td>
				</tr>
				<tr>
					<td class="active"><label class="pull-right"><font color="red">*</font>成交价格:</label></td>
					<td><input id="orderamount" name="orderamount" maxlength="10" class="form-control required" /></td>

				</tr>
				<tr>
					<td class="active"><label class="pull-right"><font color="red">*</font>订单状态:</label></td>
					<td><form:select path="orderstatus"  class="form-control" style="width:200px">
							<form:option value="-1">未付款</form:option>
							<form:option value="1">已付款</form:option>
							<form:option value="2">已发货</form:option>
							<form:option value="4">已完成</form:option>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="active"><label class="pull-right"><font color="red">*</font>付款方式:</label></td>
					<td><form:select path="paycode" class="form-control" style="width:200px">
							<c:forEach items="${paylist}" var="payment">
								<form:option value="${payment.paycode}">${payment.paydesc}</form:option>
							</c:forEach>
						</form:select></td>
				</tr>
				<tr>
					<td class="active"><label class="pull-right"><font color="red">*</font>服务方式:</label></td>
					<td><form:select path="shippingtype" class="form-control" style="width:200px" onchange="selectshipping(this)">
								<form:option value="0">快递发货</form:option>
								<form:option value="1">到店自取</form:option>
								<form:option value="2">到店服务</form:option>
						</form:select><font color="red">虚拟服务，到店自取都不需要快递发货</font>
					</td>
				</tr>
				<tr id="address" style="display: none;">
					 <td class="active"><label class="pull-right"><font color="red">*</font>收货人姓名:</label></td>
					<td>
						<form:input path="consignee" htmlEscape="false" maxlength="10" class="form-control required"/>
					 </td>
				
				</tr>
				<tr id="address1" style="display: none;">
					<td class="active"><label class="pull-right"><font color="red">*</font>手机号码:</label></td>
					<td>
						  <form:input path="mobile1" htmlEscape="false" maxlength="11" class="form-control required"/>
					 </td>
				</tr>
				<tr id="address2" style="display: none;">
					 <td class="active"><label class="pull-right"><font color="red">*</font>区域选择：</label></td>
					 <td>				  
						<input id="qucityId" name="city" class="form-control " type="hidden" value="">
						<div class="input-group">
							<input id="qucityName" name="cityname" readonly="readonly" type="text" value="" data-msg-required="" class="form-control valid" style="" aria-invalid="false">
					       		 <span class="input-group-btn">
						       		 <button type="button" id="qucityButton" class="btn   btn-primary  "><i class="fa fa-search"></i>
						             </button> 
					       		</span>
					    </div>
					    <label id="qucityName-error" class="error" for="qucityName" style="display:none"></label>
					    <font color="red">区域选择精确到县区级</font>
<%-- 						  <sys:treeselect id="qucity" name="city" value="${orders.city}" labelName="cityname" labelValue="${orders.cityname}" --%>
<%-- 						title="区域" url="/sys/area/treeData" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/> --%>
<!-- 						<font color="red">区域选择精确到县区级</font> -->
					 </td>
				</tr>
				<tr id="address3" style="display: none;">
					<td class="active"><label class="pull-right"><font color="red">*</font>街道地址:</label></td>
					<td>
						 <form:input path="address" htmlEscape="false" maxlength="50" class="form-control required"/>
					</td>
				
				</tr>
				<tr id="address4" style="display: none;">
				<td class="active"><label class="pull-right"><font color="red">*</font>实体店选择:</label></td>
				<td>
					<input id="officeId" name="office.id" class="form-control required" type="hidden" value="" aria-required="true">
					<div class="input-group">
						<input id="officeName" name="office.name" readonly="readonly" type="text" value="" data-msg-required="" class="form-control required" style="" aria-required="true">
				       		 <span class="input-group-btn">
					       		 <button type="button" id="officeButton" class="btn   btn-primary  "><i class="fa fa-search"></i>
					             </button> 
				       		 </span>
				    </div>
					 <label id="officeName-error" class="error" for="officeName" style="display:none"></label>
<%-- 					<sys:treeselect id="office" name="office.id" value="${orders.office.id}" labelName="office.name" labelValue="${orders.office.name}" --%>
<%-- 					title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" notAllowSelectParent="false" notAllowSelectRoot="false"/> --%>
				
				</td>
				</tr>
				
				<tr>
					<td class="active"><label class="pull-right">备注:</label></td>
					<td><form:textarea path="adminnote" htmlEscape="false"
							rows="3" maxlength="200" class="form-control" /></td>
				</tr>


			</tbody>
		</table>

	</form:form>
</body>
</html>