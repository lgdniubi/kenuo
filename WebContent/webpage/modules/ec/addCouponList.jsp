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
		var num=0;
		$.ajax({
			type:"post",
			async:false,
			data:{
				id:id
				
			 },
			url:"${ctx}/ec/activity/numByCouponId",
			success:function(date){
				num=date;
				
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
						    
			}
		});
		
		if(num>0){
			top.layer.alert('该红包已有人领取不可修改!', {icon: 0, title:'提醒'});
			return;
		}
		
		
		top.layer.open({
		    type: 2, 
		    area: ['850px', '600px'],
		    title:"编辑红包",
		    content: "${ctx}/ec/activity/addCouponForm?id="+id,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		        var obj =  layero.find("iframe")[0].contentWindow;
				var ifmObj = obj.document.getElementById("select2").options; 
				var baseAmount = obj.document.getElementById("baseAmount");
		    	var couponMoney=obj.document.getElementById("couponMoney");
				var totalNumber=obj.document.getElementById("totalNumber");
				var couponName=obj.document.getElementById("couponName");
				var ceiling=obj.document.getElementById("ceiling");
				var couponType=obj.document.getElementById("couponType");
				var usedType=obj.document.getElementById("usedType");
		        var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
		        var all = new Array(); //定义变量全部保存
		    	$(ifmObj).each(function () {
		    	  var txt = $(this).text(); //获取单个text
		    	   var val = $(this).val(); //获取单个value
		    	         // var node = val;
		    	      arr.push(val);
		    	      all.push(txt);
		    	 });
		     $("#goodsId").val(arr);
			 $("#goodsName").val(all);
			 $("#cateId").val(arr);
			 $("#cateName").val(all);
		      
				
				
				var r =/^\d+\.?\d{0,2}$/;
				var reg=/^\+?[1-9]\d*$/;
				
				if($(couponName).val()==""){
					top.layer.alert('红包名称不能为空!', {icon: 0, title:'提醒'}); 
					  return;
				}
				if($(baseAmount).val()<0){
					top.layer.alert('满减金额要大于等于0!', {icon: 0, title:'提醒'}); 
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
					top.layer.alert('红包数量要大于0!', {icon: 0, title:'提醒'}); 
					  return;
				}
				if($(ceiling).val()<=0){
					top.layer.alert('领取上限数要大于0!', {icon: 0, title:'提醒'}); 
					  return;
				}
			

				//异步保存红包
				$.ajax({
				type:"post",
				data:{
					id:id,
					couponName:$(couponName).val(),
					baseAmount:$(baseAmount).val(),
					couponMoney:$(couponMoney).val(),
					totalNumber:$(totalNumber).val(),
					actionId:$("#id").val(),
					ceiling:$(ceiling).val(),
					couponType:$(couponType).val(),
					usedType:$(usedType).val(),
					goodsId:$("#goodsId").val(),
					goodsName:$("#goodsName").val(),
					cateId:$("#cateId").val(),
					cateName:$("#cateName").val()
				 },
				url:"${ctx}/ec/activity/saveCoupon",
				success:function(date){
					if(date=="success"){
						top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
						window.location="${ctx}/ec/activity/Couponlist?id="+$("#id").val();
					}
					if(date=="error"){
						top.layer.alert('保存失败!', {icon: 2, title:'提醒'});
						window.location="${ctx}/ec/activity/Couponlist?id="+$("#id").val();
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
				id:id,
				status:status
			 },
			url:"${ctx}/ec/activity/updateCouponStatus",
			success:function(date){
				
				if(date=="success"){
					top.layer.alert('操作成功!', {icon: 1, title:'提醒'});
					window.location="${ctx}/ec/activity/Couponlist?id="+$("#id").val();
				}
				if(date=="error"){
					top.layer.alert('操作失败!', {icon: 2, title:'提醒'});
					window.location="${ctx}/ec/activity/Couponlist?id="+$("#id").val();
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
					<form:form id="inputForm" modelAttribute="activity" action="${ctx}/ec/coupon/saveGoods" method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<form:hidden path="goodsId"/>
						<form:hidden path="goodsName"/>
						<form:hidden path="cateId"/>
						<form:hidden path="cateName"/>
					</form:form>	
				</div>
				<div>
<%-- 					<c:if test="${activityCoupon.dateCompare>0}"> --%>
<!-- 						<a href="#" style="background: #C0C0C0; color: #FFF" class="btn  btn-xs"><i class="fa fa-plus"></i>添加红包</a> -->
<%-- 					</c:if> --%>
<%-- 					<c:if test="${activityCoupon.dateCompare<0}"> --%>
						<a href="#" onclick="addActionGoods(0)" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加红包</a>
<%-- 					</c:if> --%>
					
				</div>
				<p></p>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">红包名称</th>
							<th style="text-align: center;">红包类型</th>
							<th style="text-align: center;">满减金额</th>
							<th style="text-align: center;">红包金额</th>
							<th style="text-align: center;">红包数量</th>
							<th style="text-align: center;">剩余数量</th>
							<th style="text-align: center;">使用范围</th>
							<th style="text-align: center;">领取上限</th>
							<th style="text-align: center;">创建时间</th>
							<th style="text-align: center;">创建者</th>
							<th style="text-align: center;">状态</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${list}" var="list">
							<tr>
								<td>${list.id}</td>
								<td>${list.couponName}</td>
								<c:if test="${list.couponType==1}">
									<td>商品详情页</td>
								</c:if>
								<c:if test="${list.couponType==2}">
									<td>活动页</td>
								</c:if>
								<c:if test="${list.couponType==3}">
									<td>新注册</td>
								</c:if>
								<c:if test="${list.couponType==4}">
									<td>手工红包</td>
								</c:if>
								<td>${list.baseAmount}</td>
								<td>${list.couponMoney}</td>
								<td>${list.totalNumber}</td>
								<td>${list.couponNumber}</td>
								<c:if test="${list.usedType==1}">
									<td>全部商品</td>
								</c:if>
								<c:if test="${list.usedType==2}">
									<td>指定分类</td>
								</c:if>
								<c:if test="${list.usedType==3}">
									<td>指定商品</td>
								</c:if>
								<td>${list.ceiling}</td>
							 	<td><fmt:formatDate value="${list.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${list.createBy.name}</td>
								<c:if test="${list.status==1}">
									<td>发放</td>
								</c:if>
								<c:if test="${list.status==2}">
									<td>关闭</td>
								</c:if>	
								<td>
								<c:if test="${list.status==2}">
									<a href="#" onclick="updateStatus('${list.id}','1')" class="btn btn-primary btn-xs" ><i class="fa fa-file"></i>开启</a>
								</c:if>
								<c:if test="${list.status==1}">
									<a href="#" onclick="updateStatus('${list.id}','2')" class="btn btn-danger btn-xs" ><i class="fa fa-close"></i>关闭</a>
								</c:if>	
								<c:if test="${list.status==2}">
<%-- 	                                <c:if test="${list.totalNumber!=list.couponNumber}"> --%>
<!-- 	                                	<a href="#" style="background: #C0C0C0; color: #FFF" class="btn  btn-xs"><i class="fa fa-edit"></i>修改</a> -->
<%-- 	                                </c:if>	 --%>
<%-- 	                                <c:if test="${list.totalNumber==list.couponNumber}"> --%>
                                		<a href="#" onclick="addActionGoods(${list.id})" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>修改</a>
<%--                                 	</c:if>	 --%>
	                            </c:if>
	                            <c:if test="${list.status==1}">
	                            <a href="#" style="background: #C0C0C0; color: #FFF" class="btn  btn-xs"><i class="fa fa-edit"></i>修改</a>
	                             </c:if>   
                                
								<a href="#" onclick="openDialogView('信息列表', '${ctx}/ec/activity/CateGoodsList?id=${list.id}&usedType=${list.usedType}','300px','400px')"
												class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>查看</a>
								
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