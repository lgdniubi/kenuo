<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>通用卡添加商品</title>
<meta name="decorator" content="default" />
<!-- 时间控件引用 -->
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
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
	var cateid = 0;  // 分类的id
	var isRel=1;
	var validateForm;
	
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {

			$("#inputForm").submit();
			return true;
		}

		return false;
	}

	//根据规格查询商品
	function selectspecprice(o){
		var isNeworderSon = $("#isNeworderSon").val();
		var specgoodkey = $(o).val();
		var val=jQuery("#specgoods").val();
		var goodid=$("#goodselectId").val();
		//	alert(val)
		if(val!=0){
			$.ajax({
				 type:"get",
				 dataType:"json",
				 url:"${ctx}/ec/orders/getSpecPrce?id="+val+"&goodid="+goodid,
				 success:function(date){
				   $("#marketPrice").val(date.marketPrice);
				   $("#goodsPrice").val(date.price);
				   $("#orderAmount").val(date.price);
				   $("#speckey").val(date.specKey);
				   $("#speckeyname").val(date.specKeyValue);
				   $("#costPrice").val(date.costPrice);
				   $("#goodsNo").val(date.goodsNo);
				   $("#actualSpeckeyTimes").val($("#"+specgoodkey).val());
				   $("#computType").val(1);
				   $("#afterPayment").val("");
				   $("#debtMoney").val("");
				   $("#spareMoney").val("");
				   $("#remaintimes").val($("#"+specgoodkey).val());
				   $("#remaintimes").attr("readonly",true);
				   $("#actualPayment").val("");
				   $("#orderAmount").removeAttr("readonly");
				   $("#actualPayment").removeAttr("readonly");
				 },
				 error:function(XMLHttpRequest,textStatus,errorThrown){
				    
				 }
				 
				});
		}
		
	}
	
	//计算成交价格
	function numPrice(o){
		
		var  num=$("#goodsNum").val();
		var price=$("#goodsPrice").val();
		var numprice=num*price;
		$("#orderAmount").val(numprice);
	
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
					$("#isRel").val(isRel);
					$("#goodsNum").attr("readonly",true);
					$("#goodsNum").val(1);
				   $("#marketPrice").val(date.marketPrice);
				   $("#goodsPrice").val(date.shopPrice);
				   $("#orderAmount").val(date.shopPrice);
				     
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
					$("#serviceTimes").empty();
					$.each(date,function(i,e){
						$("#specgoods").append("<option value='"+e.id+"' >"+e.name+"</option>");
						$("#serviceTimes").append("<input type='hidden' id='"+e.id+"' value='"+e.serviceTimes+"' />");
						$("#costPrice").val(e.costPrice);
					})
				},
				error:function(XMLHttpRequest,textStatus,errorThrown) {
				    
				}
				 
			});
		}
		
	}
	
	$(document).ready(function() {
		var isNeworderSon = $("#isNeworderSon").val()
		if(isNeworderSon == 0){ //订单
			$("#remaintimes").attr("readonly",true);
		}else{
			$("#orderAmount").attr("readonly",true);
			$("#actualPayment").attr("readonly",true);
		}
		//商品分类选择框
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
			    content: "${ctx}/tag/treeselect?url="+encodeURIComponent("/ec/goodscategory/treeData?positionType=")+"&module=&checked=&extId=&isAll=" ,
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

							$("#service").hide();
							$("#specgoods").empty().append("<option value='0'>规格选择</option>");
							$("#specgoods").val(0);
							$("#speckey").val("");
							$("#speckeyname").val("");
							$("#goodsNum").val("");
							$("#marketPrice").val("");
							$("#goodsPrice").val("");
							$("#orderAmount").val("");
							
							cateid=$("#goodsCategoryIdId").val();
							top.layer.close(index);
					 },
	    		cancel: function(index){ //或者使用btn2
	    	           //按钮【按钮二】的回调
	    	 	 }
			}); 
		
		});

		//根据商品分类查询商品
		//$("#goodselectButton, #goodselectName").click(function(){	    增加  , #goodselectName  文本框有点击事件
		$("#goodselectButton").click(function(){
			var goodsName = $("#goodsName").val();
			// 是否限制选择，如果限制，设置为disabled
			if ($("#goodselectButton").hasClass("disabled")){
				return true;
			}
			if((cateid == "0" || cateid == "") && (goodsName == "")){
				top.layer.alert('请先选择一个条件!', {icon: 0, title:'提醒'});
			}else{
				// 正常打开	
				top.layer.open({
				    type: 2, 
				    area: ['300px', '420px'],
				    title:"商品选择",
				    ajaxData:{selectIds: $("#goodselectId").val()},
				    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/ec/goods/treeGoodsData?&isReal=3&goodsCategory="+cateid+"&goodsName="+goodsName+"&type=1")+"&module=&checked=&extId=&isAll=",
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
								$("#orderAmount").val("");
								$("#actualPayment").val("");
								$("#afterPayment").val("");
								$("#remaintimes").val("");
								$("#debtMoney").val("");
								$("#spareMoney").val("");
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

			
		validateForm = $("#inputForm").validate({
			rules : {
			},
			messages:{
			},
			submitHandler : function(form) {
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

	});
	
	//计算费用
	function compute(){
		var orderAmount = $("#orderAmount").val();		//应付价
		var actualPayment = $("#actualPayment").val();	//实际付款
		var isNeworderSon = $("#isNeworderSon").val();  //新老订单
		var goodsPrice = $("#goodsPrice").val(); //优惠价格
		var specgoods = $("#specgoods").val();
		var actualSpeckeyTimes = $("#actualSpeckeyTimes").val();   //实际规格次数
		//验证必填
		if(specgoods == 0){
			top.layer.alert('请选择商品规格!', {icon: 0, title:'提醒'});
			return;
		}
		if(actualSpeckeyTimes == 'undefined' || actualSpeckeyTimes == '' || actualSpeckeyTimes == 0){
			top.layer.alert('实际规格次数不可为空或者0!', {icon: 0, title:'提醒'});	
			return;
		}
		if(orderAmount == 'undefined' || orderAmount == 0){
			top.layer.alert('应付价格不可为空或者0!', {icon: 0, title:'提醒'});
			return;
		}
		if(actualPayment == '' || actualPayment == 0){
			top.layer.alert('实际付款不可为空或者0!', {icon: 0, title:'提醒'});	
			return;
		}

		if(!/^\d+(\.\d{1,2})?$/.test(actualPayment)){
			top.layer.alert('实际付款（前）小数点后不可以超过2位!', {icon: 0, title:'提醒'});
			return;
		}
		if(parseFloat(actualPayment) > parseFloat(orderAmount)){
			top.layer.alert('实际付款不可大于应付款!', {icon: 0, title:'提醒'});	
			return;
		}
		
		$("#actualSpeckeyTimes").attr("readonly",true);
		//如果实际付款比应付价格大
		if(parseFloat(actualPayment) > parseFloat(orderAmount)){
			$("#afterPayment").val(changeTwoDecimal_f(orderAmount));
			$("#debtMoney").val(0);
			$("#spareMoney").val(changeTwoDecimal_f(actualPayment-orderAmount));
			$("#remaintimes").val($("#actualSpeckeyTimes").val());
			$("#orderAmount").attr("readonly",true);
			$("#actualPayment").attr("readonly",true);
			$("#computType").val(0);//已点击计算费用
			return;
		}
		
		//如果成交价等于实际付款
		if(orderAmount == actualPayment){ //因为涉及到运算有小数点不能都被整除 所以赋值为0
			$("#afterPayment").val(changeTwoDecimal_f(actualPayment));
			$("#debtMoney").val(0);
			$("#spareMoney").val(0);
			$("#remaintimes").val($("#actualSpeckeyTimes").val());
			$("#orderAmount").attr("readonly",true);
			$("#actualPayment").attr("readonly",true);
			$("#computType").val(0);//已点击计算费用
		}else{
			$.ajax({
				type:"get",
				dataType:"json",
				data:{
					transactionPrice:orderAmount,
					actualPayment:actualPayment,
					ServiceNum:actualSpeckeyTimes,
					favourablePrice:goodsPrice
				},
				url:"${ctx}/ec/orders/computingCost",
				success:function(date){
					var isNeworderSon = $("#isNeworderSon").val()
					if(isNeworderSon == 0){ //新订单
						$("#debtMoney").val(date.debtMoney);
						$("#spareMoney").val(date.spareMoney);
						$("#remaintimes").val("");
					}
					$("#afterPayment").val(date.afterPayment);
					$("#remaintimes").val(date.actualNum);
					
					$("#orderAmount").attr("readonly",true);
					$("#actualPayment").attr("readonly",true);
					$("#computType").val(0);//已点击计算费用
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
				    
				}
			});		
		}
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
	
	//修改费用
	function updateCompute(){
		$("#actualSpeckeyTimes").removeAttr("readonly");	
		$("#orderAmount").removeAttr("readonly");
		$("#actualPayment").removeAttr("readonly");
		$("#computType").val(1);//没有点击计算费用
	}
	
	 $(document).ready(function(){
		function today(){
		    var today=new Date();
		    var h=today.getFullYear();
		    var m=today.getMonth()+1;
		    var d=today.getDate();
		    m= m<10?"0"+m:m;      
		    d= d<10?"0"+d:d;  
		    return h+"-"+m+"-"+d;
		}
		document.getElementById("realityAddTime").value = today(); 
	 });
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="inputForm" modelAttribute="orders" action="#" method="post" class="form-horizontal">
							<table class="table  table-bordered  table-hovertable-condensed   dataTables-example dataTable no-footer">
								<tr>
									<td colspan="4">
										<c:if test="${orders.isNeworder == 0 }"><label class="pull-left">新订单</label></c:if>
										<c:if test="${orders.isNeworder == 1 }"><label class="pull-left">老订单</label></c:if>
										<input type="hidden" id="isNeworderSon" value="${orders.isNeworder}" />
									</td>
								</tr>
								<tr>
									<td  style="width:120px;"><label class="pull-right">分类选择：</label></td>
									<td>
										<div  style="width:200px;">
											<input id="goodsCategoryIdId" class="form-control required" type="hidden" value="" name="goodsCategoryId" aria-required="true">
											<div class="input-group">
												<input id="goodsCategoryIdName" class="form-control required" type="text" style="" data-msg-required="" value="" readonly="readonly" name="goodsCategory.name" aria-required="true"> <span class="input-group-btn">
													<button id="goodsCategoryIdButton" class="btn btn-primary " type="button">
														<i class="fa fa-search"></i>
													</button>
												</span>
											</div> 
											<label id="goodsCategoryIdName-error" class="error" style="display: none" for="goodsCategoryIdName"></label>
										</div>
									</td>
									<td><label class="pull-right">商品名称：</label></td>
									<td>
										<input id="goodsName" name="goodsName" type="text" value="" class="input-sm"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right">商品选择：</label></td>
									<td colspan="3">
										<div  style="width:200px;">
											<input id="goodselectId" name="goodsid" class="form-control required" type="hidden" value="" aria-required="true">
											<div class="input-group">
												<input id="goodselectName" name="goodsname" readonly="readonly" type="text" value="" data-msg-required="" class="form-control required" style="" aria-required="true">
												<span class="input-group-btn">
													<button type="button" id="goodselectButton" class="btn   btn-primary  ">
														<i class="fa fa-search"></i>
													</button>
												</span>
											</div>
											<label id="goodselectName-error" class="error" for="goodselectName" style="display: none"></label>
										</div>
									</td>
								</tr>
								<tr>	
									<td><label class="pull-right">规格选择：</label></td>
									<td colspan="3">
										<select id="specgoods" onchange="selectspecprice(this)" style="width: 120px;height:30px;">
												<option value="0">规格选择</option>
										</select><font color="red">如果商品有规格必选项</font> 
										<div id="serviceTimes">
										</div>
										<input id="speckey" name="speckey" type="hidden" value="" class="form-control">
										<input id="speckeyname" name="speckeyname" type="hidden" value="">
										<input id="isRel" name="isRel" type="hidden" value="">
									</td>
								</tr>
								<tr>	
									<td><label class="pull-right"><font color="red">*</font>实际规格次数：</label></td>
									<td>
										<input id="actualSpeckeyTimes" name="actualSpeckeyTimes" maxlength="10" class="form-control required" style="width:150px;height:30px;"/>
									</td>
									<td><label class="pull-right">商品编号：</label></td>
									<td>
										<input id="goodsNo" name="goodsNo" readonly="readonly" type="text" value="" class="form-control" style="width:150px;height:30px;">
									</td>
								</tr>
								<tr>
									<td><label class="pull-right">系统价格：</label></td>
									<td>
										<input id="costPrice" name="costPrice" maxlength="10" class="form-control required" readonly="readonly" style="width:150px;height:30px;"/>
									</td>
									<td><label class="pull-right">优惠价格：</label></td>
									<td><input id="goodsPrice" name="goodsPrice" maxlength="10" class="form-control required" readonly="readonly"   style="width:150px;height:30px;"/></td>
								</tr>
								<tr>
									<td><label class="pull-right">市场价格：</label></td>
									<td><input id="marketPrice" name="marketPrice" maxlength="10" class="form-control" readonly="readonly"  style="width:150px;height:30px;"/></td>
									<td><label class="pull-right">购买数量：</label></td>
									<td><input id="goodsNum" value="1" name="goodsNum" maxlength="10" class="form-control required" readonly="readonly" style="width:150px;height:30px;"/></td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>应付价格：</label></td>
									<td>
										<input id="orderAmount" name="orderAmount" maxlength="10" class="form-control required" style="width:150px;height:30px;"/>
									</td>
									<td><label class="pull-right"><font color="red">*</font>实际付款(前)：</label></td>
									<td>
										<input id="actualPayment" name="actualPayment" maxlength="10" class="form-control required" style="width:150px;height:30px;"/>
										<a href="#" id="compute" onclick="compute()">计算费用</a>
										<input type="hidden" id="computType" name="computType" />
										<div id="updateCompute">
											<a href="#" id="compute" onclick="updateCompute()">修改费用</a>
										</div>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right">实际付款(后)：</label></td>
									<td><input id="afterPayment" name="afterPayment" maxlength="10" class="form-control required" readonly="readonly" style="width:150px;height:30px;"/></td>
									<td><label class="pull-right">实际次数：</label></td>
									<td>
										<input id="remaintimes" name="remaintimes" maxlength="10" class="form-control required digits" style="width:150px;height:30px;"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right">欠款：</label></td>
									<td><input id="debtMoney" name="debtMoney" maxlength="10" class="form-control required" readonly="readonly"  style="width:150px;height:30px;"/></td>
									<td><label class="pull-right">余额：</label></td>
									<td><input id="spareMoney" name="spareMoney" maxlength="10" class="form-control required" readonly="readonly"  style="width:150px;height:30px;"/></td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>实际下单时间：</label></td>	
									<td>
										<input id="realityAddTime" name="realityAddTime" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/>
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