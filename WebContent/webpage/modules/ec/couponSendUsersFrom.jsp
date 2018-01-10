<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>列推用户管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
<%-- 	<link rel="stylesheet" href="${ctxStatic}/multiple/docs/css/bootstrap-3.3.2.min.css" type="text/css"> --%>
<%-- 	<script type="text/javascript" src="${ctxStatic}/multiple/js/bootstrap-multiselect.js"></script> --%>
	<script type="text/javascript">
	var validateForm;
	
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			var val=jQuery("#sendType").val();
			if(val==2){
				var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
			    var all = new Array(); //定义变量全部保存
			    $("#select2 option").each(function () {
			          var txt = $(this).text(); //获取单个text
			          var val = $(this).val(); //获取单个value
			         // var node = val;
			          arr.push(val);
			          all.push(txt);
			      });
			    $("#userId").val(arr);
			  //  $("#goodsName").val(all);
				
			}
			
			$("#inputForm").submit();
			return true;
		}

		return false;
	}
	
	
	//将分类下商品加载到左侧的下拉框
	function addSelectUser(){
		if(($("#nickname").val() != null && $("#nickname").val() != "" )||($("#userid").val() != null && $("#userid").val() != "" )||($("#districtId").val() != null && $("#districtId").val() != "" )||($("#sendType").val() != null && $("#sendType").val() != "")||($("#mobile").val() != null && $("#mobile").val() != "")){
			$(".loading").show();
			$("#select1").empty();
			var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
		    var all = new Array(); //定义变量全部保存
			$("#select1").empty();	
			 $("#select2 option").each(function (){
				// alert("option");
		          var txt = $(this).text(); //获取单个text
		          var val = $(this).val(); //获取单个value
		         // var node = val;
		          arr.push(val);
		          all.push(txt);
		      });
			
			
			$.ajax({
				 type:"get",
				 data : $("#inputForm").serialize(),     //此处表单序列化
				 url:"${ctx}/ec/mtmyuser/findAllBy",
				 dataType: 'json',
				 success:function(date){
					$(".loading").hide();
						var data=date;
						if(arr.length>0){
							for(var j=0;j<arr.length;j++){
								for(var i=0;i<data.length;i++){
								
									if(arr[j]==data[i].id){
										data.splice(i,1);
										break;
									}
								}
							
							}	
							
							for(var i=0;i<data.length;i++){
								$("#select1").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
							}
							
						}else{
							for(var i=0;i<data.length;i++){
								$("#select1").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
							}	
						}

				 },
				 error:function(XMLHttpRequest,textStatus,errorThrown) {
					$(".loading").hide();
				 }
			});
		}else{
			top.layer.alert('请至少填写一个条件！', {icon: 0, title:'提醒'});
		}
	}	
	$(function(){
	    //移到右边
	    $('#add').click(function() {
	    //获取选中的选项，删除并追加给对方
	        $('#select1 option:selected').appendTo('#select2');
	    });
	    //移到左边
	    $('#remove').click(function() {
	        $('#select2 option:selected').appendTo('#select1');
	    });
	    //全部移到右边
	    $('#add_all').click(function() {
	        //获取全部的选项,删除并追加给对方
	        $('#select1 option').appendTo('#select2');
	    });
	    //全部移到左边
	    $('#remove_all').click(function() {
	        $('#select2 option').appendTo('#select1');
	    });
	    //双击选项
	    $('#select1').dblclick(function(){     //绑定双击事件
	        //获取全部的选项,删除并追加给对方
	        $("option:selected",this).appendTo('#select2'); //追加给对方
	    });
	    //双击选项
	    $('#select2').dblclick(function(){
	       $("option:selected",this).appendTo('#select1');
	    });
	});
	
	function sendshow(o){
		var type=$("#sendType").val();
		if(type==1){
			$("#select1").empty();
			$("#select2").empty();
			$("#sendForm").hide();
			$("#mobile").val("");
			$("#level").val(1);
			$("#districtId").val("");
			$("#districtName").val("");
			$("#textMobile").hide();
			$("#mobileNum").val("");
		}
		if(type==2){
			$("#sendForm").show();
			$("#textMobile").hide();
			$("#mobileNum").val("");
		}
		if(type==3){
			$("#select1").empty();
			$("#select2").empty();
			$("#sendForm").hide();
			$("#mobile").val("");
			$("#level").val(1);
			$("#districtId").val("");
			$("#districtName").val("");
			$("#textMobile").hide();
			$("#mobileNum").val("");
		}
		if(type==4){
			$("#select1").empty();
			$("#select2").empty();
			$("#sendForm").hide();
			$("#mobile").val("");
			$("#level").val(1);
			$("#districtId").val("");
			$("#districtName").val("");
			$("#textMobile").show();
		}
		
	}
	
	function selectType(value){
		$("#mobileNum").val("");
		$("#moreType").val(value);
	}
	
	$(document).ready(function() {
		
		
		validateForm = $("#inputForm").validate({
			rules : {
				

					},
				messages : {
					

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

		
		
		
	});
	
	
	</script>
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
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<div class="clearfix">
					<form:form id="inputForm" modelAttribute="activity" action="${ctx}/ec/activity/saveSendCoupon" method="post" class="navbar-form navbar-left searcharea">
						<form:hidden path="id"/>
						<label>发放类型：</label>
						<select id="sendType" name="sendType" class="form-control" style="width:200px;" onchange="sendshow(this)">
							<option value="1">内部推送</option>
							<option value="3">非内部推送</option>
							<option value="2">指定用户</option>
							<option value="4">多用户推送</option>
						</select>
						<p></p>
						<label>选择红包：</label>
						<sys:treeselect id="couponId" name="couponId" value="" labelName="couponName" labelValue=""  checked="true"
						     	title="选择红包" url="/ec/activity/treeData?id=${activity.id}" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
						<p></p>
						<div id="sendForm" style="display:none;width:700px;padding-top:10px;border:2px solid #DDDDDD">
							<div style="margin-left:10px">
							<label>手机号码：</label>
							<input id="mobile" name="mobile" type="text" style="width:200px;" class="form-control" >
							<label>用户ID：</label>
							<input id="userid" name="userid" type="text" style="width:200px;" class="form-control" >
							<p></p>
							<label>用户等级：</label>
							<select id="level" name="level" class="form-control" style="width:200px;">
								<option value="1">1级以上</option>
								<option value="2">2级以上</option>
								<option value="3">3级以上</option>
								<option value="4">4级以上</option>
								<option value="5">5级以上</option>
								<option value="6">6级以上</option>
								<option value="7">7级以上</option>
								<option value="8">8级以上</option>
								<option value="9">9级以上</option>
								<option value="10">10级以上</option>
							</select>
							<label>用  户  名 :</label>
					        <input id="nickname" name="nickname" type="text" style="width:200px;" class="form-control" >
						    <p></p>
					        <label>选择区域：</label>
							<sys:treeselect id="district" name="district" value="" labelName="areaName" labelValue="" 
						     	title="地区" url="/sys/area/treeData" cssClass="area1 form-control"  allowClear="true" notAllowSelectRoot="true"/>
							<div class="pull-right">
								<a href="#" class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="addSelectUser()" ><i class="fa fa-search"></i> 查询</a>
							</div>
							
							<div id="good" style="padding-top:10px;padding-left: 20px;">
								<div style="float:left">
									<select multiple="multiple" id="select1" style="width:250px;height:350px; float: left;"></select>
								</div>
								<div style="float:left"> 
									<span id="add">
							          <input type="button" class="fabtn" value=">"/>
							          </span><br/>
							          <span id="add_all">
							          <input type="button" class="fabtn" value=">>"/>
							          </span> <br/>
							          <span id="remove">
							          <input type="button" class="fabtn" value="&lt;"/>
							          </span><br/>
							          <span id="remove_all">
							          <input type="button" class="fabtn" value="<<"/>
							          </span>
							    </div>
								<div>
									<select multiple="multiple" id="select2" style="width:250px;height:350px;float:lfet;">
										<c:forEach items="${list}" var="list" >
											<option value="${list.users.id}">${list.users.nickname}</option>
										</c:forEach>
									</select>
								</div>
							</div>

						</div>
					</div>
					<div id="textMobile" style="display:none;width:700px;padding-top:10px;border:2px">
						<label>选择方式：</label>
						<select class="form-control required" style="width:200px" onchange="selectType(this.value)">
		       				<option value="0">输入手机号</option>
		       				<option value="1">输入用户ID</option>
		       			</select>
		       			<input id="moreType" name="moreType" type="hidden" value="0">
						<p></p>
						<label>输入用户手机号或用户ID：<font color="red">输入用户手机号码或用户ID并且要“,”分割开</font></label>
						<form:textarea path="mobileNum" class="form-control" rows="8" cols="60"/>
						
					</div>
				<form:hidden path="userId"/>
				</form:form>
			</div>
				
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>