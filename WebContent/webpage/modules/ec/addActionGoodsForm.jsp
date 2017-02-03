<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>创建活动</title>
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
			$("#inputForm").submit();
			return true;
		}

		return false;
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
	
	//将分类下商品加载到左侧的下拉框
	function addselectgoods(){
		var goodsName=jQuery("#goodsName").val();
		var cateid=$("#categoryId").val();
		var name=$("#categoryName").val();
		$("#select1").empty();
		var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
	    var all = new Array(); //定义变量全部保存
	    $("#select2 option").each(function () {
	          var txt = $(this).text(); //获取单个text
	          var val = $(this).val(); //获取单个value
	         // var node = val;
	          arr.push(val);
	          all.push(txt);
	      });
			$.ajax({
				 type:"get",
				 dataType:"json",
				  data:{
					  goodsCategory:cateid,
					  goodsName:goodsName,
					  actionId:5
					  },
				  url:"${ctx}/ec/goods/treeGoodsData",
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
				 error:function(XMLHttpRequest,textStatus,errorThrown){
				    
				 }
				 
				});
		
		
	}
	
	function addActionGoods(){
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
	   // $("#goodsName").val(all);
	    var goodsId= $("#goodsId").val();
	    var actionId=$("#actionId").val();
	    $.ajax({
			 type:"post",
			  data:{
				  goodsId:goodsId,
				  actionId:actionId
				  },
			 url:"${ctx}/ec/action/saveGoods",
			 success:function(date){
				if(date="success"){
					top.layer.alert('添加成功!', {icon: 0, title:'提醒'});
					window.location="${ctx}/ec/action/addActionGoods?actionId="+actionId;
				}
				
			 },
			 error:function(XMLHttpRequest,textStatus,errorThrown){
			    
			 }
			 
			});
	
		
	}

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
							
							//addselectgoods(cateid,name);
						
							top.layer.close(index);
					    	       },
	    	cancel: function(index){ //或者使用btn2
	    	           //按钮【按钮二】的回调
	    	       }
			}); 
		
		});

			
				validateForm = $("#inputForm").validate({
					rules : {
						

					},
					messages:{
							
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
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="inputForm" modelAttribute="actionInfo" action="${ctx}/ec/action/saveGoods" method="post" class="form-horizontal">
						<form:hidden path="actionId"/>
						
						<div id="categ" style="width:300px;float: left;">
						<label style="float:left;padding-top:10px;">选择分类：</label>
								<input id="categoryId" name="categoryId" class="form-control required" type="hidden" value="" aria-required="true">
								<div class="input-group">
									<input id="categoryName" name="categoryName" readonly="readonly" type="text" value="" data-msg-required="" class="form-control required valid" style="" aria-required="true" aria-invalid="false">
							       		 <span class="input-group-btn">
								       		 <button type="button" id="categoryIdButton" class="btn   btn-primary  "><i class="fa fa-search"></i>
								             </button> 
							       		 </span>
							    </div>
							 <label id="categoryIdName-error" class="error" for="categoryIdName" style="display:none"></label>
							
						</div>
						<div  style="float:left;">
							&nbsp;<label>商品关键字：</label>
							<form:input path="goodsName" htmlEscape="false" maxlength="50" style="width:200px;" class="form-control" />
						</div>
						<div class="pull-right">
								<a href="#"  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="addselectgoods()" ><i class="fa fa-search"></i> 查询</a>
						</div>
						<div id="good" style="padding-top:50px">
						<div style="float:left"><label>指定商品：&nbsp;</label></div>
							<div style="float:left">
								<select multiple="multiple" id="select1" style="width:250px;height:300px; float: left;  padding: 4px;">
									
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
							<div style="float:left">
								<select multiple="multiple" id="select2" style="width:250px;height:300px;float:lfet;padding: 4px;">
									
								</select>
							</div>
							
						
						</div>
						
						</form:form>
						
						
				</div>
				
			</div>
		</div>
	</div>
	
</body>
</html>