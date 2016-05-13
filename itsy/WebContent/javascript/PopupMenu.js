// ===================================================================
// Author: Matt Kruse <matt@mattkruse.com>
// WWW: http://www.mattkruse.com/
//
// NOTICE: You may use this code for any purpose, commercial or
// private, without any further permission from the author. You may
// remove this notice from your final code if you wish, however it is
// appreciated by the author if at least my web site address is kept.
//
// You may *NOT* re-distribute this code in any way except through its
// use. That means, you can include it in your product, or your web
// site, or any other form where the code is actually being used. You
// may not put the plain javascript up on your site for download or
// include it in your javascript libraries for download. Instead,
// please just point to my URL to ensure the most up-to-date versions
// of the files. Thanks.
// ===================================================================


/* 
PopupMenu.js
Author: Matt Kruse (adapted by Einstein)
Last modified: 3/21/02 (6/25/07)

DESCRIPTION: This object allows you to easily and quickly popup a window
in a certain place. The window can either be a DIV or a separate browser
window.

COMPATABILITY: Works with Netscape 4.x, 6.x, IE 5.x on Windows. Some small
positioning errors - usually with Window positioning - occur on the 
Macintosh platform. Due to bugs in Netscape 4.x, populating the popup 
window with <STYLE> tags may cause errors.

USAGE:
// Create an object for a WINDOW popup
var win = new PopupMenu(); 

// Create an object for a DIV window using the DIV named 'mydiv'
var win = new PopupMenu('mydiv'); 

// Set the window to automatically hide itself when the user clicks 
// anywhere else on the page except the popup
win.autoHide(); 

// Show the window relative to the anchor name passed in
win.showPopup(anchorname);

// Hide the popup
win.hidePopup();

// Set the size of the popup window (only applies to WINDOW popups
win.setSize(width,height);

// Populate the contents of the popup window that will be shown. If you 
// change the contents while it is displayed, you will need to refresh()
win.populate(string);

// Refresh the contents of the popup
win.refresh();

// Specify how many pixels to the right of the anchor the popup will appear
win.offsetX = 50;

// Specify how many pixels below the anchor the popup will appear
win.offsetY = 100;

NOTES:
1) Requires the functions in AnchorPosition.js

2) Your anchor tag MUST contain both NAME and ID attributes which are the 
   same. For example:
   <A NAME="test" ID="test"> </A>

3) There must be at least a space between <A> </A> for IE5.5 to see the 
   anchor tag correctly. Do not do <A></A> with no space.

4) When a PopupMenu object is created, a handler for 'onmouseup' is
   attached to any event handler you may have already defined. Do NOT define
   an event handler for 'onmouseup' after you define a PopupMenu object or
   the autoHide() will not work correctly.
*/ 


// Set the position of the popup window based on the anchor
function PopupMenu_getXYPosition(anchorname) {
	var coordinates;
	if (this.type == "WINDOW") {
		coordinates = getAnchorWindowPosition(anchorname);
	}
	else {
		coordinates = getAnchorPosition(anchorname);
	}
	this.x = coordinates.x;
	this.y = coordinates.y;
}

// Set width/height of popup window (Not DIV)
function PopupMenu_setSize(width,height) {
	this.width = width;
	this.height = height;
}

// Fill the window with contents
function PopupMenu_populate(contents) {
	this.contents = contents;
	this.populated = false;
}

// Refresh the displayed contents of the popup
function PopupMenu_refresh() {
	if (this.divName != null) {
		// refresh the DIV object
		if (this.use_gebi) {
			window.frames["mainFrame"].document.getElementById(this.divName).innerHTML = this.contents;
		}
	}
	else {
		if (this.popupWindow != null && !this.popupWindow.closed) {
			this.popupWindow.document.open();
			this.popupWindow.document.writeln(this.contents);
			this.popupWindow.document.close();
			this.popupWindow.focus();
		}
	}
}

// Position and show the popup, relative to an anchor object
// Modified to accept x and y instead of an anchor - Dave
//function PopupMenu_showPopup(anchorname) {
function PopupMenu_showPopup(xpos, ypos) {
	//this.getXYPosition(anchorname);
	this.x = xpos;
	this.y = ypos;
	this.x += this.offsetX;
	this.y += this.offsetY;
    var scrollArray = getScrollXY();
	this.x += scrollArray[0];
	this.y += scrollArray[1];
	if (!this.populated && (this.contents != "")) {
		this.populated = true;
		this.refresh();
	}
	if (this.divName != null) {
		// Show the DIV object
		// Added the px because FF requires the units to correctly position DIV
		if (this.use_gebi) {
			window.frames["mainFrame"].document.getElementById(this.divName).style.left = this.x +"px";
			window.frames["mainFrame"].document.getElementById(this.divName).style.top = this.y +"px";
			window.frames["mainFrame"].document.getElementById(this.divName).style.visibility = "visible";
			this.hideSelect(); // Added to hide select drop-downs on main page.
		}
	}
	else {
		if (this.popupWindow == null || this.popupWindow.closed) {
			// If the popup window will go off-screen, move it so it doesn't
			if (screen && screen.availHeight) {
				if ((this.y + this.height) > screen.availHeight) {
					this.y = screen.availHeight - this.height;
				}
			}
			if (screen && screen.availWidth) {
				if ((this.x + this.width) > screen.availWidth) {
					this.x = screen.availWidth - this.width;
				}
			}
			this.popupWindow = window.open("about:blank","window_"+anchorname,"toolbar=no,location=no,status=no,menubar=no,scrollbars=auto,resizable,alwaysRaised,dependent,titlebar=no,width="+this.width+",height="+this.height+",screenX="+this.x+",left="+this.x+",screenY="+this.y+",top="+this.y+"");
		}
		this.refresh();
	}
}

// Hide the popup
function PopupMenu_hidePopup() {
//alert("In hide popup: "+this.divName);
	if (this.divName != null) {
		if (this.use_gebi) {
			var obj = window.frames["mainFrame"].document.getElementById(this.divName);
			obj.style.visibility = "hidden";
			hideAllSubs(obj);
//			var kids = obj.getElementsByTagName("ul");
//			ulCnt = kids.length;
//	    	for (var jnx=0; jnx<ulCnt; jnx++) {
//	    		if(kids[jnx].toref) window.clearTimeout(kids[jnx].toref);
//	      		kids[jnx].style.display="none";
//	    	}
		}
		this.unhideSelect(); // Added to re-display select drop-downs on main page.
	}
	else {
		if (this.popupWindow && !this.popupWindow.closed) {
			this.popupWindow.close();
			this.popupWindow = null;
		}
	}
}

// Pass an event and return whether or not it was the popup DIV that was clicked
function PopupMenu_isClicked(e) {
	if (!e) var e = window.event
	if (this.divName != null) {
		if (this.use_layers) {
			var clickX = e.pageX;
			var clickY = e.pageY;
			var t = document.layers[this.divName];
			if ((clickX > t.left) && (clickX < t.left+t.clip.width) && (clickY > t.top) && (clickY < t.top+t.clip.height)) {
				return true;
			}
			else { return false; }
		}
		else if (document.all) { // Need to hard-code this to trap IE for error-handling
			var t = window.frames["mainFrame"].event.srcElement;
			if (t) {
			  while (t.parentElement != null) {
				if (t.id==this.divName) {
					return true;
				}
				t = t.parentElement;
			  }
			}
			return false;
		}
		else if (this.use_gebi) {
			var t = e.originalTarget;
			while (t.parentNode != null) {
				if (t.id==this.divName) {
					return true;
				}
				t = t.parentNode;
			}
			return false;
		}
		return false;
	}
	return false;
}

// Check an onMouseDown event to see if we should hide
function PopupMenu_hideIfNotClicked(e) {
	if (!e) var e = window.event
	if (this.autoHideEnabled && !this.isClicked(e)) {
//alert("In hide if not clicked: ");
		this.hidePopup();
	}
}

// Call this to make the DIV disable automatically when mouse is clicked outside it
function PopupMenu_autoHide() {
	this.autoHideEnabled = true;
}

// This global function checks all PopupMenu objects to see if they should be hidden
function PopupMenu_hidePopupMenus(e) {
	if (!e) var e = window.event
	for (var i=0,plen=popupWindowObjects.length; i<plen; i++) {
		if (popupWindowObjects[i] != null) {
			var p = popupWindowObjects[i];
			p.hideIfNotClicked(e);
//alert("hide popup menus: " + plen + ", "+i);
		}
	}
}

// Run this immediately to attach the event listener
var listenerNotAttached = true;
function PopupMenu_attachListener() {
//  if (listenerNotAttached) 
  {
	var frameWin = window.top.window.frames["mainFrame"].document;
	if (document.layers) {
		document.captureEvents(Event.MOUSEUP | Event.SCROLL | Event.MOUSEWHEEL);
	}
//	alert("onmouseup = "+frameWin.onmouseup);
	frameWin.onmouseup = PopupMenu_hidePopupMenus;

// Added for scroll wheel to hide menu
	frameWin.onmousewheel = PopupMenu_hidePopupMenus;

// Added for scrolling to hide menu
   //If we are browsing with IE, reassign frameWin
    if (document.all) {    
        frameWin = window.top.window.frames["mainFrame"].document.body;
    }
	frameWin.onscroll = PopupMenu_hidePopupMenus;
  }
  listenerNotAttached = false;
}

// Determine the window scrolling so we can correctly place the menu
function getScrollXY() {
  var scrOfX = 0, scrOfY = 0;
  var frameWin = window.frames["mainFrame"].document;
  if( document.documentElement && ( frameWin.documentElement.scrollLeft || frameWin.documentElement.scrollTop ) ) {
    //IE6 standards compliant mode
    scrOfY = frameWin.documentElement.scrollTop;
    scrOfX = frameWin.documentElement.scrollLeft;
  } else if( typeof( window.pageYOffset ) == 'number' ) {
    //Netscape compliant
    scrOfY = window.frames["mainFrame"].pageYOffset;
    scrOfX = window.frames["mainFrame"].pageXOffset;
  } else if( document.body && ( frameWin.body.scrollLeft || frameWin.body.scrollTop ) ) {
    //DOM compliant
    scrOfY = frameWin.body.scrollTop;
    scrOfX = frameWin.body.scrollLeft;
  }
  return [ scrOfX, scrOfY ];
}

// CONSTRUCTOR for the PopupMenu object
// Pass it a DIV name to use a DHTML popup, otherwise will default to window popup
function PopupMenu() {
	if (!window.popupWindowIndex) { window.popupWindowIndex = 0; }
	if (!window.popupWindowObjects) { window.popupWindowObjects = new Array(); }
	if (!window.listenerAttached) {
		window.listenerAttached = true;
		PopupMenu_attachListener();
		}
//	this.index = popupWindowIndex++; // If you want multiple popups on the same page - use this.
	this.index=0;
	popupWindowObjects[this.index] = this;
	this.divName = null;
	this.popupWindow = null;
	this.width=0;
	this.height=0;
	this.populated = false;
	this.visible = false;
	this.autoHideEnabled = false;
	
	this.contents = "";
	if (arguments.length>0) {
		this.type="DIV";
		this.divName = arguments[0];
		}
	else {
		this.type="WINDOW";
		}
	this.use_gebi = false;
	this.use_css = false;
	this.use_layers = false;
	if (document.getElementById) { this.use_gebi = true; }
	else if (document.all) { this.use_css = true; }
	else if (document.layers) { this.use_layers = true; }
	else { this.type = "WINDOW"; }
	this.offsetX = 0;
	this.offsetY = 0;
	// Method mappings
	this.getXYPosition = PopupMenu_getXYPosition;
	this.populate = PopupMenu_populate;
	this.refresh = PopupMenu_refresh;
	this.showPopup = PopupMenu_showPopup;
	this.hidePopup = PopupMenu_hidePopup;
	this.setSize = PopupMenu_setSize;
	this.isClicked = PopupMenu_isClicked;
	this.autoHide = PopupMenu_autoHide;
	this.hideIfNotClicked = PopupMenu_hideIfNotClicked;
	this.hideSelect = PopupMenu_hideSelect;
	this.unhideSelect = PopupMenu_unhideSelect;
	this.attachListener = PopupMenu_attachListener;
}
var inHideSelectProcess = false;
function PopupMenu_hideSelect()
{
  if (!inHideSelectProcess) {
    inHideSelectProcess = true;
    var selElems=top.mainFrame.document.getElementsByTagName('select');
    var numElems=selElems.length;
    for(i=0; i < numElems; i++) {
        selElems[i].style.visibility = "hidden";
    }
    inHideSelectProcess = false;
  }
}
function PopupMenu_unhideSelect()
{
  if (!inHideSelectProcess) {
    inHideSelectProcess = true;
    var selElems=top.mainFrame.document.getElementsByTagName('select');
    var numElems=selElems.length;
//alert("In unhide select: "+numElems);
    for(i=0; i < numElems; i++) {
        selElems[i].style.visibility = "visible";
    }
    inHideSelectProcess = false;
  }
}

function hideAllSubs(liobj) {
  var p=liobj.childNodes;
  var ulCnt = 0;
  var inx = 0;
  try {
	for (inx=0, pLen=p.length; inx<pLen; inx++) {
	  ulCnt = p[inx].getElementsByTagName("ul").length;
	  if ("LI"==p[inx].tagName) {
	    for (var jnx=0; jnx<ulCnt; jnx++) {
	      p[inx].getElementsByTagName("ul")[jnx].style.display="none";
	    }
	  }
   	}
  } catch(e) {
    alert(p[inx].innerHTML)
  }
}
