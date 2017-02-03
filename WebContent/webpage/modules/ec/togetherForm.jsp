<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>tab_banner图</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	
	<!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<script type="text/javascript">
	  function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		$(function(){
			$(".imgUrl img").each(function(){
				var $this = $(this),
					$src = $this.attr('data-src');  
				$this.attr({'src':$src})
			});
			
		});
		
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		    if($("#tabBg").val() == null || $("#tabBg").val() == ""){
			   top.layer.alert('背景图不可为空！', {icon: 0, title:'提醒'});
			   return false;
		    }
	    	if(validateForm.form()){
	    		loading("正在提交，请稍候...");
				$("#inputForm").submit();
		    	return true;
	    	}
	    	return false;
	    }
		$(document).ready(function() {
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
						<form id="inputForm" action="${ctx}/ec/tab_banner/save">
							<input class="form-control" id="tabBackgroundId" name="tabBackgroundId" type="hidden" value="${tabBackground.tabBackgroundId}"/>
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<td><label class="pull-right"><font color="red">*</font>标签组名称：</label></td>
									<td>
										<input class="form-control required" id="groupName" name="groupName" type="text" value="${tabBackground.groupName }" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>背景图：</label></td>
									<td>
										<img id="img" src="${tabBackground.tabBg }" alt="" style="width: 200px;height: 100px;"/>
										<input class="form-control" id="tabBg" name="tabBg" type="hidden" value="${tabBackground.tabBg }"/><!-- 图片隐藏文本框 -->
										<input type="file" name="file_img_upload" id="file_img_upload">
										<div id="file_img_queue"></div>
									</td>
								</tr>
									<tr>
									<td><label class="pull-right">顶部样式名称：</label></td>
									<td>
										<input class="form-control required" id="topStyle" name="topStyle" type="text" value="${tabBackground.topStyle }"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right">样式名称：</label></td>
									<td>
										<input class="form-control required" id="tabNameStyle" name="tabNameStyle" type="text" value="${tabBackground.tabNameStyle }" />
									</td>
								</tr>
								<tr>
									<td><label class="pull-right">未选中样式颜色：</label></td>
									<td>
										<input class="form-control required" id="tabNameColor" name="tabNameColor" type="text" value="${tabBackground.tabNameColor }" />
									</td>
								</tr>
								<tr>
									<td><label class="pull-right">选中样式颜色：</label></td>
									<td>
										<input class="form-control required" id="tabNameSelectColor" name="tabNameSelectColor" type="text" value="${tabBackground.tabNameSelectColor }"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>备注：</label></td>
									<td>
										<textarea id="remarks" name="remarks" cols="35" rows="4" class="form-control required">${tabBackground.remarks }</textarea>
									</td>
								</tr>
							</table>
						</form>
					</div>
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">内容名称</th>
							<th style="text-align: center;">未选中样式图标</th>
							<th style="text-align: center;">选中样式图标</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="page">
							<tr>
								<td style="text-align: center;">${page.tabBannerId}</td>
								<td style="text-align: center;">${page.tabName}</td>
								<td style="text-align: center;" class="imgUrl"><img alt="" src="${ctxStatic}/images/lazylode.png" data-src="${page.tabIcon}" style="width: 51px;height: 38px;"></td>
								<td style="text-align: center;" class="imgUrl"><img alt="" src="${ctxStatic}/images/lazylode.png" data-src="${page.tabSelectIcon}" style="width: 51px;height: 38px;"></td>
								<td style="text-align: center;">
									<shiro:hasPermission name="ec:tab_banner:view">
										<a href="#" onclick="openDialogView('查看', '${ctx}/ec/tab_banner/newTabBannerForm?tabBannerId=${page.tabBannerId}','600px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
									</shiro:hasPermission>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
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
						$("#tabBg").val(jsonData.file_url);
						$("#img").attr('src',jsonData.file_url); 
					}
				}
			});
		});
	</script>
</body>
</html>