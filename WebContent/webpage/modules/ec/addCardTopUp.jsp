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
	                	<input type="hidden" id="isReal" name="isReal" value="${isReal }" />
	                	<c:if test="${isReal == 3 }"> <!-- 通用卡 -->
		                	<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>服务单次价：</label>
		                	${singleRealityPrice }
		                	<p></p>
		                	<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>商品余额：</label>
	    	            	<input type="text" id="goodsBalance" value=${goodsBalance} name="goodsBalance" class="form-control required" style="width:150px;" readonly="readonly"/>
	                	</c:if>
	                	<c:if test="${isReal == 2 }"> <!-- 套卡 -->
	                	    <p></p>
		                	<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>套卡余额：</label>
	    	            	<input type="text" id="goodsBalance" value=${goodsBalance} name="goodsBalance" class="form-control required" style="width:150px;" readonly="readonly"/>
	                	</c:if>
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
						<p></p>
						<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>归属店铺：</label>	
						<input id="belongOfficeId" class=" form-control" name="belongOfficeId" value="" type="hidden">
						<input id="belongOfficeName" class=" form-control required" name="belongOfficeName" readonly="readonly" type="text">
						<button id="belongOfficeButton" class="btn btn-primary " type="button">
							<i class="fa fa-search"></i>
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading" id="loading"></div>
	<script type="text/javascript">
		function sum(){
			var isReal = $("#isReal").val();
			var rechargeAmount = parseFloat($("#rechargeAmount").val());
			var accountBalance = parseFloat($("#accountBalance").val());
			var goodsBalance = parseFloat($("#goodsBalance").val());
			var topUpTotalAmount = (rechargeAmount+accountBalance).toFixed(2);
			if(isReal == 3){
				var newTopUpTotalAmount = (rechargeAmount+accountBalance+goodsBalance).toFixed(2);
			}else if(isReal == 2){
				var newTopUpTotalAmount = (rechargeAmount+accountBalance).toFixed(2);
			}
			
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
			
			$("#belongOfficeButton").click(function(){
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
								for(var i=0; i<nodes.length; i++) {
									if (nodes[i].level == 0){
										//top.$.jBox.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
										top.layer.msg("不能选择根节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
										return false;
									}
									if (nodes[i].isParent){
										//top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
										//layer.msg('有表情地提示');
										top.layer.msg("不能选择父节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
										return false;
									}//
									ids.push(nodes[i].id);
									names.push(nodes[i].name);//
									break; // 如果为非复选框选择，则返回第一个选择  
								}
								
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
			
		});
	</script>
</body>
</html>