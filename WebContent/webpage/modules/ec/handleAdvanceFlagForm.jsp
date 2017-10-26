<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
	<title>处理预约金</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<style>
		#ichecks{display: inline-block;width: 17px;height: 17px;background: none;vertical-align: middle;margin-top: -1px;margin-right: 2px;}
	</style>
	<script type="text/javascript">
	$(document).ready(function(){
		var accountBalance = $("#accountBalance").val();
		var debt = $("#debt").val();
		var advance = $("#advance").val();
		var singleRealityPrice = $("#singleRealityPrice").val();
		//账户余额小于欠款，则无法使用账户的余额
		if(accountBalance - debt < 0){
			$("#ichecks").attr("disabled",true);
		}
		//若订金大于等于服务单次价，则也就不使用账户的余额
		if(advance - singleRealityPrice >= 0){
			$("#ichecks").attr("disabled",true);
		}
		
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
							for(var i=0; i<nodes.length; i++) {//
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
		});
	}); 
	
	function sum(){
		if($("input[type='checkbox']").is(':checked')){
			$("#sum").val(0);
		}else{
			$("#sum").val(1);
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
	                	<p></p>
						<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;预约金：</label>
						<input type="text" id="advance" name="advance" readonly="readonly" value="${orderGoods.advance}" class="form-control required" style="width:150px;"  />
						<c:if test="${servicetimes != 999}">
							<p></p>
							<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;服务单次价：</label>
							<input type="text" id="singleRealityPrice" name="singleRealityPrice" readonly="readonly" value="${orderGoods.singleRealityPrice}" class="form-control required" style="width:150px;"  />
							<p></p>
							<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;服务次数：</label>
							<input type="text" id="advanceServiceTimes" name="advanceServiceTimes" readonly="readonly" value="${orderGoods.advanceServiceTimes}" class="form-control required" style="width:150px;"  />	
						</c:if>
						<p></p>
						<c:if test="${orderGoods.advanceServiceTimes == 0}">
							<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;欠款：</label>
							<input type="text" id="debt" name="debt" readonly="readonly" value="${orderGoods.debt}" class="form-control required" style="width:150px;"  />
							<p></p>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="ichecks" onchange="sum()" name="ichecks"/><label class="active">是否使用</label>
							（账户可用余额：${orderGoods.accountBalance}）
							<input type="hidden" id="accountBalance" name="accountBalance" value="${orderGoods.accountBalance}" class="form-control required" style="width:150px;"  />
						</c:if>
						<input type="hidden" id="sum" name="sum" value="1"/>
						<c:if test="${orderGoods.advanceServiceTimes != 0}">
							<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;余额：</label>
							<input type="text" id="advanceBalance" name="advanceBalance" readonly="readonly" value="${orderGoods.advanceBalance}" class="form-control required" style="width:150px;"  />
						</c:if>
						<p></p>
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
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>