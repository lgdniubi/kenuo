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
					},
					memberCount:{max : 50000},
					groupUserCount:{max : 2500},
					groupCount:{max : 400}
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
					},
					memberCount:{max : "数量超过上限"},
					groupUserCount:{max : "数量超过上限"},
					groupCount:{max : "数量超过上限"}
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
			
			var c = '${modelFranchisee.paytype}';
			$(".pay").on("click",function(e){
				e.preventDefault();
								
				var that = $(this)
				
				if(c==that.val()){
					return;
				}
				var msg = "修改支付方式之后，商家内店铺的支付账户性质会发生变化，之前的付款方式将仅用于还款，还款方式可以用于付款/还款，是否继续更改？";
				top.layer.confirm(msg, {icon: 3, title:'系统提示'},function(index, layero){
					c = that.val();
					that.prop("checked",true);
					top.layer.close(index);
					}, function(index){
					  
				});
			});
			
		});
		
		
		
		function changeIM(value){
			switch(value)
			{
			case '5':
				setValIM(0,0,50);
			  break;
			case '6':
				setValIM(100,100,50);
			  break;
			case '7':
				setValIM(50000,1000,300);
			  break;
			default:
				setValIM(0,0,50);
			}
		}
		function setValIM(memC,groupUserC,groupC){
			$("#memberCountId").val(memC);
			$("#groupUserCountId").val(groupUserC);
			$("#groupCountId").val(groupC);
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="modelFranchisee" action="${ctx}/sys/franchisee/saveFranchise?opflag=qy" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<input name="franchiseeid" value="${modelFranchisee.franchiseeid}" type="hidden">
		<input name="userid" value="${modelFranchisee.userid}" type="hidden">
		<input name="applyid" value="${modelFranchisee.applyid}" type="hidden">
		<input name="id" value="${modelFranchisee.id}" type="hidden">
		<input name="pageNo" type="hidden" value="${pageNo}" />
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
			    <tr>
			    	<td align="center" style="height:1px;border-top:2px solid #555555;" colspan="4"><label class="pull-left">企业权益设置:</label></td>
				</tr>
			    <tr>
			         <td style= "width:100px" class="active"><label class="pull-right">企业会员类型:</label></td>
			         <td style= "width:160px"><input id="mod_id1" class=" input-sm required" name="modid" onclick="changeIM(this.value)" value="5" aria-required="true" <c:if test="${empty modelFranchisee.modid|| modelFranchisee.modid == 5}">checked="checked"</c:if>  type="radio">标准版</td>
			         <td style= "width:160px"><input id="mod_id2" class=" input-sm required" name="modid" onclick="changeIM(this.value)" value="6" aria-required="true" <c:if test="${modelFranchisee.modid == 6}">checked="checked"</c:if> type="radio">高级版</td>
			         <td style= "width:160px"><input id="mod_id3" class=" input-sm required" name="modid" onclick="changeIM(this.value)" value="7" aria-required="true" <c:if test="${modelFranchisee.modid == 7}">checked="checked"</c:if> type="radio">旗舰版</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">采购支付方式:</label></td>
			         <td><input id="paytype1" class="pay input-sm required" name="paytype" value="1" aria-required="true" <c:if test="${modelFranchisee.paytype == 1}">checked="checked"</c:if> type="radio">在线支付</td>
			         <td colspan="2"><input id="paytype2" class="pay input-sm required" name="paytype" value="0" aria-required="true" <c:if test="${modelFranchisee.paytype == 0}">checked="checked"</c:if> type="radio">线下支付</td>
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
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
			    	<td align="center"  style="height:1px;border-top:2px solid #555555;" colspan="3"><label class="pull-left">IM权限:</label></td>
				</tr>
		        <tr>
			         <td><label class="pull-right">1V1尊享技师服务使用人数</label></td>
			         <td >
			         	<input id="memberCountId" name="memberCount" type="text" maxlength="5" class="form-control input-sm required number" value="${modelFranchisee.memberCount}" />
			         </td>
		         	<td>(最多50000)</td>
				</tr>
				<tr>
			         <td><label class="pull-right">创建群组人数上限:</label></td>
			         <td >
			         	<input id="groupUserCountId" name="groupUserCount" type="text" maxlength="4" class="form-control input-sm required number" value="${modelFranchisee.groupCount}" />
			         </td>
		         	<td>(最多2500)</td>
		        <tr>
		        <tr>
			         <td><label class="pull-right">个人可参与群组数:</label></td>
			         <td >
			         	<input id="groupCountId" name="groupCount" type="text" maxlength="3" class="form-control input-sm required number" value="${modelFranchisee.groupUserCount}" />
			         </td>
		         	<td>(最多400)</td>
		        </tr>
			</tbody>
		</table>
	</form:form> 
</body>
</html>