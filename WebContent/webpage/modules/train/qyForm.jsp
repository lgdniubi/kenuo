<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> --%>
<%-- <%@ page import="com.training.modules.sys.utils.ParametersFactory"%> --%>
<html>
<head>
	<title>企业审核</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if(validateForm.form()){
			  //if(confirms('点击确定相当于授权，不想授权请点击取消')){
	        	  loading('正在提交，请稍等...');
			      $("#inputForm").submit();
		     	  return true;
		  	}
		  return false;
		}
		
		$(document).ready(function() {
			
			var start = {
				    elem: '#auth_start_date',
				    format: 'YYYY-MM-DD',
				    event: 'focus',
				    min: laydate.now(), //设定最小日期为当前日期  
				    //max: $("#auth_start_date").val(),   //最大日期
				    istime: false,				//是否显示时间
				    isclear: true,				//是否显示清除
				    istoday: true,				//是否显示今天
				    issure: true,				//是否显示确定
				    festival: true,				//是否显示节日
				    choose: function(datas){
				         end.min = datas; 		//开始日选好后，重置结束日的最小日期
				         end.start = datas 		//将结束日的初始值设定为开始日
				    }
				};
			var end = {
				    elem: '#auth_end_date',
				    format: 'YYYY-MM-DD',
				    event: 'focus',
				    min: laydate.now(), //设定最小日期为当前日期  
				   // min: $("#auth_end_date").val(),
				    istime: false,
				    isclear: true,
				    istoday: true,
				    issure: true,
				    festival: true,
				    choose: function(datas){
				        start.max = datas; //结束日选好后，重置开始日的最大日期
				    }
				};
			laydate(start);
			laydate(end);
		
		
			validateForm = $("#inputForm").validate({
				rules:{
					paytype:{
						required:true
					},
					authStartDate:{
						required:true
					},
					authEndDate:{
						required:true
					}
				},
				messages:{
					paytype:{
						required:"选择支付方式"
					},
					authStartDate:{
						required:"选择开始时间"
					},
					authEndDate:{
						required:"选择结束时间"
					}
				},
				submitHandler: function(form){
						loading('正在提交，请稍等...');
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
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="modelFranchisee" action="${ctx}/train/userCheck/saveFranchise?opflag=qy" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<input name="franchiseeid" value="${modelFranchisee.franchiseeid}" type="hidden">
		<input name="userid" value="${modelFranchisee.userid}" type="hidden">
		<input name="applyid" value="${modelFranchisee.applyid}" type="hidden">
		<input name="id" value="${modelFranchisee.id}" type="hidden">
		<input name="pageNo" type="hidden" value="${pageNo}" />
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="4"><label class="pull-left">企业权益设置:</label></td>
				</tr>
			    <tr>
			         <td style= "width:100px" class="active"><label class="pull-right">企业会员类型:</label></td>
			         <td style= "width:160px"><input id="mod_id1" class=" input-sm required" name="modid" value="5" aria-required="true" <c:if test="${empty modelFranchisee.modid|| modelFranchisee.modid == 5}">checked="checked"</c:if>  type="radio">标准版</td>
			         <td style= "width:160px"><input id="mod_id2" class=" input-sm required" name="modid" value="6" aria-required="true" <c:if test="${modelFranchisee.modid == 6}">checked="checked"</c:if> type="radio">高级版</td>
			         <td style= "width:160px"><input id="mod_id3" class=" input-sm required" name="modid" value="7" aria-required="true" <c:if test="${modelFranchisee.modid == 7}">checked="checked"</c:if> type="radio">旗舰版</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">采购支付方式:</label></td>
			         <td><input id="paytype1" class=" input-sm required" name="paytype" value="1" aria-required="true" <c:if test="${modelFranchisee.paytype == 1}">checked="checked"</c:if> type="radio">线上支付</td>
			         <td colspan="2"><input id="paytype2" class=" input-sm required" name="paytype" value="0" aria-required="true" <c:if test="${modelFranchisee.paytype == 0}">checked="checked"</c:if> type="radio">线下支付</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">授权期限:</label></td>
			         <td ><input id="auth_start_date" name="authStartDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${modelFranchisee.authStartDate}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="开始时间" readonly="readonly"/></td>
			         <td align="center"><label class="center">----</label></td>
			         <td ><input id="auth_end_date" name="authEndDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${modelFranchisee.authEndDate}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="结束时间" readonly="readonly"/></td>
				</tr>
			    <tr>
			    	<td align="center" colspan="4">
			    	<font color="red">点击确定相当于授权，不想授权请点击取消</font>
			    	</td>
				</tr>
			</tbody>
		</table>  
	</form:form> 
</body>
</html>