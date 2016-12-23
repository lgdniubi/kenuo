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
    		//$("#userid,#name,#mobile,#recid,#servicemin").val() != "" && $("#userid,#name,#mobile,#recid,#servicemin").val() != null
    		if($("#name").val() != "" && $("#name").val() != null && $("#userid").val() != "" && $("#userid").val() != null && $("#mobile").val() != "" && $("#mobile").val() != null && $("#recid").val() != "" && $("#recid").val() != null && $("#servicemin").val() != "" && $("#servicemin").val() != null){
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
    	validateForm = $("#inputForm").validate({
			rules: {
				reservationStatus: "required",
				officeIdName: "required",
				beauticianId: "required"
			},
			messages: {
				reservationStatus: "请输入正确预约状态！",
				beauticianId: "美容师不能为空！"
			},
		});
	}); 
    ////异步加载美容师
    function findBeauty(){
   		$("#beauticianId option[value !='']").remove();  //移除不为空的下拉框
   		$("#date option[value !='']").remove();
   		$("#times option[value !='']").remove();
   		if($("#officeIdbeautId").val() != ""){
   			$(".loading").show();
   	       	$.ajax({
   	       		type : 'post',
   	       		url : '${ctx}/ec/mtmyMnappointment/loadBeauty',
   	       		data:{'office.id':$('#officeIdbeautId').val()},
   	       		dateType: 'text',
   	       		success:function(data){
   	   				$.each(data.user,function(index,item){
   	   					$("#beauticianId").append("<option value="+item.id+">"+item.name+"</option>");
   	       			}); 
   	   				$(".loading").hide();
   	       		}
   	       	})
   		}
    }
    
    function ReservationTime(){
       	$("#date option[value !='']").remove();
       	$("#times option[value !='']").remove();
    	if($("#beauticianId").val() != ""){
    		$(".loading").show();
        	$.ajax({
        		type : 'post',
        		url : '${ctx}/ec/mtmyMnappointment/ReservationTime',
        		data:{'beauticianId':$("#beauticianId").val()},
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
    	}
    }
    function ReservationDate(){
    	$("#times option[value !='']").remove();
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
    function findOrderGoods(){
    	if($("#orderid").val() != "" && $("#oldorderid").val() != $("#orderid").val()){
    		$("#serve option[value !='']").remove();
        	$("#mytable").empty("");
        	$("#userid,#name,#mobile,#recid,#servicemin").val("");
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
    function serveChange(num){
    	$("#mytable").empty("");
    	$("#userid,#name,#mobile,#recid,#servicemin").val("");
    	if(num != ""){
    	$(".loading").show();
    	if(serveList[num].users == null){
    		$(".loading").hide(); 
    		top.layer.alert('此订单不存在用户,请仔细核对订单', {icon: 0, title:'提醒'});
    	}else{
		    	$("#mytable").append("<tr><td class='active'><font color='red'>*</font>订单号</td><td colspan='4'>"+serveList[num].orderid+"</td></tr>"
					    			+ "<tr><td class='active'><font color='red'>*</font>商品名称</td><td>"+serveList[num].goodsname+"</td><td class='active'><font color='red'>*</font>服务时长</td><td>"+serveList[num].servicemin+"分钟</td></tr>"
					    			+ "<tr><td class='active'><font color='red'>*</font>总服务次数</td><td>"+serveList[num].servicetimes+"次</td><td class='active'><font color='red'>*</font>剩余次数</td><td>"+serveList[num].remaintimes+"次</td></tr>"
					    			+ "<tr><td class='active'><font color='red'>*</font>真实姓名</td><td>"+serveList[num].users.name+"</td><td class='active'><font color='red'>*</font>手机号</td><td>"+serveList[num].users.mobile+"</td></tr>"
			    				);
		    	if(serveList[num].users.name == "" || serveList[num].users.name == null){
		    		top.layer.alert('此用户真实姓名为空,请用户先完善个人信息', {icon: 0, title:'提醒'});
		    	}
		    	$("#userid").val(serveList[num].users.userid);
		    	$("#name").val(serveList[num].users.name);
		    	$("#mobile").val(serveList[num].users.mobile);
		    	$("#recid").val(serveList[num].recid);
		    	$("#servicemin").val(serveList[num].servicemin);
		    	$(".loading").hide();  
	    	}
    	}
    }
    </script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
	    	<div class="ibox-content">
				<form:form id="inputForm" modelAttribute="reservation" action="${ctx }/ec/mtmyMnappointment/saveReservation">
					<input id="userid" name="userid" type="hidden">
					<input id="name" name="name" type="hidden">
					<input id="mobile" name="mobile" type="hidden">
					<input id="recid" name="recid" type="hidden">
					<input id="servicemin" name="servicemin" type="hidden">
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>订单号：</label></td>
							<td>
								<input class="form-control" id="orderid" onblur="findOrderGoods()">
								<input type="hidden" id="oldorderid">
								<br><span class="help-inline">注：订单号请在订单列表中查询</span>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>已购买服务：</label></td>
							<td>
								<select id="serve" name="serve" class="form-control" onchange="serveChange(this.options[this.options.selectedIndex].value)">
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
							<td class="active"><label class="pull-right"><font color="red">*</font>店铺：</label></td>
							<td>
								<sys:treeselect id="officeIdbeaut" name="officeIdbeaut" value="" labelName="office.name" labelValue="" 
								     title="店铺" url="/sys/office/treeData" cssClass="area1 form-control required" allowClear="true" notAllowSelectParent="true"/>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>美容师：</label></td>
							<td>
								<select id="beauticianId" name="beauticianId" class="form-control" onchange="ReservationTime()">
									<option value="">请选择美容师</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>预约日期：</label></td>
							<td>
								<select id="date" onchange="ReservationDate()" class="form-control">
									<option value="">请选择预约日期</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>预约时间：</label></td>
							<td>
								<select id="times" name="times" class="form-control">
									<option value="">请选择预约时间</option>
								</select>
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