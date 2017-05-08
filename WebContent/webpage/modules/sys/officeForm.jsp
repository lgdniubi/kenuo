<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>机构管理</title>
	<meta name="decorator" content="default"/>
	 <!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
			unfold($("#grade").val());
			$("#name").focus();
			validateForm = $("#inputForm").validate({
				rules: {
					name: {
						remote: {
			            	url:"${ctx}/sys/office/verifyName",
		            		type: "get",
		                    data: {
		                    	'id' : function(){return $("#officeId").val();},
		                        'name': function(){return $("input[name='name']").val();},
			            		'oldOfficeName': function(){return $("#oldOfficeName").val();}
		                    }
						 }
					}//设置了远程验证，在初始化时必须预先调用一次。
				},
				messages:{
					name: {remote: "机构名称已存在"}
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
			
			//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
			//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
			//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
			$("#inputForm").validate().element($("#name"));
			
			$("#file_img_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_img_upload',//<input type="file"/>的name
				'queueID' : 'file_img_queue',//与下面HTML的div.id对应
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
						$("#img").val(jsonData.file_url);
						$("#imgsrc").attr('src',jsonData.file_url); 
					}
				}
			});
		});
		function unfold(num){
			//num = 1 不是店铺  num = 2 为店铺
			if(num == 1){
				$("#unfold,#area1").show();
				$("#a1,#a2,#a3,#a4,#a5,#area2").hide();
			}else{
				$("#unfold,#area1").hide();
				$("#a1,#a2,#a3,#a4,#a5,#area2").show();
			}
		}
		function changeTableVal(id,status){
			if($("#officeStatus").val() == 1){
				$(".loading").show();//打开展示层
				$.ajax({
					type : "POST",
					url : "${ctx}/sys/office/updateOfficeStatus?id="+id+"&status="+status,
					dataType: 'json',
					success: function(data) {
						$(".loading").hide(); //关闭加载层
						var flag = data.FLAG;
						if("OK" == flag){
							$("#status").html("");//清除DIV内容	
							if(status == '1'){
								//当前状态为【否】，则打开
								$("#status").append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+id+"','0')\">&nbsp;&nbsp;隐藏状态");
							}else if(status == '0'){
								//当前状态为【是】，则取消
								$("#status").append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+id+"','1')\">&nbsp;&nbsp;正常状态");
							}
						}
						top.layer.alert(data.MESSAGE, {icon: 0, title:'提醒'}); 
					}
				});   
			}else{
				top.layer.alert("无此操作权限!", {icon: 0, title:'提醒'}); 
			}
		}
	</script>
</head>
<body>
	<div class="ibox-content">
		<form:form id="inputForm" modelAttribute="office" action="${ctx}/sys/office/save" method="post" class="form-horizontal">
			<!-- 操作隐藏店铺按钮权限 -->
			<shiro:hasPermission name="sys:office:updateOfficeStatus">
				<input type="hidden" id="officeStatus" value="1">
			</shiro:hasPermission>
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
				<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
				   <tbody>
						<tr><td colspan="4" class="active"><label class="pull-left">基本信息</label></td></tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>所属商家:</label></td>
				         	<td class="width-35">
				         		<input value="${a }" class="form-control" readonly="readonly">
				         	</td>
				         	<td class="width-15 active"><label class="pull-right">机构类型:</label></td>
					        <td class="width-35">
					         	<form:hidden path="type"/>
					         	<form:select path="type" id="type" class="form-control" disabled="true">
									<form:options items="${fns:getDictList('sys_office_type')}" itemLabel="label" itemValue="value" htmlEscape="false" disabled="true"/>
								</form:select>
							</td>
						</tr>
				      	<tr>
					         <td class="width-15 active"><label class="pull-right">上级机构:</label></td>
					         <td class="width-35"><sys:treeselect id="office" name="parent.id" value="${office.parent.id}" labelName="parent.name" labelValue="${office.parent.name}"
								title="机构" url="/sys/office/treeData?isGrade=true" extId="${office.id}"  cssClass="form-control" allowClear="${office.currentUser.admin}"/></td>
					         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>归属区域:</label></td>
					         <td class="width-35">
						        <div id="area1">
							         <sys:treeselect id="area" name="area.id" value="${office.area.id}" labelName="area.name" labelValue="${office.area.name}" 
							         title="区域" url="/sys/area/treeData" cssClass="area1 form-control required" allowClear="true" notAllowSelectParent="true"/>
						         </div>
						         <div id="area2"> 
							         <sys:treeselect id="areaInfo" name="areaInfo.id" value="${office.area.id}" labelName="areaInfo.name" labelValue="${office.area.name}" 
							         title="区域" url="/sys/area/treeData" cssClass="area2 form-control required"/>
						         </div>
					         </td>
				       </tr>
				       <tr>
				         <td class="width-15 active"><label class="pull-right"><font color="red">*</font>机构名称:</label></td>
				         <td class="width-35"><input id="oldOfficeName" value="${office.name }" type="hidden"><input id="name" name="name" value="${office.name }" class="form-control required"></td>
				         <td  class="width-15 active"><label class="pull-right">机构编码:</label></td>
				         <td class="width-35"><form:input path="code" htmlEscape="false" maxlength="50" class="form-control" readonly="true"/></td>
				      </tr>
				      <tr>
				         <td class="width-15 active"><label class="pull-right">是否可用:</label></td>
				         <td class="width-35"><form:select path="useable" class="form-control">
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" cssClass="form-control"/>
							</form:select>
							<span class="help-inline">“是”代表此账号允许登陆，“否”则表示此账号不允许登陆</span></td>
				         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>是否为店铺:</label></td>
				         <td class="width-35">
							<c:if test="${office.num == '0'}">
					         	<form:select path="grade" id="grade" cssClass="form-control" onchange="unfold(this.options[this.options.selectedIndex].value)">
									<form:options items="${fns:getDictList('sys_office_grade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
								<span class="help-inline"><font color="red">注：更改此类时请重新选择归属区域</font></span>
							</c:if>
							<c:if test="${office.num != '0'}">
								<form:hidden path="grade"/>
								<form:select path="grade" id="grade" cssClass="form-control" onchange="unfold(this.options[this.options.selectedIndex].value)" disabled="true">
									<form:options items="${fns:getDictList('sys_office_grade')}" itemLabel="label" itemValue="value" htmlEscape="false" disabled="true"/>
								</form:select>
								<span class="help-inline">存在子类时不可改为是店铺</span>
							</c:if>
						</td>
				     </tr>
				     <tr id="a5">
				         <td class="width-15 active"><label class="pull-right">主负责人:</label></td>
				         <td class="width-35"><sys:treeselect id="primaryPerson" name="primaryPerson.id" value="${office.primaryPerson.id}" labelName="office.primaryPerson.name" labelValue="${office.primaryPerson.name}"
							title="用户" url="/sys/office/treeData?type=3" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/></td>
						 <td class="width-15 active"><label class="pull-right">副负责人:</label></td>
				         <td class="width-35"><sys:treeselect id="deputyPerson" name="deputyPerson.id" value="${office.deputyPerson.id}" labelName="office.deputyPerson.name" labelValue="${office.deputyPerson.name}"
							title="用户" url="/sys/office/treeData?type=3" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/></td>
				     </tr>
				     <tr id="a1">
				         <td  class="width-15 active"><label class="pull-right">负责人:</label></td>
				         <td class="width-35"><form:input path="master" htmlEscape="false" maxlength="50" cssClass="form-control" /></td>
				         <td class="width-15 active"><label class="pull-right">联系地址:</label></td>
				         <td class="width-35"><form:input path="address" htmlEscape="false" maxlength="50" cssClass="form-control" /></td>
				     </tr>
				     <tr id="a2">
				     	 <td class="width-15 active"><label class="pull-right">邮箱:</label></td>
				         <td class="width-35"><form:input path="email" htmlEscape="false" maxlength="50" cssClass="form-control" /></td>
				         <td class="width-15 active"><label class="pull-right">邮政编码:</label></td>
				         <td class="width-35"><form:input path="zipCode" htmlEscape="false" maxlength="50" cssClass="form-control" /></td>
				     </tr>
				     <tr id="a3">
				         <td class="width-15 active"><label class="pull-right">电话:</label></td>
				         <td class="width-35"><form:input path="phone" htmlEscape="false" maxlength="50" cssClass="form-control" /></td>
				         <td  class="width-15 active"><label class="pull-right">传真:</label></td>
				         <td class="width-35"><form:input path="fax" htmlEscape="false" maxlength="50" cssClass="form-control" /></td>
				     </tr>
				     <tr id="a4">
				         <td  class="width-15 active"><label class="pull-right">备注:</label></td>
				         <td class="width-35"  colspan="3"><form:textarea path="remarks" htmlEscape="false" rows="3" cols="30" maxlength="200" class="form-control"/></td>
				     </tr>
			      </tbody>
			      <tbody id="unfold">
			      	 <tr><td colspan="4" class="active"><label class="pull-left">店铺详细信息</label></td></tr>
			      	  <tr>
				      	 <td  class="width-15 active"><label class="pull-right">店铺首图:</label></td>
				         <td class="width-35">
				         	<img id="imgsrc" src="${office.officeInfo.img}" alt="" style="width: 200px;height: 100px;"/>
							<input type="hidden" id="img" name="officeInfo.img" value="${office.officeInfo.img}"><!-- 图片隐藏文本框 -->
							<p>&nbsp;</p>
		                   	<div class="upload">
								<input type="file" name="file_img_upload" id="file_img_upload">
							</div>
							<div id="file_img_queue"></div>
				         </td>
				         <td class="width-15 active"><label class="pull-right"><font color="red">*</font>店铺短名称：</label></td>
				         <td class="width-35"><form:input path="officeInfo.shortName" htmlEscape="false" maxlength="50" cssClass="form-control required" /></td>
				      </tr>
				     <tr>
				         <td class="width-15 active"><label class="pull-right"><font color="red">*</font>邮政编码:</label></td>
				         <td class="width-35"><form:input path="officeInfo.postalCode" htmlEscape="false" maxlength="50" cssClass="form-control required" onkeyup="this.value=this.value.replace(/\D/g,'')"/></td>
				         <td class="width-15 active"><label class="pull-right"><font color="red">*</font>店长/联系人：</label></td>
				         <td class="width-35"><form:input path="officeInfo.contacts" htmlEscape="false" maxlength="50" cssClass="form-control required" /></td>
				     </tr>
				     <tr>
				         <td class="width-15 active"><label class="pull-right"><font color="red">*</font>店铺电话：</label></td>
				         <td class="width-35"><form:input path="officeInfo.storePhone" htmlEscape="false" cssClass="form-control required"/></td>
				         <td class="width-15 active"><label class="pull-right"><font color="red">*</font>店长电话：</label></td>
				         <td class="width-35"><form:input path="officeInfo.telephone" htmlEscape="false" cssClass="form-control required"/></td>
				     </tr>
			      	 <tr>
				         <td class="width-15 active"><label class="pull-right"><font color="red">*</font>工作时间:</label></td>
				         <td class="width-35" colspan="3">
				         	<form:input path="officeInfo.beginTime" htmlEscape="false" maxlength="50" cssClass="datetimepicker form-control required" placeholder="上班时间"/>
				         	--
				         	<form:input path="officeInfo.endTime" htmlEscape="false" maxlength="50" cssClass="datetimepicker form-control required" placeholder="下班时间"/>
				         </td>
				      </tr>
				      <tr>
				      	 <td class="width-15 active"><label class="pull-right"><font color="red">*</font>床位：</label></td>
				         <td class="width-35"><form:input path="officeInfo.bedNum" htmlEscape="false" maxlength="5" cssClass="form-control digits required"/></td>
						 <td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否新店：</label></td>
						 <td>
							<select class="form-control required" id="isNew" name="isNew">
								<option value='0' <c:if test="${office.isNew == '0'}">selected</c:if>>否</option>
								<option value='1' <c:if test="${office.isNew == '1'}">selected</c:if>>是</option>
							</select>
						 </td>
				      </tr>
				      <tr>
				      	 <td class="width-15 active"><label class="pull-right"><font color="red">*</font>店铺状态：</label></td>
				         <td class="width-35" id="status">
				         	<c:if test="${not empty office.id }">
				         		<c:if test="${office.officeInfo.status == 1}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('${office.id}',0)">&nbsp;&nbsp;隐藏状态
								</c:if>
								<c:if test="${office.officeInfo.status == 0}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('${office.id}',1)">&nbsp;&nbsp;正常状态
								</c:if>
				         	</c:if>
				         	<c:if test="${empty office.id }">
				         		<select id="officeInfo.status" name="officeInfo.status" class="form-control">
				         			<option value="0">正常</option>
				         			<option value="1">隐藏</option>
				         		</select>
				         	</c:if>
				         </td>
				      	 <td class="width-15 active"><label class="pull-right"><font color="red">*</font>详细地址:</label></td>
				         <td class="width-35" colspan="3"><form:textarea path="officeInfo.detailedAddress" htmlEscape="false" rows="3" cols="30" maxlength="200" style="width: 100%" class="form-control required"/></td>
				      </tr>
			      </tbody>
		      </table>
		</form:form>
	</div>
	<script type="text/javascript" src="${ctxStatic}/train/js/jquery.datetimepicker.js"></script>
	<script>
	    // 选取时间
	    $('.datetimepicker').datetimepicker({
			datepicker:false,
			format:'H:i',
			step:30
		});
	</script>
</body>
</html>