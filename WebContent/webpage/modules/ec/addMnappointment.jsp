<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>预约详情</title>
	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
    <script type="text/javascript">
    var validateForm;
    var dates;
    var serveList;
    function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
    	if(validateForm.form()){
    		if($("#userid").val() != "" && $("#userid").val() != null && $("#recid").val() != "" && $("#recid").val() != null && $("#servicemin").val() != "" && $("#servicemin").val() != null){
    			loading("正在提交，请稍候...");
        		$("#inputForm").submit();
    	    	return true; 
    		}else{
    			top.layer.alert('此页面的数据均为必填项！请仔细核对数据！', {icon: 0, title:'提醒'});
	    		return false;
    		}
    	}
    	return false;
    } 
    $(document).ready(function() {
    	validateForm = $("#inputForm").validate();
	}); 
    //  加载订单详情
    function findOrderGoods(){
    	if($("#oldorderid").val() != $("#orderid").val()){
    		$("#warning").hide();
    		$("#mytable").empty("");
        	//清除input值
        	clearInput("areaId,areaName,userid,name,mobile,recid,servicemin,goodsId,skillId,labelId,oldorderid");
        	//清除下拉框的值
        	clearSelect("serve,shopId,beauticianId,date,times");
    	}
    	if($("#orderid").val() != "" && $("#oldorderid").val() != $("#orderid").val()){
    		$(".loading").show();
    		$("#oldorderid").val($("#orderid").val());
	    	$.ajax({
	    		type : 'post',
	    		url : '${ctx}/ec/mtmyMnappointment/findOrderGoods',
	    		data:{'orderid':$("#orderid").val()},
	    		success:function(data){
	    			if(data.orderGoods.length > 0){
	    				serveList = data.orderGoods;
	    				$.each(data.orderGoods,function(index,item){
	    					$("#serve").append("<option value="+index+">"+item.goodsname+"</option>");
	           			});
	    				$(".loading").hide();
	    			}else{
	    				$(".loading").hide();
	    				top.layer.alert('1、此订单号输入有误;<br>2、请确认此订单已支付;<br>3、请确认此订单下是否有虚拟服务;<br>4、请确认此订单号下是否有未完成的服务！', {icon: 0, title:'提醒'});
	    			}
	       		}
	    	})
    	}
    }
    // 选择订单下虚拟订单
    function serveChange(num){
    	$("#mytable").empty("");
    	//清除input值
    	clearInput("areaId,areaName,userid,name,mobile,recid,servicemin,goodsId,skillId,labelId");
    	//清除下拉框的值
    	clearSelect("shopId,beauticianId,date,times");
    	if(num != ""){
    	$(".loading").show();
    	if(serveList[num].users == null){
    		$(".loading").hide(); 
    		top.layer.alert('此订单不存在用户,请仔细核对订单', {icon: 0, title:'提醒'});
    	}else{
		    	$("#mytable").append("<tr><td class='active'><font color='red'>*</font>订单号</td><td colspan='4'>"+serveList[num].orderid+"</td></tr>"
					    			+ "<tr><td class='active'><font color='red'>*</font>商品名称</td><td>"+serveList[num].goodsname+"</td><td class='active'><font color='red'>*</font>服务时长</td><td>"+serveList[num].servicemin+"分钟</td></tr>"
					    			+ "<tr><td class='active'><font color='red'>*</font>总服务次数</td><td>"+serveList[num].servicetimes+"次</td><td class='active'><font color='red'>*</font>剩余次数</td><td>"+serveList[num].remaintimes+"次</td></tr>"
					    			+ "<tr><td class='active'><font color='red'></font>真实姓名</td><td>"+serveList[num].users.name+"</td><td class='active'><font color='red'></font>手机号</td><td>"+serveList[num].users.mobile+"</td></tr>"
			    				);
		    	
		    	$("#userid").val(serveList[num].users.userid);
		    	$("#name").val(serveList[num].users.name);
		    	$("#mobile").val(serveList[num].users.mobile);
		    	$("#recid").val(serveList[num].recid);
		    	$("#servicemin").val(serveList[num].servicemin);
		    	$("#goodsId").val(serveList[num].goodsid);
		    	$("#skillId").val(serveList[num].skillId);
		    	$("#labelId").val(serveList[num].labelId);
		    	$(".loading").hide();  
	    	}
    	}
    }
    //异步加载店铺
    function findOffice(){
    	//清除下拉框的值
    	clearSelect("shopId,beauticianId,date,times");
   		if($("#goodsId").val() != ""){
   			$(".loading").show();
   	       	$.ajax({
   	       		type : 'post',
   	       		url : '${ctx}/ec/mtmyMnappointment/loadOffice',
   	       		data:{'areaId':$('#areaId').val(),'goodsId':$("#goodsId").val()},
   	       		dateType: 'text',
   	       		success:function(data){
   	   				$.each(data.office,function(index,item){
   	   					$("#shopId").append("<option value="+item.officeId+">"+item.officeName+"</option>");
   	       			}); 
   	   				$(".loading").hide();
   	       		}
   	       	})
   		}else{
   			top.layer.alert('未选择商品,请核对信息', {icon: 0, title:'提醒'});
   		}
    }
    //加载美容师
    function changeShop(){
    	//清除下拉框的值
    	clearSelect("beauticianId,date,times");
    	if($("#shopId").val() != ""){
    		$(".loading").show();
    		$.ajax({
   	       		type : 'post',
   	       		url : '${ctx}/ec/mtmyMnappointment/loadBeaut',
   	       		data:{'shopId':$('#shopId').val(),'skillId':$("#skillId").val()},
   	       		dateType: 'text',
   	       		success:function(str){
   	       			var data = $.parseJSON(str);
	   	       		if("200" != data.code){
						$(".loading").hide();
						top.layer.alert('数据加载失败！', {icon: 0, title:'提醒'});
					}else{
						$.each(data.data.beautys,function(index,item){
							$("#beauticianId").append("<option value="+item.beauty_id+">"+item.name+"</option>");
		    			}); 
					}
   	   				$(".loading").hide();
   	       		}
   	       	})
    	}else{
    		top.layer.alert('未选择店铺,请核对信息', {icon: 0, title:'提醒'});
    	}
    }
    //加载美容师时间
    function ReservationTime(){
    	//清除下拉框的值
    	clearSelect("date,times");
    	if($("#beauticianId").val() != ""){
    		$(".loading").show();
        	$.ajax({
        		type : 'post',
        		url : '${ctx}/ec/mtmyMnappointment/ReservationTime',
        		data:{'beauticianId':$("#beauticianId").val(),'serviceMin':$("#servicemin").val(),'shopId':$("#shopId").val(),'labelId':$("#labelId").val()},
        		success:function(str){
        			var data = $.parseJSON(str);
    				if("200" != data.code){
    					$(".loading").hide();
    					top.layer.alert('数据加载失败！', {icon: 0, title:'提醒'});
    				}else{
    					dates = data.data.odts;
    					$.each(dates,function(index,item){
    						$("#date").append("<option value="+index+">"+item.key+"</option>");
    	    			}); 
    				}
    				$(".loading").hide();
        		}
        	})
    	}else{
    		top.layer.alert('未选择美容师,请核对信息', {icon: 0, title:'提醒'});
    	}
    }
    function ReservationDate(){
    	//清除下拉框的值
    	clearSelect("times");
    	var num = $("#date").val();
    	if(num != ""){
    		$(".loading").show();
	    	$.each(dates[num].times, function(index,item){
				if (item.is_use == 0) {
					$('#times').append('<option value="'+dates[num].key+item.name+'">'+item.name+'</option>');
				}else{
					$('#times').append('<option disabled="disabled">'+item.name+'(已预约)</option>');
				}
			});
	    	$(".loading").hide();
    	}
    }
    function clearInput(str){
    	var a = str.split(","); 
    	$.each(a, function(key, val) {
    	   $("#"+val).val("");
    	});
    }
    function clearSelect(str){
    	var a = str.split(","); 
    	$.each(a, function(key, val) {
    	   $("#"+val+" option[value !='']").remove();
    	});
    }
    </script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
	    	<div class="ibox-content">
				<form:form id="inputForm" modelAttribute="reservation" action="${ctx }/ec/mtmyMnappointment/saveReservation">
					<!-- 用于提交时 验证数据是否有误 -->  
					<input id="userid" name="userid" type="hidden"> 
					<input id="recid" name="recid" type="hidden"><!-- 用于添加预约 -->
					<input id="servicemin" name="servicemin" type="hidden"><!-- 1.验证  2.获取美容师预约时间时 传服务时长 -->
					<input id="goodsId" name="goodsId" type="hidden"><!-- 调用存储过程获取可用店铺数据 -->
					<input id="skillId" name="skillId" type="hidden"><!-- 获取美容师详情 -->
					<input id="labelId" name="labelId" type="hidden"><!-- 获取美容师预约时间 -->
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>订单号：</label></td>
							<td>
								<input class="form-control required" id="orderid" onblur="findOrderGoods()">
								<input type="hidden" id="oldorderid">
								<br><span class="help-inline">注：订单号请在订单列表中查询</span>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>已购买服务：</label></td>
							<td>
								<select id="serve" name="serve" class="form-control required" onchange="serveChange(this.options[this.options.selectedIndex].value)">
									<option value="">请选择已购买服务</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>服务详情：</label></td>
							<td>
								<table id="mytable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer"></table>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>用户姓名：</label></td>
							<td>
								<input id="name" name="name" type="text" class="form-control required">
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>用户手机号：</label></td>
							<td>
								<input id="mobile" name="mobile" type="text" class="form-control required">
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red"></font>区域：</label></td>
							<td>
								<sys:treeselect id="area" name="area.id" value="" labelName="area.name" labelValue="" 
							         title="区域" url="/sys/area/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>店铺：</label></td>
							<td>
								<a href="#" onclick="findOffice()" class="btn btn-primary btn-xs">获取店铺信息</a><p></p>
								<select id="shopId" name="shopId" class="form-control required" onchange="changeShop()">
									<option value="">请选择店铺</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>美容师：</label></td>
							<td>
								<select id="beauticianId" name="beauticianId" class="form-control required" onchange="ReservationTime()">
									<option value="">请选择美容师</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>预约日期：</label></td>
							<td>
								<select id="date" onchange="ReservationDate()" class="form-control required">
									<option value="">请选择预约日期</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>预约时间：</label></td>
							<td>
								<select id="times" name="times" class="form-control required">
									<option value="">请选择预约时间</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right">备注：</label></td>
							<td>
								<textarea rows="7" cols="30" id="remarks" name="remarks" class="form-control"></textarea>
							</td>
						</tr>
					</table>
				</form:form>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>