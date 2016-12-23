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
		if(($("#companyId").val() != null && $("#companyId").val() != "" )||($("#loginName").val() != null && $("#loginName").val() != "")||($("#officeId").val() != null && $("#officeId").val() != "")||($("#name").val() != null && $("#name").val() != "")){
			$(".loading").show();
			$("#select1").empty();
			$.ajax({
				 type:"get",
				 data : $("#searchForm").serialize(),     //此处表单序列化
				 url:"${ctx}/sys/user/oaList",
				 dataType: 'json',
				 success:function(date){
					$(".loading").hide();
					if(date.list.length>0){
						for(var i=0;i<date.list.length;i++){
							$("#select1").append("<option value='"+date.list[i].id+"'>"+date.list[i].name+"</option>");
						}
					}
				 },
				 error:function(XMLHttpRequest,textStatus,errorThrown) {
					$(".loading").hide();
				 }
			});
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
					<form id="searchForm" modelAttribute="user" method="post" class="form-inline">
							<div class="form-group">
								<input id="loginName" name="loginName" value="${user.loginName}" placeholder="登录名" class=" form-control input-sm" />
								<%-- <span>归属商家：</span>
								<sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" 
									labelValue="${user.company.name}" title="公司" 
									url="/sys/franchisee/treeData" cssClass=" form-control input-sm" allowClear="true" /> --%>
								<input id="name" name="name" value="${name }" placeholder="姓名" class=" form-control input-sm" />
								<span>归属机构：</span>
								<sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}" title="部门"
									url="/sys/office/treeData?type=2" cssClass=" form-control input-sm" allowClear="true"
									notAllowSelectRoot="false" notAllowSelectParent="false" />
							    <input id="parentDel" name="parentDel" value="0" type="hidden">
							</div>
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
								<option value="${list.user.id}">${list.user.name}</option>
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