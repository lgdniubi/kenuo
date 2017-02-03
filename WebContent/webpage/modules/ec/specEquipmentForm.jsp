 <%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
<title>设备表</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->


<script type="text/javascript">
	var validateForm;
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			$("#inputForm").submit();
			return true;
		}
		return false;
	}
	$(document).ready(function() {
		select($("#TYPE").val());
	
		$("#name").focus();
		validateForm = $("#inputForm").validate({
			
			submitHandler : function(form) {
				//alert("提交")
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

		
		//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
		//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
		//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
		$("#inputForm").validate().element($("#name"));
		
		
	//	$("#shopButton, #shopName").click(function(){	    增加  , #shopName  文本框有点击事件
			$("#shopButton").click(function(){
						
				var bazaarId = $("#bazaarId").val();
				// 是否限制选择，如果限制，设置为disabled
				if ($("#shopButton").hasClass("disabled")){
					return true;
				}
				
				if(bazaarId == null || bazaarId == ""){
					top.layer.alert('请先选择市场!', {icon: 0, title:'提醒'});
				}else{
					// 正常打开	
					top.layer.open({
					    type: 2, 
					    area: ['300px', '420px'],
					    title:"选择所在店铺",
					    ajaxData:{selectIds: $("#shopId").val()},
					    content: "${ctx}/tag/treeselect?url="+encodeURIComponent("/ec/specEquipment/treeDataForShop?id="+bazaarId)+"&module=&checked=&extId=&isAll=" ,
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
								$("#shopId").val(ids.join(",").replace(/u_/ig,""));
								$("#shopName").val(names.join(","));
								$("#shopName").focus();
								//预约管理-->> 修改预约地址时异步加载美容师   2016-06-29  咖啡
								if("shop" == "officeIdbeaut"){
									findBeauty();
								}
								top.layer.close(index);
						    	       },
			    	cancel: function(index){ //或者使用btn2
			    	           //按钮【按钮二】的回调
			    	       }
					}); 
			
				}
			});
	
			$("#bazaarButton").click(function(){
				$("#shopId").val("");	
				$("#shopName").val("");		
				// 是否限制选择，如果限制，设置为disabled
				if ($("#bazaarButton").hasClass("disabled")){
					return true;
				}
				// 正常打开	
				top.layer.open({
				    type: 2, 
				    area: ['300px', '420px'],
				    title:"选择市场",
				    ajaxData:{selectIds: $("#bazaarId").val()},
				    content: "${ctx}/tag/treeselect?url="+encodeURIComponent("/sys/office/treeData?isGrade=true")+"&module=&checked=&extId=&isAll=" ,
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
									if (nodes[i].level == 0){
										//top.$.jBox.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
										top.layer.msg("不能选择根节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
										return false;
									}//
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
								$("#bazaarId").val(ids.join(",").replace(/u_/ig,""));
								$("#bazaarName").val(names.join(","));
								$("#bazaarName").focus();
								//预约管理-->> 修改预约地址时异步加载美容师   2016-06-29  咖啡
								if("bazaar" == "officeIdbeaut"){
									findBeauty();
								}
								top.layer.close(index);
						    	       },
		    	cancel: function(index){ //或者使用btn2
		    	           //按钮【按钮二】的回调
		    	       }
				}); 
			
			});
			
			$("#labelButton").click(function(){
				var bazaarId = $("#bazaarId").val();
			
				// 是否限制选择，如果限制，设置为disabled
				if ($("#labelButton").hasClass("disabled")){
					return true;
				}
				
				if(bazaarId == null || bazaarId == ""){
					top.layer.alert('请先选择市场!', {icon: 0, title:'提醒'});
				}else{
					var newFlag = $('#type').val();
					// 正常打开	
					top.layer.open({
					    type: 2, 
					    area: ['300px', '420px'],
					    title:"选择设备标签",
					    ajaxData:{selectIds: $("#labelId").val()},
					    content: "${ctx}/tag/treeselect?url="+encodeURIComponent("/ec/equipmentLabel/treeData?newFlag="+newFlag)+"&module=&checked=&extId=&isAll=" ,
					    btn: ['确定', '关闭'],
					    yes: function(index, layero){ //或者使用btn1
								var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
								var ids = [], names = [], nodes = [];
								if ("" == "true"){
									nodes = tree.getCheckedNodes(true);
								}else{
									nodes = tree.getSelectedNodes();
								}
								for(var i=0; i<nodes.length; i++) {//
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
								$("#labelId").val(ids.join(",").replace(/u_/ig,""));
								$("#labelName").val(names.join(","));
								$("#labelName").focus();
								//预约管理-->> 修改预约地址时异步加载美容师   2016-06-29  咖啡
								if("label" == "officeIdbeaut"){
									findBeauty();
								}
								
								//异步添加设备名称
								$.ajax({
								type:"post",
								data:{
									labelId:$("#labelId").val(),
									bazaarId:$("#bazaarId").val(),
									labelName:$("#labelName").val()
								 },
								url:"${ctx}/ec/specEquipment/getEquipmentName",
								success:function(date){
									var newDate = eval("("+date+")");
								 	var type = newDate.type;
									var name = newDate.name;
									if(type =="success"){
										$("#name").val(name);
									}
									if(date.type=="error"){
										top.layer.alert('获取设备标签失败!', {icon: 2, title:'提醒'});
									}
												
								},
								error:function(XMLHttpRequest,textStatus,errorThrown){
											    
								}
											 
						});
								
								top.layer.close(index);
						    	       },
			    	cancel: function(index){ //或者使用btn2
			    	           //按钮【按钮二】的回调
			    	       }
					}); 
				}			
	
				});
			
		});
		function select(obj){
			$("#shopId").val("");	
			$("#shopName").val("");		
			if(obj == 1 || obj == 0){
				$("#shop").show();
			}else if(obj == 2){
				$("#shop").hide();
			}
		}
</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="equipment" action="${ctx}/ec/specEquipment/save" method="post" class="form-horizontal">
		<form:hidden path="equipmentId" />
		<input id="TYPE" value="${equipment.type}" type="hidden"/>
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<c:if test="${equipment.flag == 1}">
				<tr>
					<td class="active" style="width:100px;">
						<label class="pull-right"><font color="red">*</font>设备类型:</label>
					</td>
					<td>
						<select class="form-control required" id="type" name="type" onchange="select(this.value)">
							<option value=1 <c:if test="${equipment.type == 1}">selected</c:if>>特殊</option>
							<option value=2 <c:if test="${equipment.type == 2}">selected</c:if>>通用</option>
						</select>
					</td>
				</tr>
				</c:if>
				<tr>
					<td class="active">
						<label class="pull-right"><font color="red">*</font>选择市场:</label>
					</td>
					<td>
						<%-- <sys:treeselect id="bazaar" name="bazaarId" value="${equipment.bazaarId}" labelName="bazaarName" labelValue="${equipment.bazaarName}" title="市场" url="/sys/office/treeData?isGrade=true" cssClass=" form-control input-sm required" allowClear="true" notAllowSelectRoot="true" notAllowSelectParent="true"/> --%>
						<input id="bazaarId" class=" form-control input-sm required" name="bazaarId" value="${equipment.bazaarId}" aria-required="true" type="hidden">
						<div class="input-group">
							<input id="bazaarName" class="form-control input-sm required valid" name="bazaarName" readonly="readonly" value="${equipment.bazaarName}" data-msg-required="" style="" aria-required="true" aria-invalid="false" type="text">
							<span class="input-group-btn">
								<button id="bazaarButton" class="btn btn-sm btn-primary " type="button">
									<i class="fa fa-search"></i>
								</button>
							</span>
						</div> 
						<label id="bazaarName-error" class="error" for="bazaarName" style="display: none"></label>
					</td>
				</tr>
			
				<tr id="shop">
					<td class="active">
						<label class="pull-right">选择店铺:</label>
					</td>
					<td>
					<%-- 	<sys:treeselect id="shop" name="shopId" value="${equipment.shopId}" labelName="" labelValue="" title="所在店铺" url="/ec/specEquipment/treeDataForShop" cssClass=" form-control input-sm" allowClear="true" notAllowSelectRoot="false" notAllowSelectParent="false" /> --%>
					
					<input id="shopId" class=" form-control input-sm" name="shopId" value="" type="hidden">
						<div class="input-group">
							<input id="shopName" class=" form-control input-sm" name="shopName" readonly="readonly" value="${equipment.shopName}" data-msg-required="" style="" type="text">
								<span class="input-group-btn">
									<button id="shopButton" class="btn btn-sm btn-primary " type="button">
										<i class="fa fa-search"></i>
									</button>
								</span>
						</div>
						<label id="shopName-error" class="error" for="shopName" style="display:none"></label>
					</td>
				</tr>
				
				<tr>
					<td class="active">
						<label class="pull-right"><font color="red">*</font>设备标签:</label>
					</td>
					<td>
						<input id="labelId" class="form-control input-sm required" name="labelId" value="0" type="hidden">
						<div class="input-group">
							<input id="labelName" class="form-control input-sm required" name="labelName" readonly="readonly" value="${equipment.labelName}" data-msg-required="" style="" type="text"> 
							<span class="input-group-btn">
								<button id="labelButton" class="btn btn-sm btn-primary " type="button">
									<i class="fa fa-search"></i>
								</button>
							</span>
						</div> 
						<label id="labelName-error" class="error" for="labelName" style="display: none"></label>
						
					</td>
				</tr>
				<tr>
					<td class="active">
						<label class="pull-right">设备名称:</label>
					</td>
					<td>
						<form:input path="name" htmlEscape="false" maxlength="64" class="form-control" readonly="true" />
					</td>
				</tr>
				<tr>
					<td class="active">
						<label class="pull-right">备注:</label>
					</td>
					<td>
						<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/>
					</td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>