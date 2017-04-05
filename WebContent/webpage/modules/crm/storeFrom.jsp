<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>投诉咨询</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">
<script type="text/javascript">

		function selectUser(){
			var mobile = $("#mobile").val();	
			if(mobile == ""){
				return;
			}
			$.ajax({
				type:"get",
				data:{
					mobile:mobile
				},
				url:"${ctx}/crm/store/getUser",
				success:function(date){
					if(date != null && date != ""){
 						top.layer.alert('会员信息查询成功!', {icon: 0, title:'提醒'});  
						$("#nickName").val(date.nickName);
						$("#name").val(date.name);
					}else{
						top.layer.alert('此号码不是会员!', {icon: 0, title:'提醒'}); 
						$("#nickName").val("");
						$("#name").val("");
					}
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
				}
			});
		}
		
		function gradeChange(){
			$.ajax({
				 type:"get",
				  data : {"redirectUserId":$("#select1").val(),
					      "id":$("#id").val()			  
				  }, 
				 url:"${ctx}/crm/store/getRedirectUserId",
				 dataType: 'json',
				 success:function(date){
					if(date > 0){
						top.layer.alert('转交人不能为已处理人或记录人！', {icon: 0, title:'提醒'});
						$("#select1").val(-1);
					}else{
					 }
				 },
				 error:function(XMLHttpRequest,textStatus,errorThrown) {
					$(".loading").hide();
				 }
			});
		   }
		
		 function selectType(v){
				if(($("#companyId").val() != null && $("#companyId").val() != "" )||($("#loginName").val() != null && $("#loginName").val() != "")||($("#officeId").val() != null && $("#officeId").val() != "")||($("#name").val() != null && $("#name").val() != "")){
					$(".loading").show();
					$("#select1").empty();
					$.ajax({
						 type:"get",
						 //data : $("#").serialize(),     //此处表单序列化
						  data : {"office.id":$("#officeId").val()}, 
						 url:"${ctx}/crm/store/oaList",
						 dataType: 'json',
						 success:function(date){
							$(".loading").hide();
							if(date.list.length>0){
								top.layer.alert('转交对象查询成功!', {icon: 0, title:'提醒'}); 
								$("#select1").append("<option value=''>请选择转交对象</option>")
								for(var i=0;i<date.list.length;i++){
									$("#select1").append("<option value='"+date.list[i].id+"'>"+date.list[i].name+"</option>")
								}
							}else{
								top.layer.alert('没有查询到转交对象!', {icon: 0, title:'提醒'});								
							}
						 },
						 error:function(XMLHttpRequest,textStatus,errorThrown) {
							$(".loading").hide();
						 }
					});
				}else{
					top.layer.alert('请先选择部门！', {icon: 0, title:'提醒'});
				}
			} 
		 
			var validateForm;
			function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
				if (validateForm.form()) {
					$("#inputForm").submit();
					return true;
				}
				return false;
			}
			$(document).ready(function() {
				$("#name").focus();
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
			});
		
		 	function toggle(id) {
				var tb = document.getElementById(id);
				if (tb.style.display == 'none')
					tb.style.display = 'block';
				else
					tb.style.display = 'none';
			}
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- <div class="ibox-title">
				<h5>投诉咨询</h5>
			</div> -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<div class="clearfix">
					<form:form id="inputForm" modelAttribute="complain" action="${ctx}/crm/store/save" method="post" class="form-horizontal">
						<form:hidden path="id" id="id"/>							
						<div class="row" style="text-align:center;margin:10px">				
							<div class="col-sm-12 col-offset-sm-1" style="text-align:left;">
								<div style="float:left;">
									<label ><font color="red">*</font>手机号码：&nbsp;</label>
								</div>
								<div style="float:left;">
									<form:input path="mobile" htmlEscape="false" maxlength="16" class="form-control required" style="width:200px" />
									<input class="btn btn-primary btn-sm pull-right" type="button" id="pushType" value="会员查询" onclick="selectUser()"/>
								</div>
							</div>									
						</div>
						<div class="row" style="text-align:center;margin:10px" >				
							<div class="col-sm-6 col-offset-sm-1" style="text-align:left;">
								<label ><font color="red">*</font>客户姓名：</label>
								<form:input id="name" path="name" htmlEscape="false" maxlength="11" class="form-control required" style="width:200px" />
							</div>
							<div class="col-sm-6 col-offset-sm-1" style="text-align:left;">
								<label ><font color="red">*</font>客户昵称：</label>
								<form:input id="nickName" path="nickName" htmlEscape="false" maxlength="10" class="form-control required" style="width:200px" />
							</div>												
						</div>	
						<div class="row" style="text-align:center;margin:10px">				
							 						
							<div class="col-sm-6 col-offset-sm-1" style="text-align:left;">
							    <label ><font color="red">*</font>品牌分类：</label>
								<select class="form-control" id="brandType" name="brandType" style="text-align: center; width:200px;">
									<c:forEach items="${goodsBrandList}" var="goodsBrand">
									  <option ${(goodsBrand.id == complain.brandType)?'selected="selected"':''} value="${goodsBrand.id}">${goodsBrand.name}</option>
								    </c:forEach> 
							    </select>
							</div>
							<div class="col-sm-6 col-offset-sm-1" style="text-align:left;float: left;">
								<label ><font color="red">*</font>问题主题：</label>
								<form:input path="theme" htmlEscape="false" maxlength="10" class="form-control required" style="width:200px" />
							</div>												
						</div>
						<div class="row" style="text-align:center;margin:10px">				
							<div class="col-sm-12 col-offset-sm-1" style="text-align:left;">
								<div style="float:left;">
									<label ><font color="red">*</font>消费门店：</label>&nbsp;
								</div>
								<div style="float:left;width:260px;">
								    <sys:treeselect id="officeId" name="officeId" value="${complain.officeId}" labelName="officeName" labelValue="${complain.officeName}" title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" notAllowSelectParent="false" notAllowSelectRoot="false"/>
								</div>
							</div>					
						</div>
						<div class="row" style="text-align:center;margin:10px">				
							<div class="col-sm-12 col-offset-sm-1" style="text-align:left;">
								<label ><font color="red">*</font>问题分类：</label>&nbsp;&nbsp;
								<input type="radio"name="questionType${status.index}" class=" required" value="1" ${(complain.questionType =='1')?'checked':''} />投诉 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="radio" name="questionType${status.index}" class=" required" value="2" ${(complain.questionType =='2')?'checked':''} />咨询 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="radio" name="questionType${status.index}" class=" required" value="3" ${(complain.questionType =='3')?'checked':''} />销售机会 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="radio" name="questionType${status.index}" class=" required" value="4" ${(complain.questionType =='4')?'checked':''} />其它
							</div>				
						</div>
						<div class="row" style="text-align:center;margin:10px">				
							<div class="col-sm-12 col-offset-sm-1" style="text-align:left;">
								<label ><font color="red">*</font>紧急程度：</label>&nbsp;&nbsp;
								<input type="radio" name="degree${status.index}" class=" required" value="1" ${(complain.degree =='1')?'checked':''} />非常紧急&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="radio" name="degree${status.index}" class=" required" value="2" ${(complain.degree =='2')?'checked':''} />急&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							    <input type="radio" name="degree${status.index}" class=" required" value="3" ${(complain.degree =='3')?'checked':''} />普通
							</div>				
						</div>						
						<div class="row" style="text-align:center;margin:10px">				
							<div class="col-sm-12 col-offset-sm-1" style="text-align:left;">
								<label ><font color="red">*</font>问题描述：</label>
								<form:textarea path="content" htmlEscape="false" rows="2" maxlength="200" class="form-control required" style="width:480px" />
							</div>				
						</div>	
						<div class="row" style="text-align:center;margin:10px" >				
							<div class="col-sm-12 col-offset-sm-1" style="text-align:left;">
								<label > &nbsp;回电号码：</label>
								<form:input path="callBack" htmlEscape="false" maxlength="16" class="form-control" style="width:200px" />
							</div>					
						</div>	
						<div class="row" style="text-align:center;margin:10px" >				
							<div class="col-sm-12 col-offset-sm-1" style="text-align:left;">
								<label ><font color="red">*</font>状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态：</label>&nbsp;&nbsp;
								<input type="radio" name="status" class="required" value="1" ${(complain.status =='1')?'checked':''} />未处理 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="radio" name="status" class="required" value="2" ${(complain.status =='2')?'checked':''} />已处理
							</div>					
						</div>	
						<div class="row" style="text-align:center;margin:10px" >				
							<div class="col-sm-12 col-offset-sm-1" style="text-align:left;">
								<label ><font color="red">*</font>花费时间：</label>
								<form:select path="recordTime" class="form-control" style="width:200px;">
									<form:option value="1">0-5分钟</form:option>
									<form:option value="2">6-15分钟</form:option>
									<form:option value="3">15-30分钟</form:option>
									<form:option value="3">30~~分钟</form:option>
								</form:select>
							</div>					
						</div>
				        	<div class="row" style="text-align:center;margin:10px">
								<div class="col-sm-6 col-offset-sm-1" style="text-align:left;">
									<label >&nbsp;记&nbsp;&nbsp;录&nbsp;&nbsp;人：</label>
	                                ${complain.creatBy}
								</div>	
							<c:if test="${complain.creatDate != null }">		
								<div class="col-sm-6 col-offset-sm-1" style="text-align:left;">
									<label >&nbsp;记录时间：</label>
	                                <fmt:formatDate value="${complain.creatDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /> 
								</div>	
				           </c:if>											        
				        	</div>										
						<hr />							
						
					 <c:if test="${complain.status != 2 and complain.questionSource != 2 and stamp != 1}">		    			 
						<input type="button" style="text-align:left;" class="btn btn-primary btn-sm pull-left" value="添加处理过程" onClick="toggle('table1')" />	    
						<hr/>	
						<div id="table1" style="display: none;">
						<!-- <table id="table1" style="display: none;" border="1" class="table table-bordered  table-condensed dataTables-example dataTable no-footer"> -->
							<div class="row" style="text-align:center;margin:10px">				
								<div class="col-sm-6 col-offset-sm-1" style="text-align:left;">
									<div style="float:left;"><label class="pull-right"><font color="red">*</font>紧急程度：</label></div>
									<div style="float:left;">&nbsp;&nbsp;
									<input type="radio" name="questionDegree${status.index}" class=" required" value="1" ${(complain.questionDegree =='1')?'checked':''} />非常紧急&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="questionDegree${status.index}" class=" required" value="2" ${(complain.questionDegree =='2')?'checked':''} />急 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="questionDegree${status.index}" class=" required" value="3" ${(complain.questionDegree =='3')?'checked':''} />普通
								    </div>
								</div>	
								<div class="col-sm-6 col-offset-sm-1" style="text-align:left;">
								    <div style="float:left;"><label class="pull-right"><font color="red">*</font>处理结果：</label></div>
								    <div style="float:left;">&nbsp;&nbsp;
									<input type="radio" name="handResult${status.index}" class=" required" value="1" ${(complain.handResult =='1')?'checked':''} />未处理&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="handResult${status.index}" class=" required" value="2" ${(complain.handResult =='2')?'checked':''} />已处理
								    </div>
								</div>				
							</div> 
					        <div class="row" style="text-align:center;margin:10px">				        
					        	<div class="col-sm-6 col-offset-sm-1" style="text-align:left;">
									<label >&nbsp;处&nbsp;&nbsp;理&nbsp;&nbsp;人：</label>
	                                ${complain.handler}
								</div>	
								<div class="col-sm-6 col-offset-sm-1" style="text-align:left;">
									<label >&nbsp;处理人工号：</label>
	                                ${complain.handlerID}
								</div>					        
				            </div>
				        	<div class="row" style="text-align:center;margin:10px">
					        	<div class="col-sm-12 col-offset-sm-1" style="text-align:left;">
									<label >&nbsp;解决方案：</label>
	                                <form:textarea path="solveContent" htmlEscape="false" rows="2" maxlength="200" class="form-control" style="width:480px;" />
								</div>					        
				       	 	</div>
				       	 	<c:if test="${complain.changeTimes != 2 }">
				        	<div class="row" style="text-align:center;margin:10px">
					        	<div class="col-sm-12 col-offset-sm-1" style="text-align:left;">
					        	    <div style="float:left;">
									   <label >&nbsp;转交对象：&nbsp;</label>
	                                 </div>
	                                 <div style="float:left;width:215px;">
										<sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}" title="部门"
											url="/sys/office/treeData?type=2" cssClass=" form-control" allowClear="true" 
											notAllowSelectRoot="false" notAllowSelectParent="false" />
									    <input id="parentDel" name="parentDel" value="0" type="hidden">
									 </div>
									 <div style="float:left;">
			         			     	 <select class="form-control" id="select1" name="redirectUserId" onchange="gradeChange()" style="text-align: center; width: 195px;">
			         			     	 </select>
								     </div>
									 <div style="float:left;">
									    <input class="btn btn-primary btn-sm pull-right" type="button" id="pushType" value="查询人员" onclick="selectType(this.value)"/>
									 </div>				        
				        	   </div>
				        	</div>
				        	</c:if>
				        	<div class="row" style="text-align:center;margin:10px">
								<div class="col-sm-12 col-offset-sm-1" style="text-align:left;">
									 <label >&nbsp;备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</label>
	                                 <form:textarea path="remarks" htmlEscape="false" rows="2" maxlength="200" class="form-control" style="width:480px;"/>
								</div>					        
				        	</div>
				        	<hr/>
				            </div>				        
				       </c:if>	
				       
					   <c:forEach items="${complains}" var="complains" varStatus="status">
							<div class="row" style="text-align:center;margin:10px">				
								<div class="col-sm-3 col-offset-sm-1" style="text-align:left;">
									<font color="#660000"><label >处理过程：</label>
									${complains.solveTimes}</font>
								</div>
								<div class="col-sm-4 col-offset-sm-1" style="text-align:left;">
									<label >&nbsp;紧急程度：</label>
	                                <c:if test="${complains.questionDegree =='1'}">
								             非常紧急
								    </c:if>
								    <c:if test="${complains.questionDegree =='2'}">
								             急
								    </c:if>
								    <c:if test="${complains.questionDegree =='3'}">
								             普通
									</c:if>
								</div>	
								<div class="col-sm-4 col-offset-sm-1" style="text-align:left;">
									<label >&nbsp;&nbsp;处理过程：</label>
									<c:if test="${complains.handResult =='1'}">
								             未处理 
								    </c:if>
									<c:if test="${complains.handResult =='2'}">
								            已处理
								    </c:if>
								</div>				
						    </div>
					        <div class="row" style="text-align:center;margin:10px">
					        	<div class="col-sm-3 col-offset-sm-1" style="text-align:left;">
									<label >&nbsp;处&nbsp;&nbsp;理&nbsp;&nbsp;人：</label>
	                                ${complains.handler}
								</div>
								<div class="col-sm-3 col-offset-sm-1" style="text-align:left;">
									<label >&nbsp;工&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</label>
	                                ${complains.handlerID}
								</div>
								<div class="col-sm-5 col-offset-sm-1" style="text-align:left;">
									<label >&nbsp;处理时间：</label>
	                                <fmt:formatDate value="${complains.cretDate}"
										pattern="yyyy-MM-dd HH:mm:ss" />
								</div>	
							</div>	
							<div class="row" style="text-align:center;margin:10px">
					        	<div class="col-sm-12 col-offset-sm-1" style="text-align:left;">
									<label >&nbsp;转交对象：</label>
	                                <c:if test="${complains.redirectUserId !=complains.handler}" var="young" scope="session">
	                                     ${complains.redirectUserId}
	                                 </c:if>
								</div>	
							</div>					        
					        <div class="row" style="text-align:center;margin:10px">
								<div class="col-sm-12 col-offset-sm-1" style="text-align:left;">
									<label >&nbsp;解决方案：</label>
	                                <textarea rows="2" maxlength="200" class="form-control" readonly="readonly" style="width:480px">${complains.solveContent}</textarea>                               
								</div>					        
					        </div>
					        <div class="row" style="text-align:center;margin:10px">
								<div class="col-sm-12 col-offset-sm-1" style="text-align:left;">
									<label >&nbsp;备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</label>
	                                <textarea rows="2" maxlength="200" class="form-control" readonly="readonly" style="width:480px">${complains.remarks}</textarea>                         
								</div>					        
					        </div>
					        <hr/>	
	                    </c:forEach>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>