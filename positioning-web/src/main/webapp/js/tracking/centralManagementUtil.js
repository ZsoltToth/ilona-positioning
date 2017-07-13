var oneYear = 31536000000; // day 365
var oneMonth = 2678400000; // day 31
var oneDay = 86400000;
var oneHour = 3600000;
var oneMinute = 60000;

function calculateDayHourMinute(time) {
	try {
		var temp = time;

		data = {
			days : 0,
			hours : 0,
			minutes : 0
		}

		while (temp - oneDay >= 0) {
			temp -= oneDay;
			data.days++;
		}

		while (temp - oneHour >= 0) {
			temp -= oneHour;
			data.hours++;
		}

		while (temp - oneMinute >= 0) {
			temp -= oneMinute;
			data.minutes++;
		}

		return data;
	} catch (error) {
		throw "Function :: calculateDayHourMinute Error: " + error;
	}
}

function calculateYearMonthDayHour(time) {
	try {
		var expirationTemp = time;

		data = {
			years : 0,
			months : 0,
			days : 0,
			hours : 0
		}
		/*
		 * data.years = expirationTemp / oneYear; expirationTemp =
		 * expirationTemp % oneYear;
		 * 
		 * data.months = expirationTemp / oneMonth; expirationTemp =
		 * expirationTemp % oneMonth;
		 * 
		 * data.days = expirationTemp / oneYear; expirationTemp = expirationTemp %
		 * oneDay; // seconds + milliseconds division data.hours =
		 * expirationTemp / 3600000;
		 */

		while (expirationTemp - oneYear >= 0) {
			expirationTemp -= oneYear;
			data.years++;
		}

		while (expirationTemp - oneMonth >= 0) {
			expirationTemp -= oneMonth;
			data.months++;
		}

		while (expirationTemp - oneDay >= 0) {
			expirationTemp -= oneDay;
			data.days++;
		}

		while (expirationTemp - oneHour >= 0) {
			expirationTemp -= oneHour;
			data.hours++;
		}
		return data;
	} catch (error) {
		throw "Function :: calculateYearMonthDayHour Error: " + error;
	}
}

$(".adminCentManAccountChangeValueInMillisClass").click(function(event) {
	try {

		var elementName = $(this).attr("data-element");
		var elementValue = states[elementName];

		var value = $(this).attr("data-expirationtime");

		var minValue = states[elementName + "Min"];
		var maxValue = states[elementName + "Max"];

		var dependency = {
			calculate : calculateYearMonthDayHour,
			format : generateText
		}

		if ($(this).attr("data-execute") == "DayHourMinute") {
			dependency.calculate = calculateDayHourMinute;
			dependency.format = generateDaysHoursMinutesText;
		}
		
		if ($(this).attr("data-execute") == "value") {
			dependency.calculate = function(data) {
				return data;
			};
			dependency.format = function(data) {
				return data;
			};
		}

		function checkAccountExpiration(min, max) {
			if (elementValue > max) {
				elementValue = max;
			}
			if (elementValue < min) {
				elementValue = min;
			}
		}

		switch (value) {
		case "yearplus":
			elementValue += oneYear;
			checkAccountExpiration(minValue, maxValue);
			break;
		case "yearminus":
			elementValue -= oneYear;
			checkAccountExpiration(minValue, maxValue);
			break;
		case "monthplus":
			elementValue += oneMonth;
			checkAccountExpiration(minValue, maxValue);
			break;
		case "monthminus":
			elementValue -= oneMonth;
			checkAccountExpiration(minValue, maxValue);
			break;
		case "dayplus":
			elementValue += oneDay;
			checkAccountExpiration(minValue, maxValue);
			break;
		case "dayminus":
			elementValue -= oneDay;
			checkAccountExpiration(minValue, maxValue);
			break;
		case "hourplus":
			elementValue += oneHour;
			checkAccountExpiration(minValue, maxValue);
			break;
		case "hourminus":
			elementValue -= oneHour;
			checkAccountExpiration(minValue, maxValue);
			break;
		case "minuteplus":
			elementValue += oneMinute;
			checkAccountExpiration(minValue, maxValue);
			break;
		case "minuteminus":
			elementValue -= oneMinute;
			checkAccountExpiration(minValue, maxValue);
			break;
		case "valueplus":
			elementValue += 1;
			checkAccountExpiration(minValue, maxValue);
			break;
		case "valueminus":
			elementValue -= 1;
			checkAccountExpiration(minValue, maxValue);
			break;
		default:
			break;
		}
		states[elementName] = elementValue;
		var output = dependency.format(dependency.calculate(elementValue));
		var out = $(this).attr("data-result");
		$("#" + out).html(output);
	} catch (error) {
		console.log(error);
	}
});

$(".onOffButton").click(function(event) {
	try {
		var value = $(this).attr("value");
		if (value == "ON") {
			$(this).removeClass("btn-success");
			$(this).addClass("btn-danger");
			$(this).attr("value", "OFF");
		} else {
			$(this).removeClass("btn-danger");
			$(this).addClass("btn-success");
			$(this).attr("value", "ON");
		}
	} catch (error) {
		console.log(error);
	}
});

$(".centManUpdateState").click(function(event) {
	try {
		event.preventDefault();
		var value = $("#" + $(this).attr("data-value")).attr("value");
		var lock = $(this).attr("data-lock");
		updateDetails($(this).attr("data-url"), {
			data : value
		}, $(this).attr("data-result"), states["" + lock]);
	} catch (error) {
		console.log(error);
	}
});

function updateDetails(url, data, resultDiv, lock) {
	try {
		if (lock.value == true) {
			lock.value = false;
		} else {
			return;
		}
		$.ajax({
			type : "POST",
			async : true,
			url : url,
			data : data,
			timeout : 10000,
			beforeSend : function(xhr) {
				xhr.setRequestHeader($("meta[name='_csrf_header']").attr(
						"content"), $("meta[name='_csrf']").attr("content"));
			},
			success : function(result, status, xhr) {
				try {
					lock.value = true;
					if (result.responseState == 100) {
						$("#" + resultDiv).html(
								"<p class='bg-primary'>Update success!</p>");
					} else {
						$("#" + resultDiv).html(
								"<p class='bg-primary'>Update failed!!</p>");
					}
				} catch (error) {
					console.log(error);
				}
			},
			error : function(xhr, status, error) {
				try {
					lock.value = true;
					$("#" + resultDiv).html(
							"<p class='bg-primary'>Update failed!!</p>");
					console.log("" + status + " " + error);
				} catch (err) {
					console.log(err);
				}
			}
		});
	} catch (error) {
		try {
			lock.value = true;
			$("#" + resultDiv)
					.html("<p class='bg-primary'>Update failed!!</p>");
		} catch (err) {
			console.log(err);
		}
	}
}

function generateDaysHoursMinutesText(data) {
	try {
		return "<h4>Days: " + data.days + " / Hours: " + data.hours
				+ " / Minutes: " + data.minutes + "</h4>";
	} catch (error) {

	}
}
function generateText(data) {
	try {
		return "<h4>Year: " + data.years + " / Months: " + data.months
				+ " / Days: " + data.days + " / Hours: " + data.hours + "</h4>";
	} catch (error) {
		console.log(error);
	}
}

$(".centManUpdateLongValue").click(function(event){
	try {
		var elementName = $(this).attr("data-element");
		var lock = states[elementName + "Lock"];
		if(lock.value == true) {
			lock.value = false;
		} else {
			return;
		}			
		
		var elementValue = states[elementName];			
		var url = $(this).attr("data-url");
		var resultDiv = $(this).attr("data-result");
		
		$.ajax({
			type: "POST",
			async: true,
			url: url,
			timeout: 10000,
			data: {data: elementValue},
			beforeSend : function(xhr) {
				xhr.setRequestHeader($("meta[name='_csrf_header']").attr(
						"content"), $("meta[name='_csrf']").attr("content"));
			},
			success : function(result, status, xhr) {
				try {
					lock.value = true;
					if (result.responseState == 100) {
						$("#" + resultDiv).html(
								"<p class='bg-primary'>Update success!</p>");
					} 				
					if(result.responseState == 300) {
						$("#" + resultDiv).html(
							"<p class='bg-primary'>Invalid parameter!</p>");
					} else {
						$("#" + resultDiv).html(
								"<p class='bg-primary'>Update failed!!</p>");
					}
				} catch (error) {
					console.log(error);
				}
			},
			error : function(xhr, status, error) {
				try {
					lock.value = true;
					$("#" + resultDiv).html(
							"<p class='bg-primary'>Update failed!!</p>");
					console.log("" + status + " " + error);
				} catch (err) {
					console.log(err);
				}
			}
		});			
	} catch(error) {
		lock.value = true;
		console.log(error);
	}
});
