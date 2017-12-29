<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>城市异价</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		    if($("#ratio").val() > 1 || $("#ratio").val() < 0.01){
		   		top.layer.alert('请输入正确的数字!', {icon: 0, title:'提醒'}); 
				return;
		    }else{
		    	var re = /^([+-]?)\d*\.?\d{0,2}$/; 
		    	if(!re.test($("#ratio").val())){
					top.layer.alert('请输入正确的数字', {icon: 0, title:'提醒'});
					return;
				}
		    }

			if(validateForm.form()){
	    		loading("正在提交，请稍候...");
				$("#inputForm").submit();
		    	return true;
	    	}
	    	return false;
	    }
		$(document).ready(function(){
			validateForm = $("#inputForm").validate();
		});
		
		function chooseCity(){
			// 正常打开	
			top.layer.open({
			    type: 2, 
			    area: ['300px', '420px'],
			    title:"选择城市",
			    ajaxData:{selectIds: $("#cityIds").val()},
			    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/sys/area/findListByPID")+"&module=&checked=true&extId=&isAll=&selectIds=" ,
			    btn: ['确定', '关闭'],
			    yes: function(index, layero){ //或者使用btn1
						var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
						var ids = [], names = [], nodes = [];
						if ("true" == "true"){
							nodes = tree.getCheckedNodes(true);
						}else{
							nodes = tree.getSelectedNodes();
						}
						for(var i=0; i<nodes.length; i++) {//
							if (nodes[i].isParent){
								continue; // 如果为复选框选择，则过滤掉父节点
							}//
							if (nodes[i].isParent){
								//top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
								//layer.msg('有表情地提示');
								top.layer.msg("不能选择父节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
								return false;
							}//
							ids.push(nodes[i].id);
							names.push(nodes[i].name);//
						}
						$("#goodsIds").val("");
						$("#goodsNames").val("");
						
						$("#cityIds").val(ids.join(",").replace(/u_/ig,""));
						$("#cityNames").val(names.join(","));
						$("#cityNames").focus();
						top.layer.close(index);
				    	       },
	    	cancel: function(index){ //或者使用btn2
	    	           //按钮【按钮二】的回调
	    	       }
			}); 
		}
		
	function addGoods(){
		if($("#cityIds").val() == ''){
			top.layer.alert('请先选择城市!', {icon: 0, title:'提醒'}); 
			return;
		}
		var goodsIds = $("#goodsIds").val();
		top.layer.open({
		    type: 2, 
		    area: ['900px', '550px'],
		    title:"添加商品",
		    content: "${ctx}/ec/goodsPriceRatio/addGoodsForm?goodsIds="+goodsIds+"&cityIds="+$("#cityIds").val(),
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		        var obj =  layero.find("iframe")[0].contentWindow;
				var select2 = obj.document.getElementById("select2");
		        var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
		        var all = new Array(); //定义变量全部保存
		        
		       	for(i=0;i<select2.length;i++){
		        	arr.push(select2[i].value);
		        	all.push(select2[i].text);
		        } 
				
		        $("#goodsIds").val(arr);
		        $("#goodsNames").val(all);
		    top.layer.close(index);
			},
			cancel: function(index){ //或者使用btn2
			    	           //按钮【按钮二】的回调
		  	}
		}); 

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
						<form id="inputForm" action="${ctx}/ec/goodsPriceRatio/save">
						<input id="goodsPriceRatioId" name="goodsPriceRatioId" value="${goodsPriceRatio.goodsPriceRatioId}" type="hidden">
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<td><label class="pull-right"><font color="red">*</font>异价比例：</label></td>           
									<td>                                                                                                                                                 
										<input class="form-control required" id="ratio" name="ratio" type="text" value="${goodsPriceRatio.ratio}" onkeyup="this.value=this.value.replace(/[^\d.]/g,&quot;&quot;)" onpaste="this.value=this.value.replace(/[^\d.]/g,&quot;&quot;)"/>
										（设置下浮比例在0.01~1之间）
										<p></p>
										异价公式：异价后商品的价格=商品优惠价*（1-异价比例）
										<p></p>
										异价比例是根据不同城市进行的价格下浮，下浮比例在0.01~1之间（即1%~100%）
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>城市：</label></td>
									<td>
										<a href="#" onclick="chooseCity()" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>选择城市</a>
										<p></p>
										<input id="cityIds" name="cityIds" value="${goodsPriceRatio.cityIds}" type="hidden">
										<textarea rows="7" cols="65" id="cityNames" name="cityNames" readonly="readonly" class="form-control required">${goodsPriceRatio.cityNames}</textarea>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>商品：</label></td>
									<td>
										<a href="#" onclick="addGoods()" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加商品</a>
										<p></p>
										<input id="goodsIds" name="goodsIds" value="${goodsPriceRatio.goodsIds}" type="hidden">
										<textarea rows="7" cols="65" id="goodsNames" name="goodsNames" readonly="readonly" class="form-control required">${goodsPriceRatio.goodsNames}</textarea>
									</td>
								</tr>
							</table>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>