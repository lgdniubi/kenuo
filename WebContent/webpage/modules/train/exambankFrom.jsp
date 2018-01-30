<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html lang="en">
<head>
    <meta name="decorator" content="default"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
    <script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		//一级分类改变事件，联动二级分类
		function nowcategorychange(v){
			if(v!="null"){
				$(".loading").show();//打开展示层
				$('#lessCategoryId').css('display', '');
				$.ajax({
					type : "POST",   
					url : "${ctx}/train/categorys/listtow?categoryId="+v,
					dataType: 'json',
					success: function(data) {
						$(".loading").hide();//隐藏展示层
						$("#lessCategoryId").empty();
						$.each(data.listtow, function(index,item){
							$("#lessCategoryId").prepend("<option value='"+item.categoryId+"'>"+item.name+"</option>");
						});
					}
				});  
			}
		}
		//显示后台传过来的值
		function categorychange(v,n){
			if((v !='')){
				$(".loading").show();//打开展示层
				$('#lessCategoryId').css('display', '');
				$.ajax({
					type : "POST",   
					url : "${ctx}/train/categorys/listtow?categoryId="+n,
					dataType: 'json',
					success: function(data) {
						$("#lessCategoryId").empty();
						$.each(data.listtow, function(index,item){
							if (item.categoryId == v) {
								$("#lessCategoryId").prepend("<option value='"+item.categoryId+"' selected='selected'>"+item.name+"</option>");
							}else{
								$("#lessCategoryId").append("<option value='"+item.categoryId+"'>"+item.name+"</option>");
							}
							document.getElementById("lessCategoryId").value=v;
						});
						$(".loading").hide();//隐藏展示层
					}
				});   
				document.getElementById("parentId").value=n;
			}
		}
		//页面加载事件
		$(document).ready(function() {
			//默认加载显示后台传过来的值
			var lessCateId = "${lessCategoryId}";
			categorychange(lessCateId,$("#parentId").val());
		});
		$(function() {
		    $('#treeTable thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		    	  $('#treeTable tbody tr td input.i-checks').iCheck('check');
		    	});

		    $('#treeTable thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		    	  $('#treeTable tbody tr td input.i-checks').iCheck('uncheck');
		    	});
		})
		
		function delAll() {
		  var str="";
		  var exerciseIds="";
		  $("#treeTable tbody tr td input.i-checks:checked").each(function(){
		   // if(true == $(this).is(':checked')){
		      str+=$(this).val()+",";
		    //}
		  });
		  if(str.substr(str.length-1)== ','){
			  exerciseIds = str.substr(0,str.length-1);
		  }
		  if(exerciseIds == ""){
			top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
			return;
		  }
			top.layer.confirm('确认要彻底删除数据吗?', {icon: 3, title:'系统提示'}, function(index){
				$("#exerciseIds").val(exerciseIds);
				$("#delAllData").submit();
			    top.layer.close(index);
			})
		}
		
		
	</script>
    <title>试题库</title>
</head>
<body>
    <div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>
	                <c:if test="${lessontype == 1}">
							课后习题管理
					</c:if>
					<c:if test="${lessontype == 2}">
							单元测试管理
					</c:if>
                </h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class="searcharea clearfix">
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="exercise" action="${ctx}/train/exambank/exambankFrom" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		 				<%-- <input id="parentId" name="parentId" value="${parentId}"  type="hidden"/>	 --%><!-- 一级父类ID   -->
		 				<input id="categoryId" name="categoryId" value="${categoryId}" type="hidden"/>	<!--课程分类ID  -->
		 				<input id="lessonId" name="lessonId" value="${lessonsId}" type="hidden"/>  <!--添加课程习题 翻页是提交课程ID  -->
						<input id="lessontype" name="lessontype" value="${lessontype}" type="hidden"/>	<!-- 判断课程习题还是单元试题 -->
						<input type="hidden" name="restNum" id="restNum"><!-- 重置表单  为了不影响批量添加提交课程id -->
                        <div class="form-group">
                            <label>关键字：<input id="exerciseTitle" name="exerciseTitle" type="text" value="${exercisesCategorys.exerciseTitle}" class="form-control" placeholder="请输入关键字" maxlength="10"></label> 
                            <!-- 下拉框二级联动 -->
                            <select class="form-control" id="parentId" name="parentId" onchange="nowcategorychange(this.options[this.options.selectedIndex].value)">
                            	<option value="null">请选择分类</option>
								<c:forEach items="${listone}" var="trainCategorys">
									<option ${trainCategorys.categoryId == exercisesCategorys.parentId?'selected="selected"':'' } value="${trainCategorys.categoryId}">${trainCategorys.name}</option>
								</c:forEach>
							</select>
							<select class="form-control" id="lessCategoryId" name="lessCategoryId" ></select>
							<select class="form-control" id="exerciseType" name="exerciseType">
								<option value=0>请选择试题类型</option>
								<c:forEach items="${fns:getDictList('exercise_type')}" var="exercise_type">
									<c:choose>
										<c:when test="${exercise_type.value eq exercisesCategorys.exerciseType}">
											<option value="${exercise_type.value }" selected="selected">${exercise_type.label }</option>
										</c:when>
										<c:otherwise>
											<option value="${exercise_type.value }">${exercise_type.label }</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
                        </div>
                    </form:form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<button id="addexam" class="btn btn-white btn-sm" title="添加试题" onclick="openDialog('批量试题', '${ctx}/train/exambank/exambank?lessonId=${lessonsId }&categoryId=${categoryId }&lessontype=${lessontype }','80%', '90%')" data-placement="left" data-toggle="tooltip">
									<i class="fa fa-plus"></i>添加试题
								</button>
	                        	<button class="btn btn-white btn-sm" title="随机试题" onclick="openDialog('随机试题', '${ctx}/train/exambank/random?lessonId=${lessonsId }&categoryId=${categoryId }&lessontype=${lessontype }','800px', '500px')" data-placement="left" data-toggle="tooltip">
									<i class="fa fa-plus"></i>随机试题
								</button>
								<shiro:hasPermission name="train:exercises:delete">
									<button class="btn btn-white btn-sm" onclick="delAll()"><i class="fa fa-trash-o">删除</i></button>
								</shiro:hasPermission>
	                        </div>
	                        <div class="pull-right">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
							</div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                    <thead>
                        <tr>
                            <th width="120" style="text-align: center;"><input type="checkbox" class="i-checks"></th>
                            <th style="text-align: center;">试题名称</th>
                            <th width="200" style="text-align: center;">试题分类</th>
                            <th width="200" style="text-align: center;">所属分类</th>
                            <th width="200" style="text-align: center;">发布时间</th>
                            <th width="100" style="text-align: center;">操作</th>
                        </tr>
                    </thead>
                    <tbody>
	                    <c:forEach items="${page.list}" var="Exercises">
	                        <tr style="text-align: center;">
	                            <td>
	                            	<input type="checkbox" class="i-checks" value="${Exercises.exerciseId }">
	                            </td>
	                            <td>${Exercises.exerciseTitle}</td>
	                            <td>
	                            	${fns:getDictLabel(Exercises.exerciseType, 'exercise_type', '')}
	                            </td>
	                            <td>${Exercises.name}</td>
	                            <td><fmt:formatDate value="${Exercises.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	                            <td>
		                            <shiro:hasPermission name="train:exercises:delete">
										<a href="${ctx}/train/exambank/delete?lessonId=${lessonsId}&categoryId=${categoryId}&exerciseIds=${Exercises.exerciseId}&lessontype=${lessontype}" onclick="return confirmx('确认要删除该试题吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
									</shiro:hasPermission>
								</td>
	                        </tr>
	                     </c:forEach> 
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="20">
                                <div class="tfoot">
                                </div>
                               	<table:page page="${page}"></table:page>
                            </td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
    </div>
    <div class="loading"></div> 
    <div style="display: none">
    <!-- 点击批量删除提交对应的from -->
    <form:form class="navbar-form navbar-left searcharea"  id="delAllData" modelAttribute="exercise" action="${ctx}/train/exambank/delete" method="post">
    	<input id="categoryId" name="categoryId" value="${categoryId}" />
		<input id="lessontype" name="lessontype" value="${lessontype}" />	
	    <input id="lessonId" name="lessonId" value="${lessonsId}" />
		<input id="exerciseIds" name="exerciseIds"/>
	</form:form>
	</div>
</body>
</html>