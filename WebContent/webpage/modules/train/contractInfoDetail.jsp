<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> --%>
<%-- <%@ page import="com.training.modules.sys.utils.ParametersFactory"%> --%>
<html>
<head>
	<title>企业审核</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">
		/* var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if(validateForm.form()){
        	  loading('正在提交，请稍等...');
		      $("#inputForm").submit();
	     	  return true;
		  	}
		  return false;
		} */
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
			         <td colspan="5">
			         ${contractInfo.office_name }
			         </td>
		         </tr>
				<tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">管理员信息:</label></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">管理员名称:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.sign_username != 'null'}">
			         	${contractInfo.sign_username }
			         </c:if>
			         
			         </td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">管理员证件:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.sign_idcard != 'null'}">
			         	${contractInfo.sign_idcard }
			         </c:if>
			         </td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">管理员邮箱:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.sign_email != 'null'}">
			         	${contractInfo.sign_email }
			         </c:if>
			         </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">管理员手机号:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.sign_mobile != 'null' }">
			         	${contractInfo.sign_mobile }
			         </c:if>
			         </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">身份证正照:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.sign_fonturl != 'null'}">
			         <img id="photosrc" src="${contractInfo.sign_fonturl }" alt="images" style="width: 200px;height: 100px;"/>
			         </c:if>
			         </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">身份证反照:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.sign_backurl != 'null'}">
			         <img id="photosrc" src="${contractInfo.sign_backurl }" alt="images" style="width: 200px;height: 100px;"/>
			         </c:if>
			         </td>
			    </tr>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">报货人信息:</label></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">报货人名称:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.cargo_username != 'null'}">
			         	${contractInfo.cargo_username }
			         </c:if>
			         </td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">报货人证件:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.cargo_idcard != 'null' }">
			         	${contractInfo.cargo_idcard }
			         </c:if>
			         </td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">报货人邮箱:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.cargo_email != 'null'}">
						${contractInfo.cargo_email }			
					</c:if>         
			         </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">报货人手机号:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.cargo_mobile != 'null' }">
			         	${contractInfo.cargo_mobile }
			         </c:if>
			         </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">身份证正照:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.cargo_fonturl != 'null'}">
			         <img id="photosrc" src="${contractInfo.cargo_fonturl }" alt="images" style="width: 200px;height: 100px;"/>
			         </c:if>
			         </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">身份证反照:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.cargo_backurl != 'null'}">
			         <img id="photosrc" src="${contractInfo.cargo_backurl }" alt="images" style="width: 200px;height: 100px;"/>
			         </c:if>
			         </td>
			    </tr>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">审核人信息:</label></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">审核人名称:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.audit_username != 'null' }">
			         	${contractInfo.audit_username }
			         </c:if>
			         </td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">审核人证件:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.audit_idcard != 'null' }">
			         	${contractInfo.audit_idcard }
			         </c:if>
			         </td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">审核人邮箱:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.audit_email != 'null'}">
			         	${contractInfo.audit_email }
			         </c:if>
			         </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">审核人手机号:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.audit_mobile != 'null'}">
			         	${contractInfo.audit_mobile }
			         </c:if>
			         </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">身份证正照:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.audit_fonturl != 'null'}">
			         <img id="photosrc" src="${contractInfo.audit_fonturl }" alt="images" style="width: 200px;height: 100px;"/>
			         </c:if>
			         </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">身份证正反照:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.audit_backurl != 'null'}">
			         <img id="photosrc" src="${contractInfo.audit_backurl }" alt="images" style="width: 200px;height: 100px;"/>
			         </c:if>
			         </td>
			    </tr>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">代付人信息:</label></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">代付人名称:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.proxy_username != 'null' }">
			         	${contractInfo.proxy_username }
			         </c:if>
			         </td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">代付人证件:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.proxy_idcard != 'null'}">
			         	${contractInfo.proxy_idcard }
			         </c:if>
			         </td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">代付人邮箱:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.proxy_email != 'null'}">
			         	${contractInfo.proxy_email}
			         </c:if>
			         </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">代付人手机号:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.proxy_mobile != 'null'}">
			         	${contractInfo.proxy_mobile }
			         </c:if>
			         </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">身份证正照:</label></td>
			         <td colspan="5">
			         <c:if test="${contractInfo.proxy_fonturl != 'null'}">
			         <img id="photosrc" src="${contractInfo.proxy_fonturl }" alt="images" style="width: 200px;height: 100px;"/>
			         </c:if>
			         </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">身份证反照:</label></td>
			         <td colspan="5">
			         	<c:if test="${contractInfo.proxy_backurl != 'null'}">
			        	 <img id="photosrc" src="${contractInfo.proxy_backurl }" alt="images" style="width: 200px;height: 100px;"/>
			        	 </c:if>
			         </td>
			    </tr>
			    <c:if test="${!empty contractInfo.payInfos}">
			    <tr>
			    	  <td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">账户信息:</label></td>
				</tr>
				<c:forEach var="bank" items="${ contractInfo.payInfos}">
					<tr>
				         <td class="active"><label class="pull-right">账户名称:</label></td>
				         <td >${bank.pay_username }</td>
				         <td class="active"><label class="pull-right">开户银行:</label></td>
				         <td >${bank.pay_name }</td>
				    </tr>
				    <tr>
				    	<td class="active"><label class="pull-right">银行账户:</label></td>
				         <td >${bank.pay_account}</td>
				         <td class="active"><label class="pull-right">开户地址:</label></td>
				         <td >${bank.pay_mobile }</td>
				    </tr>
				    <tr>
					         <td class=""><label class="pull-right">正面:</label></td>
					         <td >
					         <c:if test="${!(bank.pay_fonturl eq null || bank.pay_fonturl eq '')}">
					         <img id="photosrc" src="${bank.pay_fonturl }" alt="images" style="width: 200px;height: 100px;"/>
					         </c:if>
					         </td>
					         <td class=""><label class="pull-right">反面:</label></td>
					         <td>
					          <c:if test="${!(bank.pay_backurl eq null || bank.pay_backurl eq '')}">
					         <img id="photosrc" src="${bank.pay_backurl }" alt="images" style="width: 200px;height: 100px;"/>
					          </c:if>
					         </td>
						</tr>
				    <tr>
			    </c:forEach>
			    </c:if>
			    <c:if test="${!(contractInfo.remarks eq null)}">
			    <tr>
			         <td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">驳回原因:</label></td>
			    </tr>
			    <tr>  
			         <td height="100px" colspan="6">
			         ${contractInfo.remarks }
			         </td>
				</tr>
				</c:if>
			</tbody>
		</table>  
	</form:form> 
</body>
</html>