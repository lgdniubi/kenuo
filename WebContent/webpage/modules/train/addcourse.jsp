<%@page import="com.training.common.config.Global"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
	<head>
		<title>添加课程</title>
		<meta name="decorator" content="default" />
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/css/addcourse.css">
		<!-- 内容上传 引用-->
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
		<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
		<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
		
		<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.js"></script>
		<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js"></script>
		<script type="text/javascript" src="${ctxStatic}/train/js/jquery.range.js"></script>
		<script type="text/javascript" src="${ctxStatic}/train/js/coursemange.js"></script>
		<script type="text/javascript">
			var flag = 1;		//上传进度条
			
			//一级分类改变事件，联动二级分类
			function categorychange(v){
				if(flag == 0){
					$(".loading").show();//打开展示层
				}
				$.ajax({
					type : "POST",   
					url : "${ctx}/train/categorys/listtow?categoryId="+v,
					dataType: 'json',
					success: function(data) {
						flag = 0;
						$(".loading").hide(); //关闭加载层
						$("#categoryId").empty();
						$.each(data.listtow, function(index,item){
							$("#categoryId").prepend("<option value='"+item.categoryId+"'>"+item.name+"</option>");
						});
					}
				});   
			}
			//页面加载事件
			$(document).ready(function() {
				//默认加载第一个分类
				categorychange($("#s1").val());
				
				//过滤一道加载课程ID
				var lessonId = $("#lessonId").val();
				if(null == lessonId || '' == lessonId || 'undefined' == lessonId){
					//自动生成课程ID。后期进行修改
					guidcourse();
				}
				//表单验证
				$("#courseForm").validate();
				
				
				var lessonId = $("#lessonId").val();
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
					'onFallback' : uploadify_onFallback,
					'onUploadSuccess' : function(file, data, response) { 
						var jsonData = $.parseJSON(data);//text 转 json
						if(jsonData.result == '200'){
							$("#coverPic").val(jsonData.file_url);
							$("#coverpicsrc").attr('src',jsonData.file_url); 
						}
					}
				});
				
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
					'onFallback' : uploadify_onFallback,
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
			
			//自动生成课程ID。后期进行修改
			function guidcourse(){
				$.ajax({
					type : "POST",   
					url : "${ctx}/train/course/guidcourse",
					dataType: 'text',
					success: function(data) {
						$("#lessonId").val(data);	
					}
				}); 
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
							var next = $("#"+v+"list").html();  
			                $("#"+v+"list").html("");//清除DIV内容
			                if(v == 'video'){
								//添加DIV-video内容
			                	$("#"+v+"list").append("<div id='"+data.CONTENTSID+"' class='col-xs-6 col-md-3 thumbnail' style='margin-left: 20px;'><img id=\""+data.CONTENTSID+"_img\" style=\"width:220px;height:120px;\" src='' alt='video'><div class='caption'><h5>附件名称："+data.FILENAME+"</h5><p>视频时长："+data.CONTENTLENGTH+"</p><p><button type='button' onclick=\"ajaxFileDelete('"+data.CONTENTSID+"')\" class='btn btn-danger' role='button'><span class='glyphicon glyphicon-remove'></span>删除此文件</button></p></div></div>");
			                }else{
								//添加DIV-word or pdf内容
								$("#"+v+"list").append("<div id='"+data.CONTENTSID+"' class='col-xs-6 col-md-3 thumbnail' style='margin-left: 20px;'><img src=\""+data.COVERPIC+"\" style=\"width:220px;height:120px;\" alt='video'><div class='caption'><h5>附件名称："+data.FILENAME+"</h5><p><button type='button' onclick=\"ajaxFileDelete('"+data.CONTENTSID+"')\" class='btn btn-danger' role='button'><span class='glyphicon glyphicon-remove'></span>删除此文件</button></p></div></div>");
							}
			                $("#"+v+"list").append(next);
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
			
			function close(){
				
			}
			
		</script>
	</head>
	<body>
		<div class="ibox-content">
			<sys:message content="${message}"/>
			<div class="warpper-content">
				<div>
				</div>
				<div class="ibox-title font-head">
					<h5>课程信息</h5>
					<h5 style="padding-left: 92%;margin-top: -10px;">
	           		<a href="javascript:history.back(-1)">
	            		<button type="button" class="btn btn-info">返回</button>
	            	</a>
	            </h5> 
				</div>
				<div id="collapseOne" class="panel-collapse collapse in ibox-content" role="tabpanel" aria-labelledby="headingTwo">
					<div class="panel-body bg-color">
						<ul id="myTab1" class="nav nav-tabs">
							<li class="active"><a href="#contentkc" data-toggle="tab">课程内容</a></li>
						</ul>
						<div id="myTabContent1" class="tab-content">
							<!-- 课程内容 -->
							<form:form id="courseForm" class="tab-pane fade in active" modelAttribute="trainLessons" action="${ctx}/train/course/savecourse" method="post" enctype="multipart/form-data">
								<!-- 前期：页面加载事件中得到课程ID。后期进行修改。需注意 -->
								<input type="hidden" id="lessonId" name="lessonId" value="${trainLessons.lessonId }"/>
								<div class="input-item">
									<span><font color="red">*</font>课程分类：</span> 
									<select class="form-control required" id="s1" name="s1" onchange="categorychange(this.options[this.options.selectedIndex].value)">
										<c:forEach items="${listone}" var="trainCategorys">
											<option value="${trainCategorys.categoryId}">${trainCategorys.name}</option>
										</c:forEach>
									</select>
									<select class="form-control required" id="categoryId" name="categoryId"></select> 
								</div>
								<div class="input-item ">
									<p>
										<font color="red">*</font>课程名称：<input type="text" id="name" name="name" maxlength="20" class="text-item required">
									</p>
									<!-- <p>
										<font color="red">*</font>课程学分：<input type="text" id="lessonScore" name="lessonScore" maxlength="3" class="text-item required digits">
									</p> -->
									<p>
										<font color="red">*</font>推荐类型：<form:select path="showType"  class="form-control">
											<form:options items="${fns:getDictList('lesson_show_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
										</form:select>
									</p>
									<p>
										<font color="red">*</font>课程介绍：<input type="text" id="introduce" name="introduce" maxlength="200" class="text-item required">
									</p>
									<p>
										<font color="red">*</font>排序：<input id="sort" name="sort" class="form-control required" onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="3">
									</p>
								</div>
								<div class="input-item">
									<span><font color="red">*</font>是否显示：</span> 
									<select name="isShow" id="isShow" class="form-control required">
										<option selected value="0">是</option>
										<option value="1">否</option>
									</select>
								</div>
								<div class="input-item">
									<span><font color="red">*</font>是否公开：</span> 
									<select name="isOpen" id="isOpen" class="form-control required">
										<option  value="0">公开</option>
										<option selected value="1">不公开</option>
									</select>
								</div>
								<div class="input-item">
									<span><font color="red">*</font>课程类型：</span> 
									<select id="lessontype" name="lessontype" class="form-control input-item-bootm">
										<option value="1">线上课程</option>
										<option value="2">线上测试</option>
										<option value="3">线下预约</option>
									</select>
									<p>
										<input type="hidden" id="coverPic" name="coverPic" value="">
										课程封面：<img class="input-item-img" id="coverpicsrc" src="" alt="images">
									</p>
								</div>
								<!-- 封面上传 -->
								<div class="upload">
									<input type="file" name="file_course_upload" id="file_course_upload">
								</div>
								<div id="file_course_queue"></div>
								
								<!-- 课程内容添加 -->
								<div id="collapseTwo" class="panel-collapse collapse in panel-body" role="tabpanel" aria-labelledby="headingTwo">
									<ul id="myTab" class="nav nav-tabs input-item">
										<li class="active"><a href="#video" data-toggle="tab">视频</a></li>
										<!-- 暂时屏蔽word上传 2016-3-24 
										<li><a href="#word" data-toggle="tab">Word文档</a></li>
										-->
										<li><a href="#pdf" data-toggle="tab">PDF文档</a></li>
									</ul>
									<div id="myTabContent" class="tab-content">
										<div id="video" class="tab-pane fade in active">
											<!-- 视频 -->
											<div class="row">
												<div class="col-xs-6 col-md-3 thumbnail" style="width: 350px;">
							                        <div class="caption">
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
							                        </div>
												</div>
												<!-- 视频列表  begin-->
												<div id="videolist">
													
												</div>
												<!-- 视频列表  end -->
											</div>
										</div>
										<!-- 暂时屏蔽word上传 2016-3-24 -->
										<div class="tab-pane fade" id="word" style="display: none">
											<!-- word -->
											<div class="row">
												<div class="col-xs-6 col-md-3 thumbnail">
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
												</div>
												<!-- word 列表 begin-->
												<div id="wordlist">
													
												</div>
												<!-- word 列表 end-->
											</div>
										</div>
										<div class="tab-pane fade" id="pdf">
											<!-- pdf -->
											<div class="row">
												<div class="col-xs-6 col-md-3 thumbnail">
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
												</div>
												<!-- pdf 列表 begin -->
												<div id="pdflist">
													
								                </div>
								                <!-- pdf 列表 end -->
											</div>
										</div>
									</div>
								</div>
								<shiro:hasPermission name="train:course:savecourse">
									<input type="submit" value="确认添加" class="btn btn-info">
								</shiro:hasPermission>
							</form:form>
						</div>
					</div>
				</div>
				<div class="loading"></div>
			</div>
		</div>
	</body>
</html>