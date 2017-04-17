<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
<title>特殊美容师管理</title>
<meta name="decorator" content="default" />
<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
<script type="text/javascript">
 function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
	 var str="";
	 var ids="";
	 $("#treeTable tbody tr td input.i-checks:checkbox").each(function(){
		 if(true == $(this).is(':checked')){
	      str+=$(this).attr("id")+",";
	    }
	  });
	  if(str.substr(str.length-1)== ','){
	    ids = str.substr(0,str.length-1);
	  }
	  if(ids != ""){
		 $("#ids").val(ids);
		 loading("正在提交，请稍候...");
		 $("#inputForm").submit();
		 return true;
	  }
	  top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
	  return false;
 }

$(document).ready(function() {
	
	$('#treeTable thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
    	$('#treeTable tbody tr td input.i-checks').iCheck('check');
    });
    $('#treeTable thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
    	$('#treeTable tbody tr td input.i-checks').iCheck('uncheck');
    });
    
	$("#shopButton").click(function(){
		
		var bazaarId = $("#companyId").val();
		// 是否限制选择，如果限制，设置为disabled
		if ($("#shopButton").hasClass("disabled")){
			return true;
		}
		
		if(bazaarId == null || bazaarId == ""){
			top.layer.alert('请先选择市场!', {icon: 0, title:'提醒'});
		}else{
			// 正常打开	
			top.layer.open({
			    type: 2, 
			    area: ['300px', '420px'],
			    title:"选择所在店铺",
			    ajaxData:{selectIds: $("#shopId").val()},
			    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/ec/specEquipment/treeDataForShop?id="+bazaarId)+"&module=&checked=&extId=&isAll=" ,
			    	btn: ['确定', '关闭']
	    	       ,yes: function(index, layero){ //或者使用btn1
	    	    	   var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
						var ids = [], names = [], nodes = [];
						if ("" == "true"){
							nodes = tree.getCheckedNodes(true);
						}else{
							nodes = tree.getSelectedNodes();
						}
						for(var i=0; i<nodes.length; i++) {//
							ids.push(nodes[i].id);
							names.push(nodes[i].name);//
							break; // 如果为非复选框选择，则返回第一个选择  
						}
						$("#officeId").val(ids.join(",").replace(/u_/ig,""));
						$("#officeName").val(names.join(","));
						$("#officeName").focus();
						//预约管理-->> 修改预约地址时异步加载美容师   2016-06-29  咖啡
						if("shop" == "officeIdbeaut"){
							findBeauty();
						}
						top.layer.close(index);
				    	       },
	    	cancel: function(index){ //或者使用btn2
	    	           //按钮【按钮二】的回调
	    	       }
			}); 
	
		}
	});
	
	$("#companyButton").click(function(){
		$("#officeId").val("");
		$("#officeName").val("");
		// 是否限制选择，如果限制，设置为disabled
		if ($("#companyButton").hasClass("disabled")){
			return true;
		}
		// 正常打开	
		top.layer.open({
		    type: 2, 
		    area: ['300px', '420px'],
		    title:"选择市场",
		    ajaxData:{selectIds: $("#companyId").val()},
		    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/sys/office/treeData?isGrade=true")+"&module=&checked=&extId=&isAll=" ,
		    btn: ['确定', '关闭']
    	       ,yes: function(index, layero){ //或者使用btn1
						var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
						var ids = [], names = [], nodes = [];
						if ("" == "true"){
							nodes = tree.getCheckedNodes(true);
						}else{
							nodes = tree.getSelectedNodes();
						}
						for(var i=0; i<nodes.length; i++) {//
							/* if (nodes[i].level == 0){
								//top.$.jBox.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
								top.layer.msg("不能选择根节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
								return false;
							}// */
							if (nodes[i].isParent){
								//top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
								//layer.msg('有表情地提示');
								top.layer.msg("不能选择父节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
								return false;
							}//
							ids.push(nodes[i].id);
							names.push(nodes[i].name);//
							break; // 如果为非复选框选择，则返回第一个选择  
						}
						$("#companyId").val(ids.join(",").replace(/u_/ig,""));
						$("#companyName").val(names.join(","));
						$("#companyName").focus();
						//预约管理-->> 修改预约地址时异步加载美容师   2016-06-29  咖啡
						if("company" == "officeIdbeaut"){
							findBeauty();
						}
						top.layer.close(index);
				    	       },
    	cancel: function(index){ //或者使用btn2
    	           //按钮【按钮二】的回调
    	       }
		}); 
	
	});
});	
	function newSearch(){
		if($("#companyId").val() == null || $("#companyId").val()== "" ){
			top.layer.alert('请先选择市场!', {icon: 0, title:'提醒'});
		}else{
			$("#searchForm").submit();
		}
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
						<form:form id="searchForm" modelAttribute="user" action="${ctx}/sys/specBeautician/listForSpecial" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group" >
								<span>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</span>
								<form:input path="name" htmlEscape="false" maxlength="50" class=" form-control input-sm" style="width:200px"/> 
								<span><font color="red">*</font>选择市场：</span> 
								<input id="companyId" class=" form-control input-sm" name="company.id" value="${user.company.id}" type="hidden">
								<div class="input-group">
									<input id="companyName" class=" form-control input-sm" name="company.name" readonly="readonly" value="${user.company.name}" data-msg-required="" style="" type="text"> 
									<span class="input-group-btn">
										<button id="companyButton" class="btn btn-sm btn-primary " type="button">
											<i class="fa fa-search"></i>
										</button>
									</span>
								</div>
								<label id="companyName-error" class="error" for="companyName" style="display: none"></label>
							</div>
							<p></p>
							<div class="form-group" >
								<span>选择店铺：</span>
								<input id="officeId" class=" form-control input-sm" name="office.id" value="${user.office.id}" type="hidden">
									<div class="input-group">
										<input id="officeName" class=" form-control input-sm" name="office.name" readonly="readonly" value="${user.office.name}" data-msg-required="" style="" type="text">
											<span class="input-group-btn">
												<button id="shopButton" class="btn btn-sm btn-primary " type="button">
													<i class="fa fa-search"></i>
												</button>
											</span>
									</div>
									<label id="shopName-error" class="error" for="shopName" style="display:none"></label>
								
								<span>技能标签：</span>
								<sys:treeselect id="skill" name="skill.id" value="${user.skill.id}" labelName="skill.name" labelValue="${user.skill.name}" title="技能标签" url="/sys/skill/newTreeData" cssClass=" form-control input-sm" allowClear="true" notAllowSelectRoot="false" notAllowSelectParent="false" /> 
							</div>
						</form:form>
						<br />
					</div>
				</div>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-right">
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="newSearch()">
								<i class="fa fa-search"></i> 查询
							</button>
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()">
								<i class="fa fa-refresh"></i> 重置
							</button>
						</div>
					</div>
				</div>
				<!-- 主要内容展示 -->
			<form id="inputForm" action="${ctx}/sys/specBeautician/save" method="post">
				<input id="ids" value="" name="ids" type="hidden">
			</form>
				<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th><input type="checkbox" class="i-checks"></th>
							<th style="text-align: center;">姓名</th>
							<th style="text-align: center;">所在店铺</th>
							<th style="text-align: center;">手机号码</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="user">
						<tr>
							<td><input type="checkbox" id="${user.id}" class="i-checks"></td>
							<td style="text-align: center;">${user.name}</td>
							<td style="text-align: center;">${user.office.name}</td>
							<td style="text-align: center;">${user.mobile}</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
				<!-- 分页table -->
				<table:page page="${page}"></table:page>
			
			</div>
		</div>
	</div>
</body>
</html>