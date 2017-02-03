<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>内容修改</title>
	<meta name="decorator" content="default" />
    <link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<!-- 内容上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/js/jquery.range.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/js/coursemange.js"></script>
		
	<script type="text/javascript">
			var num;
	
			//点击图片按钮，添加图片
			 function showdiv(v){
				num=v;
				$("#Wrap").show();
				$("#specitemimgsrc").hide();
		
			}
		 	
			//保存图片
			var saveuploadImg = function(){
				//var itemimgid = $('#itemimgid').val(); 
				
				var path = $("#specitemimgsrc")[0].src; 
				$("#imgurl"+num).attr('src',path); 
				$("#Wrap").hide();
				$("#specitemimgsrc").attr('src',''); 
				var contentId=$("#contentId"+num).val();
				$.ajax({
					 type:"post",
					 url:"${ctx}/train/course/updateContentPic",
					  data: {contentId:contentId,coverPic:path},
					 success:function(date){
						//alert("保存成功!");
					     
					 },
					 error:function(XMLHttpRequest,textStatus,errorThrown) {
					  //  alert("保存失败")
					  }
					 
					});
				
				
			}
			
			
			//关闭上传图片按钮-清楚图片内容
			var closeuploadImg = function(){
				$("#specitemimgsrc").attr('src',''); 
				$("#Wrap").hide();
			}
			

	
		//页面加载事件
		$(document).ready(function() {
			
			
			$("#images_upload").uploadify({
				'buttonText' : '请选择封面',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'images_upload',//<input type="file"/>的name
				'queueID' : 'images_queue',//与下面HTML的div.id对应
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
					var jsonData = $.parseJSON(data);//text 转 json"WebContent/static/ec/images/uploadify.jpg"
					if(jsonData.result == '200'){
						alert("jinlai le");
						var contentId = $("#contentId"+num).val();
						$("#imgurl"+num).attr('src',jsonData.file_url);
					}
				}
			});
			
			
			//商品规格图片
			$("#file_specitemimg_upload").uploadify({
				'buttonText' : '请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_specitemimg_upload',//<input type="file"/>的name
				'queueID' : 'file_specitemimg_queue',//与下面HTML的div.id对应
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
						$("#specitemimg").val(jsonData.file_url);
						$("#specitemimgsrc").show();
						$("#specitemimgsrc").attr('src',jsonData.file_url); 
					}
				}
			});
			
				
			
		
			var lessonId = $("#lessonId").val();//课程ID
			//视频上传
			$("#file_video_upload").uploadify({
				'buttonText' : '请选择视频',
				'method' : 'post',
				'langFile' : '',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_video_upload',//<input type="file"/>的name
				'queueID' : 'file_video_queue',//与下面HTML的div.id对应
				'method' : 'post',
				'fileTypeDesc': '支持的格式：*.mp4',
				'fileTypeExts' : '*.mp4', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '200MB',//上传文件的大小限制
				'multi' : false,//设置为true时可以上传多个文件
				'auto' : false,//点击上传按钮才上传
				'onSWFReady' : function(){
					//用于视频与视频封面交替上传
					var contentId = $("#contentId").val();
					if($.trim(contentId) < 1){
						$("#file_images_upload").uploadify('disable',true);//上传图片功能 - 禁用
						$("#btn_images").attr({"disabled":"disabled"});//视频缩略图"上传"按钮 -禁用
						
						$("#file_video_upload").uploadify('disable',false);//上传视频功能 -启用
						$("#btn_video").removeAttr("disabled");//视频"上传"按钮 -启用
					}else{
						$("#file_video_upload").uploadify('disable',true);//上传视频功能 -禁用
						$("#btn_video").attr({"disabled":"disabled"});//视频"上传"按钮 -禁用
						
						$("#file_images_upload").uploadify('disable',false);//上传图片功能 - 启用
						$("#btn_images").removeAttr("disabled");//视频"上传"按钮 -启用//视频缩略图"上传"按钮 -启用
					}
				},
				'onFallback': uploadify_onFallback,
				'onUploadSuccess' : function(file, data, response) {  
					$("#file_video_upload").uploadify('disable',true);//上传视频功能 -禁用
					$("#btn_video").attr({"disabled":"disabled"});//视频"上传"按钮 -禁用
					
					$("#file_images_upload").uploadify('disable',false);//上传图片功能 - 启用
					$("#btn_images").removeAttr("disabled");//视频"上传"按钮 -启用//视频缩略图"上传"按钮 -启用
					
					var jsonData = $.parseJSON(data);//text 转 json
					if(jsonData.result == '200'){
						var contentlength = $("#contentlength").val();
						ajaxSaveFile(lessonId,file.name,jsonData.file_url,contentlength,"video","");
					}
				}
			});
			
			//视频封面上传
			$("#file_images_upload").uploadify({
				'buttonText' : '请选择图片',
				'method' : 'post',
				'langFile' : '',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_images_upload',//<input type="file"/>的name
				'queueID' : 'file_images_queue',//与下面HTML的div.id对应
				'method' : 'post',
				'fileTypeDesc': '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
				'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '10MB',//上传文件的大小限制
				'multi' : false,//设置为true时可以上传多个文件
				'auto' : false,//点击上传按钮才上传
				'onFallback' : uploadify_onFallback,
				'onUploadSuccess' : function(file, data, response) {  
					
					$("#file_images_upload").uploadify('disable',true);//上传图片功能 - 禁用
					$("#btn_images").attr({"disabled":"disabled"});//视频缩略图"上传"按钮 -禁用
					
					$("#file_video_upload").uploadify('disable',false);//上传视频功能 -启用
					$("#btn_video").removeAttr("disabled");//视频"上传"按钮 -启用
					
					var jsonData = $.parseJSON(data);//text 转 json
					if(jsonData.result == '200'){
						var contentId = $("#contentId").val();
						if(null == contentId || "" == contentId){
							alert("保存视频封面出现异常，请与管理员联系");
						}else{
							ajaxSaveFile("","",jsonData.file_url,"","images",contentId);
						}
					}
				}
			});
			
			//pdf上传
			$("#file_pdf_upload").uploadify({
				'buttonText' : '请选择PDF',
				'method' : 'post',
				'langFile' : '',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_pdf_upload',//<input type="file"/>的name
				'queueID' : 'file_pdf_queue',//与下面HTML的div.id对应
				'method' : 'post',
				'fileTypeDesc': '支持的格式：*.pdf;',
				'fileTypeExts' : '*.pdf;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '50MB',//上传文件的大小限制
				'multi' : false,//设置为true时可以上传多个文件
				'auto' : false,//点击上传按钮才上传
				'onFallback' : uploadify_onFallback,
				'onUploadSuccess' : function(file, data, response) { 
					var jsonData = $.parseJSON(data);//text 转 json
					if(jsonData.result == '200'){
						ajaxSaveFile(lessonId,file.name,jsonData.file_url,"","pdf","");
					}
				}
			});
		});
		
		//没有兼容的FLASH时触发
		var uploadify_onFallback = function() {
			alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
		}
		
		//课程内容上传-点击事件
		function uploadifclick(v){
			if(Uploadvalidate(v)){
				$("#file_"+v+"_upload").uploadify('upload','*');
			}
		}
		
		//上传内容校验
		function Uploadvalidate(v){
			var lessonId = $("#lessonId").val();
			var filequeue = $("#file_"+v+"_queue").html();
			if($.trim(lessonId) < 1 ){
				alert("课程ID异常,请与管理员联系");
				return false;
			}else if(null == filequeue || "" == filequeue){
				alert("请选择上传文件");	
				return false;
			}else if(v == 'video'){
				var contentlength = $("#contentlength").val();
				if(contentlength == '00:00:00'){
					alert("请选择视频时长");
					return false;
				}
			}else if(v == 'images'){
				var contentId = $("#contentId").val();
				if($.trim(contentId) < 1){
					alert("保存视频封面出现异常，请与管理员联系");
					return false;
				}
			}
			return true;
		}
		
		//课程内容上传-ajax文件上传(video/word/pdf)
		function ajaxSaveFile(lessonId,filename,contentPath,contentlength,v,contentId){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",  
				url : "${ctx}/train/course/fileuploadcontents?contentTypeName="+v+"&contentId="+contentId+"&lessonId="+lessonId+"&contentlength="+contentlength+"&filename="+filename+"&contentPath="+contentPath,
				dataType : 'json',            	//服务器返回的格式,可以是json或xml或text等 
				success: function(data, status) {
					$(".loading").hide(); //关闭加载层
					//视频封面
					if(v == 'images'){
						$("#"+data.CONTENTSID+"_img").attr('src',data.COVERPIC); 
						$("#contentId").val("");//清空
					}else{
						if(v == 'video'){
							$("#contentId").val(data.CONTENTSID);//保存
						}
						var next = $("#"+v).html(); //保存当前DIV的内容
						$("#"+v).html("");//清除DIV内容
						if(v == 'video'){
							//添加DIV-video内容
							$("#"+v).append("<div id='"+data.CONTENTSID+"' class='col-xs-6 col-md-3'><form class='thumbnail'><img id=\""+data.CONTENTSID+"_img\" style=\"width:220px;height:120px;\" src='' alt='video'><div class='caption'><h5>附件名称："+data.FILENAME+"</h5><p>视频时长："+data.CONTENTLENGTH+"</p><p><button type='button' onclick=\"ajaxFileDelete('"+data.CONTENTSID+"')\" class='btn btn-danger' role='button'><span class='glyphicon glyphicon-remove'></span>删除此文件</button></p></div></form></div>");
						}else{
							//添加DIV-word or pdf内容
							$("#"+v).append("<div id='"+data.CONTENTSID+"' class='col-xs-6 col-md-3'><form class='thumbnail'><img src=\""+data.COVERPIC+"\" style=\"width:220px;height:120px;\" alt='video'><div class='caption'><h5>附件名称："+data.FILENAME+"</h5><p><button type='button' onclick=\"ajaxFileDelete('"+data.CONTENTSID+"')\" class='btn btn-danger' role='button'><span class='glyphicon glyphicon-remove'></span>删除此文件</button></p></div></form></div>");
						}
						$("#"+v).append(next); 
					}
				},
				error:function(data, status, e){ //服务器响应失败时的处理函数  
					$(".loading").hide(); //关闭加载层
					alert(data.FILENAME+" "+data.MESSAGE);
		        }
			});   
		}
		
		//删除课程内容
		function ajaxFileDelete(contentsid){
			if(confirm('确认要删除此文件吗?')){
				if(contentsid < 1 ){
					alert("内容ID出现异常，请与管理员联系");
					return false;
				}else{
					$(".loading").show();//打开展示层
					$.ajax({
						type : "POST",   
						url : "${ctx}/train/course/deleteuploadcontents?CONTENTSID="+contentsid,
						dataType : 'json',            	//服务器返回的格式,可以是json或xml或text等  
						success : function(data, status) {
							$(".loading").hide(); //关闭加载层
							if(data.STATUS == 'OK'){
								alert(data.FILENAME+": "+data.MESSAGE);
							}else{
								alert(data.MESSAGE);
							}
							//删除文件div
							$("#"+contentsid).remove();
						},
						error:function(data, status, e){ //服务器响应失败时的处理函数 
							$(".loading").hide(); //关闭加载层
							alert(data.MESSAGE);
				        } 
					});
				}
			}
		}
		
		
		
	
	</script>
</head>
<body>
	<div class="warpper-content coursemanage">
		<input type="hidden" id="lessonId" name="lessonId" value="${trainLessons.lessonId}">
	    <div class="ibox">
	        <div class="ibox-title">
	            <h5>${trainLessons.name}</h5>-课程内容修改
	        </div>
	        <!-- 视频列表 -->
	        <div class="ibox-content ">
	            <div class="breadcrumb">视频文件</div>
	            <div class="row">
	                <!-- 添加视频 -->
	                <div class="col-xs-6 col-md-3">
	                    <form class="thumbnail" style="min-height:300px;padding: 9px;">
                            <div class="form-group">
                                <p>附件上传：</p>
                                <p><input type="file" name="file_video_upload" id="file_video_upload"></p>
                            	<p>视频时长：<input type="text" id="contentlength" name="contentlength" value="00:00:00" style="width: 110px;" class="datetimepicker form-control"></p>
                            </div>
                            <div id="file_video_queue"></div>
                            <shiro:hasPermission name="train:course:fileuploadcontents">
                       			<p>
                       				<button type="button" id="btn_video"  class="btn btn-info" onclick="uploadifclick('video')">
                       					<span class="glyphicon glyphicon-ok"></span>视频上传
                       				</button>
                       			</p>
                       		</shiro:hasPermission>
                       		<div class="form-group">
                       			<input type="hidden" id="contentId" name="contentId" value=""/>
                       			<br/>
                                <p><input type="file" name="file_images_upload" id="file_images_upload"></p>
                            </div>
                            <div id="file_images_queue"></div>
                       		<shiro:hasPermission name="train:course:fileuploadcontents">
                       			<p>
                       				<button type="button" id="btn_images" class="btn btn-info" onclick="uploadifclick('images')">
                       					<span class="glyphicon glyphicon-ok"></span>视频封面上传
                       				</button>
                       			</p>
                       		</shiro:hasPermission>
	                        
	                    </form>
	                </div>
	                <!-- 添加视频end -->
	                <!-- 视频列表 begin-->
	                <div id="video">
	                	<!-- 循环遍历 video文件 -->
	                	<c:forEach items="${listvideo}" var="trainLessonContents" varStatus="status">
	                		<div id="${trainLessonContents.contentId}" class="col-xs-6 col-md-3">
		                		<form class="thumbnail">
			                        <img id="imgurl${status.index+1}" style="width:220px;height:120px;" src="${trainLessonContents.coverPic}" alt="video" >
			                        <div class="caption">
			                            <h5>附件名称：${trainLessonContents.name }</h5>
			                            <p>视频时长：${trainLessonContents.contentlength }</p>
			                            <div class="form-group">
				                            <input type="hidden" id="contentId${status.index+1}" name="contentId${status.index+1}" value="${trainLessonContents.contentId}"/>
				                            <img src="${ctxStatic}/ec/images/uploadify.jpg" style="width:120px;height:32px;" onclick="showdiv('${status.index+1}')"/>
                           				 </div>
			                            <shiro:hasPermission name="train:course:deleteuploadcontents">
			                            	<p><button type="button" onclick="ajaxFileDelete('${trainLessonContents.contentId}')" class="btn btn-danger" role="button"><span class="glyphicon glyphicon-remove"></span>删除此文件</button></p>
			                        	</shiro:hasPermission>
			                        </div>
			                    </form>
	                		</div>
	                	</c:forEach>
	                </div>
	                <!-- 视频列表 end-->
	            </div>
	        </div>
	        <!-- pdf列表 -->
	        <div class="ibox-content ">
	            <div class="breadcrumb">PDF文件</div>
	            <div class="row">
	                <!-- 添加pdf -->
	                <div class="col-xs-6 col-md-3">
	                    <form class="thumbnail">
	                        <div class="caption">
	                            <div class="form-group">
	                            	<p>附件上传：</p>
	                                <p><input type="file" name="file_pdf_upload" id="file_pdf_upload"></p>
	                            </div>
	                            <div id="file_pdf_queue"></div>
	                            <shiro:hasPermission name="train:course:fileuploadcontents">
	                       			<p>
	                       				<button type="button" class="btn btn-info" onclick="uploadifclick('pdf')">
	                       					<span class="glyphicon glyphicon-ok"></span>PDF上传
	                       				</button>
	                       			</p>
	                       		</shiro:hasPermission>
	                        </div>
	                    </form>
	                </div>
	                <!-- 添加pdf end -->
	                <!-- pdf列表 begin -->
	                <div id="pdf">
	                	<!-- 循环遍历 pdf -->
	                	<c:forEach items="${listpdf}" var="trainLessonContents">
	                		<div id="${trainLessonContents.contentId}" class="col-xs-6 col-md-3">
	                			<form class="thumbnail">
			                        <img style="width: 220px;height: 120px;" src="${trainLessonContents.coverPic}" alt="pdf">
			                        <div class="caption">
			                            <h5>附件名称：${trainLessonContents.name }</h5>
			                            <br/>
			                            <shiro:hasPermission name="train:course:deleteuploadcontents">
			                            	<p><button type="button" onclick="ajaxFileDelete('${trainLessonContents.contentId}')" class="btn btn-danger" role="button"><span class="glyphicon glyphicon-remove"></span>删除此文件</button></p>
			                            </shiro:hasPermission>
			                        </div>
			                    </form>
	                		</div>		
	                	</c:forEach>
	                </div>
	            </div>
	        </div>
	        <!-- 暂时屏蔽word 2016-3-24 -->
	        <!-- word列表 -->
	        <div class="ibox-content " style="display: none;">
	            <div class="breadcrumb">word文件</div>
	            <div class="row">
	                <!-- 添加word -->
	                <div class="col-xs-6 col-md-3">
	                    <form class="thumbnail">
	                        <div class="caption">
	                            <div class="form-group">
	                            	<p>附件上传：</p>
	                                <p><input type="file" name="file_word_upload" id="file_word_upload"></p>
	                            </div>
	                            <div id="file_word_queue"></div>
	                            <shiro:hasPermission name="train:course:fileuploadcontents">
	                       			<p>
	                       				<button type="button" class="btn btn-info" onclick="uploadifclick('word')">
	                       					<span class="glyphicon glyphicon-ok"></span>WORD上传
	                       				</button>
	                       			</p>
	                       		</shiro:hasPermission>
	                        </div>
	                    </form>
	                </div>
	                <!-- 添加word end-->
	                <!-- word列表 begin -->
	                <div id="word">
	                	<c:forEach items="${listword}" var="trainLessonContents">
	                		<div id="${trainLessonContents.contentId}" class="col-xs-6 col-md-3">
	                			<form class="thumbnail">
			                        <img style="width: 220px;height: 120px;" src="${trainLessonContents.coverPic}" alt="word">
			                        <div class="caption">
			                            <h5>附件名称：${trainLessonContents.name }</h5>
			                            <shiro:hasPermission name="train:course:deleteuploadcontents">
			                            	<p style="padding:20px;"><button type="button" onclick="ajaxFileDelete('${trainLessonContents.contentId}')" class="btn btn-danger" role="button"><span class="glyphicon glyphicon-remove"></span>删除此文件</button></p>
			                            </shiro:hasPermission>
			                        </div>
			                    </form>
	                		</div>
	                	</c:forEach>
	                </div>
	            </div>
	        </div>
	    </div>
	    <div class="loading"></div>
	</div>
	<div class="W" id="Wrap" style="display:none;">
		<div class="Wrap">
			<div class="Title">
				<h3 class="MainTit" id="MainTit"></h3>
				<a href="javascript:closeuploadImg()" title="关闭" class="Close"></a>
			</div>
			<div class="Cont">
				<p class="Note">最多上传<strong>1</strong>个附件,单文件最大<strong>10M</strong>,类型<strong>BMP,JPG,PNG,GIF</strong></p>
				<div class="flashWrap">
					
				</div>
				<input type="file" id="file_specitemimg_upload" name="Filedata"/>
				<div id="file_specitemimg_queue"></div>
				<div class="fileWarp">
					<fieldset>
						<legend>列表</legend>
						<img id="specitemimgsrc" src="" style="width: 200px;height: 200px;display: none;"/>
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
	
	
</body>
</html>