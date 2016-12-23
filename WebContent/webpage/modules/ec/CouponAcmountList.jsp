<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>创建红包列表</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->


<script type="text/javascript">
	var validateForm;
	
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			
			$("#inputForm").submit();
			return true;
		}

		return false;
	}

	
	function addActionGoods(id){
		
		top.layer.open({
		    type: 2, 
		    area: ['600px', '500px'],
		    title:"添加面值",
		    content: "${ctx}/ec/coupon/addCouponForm?amountId="+id,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		        var obj =  layero.find("iframe")[0].contentWindow;  
				var baseAmount = obj.document.getElementById("baseAmount"); 
		    	var couponMoney=obj.document.getElementById("couponMoney");
				var totalNumber=obj.document.getElementById("totalNumber");
				var amountName=obj.document.getElementById("amountName");
				var r =/^\d+\.?\d{0,2}$/;
				var reg=/^\+?[1-9]\d*$/;
				
				if($(amountName).val()==""){
					top.layer.alert('面值名称不能为空!', {icon: 0, title:'提醒'}); 
					  return;
				}
				if($(baseAmount).val()<=0){
					top.layer.alert('满减金额要大于0!', {icon: 0, title:'提醒'}); 
					return;
				}
				if(!r.test($(baseAmount).val())){
					top.layer.alert('满减金额最多保留2位小数!', {icon: 0, title:'提醒'}); 
					return;
				}
				if($(couponMoney).val()<=0){
					top.layer.alert('红包金额要大于0!', {icon: 0, title:'提醒'}); 
					  return;
				}
				if(!r.test($(couponMoney).val())){
					top.layer.alert('红包金额最多保留2位小数!', {icon: 0, title:'提醒'}); 
					  return;
				}
				if($(totalNumber).val()<=0){
					top.layer.alert('红包数量要大于0正整数!', {icon: 0, title:'提醒'}); 
					  return;
				}
			

				//异步保存红包
				$.ajax({
				type:"post",
				data:{
					amountId:id,
					amountName:$(amountName).val(),
					baseAmount:$(baseAmount).val(),
					couponMoney:$(couponMoney).val(),
					totalNumber:$(totalNumber).val(),
					couponId:$("#couponId").val()
				 },
				url:"${ctx}/ec/coupon/saveAmount",
				success:function(date){
					if(date=="success"){
						top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
						window.location="${ctx}/ec/coupon/addCoupon?couponId="+$("#couponId").val();
					}
					if(date=="error"){
						top.layer.alert('保存失败!', {icon: 2, title:'提醒'});
						window.location="${ctx}/ec/coupon/addCoupon?couponId="+$("#couponId").val();
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

	}
	
	function updateStatus(id,status){
		$.ajax({
			type:"post",
			data:{
				amountId:id,
				status:status
			 },
			url:"${ctx}/ec/coupon/updateStatus",
			success:function(date){
				if(date=="success"){
					top.layer.alert('操作成功!', {icon: 1, title:'提醒'});
					window.location="${ctx}/ec/coupon/addCoupon?couponId="+$("#couponId").val();
				}
				if(date=="error"){
					top.layer.alert('操作失败!', {icon: 2, title:'提醒'});
					window.location="${ctx}/ec/coupon/addCoupon?couponId="+$("#couponId").val();
				}
				
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
						    
			}
		});

		
	}
	
	function addGoodsNum(goodsId){

		top.layer.open({
		    type: 2, 
		    area: ['600px', '400px'],
		    title:"添加商品",
		    content: "${ctx}/ec/goods/fromspecstocks?id="+goodsId,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		    	var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
		        var all = new Array(); //定义变量全部保存
		        var obj =  layero.find("iframe")[0].contentWindow;  
				var ifmObj = obj.document.getElementsByTagName("input");
		    	for(var i=0; i<ifmObj.length; i++){
		    	    if(ifmObj[i].type=='number'){
		    	    	arr.push(ifmObj[i].id);
		    	    	all.push(ifmObj[i].value);
		    	    }
		    	}   
				if(goodsId==""){
					return;
				}
				
				$.ajax({
				type:"post",
				data:{
					goodsId:goodsId,
					actionId:actionId,
					actionType:1
				 },
				url:"${ctx}/ec/action/saveGoods",
				success:function(date){
					if(date="success"){
						top.layer.alert('添加成功!', {icon: 1, title:'提醒'});
						window.location="${ctx}/ec/action/addActionGoodsList?actionId="+actionId;
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
	}

	$(document).ready(function() {
		

			
				validateForm = $("#inputForm").validate({
					rules : {
						

					},
					messages:{
							
					},

					submitHandler : function(form) {
						loading('正在提交，请稍等...');
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
					<form:form id="inputForm" modelAttribute="couponAcmountMapping" action="${ctx}/ec/coupon/saveGoods" method="post" class="form-horizontal">
						<form:hidden path="couponId"/>	
					</form:form>	
				</div>
				<div>
				</div>
				<p></p>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">面值名称</th>
							<th style="text-align: center;">满减金额</th>
							<th style="text-align: center;">红包金额</th>
							<th style="text-align: center;">红包数量</th>
							<th style="text-align: center;">剩余数量</th>
							<th style="text-align: center;">创建时间</th>
							<th style="text-align: center;">创建者</th>
							<th style="text-align: center;">状态</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${list}" var="list">
							<tr>
								<td>${list.amountId}</td>
								<td>${list.amountName}</td>
								<td>${list.baseAmount}</td>
								<td>${list.couponMoney}</td>
								<td>${list.totalNumber}</td>
								<td>${list.couponNumber}</td>
							 	<td><fmt:formatDate value="${list.createDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${list.createBy.name}</td>
								<c:if test="${list.status==1}">
									<td>未发放</td>
								</c:if>
								<c:if test="${list.status==2}">
									<td>发放中</td>
								</c:if>	
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
</body>
</html>