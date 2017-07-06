<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>添加商品</title>
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
		          arr.push(val);
		          all.push(txt);
		      });
		    $("#goodsId").val(arr);
		    $("#goodsName").val(all);
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
	    
	    $("#select3").hide();
	});
	
	//将分类加载到左侧的下拉框  
	function addselectgoods(cateid){
		var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
	    var all = new Array(); //定义变量全部保存
		$("#select1").empty();	
		 $("#select2 option").each(function (){
	          var txt = $(this).text(); //获取单个text
	          var val = $(this).val(); //获取单个value
	          arr.push(val);
	          all.push(txt);
	      });
		
	}
	
	//根据条件查询商品    搜索按键
	function findGood(){
		var cateid=$("#goodsCategoryId").val();
		var goodsName = $("#goodsName").val();
		var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
	    var all = new Array(); //定义变量全部保存
		$("#select1").empty();	
		$("#select2 option").each(function (){
	          var txt = $(this).text(); //获取单个text
	          var val = $(this).val(); //获取单个value
	          arr.push(val);
	          all.push(txt);
	      });
		 $("#select3 option").each(function (){
	          var txt = $(this).text(); //获取单个text
	          var val = $(this).val(); //获取单个value
	          arr.push(val);
	          all.push(txt);
	      });
		 
			$.ajax({
				 type:"get",
				 dataType:"json",
				 url:"${ctx}/ec/goods/treeGoodsData?goodsCategory="+cateid+"&goodsName="+goodsName+"&isOnSale=1",
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

</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>添加商品</h5>
			</div>
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="inputForm" modelAttribute="goods" action="${ctx}/ec/goodsSubhead/saveGoodsSubheadGoods" method="post" class="form-horizontal">
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<tr>
								<td>
									<label class="pull-right" >选择分类：</label>
								</td>
								<td>
									<sys:treeselect id="goodsCategory" name="goodsCategoryId" value="" labelName="goodsCategory.name" labelValue="" title="商品分类" url="/ec/goodscategory/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
								</td>
							</tr>
							<tr>
								<td>
									<label class="pull-right" >商品关键词：</label>
								</td>
								<td>
									<input id="goodsName" name="goodsName" type="text" value="" class="input-sm" />
									<a href="#"  class="btn btn-primary btn-rounded btn-outline btn-sm pull-right" onclick="findGood()" ><i class="fa fa-search"></i> 查询</a>
								</td>
							</tr>
							<tr style="padding-top:10px">
								<td><label class="pull-right">选择：</label></td>
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
											<c:forEach items="${goodsList}" var="list" varStatus="status">
												<option value="">${list.goods.goodsName}</option>
											</c:forEach>
										</select>
										
										<select multiple="multiple" id="select3" style="width:250px;height:300px;float:lfet;padding: 4px;">
											<c:forEach items="${goodsList}" var="list" varStatus="status">
												<option value="${list.goods.goodsId}">${list.goods.goodsName}</option>
											</c:forEach>
										</select>
									</div>
								</td>
							</tr>
						</table>
					</form:form>
				</div>
			</div>
		</div>
	</div>
	
</body>
</html>