<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>直播编辑</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->

<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  var liveRemarks=$("#liveRemarks").val();
			  var auditStatus=$("input[name=auditStatus]:checked").val();
			  var earningsRatio = $("input[name=earningsRatio]").val();
			  if(auditStatus==0){
				  if(liveRemarks==""){
					  top.layer.alert('审核失败备注说明不能为空!', {icon: 0, title:'提醒'}); 
					  return;
				  }
			  }
			  
			  //公开到每天美耶,才可以选择可见范围
			  if($('input[name=isOpen]:checked').val() == 1){
				  //每天美耶可见范围
				  if($('input[name=isOpenLabel]:checked').length <= 0){
						top.layer.alert('|每天美耶可见范围|必选！', {icon: 0, title:'提醒'});
						return false;
				  }else{
					  var str=document.getElementsByName("isOpenLabel");
	
					  var ids = "";
					  for (i=0;i<str.length;i++){
					      if(str[i].checked == true){
					    	  ids = ids + str[i].value + ",";
					      }
					  }
					  $("#isOpenRole").val(ids);
				  }
			  }else{//非公开,需要默认可见范围的值为'0,'
				  $("#isOpenRole").val("0,");
			  }
			
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		
		//是否公开到每天美耶
		function isShow(show){
			if(show == 0){//非公开
				$("#isOpenRoleShow").hide();
			}else{//公开
				$("#isOpenRoleShow").show();
			}
		}
		//每天美耶可见范围
		function queryIsOpen(value){
			var isOpen = $('input[name=isOpen]:checked').val();
			if($('input[name=isOpenLabel]:checked').length == 0){
				$("input[type=checkbox][name=isOpenLabel]").attr("disabled",false);
			}else{
				if(value == '0'){
					$("input[type=checkbox][name=isOpenLabel][id=notOpen]").attr("disabled",true);
				}else{
					$("input[type=checkbox][name=isOpenLabel][id=open]").attr("disabled",true);
				}				
			}
		}
	
		$(document).ready(function(){
			
			/* $("#reason").focus(); */
			validateForm = $("#inputForm").validate({
				rules: {
					earningsRatio:{
						digits:true,
						range:[0,99]
					} 
				},
				messages: {
					earningsRatio:{
						digits: "只能输入整数",
						range:"请输入一个介于 0 和 99 之间的值"
					}
				},
				submitHandler: function(form){
				//	loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					error.appendTo(element.parent());
					/* if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					} */
				}
			});
		
			//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
			//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
			//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
			/* $("#inputForm").validate().element($("#reason")); */
			
// 			laydate({
// 	            elem: '#userinfo.birthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
// 	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
// 	        });
			
			var earningsRatio = $("#earningsRatio").val();
			var afterEarningsRatio = parseInt(earningsRatio);
			$("#earningsRatio").val(afterEarningsRatio);
			
			var isOpen = "${trainLiveAudit.isOpen}";//是否公开到每天美耶
			if(isOpen == 0){//非公开到每天美耶,可见范围不能选择
				$("#isOpenRoleShow").hide();
			}else{
				$("#isOpenRoleShow").show();
				//每天美耶可见范围
				var isOpenIds = "${trainLiveAudit.isOpenRole}";
				if(isOpenIds.length > 0){
					var isOpenId = isOpenIds.split(",");
					for(q=0;q<isOpenId.length-1;q++){
						$("input[type=checkbox][name=isOpenLabel][value="+isOpenId[q]+"]").attr("checked",true);
						queryIsOpen(isOpenId[q]);
					}
				}
				
				//修改时,可见范围不能修改
				var auditStatus = "${trainLiveAudit.auditStatus}";
				if(auditStatus == 2 || auditStatus == 3 || auditStatus == 4){
					var id = "${trainLiveAudit.id}";
					if(id > 0){
						$("input[type=checkbox][name=isOpenLabel]").attr("disabled",true);
					}
				}
			}
		});
			
</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="trainLiveAudit" action="${ctx}/train/live/save" method="post" class="form-horizontal">
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<form:hidden path="userId" />
				<form:hidden path="id" />
				<tr>
					<td><label class="pull-right">申请人姓名:</label></td>
					<td><label>${trainLiveAudit.userName}</label> <%-- <form:input path="userName" htmlEscape="false" maxlength="50" class="form-control" readonly="true"/> --%>
					</td>
				</tr>
				<tr>
					<td><label class="pull-right">申请人描述:</label></td>
					<td><label>${trainLiveAudit.remarks}</label> <%-- <form:input path="remarks" htmlEscape="false" maxlength="64" class="form-control" readonly="true"/> --%>
					</td>
				</tr>

				<tr>
					<td><label class="pull-right">直播主题:</label></td>
					<td><label>${trainLiveAudit.title}</label> <%-- <form:input path="title" htmlEscape="false" maxlength="30" class="form-control"  readonly="true"/> --%>
					</td>
				</tr>
				<tr>
					<td><label class="pull-right">直播分类:</label></td>
					<td><sys:treeselect id="category" name="category.trainLiveCategoryId" value="${trainLiveAudit.category.trainLiveCategoryId}" labelName="categorye.name" labelValue="${trainLiveAudit.category.name}" title="直播分类"
									url="/train/category/treeData" cssClass=" form-control input-sm"/>
					</td>
				</tr>
				<tr>
					<td><label class="pull-right">直播房间名称:</label></td>
					<td><label>${trainLiveAudit.name}</label> <%-- <form:input path="name" htmlEscape="false" maxlength="300" class="form-control"  readonly="true"/> --%>
					</td>
				</tr>
				<tr>
					<td><label class="pull-right">是否设置密码:</label></td>
					<td><c:if test="${trainLiveAudit.authtype==1}">
							<label>有</label>&nbsp;
						</c:if> 
						<c:if test="${trainLiveAudit.authtype==2}">
			      			<label>无</label>
						</c:if></td>
				</tr>
				<tr>
					<td><label class="pull-right">房间密码:</label></td>
					<td><label>${trainLiveAudit.playpass}</label></td>
				</tr>
				<tr>
					<td><label class="pull-right">直播描述:</label></td>
					<td><label>${trainLiveAudit.desc}</label> <%-- <form:input path="desc" htmlEscape="false" maxlength="300" class="form-control" readonly="true"/> --%>
					</td>
				</tr>
				<tr>
					<td><label class="pull-right">直播时间:</label></td>
					<td><label><fmt:formatDate value="${trainLiveAudit.bengTime}" pattern="yyyy-MM-dd HH:mm:ss" /></label></td>
				</tr>
				<tr>
					<td><label class="pull-right">审核状态:</label></td>
					<td><input id="oldStatus" name="oldStatus" type="hidden" value="${trainLiveAudit.auditStatus}" /> 
						<c:if test="${trainLiveAudit.auditStatus==0}">
							<label><input id="auditStatus" name="auditStatus" type="radio" value="0" checked="checked" class="form"/>审核失败 </label>
							<label><input id="auditStatus" name="auditStatus" type="radio" value="2" class="form"/>审核通过</label>
						</c:if> 
						<c:if test="${trainLiveAudit.auditStatus==1}">
							<label><input id="auditStatus" name="auditStatus" type="radio" value="0" class="form"/>审核失败 </label>
							<label><input id="auditStatus" name="auditStatus" type="radio" value="1" checked="checked" class="form"/>请求审核</label>
							<label><input id="auditStatus" name="auditStatus" type="radio" value="2" class="form"/>审核通过</label>
						</c:if> 
						<c:if test="${trainLiveAudit.auditStatus==2}">
							<label><input id="auditStatus" name="auditStatus" type="radio" value="0" class="form"/>审核失败</label>
							<label><input id="auditStatus" name="auditStatus" type="radio" value="2" checked="checked" class="form"/>审核通过</label>
						</c:if> 
						<c:if test="${trainLiveAudit.auditStatus==3}">
							<label><input id="auditStatus" name="auditStatus" type="radio" value="3" checked="checked" class="form" />已完成</label>
						</c:if>
						<c:if test="${trainLiveAudit.auditStatus==4}">
							<label><input id="auditStatus" name="auditStatus" type="radio" value="4" checked="checked" class="form" />正在直播</label>
						</c:if>
					</td>
				</tr>
				<tr id="earningsRatioShow">
					<td><label class="pull-right">平台抽取比例:</label></td>
					<td> 
						<label><input id="earningsRatio" name="earningsRatio" type="text" value="${trainLiveAudit.earningsRatio*100}" class="form" onkeyup="this.value=this.value.replace(/[^\d.]/g,&quot;&quot;)" onpaste="this.value=this.value.replace(/[^\d.]/g,&quot;&quot;)" onfocus="if(value == '0')value=''" onblur="if(this.value == '')this.value='0';"/>% </label>
					</td>
				</tr>
				<tr id="earningsRatioShow">
					<td><label class="pull-right">权限设置:</label></td>
					<td> 
						<label>
							<sys:treeselect id="trainLiveAudit" name="companyIds" value="${trainLiveAudit.companyIds }" labelName="companyNames" labelValue="${trainLiveAudit.companyNames }"
								title="公司" url="/sys/franchisee/treeData" cssClass="form-control" checked="true" extId="1"/>
						</label>
					</td>
				</tr>
				<tr id="earningsRatioShow">
					<td><label class="pull-right">是否公开到每天美耶:</label></td>
					<td>
						<c:if test="${trainLiveAudit.auditStatus==4}">
							<label><input id="isOpen" name="isOpen" type="radio" value="1" ${(trainLiveAudit.isOpen==1)?'checked="checked"':''} class="form" disabled="disabled" onclick="isShow(1)"/>是</label>
							<label><input id="isOpen" name="isOpen" type="radio" value="0" ${(trainLiveAudit.isOpen==0)?'checked="checked"':''} class="form" disabled="disabled" onclick="isShow(0)"/>否</label>
						</c:if> 
						<c:if test="${trainLiveAudit.auditStatus!=4}">
							<label><input id="isOpen" name="isOpen" type="radio" value="1" ${(trainLiveAudit.isOpen==1)?'checked="checked"':''} class="form" onclick="isShow(1)"/>是</label>
							<label><input id="isOpen" name="isOpen" type="radio" value="0" ${(trainLiveAudit.isOpen==0)?'checked="checked"':''} class="form" onclick="isShow(0)"/>否</label>
						</c:if> 
					</td>
				</tr>
				<tr>
					<td><label class="pull-right">每天美耶可见范围:</label></td>	
					<td id="isOpenRoleShow">
						<label><input type="checkbox" id="open" name="isOpenLabel" value="0" onclick="queryIsOpen('0')">公开</label>
						<c:forEach items="${list}" var="franchisee">
							<label><input type="checkbox" id="notOpen" name="isOpenLabel" value="${franchisee.id}" onclick="queryIsOpen('1')">${franchisee.name}</label>
						</c:forEach>
						<input id="isOpenRole" value="${trainLiveAudit.isOpenRole}" name="isOpenRole" type="hidden">
					</td>			
				</tr>
				<tr>
					<td><label class="pull-right">是否付费:</label></td>
					<td>
						<c:if test="${trainLiveAudit.isPay==1}">
							<label><input id="isPay" name="isPay" type="radio" value="1" checked="checked" class="form" />免费</label>
							<label><input id="isPay" name="isPay" type="radio" value="2" class="form" />线下收费 </label>
							<label><input id="isPay" name="isPay" type="radio" value="3" class="form" disabled="disabled"/>线上收费</label>
						</c:if> 
						<c:if test="${trainLiveAudit.isPay==2}">
							<label><input id="isPay" name="isPay" type="radio" value="1" class="form" />免费</label>
							<label><input id="isPay" name="isPay" type="radio" value="2" checked="checked" class="form" />线下收费</label>
							<label><input id="isPay" name="isPay" type="radio" value="3" class="form" disabled="disabled"/>线上收费</label>
						</c:if> 
						<c:if test="${trainLiveAudit.isPay==3}">
							<label><input id="isPay" name="isPay" type="radio" value="1" class="form" disabled="disabled"/>免费</label>
							<label><input id="isPay" name="isPay" type="radio" value="2" class="form" disabled="disabled"/>线下收费</label>
							<label><input id="isPay" name="isPay" type="radio" value="3" checked="checked" class="form" />线上收费</label>
						</c:if> 
					</td>
				</tr>
				<tr>
					<td><label class="pull-right">备注说明（<font color="red">审核失败必须说明</font>）：
					</label></td>
					<td><form:textarea path="liveRemarks" htmlEscape="false"
							rows="3" maxlength="300" class="form-control"></form:textarea></td>
				</tr>
				<tr>
					<td><label class="pull-right">直播封面图片:</label></td>
					<td><img alt="封面图片" src="${trainLiveAudit.imgurl}"
						style="height: 300px; width: 450px"></td>
				</tr>

			</tbody>
		</table>

	</form:form>
</body>
</html>