<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> --%>
<%-- <%@ page import="com.training.modules.sys.utils.ParametersFactory"%> --%>
<html>
<head>
	<title>手艺人审核</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if(validateForm.form()){
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
				   // max: $("#auth_start_date").val(),   //最大日期
				    min: laydate.now(), //设定最小日期为当前日期  
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
					discount:{
						number:true,
						min:0,
						max:1
					},
					authStartDate:{
						required:true
					},
					authEndDate:{
						required:true
					}
				},
				messages:{
					discount:{
						number:"输入合法的小数",
						min:"最小为0",
						max:"最大为1"
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
		//设置折扣，选择免费折扣只能为1，收费的时候设置成原来的
		function setDiscount(value){
			if(value==3){
				$("#discount").val("1");
				$("#discount").prop("readonly","readonly");
			}else{
				var oldDiscount = '${modelFranchisee.discount}';
				if (oldDiscount){//原来的不是空
					$("#discount").val(oldDiscount);
				}else{
					$("#discount").val("");
				}
				$("#discount").removeAttr("readonly");
			}
		}
		
		function changeTab(obj){
			var cval = $(obj).val();
			if(cval == 0){	//审核不通过
				$("#passForm").hide();
				$("#refuseForm").show(); 
				$("#inputForm").prop("action","${ctx}/train/userCheck/save");
				
			}else if(cval == 1){		//审核通过
				 $("#passForm").show();
				$("#refuseForm").hide();
				$("#inputForm").prop("action","${ctx}/train/userCheck/saveFranchise?opflag=qy");
			}
		}
	</script>
</head>
<body>
	<sys:message content="${message}"/>
	<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		<tbody>
		    <tr>
		    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">基本资料:</label></td>
			</tr>
		    <tr>
		         <td ><label class="pull-right">姓名:</label></td>
		         <td colspan="1">${userCheck.name}</td>
		         <td width="100px" ><label class="pull-right">电话:</label></td>
		         <td colspan="1">${userCheck.mobile}</td>
			</tr>
		    <tr>
		         <td ><label class="pull-right">昵称:</label></td>
		         <td colspan="1">${userCheck.nickname}</td>
		         <td class=""><label class="pull-right">身份证:</label></td>
		         <td colspan="1">${userCheck.addr.idcard}</td>
			</tr>
		    <tr>
		    	 <td class=""><label class="pull-right">E-mail:</label></td>
		         <td  colspan="1"> ${userCheck.addr.email}</td>
		         <td ><label class="pull-right">目前收入:</label></td>
		         <td colspan="1">${userCheck.income}</td>
			</tr>
		    <tr>
		         <td ><label class="pull-right">工作经验:</label></td>
		         <td colspan="1">${userCheck.startDate}</td>
		         <td class=""><label class="pull-right">所在地区:</label></td>
		         <td colspan="1">${userCheck.addr.provinceName}${userCheck.addr.cityName}${userCheck.addr.districtName}</td>
			</tr>
		    <tr>
		         <td ><label class="pull-right">专长:</label></td>
		         <td colspan="5">${userCheck.speciality}</td>
			</tr>
		   
		    <tr>
		         <td class=""><label class="pull-right">详细地址:</label></td>
		         <td colspan="5">${userCheck.addr.syrAddress}</td>
			</tr>
			</tbody>
			<tbody>
			<c:forEach var="bank" items="${userCheck.bankAccount}">
			    <tr>
			         <td class="active" colspan="4"><label class="pull-left">银行卡:</label></td>
				</tr>
			    <tr>
			         <td class=""><label class="pull-right">账户名称:</label></td>
			         <td colspan="3">${bank.accountname}</td>
				</tr>
			    <tr>
			         <td class=""><label class="pull-right">开户银行:</label></td>
			         <td>${bank.openbank}</td>
			         <td class=""><label class="pull-right">银行账户:</label></td>
			         <td>${bank.bankaccount}</td>
				</tr>
			    <tr>
			         <td class=""><label class="pull-right">开户地址:</label></td>
			         <td>${bank.openaddress}</td>
			         <td class=""><label class="pull-right">详细地址:</label></td>
			         <td>${bank.detailedaddress}</td>
				</tr>
			    <tr>
			         <td class=""><label class="pull-right">银行卡正面:</label></td>
			         <td ><img id="photosrc" src="${bank.cardup}" alt="images" style="width: 200px;height: 100px;"/></td>
			         <td class=""><label class="pull-right">银行卡反面:</label></td>
			         <td><img id="photosrc" src="${bank.carddown}" alt="images" style="width: 200px;height: 100px;"/></td>
				</tr>
			</c:forEach>
			<c:forEach var="pay" items="${userCheck.payAccount}">
			<c:if test="${pay.payType ==1 }">
			    <tr>
			         <td class="active" colspan="4"><label class="pull-left">支付宝:</label></td>
				</tr>
			    <tr>
			         <td class=""><label class="pull-right">账号:</label></td>
			         <td>${pay.no}</td>
			         <td class=""><label class="pull-right">姓名:${pay.name}</label></td>
			         <td class=""><label class="pull-right">手机号:${pay.mobile}</label></td>
				</tr>
			</c:if>
			<c:if test="${pay.payType ==2}">
			    <tr>
			         <td class="active" colspan="4"><label class="pull-left">微信:</label></td>
				</tr>
			    <tr>
			         <td class=""><label class="pull-right">账号:</label></td>
			         <td>${pay.no}</td>
			         <td class=""><label class="pull-right">姓名:${pay.name}</label></td>
			         <td class=""><label class="pull-right">手机号:${pay.mobile}</label></td>
				</tr>
			</c:if>
			</c:forEach>
		   
			<c:if test="${userCheck.status == 1}">
			    <tr>
			         <td class="active"><label class="pull-right">不通过原因:</label></td>
			         <td >${userCheck.remarks}</td>
				</tr>
		    </c:if>
		</tbody>
	</table>  
	审核结果：
	<div class="form-group">
		<label>审核结果：</label>
		<select id="authBtn" onchange="changeTab(this)" name="authResult">
			<option value="1" >通过</option>
			<option value="0"  >不通过</option>
		</select>
	</div>
	<form:form id="inputForm" modelAttribute="modelFranchisee" action="${ctx}/train/userCheck/saveFranchise?opflag=syr" method="post" class="form-horizontal">
		<div id="passForm">
			<input name="userid" value="${userCheck.userid }" type="hidden">
			<input name="applyid" value="${applyid}" type="hidden">
			<input name="pageNo" type="hidden" value="${pageNo}" />
			<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
				<tbody>
				    <tr>
				    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="3"><label class="pull-left">手艺人权益设置:</label></td>
					</tr>
				    <tr>
				         <td class="active"><label class="pull-right">手艺人会员类型:</label></td>
				         <td><input id="mod_id1" class=" input-sm required" name="modid" value="4" aria-required="true" <c:if test="${modelFranchisee.modid == 4}">checked="checked"</c:if>  type="radio" onclick="setDiscount(this.value)">收费版</td>
				         <td><input id="mod_id2" class=" input-sm required" name="modid" value="3" aria-required="true" <c:if test="${modelFranchisee.modid == 3}">checked="checked"</c:if> type="radio" onclick="setDiscount(this.value)">免费版</td>
					</tr>
				    <tr>
				         <td class="active"><label class="pull-right">采购折扣:</label></td>
				         <td colspan="2"><input id="discount" class=" form-control input-sm required" name="discount" <c:if test="${modelFranchisee.modid == 3}">readonly="readonly"</c:if> value="${modelFranchisee.discount}" aria-required="true" placeholder="请输入0-1的2位小数" onkeyup="value=value.replace(/\.\d{2,}$/,value.substr(value.indexOf('.'),3))"></td>
					</tr>
				    <tr>
				         <td class="active"><label class="pull-right">授权期限:</label></td>
				         <td ><input id="auth_start_date" name="authStartDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${modelFranchisee.authStartDate}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="开始时间" readonly="readonly"/></td>
				         
				         <td ><input id="auth_end_date" name="authEndDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${modelFranchisee.authEndDate}" pattern="yyyy-MM-dd"/>" style="width:285px;" placeholder="结束时间" readonly="readonly"/></td>
				         
					</tr>
				</tbody>
			</table>  
		</div>
		<div id="refuseForm" style="display: none">
<%-- 			<input name="userid" value="${userCheck.userid}" type="hidden"> --%>
			<input name="id" value="${applyid}" type="hidden">
			<input name="status" value="1" type="hidden">
			<input name="auditType" value="${userCheck.auditType}" type="hidden">
<%-- 			<input name="pageNo" type="hidden" value="${pageNo}" /> --%>
			<label>拒绝原因：</label>
			<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
				<tbody>
				    <tr>
				        <td> <textarea name="remarks" id="reason" style="width:270px;height:170px" class="required" maxlength="50" placeholder="请输入拒绝理由（最多50个字）"></textarea></td>
					</tr>
				</tbody>
			</table> 
		</div>
	</form:form> 
</body>
</html>