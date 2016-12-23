<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/echarts/echarts.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			getChartData();
			var start = {
					elem : '#begtime',
					format : 'YYYY-MM-DD',
					event : 'focus',
					max : $("#endtime").val(), //最大日期
					istime : false, //是否显示时间
					isclear : false, //是否显示清除
					istoday : false, //是否显示今天
					issure : true, //是否显示确定
					festival : true, //是否显示节日
					choose : function(datas) {
						end.min = datas; //开始日选好后，重置结束日的最小日期
						end.start = datas; //将结束日的初始值设定为开始日
					}
				};
				var end = {
					elem : '#endtime',
					format : 'YYYY-MM-DD',
					event : 'focus',
					min : $("#begtime").val(),
					istime : false,
					isclear : false,
					istoday : false,
					issure : true,
					festival : true,
					choose : function(datas) {
						start.max = datas; //结束日选好后，重置开始日的最大日期
					}
				};
				laydate(start);
				laydate(end);
		});
		function nowreset(){
			$("#begtime,#endtime,#parentId").val("");
			getChartData();
		}
	</script>
<title>类目分析</title>
</head>
<body>
	<div class="wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>类目分析</h5>
			</div>
			<sys:message content="${message}" />
			<div class="ibox-content">
				<div class="clearfix">
					<div id="searchForm" class="navbar-form navbar-left searcharea">
						<div class="form-group">
							<label>日期范围：</label> 
							<input id="begtime" name="begtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm" value="" style="width: 185px;" placeholder="开始时间" readonly="readonly" />
						  --<input id="endtime" name="endtime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" value="" style="width: 185px;" placeholder="结束时间" readonly="readonly" />&nbsp;&nbsp;
						  	<select class="form-control" id="parentId" name="parentId">
								<option value="">全部分类</option>
								<c:forEach items="${list }" var="category">
									<option value="${category.categoryId }">${category.name }</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="pull-right">
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="getChartData()" ><i class="fa fa-search"></i> 查询</button>
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="nowreset()" ><i class="fa fa-refresh"></i> 重置</button>
					</div>
				</div>
				<div id="main" style="width: 100%; height: 500px;"></div>
				<script type="text/javascript">
            	 // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('main'));
                      option = {
                      		title: {
                      	        text: '类目趋势'
                      	    },
                      	    tooltip: {
                      	        trigger: 'axis'
                      	    },
                      	    legend: {
                      	        data:['下单金额','下单商品数','下单量']
                      	    },
                      	    grid: {
                      	        left: '3%',
                      	        right: '4%',
                      	        bottom: '3%',
                      	        containLabel: true
                      	    },
                      	    toolbox: {
                      	        feature: {
                      	        	mark : {show: true},
                                    dataView : {show: true, readOnly: false},
                                    magicType: {show: true, type: ['line', 'bar']},
                                    restore : {show: true},
                                    saveAsImage : {show: true}
                      	        }
                      	    },
                      	    xAxis: {
                      	        type: 'category',
                      	        boundaryGap: true,
                      	        data: [ 0 ]
                      	    },
                      	    yAxis: {
                      	        type: 'value'
                      	    },
                      	  dataZoom: [
                      	            {
                      	                show: true,
                      	                start: 0,
                      	                end: 100
                      	            },
                      	            {
                      	                type: 'inside',
                      	                start: 0,
                      	                end: 100
                      	            },
                      	            {
                      	                show: true,
                      	                yAxisIndex: 0,
                      	                filterMode: 'empty',
                      	                width: 30,
                      	                height: '80%',
                      	                showDataShadow: false,
                      	                left: '98%'
                      	            }
                      	    ],
                      	    series: [
                      	        {
                      	            name:'下单金额',
                      	            type:'bar',
                      	            data:[ 0 ]
                      	        },{
                      	            name:'下单商品数',
                      	            type:'bar',
                      	            data:[ 0 ]
                      	        },{
                      	            name:'下单量',
                      	            type:'bar',
                      	            data:[ 0 ]
                      	        }
                      	    ]
                      };
                      myChart.setOption(option);
                      function getChartData(){
                    	 myChart.showLoading();		//自带等待样式
  	            		 var options = myChart.getOption();
        						$.ajax({
      	            	    		type : 'post',
      	            	    		url:'${ctx}/report/user/categoryReportList',
      	            	    		data:{"parentId":$(parentId).val(),"begtime":$(begtime).val(),"endtime":$(endtime).val()},
      	            	    		dataType: 'json',
      	            	    		success:function(data){
      	            	    			if (data) {
      	            	    				options.xAxis[0].data  = data[0].xdata;//x轴赋值数据
	      	          						options.series[0].data = data[0].amount;//y轴下单金额数赋值数据
	      	          						options.series[1].data = data[0].goodsnum;//y轴下单商品数赋值数据
	      	          						options.series[2].data = data[0].ordernum;//y轴下单量赋值数据
	      	          						myChart.setOption(options);
      	             					}
      	            	    		},
      	            	    		error : function(errorMsg) {
      	             					alert("图表请求数据失败啦!");
      	             				}
      	            	    	})
  	            	    	myChart.hideLoading();
  	            	 	}
				</script>
			</div>
		</div>
	</div>
</body>
</html>