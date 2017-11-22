<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>业务员查询页面</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<script type="text/javascript">
		
		function selectUser(){
			var sysName = $("#sysName").val();
			if(sysName == ""){
				return;
			}
			
			$.ajax({
				type:"get",
				dataType:"json",
				data:{
					name:sysName
				},
				url:"${ctx}/ec/orders/querySysUser",
				success:function(data){
					$("#addSon").html("");
					if(data.length > 0){
						$.each(data,function(index,item){
							$("#addSon").append("<tr><td><input type='radio' name='coffee' value='"+item.id+"'></td><td style='text-align: center;'>"+item.name+"<input id='userId' name='userId' type='hidden' value='"+item.id+"'></td>"
											   +"<td style='text-align: center;'>"+item.positonName+"</td>"
											   +"<td style='text-align: center;'>"+item.departmentName+"<input id='departmentId' name='departmentId' type='hidden' value='"+item.departmentId+"'></td>"
											   +"<td style='text-align: center;'>"+item.officeName+"</td>"
											   +"<td style='text-align: center;'>"+item.mobile+"</td></tr>");
						}); 
					}else{
						top.layer.alert('请确定姓名的正确性以及该用户是否属于妃子校', {icon: 0, title:'提醒'});
						return;
					}
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
					top.layer.alert('请确定姓名的正确性以及该用户是否属于妃子校', {icon: 0, title:'提醒'});
				}
			});
		}
		
		function empty(){
			$("#sysName").val("");
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
	                	<label class="active" id="search"><font color="red">*</font>业务员姓名：</label>
	                	<input type="text" id="sysName" name="sysName" class="form-control required" />
	                	<div class="pull-right" id="condition">
		                	<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="selectUser()" ><i class="fa fa-search"></i> 查询</button>
		                	<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="empty()" ><i class="fa fa-refresh"></i> 重置</button>
	                	</div>
	                	<p></p>
	                	<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
						<tr>
							<th style="text-align: center;"></th>
							<th style="text-align: center;">业务员姓名</th>
							<th style="text-align: center;">职位</th>
							<th style="text-align: center;">部门</th>
							<th style="text-align: center;">归属机构</th>
							<th style="text-align: center;">手机号</th>
						</tr>
						<tbody id="addSon"></tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>