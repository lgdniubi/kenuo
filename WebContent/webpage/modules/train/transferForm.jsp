<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
	<head>
		<title>分类直播</title>
		<meta name="decorator" content="default" />
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
		<script type="text/javascript">
			$(function(){
		    	 $('#contentTable thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		       	  $('#contentTable tbody tr td input.i-checks').iCheck('check');
		       	});
		    	 $('#contentTable thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		       	  $('#contentTable tbody tr td input.i-checks').iCheck('uncheck');
		       	})
			})
			
			/* 转移直播 */
			function transferCategory(id,liveCategoryId){
				var idarr = new Array();
				var ids;
				if (id != "") {
					idarr.push(id);
					ids=idarr.join(',');
				}else{
					$('input[name="key"]:checked').each(function(){  
						 idarr.push($(this).val());//向数组中添加元素  
						});
					 ids=idarr.join(',');
				}
				if(ids == ""){
					top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
					return;
				  }else{
					  top.layer.open({
						    type: 2, 
						    area: ['300px', '420px'],
						    title:"选择分类",
						    content: "${ctx}/tag/treeselect?url="+encodeURIComponent("/train/category/treeData")+"",
						    btn: ['确定', '关闭'],
				    	    yes: function(index, layero){ //或者使用btn1
									var tree = layero.find("iframe")[0].contentWindow.tree;
									var nodes = tree.getSelectedNodes();
									var categoryId = nodes[0].id;
									window.location = "${ctx}/train/live/transferCategory?ids="+ids+"&categoryId="+categoryId+"&liveCategoryId="+liveCategoryId;
									top.layer.close(index);
								    	    },
				    	cancel: function(index){ //或者使用btn2
				    	           //按钮【按钮二】的回调
				    	       }
						}); 
				  }
			}
			
			/* 刷新 */
			function refresh(liveCategoryId){
				window.location = "${ctx}//train/live/transferForm?category.trainLiveCategoryId="+liveCategoryId;
			}
		</script>
	</head>
	<body>
		<div class="wrapper wrapper-content">
			<div class="ibox">
				<div class="ibox-content">
					<sys:message content="${message}" />
					<div class="row">
						<div class="col-sm-12">
							<form:form id="searchForm" modelAttribute="trainLiveAudit" action="${ctx}/train/live/transferForm" method="post" class="form-inline">
								<input id="categoryId" name="category.trainLiveCategoryId" type="hidden" value="${liveCatrgoryId}"/>
								<div class="form-group">
									<label>申请人：</label><form:input path="userName" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
									<label>手机号：</label><form:input path="mobile" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
									<label>直播房间：</label><form:input path="name" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
									<label>直播主题：</label><form:input path="title" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
									<span>归属机构：</span>
									<sys:treeselect id="organization" name="organization.id" value="${trainLiveAudit.organization.id}" labelName="organization.name" 
									labelValue="${trainLiveAudit.organization.name}" title="机构" url="/sys/office/treeData?type=2" cssClass=" form-control input-sm" allowClear="true" notAllowSelectRoot="false" notAllowSelectParent="false" />
								</div>
							</form:form>
							<br />
							<div class="pull-left">
								<shiro:hasPermission name="train:category:transfer">
									<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="transferCategory('','${liveCatrgoryId}')"><i class="fa fa-edit"></i> 批量转移</button>	
								</shiro:hasPermission>
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh('${liveCatrgoryId}')" title="刷新">
									<i class="glyphicon glyphicon-repeat"></i> 刷新
								</button>
							</div>
							<div class="pull-right">
								<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()">
									<i class="fa fa-search"></i> 查询
								</button>
								<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()">
									<i class="fa fa-refresh"></i> 重置
								</button>
							</div>
						</div>
					</div>
					<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<th style="text-align: center;"><input type="checkbox" class="i-checks"></th>
								<th style="text-align: center;">编号</th>
								<th style="text-align: center;">申请人</th>
								<th style="text-align: center;">直播房间</th>
								<th style="text-align: center;">直播id</th>
								<th style="text-align: center;">直播主题</th>
								<th style="text-align: center;">直播描述</th>
								<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${list }" var="tranLiveAudit">
								<tr>
									<td><input type="checkbox" class="i-checks" name="key" value="${tranLiveAudit.id }"></td>
									<td>${tranLiveAudit.id }</td>
									<td>${tranLiveAudit.userName }</td>
									<td>${tranLiveAudit.name }</td>
									<td>${tranLiveAudit.roomId }</td>
									<td>${tranLiveAudit.title }</td>
									<td>${tranLiveAudit.desc }</td>
									<td>
										<shiro:hasPermission name="train:live:view">
											<a href="#" onclick="openDialogView('查看直播', '${ctx}/train/live/form?id=${tranLiveAudit.id}','800px','650px')"
												class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>查看</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="train:category:transfer">
											<a href="#" onclick="transferCategory('${tranLiveAudit.id}','${tranLiveAudit.category.trainLiveCategoryId }')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 转移直播</a>
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