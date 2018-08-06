<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单修改</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<script type="text/javascript">
		//编辑业务员营业额
		function editOrderPushmoneyRecord(orderId,detailsId){
			$(".loading").show();
			var lock = true;
			top.layer.open({
	 			type: 2, 
	 		    area: ['800px', '600px'],
	 		    title:"新增/修改营业额",
	 		    content: "${ctx}/ec/returned/editOrderPushmoneyRecord?orderId="+orderId+"&detailsId="+detailsId,
	 		    btn: ['确定', '关闭']
	     	    ,yes: function(index, layero){
	     	    	if(lock){//加入锁,防止重复提交
		     	    	var obj =  layero.find("iframe")[0].contentWindow;
		     	    	var returnAmount = obj.document.getElementById("returnAmount").value; //退款金额
		     	    	var departmentId = obj.document.getElementById("departmentIds").value; //部门id
		     	    	var orderId = obj.document.getElementById("orderId").value; //订单id
		     	    	var returnedId = obj.document.getElementById("returnedId").value; //售后id
		     	    	
		     	    	//判断是否存在数据
		     	    	if(returnAmount==""){
		     	    		top.layer.alert('该订单无营业额数据', {icon: 0, title:'提醒'});
		     	    		return;
		     	    	}
		     	    	//校验当前每个业务员所在店铺的营业额+增减值>=0
		     	    	var num = obj.document.getElementById("num").value; //数据列表的个数
		     	    	for(var k = num; k > 0; k--){
			     	    	var y = obj.document.getElementById("added"+k).value; //每个业务员所在店铺的营业额
			     	    	var z = obj.document.getElementById("Amount"+k).value; //每条营业额对应输入的增减值
			     	    	
			     	    	var re = /^([+-]?)\d*\.?\d{0,2}$/; 
			     	    	if(!re.test(z) || z == "" || z == null || z == undefined){
			     	    		top.layer.alert('请输入正确的营业额(最多两位小数)', {icon: 0, title:'提醒'});
			     	    		return;
			     	    	}
		     	    		if(parseFloat(y) + parseFloat(z) < 0){
		     	    			top.layer.alert('营业额必须大于等于0', {icon: 0, title:'提醒'});
		     					return;
		     	    		}
		     	    	}
		     	    	//校验同部门的营业额总和  = 售后金额
		  			    var add = 0;//已扣减的值
		  			    var turnover = 0;//增减值
		  			    var totalAdd = 0;//增减值的合计
		  			    var surplusMoney = 0;//除本次售后外,该部门剩余分享的营业额
		  			    var pushMoneys = "";//输入的增减值拼接字符串
		  			    var flag = false;//判断部门业务员扣减营业额之和是否符合要求
		  			    departmentId = departmentId.split(",");
		  			    for(var i = 0; i < departmentId.length-1; i++){
		  			    	var addeds = "pushMoney"+departmentId[i];
		  			    	var added = obj.document.getElementsByName(addeds); //已扣减的值
		  			    	var departmentIds = "Amount"+departmentId[i];
		     	    		var dept = obj.document.getElementsByName(departmentIds); //当前输入的增减值
		     	    		
		     	    		for(var j = 0; j < added.length; j++){
		  				    	add += parseFloat($(added[j]).val())*10000;//已扣减的值(*10000 防止精度问题)
		  				    }
		  			    	
		  				    for(var k = 0; k < dept.length; k++){
		  				    	turnover += parseFloat($(dept[k]).val())*10000;//增减值(*10000 防止精度问题)
		  				    	pushMoneys += $(dept[k]).val() + ",";//输入的增减值拼接字符串
		  				    }
		  				    totalAdd = (add + turnover) / 10000;
		  				    totalAdd = totalAdd.toFixed(2);//扣减营业额合计-->可能出现精度丢失问题
		     	    		surplusMoney = obj.document.getElementById(departmentId[i]).value; //除本次售后外,该部门剩余分享的营业额
					    	//退款金额<除本次售后外,该部门剩余分享的营业额  --> 0<=部门业务员扣减营业额之和<=退款金额
		  				    if(parseFloat(returnAmount) < parseFloat(surplusMoney)){
			  				    if(parseFloat(totalAdd) > 0){
			  				        top.layer.alert('每个部门的营业额之和大于等于0,小于等于售后金额！', {icon: 0, title:'提醒'});
			  					    return;
			  				    }else if(parseFloat(returnAmount) + parseFloat(totalAdd) < 0){
			  				    	top.layer.alert('每个部门的营业额之和大于等于0,小于等于售后金额！', {icon: 0, title:'提醒'});
			  					    return;
			  				    }
		  				    }

		  				    //退款金额>=除本次售后外,该部门剩余分享的营业额  --> 0<=部门业务员扣减营业额之和<=除本次售后外,该部门剩余分享的营业额
		  				    if(parseFloat(returnAmount) >= parseFloat(surplusMoney)){
		  				    	if(parseFloat(totalAdd) > 0){
			  				        top.layer.alert('每个部门的营业额之和大于等于0,小于等于该部门剩余的分享营业额！', {icon: 0, title:'提醒'});
			  					    return;
			  				    }else if(parseFloat(surplusMoney) + parseFloat(totalAdd) < 0){
			  				    	top.layer.alert('每个部门的营业额之和大于等于0,小于等于该部门剩余的分享营业额！', {icon: 0, title:'提醒'});
			  					    return;
			  				    }
		  				    }
		  				  	add = 0;
		  				    turnover = 0;
		  				    totalAdd = 0;
		  				  	surplusMoney = 0;
		  			    }
		  			    
		  			    top.layer.close(index);
	     	    		lock = false;//上锁
		   	    		$.ajax({
		       				type:"post",
		       				data:{
		       					orderId:orderId,
		       					returnedId:returnedId,
		       					pushMoneys:pushMoneys
		       				 },
		       				url:"${ctx}/ec/returned/saveOrderPushmoneyRecord",
		       				success:function(date){
		       					if(date == 'success'){
		       						$(obj).parent().parent().remove();
		       						top.layer.alert('修改业务员成功', {icon: 1, title:'提醒'});
		       					}else{
				     	    		lock = true;//添加失败,还可以让修改. 解锁
		       						top.layer.alert('修改业务员失败', {icon: 0, title:'提醒'});
		       					}
	       						window.location="${ctx}/ec/returned/getTurnoverByOrderId?returnedId="+returnedId+"&orderId="+orderId;
		       				},
		       				error:function(XMLHttpRequest,textStatus,errorThrown){
		       					
		       				}
		       			});
	     	    	}
	 			},
				cancel: function(index){ //或者使用btn2
					$(".loading").hide();//按钮【按钮二】的回调
				}
	 		}); 
		}
		//编辑店铺营业额
		function editMtmyTurnoverDetails(orderId,detailsId){
			$(".loading").show();
			var lockTurnover = true;
			top.layer.open({
	 			type: 2, 
	 		    area: ['800px', '600px'],
	 		    title:"新增/修改营业额",
	 		    content: "${ctx}/ec/returned/editMtmyTurnoverDetails?orderId="+orderId+"&detailsId="+detailsId,
	 		    btn: ['确定', '关闭']
	     	    ,yes: function(index, layero){
	     	    	if(lockTurnover){//防止重复提交
		     	    	var obj =  layero.find("iframe")[0].contentWindow;
		     	    	var returnAmount = obj.document.getElementById("returnAmount").value; //退款金额
		     	    	
		     	    	var orderId = obj.document.getElementById("orderId").value; //订单id
		     	    	var detailsId = obj.document.getElementById("detailsId").value; //售后id
		     	    	var mappingId = obj.document.getElementById("mappingId").value; //mapping表的rec_id
		     	    	var goodsId = obj.document.getElementById("goodsId").value; //商品id
		     	    	var surplusTurnOver = obj.document.getElementById("surplusTurnOver").value; //除本次售后外,各门店剩余分享营业额之和
		     	    	
		     	    	//判断是否存在数据
		     	    	if(returnAmount==''){
		     	    		top.layer.alert('该订单无营业额数据', {icon: 0, title:'提醒'});
		     	    		return;
		     	    	}
		     	    	//校验输入营业额+增减值+数据库的增减值>=0
		     	    	var num = obj.document.getElementById("num").value; //数据列表的个数
		     	    	for(var k = 0; k < num; k++){
			     	    	var y = obj.document.getElementById("amount"+k).value; //获取每条对应输入的增减值
			     	    	var z = obj.document.getElementById("storeTurnover"+k).value; //获取业务员SUM营业额(存在已退)
		     	    		
			     	    	var re = /^([+-]?)\d*\.?\d{0,2}$/; 
			     	    	if(!re.test(y) || y == "" || y == null || y == undefined){
			     	    		top.layer.alert('请输入正确的营业额(最多两位小数)', {icon: 0, title:'提醒'});
			     	    		return;
			     	    	}
			     	    	
			     	    	if(parseFloat(y) + parseFloat(z) < 0){
		     	    			top.layer.alert('营业额必须大于等于0', {icon: 0, title:'提醒'});
		     					return;
		     	    		}
		     	    	}
		     	    	//校验所有店铺营业额合计值  = 售后金额
			  			var amount = 0;//店铺营业额
			  			var added = 0;
			  			var totalAdd = 0;//扣减的营业额总计
						var amounts = "";//店营业额增减值字符串
	  			    	var turnovers = obj.document.getElementsByName("amount"); //部门id
	     	    		var addeds = obj.document.getElementsByName("addeds"); //获取数据库查询的原本就有的营业额增减值
	     	    		for(var j = 0; j < turnovers.length; j++){
	  				    	amount += parseFloat($(turnovers[j]).val()) * 10000;
	  				    	added += parseFloat($(addeds[j]).val()) * 10000;
	  				    	amounts += $(turnovers[j]).val() + ",";
	  				    }
  				    	totalAdd = (amount + added)/10000;
	     	    		totalAdd = totalAdd.toFixed(2);
	     	    		
	  				    //退款金额>除本次售后外门店剩余分享营业额之和-->0<=门店扣减的营业额之和<=门店剩余分享营业额之和
	     	    		if(parseFloat(returnAmount) > parseFloat(surplusTurnOver)){
	  				    	if(parseFloat(totalAdd) > 0){
		  				        top.layer.alert('店铺的营业额之和大于等于0,小于等于店铺剩余的分享营业额！', {icon: 0, title:'提醒'});
		  					    return;
		  				    }else if(parseFloat(surplusTurnOver) + parseFloat(totalAdd) < 0){
		  				    	top.layer.alert('店铺的营业额之和大于等于0,小于等于店铺剩余的分享营业额！', {icon: 0, title:'提醒'});
		  					    return;
		  				    }
	  				    }
	  				    //退款金额<=除本次售后外门店剩余分享营业额之和-->0<=门店扣减营业额之和<=退款金额
	     	    		if(parseFloat(returnAmount) <= parseFloat(surplusTurnOver)){
		  				    if(parseFloat(totalAdd) > 0){
		  				        top.layer.alert('店铺的营业额之和大于等于0,小于等于售后金额！', {icon: 0, title:'提醒'});
		  					    return;
		  				    }else if(parseFloat(returnAmount) + parseFloat(totalAdd) < 0){
		  				    	top.layer.alert('店铺的营业额之和大于等于0,小于等于售后金额！', {icon: 0, title:'提醒'});
		  					    return;
		  				    }
	  				    }
	  				    
	  				    top.layer.close(index);
	  				    lockTurnover = false;//上锁,防止重复提交
		   	    		$.ajax({
		       				type:"post",
		       				data:{
		       					orderId:orderId,
		       					detailsId:detailsId,
		       					mappingId:mappingId,
		       					goodsId:goodsId,
		       					amounts:amounts
		       				 },
		       				url:"${ctx}/ec/returned/saveMtmyTurnoverDetails",
		       				success:function(date){
		       					if(date == 'success'){
		       						$(obj).parent().parent().remove();
		       						top.layer.alert('修改店铺营业额成功', {icon: 0, title:'提醒'});
		       					}else{
		       						lockTurnover = true;//添加失败,还可以让修改. 解锁
		       						top.layer.alert('修改店铺营业额失败', {icon: 0, title:'提醒'});
		       					}
	       						window.location="${ctx}/ec/returned/getTurnoverByOrderId?returnedId="+detailsId+"&orderId="+orderId;
		       				},
		       				error:function(XMLHttpRequest,textStatus,errorThrown){
		       					
		       				}
		       			});
	     	    	}
	     	    },
				cancel: function(index){ //或者使用btn2
					$(".loading").hide();//按钮【按钮二】的回调
				}
	 		}); 
		}
</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="clearfix">
				<h5>业务员营业额：</h5>
				<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">时间</th>
							<th style="text-align: center;">类型</th>
							<th style="text-align: center;">售后金额</th>
							<th style="text-align: center;">业务员</th>
							<th style="text-align: center;">部门</th>
							<th style="text-align: center;">手机号</th>
							<th style="text-align: center;">营业额</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody>
						${pushmoneyTurnover}
					</tbody>						
				</table>
				<h5>店营业额：</h5>
				<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">时间</th>
							<th style="text-align: center;">类型</th>
							<th style="text-align: center;">售后金额</th>
							<th style="text-align: center;">店名</th>
							<th style="text-align: center;">营业额</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody>
						${shopTurnover}
					</tbody>						
				</table>
			</div>
		</div>
	</div>
	<div class="loading" id="loading"></div>
</body>
</html>