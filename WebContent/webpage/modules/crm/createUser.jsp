<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>预约详情</title>
	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
    <script type="text/javascript">
	    function save(){
	    	    $.ajax({
	    	    type : "POST",
	    	    url : '${ctx}/crm/saveUser/',
				data:{'name':$("#name").val(),
					  'nickname':$("#nickname").val(),
					  'mobile':$('#mobile').val(),
					  'sex':$('#sex').val(),
					  'beautician':$('#beautician').val(),
					  'password':$('#password').val()
					  },
	    	    success : function (data) {
	    	         console.log(data);   //data即为后台返回的数据
	    	    },
			    error: function(request) {
		             console.log("提交失败");
		        }
	    	});
	    }
	    function check() {          
	        var passwd1=$('#password').val(); 
	        var passwd2=$('#password2').val();
	        if (passwd1 =="") {
	            alert("请输入密码");
	            return;
	        }
	        if (passwd1 != passwd2) {
	        	 alert("密码不一致");
	        } 
	   }  
	</script>

</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
	    	<div class="ibox-content">
				<form:form id="inputForm">
					<!-- 用于提交时 验证数据是否有误 -->  
				
					<table id="user" class="table table-striped table-bordered  table-hover table-condensed no-footer">
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>真实姓名：</label></td>
							<td>
								<input class="form-control required" id="name" >
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right">用户昵称：</label></td>
							<td>
								<input class="form-control" name="nickname" id="nickname" >
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>手机号码：</label></td>
							<td>
							    <input class="form-control required" type="tel" name="mobile" id="mobile" >
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right">用户性别：</label></td>
							<td>
								<select name="sex" id="sex"  class="form-control ">
								   <option></option>							
								   <option value="0">男</option>
								   <option value="1">女</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>选择美容师</label></td>
							<td>
								<select  id="beautician" class="form-control required">
								   <option value="0"></option>
								   <option value="1">小张</option>
								   <option value="2">小王</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>用户密码：</label></td>
							<td>
								<input type="password" class="form-control required" id="password" name="password">
							</td>
							
						</tr>
						<tr>
							<td class="active"><label class="pull-right"><font color="red">*</font>确认密码：</label></td>
							<td>
								<input type="password" class="form-control required" id="password2" name="password2" onblur="check()">
							</td>
						</tr>
					</table>
				</form:form>
			<div class="ibox-content">
				<button class="ax_default primary_button" type="button" onclick="save()" >保存</button>		
				<button class="ax_default primary_button" type="button" onclick='window.history.back(-1)'>返回</button>	
		    </div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>