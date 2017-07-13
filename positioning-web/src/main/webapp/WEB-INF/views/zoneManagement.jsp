<%@page import="java.util.List"%>
<%@page import="uni.miskolc.ips.ilona.measurement.model.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" %>
<script type="text/javascript">
<!--

//-->
function addZone(){
	//alert($('input[name="name"]').val());
	//alert($('#parentZone').val());
	/**/
	$.get(
			"addZone", 
			{
				name : $('input[name="name"]').val(),
				parentZoneId : $('#parentZone').val()
			},
			function(data,status){
				//alert(data);//$("#content").html(data);
				relaodZoneManager();
			});
	/**/
}

$('.removeZoneLink').each(function(){
	$(this).click(function(e){
		e.preventDefault();
		$.get(
				'deleteZone',
				{
				id : $(this).attr('href')
				},
				function(data){
					relaodZoneManager();
				}
				);
	});
	
});

function relaodZoneManager(){
	$.get(
			'zoneManagement',
			function(data){
				$('#page-wrapper').html(data);
			}
			);
}
</script>
<div class="col-lg-12">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">Create Zone</h3>
		</div>
		<div class="panel-body">
			<form id="addZoneForm" role="form">
				<input type="text" class="form-control" name="name" value="Zone"></input><br />

				<button type="button" class="btn" onclick="addZone();">Add Zone</button>
			</form>
		</div>
	</div>
</div>

<div class="col-lg-12">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">Zones</h3>
		</div>
		<div class="panel-body">
			<table class="table">
				<thead>
					<th>Zone</th><th>Id</th><th/>
				</thead>
				<tbody>
					<c:forEach var="zone" items="${zones }">
						<tr>
							<td>${zone.getName() }</td>
							<td>${zone.getId() }</td>
							<td><a href="${zone.getId() }" class="removeZoneLink"><i class="fa fa-times"> Delete</i></a>
						</tr>
					</c:forEach>
					
				</tbody>
			</table>
		</div>
	</div>
</div>
