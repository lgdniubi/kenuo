<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> --%>
<%-- <%@ page import="com.training.modules.sys.utils.ParametersFactory"%> --%>
<html>
<head>
	<title>企业审核</title>
	<meta name="decorator" content="default"/>
	<!-- 放大图片js -->
	<script type="text/javascript" src="${ctxStatic}/train/imgZoom/jquery.imgZoom.js"></script>
	
	<script type="text/javascript">
		var validateForm;
		var validRefForm;
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
			
			//validRefForm = $("#refuseForm").validate({ });
			
		});
		
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
	<style>
	html, body{height: auto !important}
	</style>
	<sys:message content="${message}"/>
	<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		<tbody>
		    <tr>
		    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">申请人基本信息:</label></td>
			</tr>
		    <tr>
		         <td width="100px" class="active"><label class="pull-right">电话:</label></td>
		         <td>${userCheck.mobile}</td>
		         <td class="active"><label class="pull-right">姓名:</label></td>
		         <td>${userCheck.name}</td>
		         <td class="active"><label class="pull-right">昵称:</label></td>
		         <td>${userCheck.nickname}</td>
			</tr>
		    <tr>
		         <td class="active"><label class="pull-right">申请时间:</label></td>
		         <td colspan="5"><fmt:formatDate value="${userCheck.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			</tr>
		    <tr>
		         <td class="active"><label class="pull-right">当前会员类型:</label></td>
		         <td colspan="5">
		         	<c:if test="${userCheck.applyType eq 'pt'}">普通会员</c:if>
					<c:if test="${userCheck.applyType eq 'syr'}">手艺人</c:if>
					<c:if test="${userCheck.applyType eq 'qy'}">企业</c:if>
				 </td>
			</tr>
		    <tr>
		         <td class="active"><label class="pull-right">申请会员类型:</label></td>
		         <td colspan="5">
				 	<c:if test="${userCheck.auditType eq 'syr'}">手艺人</c:if>
					<c:if test="${userCheck.auditType eq 'qy'}">企业</c:if>
				 </td>
			</tr>
			<tr>
		    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">申请资料:</label></td>
			</tr>
		    <tr>
		         <td class="active"><label class="pull-right">企业名称:</label></td>
		         <td>${userCheck.companyName}</td>
		         <td class="active"><label class="pull-right">企业简称:</label></td>
		         <td>${userCheck.shortName}</td>
		         <td class="active"><label class="pull-right">执照编号:</label></td>
		         <td>${userCheck.charterCard}</td>
			</tr>
		    <tr>
		         <td class="active"><label class="pull-right">企业类型:</label></td>
		         <td>
		         	<c:if test="${userCheck.addr.type == 1}">个体户</c:if>
		         	<c:if test="${userCheck.addr.type == 2}">合伙企业</c:if>
		         	<c:if test="${userCheck.addr.type == 3}">个人独资企业</c:if>
		         	<c:if test="${userCheck.addr.type == 4}">公司</c:if>
		         </td>
		         <td class="active"><label class="pull-right">成立日期:</label></td>
		         <td><fmt:formatDate value="${userCheck.addr.setDate}" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
		         <td class="active"><label class="pull-right">所在地区:</label></td>
		         <td>${userCheck.addr.provinceName}${userCheck.addr.cityName}${userCheck.addr.districtName}</td>
			</tr>
		    <tr>
		         <td class="active"><label class="pull-right">详细地址:</label></td>
		         <td>${userCheck.address}</td>
		         <td class="active"><label class="pull-right">企业法人:</label></td>
		         <td>${userCheck.legalPerson}</td>
		         <td class="active"><label class="pull-right">法人手机:</label></td>
		         <td>${userCheck.legalMobile}</td>
			</tr>
		    <tr>
		         <td class="active"><label class="pull-left">身份证号:</label></td>
		         <td colspan="5">${userCheck.legalCard}</td>
		    </tr>
		    <tr>
		         <td class="active" colspan="6"><label class="pull-left">法人身份证:</label></td>
		    </tr>
		    <tr>
		         <td height="100px" colspan="3"><img id="photosrc" src="${userCheck.icardone}" class='imgZoom' alt="images" style="width: 200px;height: 100px;"/></td>
		         <td  colspan="3"><img id="photosrc" src="${userCheck.icardtwo}" alt="images" class='imgZoom' style="width: 200px;height: 100px;"/></td>
			</tr>
		    <tr>
		    	  <td class="active" colspan="6"><label class="pull-left">账户信息</label></td>
			</tr>
			<c:forEach var="bank" items="${userCheck.bankAccount}">
				<tr>
			         <td class="active"><label class="pull-left">账户名称:</label></td>
			         <td >${bank.accountname}</td>
			         <td class="active"><label class="pull-left">开户银行:</label></td>
			         <td >${bank.openbank}</td>
			         <td class="active"><label class="pull-left">银行账户:</label></td>
			         <td >${bank.bankaccount}</td>
			    </tr>
			    <tr>
			         <td class="active"><label class="pull-left">开户地址:</label></td>
			         <td >${bank.openaddress}</td>
			         <td class="active"><label class="pull-left">详细地址:</label></td>
			         <td >${bank.detailedaddress}</td>
			    </tr>
			    <tr>
				         <td class=""><label class="pull-right">银行卡正面:</label></td>
				         <td ><img id="photosrc" src="${bank.cardup}" class='imgZoom' alt="images" style="width: 200px;height: 100px;"/></td>
				         <td class=""><label class="pull-right">银行卡反面:</label></td>
				         <td><img id="photosrc" src="${bank.carddown}" class='imgZoom' alt="images" style="width: 200px;height: 100px;"/></td>
					</tr>
			    <tr>
		    </c:forEach>
		         <td height="100px" class="active"><label class="pull-left">营业执照:</label></td>
		    	 <td colspan="5"><img id="photosrc" src="${userCheck.charterUrl}" class='imgZoom' alt="images" style="width: 200px;height: 100px;"/></td>
		    </tr>
		    <tr>
		         <td class="active" colspan="6"><label class="pull-left">企业介绍:</label></td>
		    </tr>
		    <tr>  
		         <td height="100px"></td>
		         <td colspan="5">${userCheck.intro}</td>
			</tr>
			<c:if test="${userCheck.status == 1}">
			    <tr>
			         <td class="active" colspan="6"><label class="pull-left">不通过原因:</label></td>
			    </tr>
			    <tr>  
			         <td height="100px" colspan="6">${userCheck.remarks}</td>
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
	<div id="authForm">
		<form:form id="inputForm" modelAttribute="modelFranchisee" action="${ctx}/train/userCheck/saveFranchise?opflag=qy" method="post" class="form-horizontal">
			<sys:message content="${message}"/>
			<div id="passForm">
				<input name="userid" value="${userCheck.userid}" type="hidden">
				<input name="applyid" value="${applyid}" type="hidden">
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
					         <td><input id="paytype1" class=" input-sm required" name="paytype" value="1" aria-required="true" <c:if test="${modelFranchisee.paytype == 1}">checked="checked"</c:if> type="radio">在线支付</td>
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
			</div> 
			<div id="refuseForm" style="display: none">
<%-- 				<input name="userid" value="${userCheck.userid}" type="hidden"> --%>
				<input name="id" value="${applyid}" type="hidden">
				<input name="status" value="1" type="hidden">
				<input name="auditType" value="${userCheck.auditType}" type="hidden">
<%-- 				<input name="pageNo" type="hidden" value="${pageNo}" /> --%>
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
	</div>
	<script type="text/javascript">
		//点击放大图片
		$(".imgZoom").imgZoom();
	</script>
</body>
</html>