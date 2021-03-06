<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<!-- 内容上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>

	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if(validateForm.form()){
        	  loading('正在提交，请稍等...');
		      $("#inputForm").submit();
	     	  return true;
		  	}
		  return false;
		}
		
		function init(){
			var val="${user.roleNames}";
			if(val.indexOf("排班")>=0){
				document.getElementById("hideen").style.display="";
			}
			if($("#id").val() == null || $("#id").val() == ""){
				document.getElementById("hideen").style.display="";
			}
			
		}
			// 手机号码验证
			jQuery.validator.addMethod("isMobile", function(value, element) {
			    var length = value.length;
			    //var mobile = /^(133[0-9]{8})|(153[0-9]{8})|(180[0-9]{8})|(181[0-9]{8})|(189[0-9]{8})|(177[0-9]{8})|(130[0-9]{8})|(131[0-9]{8})|(132[0-9]{8})|(155[0-9]{8})|(156[0-9]{8})|(185[0-9]{8})|(186[0-9]{8})|(145[0-9]{8})|(170[0-9]{8})|(176[0-9]{8})|(134[0-9]{8})|(135[0-9]{8})|(136[0-9]{8})|(137[0-9]{8})|(138[0-9]{8})|(139[0-9]{8})|(150[0-9]{8})|(151[0-9]{8})|(152[0-9]{8})|(157[0-9]{8})|(158[0-9]{8})|(159[0-9]{8})|(182[0-9]{8})|(183[0-9]{8})|(184[0-9]{8})|(187[0-9]{8})|(188[0-9]{8})|(147[0-9]{8})|(178[0-9]{8})|(149[0-9]{8})|(173[0-9]{8})|(175[0-9]{8})|(171[0-9]{8})$/;       
			    //新的手机号码验证规则，只验证是否为11位数字   兵子
			    var mobile = /^1\d{10}$/;
			    var isShow;
			    var num = clean();
			    var id = $("#id").val();
			    if(id !='' && id != null){//修改
			    	if(num == '1'){ // 只存在妃子校
			    		    layer.msg('该号码妃子校已存在!'); 
			    		 	isShow = false;
					    }else if(num == '3'){ // 妃子校、每天美耶都存在
					    	layer.msg('该号码每天美耶、妃子校已存在!'); 
					    	isShow = false;
					    }else if(num == '4'){ 
					    	isShow = true;
					    }else if(num == 'Z'){ 
					    	layer.msg('该号码每天美耶已存在!'); 
					    	isShow = false;
					    }else {
					    	layer.msg('该号码每天美耶已存在!');
					    	isShow = false;
					    }
			    }else{//添加
			    	 if(num == '1'){ // 只存在妃子校
			    		    layer.msg('该号码妃子校已存在!'); 
					    	isShow = false;
					    }else if(num == '3'){ // 妃子校、每天美耶都存在
					    	layer.msg('该号码每天美耶、妃子校已存在!'); 
					    	isShow = false;
					    }else if(num == '4'){ 
					    	isShow = true;
					    }else if(num == 'Z'){ 
					    	$("#result").val('2');
					    	$("#layer").val(num);
					    	layer.msg('该号码每天美耶已存在!该用户等级为'+num+'级'); 
					    	isShow = true;
					    }else {
					    	$("#result").val('2');
					    	$("#layer").val(num);
					    	layer.msg('该号码每天美耶已存在!该用户等级为'+num+'级');
					    	isShow = true;
					    }
			    }
			    
			    return this.optional(element) || ((length == 11 && mobile.test(value)) && isShow);
			}, "请正确填写您的手机号码");
			// 身份证号码验证
			jQuery.validator.addMethod("isIdCard", function(value, element) {
			    var length = value.length;
			    var idCard = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;       
			    return this.optional(element) || ((length == 15 || length == 18) && idCard.test(value));
			}, "请正确填写您的身份证号码");
			function delIdCard(num){
				  $.ajax({  
			          type:"get",  
			          url:"${ctx}/sys/user/checkDelIdcard",  
			          data:{'oldIdCard':$('#oldIdCard').val(),'idCard':num},  
			          dateType: 'text',
			          success: function(data){ 
			               if(data.id != null && data.delindex == 1){ 
			            	   top.layer.confirm('此用户已离职!是否查看其详细信息?', {icon: 3, title:'系统提示'}, function(index){
			            	   openDialogView("查看用户", "${ctx}/sys/user/delForm?id="+data.id,"430px", "500px");
			       			   top.layer.close(index);
			       			});
			               }
			          }  
			      }); 
			  }
			  
		$(document).ready(function(){
			$("#userTypeButton").click(function(){
				// 是否限制选择，如果限制，设置为disabled
				if ($("#userTypeButton").hasClass("disabled")){
					return true;
				}
				// 正常打开	
				top.layer.open({
				    type: 2, 
				    area: ['300px', '420px'],
				    title:"选择职位",
				    ajaxData:{selectIds: $("#userTypeId").val()},
				    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/sys/dict/dictTree?type=sys_user_type")+"&module=&checked=&extId=&isAll=" ,
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
									ids.push(nodes[i].id);
									names.push(nodes[i].name);//
									break; // 如果为非复选框选择，则返回第一个选择  
								}
								$("#userTypeId").val(ids.join(",").replace(/u_/ig,""));
								$("#userTypeName").val(names.join(","));
								$("#userTypeName").focus();
								top.layer.close(index);
						    	       },
		    	cancel: function(index){ //或者使用btn2
		    	           //按钮【按钮二】的回调
		    	       }
				}); 
			
			});
			
			
			
			//用户头像上传
			$("#file_user_upload").uploadify({
				'buttonText' : '请选择封面',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_user_upload',//<input type="file"/>的name
				'queueID' : 'file_category_queue',//与下面HTML的div.id对应
				'method' : 'post',
				'fileTypeDesc': '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
				'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '10MB',//上传文件的大小限制
				'multi' : false,//设置为true时可以上传多个文件
				'auto' : true,//点击上传按钮才上传(false)
				'onFallback' : function(){
					//没有兼容的FLASH时触发
					alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
				},
				'onUploadSuccess' : function(file, data, response) { 
					var jsonData = $.parseJSON(data);//text 转 json
					if(jsonData.result == '200'){
						$("#photo").val(jsonData.file_url);
						$("#photosrc").attr('src',jsonData.file_url); 
					}
				}
			});
			
			//先定死的图片上传 之后会动态添加上传功能
			
			$("#file_live_upload1").uploadify({
				'buttonText' : '请选择封面',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_live_upload1',//<input type="file"/>的name
				'queueID' : 'file_user_queue1',//与下面HTML的div.id对应
				'fileTypeDesc': '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
				'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '10MB',//上传文件的大小限制
				'multi' : false,//设置为true时可以上传多个文件
				'auto' : true,//点击上传按钮才上传(false)
				'onFallback' : function(){
					//没有兼容的FLASH时触发
					alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
				},
				'onUploadSuccess' : function(file, data, response) { 
					var jsonData = $.parseJSON(data);//text 转 json
					if(jsonData.result == '200'){
						$(".photo1").val(jsonData.file_url);
						$("#livesrc1").attr('src',jsonData.file_url); 
					}
				}
			});
			
			$("#file_live_upload2").uploadify({
				'buttonText' : '请选择封面',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_live_upload2',//<input type="file"/>的name
				'queueID' : 'file_user_queue2',//与下面HTML的div.id对应
				'fileTypeDesc': '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
				'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '10MB',//上传文件的大小限制
				'multi' : false,//设置为true时可以上传多个文件
				'auto' : true,//点击上传按钮才上传(false)
				'onFallback' : function(){
					//没有兼容的FLASH时触发
					alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
				},
				'onUploadSuccess' : function(file, data, response) { 
					var jsonData = $.parseJSON(data);//text 转 json
					if(jsonData.result == '200'){
						$(".photo2").val(jsonData.file_url);
						$("#livesrc2").attr('src',jsonData.file_url); 
					}
				}
			});
			
			$("#file_live_upload3").uploadify({
				'buttonText' : '请选择封面',
				'method' : 'post',
				'swf': '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_live_upload3',//<input type="file"/>的name
				'queueID' : 'file_user_queue3',//与下面HTML的div.id对应
				'fileTypeDesc': '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
				'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '10MB',//上传文件的大小限制
				'multi' : false,//设置为true时可以上传多个文件
				'auto' : true,//点击上传按钮才上传(false)
				'onFallback' : function(){
					//没有兼容的FLASH时触发
					alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
				},
				'onUploadSuccess' : function(file, data, response) { 
					var jsonData = $.parseJSON(data);//text 转 json
					if(jsonData.result == '200'){
						$(".photo3").val(jsonData.file_url);
						$("#livesrc3").attr('src',jsonData.file_url); 
					}
				}
			});
			validateForm = $("#inputForm").validate({
				rules: {
					/* no:{remote:"${ctx}/sys/user/checkNO?oldNo=" + encodeURIComponent('${user.no}')}, */
					no:{
						remote:{
							type: "post",
							async: false,
							url: "${ctx}/sys/user/checkNO?oldNo=" + encodeURIComponent('${user.no}')
						}
					},
					/* loginName: {remote: "${ctx}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')}, *///设置了远程验证，在初始化时必须预先调用一次。
					loginName: {
						remote: {
							type: "post",
							async: false,
							url: "${ctx}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')
						}
					},
					mobile:{
						digits:true,
						isMobile:true
					},
					idCard:{
						isIdCard: true,
						remote: {
							type: "post",
							async: false,
							url: "${ctx}/sys/user/checkIdcard?oldIdCard=" + encodeURIComponent('${user.idCard}')
						}
					}
				},
				messages:{
					no:{remote:"用户工号已存在"},
					loginName: {remote: "用户登录名已存在"},
					mobile:{
						digits:"输入合法手机号",
						isMobile :"请输入正确手机号"
					},
					idCard:{
						isIdCard :"请输入正确身份证号码",
						remote:"身份证号已经存在"
					},
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
			//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
			//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
			$("#inputForm").validate().element($("#no"));
			$("#inputForm").validate().element($("#loginName"));
			$("#inputForm").validate().element($("#idCard"));
			$("#inputForm").validate().element($("#mobile"));
			
			
			laydate({
				fixed: true, //是否固定在可视区域
	            elem: '#inductionTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
			/* laydate({
				fixed: true,
	            elem: '#userinfo.birthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        }); 
			laydate({
				fixed: true,
	            elem: '#userinfo.workYear', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });  */
		});

		//window.onload=init;
		//window.onload=BindSelect;
		function clean(){
			 var flag;
			 $.ajax({
				  async:false,
		          type:"get",  
		          url:"${ctx}/sys/user/newCheckMobile",  
		          data:{'mobile':$('#mobile').val(),'oldMobile':$('#oldMobile').val()},  
		          dateType:'json',
		          success: function(data){ 
		        	  newData = eval("("+data+")");
		        	  var result = newData.result;
		        	  var layer = newData.layer;
		              if("4" == result){ /* 正常数据 */
		            	  flag = '4';
		              }else if("3" == result){ /* 妃子校、每天美耶都存在 */
		            	  flag = '3';
		              }else if("2" == result){ /* 只存在每天美耶 */
		            	  flag = layer;
		              }else if("1" == result){ /* 只存在妃子校 */
		            	  flag = '1';
		              }
		          }
		      });
			 return flag;
		}
		
		function changeTableVal(buttomName,id,isChange){
			if($("#user"+buttomName).val() == 1){
				$(".loading").show();//打开展示层
				$.ajax({
					type : "POST",
					url : "${ctx}/sys/user/updateStatus?id="+id+"&isChange="+isChange+"&buttomName="+buttomName,
					dataType: 'json',
					success: function(data) {
						$(".loading").hide(); //关闭加载层
						var flag = data.FLAG;
						if("OK" == flag){
							$("#"+buttomName).html("");//清除DIV内容
							if(isChange == '0'){
								//当前状态为【否】
								$("#"+buttomName).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+buttomName+"','"+id+"','1')\">&nbsp;&nbsp;");
							}else if(isChange == '1'){
								//当前状态为【是】
								$("#"+buttomName).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+buttomName+"','"+id+"','0')\">&nbsp;&nbsp;");
							}
						}
						top.layer.alert(data.MESSAGE, {icon: 0, title:'提醒'}); 
					}
				});   
			}else{
				top.layer.alert("无此操作权限!", {icon: 0, title:'提醒'}); 
			}
		}
		
		
	/* 绑定归属商家和归属机构联动 */
	function officeButtion() {
		var compId = $("#companyId").val();
		if (compId != "") {
			top.layer.open({
			    type: 2, 
			    area: ['300px', '420px'],
			    title:"选择机构",
			    content: "${ctx}/sys/office/newUserOffice?compId="+compId,
			    btn: ['确定', '关闭'],
	    	    yes: function(index, layero){ //或者使用btn1
							var treeId = layero.find("iframe")[0].contentWindow.document.getElementById("offtreeId").value;//h.find("iframe").contents();
							var treeName = layero.find("iframe")[0].contentWindow.document.getElementById("offtreeName").value;
							$("#officeId").val(treeId);
							$("#officeName").val(treeName);
							top.layer.close(index);
					    	       },
	    	cancel: function(index){ //或者使用btn2
	    	           //按钮【按钮二】的回调
	    	       }
			}); 
			
		}else{
			top.layer.alert("请先选择归属商家",{icon: 6});
		}
		
	}
	
	/* 选择归属商家时清空机构中的数据，防止错误数据 */
	function companyButtion(){
		top.layer.open({
		    type: 2, 
		    area: ['300px', '420px'],
		    title:"选择${title}",
		    content: "${ctx}/tag/treeselect?url="+encodeURIComponent("/sys/franchisee/treeData")+"" ,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		    	var tree = layero.find("iframe")[0].contentWindow.tree;
		    	var nodes = tree.getSelectedNodes();
		    	$("#officeId").val("");//清空机构id
				$("#officeName").val("");//选择归属商家之前将归属机构清空
		    	$("#companyId").val(nodes[0].id);
		    	$("#companyName").val(nodes[0].name);
		    	top.layer.close(index);
		    },
		    cancel: function(index){ //或者使用btn2
 	           //按钮【按钮二】的回调
 	       }
		})
	}
		
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/save" method="post" class="form-horizontal">
		<!-- 是否推荐按钮权限 -->
		<shiro:hasPermission name="sys:user:updateIsRecommend">
			<input type="hidden" id="userISRECOMMEND" value="1">
		</shiro:hasPermission>
		<form:hidden path="id" id="id"/>
		<input id="result" name="result" type="hidden"/>
		<input id="layer" name="layer" type="hidden"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td class="width-15 active"><label class="pull-right">头像:</label></td>
		         <td class="width-35">
					<img id="photosrc" src="${user.photo}" alt="images" style="width: 200px;height: 100px;"/>
					<input type="hidden" id="photo" name="photo" value="${user.photo}">
					<div class="upload">
						<input type="file" name="file_user_upload" id="file_user_upload">
					</div>
					<div id="file_user_queue"></div>
				</td>
				 <td class="active"><label class="pull-right"><font color="red">*</font>工号:</label></td>
		         <td><input id="oldNO" name="oldNO" type="hidden" value="${user.no}">
		            <form:input path="no" htmlEscape="false" maxlength="50" class="form-control required"/></td>
		      </tr>
		      
		     <%-- 2018-05-24修改删除归属商家，删除归属机构，删除完善信息 --%>
		      
		      <tr>
		         <td class="active"><label class="pull-right"><font color="red">*</font>姓名:</label></td>
		         <td><form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/></td>
		         <td class="active"><label class="pull-right"><font color="red">*</font>登录名:</label></td>
		         <td><input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
					 <form:input path="loginName" htmlEscape="false" maxlength="50" class="form-control required userName"/></td>
		      </tr>
		      <tr>
		         <td class="active"><label class="pull-right"><c:if test="${empty user.id}"><font color="red">*</font></c:if>密码:</label></td>
		         <td><input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="form-control ${empty user.id?'required':''}"/>
					<c:if test="${not empty user.id}"><span class="help-inline">若不修改密码，请留空。</span></c:if></td>
		         <td class="active"><label class="pull-right"><c:if test="${empty user.id}"><font color="red">*</font></c:if>确认密码:</label></td>
		         <td><input id="confirmNewPassword" name="confirmNewPassword" type="password"  class="form-control ${empty user.id?'required':''}" value="" maxlength="50" minlength="3" equalTo="#newPassword"/></td>
		      </tr>
		      <tr>
		         <td class="active"><label class="pull-right"><font color="red">*</font>身份证号:</label></td>
		         <td><input id="oldIdCard" name="oldIdCard" type="hidden" value="${user.idCard}">
		         <form:input path="idCard" htmlEscape="false" maxlength="18" class="form-control required"  onblur="delIdCard(this.value)"/></td>
		         <td class="active"><label class="pull-right"><font color="red">*</font>入职日期:</label></td>
		         <td> <input id="inductionTime" name="inductionTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required"
				value="<fmt:formatDate value="${user.inductionTime}" pattern="yyyy-MM-dd"/>"/></td>
		      </tr>
		       <tr>
		         <td class="active"><label class="pull-right">邮箱:</label></td>
		         <td><form:input path="email" htmlEscape="false" maxlength="100" class="form-control email"/></td>
		         <td class="active"><label class="pull-right">电话:</label></td>
		         <td><form:input path="phone" htmlEscape="false" maxlength="100" class="form-control"/></td>
		      </tr>
		      
		      <tr>
		         <td class="active"><label class="pull-right"><font color="red">*</font>手机:</label></td>
		         <td>
			         <input id="oldMobile" name="oldMobile" type="hidden" value="${user.mobile}">
			         <form:input path="mobile" htmlEscape="false" maxlength="11" class="form-control required"/>
			         <input type="hidden" id="star"> 
		         </td>
		         <td class="active"><label class="pull-right"><font color="red">*</font>职位:</label></td>
		         <td>
					<input id="userTypeId" class="form-control required" type="hidden" value="${dict.value }" name="userType" aria-required="true">
					<div class="input-group">
						<input id="userTypeName" class="form-control required" type="text" style="" data-msg-required="" value="${dict.label }" readonly="readonly" name="userType.name" aria-required="true">
						<span class="input-group-btn">
							<button id="userTypeButton" class="btn btn-sm btn-primary " type="button">
								<i class="fa fa-search"></i>
							</button>
						</span>
					</div>
					<label id="userTypeName-error" class="error" style="display:none" for="userTypeName"></label>
					
				 </td>
		      </tr>
		      
		      <tr>
		         <td class="active"><label class="pull-right"><font color="red">*</font>性别:</label></td>
		         <td>
		         	<form:select path="sex" cssClass="form-control required">
		         		<form:option value=""></form:option>
		         		<form:option value="1">男</form:option>
		         		<form:option value="2">女</form:option>
		         	</form:select>
		         </td>
		         <td class="active">
		         	<label class="pull-right"><c:if test="${not empty user.id}">用户组织架构</c:if></label>
		         </td>
		         <td>${user.parendNames}<form:input path="delFlag" cssStyle="display:none;"></form:input></td>
		      </tr>
		      <tr>
		         <td class="active"><label class="pull-right">备注:</label></td>
		         <td colspan="3"><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/></td>
		      </tr>
		      
		      <c:if test="${not empty user.id}">
		       <tr>
		         <td class=""><label class="pull-right">创建时间:</label></td>
		         <td><span class="lbl"><fmt:formatDate value="${user.createDate}" type="both" dateStyle="full"/></span></td>
		         <td class=""><label class="pull-right">最后登陆:</label></td>
		         <td><span class="lbl">IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/></span></td>
		      </tr>
		     </c:if>
		  </tbody>
		  </table>   
		  
		 <c:if test="${not empty user.id && !empty userLogs}">    
			<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
				<thead>
					 <tr>
					 	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;">
					 		<a href="#" onclick="openDialogView('查看日志', '${ctx}/sys/user/userLogForm?userId=${user.id}','700px', '550px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i>日志详情</a>
					 	</td>
			  			<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="2"><label>操作日志</label></td>
			  		</tr>
					<tr>
						<th style="text-align: center;width:150px;" class="active">操作者</th>
					    <th style="text-align: center;width:100px;" class="active">操作时间</th>
					    <th style="text-align: center;" class="active">相关内容</th>
					</tr>
				</thead>
				<tbody style="text-align: center;">
					<c:forEach items="${userLogs }" var="userLogs">
						<tr>
						  	<td>${userLogs.name }</td>
						  	<td><fmt:formatDate value="${userLogs.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						  	<td>${userLogs.content }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</form:form>
</body>
</html>