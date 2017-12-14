<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 

<script>
function setupKNN(){
	//alert($('input[name="k"]').val());
	$.get(
		'positioningSetup/setupKNN',
		{
			k : $('input[name="k"]').val()
		},
		function(data){
			alert(data);
		}
	);
}
</script>

<br/>
<div class="col-lg-12">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">k-NN</h3>
		</div>
		<div class="panel-body">
			<form>
				<input name="k" type="number" value="3"/><br/>
				<input type="button" value="Set" onclick="setupKNN();">
			</form>
		</div>
	</div>
</div>