<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>通知管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if($("#pushType").val() == 1 && ($("#oaNotifyRecordId").val() == null || $("#oaNotifyRecordId").val() == "")){
				 top.layer.alert('接收人不可为空！', {icon: 0, title:'提醒'});
				 return false;
			}
		    if(validateForm.form()){
			    $("#inputForm").submit();
			    return true;
		    }
	
		  return false;
		}
		$(document).ready(function() {
			//$("#name").focus();
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
			
			//外部js调用
	       /*  laydate({
	            elem: '#beginDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        }); */
		    var start = {
			    elem: '#beginDate',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    max: $("#endDate").val(),   //最大日期
			    min: '${oaNotify.start }',	//最小日期
			    istime: false,				//是否显示时间
			    isclear: false,				//是否显示清除
			    istoday: false,				//是否显示今天
			    issure: false,				//是否显示确定
			    festival: true,				//是否显示节日
			    choose: function(datas){
			         end.min = datas; 		//开始日选好后，重置结束日的最小日期
			         end.start = datas 		//将结束日的初始值设定为开始日
			    }
			};
			var end = {
			    elem: '#endDate',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    min: $("#beginDate").val(),
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
			laydate(end);
	      	push($("#typeVal").val());
	      	if($("#pushType").val() == 1){
				$("#tr").show();
			};
		});
		
		function selectType(v){
			
			//var v = obj.value;
			var contentObj = $("#contentdict");
			
			if(v == 1 & ($("#status").val() == "" || $("#status").val() == "0")){
				$("#tr").show();
				top.layer.open({
				    type: 2, 
				    area: ['800px', '620px'],
				    title:"列推用户选择",
				    content: "${ctx}/oa/oaNotify/oaList?id="+$("#id").val() ,
				    btn: ['确定', '关闭'],
				    	yes: function(index, layero){
				    			//  点击确定之后再删除其内容
					    		$("#oaNotifyRecord").val("");
								$("#oaNotifyRecordId").val("");
								$("#oaNotifyRecordName").val("");
								document.getElementById("contentdict").innerHTML="";
				    		
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
							   
							    $("#oaNotifyRecord").val(ids);
								document.getElementById("contentdict").innerHTML += names;
								$("#oaNotifyRecordId").val(ids);
								$("#oaNotifyRecordName").val(names);
								
								top.layer.close(index);
						    	       },
		    	cancel: function(index){ //或者使用btn2
		    	           //按钮【按钮二】的回调
		    	       }
				}); 
			}else{
				$("#tr").hide();
			}
		}
		function push(num){
			if(num==4){
				$("#div2,#divtime").css("display", "block");
                $("#div1,#div3").css("display", "none");
			}else if(num==5){
				$("#div3,#divtime").css("display", "block");
                $("#div1,#div2").css("display", "none");
			}else{
				$("#div1,#divtime").css("display", "block");
                $("#div2,#div3,#divtime").css("display", "none");
                $("#endDate").val("");
			}
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="oaNotify" action="${ctx}/oa/oaNotify/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="status"/>
		<input id="isSelf" name="isSelf" type="hidden" value="${oaNotify.self}"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		      	 <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>标题：</label></td>
		         <td class="width-35" ><form:input path="title" maxlength="200" class="form-control required"/></td>
		         <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>类型：</label></td>
		         <td class="width-35" ><form:select path="type" class="form-control required" id="typeVal" onclick="push(this.value)">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('oa_notify_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select></td>
		      </tr>
		      <tr>
		      		<td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>推送类型：</label></td>
		       		<td class="width-35" colspan="3">
			       		<form:select path="pushType" class="form-control required" id="pushType" onchange="selectType(this.value)">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('oa_push_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<%--  <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>状态：</label></td>
			         <td class="width-35" ><form:radiobuttons path="status" items="${fns:getDictList('oa_notify_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/></td> --%>
	        </tr>
	        <c:if test="${oaNotify.status ne '1'}">
			     <tr id="tr" style="display:none">
			         <td  class="width-15 active">	
			         	<label class="pull-right"><font color="red">*</font>接受人：</label>
			         	<input id="oaNotifyRecord" name="oaNotifyRecord" value="${oaNotify.oaNotifyRecordIds}" type="hidden">
			        	<input id="oaNotifyRecordId" name="oaNotifyRecordIds" value="${oaNotify.oaNotifyRecordIds}" type="hidden">
			        	<input id="oaNotifyRecordName" name="oaNotifyRecordNames" value="${oaNotify.oaNotifyRecordNames}" type="hidden">
			         </td>
			         <td class="width-35" id="contentdict" colspan="3">
						${oaNotify.oaNotifyRecordNames}
					</td>
				</tr>
			</c:if>
			<c:if test="${oaNotify.status eq '1'}">
				<tr>
		           <td  class="width-15 active">	<label class="pull-right">接受人：</label></td>
		           <td class="width-35" colspan="3"><table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<th>接受人</th>
								<th>接受部门</th>
								<th>阅读状态</th>
								<th>阅读时间</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${oaNotify.oaNotifyRecordList}" var="oaNotifyRecord">
							<tr>
								<td>
									${oaNotifyRecord.user.name}
								</td>
								<td>
									${oaNotifyRecord.user.office.name}
								</td>
								<td>
									${fns:getDictLabel(oaNotifyRecord.readFlag, 'oa_notify_read', '')}
								</td>
								<td>
									<fmt:formatDate value="${oaNotifyRecord.readDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
					已查阅：${oaNotify.readNum} &nbsp; 未查阅：${oaNotify.unReadNum} &nbsp; 总共：${oaNotify.readNum + oaNotify.unReadNum}</td>
		      </tr>
			</c:if>
		      <tr>
		      	<td  class="width-15 active"><label class="pull-right">任务开始：</label></td>
		      	<td class="width-35" >
		      		<form:input path="startTime" id="beginDate" name="startTime" value="${oaNotify.start }" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm" readonly="true"/>
		      	</td>
		      	<td  class="width-15 active"><label class="pull-right"><font color="red">*</font>任务结束：</label></td>
		      	<td class="width-35" >
		      		<div id="divtime" style="display: none">
		      		<form:input path="endTime" id="endDate" name="endTime" value="${oaNotify.end }" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required" readonly="true"/>
		      		</div>
		      	</td>
		      </tr>
		       <tr>
		         <td  class="width-15 active">	
		         		<label class="pull-right"><font color="red">*</font>内容：</label>
		         </td>
		         <td class="width-35">
					  <div id="div1">
			          <form:textarea path='content' htmlEscape='false' rows='6' maxlength='2000' class='form-control required' id='content'/>
			          </div>
			         <div id="div2" style="display: none">
			         <sys:treeselect id="traincategorylesson" name="lessonId" value="${oaNotify.lessonId}" labelName="lessonName" labelValue="${oaNotify.lessonName}" 
							title="推送课程类别" url="/train/categorys/treeData?type=4" cssClass=" form-control required input-sm" allowClear="true" notAllowSelectParent="true"/>
			         </div>
			         <div id="div3"  style="display: none">
			         <sys:treeselect id="traincategory" name="categoryId" value="${oaNotify.categoryId}" labelName="name" labelValue="${oaNotify.name}" 
							title="推送考试类别" url="/train/categorys/treeData?type=2" cssClass=" form-control required input-sm" allowClear="true" notAllowSelectParent="true"/>
			         </div>
			         <!--  <textarea name="temp" style="width: 249px; height: 138px; display: none;" rows="6" id="temp" >temp</textarea> -->
		         </td>
		         
		         <td  class="width-15 active">	<label class="pull-right">附件：</label></td>
		         <td class="width-35" >
		         <c:if test="${oaNotify.status ne '1'}">
					<form:hidden id="files" path="files" htmlEscape="false" maxlength="255" class="form-control"/>
					<sys:ckfinder input="files" type="files" uploadPath="/oa/notify" selectMultiple="true"/>
				</c:if>
		         <c:if test="${oaNotify.status eq '1'}">
					<form:hidden id="files" path="files" htmlEscape="false" maxlength="255" class="form-control"/>
					<sys:ckfinder input="files" type="files" uploadPath="/oa/notify" selectMultiple="true" readonly="true" />
		         </c:if>
		         
		         
		         </td>
		      </tr>
		      
		</tbody>
		</table>
	</form:form>
</body>
</html>