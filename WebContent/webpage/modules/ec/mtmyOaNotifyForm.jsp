<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>通知管理</title>
	<meta name="decorator" content="default"/>
	
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" href="${ctxStatic}/bootstrap-switch/lc_switch.css">
  	<script src="${ctxStatic}/bootstrap-switch/js/lc_switch.js" type="text/javascript"></script>
  	
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
			var autoPushTime = {
			    elem: '#autoPushTime',
			    format: 'YYYY-MM-DD hh:mm:ss',
			    event: 'focus',
			    min: laydate.now(),
			    istime: true,				//是否显示时间
			    isclear: true,				//是否显示清除
			    istoday: true,				//是否显示今天
			    issure: true,				//是否显示确定
			    festival: true			//是否显示节日
			};
			
			laydate(autoPushTime);
			
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
			
			if($("#pushType").val() == 1){
				$("#tr").show();
			}else if($("#pushType").val() == 2){
				$("#group").show();
			};
			
			if("${mtmyOaNotify.autoPushTime}" == null || "${mtmyOaNotify.autoPushTime}" == ""){
				$("#time").hide();
			}else{
				$("#time").show();
			}
			push($("#type").val());
			
			$(".pd").lc_switch();
			
			$('body').delegate('.pd', 'lcs-statuschange', function() {
			    var status = ($(this).is(':checked')) ? '0' : '1';
			    $(".pd").val(status);
			});
			
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
								$("<a href='#' class='btn btn-success btn-xs' onclick='editNames()'><i class='fa fa-edit'></i> 修改</a> ").appendTo($("#userName"));
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
			$("#templateType").val($("#groupImportType").val());
			$("#newComment").empty();
			$('#commentModal').modal('show');
			closeTip(); 
		 }
		 
		 $(function(){
	          $("#yes").click(function(){
	        	  var options = {                                         
    	              url : "${ctx}/ec/mtmyOaNotify/importPhonesOrUserIds",
    	              dataType : "text",
    	              type : "post",
    	              success : function(data){
    	            	  $('#commentModal').modal('hide');
    	                  var newDate = $.parseJSON(data);
    				 	  var type = newDate.type;
    				 	  var data = newDate.data;
    					  if(type =="success"){
    						  var a = $("#data").val();
    						  if(a.endsWith(",") || a.length == 0){
    							  $("#data").val(data);
    						  }else{
    							  $("#data").val(a + "," + data);
    						  }
    						
    					  }
    					  if(type=="error"){
    						 top.layer.alert('导入失败!', {icon: 2, title:'提醒'});
    					  }
    	              },
    	              error:function(XMLHttpRequest,textStatus,errorThrown){
    				  }
    	          };
	        	  
	             $("#inputForm2").ajaxSubmit(options)
	          });
	      });
		 
		//导出下载模板	
		function importFileTemplate(){
			var type = $("#templateType").val();
			window.location.href= '${ctx}/ec/mtmyOaNotify/importPhonesOrders/template?type='+type;
		}
		
		//修改列推的用户
		function editNames(){
			top.layer.open({
			    type: 2, 
			    area: ['800px', '620px'],
			    title:"列推用户选择",
			    content: "${ctx}/ec/mtmyOaNotify/oaList?flag=editNames"+"&mtmyOaNotifyRecordIds="+$("#mtmyOaNotifyRecordId").val()+"&mtmyOaNotifyRecordNames="+$("#mtmyOaNotifyRecordName").val(),
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
							$("<a href='#' class='btn btn-success btn-xs' onclick='editNames()'><i class='fa fa-edit'></i> 修改</a> ").appendTo($("#userName"));
							top.layer.close(index);
					    	       },
	    	cancel: function(index){ //或者使用btn2
	    	           //按钮【按钮二】的回调
	    	       }
			}); 
		}
		
		function selectImportType(){
			$("#data").val("");
		}
		
		function nowxiugai(){
			if($(".pd").val() == "0"){
				$("#time").show();
			}else{
				$("#time").hide();
				$("#autoPushTime").val("");
			}
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
		          <td class="width-35" ><form:input path="title" maxlength="200" class="form-control required"/></td>
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
							<form:option value="" >请选择类型</form:option>
							<form:options items="${fns:getDictList('mtmy_oa_push_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
			       			<a href='#' class='btn btn-success btn-xs' onclick='editNames()'><i class='fa fa-edit'></i> 修改</a>
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
		       			<select class="form-control required" id="groupImportType" name="groupImportType" style="width:200px" onchange="selectImportType(this.value)">
		       				<option ${(mtmyOaNotify.groupImportType == 1)?'selected="selected"':''} value="1">批量导入用户ID</option>
		       				<option ${(mtmyOaNotify.groupImportType == 0)?'selected="selected"':''} value="0">批量导入手机号</option>
		       			</select>
		       			<p></p>
	       				<a href="#" onclick="newImport()" class="btn btn-white btn-sm"><i class="fa fa-folder-open-o"></i>导入</a><font color="red">若要手动输入，则要“,”分割开</font><br>
	       				<textarea id='data' name="data" rows="6" cols="1" class='form-control required' ><c:if test="${mtmyOaNotify.groupImportType == 0}">${mtmyOaNotify.mtmyOaNotifyRecordMobile}</c:if><c:if test="${mtmyOaNotify.groupImportType == 1}">${mtmyOaNotify.mtmyOaNotifyRecordUserIds}</c:if></textarea>
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
		    	<tr>
		        	<td  class="width-15 active"><label class="pull-right"><font color="red">*</font>自动推送：</label></td>
	         		<td class="width-35" colspan="3">
	         			<c:choose>
							<c:when test="${mtmyOaNotify.autoPushTime == null || mtmyOaNotify.autoPushTime == ''}">
								<a href="javascript:nowxiugai();">
					 				<input type="checkbox"  name="check" class="pd" />
								</a>
							</c:when>
							<c:otherwise>
								<a href="javascript:nowxiugai();">
									<input type="checkbox"  name="check" class="pd" checked="checked"  />
								</a>
							</c:otherwise>
						</c:choose>
						<span id="time">
							<input id="autoPushTime" name="autoPushTime" type="text" maxlength="30" class="laydate-icon form-control layer-date input-sm required" value="<fmt:formatDate value="${mtmyOaNotify.autoPushTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width:185px;" placeholder="自动推送时间" readonly="readonly"/>
						</span>
	         		</td>
	         	</tr>
			</tbody>
		</table>
	</form:form>
	<div class="loading"></div>
	<!-- 组推批量导入手机号或者id的对话框 -->
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
					<input id="templateType" name="templateType" type="hidden">
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