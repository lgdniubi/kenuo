<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>添加会员</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
	    	if(validateForm.form()){
	    		loading("正在提交，请稍候...");
				$("#inputForm").submit();
		    	return true;
	    	}
	    	return false;
	    }
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
					rules: {
						  nickname: {
					            required : true,
					            remote: "${ctx}/ec/mtmyuser/verifynickname"
					      },
					      password: {
					        required: true,
					        minlength: 6
					      },
					      confirm_password: {
					        required: true,
					        minlength: 6,
					        equalTo: "#password"
					      },
					      mobile: {
					            required : true,
					            minlength : 7,
					            remote: "${ctx}/ec/mtmyuser/verifymobile"
					      }
					  },
					  messages: {
						  nickname: {
					            required : "请输入您的昵称",
					            remote: "此昵称已存在"
					       },
					      password: {
					        required: "请输入密码",
					        minlength: "密码长度不能小于 6 个字符"
					      },
					      confirm_password: {
					        required: "请输入密码",
					        minlength: "密码长度不能小于 6 个字符",
					        equalTo: "两次密码输入不一致"
					      },
					      mobile: {
					            required : "请输入手机号",
					            minlength : "请输入正确手机号",
					            remote: "此手机号已存在"
					       }
					  },
			 });
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
						<form id="inputForm" action="${ctx}/ec/mtmyuser/adduser">
							<ul>
			            		<li>
									<span class="col-sm-2" style="width: 100;">真实姓名：</span><input class="form-control" id="name" name="name" type="text" value="${users.name }" maxlength="10"/>
								</li>
								<li>
									<span class="col-sm-2"><font color="red">*</font>用户昵称：</span><input class="form-control" id="nickname" name="nickname" type="text" value="${users.nickname }"/>
								</li>
								<li>
									<span class="col-sm-2"><font color="red">*</font>手机号码：</span><input class="form-control" id="mobile" name="mobile" type="tel" maxlength="11" value="${users.mobile }" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
								</li>
								<li>
									<span class="col-sm-2">性别：</span>
									<select class="form-control" name="sex" id="sex">
										<option value="0">保密</option>
										<option value="1">男</option>
										<option value="2">女</option>
									</select>
								</li>
								<li>
									<span class="col-sm-2"><font color="red">*</font>用户密码：</span><input class="form-control" id="password" name="password" type="password"/>
								</li>
								<li>
									<span class="col-sm-2"><font color="red">*</font>再次输入密码：</span><input class="form-control" id="confirm_password" name="confirm_password" type="password"/>
								</li>
							</ul>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>