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
        	  loading('正在提交，请稍等...');
		      $("#inputForm").submit();
	     	  return true;
		  	}
		  return false;
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="userCheck" action="${ctx}/train/userCheck/save" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">企业信息:</label></td>
				</tr>
			    <tr>
			         <td width="100px" class="active"><label class="pull-right">机构名称:</label></td>
			         <td colspan="5">${contractInfo.office_name }</td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">同一社会信用代码/注册号:</label></td>
			         <td colspan="5">${contractInfo.office_no }</td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">企业类型:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.office_type eq '0'}">个体</c:if>
			         <c:if test="${contractInfo.office_type eq '1'}">合伙企业</c:if>
			         <c:if test="${contractInfo.office_type eq '2'}">个人独资企业</c:if>
			         <c:if test="${contractInfo.office_type eq '3'}">公司</c:if>
			         </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">营业执照:</label></td>
			         <td colspan="5">${contractInfo.office_license }</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">营业地址:</label></td>
			         <td colspan="5">${contractInfo.office_address }</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">法人:</label></td>
			         <td colspan="5">${contractInfo.office_legalman }</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">法人证件前照:</label></td>
			         <td colspan="5">${contractInfo.office_fonturl }</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">法人证件反照:</label></td>
			         <td colspan="5">${contractInfo.office_backurl }</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">绑定银行:</label></td>
			         <td colspan="5">${contractInfo.office_bank }</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">账户名称:</label></td>
			         <td colspan="5">${contractInfo.office_accountname }</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">银行卡号:</label></td>
			         <td colspan="5">${contractInfo.office_account }</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">银行卡前照:</label></td>
			         <td colspan="5">${contractInfo.office_fontbank }</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">银行卡反照:</label></td>
			         <td colspan="5">${contractInfo.office_backbank }</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">成立日期:</label></td>
			         <td colspan="5">${contractInfo.office_date }</td>
				</tr>
				<tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">管理员信息:</label></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">管理员名称:</label></td>
			         <td colspan="5">${contractInfo.sign_username }</td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">管理员证件:</label></td>
			         <td colspan="5">${contractInfo.sign_idcard }</td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">管理员邮箱:</label></td>
			         <td colspan="5">${contractInfo.sign_email }</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">管理员手机号:</label></td>
			         <td colspan="5">${contractInfo.sign_mobile }</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">证件前照:</label></td>
			         <td colspan="5">${contractInfo.sign_fonturl }</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-left">证件反照:</label></td>
			         <td colspan="5">${contractInfo.sign_backurl }</td>
			    </tr>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">报货人信息:</label></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">报货人名称:</label></td>
			         <td colspan="5">${contractInfo.cargo_username }</td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">报货人证件:</label></td>
			         <td colspan="5">${contractInfo.cargo_idcard }</td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">报货人邮箱:</label></td>
			         <td colspan="5">${contractInfo.cargo_email }</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">报货人手机号:</label></td>
			         <td colspan="5">${contractInfo.cargo_mobile }</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">证件前照:</label></td>
			         <td colspan="5">${contractInfo.cargo_fonturl }</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-left">证件反照:</label></td>
			         <td colspan="5">${contractInfo.cargo_backurl }</td>
			    </tr>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">审核人信息:</label></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">审核人名称:</label></td>
			         <td colspan="5">${contractInfo.audit_username }</td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">审核人证件:</label></td>
			         <td colspan="5">${contractInfo.audit_idcard }</td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">审核人邮箱:</label></td>
			         <td colspan="5">${contractInfo.audit_email }</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">审核人手机号:</label></td>
			         <td colspan="5">${contractInfo.audit_mobile }</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">证件前照:</label></td>
			         <td colspan="5">${contractInfo.audit_fonturl }</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-left">证件反照:</label></td>
			         <td colspan="5">${contractInfo.audit_backurl }</td>
			    </tr>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">代付人信息:</label></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">代付人名称:</label></td>
			         <td colspan="5">${contractInfo.proxy_username }</td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">代付人证件:</label></td>
			         <td colspan="5">${contractInfo.proxy_idcard }</td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">代付人邮箱:</label></td>
			         <td colspan="5">${contractInfo.proxy_email }</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">代付人手机号:</label></td>
			         <td colspan="5">${contractInfo.proxy_mobile }</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">证件前照:</label></td>
			         <td colspan="5">${contractInfo.proxy_fonturl }</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-left">证件反照:</label></td>
			         <td colspan="5">${contractInfo.proxy_backurl }</td>
			    </tr>
			    <tr>
			    	  <td class="active" colspan="6"><label class="pull-left">账户信息</label></td>
				</tr>
				<c:forEach var="bank" items="payInfos">
					<tr>
				         <td class="active"><label class="pull-left">账户名称:</label></td>
				         <td ></td>
				         <td class="active"><label class="pull-left">开户银行:</label></td>
				         <td ></td>
				         <td class="active"><label class="pull-left">银行账户:</label></td>
				         <td ></td>
				    </tr>
				    <tr>
				         <td class="active"><label class="pull-left">开户地址:</label></td>
				         <td ></td>
				         <td class="active"><label class="pull-left">详细地址:</label></td>
				         <td ></td>
				    </tr>
				    <tr>
					         <td class=""><label class="pull-right">正面:</label></td>
					         <td ><img id="photosrc" src="" alt="images" style="width: 200px;height: 100px;"/></td>
					         <td class=""><label class="pull-right">反面:</label></td>
					         <td><img id="photosrc" src="" alt="images" style="width: 200px;height: 100px;"/></td>
						</tr>
				    <tr>
			    </c:forEach>
			         <td height="100px" class="active"><label class="pull-left">营业执照:</label></td>
			    	 <td colspan="5"><img id="photosrc" src="" alt="images" style="width: 200px;height: 100px;"/></td>
			    </tr>
			    <tr>
			         <td class="active" colspan="6"><label class="pull-left">企业介绍:</label></td>
			    </tr>
			    <tr>  
			         <td height="100px"></td>
			         <td colspan="5"></td>
				</tr>
				<c:if test="">
				    <tr>
				         <td class="active" colspan="6"><label class="pull-left">不通过原因:</label></td>
				    </tr>
				    <tr>  
				         <td height="100px" colspan="6"></td>
					</tr>
				</c:if>
			</tbody>
		</table>  
	</form:form> 
</body>
</html>