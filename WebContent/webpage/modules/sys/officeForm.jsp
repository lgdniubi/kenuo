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
	<!-- 日期控件 -->
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	 <!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<!-- 富文本框 -->
	<link rel="stylesheet" href="${ctxStatic}/kindEditor/themes/default/default.css" />
	<script src="${ctxStatic}/kindEditor/kindeditor-all.js" type="text/javascript"></script>
	
	<!-- 富文本框中新增的东西：上传照片，上传代码，商品分类，商品选择 -->
	<script src="${ctxStatic}/kindEditor/themes/editJs/editJs.js" type="text/javascript"></script>
	
	<!-- 富文本框上传图片样式引用 -->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/kindEditor/themes/editCss/edit.css">
	<link rel="stylesheet" href="${ctxStatic}/train/css/tips.css">
	<style>
	</style>
	<script type="text/javascript">
		var newUploadURL = '<%=uploadURL%>';
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			var verifyForm = "true";
			$.ajax({
				type : "POST",   
				url : "${ctx}/sys/office/verifyLevel",
				data: {id:$("#officeId").val(), grade:$("#grade").val(),officeId:$("#id").val()},
				async:false,
				success: function(data) {
					if("ERROR" == data.FLAG){
						verifyForm = "false";
						top.layer.alert(data.MESSAGE, {icon: 0, title:'提醒'}); 
					}
				}
			}); 
			if(verifyForm == "true"){
				if(validateForm.form()){
					var content = $(".ke-edit-iframe").contents().find(".ke-content").html();
					if(content.indexOf("style") >=0){
						content = content.replace("&lt;style&gt;","<style>");
						content = content.replace("&lt;/style&gt;","</style>");
					}
					$("#details").val(content);
					$("#tags").val($("#officeInfotagsName").val());//标签
					$("#inputForm").submit();
					$("#btn1,#btn2,#btn3").prop("disabled","disabled");
					return true;
				}
			}
		  return false;
		}
		//校验图片不能为空
		var isShop;
		function validateImgUrl(){
			if(isShop == 1){ //是店铺
				var flag = validOneImg('char');
				return flag;
			}else{
				return true
			}
		}
		function validOneImg(id){
			if($("#"+id).val() == null || $("#"+id).val() == ""){
				   top.layer.alert('图片不可为空！', {icon: 0, title:'提醒'});
				   return false;
			}else{
				return true;
			}
		}
		$(document).ready(function() {
			unfold($("#grade").val());
			$("#name").focus();
			validateForm = $("#inputForm").validate({
				rules: {
					longitude:{
						number:true,
						lrunlv:true
						},
					latitude:{
						number:true,
						lrunlv:true
						},
					name: {
						remote: {
			            	url:"${ctx}/sys/office/verifyName",
		            		type: "post",
		            		async:false,
		                    data: {
		                    	'id' : function(){return $("#officeId").val();},
		                        'name': function(){return $("input[name='name']").val();},
			            		'oldOfficeName': function(){return $("#oldOfficeName").val();}
		                    }
						 }
					},//设置了远程验证，在初始化时必须预先调用一次。
					//添加唯一编码唯一性验证
					officeCode:{
						maxlength:20,
						officeCodeMethod:true,
						remote:{
							url:"${ctx}/sys/office/checkOfficeCode",
							type:"post",
							async:false,
							data:{
								'officeCode':function(){return $("#officeCode").val();},
								'oldOfficeCode': function(){return $("#oldOfficeCode").val();}
							}
						}
					},
					'officeInfo.legalPerson':{
						legalPersonMethod:true						
					},
					'officeInfo.accountname':{
						accountnameMethod:true						
					},
					'officeInfo.openbank':{
						openbankMethod:true						
					},
					'officeInfo.bankaccount':{
						rangelength:[13,19]	,
						number:true
					},
					"officeInfo.creditCode":{
						creditCodeMethod:true
					}
				},
				messages:{
					longitude:{
						number:"输入正确的经度",
						lrunlv:"小数点前不能超过三位，小数点后不能超过六位"
					},
					latitude:{
						number:"输入正确的纬度",
						lrunlv:"小数点前不能超过三位，小数点后不能超过六位"
					},
					name: {remote: "机构名称已存在"},
					officeCode: {
						maxlength:"机构编码不能超过20个字符",
						remote: "机构编码已存在"
					},
					'officeInfo.bankaccount':{
						rangelength:"请输入13-19位数字",
						number:"请输入13-19位数字"
					}
				}
			});
			//判断经纬度最多6位小数
			jQuery.validator.addMethod("lrunlv", function(value, element) {     
				var returnVal = true;
	            var inputZ = value;
	            var ArrMen= inputZ.split(".");    //截取字符串
	            if(ArrMen.length==1){
	            	if(ArrMen[0].length>3 || ArrMen[0].length<1){    //判断小数点后面的字符串长度
	                    returnVal = false;
	                    return false;
	                }
	            }else if(ArrMen.length==2){
	                if(ArrMen[1].length>6 || ArrMen[1].length<1 || ArrMen[0].length>3 || ArrMen[0].length<1){    //判断小数点后面的字符串长度
	                    returnVal = false;
	                    return false;
	                }
	            }
	            return returnVal;
			}, "小数点前不能超过三位，小数点后不能超过六位");//验证错误信息
			//判断经纬度最多6位小数
			jQuery.validator.addMethod("legalPersonMethod", function(value, element) {     
				 return this.optional(element) || /^[\u0391-\uFFE5]+$/.test(value) || /^\d+$/.test(value);
			}, "请输入汉字或字母，最多8个字");//验证错误信息
			jQuery.validator.addMethod("accountnameMethod", function(value, element) {   
				 return this.optional(element) || /^[\u0391-\uFFE5]+$/.test(value) ||  /^[A-Za-z]+$/.test(value);
			}, "仅支持中文或英文、最多20个字");//验证错误信息
			jQuery.validator.addMethod("openbankMethod", function(value, element) {   
				 return this.optional(element) || /^[\u0391-\uFFE5]+$/.test(value);
			}, "仅支持中文、最多20个字");//验证错误信息
			jQuery.validator.addMethod("creditCodeMethod", function(value, element) {   
				 return this.optional(element) || /^[0-9A-Z]+$/.test(value);
			}, "请输入18位数字或大写字母");//验证社会信用代码
			jQuery.validator.addMethod("officeCodeMethod", function(value, element) {   
				 return this.optional(element) || /^[0-9A-Za-z_]+$/.test(value);
			}, "只能输入数字字母和下划线");//机构唯一编码  ^[0-9a-zA-Z_]{1,}$

			//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
			//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
			//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
			$("#inputForm").validate().element($("#name"));
			$("#inputForm").validate().element($("#officeCode"));
			
			$("#file_photo_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_photo_upload',//<input type="file"/>的name
				'queueID' : 'file_photo_queue',//与下面HTML的div.id对应
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
						$("#officeInfoimg").val(jsonData.file_url);
						$("#officeInfoimgsrc").attr('src',jsonData.file_url); 
					}
				}
			});
			$("#file_char_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_char_upload',//<input type="file"/>的name
				'queueID' : 'file_char_queue',//与下面HTML的div.id对应
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
						$("#char").val(jsonData.file_url);
						$("#officeCharImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});
			
			$("#file_icardone_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_icardone_upload',//<input type="file"/>的name
				'queueID' : 'file_icardone_queue',//与下面HTML的div.id对应
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
						$("#icardone").val(jsonData.file_url);
						$("#officeIcardoneImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});
			$("#file_icardtwo_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_icardtwo_upload',//<input type="file"/>的name
				'queueID' : 'file_icardtwo_queue',//与下面HTML的div.id对应
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
						$("#icardtwo").val(jsonData.file_url);
						$("#officeIcardtwoImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});
			$("#file_cardup_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_cardup_upload',//<input type="file"/>的name
				'queueID' : 'file_cardup_queue',//与下面HTML的div.id对应
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
						$("#cardup").val(jsonData.file_url);
						$("#officeCardupImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});
			$("#file_carddown_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_carddown_upload',//<input type="file"/>的name
				'queueID' : 'file_carddown_queue',//与下面HTML的div.id对应
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
						$("#carddown").val(jsonData.file_url);
						$("#officeCarddownImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});
			
		});
		function unfold(num){
			//num = 1 不是店铺  num = 2 为店铺
			if(num == 1){
				isShop= 1;
				$("#unfold,#area1,#signtab,#next,#signNext").show();
				$("#a1,#a2,#a3,#a4,#a5,#sbutton,#area2").hide();
				$("#otypeid").html("店铺创建成功无法变更机构类型");
			}else{
				isShop= 0;
				$("#unfold,#area1,#signtab,#next,#signNext").hide();
				$("#a1,#a2,#a3,#a4,#a5,#sbutton,#area2").show();
				$("#otypeid").html("存在子类时不可修改为店铺");
			}
		}
		function changeTableVal(type,id,isyesno){
			if($("#office"+type).val() == 1){
				$(".loading").show();//打开展示层
				$.ajax({
					type : "POST",
					url : "${ctx}/sys/office/updateisyesno?ID="+id+"&ISYESNO="+isyesno+"&TYPE="+type,
					dataType: 'json',
					success: function(data) {
						$(".loading").hide(); //关闭加载层
						var flag = data.FLAG;
						if("OK" == flag){
							$("#"+type).html("");//清除DIV内容	
							if(type == "isCargo"){
								if(isyesno == '1'){
									//当前状态为【否】，则打开
									$("#"+type).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+type+"','"+id+"','0')\">&nbsp;&nbsp;否");
								}else if(isyesno == '0'){
									//当前状态为【是】，则取消
									$("#"+type).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+type+"','"+id+"','1')\">&nbsp;&nbsp;是");
								}
							}else{
								if(isyesno == '0'){
									//当前状态为【否】，则打开
									$("#"+type).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+type+"','"+id+"','1')\">&nbsp;&nbsp;否");
								}else if(isyesno == '1'){
									//当前状态为【是】，则取消
									$("#"+type).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+type+"','"+id+"','0')\">&nbsp;&nbsp;是");
								}
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
	<div>
	<label class="pull-left"><a href="#">基础信息</a></label>
	<c:if test="${not empty office && not empty office.id}">
	<label class="pull-left" id="signtab" ><a href="${ctx}/sys/office/signInfo?id=${office.id}&opflag=${opflag}">-----签约信息</a></label>
	</c:if>
	</div>
		<form:form id="inputForm" modelAttribute="office" action="${ctx}/sys/office/save" method="post" class="form-horizontal">
			<!-- 操作隐藏店铺按钮权限 -->
			<shiro:hasPermission name="sys:office:updateOfficeStatus"><input type="hidden" id="officestatus" value="1"></shiro:hasPermission>
			<!-- 操作是否为新店按钮权限 -->
			<shiro:hasPermission name="sys:office:updateOfficeIsNew"><input type="hidden" id="officeisNew" value="1"></shiro:hasPermission>
			<!-- 操作是否推荐按钮权限 -->
			<shiro:hasPermission name="sys:office:updateIsRecommend"><input type="hidden" id="officeisRecommend" value="1"></shiro:hasPermission>
			<!-- 操作是否可报货按钮权限 -->
			<shiro:hasPermission name="sys:office:updateIsCargo"><input type="hidden" id="officeisCargo" value="1"></shiro:hasPermission>
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
				<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
				   <tbody>
						<tr><td colspan="4" class="active"><label class="pull-left">基本信息</label></td></tr>
						<tr>
				         	<td class="width-15 active"><label class="pull-right">
				         		<span class="help-tip">
									<span class="help-p" id="otypeid">存在子类时不可修改为店铺</span>
								</span>
				         		机构类型:</label></td>
<!-- 							<td  class="width-15 active"><label class="pull-right"><font color="red">*</font>是否为店铺:</label></td> -->
					         <td class="width-35">
					         	<c:choose>
					         		<c:when test="${office.num == '0' and office.grade == 1}">
							         	<form:select path="grade" id="grade" cssClass="form-control" >
											<form:option  value="1" label="店铺"/>
										</form:select>
										<span class="help-inline"><font color="red">注：更改此类时请重新选择归属区域</font></span>
					         		</c:when>
					         		<c:when test="${office.num == '0' and office.grade == 2}">
							         	<form:select path="grade" id="grade" cssClass="form-control" onchange="unfold(this.options[this.options.selectedIndex].value)">
											<form:option  value="2" label="非店铺"/>
											<form:option  value="1" label="店铺"/>
										</form:select>
										<span class="help-inline"><font color="red">注：更改此类时请重新选择归属区域</font></span>
					         		</c:when>
					         		<c:when test="${office.num == '0' }">
							         	<form:select path="grade" id="grade" cssClass="form-control" onchange="unfold(this.options[this.options.selectedIndex].value)">
											<form:option  value="2" label="非店铺"/>
											<form:option  value="1" label="店铺"/>
										</form:select>
										<span class="help-inline"><font color="red">注：更改此类时请重新选择归属区域</font></span>
					         		</c:when>
					         		<c:when test="${office.num != '0'}">
										<form:hidden path="grade"/>	<!-- disabled="true" form表单提交的时候,就不会传值到后台 -->
										<form:select path="grade" id="grade" cssClass="form-control" disabled="true">
											<form:option  value="2" label="非店铺"/>
										</form:select>
										<span class="help-inline">存在子类时不可改为是店铺</span>
					         		</c:when>
					         	</c:choose>
							</td>
							<td class="width-15 active"><label class="pull-right">数据类型:</label></td>
							<td class="width-35">
								<shiro:hasPermission name="sys:office:isRealData">
								<select id="isTest" name="isTest">
									<option value="0" <c:if test="${office.isTest == 0}">selected="true"</c:if> >正式数据</option>
									<option value="1" <c:if test="${office.isTest == 1}">selected="true"</c:if>>测试数据</option>
								</select>
						    	</shiro:hasPermission>
							</td>
							     <%-- <tr>
							     	<td class="width-15 active"><label class="pull-right">是否正常数据</label></td>
						     		<td>
										<form:select path="isTest" class="form-control required">
											<form:option value="0">正常数据</form:option>
											<form:option value="1">测试数据</form:option>
										</form:select>
									</td>
							     </tr> --%>
						</tr>
						<tr>
							<td class="width-15 active"><label class="pull-right">是否可报货</label></td>
					     	<td id="isCargo">
					     		<c:if test="${not empty office.id }">
					     			<c:choose>
					     				<c:when test="${not empty status and status eq '1' }">
							     			<select name="isCargo" id="grade" class="form-control" >
												<c:if test="${office.isCargo == 0}"><option  value="0" label="是"/></c:if>
												<c:if test="${office.isCargo == 1}"><option  value="1" label="否"/></c:if>
											</select>
					     				</c:when>
					     				<c:otherwise>
							     			<form:select path="isCargo" id="grade" cssClass="form-control" >
												<form:option  value="0" label="是"/>
												<form:option  value="1" label="否"/>
											</form:select>
					     				</c:otherwise>
					     			</c:choose>
					         		<%-- <c:if test="${office.isCargo == 1}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('isCargo','${office.id}',0)">&nbsp;&nbsp;否
									</c:if>
									<c:if test="${office.isCargo == 0}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('isCargo','${office.id}',1)">&nbsp;&nbsp;是
									</c:if> --%>
					         	</c:if>
					         	<c:if test="${empty office.id }">
					         		<select class="form-control required" id="isCargo" name="isCargo">
										<option value='1'>否</option>
									</select>
					         	</c:if>
					     	</td>
						</tr>
						<tr>
							<td class="width-15 active"><label class="pull-right">
								<!-- <span class="help-tip">
									<span class="help-p">此字段需从签约信息页面编辑</span>
								</span> -->
							<font color="red">*</font>机构名称:</label></td>
				         	<td class="width-35"><input id="oldOfficeName" value="${office.name }" type="hidden">
				         	<input id="name" name="name" value="${office.name }" maxlength="20" class="form-control required"></td>
					        <td class="width-15 active"><label class="pull-right">归属机构:</label></td>
					        <td class="width-35"><sys:treeselect id="office" name="parent.id" value="${office.parent.id}" labelName="parent.name" labelValue="${office.parent.name}"
								title="机构" url="/sys/office/parentTreeData?isGrade=true" extId="${office.id}"  cssClass="form-control required" allowClear="${office.currentUser.admin}"/></td>
				         
						</tr>
						<tr>
							<td class="width-15 active"><label class="pull-right">
								<span class="help-tip">
									<span class="help-p">最多8个字</span>
								</span>
							<font color="red">*</font>机构缩写：</label></td>
				         	<td class="width-35"><form:input path="shortName" htmlEscape="false" maxlength="8" cssClass="form-control required" /></td>
					        <td class="width-15 active"><label class="pull-right"><font color="red">*</font>归属区域:</label></td>
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
						<%-- <tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>所属商家:</label></td>
				         	<td class="width-35">
				         		<input value="${a }" class="form-control" readonly="readonly">
				         	</td>
				      	</tr> --%>
				       <tr>
				         <td  class="width-15 active"><label class="pull-right">机构编码:</label></td>
				         <td class="width-35"><form:input path="code" htmlEscape="false" maxlength="50" class="form-control" readonly="true"/></td>
				         <td class="width-15 active"><label class="pull-right">
				         	<span class="help-tip">
								<span class="help-p">只能输入数字字母和下划线</span>
							</span>
				         	唯一编码:</label></td>
					     <td class="width-35">
					     	<input id="oldOfficeCode" value="${office.officeCode }" type="hidden">
					       	<input id="officeCode" name="officeCode" value="${office.officeCode }" class="form-control" onkeyup="this.value=this.value.replace(/[^\w]/ig,'')">
					       	<span class="help-inline">只能输入数字字母和下划线</span>
					     </td>
				      	</tr>
				      	<tr id="a1">
					      	<td class="width-15 active"><label class="pull-right">主负责人:</label></td>
					        <td class="width-35"><sys:treeselect id="primaryPerson" name="primaryPerson.id" value="${office.primaryPerson.id}" labelName="office.primaryPerson.name" labelValue="${office.primaryPerson.name}"
								title="用户" url="/sys/office/treeData?type=3" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/></td>
							<td class="width-15 active"><label class="pull-right">联系人电话:</label></td>
				         	<td class="width-35"><form:input path="phone" htmlEscape="false" maxlength="50" cssClass="form-control" /></td>
				      	</tr>
				      	 <tr id="a2">
					     	 <td class="width-15 active"><label class="pull-right">邮箱:</label></td>
					         <td class="width-35"><form:input path="email" htmlEscape="false" maxlength="50" cssClass="form-control" /></td>
					         <td class="width-15 active"><label class="pull-right">邮政编码:</label></td>
					         <td class="width-35"><form:input path="zipCode" htmlEscape="false" maxlength="50" cssClass="form-control" /></td>
					     </tr>
					     <tr id="a3">
					        <td  class="width-15 active"><label class="pull-right">传真:</label></td>
					        <td class="width-35"><form:input path="fax" htmlEscape="false" maxlength="50" cssClass="form-control" /></td>
					     </tr>
				      	<%-- <tr>
				         <td class="width-15 active"><label class="pull-right">是否可用:</label></td>
				         <td class="width-35"><form:select path="useable" class="form-control">
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" cssClass="form-control"/>
							</form:select>
							<span class="help-inline">“是”代表此账号允许登陆，“否”则表示此账号不允许登陆</span></td>
				         
				     	</tr> --%>
				     <%-- <tr id="a5">
						 <td class="width-15 active"><label class="pull-right">副负责人:</label></td>
				         <td class="width-35"><sys:treeselect id="deputyPerson" name="deputyPerson.id" value="${office.deputyPerson.id}" labelName="office.deputyPerson.name" labelValue="${office.deputyPerson.name}"
							title="用户" url="/sys/office/treeData?type=3" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/></td>
				         <td  class="width-15 active"><label class="pull-right">负责人:</label></td>
				         <td class="width-35"><form:input path="master" htmlEscape="false" maxlength="50" cssClass="form-control" /></td>
				     </tr> --%>
				     <tr id="a4">
				         <td class="width-15 active">
				         	<label class="pull-right">
				         	<!-- <span class="help-tip">
								<span class="help-p">此字段需从签约信息页面编辑</span>
							</span> -->
							机构地址:</label></td>
				         <td class="width-35"><form:input path="address" htmlEscape="false" maxlength="50" cssClass="form-control" /></td><%-- <c:if test="${not empty office.address}">disabled="true"</c:if>/> --%>
				     </tr>
				    
				    
				     <tr id="a5">
				         <td  class="width-15 active"><label class="pull-right">备注:</label></td>
				         <td class="width-35"  colspan="3"><form:textarea path="remarks" htmlEscape="false" rows="3" cols="30" maxlength="200" class="form-control"/></td>
				     </tr>
			      </tbody>
			      <tbody id="unfold">
					<%-- <tr>
						<td colspan="4" class="active">
							<label class="pull-left">店铺详细信息</label>
							<form:hidden path="officeInfo.id"/>
						</td>
					</tr> --%>
			      	  <tr>
				      	 <td  class="width-15 active"><label class="pull-right">店铺首图:</label></td>
				         <td class="width-35">
				         	<img id="officeInfoimgsrc" src="${office.officeInfo.img}" alt="" style="width: 200px;height: 100px;"/>
							<input type="hidden" id="officeInfoimg" name="officeInfo.img" value="${office.officeInfo.img}"><!-- 图片隐藏文本框 -->
							<p>&nbsp;</p>
		                   	<div class="upload">
								<input type="file" name="file_photo_upload" id="file_photo_upload">
							</div>
							<div id="file_photo_queue"></div>
				         </td>
				         
				         
				      </tr>
				     <tr>
				         <td class="width-15 active"><label class="pull-right"><font color="red">*</font>店长电话：</label></td>
				         <td class="width-35"><form:input path="officeInfo.telephone" htmlEscape="false" cssClass="form-control required"/></td>
				         <td class="width-15 active"><label class="pull-right"><font color="red">*</font>店长/联系人：</label></td>
				         <td class="width-35"><form:input path="officeInfo.contacts" htmlEscape="false" maxlength="50" cssClass="form-control required" /></td>
				     </tr>
				     <tr>
						<td class="width-15 active"><label class="pull-right">
							<span class="help-tip">
								<span class="help-p">接收预约短信</span>
							</span>
							助理电话:</label></td>
						<%-- <td class="width-35"><form:input path="legalName" htmlEscape="false" maxlength="50" class="form-control required" />${user.id }${user.mobile}${user.name }</td> --%>
						<td class="width-35">
							<div class="input-group">
					         	<input value="${user.mobile}" id="shopAssistantMobile"  class="form-control" placeholder="请输入手机号">
					       		 <span class="input-group-btn">
						       		 <button type="button"  onclick="findUser('shopAssistant')" class="btn btn-primary"><i class="fa fa-search">查询</i></button> 
					       		 </span>
						    </div>
				        </td>
						<td class="width-15 active"><label class="pull-right">店助理:</label></td>
						<td class="width-35">
							<input id="shopAssistantId" name="officeInfo.shopAssistantId" class="form-control required" type="hidden" value="${user.id }"/>
							<input id="shopAssistantName" type="text" readonly="readonly" value="${user.name }" class="form-control" />
						</td>
					</tr>
				     <tr>
				         <td class="width-15 active"><label class="pull-right">
				         	<span class="help-tip">
								<span class="help-p">仅手机号请填写到后输入框</span>
							</span>
				         <font color="red">*</font>店铺电话：</label></td>
				         <td class="width-35"><form:input path="officeInfo.storePhone" htmlEscape="false" cssClass="form-control required"/></td>
				         <td class="width-15 active"><label class="pull-right"><font color="red">*</font>邮政编码:</label></td>
				         <td class="width-35"><form:input path="officeInfo.postalCode" htmlEscape="false" maxlength="50" cssClass="form-control required" onkeyup="this.value=this.value.replace(/\D/g,'')"/></td>
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
				         <td class="width-15 active"><label class="pull-right"><font color="red">*</font>经纬度:</label></td>
				         <td class="width-35" colspan="3">
				         	<form:input path="longitude" htmlEscape="false" maxlength="20" cssClass="form-control required" placeholder="经度" class="form-control" 
					        	onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
								onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
								onfocus="if(value == '0.000000'){value=''}"
								onblur="if(value == ''){value='0.000000'}"/>
				         	--
				         	<form:input path="latitude" htmlEscape="false" maxlength="20" cssClass="form-control required" placeholder="纬度" class="form-control"
				         		onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
								onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
								onfocus="if(value == '0.000000'){value=''}"
								onblur="if(value == ''){value='0.000000'}"/>
				         </td>
				      </tr>
				      <tr>
				      	 <td class="width-15 active"><label class="pull-right"><font color="red">*</font>床位：</label></td>
				         <td class="width-35"><form:input path="officeInfo.bedNum" htmlEscape="false" maxlength="5" cssClass="form-control digits required"/></td>
						 <td class="width-15 active"><label class="pull-right">
						 	<span class="help-tip">
								<span class="help-p">会在APP商家主页新店模块显示</span>
							</span>
						 <font color="red">*</font>是否新店：</label></td>
						 <td id="isNew">
						 	<c:if test="${not empty office.id}">
						 		<form:select class="form-control required"  path="isNew">
									<form:option value='0'>否</form:option>
									<form:option value='1'>是</form:option>
								</form:select>
				         		<%-- <c:if test="${office.isNew == 0}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('isNew','${office.id}',1)">&nbsp;&nbsp;否
								</c:if>
								<c:if test="${office.isNew == 1}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('isNew','${office.id}',0)">&nbsp;&nbsp;是
								</c:if> --%>
				         	</c:if>
				         	<c:if test="${empty office.id}">
				         		<select class="form-control required" id="isNew" name="isNew">
									<option value='0'>否</option>
								</select>
				         	</c:if>
						 </td>
				      </tr>
				      <tr>
						 <td class="width-15 active"><label class="pull-right">标签</label></td>
						 <td class="width-35">
					 		<%-- <input type="hidden" id="tags" name="officeInfo.tags" value="${office.officeInfo.tags}"> --%>
							<sys:treeselect id="officeInfotags" name="officeInfotags" value="${office.officeInfo.tags}" 
								labelName="officeInfo.tags" labelValue="${office.officeInfo.tags}" 
				     		title="店铺标签" url="/train/shopSpeciality/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true" checked="true"/>
						 </td>
						 <td class="width-15 active"><label class="pull-right"><font color="red">*</font>店铺状态(是否隐藏)：</label></td>
				         <td class="width-35" id="status">
				         	<c:if test="${not empty office.officeInfo.id}">
				         		<form:select class="form-control required"  path="officeInfo.status">
									<form:option value='0'>正常</form:option>
									<form:option value='1'>隐藏</form:option>
								</form:select>
								<%-- <c:if test="${office.officeInfo.status == 1}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('status','${office.id}',0)">&nbsp;&nbsp;是
								</c:if>
				         		<c:if test="${office.officeInfo.status == 0}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('status','${office.id}',1)">&nbsp;&nbsp;否
								</c:if> --%>
				         	</c:if>
				         	<c:if test="${empty office.officeInfo.id}">
				         		<select id="officeInfo.status" name="officeInfo.status" class="form-control">
				         			<option value="0">正常</option>
				         		</select>
				         	</c:if>
				         </td>
					  </tr>
				      <tr>
				      	 <td class="width-15 active">
				      	 	<label class="pull-right">
				      	 	<!-- <span class="help-tip">
								<span class="help-p">此字段需从签约信息页面编辑</span>
							</span> -->
				      	 	<font color="red">*</font>详细地址:</label></td>
				          <td class="width-35" colspan="3"><textarea name="officeInfo.detailedAddress" htmlEscape="false" rows="3" cols="30" maxlength="200" style="width: 100%" class="form-control required"  >${office.officeInfo.detailedAddress}</textarea></td>
				      </tr> 
				      <tr>
				        <td class="width-15 active"><label class="pull-right">简介：</label></td>
				        <td class="width-35" colspan="3"><form:textarea path="officeInfo.intro" htmlEscape="false" rows="3" style="width: 100%" maxlength="50" class="form-control"/></td>
				      </tr>
				      <tr>
                        <td class="width-15 active"><label class="pull-right">详细介绍：</label></td>
                        <td class="width-35" colspan="3">
                         	<textarea id="editor1" name="content1" rows="9" style="width: 100%" maxlength="2000" class="form-control"></textarea>
							<textarea id="details" name="officeInfo.details" style="display:none;">${office.officeInfo.details}</textarea>
						</td>
				      </tr>
			      </tbody>
		      </table>
		      <%-- <c:if test="${opflag == 1 && office.grade == '1'}">	<!-- 点击修改opflag=1，根据机构数量判断是否是店铺 -->
		      	<input type="button" value="下一步" onclick="doSubmit()"/>
		      </c:if> --%>
		      <c:if test="${opflag == 1 }">
		      	<input type="button" id="btn1" value="保存基础信息" onclick="doSubmit()"/>
		      </c:if>
		      <c:if test="${opflag == 2}"><!-- 点击添加下级机构 -->
		      <label class="pull-left" id="next" ><input type="button" id="btn2" value="保存并下一步" onclick="doSubmit()"/></label>
		      <label class="pull-left" id="sbutton" ><input type="button" id="btn3" value="保存基础信息" onclick="doSubmit()"/></label>
		      </c:if>
		</form:form>
		<shiro:hasPermission name="sys:office:shopLogs">
			<c:if test="${office.grade == '1'}">
				<a href="#" onclick="openDialogView('操作日志', '${ctx}/sys/office/shopLogs?officeId=${office.id}','900px','450px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>操作日志</a>
			</c:if>
		</shiro:hasPermission>
		<div class="loading"></div>
	</div>
	<!-- 富文本框上传图片弹出框 -->
	<div class="ke-dialog-default ke-dialog ke-dalog-addpic" id="ke-dialog">
		<div class="ke-dialog-content">
			<div class="ke-dialog-header">图片<span class="ke-dialog-icon-close" id="close" title="关闭"></span></div>
			<div class="ke-dialog-body">
				<div class="ke-tabs" style="padding:20px;">
					<input type="hidden" id="ke-dialog-num">
					<ul class="ke-tabs-ul ke-clearfix">
						<li class="ke-tabs-li ke-tabs-li-on ke-tabs-li-selected">网络图片</li>
						<li class="ke-tabs-li">本地上传</li>
					</ul>
					<div class="tab1" style="display:block">
						<div class="form-group">
							图片地址：<input type="text" id="httpImg" class="" value="http://" style="width: 300px;height: 35px">
						</div>
						<div class="form-group">
							图片宽度：<input type="text" id="w_httpImg" class="" style="width: 300px;height: 35px">
						</div>
						<div class="form-group">
							图片高度：<input type="text" id="h_httpImg" class="" style="width: 300px;height: 35px">
						</div>
					</div>
					<div class="tab2">
				        <div class="upload" style="margin top:10px;">
							<input type="file" name="file_img_upload" id="file_img_upload"> 
						</div>
						<div id="file_img_queue" style="margin top:10px;"></div> 
						<div class="t3">
							<div id="file_img_queue" style="margin top:10px;"></div> 
						</div>
					</div>
				</div>
			</div>
			<div class="ke-dialog-footer">
			    <span class="ke-button-common ke-button-outer ke-dialog-yes" title="确定">
			        <input class="ke-button-common ke-button" type="button" value="确定" onclick="saveImg()">
			    </span>
			    <span class="ke-button-common ke-button-outer ke-dialog-no" title="取消">
			        <input class="ke-button-common ke-button" id="newClose" type="button" value="取消">
			    </span>
			</div>
			<div class="ke-dialog-shadow"></div>
			<div class="ke-dialog-mask ke-add-mask"></div>
		</div>
	</div>
	<!-- 输入代码弹出框 -->
	<div class="ke-dialog-default ke-dialog ke-dalog-addpic" id="ke-dialog-code">
		<div class="ke-dialog-content">
			<div class="ke-dialog-header">插入程序代码<span class="ke-dialog-icon-close" id="closeCode" title="关闭"></span></div>
			<div class="ke-dialog-body">
				<div class="ke-tabs navbar-form" style="padding: 5px;">
					<div style="width: 100%;padding-top: 10px;">
						<select id="selectOption" onchange="choose(this.options[this.options.selectedIndex].value)">
							<option value="">自定义样式</option>
							<option value="1">商品卡片样式</option>
							<option value="2">商品详情样式</option>
						</select>
						<div style="width: 100%;padding-top: 10px;">
							<textarea rows="20" cols="67" id="styleCss"></textarea>
						</div>
					</div>
				</div>
			</div>
			<div class="ke-dialog-footer">
			    <span class="ke-button-common ke-button-outer ke-dialog-yes" title="确定">
			        <input class="ke-button-common ke-button" type="button" value="确定" onclick="saveCode()">
			    </span>
			    <span class="ke-button-common ke-button-outer ke-dialog-no" title="取消">
			        <input class="ke-button-common ke-button" id="newCloseCode" type="button" value="取消">
			    </span>
			</div>
			<div class="ke-dialog-shadow"></div>
			<div class="ke-dialog-mask ke-add-mask"></div>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/train/js/jquery.datetimepicker.js"></script>
	<script>
	    // 选取时间
	    $('.datetimepicker').datetimepicker({
			datepicker:false,
			format:'H:i',
			step:30
		});
	    var editor;
		KindEditor.ready(function(K) {
			editor = K.create('textarea[name="content1"]', {
				width : "100%",
				items : ['undo', 'redo', '|','plainpaste','image','media','fontname','fontsize','forecolor','hilitecolor','bold','italic','underline','|','justifyleft', 'justifycenter', 'justifyright','justifyfull','|','clearhtml','code','source','|','fullscreen']
			});
		});
		function LoadOver(){
			$("#ke-dialog-num").val("1");
			//给富文本框赋值
			var content = $("#details").val();
			if(content.indexOf("style") >=0){
				content = content.replace("<style>","&lt;style&gt;");
				content = content.replace("</style>","&lt;/style&gt;");
			}
			$(".ke-edit-iframe").contents().find(".ke-content").html(content);
		}
		window.onload = LoadOver;
		
		var lastValue = "";
		var lastId = "";
		function findUser(id){
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
			// 保存最后一次
			lastValue = value;
			lastId = id;
			$.ajax({
				url:'${ctx}/sys/user/treeDataCompany?officeId=${companyId}',
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
		}
				
			
	</script>
</body>
</html>