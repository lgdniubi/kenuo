<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>客户详情</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	//  $(".loading").show();
	    $(document).ready(function() {
		    var start = {
			    elem: '#beginDate',
			    format: 'YYYY-MM-DD hh:mm:ss',
			    event: 'focus',
			    max: $("#endDate").val(),   //最大日期
			    istime: true,				//是否显示时间
			    isclear: false,				//是否显示清除
			    istoday: false,				//是否显示今天
			    issure: true,				//是否显示确定
			    festival: true,				//是否显示节日
			    choose: function(datas){
			         end.min = datas; 		//开始日选好后，重置结束日的最小日期
			         end.start = datas 		//将结束日的初始值设定为开始日
			    }
			};
			var end = {
			    elem: '#endDate',
			    format: 'YYYY-MM-DD hh:mm:ss',
			    event: 'focus',
			    min: $("#beginDate").val(),
			    istime: true,
			    isclear: false,
			    istoday: false,
			    issure: true,
			    festival: true,
			    choose: function(datas){
			        start.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			laydate(start);
			laydate(end);
	    })
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>客户详情</h5>
			</div>
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<div class="clearfix">
					<form id="searchForm" modelAttribute="" action="" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
					</form>
				</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							 <th style="text-align: center;">用户名</th>
						     <th style="text-align: center;">姓名</th>
						     <th style="text-align: center;">电话</th>
						     <th style="text-align: center;">订单数</th>
						     <th style="text-align: center;">注册时间</th>
						     <th style="text-align: center;">最近登陆时间</th>
						     <th style="text-align: center;">地区</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<tr>
						  	 <td>wangya</td>
						  	 <td>王丫丫</td>
						  	 <td>13833863698</td>
						  	 <td>12</td>
						  	 <td>2016-12-12 09:09—12:00</td>
						  	 <td>2012-09-09 09:09</td>
						  	 <td>北京</td>
					   </tr>
						  	 <td>wangya</td>
						  	 <td>里丫丫</td>
						  	 <td>13833863698</td>
						  	 <td>12</td>
						  	 <td>2016-12-12 09:09—12:00</td>
						  	 <td>2012-09-09 09:09</td>
						  	 <td>北京</td>
					 	</tr>
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
	<div class="loading"></div>
	<!--查看电话号码-->
		<div class="modal fade bs-example-modal-lg in" id="telphone" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" style="display: none; padding-right: 17px;">
    		<form class="modal-dialog modal-lg">
      			<div class="modal-content">
        			<div class="modal-header">
        				<span>查看电话</span>
          			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
        			</div>
        			<div class="modal-body">
          				<label><span>座机号码：</span><input class="form-control" type="tel" value=""/></label>
          				<label><span>手机号码：</span><input class="form-control" type="tel" value=""/></label>
        			</div>
        			<div class="modal-footer">
        				<a href="#" class="btn btn-success">保  存</a>
						<a href="#" class="btn btn-primary" data-dismiss="modal">关   闭</a>
        			</div>
      			</div>
    		</form>
  		</div>
  		<!--重置密码-->
  		<div class="modal fade bs-example-modal-lg in" id="resetpsd" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" style="display: none; padding-right: 17px;">
    		<form class="modal-dialog modal-lg">
      			<div class="modal-content">
        			<div class="modal-header">
        				<span>重置密码</span>
          			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
        			</div>
        			<div class="modal-body">
          				<label><span>输入新密码：</span><input class="form-control" type="text" value="" /></label>
        			</div>
        			<div class="modal-footer">
        				<input type="submit" class="btn btn-success" value="保 存">
						<a href="#" class="btn btn-primary" data-dismiss="modal">关   闭</a>
        			</div>
      			</div>
    		</form>
  		</div>
		<script src="${ctxStatic}/train/js/jquery.datetimepicker.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript">
			/*
			 * 弹框采用bootstrop 自带模态框插件
			 * http://v3.bootcss.com/javascript/#modals
			*/
			 $(function(){
			 	/*注册时间*/
				$('.datetimepicker').datetimepicker({lang:'ch'});
				
				// 1查看用户电话号码弹出框  可编辑
		        $('.infos').click(function(){
		        	//获取内容成功后将模态框展现
		           $('#telphone').modal("show");
		        });
		        //点击保存
		        $('#telphone .btn-success').click(function(){
		        	$('#telphone').modal("hide");
		        })

		        // 2重置密码
		        $('.resetpsd').click(function(){
		        	$('#resetpsd').modal('show');
		        });
		        // 保存密码
		        $('#resetpsd .btn-success').click(function(){
		        	$('#resetpsd').modal('hide');
		        });

			 })
		</script>
</body>
</html>