<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>发布界面</title>
	<meta name="decorator" content="default"/>
	<!-- 日期控件 -->
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
		.layer-date{vertical-align: middle;}
	</style>
	<script type="text/javascript">
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if($("input[type=checkbox][name=checkType]:checked").length == 1){
				if($("input[type=checkbox][name=checkType]:checked").val() == "train"){
					if($("#trainsCategoryId").val() != 0){
						loading("正在提交，请稍候...");
						$("#inputForm").submit();
				    	return true;
					}else{
						top.layer.alert('未勾选分类！', {icon: 0});
						return false;
					}
				}else if($("input[type=checkbox][name=checkType]:checked").val() == "mtmy"){
					if($("#mtmyCategoryId").val() != 0){
						loading("正在提交，请稍候...");
						$("#inputForm").submit();
				    	return true;
					}else{
						top.layer.alert('未勾选分类！', {icon: 0});
						return false;
					}
				}
			}else if($("input[type=checkbox][name=checkType]:checked").length == 2){
				if($("#trainsCategoryId").val() != 0 && $("#mtmyCategoryId").val() != 0){
					loading("正在提交，请稍候...");
					$("#inputForm").submit();
			    	return true;
				}else{
					top.layer.alert('未勾选分类！', {icon: 0});
					return false;
				}
			}else{
				top.layer.alert('未勾选发布方', {icon: 0});
				return false;
			}
	    	return false;
	    };
	    function changeTrainCate(num){
	    	$("#trainsCategoryId").val(num); 
	    	$("#allTrainCate").find(".trainCate").css("background","");
	    }
		function selectTrainCommCate(num){
			$("#oldTrainsCategoryId").val(0);
			$("#trainCate"+num).css("background","#5BC0DE").siblings().css("background","");
			$("#trainsCategoryId").val(num); 
		}
		function changeMtmyCate(num){
			$("#mtmyCategoryId").val(num); 
			$("#allMtmyCate").find(".mtmyCate").css("background","");
	    }
		function selectMtmyCommCate(num){
			$("#oldMtmyCategoryId").val(0);
			$("#mtmyCate"+num).css("background","#5BC0DE").siblings().css("background","");
			$("#mtmyCategoryId").val(num); 
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
					<shiro:hasPermission name="ec:articles:findLogs">
						<button class="btn btn-primary btn-xs" title="发布日志" onclick="openDialog('发布日志', '${ctx}/ec/articles/findLogs?articleId=${articleRepository.articleId }','650px', '500px')" data-placement="left" data-toggle="tooltip">
							<i class="fa fa-calendar-o"></i> 发布日志
						</button>
					</shiro:hasPermission>
	                <div class="tab-inner">
						<form:form id="inputForm"  action="${ctx}/ec/articles/sendArticles" method="post">
							<input type="hidden" id="articleId" name="articleId" value="${articleRepository.articleId }">
							<div class="row">
								<div class="col-xs-12 col-md-12 "style="height:35px;line-height:35px;" >
									<input type="checkbox" id="checkType" name="checkType" value="train"> 妃子校
								</div>		
								<div class="col-xs-6 col-md-6" style="height:70px;line-height:35px;" id="allTrainCate">
									<span style="float:left;">分类：</span>
									<select id="oldTrainsCategoryId" name="oldTrainsCategoryId" class="form-control" style="width:200px;" onclick="changeTrainCate(this.value)">
										<option value=0>请选择分类</option>
										<c:forEach items="${trainsCategoryList }" var="list">
											<option value="${list.categoryId}">${list.name}</option>
										</c:forEach>
									</select>
									<c:forEach items="${trainCateCommList }" var="trainCateCommList">
										<input type="button" value="${trainCateCommList.name }" onclick="selectTrainCommCate(${trainCateCommList.categoryId })" class="trainCate btn btn-white btn-sm" id="trainCate${trainCateCommList.categoryId }">
									</c:forEach>
									<input id="trainsCategoryId" name="trainsCategoryId" type="hidden">
								</div>
								<div class="col-xs-6 col-md-6" style="height:35px;line-height:35px;">
									<span style="float:left;">发布时间：</span><input id="trainsTaskDate" name="trainsTaskDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" value="<fmt:formatDate value="${articles.taskDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
												onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s}'})"/>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-12 col-md-12 "style="height:35px;line-height:35px;" >
									<input type="checkbox" id="checkType" name="checkType" value="mtmy"> 每天美耶
								</div>
								<div class="col-xs-6 col-md-6" style="height:70px;line-height:35px;" id="allMtmyCate">
									<span style="float:left;">分类：</span>
									<select id="oldMtmyCategoryId" name="oldMtmyCategoryId" class="form-control" style="width:200px;" onclick="changeMtmyCate(this.value)">
										<option value=0>请选择分类</option>
										<c:forEach items="${mtmyCategoryList }" var="list">
											<option value="${list.id}">${list.name}</option>
										</c:forEach>
									</select>
									<c:forEach items="${mtmyCateCommList }" var="mtmyCateCommList">
										<input type="button" value="${mtmyCateCommList.name }" onclick="selectMtmyCommCate(${mtmyCateCommList.categoryId })" class="mtmyCate btn btn-white btn-sm" id="mtmyCate${mtmyCateCommList.categoryId }">
									</c:forEach>
									<input id="mtmyCategoryId" name="mtmyCategoryId" type="hidden">
								</div>
								<div class="col-xs-6 col-md-6" style="height:35px;line-height:35px;">
									<span style="float:left;">发布时间：</span>
									<input id="mtmyTaskDate" name="mtmyTaskDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" value="<fmt:formatDate value="${articles.taskDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
												onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s}'})"/>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>