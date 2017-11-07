<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>添加多项目预约</title>
	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
    <script type="text/javascript">
    var validateForm;
    var map = {};
    var serveList;
    function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
    	
    	if(validateForm.form()){
    		if($("#userid").val() != "" && $("#userid").val() != null){
    			/* loading("正在提交，请稍候..."); */
    		 	$.ajax({
       	       		type : 'post',
       	       		url : '${ctx}/ec/mtmyMnappointment/saveMoreReservation',
       	       		data:$("#inputForm").serialize(),
       	       		dateType: 'text',
       	       		success:function(datas){
       	       			var data = eval("("+datas+")");
	       	       		if(data.code == "200"){
	       	       			top.layer.alert('添加多项目预约成功！', {icon: 0, title:'提醒'});
	       	       		 
	       	       			var $contentMain = $('#content-main', window.parent.document);
					    	var $navIndex = $('.page-tabs-content', window.parent.document).find('.active').index();
					       	$contentMain.find('.J_iframe').eq($navIndex).attr('src', $contentMain.find('.J_iframe').eq($navIndex).attr('src'));
	       	       		    
	       	       		    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	       	            	parent.layer.close(index);
	       				}else{
	       					top.layer.alert('添加多项目预约失败:'+data.msg, {icon: 0, title:'提醒'});
	       					return false;
	       				}
       	       		}
       	       	}) 
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
    
    //根据输入的手机号查询用户
    function searchUser(){
		if($("#searchMobile").val() == ""){
			top.layer.alert('请先输入手机号！', {icon: 0, title:'提醒'});
			return;
		}else{
			if($("#searchMobile").val() == $("#oldMobile").val()){
				return;
			}else{
				$("#name").val("");
				$("#mobile").val("");
				$("#userid").val("");
				//清除input值
	        	clearInput("areaId,areaName");
	        	//清除下拉框的值
	        	clearSelectByName("shopId,beauticianId,date,times");
	        	$("#addZTD").html("");
	        	
				var mask = layer.load(0, {shade: false}); //0代表加载的风格，支持0-2
				$.ajax({
					type:"get",
					dataType:"json",
					data:{
						mobile:$("#searchMobile").val()
					},
					url:"${ctx}/ec/orders/getUser",
					success:function(date){
						layer.close(mask);
						var user = date.users;
						if(user){
							top.layer.alert('查询用户信息成功!', {icon: 0, title:'提醒'}); 
							$("#name").val(date.users.nickname);
							$("#userid").val(date.users.userid);
							$("#mobile").val(date.users.mobile);
							$("#oldMobile").val(date.users.mobile);
						}else{
							top.layer.alert('查询用户信息失败!', {icon: 0, title:'提醒'}); 
						}
					},
					error:function(XMLHttpRequest,textStatus,errorThrown){
						layer.close(mask);
						top.layer.alert('查询用户信息失败!', {icon: 0, title:'提醒'}); 
					}
				});
			}
		}
    }
    
    //查询该用户已购买的服务
    function chooseAlreadyBuyProject(){               
    	var chooseRecId = document.getElementsByName("chooseRecId");
    	var chooseParentRecId = document.getElementsByName("chooseParentRecId");
    	var chooseRecIds = "";
    	var chooseParentRecIds = "";
    	if(chooseRecId.length > 0){
    		for(i=0;i<chooseRecId.length;i++){
    			chooseRecIds = chooseRecIds + chooseRecId[i].value + ",";
    			chooseParentRecIds = chooseParentRecIds + chooseParentRecId[i].value + ",";
    		}
    	}
    	var userid = $("#userid").val();
    	if(userid == ""){
    		top.layer.alert('请先通过手机号查询用户信息!', {icon: 0, title:'提醒'}); 
    		return;
    	}
    	
    	top.layer.open({
		    type: 2, 
		    area: ['650px', '500px'],
		    title:"选择项目",
		    content: "${ctx}/ec/mtmyMnappointment/alreadyBuyProject?userid="+userid+"&chooseRecIds="+chooseRecIds+"&chooseParentRecIds="+chooseParentRecIds,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		    	$(".loading").show();
		        var obj =  layero.find("iframe")[0].contentWindow;
		        var str = obj.document.getElementsByName("box");
		        var recIdsAndSkillId = "";
		        var sum = 0;
		        for (i=0;i<str.length;i++){
		       		if(str[i].checked == true){
		       			sum = sum + 1;	 
		        	}
		        }
		        if(sum == 0){
		        	top.layer.alert('至少选择一个商品!', {icon: 0, title:'提醒'}); 
		        	return;
		        }else{
		        	//清除input值
		        	clearInput("areaId,areaName");
		        	$("#addZTD").html("");
			        for (i=0;i<str.length;i++){
					    if(str[i].checked == true){
					    	var chooseId = str[i].id;
					    	var chooseValue = str[i].value;
					    	var goodsName = obj.document.getElementById("goodsName_"+chooseId).value;
					    	var serviceMin = obj.document.getElementById("serviceMin_"+chooseId).value;
					    	var serviceTimes = obj.document.getElementById("serviceTimes_"+chooseId).value;
					    	var goodsId = obj.document.getElementById("goodsId_"+chooseId).value;
					    	var skillId = obj.document.getElementById("skillId_"+chooseId).value;
					    	var labelId = obj.document.getElementById("labelId_"+chooseId).value;
					    	var positionId = obj.document.getElementById("positionId_"+chooseId).value;
					    	var positionIds = obj.document.getElementById("positionIds_"+chooseId).value;
					    	var isReal = obj.document.getElementById("isreal_"+chooseId).value;
					    	var franchiseeId = obj.document.getElementById("franchiseeId_"+chooseId).value;
					    	recIdsAndSkillId = chooseId + "_" + skillId;
					    	$("<tr>"+
						    		"<td>"+goodsName+"<input id='chooseRecId' name='chooseRecId' type='hidden' value='"+chooseId+"'><input id='positionId_"+chooseId+"' name='positionId' type='hidden' value='"+positionId+"'><input id='positionIds_"+chooseId+"' name='positionIds' type='hidden' value='"+positionIds+"'><input id='goodsName_"+chooseId+"' name='goodsName' type='hidden' value='"+goodsName+"'></td>"+
				    			    "<td>"+serviceMin+"分钟<input id='serviceMin_"+chooseId+"' name='serviceMin' type='hidden' value='"+serviceMin+"'><input id='labelId_"+chooseId+"' name='labelId' type='hidden' value='"+labelId+"'><input id='goodsId_"+chooseId+"' name='goodsId' type='hidden' value='"+goodsId+"'><input id='franchiseeId' name='franchiseeId' type='hidden' value='"+franchiseeId+"'></td>"+
				    			    "<td>"+serviceTimes+"<input id='chooseParentRecId_"+chooseId+"' name='chooseParentRecId' type='hidden' value='"+chooseValue+"'><input id='recIdsAndSkillId_"+chooseId+"' name='recIdsAndSkillId' type='hidden' value='"+recIdsAndSkillId+"'><input id='isReal_"+chooseId+"' name='isReal' type='hidden' value='"+isReal+"'></td>"+
				    			    "<td>"+
				    			    	"<select id='beauticianId_"+chooseId+"' name='beauticianId' class='form-control required' onchange='ReservationTime("+chooseId+")'><option value=''>请选择美容师</option></select>"+
				    			    "</td>"+
				    			    "<td>"+
				    			    	"<select id='date_"+chooseId+"' name='date' class='form-control required' onchange='ReservationDate("+chooseId+")'><option value=''>请选择预约日期</option></select>"+
				    			    "</td>"+
				    			    "<td>"+
				    			    	"<select id='times_"+chooseId+"' name='times' class='form-control required' onchange='queryCanReservation("+chooseId+","+positionId+","+serviceMin+")'><option value=''>请选择预约时间</option></select>"+
				    			    "</td>"+
				    			    "<td>"+
				    			  		"<a href='#' class='btn btn-danger btn-xs' onclick='delFile(this,"+chooseId+")'><i class='fa fa-trash'></i> 删除</a> "+
				    			    "</td>"+
				    			"</tr>").appendTo($("#addZTD"));
					    }
					}
			        findOffice();	// 若选中商品后则自动加载店铺
		        }
		        top.layer.close(index);
		        $(".loading").hide();
		},
		cancel: function(index){ //或者使用btn2
			    	           //按钮【按钮二】的回调
			$(".loading").hide();
		}
	}); 
    	
    }
    
    function delFile(obj,recId){
    	/* //清除input值
    	clearInput("areaId,areaName");
    	//清除下拉框的值
    	clearSelectByName("shopId,beauticianId,date,times"); */
    	delete(map[recId]);
    	$(obj).parent().parent().remove();
    	
    }

    //异步加载店铺
    function findOffice(){
    	//清除下拉框的值
    	clearSelectByName("shopId,beauticianId,date,times");
    	var goodsId = document.getElementsByName("goodsId");
    	if(goodsId.length > 0){
   			var goodsIds = "";
   			for(i=0;i<goodsId.length;i++){
   				goodsIds = goodsIds + goodsId[i].value + ",";
    		}
   			if(goodsIds.substr(goodsIds.length-1)== ','){
	        	newGoodsIds = goodsIds.substr(0,goodsIds.length-1);
			}
   			$(".loading").show();
   	       	$.ajax({
   	       		type : 'post',
   	       		url : '${ctx}/ec/mtmyMnappointment/loadOffice',
   	       		data:{'areaId':$('#areaId').val(),'goodsIds':newGoodsIds,'franchiseeId':$("#franchiseeId").val()},
   	       		dateType: 'text',
   	       		success:function(data){
   	   				$.each(data.office,function(index,item){
   	   					if(data.loginOfficeId == item.officeId){
   	   						$("#shopId").append("<option value="+item.officeId+" selected=\"selected\" >"+item.officeName+"</option>");
   	   					}else{
	   	   					$("#shopId").append("<option value="+item.officeId+">"+item.officeName+"</option>");
   	   					}
   	       			}); 
   	   				$(".loading").hide();
   	   				if($("#shopId").val() != ""){	// 若选中店铺则自动加载美容师
   	   					changeShop();
   	   				}else{
   	   					$("#shopId").focus();	// 若无选中店铺 则自动定位到 选中店铺 下拉框
   	   				}
   	       		}
   	       	})
   		}else{
   			top.layer.alert('未选择商品,请核对信息', {icon: 0, title:'提醒'});
   		}
    }
    //加载美容师
    function changeShop(){
    	//清除下拉框的值
    	clearSelectByName("beauticianId,date,times");
    	if($("#shopId").val() != ""){
    		var recIdsAndSkillId = document.getElementsByName("recIdsAndSkillId");
    		for(var j=0;j<recIdsAndSkillId.length;j++){
    			var result = recIdsAndSkillId[j].value.split("_");
	    		$(".loading").show();
	    		$.ajax({
	   	       		type : 'post',
	   	       		url : '${ctx}/ec/mtmyMnappointment/loadMoreBeaut',
	   	       		data:{'shopId':$('#shopId').val(),'skillId':result[1],'recId':result[0]},
	   	       		dateType: 'text',
	   	       		success:function(str){
	   	       			var data = $.parseJSON(str);
		   	       		if("200" != data.code){
							$(".loading").hide();
							top.layer.alert('数据加载失败！', {icon: 0, title:'提醒'});
						}else{
							$.each(data.data.beautys,function(index,item){
								$("#beauticianId_"+data.recId).append("<option value="+item.beauty_id+">"+item.name+"</option>");
								
								/* if(item.beauty_id == data.loginUserId){
									$("#beauticianId_"+data.recId).append("<option value="+item.beauty_id+" selected=\"selected\">"+item.name+"</option>");
								}else{
									$("#beauticianId_"+data.recId).append("<option value="+item.beauty_id+">"+item.name+"</option>");
								} */
			    			}); 
						}
	   	   				$(".loading").hide();
	   	   				if($("#beauticianId_"+data.recId).val() != ""){	// 若选中技师后 则自动加载美容师时间
	   	   					ReservationTime();
	   	   					$("#date_"+data.recId).focus();	// 定位到 选择美容师时间 下拉框
	   	   				}else{
	   	   					$("#beauticianId_"+data.recId).focus();	// 定位到 选择美容师 下拉框
	   	   				}
	   	       		}
	   	       	})
    		}
    	}else{
    		top.layer.alert('未选择店铺,请核对信息', {icon: 0, title:'提醒'});
    	}
    }
    //加载美容师时间
    function ReservationTime(recId){
    	//清除下拉框的值
    	clearSelect("date_"+recId+",times_"+recId);
    	if($("#beauticianId_"+recId).val() != ""){
    		$(".loading").show();
        	$.ajax({
        		type : 'post',
        		url : '${ctx}/ec/mtmyMnappointment/ReservationTime',
        		data:{'beauticianId':$("#beauticianId_"+recId).val(),'serviceMin':$("#serviceMin_"+recId).val(),'shopId':$("#shopId").val(),'labelId':$("#labelId_"+recId).val()},
        		success:function(str){
        			var data = $.parseJSON(str);
    				if("200" != data.code){
    					$(".loading").hide();
    					top.layer.alert('数据加载失败！', {icon: 0, title:'提醒'});
    				}else{
    					map[recId] = data.data.odts;
    					$.each(map[recId],function(index,item){
    						$("#date_"+recId).append("<option value="+index+">"+item.key+"</option>");
    	    			}); 
    				}
    				$(".loading").hide();
        		}
        	})
    	}else{
    		top.layer.alert('未选择美容师,请核对信息', {icon: 0, title:'提醒'});
    	}
    }
    function ReservationDate(recId){
    	//清除下拉框的值
    	clearSelect("times_"+recId);
    	var num = $("#date_"+recId).val();
    	if(num != ""){
    		$(".loading").show();
	    	$.each((map[recId])[num].times, function(index,item){
				if(item.is_use == 0) {
					$('#times_'+recId).append('<option value="'+(map[recId])[num].key+item.name+'">'+item.name+'</option>');
				}else{
					$('#times_'+recId).append('<option disabled="disabled">'+item.name+'(已预约)</option>');
				}
			});
	    	$(".loading").hide();
    	}
    }
    
    function queryCanReservation(queryRecId,queryPositionId,queryServiceMin){
    	var queryDate = document.getElementById("date_"+queryRecId);
    	var queryTime = document.getElementById("times_"+queryRecId);
    	var queryBeauticianId = document.getElementById("beauticianId_"+queryRecId).value;
    	var queryParentType = document.getElementById("positionIds_"+queryRecId).value.split("_")[0];
    	
    	var recIds = document.getElementsByName("chooseRecId");
    	for(i=0;i<recIds.length;i++){
    		if(recIds[i].value != queryRecId){
    			var choosedTimes = document.getElementById("times_"+recIds[i].value);
    			if(choosedTimes.value != ""){
    				var choosedTimesTest = choosedTimes.options[choosedTimes.selectedIndex].text; 
    				var positionId = document.getElementById("positionId_"+recIds[i].value).value;
    				var serviceMin = document.getElementById("serviceMin_"+recIds[i].value).value;
    				var beauticianId = document.getElementById("beauticianId_"+recIds[i].value).value;
    				var date = document.getElementById("date_"+recIds[i].value);
    				var dateText = date.options[date.selectedIndex].text; 
    				var parentType = document.getElementById("positionIds_"+recIds[i].value).value.split("_")[0];
    				
    				var queryDateText = queryDate.options[queryDate.selectedIndex].text; 
    				var queryTimeText = queryTime.options[queryTime.selectedIndex].text; 
					if(caculateTime(queryDateText,queryTimeText,queryServiceMin,dateText,choosedTimesTest,serviceMin)){
						if(queryParentType != parentType){
							$('#times_'+queryRecId).val("");
							top.layer.alert('该时间段与其他商品预约时间冲突', {icon: 0, title:'提醒'});
						}else{
							if(queryPositionId == positionId){
								$('#times_'+queryRecId).val("");
								top.layer.alert('该时间段与其他商品预约时间冲突', {icon: 0, title:'提醒'});
							}else{
								if(queryBeauticianId == beauticianId){
									$('#times_'+queryRecId).val("");
									top.layer.alert('该时间段与其他商品预约时间冲突', {icon: 0, title:'提醒'});
								}
							}
							
						}
					}  				
    			}
    		}
    	}
    }
	
   function caculateTime(queryDate,queryTime,queryServiceMin,date,choosedTimes,serviceMin){
	   if(queryDate == date){
            var sum1 = Number(queryTime.split(':')[0]*3600) + Number(queryTime.split(':')[1]*60);
            var sum2 = Number(queryTime.split(':')[0]*3600) + Number(queryTime.split(':')[1]*60) + Number(queryServiceMin*60);
            
            var sum3 = Number(choosedTimes.split(':')[0]*3600) + Number(choosedTimes.split(':')[1]*60);
            var sum4 = Number(choosedTimes.split(':')[0]*3600) + Number(choosedTimes.split(':')[1]*60) + Number(serviceMin*60);
            if((sum1 >= sum4) || (sum2 <= sum3)){
            	return false;
            }else{
            	return true;
            }
    	}else{
    		return false;
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
    
    function clearSelectByName(str){
    	var a = str.split(","); 
    	$.each(a, function(key, val){
		    $("select[name='"+val+"'] option[value !='']").remove(); 
    	});
    }
    
    function checkEnter(e){
	    var code;  
	    if (!e) var  e = window.event;  
	    if (e.keyCode) code = e.keyCode;  
	    else if (e.which) code = e.which;  
	    if(code==13 && window.event){  
	        e.returnValue = false;  
	        top.layer.alert('禁止换行!', {icon: 0, title:'提醒'}); 
	    }else if(code==13){  
	        e.preventDefault();
	        top.layer.alert('禁止换行!', {icon: 0, title:'提醒'}); 
	    }  
    }
    </script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
	    	<div class="ibox-content">
				<form:form id="inputForm" modelAttribute="reservation" action="${ctx}/ec/mtmyMnappointment/saveMoreReservation">
					<!-- 用于提交时 验证数据是否有误 -->  
					<input id="oldMobile" name="oldMobile" value="" type="hidden">
					
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<tr>
							<td class="active" width="110px;"><label class="pull-right"><font color="red">*</font>手机号：</label></td>
							<td>
								<input class="form-control required" id="searchMobile" name="searchMobile" width="100px">
							</td>
							<td colspan="2">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="searchUser()" type="button"><i class="fa fa-search"></i> 查询</button>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>用户姓名：</label></td>
							<td>
								<input id="name" name="name" type="text" class="form-control required">
								<input id="userid" name="userid" type="hidden">
							</td>
							<td class="active"><label class="pull-right"><font color="red">*</font>用户手机号：</label></td>
							<td>
								<input id="mobile" name="mobile" type="text" class="form-control required">
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>已购买服务：</label></td>
							<td colspan="3">
								<button class="btn btn-white btn-sm" title="选择项目" onclick="chooseAlreadyBuyProject()" type="button">
									<i class="fa fa-plus"></i>选择项目
								</button>
							</td>
						</tr>
						<tr>
							<td colspan="4">
								<table id="mytable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
									<thead>
										<tr>
											<td colspan="7" ><font color="red">*</font>服务详情：</td>
										</tr>
										<tr>
											<th style="text-align: center;">商品名称</th>
											<th style="text-align: center;">服务时长</th>
											<th style="text-align: center;">总服务次数</th>
											<th style="text-align: center;">美容师</th>
											<th style="text-align: center;">预约日期</th>
											<th style="text-align: center;">预约时间</th>
											<th style="text-align: center;">操作</th>
										</tr>
									</thead>
									<tbody id="addZTD" style="text-align:center;">	
									</tbody>
								</table>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red"></font>区域：</label></td>
							<td colspan="3" width="200px">
								<sys:treeselect id="area" name="area.id" value="" labelName="area.name" labelValue="" 
							         title="区域" url="/sys/area/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>店铺：</label></td>
							<td colspan="3">
								<a href="#" onclick="findOffice()" class="btn btn-primary btn-xs">获取店铺信息</a><p></p>
								<select id="shopId" name="shopId" class="form-control required" onchange="changeShop()">
									<option value="">请选择店铺</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right">消费者备注：</label></td>
							<td colspan="3">
								<textarea rows="7" cols="30" id="userNote" name="userNote" onkeydown="checkEnter(event)" class="form-control"></textarea>
							</td>
						</tr>
					</table>
				</form:form>
				<form:form id="newInputForm" modelAttribute="reservation" action="${ctx}/ec/mtmyMnappointment/mnappointment">
				</form:form>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>