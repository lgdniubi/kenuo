<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
<title>明星技师</title>
<meta name="decorator" content="default" />
<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">

<!-- 图片上传 引用-->
<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>

<script type="text/javascript">

	$(document).ready(function() {
		
		$('#treeTable thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
	    	$('#treeTable tbody tr td input.i-checks').iCheck('check');
	    });
	    $('#treeTable thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
	    	$('#treeTable tbody tr td input.i-checks').iCheck('uncheck');
	    });
	    
	});
	$(function(){
		$(".images").each(function(){
			var $this = $(this),
				$src = $this.attr('data-src');  
			$this.attr({'src':$src})
		});
	});
	function newSearch(){
		$("#searchForm").submit();
	}
	//重置信息
	function newreset(){
		$("#name").val("");
		$("#mobile").val("");
		$("#officeName").val("");
	}
	
	//明星技师修改查询出的信息
	function selectFunction(o){
		
		var userId=$("#selectId:checked").val();//用户ID
		var officeIds = $("#"+userId+"parentIds").val()+$("#"+userId+"officeid").val();
		var starBeautyName = $("#"+userId+"name").val();
		var starBeautyPhoto = $("#"+userId+"photo").val();
		//为各数据赋值
		$("#userId").val(userId);
		$("#officeIds").val(officeIds);
		$("#starBeautyName").val(starBeautyName);
		$("#img").attr("src",starBeautyPhoto);
		$("#starBeautyPhoto").val(starBeautyPhoto);
		
		$("#div1").show();
		
		//图片加载
		$("#file_img_upload").uploadify({
			'buttonText' : '请选择图片',
			'width' : 140,
			'method' : 'post',
			'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
			'uploader' : '<%=uploadURL%>',
			'fileObjName' : 'file_img_upload',//<input type="file"/>的name
			'queueID' : 'img',//与下面HTML的div.id对应
			'method' : 'post',
			'fileTypeDesc': '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
			'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
			'fileSizeLimit' : '10MB',//上传文件的大小限制
			'multi' : false,//设置为true时可以上传多个文件
			'auto' : true,//点击上传按钮才上传(false)
			'onFallback' : function(){
				//没有兼容的FLASH时触发
				alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
			},
			'onUploadSuccess' : function(file, data, response) { 
				var jsonData = $.parseJSON(data);//text 转 json
				if(jsonData.result == '200'){
					$("#starBeautyPhoto").val(jsonData.file_url);
					$("#img").attr('src',jsonData.file_url); 
				}
			}
		});
		
	}
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="user" action="${ctx}/ec/starBeauty/querySysUser" method="post" class="form-inline">
							<input type="hidden" id="starId" name="starId" value="${starId}"/>
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group" >
								<span>姓名：</span>
								<form:input path="name" htmlEscape="false" maxlength="50" class=" form-control input-sm" style="width:200px"/> 
								<span>电话：</span>
								<form:input path="mobile" htmlEscape="false" maxlength="50" class=" form-control input-sm" style="width:200px"/> 
								<span>归属机构：</span>
								<sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}" title="部门"
									url="/sys/office/treeData?type=2" cssClass=" form-control input-sm" allowClear="true"
									notAllowSelectRoot="false" notAllowSelectParent="false" />
							</div>
						</form:form>
					</div>
				</div>
				<br/>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-right">
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="newSearch()">
								<i class="fa fa-search"></i> 查询
							</button>
						</div>
					</div>
				</div>
				<!-- 主要内容展示 -->
				<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th style="text-align: center;"></th>
							<th style="text-align: center;">姓名</th>
							<th style="text-align: center;">所在店铺</th>
							<th style="text-align: center;">手机号码</th>
							<th style="text-align: center;">图片</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="user">
						<tr>
							<td><input id="selectId" name="selectId" type="radio" value="${user.id}"  class="form required"  onchange="selectFunction(this)"/></td>
							<td style="text-align: center;">${user.name}</td>
							<td style="text-align: center;">${user.office.name}</td>
							<td style="text-align: center;">${user.mobile}</td>
							<td style="text-align: center;" class="imgUrl">
								<img class="images" alt="" src="${ctxStatic}/images/lazylode.png"  data-src="${user.photo}" style="width: 150px;height: 100px;border:1px solid black; ">
							</td>
						</tr>
						<input type="hidden" id="${user.id}officeid" value="${user.office.id}"/>
						<input type="hidden" id="${user.id}parentIds" value="${user.office.parentIds}"/>
						<input type="hidden" id="${user.id}name" value="${user.name}"/>
						<input type="hidden" id="${user.id}photo" value="${user.photo}"/>
						</c:forEach>
					</tbody>
				</table>
				<!-- 分页table -->
				<table:page page="${page}"></table:page>
			</div>
			<div id="div1" style="display: none;">
				<form action="${ctx}/ec/starBeauty/saveStarBeauty" method="post" class="form-horizontal">
					<input type="hidden" id="userId" name="userId"/>
					<input type="hidden" id="officeIds" name="officeIds"/>
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
				  		<tr>
				  			<td><label class="pull-right"><font color="red">*</font>姓名：</label></td>
				       		<td><input id="starBeautyName" name="starBeautyName" style="width: 300px;height:30px;" class="form-control required"/></td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>图片：</label></td>
							<td>
								<img id="img" src="" alt="" style="width: 200px;height: 100px;"/>
								<input class="form-control" id="starBeautyPhoto" name="starBeautyPhoto" type="hidden"/><!-- 图片隐藏文本框 -->
								<input type="file" name="file_img_upload" id="file_img_upload" onclick="fileClick()">
								<div id="file_img_queue"></div>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>排序：</label></td>
				       		<td><input id="sort" name="sort" value="0" style="width: 300px;height:30px;" class="form-control required"/></td>
						</tr>
						<tr>
							<td><label class="pull-right">备注：</label></td>
							<td>
								<textarea id="remarks" name="remarks" class="form-control" style="resize: none;" cols="37" rows="4"></textarea>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#file_img_upload").uploadify({
				'buttonText' : '请选择图片',
				'width' : 140,
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_img_upload',//<input type="file"/>的name
				'queueID' : 'img',//与下面HTML的div.id对应
				'method' : 'post',
				'fileTypeDesc': '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
				'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '10MB',//上传文件的大小限制
				'multi' : false,//设置为true时可以上传多个文件
				'auto' : true,//点击上传按钮才上传(false)
				'onFallback' : function(){
					//没有兼容的FLASH时触发
					alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
				},
				'onUploadSuccess' : function(file, data, response) { 
					var jsonData = $.parseJSON(data);//text 转 json
					if(jsonData.result == '200'){
						$("#starBeautyPhoto").val(jsonData.file_url);
						$("#img").attr('src',jsonData.file_url); 
					}
				}
			});
		});
	</script>
</body>
</html>