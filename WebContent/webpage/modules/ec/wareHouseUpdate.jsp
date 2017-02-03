<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>创建活动</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>

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
		
				validateForm = $("#inputForm").validate({
					rules : {},
					messages : {},
					submitHandler : function(form) {
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
				$("#areaIdButton").click(function(){
					// 是否限制选择，如果限制，设置为disabled
					$("#address").val("");
					if ($("#areaIdButton").hasClass("disabled")){
						return true;
					}
					// 正常打开	
					top.layer.open({
					    type: 2, 
					    area: ['300px', '420px'],
					    title:"选择区域",
					    ajaxData:{selectIds: $("#areaIdId").val()},
					    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/sys/area/treeData")+"&amp;module=&amp;checked=&amp;extId=&amp;isAll=" ,
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
									$("#areaIdId").val(ids.join(",").replace(/u_/ig,""));
									$("#areaIdName").val(names.join(","));
									$("#areaIdName").focus();
									//预约管理--&gt;&gt; 修改预约地址时异步加载美容师   2016-06-29  咖啡
									if("areaId" == "officeIdbeaut"){
										findBeauty();
									}
									top.layer.close(index);
							    	       },
			    	cancel: function(index){ //或者使用btn2
			    	           //按钮【按钮二】的回调
			    	       }
					}); 
				
				});
			});
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="inputForm" modelAttribute="pdWareHouse" action="${ctx}/ec/wareHouse/update" method="post" class="form-horizontal">
					<form:hidden path="wareHouseId"/>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>所属商家：</label></td>
								<td>
									<div style="width:249px;float:left">
										<sys:treeselect id="company" name="company.id" value="${pdWareHouse.company.id}" 
										labelName="company.name" labelValue="${pdWareHouse.company.name}"
										title="公司" url="/sys/franchisee/treeData" cssClass="form-control required"/>
									</div>
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>库房名称：</label></td>
								<td>
									<form:input path="name" htmlEscape="false" maxlength="50" style="width:250px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>库房管理员：</label></td>
								<td>
									<form:input path="governor" htmlEscape="false" maxlength="50" style="width:250px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>联系方式：</label></td>
								<td>
									<form:input path="phone" htmlEscape="false" maxlength="11" style="width:250px;" class="form-control required  digits" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>库房地址：</label></td>
								<td>
									<input id="areaIdId" name="area.id" class="form-control required" value="${pdWareHouse.areaId }" aria-required="true" type="hidden">
									<div class="input-group" style="width:249px;float:left">
										<input id="areaIdName" name="area.name" readonly="readonly" value="${pdWareHouse.areaName }" data-msg-required="" class="form-control required" style="" aria-required="true" type="text">
								       		 <span class="input-group-btn">
									       		 <button type="button" id="areaIdButton" class="btn   btn-primary  "><i class="fa fa-search"></i>
									             </button> 
								       		 </span>
								    </div>
									 <label id="areaIdName-error" class="error" for="areaIdName" style="display:none"></label>
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>详细地址：</label></td>
								<td>
									<form:input path="address" htmlEscape="address"  maxlength="80" style="width:250px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right">邮政编码：</label></td>
								<td>
									<form:input path="postalcode" htmlEscape="postalcode" maxlength="8" style="width:250px;" class="form-control digits" />	
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>备注：</label></td>
								<td>
									<textarea class="form-control required" name="remarks" maxlength="150" style="width:250px;">${pdWareHouse.remarks}</textarea>
								</td>
							</tr>
						</table>
					
					</form:form>
				</div>
				
			</div>
		</div>
	</div>
	
</body>
</html>