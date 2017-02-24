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
    
    <!-- 日期控件 -->
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<!-- 截图 -->
	<link href="${ctxStatic}/jquery.imgareaselect-0.9.10/css/imgareaselect-default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${ctxStatic}/jquery.imgareaselect-0.9.10/scripts/jquery.imgareaselect.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery.imgareaselect-0.9.10/scripts/jquery.imgareaselect.pack.js"></script>
	
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
		.delAuthor{position:relative;}
		.delAuthor:hover::after{display:block}
		.delAuthor::after{content:'删除此作者';display:none;cursor:pointer; position:absolute;width:100%;height:100%;text-align:center;line-height:120px;color:#fff;font-size:14px; left:0;top:0; background-color:rgba(0,0,0,.2);background-image:url(${ctxStatic}/kindEditor/themes/default/delAuthor.png);background-position:right top;background-repeat:no-repeat;}
		.layer-date{vertical-align: middle;}
		.allImage div{cursor:pointer}
		.allImage .queryImg{position:relative;}
		.allImage .queryImg::after{position:absolute;content:'';width:25px;height:25px;right:0;top:-30px;background:url(${ctxStatic}/kindEditor/themes/default/imgGou.png) center;background-size:25px;}
	</style>
	<script type="text/javascript">
	var cateid = 0;  // 分类的id
	var imgType = 0;
	$(document).ready(function(){
		imgType = $("#type").val();
		newChangeType($("#type").val());
		
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
		
		$("input[type=radio][name=imageType]").click(function(){
			if(imgType != $(this).val()){
				imgType = $(this).val();
				$("#livesrc1,#livesrc2,#livesrc3").attr("src","");
				$(".photo1,.photo2,.photo3").val("");
				newChangeType(imgType);
			}
		})
		// 验证作者头像
		var oldPhoto = $("#oldAuthorPhoto").val();
		var oldName = $("#oldAuthorName").val();
		$("input[type=radio][name=authorList][value='"+oldPhoto+"']").attr("checked",'checked');
		if($("input[name=authorList]:checked").length == 1){
			$("#authorPhoto").val(oldPhoto);
			$("#authorName").val(oldName);
		}
		// 原文章分类可能删除
		$("#categoryId").val($("#oldCategoryId").val());
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
	function newChangeType(type){
		$("input[type=radio][name=imageType][value="+type+"]").attr("checked",'checked');
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
	function selectCommCate(num){
		$("#oldCategoryId").val(0);
		$("#cate"+num).css("background","#5BC0DE").siblings().css("background","");
		$("#categoryId").val(num);
	}
	function selectCate(num){
		$("#allCate").find(".cate").css("background","");
		$("#categoryId").val(num);
	}
	//发布作者 选择头像 单选框
	function postAuthor(elem){
		var $parent =  $(elem).parent();
		var	imgUrl = $parent.siblings('li').find('img').attr('src');
		var name = $parent.siblings('li').text();
		if(imgUrl.length > 0 && name.length > 0){
			$("#authorPhoto").val(imgUrl);
			$("#authorName").val(name);
		}else{
			$(elem).removeAttr("checked");
			top.layer.alert('选择作者有误！', {icon: 0});
		}
	}
	// 发布文章  选择妃子校分类
	function changeTrainCate(num){
    	$("#trainsCategoryId").val(num); 
    	$("#allTrainCate").find(".trainCate").css("background","");
    }
	// 发布文章  选择妃子校常用分类
	function selectTrainCommCate(num){
		$("#oldTrainsCategoryId").val(0);
		$("#trainCate"+num).css("background","#5BC0DE").siblings().css("background","");
		$("#trainsCategoryId").val(num); 
	}
	// 发布文章  选择每天美耶分类
	function changeMtmyCate(num){
		$("#mtmyCategoryId").val(num); 
		$("#allMtmyCate").find(".mtmyCate").css("background","");
    }
	// 发布文章  选择每天美耶常用分类
	function selectMtmyCommCate(num){
		$("#oldMtmyCategoryId").val(0);
		$("#mtmyCate"+num).css("background","#5BC0DE").siblings().css("background","");
		$("#mtmyCategoryId").val(num); 
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
		                <input id="oldAuthorName" value="${articleRepository.authorName }" type="hidden"> <!-- 原作者名称  --> 
		                <input id="oldAuthorPhoto" value="${articleRepository.authorPhoto }" type="hidden"> <!-- 原作者头像 -->
                       <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>标题： </label>
                            <div class="col-sm-8">
                               <form:input path="title" cssClass="form-control required" maxlength="26" placeholder="请输入标题"/>
						    </div>
					   </div>
					    <div class="form-group">
                           <label class="col-sm-2 control-label">内容：</label>
                           <div class="col-sm-8">
                         		<textarea name="content1" id="editor1" cols="30" rows="20"></textarea>
						   </div>
					   </div>
					  <%--  <div class="form-group">
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
					   </div> --%>
					   <%-- <div class="form-group">
                           <label class="col-sm-2 control-label"><font color="red">*</font>作者：</label>
                           <div class="col-sm-8">
                           		<form:hidden path="authorName" cssClass="form-control required" maxlength="20"/>
						   </div>
					   </div>
					   <div class="form-group">
                           <label class="col-sm-2 control-label"><font color="red">*</font>作者头像：</label>
                           <div class="col-sm-8">
                           		<input type="hidden" id="authorPhoto" name="authorPhoto" value="${articleRepository.authorPhoto}" class="form-control" style="width: 350px;" >   
								<img id="authorPhotoSrc" src="${articleRepository.authorPhoto}" onerror="this.src='${ctxStatic}/kindEditor/themes/default/upload.png'" alt="" style="width: 200px;height: 100px;" onclick="selectAuthorPhoto()"/>
						   </div>
					   </div> --%>
					   <div class="form-group" style="padding:10px 0;" id="postAuthor">
                           <label class="col-sm-2 control-label"><font color="red">*</font>作者：</label>
                           <div class="col-sm-8" style="overflow:hidden">
                           		<div style="float:left;width:120px;height: 160px;text-align:center;margin:0 10px 10px 0" >
                           			<ul>
                           				<li><img alt="" src="http://resource.idengyun.com/resource/images/2017/02/cc019215-b136-4394-b2a0-4d7d4bf0bdd1.png" style='width:120px;height:120px;border:1px solid #ccc'></li>
                           				<li style='height:30px;line-height:40px;'>每天美耶</li>
                           				<li>
                           					<input type="radio" id="authorList" name="authorList" onclick="postAuthor(this)" value="http://resource.idengyun.com/resource/images/2017/02/cc019215-b136-4394-b2a0-4d7d4bf0bdd1.png">
                           				</li>
                           			</ul>
                           		</div>
                           		<div style="float:left;width:120px;height: 160px;text-align:center;margin:0 10px 10px 0" >
	                           		<ul>
	                           			<li><img alt="" src="http://resource.idengyun.com/resource/images/2017/02/80c54153-a0c5-4dd6-bd2e-a362955a392d.png" style='width:120px;height:120px;border:1px solid #ccc'></li>
	                           			<li style='height:30px;line-height:40px;'>美容养生</li>
	                           			<li>
	                           				<input type="radio" id="authorList" name="authorList" onclick="postAuthor(this)" value="http://resource.idengyun.com/resource/images/2017/02/80c54153-a0c5-4dd6-bd2e-a362955a392d.png">
	                           			</li>
	                           		</ul>
                           		</div>
                           		<div style="float:left;width:120px;height: 160px;text-align:center;margin:0 10px 10px 0" >
                           			<ul>
	                           			<li><img alt="" src="http://resource.idengyun.com/resource/images/2017/02/cb3c8c26-2fa6-4314-b18a-41c0b513586d.png" style='width:120px;height:120px;border:1px solid #ccc'></li>
	                           			<li style='height:30px;line-height:40px;'>新浪时尚</li>
	                           			<li>
	                           				<input type="radio" id="authorList" name="authorList" onclick="postAuthor(this)" value="http://resource.idengyun.com/resource/images/2017/02/cb3c8c26-2fa6-4314-b18a-41c0b513586d.png">
	                           			</li>
	                           		</ul>
                           		</div>
                           		<c:forEach items="${authorList }" var="authorList">
                           			<div id="delAuthor${authorList.authorId }" style="float:left;width:120px;height: 160px;text-align:center;margin:0 10px 10px 0">
	                           			<ul>
		                           			<li class="delAuthor" onclick="delAuthor(${authorList.authorId })"><img alt="" src="${authorList.photoUrl }" style="width:120px;height:120px;border:1px solid #ccc"></li>
		                           			<li style='height:30px;line-height:40px;'>${authorList.authorName }</li>
		                           			<li>
		                           				<input type="radio" id="authorList" name="authorList" onclick="postAuthor(this)" value="${authorList.photoUrl }">
		                           			</li>
		                           		</ul>
	                           		</div>
                           		</c:forEach>
                           		<div style="float:left;width:120px;height: 160px;text-align:center;margin:0 10px 10px 0" id="divAuthor">
                           			<ul>
	                           			<li><img alt="" src="" onerror="this.src='${ctxStatic}/kindEditor/themes/default/upload.png'" style='width:120px;height:120px;border:1px solid #ccc'></li>
	                           			<li style='height:30px;line-height:40px;'>
	                           				<input type="button" value="请选择文件" class="btn btn-white btn-sm" onclick="selectAuthorPhoto()">
	                           			</li>
	                           		</ul>
                           		</div>
                           		<input id="authorName" name="authorName" type="hidden">
                           		<input id="authorPhoto" name="authorPhoto" type="hidden">
						   </div>
					   </div>
					   <div class="form-group">
                   	  	  <label class="col-sm-2 control-label"><font color="red">*</font>分类：</label>
                  	  	  <div class="col-sm-8" id="allCate">
	                          <select class="form-control" id="oldCategoryId" name="oldCategoryId" style="width:200px;" onchange="selectCate(this.value)">
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
							  <c:forEach items="${commonCate }" var="commonCate">
							  		<input type="button" value="${commonCate.name }" onclick="selectCommCate(${commonCate.categoryId })" class="cate btn btn-white btn-sm" id="cate${commonCate.categoryId }">
							  </c:forEach>
							  <input type="hidden" id="categoryId" name="categoryId" value="">
						  </div>
					   </div>
					  <%--  <div class="form-group">
                           <label class="col-sm-2 control-label"><font color="red">*</font>摘要：</label>
                           <div class="col-sm-8">
                           		<form:input path="digest" cssClass="form-control required" maxlength="50"/>
						   </div>
					   </div> --%>
					   <div class="form-group">
                           <label class="col-sm-2 control-label"><font color="red">*</font>封面：</label>
                           <div class="col-sm-8" style="height:35px;line-height:35px;">
							   <input type="radio" checked="checked"  id="imageType" name="imageType" value=0 style="margin: auto;" >无图模式
	                           &nbsp;&nbsp;&nbsp;<input type="radio" id="imageType" name="imageType" value=1 class="from">单图模式
	                           &nbsp;&nbsp;&nbsp;<input type="radio" id="imageType" name="imageType" value=2 class="from">双图模式
	                           &nbsp;&nbsp;&nbsp;<input type="radio" id="imageType" name="imageType" value=3 class="from">三图模式
                           </div>
					   </div>
					   <div class="form-group" id="imagePattern" style="padding:10px 0;">
                          <label class="col-sm-2 control-label"></label>
                           <div style="overflow:hidden">
                         		<c:forEach items="${articleRepository.imageList}" var="list" varStatus="status">
                         			<div id="img${status.index+1}" style="float:left;width:120px;height: 130px;text-align:center;margin:0 10px 10px 0">
                         			<ul>
                         				<li><img id="livesrc${status.index+1}" src="${list.imgUrl}" onerror="this.src='${ctxStatic}/kindEditor/themes/default/upload.png'" alt="images" style="width:200px;height:100px;" /></li>
                         				<li style='height:30px;line-height:40px;'>
                         					<input type="button" value="请选择文件" class="btn btn-white btn-sm" onclick="findAllImg(${status.index+1})" id="allImage${status.index+1}">
                         					<form:input type="hidden" path="articleImage.imgUrl" class="photo${status.index+1}" name="photo${status.index+1}" value="${list.imgUrl}"/>
                         				</li>
                         			</ul>
									</div>
                         		</c:forEach>
						   </div>
					   </div>
				    </form:form>
            	 </div>
	             <div class="mail-body text-center tooltip-demo">
	             		<button type="button" class="btn btn-primary  btn-sm" onclick="sendIssue()"><i class="fa fa-reply"></i>发布</button>
           				<button type="button" class="btn btn-primary  btn-sm" onclick="sendLetter()"><i class="fa fa-reply"></i>保存草稿</button>
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
	<!-- 选择首图弹出框 -->
	<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" id="ke-dialog-allImage" >
		<div class="modal-dialog modal-lg">
		    <div class="modal-content">
		    	<div class="modal-header">
		    		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        	<h4 class="modal-title" id="myModalLabel">确认操作</h4>
				</div>
				<div class="modal-body">
					<div id="allImage" class="allImage"></div>
		      	</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="selectPhoto()">确定</button>
		      	</div>
		    </div>
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
					<div style="width: 410px;height: 410px;line-height:410px;text-align:center; margin: 0 auto">
						<img alt="" src="" style="max-width: 400px;max-height: 400px;border: 1px solid black;" id="cutphoto">
						<input id="x1" name="x1" type="hidden">
						<input id="y1" name="y1" type="hidden">
						<input id="width" name="width" type="hidden">
						<input id="height" name="height" type="hidden">
						<br><span id="cutImgContent"></span>
					</div>
		      	</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="cutPhoto()">确定</button>
		      	</div>
		    </div>
 		</div>
	</div>
	<!-- 发布弹出框 -->
	<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" id="ke-dialog-sendIssue" >
		<div class="modal-dialog modal-lg">
		    <div class="modal-content">
		    	<div class="modal-header">
		    		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        	<h4 class="modal-title" id="myModalLabel"></h4>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-xs-12 col-md-12 "style="height:35px;line-height:35px;" >
							<input type="checkbox" id="checkType" name="checkType" value="train"> 妃子校
						</div>		
						<div class="col-xs-12 col-md-6" style="height:70px;line-height:35px;">
							<span style="float:left;">分类：</span>
							<select id="oldTrainsCategoryId" name="oldTrainsCategoryId" class="form-control" style="width:200px;" onclick="changeTrainCate(this.value)">
								<option value=0>请选择分类</option>
								<c:forEach items="${trainsCategoryList }" var="list">
									<option value="${list.categoryId}">${list.name}</option>
								</c:forEach>
							</select>
							<c:forEach items="${trainCateCommList }" var="trainCateCommList">
								<input type="button" value="${trainCateCommList.name }" onclick="selectTrainCommCate(${trainCateCommList.categoryId })" class="trainCate btn btn-white btn-sm" id="trainCate${trainCateCommList.categoryId }">
							</c:forEach>
							<input id="trainsCategoryId" name="trainsCategoryId" type="hidden">
						</div>
						<div class="col-xs-12 col-md-6" style="height:35px;line-height:35px;">
							<span style="float:left;">发布时间：</span><input id="trainsTaskDate" name="trainsTaskDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" value="<fmt:formatDate value="${articles.taskDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
										onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s}'})"/>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12 col-md-12 "style="height:35px;line-height:35px;" >
							<input type="checkbox" id="checkType" name="checkType" value="mtmy"> 每天美耶
						</div>
						<div class="col-xs-12 col-md-6" style="height:70px;line-height:35px;">
							<span style="float:left;">分类：</span>
							<select id="oldMtmyCategoryId" name="oldMtmyCategoryId" class="form-control" style="width:200px;" onclick="changeMtmyCate(this.value)">
								<option value=0>请选择分类</option>
								<c:forEach items="${mtmyCategoryList }" var="list">
									<option value="${list.id}">${list.name}</option>
								</c:forEach>
							</select>
							<c:forEach items="${mtmyCateCommList }" var="mtmyCateCommList">
								<input type="button" value="${mtmyCateCommList.name }" onclick="selectMtmyCommCate(${mtmyCateCommList.categoryId })" class="mtmyCate btn btn-white btn-sm" id="mtmyCate${mtmyCateCommList.categoryId }">
							</c:forEach>
							<input id="mtmyCategoryId" name="mtmyCategoryId" type="hidden">
						</div>
						<div class="col-xs-12 col-md-6" style="height:35px;line-height:35px;">
							<span style="float:left;">发布时间：</span>
							<input id="mtmyTaskDate" name="mtmyTaskDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" value="<fmt:formatDate value="${articles.taskDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
										onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s}'})"/>
						</div>
					</div>
		      	</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="issue()">确定</button>
		      	</div>
		    </div>
 		</div>
	</div>
	<!-- 作者头像弹出框 -->
	<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" id="ke-dialog-authorPhoto" >
		<div class="modal-dialog modal-lg">
		    <div class="modal-content">
		    	<div class="modal-header">
		    		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        	<h4 class="modal-title" id="myModalLabel">自定义作者</h4>
				</div>
				<div class="modal-body">
					<ul>
						<li>
							<input type="file" name="file_photo_upload" id="file_photo_upload">
							<div id="file_photo_queue" style="margin top:10px;"></div> 
						</li>
						<li>
							<img id="newAuthorPhotoSrc" src="http://resource.idengyun.com/resource/images/2017/02/cc019215-b136-4394-b2a0-4d7d4bf0bdd1.png" alt="" style="width: 100px;height: 100px;"/>
		                    <input type="hidden" id="newAuthorPhoto" name="newAuthorPhoto" value="" class="form-control" style="width: 350px;" >   
						</li>
						<li style='margin-top:15px;'>
		                    <input type="text" id="newAuthorName" name="newAuthorName" placeholder="请输入作者名称" maxlength="5" value="" class="form-control" style="width: 350px;" >
						</li>
					</ul>
		      	</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="changePhoto()">确定</button>
		      	</div>
		    </div>
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
				width : "100%",
				items : ['plainpaste','image','media','link','shoptag','fontname','fontsize','forecolor','bold','italic','underline','|','code','source','|','fullscreen']
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
				$('.ke-dalog-addpic .tab1').hide();
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
						$("#newAuthorPhoto").val(jsonData.file_url);
						$("#newAuthorPhotoSrc").attr('src', jsonData.file_url);
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
			}else if($("input[name=authorList]:checked").length != 1){
				top.layer.alert('作者不能为空！', {icon: 0});
			}else if($("#categoryId").val() == 0){
				top.layer.alert('文章分类不能为空！', {icon: 0});
			}else if($("input[type=radio][name=imageType]:checked").val() == 1 && $(".photo1").val() == ""){
				top.layer.alert('首图不能为空！', {icon: 0});
			}else if($("input[type=radio][name=imageType]:checked").val() == 2 && ($(".photo1").val() == "" || $(".photo2").val() == "")){
				top.layer.alert('首图不能为空！', {icon: 0});
			}else if($("input[type=radio][name=imageType]:checked").val() == 3 && ($(".photo1").val() == "" || $(".photo2").val() == "" || $(".photo3").val() == "")){
				top.layer.alert('首图不能为空！', {icon: 0});
			}else{
				if(validateForm.form()){
		    		loading("正在提交，请稍候...");
		    		$("#inputForm").attr("action","${ctx}/ec/articles/saveArticle");
					$("#inputForm").submit();
		    	}
			}
		}
		window.onload=LoadOver;
		// 自定义作者
		function selectAuthorPhoto(){
			$("#newAuthorPhoto").val("");
			$("#newAuthorName").val("");
			$("#newAuthorPhotoSrc").attr('src', "");
			$("#ke-dialog-authorPhoto").modal('show');
		}
		// 添加作者
		function changePhoto(){
			if($("#newAuthorPhoto").val().length > 0 && $("#newAuthorName").val().length > 0){
				$(".loading").show();//隐藏展示层
				$("#ke-dialog-authorPhoto").modal('hide');
				// 异步加载用户信息
				$.ajax({
					type : "POST",   
					url : "${ctx}/ec/articles/addAuthor",
					data:{'authorName':$("#newAuthorName").val(),'photoUrl':$("#newAuthorPhoto").val()},
					dataType: 'text',
					success: function(str) {
						$(".loading").hide();//隐藏展示层
						var data = $.parseJSON(str);
						if(data.status == 200){
							$("#divAuthor").before("<div id=\"delAuthor"+data.msg+"\" style=\"float:left;width:120px;height: 160px;text-align:center;margin:0 10px 10px 0\">"
									+"<ul><li class='delAuthor' onclick='delAuthor("+data.msg+")'><img alt='' src=\""+$('#newAuthorPhoto').val()+"\" style=\"width:120px;height:120px;border:1px solid #ccc\"></li>"
									+"<li style=\"height:30px;line-height:40px;\">"+$('#newAuthorName').val()+"</li>"
									+"<li><input type=\"radio\" id=\"authorList\" name=\"authorList\"></li></ul><div>");
							if($("input[type=radio][name=authorList]").length >= 8){
								$("#divAuthor").hide();
							}
						}else{
							top.layer.alert('添加作者出现异常!', {icon: 0});
						}
					}
				});  
			}else{
				top.layer.alert('未选取作者头像或作者名称！', {icon: 0});
			}
		}
		// 删除作者
		function delAuthor(num){
			$(".loading").show();//隐藏展示层
			$.ajax({
				type : "POST",   
				url : "${ctx}/ec/articles/delAuthor?authorId="+num,
				dataType: 'text',
				success: function(str) {
					$(".loading").hide();//隐藏展示层
					var data = $.parseJSON(str);
					if(data.status == 200){
						$("#delAuthor"+num).remove();
					}else{
						top.layer.alert('删除作者出现异常!', {icon: 0});
					}
				}
			});  
		}
		var imgUrl = '';
		var num = '';
		// 查询所有文章内的照片
		function findAllImg(s){
			num = s;
			$("#allImage").empty();
			imgUrl = '';
			$(".ke-edit-iframe").contents().find(".ke-content img").each(function(){
				$("#allImage").append("<div style=\"display:inline;margin-left: 5px;\"><img src="+this.src+" style=\"width: 100px;height: 100px;\"></div>");
			})
			$("#ke-dialog-allImage").modal('show');
			$('#allImage div').click(function(){
				$(this).addClass('queryImg').siblings().removeClass('queryImg');
				imgUrl = $(this).find("img").attr('src');
			});
		}
		// 选中图片
		function selectPhoto(){
			if(imgUrl.length > 0){
				$("#ke-dialog-allImage").modal('hide');
				$("#x1,#y1,#width,#height").val("");
				$("#cutImgContent").html("");
				$("#cutphoto").attr("src",imgUrl);
				$("#ke-dialog-cutPhoto").modal('show');
				if($("#imageType").val() == 1){
					cutImg("2:1");
				}else{
					cutImg("1:1");
				}
			}else{
				top.layer.alert('未选择图片!请重新选择!', {icon: 0});
			}
		}
		// 截取照片方法
		function cutImg(s){
			$('#cutphoto').imgAreaSelect({
				aspectRatio: s, 
				onSelectEnd: function (img, selection) {
		            $('input[name="x1"]').val(selection.x1);
		            $('input[name="y1"]').val(selection.y1);
		            $('input[name="width"]').val(selection.width);
		            $('input[name="height"]').val(selection.height); 
		            $("#cutImgContent").html("当前选择尺寸:"+selection.width+"*"+selection.height);
		        }
            }); 
		}
		// 确定截取照片
		function cutPhoto(){
			if($("#x1").val().length > 0 && $("#y1").val().length > 0 && $("#width").val().length > 0 && $("#height").val().length > 0){
				$(".loading").show();
				$.ajax({
					type : "POST",   
					url : "${ctx}/ec/articles/ajaxCutImg",
					data:{'path':imgUrl,'x':$("#x1").val(),'y':$("#y1").val(),'width':$("#width").val(),'height':$("#height").val()},
					dataType: 'text',
					success: function(str) {
						$(".loading").hide();//隐藏展示层
						closeCutPhoto();
						var data = $.parseJSON(str);
						if(data.status == 200){
							$("#livesrc"+num).attr("src",data.msg.file_url);
							$(".photo"+num).val(data.msg.file_url);
						}else{
							top.layer.alert('截图出现异常!', {icon: 0});
						}
					}
				});   
			}else{
				top.layer.alert('未截取图片!请重新截取!', {icon: 0});
			}
		}
		// 关闭截图弹出框
		function closeCutPhoto(){
			$('#cutphoto').imgAreaSelect({
				remove:true
            }); 
			$("#ke-dialog-cutPhoto").modal('hide');
		}
		// 发布文章弹出框
		function sendIssue(){
			var content = $(".ke-edit-iframe").contents().find(".ke-content").html();
			if(content.indexOf("style") >=0){
				content = content.replace("&lt;style&gt;","<style>");
				content = content.replace("&lt;/style&gt;","</style>");
			}
			$("#contents").val(content);
			if($("#contents").val().length >= 15000){
				top.layer.alert('文章内容过长！', {icon: 0});
			}else if($("input[name=authorList]:checked").length != 1){
				top.layer.alert('作者不能为空！', {icon: 0});
			}else if($("#categoryId").val() == 0){
				top.layer.alert('文章分类不能为空！', {icon: 0});
			}else if($("input[type=radio][name=imageType]:checked").val() == 1 && $(".photo1").val() == ""){
				top.layer.alert('首图不能为空！', {icon: 0});
			}else if($("input[type=radio][name=imageType]:checked").val() == 2 && ($(".photo1").val() == "" || $(".photo2").val() == "")){
				top.layer.alert('首图不能为空！', {icon: 0});
			}else if($("input[type=radio][name=imageType]:checked").val() == 3 && ($(".photo1").val() == "" || $(".photo2").val() == "" || $(".photo3").val() == "")){
				top.layer.alert('首图不能为空！', {icon: 0});
			}else{
				if(validateForm.form()){
					$("#ke-dialog-sendIssue").modal('show');
		    	}
			}
		}
		// 确认发布
		function issue(){
			if($("input[type=checkbox][name=checkType]:checked").length == 1){
				if($("input[type=checkbox][name=checkType]:checked").val() == "train"){
					if($("#trainsCategoryId").val() != 0){
						postIssue();
					}else{
						top.layer.alert('未勾选分类！', {icon: 0});
					}
				}else if($("input[type=checkbox][name=checkType]:checked").val() == "mtmy"){
					if($("#mtmyCategoryId").val() != 0){
						postIssue();
					}else{
						top.layer.alert('未勾选分类！', {icon: 0});
					}
				}
			}else if($("input[type=checkbox][name=checkType]:checked").length == 2){
				if($("#trainsCategoryId").val() != 0 && $("#mtmyCategoryId").val() != 0){
					postIssue();
				}else{
					top.layer.alert('未勾选分类！', {icon: 0});
				}
			}else{
				top.layer.alert('未勾选发布方', {icon: 0});
			}
		}
		function postIssue(){
			var str = '';
			$("input[name=checkType]:checked").each(function(){ 
				str = str + $(this).val() + ",";
			}); 
    		loading("正在提交，请稍候...");
    		$("#inputForm").attr("action","${ctx}/ec/articles/confirmIssue?checkType="+str+"&trainsCategoryId="+$("#trainsCategoryId").val()+"&trainsTaskDate="+$("#trainsTaskDate").val()
    				+"&mtmyCategoryId="+$("#mtmyCategoryId").val()+"&mtmyTaskDate="+$("#mtmyTaskDate").val());
			$("#inputForm").submit();
		}
    </script>
</body>
</html>