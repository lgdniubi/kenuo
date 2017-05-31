<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>编辑</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	 
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
			checktaskTime($("#task").val());
			validateForm = $("#inputForm").validate();
		});
		
		function checktaskTime(Tnum){
			$("#isTask").val(Tnum);
			if(Tnum == 1){
				$("#taskTime").show();
			}else{
				$("#taskTime").hide();
			}
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
						<form id="inputForm"  action="${ctx}/ec/mtmyArticleList/saveEdit">
							<input class="form-control" id="id" name="id" type="hidden" value="${mtmyArticle.id}"/>
							<input id="task" value="${mtmyArticle.isTask }" type="hidden">
							<input id="key" name="key" value="${key }" type="hidden">
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<c:if test="${key eq 'YEStask' }">
								<tr>
									<td class="active"><label class="pull-right"><font color="red">*</font>是否定时:</label></td>
									<td>
										<select class="form-control required" id="isTask" name="isTask" onchange="checktaskTime(this.value)">
											<option value=0>否</option>
											<option value=1>是</option>
										</select>
									</td>
								</tr>
								<tr id="taskTime" class="active">
									<td><label class="pull-right"><font color="red">*</font>定时时间:</label></td>
									<td>
										<input id="taskDate" name="taskDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" value="<fmt:formatDate value="${mtmyArticle.taskDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
											onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s}'})"/>
									</td>
								</tr>
								</c:if>
								<c:if test="${key eq 'NO' }">
								<tr>
									<td><label class="pull-right"><font color="red">*</font>类别：</label></td>
									<td>
										<select class="form-control required" id="categoryId" name="categoryId">
								   				<c:forEach items="${categoryList}" var="categoryList">
								   	   			<c:choose>
												<c:when test="${mtmyArticle.categoryId eq categoryList.id}">
													<option value="${categoryList.id}" selected="selected">${categoryList.name}</option>
												</c:when>
												<c:otherwise>
													<option value="${categoryList.id}">${categoryList.name}</option>
												</c:otherwise>
												</c:choose>
								   				</c:forEach>
										</select>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>排序：</label></td>
									<td>
										<input class="form-control required" id="sort" name="sort" type="text" value="${mtmyArticle.sort }" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>是否显示：</label></td>
									<td>
									 	<select class="form-control required" id="isShow" name="isShow">
											<option value=0 <c:if test="${mtmyArticle.isShow == 0}">selected</c:if>>是</option>
											<option value=1 <c:if test="${mtmyArticle.isShow == 1}">selected</c:if>>否</option>
										</select>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>是否推荐：</label></td>
									<td>
										<select class="form-control required" id="isRecommend" name="isRecommend">
											<option value=0 <c:if test="${mtmyArticle.isRecommend == 0}">selected</c:if>>是</option>
											<option value=1 <c:if test="${mtmyArticle.isRecommend == 1}">selected</c:if>>否</option>
										</select>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>是否置顶：</label></td>
									<td>
										<select class="form-control required" id="isTop" name="isTop">
											<option value=0 <c:if test="${mtmyArticle.isTop == 0}">selected</c:if>>是</option>
											<option value=1 <c:if test="${mtmyArticle.isTop == 1}">selected</c:if>>否</option>
										</select>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>是否头条：</label></td>
									<td>
										<select class="form-control required" id="isTopline" name="isTopline">
											<option value='0' <c:if test="${mtmyArticle.isTopline == '0'}">selected</c:if>>否</option>
											<option value='1' <c:if test="${mtmyArticle.isTopline == '1'}">selected</c:if>>是</option>
										</select>
									</td>
								</tr>
								</c:if>
							</table>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
	
</body>
</html>