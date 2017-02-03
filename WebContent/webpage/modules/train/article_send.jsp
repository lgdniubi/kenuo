<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
      <!-- SUMMERNOTE -->
	 <link href="${ctxStatic}/summernote/summernote.css" rel="stylesheet">
	 <link href="${ctxStatic}/summernote/summernote-bs3.css" rel="stylesheet">
	 <script src="${ctxStatic}/summernote/summernote.min.js"></script>
	 <script src="${ctxStatic}/summernote/summernote-zh-CN.js"></script>
	 <!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
</head>
<body class="gray-bg">
<div class="wrapper-content">
    <div class="wrapper wrapper-content">
        <div class="row">
        	<div class="ibox-title">
	            <h2>
	           		<c:if test="${num == 1}">
	           			查看文章
	           		</c:if>
	           		<c:if test="${num != 1}">
	           			  发布文章
	           		</c:if>
	            </h2>
            </div>
            <div class="mail-box">
                 <div class="mail-body">
					<form:form id="inputForm" modelAttribute="Article" action="${ctx}/train/articlelist/addArticle" method="post" class="form-horizontal">
                       <div class="form-group">
                           	  <label class="col-sm-2 control-label">标题：</label>
                           <div class="col-sm-8">
                               <input type="text" id="title" maxlength="50" name="title"  class="form-control" value="${article.title}">
						   </div>
					   </div>
					   <div class="form-group">
                   	  	  <label class="col-sm-2 control-label">分类：</label>
                  	  	  <div class="col-sm-8">
	                          <select class="form-control" id="cateId" name="cateId">
								   <option value="null">请选择分类</option>
								   <c:forEach items="${categoryList}" var="categoryList">
										<option value="${categoryList.categoryId}">${categoryList.name}</option>
								   </c:forEach>
							  </select>
						  </div>
					   </div>
                       <input type="hidden" id="status" name="status" value="0">
                 	   <input type="hidden" id="content" name="content"><!-- 内容 -->
                 	   <input type="hidden" id="articleId" name="articleId" value="${article.articleId}"><!-- 内容 -->
				    </form:form>	
             	 </div>
	             <div class="mail-text h-200">
	                 <div class="summernote">
	                 </div>
	                 <div class="clearfix"></div>
	             </div>
	             <div class="mail-body text-right tooltip-demo">
	             	<c:if test="${num == 1}">
	                  	<a href="${ctx}/train/articlelist/articlelist" class="btn btn-danger btn-sm" data-toggle="tooltip" data-placement="top" title="返回"><i class="fa fa-times"></i> 返回</a>
	            	</c:if>
            		<c:if test="${num != 1}">
            			<button type="button" class="btn btn-primary  btn-sm" onclick="sendLetter()"> <i class="fa fa-reply"></i>发布</button>
                  		<a href="${ctx}/train/articlelist/articlelist" class="btn btn-danger btn-sm" data-toggle="tooltip" data-placement="top" title="放弃"><i class="fa fa-times"></i> 放弃</a>
            		</c:if>
	             </div>
	             <div class="clearfix"></div>
            </div>
        </div>
    </div>
</div>
    <div style="display:none" id="contentView">
   		${article.content}
    </div>
    <div style="display:none">
   		<input type="text" id="nowcateId" value="${article.cateId}">
    </div>
    <script>
        $(document).ready(function () {
            $('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-green',
                radioClass: 'iradio_square-green',
            });
            $('.summernote').summernote({
                lang: 'zh-CN',
                height: 300,
             //	  重写上传图片方法
	            onImageUpload: function(files, editor, $editable) {
	            	showTip('请点击图片按钮进行添加图片');
				}
         	});
            $(".note-editable").html($("#contentView").text());
            if($('#nowcateId').val()!=''){
            	document.getElementById("cateId").value=$("#nowcateId").val();
            }
            
          //插入图片时  先上传照片
			$("#file_photo_upload").uploadify({
				'buttonText' : '请选择图片',
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
						$("#coverPic").val(jsonData.file_url);
						//$(".note-editable").append(jsonData.file_url);
					}
				}
			});
        });
        var edit = function () {
            $('.click2edit').summernote({
                focus: true
            });
        };
        var save = function () {
            var aHTML = $('.click2edit').code(); //save HTML If you need(aHTML: array).
            $('.click2edit').destroy();
        };
        function sendLetter(){
            if($("#title").val()==''){
              	top.layer.alert('标题不能为空！', {icon: 0});
              	return;
              }
            if($("#cateId").val()=='null'){
              	top.layer.alert('类别不能为空！', {icon: 0});
              	return;
              }
			$("#content").val($(".note-editable").html());
			$("#inputForm").submit();
	    }
    </script>
</body>
</html>