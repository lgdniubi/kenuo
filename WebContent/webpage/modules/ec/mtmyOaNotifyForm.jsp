<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>通知管理</title>
	<meta name="decorator" content="default"/>
	<script src="http://malsup.github.io/min/jquery.form.min.js"></script>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		   if($("#pushType").val() == 1 && ($("#mtmyOaNotifyRecordId").val() == null || $("#mtmyOaNotifyRecordId").val() == "")){
			   top.layer.alert('接收人不可为空！', {icon: 0, title:'提醒'});
			   return false;
		   }
		   if(validateForm.form()){
			   if($("#pushType").val() == 0){
				   if(confirm("警告！所有用户都可以收到本次信息内容，请谨慎使用！")){
				   		$("#inputForm").submit();
				   		return true;
				   }
			   }else{
			   		$("#inputForm").submit();
			   		return true;
			   }
		   }
		  return false;
		}
		$(document).ready(function() {
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
			
		   /*var start = {
			    elem: '#startTime',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    max: $("#endTime").val(),   //最大日期
			    istime: false,				//是否显示时间
			    isclear: false,				//是否显示清除
			    istoday: false,				//是否显示今天
			    issure: false,				//是否显示确定
			    festival: true,				//是否显示节日
			    choose: function(datas){
			         end.min = datas; 		//开始日选好后，重置结束日的最小日期
			    }
			};
			var end = {
			    elem: '#endTime',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    min: $("#startTime").val(),
			    istime: false,
			    isclear: false,
			    istoday: false,
			    issure: false,
			    festival: true,
			    choose: function(datas){
			        start.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			laydate(start);
			laydate(end); */
			if($("#pushType").val() == 1){
				$("#tr").show();
			}else if($("#pushType").val() == 2){
				$("#group").show();
			};
			
			push($("#type").val());
		});
		function selectType(v){
			if(v == 1 & ($("#status").val() == "" || $("#status").val() == "0")){
				$("#tr").show();
				$("#group").hide();
				top.layer.open({
				    type: 2, 
				    area: ['800px', '620px'],
				    title:"列推用户选择",
				    content: "${ctx}/ec/mtmyOaNotify/oaList?id="+$("#id").val() ,
				    btn: ['确定', '关闭'],
				    	yes: function(index, layero){
				    			//  点击确定之后再删除其内容
					    		$("#mtmyOaNotifyRecord").val("");
								$("#mtmyOaNotifyRecordId").val("");
								$("#mtmyOaNotifyRecordName").val("");
								document.getElementById("userName").innerHTML="";
				    		
		    	    	   		var ids = [];
		    	    	   		var names = [];
								var obj = layero.find("iframe")[0].contentWindow;  
								var ifmObj = obj.document.getElementById("select2"); 
							
							    var options = ifmObj.options;
							    for(var i=0,len=options.length;i<len;i++){
							        var opt = options[i];
							        //jquery 数组去重
							        if($.inArray(opt.value,ids)==-1) {
							        	ids.push(opt.value);
									}
							        if($.inArray(opt.text,names)==-1) {
							        	names.push(opt.text);
									}
							    }
							   
							    $("#mtmyOaNotifyRecord").val(ids);
								document.getElementById("userName").innerHTML += names;
								$("#mtmyOaNotifyRecordId").val(ids);
								$("#mtmyOaNotifyRecordName").val(names);
								
								top.layer.close(index);
						    	       },
		    	cancel: function(index){ //或者使用btn2
		    	           //按钮【按钮二】的回调
		    	       }
				}); 
			}else if(v == 2){
				$("#group").show();
				$("#tr").hide();
			}else{
				$("#group").hide();
				$("#tr").hide();
			}
		}
		function push(num){
			if(num == 1){
				$(".help-inline").show();
			}else{
				$(".help-inline").hide();
			}
		}
	
		 function newImport(){
			$("#newComment").empty();
			$('#commentModal').modal('show');
			closeTip(); 
		 }
		 
		 $(function(){
	          var options = {
	              url : "${ctx}/ec/mtmyOaNotify/importPhones",
	              dataType : "text",
	              type : "post",
	              success : function(data){
	            	  $('#commentModal').modal('hide');
	                  var newDate = $.parseJSON(data);
				 	  var type = newDate.type;
					  var phones = newDate.phones;
					  if(type =="success"){
						  var a = $("#phones").val();
						  if(a.endsWith(",") || a.length == 0){
							  $("#phones").val(phones);
						  }else{
							  $("#phones").val(a + "," + phones);
						  }
						
					  }
					  if(type=="error"){
						 top.layer.alert('导入手机号失败!', {icon: 2, title:'提醒'});
					  }
	              },
	              error:function(XMLHttpRequest,textStatus,errorThrown){
				  }
	          };
	          $("#yes").click(function(){
	             $("#inputForm2").ajaxSubmit(options)
	          });
	      });
		 
		//导出下载模板	
		function importFileTemplate(){
			window.location.href= '${ctx}/ec/mtmyOaNotify/importPhonesOrders/template';
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="mtmyOaNotify" action="${ctx}/ec/mtmyOaNotify/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="status"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		       <tr>
		      	  <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>标题：</label></td>
		          <td class="width-35" ><form:input path="title" htmlEscape="false" maxlength="200" class="form-control required"/></td>
		          <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>类型：</label></td>
		          <td class="width-35" >
				  <form:select path="type"  class="form-control required" onchange="push(this.value)">
					  <form:option value="" label="推送类型"/>
					  <form:options items="${fns:getDictList('mtmy_oa_notify_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				  </form:select>
				  </td>
		     	</tr>
		      	<tr>
		      		<td  class="width-15 active"><label class="pull-right"><font color="red">*</font>推送类型：</label></td>
		       		<td class="width-35" colspan="3">
			       		<form:select path="pushType" class="form-control required" id="pushType" onchange="selectType(this.value)">
							<%-- <form:option value="" label=""/>
							<form:options items="${fns:getDictList('oa_push_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/> --%>
							<form:option value="-1" >请选择类型</form:option>
							<form:option value="0" >群推</form:option>
							<form:option value="1" >列推</form:option>
							<form:option value="2" >组推</form:option>
						</form:select>
					</td>
		        </tr>
		        <c:if test="${mtmyOaNotify.status == '0'}">
			        <tr id="tr" style="display:none">
			        	<td  class="width-15 active">
			        		<label class="pull-right"><font color="red">*</font>接收人：</label>
			        		<input id="mtmyOaNotifyRecord" name="mtmyOaNotifyRecord" value="${mtmyOaNotify.mtmyOaNotifyRecordIds}" type="hidden">
			        		<input id="mtmyOaNotifyRecordId" name="mtmyOaNotifyRecordIds" value="${mtmyOaNotify.mtmyOaNotifyRecordIds}" type="hidden">
			        		<input id="mtmyOaNotifyRecordName" name="mtmyOaNotifyRecordNames" value="${mtmyOaNotify.mtmyOaNotifyRecordNames}" type="hidden">
			        	</td>
			       		<td id="userName" class="width-35" colspan="3">
			       			${mtmyOaNotify.mtmyOaNotifyRecordNames}
			       		</td>
			        </tr>
		        </c:if>
		        
		         <c:if test="${mtmyOaNotify.status == '0'}">
		         <tr id="group" style="display:none">
		        	<td  class="width-15 active">
		        		<label class="pull-right"><font color="red">*</font>接收人：</label>
		        		<input id="mtmyOaNotifyRecordMobile" name="mtmyOaNotifyRecordMobile" value="${mtmyOaNotify.mtmyOaNotifyRecordMobile}" type="hidden">
		        	</td>
		       		<td colspan="3">
	       				<a href="#" onclick="newImport()" class="btn btn-white btn-sm"><i class="fa fa-folder-open-o"></i>导入</a><font color="red">若输入用户手机号码，则要“,”分割开</font><br>
	       				<textarea id='phones' name="phones" rows="6" cols="1" maxlength='2000' class='form-control required' >${mtmyOaNotify.mtmyOaNotifyRecordMobile}</textarea>
		       		</td>
		        </tr>
		        </c:if>
		       
		        
				<c:if test="${mtmyOaNotify.status == '1' || mtmyOaNotify.status == '2'}">
			        <tr>
			         	<td  class="width-15 active">	<label class="pull-right">接受人：</label></td>
			         	<td class="width-35" colspan="3">
			         	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
							<thead>
								<tr>
									<th>接受人</th>
									<th>阅读状态</th>
									<th>阅读时间</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${mtmyOaNotify.mtmyOaNotifyRecordList}" var="mtmyOaNotifyRecord">
								<tr>
									<td>
										${mtmyOaNotifyRecord.users.nickname}
									</td>
									<td>
										${fns:getDictLabel(mtmyOaNotifyRecord.readFlag, 'oa_notify_read', '')}
									</td>
									<td>
										<fmt:formatDate value="${mtmyOaNotifyRecord.readDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
						已查阅：${mtmyOaNotify.readNum} &nbsp; 未查阅：${mtmyOaNotify.unReadNum} &nbsp; 总共：${mtmyOaNotify.readNum + mtmyOaNotify.unReadNum}
						</td>
					</tr>
				</c:if>
				<%-- <tr>
			      	<td  class="width-15 active"><label class="pull-right"><font color="red">*</font>任务开始：</label></td>
			      	<td class="width-35" >
			      		<input id="startTime" name="startTime" value="${mtmyOaNotify.start }" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm" readonly="true"/>
			      	</td>
			      	<td  class="width-15 active"><label class="pull-right">任务结束：</label></td>
			      	<td class="width-35" >
			      		<input id="endTime" name="endTime" value="${mtmyOaNotify.end }" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm" readonly="true"/>
		    		</td>
				</tr> --%>
		 		<tr>
		        	<td  class="width-15 active"><label class="pull-right"><font color="red">*</font>内容：</label></td>
	         		<td class="width-35" colspan="3">
			        	<form:textarea path='content' htmlEscape='false' rows='6' maxlength='2000' class='form-control required' id='content'/>
			        	<span class="help-inline" style="display: none"><font color="red">* 此文本框内容只能为纯链接</font></span>
			    	</td>
			        <%-- <td  class="width-15 active"><label class="pull-right">附件：</label></td>
			        <td class="width-35" >
		         	<c:if test="${mtmyOaNotify.status ne '1'}">
						<form:hidden id="files" path="files" htmlEscape="false" maxlength="255" class="form-control"/>
						<sys:ckfinder input="files" type="files" uploadPath="/oa/notify" selectMultiple="true"/>
					</c:if>
			        <c:if test="${mtmyOaNotify.status eq '1'}">
				    	<form:hidden id="files" path="files" htmlEscape="false" maxlength="255" class="form-control"/>
						<sys:ckfinder input="files" type="files" uploadPath="/oa/notify" selectMultiple="true" readonly="true" />
			        </c:if>
		        	</td> --%>
		    	</tr>
			</tbody>
		</table>
	</form:form>
	<div class="loading"></div>
	<!-- 评论对话框 -->
	<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" id="commentModal">
 		<div class="modal-dialog modal-lg">
		    <div class="modal-content">
		    	<div class="modal-header">
		    		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        	<h4 class="modal-title" id="myModalLabel"></h4>
				</div>
				<div class="modal-body">
		      		<form id="inputForm2" action="" method="post" enctype="multipart/form-data" style="padding-left:20px;"><br/>
						<input id="uploadFile" accept=".xls,.xlsx" name="file" type="file" style="width:330px"/>
						导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！
					</form>
					<input class="btn" style="margin-left: 150px;margin-top: 7px;" type="button" value="下载模板" onclick="importFileTemplate()">
		      	</div>
				<div class="modal-footer">
					<button id="yes">上传</button>
		      	</div>
		    </div>
		</div>
	</div>
</body>
</html>