/*
 * Validate result: <br> 100: input error <br> 200: minlength <br> 300:
 * maxlength <br> 400: format <br> 500: Values dont match
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
 * @param userid
 * @return
 *            <ul>
 *            <li> Result object:
 *            <ul>
 *            <li>valid: true if valid,<br>
 *            false if not</li>
 *            <li>errors: If not valid this property stores the erros codes.</li>
 *            </ul>
 *            </li>
 *            <li>Exception if an error occured!</li>
 *            </ul>
 */
function validateUserid(userid) {
	try {
		// outer variable?!
		var regex = /^(?=.{5,20}$)^[a-zA-Z]{1}[a-zA-Z0-9]{4,19}$/;
		var result = {
			valid : true,
			errors : []
		};
		/*
		 * === null?!
		 */
		if (validateInputIntegrityCheck(userid)) {
			result.valid = false;
			result.errors.push(100);
			return result;
		}

		/*
		 * 
		 */
		if (userid.length < 5) {
			result.valid = false;
			result.errors.push(200);

		}

		if (userid.length > 20) {
			result.valid = false;
			result.errors.push(300);

		}

		if (!regex.test(userid)) {
			result.valid = false;
			result.errors.push(400);
			return result;
		}
		return result;
	} catch (error) {
		throw "Function :: validateUserid Cause: " + error;
	}
}

function validateUsername(username) {
	try {
		var regex = /^(?=.{5,30}$)^[a-zA-ZöüóőúéáűÖÜÓŐÚŰÁÉ][a-zA-ZöüóőúéáűÖÜÓŐÚŰÁÉ0-9]{4,30}$/;
		var result = {
			valid : true,
			errors : []
		};
		/*
		 * 
		 */
		if (validateInputIntegrityCheck(username)) {
			result.valid = false;
			result.errors.push(100);
			return result;
		}

		/*
		 * 
		 */
		if (username.length < 5) {
			result.valid = false;
			result.errors.push(200);

		}

		if (username.length > 30) {
			result.valid = false;
			result.errors.push(300);

		}

		if (!regex.test(username)) {
			result.valid = false;
			result.errors.push(400);
			return result;
		}
		return result;
	} catch (error) {
		throw "Function :: validateUsername Cause: " + error;
	}
}

function validatePassword(password1, password2) {
	try {
		var regex = /^(?=.{5,30}$).{5,30}$/;
		var result = {
			valid : true,
			errors : []
		};
		/*
		 * 
		 */
		if (validateInputIntegrityCheck(password1)
				|| validateInputIntegrityCheck(password2)) {
			result.valid = false;
			result.errors.push(100);
			return result;
		}

		/*
		 * 
		 */
		if (password1.length < 5) {
			result.valid = false;
			result.errors.push(200);

		}

		if (password1.length > 30) {
			result.valid = false;
			result.errors.push(300);

		}

		if (!regex.test(password1)) {
			result.valid = false;
			result.errors.push(400);

		}

		if (password1 != password2) {
			result.valid = false;
			result.errors.push(500);
			return result;
		}
		return result;
	} catch (error) {
		throw "Function :: validatePassword Cause: " + error;
	}
}

function validateEmail(email) {
	try {
		var regex = /^[\w!#$%&'*+/=?`{|}~^-]+(?:\.[\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,6}$/;
		var result = {
			valid : true,
			errors : []
		};
		/*
		 * 
		 */
		if (validateInputIntegrityCheck(email)) {
			result.valid = false;
			result.errors.push(100);
			return result;
		}

		if (!regex.test(email)) {
			result.valid = false;
			result.errors.push(400);
			return result;
		}
		return result;
	} catch (error) {
		throw "Function :: validateEmail Cause: " + error;
	}
}

/**
 * 
 * @param errors
 * @returns
 */
function getUseridErrorMessages(errors) {
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
				errorText.push("The userid length is less than 5 characters!");
				break;
			}
			case 300: {
				errorText.push("The userid length is  more 20 characters!");
				break;
			}
			case 400: {
				errorText.push("The userid format is not valid!");
				break;
			}
			default: {

			}
			}
		}
		return errorText;
	} catch (error) {
		throw "Function :: getLocalUseridErrorMessages  Cause: " + error;
	}
}

function getUsernameErrorMessages(errors) {
	try {
		var length = errors.length;
		var i = 0;
		var errorText = [];
		for (i; i < length; i++) {
			switch (errors[i]) {
			case 100: {
				errorText.push("Invalid username!");
				break;
			}
			case 200: {
				errorText
						.push("The username length is less than 5 characters!");
				break;
			}
			case 300: {
				errorText.push("The username length is  more 30 characters!");
				break;
			}
			case 400: {
				errorText.push("The username format is not valid!");
				break;
			}
			default: {

			}
			}
		}
		return errorText;
	} catch (error) {
		throw "Function :: getUsernameErrorMessages  Cause: " + error;
	}
}

function getEmailErrorMessages(errors) {
	try {
		var length = errors.length;
		var i = 0;
		var errorText = [];
		for (i; i < length; i++) {
			switch (errors[i]) {
			case 100: {
				errorText.push("Invalid email!");
				break;
			}
			case 400: {
				errorText.push("The email format is not valid!");
				break;
			}
			default: {

			}
			}
		}
		return errorText;
	} catch (error) {
		throw "Function :: getUsernameErrorMessages  Cause: " + error;
	}
}

function getPasswordErrorMessages(errors) {
	try {
		var length = errors.length;
		var i = 0;
		var errorText = [];
		for (i; i < length; i++) {
			switch (errors[i]) {
			case 100: {
				errorText.push("Invalid password!");
				break;
			}
			case 200: {
				errorText
						.push("The password length is less than 5 characters!");
				break;
			}
			case 300: {
				errorText.push("The password length is  more 30 characters!");
				break;
			}
			case 400: {
				errorText.push("The password format is not valid!");
				break;
			}
			case 500: {
				errorText.push("The two password fields must be equal!");
				break;
			}
			default: {

			}
			}
		}
		return errorText;
	} catch (error) {
		throw "Function :: getUsernameErrorMessages  Cause: " + error;
	}
}

function outputParagraphFormatter(messages) {
	try {
		var newMessages = [];
		var length = messages.length;
		var i = 0;
		for (i; i < length; i++) {
			newMessages.push("<p class='bg-primary'>" + messages[i] + "</p>");
		}
		return newMessages;
	} catch (error) {
		throw "Function :: outputParagraphFormatter Cause: " + error;
	}
}

function checkInputsValidity(inputs) {
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
			useridMessage : getUseridErrorMessages,
			usernameMessage : getUsernameErrorMessages,
			passwordMessage : getPasswordErrorMessages,
			emailMessage : getEmailErrorMessages,
			formatter : outputParagraphFormatter
		}

		var inputValue = inputs["userid"];
		if (checkInputIntegrity(inputValue)) {
			try {
				result = validateUserid(inputValue);
				if (!result.valid) {
					hadError = true;
					errorText = errorText.concat(dep.formatter(dep
							.useridMessage(result.errors)));
				}
			} catch (err) {
				throw "" + err;
			}
		}

		inputValue = inputs["username"];
		if (checkInputIntegrity(inputValue)) {
			try {
				result = validateUsername(inputValue);
				if (!result.valid) {
					hadError = true;
					errorText = errorText.concat(dep.formatter(dep
							.usernameMessage(result.errors)));
				}
			} catch (err) {
				throw "" + err;
			}
		}

		inputValue = inputs["email"];
		if (checkInputIntegrity(inputValue)) {
			try {
				result = validateEmail(inputValue);
				if (!result.valid) {
					hadError = true;
					errorText = errorText.concat(dep.formatter(dep
							.emailMessage(result.errors)));
				}
			} catch (err) {
				throw "" + err;
			}
		}

		inputValue = inputs["password"];
		if (typeof inputValue != 'undefined' && typeof inputValue != null
				&& inputValue instanceof Array) {
			if (inputValue.length == 2) {
				var password1 = inputValue[0];
				var password2 = inputValue[1];
				if (checkInputIntegrity(password1)
						&& checkInputIntegrity(password2)) {
					try {
						result = validatePassword(password1, password2);
						if (!result.valid) {
							hadError = true;
							errorText = errorText.concat(dep.formatter(dep
									.passwordMessage(result.errors)));
						}
					} catch (err) {
						throw "" + err;
					}
				}
			} else {
				throw "Invalid array length: " + inputValue.length;
			}
		}

		return {
			valid : !hadError,
			errors : errorText
		};

	} catch (error) {
		throw "Function :: checkInputsValidity Cause: " + error;

	}
}