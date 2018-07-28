<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>创建优惠卷</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->
<style type="text/css">
#one {
	width: 200px;
	height: 180px;
	float: left
}

#two {
	width: 50px;
	height: 180px;
	float: left
}

#three {
	width: 200px;
	height: 180px;
	float: left
}

.fabtn {
	width: 50px;
	height: 30px;
	margin-top: 10px;
	cursor: pointer;
}
</style>

<script type="text/javascript">
	var validateForm;
	
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			var val=jQuery("#usedType").val();
			if(val==3){
				var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
			    var all = new Array(); //定义变量全部保存
			    $("#select2 option").each(function () {
			          var txt = $(this).text(); //获取单个text
			          var val = $(this).val(); //获取单个value
			         // var node = val;
			          arr.push(val);
			          all.push(txt);
			      });
			    $("#goodsId").val(arr);
			    $("#goodsName").val(all);
				
			}
			if(val==2){
				var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
			    var all = new Array(); //定义变量全部保存
			    $("#select2 option").each(function () {
			          var txt = $(this).text(); //获取单个text
			          var val = $(this).val(); //获取单个value
			         // var node = val;
			          arr.push(val);
			          all.push(txt);
			      });
			    $("#cateId").val(arr);
			    $("#cateName").val(all);
			}
			
			var begtime=$("#getBegintime").val();
			var endtime=$("#getEndtime").val();
			var expirationDate=$("#expirationDate").val();
			if(endtime<begtime){
				top.layer.alert('领取结束日期要大于开始日期!', {icon: 0, title:'提醒'}); 
				  return;
			}
			if(expirationDate<endtime){
				top.layer.alert('有效时间要大于领取结束日期!', {icon: 0, title:'提醒'}); 
				  return;
			}
			
			$("#inputForm").submit();
			return true;
		}

		return false;
	}

	function init() {
		var val = "${activityCoupon.usedType}";
		var typeval="${activityCoupon.couponType}";
		
	
		if (val == 2) {
			$("#categ").hide();
			$("#good").show();
			$("#oneCate").show();
		}else if(val == 3){
			$("#good").show();
			$("#oneCate").hide();
			$("#categ").show();
			$("#acttype").show();
				
				
		}
			
		
		

	}
	
	function coupontype(o) {
		var type = $("#usedType").val();
		if (type == 1) {
			$("#categoryId").val("");
			$("#categoryName").val("");
			$("#select1").empty();
			$("#select2").empty();
			$("#categ").hide();
			$("#oneCate").hide();
			$("#good").hide();
			$("#acttype").hide();
		} else if (type == 2) {
			$("#select1").empty();
			$("#select2").empty();
			$("#good").show();
			$("#categ").hide();
			$("#oneCate").show();
			$("#acttype").hide();

		} else if (type == 3) {
			$("#select1").empty();
			$("#select2").empty();
			$("#good").show();
			$("#categ").show();
			$("#acttype").show();
			$("#goodsIdsArea").show();
			$("#oneCate").hide();
		}
	}
	
	function retype(o){
		var type = $("#couponType").val();
		if(type==1){
			$("#usetype").show();
			$("#acttype").hide();
			$("#usedType").val(1);
			$("#categoryId").val("");
			$("#categoryName").val("");
			$("#select1").empty();
			$("#select2").empty();
			$("#categ").hide();
			$("#oneCate").hide();
			$("#good").hide();
			$("#acttype").hide();
		}else if(type==2){
			$("#usetype").show();
			$("#acttype").hide();
			$("#usedType").val(1);
			$("#categoryId").val("");
			$("#categoryName").val("");
			$("#select1").empty();
			$("#select2").empty();
			$("#categ").hide();
			$("#oneCate").hide();
			$("#good").hide();
			$("#acttype").hide();
		}else if(type==3){
			$("#usetype").show();
			$("#acttype").hide();
			$("#usedType").val(1);
			$("#categoryId").val("");
			$("#categoryName").val("");
			$("#select1").empty();
			$("#select2").empty();
			$("#categ").hide();
			$("#oneCate").hide();
			$("#good").hide();
			$("#acttype").hide();
		}else{
			$("#usetype").show();
			$("#acttype").hide();
			$("#usedType").val(1);
			$("#categoryId").val("");
			$("#categoryName").val("");
			$("#select1").empty();
			$("#select2").empty();
			$("#categ").hide();
			$("#oneCate").hide();
			$("#good").hide();
			$("#acttype").hide();
		}
	}
	function ondeit(o){
		var val=$(o).val();
		if(val=="0.0"){
			$(o).val("");
		}
		if(val=="0"){
			$(o).val("");
		}
		
	}
	
	function bldeit(o){
		var val=$(o).val();
		if(val==""){
			$(o).val(0);
		}
	}

	
	
	$(function(){
	    //移到右边
	    $('#add').click(function() {
	    //获取选中的选项，删除并追加给对方
	        $('#select1 option:selected').appendTo('#select2');
	    });
	    //移到左边
	    $('#remove').click(function() {
	        $('#select2 option:selected').appendTo('#select1');
	    });
	    //全部移到右边
	    $('#add_all').click(function() {
	        //获取全部的选项,删除并追加给对方
	        $('#select1 option').appendTo('#select2');
	    });
	    //全部移到左边
	    $('#remove_all').click(function() {
	        $('#select2 option').appendTo('#select1');
	    });
	    //双击选项
	    $('#select1').dblclick(function(){     //绑定双击事件
	        //获取全部的选项,删除并追加给对方
	        $("option:selected",this).appendTo('#select2'); //追加给对方
	    });
	    //双击选项
	    $('#select2').dblclick(function(){
	       $("option:selected",this).appendTo('#select1');
	    });
	});
	
	//将分类加载到左侧的下拉框  
	function addselectgoods(cateid){
		var val=jQuery("#usedType").val();
		var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
	    var all = new Array(); //定义变量全部保存
		$("#select1").empty();	
		 $("#select2 option").each(function (){
			// alert("option");
	          var txt = $(this).text(); //获取单个text
	          var val = $(this).val(); //获取单个value
	         // var node = val;
	          arr.push(val);
	          all.push(txt);
	      });
		
		$("#select1").empty();
		if(val==2){
			$.ajax({
				 type:"get",
				 dataType:"json",
				 url:"${ctx}/ec/goodscategory/catetwolist?onCate="+cateid,
				 success:function(date){
					var data=date;
						
						if(arr.length>0){
							for(var j=0;j<arr.length;j++){
								for(var i=0;i<data.length;i++){
									if(arr[j]==data[i].id){
										data.splice(i,1);
										break;
									}
								}
							
							}	
							for(var i=0;i<data.length;i++){
								
								$("#select1").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
							}
						}else{
							for(var i=0;i<data.length;i++){
								$("#select1").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
							}	
						}
					
						
				 },
				 error:function(XMLHttpRequest,textStatus,errorThrown) {
				    
				  }
				 
				});
		}
		
	}
	
	//根据条件查询商品    搜索按键
	function findGood(){
		var val=$("#usedType").val();
		var cateid=$("#categoryId").val();
		var actionType=$("#actionType").val();
		var goodsIds = $("#goodsIds").val();
		var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
	    var all = new Array(); //定义变量全部保存
		$("#select1").empty();	
		 $("#select2 option").each(function (){
			// alert("option");
	          var txt = $(this).text(); //获取单个text
	          var val = $(this).val(); //获取单个value
	         // var node = val;
	          arr.push(val);
	          all.push(txt);
	      });
		
		if(val==3){
			$.ajax({
				 type:"get",
				 dataType:"json",
				 url:"${ctx}/ec/goods/treeGoodsData?goodsCategory="+cateid+"&actionType="+actionType+"&franchiseeId="+$("#franchiseeId").val()+"&id="+goodsIds,
				 success:function(date){
					var data=date;
// 					console.log(data);
// 					console.log("arr--------"+arr);
					if(arr.length>0){
						for(var j=0;j<arr.length;j++){
							for(var i=0;i<data.length;i++){
								if(arr[j]==data[i].id){
									data.splice(i,1);
									break;
								}
							}
						
						}	
						
						for(var i=0;i<data.length;i++){
							$("#select1").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
						}
						
					}else{
						for(var i=0;i<data.length;i++){
							$("#select1").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
						}
					}
						
						
				 },
				 error:function(XMLHttpRequest,textStatus,errorThrown) {
				    
				 }
				 
				});
			
			
		}
		
		
	} 
	
	

	jQuery.validator.addMethod("lrunlv", function(value, element) {
		return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
	}, "小数位不能超过三位");

	$(document).ready(function() {
		
		
		$("#categoryIdButton").click(function(){
			// 是否限制选择，如果限制，设置为disabled
			if ($("#categoryIdButton").hasClass("disabled")){
				return true;
			}
			// 正常打开	
			top.layer.open({
			    type: 2, 
			    area: ['300px', '420px'],
			    title:"选择商品分类",
			    ajaxData:{selectIds: $("#categoryId").val()},
			    content: "${ctx}/tag/treeselect?url="+encodeURIComponent("/ec/goodscategory/treeData")+"&module=&checked=&extId=&isAll=" ,
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
							$("#categoryId").val(ids.join(",").replace(/u_/ig,""));
							$("#categoryName").val(names.join(","));
							$("#categoryName").focus();
							var cateid=$("#categoryId").val();
							var name=$("#categoryName").val();
							//addselectgoods(cateid,name);
						
							top.layer.close(index);
					    	       },
	    	cancel: function(index){ //或者使用btn2
	    	           //按钮【按钮二】的回调
	    	       }
			}); 
		
		});
		
		
//		$("#oneCategoryIdButton, #oneCategoryIdName").click(function(){	    增加  , #oneCategoryIdName  文本框有点击事件
		$("#oneCategoryIdButton").click(function(){
			// 是否限制选择，如果限制，设置为disabled
			if ($("#oneCategoryIdButton").hasClass("disabled")){
				return true;
			}
			// 正常打开	
			top.layer.open({
			    type: 2, 
			    area: ['300px', '420px'],
			    title:"选择分类",
			    ajaxData:{selectIds: $("#oneCategoryIdId").val()},
			    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/ec/goodscategory/catelist")+"&module=&checked=&extId=&isAll=" ,
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
							$("#oneCategoryIdId").val(ids.join(",").replace(/u_/ig,""));
							$("#oneCategoryIdName").val(names.join(","));
							$("#oneCategoryIdName").focus();
							var oneCate=$("#oneCategoryIdId").val();
							addselectgoods(oneCate);
							top.layer.close(index);
					    	       },
	    	cancel: function(index){ //或者使用btn2
	    	           //按钮【按钮二】的回调
	    	       }
			}); 
		
		});

				var start = {
					elem : '#getBegintime',
					format : 'YYYY-MM-DD hh:mm:ss',
					event : 'focus',
					max : $("#getEndtime").val(), //最大日期
					min :laydate.now(new Date().toLocaleString(),"YYYY-MM-DD hh"),
					istime : true, //是否显示时间
					isclear : false, //是否显示清除
					istoday : false, //是否显示今天
					issure : true, //是否显示确定
					festival : true, //是否显示节日
					choose : function(datas) {
						var time=datas.substr(0,13); 
						end.min = time; //开始日选好后，重置结束日的最小日期
						end.start = time; //将结束日的初始值设定为开始日
					}
				};
				var end = {
					elem : '#getEndtime',
					format : 'YYYY-MM-DD hh:mm:ss',
					event : 'focus',
					min : $("#getBegintime").val(),
					istime : true,
					isclear : false,
					istoday : false,
					issure : true,
					festival : true,
					choose : function(datas) {
						var time=datas.substr(0,13); 
						start.max = time; //结束日选好后，重置开始日的最大日期
						actiontime.min = time;
					}
				};
				var actiontime = {
						elem : '#expirationDate',
						format : 'YYYY-MM-DD hh:mm:ss',
						event : 'focus',
						min : $("#getEndtime").val(),
						istime : true,
						isclear : false,
						istoday : false,
						issure : true,
						festival : true,
						choose : function(datas) {
							var time=datas.substr(0,13); 
							end.max=time;
							//actiontime.max = datas; //结束日选好后，重置开始日的最大日期
						}
					};
				laydate(start);
				laydate(end);
				laydate(actiontime);
				
				validateForm = $("#inputForm").validate({
					rules : {
						

									},
						messages : {
							

							},

							submitHandler : function(form) {
								loading('正在提交，请稍等...');
								form.submit();
							},
							errorContainer : "#messageBox",
							errorPlacement : function(error, element) {
								$("#messageBox").text("输入有误，请先更正。");
							if (element.is(":checkbox")|| element.is(":radio")|| element.parent().is(".input-append")) {
									error.appendTo(element.parent().parent());
							} else {
								error.insertAfter(element);
						}
					}
				});

				//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
				//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
				//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
				//$("#inputForm").validate().element($("#reason"));

// 				laydate({
// 					elem : '#expirationDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
// 					event : 'focus' //响应事件。如果没有传入event，则按照默认的click
// 				});

			});
	window.onload = init;
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>创建红包</h5>
			</div>
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="inputForm" modelAttribute="activityCoupon" action="${ctx}/ec/activity/saveCoupon" method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<input value="${franchiseeId }" type="hidden" id="franchiseeId"> <!-- 活动所属品牌 -->
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">

							<tr>
								<td><label class="pull-right"><font color="red">*</font>红包名称：</label></td>
								<td><form:input path="couponName" htmlEscape="false" maxlength="50"
									style="width:200px;" class="form-control required" /></td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>红包类型：</label></td>
								<td><form:select path="couponType"  class="form-control" style="width:200px;" onchange="retype(this)">
										<form:options items="${fns:getDictList('coupon_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
									</form:select></td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>满减金额：</label></td>
								<td><input id="baseAmount" name="baseAmount" value="${activityCoupon.baseAmount}" onclick="ondeit(this)" onBlur="bldeit(this)" style="width:200px;" maxlength="50" class="form-control required" /></td>
							</tr>
							<tr>	
								<td><label class="pull-right"><font color="red">*</font>红包金额：</label></td>
								<td><input id="couponMoney" name="couponMoney" value="${activityCoupon.couponMoney}" onclick="ondeit(this)" onBlur="bldeit(this)"  style="width:200px;" maxlength="50" class="form-control required" /></td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>发放数量：</label></td>
								<td><input id="totalNumber" name="totalNumber" value="${activityCoupon.totalNumber}" onclick="ondeit(this)" onBlur="bldeit(this)"  style="width:200px;" maxlength="50" class="form-control required" /></td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>领取上限：</label></td>
								<td><input id="ceiling" name="ceiling" value="${activityCoupon.ceiling}" onclick="ondeit(this)" onBlur="bldeit(this)"  style="width:200px;" maxlength="50" class="form-control required" /></td>
							</tr>
							
							<tr  id="usetype" style="display:display;">
								<td><label class="pull-right"><font color="red">*</font>使用范围：</label></td>
								<td><form:select path="usedType" class="form-control" style="width:200px;"  onchange="coupontype(this)">
										<form:option value="1">全部商品</form:option>
										<form:option value="2">指定分类</form:option>
										<form:option value="3">指定商品</form:option>
									</form:select>
								</td>
							</tr>
							<tr id="oneCate" style="display:none;">
								<td><label class="pull-right">选择分类：</label></td>
								<td>
									<input id="oneCategoryIdId" name="oneCategoryId" class="form-control" type="hidden" value="" aria-required="true">
									<div class="input-group" style="width:300px;">
										<input id="oneCategoryIdName" name="oneCategoryName" readonly="readonly" type="text" value=""  data-msg-required="" class="form-control error" style="" aria-required="true" aria-invalid="true">
								       		 <span class="input-group-btn">
									       		 <button type="button" id="oneCategoryIdButton" class="btn   btn-primary  "><i class="fa fa-search"></i>
									             </button> 
								       		 </span>
								    </div>
									 <label id="oneCategoryIdName-error" class="error" for="oneCategoryIdName" style="display:none"></label>
								</td>
							</tr>
							<tr id="categ" style="display:none;" >
								<td>
									<label class="pull-right" >选择分类：</label>
								</td>
								<td>
									<input id="categoryId" name="categoryId" class="form-control" type="hidden" value="${coupon.categoryId}" aria-required="true">
									<div class="input-group" style="width:300px;">
										<input id="categoryName" name="categoryName" readonly="readonly"  type="text" value="${coupon.categoryName}" data-msg-required="" class="form-control valid" style="" aria-required="true" aria-invalid="false">
								       		 <span class="input-group-btn">
									       		 <button type="button" id="categoryIdButton" class="btn   btn-primary  "><i class="fa fa-search"></i>
									             </button> 
								       		 </span>
								    </div>
									 <label id="categoryIdName-error" class="error" for="categoryIdName" style="display:none"></label>
								</td>
							
							</tr>
							<tr id="acttype"  style="display:none;">
								<td><label class="pull-right">活动类型：</label></td>
								<td>
									<form:select path="actionType" class="form-control" style="width:200px;">
										<form:option value=" ">全部商品</form:option>
										<form:option value="0">普通商品</form:option>
										<form:option value="1">限时抢购</form:option>
										<form:option value="2">团购</form:option>
										<form:option value="3">促销优惠</form:option>
									</form:select>
								</td>
							</tr>
							<tr id="goodsIdsArea"  style="display:none;">
								<td><label class="pull-right">商品ids：</label></td>
								<td>
									<textarea id="goodsIds" name="goodsIds" rows="5" cols="50"></textarea>
									<a href="#"  class="btn btn-primary btn-rounded btn-outline btn-sm pull-right" onclick="findGood()" ><i class="fa fa-search"></i> 查询</a>
								</td>
							</tr>
							<tr id="good" style="display: none; padding-top:10px">
								<td><label class="pull-right"><font color="red">*</font>选择：</label></td>
								<td>
									<div style="float:left">
										<select multiple="multiple" id="select1" style="width:250px;height:300px;float:left;padding:4px;">
											
										</select>
									</div>
									<div style="float:left"> 
										<span id="add">
								          <input type="button" class="fabtn" value=">"/>
								          </span><br/>
								          <span id="add_all">
								          <input type="button" class="fabtn" value=">>"/>
								          </span> <br/>
								          <span id="remove">
								          <input type="button" class="fabtn" value="&lt;"/>
								          </span><br/>
								          <span id="remove_all">
								          <input type="button" class="fabtn" value="<<"/>
								          </span>
								    </div>
									<div>
										<select multiple="multiple" id="select2" style="width:250px;height:300px;float:lfet;padding: 4px;">
											<c:forEach items="${activityCoupon.list}" var="list" varStatus="status">
												<option value="${list.goodsId}">${list.goodName}</option>
											</c:forEach>
											<c:forEach items="${activityCoupon.catelist}" var="list" varStatus="status">
												<option value="${list.categoryId}">${list.categoryName}</option>
											</c:forEach>
										</select>
									</div>
								</td>
							</tr>
						</table>
						
						<form:hidden path="cateId"/>
						<form:hidden path="cateName"/>
						<form:hidden path="goodsId"/>
						<form:hidden path="goodsName"/>
					
						</form:form>
				</div>
				
			</div>
		</div>
	</div>
	
</body>
</html>