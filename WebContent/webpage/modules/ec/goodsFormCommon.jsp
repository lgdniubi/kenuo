<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>添加通用卡</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<link rel="stylesheet" href="${ctxStatic}/ec/css/base.css">
	
	<!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js"></script>
	<!-- 引入自定义js -->
	<script type="text/javascript" src="${ctxStatic}/ec/js/goods.js"></script>
	
	<!-- 富文本框 -->
	<link rel="stylesheet" href="${ctxStatic}/kindEditor/themes/default/default.css" />
	<script src="${ctxStatic}/kindEditor/kindeditor-all.js" type="text/javascript"></script>
	
	<!-- 富文本框中新增的东西：上传照片，上传代码，商品分类，商品选择 -->
	<script src="${ctxStatic}/kindEditor/themes/editJs/editJs.js" type="text/javascript"></script>
	
	<!-- 富文本框上传图片样式引用 -->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/kindEditor/themes/editCss/edit.css">
	
	<!-- 富文本框上传图片样式 -->
	<style>
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
		var newUploadURL = '<%=uploadURL%>';
		var cateid = 0;  // 分类的id  
		$(document).ready(function(){
//			$("#goodsCategoryIdButton, #goodsCategoryIdName").click(function(){	    增加  , #goodsCategoryIdName  文本框有点击事件
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
				    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/ec/goodscategory/treeData?positionType=")+"&module=&checked=&extId=&isAll=&selectIds=",
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
								top.layer.close(index);
						    	       },
		    	cancel: function(index){ //或者使用btn2
		    	           //按钮【按钮二】的回调
		    	       }
				}); 
			
			});
			$("#franchiseeIdButton").click(function(){
				// 是否限制选择，如果限制，设置为disabled
				if ($("#franchiseeIdButton").hasClass("disabled")){
					return true;
				}
				// 正常打开	
				top.layer.open({
				    type: 2, 
				    area: ['300px', '420px'],
				    title:"选择加盟商",
				    ajaxData:{selectIds: $("#franchiseeIdId").val()},
				    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/sys/franchisee/treeData")+"&module=&checked=&extId=&isAll=&selectIds=" ,
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
								if($("#franchiseeIdId").val() != "" && $("#franchiseeIdId").val() != null){
									if(confirm("确定要改变所属商家吗?(子项商品会被删除)")){
										$("#franchiseeIdId").val(ids.join(",").replace(/u_/ig,""));
										$("#franchiseeIdName").val(names.join(","));
										$("#franchiseeIdName").focus();
										$("#addZTD").find("tr").remove();
									}
								}else{
									$("#franchiseeIdId").val(ids.join(",").replace(/u_/ig,""));
									$("#franchiseeIdName").val(names.join(","));
									$("#franchiseeIdName").focus();
								}
								top.layer.close(index);
						    	       },
		    	cancel: function(index){ //或者使用btn2
		    	           //按钮【按钮二】的回调
		    	       }
				}); 
			
			});
		});
	</script> 
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">
			<div class="ibox-title">
				<h5>添加通用卡</h5>
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
		                <li><a href="#tab_goods_card" data-toggle="tab">通用卡子项</a></li>                        
		                <li><a href="#tab_goods_images" data-toggle="tab">商品相册</a></li>
		                <li><a href="#tab_goods_spec" data-toggle="tab">商品规格</a></li>                        
		            </ul>
		          <form:form id="goodsForm" modelAttribute="goods" action="${ctx}/ec/goodsCard/save" method="post">
		            <div class="tab-content" id="myTabContent">
		            	<!-- 通用信息 Begin -->
						<div class="tab-pane fade in active" id="tab_tongyong">
						
								<form:hidden path="goodsId"/>
								<form:hidden path="attrType"/>
								<form:hidden path="specType"/>
								<form:hidden path="actionId"/>
								<form:hidden path="isReal" value="3"/>
								<ul class="formArea">
									<li class="form-group" id="goodsTypeLi">
										<span class="control-label col-sm-2"><font color="red">*</font>商品区分：</span>
										<input type="hidden" id="goodsTypeValue" value="${goods.goodsType }">
										<input type="radio" id="goodsType" name="goodsType" value="1" ${(goods.goodsType == '1')?'checked="checked"':''}>新商品
										<input type="radio" id="goodsType" name="goodsType" value="0" ${(goods.goodsType == '0' || goods.goodsType == null)?'checked="checked"':''}>老商品
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
											<input id="franchiseeIdId" name="franchiseeId" class="form-control required" type="hidden" value="" aria-required="true">
											<div class="input-group">
												<input id="franchiseeIdName" name="franchisee.name" readonly="readonly" type="text" value="" data-msg-required="" class="form-control required" style="" aria-required="true">
										       	<span class="input-group-btn">
											    	<button type="button" id="franchiseeIdButton" class="btn   btn-primary  "><i class="fa fa-search"></i></button> 
										       	</span>
										    </div>
											<label id="franchiseeIdName-error" class="error" for="franchiseeIdName" style="display:none"></label>
										</div>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>所属供应商：</span>
										<div style="width: 40%;padding-left: 150px;">
										<sys:treeselect id="supplierId" name="supplierId" value="${goods.supplierId}" labelName="supplierName" labelValue="${goods.supplierName}" 
	 											title="供应商" url="/ec/goodsSupplier/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/> 
										</div>
					                </li>
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>商品分类：</span>
										<div style="width: 40%;padding-left: 150px;">
											<input id="goodsCategoryIdId" name="goodsCategoryId" class="form-control required" type="hidden" value="${goods.goodsCategoryId}" aria-required="true">
											<div class="input-group">
												<input id="goodsCategoryIdName" name="goodsCategory.name" readonly="readonly" type="text" value="${goods.goodsCategory.name }" data-msg-required="" class="form-control required" style="" aria-required="true">
										       		 <span class="input-group-btn">
											       		 <button type="button" id="goodsCategoryIdButton" class="btn   btn-primary  "><i class="fa fa-search"></i>
											             </button> 
										       		 </span>
										    </div>
											 <label id="goodsCategoryIdName-error" class="error" for="goodsCategoryIdName" style="display:none"></label>
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
									<li class="form-group" id="advancePriceLi">
										<span class="control-label col-sm-2"><font color="red">*</font>预约金：</span>
										<form:input path="advancePrice" htmlEscape="false" maxlength="150" class="form-control"
											onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
											onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
											onfocus="if(value == '0.0'){value=''}"
											onblur="if(value == ''){value='0.0'}"/>
										<span class="control-label cannotEdit">(虚拟商品,预约金必填)</span>
									</li>
									<li class="form-group">
										<span class="control-label col-sm-2"><font color="red">*</font>商品总重量：</span>
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
										<span class="control-label col-sm-2"><font color="red">*</font>赠送云币数量：</span>
										<form:input path="integral"  maxlength="4" class="form-control digits required" 
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
										<div style="width: 40%;padding-left: 150px;"><font color="red" size="2">备注:首图在移动端指商品列表的图片</font></div>
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
						<!-- 通用卡子项 Begin -->
						<div class="tab-pane fade" id="tab_goods_card" style="border: 0">
							<div>
								<a href="#" onclick="addGoods(${goods.goodsId},1,3)" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加虚拟商品</a>
								<a href="#" onclick="addGoods(${goods.goodsId},0,3)" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加实物商品</a>
							</div>
							<table class="table table-bordered table-hover table-left">
								<thead>
									<tr style="text-align: center;">
										<th style="text-align: center;">序号</th>
										<th style="text-align: center;">子项名称</th>
										<th style="text-align: center;">商品类型</th>
										<th style="text-align: center;">数量</th>
										<th style="text-align: center;">操作</th>
									</tr>
								</thead>
								<tbody id="addZTD" style="text-align:center;">	
								</tbody>
							</table>
						</div>
						<!-- 通用卡子项 End -->
						<!-- 商品相册 Begin -->
						<div class="tab-pane fade" id="tab_goods_images">
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
							<div><font color="red" size="2">备注:商品相册在移动端指商品详情里的顶部轮播图</font></div>
						</div>
						<!-- 商品相册 End -->
						<!-- 商品规格  Begin -->
						<div class="tab-pane fade" id="tab_goods_spec">
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
												<td><b>优惠价</b></td>
												<td><b>市场价</b></td>
												<td><b>系统价</b></td>
								            	<td><b>库存</b></td>
								            	<td><b>条码</b></td>
								            	<td><b>商品编码</b></td>
								            	<td><b>商品重量(克)</b></td>
								            	<td><b>服务次数</b></td>
								            	<td><b>截止时间(月)</b></td>
								            </tr>
										</table>
									</c:if>
									<c:if test="${not empty goodsSpecItemtablecontent }">
										${goodsSpecItemtablecontent }
									</c:if>
								</div>
							</div>
							<font color="red">*亲,修改商品规格,请到列表页面的商品规格修改,谢谢!</font>
							<!-- 提交按钮 -->
							<div class="box-footer">   
								<input type="hidden" id="specarr" name="specarr">  
								<input type="hidden" id="itemimgid" name="itemimgid" value="">                  	
					    	</div> 
						</div>
						<!-- 商品规格  End -->
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
	
	<script type="text/javascript">
		var editor;
		KindEditor.ready(function(K) {
			editor = K.create('textarea[name="content1"]', {
				width : "100%"
			});
		});

		//去空格
		var ignoreSpaces = function(str) {
			return str = str.replace(/\s+/g,"");
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
									$("#goods_spec_table2").append("<table class=\"table table-bordered\" id=\"goods_spec_input_table\"><tr><td><b>优惠价</b></td><td><b>市场价</b></td><td><b>系统价</b></td><td><b>库存</b></td><td><b>条码</b></td><td><b>商品编码</b></td><td><b>商品重量(克)</b></td><td><b>服务次数</b></td><td><b>截止时间(月)</b></td></tr></table>");//填写table
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
		}
		
		//上传图片，用户记录div的ID
		var imagecount = 0;
		//页面加载事件
		$(document).ready(function() {
			
			//表单验证
			$("#goodsForm").validate({
				rules:{
					integral:{
						digits:true
					}
				},
				messages:{
					integral:{
						digits:"请输入正整数"
					}
				},
				submitHandler: function(form){
					
					//$("#goodsContent").val($(".note-editable").html());//	原富文本框
					var goodsName=$("#goodsName").val();
					var goodsShortName=$("#goodsShortName").val();
					var goodsRemark=$("#goodsRemark").val();
					var goodsSn=$("#goodsSn").val();
					var franchiseeId=$("#franchiseeIdId").val();
					var goodsCategoryId=$("#goodsCategoryId").val();
					var goodsIds = $("#goodsIds").val();
					var marketPrice = $("#marketPrice").val();
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
					if(goodsIds == undefined){
						top.layer.alert('子项信息不能为空!', {icon: 0, title:'提醒'}); 
						return;
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
				'multi' : true,//设置为true时可以上传多个文件
				'auto' : true,//点击上传按钮才上传(false)
				'onFallback' : function() {
					//没有兼容的FLASH时触发
					alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
				},
				'onUploadSuccess' : function(file, data, response) {
					var jsonData = $.parseJSON(data);//text 转 json
					console.log(jsonData);
					if (jsonData.result == '200') {
					/* 	$("#img").val(jsonData.file_url); */
						$(".t3").append("<input type='hidden' readonly='readonly' id='img' name='img' value='"+jsonData.file_url+"' class='form-control' style='width: 350px;' > <img id='imgsrc' src='"+jsonData.file_url+"' alt='' style='width: 100px;height: 100px;'/> ");
						
					/* 	$('.img').attr('href', jsonData.file_url);
						$("#imgsrc").attr('src', jsonData.file_url); */
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
		
		
		//添加通用卡子项
		j=0;
		function addGoods(goodsId,num,isReal){
			franchiseeId = $("#franchiseeIdId").val();
			if(franchiseeId==""){
				top.layer.alert('请先选择 | 所属商家 |', {icon: 0, title:'提醒'});
				return;
			}
			var type;
			if(num == 0){
				type="实物";
			}else if(num == 1){
				type="虚拟";
			}
			top.layer.open({
			    type: 2, 
			    area: ['900px', '550px'],
			    title:"添加"+type+"商品",
			    content: "${ctx}/ec/goods/GoodsCardForm?goodsId="+goodsId+"&isReal="+num+"&franchiseeId="+franchiseeId,//虚拟和实物商品都是线下
			    btn: ['确定', '关闭'],
			    yes: function(index, layero){
			        var obj =  layero.find("iframe")[0].contentWindow;
					var goodsId = obj.document.getElementById("select2");
					newfranchiseeId = obj.document.getElementById("franchiseeId");
			        var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
			       
			        if(goodsId.length==0){
						top.layer.alert('商品不能为空!', {icon: 0, title:'提醒'}); 
						  return;
					}
			        if(num == 1){//添加虚拟商品时,没有数量
				       	for(var i=0;i<goodsId.length;i++){
				        	$("<tr style='text-align: center;'> "+
								"<td style='text-align: center;'> "+goodsId[i].value+"<input id='goodsIds' name='goodsIds' type='hidden' value='"+goodsId[i].value+"' class='form-control'></td> "+
								"<td style='text-align: center;width: 200px;'> "+goodsId[i].text+"<input id='goodsNames' name='goodsNames' type='hidden' value='"+goodsId[i].text+"' class='form-control'></td> "+
								"<td style='text-align: center;'> "+type+"</td> "+
								"<td style='text-align: center;'><input id='goodsNums"+j+"' name='goodsNums' type='hidden' value='0' readonly class='form-control'></td> "+
								"<td style='text-align: center;'> "+												  
									"<a href='#' class='btn btn-danger btn-xs' onclick='delFile(this)'><i class='fa fa-trash'></i> 删除</a> "+
								"</td>"+										
							"</tr>").appendTo($("#addZTD"));
				        	j++;
				        } 
			        }else{
			        	for(var i=0;i<goodsId.length;i++){
				        	$("<tr style='text-align: center;'> "+
								"<td style='text-align: center;'> "+goodsId[i].value+"<input id='goodsIds' name='goodsIds' type='hidden' value='"+goodsId[i].value+"' class='form-control'></td> "+
								"<td style='text-align: center;width: 200px;'> "+goodsId[i].text+"<input id='goodsNames' name='goodsNames' type='hidden' value='"+goodsId[i].text+"' class='form-control'></td> "+
								"<td style='text-align: center;'> "+type+"</td> "+
								"<td style='text-align: center;'> <input id='goodsNums"+j+"' name='goodsNums' type='text' value='0' readonly class='form-control'></td> "+
								"<td style='text-align: center;'> "+												  
									"<a href='#' class='btn btn-success btn-xs' onclick='updateByGoodsCard(this,"+goodsId[i].value+","+j+","+isReal+")'><i class='fa fa-edit'></i> 填写数量</a> "+
									"<a href='#' class='btn btn-danger btn-xs' onclick='delFile(this)'><i class='fa fa-trash'></i> 删除</a> "+
								"</td>"+										
							"</tr>").appendTo($("#addZTD"));
				        	j++;
				        } 
			        }
					top.layer.close(index);
			},
			cancel: function(index){ //或者使用btn2
					    	           //按钮【按钮二】的回调
			}
		}); 
		}
		function updateByGoodsCard(obj,goodsId,i,isReal){
			top.layer.open({
			    type: 2, 
			    area: ['350px', '550px'],
			    title:"填写价格",
			    content: "${ctx}/ec/goodsCard/fromPrice?goodsId="+goodsId+"&isReal="+isReal,
			    btn: ['确定', '关闭'],
			    yes: function(index, layero){
			    	
			        var obj = layero.find("iframe")[0].contentWindow;
					var goodsId = obj.document.getElementById("goodsId");
					var goodsNum = obj.document.getElementById("goodsNum");
					
					if(!/^[1-9][0-9]*$/.test($(goodsNum).val()) || $(goodsNum).val() == 0){
						top.layer.alert('数量  必须大于0的正整数', {icon: 0, title:'提醒'});
						return;
					}
					
					$("#goodsNums"+i).val($(goodsNum).val());
					
					top.layer.close(index);
				},cancel: function(index){ //或者使用btn2
					//按钮【按钮二】的回调
				}
			}); 
		}
		//删除卡项中的商品
		function delFile(obj){
			if(confirm("确认要删除吗？","提示框")){
				$(obj).parent().parent().remove();
				countPrice();
			}
	    }
    </script>
</body>
</html>