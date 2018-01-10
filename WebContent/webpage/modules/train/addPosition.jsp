<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
	<head>
		<title>添加职位</title>
		<meta name="decorator" content="default" />
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
		<script type="text/javascript">
			function rightOneButton(value) {
				if (value != "" && value != null) {
					var appText = $("#"+value).text();
					var appHtml = '<option id="'+value+'" value="'+value+'" >'+appText+'</option>';
					$("#exiPosition").append(appHtml);
					$("#notPosition #"+value).remove();
				}
			}
			function rightButtion() {
				$("#exiPosition").append($("#notPosition").html());
				$("#notPosition option").remove();
			}
			function leftOneButton(value) {
				if (value != "" && value != null) {
					var appText = $("#"+value).text();
					var appHtml = '<option id="'+value+'" value="'+value+'" >'+appText+'</option>';
					$("#notPosition").append(appHtml);
					$("#exiPosition #"+value).remove();
				}
			}
			function leftButton() {
				$("#notPosition").append($("#exiPosition").html());
				$("#exiPosition option").remove();
			}
			/* 查询 */
			function searchPosition() {
				var searchValue = $("#search_position").val();
				var exiPositionValues = $("#exiPosition option").map(function(){return $(this).val();}).get().join(",");//获得所有的value
				$.post("${ctx}/train/position/searchPosition",{searchValue:searchValue,exiPositionValues:exiPositionValues},function(data){
					$("#notPosition").empty();
					for (var i = 0; i < data.length; i++) {
						var val = data[i].value;
						var text = data[i].label;
						var appHtml = '<option id="'+val+'" value="'+val+'" >'+text+'</option>';
						$("#notPosition").append(appHtml);
					}
					
				},"json")
			}
		</script>
	</head>
	<body>
		<div class="wrapper wrapper-content">
			<div class="form-group">
				<input id="search_position" type="text" placeholder="职位名称"/>
				<button onclick="searchPosition()"> 查询</button>
			</div>
			<div style="float: left;">
				<form action="">
					<select id="notPosition" style="width: 200px;height: 180px;"size="7" ondblclick="rightOneButton(this.value)">
						<c:forEach items="${notPosition }" var="position">
							<option id="${position.value }" value="${position.value }" >${position.label }</option>
						</c:forEach>
					</select>			
				</form> 
			</div>
			<div style="float: left;margin-left: 87px;margin-top: 35px;">
				<p><a onclick="rightOneButton($('#notPosition').val())">>|</a></p>
				<p><a onclick="rightButtion()">>>|</a></p>
				<br/>
				<p><a onclick="leftOneButton($('#exiPosition').val())">|<</a></p>
				<a onclick="leftButton()">|<<</a>
			</div>
			<div style="float: left;margin-left: 80px;">
				<form action="">
					<select id="exiPosition" style="width: 200px;height: 180px;" size="7" ondblclick="leftOneButton(this.value)">
						<c:forEach items="${exiPosition }" var="exposition">
							<option id="${exposition.value }" value="${exposition.value }">${exposition.label }</option>
						</c:forEach>
					</select>			
				</form>
			</div>
		</div>
	</body>
</html>