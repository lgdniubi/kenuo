<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
<title>用户管理</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/train/uploadify/uploadify.css">
<script type="text/javascript"
	src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
<!-- 时间控件 -->
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<!-- 图片上传 引用-->
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/ec/css/custom_uploadImg.css">
<script type="text/javascript">
		var validateForm;
		function verify(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function(){
			//用户头像上传
			$("#file_user_upload").uploadify({
				'buttonText' : '添加照片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
			'fileObjName' : 'file_user_upload',//<input type="file"/>的name
			'queueID' : 'file_category_queue',//与下面HTML的div.id对应
			'method' : 'post',
			'fileTypeDesc' : '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
			'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
			'fileSizeLimit' : '10MB',//上传文件的大小限制
			'multi' : false,//设置为true时可以上传多个文件
			'auto' : true,//点击上传按钮才上传(false)
			'onFallback' : function() {
				//没有兼容的FLASH时触发
				alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
			},
			'onUploadSuccess' : function(file, data, response) {
				var jsonData = $.parseJSON(data);//text 转 json
				if (jsonData.result == '200') {
					$("#specitemimg").val(jsonData.file_url);
					$("#specitemimgsrc").show();
					$("#specitemimgsrc").attr('src', jsonData.file_url);
				}
			}
		});
		validateForm = $("#inputForm").validate({
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

	//点击商品规格图片按钮，添加商品图片
	var specitemupload = function(v) {
		$("#itemimgid").val(v);
		var itemimgid = $('#itemimgid').val();
		if (!$.trim(itemimgid) == '') {
			$("#Wrap").show();
			$("#specitemimgsrc").hide();
		} else {
			alert("比较参数为空");
		}
	}

	//保存商品规格图片
	var saveuploadImg = function() {
		var itemimgid = $('#itemimgid').val();
		var path = $("#specitemimgsrc")[0].src;
	   if (!$.trim(itemimgid) == '' && !$.trim(path) == '') {
		$("#item_img_" + itemimgid).find("img").attr('src', path);
		if(itemimgid == 'user'){
			$("#item_img_" + itemimgid).find("img").css("width", 300);
		}else{
			$("#item_img_" + itemimgid).find("img").css("width", 150);
		}
		if(itemimgid == 'user'){
			$("#item_img_" + itemimgid).find("input[name='photo']").val(path);
		}else{
			$("#item_img_" + itemimgid).find("input[name*='imgUrl']").val(path);
		}
		$("#Wrap").hide();
		$("#specitemimgsrc").attr('src', '');
	   }
	}

	//关闭上传图片按钮-清楚图片内容
	var closeuploadImg = function() {
		$("#specitemimgsrc").attr('src', '');
		$("#Wrap").hide();
	}

	function removeTr(td){
		$(td).parent().parent().prev("tr").remove();
		$(td).parent().parent().remove();
	}
</script>
<style type="text/css">
#mtable{border:1px solid #ccc;}
#mtable td{border:1px solid #ccc;}
</style>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>添加档案</h5>
				<h5 style="padding-left: 92%; margin-top: -10px;">
					<a href="${ctx}/personnelfile/user/list">
						<button type="button" class="btn btn-info">返回</button>
					</a>
				</h5>
			</div>
			<form:form id="inputForm" modelAttribute="personnelFile"
				action="${ctx}/personnelfile/user/save" method="post" class="form-horizontal">
				<form:hidden path="id" />
				<input type="hidden" name="filing" value="1" />
				<table class="table table-condensed dataTables-example dataTable no-footer" id='mtable'>
					
						<tr>
							<td colspan="6" style="background-color: #5553;"><label class="pull-left">基本信息</label></td>
						</tr>
						<tr>
							<td><label class="pull-right">登录名：</label></td>
							<td>${personnelFile.loginName}</td>
							<td style="width: 187px;"><label class="pull-right">姓名：</label></td>
							<td>${personnelFile.sName}</td>
							<td rowspan="8" style="width: 100px;" colspan="2" align="center">
								<span id="item_img_user" style="margin-right: 8px;">
									<img src="/kenuo/static/ec/images/tianjia.jpg" onclick="specitemupload('user')" />
									<input type="hidden" value="" name="photo" />
								</span>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">归属机构：</label></td>
							<td>${personnelFile.soName}</td>
							<td><label class="pull-right">角色：</label></td>
							<td>
								<c:forEach items="${personnelFile.rolelist}" var="role">
									${role.name}
								</c:forEach>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">员工编号：</label></td>
							<td>
								${personnelFile.no}
							</td>
							<td class="width-5"><label class="pull-right">身份证号：</label></td>
							<td class="">
								${personnelFile.idcard}
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>年龄：</label></td>
							<td><form:input path="userBaseInfo.age" htmlEscape="false"
									maxlength="10" class="form-control  max-width-250  required digits" />
							</td>
							<td><label class="pull-right"><font color="red">*</font>性别：</label></td>
							<td>
								<form:select path="sex"  class="form-control" style="width:180px">
									<form:option value="0">男</form:option>
									<form:option value="1">女</form:option>
								</form:select>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>出生日期：</label></td>
							<td>
								<input id="birthday" name="userBaseInfo.birthday" class="Wdate form-control layer-date input-sm" style="height: 30px;width: 200px" type="text" 
									value="<fmt:formatDate value="${userEmployed.probationStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
							</td>
							<td><label class="pull-right"><font color="red">*</font>民族：</label></td>
							<td><form:input path="userBaseInfo.nation" htmlEscape="false"
									maxlength="10" class="form-control  max-width-250 required" />
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>身高：</label></td>
							<td><form:input path="userBaseInfo.stature" htmlEscape="false"
									maxlength="10" class="form-control  max-width-250 required" />
							</td>
							<td><label class="pull-right">血型：</label></td>
							<td><form:input path="userBaseInfo.bloodType" htmlEscape="false"
									maxlength="10" class="form-control  max-width-250" />
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">政治面貌：</label></td>
							<td><form:input path="userBaseInfo.party" htmlEscape="false"
									maxlength="10" class="form-control  max-width-250" />
							</td>
							<td><label class="pull-right"><font color="red">*</font>婚姻状况：</label></td>
							<td>
								<select class="form-control max-width-150" name="maritalStatus">
									<option value="0">已婚</option>
									<option value="1">未婚</option>
									<option value="2">离异</option>
								</select>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">入党时间：</label></td>
							<td>
								<input id="joinParty" name="userBaseInfo.joinParty" class="Wdate form-control layer-date input-sm" style="height: 30px;width: 200px" type="text" 
									value="<fmt:formatDate value="${userEmployed.probationStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">身份证正面照：</label></td>
							<td>
								<span id="item_img_zm" style="margin-right: 8px;"> 
									<img src="/kenuo/static/ec/images/zhengmian.jpg"  onclick="specitemupload('zm')" /> 
									<input type="hidden" value="" name="imgList[1].imgUrl" />
								    <input type="hidden" value="1" name="imgList[1].imgType" />
								</span>
							</td>
							<td><label class="pull-right">身份证反面照：</label></td>
							<td>
								<span id="item_img_fm" style="margin-right: 8px;"> 
									<img src="/kenuo/static/ec/images/fanmian.jpg"  onclick="specitemupload('fm')" /> 
									<input type="hidden" value="" name="imgList[2].imgUrl" />
								    <input type="hidden" value="2" name="imgList[2].imgType" />
								</span>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">照片：</label></td>
							<td colspan="5">
								<span id="item_img_shz"	style="margin-right: 8px;"> 
									<img src="/kenuo/static/ec/images/shenghuo.jpg" onclick="specitemupload('shz')" /> 
									<input type="hidden" value="" name="imgList[3].imgUrl" />
									<input type="hidden" value="3" name="imgList[3].imgType" />
								</span> 
								<span id="item_img_gzz" style="margin-right: 8px;"> 
									<img src="/kenuo/static/ec/images/gongzuo.jpg" onclick="specitemupload('gzz')" /> 
									<input type="hidden" value="" name="imgList[4].imgUrl" />
									<input type="hidden" value="4" name="imgList[4].imgType" />
								</span> 
							<span id="item_img_dphz" style="margin-right: 8px;"> 
								<img src="/kenuo/static/ec/images/dianpu.jpg" onclick="specitemupload('dphz')" /> 
								<input type="hidden" value="" name="imgList[5].imgUrl" />
								<input type="hidden" value="5" name="imgList[5].imgType" />
							</span>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>户籍类别：</label></td>
							<td>
								<select class="form-control max-width-150" name="userBaseInfo.registerType">
										<option value="0">农业</option>
										<option value="1">非农业</option>
								</select>
							</td>
							<td><label class="pull-right"><font color="red">*</font>户籍所在地：</label></td>
							<td colspan="5">
								<div style="width:200px;float:left">
									<sys:treeselect id="registerSite" name="oneArea.id"
									value="" labelName="oneArea.name"
									labelValue="" title="区域"
									url="/sys/area/treeData" cssClass="form-control required" />
								</div>
								<div style="float:left;margin-left:10px;width:350px;">
									<form:input path="userBaseInfo.registerSite1" htmlEscape="false" maxlength="50" class="form-control  max-width-500 required" name="registerSite1" />
								</div>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>现居住地：</label></td>
							<td colspan="5">
								<div style="width:200px;float:left">
									<sys:treeselect id="dwelling" name="twoArea.id"
										value="" labelName="twoArea.name"
										labelValue="" title="区域"
										url="/sys/area/treeData" cssClass="form-control required" />
								</div>
								<div style="float:left;margin-left:10px;width:450px;">
									<form:input path="userBaseInfo.dwelling1" htmlEscape="false" maxlength="50" class="form-control  max-width-600 required" name="dwelling1" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="6" style="background-color: #5553;"><label class="pull-left">入职情况及联系方式</label></td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>手机号码：</label></td>
							<td>
									${personnelFile.mobile}
								<input type="hidden" name="userEmployed.phoneNumber" value="${personnelFile.mobile}"/>
							</td>
							<td><label class="pull-right">办公电话：</label></td>
							<td><form:input path="userEmployed.officeTel" htmlEscape="false"
									maxlength="11" class="form-control  max-width-600" />
							</td>
							<td><label class="pull-right">家庭电话：</label></td>
							<td><form:input path="userEmployed.familyTel" htmlEscape="false"
									maxlength="11" class="form-control  max-width-600" />
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">从何处了解到公司招聘信息：</label></td>
							<td><form:input path="userEmployed.source" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600" />
							</td>
							<td><label class="pull-right">入职介绍人：</label></td>
							<td><form:input path="userEmployed.introducer" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600" />
							</td>
							<td><label class="pull-right">介绍人电话：</label></td>
							<td><form:input path="userEmployed.introducerTel" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600" />
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>试用开始日期：</label></td>
							<td>
								<input id="probationStartDate" name="userEmployed.probationStartDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" 
									value="<fmt:formatDate value="${userEmployed.probationStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'memberDate\')}'})"/>
							</td>
							<td><label class="pull-right"><font color="red">*</font>转正日期：</label></td>
							<td>
								<input id="memberDate" name="userEmployed.memberDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" 
								value="<fmt:formatDate value="${actionInfo.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'probationStartDate\')}',maxDate:'#F{$dp.$D(\'contractEndDate\')}'})"/>
							</td>
							<td><label class="pull-right"><font color="red">*</font>合同截止日期：</label></td>
							<td>
								<input id="contractEndDate" name="userEmployed.contractEndDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" value="<fmt:formatDate value="${actionInfo.showTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'memberDate\')}'})"/>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>试用期工资：</label></td>
							<td><form:input path="userEmployed.probationWages" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600 required" />
							</td>
							<td><label class="pull-right"><font color="red">*</font>基本工资：</label></td>
							<td><form:input path="userEmployed.baseWages" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600 required" />
							</td>
							<td><label class="pull-right">第二工作职业：</label></td>
							<td><form:input path="userEmployed.secondWork" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600" />
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>工资卡开户行：</label></td>
							<td><form:input path="userEmployed.bankBeneficiary" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600 required" />
							</td>
							<td><label class="pull-right"><font color="red">*</font>账号：</label></td>
							<td colspan="3">
								<form:input path="userEmployed.bankCode" htmlEscape="false"
									class="form-control  max-width-600 required" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
							</td>
						</tr>
						<tr>
							<td colspan="6" style="background-color: #5553;"><label class="pull-left">教育背景</label></td>
						</tr>
						<tr>
							<td><label class="pull-right">学历：</label></td>
							<td>
								<select class="form-control max-width-150" name="userEducation.education">
									<option value="1">小学</option>
									<option value="2">初中</option>
								</select>
							</td>
							<td><label class="pull-right">学位：</label></td>
							<td>
								<select class="form-control max-width-150" name="userEducation.degree">
									<option value="1">博士</option>
									<option value="2">博士后</option>
								</select>
							</td>
							<td><label class="pull-right">毕业时间：</label></td>
							<td>
								<input id="graduationDate" name="userEducation.graduationDate" class="Wdate form-control layer-date input-sm" style="height: 30px;width: 200px" type="text" 
									value="<fmt:formatDate value="${userEmployed.probationStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">毕业学校：</label></td>
							<td><form:input path="userEducation.graduationSchool" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600" />
							</td>
							<td><label class="pull-right">专业：</label></td>
							<td><form:input path="userEducation.major" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600" />
							</td>
							<td><label class="pull-right">外语能力：</label></td>
							<td><form:input path="userEducation.languageAbili" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600" />
							</td>
						</tr>
						<tr>
							<td colspan="6" style="background-color: #5553;"><label class="pull-left">工作经历</label></td>
						</tr>
						<tr>
							<td><label class="pull-right">曾工作过公司1：</label></td>
							<td><form:input path="userWorkExperienceList[0].onceCompany" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600" />
								<input type="hidden" name="userWorkExperienceList[0].workType" value="1" />
							</td>
							<td><label class="pull-right">开始日期：</label></td>
							<td>
								<input id="workStartDate" name="userWorkExperienceList[0].workStartDate" class="Wdate form-control layer-date input-sm" style="height: 30px;width: 200px" type="text" 
									value="<fmt:formatDate value="${userEmployed.probationStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'workEndDate\')}'})"/>
							</td>
							<td><label class="pull-right">结束日期：</label></td>
							<td>
								<input id="workEndDate" name="userWorkExperienceList[0].workEndDate" class="Wdate form-control layer-date input-sm" style="height: 30px;width: 200px" type="text" 
								value="<fmt:formatDate value="${actionInfo.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'workStartDate\')}'})"/>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">职位：</label></td>
							<td><form:input path="userWorkExperienceList[0].position" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600" />
							</td>
							<td><label class="pull-right">离职原因：</label></td>
							<td colspan="3"><form:input path="userWorkExperienceList[0].languageAbili"
									htmlEscape="false" maxlength="10"
									class="form-control  max-width-600" /></td>
						</tr>
						<tr>
							<td><label class="pull-right">曾工作过公司2：</label></td>
							<td><form:input path="userWorkExperienceList[1].onceCompany" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600" />
								<input type="hidden" name="userWorkExperienceList[1].workType" value="2" />
							</td>
							<td><label class="pull-right">开始日期：</label></td>
							<td>
								<input id="workStartDate1" name="userWorkExperienceList[1].workStartDate" class="Wdate form-control layer-date input-sm" style="height: 30px;width: 200px" type="text" 
									value="<fmt:formatDate value="${userEmployed.probationStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'workEndDate1\')}'})"/>
							</td>
							<td><label class="pull-right">结束日期：</label></td>
							<td>
							
								<input id="workEndDate1" name="userWorkExperienceList[1].workEndDate" class="Wdate form-control layer-date input-sm" style="height: 30px;width: 200px" type="text" 
									value="<fmt:formatDate value="${actionInfo.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'workStartDate1\')}'})"/>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">职位：</label></td>
							<td><form:input path="userWorkExperienceList[1].position" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600" />
							</td>
							<td><label class="pull-right">离职原因：</label></td>
							<td colspan="3"><form:input path="userWorkExperienceList[1].languageAbili"
									htmlEscape="false" maxlength="10"
									class="form-control  max-width-600" /></td>
						</tr>
						<tr>
							<td colspan="6" style="background-color: #5553;"><label class="pull-left">家庭情况</label></td>
						</tr>
						<tr>
							<td><label class="pull-right">父亲：</label></td>
							<td><form:input path="userFamilies[0].name" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600" />
									<input type="hidden" name="userFamilies[0].nameType" value="1" />
							</td>
							<td><label class="pull-right">联系电话：</label></td>
							<td><form:input path="userFamilies[0].contactNumber" htmlEscape="false"
									maxlength="11" class="form-control  max-width-600" />
							</td>
							<td><label class="pull-right">出生日期：</label></td>
							<td>
								<input id="fatherBirthday" name="userFamilies[0].birthday" class="Wdate form-control layer-date input-sm" style="height: 30px;width: 200px" type="text" 
									value="<fmt:formatDate value="${userEmployed.probationStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">母亲：</label></td>
							<td><form:input path="userFamilies[1].name" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600" />
								<input type="hidden" name="userFamilies[1].nameType" value="2" />
							</td>
							<td><label class="pull-right">联系电话：</label></td>
							<td><form:input path="userFamilies[1].contactNumber" htmlEscape="false"
									maxlength="11" class="form-control  max-width-600" />
							</td>
							<td><label class="pull-right">出生日期：</label></td>
							<td>
								<input id="motherBirthday" name="userFamilies[1].birthday" class="Wdate form-control layer-date input-sm" style="height: 30px;width: 200px" type="text" 
									value="<fmt:formatDate value="${userFamilies[1].birthday}" pattern="yyyy-MM-dd HH:mm:ss"/>"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">父母亲住址：</label></td>
							<td colspan="5"><form:input path="userFamilies[0].familyAddress"
									htmlEscape="false" maxlength="10"
									class="form-control  max-width-600" /></td>
						</tr>
						<tr>
							<td colspan="6" style="background-color: #5553;"><label class="pull-left">主要家庭成员信息</label></td>
						</tr>
						<tr>
							<td><label class="pull-right">姓名：</label></td>
							<td><form:input path="userFamilymembers[0].name" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600 " />
								<input type="hidden" name="userFamilymembers[0].nameType" value="1" />
							</td>
							<td><label class="pull-right">关系：</label></td>
							<td><form:input path="userFamilymembers[0].relation" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600 " />
							</td>
							<td><label class="pull-right">出生日期：</label></td>
							<td>
								<input id="contactBirthday" name="userFamilymembers[0].birthday" class="Wdate form-control layer-date input-sm" style="height: 30px;width: 200px" type="text" 
									value="<fmt:formatDate value="${userFamilymembers[0].birthday}" pattern="yyyy-MM-dd HH:mm:ss"/>"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">工作单位：</label></td>
							<td><form:input path="userFamilymembers[0].works" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600 " />
							</td>
							<td><label class="pull-right">联系电话：</label></td>
							<td><form:input path="userFamilymembers[0].phone" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600" />
							</td>
							<td><label class="pull-right">是否有小孩：</label></td>
							<td>
								<select class="form-control max-width-150" name="userFamilymembers[0].ischild">
									<option value="0">是</option>
									<option value="1">否</option>
								</select>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>紧急联系人姓名：</label></td>
							<td><form:input path="userFamilymembers[1].name" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600 required" />
								<input type="hidden" name="userFamilymembers[1].nameType" value="2" />
							</td>
							<td><label class="pull-right"><font color="red">*</font>关系：</label></td>
							<td><form:input path="userFamilymembers[1].relation" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600 required" />
							</td>
							<td><label class="pull-right"><font color="red">*</font>联系电话：</label></td>
							<td><form:input path="userFamilymembers[1].phone" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600 required required digits" />
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>紧急联系人姓名：</label></td>
							<td><form:input path="userFamilymembers[2].name" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600 required" />
								<input type="hidden" name="userFamilymembers[2].nameType" value="3" />
							</td>
							<td><label class="pull-right"><font color="red">*</font>关系：</label></td>
							<td><form:input path="userFamilymembers[2].relation" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600 required" />
							</td>
							<td><label class="pull-right"><font color="red">*</font>联系电话：</label></td>
							<td><form:input path="userFamilymembers[2].phone" htmlEscape="false"
									maxlength="10" class="form-control  max-width-600 required required digits" />
							</td>
						</tr>
						<tr>
							<td colspan="6" style="background-color: #5553;"><label class="pull-left">个人评价</label></td>
						</tr>
						<tr>
							<td><label class="pull-right">爱好：</label></td>
							<td colspan="5"><form:input path="userSelfevaluation.hobby"
									htmlEscape="false" maxlength="10"
									class="form-control  max-width-600" /></td>
						</tr>
						<tr>
							<td><label class="pull-right">优点／专长：</label></td>
							<td colspan="5"><form:input path="userSelfevaluation.specialty"
									htmlEscape="false" maxlength="10"
									class="form-control  max-width-600" /></td>
						</tr>
						<tr>
							<td><label class="pull-right">弱点：</label></td>
							<td colspan="5"><form:input path="userSelfevaluation.failing"
									htmlEscape="false" maxlength="10"
									class="form-control  max-width-600" /></td>
						</tr>
						<tr>
							<td><label class="pull-right">发展性：</label></td>
							<td colspan="5"><form:input path="userSelfevaluation.expansibility"
									htmlEscape="false" maxlength="10"
									class="form-control  max-width-600" /></td>
						</tr>
						<tr>
							<td><label class="pull-right">自我评价：</label></td>
							<td colspan="5"><form:input path="userSelfevaluation.selfEvaluation"
									htmlEscape="false" maxlength="10"
									class="form-control  max-width-600" /></td>
						</tr>
<%-- 						<tr>
							<td>离职情况</td>
						</tr>
						<tr>
							<td><label class="pull-right">是否离职：</label></td>
							<td>
								<select class="form-control max-width-150" name="userDepartures.isleaveoffice">
									<option value="0">是</option>
									<option value="1">否</option>
								</select>
							</td>
							<td><label class="pull-right">离职日期：</label></td>
							<td>
								<input id="leaveofficeDate" name="userDepartures.leaveofficeDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required" 
								value="<fmt:formatDate value="${userDepartures.leaveofficeDate}" pattern="yyyy-MM-dd"/>" />
							</td>
							<td><label class="pull-right">停止核算工资：</label></td>
							<td>
								<select class="form-control max-width-150" name="userDepartures.stopAccountingwages">
									<option value="0">是</option>
									<option value="1">否</option>
								</select>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">离职原因：</label></td>
							<td colspan="5">
								<form:textarea class="form-control  max-width-600 required" path="userDepartures.leaveofficeReasons" cols="150" rows="6"/>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right">操作日志：</label></td>
							<td colspan="5">
								<form:textarea class="form-control  max-width-600 required" path="userDepartures.operationLog" cols="150" rows="6"/>
							</td>
						</tr>
 --%>						<tr>
							<td colspan="6" align="center" style="background-color: #5553;"><input type="button" class="btn btn-primary" value="提 交" onclick="verify()"></td>
						</tr>
					
				</table>
			</form:form>
			<!-- 商品规格图片上传弹出层 begin -->
			<div class="W" id="Wrap" style="display: none;">
				<div class="Wrap">
					<div class="Title">
						<h3 class="MainTit" id="MainTit"></h3>
						<a href="javascript:closeuploadImg()" title="关闭" class="Close"></a>
					</div>
					<div class="Cont">
						<p class="Note">
							最多上传<strong>1</strong>个附件,单文件最大<strong>10M</strong>,类型<strong>BMP,JPG,PNG,GIF</strong>
						</p>
						<div class="flashWrap"></div>
						<input type="file" id="file_user_upload" name="fileName" />
						<div id="file_category_queue"></div>
						<div class="fileWarp">
							<fieldset>
								<legend>列表</legend>
								<img id="specitemimgsrc" src=""
									style="width: 200px; height: 200px; display: none;" />
							</fieldset>
						</div>
						<div class="btnBox">
							<button class="btn" id="SaveBtn" onclick="saveuploadImg()">保存</button>
							&nbsp;
							<button class="btn" id="CancelBtn" onclick="closeuploadImg()">取消</button>
						</div>
					</div>
				</div>
			</div>
			<div class="box-footer">
				<!-- 商品规格图片上传字段 -->
				<%-- <input type="hidden" id="specgoodsid" name="goodsid" value="${goods.goodsId }"> --%>
				<input type="hidden" id="specarr" name="specarr"> <input
					type="hidden" id="itemimgid" name="itemimgid" value="">
				<%-- <input type="button" class="btn btn-primary" value="提 交" onclick="goodsspecOnsubmit()">--%>
			</div>
		</div>
	</div>
</body>
</html>