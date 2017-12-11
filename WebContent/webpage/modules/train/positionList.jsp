<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
	<head>
		<title>职位管理</title>
		<meta name="decorator" content="default" />
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
		<script type="text/javascript">
		    function deletePositionButton(value) {
				var idarr = new Array();
				$('input[name="key"]:checked').each(function(){  
					 idarr.push($(this).val());//向数组中添加元素  
					});
				 var ids=idarr.join(',');
				 if(ids == ""){
						top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
						return;
					  };
				 top.layer.confirm('确认要彻底删除该职位吗?', {icon: 3, title:'系统提示'}, function(index){
					var url = "${ctx}/train/position/deleteAll?ids="+ids+"&department.dId="+value;
					window.location = url;
				    top.layer.close(index);
				 })
		    }
		    /* 页面刷新 */
		    function refresh(value) {
		    	window.location.href = "${ctx}/train/department/positionList?dId="+value;
			}
		    
		    function addPositionButton(dId) {
		    	var value = new Array();
		    	$("#contentTable tr #position_key").each(function(){
		    		value.push($(this).text());
		    	})
		    	var values=value.join(',');
		    	top.layer.open({
				    type: 2, 
				    area: ['800px', '600px'],
				    title:"添加职位",
			        maxmin: true, //开启最大化最小化按钮
				    content: "${ctx}/train/position/addPosition?department.dId="+dId+"&values="+values,
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){
		    	           var exiselect = layero.find("iframe")[0].contentWindow.document.getElementById("exiPosition");
			    	       var options = exiselect.options;
			    	       var exvalues = new Array(); 
			    	       for (var i = 0; i < options.length; i++) {
							exvalues.push(options[i].value);
						}
			    	       if (exvalues.length != 0) {
				    	       var posValues = exvalues.join(",");
				    	       $("#exiValues").val(posValues);
				    	       $('#addPositionForm').submit();
				    	       top.layer.close(index);
			    	       }
					  },
					cancel: function(index){ 
		    	       }
				});
			}
		    
		    $(function(){
		    	 $('#contentTable thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		       	  $('#contentTable tbody tr td input.i-checks').iCheck('check');
		       	});
		    	 $('#contentTable thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		       	  $('#contentTable tbody tr td input.i-checks').iCheck('uncheck');
		       	})
		    })
		</script>
	</head>
	<body>
		<div class="wrapper wrapper-content">
			<div class="ibox">
				<!-- 标题 -->
				<div class="ibox-title">
					<h5>职位列表</h5>
				</div>
				<!-- 主体内容 -->
				<div class="ibox-content">
					<sys:message content="${message}" />
						<div class="row">
						<div class="col-sm-12">
							<div class="pull-left">
								<!-- 增加按钮 -->
								<form id="addPositionForm" action="${ctx}/train/position/savePosition" method="post" class="hide">
									<input type="hidden" name="department.dId" value="${department.dId}"/>
									<input type="hidden" id="exiValues" name="values"/>
								</form>
								<shiro:hasPermission name="train:position:add">
									<button id="addPositionButton" type="submit"  class="btn btn-outline btn-primary btn-sm" title="添加" onclick="addPositionButton('${department.dId}')"><i class="fa fa-plus"></i> 添加</button>
								</shiro:hasPermission>
								<shiro:hasPermission name="train:position:del">
									<button type="button"  class="btn btn-white btn-sm" title="删除" onclick="deletePositionButton('${department.dId}')"><i class="fa fa-trash"></i> 删除</button>
								</shiro:hasPermission>
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh('${department.dId}')" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
							</div>
						</div>
						
						
					</div>
					<!-- 主要内容展示 -->
					<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<th style="text-align: center;"><input type="checkbox" class="i-checks"></th>
								<th style="text-align: center;">键值</th>
								<th style="text-align: center;">标签</th>
								<th style="text-align: center;">描述</th>
								<th style="text-align: center;">排序</th>
								<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${positionList}" var="position">
								<tr>
									<td style="text-align: center;"><input type="checkbox" name="key" class="i-checks" value="${position.value}"></td>
									<td id="position_key" style="text-align: center;">${position.value}</td>
									<td style="text-align: center;"><a href="#" onclick="openDialogView('查看职位', '${ctx}/train/position/form?value=${position.value}&type=${position.type }&department.dId=${position.department.dId }','800px', '650px')">${position.label}</a></td>
									<td style="text-align: center;">${position.remarks}</td>
									<td style="text-align: center;">${position.sort}</td>
									<td style="text-align: center;">
										<shiro:hasPermission name="train:position:view">
											<a href="#" onclick="openDialogView('查看职位', '${ctx}/train/position/form?value=${position.value}&type=${position.type }&department.dId=${position.department.dId }','800px', '650px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
										</shiro:hasPermission> 
										<shiro:hasPermission name="train:position:del">
											<c:if test="${position.delFlag == 0 }">
												<a href="${ctx}/train/position/deleteAll?ids=${position.value}&department.dId=${position.department.dId }" onclick="return confirmx('确定要删除该职位吗？',this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
											</c:if>
										</shiro:hasPermission>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</body>
</html>