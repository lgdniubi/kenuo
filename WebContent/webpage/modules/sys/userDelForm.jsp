<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
   	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
    <title>删除用户详情</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>删除用户详情</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	 <tbody>
					      <tr>
					         <td class="active" width="70"><label class="pull-right">归属商家:</label></td>
					         <td><sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}"
									title="公司" url="/sys/franchisee/treeData" cssClass="form-control required" hideBtn="true"/></td>
					      </tr>
					      <tr>
					         <td class="active"><label class="pull-right">归属机构:</label></td>
					         <td><sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
								title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" notAllowSelectParent="false" notAllowSelectRoot="false" hideBtn="true"/></td>
					      </tr>
					      <tr>
					      	 <td class="active"><label class="pull-right">工号:</label></td>
					         <td><input type="text" class="form-control" readonly="readonly" value="${user.no}"></td>
					      </tr>
					      <tr>
					         <td class="active"><label class="pull-right">姓名:</label></td>
					         <td><input value="${user.name}" class="form-control" readonly="readonly"/></td>
					      </tr>
					      <tr>
					      	 <td class="active"><label class="pull-right">登录名:</label></td>
					         <td><input type="text" class="form-control" readonly="readonly" value="${user.loginName}"></td>
					      </tr>
					      <tr>
					         <td class="active"><label class="pull-right">身份证号:</label></td>
					         <td><input type="text" class="form-control" readonly="readonly" value="${user.idCard}"></td>
					      </tr>
					      <tr>
					      	 <td class="active"><label class="pull-right">入职日期:</label></td>
					         <td><input type="text" class="form-control" readonly="readonly" value="<fmt:formatDate value="${user.inductionTime}" pattern="yyyy-MM-dd"/>"></td>
					      </tr>
					       <tr>
					         <td class="active"><label class="pull-right">电话:</label></td>
					         <td><input type="text" class="form-control" readonly="readonly" value="${user.phone}"></td>
					      </tr>
					      
					      <tr>
					         <td class="active"><label class="pull-right">手机:</label></td>
					         <td><input type="text" class="form-control" readonly="readonly" value="${user.mobile}"></td>
					      </tr>
					      <tr>
					         <td class="active"><label class="pull-right">用户角色:</label></td>
					         <td>
				      			<form:form modelAttribute="user">
					         		<form:checkboxes path="roleIdList" items="${allRoles}" itemLabel="name" itemValue="id" htmlEscape="false" cssClass="i-checks required"/>
				     		 	</form:form>
					         </td>
					      </tr>
					       <tr>
					         <td class="active"><label class="pull-right">删除备注:</label></td>
					         <td><p>${user.delRemarks }</p></td>
					      </tr>
					  </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>