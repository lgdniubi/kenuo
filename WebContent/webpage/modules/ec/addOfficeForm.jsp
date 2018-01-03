<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>添加店铺</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->
<script type="text/javascript">
	$(document).ready(function(){
		if($("#mappingId").val() != 0){
			$("#choose").hide();
		}
	}); 
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
					<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
						<tr id="choose">
							<td>
								<label class="pull-right" >选择店铺：</label>
							</td>
							<td>
								<sys:treeselect id="office" name="officeId" value="${officeRecommendMapping.officeId}" labelName="officeName" labelValue="${officeRecommendMapping.officeName}" title="店铺" url="/sys/office/treeData?type=2" cssClass=" form-control input-sm" allowClear="true" notAllowSelectRoot="false" notAllowSelectParent="true"/>
							</td>
						</tr>
						<tr>
							<td>
								<label class="pull-right" >排序：</label>
							</td>
							<td>
								<input class="form-control required" id="sort" name="sort" type="text" value="${officeRecommendMapping.sort }" style="width: 300px"/>
								<input id="mappingId" name="mappingId" type="hidden" value="${officeRecommendMapping.officeRecommendMappingId }"/>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
	
</body>
</html>