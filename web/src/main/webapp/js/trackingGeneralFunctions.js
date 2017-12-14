/**
 * DOC!
 * 
 * @param url
 * @param successDiv
 *            The new content will be loaded in this div. ModelAndView.
 * @return void <br>
 *         exception if the request failed
 */
function loadWebpage(url, successDiv) {

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", url, false);
	xhttp.setRequestHeader(header, token);
	xhttp.send();
	if (xhttp.readyState == 4 && xhttp.status == 200) {
		$("#" + successDiv).html(xhttp.responseText);
	} else {
		throw new ResultDetails(500, [ "Status:" + xhttp.status
				+ " Status text: " + xhttp.statusText ]);
	}

}

/**
 * 
 * @param url
 * @returns
 */
function loadDivIntoPagewrapper(url) {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", url, false);
	xhttp.setRequestHeader(header, token);
	xhttp.send();
	if (xhttp.readyState == 4 && xhttp.status == 200) {
		$("#page-wrapper").html(xhttp.responseText);
	} else {
		throw new ResultDetails(500, [ "Status:" + xhttp.status
				+ " Status text: " + xhttp.statusText ]);
	}
}

function loadWebpageAsync(url, successDiv, callbackFunction) {
	try {
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajax({
			type : "POST",
			async : true,
			url : url,
			timeout : 10000,
			beforeSend : function(xhr) {
				/*
				 * Set the csrf token into the header.
				 */
				xhr.setRequestHeader(header, token);
			},
			success : function(result, status, xhr) {
				$("#" + successDiv).html(result);
			},
			error : function(xhr, status, error) {
				try {
					callbackFunction();
				} catch (err) {
					console.log(err);
				}
			}
		});
	} catch (error) {
		console.log(error);
	}
}

/**
 * 
 * @param responseStatus
 * @param messages
 * @return
 */
function ResultDetails(responseStatus, messages) {
	this.responseStatus = responseStatus;
	this.messages = messages;
}

/**
 * 
 * @param url
 * @param data
 * @param successCallback
 * @param errorCallback
 * @param csrfToken
 * @return ExecutionResultDTO: <br>
 *         <ul>
 *         <li>responseStatus</li>
 *         </ul>
 */
function sendDataToServerAsync(url, data, successCallback, errorCallback,
		csrfToken) {
	try {
		$.ajax({
			type : "POST",
			async : true,
			url : url,
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			data : data,
			timeout : 10000,
			success : function(result, status, xhr) {
				successCallback(result, status, xhr);
			},
			error : function(xhr, status, error) {
				errorCallback(xhr, status, error)
				throw "Cause: Status: " + status + " Error: " + error;
			}
		});
	} catch (error) {
		console.log(error);
	}
}