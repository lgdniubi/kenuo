<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>推荐图</title>
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
		    if($("#img").val() == null || $("#img").val() == ""){
			   top.layer.alert('头图不可为空！', {icon: 0, title:'提醒'});
			   return false;
		    }
			
		    if($('input:checked').length <= 0){
				top.layer.alert('可见范围必选！', {icon: 0, title:'提醒'});
				return false;
			}else{
				var str=document.getElementsByName("isOpenLabel");
				var id = "";
				for (i=0;i<str.length;i++){
				    if(str[i].checked == true){
				    	id = id + str[i].value;
				    }
				}
				if(id == '0'){
					$("#isOpen").val(0);
					$("#franchiseeId").val("");
				}else{
					$("#isOpen").val(1);
					$("#franchiseeId").val(id);
				}
			}
			
		    $("input[type=radio][name=isOpenLabel]").removeAttr("disabled");
		    if(validateForm.form()){
	    		loading("正在提交，请稍候...");
				$("#inputForm").submit();
		    	return true;
	    	}
	    	return false;
	    }
		$(document).ready(function(){
			var id = "${officeRecommend.officeRecommendId}";
			var franchiseeId = "${officeRecommend.franchiseeId}";
			if(id > 0){
				$("input[type=radio][name=isOpenLabel]").attr("disabled","disabled");
				if(franchiseeId.length > 0){
					$("input[type=radio][name=isOpenLabel][value="+franchiseeId+"]").attr("checked",true);
				}else{
					$("input[type=radio][id=open][name=isOpenLabel]").attr("checked",true);
				}
			}
			
			validateForm = $("#inputForm").validate();
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
						<form id="inputForm" action="${ctx}/ec/officeRecommend/save">
							<input class="form-control" id="officeRecommendId" name="officeRecommendId" type="hidden" value="${officeRecommend.officeRecommendId}"/>
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<td><label class="pull-right"><font color="red">*</font>推荐组名称：</label></td>
									<td>
										<input class="form-control required" id="name" name="name" type="text" value="${officeRecommend.name }" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>头图：</label></td>
									<td>
										<img id="img1" src="${officeRecommend.img }" alt="" style="width: 200px;height: 100px;" />
										<input class="form-control" id="img" name="img" type="hidden" value="${officeRecommend.img }"/><!-- 图片隐藏文本框 -->
										<input type="file" name="file_img_upload" id="file_img_upload">
										<div id="file_img_queue"></div>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>排序：</label></td>
									<td>
										<input id="sort" name="sort" value="${officeRecommend.sort}" class="form-control required" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td width="100px"><label class="pull-right"><font color="red">*</font>可见范围：</label></td>
									<td>
										<input type="radio" id="open" name="isOpenLabel" value="0">公开
										<c:forEach items="${list}" var="franchisee">
											<input type="radio" id="notOpen" name="isOpenLabel" value="${franchisee.id}">${franchisee.name}
										</c:forEach>
										<input id="isOpen" value="${officeRecommend.isOpen}" name="isOpen" type="hidden">
										<input id="franchiseeId" value="${officeRecommend.franchiseeId}" name="franchiseeId" type="hidden">
									</td>
								</tr>
							</table>
						</form>
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
				'width' : 110,
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_img_upload',//<input type="file"/>的name
				'queueID' : 'img1',//与下面HTML的div.id对应
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
						$("#img1").attr('src',jsonData.file_url); 
						$("#img1").attr('style',"width: 200px;height: 100px;border:1px solid black;"); 
					}
				}
			});
		});
	</script>
</body>
</html>