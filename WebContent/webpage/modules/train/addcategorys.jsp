<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>添加课程分类</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<!-- 内容上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js"></script>
	<script type="text/javascript">
		//一级分类改变事件
		function categorychange(v){
			if(v == 1){
				$("#secondcategory").hide();
				$("#categoryupload").show();
				$("#auth2").hide();
				$("#auth1").show();
				/* $("#rank2").hide();
				$("#rank1").show();   //必选是否显示 */
			}else if(v == 2){
				$("#secondcategory").show();
				$("#categoryupload").show();
				$("#auth2").show();
				$("#auth1").hide();
				/* $("#rank2").show();
				$("#rank1").hide();  //必选是否显示 */
			}
		}
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if(!$("#coverPic").val()){
				top.layer.alert('课程封面不能为空!', {icon: 0, title:'提醒'});
				return;
			}
			
			if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		
		$(document).ready(function() {
			/* //一级分类隐藏上传图片
			//$("#categoryupload").hide(); */
			var parentId = $("#parentvalue").val();
			if(parentId == '0'){
				$("#firstcategory").find("option[value='2']").attr("selected",true);
				$("#firstcategory").attr({"disabled":"disabled"});//禁用分类选项
				categorychange(2);//二级分类
				$("#secondcategory").attr({"disabled":"disabled"});//禁用分类选项
				//$("#customization").hide(); 
				$("#cateType").attr({"disabled":"disabled"});//禁用分类选项
			/* 	$("#rank2").show();   
				$("#rank1").hide();  //必选是否显示 */
			}else{
				//一级分类隐藏上传图片
				$("#categoryupload").show(); 
				$("#auth2").hide();
				$("#auth1").show();
				/* $("#rank2").hide();
				$("#rank1").show();   //必选是否显示 */
			}
			
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
			$("#file_category_upload").uploadify({
				'buttonText' : '请选择封面',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_category_upload',//<input type="file"/>的name
				'queueID' : 'file_category_queue',//与下面HTML的div.id对应
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
	<form:form class="form-horizontal" id="inputForm" modelAttribute="trainCategorys" action="${ctx}/train/categorys/savecategorys" method="post">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="modal-body">
			<div class="form-inline">
				<div class="form-group">
					<span style="color: red;">*</span>
					<span>名称分类：</span> 
					<input type="text" id="name" name="name" maxlength="6" class="input-medium form-control required" style="width: 70%;">
				</div>
			</div>
			<div class="form-inline">
				<div class="form-group input-item">
					<span style="color: red;">*</span>
					<span>分类描述：</span> 
					<input type="text" id="introduce" name="introduce" maxlength="200" class="input-medium form-control required" style="width: 70%;">
				</div>
			</div>
			<div class="form-inline" id="auth2">
				<div class="form-group input-item" style="line-height:34px;">
					<span style="color: red;">*</span>
					<span>数据权限：</span> 
					<div style="width: 70%;float: right;margin-right:33px;">
					<sys:treeselect id="office" name="office.id" value="${office.id}" labelName="office.name" labelValue="${office.name}"
							title="机构" url="/sys/office/treeData?isGrade=true"  cssClass="form-control required"/>
					</div>
				</div>
			</div>
			<div class="form-inline" id="auth1">
				<div class="form-group input-item" style="line-height:34px;">
					<span style="color: red;">*</span>
					<span>数据权限：</span> 
					<div style="width: 70%;float: right;margin-right:33px;">
					<sys:treeselect id="franchisee" name="franchisee.id" value="${office.id}" labelName="franchisee.name" labelValue="${office.name}"
							title="机构" url="/sys/franchisee/treeData"  cssClass="form-control required" notAllowSelectParent="true"/>
					</div>
				</div>
			</div>
			<div class="form-group input-item" id="customization">
				<span style="color: red;">*</span>
				<span>分类属性：</span> 
				<select name="cateType" id="cateType" class="form-control">
					<option selected value="0">公共</option>
					<option value="1">定制</option>
				</select>
			</div>
			<div class="form-group input-item">
				<span style="color: red;">*</span>
				<span>&nbsp;&nbsp;&nbsp;父类ID：</span> 
				<input type="hidden" id="parentvalue" name="parentvalue" value="${trainCategorys.parentId}" />
				<input type="hidden" id="categoryvalue" name="categoryvalue" value="${trainCategorys.categoryId}" />
				<select name="firstcategory" id="firstcategory" class="form-control" onchange="categorychange(this.options[this.options.selectedIndex].value)">
					<option selected value="1">一级分类</option>
					<option value="2">二级分类</option>
				</select>
				<select name="secondcategory" id="secondcategory" class="form-control" style="display: none;width: 95px;padding-left: 0px;">
					<c:if test="${list.size() > 0 }">
						<c:forEach items="${list}" var="trainCategorys">
							<option value="${trainCategorys.categoryId}">${trainCategorys.name}</option>
						</c:forEach>
					</c:if>
					<c:if test="${list.size() == 0 }">
						<option value="${trainCategorys.categoryId}">${trainCategorys.name}</option>
					</c:if>
				</select>
			</div>
			<div class="form-inline">
				<div class="form-group input-item" style="line-height:34px;">
					<span style="color: red;">*</span>
					<span>是否显示：</span> 
					<div style="width: 70%;float: right;margin-right:33px;">
						<select name="isShow" id="isShow" class="form-control required">
							<option selected value="0">是</option>
							<option value="1">否</option>
						</select>
					</div>
				</div>
			</div>
			<div class="form-inline">
				<div class="form-group input-item" style="line-height:34px;">
					<span style="color: red;">*</span>
					<span>是否公开：</span> 
					<div style="width: 70%;float: right;margin-right:33px;">
						<select name="isOpen" id="isOpen" class="form-control required">
							<option  value="0">公开</option>
							<option selected value="1">不公开</option>
						</select>
					</div>
				</div>
			</div>
			<div class="form-inline">
				<div class="form-group input-item" style="line-height:34px;">
					<span style="color: red;">*</span>
					<span>排序：</span> 
					<div style="width: 70%;float: right;margin-right:33px;">
						<input id="sort" name="sort" class="form-control required" onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="3">
					</div>
				</div>
			</div>
			<div class="form-group input-item" id="categoryupload">
				<span id="rank1" style="color: red;">*</span>
				<span id="rank2">&nbsp;&nbsp;</span>
				<span>课程封面：</span> 
				<img class="input-item-img" id="coverpicsrc" src="${trainLessons.coverPic}" alt="images" />
				<input type="hidden" id="coverPic" name="coverPic" value="${trainLessons.coverPic}">
				<!-- 封面上传 -->
				<div class="upload">
					<input type="file" name="file_category_upload" id="file_category_upload">
				</div>
				<div id="file_category_queue"></div>
			</div>
		</div>
	</form:form>
</body>
</html>