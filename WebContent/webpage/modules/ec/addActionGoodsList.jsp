<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>创建活动列表</title>
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

	
	function addActionGoods(){
		
		top.layer.open({
		    type: 2, 
		    area: ['800px', '600px'],
		    title:"添加商品",
		    content: "${ctx}/ec/action/addActionGoodsForm?actionId="+$("#actionId").val(),
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		    	var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
		        var all = new Array(); //定义变量全部保存
		        var obj =  layero.find("iframe")[0].contentWindow;  
				var ifmObj = obj.document.getElementById("select2").options; 
		    	$(ifmObj).each(function () {
		    	  var txt = $(this).text(); //获取单个text
		    	   var val = $(this).val(); //获取单个value
		    	         // var node = val;
		    	      arr.push(val);
		    	      all.push(txt);
		    	 });
		      $("#goodsId").val(arr);
		    // $("#goodsName").val(all);
				var goodsId= $("#goodsId").val();
				var actionId=$("#actionId").val();
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
					if(date=="success"){
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
	
	function dellActionGoods(id){
		var actionId=$("#actionId").val();
		var num=0;
		$.ajax({
			type:"post",
			async:false,
			data:{
				goodsId:id
				
			 },
			url:"${ctx}/ec/action/numByGoodsId",
			success:function(date){
				num=date;
				
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
						    
			}
		});
		
		if(num>0){
			top.layer.alert('该商品已经购买不可移除!', {icon: 0, title:'提醒'});
			return;
		}else{

			$.ajax({
				type:"post",
				data:{
					goodsId:id,
					actionId:0,
					actionType:0
				 },
				url:"${ctx}/ec/action/dellGoods",
				success:function(date){
					if(date="success"){
						top.layer.alert('移除成功!', {icon: 0, title:'提醒'});
						window.location="${ctx}/ec/action/addActionGoodsList?actionId="+actionId;
					}
								
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
							    
				}
			});
		}
		
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
						top.layer.alert('添加成功!', {icon: 0, title:'提醒'});
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
					<form:form id="inputForm" modelAttribute="actionInfo" action="${ctx}/ec/action/saveGoods" method="post" class="form-horizontal">
						<form:hidden path="actionId"/>	
						<form:hidden path="goodsId"/>
						<form:hidden path="goodsName"/>
					</form:form>	
				</div>
				<div>
					<a href="#" onclick="addActionGoods()" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加商品</a>
				</div>
				<p></p>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">商品名称</th>
							<th style="text-align: center;">货号</th>
							<th style="text-align: center;">商品分类</th>
							<th style="text-align: center;">成本价</th>
							<th style="text-align: center;">总库存</th>
							<th style="text-align: center;">剩余库存</th>
							<th style="text-align: center;">抢购价</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${list}" var="list">
							<tr>
								<td>${list.goodsId}</td>
								<td>${list.goodsName}</td>
								<td>${list.goodsSn}</td>
								<td>${list.goodsCategory.name}</td>
								<td>${list.costPrice}</td>
								<td>${list.totalStore}</td>
								<td>${list.storeCount}</td>
							 	<td>${list.shopPrice}</td>
								<td>
								 <a href="#" onclick="dellActionGoods(${list.goodsId})" class="btn btn-danger btn-xs" ><i class="fa fa-close"></i>移除</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
</body>
</html>