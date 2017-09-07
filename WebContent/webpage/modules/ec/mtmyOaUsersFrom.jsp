<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>列推用户管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<script type="text/javascript">
	//将分类下商品加载到左侧的下拉框
	function addSelectUser(){
		if(($("#districtId").val() != null && $("#districtId").val() != "" )||($("#nickname").val() != null && $("#nickname").val() != "")||($("#mobile").val() != null && $("#mobile").val() != "")){
			$(".loading").show();
			$("#select1").empty();
			var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
		    var all = new Array(); //定义变量全部保存
			 $("#select2 option").each(function (){
		          var txt = $(this).text(); //获取单个text
		          var val = $(this).val(); //获取单个value
		          arr.push(val);
		          all.push(txt);
		      });
			
			$.ajax({
				 type:"get",
				 data : $("#searchForm").serialize(),     //此处表单序列化
				 url:"${ctx}/ec/mtmyuser/oaList",
				 dataType: 'json',
				 success:function(date){
					$(".loading").hide();
					if(date.list.length>0){
						if(arr.length>0){
							for(var j=0;j<arr.length;j++){
								for(var i=0;i<date.list.length;i++){
									if(arr[j]==date.list[i].userid){
										date.list.splice(i,1);
										break;
									}
								}
							}	
							for(var i=0;i<date.list.length;i++){
								$("#select1").append("<option value='"+date.list[i].userid+"'>"+date.list[i].nickname+" ("+date.list[i].mobile+")</option>");
							}
						}else{
							for(var i=0;i<date.list.length;i++){
								$("#select1").append("<option value='"+date.list[i].userid+"'>"+date.list[i].nickname+" ("+date.list[i].mobile+")</option>");
							}
						}
					}
				 },
				 error:function(XMLHttpRequest,textStatus,errorThrown) {
					$(".loading").hide();
				 }
			});
			
			
			/* $.ajax({
				 type:"get",
				 dataType:"json",
				 url:"${ctx}/ec/goods/treeGoodsData?goodsCategory="+cateid+"&actionType="+actionType+"&isOnSale=1"+"&isReal="+isReal,
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
				 
				}); */
		}else{
			top.layer.alert('请至少填写一个条件！', {icon: 0, title:'提醒'});
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
	</script>
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
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<div class="clearfix">
					<form id="searchForm" action="" method="post" class="navbar-form navbar-left searcharea">
						<input id="nickname" name="nickname" type="text"  class="form-control" placeholder="用户名">
						<input id="mobile" name="mobile" type="text" class="form-control" placeholder="手机号码" onkeyup="this.value=this.value.replace(/\D/g,'')">
						<span><label>选择区域:</label></span>
						<sys:treeselect id="district" name="district" value="" labelName="areaName" labelValue="" 
					     	title="地区" url="/sys/area/treeData" cssClass="area1 form-control required"  allowClear="true" notAllowSelectRoot="true"/>
					</form>
					<div class="pull-right">
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="addSelectUser()" ><i class="fa fa-search"></i> 查询</button>
					</div>
				</div>
				
				<div id="good" style="padding-top:10px;padding-left: 20px;">
					<div style="float:left">
						<select multiple="multiple" id="select1" style="width:250px;height:350px; float: left;"></select>
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
						<select multiple="multiple" id="select2" style="width:250px;height:350px;float:lfet;">
							<c:forEach items="${list}" var="list" >
								<option value="${list.users.id}">${list.users.nickname}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>