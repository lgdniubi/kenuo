<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/loading.css">
    
    <!-- 日期控件 -->
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
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
	
	
	<!-- 富文本框上传图片样式引用 -->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/kindEditor/themes/editCss/edit.css">
	<!-- 富文本框上传图片样式 -->
	<script type="text/javascript">
	</script>
</head>
<body class="gray-bg">
<div class="wrapper-content">
    <div class="wrapper wrapper-content">
        <div class="row">
            <div class="ibox-title font-head">
				<h5>添加协议</h5>
			</div>
            <div class="mail-box">
                 <div class="mail-body">
					<form:form id="inputForm" modelAttribute="protocolModel" action="${ctx}/train/protocolModel/save" method="post" class="form-horizontal">
						<input type="hidden" id="id" name="id" value="${protocolModel.id}">
						<input type="hidden" id="type" name="type" value="${protocolModel.type}">
						<input type="hidden" id="isPid" name="isPid" value="${protocolModel.isPid}">
		                <input type="hidden" id="contents" name="content" value="${protocolModel.content}"><!-- 内容 不能删除-->
                       <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>标题： </label>
                            <div class="col-sm-8">
                               <form:input path="name" cssClass="form-control required" maxlength="20" placeholder="请输入标题"/>
						    </div>
                             <input type="checkbox"  name="pids" value="0" id="isPidId"  />是否重新签订
					   </div>
					    <div class="form-group">
                           <label class="col-sm-2 control-label">内容：</label>
                           <div class="col-sm-8">
                         		<textarea name="contents" id="editor1" cols="30" class="form-control required" rows="20"></textarea>
						   </div>
					   </div>
				    </form:form>
            	 </div>
	            <!-- <div class="mail-body text-center tooltip-demo">
	             		<button type="button" class="btn btn-primary  btn-sm" onclick="sendIssue()"><i class="fa fa-reply"></i>发布</button>
            				<button type="button" class="btn btn-primary  btn-sm" onclick="sendLetter()"><i class="fa fa-reply"></i>保存草稿</button> 
                  		<a href="javascript:history.back(-1)" class="btn btn-danger btn-sm" data-toggle="tooltip" data-placement="top" title="放弃"><i class="fa fa-times"></i> 放弃</a>
	             </div>-->
            </div>
        </div>
    </div>
</div>
	<!-- 富文本框上传图片弹出框 -->
	<!-- 富文本框自定义商品标签弹出框 -->
	<!-- 输入代码弹出框 -->
	<!-- 选择首图弹出框 -->
	
    <script>
		var editor;
		KindEditor.ready(function(K) {
			editor = K.create('textarea[name="contents"]', {
				width : "100%",
				items : ['undo', 'redo', '|','plainpaste','link','fontname','fontsize','forecolor','hilitecolor','bold','italic','underline','|','justifyleft', 'justifycenter', 'justifyright','justifyfull','|','clearhtml','source','|','fullscreen']
			});
		});
		
		
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  var content = $(".ke-edit-iframe").contents().find(".ke-content").html();
				if(content.indexOf("style") >=0){
					content = content.replace("&lt;style&gt;","<style>");
					content = content.replace("&lt;/style&gt;","</style>");
				}
				$("#contents").val(content);
				var flag = $("#isPidId").is(':checked');
				if (flag){
					$("#isPid").val("1");
				}else{
					$("#isPid").val("0");
				}
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate();
		});
		function LoadOver(){
			$("#ke-dialog-num").val("1");
			//给富文本框赋值
			var content = $("#contents").val();
			if(content.indexOf("style") >=0){
				content = content.replace("<style>","&lt;style&gt;");
				content = content.replace("</style>","&lt;/style&gt;");
			}
			$(".ke-edit-iframe").contents().find(".ke-content").html(content);
		}
		window.onload=LoadOver;
		// 发布文章弹出框
    </script>
</body>
</html>