<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" %>
<script>
$('.deleteMeasurement').each(function(){
	$(this).click(function(e){
		e.preventDefault();
		//alert($(this).attr('href'));
		$.get(
			'deleteMeasurement',
			{
				timestamp : $(this).attr('href')
			},
			function(data){
				$.get(
					'measurementManagement',
					function(d){
						$('#page-wrapper').html(d);
					}
				);
			}
		);
	});
});
</script>
<br/>
<div class="col-lg-12">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">Measurements</h3>
		</div>
		<div class="panel-body">
		<table class="table">
			<thead>
				<th>Zone</th>
				<th>Timestamp</th>
				<c:forEach var="ap" items="${accessPoints }">
					<th>${ap }</th>
				</c:forEach>
				<th/>
			</thead>
			<tbody>
				<c:forEach var="measurement" items="${measurements }">
					<tr>
						<td>${measurement.getLocation().getName() }</td>
						<td>${measurement.getTimestamp() }</td>
						<c:forEach var="accessPoint" items="${accessPoints }">
							<td>
								${measurement.getValues().get(accessPoint) }
							</td>
						</c:forEach>
						<td>
							<a href="${measurement.getTimestamp().getTime() }" class="deleteMeasurement"><i class="fa fa-times"> Delete</i></a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</div>
</div>