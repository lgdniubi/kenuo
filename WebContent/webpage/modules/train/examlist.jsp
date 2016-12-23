<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
    <title>试题列表</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
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
				$('#categoryId').css('display', '');
				$.ajax({
					type : "POST",   
					url : "${ctx}/train/categorys/listtow?categoryId="+v,
					dataType: 'json',
					success: function(data) {
						$(".loading").hide();//隐藏展示层
						$("#categoryId").empty();
						$.each(data.listtow, function(index,item){
							$("#categoryId").prepend("<option value='"+item.categoryId+"'>"+item.name+"</option>");
						});
					}
				});  
			}else{
				$("#categoryId").empty();
				$('#categoryId').css('display', 'none');
			}
		}
		//显示后台传过来的值
		function categorychange(v,n){
			// v  子类    n  父类
			if((v !='')){
				$(".loading").show();//打开展示层
				$('#categoryId').css('display', '');
				$.ajax({
					type : "POST",   
					url : "${ctx}/train/categorys/listtow?categoryId="+n,
					dataType: 'json',
					success: function(data) {
						$(".loading").hide();//隐藏展示层
						$("#categoryId").empty();
						$.each(data.listtow, function(index,item){
							$("#categoryId").prepend("<option value='"+item.categoryId+"'>"+item.name+"</option>");
							document.getElementById("categoryId").value=v;
						});
				document.getElementById("s1").value=n;
					}
				});   
			}else{
				$("#categoryId").empty();
				$('#categoryId').css('display', 'none');
			}
		}
		//页面加载事件
		$(document).ready(function() {
			//默认加载显示后台传过来的值
			categorychange($("#categoryid1").val(),$("#parentId").val());
			
		});
		//重置
		/* function nowreset(){
			 $("#exerciseTitle").val(""); //清空
			 $("#s1").val(""); //清空
			 $("#categoryId").val(""); //清空
			 $("#exerciseType").val(""); //清空
		} */
	</script>
</head>
<body>
    <div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>试题列表</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="exercise" action="${ctx}/train/examlist/examlist" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		 				<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		 				<input id="parentId" name="parentId" value="${parentId}"  type="hidden"/>
		 				<input id="categoryid1" name="categoryid1" value="${categoryid1}" type="hidden"/>
		 				
                        <div class="form-group">
                            <label>关键字：<input id="exerciseTitle" name="exerciseTitle" type="text" value="${exerciseTitle}" class="form-control" placeholder="搜索标题或关键字" maxlength="10"></label> 
                            <!-- 下拉框二级联动 -->
                            <select class="form-control" id="s1" name="s1" onchange="nowcategorychange(this.options[this.options.selectedIndex].value)">
                            	<option value="null">请选择分类</option>
								<c:forEach items="${listone}" var="trainCategorys">
									<option value="${trainCategorys.categoryId}">${trainCategorys.name}</option>
								</c:forEach>
							</select>
							<select class="form-control" id="categoryId" name="name" ></select>
							<select class="form-control" id="exerciseType" name="exerciseType">
								<option value=0>请选择试题类型</option>
								<c:forEach items="${fns:getDictList('exercise_type')}" var="exercise_type">
									<c:choose>
										<c:when test="${exercise_type.value eq exerciseType}">
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
                    <div class="pull-right">
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
					</div>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="train:examlist:addexam">
		                        	<button class="btn btn-white btn-sm" title="添加" onclick='top.openTab("${ctx}/train/examlist/addexam","添加试题", false)' data-placement="left" data-toggle="tooltip">
										<i class="fa fa-plus"></i>添加试题
									</button>
			                    </shiro:hasPermission>
			                    <a href="#" onclick="openDialog('导入数据', '${ctx}/train/examlist/importPage','400px', '220px')" class="btn btn-white btn-sm" ><i class="fa fa-folder-open-o"></i> 导入</a>
	                        	<shiro:hasPermission name="train:course:deleteAll">
	                             	<table:delRow url="${ctx}/train/examlist/deleteAll" id="treeTable"></table:delRow><!-- 删除按钮 -->
	                            </shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                    <thead>
                        <tr>
                            <th width="120" style="text-align: center;"><label for="i-checks"><input type="checkbox" name="" id="i-checks" class="i-checks"></label></th>
                            <!--  
                            <th width="120">序号</th>
                            -->
                            <th width="200" style="text-align: center;">所属分类</th>
                            <th style="text-align: center;">试题名称</th>
                            <th width="200" style="text-align: center;">试题分类</th>
                            <th width="200" style="text-align: center;">添加时间</th>
                            <th width="150" style="text-align: center;">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${page.list}" var="Exercises">
	                       <tr style="text-align: center;">
	                            <td style="text-align: center;">
	                            <input type="checkbox" id="${Exercises.exerciseId}" name="ids" class="i-checks">
                            	</td>
	                            <td style="text-align: center;">${Exercises.name}</td>
	                            <td style="text-align: center;">${Exercises.exerciseTitle}</td>
	                        <!--      <td>${Exercises.createtime}</td>	-->
	                        	<td>${fns:getDictLabel(Exercises.exerciseType, 'exercise_type', '')}</td>
	                            <td><fmt:formatDate value="${Exercises.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	                            <td>
	                            	<!--修改按钮
		                            <a class="btn btn-circle btn-xg"><i class="glyphicon glyphicon-edit" onclick="openDialogView('试题基本信息 ', '${ctx}/train/examlist/examOne?exerciseId=${Exercises.exerciseId}','750px', '600px')"></i></a>　
		                             -->
		                             <shiro:hasPermission name="train:addexam:save">
		                             <a class="btn btn-info btn-xs btn-circle" 
										onclick="openDialogView('查看试题基本信息 ', '${ctx}/train/examlist/examOne?exerciseId=${Exercises.exerciseId}','750px', '600px')">
										<i class="fa fa-search-plus"></i>
									</a> 
		                            <a class="btn btn-success btn-xs btn-circle" 
										onclick="openDialog('修改试题基本信息 ', '${ctx}/train/examlist/examOne?exerciseId=${Exercises.exerciseId}','750px', '600px')">
										<i class="fa fa-edit"></i>
									</a> 
									</shiro:hasPermission>
		                            <!--删除按钮  -->
		                            <shiro:hasPermission name="train:addexam:save">
		                            	<a href="${ctx}/train/examlist/deleteOneExam?exerciseId=${Exercises.exerciseId}" onclick="return confirmx('要删除该试题吗？', this.href)" class="btn btn-circle btn-del"><i class=" glyphicon glyphicon-trash"></i></a>
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
                                <!-- 分页代码 --> 
                               	<table:page page="${page}"></table:page>
                            </td>	
                        </tr>
                    </tfoot>
                </table>
             </div>
         </div>
    </div>
    <div class="loading"></div> 
</body>
</html>