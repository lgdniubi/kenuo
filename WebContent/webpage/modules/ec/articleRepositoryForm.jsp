<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/loading.css">
	
	<!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	
	<!-- 富文本框 -->
	<link rel="stylesheet" href="${ctxStatic}/kindEditor/themes/default/default.css" />
	<script src="${ctxStatic}/kindEditor/kindeditor-all.js" type="text/javascript"></script>
	<!-- 富文本框上传图片样式 -->
	<style>
		.ke-dalog-addpic{width:450px;position:fixed; z-index:1000; left:50%; top: 50%;margin:-150px 0 0 -225px;display:none;}
		.ke-dalog-addpic .tab1,.ke-dalog-addpic .tab2{display:none;padding-top:15px;}
		.ke-add-mask{position:fixed;z-index:-1;width:100%;height:100%;left:0;top:0;}
    	/* 商品卡片样式 */
    	p{margin:0;padding:0 12px;line-height:26px;color:#1a1a1a;font-size:16px;}
		img{max-width:100%;}
		.mt-item{margin:12px;padding:10px;overflow:hidden;box-shadow:0 1px 4px 2px #f0f0f0}
		.mt-item dt{float:left;width:80px;height:80px;position:relative;overflow:hidden;}
		.mt-item dt img{margin:0 !important;}
		.mt-item dd{margin-left:90px;}
		.mt-item h3{height:30px;line-height:30px; margin-top:6px;font-size:16px;color:#1a1a1a;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;}
		.mt-item p{height:30px;line-height:30px;padding:0; font-size:14px;color:#ebbd30;margin-top:10px;}
		.mt-item p .cost{font-size:20px;}
		.mt-item p .cost i{font-size:12px;}
		.mt-item p .link-btn{float:right;}
		strong{font-weight:bold}
	</style>
	<script type="text/javascript">
	var cateid = 0;  // 分类的id
	$(document).ready(function(){
		$("#imageType").val($("#type").val());
		changeType($("#type").val());
		
		$("#goodsCategoryIdButton").click(function(){
			// 是否限制选择，如果限制，设置为disabled
			if ($("#goodsCategoryIdButton").hasClass("disabled")){
				return true;
			}
			// 正常打开	
			top.layer.open({
			    type: 2, 
			    area: ['300px', '420px'],
			    title:"选择商品分类",
			    ajaxData:{selectIds: $("#goodsCategoryIdId").val()},
			    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/ec/goodscategory/treeData")+"&module=&checked=&extId=&isAll=" ,
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
							
							cateid=$("#goodsCategoryIdId").val();
							
							$("#goodselectId").val("");
							$("#goodselectName").val("");
							$("#goodsdetails").empty();
							top.layer.close(index);
					    	       },
	    	cancel: function(index){ //或者使用btn2
	    	           //按钮【按钮二】的回调
	    	       }
			}); 
		});
		
		$("#goodselectButton").click(function(){
			// 是否限制选择，如果限制，设置为disabled
			if ($("#goodselectButton").hasClass("disabled")){
				return true;
			}
			if(cateid == "0" || cateid == ""){
				top.layer.alert('请先选择商品分类!', {icon: 0, title:'提醒'});
			}else{
				// 正常打开	
				top.layer.open({
				    type: 2, 
				    area: ['300px', '420px'],
				    title:"商品选择",
				    ajaxData:{selectIds: $("#goodselectId").val()},
				    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/ec/goods/treeGoodsData?goodsCategory="+cateid)+"&module=&checked=&extId=&isAll=",
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
								$(".loading").show();
								$("#goodselectId").val(ids.join(",").replace(/u_/ig,""));
								$("#goodselectName").val(names.join(","));
								$("#goodselectName").focus();
								loadGoods($("#goodselectId").val());
								top.layer.close(index);
						    	       },
		    	cancel: function(index){ //或者使用btn2
		    	           //按钮【按钮二】的回调
		    	       }
				}); 
			}
		});
	});
	function loadGoods(num){
		$("#goodsdetails").empty();
		if(num != ""){
			$.ajax({
        		type : 'post',
        		url : '${ctx}/ec/mtmyArticleList/loadGoods?goodsId='+num,
        		dateType: 'text',
        		success:function(data){
    				$("#goodsdetails").append('<dl class="mt-item" data-id="'+data.goodsId+'" action_type="'+data.actionType+'"><dt>'
    					+ '<img src="'+data.originalImg+'" alt="商品图片" style="width: 80px;height: 80px;"></dt>'
    					+ '<dd><h3>'+data.goodsName+'</h3>'
    					+ '<p class="clearfix">'
    					+ '<span class="cost"><i>¥</i>'+data.shopPrice+'</span>'
    					+ '<span class="link-btn">查看详情>> </span>'
    					+ '</p></dd></dl><br>');
    				$(".loading").hide();
        		}
        	})
		}
		$(".loading").hide();
	}
	function choose(num){
		$("#styleCss").val("");
		if(num == 1){
			$("#styleCss").val("<style>\n"
							+ "	p{margin:0;padding:0 12px;line-height:26px;color:#1a1a1a;font-size:16px;}\n"
							+ "	img{max-width:100%;}\n"
							+ "	.mt-item{margin:12px;padding:10px;overflow:hidden;box-shadow:0 1px 4px 2px #f0f0f0}\n"
							+ "	.mt-item dt{float:left;width:80px;height:80px;position:relative;overflow:hidden;}\n"
							+ "	.mt-item dt img{margin:0 !important;}\n"
							+ "	.mt-item dd{margin-left:90px;}\n"
							+ "	.mt-item h3{height:30px;line-height:30px; margin-top:6px;font-size:16px;color:#1a1a1a;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;}\n"
							+ "	.mt-item p{height:30px;line-height:30px;padding:0; font-size:14px;color:#ebbd30;margin-top:10px;}\n"
							+ "	.mt-item p .cost{font-size:20px;}\n"
							+ "	.mt-item p .cost i{font-size:12px;}\n"
							+ "	.mt-item p .link-btn{float:right;}\n"
							+ "strong{font-weight:bold}\n"
							+ "</style>");
		}else if(num == 2){
			$("#styleCss").val("<style>\n"
					+ "	h1{margin:0;padding:0;line-height:40px;font-size:16px;color:#1a1a1a;}\n"
					+ "	p{margin:0;padding:0;line-height:26px;font-size:14px;color:#b2b2b2}\n"
					+ "</style>");
		}
	}
	function changeType(type){
		if(type == 0){
			$("#imagePattern,#img1,#img2,#img3").hide();
		}else if(type == 1){
			$("#imagePattern,#img1").show();
			$("#img2,#img3").hide();
		}else if(type == 2){
			$("#imagePattern,#img1,#img2").show();	
			$("#img3").hide();
		}else if(type == 3){
			$("#imagePattern,#img1,#img2,#img3").show();	
		}else{
			top.layer.alert('首图类型有误！', {icon: 0});
		}
	}
	</script>
</head>
<body class="gray-bg">
<div class="wrapper-content">
    <div class="wrapper wrapper-content">
        <div class="row">
            <div></div>
            <div class="ibox-title font-head">
				<h5>发布文章</h5>
				<h5 style="padding-left: 92%;margin-top: -10px;">
	           		<a href="javascript:history.back(-1)">
	            		<button type="button" class="btn btn-info">返回</button>
	            	</a>
	            </h5> 
			</div>
            <div class="mail-box">
                 <div class="mail-body">
					<form:form id="inputForm" modelAttribute="articleRepository" action="${ctx}/ec/articles/saveArticle" method="post" class="form-horizontal">
						<input type="hidden" id="articleId" name="articleId" value="${articleRepository.articleId}">
						<input type="hidden" id="type" name="type" value="${articleRepository.imageType}">
		                <input type="hidden" id="contents" name="contents" value="${articleRepository.contents}"><!-- 内容 -->
                       <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>标题：</label>
                            <div class="col-sm-8">
                               <form:input path="title" cssClass="form-control required" maxlength="26"/>
						    </div>
					   </div>
					   <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>短标题：</label>
                            <div class="col-sm-8">
                           		<form:input path="shortTitle" cssClass="form-control required" maxlength="8"/>
						    </div>
					   </div>
					   <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>关键词：</label>
                            <div class="col-sm-8">
                           		<form:input path="keywords" cssClass="form-control required" maxlength="16"/>
						    </div>
					   </div>
					   <div class="form-group">
                           <label class="col-sm-2 control-label"><font color="red">*</font>作者：</label>
                           <div class="col-sm-8">
                           		<form:input path="authorName" cssClass="form-control required" maxlength="20"/>
						   </div>
					   </div>
					   <div class="form-group">
                           <label class="col-sm-2 control-label"><font color="red">*</font>作者头像：</label>
                           <div class="col-sm-8">
                           		<input type="file" name="file_photo_upload" id="file_photo_upload">
                           		<input type="hidden" id="authorPhoto" name="authorPhoto" value="${articleRepository.authorPhoto}" class="form-control" style="width: 350px;" >   
								<div id="file_photo_queue" style="margin top:10px;"></div> 
								<img id="authorPhotoSrc" src="${articleRepository.authorPhoto}" alt="" style="width: 200px;height: 100px;"/>
						   </div>
					   </div>
					   <div class="form-group">
                   	  	  <label class="col-sm-2 control-label"><font color="red">*</font>分类：</label>
                  	  	  <div class="col-sm-8">
	                          <select class="form-control required" id="categoryId" name="categoryId">
								   <option value=0>请选择分类</option>
								   <c:forEach items="${categoryList}" var="categoryList">
								   	   <c:choose>
											<c:when test="${articleRepository.categoryId eq categoryList.categoryId}">
												<option value="${categoryList.categoryId}" selected="selected">${categoryList.name}</option>
											</c:when>
											<c:otherwise>
												<option value="${categoryList.categoryId}">${categoryList.name}</option>
											</c:otherwise>
										</c:choose>
								   </c:forEach>
							  </select>
						  </div>
					   </div>
					   <div class="form-group">
                           <label class="col-sm-2 control-label"><font color="red">*</font>摘要：</label>
                           <div class="col-sm-8">
                           		<form:input path="digest" cssClass="form-control required" maxlength="50"/>
						   </div>
					   </div>
					   <div class="form-group">
                           <label class="col-sm-2 control-label"><font color="red">*</font>内容：</label>
                           <div class="col-sm-8">
                         		<textarea name="content1" id="editor1" cols="30" rows="20"></textarea>
						   </div>
					   </div>
					   <div class="form-group">
                           <label class="col-sm-2 control-label"><font color="red">*</font>首图模式：</label>
                           <div class="col-sm-8">
                         		<select id="imageType" name="imageType" onchange="changeType(this.value)" class="form-control required">
                         			<option value=0>无图模式</option>
                         			<option value=1>单图模式</option>
                         			<option value=2>双图模式</option>
                         			<option value=3>三图模式</option>
                         		</select>
						   </div>
					   </div>
					   <div class="form-group" id="imagePattern">
                           <label class="col-sm-2 control-label"><font color="red">*</font>选择首图：</label>
                           <div class="col-sm-8">
                         		<c:forEach items="${articleRepository.imageList}" var="list" varStatus="status">
                         			<div id="img${status.index+1}">
					  					<img id="livesrc${status.index+1}" src="${list.imgUrl}" alt="images" style="width:200px;height:100px;"/>
						  				<form:input type="hidden" path="articleImage.imgUrl" class="photo${status.index+1}" name="photo${status.index+1}" value="${list.imgUrl}"/>
										<input type="file" name="file_live_upload${status.index+1}" id="file_live_upload${status.index+1}" onclick="a('${status.index+1}')">
									</div>
                         		</c:forEach>
						   </div>
					   </div>
			    	   <div class="form-group">
            	 	   		<label class="col-sm-2 control-label"><font color="red">*</font>注：</label>
            	 	 		<div class="col-sm-8">
	            	 	 	1、<img alt="" src="${ctxStatic}/kindEditor/themes/default/tag.png" style="width: 16px;height: 16px;">此按钮为插入商品卡片按钮；<br>
	            	 	 	2、在插入商品卡片时需点击插入代码按钮(<img alt="" src="${ctxStatic}/kindEditor/themes/default/code.png" style="width: 16px;height: 16px">)插入商品卡片样式；<br>
	            	 	 	3、编辑商品详情时需点击插入代码按钮(<img alt="" src="${ctxStatic}/kindEditor/themes/default/code.png" style="width: 16px;height: 16px">)插入商品详情样式；<br>
	            	 	 	4、若为新样式需点击插入代码按钮(<img alt="" src="${ctxStatic}/kindEditor/themes/default/code.png" style="width: 16px;height: 16px">)自行编辑自定义样式；
	            	 	    </div>
            	 	   </div>
				    </form:form>
            	 </div>
	             <div class="mail-body text-right tooltip-demo">
           				<button type="button" class="btn btn-primary  btn-sm" onclick="sendLetter()"><i class="fa fa-reply"></i>确认添加</button>
                  		<a href="javascript:history.back(-1)" class="btn btn-danger btn-sm" data-toggle="tooltip" data-placement="top" title="放弃"><i class="fa fa-times"></i> 放弃</a>
	             </div>
	             <div class="clearfix"></div>
            </div>
        </div>
    </div>
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
						<input type="text" readonly="readonly" id="img" name="img" value="" class="form-control" style="width: 350px;" >   
						<div id="file_img_queue" style="margin top:10px;"></div> 
						<img id="imgsrc" src="" alt="" style="width: 200px;height: 100px;"/> 
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
	<!-- 富文本框自定义商品标签弹出框 -->
	<div class="ke-dialog-default ke-dialog ke-dalog-addpic" id="ke-dialog-shoptag">
		<div class="ke-dialog-content">
			<div class="ke-dialog-header">商品卡片<span class="ke-dialog-icon-close" id="closeShopTag" title="关闭"></span></div>
			<div class="ke-dialog-body">
				<div class="ke-tabs navbar-form" style="padding:20px;">
					<div style="width: 100%;">
						商品分类：
						<input id="goodsCategoryIdId" class="form-control" type="hidden" value="" name="goodsCategoryId" aria-required="true">
						<div class="input-group">
							<input id="goodsCategoryIdName" class="form-control" type="text" style="" data-msg-required="" value="" readonly="readonly" name="goodsCategory.name" aria-required="true"> <span class="input-group-btn">
								<button id="goodsCategoryIdButton" class="btn btn-primary "
									type="button">
									<i class="fa fa-search"></i>
								</button>
							</span>
						</div> 
						<label id="goodsCategoryIdName-error" class="error" style="display: none" for="goodsCategoryIdName"></label>
					</div>
					<div style="width: 100%;padding-top: 10px;">
						商品选择：
						<input id="goodselectId" name="goodsid" class="form-control required" type="hidden" value="" aria-required="true">
						<div class="input-group">
							<input id="goodselectName" name="goodsname" readonly="readonly" type="text" value="" data-msg-required="" class="form-control" style="" aria-required="true">
							<span class="input-group-btn">
								<button type="button" id="goodselectButton" class="btn   btn-primary  ">
									<i class="fa fa-search"></i>
								</button>
							</span>
						</div> 
						<label id="goodselectName-error" class="error" for="goodselectName" style="display: none"></label>
					</div>
					<div style="width: 100%;height: 100px" id="goodsdetails"></div>
				</div>
			</div>
			<div class="ke-dialog-footer">
			    <span class="ke-button-common ke-button-outer ke-dialog-yes" title="确定">
			        <input class="ke-button-common ke-button" type="button" value="确定" onclick="saveTag()">
			    </span>
			    <span class="ke-button-common ke-button-outer ke-dialog-no" title="取消">
			        <input class="ke-button-common ke-button" id="newCloseShopTag" type="button" value="取消">
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
	<div class="loading"></div>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
    <script>
	    /**
		 * 自定义上传照片
		 */
		KindEditor.plugin('image', function(K) {
		    var editor = this, self = this, name = 'image';
		    //点击图标时执行
		    editor.clickToolbar(name, function() {
		    //  editor.insertHtml('');
			    	$('#ke-dialog').show();
			});
		});
	    //自定义插入标签
		KindEditor.plugin('shoptag', function(K) {
		    var editor = this, self = this, name = 'shoptag';
		    //点击图标时执行
		    editor.clickToolbar(name, function() {
		    //  editor.insertHtml('');
	    		$("#goodsCategoryIdId,#goodsCategoryIdName,#goodselectId,#goodselectName").val("");
	    		$("#goodsdetails").empty();
		    	$('#ke-dialog-shoptag').show();
			});
		});
		KindEditor.lang({
			shoptag : '商品卡片'
		});
		//自定义插入代码
		KindEditor.plugin('code', function(K) {
		    var editor = this, self = this, name = 'code';
		    //点击图标时执行
		    editor.clickToolbar(name, function() {
		    //  editor.insertHtml('');
		    	$("#selectOption").val("");
		    	$("#styleCss").val("");
		    	$('#ke-dialog-code').show();
			});
		});
		var editor;
		KindEditor.ready(function(K) {
			editor = K.create('textarea[name="content1"]', {
				width : "100%"
			});
		});
		$("#close,#newClose,#closeShopTag,#newCloseShopTag,#closeCode,#newCloseCode").click(function(){
			$("#imgsrc").attr('src',null); 
			$("#img,#w_httpImg,#h_httpImg").val('');
			$("#httpImg").val('http://');
			$('#ke-dialog').hide();
			
			$("#goodsCategoryIdId,#goodsCategoryIdName,#goodselectId,#goodselectName").val("");
    		$("#goodsdetails").empty();
			$('#ke-dialog-shoptag').hide();
			
			$("#styleCss").val("");
			$('#ke-dialog-code').hide();
		});
		function saveImg(){
			if("1" == $("#ke-dialog-num").val()){
				if($("#httpImg").val() != "http://" && $("#httpImg").val() != ""){
					editor.insertHtml('<img alt="" src="'+$("#httpImg").val()+'" style="width:'+$("#w_httpImg").val()+'px;height:'+$("#h_httpImg").val()+'px;">');
				}
				$("#imgsrc").attr('src',null); 
				$("#img,#w_httpImg,#h_httpImg").val('');
				$("#httpImg").val('http://');
				$('#ke-dialog').hide();
			}else if("2" == $("#ke-dialog-num").val()){
				if($("#img").val() != ""){
					editor.insertHtml('<img alt="" src="'+$("#img").val()+'">');
				}
				$("#imgsrc").attr('src',null); 
				$("#img,#w_httpImg,#h_httpImg").val('');
				$("#httpImg").val('http://');
				$('#ke-dialog').hide();
			}else{
				$("#imgsrc").attr('src',null); 
				$("#img,#w_httpImg,#h_httpImg").val('');
				$("#httpImg").val('http://');
				$('#ke-dialog').hide();
			}
		}	
		function saveTag(){
			if($("#goodselectId").val() != ""){
				$("#goodsdetails img").removeAttr("style");
				editor.insertHtml($("#goodsdetails").html());
			}
			$('#ke-dialog-shoptag').hide();
		}
		function saveCode(){
			if($("#styleCss").val() != ""){
				var content = $("#styleCss").val();
				if(content.indexOf("style") >=0){
					content = content.replace("<style>","&lt;style&gt;");
					content = content.replace("</style>","&lt;/style&gt;");
				}
				editor.insertHtml(content);
			}
			$('#ke-dialog-code').hide();
		}
		$('.ke-dalog-addpic .ke-tabs-ul li').click(function(){
			$(this).addClass('ke-tabs-li-on ke-tabs-li-selected').siblings().removeClass('ke-tabs-li-on ke-tabs-li-selected')
			if ($(this).index() == '0'){
				$('.ke-dalog-addpic .tab1').show()
				$('.ke-dalog-addpic .tab2').hide();
				$("#ke-dialog-num").val("1");
				
			}else{
				console.log($(this).index())
				$('.ke-dalog-addpic .tab2').show()
				$('.ke-dalog-addpic .tab1').hide();-
				$("#ke-dialog-num").val("2");
			};
		});
		
		var validateForm;
		
		$(document).ready(function() {
			//富文本框上传照片
			$("#file_img_upload").uploadify({
				'buttonText' : '&nbsp;&nbsp;&nbsp;请选择上传图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_img_upload',//<input type="file"/>的name
				'queueID' : 'file_img_queue',//与下面HTML的div.id对应
				'method' : 'post',
				'fileTypeDesc' : '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
				'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '10MB',//上传文件的大小限制
				'multi' : false,//设置为true时可以上传多个文件
				'auto' : true,//点击上传按钮才上传(false)
				'onFallback' : function() {
					//没有兼容的FLASH时触发
					alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
				},
				'onUploadSuccess' : function(file, data, response) {
					var jsonData = $.parseJSON(data);//text 转 json
					if (jsonData.result == '200') {
						$("#img").val(jsonData.file_url);
						$('.img').attr('href', jsonData.file_url);
						$("#imgsrc").attr('src', jsonData.file_url);
					}
				}
			});
			//文章作者头像
			$("#file_photo_upload").uploadify({
				'buttonText' : '&nbsp;&nbsp;&nbsp;请选择上传图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_photo_upload',//<input type="file"/>的name
				'queueID' : 'file_photo_queue',//与下面HTML的div.id对应
				'method' : 'post',
				'fileTypeDesc' : '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
				'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '10MB',//上传文件的大小限制
				'multi' : false,//设置为true时可以上传多个文件
				'auto' : true,//点击上传按钮才上传(false)
				'onFallback' : function() {
					//没有兼容的FLASH时触发
					alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
				},
				'onUploadSuccess' : function(file, data, response) {
					var jsonData = $.parseJSON(data);//text 转 json
					if (jsonData.result == '200') {
						$("#authorPhoto").val(jsonData.file_url);
						$('.authorPhoto').attr('href', jsonData.file_url);
						$("#authorPhotoSrc").attr('src', jsonData.file_url);
					}
				}
			});
			
			//  文章首图实现裁剪功能后  上传方法进行修改
			$("#file_live_upload1").uploadify({
				'buttonText' : '请选择封面',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_live_upload1',//<input type="file"/>的name
				'queueID' : 'file_user_queue1',//与下面HTML的div.id对应
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
						$(".photo1").val(jsonData.file_url);
						$("#livesrc1").attr('src',jsonData.file_url); 
					}
				}
			});
			
			$("#file_live_upload2").uploadify({
				'buttonText' : '请选择封面',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_live_upload2',//<input type="file"/>的name
				'queueID' : 'file_user_queue2',//与下面HTML的div.id对应
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
						$(".photo2").val(jsonData.file_url);
						$("#livesrc2").attr('src',jsonData.file_url); 
					}
				}
			});
			
			$("#file_live_upload3").uploadify({
				'buttonText' : '请选择封面',
				'method' : 'post',
				'swf': '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_live_upload3',//<input type="file"/>的name
				'queueID' : 'file_user_queue3',//与下面HTML的div.id对应
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
						$(".photo3").val(jsonData.file_url);
						$("#livesrc3").attr('src',jsonData.file_url); 
					}
				}
			});
			
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
		function sendLetter(){
			var content = $(".ke-edit-iframe").contents().find(".ke-content").html();
			if(content.indexOf("style") >=0){
				content = content.replace("&lt;style&gt;","<style>");
				content = content.replace("&lt;/style&gt;","</style>");
			}
			$("#contents").val(content);
			if($("#contents").val().length >= 15000){
				top.layer.alert('文章内容过长！', {icon: 0});
			}else if($("#authorPhoto").val() == ""){
				top.layer.alert('作者头像不能为空！', {icon: 0});
			}else if($("#categoryId").val() == 0){
				top.layer.alert('文章分类不能为空！', {icon: 0});
			}else if($("#imageType").val() == 1 && $(".photo1").val() == ""){
				top.layer.alert('首图不能为空！', {icon: 0});
			}else if($("#imageType").val() == 2 && ($(".photo1").val() == "" || $(".photo2").val() == "")){
				top.layer.alert('首图不能为空！', {icon: 0});
			}else if($("#imageType").val() == 3 && ($(".photo1").val() == "" || $(".photo2").val() == "" || $(".photo3").val() == "")){
				top.layer.alert('首图不能为空！', {icon: 0});
			}else if($("#contents").val() == ""){
				top.layer.alert('文章内容不能为空！', {icon: 0});
			}else{
				if(validateForm.form()){
		    		loading("正在提交，请稍候...");
					$("#inputForm").submit();
		    	}
			}
		}
		window.onload=LoadOver;
		
    </script>
</body>
</html>