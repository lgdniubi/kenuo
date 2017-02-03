<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>添加商品服务费</title>
<meta name="decorator" content="default" />
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
					if(date.isReal==1){
						$("#service").show();
						//$("#idnum").hide();
						$("#goodsnum").attr("readonly",true);
						$("#goodsnum").val(1);
					}else{
						//$("#idnum").show();
						$("#goodsnum").attr("readonly",false);
						$("#service").hide();
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
	
	$(document).ready(function() {
		
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

							$("#service").hide();
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

		//根据商品分类查询商品
		//$("#goodselectButton, #goodselectName").click(function(){	    增加  , #goodselectName  文本框有点击事件
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

			
				validateForm = $("#inputForm").validate({
					rules : {
						basisFee:{
							number:true,
							min:0
						},
						primary:{
							number:true,
							min:0
						},
						middle:{
							number:true,
							min:0
						},
						high:{
							number:true,
							min:0
						},
						internship:{
							number:true,
							min:0
						},
						store:{
							number:true,
							min:0
						},
						prther:{
							number:true,
							min:0
						}
						

					},
					messages:{
						basisFee:{
							number:"输入合法的数",
							min:"最小为0"
						},
						primary:{
							number:"输入合法的数",
							min:"最小为0"
						},
						middle:{
							number:"输入合法的数",
							min:"最小为0"
						},
						high:{
							number:"输入合法的数",
							min:"最小为0"
						},
						internship:{
							number:"输入合法的数",
							min:"最小为0"
						},
						store:{
							number:"输入合法的数",
							min:"最小为0"
						},
						prther:{
							number:"输入合法的数",
							min:"最小为0"
						}
					
							
					},

					submitHandler : function(form) {
						//loading('正在提交，请稍等...');
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
					<form:form id="inputForm" modelAttribute="goodsBeauticianFee" action="${ctx}/ec/beautician/save" method="post" class="form-horizontal">
						<form:hidden path="goodsId"/>
						<label>商品名称：</label>
						<form:input path="goodsName" maxlength="60" class="form-control required" readonly="true"  style="width:150px;height:30px;"/>
						<label>商品编号：</label>
						<form:input path="goodsNo"  maxlength="60" class="form-control required" readonly="true"  style="width:150px;height:30px;"/>
						<hr>
						<p></p>
						<label ><font color="red">*</font>项目基础业绩：</label>
						<form:input path="basisFee"  maxlength="10" class="form-control required"  style="width:150px;height:30px;"/>
						<p></p>
						<label ><font color="red">*</font>初级费用:</label>
						<form:input path="primary"  maxlength="10" class="form-control required"  style="width:150px;height:30px;"/>
						<label ><font color="red">*</font>中级费用:</label>
						<form:input path="middle"  maxlength="10" class="form-control required"  style="width:150px;height:30px;"/>
						<p></p>
						<label><font color="red">*</font>高级费用:</label>
						<form:input path="high"  maxlength="10" class="form-control required"  style="width:150px;height:30px;"/>
						<label ><font color="red">*</font>实习费用:</label>
						<form:input path="internship" maxlength="10" class="form-control required"  style="width:150px;height:30px;"/>
						<p></p>
						<label ><font color="red">*</font>储备费用:</label>
						<form:input path="store" maxlength="10" class="form-control required"  style="width:150px;height:30px;"/>
						<label ><font color="red">*</font>项目师费用:</label>
						<form:input path="prther"   maxlength="10" class="form-control required"  style="width:150px;height:30px;"/>
						<p></p>
						<label>备注说明:</label>
						<form:textarea path="remarks" rows="4"  maxlength="200" style="width:400px;" class="form-control"></form:textarea>
					</form:form>
				</div>
			</div>
		</div>
	</div>
	
</body>
</html>