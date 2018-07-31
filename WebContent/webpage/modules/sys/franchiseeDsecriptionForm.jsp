<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/loading.css">
    
	<!-- 截图 -->
	<link href="${ctxStatic}/jquery.imgareaselect-0.9.10/css/imgareaselect-default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${ctxStatic}/jquery.imgareaselect-0.9.10/scripts/jquery.imgareaselect.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery.imgareaselect-0.9.10/scripts/jquery.imgareaselect.pack.js"></script>
	
	<!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<!-- 富文本框 -->
	<link rel="stylesheet" href="${ctxStatic}/kindEditor/themes/default/default.css" />
	<script src="${ctxStatic}/kindEditor/kindeditor-all.js" type="text/javascript"></script>
	
	<!-- 富文本框中新增的东西：上传照片，上传代码，商品分类，商品选择 -->
	<script src="${ctxStatic}/kindEditor/themes/editJs/editJs.js" type="text/javascript"></script>
	
	<!-- 富文本框上传图片样式引用 -->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/kindEditor/themes/editCss/edit.css">
	<!-- 富文本框上传图片样式 -->
	
	<script type="text/javascript">
		var newUploadURL = '<%=uploadURL%>';
		
		function doSubmit(){
			var content = $(".ke-edit-iframe").contents().find(".ke-content").html();
			if(content.indexOf("style") >=0){
				content = content.replace("&lt;style&gt;","<style>");
				content = content.replace("&lt;/style&gt;","</style>");
			}
			$("#description").val(content);
			loading("正在提交，请稍候...");
			$("#inputForm").submit();
			return true;
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper-content">
		<form:form id="inputForm" action="${ctx}/sys/franchisee/saveFranchiseeDsecription" method="post" class="form-horizontal">
	    	<input id="id" name="id" value="${franchisee.id}" type="hidden">
	    	<input id="description" name="description" value="${franchisee.description}" type="hidden">
	    	<div class="form-group">
	           <textarea name="description1" id="description1" cols="30" rows="20"></textarea>
	   		</div>
	   </form:form>
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
	<!-- 截图弹出框 -->
	<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" id="ke-dialog-cutPhoto" >
		<div class="modal-dialog modal-lg">
		    <div class="modal-content">
		    	<div class="modal-header">
		    		<button type="button" class="close" onclick="closeCutPhoto()"><span aria-hidden="true">&times;</span></button>
		        	<h4 class="modal-title" id="myModalLabel">裁剪照片</h4>
				</div>
				<div class="modal-body" style="background:url(${ctxStatic}/kindEditor/themes/default/cutImgBg.png);background-size:20px;">
					<div style="width: 350px;height: 350px;line-height:350px;text-align:center; margin: 0 auto">
						<img alt="" src="" style="max-width: 350px;max-height: 350px;border: 1px solid black;" id="cutphoto">
						<input id="x1" name="x1" type="hidden">
						<input id="y1" name="y1" type="hidden">
						<input id="width" name="width" type="hidden">
						<input id="height" name="height" type="hidden">
					</div>
		      	</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="cutPhoto()">确定</button>
		      	</div>
		    </div>
 		</div>
	</div>
	<div class="loading"></div>
	
    <script>
		var editor;
		KindEditor.ready(function(K) {
			editor = K.create('textarea[name="description1"]', {
				width : "100%",
				items : ['undo', 'redo', '|','plainpaste','image','media','link','fontname','fontsize','forecolor','hilitecolor','bold','italic','underline','|','justifyleft', 'justifycenter', 'justifyright','justifyfull','|','clearhtml','code','source','|','fullscreen']
			});
		});
		
		function LoadOver(){
			$("#ke-dialog-num").val("1");
			//给富文本框赋值
			var content = $("#description").val();
			if(content.indexOf("style") >=0){
				content = content.replace("<style>","&lt;style&gt;");
				content = content.replace("</style>","&lt;/style&gt;");
			}
			$(".ke-edit-iframe").contents().find(".ke-content").html(content);
		}
		window.onload=LoadOver;
    </script>
</body>
</html>