<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单修改</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<script type="text/javascript">
		$(document).ready(function() {
	    });
		//编辑业务员营业额
		function editOrderPushmoneyRecord(orderId,detailsId){
			top.layer.open({
	 			type: 2, 
	 		    area: ['800px', '600px'],
	 		    title:"新增/修改营业额：（提示：营业额数字不能修改，但可以插入正负数增减）",
	 		    content: "${ctx}/ec/returned/editOrderPushmoneyRecord?orderId="+orderId+"&detailsId="+detailsId,
	 		    btn: ['确定', '关闭']
	     	    ,yes: function(index, layero){
	     	    	var obj =  layero.find("iframe")[0].contentWindow;
	     	    	var returnAmount = obj.document.getElementById("returnAmount").value; //退款金额
	     	    	var departmentId = obj.document.getElementById("departmentIds").value; //部门id
	     	    	var orderId = obj.document.getElementById("orderId").value; //订单id
	     	    	var returnedId = obj.document.getElementById("returnedId").value; //售后id
	     	    	
	     	    	//判断是否存在数据
	     	    	if(returnAmount==""){
	     	    		top.layer.alert('该订单无数据', {icon: 0, title:'提醒'});
	     	    		return;
	     	    	}
	     	    	//校验输入营业额+增减值+数据库的增减值>=0
	     	    	var num = obj.document.getElementById("num").value; //数据列表的个数
	     	    	for(var k = 0; k < num; k++){
		     	    	var y = obj.document.getElementById("Amount"+k).value; //获取每条对应输入的增减值
		     	    	var z = obj.document.getElementById("beauticianTurnover"+k).value; //获取业务员SUM营业额(存在已退)
		     	    	
		     	    	var re = /^([+-]?)\d*\.?\d{0,2}$/; 
		     	    	if(!re.test(y)){
		     	    		top.layer.alert('请输入正确的营业额(最多两位小数)', {icon: 0, title:'提醒'});
		     	    		return;
		     	    	}
		     	    	
	     	    		if(parseFloat(y) + parseFloat(z) < 0){
	     	    			top.layer.alert('营业额必须大于等于0', {icon: 0, title:'提醒'});
	     					return;
	     	    		}
	     	    	}
	     	    	//校验同部门的营业额的值 = 售后金额
	  			    var turnover = 0;//部门营业额
	  			    var pushMoneys = "";//当前营业额
	  			    var add = 0;
	  			    departmentId = departmentId.split(",");
	  			    for(var i = 0; i < departmentId.length-1; i++){
	  			    	var departmentIds = "Amount"+departmentId[i];
	     	    		var dept = obj.document.getElementsByName(departmentIds); //部门id
	  			    	var addeds = "added"+departmentId[i];//获取数据库查询的原本就有的营业额增减值
	  			    	var added = obj.document.getElementsByName(addeds); //部门id
	  				    for(var j = 0; j < dept.length; j++){
	  				    	turnover += parseFloat($(dept[j]).val());
	  				    	add += parseFloat($(added[j]).val());
	  				    	pushMoneys += $(dept[j]).val() + ",";
	  				    }
	  				    if(parseFloat(returnAmount) + parseFloat(turnover) + parseFloat(add) != 0){
	  				        top.layer.alert('每个部门的营业额之和必须等于售后金额！', {icon: 0, title:'提醒'});
	  					    return;
	  				    }
	  				  	add = 0;
	  				    turnover = 0;
	  			    }
	  			    
	  			  return;
	  			    
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
	       						top.layer.alert('修改业务员成功', {icon: 0, title:'提醒'});
	       						window.location="${ctx}/ec/returned/getTurnoverByOrderId?returnedId="+returnedId+"&orderId="+orderId;
	       	     				top.layer.close(index);
	       					}else{
	       						top.layer.alert('修改业务员失败', {icon: 0, title:'提醒'});
	       					}
	       				},
	       				error:function(XMLHttpRequest,textStatus,errorThrown){
	       					
	       				}
	       			});
	 			}
	 		}); 
		}
		//编辑店铺营业额
		function editMtmyTurnoverDetails(orderId,detailsId,mappingId){
			top.layer.open({
	 			type: 2, 
	 		    area: ['800px', '600px'],
	 		    title:"新增/修改营业额：（提示：营业额数字不能修改，但可以插入正负数增减）",
	 		    content: "${ctx}/ec/returned/editMtmyTurnoverDetails?orderId="+orderId+"&detailsId="+detailsId+"&mappingId="+mappingId,
	 		    btn: ['确定', '关闭']
	     	    ,yes: function(index, layero){
	     	    	var obj =  layero.find("iframe")[0].contentWindow;
	     	    	var returnAmount = obj.document.getElementById("returnAmount").value; //退款金额
	     	    	
	     	    	var orderId = obj.document.getElementById("orderId").value; //订单id
	     	    	var detailsId = obj.document.getElementById("detailsId").value; //售后id
	     	    	
	     	    	//判断是否存在数据
	     	    	if(returnAmount==''){
	     	    		top.layer.alert('该订单无数据', {icon: 0, title:'提醒'});
	     	    		return;
	     	    	}
	     	    	//校验输入营业额+增减值+数据库的增减值>=0
	     	    	var num = obj.document.getElementById("num").value; //数据列表的个数
	     	    	for(var k = 0; k < num; k++){
		     	    	var y = obj.document.getElementById("amount"+k).value; //获取每条对应输入的增减值
		     	    	var z = obj.document.getElementById("storeTurnover"+k).value; //获取业务员SUM营业额(存在已退)
	     	    		
		     	    	var re = /^([+-]?)\d*\.?\d{0,2}$/; 
		     	    	if(!re.test(y)){
		     	    		top.layer.alert('请输入正确的营业额(最多两位小数)', {icon: 0, title:'提醒'});
		     	    		return;
		     	    	}
		     	    	
		     	    	if(parseFloat(y) + parseFloat(z) < 0){
	     	    			top.layer.alert('营业额必须大于等于0', {icon: 0, title:'提醒'});
	     					return;
	     	    		}
	     	    	}
	     	    	//校验同部门的营业额的值 = 售后金额
		  			var amount = 0;//店铺营业额
		  			var added = 0;
					var amounts = "";//店营业额增减值字符串
  			    	var turnovers = obj.document.getElementsByName("amount"); //部门id
     	    		var addeds = obj.document.getElementsByName("addeds"); //获取数据库查询的原本就有的营业额增减值
     	    		for(var j = 0; j < turnovers.length; j++){
  				    	amount += parseFloat($(turnovers[j]).val());
  				    	added += parseFloat($(addeds[j]).val());
  				    	amounts += $(turnovers[j]).val() + ",";
  				    }
  				    if(parseFloat(amount) + parseFloat(added) + parseFloat(returnAmount) != 0){
  					    top.layer.alert('每个店铺的营业额之和必须等于售后金额！', {icon: 0, title:'提醒'});
  					    return;
  				    }
	   	    		$.ajax({
	       				type:"post",
	       				data:{
	       					orderId:orderId,
	       					detailsId:detailsId,
	       					amounts:amounts
	       				 },
	       				url:"${ctx}/ec/returned/saveMtmyTurnoverDetails",
	       				success:function(date){
	       					if(date == 'success'){
	       						$(obj).parent().parent().remove();
	       						top.layer.alert('修改店铺营业额成功', {icon: 0, title:'提醒'});
	       						window.location="${ctx}/ec/returned/getTurnoverByOrderId?returnedId="+detailsId+"&orderId="+orderId;
	       	     				top.layer.close(index);
	       					}else{
	       						top.layer.alert('修改店铺营业额失败', {icon: 0, title:'提醒'});
	       					}
	       				},
	       				error:function(XMLHttpRequest,textStatus,errorThrown){
	       					
	       				}
	       			});
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
							<th style="text-align: center;">金额</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody id="pushMoneyList">
						<tr>
							<c:if test="${empty mtmyTurnoverDetails.createDate}">
								<th style="text-align: center;"><fmt:formatDate value="${mtmyTurnoverDetails.applyDate}" pattern="yyyy-MM-dd HH:mm:ss" /></th>
							</c:if>
							<c:if test="${not empty mtmyTurnoverDetails.createDate}">
								<th style="text-align: center;"><fmt:formatDate value="${mtmyTurnoverDetails.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></th>
							</c:if>
							<th style="text-align: center;">售后</th>
							<th style="text-align: center;">${mtmyTurnoverDetails.amount}</th>
							<th style="text-align: center;">
								<a href="#" onclick="editOrderPushmoneyRecord('${mtmyTurnoverDetails.orderId}','${mtmyTurnoverDetails.detailsId}')"  class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>编辑</a>
								<a href="#" onclick="openDialogView('查看日志记录', '${ctx}/ec/orders/operationLog?orderId=${mtmyTurnoverDetails.orderId}','800px','600px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 日志记录</a>
							</th>
						</tr>
					</tbody>						
				</table>
				<h5>店营业额：</h5>
				<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">时间</th>
							<th style="text-align: center;">类型</th>
							<th style="text-align: center;">金额</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody id="officeList">
						<tr>
							<c:if test="${empty mtmyTurnoverDetails.createDate}">
								<th style="text-align: center;"><fmt:formatDate value="${mtmyTurnoverDetails.applyDate}" pattern="yyyy-MM-dd HH:mm:ss" /></th>
							</c:if>
							<c:if test="${not empty mtmyTurnoverDetails.createDate}">
								<th style="text-align: center;"><fmt:formatDate value="${mtmyTurnoverDetails.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></th>
							</c:if>
							<th style="text-align: center;">售后</th>
							<th style="text-align: center;">${mtmyTurnoverDetails.amount}</th>
							<th style="text-align: center;">
								<a href="#" onclick="editMtmyTurnoverDetails('${mtmyTurnoverDetails.orderId}','${mtmyTurnoverDetails.detailsId}',${mtmyTurnoverDetails.mappingId})"  class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>编辑</a>
								<a href="#" onclick="openDialogView('操作日志', '${ctx}/ec/returned/findMtmyTurnoverDetailsList?orderId=${mtmyTurnoverDetails.orderId}&mappingId=${mtmyTurnoverDetails.mappingId}','750px','600px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 操作日志</a>
							</th>
						</tr>
					</tbody>						
				</table>
			</div>
		</div>
	</div>
</body>
</html>