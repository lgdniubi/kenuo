<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
	<head>
		<title>职位管理</title>
		<meta name="decorator" content="default" />
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
	</head>
	<body>
		<div class="wrapper wrapper-content">
			<div class="ibox-title">
				<form action="">
					<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
					   <tbody>
					      <tr>
							 <td class="width-15 active">
								<label class="pull-right">键值:</label>
							 </td>
							 <td class="width-35">
								<input name="value" type="text" value="${position.value}" class="form-control"/>
							 </td>
					         <td  class="width-15 active" class="active"><label class="pull-right">职位名称:</label></td>
					         <td class="width-35">
					         	<input name="label" type="text" value="${position.label}" class="form-control"/>
							 </td>
					      </tr>
					      <tr>
					         <td class="width-15 active"><label class="pull-right">类型:</label></td>
					         <td class="width-35"><input name="description" type="text" value="${position.description}" class="form-control"></td>
							 <td class="width-15 active"><label class="pull-right">所属部门:</label></td>
					         <td class="width-35"><input name="department.name" type="text" value="${position.department.name}" class="form-control"></td>
					      </tr>
					      <tr>
					      	 <td class="width-15 active"><label class="pull-right">排序:</label></td>
					         <td class="width-35"><input name="type" type="text" value="${position.sort}" class="form-control"></td>
					      	 <td class="width-15 active"><label class="pull-right">归属商家:</label></td>
					         <td class="width-35"><input name="department.office.name" type="text" value="${position.department.office.name}" class="form-control"></td>
					      </tr>
					      <tr>
							 <td class="width-15 active"><label class="pull-right">备注:</label></td>
					         <td class="width-85" colspan="3">
					         	<textarea rows="3" class="form-control">${position.remarks}</textarea>
					         </td>
					      </tr>   
						</tbody>
					</table>
				</form>
			</div>
		</div>
	</body>
</html>