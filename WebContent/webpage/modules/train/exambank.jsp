<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html lang="en">
<head>
    <meta name="decorator" content="default"/>
   <%--  <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css"> --%>
    <%-- <script type="text/javascript" src="${ctxStatic}/train/js/jquery-1.11.3.js"></script> --%>
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
			if((v !='')){
				$(".loading").show();//打开展示层
				$('#categoryId').css('display', '');
				$.ajax({
					type : "POST",   
					url : "${ctx}/train/categorys/listtow?categoryId="+n,
					dataType: 'json',
					success: function(data) {
						$("#categoryId").empty();
						$.each(data.listtow, function(index,item){
							if (item.categoryId == v) {
								$("#categoryId").prepend("<option value='"+item.categoryId+"' selected='selected'>"+item.name+"</option>");
							}else{
								$("#categoryId").prepend("<option value='"+item.categoryId+"'>"+item.name+"</option>");
							}
							document.getElementById("categoryId").value=v;
						});
						$(".loading").hide();//隐藏展示层
					}
				});   
				document.getElementById("parentId").value=n;
			}else{
				$("#categoryId").empty();
				$('#categoryId').css('display', 'none');
			}
		}
		//页面加载事件
		$(document).ready(function() {
			//默认加载显示后台传过来的值
				categorychange($("#categoryId1").val(),$("#parentId").val());
				if($("#examLessionMapping").val().length>0){
					check($("#examLessionMapping").val());	
				}
		});
		
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
        	var exerciseIds =  $('textarea').text();
			if(exerciseIds == ''){
				 top.layer.alert("请选择一条数据！", {icon: 0, title:'提醒'}); 
				 return false;
			}else{
        	  loading('正在提交，请稍等...');
		      $("#inputForm").submit();
	     	  return true;
			}
			return false;
		}
		/* function addAllExam(){
			$("#all").submit();
		} */
		//默认选中已添加的试题
		/* function check(num){
			var arr=new Array();
            arr=num.split(',');//注split可以用字符或字符串分割
            for(var i=0;i<arr.length;i++){
          	  $("input[type=checkbox][name=exerciseId][value="+arr[i]+"]").attr("checked",'checked');
          	  		if( $("input[type=checkbox][name=exerciseId][value="+arr[i]+"]").is(':checked')){
          	  			//将当前页面复选框选中的值用，替代 
          	  			num=num.replace($("input[type=checkbox][name=exerciseId][value="+arr[i]+"]").val(),",");  
          	  		}
          	  	document.getElementById("text").value=num;
            } 
        }  */
		//重置
		/* function nowreset(){
			 $("#exerciseTitle").val(""); //清空
			 $("#s1").val(""); //清空
			 $("#categoryId").val(""); //清空
			 $("#exerciseType").val(""); //清空
			 $("#restNum").val("-1");
		} */
	</script>
    <title>试题库</title>
</head>
<body>
    <div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>
	                <c:if test="${lessontype == 1}">
							添加课后习题
					</c:if>
					<c:if test="${lessontype == 2}">
							添加单元试题
					</c:if>
                </h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class="searcharea clearfix">
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="exercise" action="${ctx}/train/exambank/exambank" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		 				<%-- <input id="parentId" name="parentId" value="${parentId}"  type="hidden"/> --%>	<!-- 一级父类ID   -->
		 				<input id="categoryId1" name="ziCategoryId" value="${ziCategoryId}" type="hidden"/>	<!--二级子类ID  -->
		 				<input id="lessonId" name="lessonId" value="${exercisesCategorys.lessonId}" type="hidden"/>  <!--添加课程习题 翻页是提交课程ID  -->
		 				<%-- <input id="ziCategoryId" name="ziCategoryId" value="${ziCategoryId}" type="hidden"/> --%>  <!--添加单元试题 翻页是提交二级类别ID  -->
						<input id="lessontype" name="lessontype" value="${exercisesCategorys.lessontype}" type="hidden"/>	<!-- 判断课程习题还是单元试题 -->
						<input type="hidden" name="restNum" id="restNum"><!-- 重置表单  为了不影响批量添加提交课程id -->
                        <div class="form-group">
                            <label>关键字：<input id="exerciseTitle" name="exerciseTitle" type="text" value="${exerciseTitle}" class="form-control" placeholder="请输入关键字" maxlength="10"></label> 
                            <!-- 下拉框二级联动 -->
                            <select class="form-control" id="parentId" name="parentId" onchange="nowcategorychange(this.options[this.options.selectedIndex].value)">
                            	<option value="null">请选择分类</option>
								<c:forEach items="${listone}" var="trainCategorys">
									<option ${trainCategorys.categoryId == parentId?'selected="selected"':'' } value="${trainCategorys.categoryId}">${trainCategorys.name}</option>
								</c:forEach>
							</select>
							<select class="form-control" id="categoryId" name="categoryId" ></select>
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
                        <div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
                    </form:form>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                    <thead>
                        <tr>
                            <th width="120" style="text-align: center;"><label for="checkall"><input type="checkbox" name="" id="checkall" ></label></th>
                            <!--  
                            <th width="120">序号</th>
                            -->
                            <th style="text-align: center;">试题名称</th>
                            <th width="200" style="text-align: center;">试题分类</th>
                            <th width="200" style="text-align: center;">所属分类</th>
                            <th width="200" style="text-align: center;">发布时间</th>
                        </tr>
                    </thead>
                    <tbody>
                		<input id="examLessionMapping" value="${examLessionMapping}" type="hidden"/>	<!-- 课程ID  -->
	                    <c:forEach items="${page.list}" var="Exercises">
	                        <tr style="text-align: center;">
	                            <td><input type="checkbox" name="exerciseId" class="checkall" value="${Exercises.exerciseId}"></td>
	                            <td>${Exercises.exerciseTitle}</td>
	                            <td>
	                            	${fns:getDictLabel(Exercises.exerciseType, 'exercise_type', '')}
	                            </td>
	                            <td>${Exercises.name}</td>
	                            <td><fmt:formatDate value="${Exercises.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	                        </tr>
	                     </c:forEach> 
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="20">
                                <div class="tfoot">
                                </div>
                                <!-- 分页代码 btn btn-default  glyphicon glyphicon-ok--> 
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
    <!-- 点击批量添加提交对应的from -->
    <form:form class="navbar-form navbar-left searcharea"  id="inputForm" modelAttribute="exercise" action="${ctx}/train/exambank/addAllExam" method="post">
    	<input id="ziCategoryId" name="ziCategoryId" value="${ziCategoryId}" />  
		<input id="lessontype" name="lessontype" value="${exercisesCategorys.lessontype}" />	
	    <input id="lessonId" name="lessonId" value="${exercisesCategorys.lessonId}" />
		<input id="text" name="exerciseId1"/>
		<textarea rows="5px" cols="200px" name="exerciseId2"></textarea>
	</form:form>
	</div>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<script type="text/javascript">
	  //  $(function(){
	   //     var $load = $('#loading');
	        // 全选
	        $('#checkall').click(function(){
	        	$("#addexam").attr("disabled", false); 
		        var result=""; 
	        	if ($(this).is(':checked')) {
	        		$(":checkbox").prop('checked',true); 
	        			$(this)} else {
	        				$(":checkbox").prop('checked',false);
        		}
	        	$("input[name='exerciseId']:checked").each(function(){
		               result+=$(this).val()+','; 
		        }); 
	        	//判断全选框是否被选中  
		        	if($("input[name='exerciseId']:checked")){
			        	$('textarea').text(result); 
		        	}else{
		        		result.splice($(this),1)
		        	}
        	});
	      //将已选中的值放入文本框中
			$("input[name='exerciseId']").change(function(){
				$("#addexam").attr("disabled", false); 
		        var result="";
		        $("input[name='exerciseId']:checked").each(function(){
		              	result+=$(this).val()+',';
		        }); 
		        $('textarea').text(result);
		    })
	</script>
</body>
</html>