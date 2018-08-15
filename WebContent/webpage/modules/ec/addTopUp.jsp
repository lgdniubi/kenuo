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
	                	<input type="hidden" id="orderArrearage" name="orderArrearage" value="${orderArrearage }" />
	                	<input type="hidden" id="userid" name="userid" value="${userid }" />
	                	<input type="hidden" id="couponId" name="couponId" value="0">
						<input type="hidden" id="baseAmount" name="baseAmount" value="0">
						<input type="hidden" id="couponMoney" name="couponMoney" value="0">
						<input type="hidden" id="useCouponFlag" name="useCouponFlag" value="0">
						<input type="hidden" id="useCouponId" name="useCouponId" value="0">
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
						<c:if test="${fn:length(rechargeCouponList) > 0}">
							<p style="position: relative;padding-left: 8em;">
								<label class="active" style="position: absolute;left: 0;top: 0;">　 &nbsp;&nbsp;选择红包：</label>
								<c:forEach items="${rechargeCouponList}" varStatus="status" step="2">
								<span>
									<span class="check-box"><input type="checkbox" id="${rechargeCouponList[status.index].id}" name="useCoupon" value="${rechargeCouponList[status.index].id}" onchange="change(${rechargeCouponList[status.index].id},${rechargeCouponList[status.index].baseAmount},${rechargeCouponList[status.index].couponMoney},${rechargeCouponList[status.index].useCouponId})"><label for="${rechargeCouponList[status.index].id}"></label>${rechargeCouponList[status.index].couponName}:￥${rechargeCouponList[status.index].couponMoney}</span>
									<c:if test="${(status.index + 1) < fn:length(rechargeCouponList)}">
									　　<span class="check-box"><input type="checkbox" id="${rechargeCouponList[status.index+1].id}" name="useCoupon" value="${rechargeCouponList[status.index+1].id}" onchange="change(${rechargeCouponList[status.index+1].id},${rechargeCouponList[status.index+1].baseAmount},${rechargeCouponList[status.index+1].couponMoney},${rechargeCouponList[status.index+1].useCouponId})"><label for="${rechargeCouponList[status.index+1].id}"></label>${rechargeCouponList[status.index+1].couponName}:￥${rechargeCouponList[status.index+1].couponMoney}</span>
									</c:if>
								</span><br>
								</c:forEach>
							</p>
						</c:if>
						<p>
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
			var rechargeAmount = parseFloat($("#rechargeAmount").val());  	// 充值金额
			var accountBalance = parseFloat($("#accountBalance").val());	// 账户余额
			var goodsBalance = parseFloat($("#goodsBalance").val());		// 商品余额
			var topUpTotalAmount = (rechargeAmount+accountBalance).toFixed(2);
			var newTopUpTotalAmount = (rechargeAmount+accountBalance+goodsBalance).toFixed(2);
			if(!checknum($("#rechargeAmount").val(),$("#accountBalance").val())){
				return;
			}
			
			if(newTopUpTotalAmount <= 0){
				top.layer.alert('充值总金额不可小于等于0!', {icon: 0, title:'提醒'});
				return;
			}
			
			// 不可多充值
			if(newTopUpTotalAmount > parseFloat($("#orderArrearage").val())){
				top.layer.alert('充值总金额不可大于欠款!', {icon: 0, title:'提醒'});
				return;
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
		//判断输入的营业额
		function checknum(obj1,obj2){
			var re = /^([+-]?)\d*\.?\d{0,2}$/; 
			if(obj1 == ''){ obj1 = 0; }
			if(obj2 == ''){ obj2 = 0; }
			if(!re.test(obj1) || !re.test(obj2)){
				top.layer.alert('请输入正确的金额(最多两位小数)', {icon: 0, title:'提醒'});
				return false;
			}else{
				return true;
			}
		}
		
		function change(couponId,baseAmount,couponMoney,useCouponId){
			if($('input:checkbox[name=useCoupon]:checked').length == 0){
				$("input[type=checkbox][name=useCoupon]").attr("disabled",false);
				$("#useCouponFlag").val("0");
				$("#couponId").val("0");
				$("#baseAmount").val("0");
				$("#couponMoney").val("0");
				$("#useCouponId").val("0");
			}else if($('input:checkbox[name=useCoupon]:checked').length == 1){
				if($("#rechargeAmount").val() <= 0){
					$("input[type=checkbox][name=useCoupon][id="+couponId+"]").attr("checked",false);
					top.layer.alert('充值金额必须大于0！！！', {icon: 0, title:'提醒'});
					return false;
				}else{
					if($("#rechargeAmount").val() < baseAmount){
						$("input[type=checkbox][name=useCoupon][id="+couponId+"]").attr("checked",false);
						top.layer.alert('充值金额不足该红包满减金额！！！', {icon: 0, title:'提醒'});
						return false;
					}else{
						$("input[type=checkbox][name=useCoupon][value != "+couponId+"]").attr("disabled",true);
						$("#useCouponFlag").val("1");
						$("#couponId").val(couponId);
						$("#baseAmount").val(baseAmount);
						$("#couponMoney").val(couponMoney);
						$("#useCouponId").val(useCouponId);
					}
				}
			}
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
									/* if (nodes[i].level == 0){
										//top.$.jBox.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
										top.layer.msg("不能选择根节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
										return false;
									} */
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