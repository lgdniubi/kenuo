<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
	<title>充值</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<style>
		#ichecks{display: inline-block;width: 17px;height: 17px;background: none;vertical-align: middle;margin-top: -1px;margin-right: 2px;}
	</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
	                	<input type="hidden" id="userid" name="userid" value="${userid }" />
	                	<c:if test="${isReal == 1 && servicetimes != 999}"> <!-- 0实物 -->
		                	<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>服务单次价：</label>
		                	${singleRealityPrice }<font color="red">&nbsp;&nbsp;充值金额要满足单次价的倍数！</font>
	                	</c:if>
	                	<p></p>
	                	<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>商品余额：</label>
	                	<input type="text" id="goodsBalance" value=${goodsBalance} name="goodsBalance" class="form-control required" style="width:150px;" readonly="readonly"/>
	                	<p></p>
						<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>充值金额：</label>
						<input type="text" id="rechargeAmount" value="0" name="rechargeAmount" class="form-control required" style="width:150px;"  value="0" onkeyup="this.value=this.value.replace(/[^\d.]/g,&quot;&quot;)" onpaste="this.value=this.value.replace(/[^\d.]/g,&quot;&quot;)" onfocus="if(value == '0')value=''" onblur="if(this.value == '')this.value='0';"/>
						<p></p>
						&nbsp;<input type="checkbox" id="ichecks" /><label class="active">账户余额：</label>
						<input type="text" id="accountBalance" name="accountBalance" readonly="readonly" style="width:150px;" value="0" class="form-control required" value="0" onkeyup="this.value=this.value.replace(/[^\d.]/g,&quot;&quot;)" onpaste="this.value=this.value.replace(/[^\d.]/g,&quot;&quot;)" onfocus="if(value == '0')value=''" onblur="if(this.value == '')this.value='0';"/>（可用余额：<label id="ye"></label>）
						<p></p>
						<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>总付款：</label>
						<input type="hidden" id="topUpTotalAmount" name="topUpTotalAmount" readonly="readonly" class="form-control required" style="width:150px;" />
						<input type="text" id="newTopUpTotalAmount" name="newTopUpTotalAmount" readonly="readonly" class="form-control required" style="width:150px;" />
						&nbsp;&nbsp;<a href="#" onclick="sum()">计算金额</a>
						<a href="#" onclick="update()">修改金额</a>
						<input type="hidden" name="jsmoney" id="jsmoney" />
						<!-- <p></p>
						<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>归属机构：</label>	
						<input id="belongOfficeId" class=" form-control" name="belongOfficeId" value="" type="hidden">
						<input id="belongOfficeName" class=" form-control required" name="belongOfficeName" readonly="readonly" type="text">
						<button id="belongOfficeButton" class="btn btn-primary " type="button">
							<i class="fa fa-search"></i>
						</button>
						<p></p>
						<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;归属人：</label>	
						<input id="belongUserId" class=" form-control" name="belongUserId" value="" type="hidden">
						<input id="belongUserName" class=" form-control" name="belongUserName" readonly="readonly" value="" data-msg-required="" style="" type="text">
						<button id="belongUserButton" class="btn btn-primary " type="button">
							<i class="fa fa-search"></i>
						</button> -->
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading" id="loading"></div>
	<script type="text/javascript">
		function sum(){
			var rechargeAmount = parseFloat($("#rechargeAmount").val());
			var accountBalance = parseFloat($("#accountBalance").val());
			var goodsBalance = parseFloat($("#goodsBalance").val());
			var topUpTotalAmount = (rechargeAmount+accountBalance).toFixed(2);
			var newTopUpTotalAmount = (rechargeAmount+accountBalance+goodsBalance).toFixed(2);
			
			$("#rechargeAmount").attr("readonly",true);
			$("#accountBalance").attr("readonly",true);
			$("#topUpTotalAmount").val(topUpTotalAmount);
			$("#newTopUpTotalAmount").val(newTopUpTotalAmount);
			$("#jsmoney").val(1); //计算过费用
			$("#ichecks").attr("disabled",true);
		}
		function update(){
			$("#rechargeAmount").removeAttr("readonly");
			$("#accountBalance").removeAttr("readonly");
			$("#topUpTotalAmount").val("");
			$("#newTopUpTotalAmount").val("");
			$("#jsmoney").val(0); //未计算过
			$("#ichecks").attr("disabled",false);
		}
		$(document).ready(function() {
			$("#ichecks").change(function(){
				if($(this).is(":checked")){
					$("#accountBalance").removeAttr("readonly");
					var userid = $("#userid").val();
					$.ajax({
						type:"post",
						data:{
							userid:userid
						 },
						url:"${ctx}/ec/orders/getAccount",
						success:function(date){
							$("#ye").html(date);
							$("#topUpTotalAmount").val("");
							$("#newTopUpTotalAmount").val("");
						},
						error:function(XMLHttpRequest,textStatus,errorThrown){}
					}); 
				}else{
					$("#accountBalance").val(0);
					$("#accountBalance").attr("readonly",true);
				}
			});
			
			/* $("#belongOfficeButton").click(function(){
				// 是否限制选择，如果限制，设置为disabled
				if ($("#belongOfficeButton").hasClass("disabled")){
					return true;
				}
				// 正常打开	
				top.layer.open({
				    type: 2, 
				    area: ['300px', '420px'],
				    title:"选择部门",
				    ajaxData:{selectIds: $("#belongOfficeId").val()},
				    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/sys/office/treeData?type=2")+"&module=&checked=&extId=&isAll=&selectIds=" ,
				    btn: ['确定', '关闭']
		    	       ,yes: function(index, layero){ //或者使用btn1
								var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
								var ids = [], names = [], nodes = [];
								if ("" == "true"){
									nodes = tree.getCheckedNodes(true);
								}else{
									nodes = tree.getSelectedNodes();
								}
								for(var i=0; i<nodes.length; i++) {//
									ids.push(nodes[i].id);
									names.push(nodes[i].name);//
									break; // 如果为非复选框选择，则返回第一个选择  
								}
								
								$("#belongUserId").val("");
								$("#belongUserName").val("");
								
								$("#belongOfficeId").val(ids.join(",").replace(/u_/ig,""));
								$("#belongOfficeName").val(names.join(","));
								$("#belongOfficeName").focus();
								top.layer.close(index);
						    	       },
		    	cancel: function(index){ //或者使用btn2
		    	           //按钮【按钮二】的回调
		    	       }
				}); 
			
			});
			
			$("#belongUserButton").click(function(){
				var belongOfficeId = $("#belongOfficeId").val();
				// 是否限制选择，如果限制，设置为disabled
				if ($("#belongUserButton").hasClass("disabled")){
					return true;
				}
				
				if(belongOfficeId == null || belongOfficeId == ""){
					top.layer.alert('请先选择归属机构!', {icon: 0, title:'提醒'});
				}else{
					// 正常打开	
					top.layer.open({
					    type: 2, 
					    area: ['300px', '420px'],
					    title:"选择人员",
					    ajaxData:{belongOfficeId:belongOfficeId},
					    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/sys/user/officeUserTreeData?belongOfficeId="+belongOfficeId),
					    btn: ['确定', '关闭']
			    	       ,yes: function(index, layero){ //或者使用btn1
									var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
									var ids = [], names = [], nodes = [];
									if ("" == "true"){
										nodes = tree.getCheckedNodes(true);
									}else{
										nodes = tree.getSelectedNodes();
									}
									for(var i=0; i<nodes.length; i++) {//
										ids.push(nodes[i].id);
										names.push(nodes[i].name);//
										break; // 如果为非复选框选择，则返回第一个选择  
									}
									$("#belongUserId").val(ids.join(",").replace(/u_/ig,""));
									$("#belongUserName").val(names.join(","));
									$("#belongUserName").focus();
									top.layer.close(index);
							    	       },
			    	cancel: function(index){ //或者使用btn2
			    	           //按钮【按钮二】的回调
			    	       }
					}); 
				}
			}); */
		});
	</script>
</body>
</html>