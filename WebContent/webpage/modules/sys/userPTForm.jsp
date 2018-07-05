<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>普通用户管理</title>
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
			laydate({
				fixed: true,
	            elem: '#userinfo.birthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        }); 
			laydate({
				fixed: true,
	            elem: '#userinfo.workYear', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        }); 
		});

		window.onload=init;
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
		
	
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="userpt" action="${ctx}/sys/user/save" method="post" class="form-horizontal">
		<!-- 是否推荐按钮权限 -->
		<form:hidden path="id" id="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="4"><label>基本信息</label></td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right"><font color="red">*</font>会员姓名:</label></td>
			         <td><form:input path="memName" htmlEscape="false" maxlength="50" class="form-control required"/></td>
			         <td class="active"><label class="pull-right"><font color="red">*</font>会员类型:</label></td>
			         <td><input id="memType" name="memType" type="hidden" value="${userpt.memType}">
						 <form:select path="memType" id="memType" class="form-control required">
							<form:options items="${fns:getDictList('model_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select></td>
			    </tr>
			    <tr>
			         <td class="active"><label class="pull-right"><font color="red">*</font>会员昵称:</label></td>
			         <td><form:input path="memNickName" htmlEscape="false" maxlength="50" class="form-control required"/></td>
			         <td class="active"><label class="pull-right"><font color="red">*</font>微博:</label></td>
			         <td><form:input path="weibo" htmlEscape="false" maxlength="50" class="form-control required"/></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right"><font color="red">*</font>会员等级:</label></td>
			         <td><form:input path="memLevel" htmlEscape="false" maxlength="50" class="form-control required"/></td>
			         <td class="active"><label class="pull-right"><font color="red">*</font>微信:</label></td>
			         <td><form:input path="weiXin" htmlEscape="false" maxlength="50" class="form-control required"/></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right"><font color="red">*</font>会员手机号:</label></td>
			         <td><form:input path="memMobile" htmlEscape="false" maxlength="50" class="form-control required"/></td>
			         <td class="active"><label class="pull-right"><font color="red">*</font>QQ:</label></td>
			         <td><form:input path="QQ" htmlEscape="false" maxlength="50" class="form-control required"/></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right"><font color="red">*</font>注册时间:</label></td>
			         <td><form:input path="regisTime" htmlEscape="false" maxlength="50" class="form-control required"/></td>
			         <td class="active"><label class="pull-right"><font color="red">*</font>最近消费:</label></td>
			         <td><form:input path="lastFee" htmlEscape="false" maxlength="50" class="form-control required"/></td>
				</tr>
			</tbody>
		</table>  
<%-- 		</form:form>  --%>
<%-- 		<form:form id="info" modelAttribute="userpt" class="form-horizontal"> --%>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="4"><label>活跃信息</label></td>
				</tr>
			    <tr>
			         <td class=""><label class="pull-right">关注1:</label></td>
			         <td>
<%-- 			         <form:input path="attention" htmlEscape="false" maxlength="50" readonly="readonly" class="form-control required"/> --%>
			         	<input value="${userpt.attention }" readonly="readonly" class="form-control"/>
			         </td>
			         <td class="active"><label class="pull-right">收藏课程:</label></td>
			         <td><form:input path="lesson" htmlEscape="false" maxlength="50" class="form-control required"/></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">粉丝:</label></td>
			         <td><form:input path="fans" htmlEscape="false" maxlength="50" class="form-control required"/></td>
			         <td class="active"><label class="pull-right">收藏视频:</label></td>
			         <td><form:input path="video" htmlEscape="false" maxlength="50" class="form-control required"/></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">学分:</label></td>
			         <td><form:input path="score" htmlEscape="false" maxlength="50" class="form-control required"/></td>
			         <td class="active"><label class="pull-right">收藏文章:</label></td>
			         <td><form:input path="article" htmlEscape="false" maxlength="50" class="form-control required"/></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">获赞数:</label></td>
			         <td><form:input path="praise" htmlEscape="false" maxlength="50" class="form-control required"/></td>
			         <td class="active"><label class="pull-right">收藏商品:</label></td>
			         <td><form:input path="goods" htmlEscape="false" maxlength="50" class="form-control required"/></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">历史浏览:</label></td>
			         <td><form:input path="scan" htmlEscape="false" maxlength="50" class="form-control required"/></td>
			         <td class="active"><label class="pull-right">文章评论:</label></td>
			         <td><form:input path="articleComment" htmlEscape="false" maxlength="50" class="form-control required"/></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">问答评论:</label></td>
			         <td><form:input path="answers" htmlEscape="false" maxlength="50" class="form-control required"/></td>
			         <%-- <td class="active"><label class="pull-right">最近消费:</label></td>
			         <td><form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/></td> --%>
				</tr>
			</tbody>
		</table>   
		  
		  <table class="table table-bordered  table-condensed dataTables-example dataTable no-footer"  id="hideen" style="display:none;">  
		   <tbody>
			   <tr>
			  		<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="4"><label>消费信息</label></td>
			  	</tr>
			    <tr>
			         <td class="active"><label class="pull-right">消费总金额:</label></td>
			         <td><form:input path="totalFee" htmlEscape="false" maxlength="50" class="form-control required"/></td>
			         <td class="active"><label class="pull-right">昨日消费:</label></td>
			         <td><form:input path="yesterdayFee" htmlEscape="false" maxlength="50" class="form-control required"/></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">最近7日消费:</label></td>
			         <td><form:input path="sevenFee" htmlEscape="false" maxlength="50" class="form-control required"/></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">商学院订单数量:</label></td>
			         <td><form:input path="totalOrderNum" htmlEscape="false" maxlength="50" class="form-control required"/></td>
			         <td class="active"><label class="pull-right">商学院订单金额:</label></td>
			         <td><form:input path="totalOrderFund" htmlEscape="false" maxlength="50" class="form-control required"/></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">商品订单数量:</label></td>
			         <td><form:input path="goodsOrderNum" htmlEscape="false" maxlength="50" class="form-control required"/></td>
			         <td class="active"><label class="pull-right">商品订单金额:</label></td>
			         <td><form:input path="goodsOrderFund" htmlEscape="false" maxlength="50" class="form-control required"/></td>
				</tr>
		 </tbody>
		 </table> 
		<%--  <c:if test="${not empty user.id && !empty userLogs}">    
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
		</c:if> --%>
	</form:form>
</body>
</html>