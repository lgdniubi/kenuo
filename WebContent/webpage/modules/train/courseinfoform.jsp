<%@page import="com.training.common.config.Global"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>课程信息修改</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<!-- 内容上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js"></script>
	<script type="text/javascript">
		
		//一级分类改变事件，联动二级分类
		function categorychange(v){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",   
				url : "${ctx}/train/categorys/listtow?categoryId="+v,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					$("#categoryId").html("");
					var categoryIdval = $("#categoryIdval").val();
					$.each(data.listtow, function(index,item){
						if(item.categoryId == categoryIdval){
							$("#categoryId").prepend("<option value='"+item.categoryId+"' selected>"+item.name+"</option>");
						}else{
							$("#categoryId").prepend("<option value='"+item.categoryId+"'>"+item.name+"</option>");
						}
					});
				}
			});   
		}
	
		//课程类型选择事件
		function lessontypechange(v){
			$("#lessontype").val(v);
		}	
	
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		
		$(document).ready(function() {
			
			//课程分类
			var parentIdval = $("#parentIdval").val();
			if(null != parentIdval && '' != parentIdval){
				categorychange(parentIdval);
			}
			
			//课程类型回填
			var lessontype = $("#lessontype").val();
			$("#s1 option").each(function(){
				$this = $(this).val();
				if($this == lessontype){
					$(this).attr("selected",true);
				}
			});
			
			//表单验证
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
			
			//课程封面上传
			$("#file_course_upload").uploadify({
				'buttonText' : '请选择图片',
				'method' : 'post',
				'langFile' : '',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_course_upload',//<input type="file"/>的name
				'queueID' : 'file_course_queue',//与下面HTML的div.id对应
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
						$("#coverPic").val(jsonData.file_url);
						$("#coverpicsrc").attr('src',jsonData.file_url); 
					}
				}
			});
		});
	</script>
</head>
<body>
	<form:form class="form-horizontal" id="inputForm" modelAttribute="trainLessons" action="${ctx}/train/course/updatecourse" method="post" enctype="multipart/form-data">
		<form:hidden path="lessonId"/>
		<sys:message content="${message}" />
		<div class="modal-body">
			<div class="form-inline">
				<div class="form-group">
					<span style="color: red;">*</span>
					<span>课程分类：</span> 
					<input type="hidden" id="parentIdval" name="parentIdval" value="${trainLessons.parentId}"/>
					<select class="form-control required" id="parentId" name="parentId" onchange="categorychange(this.options[this.options.selectedIndex].value)">
						<c:forEach items="${listone}" var="trainCategorys">
							<option ${trainCategorys.categoryId == trainLessons.parentId?'selected="selected"':''} value="${trainCategorys.categoryId}">${trainCategorys.name}</option>
						</c:forEach>
					</select>
					<input type="hidden" id="categoryIdval" name="categoryIdval" value="${trainLessons.categoryId}"/>
					<select class="form-control required" id="categoryId" name="categoryId"></select> 
				</div>
			</div>
			<div class="form-inline">
				<div class="form-group">
					<span style="color: red;">*</span>
					<span>课程名称：</span> 
					<input id="name" name="name" type="text" class="text-item required" value="<c:out value="${trainLessons.name}"></c:out>">
				</div>
			</div>
			<div class="form-inline">
				<div class="form-group input-item">
					<span style="color: red;">*</span>
					<%-- <span>课程学分：</span> 
					<input id="lessonScore" name="lessonScore" type="text" class="text-item required digits" value="${trainLessons.lessonScore}"> --%>
					<span>推荐类型：</span> 
					<select class="form-control" id="showType" name="showType">
						<c:forEach items="${fns:getDictList('lesson_show_type')}" var="show_type">
							<c:choose>
								<c:when test="${show_type.value eq trainLessons.showType}">
									<option value="${show_type.value }" selected="selected">${show_type.label }</option>
								</c:when>
								<c:otherwise>
									<option value="${show_type.value }">${show_type.label }</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-inline">
				<div class="form-group input-item">
					<span style="color: red;">*</span>
					<span>课程介绍：</span> 
					<textarea id="introduce" name="introduce" class="textarea-item required" rows="4" cols="40" style="width:250px;height:95px;font-size: 12px;padding-top:0px;resize: none;">${trainLessons.introduce}</textarea>
				</div>
			</div>
			<div class="form-inline">
				<div class="form-group input-item">
					<span style="color: red;">*</span>
					<span>排序：</span> 
					<input id="sort" name="sort" type="text" class="text-item required digits" value="${trainLessons.sort}">
				</div>
			</div>
			<div class="form-inline">
				<div class="form-group input-item">
					<span style="color: red;">*</span>
					<span>是否显示：</span> 
					<form:select path="isShow" cssClass="form-control required">
						<form:option value="0">是</form:option>
						<form:option value="1">否</form:option>
					</form:select>
				</div>
			</div>
			<input type="hidden" id="lessontype" name="lessontype" value="${trainLessons.lessontype}" />
			<div class="form-group input-item">
				<!-- <span style="color: red;">*</span> -->
				&nbsp;&nbsp;
				<span>课程类型：</span> 
				<select name="s1" id="s1" class="form-control" onchange="lessontypechange(this.options[this.options.selectedIndex].value)">
					<option value="1">线上课程</option>
					<option value="2">线上测试</option>
					<option value="3">线下预约</option>
				</select>
			</div>
			<div class="form-group input-item">
				<span>&nbsp;&nbsp;</span>
				<span>课程封面：</span> 
				<img class="input-item-img" id="coverpicsrc" src="${trainLessons.coverPic}" alt="images" />
				<input type="hidden" id="coverPic" name="coverPic" value="${trainLessons.coverPic}">
				<!-- 封面上传 -->
				<div class="upload">
					<input type="file" name="file_course_upload" id="file_course_upload">
				</div>
				<div id="file_course_queue"></div> <!--上传队列展示区-->
			</div>
		</div>
	</form:form>
	<div class="loading"></div>
</body>
</html>