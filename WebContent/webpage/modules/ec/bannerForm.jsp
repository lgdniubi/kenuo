<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>banner图</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	
	<!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		    if($("#imgUrl").val() == null || $("#imgUrl").val() == ""){
			   top.layer.alert('banner图不可为空！', {icon: 0, title:'提醒'});
			   return false;
		    }
			
		    if($('input:checked').length <= 0){
				top.layer.alert('可见范围必选！', {icon: 0, title:'提醒'});
				return false;
			}else{
				var str=document.getElementsByName("isOpenLabel");
				var ids = "";
				for (i=0;i<str.length;i++){
				    if(str[i].checked == true){
				    	ids = ids + str[i].value + ",";
				    }
				}
				if(ids == '0,'){
					$("#isOpen").val(0);
					$("#franchiseeIds").val("");
				}else{
					$("#isOpen").val(1);
					$("#franchiseeIds").val(ids);
				}
			}
		    
	    	if(validateForm.form()){
	    		loading("正在提交，请稍候...");
				$("#inputForm").submit();
		    	return true;
	    	}
	    	return false;
	    };
	    
		$(document).ready(function() {
			var id = "${banner.bannerId}";
			var franchiseeIds = "${banner.franchiseeIds}";
			if(id > 0){
				if(franchiseeIds.length > 0){
					var franchiseeId = franchiseeIds.split(",");
					for(q=0;q<franchiseeId.length-1;q++){
						$("input[type=checkbox][name=isOpenLabel][value="+franchiseeId[q]+"]").attr("checked",true);
						queryIsOpen(franchiseeId[q]);
					}
				}else{
					$("input[type=checkbox][id=open][name=isOpenLabel]").attr("checked",true);
					queryIsOpen(0);
				}
			}
			
			validateForm = $("#inputForm").validate({
					rules:{
						titleStyle:{
							isStyle:true
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
				}
			);
		});
		
		function queryIsOpen(value){
			if($('input:checked').length == 0){
				$("input[type=checkbox][name=isOpenLabel]").attr("disabled",false);
			}else{
				if(value == '0'){
					$("input[type=checkbox][name=isOpenLabel][id=notOpen]").attr("disabled",true);
				}else{
					$("input[type=checkbox][name=isOpenLabel][id=open]").attr("disabled",true);
				}				
			}
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
						<form:form id="inputForm" modelAttribute="banner" action="${ctx}/ec/banner/save">
							<form:hidden path="bannerId"/>
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<td><label class="pull-right"><font color="red">*</font>banner图名称：</label></td>
									<td>
										<form:input path="bannerName" class="form-control required" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>位置类型：</label></td>
									<td>
										<form:select path="bannerType" class="form-control">
											<form:options items="${fns:getDictList('bannerType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
										</form:select>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>banner图：</label></td>
									<td>
										<img id="img" src="${banner.imgUrl }" alt="" style="width: 200px;height: 100px;"/>
										<input class="form-control" id="imgUrl" name="imgUrl" type="hidden" value="${banner.imgUrl }"/><!-- 图片隐藏文本框 -->
										<input type="file" name="file_img_upload" id="file_img_upload">
										<div id="file_img_queue"></div>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right">链接：</label></td>
									<td>
										<form:input path="redirectUrl" class="form-control" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right">商品id：</label></td>
									<td>
										<form:input path="goodsId" class="form-control" style="width: 300px" onfocus="if(value == '0')value=''" onblur="if(this.value == '')this.value='0';"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right">商品类型：</label></td>
									<td>
										<form:select path="actionType" class="form-control">
											<form:option value="0">普通商品</form:option>
											<form:option value="1">限时抢购</form:option>
											<form:option value="2">团购</form:option>
											<form:option value="3">促销优惠</form:option>
										</form:select>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right">跳转类型：</label></td>
									<td>
										<form:select path="pageType" class="form-control">
											<form:options items="${fns:getDictList('pageType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
										</form:select>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>排序：</label></td>
									<td>
										<form:input path="sort" class="form-control required" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td width="100px"><label class="pull-right"><font color="red">*</font>可见范围：</label></td>
									<td>
										<input type="checkbox" id="open" name="isOpenLabel" value="0" onclick="queryIsOpen('0')">公开
										<c:forEach items="${list}" var="franchisee">
											<input type="checkbox" id="notOpen" name="isOpenLabel" value="${franchisee.id}" onclick="queryIsOpen('1')">${franchisee.name}
										</c:forEach>
										<input id="isOpen" value="${banner.isOpen}" name="isOpen" type="hidden">
										<input id="franchiseeIds" value="${banner.franchiseeIds}" name="franchiseeIds" type="hidden">
									</td>
								</tr>
								<tr>
									<td><label class="pull-right">备注：</label></td>
									<td>
										<form:textarea path="remark" class="form-control" style="resize: none;" cols="37" rows="4"/>
									</td>
								</tr>
							</table>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#file_img_upload").uploadify({
				'buttonText' : '请选择图片',
				'width' : 140,
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_img_upload',//<input type="file"/>的name
				'queueID' : 'img',//与下面HTML的div.id对应
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
						$("#imgUrl").val(jsonData.file_url);
						$("#img").attr('src',jsonData.file_url); 
					}
				}
			});
		});
	</script>
</body>
</html>