<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="default"/>
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
    <script src="${ctxStatic}/train/js/jquery-1.11.3.js"></script>
    <script type="text/javascript">
	    $(document).ready(function() {
			//默认加载显示后台传过来的值
	    	var parentIdval = $("#parentIdval").val();
			if(null != parentIdval && '' != parentIdval){
				categorychange(parentIdval);
			}
			//一级分类默认
			$("#parentId option").each(function(){
				$this = $(this).val();
				if($this == parentIdval){
					$(this).attr("selected",true);
				}
			});
			
		});
	  //一级分类改变事件，联动二级分类
		function categorychange(v){
		  	if(v != "-1"){
				$(".loading").show();//打开展示层
				$('#categoryId').css('display', '');
				$.ajax({
					type : "POST",   
					url : "${ctx}/train/categorys/listtow?categoryId="+v,
					dataType: 'json',
					success: function(data) {
						$("#categoryId").html("");
						$.each(data.listtow, function(index,item){
							if(item.categoryId == $("#Tid").val()){
								$("#categoryId").prepend("<option value='"+item.categoryId+"' selected>"+item.name+"</option>");
							}else{
								$("#categoryId").prepend("<option value='"+item.categoryId+"'>"+item.name+"</option>");
							}
						});
						jiazai($("#categoryId").val());	
						$(".loading").hide(); //关闭加载层
					}
			   });   
		  }else{
			  jiazai("");
			  $("#categoryId").empty();
			  $('#categoryId').css('display', 'none');
		  }
	  }
	  function jiazai(a){
		  $(".loading").show();//打开展示层
		  $("#mydiv").empty();
		  $.ajax({
				type : "POST",   
				url : "${ctx}/train/exambank/jiaZaiNum?categoryId="+a,
				dataType: 'json',
				success: function(data) {
					$("#mydiv").append("<font size='2'>当前分类下单选题数量有"+data.e1+"条;多选题有"+data.e2+"条;判断题有"+data.e3+"条;</font>");
					
					$(".loading").hide(); //关闭加载层
				}
		   });   
	  }
	  function random(){
		  if($("#num1").val() != "" || $("#num2").val() != "" || $("#num3").val() != ""){
			  randowList();
			  return true;
		  }else{
			  top.layer.alert('请至少填写一种随机试题类型', {icon: 0, title:'提醒'}); 
			  return false;
		  }
		  return false;
	  }
	  function randowList(){
		    $(".loading").show();//打开展示层 
		    $("#tab").empty();
			$.ajax({
				type : "POST",   
				url : "${ctx}/train/exambank/randomList",
				data : $('#myForm').serialize(),			//form
				dataType: 'json',
				success: function(date) {
					$("#tab").show();
					$("#tab").append("<tr><th colspan='2'>单选题：随机到实际单选题数量为"+date.e1.length+"条;<br>多选题：随机到实际多选题数量为"+date.e2.length+"条;<br>判断题：随机到实际判断题数量为"+date.e3.length+"条;</th></tr>");
					if(date.e1.length != 0){
						$("#tab").append("<tr><td colspan='2' style='text-align: center;'><h5>单选题</h5></td></tr>");
						$.each(date.e1, function(index,item){
							$("#tab").append("<tr><td width='150' style='text-align: center;'><input type='hidden' id='exerciseId' name='exerciseId' value="+item.exerciseId+">"+item.name+"</td><td>"+item.exerciseTitle+"</td></tr>");
						});
					}
					if(date.e2.length != 0){
						$("#tab").append("<tr><td colspan='2' style='text-align: center;'><h5>多选题</5></td></tr>");
						$.each(date.e2, function(index,item){
							$("#tab").append("<tr><td width='150' style='text-align: center;'><input type='hidden' id='exerciseId' name='exerciseId' value="+item.exerciseId+">"+item.name+"</td><td>"+item.exerciseTitle+"</td></tr>");
						});
					}
					if(date.e3.length != 0){
						$("#tab").append("<tr><td colspan='2' style='text-align: center;'><h5>判断题<h5></td></tr>");
						$.each(date.e3, function(index,item){
							$("#tab").append("<tr><td width='150' style='text-align: center;'><input type='hidden' id='exerciseId' name='exerciseId' value="+item.exerciseId+">"+item.name+"</td><td>"+item.exerciseTitle+"</td></tr>");
						});
					}
					$(".loading").hide(); //关闭加载层
				}
			});
	  }
	  function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(typeof($("#exerciseId").val()) != "undefined"){
			  loading("正在提交，请稍候...");
			  $("#inputForm").submit();
			  return true;
		  }
		  top.layer.alert('随机到的试题为空，请重新随机', {icon: 0, title:'提醒'}); 
		  return false;
	  }
    </script>
    <title>随机试题</title>
</head>
<body>
    <div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>随机试题</h5>
            </div>
	            <div class="ibox-content">
		            <form id="myForm" action="" method="post" class="form-horizontal" >
		                <div class="clearfix">
		                	<input type="hidden" id="parentIdval" name="parentIdval" value="${parentIdval }">
		                	<!-- 用于提交数据 -->
		                	<input type="hidden" id="lessontype" name="lessontype" value="${lessontype }">
		                	<input type="hidden" id="Tid" name="Tid" value="${Tid }">
		                	所属分类：
		                		<select class="form-control" id="parentId" name="parentId" onchange="categorychange(this.options[this.options.selectedIndex].value)">
		                			<option value="-1">请选择分类</option>
		                			<c:forEach items="${listone}" var="trainCategorys">
										<option value="${trainCategorys.categoryId}">${trainCategorys.name}</option>
									</c:forEach>
		                		</select>
								<select class="form-control" id="categoryId" name="categoryId" onchange="jiazai(this.options[this.options.selectedIndex].value)"></select>
								<span class="help-inline">注:二级分类为空时加载全部试题</span>
							<div class="pull-right">
								<a href="#" onclick="return random()" class="btn btn-primary btn-rounded btn-outline btn-sm "><i class="fa fa-search"></i>随机</a>
							</div>
							<br>
							<p></p>
		                	单选题：<input type="text" id="num1" name="num1" class="form-control" size="13" placeholder="请输入随机数量"  maxlength="2" onkeyup="this.value=this.value.replace(/\D/g,'')">
		                	多选题：<input type="text" id="num2" name="num2" class="form-control" size="13" placeholder="请输入随机数量"  maxlength="2" onkeyup="this.value=this.value.replace(/\D/g,'')">
		                	判断题：<input type="text" id="num3" name="num3" class="form-control" size="13" placeholder="请输入随机数量"  maxlength="2" onkeyup="this.value=this.value.replace(/\D/g,'')"><p></p>
		                	<div id="mydiv"></div>
		                </div>
		            </form>
	                <p></p>
	                <form id="inputForm" action="${ctx}/train/exambank/addRandomExam">
	                	<!-- 用于提交数据 -->
	                	<input type="hidden" id="lessontype" name="lessontype" value="${lessontype }">
	                	<input type="hidden" id="Tid" name="Tid" value="${Tid }">
		                <table id="tab" style="width: 100%;display: none" class="table table-bordered table-hover table-striped"></table>
	                </form>
	            </div>
        </div>
    </div>
    <div class="loading"></div>
</body>
</html>