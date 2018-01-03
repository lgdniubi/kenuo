<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
	<title>编辑订单业务员</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	
	<script type="text/javascript">
		function getSysUserInfo(){
			var isCommitted = false;		//表单是否已经提交标识，默认为false
			var pushmoneyUserIds = document.getElementsByName("pushmoneyUserId");
	 		// 正常打开	
	 		top.layer.open({
	 			type: 2, 
	 		    area: ['550px', '420px'],
	 		    title:"业务员选择",
	 		    content: "${ctx}/ec/orders/getPushmoneyView",
	 		    btn: ['确定', '关闭']
	     	    ,yes: function(index, layero){
	     	    	var obj =  layero.find("iframe")[0].contentWindow;
	     	    	var coffee = obj.document.getElementsByName("coffee"); //单选框
	     	    	var num = obj.document.getElementById("num").value; //单选框是否被选中
	     	    	var departmentId = obj.document.getElementsByName("departmentId"); //部门id
	     	    	var userId = '';
	     	    	if(coffee.length <= 0){
						top.layer.alert('请选择一条数据!', {icon: 0, title:'提醒'});
	     	    		return;
					}
				 	if(num == 0){
			        	top.layer.alert('请选择一条数据!', {icon: 0, title:'提醒'}); 
			        	return;
				 	}
	     	    	for (i=0;i<coffee.length;i++){
	 		       		if(coffee[i].checked == true){
	 		       			userId = coffee[i].value;
	 		       			chooseDepartmentId = departmentId[i].value;
	 		       			if(chooseDepartmentId == 0){
	 		       				top.layer.alert('请先绑定该业务员的部门！', {icon: 0, title:'提醒'});
    	     	    			return;
	 		       			}
	 		       			if(pushmoneyUserIds.length > 0){
	 	  	    				for(j=0;j<pushmoneyUserIds.length;j++){
	 	       	    	       		if(userId == pushmoneyUserIds[j].value){
	 	       	    	        		top.layer.alert('业务员不能相同！', {icon: 0, title:'提醒'});
	 	       	     	    			return;
	 	       	    	        	}
	 	       	    	    	}
	 		       			}
	 		       		}
	 		        }
					
	     	    	//防止表单多次提交
				    if(isCommitted == false){
				    	isCommitted = true;		//提交表单后，将表单是否已经提交标识设置为true
				   	}else{
				       	return false;			
					}
	     	    	
	     	    	$.ajax({
	    				type:"get",
	    				dataType:"json",
	    				data:{
	    					id:userId
	    				},
	    				url:"${ctx}/ec/orders/querySysUser",                   //老数据进来，只能增减，不能改变营业额，而新增加的业务员只能改变营业额，不能改变增减值
	    				success:function(data){                                //因此为了方便数据的入库，咖啡让挖个坑，让营业额和增减值的name发生调换，统一入增减值的值
	    					$.each(data,function(index,item){                                          
	    						$("#mytable").append("<tr><td style='text-align: center;'>"+item.name+"<input id='pushmoneyUserId' name='pushmoneyUserId' type='hidden' value='"+item.id+"'></td>"
	    										   +"<td style='text-align: center;'>"+item.departmentName+"<input id='departmentName' name='departmentName' type='hidden' value='"+item.departmentName+"'><input id='departmentId' name='departmentId' type='hidden' value='"+item.departmentId+"'></td>"
	    										   +"<td style='text-align: center;'>"+item.mobile+"</td>"
	    										   +"<td style='text-align: center;'><input id='changeValue' name='changeValue' value='0' style='text-align: center;width:60px;' onblur='checknum(this.value)' class='"+item.departmentId+"'></td>"
	    										   +"<td style='text-align: center;'><input id='pushMoney' name='pushMoney' value='0' disabled='disabled' style='text-align: center;width:60px;' class='"+item.departmentId+"'><input id='pushMoneySum' name='pushMoneySum' value='0' type='hidden'></td>"
	    										   +"<td style='text-align: center;'><a href='#' onclick='delSysUserInfo(this)' class='btn btn-danger btn-xs' ><i class='fa fa-edit'></i>删除</a></td></tr>"
	    						);                                                                           
	    					}); 
	    					top.layer.close(index);
	    				},
	    				error:function(XMLHttpRequest,textStatus,errorThrown){
	    					top.layer.alert('请确定姓名的正确性以及该用户是否属于妃子校', {icon: 0, title:'提醒'});
	    				}
	    			});
	 			}
	 		}); 
	 	}
		
		function delSysUserInfo(obj){
			$(obj).parent().parent().remove();
		}
		
		//判断输入的营业额
		function checknum(obj){
			var re = /^([+-]?)\d*\.?\d{0,2}$/; 
			if(obj == ''){
				$("#changeValue").val(0);
			}
			if(!re.test(obj)){
				top.layer.alert('请输入正确的金额(最多两位小数)', {icon: 0, title:'提醒'});
				return;
			}
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
	                	<div class="pull-right">
							<a href="#" onclick="getSysUserInfo()" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加业务员</a>
						</div>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<tr>
								<th style="text-align: center;">时间</th>
								<th style="text-align: center;">类型</th>
								<th style="text-align: center;">金额</th>
								<th style="text-align: center;">详情</th>
							</tr>
							<tr>
								<td align="center">
									<fmt:formatDate value="${turnOverDetails.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />
								</td>
								<td align="center">
									<c:if test="${turnOverDetails.type == 1}">下单</c:if>
									<c:if test="${turnOverDetails.type == 2}">还款</c:if>
								</td>
								<td align="center">
									${turnOverDetails.amount}
									<input id="amount" name="amount" value="${turnOverDetails.amount}" type="hidden">
								</td>
								<td align="center">
									<form id="mosaic">
										<input id="orderId" name="orderId" value="${turnOverDetails.orderId}" type="hidden">
										<input id="turnOverDetailsId" name="turnOverDetailsId" value="${turnOverDetails.turnOverDetailsId}" type="hidden">
										<input id="type" name="type" value="${turnOverDetails.type}" type="hidden">
										<table id="mytable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
											<tr>
												<th style="text-align: center;">业务员</th>
												<th style="text-align: center;">部门</th>
												<th style="text-align: center;">手机号</th>
												<th style="text-align: center;">营业额</th>
												<th style="text-align: center;">增减值</th>
												<th style="text-align: center;">操作</th>
											</tr>
											<c:forEach items="${turnOverDetails.pushMoneyList}" var="orderPushmoneyRecord">
												<tr>
													<td align="center">
														${orderPushmoneyRecord.pushmoneyUserName}
														<input id="pushmoneyUserId" name="pushmoneyUserId" value="${orderPushmoneyRecord.pushmoneyUserId}" type="hidden">
													</td>
													<td align="center">
														${orderPushmoneyRecord.departmentName}
														<input id="departmentName" name="departmentName" value="${orderPushmoneyRecord.departmentName}" type="hidden">
														<input id="departmentId" name="departmentId" value="${orderPushmoneyRecord.departmentId}" type="hidden">
													</td>
													<td align="center">${orderPushmoneyRecord.pushmoneyUserMobile}</td>
													<td align="center">
														${orderPushmoneyRecord.pushMoney}
														<input id="pushMoney" name="pushMoney" value="${orderPushmoneyRecord.pushMoney}" type="hidden" class="${orderPushmoneyRecord.departmentId}">
													</td>
													<td align="center">
														<input id="changeValue" name="changeValue" value="0" style="text-align: center;width:60px;" class="${orderPushmoneyRecord.departmentId}" onblur="checknum(this.value)">
														<input id="pushMoneySum" name="pushMoneySum" value="${orderPushmoneyRecord.pushMoneySum}" type="hidden">
													</td>
													<td align="center">
														<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-edit"></i>删除</a>
													</td>
												</tr>
											</c:forEach>
										</table>
									</form>
								</td>
							</tr>
						</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	<div class="loading" id="loading"></div>
</body>
</html>