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
				            	remote: {
									type: "post",
									async: false,
									url: "${ctx}/ec/mtmyuser/verifynickname"
								}
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
					            minlength : 11,
					            maxlength : 11,
				            	remote: {
									type: "post",
									async: false,
									url: "${ctx}/ec/mtmyuser/verifymobile"
								}
					      },
					      officeName:{
					    	    required:true
					      },
					      beautyName:{
					    	    required:true
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
					            minlength : "请输入11位手机号",
					            maxlength : "请输入11位手机号",
					            remote: "此手机号已存在"
					       },
					      officeName:{
					    	  required : "请选择所属店铺 "
					      },
					      beautyName:{
					    	  required : "请选择所属美容师 "
					      } 
					  },
			 });
		
			$("#beautyButton").click(function() {
				
				var officeId = $("#officeId").val();
				var beautyId = $("#beautyId").val();
				// 是否限制选择，如果限制，设置为disabled
				if ($("#beautyButton").hasClass("disabled")) {
					return true;
			     }
				if (officeId == null|| officeId == "") {
					top.layer.alert('请先选择店铺!', {
						icon : 0,
						title : '提醒'
						});
				}else{
				// 正常打开	
				top.layer.open({
					type : 2,
					area : [ '300px','420px' ],
					title : "选择美容师",
					ajaxData : {
						selectIds : $("#beautyId").val()
					},
					content : "/kenuo/a/tag/treeselect?url="+ 
							encodeURIComponent("/crm/user/beautyTree?officeId="+officeId),
					btn : [ '确定', '关闭' ],
					yes : function(index,layero) { 
							//或者使用btn1
						  var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
						  var ids = [], names = [], nodes = [];
						  if ("" == "true") {
								nodes = tree.getCheckedNodes(true);
							} else {
								nodes = tree.getSelectedNodes();
							}
							for (var i = 0; i < nodes.length; i++) {//
								ids.push(nodes[i].id);
								names.push(nodes[i].name);//
								break; // 如果为非复选框选择，则返回第一个选择  
							}
							$("#beautyId").val(ids.join(",").replace(/u_/ig,""));
							$("#beautyName").val(names.join(","));$("#beautyName").focus();
							top.layer.close(index);
						},
						cancel : function(index) { //或者使用btn2
												 //按钮【按钮二】的回调
								}
						});
					}
				});
			});
		$(document).ready(function() {
        //点击事件
		$("#officeButton").click(function(){
			// 是否限制选择，如果限制，设置为disabled
			if ($("#officeButton").hasClass("disabled")){
				return true;
			}
			// 正常打开	
			top.layer.open({
			    type: 2, 
			    area: ['300px', '420px'],
			    title:"选择店铺",
			    ajaxData:{selectIds: $("#officeId").val()},
			    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/sys/office/treeData?type=2")+"&module=&checked=&extId=&isAll=" ,
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
							$("#officeId").val(ids.join(",").replace(/u_/ig,""));
							$("#officeName").val(names.join(","));
							$("#officeName").focus();
							top.layer.close(index);
					    	       },
	    	cancel: function(index){ //或者使用btn2
	    	           //按钮【按钮二】的回调
	    	       }
			   }); 
		
			});
        
		$("#appellationButton").click(function(){
			// 是否限制选择，如果限制，设置为disabled
			if ($("#appellationButton").hasClass("disabled")){
				return true;
			}
			// 正常打开	
			top.layer.open({
			    type: 2, 
			    area: ['300px', '420px'],
			    title:"选择称谓标签",
			    ajaxData:{selectIds: $("#appellationId").val()},
			    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/crm/appellation/treeData")+"&module=&checked=&extId=&isAll=" ,
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
							$("#appellationId").val(ids.join(",").replace(/u_/ig,""));
							$("#appellationName").val(names.join(","));
							$("#appellationName").focus();
							top.layer.close(index);
					    	       },
	    	cancel: function(index){ //或者使用btn2
	    	           //按钮【按钮二】的回调
	    	       }
			   }); 
		
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
						<form id="inputForm" action="${ctx}/crm/user/adduser">
							<ul>
			            		<li>
									<span class="col-sm-2" style="width: 100;">真实姓名：</span><input class="form-control" id="name" name="name" type="text" value="${users.name }" maxlength="10"/>
								</li>
								<li>
									<span class="col-sm-2"><font color="red">*</font>用户昵称：</span><input class="form-control" id="nickname" name="nickname" type="text" value="${users.nickname }" readonly="readonly"/>
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
								<li>
									<span class="col-sm-2"><font color="red">*</font>选择所属店铺：</span>
								     <input id="officeId" class="form-control" name="officeId"
										 type="hidden">
										<input id="officeName" class=" form-control"
											name="officeName" readonly="readonly"
											type="text">
											<button id="officeButton" class="btn btn-primary " type="button">
												<i class="fa fa-search"></i>
											</button>
								</li>
								<li>
									<span class="col-sm-2"><font color="red">*</font>选择所属美容师：</span>
								     <input id="beautyId" class="form-control" name="beautyId"
										 type="hidden">
										<input id="beautyName" class=" form-control"
											name="beautyName" readonly="readonly"
											type="text">
											<button id="beautyButton" class="btn btn-primary " type="button">
												<i class="fa fa-search"></i>
											</button>
								</li>
								<li>
		  							<span class="col-sm-2">选择称谓标签：</span>
		  							<%-- <sys:treeselect id="appellation" name="appellationId" value="${users.appellationId}" labelName="appellationName" labelValue="${users.appellationName}" title="称谓标签" url="/crm/appellation/treeData" cssClass="form-control" notAllowSelectParent="true"/> --%>
								    <input id="appellationId" class="form-control" name="appellationId" type="hidden">
									<input id="appellationName" class=" form-control" name="appellationName" readonly="readonly" type="text">
									<button id="appellationButton" class="btn btn-primary " type="button">
										<i class="fa fa-search"></i>
									</button>
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