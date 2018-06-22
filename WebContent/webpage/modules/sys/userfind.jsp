<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>数据选择</title>
	<meta name="decorator" content="blank"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<%@include file="/webpage/include/treeview.jsp" %>
	<script type="text/javascript">
	var lastValue = "";
	function searchUser() {
		 $(".loading").show();
		var value = $("#search_condition").val()
	    //var value = $('#search_condition').val();
		// 如果和上次一次，就退出不查了。
		if (lastValue === value) {
			//等待样式隐藏
			$(".loading").hide();
			return;
		}
		
		if (value == "") {
			//等待样式隐藏
			$(".loading").hide();
			return;
		} 
		// 保存最后一次
		lastValue = value;
		$.ajax({
			url:'${ctx}/sys/user/treeDataCompany?officeId=${officeId}',
			type:'post',
			data:{mobile:value},
		 	dataType:'json',
		 	success:function(data){
		 		if(data.code==0){
		 			top.layer.msg("没有找到用户", {icon: 0});
					$("#search_mobile").val('');
			 		$("#search_name").val('');
			 		$("#search_userid").val('');
		 		}else{
					$("#search_mobile").val(value);
			 		$("#search_name").val(data.name);
			 		$("#search_userid").val(data.id);
		 		}
		 		$(".loading").hide();
		 	}
		});
	}
	
	/* function findUser(){
		alert(99)
		var mobile = $("#sign_usernameName").val()
		$.ajax({
			url:'${ctx}/sys/user/treeDataCompany?officeId=${office.franchisee.id}',
			type:'post',
			data:{labelValue:mobile},
		 	dataType:'json',
		 	success:function(data){
		 		alert(data)
		 		alert(data[0].id)
		 		alert(data[0].name)
		 	}
		});
	} */
	</script>
</head>
<body>
	<input id="search_condition" type="text" placeholder="请输入手机号查询" class="form-control" style="width: 180px;margin:0 auto; "/>
	<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="searchUser()"> <i class="fa fa-search"></i> 查询
	</button>
	<div id="tree" style="padding:15px 20px;">
		姓名：<input type="text" name="" id="search_name" value="" class="form-control required"/>
		手机号<input type="text" name="" id="search_mobile" value="" class="form-control required" />
		<input type="hidden" name="" id="search_userid" value="" class="form-control required" />
	</div>
	<div class="loading"></div>
</body>