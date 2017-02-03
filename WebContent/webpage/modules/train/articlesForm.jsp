<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <title>文章编辑</title>
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
    };
    
	$(document).ready(function() {
		check($("#show").val(),$("#recommend").val(),$("#top").val());
		checktaskTime($("#task").val())
		validateForm = $("#inputForm").validate();
	});
	function check(show,recommend,top){
		$("#isShow").val(show);
		$("#isRecommend").val(recommend);
		$("#isTop").val(top);
	}
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
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class="" id="reviewlists">
	                <form id="inputForm" action="${ctx}/train/articleslist/auditArticles" method="post" class="navbar-form navbar-left searcharea">
	                	<input id="show" value="${articles.isShow }" type="hidden">
	                	<input id="recommend" value="${articles.isRecommend }" type="hidden">
	                	<input id="top" value="${articles.isTop }" type="hidden">
	                	<input id="task" value="${articles.isTask }" type="hidden">
	                	<input id="key" name="key" value="${key }" type="hidden">
	                	<input id="flag" name="flag" value="${articles.flag }" type="hidden">
	                	<input id="articleId" name="articleId" value="${articles.articleId }" type="hidden">
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
									<input id="taskDate" name="taskDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" value="<fmt:formatDate value="${articles.taskDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
										onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s}'})"/>
								</td>
							</tr>
							</c:if>
							<c:if test="${key eq 'NO' }">
							<tr>
								<td class="active">
									<label class="pull-right"><font color="red">*</font>所属分类:</label>
								</td>
								<td>
									<select class="form-control required" id="categoryId" name="categoryId">
									   <c:forEach items="${categoryList}" var="categoryList">
									   	   <c:choose>
												<c:when test="${articles.categoryId eq categoryList.categoryId}">
													<option value="${categoryList.categoryId}" selected="selected">${categoryList.name}</option>
												</c:when>
												<c:otherwise>
													<option value="${categoryList.categoryId}">${categoryList.name}</option>
												</c:otherwise>
											</c:choose>
									   </c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>是否显示:</label></td>
								<td>
									<select class="form-control required" id="isShow" name="isShow">
										<option value=0>是</option>
										<option value=1>否</option>
									</select>
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>是否推荐:</label></td>
								<td>
									<select class="form-control required" id="isRecommend" name="isRecommend">
										<option value=0>是</option>
										<option value=1>否</option>
									</select>
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>是否置顶:</label></td>
								<td>
									<select class="form-control required" id="isTop" name="isTop">
										<option value=0>是</option>
										<option value=1>否</option>
									</select>
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>排序:</label></td>
								<td><input id="sort" name="sort" value="${articles.sort}" class="form-control required"></td>
							</tr>
							</c:if>
						</table>
					</form>
				</div>
                <table:page page="${page}"></table:page>
	        </div>
	    </div>
    </div>
</body>
</html>