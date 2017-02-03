<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>添加商品</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<link rel="stylesheet" href="${ctxStatic}/ec/css/base.css">
	
	<!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js"></script>
	<!-- 引入自定义js -->
	<script type="text/javascript" src="${ctxStatic}/ec/js/goods.js"></script>
	
	<!-- 富文本框 -->
	<link rel="stylesheet" href="${ctxStatic}/kindEditor/themes/default/default.css" />
	<script src="${ctxStatic}/kindEditor/kindeditor-all.js" type="text/javascript"></script>
	<!-- <script type="text/javascript">
		//页面加载时间
		$(document).ready(function() {
			//文本编辑器
			$('.summernote').summernote({
		    	height : 200,
		        lang : 'zh-CN',
		     	//重写上传图片方法
		        onImageUpload : function(files, editor, $editable) {
		        	showTip('请点击图片按钮进行添加图片');
				}
		 	});
		});
    </script> -->
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
		.goodsPectable tr td {
		width:100px;
		padding:10px;
		border-style:solid;
	 	border-top-width:1px;
	 	border-right-width:1px;
	  	border-bottom-width:1px;
	  	border-left-width:1px;
	  }
	</style>
	<script type="text/javascript">
		var cateid = 0;  // 分类的id
		$(document).ready(function(){
			$("#newGoodsCategoryIdButton").click(function(){
				// 是否限制选择，如果限制，设置为disabled
				if ($("#newGoodsCategoryIdButton").hasClass("disabled")){
					return true;
				}
				// 正常打开	
				top.layer.open({
				    type: 2, 
				    area: ['300px', '420px'],
				    title:"选择商品分类",
				    ajaxData:{selectIds: $("#newGoodsCategoryIdId").val()},
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
								$("#newGoodsCategoryIdId").val(ids.join(",").replace(/u_/ig,""));
								$("#newGoodsCategoryIdName").val(names.join(","));
								$("#newGoodsCategoryIdName").focus();
								
								cateid=$("#newGoodsCategoryIdId").val();
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
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">
			<div class="ibox-title">
				<c:if test="${opflag == 'UPDATE'}">
					<h5>修改商品</h5>
				</c:if>
				<c:if test="${opflag == 'ADDPARENT'}">
					<h5>添加商品</h5>
				</c:if>
				<h5 style="padding-left: 92%;margin-top: -10px;">
	           		<a href="${ctx}/ec/goods/list?actionId=${goods.actionId}">
	            		<button type="button" class="btn btn-info">返回</button>
	            	</a>
	            </h5> 
			</div>
			<div class="ibox-content">
				<div class="panel-body">
					<ul class="nav nav-tabs">
		                <li class="active"><a href="#tab_tongyong" data-toggle="tab">通用信息</a></li>
		                <li><a href="#tab_goods_images" data-toggle="tab">商品相册</a></li>
		                <li><a href="#tab_goods_spec" data-toggle="tab">商品规格</a></li>                        
		                <li><a href="#tab_goods_attr" data-toggle="tab">商品属性</a></li>                        
		            </ul>
		          <form:form id="goodsForm" modelAttribute="goods" action="${ctx}/ec/goods/save" method="post">
		            <div class="tab-content" id="myTabContent">
		            	<!-- 通用信息 Begin -->
						<div class="tab-pane fade in active" id="tab_tongyong">
						
								<form:hidden path="goodsId"/>
								<form:hidden path="attrType"/>
								<form:hidden path="specType"/>
								<form:hidden path="actionId"/>
								<ul class="formArea">
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>商品类型：</span>
										<input type="hidden" id="isRealValue" value="${goods.isReal }">
										<input type="radio" id="isReal" name="isReal" value="1" ${(goods.isReal == '1')?'checked="checked"':''} onclick="radiochange(this.value)">虚拟商品
										<input type="radio" id="isReal" name="isReal" value="0" ${(goods.isReal == '0' || goods.isReal == null)?'checked="checked"':''} onclick="radiochange(this.value)">实物商品
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>活动类型：</span>
										<select class="form-control" id="actionType" name="actionType">
					                        <option ${(goods.actionType == 0)?'selected="selected"':''} value="0">普通商品</option>
					                        <option ${(goods.actionType == 1)?'selected="selected"':''} value="1">限时抢购</option>
					                        <option ${(goods.actionType == 2)?'selected="selected"':''} value="2">团购商品</option>
					                        <option ${(goods.actionType == 3)?'selected="selected"':''} value="3">促销优惠</option>
					                    </select>
					                </li>
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>商品名称：</span>
										<form:input path="goodsName" htmlEscape="false" maxlength="150" class="form-control required"/>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>短标题：</span>
										<form:input path="goodsShortName" htmlEscape="false" maxlength="50" class="form-control required"/>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2">商品SEO：</span>
										<form:input path="goodsDescription" htmlEscape="false" maxlength="150" class="form-control"/>
										<span class="control-label cannotEdit">(多条描述以逗号分开)</span>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>商品简单描述：</span>
										<form:input path="goodsRemark" htmlEscape="false" maxlength="150" class="form-control required"/>
										<span class="control-label cannotEdit">(多条描述以逗号分开)</span>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>商品编号：</span>
										<form:input path="goodsSn" htmlEscape="false" maxlength="50" class="form-control required"/>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2">商品关键词：</span>
										<div style="width: 40%;">
											<input type="hidden" id="keywords" name="keywords" value="${goods.keywords }">
											<sys:treeselect id="keywordsSelect" name="keywordsSelect" value="${goods.keywords }" 
												labelName="effect.name" labelValue="${goods.keywords }" 
								     			title="功效" url="/ec/goods/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true" allowInput="true" checked="true"/>
										</div>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2">商品标签：</span>
										<form:input path="goodsTags" htmlEscape="false" maxlength="17" onblur="this.value=ignoreSpaces(this.value);" class="form-control"/>
										<span class="control-label cannotEdit">(多个标签用"#"分开)</span>
									</li>
									
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>排序：</span>
										<form:input path="sort" class="form-control digits" onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" onpaste="this.value=this.value.replace(/[^\d.]/g,'')"/>
										<span class="control-label cannotEdit">(数字越大越靠前)</span>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>所属商家：</span>
										<div style="width: 40%;padding-left: 150px;">
											<sys:treeselect id="franchiseeId" name="franchiseeId" value="${goods.franchisee.id}"
												labelName="franchisee.name" labelValue="${goods.franchisee.name}" 
	 											title="加盟商" url="/sys/franchisee/treeData" 
	 											cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/> 
										</div>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>商品分类：</span>
										<div style="width: 40%;padding-left: 150px;">
						                    <sys:treeselect id="goodsCategoryId" name="goodsCategoryId" value="${goods.goodsCategoryId}" 
												labelName="goodsCategory.name" labelValue="${goods.goodsCategory.name }" 
									     		title="商品分类" url="/ec/goodscategory/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
								     	</div>	
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>商品品牌：</span>
										<select class="form-control" id="goodsBrandId" name="goodsBrandId">
					                        <option value="-1">所有品牌</option>
											<c:forEach items="${goodsBrandList}" var="goodsBrand">
												<option ${(goodsBrand.id == goods.goodsBrandId)?'selected="selected"':''} value="${goodsBrand.id}">${goodsBrand.name}</option>
											</c:forEach>
					                    </select>
					                </li>
					                <li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>市场价：</span>
										<form:input path="marketPrice" maxlength="50" class="form-control required" 
											onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
											onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
											onfocus="if(value == '0.0'){value=''}"
											onblur="if(value == ''){value='0.0'}"/>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>优惠价：</span>
										<form:input path="shopPrice"  maxlength="50" class="form-control required" 
											onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
											onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
											onfocus="if(value == '0.0'){value=''}"
											onblur="if(value == ''){value='0.0'}"/>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>成本价：</span>
										<form:input path="costPrice"  maxlength="50" class="form-control required" 
											onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
											onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
											onfocus="if(value == '0.0'){value=''}"
											onblur="if(value == ''){value='0.0'}"/>
									</li>
									<%-- 
										<li class="form-group">
											<span class="control-label col-sm-2"><font color="red">*</font>剩余库存：</span>
											<form:input path="storeCount"  maxlength="50" class="form-control digits required" onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" onpaste="this.value=this.value.replace(/[^\d.]/g,'')"/>
										</li> 
									--%>
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>商品重量：</span>
										<form:input path="weight"  maxlength="50" class="form-control digits required" 
											onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
											onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
											onfocus="if(value == '0'){value=''}"
											onblur="if(value == ''){value='0'}"/>
										<span class="control-label cannotEdit">(以克为单位)</span>
									</li>					
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>赠送消费积分：</span>
										<form:input path="giveIntegral"  maxlength="50" class="form-control digits required" 
											onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
											onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
											onfocus="if(value == '0'){value=''}"
											onblur="if(value == ''){value='0'}"/>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>商品销量：</span>
										<form:input path="salesSum"  maxlength="50" class="form-control required" 
											onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
											onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
											onfocus="if(value == '0'){value=''}"
											onblur="if(value == ''){value='0'}"/>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2">护理流程：</span>
										<div class="formArea" style="width: 650x;">
											<div class="imgResult" id="goodsNurseImagesDiv">
												<c:forEach items="${goods.goodsImagesList }" var="goodsImages">
													<c:if test="${goodsImages.imageType == 1 }">
														<div style="height: 190px;" class="imgItem" id="imgItem${goodsImages.imgId }">
															<span class="contorl-bar">
																<span class="move-pro" onclick="updateImagesDiv('LEFT','goodsNurseImagesDiv','imgItem${goodsImages.imgId }')"></span>
																<span class="move-last" onclick="updateImagesDiv('RIGHT','goodsNurseImagesDiv','imgItem${goodsImages.imgId }')"></span>
																<span class="move-delelt" onclick="updateImagesDiv('DEL','goodsNurseImagesDiv','imgItem${goodsImages.imgId }')"></span>
															</span>
															<img alt="images" src="${goodsImages.imageUrl }">
															<input type="hidden" value="${goodsImages.imageUrl }" name="goodsNurseImages" id="goodsNurseImages">
															步骤(20个文字):<textarea class="nursetextarea" name="goodsNurseImagesText" id="goodsNurseImagesText" maxlength="20">${goodsImages.imageDesc }</textarea>
														</div>
													</c:if>
												</c:forEach>
											</div>
											<input type="file" name="file_nurseimages_upload" id="file_nurseimages_upload" style="padding-top:200px">
											<div id="file_nurseimages_queue"></div>
										</div>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2">服务时长：</span>
										
										<form:input path="serviceMin" htmlEscape="false" maxlength="150" class="form-control required" 
											onfocus="if(value == '0'){value=''}"
											onblur="if(value == ''){value='0'}"/>
										
										<%-- <select class="form-control" name="serviceMin" id="serviceMin">
											<option value="0"   ${(goods.serviceMin == 0)?'selected="selected"':''}>0</option>
					                        <option value="15"  ${(goods.serviceMin == 15)?'selected="selected"':''}>15分钟</option>
					                        <option value="30"  ${(goods.serviceMin == 30)?'selected="selected"':''}>30分钟</option>
					                        <option value="45"  ${(goods.serviceMin == 45)?'selected="selected"':''}>45分钟</option>
					                        <option value="60"  ${(goods.serviceMin == 60)?'selected="selected"':''}>1小时</option>
					                        <option value="75"  ${(goods.serviceMin == 75)?'selected="selected"':''}>1小时15分钟</option>
					                        <option value="90"  ${(goods.serviceMin == 90)?'selected="selected"':''}>1小时30分钟</option>
					                        <option value="105" ${(goods.serviceMin == 105)?'selected="selected"':''}>1小时45分钟</option>
					                        <option value="120" ${(goods.serviceMin == 120)?'selected="selected"':''}>2小时</option>
					                    </select> --%>
					                    <span class="control-label cannotEdit">非实物类商品(以"分"为单位)</span>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2">商品首图：</span>
										<div style="width: 40%;padding-left: 150px;">
											<img id="originalImgsrc" src="${goods.originalImg}" alt="images" style="width: 200px;height: 100px;"/>
											<input type="hidden" id="originalImg" name="originalImg" value="${goods.originalImg}">
											<c:if test="${opflag != 'VIEW'}">
												<div class="upload">
													<input type="file" name="file_originalImg_upload" id="file_originalImg_upload">
												</div>
												<div id="file_originalImg_queue"></div>
											</c:if>
										</div>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2">设置：</span>
										<input type="checkbox" id="isOnSale"  name="isOnSale" ${(goods.isOnSale == '1')?'checked="checked" value="1"':'value="0"'} onclick="setcheckclick('isOnSale')" > 上架
										<input type="checkbox" id="isFreeShipping" name="isFreeShipping" ${(goods.isFreeShipping == '1')?'checked="checked" value="1"':'value="0"'} onclick="setcheckclick('isFreeShipping')" > 包邮
										<input type="checkbox" id="isRecommend" name="isRecommend" ${(goods.isRecommend == '1')?'checked="checked" value="1"':'value="0"'} onclick="setcheckclick('isRecommend')" > 推荐
										<input type="checkbox" id="isNew" name="isNew" ${(goods.isNew == '1')?'checked="checked" value="1"':'value="0"'} onclick="setcheckclick('isNew')" > 新品
										<input type="checkbox" id="isHot" name="isHot" ${(goods.isHot == '1')?'checked="checked" value="1"':'value="0"'} onclick="setcheckclick('isHot')" > 热卖
										<input type="checkbox" id="isAppshow" name="isAppshow" ${(goods.isAppshow == '1')?'checked="checked" value="1"':'value="0"'} onclick="setcheckclick('isAppshow')" > app显示
									</li>
									
									<li class="form-group">
										<span class="control-label col-sm-2">适用地区：</span>
										<div style="width: 40%;">
											<input type="hidden" id="regionName" name="regionName" value="${goods.regionName }">
											<sys:treeselect id="regionNameSelect" name="regionNameSelect" value="${goods.regionName }" 
												labelName="regionNameSelect" labelValue="${goods.regionName }" 
									     		title="地区" url="/sys/area/findListByPID" cssClass="form-control" notAllowSelectParent="true" checked="true"/>
								     	</div>
									</li>
									
									<li class="form-group">
										<span class="control-label col-sm-2">添加设备标签：</span>
										<div style="width: 40%;padding-left: 150px;">
						                    <sys:treeselect id="equipmentLabel" name="equipmentLabel.id" value="${goods.equipmentLabel.id}" 
												labelName="equipmentLabel.name" labelValue="${goods.equipmentLabel.name }" 
									     		title="设备标签" url="/ec/equipmentLabel/treeData?newFlag=1" cssClass="form-control" allowClear="true" notAllowSelectParent="true" />
								     		<span class="control-label cannotEdit">若为特殊商品则必填</span>
								     	</div>	
								     	
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2">添加技能标签：</span>
										<div style="width: 40%;padding-left: 150px;">
						                    <sys:treeselect id="skill" name="skill.id" value="${goods.skill.id}" 
												labelName="skill.name" labelValue="${goods.skill.name }" 
									     		title="技能标签" url="/sys/skill/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true" checked="true"/>
								     	<span class="control-label cannotEdit">若为特殊商品则必填</span>
								     	</div>	
								     	
									</li>
									
									<li>
										<span class="control-label col-sm-2">商品详情：</span>
										<div class="pro-name textly" style="padding-left: 150px;">
											<!-- <div class="summernote"></div>  原富文本框 -->
											<textarea name="content1" id="editor1" cols="30" rows="20"></textarea>
										</div>
										<!-- 商品详情内容 -->
										<textarea id="goodsContent" name="goodsContent" style="display:none;">${goods.goodsContent }</textarea>
									</li>
									<li>
										<span class="control-label col-sm-2">注：</span>
										<div class="pro-name textly" style="padding-left: 150px;">
											1、<img alt="" src="${ctxStatic}/kindEditor/themes/default/tag.png" style="width: 16px;height: 16px;">此按钮为插入商品卡片按钮；<br>
					            	 	 	2、在插入商品卡片时需点击插入代码按钮(<img alt="" src="${ctxStatic}/kindEditor/themes/default/code.png" style="width: 16px;height: 16px">)插入商品卡片样式；<br>
					            	 	 	3、编辑商品详情时需点击插入代码按钮(<img alt="" src="${ctxStatic}/kindEditor/themes/default/code.png" style="width: 16px;height: 16px">)插入商品详情样式；<br>
					            	 	 	4、若为新样式需点击插入代码按钮(<img alt="" src="${ctxStatic}/kindEditor/themes/default/code.png" style="width: 16px;height: 16px">)自行编辑自定义样式；
										</div>
									</li>
								</ul>
								
						</div>
						<!-- 通用信息 End -->
						<!-- 商品相册 Begin -->
						<div class="tab-pane fade" id="tab_goods_images">
							<%-- <form action="${ctx}/ec/goods/savegoodsimages?actionId=${goods.actionId}" id="goodsImagesForm" method="post"> --%>
								<div class="formArea">
									<div class="imgResult" id="goodsImages">
										<c:forEach items="${goods.goodsImagesList }" var="goodsImages">
											<c:if test="${goodsImages.imageType == 0 }">
												<div class="imgItem" id="imgItem${goodsImages.imgId }">
													<img alt="images" src="${goodsImages.imageUrl }">
													<span class="contorl-bar">
														<span class="move-pro" onclick="updateImagesDiv('LEFT','goodsImages','imgItem${goodsImages.imgId }')"></span>
														<span class="move-last" onclick="updateImagesDiv('RIGHT','goodsImages','imgItem${goodsImages.imgId }')"></span>
														<span class="move-delelt" onclick="updateImagesDiv('DEL','goodsImages','imgItem${goodsImages.imgId }')"></span>
													</span>	
													<input type="hidden" value="${goodsImages.imageUrl }" name="goodsImages" id="goodsImages">
												</div>
											</c:if>
										</c:forEach>
									</div>
									<input type="file" name="file_goodsImages_upload" id="file_goodsImages_upload">
									<div id="file_goodsImages_queue"></div>
								</div>
								<!-- 提交按钮 -->
								<%-- <div class="box-footer">        
									<input type="hidden" id="imggoodsid" name="goodsid" value="${goods.goodsId }">                	
						    		<input type="button" class="btn btn-primary" value="提 交" onclick="goodsImgOnsubmit()">
						    	</div> --%>
							<!-- </form> -->
						</div>
						<!-- 商品相册 End -->
						<!-- 商品规格  Begin -->
						<div class="tab-pane fade" id="tab_goods_spec">
							<%-- <form action="${ctx}/ec/goods/savespec?actionId=${goods.actionId}" id="goodsSpecForm" method="post"> --%>
								<table class="table table-bordered table-hover table-left">
									<tr>
										<td>商品类型:</td>
										<td>
						                    <select class="form-control" style="text-align: center;" id="specTypeId" name="specTypeId" onchange="goodsTypeChange('specgoodsid','SPECTYPEID','goods_spec_table',this.options[this.options.selectedIndex].value)">
												<option value="-1">所有类型</option>
												<c:forEach items="${goodsTypeList}" var="goodsType">
													<option ${(goodsType.id == goods.specType)?'selected="selected"':''} value="${goodsType.id}">${goodsType.name}</option>
												</c:forEach>
											</select>
										</td>
									</tr>
								</table>
								<div id="ajax_spec_data">
									<table class="table table-bordered table-left">
										<tbody>
											<tr>
										        <td colspan="2"><b>商品规格:</b></td>
										    </tr>
									    </tbody>
									    <!-- 根据商品类型 获取商品规格-->
									    <tbody id="goods_spec_table" style="text-align: center;">
									    	<c:if test="${not empty goodsSpectablecontent }">
									    		${goodsSpectablecontent }
									    	</c:if>
									    </tbody>
									</table>
									<div id="goods_spec_table2">
										<c:if test="${empty goodsSpecItemtablecontent}">
											<table class="table table-bordered" id="goods_spec_input_table">
												<tr>
													<td><b>价格</b></td>
									            	<td><b>库存</b></td>
									            	<td><b>条码</b></td>
									            	<td><b>商品编码</b></td>
									            	<td><b>服务次数</b></td>
									            </tr>
											</table>
										</c:if>
										<c:if test="${not empty goodsSpecItemtablecontent }">
											${goodsSpecItemtablecontent }
										</c:if>
									</div>
								</div>
								
								<!-- 提交按钮 -->
								<div class="box-footer">   
									<!-- 商品规格图片上传字段 -->
									<%-- <input type="hidden" id="specgoodsid" name="goodsid" value="${goods.goodsId }"> --%>
									<input type="hidden" id="specarr" name="specarr">  
									<input type="hidden" id="itemimgid" name="itemimgid" value="">                  	
						    		<%-- <input type="button" class="btn btn-primary" value="提 交" onclick="goodsspecOnsubmit()">--%>
						    	</div> 
							<!-- </form> -->
						</div>
						<!-- 商品规格  End -->
						<!-- 商品属性 Begin -->
						<div class="tab-pane fade" id="tab_goods_attr">
							<%-- <form action="${ctx}/ec/goods/saveattribute?actionId=${goods.actionId}" id="goodsattributeForm" method="post"> --%>
								<table class="table table-bordered table-left">
									<thead>
										<tr class="attr_324">
											<td>商品类型：</td>
											<td>
							                    <select class="form-control" style="text-align: center;" id="attrTypeId" name="attrTypeId" onchange="goodsTypeChange('attrgoodsid','ATTRTYPEID','goods_attr_table',this.options[this.options.selectedIndex].value)">
													<option value="-1">所有类型</option>
													<c:forEach items="${goodsTypeList}" var="goodsType">
														<option ${(goodsType.id == goods.attrType)?'selected="selected"':''} value="${goodsType.id}">${goodsType.name}</option>
													</c:forEach>
												</select>
											</td>
										</tr>
									</thead>
									<!-- 根据商品类型 获取商品属性-->
									<tbody id="goods_attr_table">
										<c:if test="${not empty goodsAttributecontent }">
											${goodsAttributecontent }
										</c:if>
									</tbody>
								</table>
								<!-- 提交按钮 -->
							<%-- 	<div class="box-footer">    
									<input type="hidden" id="attrgoodsid" name="goodsid" value="${goods.goodsId }">
						    		<input type="button" class="btn btn-primary" value="提 交" onclick="goodsattributeOnsubmit();">
						    	</div>
							</form> --%>
						</div>
		            	<!-- 商品属性 End -->
		            </div>
		            <!-- 提交按钮 -->
						<div class="box-footer">   
						    <input type="submit" class="btn btn-primary" value="提 交">
						</div>
					</form:form>
				</div>
			</div>
		</div>
		<!-- 页面加载等待 -->
		<div class="loading"></div>
	</div>
	<!-- 商品规格图片上传弹出层 begin -->
	<div class="W" id="Wrap" style="display: none;">
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
	<!-- 商品规格图片上传弹出层 end -->
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
						图片地址:<input type="text" id="httpImg" class="form-control" value="http://" style="margin top:10px;width: 350px;"><br>
						图片宽度:<input type="text" id="w_httpImg" class="form-control" style="margin top:10px;width: 350px;"><br>
						图片高度:<input type="text" id="h_httpImg" class="form-control" style="margin top:10px;width: 350px;">
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
						<input id="newGoodsCategoryIdId" class="form-control" type="hidden" value="" name="newGoodsCategoryId" aria-required="true">
						<div class="input-group">
							<input id="newGoodsCategoryIdName" class="form-control" type="text" style="" data-msg-required="" value="" readonly="readonly" name="newGoodsCategory.name" aria-required="true"> <span class="input-group-btn">
								<button id="newGoodsCategoryIdButton" class="btn btn-primary "
									type="button">
									<i class="fa fa-search"></i>
								</button>
							</span>
						</div> 
						<label id="newGoodsCategoryIdName-error" class="error" style="display: none" for="newGoodsCategoryIdName"></label>
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
	
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	<script type="text/javascript">
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
	    		$("#newGoodsCategoryIdId,#newGoodsCategoryIdName,#goodselectId,#goodselectName").val("");
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
				$('.ke-dalog-addpic .tab2').show()
				$('.ke-dalog-addpic .tab1').hide();
				$("#ke-dialog-num").val("2");
			};
		});
		
		//去空格
		var ignoreSpaces = function(str) {
			return str = str.replace(/\s+/g,"");
		}
	
		//商品类型改变事件
		var radiochange = function(v){
			if(v == 0){
				//实物
				$("#serviceMin").attr("disabled","disabled"); 
			}else if(v == 1){
				//虚拟
				$("#serviceMin").removeAttr("disabled"); 
			}
		}
	
		//商品类型改变事件
		var goodsTypeChange = function(goodsidname,typeName,tableName,typeValue){
			$(".loading").show();//打开展示层
			
			if(typeValue == '-1'){
				//选择所有的时候
				$("#"+tableName).empty();
				$("#goods_spec_table2").empty();//清空table里面的值
				$(".loading").hide(); //关闭加载层
			}else{
				var goodsid = $("#"+goodsidname).val();
				$.ajax({
					type : "POST",
					url : "${ctx}/ec/goods/getattrcontent?TYPENAME="+typeName+"&TYPEVALUE="+typeValue+"&GOODSID="+goodsid,
					dataType: 'json',
					success: function(data) {
						$(".loading").hide(); //关闭加载层
						var status = data.STATUS;
						var tablecontent = data.TABLECONTENT;
						$("#"+tableName).empty();//清空table里面的值
						if("OK" == status){
							$("#"+tableName).append(tablecontent);//填写table
							
							if("SPECTYPEID" == typeName){
								$("#specarr").empty();
								$("#goods_spec_table2").empty();//清空table里面的值
								
								var specitemcontent = data.SPECITEMCONTENT;
								if(null != specitemcontent || !"" == specitemcontent){
									$("#goods_spec_table2").append(specitemcontent);//填写table
								}else{
									$("#goods_spec_table2").append("<table class=\"table table-bordered\" id=\"goods_spec_input_table\"><tr><td><b>价格</b></td><td><b>库存</b></td><td><b>条码</b></td><td><b>商品编码</b></td><td><b>服务次数</b></td></tr></table>");//填写table
								}
							}
						}else if("ERROR" == status){
							//失败
							alert(data.MESSAGE);
						}
					}
				});   
			}
		}	
		
		//商品设置(包邮、新品、推荐。。。)
		var setcheckclick = function(formname){
			if($("#"+formname).is(':checked')){
				$("#"+formname).val(1);
			}else{
				$("#"+formname).val(0);
			}
		}
		
		//删除\左\右切换图片
		var updateImagesDiv = function(action,divname1,divname2){
			if('DEL' == action){
				//删除div
				$("#"+divname1).find("#"+divname2).remove();
			}else{
				//左右改变图片的位置
				var arr = $("#"+divname1).find('div').toArray();
				//判断当前div所属的位置
				var now;
				for(var i in arr){
					if(divname2 == arr[i].id){
						now = parseInt(i);
					}
				}
				var temp ;//临时保存div
				if('LEFT' == action){
					if(now-1 == -1){
						//当当前位置为第一个时,则与最后一个进行调换
						temp = arr[arr.length-1];
						arr[arr.length-1] = arr[now];
					}else{
						//左移动	
						temp = arr[now-1];//左移动减1
						arr[now-1] = arr[now];
					}
				}else if('RIGHT' == action){
					if(now+1 == arr.length){
						//当当前位置为最后一个时，则与第一个进行调换
						temp = arr[0];
						arr[0] = arr[parseInt(now)];
					}else{
						//右移动
						temp = arr[now+1];//右移动加1
						arr[now+1] = arr[now];
					}
				}
				arr[now] = temp;
				$("#"+divname1).html(arr);
			}
		}
		
		//图片提交
		var goodsImgOnsubmit = function(){
			var goodsid = $("#imggoodsid").val();
			if("0" != goodsid && goodsid.length > 0){
				$("#goodsImagesForm").submit();
			}else{
				alert("无法提交");
			}
		}
		
		//规格提交
		var goodsspecOnsubmit = function(){
		//	var goodsid = $("#goodsid").val();
// 			if("0" != goodsid && goodsid.length > 0){
				
				var spec_arr = {};// 用户选择的规格数组
				$("#goods_spec_table  span").each(function(){
					if($(this).hasClass('btn-success')){
						var spec_id = $(this).data('spec_id');
						var item_id = $(this).data('item_id');
						if(!spec_arr.hasOwnProperty(spec_id))
							spec_arr[spec_id] = [];
					    spec_arr[spec_id].push(item_id);
					}		
				});
				
				var specarr = JSON.stringify(spec_arr);
				$("#specarr").val(specarr);
				//$("#goodsSpecForm").submit();
// 			}else{
// 				alert("无法提交");
// 			}
		}
		
		//属性提交
		var goodsattributeOnsubmit = function(){
			var goodsid = $("#attrgoodsid").val();
			if("0" != goodsid && goodsid.length > 0){
				$("#goodsattributeForm").submit();
			}else{
				alert("无法提交");
			}
		}
		
		//上传图片，用户记录div的ID
		var imagecount = 0;
		//页面加载事件
		$(document).ready(function() {
			
			//表单验证
			$("#goodsForm").validate({
				submitHandler: function(form){
					
					//$("#goodsContent").val($(".note-editable").html());//	原富文本框
					var goodsName=$("#goodsName").val();
					var goodsShortName=$("#goodsShortName").val();
					var goodsRemark=$("#goodsRemark").val();
					var goodsSn=$("#goodsSn").val();
					var franchiseeId=$("#franchiseeId").val();
					var goodsCategoryId=$("#goodsCategoryId").val();
					if(goodsName==""){
						top.layer.alert('商品名称不能为空!', {icon: 0, title:'提醒'});
						return;
					}
					if(goodsShortName==""){
						top.layer.alert('短名称不能为空!', {icon: 0, title:'提醒'});
						return;
					}
					if(goodsRemark==""){
						top.layer.alert('商品描述不能为空!', {icon: 0, title:'提醒'});
						return;
					}
					if(goodsSn==""){
						top.layer.alert('商品货号不能为空!', {icon: 0, title:'提醒'});
						return;
					}
					if(franchiseeId==""){
						top.layer.alert('所属商家不能为空!', {icon: 0, title:'提醒'});
						return;
					}
					var goodsNum=${goods.goodsNum};

					if(goodsNum>0){
						top.layer.alert('商品已经有人够买,仅规格数据无法修改!', {icon: 0, title:'提醒'});
					}
					var content = $(".ke-edit-iframe").contents().find(".ke-content").html();
					if(content.indexOf("style") >=0){
						content = content.replace("&lt;style&gt;","<style>");
						content = content.replace("&lt;/style&gt;","</style>");
					}
					$("#goodsContent").val(content);
					$("#keywords").val($("#keywordsSelectName").val());//关键词，功效
					$("#regionName").val($("#regionNameSelectName").val());//适用区域
					var spec_arr = {};// 用户选择的规格数组
					$("#goods_spec_table  span").each(function(){
						if($(this).hasClass('btn-success')){
							var spec_id = $(this).data('spec_id');
							var item_id = $(this).data('item_id');
							if(!spec_arr.hasOwnProperty(spec_id))
								spec_arr[spec_id] = [];
						    spec_arr[spec_id].push(item_id);
						}		
					});
					
					var specarr = JSON.stringify(spec_arr);
					$("#specarr").val(specarr);
					$(".loading").show();//打开展示层
					form.submit();
				}
			});
			
			//商品护理图片集
			$("#file_nurseimages_upload").uploadify({
				'buttonText' : '请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_nurseimages_upload',//<input type="file"/>的name
				'queueID' : 'file_nurseimages_queue',//与下面HTML的div.id对应
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
					imagecount ++;
					var jsonData = $.parseJSON(data);//text 转 json
					if(jsonData.result == '200'){
						$("#goodsNurseImagesDiv").append("<div style=\"height: 190px;\" id='imgItem"+imagecount+"' class='imgItem'><span class=\"contorl-bar\"><span class=\"move-pro\" onclick=\"updateImagesDiv('LEFT','goodsNurseImagesDiv','imgItem"+imagecount+"')\"></span><span class=\"move-last\" onclick=\"updateImagesDiv('RIGHT','goodsNurseImagesDiv','imgItem"+imagecount+"')\"></span><span class=\"move-delelt\" onclick=\"updateImagesDiv('DEL','goodsNurseImagesDiv','imgItem"+imagecount+"')\"></span></span><img src=\""+jsonData.file_url+"\" alt='images'><input type='hidden' id='goodsNurseImages' name='goodsNurseImages' value=\""+jsonData.file_url+"\">步骤(20个文字):<textarea class='nursetextarea' name='goodsNurseImagesText' id='goodsNurseImagesText' maxlength='20'></textarea></div>");
					}
				}
			});
			
		    //商品相册
			$("#file_goodsImages_upload").uploadify({
				'buttonText' : '请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_goodsImages_upload',//<input type="file"/>的name
				'queueID' : 'file_goodsImages_queue',//与下面HTML的div.id对应
				'method' : 'post',
				'fileTypeDesc': '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
				'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '10MB',//上传文件的大小限制
				'multi' : true,//设置为true时可以上传多个文件
				'auto' : true,//点击上传按钮才上传(false)
				'queueSizeLimit' : 5,//上传数量
				'overrideEvents' : ['onDialogClose'],  //设置哪些事件可以被重写，JSON格式    若不重写onDialogClose方法  将会弹出两个提示框
				'onSelectError':function(file, errorCode, errorMsg){  //返回一个错误，选择文件的时候触发  
			           switch(errorCode) { 
			               case -100: 
			                   alert("上传的文件数量已经超出系统限制的"+$('#file_goodsImages_upload').uploadify('settings','queueSizeLimit')+"个文件！"); 
			                   break; 
			               case -110: 
			                   alert("文件 ["+file.name+"] 大小超出系统限制的"+$('#file_goodsImages_upload').uploadify('settings','fileSizeLimit')+"大小！"); 
			                   break; 
			               case -120: 
			                   alert("文件 ["+file.name+"] 大小异常！"); 
			                   break; 
			               case -130: 
			                   alert("文件 ["+file.name+"] 类型不正确！"); 
			                   break; 
			           } 
			     }, 
				'onFallback' : function(){
					//没有兼容的FLASH时触发
					alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
				},
				'onUploadSuccess' : function(file, data, response) { 
					imagecount ++;
					var jsonData = $.parseJSON(data);//text 转 json
					if(jsonData.result == '200'){
						$("#goodsImages").append("<div id='imgItem"+imagecount+"' class='imgItem'><span class=\"contorl-bar\"><img src=\""+jsonData.file_url+"\" alt='images'><span class=\"move-pro\" onclick=\"updateImagesDiv('LEFT','goodsImages','imgItem"+imagecount+"')\"></span><span class=\"move-last\" onclick=\"updateImagesDiv('RIGHT','goodsImages','imgItem"+imagecount+"')\"></span><span class=\"move-delelt\" onclick=\"updateImagesDiv('DEL','goodsImages','imgItem"+imagecount+"')\"></span></span><input type='hidden' id='goodsImages' name='goodsImages' value=\""+jsonData.file_url+"\"></div>");
					}
				}
			});
		    
			//商品首图上传
			$("#file_originalImg_upload").uploadify({
				'buttonText' : '请选择LOGO',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_originalImg_upload',//<input type="file"/>的name
				'queueID' : 'file_originalImg_queue',//与下面HTML的div.id对应
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
						$("#originalImg").val(jsonData.file_url);
						$("#originalImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});
			
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
			
			//商品详情回写值
			radiochange($("#isRealValue").val());
		});
		
		//点击商品规格图片按钮，添加商品图片
		var specitemupload = function(v){
			$("#itemimgid").val(v);
			var itemimgid = $('#itemimgid').val(); 
			if(!$.trim(itemimgid) == ''){
				$("#Wrap").show();
				$("#specitemimgsrc").hide();
			}else{
				alert("比较参数为空");
			}
		}
		
		//保存商品规格图片
		var saveuploadImg = function(){
			var itemimgid = $('#itemimgid').val(); 
			var path = $("#specitemimgsrc")[0].src; 
			if(!$.trim(itemimgid) == '' && !$.trim(path) == ''){
				$("#item_img_"+itemimgid).empty();
				$("#item_img_"+itemimgid).append("<img src=\""+path+"\" style=\"width: 35px;height: 35px;\" onclick=\"specitemupload('"+itemimgid+"')\"><input type=\"hidden\" value=\""+path+"\" name=\"item_img["+itemimgid+"]\">");
				$("#Wrap").hide();
				$("#specitemimgsrc").attr('src',''); 
			}
		}
		
		//关闭上传图片按钮-清楚图片内容
		var closeuploadImg = function(){
			$("#specitemimgsrc").attr('src',''); 
			$("#Wrap").hide();
		}
		
		function LoadOver(){
			$("#ke-dialog-num").val("1");
			//给富文本框赋值
			var content = $("#goodsContent").val();
			if(content.indexOf("style") >=0){
				content = content.replace("<style>","&lt;style&gt;");
				content = content.replace("</style>","&lt;/style&gt;");
			}
			$(".ke-edit-iframe").contents().find(".ke-content").html(content);
		}
		
		window.onload = LoadOver;
    </script>
</body>
</html>