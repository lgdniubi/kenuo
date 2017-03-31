<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html>

	<head>
		<title>表单提交</title>
	</head>

	<body>
		<div class="col-sm-offset-2 col-sm-10">
			<form>
				<fieldset id="fieldset" disabled>
					<div class="form-group">
						<label class="radio-inline">
							<input type="radio" name="inlineRadioOptions" id="inlineRadio1" value="1">干性
						</label>
						<label class="radio-inline">
						    <input type="radio" name="inlineRadioOptions" id="inlineRadio2" value="2" checked>混合型
						</label>
						<label class="radio-inline">
							<input type="radio" name="inlineRadioOptions" id="inlineRadio3" value="3">油性
						</label>
					</div>

					<div class="form-group">
						<label class="checkbox-inline">
						<input type="checkbox"  name="inlineCheckboxOptions" id="inlineCheckbox1" value="干性" checked> 干性
					</label>
						<label class="checkbox-inline">
						 <input type="checkbox"  name="inlineCheckboxOptions" id="inlineCheckbox2" value="老化" checked> 老化
					</label>
						<label class="checkbox-inline">
						 <input type="checkbox"  name="inlineCheckboxOptions" id="inlineCheckbox3" value="sssss"> 油性
					</label>
					</div>
				</fieldset>
				<button type="button" class="btn btn-default" id="edit">编辑</button>
				<button type="button" class="btn btn-default" id="submit">提交</button>
			</form>
		</div>
		<select name="" id="selectId">
			<!--<option value="111" selected="true">111</option>-->
		</select>
		<form action="" method="get" id="checkboxs">
			皮肤类型？
			<!--<label><input name="Fruit" type="checkbox" value="" />苹果 </label>
			<label><input name="Fruit" type="checkbox" value="" />桃子 </label>
			<label><input name="Fruit" type="checkbox" value="" />香蕉 </label>
			<label><input name="Fruit" type="checkbox" value="" />梨 </label>-->
		</form>
		<script src="jquery/jquery-1.8.3.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript">
			$(function() {
				$('#edit').click(function() {
					if($('#fieldset').is(':disabled')) {
						$('#fieldset').removeAttr('disabled');
					}
				});
				$('#submit').click(function() {
					//					var str = '';
					//					var radioVal = $("input[name='inlineRadioOptions']:checked").val();
					//					str+=radioVal+ ",";
					//					var checkboxs = $("input[name='inlineCheckboxOptions']:checkbox");
					//					$("input[name='inlineCheckboxOptions']:checkbox").each(function() {
					//						if($(this).is(":checked")) {
					//							str += $(this).attr("value") + ",";
					//						}
					//					});
					//					if(str != null && str.length > 1) {
					//						str = str.substring(0, str.length - 1);
					//					}
					//					alert(str);
					var student = $("input[name='inlineCheckboxOptions']:checked").serialize();
					alert(student)
				});
			})
		</script>
		<script type="text/javascript">
			var arr = [1, 2, 3, 4];
			for(var i = 0; i < arr.length; i++) {
				$("#selectId").append("<option value='" + arr[i] + "'>" + arr[i] + "</option>");
				$("#checkboxs").append("<label><input type='checkbox' value='"+arr[i]+"' name='Fruit'/>"+arr[i]+"</label>");
			}
		</script>
	</body>

</html>