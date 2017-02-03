<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>仓库管理列表</title>
<meta name="decorator" content="default" />
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/ec/css/area.css">
<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	var validateForm;
	function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
	  if(validateForm.form()){
		  $("#inputForm").submit();
		  return true;
	  }
	  return false;
	}
	
	$(document).ready(function() {
		$("#areaDiv").hide();
		//表单验证
		validateForm = $("#inputForm").validate({
			rules: {
				firstWeights:{
					required:true,
					digits:true
				},
				firstPrices:{
					required:true,
				},
				addWeights:{
					required:true,
					digits:true
				},
				addPrices:{
					required:true,
				}
			},
			messages: {
				firstWeights: {
		            required : "",
		            digits: ""
		    	},
		    	firstPrices: {
		            required : "",
		            digits: ""
		    	},
		    	addWeights: {
		            required : "",
		            digits: ""
	    		},
	    		addPrices: {
		            required : "",
		            digits: ""
		   		},
			 },
			submitHandler: function(form){
				form.submit();
			},
			errorContainer: "#messageBox",
			errorPlacement: function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});
	});
	
	function addModel(){
		var i = $("#tps").val();
		var index = ++i;
		$("#tps").val(index);
		$("#close").val(index);
		var count = "<tr>"+
						"<td style='text-align: right;'>"+
							"<label class='pull-left' id='content"+i+"'></label><input type='button' value='编辑' class='btn btn-success btn-sm' onclick='showAreaDiv("+i+")'>"+
							"<input type='hidden' id='cityId"+i+"' name='areaIds' value=''>"+
						"</td>"+
						"<td align='center'><input type='text' name='firstWeights' class='form-control' style='width: 100px;' /></td>"+
						"<td align='center'><input type='text' name='firstPrices' class='form-control' style='width: 100px;' /></td>"+
						"<td align='center'><input type='text' name='addWeights' class='form-control' style='width: 100px;' /></td>"+
						"<td align='center'><input type='text' name='addPrices' class='form-control' style='width: 100px;' /></td>"+
						"<td style='text-align: center;'>"+
							"<input type='button' value='删除' class='btn btn-success btn-sm' onclick='removeTr(this)'>"+
						"</td>"+
					"</tr>";
		$("#count").append(count);
	}
	
	function removeTr(nowTr){
		$(nowTr).parent().parent().remove();
	}
	
	function showAreaDiv(i){
		$("#close").val(i);
		$("#areaDiv").show();
	}
	function closeAreaDiv(){
		$("#areaDiv").hide();
	}
	function showOrCloseCitys(type,id){
		var openId = $("#openId").val();
		if(openId != undefined && openId != ""){
			if(id == openId){
				$("#citys"+id).hide();
				$("#openId").val("");
			}else{
				$("#citys"+openId).hide();
				$("#citys"+id).show();
				$("#openId").val(id);
			}
		}else{
			$("#citys"+id).show();
			$("#openId").val(id);
		}
	}
	
	function jqchk(){ //jquery获取复选框值
		var i = $("#close").val();
		var city_id = "";
		var city_name =[];
		var city;
		//获取所有选中得省
		$('input[name="jprovince"]:checked').each(function(){
			city_id+=$(this).val()+"#";
			city_name.push($("#J_Province_"+$(this).val()).html()+" ");
			$(this).attr("checked",false);
		});
		//获取所有选中得市
		$('input[name="jcity"]:checked').each(function(){
			city_id+=$(this).val()+"#";
			city_name.push($("#J_City_"+$(this).val()).html()+" ");
			$(this).attr("checked",false);
			$(this).parent().parent().hide();
		});
		var cityids = city_id.substr(0,city_id.length-1);
		$("#content"+i).html(city_name);
		$("#cityId"+i).val(cityids);
		closeAreaDiv();
	}
	
	//选中省份，自动勾选城市
	//onclick="allSelect('${province.areaId }');"
	function allSelect(prvinceId){
		$("[id = J_City_"+prvinceId+"]:checkbox").attr("checked", true);
	}
</script>
</head>




<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
			<form:form id="inputForm" action="${ctx}/ec/pdTemplate/save" method="post">
					<table id="contentTable"
						class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<tr>
							<td colspan="6">
								<label class="pull-left"><font color="red">*</font>模板名称：</label>
								<input type="text" name="templateName" class="form-control required" style="width: 415px;display:inline"/>
								<input type="hidden" name="houseId" value="${template.houseId }" />
 							</td>
						</tr>
						<tr>
							<td colspan="6">
								<label class="pull-left"><font color="red">*</font>发货地址：</label>
								<div style="width:150px;float:left">
									<sys:treeselect id="areaId" name="area.id"
									value="${area.id }" labelName="area.name"
									labelValue="${area.name }" title="区域"
									url="/sys/area/treeData" cssClass="form-control required" />	
								</div>
								<div style="float:left;margin-left:10px;width:350px;">
									<input type="text" name="templateAddress" class="form-control required" style="width: 255px;"/>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<label class="pull-left"><font color="red">*</font>发货天数：</label>
								<input type="text" name="deliveryTime" class="form-control required digits" style="width: 100px;"/>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<label>是否包邮：</label>
								<input type="hidden" name="isFree" value="0" /><label style="padding-left: 5px;">自定义运费</label>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<label class="pull-left">计价方式：</label>按重量
								<input type="hidden" name="valuationType" value="1" class='form-control required' style="width: 415px;" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<label class="pull-center">
									<input type="hidden" name="houseId" value="${template.houseId }" />
									<font color="red">*</font>默认运费：<input type="text" name="firstWeightsDefault" style="width: 100px;display:inline" class="form-control required" />g内，
									<input type="text" name="firstPricesDefault" style="width: 100px;display:inline" class="form-control required" />元，
									每增加 <input type="text" name="addWeightsDefault" style="width: 100px;display:inline" class="form-control required" />g，
									增加运费 <input type="text" name="addPricesDefault" style="width: 100px;display: inline" class="form-control required" />元
									<input type="hidden" name="isDefault" value="1" >
								</label>
								<label class="pull-right">
									<input type="button" value="新配置" class="btn btn-success btn-sm" onclick="addModel()" />
									<input type="hidden"  id="tps" value="0">
									<input type='hidden' value='0' id='close' />
								</label>
							</td>
						</tr>
						<tr>
							<th style="text-align: center;width: 500px;">运送到</th>
							<th style="text-align: center;">首重(g)</th>
							<th style="text-align: center;">运费(元)</th>
							<th style="text-align: center;">续重(g)</th>
							<th style="text-align: center;">运费(元)</th>
							<th style="text-align: center;">操作</th>
						</tr>
						<tbody id="count">
						</tbody>
					</table>
				</form:form>
				<input type="hidden" id="openId" value="" >
			</div>
		</div>
		<div id="areaDiv" class="ks-dialog ks-overlay ks-ext-position dialog-areas ks-dialog-shown ks-overlay-shown" role="dialog" aria-labelledby="ks-dialog-header485" aria-hidden="false" tabindex="0" style="visibility: visible; left: 320px; top: 150px;">
			<div class="ks-ext-close">
					<button type="button" class="J_Submit btn btn-success btn-sm" onclick="jqchk()">确定</button><button type="button" class="J_Cancel btn btn-info btn-sm " onclick="closeAreaDiv()">取消</button>
				</div>
			<div class="ks-contentbox">
				<div class="ks-stdmod-header" id="ks-dialog-header485">
					<div class="title">选择区域</div>
				</div>
				
				<div class="ks-stdmod-body">
				<ul id="J_CityList">
					<li>
					<c:forEach items="${templates }" var="province" varStatus="status">
						<div class="ecity" style="width: 150px;">
							<span class="gareas" >
									<input type="checkbox" value="${province.areaId }" name="jprovince" class="J_Province" >
									<label id="J_Province_${province.areaId }" onclick="showOrCloseCitys('open','${province.areaId }')">${province.areaName }</label>
									<span class="check_num"></span>
									<img class="trigger" src="//gtd.alicdn.com/tps/i1/T1XZCWXd8iXXXXXXXX-8-8.gif">
							</span>
							<div class="citys" id="citys${province.areaId }">
								<c:forEach items="${province.cityList }" var="city">
									<span class="areas">
										<input type="checkbox" id="J_City_${province.areaId }" value="${city.areaId }" name="jcity" class="J_City">
										<label for="J_City_${province.areaId }" id="J_City_${city.areaId }">${city.areaName }</label>
									</span>
								</c:forEach>
								<p style="text-align:right;"><input type="button" value="关闭" class="close_button" onclick="showOrCloseCitys('close','${province.areaId }')"></p>
							</div>
						</div>
					</c:forEach>
					</li>
				</ul>
				</div>
				<div class="ks-stdmod-footer"></div>
			</div>
			<div tabindex="0" style="position:absolute;"></div>
		</div>
	</div>
</body>
</html>