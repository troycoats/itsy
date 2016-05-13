function openPopup(url, name, height, width) {
	waitWin = window.open(url, name, 'scrollbars=yes,resizable=yes,height=' + height + ',width=' + width);
	waitWin.focus(); 
}

function openPopupPDF(url, height, width) {
	openPopup(url, height, width);
	setWinNull();
}

function saveWithoutRefresh() { s('false'); }
function save()	{ s('true'); }
function s(refresh) {
	if (validate()) {
		if (document.detail != null) {
	        document.detail.closePopup.value = "false";
    	    document.detail.saveNew.value = "false";
        	document.detail.submit();
        }
 	}
}

function saveAndCloseWithoutRefresh() { sac('false'); }
function saveAndClose()	{ sac('true'); }
function sac(refresh) {
    if (validate()) {
    	showBusy();
    
    	if (document.detail != null) {
	        document.detail.closePopup.value = "true";
    	    document.detail.submit();
    	}
        
        if (refresh == 'true')
        	refreshParent();
	}
}

function saveAndNewWithoutRefresh() { san('false'); }
function saveAndNew() { san('true'); }
function san(refresh) {
    if (validate()) {
    	showBusy();
    
    	if (document.detail != null) {
		    document.detail.closePopup.value = "false";
			document.detail.saveNew.value = "true";
	    	document.detail.submit();
	    }
    }
}

function setCurrentPage(i){
    	//if (validateForm()){
	    	var searchForm = document.forms[0];
	    	searchForm.mode.value = "findByName";
	   		searchForm.currentPage.value = i;
	       	searchForm.submit();
	    	busyButton(i);
	    //}
    }

function refreshParent() {
    try {
		if (window.opener != null && !window.opener.closed) {
			window.opener.refreshPage();
		}
	} catch(ex) {
		// do nothing
	}
}

function refreshPage() {
	var dont_refresh = document.getElementById('readOnly');
	if (document.forms[0] != null) {
		if (dont_refresh == null) {
			document.forms[0].submit();
		} else {
			if (dont_refresh.value == '') {
				document.forms[0].submit();
			}
		}
	}
}

function resizeTextArea(id) {
	// for auto-resizing when typing: onkeyup="resizeTextArea(id);"
    var area = document.getElementById(id);
    if (area != null) {
	    var hasCarriageReturn = false;
		if (area.value.indexOf("\n") > -1) {
			hasCarriageReturn = true;
		}
		
	    if (area.value.length > area.cols || hasCarriageReturn) { 
	    	area.style.height = "1px";
	    	area.style.height = (27 + area.scrollHeight) + "px";
	    } else {
		    area.style.height = '34px';
		}
	}
}

function resizeTextAreaMax(id, max) {
	// for auto-resizing when typing: onkeyup="resizeTextAreaMax(id, max);"
    var textarea = document.getElementById(id);
    if (textarea != null) {
		var lines = textarea.value.split('\n');
		var width = textarea.cols;
		var height = 1;
		for (var i = 0; i < lines.length; i++) {
			var linelength = lines[i].length;
		    if (linelength >= width) {
		      height += Math.ceil(linelength / width);
		    }
		}
		height += lines.length;
		// NOTE: for the max height -- the ipad doesn't put a scroll bar on text areas
		//if (height <= max) {
			textarea.rows = height;
		//	textarea.style.height = (height * 15) + 'px';
		//} else {
		//	textarea.rows = max;
		//	textarea.style.height = (max * 15) + 'px';
		//}
	}
}

function limitText(limitField, limitNum) {
	if (limitField.value.length > limitNum) {
		limitField.value = limitField.value.substring(0, limitNum);
	}
}

function extractNumber(obj, decimalPlaces, allowNegative) {
	var temp = obj.value;
	
	// avoid changing things if already formatted correctly
	var reg0Str = '[0-9]*';
	if (decimalPlaces > 0) {
		reg0Str += '\\.?[0-9]{0,' + decimalPlaces + '}';
	} else if (decimalPlaces < 0) {
		reg0Str += '\\.?[0-9]*';
	}
	reg0Str = allowNegative ? '^-?' + reg0Str : '^' + reg0Str;
	reg0Str = reg0Str + '$';
	var reg0 = new RegExp(reg0Str);
	if (reg0.test(temp)) return true;

	// first replace all non numbers
	var reg1Str = '[^0-9' + (decimalPlaces != 0 ? '.' : '') + (allowNegative ? '-' : '') + ']';
	var reg1 = new RegExp(reg1Str, 'g');
	temp = temp.replace(reg1, '');

	if (allowNegative) {
		// replace extra negative
		var hasNegative = temp.length > 0 && temp.charAt(0) == '-';
		var reg2 = /-/g;
		temp = temp.replace(reg2, '');
		if (hasNegative) temp = '-' + temp;
	}
	
	if (decimalPlaces != 0) {
		var reg3 = /\./g;
		var reg3Array = reg3.exec(temp);
		if (reg3Array != null) {
			// keep only first occurrence of .
			//  and the number of places specified by decimalPlaces or the entire string if decimalPlaces < 0
			var reg3Right = temp.substring(reg3Array.index + reg3Array[0].length);
			reg3Right = reg3Right.replace(reg3, '');
			reg3Right = decimalPlaces > 0 ? reg3Right.substring(0, decimalPlaces) : reg3Right;
			temp = temp.substring(0,reg3Array.index) + '.' + reg3Right;
		}
	}
	
	obj.value = temp;
}

function blockNonNumbers(obj, e, allowDecimal, allowNegative) {
	var key;
	var isCtrl = false;
	var keychar;
	var reg;
		
	if (window.event) {
		key = e.keyCode;
		isCtrl = window.event.ctrlKey
	} else if (e.which) {
		key = e.which;
		isCtrl = e.ctrlKey;
	}
	
	if (key == 13) {
    	document.getElementById("detail").submit();
		return false;
   	}
   
	if (isNaN(key)) return true;
	
	keychar = String.fromCharCode(key);
	
	// check for backspace or delete, or if Ctrl was pressed
	if (key == 8 || isCtrl)	{
		return true;
	}

	reg = /\d/;
	var isFirstN = allowNegative ? keychar == '-' && obj.value.indexOf('-') == -1 : false;
	var isFirstD = allowDecimal ? keychar == '.' && obj.value.indexOf('.') == -1 : false;
	
	return isFirstN || isFirstD || reg.test(keychar);
}

function checkSpecialCharacters(e) {
	var keynum  
	var keychar  
	
	if (window.event) {  	// for IE
		keynum = e.keyCode  
	}  else if (e.which) {  // for Netscape/Firefox/Opera  
		keynum = e.which  
	}  
	keychar = String.fromCharCode(keynum)  
	
	// list of special characters to restrict -- !#'"`&;
	if (keychar == "!" || keychar == "#" || keychar == "'" || keychar == "\"" || keychar == "`" || keychar == "&") {  
		return false;  
	} else {  
		return true;  
	}  
}

function minNumber(obj, min) {
	if (obj.value.length == 0) {
		obj.value = min;
	}
}

function clearMask(obj) {
	if (obj != null && obj.value == "xxxxxx")
		obj.value = "";
}

function replaceMask(obj) {
	if (obj != null && obj.value == "")
		obj.value = "xxxxxx";
}

function addAsterik(str) {
	if (str.substring(str.length - 1, str.length) != '*') {
		str = str + ' *';
	}
	return str;
}

function removeAsterik(str) {
	if (str.substring(str.length - 2, str.length) == ' *') {
		str = str.substring(0, str.length - 2);
	}
	return str;					
}

function days_between(date1, date2) {
	var ONE_DAY = 1000 * 60 * 60 * 24;
	var date1_ms = date1.getTime();
	var date2_ms = date2.getTime();
	var difference_ms = Math.abs(date1_ms - date2_ms);
	return Math.round(difference_ms / ONE_DAY);
}

function days_between_negative(date1, date2) {
	var ONE_DAY = 1000 * 60 * 60 * 24;
	var date1_ms = date1.getTime();
	var date2_ms = date2.getTime();
	var difference_ms = date1_ms - date2_ms;
	return Math.round(difference_ms / ONE_DAY);
}

function fixDateString(str) {
	str = str.replace('-', '\/');
	str = str.replace('-', '\/');
  	return str;
}

function trim(s) {
  return s.replace(/^\s+|\s+$/, '');
}

function focus(fld) {
	if (fld != null) {
		if (fld.type != "hidden" && fld.style.display != "none" && !fld.disabled)  
			fld.focus();
		if (fld.type == "text" || fld.type == "textarea") 
			fld.select();
	}
}

function validatePhone(fld, fieldname, required) {
   var error = "";
   var stripped = fld.value.replace(/[\(\)\.\-\ ]/g, '');    

   if (required && fld.value == "") {
   		error = "\n* " + fieldname + " cannot be blank";
   		focus(fld);
   } else if (isNaN(parseInt(stripped)) && fld.value != "") {
        error = "\n* The " + fieldname + " contains illegal characters";
        focus(fld);
   } else if ((required || fld.value != "") && !(stripped.length == 10)) {
        error = "\n* The " + fieldname + " is the wrong length. Make sure you included an area code";
        focus(fld);
   }
   return error;
}

function validateEmail(fld, fieldname, required) {
    var error = "";
    var tfld = trim(fld.value);                        					// value of field with whitespace trimmed off
    var emailPattern = /^[^@]+@[^@.]+\.[^@]*\w\w$/ ;
    var illegalChars= /[\(\)\<\>\,\;\:\\\"\[\]]/ ;
   
    if (required && fld.value == "") {
        error = "\n* " + fieldname + " cannot be blank";
        focus(fld);
    } else if ((required || fld.value != "") && !emailPattern.test(tfld)) {       		// test email for illegal characters
        error = "\n* Please enter a valid " + fieldname;
   	    focus(fld);
    } else if ((required || fld.value != "") && fld.value.match(illegalChars)) {
        error = "\n* " + fieldname + " contains illegal characters";
        focus(fld);
    }
    return error;
}

function validateSSN(fld, fieldname, required) {
   var error = "";
   var stripped = fld.value.replace(/[\(\)\.\-\ ]/g, '');    

   if (required && fld.value == "") {
   		error = "\n* " + fieldname + " cannot be blank";
   		focus(fld);
   } else if (isNaN(parseInt(stripped)) && fld.value != "") {
        error = "\n* The " + fieldname + " contains illegal characters";
        focus(fld);
   } else if ((required || fld.value != "") && !(stripped.length == 9)) {
        error = "\n* The " + fieldname + " is the wrong length.";
        focus(fld);
   }
   return error;
}

function validateNotBlank(fld, fieldname) {
	var error = "";
	if (fld == null || fld.value == "" || fld.value == "0") {
        error = "\n* " + fieldname + " cannot be blank";
        focus(fld);
    }
	return error;
}

function validateNotSelected(fld, fieldname) {
	var error = "";
	if (fld == null || fld.value == "" || fld.value == "0") {
        error = "\n* " + fieldname + " must be selected";
        focus(fld);
    }
	return error;
}

function validateState(fld, fieldname, required) {
    var error = "";
	var tfld = trim(fld.value);
	var states = "wa|or|ca|ak|nv|id|ut|az|hi|mt|wy|co|nm|nd|sd|ne|ks|ok|tx|mn|ia|mo|ar|la|wi|il|ms|mi|in|ky|tn|al|fl|ga|sc|nc|oh|wv|va|pa|ny|vt|me|nh|ma|ri|ct|nj|de|md|dc";
	if (required && fld.value == "") {
        error = "\n* " + fieldname + " cannot be blank";
        focus(fld);
    } else if ((required || fld.value != "") && (fld.value.length != 2 || states.indexOf(tfld.toLowerCase() + "|") == -1)) {
		error = "\n* " + fieldname + " abbreviation is not valid";
		focus(fld);
	}
	return error;
}

function validateZipcode(fld, fieldname, required) {
	var error = "";
	var tfld = trim(fld.value);   
    var zipcodePattern = /^\d{5}([\-]\d{4})?$/;
    if (required && fld.value == "") {
        error = "\n* " + fieldname + " cannot be blank";
        focus(fld);
    } else if ((required || fld.value != "") && !zipcodePattern.test(tfld)) {
    	if (fld.value.length > 5) {
    		error = "\n* " + fieldname + " is not valid, include a dash for zip+4";
    	} else {
    		error = "\n* " + fieldname + " is not valid";
    	}
        focus(fld);
    }
    return error;
}

function validateDate(fld, fieldname) {
	var error = "";
	var valid = true;
	var date = trim(fld.value);
	
    date = date.replace('-', '', 'gi');
    date = date.replace('/', '', 'gi');

    var month = parseInt(date.substring(0, 2),10);
    var day   = parseInt(date.substring(2, 4),10);
    var year  = parseInt(date.substring(4, 8),10);

    if ((month < 1) || (month > 12)) valid = false;
    else if ((day < 1) || (day > 31)) valid = false;
    else if (((month == 4) || (month == 6) || (month == 9) || (month == 11)) && (day > 30)) valid = false;
    else if ((month == 2) && (((year % 400) == 0) || ((year % 4) == 0)) && ((year % 100) != 0) && (day > 29)) valid = false;
    else if ((month == 2) && ((year % 100) == 0) && (day > 29)) valid = false;

    if (!valid) {
        error = "\n* " + fieldname + " is not a valid date";
        focus(fld);
    }
	return error;
}

function showNotBusy() {
	window.document.body.style.cursor = 'default';
}

function showBusy() {
	window.document.body.style.cursor = 'wait';
}

function undoBusyButton(id) {
	showNotBusy();
	
	var button = document.getElementById(id);
	if (button != null) {
		button.className = 'appButtonBold';
	}
}

function busyButton(id) {
	showBusy();
	
	var button = document.getElementById(id);
	if (button != null) {
		button.className = 'appButtonBoldDisabled';
	}
}

function disableButton(id) {
	var button = document.getElementById(id);
	if (button != null) {
		button.className = 'appButtonBoldDisabled';
		button.disabled = true;
	}
}

function enableButton(id, className) {
	var button = document.getElementById(id);
	if (button != null) {
		button.className = className;
		button.disabled = false;
	}
}

function disableForm() {
	// loop through the elements of the form.
	for (var i=0; i < (document.detail.elements.length - 1); i++) {
		if (document.detail.elements[i].type == "text" ||
			document.detail.elements[i].type == "select-one"
		) {
			document.detail.elements[i].readOnly = true;
	 		document.detail.elements[i].className = 'appField disabledTextbox';
	 	}	
	}
}

function moveAttribute(listFrom, listTo) {
	var fromList = document.getElementById(listFrom);
	var toList = document.getElementById(listTo);
	for (var i=0; i < fromList.length; i++) {
		if (fromList != null && fromList[i].selected) {
			var tmp = fromList.options[i].text;
			tmp = removeAsterik(tmp);
			var tmp1 = fromList.options[i].value;
			
			fromList.remove(i);
			i--;
			var y = document.createElement('option');
			y.text = tmp;
			y.value = tmp1;
			try {
				toList.add(y);  			// ie
			} catch(ex) {
			    try {
				    toList.add(y, null);
				} catch(e) {
					toList.appendChild(y);  // ipad
				}
			}
		}
	}
}

function saveAttributes(list, str) {
	var attributes = document.getElementById(list);
	var values = "";
	for (var i=0; i < attributes.length; i++) {
	    if (values != "") {
	    	values += ","
	    }
		values += attributes.options[i].value;
	}
	document.getElementById(str).value = values;
}

function getRadioButtonValue(radioObj) {
	if (!radioObj)
		return "";
	var radioLength = radioObj.length;
	if (radioLength == undefined)
		if (radioObj.checked)
			return radioObj.value;
		else
			return "";
	for (var i=0; i < radioLength; i++) {
		if (radioObj[i].checked) {
			return radioObj[i].value;
		}
	}
	return "";
}

function setRadioButtonValue(radioObj, newValue) {
	if (!radioObj)
		return;
	var radioLength = radioObj.length;
	if (radioLength == undefined) {
		radioObj.checked = (radioObj.value == newValue.toString());
		return;
	}
	for (var i=0; i < radioLength; i++) {
		radioObj[i].checked = false;
		if (radioObj[i].value == newValue.toString()) {
			radioObj[i].checked = true;
		}
	}
}

function avoidDC(id) {
	showBusy();
	var button = document.getElementById(id);
	if (button != null) {
		button.className = 'appButtonBoldDisabled';
		button.disabled = true;
		setTimeout('document.getElementById("' + id + '").disabled = false; document.getElementById("' + id + '").className="appButtonBold"; showNotBusy();', 5000);
	}
}

function avoidRedDC(id) {
	showBusy();
	var button = document.getElementById(id);
	if (button != null) {
		button.className = 'appButtonBoldDisabled';
		button.disabled = true;
		setTimeout('document.getElementById("' + id + '").disabled = false; document.getElementById("' + id + '").className="appRedButton"; showNotBusy();', 5000);
	}
}

function toggle(id) {
	var ele = document.getElementById(id + 'Div');
	var img = document.getElementById(id + 'Img');
	var hid = document.getElementById(id + 'Expanded');
	if (ele.style.display == "block") {
    	ele.style.display = "none";
    	img.src = "images/ArrowPopup.gif";
    	hid.value = 'N';
  	} else {
		ele.style.display = "block";
		img.src = "images/ArrowPopup2.gif";
		hid.value = 'Y';
	}
}

function replaceExtendedAsciiChars(text) {
	var goodQuotes = text.replace(/[\x84\x93\x94]/g, '"')
					.replace(/[\u2018\u2019\u201A]/g, "'") // smart single quotes and apostrophe
					.replace(/[\u201C\u201D]/g, '"') // smart double quotes
					.replace(/\u2026/g, "..."); // ellipsis
	return goodQuotes;
}

var imagesPath = '/itsy/images/';