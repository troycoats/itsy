/**
 * Highlights currently selected table row
 */
function highlightTR(elem) {
	var allelems = document.getElementsByClassName('listTableValHighlight');
	for (var i = 0; i < allelems.length; i++) {
		allelems[i].className = "listTableVal";
	}
	elem.className = "listTableValHighlight";
}

/**
 * function to get the highest zindex so far
 */
function findHighestZIndex() {
	var divs = document.getElementsByTagName('div');
	var highest = 0;
	for (var i = 0; i < divs.length; i++) {
		var zindex = divs[i].style.zIndex;
		if (zindex > highest) {
			highest = zindex;
		}
	}
	return highest;
}

/**
 * function to get 'mainFrame' from top window
 */
function getMainFrame() {
	return window.top.parent.frames["mainFrame"];
}

/**
 * function to open popin with in main iframe 
 */
function openPopin(title, name, url, h, w) {
	return getMainFrame().dhtmlwindow.open(name, "iframe", url, title, "width=" + w + "px,height=" + h + "px,resize=1,scrolling=1,center=1");
}

/**
 * function to open modal popin with in main iframe 
 */
function openPopinM(title, name, url, h, w) {
	return getMainFrame().dhtmlmodal.open(name, "iframe", url, title, "width=" + w + "px,height=" + h + "px,resize=1,scrolling=1,center=1");
}

/**
 * function to open popin with in the opener window
 */
function openPopinWithin(title, name, url, h, w) {
	return dhtmlwindow.open(name, "iframe", url, title, "width=" + w + "px,height=" + h + "px,resize=1,scrolling=1,center=1");
}

/**
 * function to open modal popin with in the opener window
 */
function openPopinMWithin(title, name, url, h, w) {
	return dhtmlmodal.open(name, "iframe", url, title, "width=" + w + "px,height=" + h + "px,resize=1,scrolling=1,center=1");
}

/**
 * function to close popins
 */
function closeDhtmlPopin() {
	var win = getSelfHandle();
	if (win !== null) {
		window.parent.dhtmlmodal.closeveil(win);
		win.close();
	}
}

/**
 * function to close popins and also refresh parent, reloadDivId is the parent window Id (removes veil if available)
 */
function closeAndRefreshParentPopin(reloadDivId) {
	var win = getSelfHandle();
	if (win !== null) {
		window.parent.dhtmlmodal.closeveil(win);
		if (reloadDivId!=null && reloadDivId!="" && reloadDivId!="null")
			getWindowHandleByDivId(reloadDivId).location.reload();
		win.close();
	}
}

/**
 * function to load dhtml pop in in the same window
 */
function loadDhtmlPopin(contentType, url, title) {
	var win = getSelfHandle();
	win.load(contentType, url, title);
}

/**
 * function to resize popin window according to content
 */
function resizeToContent(divId) {
	var parent = window.parent;
	var win = parent.document.getElementById(divId);
	var windowIframe = null;
	if (win !== null) {
		windowIframe = win.contentarea.getElementsByTagName("iframe")[0];
		windowIframe.addEventListener('load', function() {
			parent.dhtmlwindow.resizetocontent(win);
		});
	} else {
		win = document.getElementById(divId);
		windowIframe = win.contentarea.getElementsByTagName("iframe")[0];
		windowIframe.addEventListener('load', function() {
			dhtmlwindow.resizetocontent(win);
		});
	}
}

/**
 * function to retrieve self window
 */
function getSelfHandle() {
	return window.parent.document.getElementById(window.name.replace(/_iframe-/, ""));
}

/**
 * function to retrieve the window by unique id
 */
function getWindowHandleByDivId(divId) {
	var win = null;
	if (divId!=null && divId!="" && divId!="null")
		win = window.parent.document.getElementById(divId).contentarea.getElementsByTagName("iframe")[0].contentWindow;
	return win;
}

/**
 * closes dhtml popin window on esc
 */
document.onkeydown = function(evt) {
	evt = evt || window.event;
	if (evt.keyCode == 27) {  // esc key pressed
		closeDhtmlPopin();    // works if the dhtml window content has focus
	}
};