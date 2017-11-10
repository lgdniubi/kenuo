<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>主菜单导航栏内容表</title>
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
		    /* if($("#imgUrl").val() == null || $("#imgUrl").val() == ""){
			   top.layer.alert('主菜单导航栏内容表图不可为空！', {icon: 0, title:'提醒'});
			   return false;
		    }
	    	if(validateForm.form()){
	    		loading("正在提交，请稍候...");
				$("#inputForm").submit();
		    	return true;
	    	}
	    	return false; */
	    };
	    
		$(document).ready(function() {
			$("#categoryIsShow").hide();
			$("#goods").hide();

			if($("#type").val() == 3){
				if($("#flag").val() != 'add' && $("#oldIds").val() > 0){
					$("#categoryIsShow").show();
					$("#goods").show();
					
					$("#name").attr("readonly",true);
					$("#categoryId").attr("readonly",true);
					$("#goodsCategoryIdButton").attr("disabled","disabled");
				}
			}
			
			//商品分类选择框
			$("#goodsCategoryIdButton").click(function(){
				// 是否限制选择，如果限制，设置为disabled
				if ($("#goodsCategoryIdButton").hasClass("disabled")){
					return true;
				}
				
				$("#name").val("");
				$("#categoryId").val("");
				
				// 正常打开	
				top.layer.open({
				    type: 2, 
				    area: ['300px', '420px'],
				    title:"选择商品分类",
				    ajaxData:{selectIds: $("#goodsCategoryIdId").val()},
				    content: "${ctx}/tag/treeselect?url="+encodeURIComponent("/ec/goodscategory/treeData?positionType=1")+"&module=&checked=&extId=&isAll=" ,
				    btn: ['确定', '关闭']
		    	       ,yes: function(index, layero){ //或者使用btn1
								var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
								var ids = [], names = [], nodes = [];
								if ("" == "true"){
									nodes = tree.getCheckedNodes(true);
								}else{
									nodes = tree.getSelectedNodes();
								}
								for(var i=0; i<nodes.length; i++) {//
									if (nodes[i].isParent){
										//top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
										//layer.msg('有表情地提示');
										top.layer.msg("不能选择父节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
										return false;
									}//
									ids.push(nodes[i].id);
									names.push(nodes[i].name);//
									break; // 如果为非复选框选择，则返回第一个选择  
								}
								$("#goodsCategoryIdId").val(ids.join(",").replace(/u_/ig,""));
								$("#goodsCategoryIdName").val(names.join(","));
								$("#goodsCategoryIdName").focus();
								
								$("#name").val($("#goodsCategoryIdName").val());
								$("#categoryId").val($("#goodsCategoryIdId").val());
								top.layer.close(index);
						 },
		    		cancel: function(index){ //或者使用btn2
		    	           //按钮【按钮二】的回调
		    	 	 }
				}); 
			
			});
			
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
				}
			);
		});
		
		function selectKind(obj){
			$("#name").val("");
			$("#categoryId").val("");
			
			if(obj == 1){
				$("#categoryIsShow").hide();
				$("#goods").hide();
				$("#name").attr("readonly",false);
				$("#categoryId").val("0");
			}else if(obj == 2){
				if($("#type").val() == 3){
					$("#categoryIsShow").show();
					$("#goods").show();
					
					$("#name").attr("readonly",true);
					$("#categoryId").attr("readonly",true);
				}
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
						<form:form id="inputForm" modelAttribute="webMainmenuNavbarContent" action="${ctx}/ec/webMainmenuNavbarContent/save">
							<form:hidden path="webMainmenuNavbarContentId"/>
							<form:hidden path="mainmenuId"/>
							<input id="type" name="type" value="${type}" type="hidden"/>
							<input id="flag" name="flag" value="${flag}" type="hidden"/>
							<input id="oldIds" name="oldIds" value="${webMainmenuNavbarContent.categoryId}" type="hidden"/>
							
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<c:if test="${type == 3 && flag == 'add'}">
									<tr>
										<td><label class="pull-right"><font color="red">*</font>选择类型：</label></td>
										<td>
											<select id="kind" name="kind" class="form-control" style="width:185px;" onchange="selectKind(this.value)">
												<option value="1">普通</option>
												<option value="2">特殊</option>
											</select>
										</td>
									</tr>
								</c:if>
								<tr id="goods">
									<td><label class="pull-right"><font color="red">*</font>商品分类：</label></td>
									<td>
										<div  style="width:200px;">
											<input id="goodsCategoryIdId" class="form-control required" type="hidden" value="" name="goodsCategoryId" aria-required="true">
											<div class="input-group">
												<input id="goodsCategoryIdName" class="form-control required" type="text" style="" data-msg-required="" value="${webMainmenuNavbarContent.name}" readonly="readonly" name="goodsCategory.name" aria-required="true"> <span class="input-group-btn">
													<button id="goodsCategoryIdButton" class="btn btn-primary " type="button">
														<i class="fa fa-search"></i>
													</button>
												</span>
											</div> 
											<label id="goodsCategoryIdName-error" class="error" style="display: none" for="goodsCategoryIdName"></label>
										</div>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>名称：</label></td>
									<td>
										<form:input path="name" id="name" class="form-control required" style="width: 300px"/>
									</td>
								</tr>
								<tr id="categoryIsShow">
									<td><label class="pull-right"><font color="red">*</font>分类id：</label></td>
									<td>
										<form:input path="categoryId" class="form-control" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>图片：</label></td>
									<td>
										<img id="img" src="${webMainmenuNavbarContent.imgUrl}" alt="" style="width: 200px;height: 100px;"/>
										<input class="form-control" id="imgUrl" name="imgUrl" type="hidden" value="${webMainmenuNavbarContent.imgUrl}"/><!-- 图片隐藏文本框 -->
										<input type="file" name="file_img_upload" id="file_img_upload">
										<div id="file_img_queue"></div>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>链接：</label></td>
									<td>
										<form:input path="redirectUrl" class="form-control" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>排序：</label></td>
									<td>
										<form:input path="sort" class="form-control required" style="width: 300px"/>
									</td>
								</tr>
							</table>
						</form:form>
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
				'width' : 140,
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
						$("#imgUrl").val(jsonData.file_url);
						$("#img").attr('src',jsonData.file_url); 
					}
				}
			});
		});
	</script>
</body>
</html>