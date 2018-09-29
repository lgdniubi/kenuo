<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>商家管理</title>
	<meta name="decorator" content="default"/>
	<!-- 内容上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	<link rel="stylesheet" href="${ctxStatic}/train/css/tips.css">
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		//公共商品服务标识
		function changeType(id,isyesno){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/sys/franchisee/changeType?id="+id+"&publicServiceFlag="+isyesno,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var flag = data.FLAG;
					if("OK" == flag){
						$("#isShow").html("");//清除DIV内容	
						if(isyesno == '0'){
							//当前状态为【不做】，改为做
							$("#isShow").append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeType('"+id+"','1')\">&nbsp;&nbsp;做");
						}else if(isyesno == '1'){
							//当前状态为【做】，改为不做
							$("#isShow").append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeType('"+id+"','0')\">&nbsp;&nbsp;不做");
						}
					}
					top.layer.alert(data.MESSAGE, {icon: 0, title:'提醒'}); 
				}
			}); 
		}
		//是否真实的商家
		function changeIsRealFranchisee(id,isyesno){
			if($("#franchiseeIsRealFranchisee").val() == 1){
				$(".loading").show();//打开展示层
				$.ajax({
					type : "POST",
					url : "${ctx}/sys/franchisee/changeIsRealFranchisee?id="+id+"&isRealFranchisee="+isyesno,
					dataType: 'json',
					success: function(data) {
						$(".loading").hide(); //关闭加载层
						var flag = data.FLAG;
						if("OK" == flag){
							$("#isRealFranchisee").html("");//清除DIV内容	
							if(isyesno == '0'){
								//当前状态为【否】，改为做是
								$("#isRealFranchisee").append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeIsRealFranchisee('"+id+"','1')\">&nbsp;&nbsp;否");
							}else if(isyesno == '1'){
								//当前状态为【是】，改为否
								$("#isRealFranchisee").append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeIsRealFranchisee('"+id+"','0')\">&nbsp;&nbsp;是");
							}
						}
						top.layer.alert(data.MESSAGE, {icon: 0, title:'提醒'}); 
					}
				});
			}else{
				top.layer.alert("无此操作权限!", {icon: 0, title:'提醒'}); 
			}
		}
		
		$(document).ready(function() {
			//税务登记上传
			$("#file_taxationUrl_upload").uploadify({
				'buttonText' : '请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_taxationUrl_upload',//<input type="file"/>的name
				'queueID' : 'file_taxationUrl_queue',//与下面HTML的div.id对应
				'method' : 'post',
				'fileTypeDesc': '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
				'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '10MB',//上传文件的大小限制
				'multi' : false,//设置为true时可以上传多个文件
				'auto' : true,//点击上传按钮才上传(false)
				'onFallback' : function(){
					//没有兼容的FLASH时触发
					alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
				},
				'onUploadSuccess' : function(file, data, response) { 
					var jsonData = $.parseJSON(data);//text 转 json
					if(jsonData.result == '200'){
						$("#taxationUrl").val(jsonData.file_url);
						$("#taxationUrlsrc").attr('src',jsonData.file_url); 
					}
				}
			});
			
			//营业执照图片上传
			$("#file_charterUrl_upload").uploadify({
				'buttonText' : '请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_charterUrl_upload',//<input type="file"/>的name
				'queueID' : 'file_charterUrl_queue',//与下面HTML的div.id对应
				'method' : 'post',
				'fileTypeDesc': '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
				'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '10MB',//上传文件的大小限制
				'multi' : false,//设置为true时可以上传多个文件
				'auto' : true,//点击上传按钮才上传(false)
				'onFallback' : function(){
					//没有兼容的FLASH时触发
					alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
				},
				'onUploadSuccess' : function(file, data, response) { 
					var jsonData = $.parseJSON(data);//text 转 json
					if(jsonData.result == '200'){
						$("#charterUrl").val(jsonData.file_url);
						$("#charterUrlsrc").attr('src',jsonData.file_url); 
					}
				}
			});
			
			
			//品牌logo图片上传
			$("#file_iconUrl_upload").uploadify({
				'buttonText' : '请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_iconUrl_upload',//<input type="file"/>的name
				'queueID' : 'file_iconUrl_queue',//与下面HTML的div.id对应
				'method' : 'post',
				'fileTypeDesc': '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
				'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '10MB',//上传文件的大小限制
				'multi' : false,//设置为true时可以上传多个文件
				'auto' : true,//点击上传按钮才上传(false)
				'onFallback' : function(){
					//没有兼容的FLASH时触发
					alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
				},
				'onUploadSuccess' : function(file, data, response) { 
					var jsonData = $.parseJSON(data);//text 转 json
					if(jsonData.result == '200'){
						$("#iconUrl").val(jsonData.file_url);
						$("#iconUrlsrc").attr('src',jsonData.file_url); 
					}
				}
			});
			
			//表单验证
			$("#name").focus();
			validateForm = $("#inputForm").validate({
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
		
		var lastValue = "";
		var lastId = "";
		function findUser(id){
			var msg = "更换超管后，旧超管将变为普通员工，归属机构为该商家，如果旧超管为申请人，那么员工资料请尽快补全！新超管保存后生效，是否继续保存？";
			var value = $("#"+id+"Mobile").val()
			
			 if(value =="" ){
				return;
			} 
			$(".loading").show();
			// 如果和上次一次，就退出不查了。
			if (lastValue === value && lastId == id) {
				//等待样式隐藏
				$(".loading").hide();
				return;
			}
			if (value == "") {
				//等待样式隐藏
				$(".loading").hide();
				return;
			} 
			top.layer.confirm(msg, {icon: 3, title:'系统提示'}, function(index){
				top.layer.close(index);
				// 保存最后一次
				lastValue = value;
				lastId = id;
				$.ajax({
					url:'${ctx}/sys/user/treeDataCompany?companyId=${franchisee.id}',
					type:'post',
					data:{mobile:value},
				 	dataType:'json',
				 	success:function(data){
				 		if(data.code==0){
				 			top.layer.msg("没有找到用户", {icon: 0});
							$("#"+id+"Id").val('');
					 		$("#"+id+"Name").val('');
				 		}else{
				 			$("#"+id+"Id").val(data.id);
							$("#"+id+"Name").val(data.name); 
				 		}
				 		$(".loading").hide();
				 	}
				});
				
			});
		}
		function confirms(mess){
			top.layer.confirm(mess, {icon: 3, title:'系统提示'}, function(index){
			    top.layer.close(index);
				return true;
			});
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="franchisee" action="${ctx}/sys/franchisee/save" method="post" class="form-horizontal">
		<!-- 操作是否真实的商家按钮权限 -->
		<shiro:hasPermission name="sys:franchisee:updateIsRealFranchisee"><input type="hidden" id="franchiseeIsRealFranchisee" value="1"></shiro:hasPermission>
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>归属机构:</label></td>
					<td class="width-35">
						<form:input path="parent.name" htmlEscape="false" maxlength="50" class="form-control" readonly="true" />
						<input type="hidden" id="parent.id" name="parent.id" value="${franchisee.parent.id}">
						<input type="hidden" id="parentIds" name="parentIds" value="${franchisee.parent.parentIds}">
					</td>
					<td class="width-15 active"><label class="pull-right">数据类型:</label></td>
					<td class="width-35">
						<select id="isTest" name="isTest">
							<option value="0" <c:if test="${franchisee.isTest == 0}">selected="true"</c:if> >正式数据</option>
							<option value="1" <c:if test="${franchisee.isTest == 1}">selected="true"</c:if> >测试数据</option>
						</select>
					</td>
				</tr>
				<tr>
					
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right">
							<span class="help-tip">
								<span class="help-p">最多输入30个字</span>
							</span>
						<font color="red">*</font>企业名称:</label>
					</td>
					<td class="width-35"><form:input path="name" htmlEscape="false" maxlength="30" class="form-control required" />
					</td>
					<td class="width-15 active">
						<label class="pull-right">
							<span class="help-tip">
								<span class="help-p">最多输入8个字</span>
							</span>
						<font color="red">*</font>企业简称:</label>
					</td>
					<td class="width-35"><form:input path="shortName" htmlEscape="false" maxlength="8" class="form-control" />
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>加盟类型:</label></td>
					<td class="width-35">
						<form:select path="type" class="form-control required">
							<form:options items="${fns:getDictList('sys_office_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>加盟商编码:</label>
					</td>
					<td class="width-35">
						<form:input path="code" htmlEscape="false" maxlength="50" class="form-control required" readonly="true" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>成立日期:</label>
					</td>
					<td class="width-35">
					<input name="${franchisee.setDate}" value="<fmt:formatDate value="${franchisee.setDate}" pattern="yyyy-MM-dd"/>" class="form-control calendars" readonly="readonly">
					</td>
					<td class="width-15 active"><label class="pull-right">邮政编码:</label></td>
					<td class="width-35">
						<form:input path="zipcode" htmlEscape="false" maxlength="50" class="form-control digits"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">固定电话:</label></td>
					<td class="width-35"><form:input path="tel" htmlEscape="false" maxlength="50" cssClass="form-control" /></td>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>归属区域:</label>
					</td>
					<td class="width-35">
						<sys:treeselect id="area" name="area.id"
							value="${franchisee.area.id}" labelName="area.name"
							labelValue="${franchisee.area.name}" title="区域"
							url="/sys/area/treeData" cssClass="form-control required" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active" class="active"><label class="pull-right"><font color="red">*</font>详细地址:</label></td>
					<%-- <td class="width-35">
						<sys:treeselect id="addr" name="add.id"
							value="${franchisee.area.id}" labelName="ass.name"
							labelValue="${franchisee.area.name}" title="区域"
							url="/sys/area/treeData" cssClass="form-control required" />
					</td> --%>
					<td class="width-35" colspan="3"><form:textarea path="address" htmlEscape="false" rows="3" maxlength="200" class="form-control required" /></td>
				</tr>
					
				<tr>
					<td class="width-15 active" class="active"><label class="pull-right"><font color="red"></font>品牌logo:</label></td>
					<td class="width-35">
						<img id="iconUrlsrc" src="${franchisee.iconUrl}" alt="images" style="width: 200px;height: 100px;"/>
						<input type="hidden" id="iconUrl" name="iconUrl" value="${franchisee.iconUrl}">
						<c:if test="${opflag == 'UPDATE' || opflag == 'ADD'}">
							<div class="upload">
								<input type="file" name="file_iconUrl_upload" id="file_iconUrl_upload">
							</div>
							<div id="file_iconUrl_queue"></div>
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">
						<span class="help-tip">
							<span class="help-p">默认使用企业申请人的账号，可更换归属机构在商家级别的员工</span>
						</span>
						<font color="red">*</font>
						超管手机号:</label></td>
					<%-- <td class="width-35"><form:input path="legalName" htmlEscape="false" maxlength="50" class="form-control required" />${user.id }${user.mobile}${user.name }</td> --%>
					<td class="width-35">
						<div class="input-group">
				         	<input value="${user.mobile}" id="superUserMobile" name="superUserPhone" class="form-control required" placeholder="请输入手机号">
				       		 <span class="input-group-btn">
					       		 <button type="button"  onclick="findUser('superUser')" class="btn btn-primary"><i class="fa fa-search">查询</i></button> 
				       		 </span>
					    </div>
			        </td>
					<td class="width-15 active"><label class="pull-right">超管姓名:</label></td>
					<td class="width-35">
						<input id="superUserId" name="superUserId" class="form-control required" type="hidden" value="${user.id }"/>
						<input id="oldSuperUserId" name="oldSuperUserId" class="form-control required" type="hidden" value="${user.id }"/>
						<input id="superUserName" name="superUserName" type="text" readonly="readonly" value="${user.name }" class="form-control required" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>加盟法人:</label></td>
					<td class="width-35"><form:input path="legalName" htmlEscape="false" maxlength="50" class="form-control required" /></td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>法人身份证号:</label></td>
					<td class="width-35"><form:input path="legalCard" htmlEscape="false" maxlength="18" cssClass="form-control required" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>加盟商联系人:</label></td>
					<td class="width-35"><form:input path="contacts" htmlEscape="false" maxlength="50" cssClass="form-control required" /></td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>法人手机:</label></td>
					<td class="width-35"><form:input path="mobile" htmlEscape="false" maxlength="50" cssClass="form-control required digits" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">公共商品服务标识:</label></td>
					<td class="width-35" id="isShow">
		         		<c:if test="${franchisee.publicServiceFlag == 0}">
							<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeType('${franchisee.id}',1)">&nbsp;&nbsp;做
						</c:if>
						<c:if test="${franchisee.publicServiceFlag == 1}">
							<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeType('${franchisee.id}',0)">&nbsp;&nbsp;不做
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">营业执照<br/>（照片）:</label></td>
					<td class="width-35">
						<img id="charterUrlsrc" src="${franchisee.charterUrl}" alt="images" style="width: 200px;height: 100px;"/>
						<input type="hidden" id="charterUrl" name="charterUrl" value="${franchisee.charterUrl}">
						<c:if test="${opflag == 'UPDATE' || opflag == 'ADD'}">
							<div class="upload">
								<input type="file" name="file_charterUrl_upload" id="file_charterUrl_upload">
							</div>
							<div id="file_charterUrl_queue"></div>
						</c:if>
					</td>
					<td class="width-15 active"><label class="pull-right">税务登记<br/>（照片）:</label></td>
					<td class="width-35">
						<img id="taxationUrlsrc" src="${franchisee.taxationUrl}" alt="images" style="width: 200px;height: 100px;"/>
						<input type="hidden" id="taxationUrl" name="taxationUrl" value="${franchisee.taxationUrl}">
						<c:if test="${opflag == 'UPDATE' || opflag == 'ADD'}">
							<div class="upload">
								<input type="file" name="file_taxationUrl_upload" id="file_taxationUrl_upload">
							</div>
							<div id="file_taxationUrl_queue"></div>
						</c:if>
					</td>
				</tr>
				<%-- <c:forEach var="bank" items="${franchisee.bankAccount}">
					<tr>
				         <td class="active"><label class="pull-left">账户名称:</label></td>
				         <td >${bank.accountname}</td>
				         <td class="active"><label class="pull-left">开户银行:</label></td>
				         <td >${bank.openbank}</td>
				    </tr>
				         <td class="active"><label class="pull-left">银行账户:</label></td>
				         <td >${bank.bankaccount}</td>
				    <tr>
				    </tr>
				    <tr>
				         <td class="active"><label class="pull-left">开户地址:</label></td>
				         <td >${bank.openaddress}</td>
				         <td class="active"><label class="pull-left">详细地址:</label></td>
				         <td >${bank.detailedaddress}</td>
				    </tr>
				    <tr>
					         <td class=""><label class="pull-right">身份证正面:</label></td>
					         <td ><img id="photosrc" src="${bank.cardup}" alt="images" style="width: 200px;height: 100px;"/></td>
					         <td class=""><label class="pull-right">身份证反面:</label></td>
					         <td><img id="photosrc" src="${bank.carddown}" alt="images" style="width: 200px;height: 100px;"/></td>
						</tr>
				    <tr>
			    </c:forEach>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>开户行:</label></td>
					<td class="width-35"><form:input path="bankBeneficiary" htmlEscape="false" maxlength="50" cssClass="form-control required" /></td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>持卡人姓名:</label></td>
					<td class="width-35"><form:input path="bankName" htmlEscape="false" maxlength="50" cssClass="form-control required" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>银行卡号:</label></td>
					<td class="width-35"><form:input path="bankCode" htmlEscape="false" maxlength="50" cssClass="form-control digits required" /></td>
					
				</tr> --%>
				<tr>
					<td class="width-15 active"><label class="pull-right">是否真实的商家:</label></td>
					<td class="width-35" id="isRealFranchisee">
		         		<c:if test="${franchisee.isRealFranchisee == 0}">
							<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeIsRealFranchisee('${franchisee.id}',1)">&nbsp;&nbsp;否
						</c:if>
						<c:if test="${franchisee.isRealFranchisee == 1}">
							<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeIsRealFranchisee('${franchisee.id}',0)">&nbsp;&nbsp;是
						</c:if>
					</td>
					<td class="width-15 active"><label class="pull-right">备注:</label></td>
					<td class="width-35"><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control" /></td>
				</tr>
				
			</tbody>
		</table>
	</form:form>
	<div class="loading"></div>
</body>
</html>