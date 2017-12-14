/**
 * 
 * @param inputs
 * @returns
 */
function resizeInputElements(inputs) {
	try {
		var length = inputs.length;
		var i = 0;
		var input;
		var minlength = 20;
		for(i; i < length; i++) {
			input = document.getElementById(inputs[i]);
			input.size = input.value.length > 20 ? input.value : 20;
		}	
	} catch (error) {
		throw "Function :: resizeInputElements Error: " + error;
	}
}

function sendDeviceDataToServer(url, device, callbackSuccess, callbackError) {
	try {
		$.ajax({
			type : "POST",
			async : true,
			url : url,
			beforeSend : function(xhr) {
				xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"), 
						$("meta[name='_csrf']").attr("content"));
			},
			data : {
				userid : device.userid,
				deviceid : device.deviceid,
				deviceType : device.deviceName,
				deviceTypeName : device.deviceTypeName
			},
			success : function(result, status, error) {
				try {
					callbackSuccess();
				} catch(error) {
					console.log(error);
				}
			},
			error : function(xhr, status, error) {
				try {
					callbackError();
				} catch(error) {
					console.log(error);
				}
			}
		});
		
	} catch(error) {
		console.log(error);
		throw new "Function :: sendDeviceDataToServer Error: " + error;
	}
}