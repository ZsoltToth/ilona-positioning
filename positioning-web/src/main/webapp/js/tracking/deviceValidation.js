/*
 * Validate result: <br> 100: input error <br> 200: minlength <br> 300:
 * maxlength <br> 400: format <br> 500: Values dont match
 */

/**
 * 
 * @param input
 * @return
 */
function validateInputIntegrityCheck(input) {
	try {
		if (typeof input == 'undefined' || input == null) {
			return true;
		} else {
			return false;
		}
	} catch (error) {
		throw "Function :: validateInputIntegrityCheck Cause: value: " + input
				+ " Error: " + error;
	}
}

/**
 * 
 * @param deviceid
 * @return
 */
function validateDeviceid(deviceid) {
	try {
		var regex = /^(?=.{5,50}$)^([a-zA-Z0-9]{1}[_:-]{0,1}){1,49}[a-zA-Z0-9]{1}$/;
		var result = {
			valid : true,
			errors : []
		};

		if (validateInputIntegrityCheck(deviceid)) {
			result.valid = false;
			result.errors.push(100);
			return result;
		}

		if (deviceid.length < 5) {
			result.valid = false;
			result.errors.push(200);

		}

		if (deviceid.length > 50) {
			result.valid = false;
			result.errors.push(300);

		}

		if (!regex.test(deviceid)) {
			result.valid = false;
			result.errors.push(400);
			return result;
		}
		return result;
	} catch (error) {
		throw "Function :: validateDeviceid Error: " + error;
	}
}

/**
 * 
 * @param deviceName
 * @returns
 */
function validateDeviceName(deviceName) {
	try {
		var regex = /^(?=.{5,50}$)^([a-zA-Z0-9]{1}[_:-]{0,1}){1,49}[a-zA-Z0-9]{1}$/;
		var result = {
			valid : true,
			errors : []
		};

		if (validateInputIntegrityCheck(deviceName)) {
			result.valid = false;
			result.errors.push(100);
			return result;
		}

		if (deviceName.length < 5) {
			result.valid = false;
			result.errors.push(200);

		}

		if (deviceName.length > 50) {
			result.valid = false;
			result.errors.push(300);

		}

		if (!regex.test(deviceName)) {
			result.valid = false;
			result.errors.push(400);
			return result;
		}
		return result;
	} catch (error) {
		throw "Function :: validateDeviceName Error: " + error;
	}
}

/**
 * 
 * @param deviceType
 * @returns
 */
function validateDeviceType(deviceType) {
	try {
		var regex = /^[a-zA-Z]{1,20}$/;
		var result = {
			valid : true,
			errors : []
		};

		if (validateInputIntegrityCheck(deviceType)) {
			result.valid = false;
			result.errors.push(100);
			return result;
		}

		if (deviceType.length < 1) {
			result.valid = false;
			result.errors.push(200);

		}

		if (deviceType.length > 20) {
			result.valid = false;
			result.errors.push(300);

		}

		if (!regex.test(deviceType)) {
			result.valid = false;
			result.errors.push(400);
			return result;
		}
		return result;
	} catch (error) {
		throw "Function :: validateDeviceType Error: " + error;
	}
}

/**
 * 
 * @param deviceTypeName
 * @returns
 */
function validateDeviceTypeName(deviceTypeName) {
	try {
		var regex = /^(?=.{1,30}$)^([a-zA-Z0-9]{1}[_ -]{0,1}){1,29}[a-zA-Z0-9]{1}$/;
		var result = {
			valid : true,
			errors : []
		};

		if (validateInputIntegrityCheck(deviceTypeName)) {
			result.valid = false;
			result.errors.push(100);
			return result;
		}

		if (deviceTypeName.length < 1) {
			result.valid = false;
			result.errors.push(200);

		}

		if (deviceTypeName.length > 30) {
			result.valid = false;
			result.errors.push(300);

		}

		if (!regex.test(deviceTypeName)) {
			result.valid = false;
			result.errors.push(400);
			return result;
		}
		return result;
	} catch (error) {
		throw "Function :: validateDeviceTypeName Error: " + error;
	}
}

/**
 * 
 * @param errors
 * @returns
 */
function getDeviceidErrorMessages(errors) {
	try {
		var length = errors.length;
		var i = 0;
		var errorText = [];
		for (i; i < length; i++) {
			switch (errors[i]) {
			case 100: {
				errorText.push("Invalid deviceid!");
				break;
			}
			case 200: {
				errorText
						.push("The deviceid length is less than 5 characters!");
				break;
			}
			case 300: {
				errorText.push("The deviceid length is  more 50 characters!");
				break;
			}
			case 400: {
				errorText.push("The deviceid format is not valid!");
				break;
			}
			default: {

			}
			}
		}
		return errorText;
	} catch (error) {
		throw "Function :: getDeviceidErrorMessages  Cause: " + error;
	}
}

/**
 * 
 * @param errors
 * @returns
 */
function getDeviceNameErrorMessages(errors) {
	try {
		var length = errors.length;
		var i = 0;
		var errorText = [];
		for (i; i < length; i++) {
			switch (errors[i]) {
			case 100: {
				errorText.push("Invalid userid!");
				break;
			}
			case 200: {
				errorText
						.push("The device name length is less than 5 characters!");
				break;
			}
			case 300: {
				errorText
						.push("The device name length is  more 50 characters!");
				break;
			}
			case 400: {
				errorText.push("The device name format is not valid!");
				break;
			}
			default: {

			}
			}
		}
		return errorText;
	} catch (error) {
		throw "Function :: getDeviceNameErrorMessages  Cause: " + error;
	}
}

/**
 * 
 * @param errors
 * @returns
 */
function getDeviceTypeErrorMessages(errors) {
	try {
		var length = errors.length;
		var i = 0;
		var errorText = [];
		for (i; i < length; i++) {
			switch (errors[i]) {
			case 100: {
				errorText.push("Invalid userid!");
				break;
			}
			case 200: {
				errorText
						.push("The device type length is less than 1 character!");
				break;
			}
			case 300: {
				errorText
						.push("The device type length is  more 20 characters!");
				break;
			}
			case 400: {
				errorText.push("The device type format is not valid!");
				break;
			}
			default: {

			}
			}
		}
		return errorText;
	} catch (error) {
		throw "Function :: getDeviceTypeErrorMessages  Cause: " + error;
	}
}

/**
 * 
 * @param errors
 * @returns
 */
function getDeviceTypeNameErrorMessages(errors) {
	try {
		var length = errors.length;
		var i = 0;
		var errorText = [];
		for (i; i < length; i++) {
			switch (errors[i]) {
			case 100: {
				errorText.push("Invalid userid!");
				break;
			}
			case 200: {
				errorText
						.push("The device type name length is less than 1 character!");
				break;
			}
			case 300: {
				errorText
						.push("The device type name length is  more 20 characters!");
				break;
			}
			case 400: {
				errorText.push("The device type name format is not valid!");
				break;
			}
			default: {

			}
			}
		}
		return errorText;
	} catch (error) {
		throw "Function :: getDeviceTypeNameErrorMessages  Cause: " + error;
	}
}

/**
 * 
 * @param messages
 * @returns
 */
function deviceOutputParagraphFormatter(messages) {
	try {
		var newMessages = [];
		var length = messages.length;
		var i = 0;
		for (i; i < length; i++) {
			newMessages.push("<p class='bg-primary'>" + messages[i] + "</p>");
		}
		return newMessages;
	} catch (error) {
		throw "Function :: deviceOutputParagraphFormatter Cause: " + error;
	}
}

function checkDevicesInputsValidity(inputs) {
	try {
		errorText = [];
		var hadError = false;

		function checkInputIntegrity(input) {
			try {
				if (typeof input != 'undefined' && input != null
						&& typeof input === 'string') {
					return true;
				} else {
					return false;
				}
			} catch (error) {
				throw "Function :: checkInputIntegrity Cause: value: " + input
						+ " Error: " + error;
			}
		}
		/*
		 * Dependency injection ?!
		 */
		var dep = {
			deviceidMessage : getDeviceidErrorMessages,
			deviceNameMessage : getDeviceNameErrorMessages,
			deviceTypeMessage : getDeviceTypeErrorMessages,
			deviceTypeNameMessage : getDeviceTypeNameErrorMessages,
			formatter : deviceOutputParagraphFormatter
		}

		var inputValue = inputs["deviceid"];
		if (checkInputIntegrity(inputValue)) {
			try {
				result = validateDeviceid(inputValue);
				if (!result.valid) {
					hadError = true;
					errorText = errorText.concat(dep.formatter(dep
							.deviceidMessage(result.errors)));
				}
			} catch (err) {
				throw "Deviceid check InnerException: " + err;
			}
		}

		inputValue = inputs["deviceName"];
		if (checkInputIntegrity(inputValue)) {
			try {
				result = validateDeviceName(inputValue);
				if (!result.valid) {
					hadError = true;
					errorText = errorText.concat(dep.formatter(dep
							.deviceNameMessage(result.errors)));
				}
			} catch (err) {
				throw "Device name check InnerException: " + err;
			}
		}

		inputValue = inputs["deviceType"];
		if (checkInputIntegrity(inputValue)) {
			try {
				result = validateDeviceType(inputValue);
				if (!result.valid) {
					hadError = true;
					errorText = errorText.concat(dep.formatter(dep
							.deviceTypeMessage(result.errors)));
				}
			} catch (err) {
				throw "Device Type check InnerException: " + err;
			}
		}

		inputValue = inputs["deviceTypeName"];
		if (checkInputIntegrity(inputValue)) {
			try {
				result = validateDeviceTypeName(inputValue);
				if (!result.valid) {
					hadError = true;
					errorText = errorText.concat(dep.formatter(dep
							.deviceTypeNameMessage(result.errors)));
				}
			} catch (err) {
				throw "Device Type check InnerException: " + err;
			}
		}

		return {
			valid : !hadError,
			errors : errorText
		};

	} catch (error) {
		throw "Function :: checkDevicesInputsValidity Cause: " + error;

	}
}