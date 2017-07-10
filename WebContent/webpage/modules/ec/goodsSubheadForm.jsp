<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>创建副标题活动</title>
	<meta name="decorator" content="default"/>
	
	<!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if($("#type").val() == 1 ){
				 if($("#img").val() == null || $("#img").val() == ""){
					 top.layer.alert('头图不可为空！', {icon: 0, title:'提醒'});
					 return false;					 
				 }
			  }
			 
	    	if(validateForm.form()){
	    		loading("正在提交，请稍候...");
				$("#inputForm").submit();
		    	return true;
	    	}
	    	return false;
	    }
		
		$(document).ready(function(){
			if($("#type").val() == 1){
				$("#isShow").show();
			}else{
				$("#isShow").hide();
			}
			validateForm = $("#inputForm").validate();
			
			if(${goodsSubhead.flag == 'add' || goodsSubhead.flag == 'view'}){
				$("#time").show();
			}
			
			var start = {
				    elem: '#startDate',
				    format: 'YYYY-MM-DD hh:mm:ss',
				    event: 'focus',
				    max: $("#endDate").val(),   //最大日期
				    istime: true,				//是否显示时间
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
				    elem: '#endDate',
				    format: 'YYYY-MM-DD hh:mm:ss',
				    event: 'focus',
				    min: $("#startDate").val(),
				    istime: true,
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
					
		});
		
		function isShow(value){
			if(value == 1){
				$("#isShow").show();
			}else{
				$("#isShow").hide();
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
						<form id="inputForm" action="${ctx}/ec/goodsSubhead/save">
							<input class="form-control" id="goodsSubheadId" name="goodsSubheadId" type="hidden" value="${goodsSubhead.goodsSubheadId}"/>
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<td><label class="pull-right"><font color="red">*</font>活动名称：</label></td>
									<td colspan="4">
										<input class="form-control required" id="name" name="name" type="text" value="${goodsSubhead.name }" style="width: 100%"/>
									</td>
								</tr>
								<tr id="time">
									<td><label class="pull-right"><font color="red">*</font>生效时间</label></td>
									<td>
										<input id="startDate" name="startDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${goodsSubhead.startDate}" pattern="yyyy-MM-dd"/>" style="width:185px;" readonly="readonly"/>
									</td>
									<td><label class="pull-right"><font color="red">*</font>失效时间</label></td>
									<td>
										<input id="endDate" name="endDate" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${goodsSubhead.endDate}" pattern="yyyy-MM-dd"/>"  style="width:185px;" readonly="readonly"/>&nbsp;&nbsp;
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>副标题文案:</label></td>
									<td colspan="3"> 
										<textarea rows="3" cols="30" id="subheading" name="subheading" style="width: 100%" class="form-control required">${goodsSubhead.subheading}</textarea>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>类型：</label></td>
									<td colspan="4">
										<select class="form-control required" id="type" name="type" onchange="isShow(this.value)">
											<option value='' ></option>	
											<option value=1 <c:if test="${goodsSubhead.type == 1}">selected</c:if>>动态</option>
											<option value=2 <c:if test="${goodsSubhead.type == 2}">selected</c:if>>静态</option>
										</select>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>文案链接：</label></td>
									<td colspan="4">
										<input class="form-control required" id="redirectUrl" name="redirectUrl" type="text" value="${goodsSubhead.redirectUrl }" style="width: 100%"/>
									</td>
								</tr>
								<tr id="isShow">
									<td><label class="pull-right"><font color="red">*</font>头图：</label></td>
									<td colspan="4">
										<img id="img2" src="${goodsSubhead.img }" alt="" style="width: 200px;height: 100px;" />
										<input class="form-control" id="img" name="img" type="hidden" value="${goodsSubhead.img }"/><!-- 图片隐藏文本框 -->
										<input type="file" name="file_img_upload2" id="file_img_upload2">
										<div id="file_img_queue2"></div>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right">备注:</label></td>
									<td colspan="3"> 
										<textarea rows="3" cols="30" id="remarks" name="remarks" style="width: 100%" class="form-control">${goodsSubhead.remarks}</textarea>
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
		$("#file_img_upload2").uploadify({
			'buttonText' : '请选择图片',
			'width' : 110,
			'method' : 'post',
			'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
			'uploader' : '<%=uploadURL%>',
			'fileObjName' : 'file_img_upload2',//<input type="file"/>的name
			'queueID' : 'img2',//与下面HTML的div.id对应
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
					$("#img2").attr('src',jsonData.file_url); 
					$("#img2").attr('style',"width: 200px;height: 100px;border:1px solid black;"); 
				}
			}
		});
	</script>
</body>
</html>