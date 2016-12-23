<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
   	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
    
    <!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<!-- 照片放大 -->
	<link href="${ctxStatic}/train/css/imgbox.css" rel="stylesheet" />
	<script src="${ctxStatic}/train/js/jquery.min.js"></script>
	<script type="text/javascript">
		var jq = $.noConflict();
	</script>
	<script src="${ctxStatic}/train/js/jquery.imgbox.pack.js"></script>
    <script>
	    function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		function show(a,b,c,d,e){
			// 弹出框	a 判断为添加还是修改  b 若为修改时传类别ID  c 若为修改时传类别名称	d 若为修改时传排序 e 照片路径
			if(a==1){
				$("#efId").val(b);
				$("#name").val(c);
				$("#sort").val(d);
				if(null != e){
					$("#imgsrc").attr('src',e); 
					$('.img').attr('href',e);
					$("#img").val(e);
				}
			}else{
				$("#efId").val(0);
				$("#name").val('');
				$("#sort").val('');
				$("#imgsrc").attr('src',null); 
				$("#img").val('');
			}
	        $('#myReply').modal('show');           
		}
		
		$(function(){
			$(".effect_img img").each(function(){
				var $this = $(this),
					$src = $this.attr('data-src');  
				$this.attr({'src':$src})
			});
			
		});
    </script>
    <style type="text/css">
	.modal-content{
		margin:0 auto;
		width: 450px;
	}
	</style>
    <title>功效管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>功效管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<form id="searchForm" modelAttribute="" action="${ctx}/ec/goods/findEffectPage" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
					</form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<button class="btn btn-white btn-sm" title="添加文章" onclick='show(0,0,0,0)' data-placement="left" data-toggle="tooltip">
									<i class="fa fa-plus"></i>添加功效
								</button>
								<table:delRow url="${ctx}/ec/goods/deleteAllEffect" id="treeTable"></table:delRow><!-- 删除按钮 -->
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;"><input type="checkbox" class="i-checks"></th>
                			<th style="text-align: center;">分类名称</th>
                			<th style="text-align: center;">排序</th>
                			<th style="text-align: center;">创建时间</th>
                			<th style="text-align: center;">照片</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="effect">
						<tr>
							<td width="50" style="text-align: center;"><input type="checkbox" id="${effect.efId}" class="i-checks"></td>
							<td style="text-align: center;">${effect.name}</td>
							<td style="text-align: center;">${effect.sort}</td>
							<td style="text-align: center;"><fmt:formatDate value="${effect.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td style="text-align: center;"><a class='effect_img' href="${effect.img}"><img alt="" src="${ctxStatic}/images/lazylode.png" data-src="${effect.img}" style="width: 150px;height: 100px;"></a></td>
							<td style="text-align: center;">
		                        <a class="btn btn-success btn-xs" href="#" onclick="show(1,' ${effect.efId}','${effect.name}','${effect.sort}','${effect.img}')"><i class="fa fa-edit"></i>修改</a>
								<a href="${ctx}/ec/goods/deleteEffect?efId=${effect.efId}" onclick="return confirmx('确认要删除该类别吗？', this.href)"   class="btn btn-info btn-xs btn-danger"><i class="fa fa-trash"></i> 删除</a>
							</td>
						</tr>
						</c:forEach>
                    </tbody>
                    <tfoot>
 						<tr>
                            <td colspan="20">
                                <!-- 分页代码 --> 
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
    <!-- 弹出框 -->
    <div class="modal fade" id="myReply" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <form class="modal-dialog" role="document" id="reviewarea" action="${ctx}/ec/goods/saveEffect">
        	<!-- 获取类别ID -->
        	<input id="efId" name="efId" type="hidden"/>
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">功效管理</h4>
                </div>                            
                <div class="modal-body">
                   	功效名称：<input type="text" id="name" name="name" class="form-control" required size="26" placeholder="输入新添加功效" maxlength="10">
                   <p>&nbsp;</p>
                   	&nbsp;&nbsp;&nbsp;&nbsp;排&nbsp;&nbsp;&nbsp;序：<input type="text" id="sort" name="sort" class="form-control digits required" size="26" placeholder="输入排序数字" maxlength="4">
                   	 <p>&nbsp;</p>
                   	<p>功效图片：</p>
                   	<a class='img' href=""><img id="imgsrc" src="" alt="" style="width: 200px;height: 100px;"/></a>
					<input type="hidden" id="img" name="img" value=""><!-- 图片隐藏文本框 -->
					<p>&nbsp;</p>
                   	<div class="upload">
						<input type="file" name="file_img_upload" id="file_img_upload">
					</div>
					<div id="file_img_queue"></div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">确定</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                </div>
            </div>
        </form>
    </div>
    <script type="text/javascript">
		$.validator.setDefaults({
		    submitHandler: function() {
		    	$("#reviewarea").submit();
		    }
		});
		$().ready(function(){
		    $("#reviewarea").validate();
		});
		$(document).ready(function() {
			$("#file_img_upload").uploadify({
				'buttonText' : '请选择功效图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_img_upload',//<input type="file"/>的name
				'queueID' : 'file_img_queue',//与下面HTML的div.id对应
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
						$("#img").val(jsonData.file_url);
						$('.img').attr('href',jsonData.file_url);
						$("#imgsrc").attr('src',jsonData.file_url); 
					}
				}
			});
			jq("a[class='img']").imgbox({
				'speedIn'		: 0,
				'speedOut'		: 0,
				'alignment'		: 'center',
				'overlayShow'	: true,
				'allowMultiple'	: false
			});
			jq("a[class='effect_img']").imgbox({
				'speedIn'		: 0,
				'speedOut'		: 0,
				'alignment'		: 'center',
				'overlayShow'	: true,
				'allowMultiple'	: false
			});
		});
	</script>
</body>
</html>