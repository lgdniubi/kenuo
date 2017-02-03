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
	});
</script>
<title>区域分布</title>
</head>
<body>
	<div class="wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>区域分布</h5>
			</div>
			<sys:message content="${message}" />
			<div class="ibox-content">
				<div id="main" style="width: 100%; height: 500px;"></div>
				<script type="text/javascript">
            	 // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('main'));
                      option = {
                      		title: {
                      	        text: '区域分布趋势'
                      	    },
                      	    tooltip: {
                      	        trigger: 'axis'
                      	    },
                      	    legend: {
                      	        data:['下单会员数','下单金额','下单量']
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
                      	            name:'下单会员数',
                      	            type:'bar',
                      	            data:[ 0 ]
                      	        },{
                      	            name:'下单金额',
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
      	            	    		url:'${ctx}/report/user/areaUserList',
      	            	    		dataType: 'json',
      	            	    		success:function(data){
      	            	    			if (data) {
      	            	    				options.xAxis[0].data  = data[0].xdata;//x轴赋值数据
	      	          						options.series[0].data = data[0].userNum;//y轴下单会员数赋值数据
	      	          						options.series[1].data = data[0].amount;//y轴下单金额赋值数据
	      	          						options.series[2].data = data[0].orderNum;//y轴下单量赋值数据
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