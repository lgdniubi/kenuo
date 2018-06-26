<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>签约信息</title>
	<meta name="decorator" content="default"/>
	<!-- 日期控件 -->
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	 <!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<!-- 富文本框 -->
	<link rel="stylesheet" href="${ctxStatic}/kindEditor/themes/default/default.css" />
	<script src="${ctxStatic}/kindEditor/kindeditor-all.js" type="text/javascript"></script>
	
	<!-- 富文本框中新增的东西：上传照片，上传代码，商品分类，商品选择 -->
	<script src="${ctxStatic}/kindEditor/themes/editJs/editJs.js" type="text/javascript"></script>
	
	<!-- 富文本框上传图片样式引用 -->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/kindEditor/themes/editCss/edit.css">
	<style>
	</style>
	<script type="text/javascript">
		var newUploadURL = '<%=uploadURL%>';
		var validateForm;
		function saveSign(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			//if(validateForm.form()){
			if(validateImgUrl()){
				$("#inputForm").submit();
				//return true;
			}
		  return false;
		}
		/* function validUserId(){
			return isNullUserId('sign_usernameId') &&isNullUserId('cargo') &&isNullUserId('auditId')&&isNullUserId('proxyId');
		}
		//判断报货人那些userID是不是空
		function isNullUserId(uid){
			var userid = $("#"+uid).val();
			if(userid == ''){
				 top.layer.alert('姓名无效，请按手机号搜索', {icon: 0, title:'提醒'});
				 return false;
			}
			 return true;
		} */
		function validateImgUrl(){
			var flag = validOneImg('sign_fonturl')&&validOneImg('sign_backurl')&&validOneImg('cargo_fonturl')&&validOneImg('cargo_backurl')&&validOneImg('audit_fonturl')&&
			validOneImg('audit_backurl')&&validOneImg('proxy_fonturl')&&validOneImg('proxy_backurl');
			var backFlag = validBackImg();
			/* if(a==0){
				 top.layer.alert('支付信息不可为空！', {icon: 0, title:'提醒'});
				 return false;
			} */
			return flag && backFlag;
		}
		//校验银行卡图片
		var payWay = '${payWay}';
		var isHasBack = 0;
		function validBackImg(id){
			if(payWay=='0' && isHasBack==1){
				$("#pay-info input[name='payInfos[0].pay_fonturl']").each(function(k,y){
					var vl = $(this).val();
					if(vl == null || vl == ""){
					   top.layer.alert('银行卡正面图片不可为空！', {icon: 0, title:'提醒'});
					   return false;
					}
				})				
				$("#pay-info input[name='payInfos[0].pay_backurl']").each(function(k,y){
					var vl = $(this).val();
					if(vl == null || vl == ""){
					   top.layer.alert('银行卡图片反面不可为空！', {icon: 0, title:'提醒'});
					   return false;
					}
				})	
			}
			return true;
		}
		
		function validOneImg(id){
			if($("#"+id).val() == null || $("#"+id).val() == ""){
				   top.layer.alert('图片不可为空！', {icon: 0, title:'提醒'});
				   return false;
			}else{
				return true;
			}
		}
		function delPayData(value,obj){
			a--;
			if (value == 0){
				isHasBack = 0;
				$(obj).parents('.bank').remove();
			}else if(value == 1) {
				$(obj).parents('.ali').remove();
			}else if(value == 2) {
				$(obj).parents('.wechat').remove();
			}
		}
		//添加付款方式
		var a = 0;
		function addPayData(value,obj){
			a++;
			if (value == 0) {//#pay-info
				isHasBack=1;
				var s= "payfonturl";
				var s2= "paybackurl";
				var Htmlvalue = $("#pay-none #bank-pay").html();
				Htmlvalue = Htmlvalue.replace(/payfonturl/g,s+a);
				Htmlvalue = Htmlvalue.replace(/paybackurl/g,s2+a);
				$("#pay-info").append(Htmlvalue);
				uploadFile(s+a);
				uploadFile(s2+a);
			}else if(value == 2) {//#pay-info
				//alert($("#ali-pay").html())
				$("#pay-info").append($("#pay-none #ali-pay").html());
			}else if(value == 1) {//#pay-info
				//alert($("#bank-pay").html())
				$("#pay-info").append($("#pay-none #wechat-pay").html());
			}
		} 
		$(document).ready(function() {
			$("#sign_username").focus();
			validateForm = $("#inputForm").validate({
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
		
			uploadFile('sign_fonturl')
			uploadFile('sign_backurl')
			uploadFile('cargo_fonturl')
			uploadFile('cargo_backurl')
			uploadFile('audit_fonturl')
			uploadFile('audit_backurl')
			uploadFile('proxy_fonturl')
			uploadFile('proxy_backurl')
			/* uploadFile('pay_fonturl')
			uploadFile('pay_backurl') */
			var paylen = '${paylen}';
			a= parseInt(paylen);
			if(payWay=='0'){
				for (var len = 0; len < a; len++) {
					uploadFile('payfonturl'+len)
					uploadFile('paybackurl'+len)
				}
			}
			
		});
		
		function findUser(id){
			top.layer.open({
			    type: 2, 
			    area: ['300px', '420px'],
			    title:"查找用户",
			    content: "${ctx}/sys/user/finduser?officeId=${office.franchisee.id}",
			    btn: ['确定', '关闭']
    	    ,yes: function(index, layero){ //或者使用btn1
    	    	var tree = layero.find("iframe")[0].contentWindow.tree;
    	    	var search_name = $(tree).find('#search_name').val();
    	    	var search_userid = $(tree).find('#search_userid').val();
				//top.layer.msg("不能选择当前栏目以外的栏目模型，请重新选择。", {icon: 0});
				 $("#"+id+"Id").val(search_userid);
				//$("#signName").val(search_name);
				$("#"+id+"Name").val(search_name); 
				top.layer.close(index);
		    },
	    	cancel: function(index){ //或者使用btn2
	    	           //按钮【按钮二】的回调
	    	       }
			}); 
		}
	
		function uploadFile(str){
			//pay_backurl	
		
			$("#file_"+str+"_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_'+str+'_upload',//<input type="file"/>的name
				'queueID' : 'file_'+str+'_queue',//与下面HTML的div.id对应
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
						$("#"+str).val(jsonData.file_url);
						$("#office"+str+"Imgsrc").attr('src',jsonData.file_url); 
					}
				}
			});	
		}
		
		
			
			
	
			
			
	</script>
</head>
<body>
	<div class="ibox-content">
	<div>
	<label class="pull-left"><a href="${ctx}/sys/office/form?id=${office.id}&opflag=${opflag}">基础信息</a>-----</label>
	<label class="pull-left"><a href="#">签约信息</a></label>
	</div>
		<form:form id="inputForm" modelAttribute="office" action="${ctx}/sys/office/saveSignInfo" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<input type="hidden" name="create_user" value="${user.id}"/>
			<input type="hidden" name="payWay" value="${payWay}"/>
			<input type="hidden" name="office_id" value="${office.id}"/>
			<input type="hidden" name="franchisee_id" value="${office.franchisee.id}"/>
			<input type="hidden" name="office_pid" value="${office.parent.id}"/>
			<input type="hidden" name="office_pids" value="${office.parentIds}"/>
			<input type="hidden" name="office_name" value="${office.name}"/>
			<sys:message content="${message}"/>
				<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
				   <tbody>
						<tr><td colspan="4" class="active"><label class="pull-left">签约信息</label></td></tr>
						<tr><td colspan="4" class=""><label class="pull-left">管理员</label></td></tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>姓名:</label></td>
				         	<td class="width-35">
					         	<input id="signId" name="sign_userid" class="form-control required" type="hidden" value="${infoVo.sign_userid}"/>
								<div class="input-group">
									<input id="signName" name="sign_username" type="text" readonly="readonly" value="${infoVo.sign_username}" class="form-control required" />
							       		 <span class="input-group-btn">
								       		 <button type="button"  onclick="findUser('sign')" class="btn btn-primary"><i class="fa fa-search"></i></button> 
							       		 </span>
							    </div>
<%-- 				         	<sys:treeselect id="sign_username" name="sign_userid" value="${infoVo.sign_userid}" labelName="sign_username" labelValue="${infoVo.sign_username}" --%>
<%-- 								title="姓名" url="/sys/user/treeDataCompany?officeId=${office.franchisee.id}" cssClass="form-control required" placeholder="请输入手机号搜索" allowInput="true"/></td> --%>
				         	<td class="width-35" rowspan="4">
				         		<img id="officesign_fonturlImgsrc" src="${infoVo.sign_fonturl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="sign_fonturl" name="sign_fonturl" value="${infoVo.sign_fonturl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_sign_fonturl_upload" class="required" id="file_sign_fonturl_upload">
								</div>
								<div id="file_sign_fonturl_queue"></div>
				         	</td>
				         	<td class="width-35" rowspan="4">
				         		<img id="officesign_backurlImgsrc" src="${infoVo.sign_backurl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="sign_backurl" name="sign_backurl" value="${infoVo.sign_backurl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_sign_backurl_upload" class="required" id="file_sign_backurl_upload">
								</div>
								<div id="file_sign_backurl_queue"></div>
				         	</td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>身份证:</label></td>
				         	<td class="width-35"><input value="${infoVo.sign_idcard}" name="sign_idcard" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>联系电话:</label></td>
				         	<td class="width-35"><input value="${infoVo.sign_mobile}" name="sign_mobile" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>e-mail:</label></td>
				         	<td class="width-35"><input value="${infoVo.sign_email}" name="sign_email" class="form-control required"></td>
						</tr>
						
						
						<tr><td colspan="4" class=""><label class="pull-left">报货人</label></td></tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>姓名:</label></td>
				         	<td class="width-35">
				         		<input id="cargoId" name="cargo_userid" class="form-control required" type="hidden" value="${infoVo.cargo_userid}"/>
								<div class="input-group">
									<input id="cargoName" name="cargo_username" type="text" readonly="readonly" value="${infoVo.cargo_username}" class="form-control required" />
							       		 <span class="input-group-btn">
								       		 <button type="button"  onclick="findUser('cargo')" class="btn btn-primary"><i class="fa fa-search"></i></button> 
							       		 </span>
							    </div>
				         	
<%-- 				         	<sys:treeselect id="cargo" name="cargo_userid" value="${infoVo.cargo_userid}" labelName="cargo_username" labelValue="${infoVo.cargo_username}" --%>
<%-- 								title="姓名" url="/sys/user/treeDataCompany?officeId=${office.franchisee.id}"  cssClass="form-control required" placeholder="请输入手机号搜索" allowInput="true"/></td> --%>
				         	<td class="width-35" rowspan="4">
				         		<img id="officecargo_fonturlImgsrc" src="${infoVo.cargo_fonturl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="cargo_fonturl" name="cargo_fonturl" value="${infoVo.cargo_fonturl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_cargo_fonturl_upload" class="required" id="file_cargo_fonturl_upload">
								</div>
								<div id="file_cargo_fonturl_queue"></div>
				         	</td>
				         	<td class="width-35" rowspan="4">
				         		<img id="officecargo_backurlImgsrc" src="${infoVo.cargo_backurl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="cargo_backurl" name="cargo_backurl" value="${infoVo.cargo_backurl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_cargo_backurl_upload" class="required" id="file_cargo_backurl_upload">
								</div>
								<div id="file_cargo_backurl_queue"></div>
				         	</td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>身份证:</label></td>
				         	<td class="width-35"><input value="${infoVo.cargo_idcard}" name="cargo_idcard" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>联系电话:</label></td>
				         	<td class="width-35"><input value="${infoVo.cargo_mobile}" name="cargo_mobile" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>e-mail:</label></td>
				         	<td class="width-35"><input value="${infoVo.cargo_email}" name="cargo_email" class="form-control required"></td>
						</tr>
						
						
						<tr><td colspan="4" class=""><label class="pull-left">审核人</label></td></tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>姓名:</label></td>
				         	<td class="width-35">
				         		<input id="auditId" name="audit_userid" class="form-control required" type="hidden" value="${infoVo.audit_userid}"/>
								<div class="input-group">
									<input id="auditName" name="audit_username" type="text" readonly="readonly" value="${infoVo.audit_username}" class="form-control required" />
							       		 <span class="input-group-btn">
								       		 <button type="button"  onclick="findUser('audit')" class="btn btn-primary"><i class="fa fa-search"></i></button> 
							       		 </span>
							    </div>
<%-- 				         	<sys:treeselect id="audit" name="audit_userid" value="${infoVo.audit_userid}" labelName="audit_username" labelValue="${infoVo.audit_username}" --%>
<%-- 								title="姓名" url="/sys/user/treeDataCompany?officeId=${office.franchisee.id}"  cssClass="form-control required" placeholder="请输入手机号搜索" allowInput="true"/></td> --%>
				         	<td class="width-35" rowspan="4">
				         		<img id="officeaudit_fonturlImgsrc" src="${infoVo.audit_fonturl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="audit_fonturl" name="audit_fonturl" value="${infoVo.audit_fonturl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_audit_fonturl_upload" class="required" id="file_audit_fonturl_upload">
								</div>
								<div id="file_audit_fonturl_queue"></div>
				         	</td>
				         	<td class="width-35" rowspan="4">
				         		<img id="officeaudit_backurlImgsrc" src="${infoVo.audit_backurl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="audit_backurl" name="audit_backurl" value="${infoVo.audit_backurl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_audit_backurl_upload" class="required" id="file_audit_backurl_upload">
								</div>
								<div id="file_audit_backurl_queue"></div>
				         	</td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>身份证:</label></td>
				         	<td class="width-35"><input value="${infoVo.audit_idcard}" name="audit_idcard" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>联系电话:</label></td>
				         	<td class="width-35"><input value="${infoVo.audit_mobile}" name="audit_mobile" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>e-mail:</label></td>
				         	<td class="width-35"><input value="${infoVo.audit_email}" name="audit_email" class="form-control required"></td>
						</tr>
						
						
						<tr><td colspan="4" class=""><label class="pull-left">付款人</label></td></tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>姓名:</label></td>
				         	<td class="width-35">
					         	<input id="proxyId" name="proxy_userid" class="form-control required" type="hidden" value="${infoVo.proxy_userid}"/>
									<div class="input-group">
										<input id="proxyName" name="proxy_username" type="text" readonly="readonly" value="${infoVo.proxy_username}" class="form-control required" />
								       		 <span class="input-group-btn">
									       		 <button type="button"  onclick="findUser('proxy')" class="btn btn-primary"><i class="fa fa-search"></i></button> 
								       		 </span>
								    </div>
<%-- 				         	<sys:treeselect id="proxy" name="proxy_userid" value="${infoVo.proxy_userid}" labelName="proxy_username" labelValue="${infoVo.proxy_username}" --%>
<%-- 								title="姓名" url="/sys/user/treeDataCompany?officeId=${office.franchisee.id}"  cssClass="form-control required" placeholder="请输入手机号搜索" allowInput="true"/></td> --%>
				         	<td class="width-35" rowspan="4">
				         		<img id="officeproxy_fonturlImgsrc" src="${infoVo.proxy_fonturl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="proxy_fonturl" name="proxy_fonturl" value="${infoVo.proxy_fonturl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_proxy_fonturl_upload" class="required" id="file_proxy_fonturl_upload">
								</div>
								<div id="file_proxy_fonturl_queue"></div>
				         	</td>
				         	<td class="width-35" rowspan="4">
				         		<img id="officeproxy_backurlImgsrc" src="${infoVo.proxy_backurl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="proxy_backurl" name="proxy_backurl" value="${infoVo.proxy_backurl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_proxy_backurl_upload" class="required" id="file_proxy_backurl_upload">
								</div>
								<div id="file_proxy_backurl_queue"></div>
				         	</td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>身份证:</label></td>
				         	<td class="width-35"><input value="${infoVo.proxy_idcard}" name="proxy_idcard" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>联系电话:</label></td>
				         	<td class="width-35"><input value="${infoVo.proxy_mobile}" name="proxy_mobile" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>e-mail:</label></td>
				         	<td class="width-35"><input value="${infoVo.proxy_email}" name="proxy_email" class="form-control required"></td>
						</tr>
						
			      </tbody>
		      </table>
		      <c:if test="${payWay == 0}"><!-- 0是线下支付 -->
		      	  <a href="#" onclick="addPayData(0,this)"  class="btn  btn-warning btn-xs" ><i class="glyphicon glyphicon-plus"></i>添加账户</a>
		      	  <!-- <div id="add-pattern" onclick="addPayData(0,this)"><i class="icon-add-pattern"></i>添加账户</div> -->
		      </c:if>
		      <c:if test="${payWay == 1}"><!-- 1是线上支付 -->
		      	  <a href="#" onclick="addPayData(2,this)"  class="btn  btn-warning btn-xs" ><i class="glyphicon glyphicon-plus"></i>添加支付宝</a>
		      	  <a href="#" onclick="addPayData(1,this)"  class="btn  btn-warning btn-xs" ><i class="glyphicon glyphicon-plus"></i>添加微信</a>
			      <!-- <div id="add-pattern" onclick="addPayData(1,this)"><i class="icon-add-pattern"></i>添加支付宝</div>
			      <div id="add-pattern" onclick="addPayData(2,this)"><i class="icon-add-pattern"></i>添加微信</div> -->
		      </c:if>
		      <table id="pay-info" class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		      	<tbody >
		      	<tr><td colspan="6" class=""><label class="pull-left">付款账户</label></td></tr>
		      	</tbody>
		      	<c:if test="${payWay == 0}">
		      		<c:forEach items="${payInfos}" var="pay" varStatus="i">
			      		<tbody class='bank'>
							<tr>
								<td  class="width-15 active"><label class="pull-right"><font color="red"></font>开户银行</label></td>
						        	<td class="width-35"><input value="${pay.pay_name}" name="payInfos[0].pay_name" class="form-control required"></td>
						        	<td class="width-35" rowspan="3">
						        		<img id="officepayfonturl${i.index}Imgsrc" src="${pay.pay_fonturl}" alt="" style="width: 200px;height: 100px;"/>
									<input type="hidden" id="payfonturl${i.index}" name="payInfos[0].pay_fonturl" class="required" value="${pay.pay_fonturl}"><!-- 图片隐藏文本框 -->
									<p>&nbsp;</p>
						                 	<div class="upload">
										<input type="file" name="file_payfonturl${i.index}_upload" class="required" id="file_payfonturl${i.index}_upload">
									</div>
									<div id="file_payfonturl${i.index}_queue"></div>
						        	</td>
						        	<td class="width-35" rowspan="3">
						        		<img id="officepaybackurl${i.index}Imgsrc" src="${pay.pay_backurl}" alt="" style="width: 200px;height: 100px;"/>
									<input type="hidden" id="paybackurl${i.index}" name="payInfos[0].pay_backurl" value="${pay.pay_backurl}"><!-- 图片隐藏文本框 -->
									<p>&nbsp;</p>
						                 	<div class="upload">
										<input type="file" name="file_paybackurl${i.index}_upload" class="required" id="file_paybackurl${i.index}_upload">
									</div>
									<div id="file_paybackurl${i.index}_queue"></div>
						        	</td>
							</tr>
							<input value="0" name="payInfos[0].pay_type" type="hidden">
							<tr>
								<td  class="width-15 active"><label class="pull-right"><font color="red"></font>账户名称:</label></td>
						        <td class="width-35"><input value="${pay.pay_username}" name="payInfos[0].pay_username" class="form-control required"></td>
							</tr>
							<tr>
								<td  class="width-15 active"><label class="pull-right"><font color="red"></font>银行账号:</label></td>
						        <td class="width-35"><input value="${pay.pay_account}" name="payInfos[0].pay_account" class="form-control required"></td>
							</tr>
							<tr>
								<td colspan="6" class="active"><a href="#" onclick="delPayData(0,this)" class="btn btn-danger btn-xs"><i class="fa fa-trash">删除</i></a></td>
								<!-- <td colspan="6" class="active"><div id="add-pattern" onclick="delPayData(0,this)"><i class="icon-add-pattern"></i>删除</div></td> -->
							</tr>
						</tbody>
		      		</c:forEach>
		      	</c:if>
		      	<c:if test="${payWay == 1}">
		      		<c:forEach items="${payInfos}" var="pay" varStatus="i">
				      	<c:if test="${pay.pay_type == 2}">
				      		<tbody class='ali'>
								<tr>
								<input value="2" name="payInfos[2].pay_type" type="hidden">
									<td colspan="6" class="active"><label class="pull-left">支付宝</label></td>
								</tr>
								<tr>
									<td class="width-15 active"><label class="pull-right"><font color="red">*</font>账号</label></td>
						        	<td class="width-35"><input value="${pay.pay_account}" name="payInfos[2].pay_account" class="form-control required"></td>
									<td class="width-15 active"><label class="pull-right"><font color="red">*</font>姓名:</label></td>
							        <td class="width-35"><input value="${pay.pay_username}" name="payInfos[2].pay_username" class="form-control required"></td>
								</tr>
								<tr>
									<td class="width-15 active"><label class="pull-right"><font color="red">*</font>电话:</label></td>
							        <td colspan="5" class="width-35"><input value="${pay.pay_mobile}" name="payInfos[2].pay_mobile" class="form-control required"></td>
								</tr>
								<tr>
									<td colspan="6" class="active"><a href="#" onclick="delPayData(1,this)" class="btn btn-danger btn-xs"><i class="fa fa-trash">删除</i></a></td>
									<!-- <td colspan="6" class="active"><div id="add-pattern" onclick="delPayData(1,this)"><i class="icon-add-pattern"></i>删除</div></td> -->
								</tr>
							</tbody>
				      	</c:if>
				      	<c:if test="${pay.pay_type == 1}">
				      		<tbody class='wechat'>
								<tr>
									<input value="1" name="payInfos[1].pay_type" type="hidden">
									<td colspan="6" class="active"><label class="pull-left">微信</label></td>
								</tr>
								<tr>
									<td class="width-15 active"><label class="pull-right"><font color="red">*</font>账号</label></td>
							        <td class="width-35"><input value="${pay.pay_account}" name="payInfos[1].pay_account" class="form-control required"></td>
									<td class="width-15 active"><label class="pull-right"><font color="red">*</font>姓名:</label></td>
							        <td class="width-35"><input value="${pay.pay_username}" name="payInfos[1].pay_username" class="form-control required"></td>
									
								</tr>
								<tr>
									<td class="width-15 active"><label class="pull-right"><font color="red"></font>电话:</label></td>
							        <td  colspan="5" class="width-35"><input value="${pay.pay_mobile}" name="payInfos[1].pay_mobile" class="form-control required"></td>
								</tr>
								<tr>
									<td colspan="6" class="active"><a href="#" onclick="delPayData(2,this)" class="btn btn-danger btn-xs"><i class="fa fa-trash">删除</i></a></td>
									<!-- <td colspan="6" class="active"><div id="add-pattern" onclick="delPayData(2,this)"><i class="icon-add-pattern"></i>删除</div></td> -->
								</tr>
							</tbody>
				      	</c:if>
		      			
		      		</c:forEach>
		      	</c:if>
		      	
		      </table>
			   <c:if test="${opflag == 1}">
		      <input type="button" value="保存签约信息" onclick="saveSign()"/>
			  <a href="${ctx}/sys/office/form?id=${office.id}&opflag=${opflag}">上一步</a>
			  </c:if>
		</form:form>
		<div class="loading"></div>
	</div>
	<div style="display: none;" id="pay-none">
		<!-- 银行卡 -->
		<div class="payment-item" >
			<table id="bank-pay">
				<tbody class='bank'>
					<tr>
						<td  class="width-15 active"><label class="pull-right"><font color="red"></font>开户银行</label></td>
			        	<td class="width-35"><input value="" name="payInfos[0].pay_name" class="form-control required"></td>
			        	<td class="width-35" rowspan="3">
				        	<img id="officepayfonturlImgsrc" src="" alt="" style="width: 200px;height: 100px;"/>
							<input type="hidden" id="payfonturl" name="payInfos[0].pay_fonturl" class="required" value=""><!-- 图片隐藏文本框 -->
							<p>&nbsp;</p>
				            <div class="upload">
								<input type="file" name="file_payfonturl_upload" class="required" id="file_payfonturl_upload">
							</div>
							<div id="file_payfonturl_queue"></div>
			        	</td>
			        	<td class="width-35" rowspan="3">
				        	<img id="officepaybackurlImgsrc" src="" alt="" style="width: 200px;height: 100px;"/>
							<input type="hidden" id="paybackurl" name="payInfos[0].pay_backurl" value=""><!-- 图片隐藏文本框 -->
							<p>&nbsp;</p>
				            <div class="upload">
								<input type="file" name="file_paybackurl_upload" class="required" id="file_paybackurl_upload">
							</div>
							<div id="file_paybackurl_queue"></div>
			        	</td>
					</tr>
					<input value="0" name="payInfos[0].pay_type" type="hidden">
					<tr>
						<td  class="width-15 active"><label class="pull-right"><font color="red"></font>账户名称:</label></td>
				        <td class="width-35"><input value="" name="payInfos[0].pay_username" class="form-control required"></td>
					</tr>
					<tr>
						<td  class="width-15 active"><label class="pull-right"><font color="red"></font>银行账号:</label></td>
				        <td class="width-35"><input value="" name="payInfos[0].pay_account" class="form-control required"></td>
					</tr>
					<tr>
						<td colspan="6" class="active"><a href="#" onclick="delPayData(0,this)" class="btn btn-danger btn-xs"><i class="fa fa-trash">删除</i></a></td>
						<!-- <td colspan="6" class="active"><div id="add-pattern" onclick="delPayData(0,this)"><i class="icon-add-pattern"></i>删除</div></td> -->
					</tr>
				</tbody>
			</table>
		</div>
		<!-- 支付宝 -->
		<div class="payment-item" >
			<table id="ali-pay">
				<tbody class='ali'>
					<tr>
					<input value="2" name="payInfos[2].pay_type" type="hidden">
						<td colspan="6" class="active"><label class="pull-left">支付宝</label></td>
					</tr>
					<tr>
						<td class="width-15 active"><label class="pull-right"><font color="red">*</font>账号</label></td>
			        	<td class="width-35"><input value="" name="payInfos[2].pay_account" class="form-control required"></td>
						<td class="width-15 active"><label class="pull-right"><font color="red">*</font>姓名:</label></td>
				        <td class="width-35"><input value="" name="payInfos[2].pay_username" class="form-control required"></td>
					</tr>
					<tr>
						<td class="width-15 active"><label class="pull-right"><font color="red">*</font>电话:</label></td>
				        <td colspan="5" class="width-35"><input value="" name="payInfos[2].pay_mobile" class="form-control required"></td>
					</tr>
					<tr>
						<td colspan="6" class="active"><a href="#" onclick="delPayData(1,this)" class="btn btn-danger btn-xs"><i class="fa fa-trash">删除</i></a></td>
						<!-- <td colspan="6" class="active"><div id="add-pattern" onclick="delPayData(1,this)"><i class="icon-add-pattern"></i>删除</div></td> -->
					</tr>
				</tbody>
			</table>
		</div>
		<!-- 微信 -->
		<div class="payment-item" >
			<table id="wechat-pay">
				<tbody class='wechat'>
					<tr>
						<input value="1" name="payInfos[1].pay_type" type="hidden">
						<td colspan="6" class="active"><label class="pull-left">微信</label></td>
					</tr>
					<tr>
						<td class="width-15 active"><label class="pull-right"><font color="red">*</font>账号</label></td>
				        <td class="width-35"><input value="" name="payInfos[1].pay_account" class="form-control required"></td>
						<td class="width-15 active"><label class="pull-right"><font color="red">*</font>姓名:</label></td>
				        <td class="width-35"><input value="" name="payInfos[1].pay_username" class="form-control required"></td>
					</tr>
					<tr>
						<td class="width-15 active"><label class="pull-right"><font color="red"></font>电话:</label></td>
				        <td colspan="5" class="width-35"><input value="" name="payInfos[1].pay_mobile" class="form-control required"></td>
					</tr>
					<tr>
						<td colspan="6" class="active"><a href="#" onclick="delPayData(2,this)" class="btn btn-danger btn-xs"><i class="fa fa-trash">删除</i></a></td>
						<!-- <td colspan="6" class="active"><div id="add-pattern" onclick="delPayData(2,this)"><i class="icon-add-pattern"></i>删除</div></td> -->
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
