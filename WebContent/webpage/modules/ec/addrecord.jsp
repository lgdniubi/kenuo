<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>添加服务记录</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/css/base.css"/>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>添加服务记录</h5>
			</div>
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="clearfix">
					<form id="searchForm" class="tab-content">
						<ul class="formArea">
							<li class="form-group">
								<span class="control-label col-sm-2">商品名称：</span><span class="form-control ">奥玛生机</span>
							</li>
							<li class="form-group">
								<span class="control-label col-sm-2">总服务次数(次)：</span><span class="form-control col-md-1">10</span>
							</li>
							<li class="form-group">
								<span class="control-label col-sm-2">已服务次数(次)：</span><span class="form-control col-md-1">8</span>
							</li>
							<li class="form-group">
								<span class="control-label col-sm-2">剩余次数(次)：</span><span class="form-control col-md-1">2</span>
							</li>
							<li class="form-group">
								<span class="control-label col-sm-2">服务时间：</span>
								<input class="form-control datetimepicker" type="text">
								<span>--</span>
								<input class="form-control datetimepicker" type="text">
							</li>
							<li class="form-group">
								<span class="control-label col-sm-2">选择店铺：</span>
								<select class="form-control" name="">
			                        <option value="0">一级分类</option>
			                        <option value="1">一级分类</option>
			                    </select>
			                    <select class="form-control" name="">
			                        <option value="0">二级分类</option>
			                        <option value="1">二级分类</option>
			                    </select>
			                    <select class="form-control" name="">
			                        <option value="0">二级分类</option>
			                        <option value="1">二级分类</option>
			                    </select>
							</li>
							<li class="form-group">
								<span class="control-label col-sm-2">选择美容师：</span>
								<select class="form-control" name="">
			                        <option value="0">一级分类</option>
			                        <option value="1">一级分类</option>
			                    </select>
			                </li>	
						</ul>
						<div class="box-footer">                        	
				    		<input type="submit" class="btn btn-primary" value="确认提交">
				    	</div>	
					</form>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
	<script src="${ctxStatic}/train/js/jquery.datetimepicker.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		$('.datetimepicker').datetimepicker({lang:'ch',timepicker:false,format:"Y-m-d"});
	</script>
</body>
</html>