<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>订单报表</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/echarts/echarts.js"></script>
<script type="text/javascript">
	function page(n,s){
		
		$("#searchForm").submit();
		return false;
	}
	

	$(document).ready(function() {

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
				end.start = datas //将结束日的初始值设定为开始日
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

</script>
</head>


<body>

	<div class="wrapper wrapper-content">
		<div class="ibox">

			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="orderReport" action="#"
						method="post" class="form-inline">
						<div class="form-group">
							<label>查询日期：</label> <input id="begtime" name="begtime"
								type="text" maxlength="20"
								class="laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${orderReport.begtime}" pattern="yyyy-MM-dd"/>"
								style="width: 185px;" placeholder="开始时间" readonly="readonly" />
							一 <input id="endtime" name="endtime" type="text" maxlength="20"
								class=" laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${orderReport.endtime}" pattern="yyyy-MM-dd"/>"
								style="width: 185px;" placeholder="结束时间" readonly="readonly" />&nbsp;&nbsp;

						</div>
					</form:form>
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<div class="pull-left"></div>
							<div class="pull-right">
								<button class="btn btn-primary btn-rounded btn-outline btn-sm "
									onclick="getChartData()">
									<i class="fa fa-search"></i> 查询
								</button>
								<button class="btn btn-primary btn-rounded btn-outline btn-sm "
									onclick="reset()">
									<i class="fa fa-refresh"></i> 重置
								</button>
							</div>
						</div>
					</div>
				<label id="amount">订单金额：0.0元</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label id="userNum">会员数量：</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label id="orderNum">下单数量：</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label id="goodNum">商品数量：</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label id="totalMoney">商品金额：</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label id="avAmount">订单平均价：0.0元</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label id="avTotalMoney">商品平均价格：0.0元</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				
				</div>
				
			</div>

		</div>
	</div>

	<div id="main" style="width: 100%; height: 600px;"></div>
	<script type="text/javascript">
		// 基于准备好的dom，初始化echarts实例
		var myChart = echarts.init(document.getElementById('main'));

		//获取图表位置

		option = {
			title : {
				text : '订单总金额趋势（元）'
			},
			tooltip : {
				trigger : 'axis'
			},
			legend : {
				data : [ '订单总金额' ]
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			toolbox : {
				feature : {
					saveAsImage : {}
				}
			},
			xAxis : {
				type : 'category',
				boundaryGap : false,
				data : [ 0 ]
			},
			yAxis : {
				type : 'value'
			},
			series : [ {
				name : '订单总金额',
				type : 'line',
				stack : '总量',
				data : [ 0 ]
			}

			]

		}//这里内容可以直接从Echat自带的列子中取
		myChart.setOption(option);//将图表内容格式内容放入到myChart位置
		myChart.hideLoading();
		//getChartData();//aja后台交互

		//后台交互
		function getChartData() {
			//获得图表的options对象
			var options = myChart.getOption();
			//  alert(options);

			  var begtime = $("#begtime ").val();
			  var endtime= $("#endtime").val();
			  var _data = {"begtime":begtime,"endtime":endtime};//这里可以加请求的参数

			//通过ajax获取数据
			$.ajax({
				type : "post",
				url : "${ctx}/report/order/lineData",//请求路径
				dataType : "json",//返回数据形式为json
				data : _data,
				success : function(result) {
					if (result) {
						//alert(result[0].legend);
						//options.legend.data = result[0].legend;  //legend赋值数据
						options.xAxis[0].data = result[0].xdata;//x轴赋值数据
						options.series[0].data = result[0].data;//y轴赋值数据
						myChart.hideLoading();
						myChart.setOption(options);
					}
				},
				error : function(errorMsg) {
					alert("图表请求数据失败啦!");
					myChart.hideLoading();
				}
			})
			getOrderData();
		}
		
		function getOrderData(){
			
			 var begtime = $("#begtime ").val();
			  var endtime= $("#endtime").val();
			  var _data = {"begtime":begtime,"endtime":endtime};//这里可以加请求的参数

			
			$.ajax({
				type : "post",
				url : "${ctx}/report/order/getOrderData",//请求路径
				dataType : "json",//返回数据形式为json
				data : _data,
				success : function(result) {				
					if (result) {
						//alert(result.amount);
						$("#amount").html("订单金额："+result.amount+".0元");
						$("#userNum").html("会员数量："+result.userNum);
						$("#orderNum").html("下单数量："+result.orderNum);
						$("#goodNum").html("商品数量："+result.goodNum);
						$("#totalMoney").html("商品金额："+result.totalMoney);
						$("#avAmount").html("订单平均价："+result.avAmount+".0元");
						$("#avTotalMoney").html("商品平均价格："+result.avTotalMoney+".0元");
					}
				}
				
			})
		}


	</script>
</body>



</html>