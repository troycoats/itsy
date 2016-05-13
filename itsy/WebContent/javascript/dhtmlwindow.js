// -------------------------------------------------------------------
// DHTML Window Widget- By Dynamic Drive, available at: http://www.dynamicdrive.com
// v1.1:  Oct 29th, 07'
// -------------------------------------------------------------------

var dhtmlwindow = {
	imagefiles: [imagesPath + 'min.png',
				 imagesPath + 'max.png',
				 imagesPath + 'close.png',
				 imagesPath + 'restore.png',
				 imagesPath + 'resize.png'], //Path to 5 images used by script, in that order
	ajaxbustcache: true, //Bust caching when fetching a file via Ajax?
	ajaxloadinghtml: '<div align="center"><div id="loadingCard" style="top:100px;"><div id="loadingMessage">Loading...</div></div></div>', //HTML to show while window fetches Ajax Content?
	windowBuffer: 40, //total buffer around dhtml windows in px
	minimizeorder: 0,
	zIndexvalue: 100,
	tobjects: [], //object to contain references to dhtml window divs, for cleanup purposes
	lastactivet: {}, //reference to last active DHTML window

	init: function(t) {
		var domwindow = document.createElement("div"); //create dhtml window div
		domwindow.id = t;
		domwindow.className = "dhtmlwindow";
		var domwindowdata = '';
		domwindowdata = '<div class="drag-handle">';
		domwindowdata += 'DHTML Window <div class="drag-controls">';
		domwindowdata += '<img src="' + this.imagefiles[0] + '" title="Minimize" />&nbsp;';
		domwindowdata += '<img src="' + this.imagefiles[1] + '" title="Maximize" />&nbsp;';
		domwindowdata += '<img src="' + this.imagefiles[2] + '" title="Close" />';
		domwindowdata += '</div>';
		domwindowdata += '</div>';
		domwindowdata += '<div class="drag-contentarea"></div>';
		domwindowdata += '<div class="drag-statusarea">';
		domwindowdata += '<div class="drag-resizearea" style="background: transparent url(' + this.imagefiles[4] + ') top right no-repeat;">&nbsp;</div>';
		domwindowdata += '</div>';
		domwindowdata += '</div>';
		domwindow.innerHTML = domwindowdata;
		document.getElementById("dhtmlwindowholder").appendChild(domwindow);
		//this.zIndexvalue=(this.zIndexvalue)? this.zIndexvalue+1 : 100; //z-index value for DHTML window: starts at 0, increments whenever a window has focus
		var t = document.getElementById(t);
		var divs = t.getElementsByTagName("div");
		for (var i = 0; i < divs.length; i++) { //go through divs inside dhtml window and extract all those with class="drag-" prefix
			if (/drag-/.test(divs[i].className))
				t[divs[i].className.replace(/drag-/, "")] = divs[i]; //take out the "drag-" prefix for shorter access by name
		}
		//t.style.zIndex=this.zIndexvalue; //set z-index of this dhtml window
		t.handle._parent = t; //store back reference to dhtml window
		t.resizearea._parent = t; //same
		t.controls._parent = t; //same
		t.onclose = function() {
			return true;
		}; //custom event handler "onclose"
		t.onmousedown = function() {
			dhtmlwindow.setfocus(this);
		}; //Increase z-index of window when focus is on it
		t.handle.onmousedown = dhtmlwindow.setupdrag; //set up drag behavior when mouse down on handle div
		t.resizearea.onmousedown = dhtmlwindow.setupdrag; //set up drag behavior when mouse down on resize div
		t.controls.onclick = dhtmlwindow.enablecontrols;
		t.show = function() {
			dhtmlwindow.show(this); //public function for showing dhtml window
		};
		t.hide = function() {
			dhtmlwindow.hide(this); //public function for hiding dhtml window
		};
		t.close = function() {
			dhtmlwindow.close(this); //public function for closing dhtml window (also empties DHTML window content)
		};
		t.setSize = function(w, h) {
			dhtmlwindow.setSize(this, w, h); //public function for setting window dimensions
		};
		t.moveTo = function(x, y) {
			dhtmlwindow.moveTo(this, x, y); //public function for moving dhtml window (relative to viewpoint)
		};
		t.isResize = function(bol) {
			dhtmlwindow.isResize(this, bol); //public function for specifying if window is resizable
		};
		t.isScrolling = function(bol) {
			dhtmlwindow.isScrolling(this, bol); //public function for specifying if window content contains scrollbars
		};
		t.load = function(contenttype, contentsource, title) {
			dhtmlwindow.load(this, contenttype, contentsource, title); //public function for loading content into window
		};
		this.tobjects[this.tobjects.length] = t;

		return t; //return reference to dhtml window div
	},

	open: function(t, contenttype, contentsource, title, attr, recalonload) {
		var d = dhtmlwindow; //reference dhtml window object
		function getValue(Name) {
			var config = new RegExp(Name + "=([^,]+)", "i"); //get name/value config pair (ie: width=400px,)
			return (config.test(attr)) ? parseInt(RegExp.$1) : 0; //return value portion (int), or 0 (false) if none found
		}
		if (document.getElementById(t) == null) //if window doesn't exist yet, create it
			t = this.init(t); //return reference to dhtml window div
		else
			t = document.getElementById(t);
		this.setfocus(t);
		t.setSize(getValue(("width")), (getValue("height"))); //Set dimensions of window
		var xpos = getValue("center") ? "middle" : getValue("left"); //Get x coord of window
		var ypos = getValue("center") ? "middle" : getValue("top"); //Get y coord of window
		//t.moveTo(xpos, ypos); //Position window
		if (typeof recalonload != "undefined" && recalonload == "recal" && this.scroll_top == 0) { //reposition window when page fully loads with updated window viewpoints?
			if (window.attachEvent && !window.opera) //In IE, add another 400 milisecs on page load (viewpoint properties may return 0 b4 then)
				this.addEvent(window, function() {
					setTimeout(function() {
						t.moveTo(xpos, ypos);
					}, 400);
				}, "load");
			else
				this.addEvent(window, function() {
					t.moveTo(xpos, ypos);
				}, "load");
		}
		this.zIndexvalue = findHighestZIndex();
		t.isResize(getValue("resize")); //Set whether window is resizable
		t.isScrolling(getValue("scrolling")); //Set whether window should contain scrollbars
		t.style.zIndex = ++this.zIndexvalue;
		t.style.visibility = "visible";
		t.style.display = "block";
		t.contentarea.style.display = "block";
		t.moveTo(xpos, ypos); //Position window
		t.load(contenttype, contentsource, title);
		if (t.state == "minimized" && t.controls.firstChild.title == "Restore Up") { //If window exists and is currently minimized?
			t.controls.firstChild.setAttribute("src", dhtmlwindow.imagefiles[0]); //Change "restore" icon within window interface to "minimize" icon
			t.controls.firstChild.setAttribute("title", "Minimize");
			t.state = "fullview"; //indicate the state of the window as being "fullview"
		}
		d.getviewpoint();
		if (parseInt(t.style.width) > (d.docwidth - 10)) {
			t.style.width = (d.docwidth - 10) + 'px';
			t.moveTo('middle', 'middle');
		}
		if (parseInt(t.contentarea.style.height) > (d.docheight - 30)) {
			t.contentarea.style.height = (d.docheight - 30) + 'px';
			t.moveTo('middle', 'middle');
		}
		return t;
	},

	setSize: function(t, w, h) { //set window size (min is 150px wide by 100px tall)
		t.style.width = Math.max(parseInt(w), 150) + "px";
		t.contentarea.style.height = Math.max(parseInt(h), 100) + "px";
	},

	moveTo: function(t, x, y) { //move window. Position includes current viewpoint of document
		this.getviewpoint(); //Get current viewpoint numbers
		t.style.left = (x == "middle") ? this.scroll_left + (this.docwidth - t.offsetWidth) / 2 + "px" : this.scroll_left + parseInt(x) + "px";
		t.style.top = (y == "middle") ? this.scroll_top + (this.docheight - t.offsetHeight) / 2 + "px" : this.scroll_top + parseInt(y) + "px";
	},

	isResize: function(t, bol) { //show or hide resize inteface (part of the status bar)
		t.statusarea.style.display = (bol) ? "block" : "none";
		t.resizeBool = (bol) ? 1 : 0;
	},

	isScrolling: function(t, bol) { //set whether loaded content contains scrollbars
		t.contentarea.style.overflow = (bol) ? "auto" : "hidden";
	},

	load: function(t, contenttype, contentsource, title) { //loads content into window plus set its title (3 content types: "inline", "iframe", or "ajax")
		window.scrollTo(0, 0);
		if (t.isClosed) {
			alert("Window has been closed, so no window to load contents into. Open/Create the window again.");
			return;
		}
		var contenttype = contenttype.toLowerCase(); //convert string to lower case
		if (typeof title != "undefined")
			t.handle.firstChild.nodeValue = title;
		if (contenttype == "inline")
			t.contentarea.innerHTML = contentsource;
		else if (contenttype == "div") {
			var inlinedivref = document.getElementById(contentsource);
			t.contentarea.innerHTML = (inlinedivref.defaultHTML || inlinedivref.innerHTML); //Populate window with contents of inline div on page
			if (!inlinedivref.defaultHTML)
				inlinedivref.defaultHTML = inlinedivref.innerHTML; //save HTML within inline DIV
			inlinedivref.innerHTML = ""; //then, remove HTML within inline DIV (to prevent duplicate IDs, NAME attributes etc in contents of DHTML window
			inlinedivref.style.display = "none"; //hide that div
		} else if (contenttype == "iframe") {
			t.contentarea.style.overflow = "hidden"; //disable window scrollbars, as iframe already contains scrollbars
			if (!t.contentarea.firstChild || t.contentarea.firstChild.tagName != "IFRAME") //If iframe tag doesn't exist already, create it first
				t.contentarea.innerHTML = '<iframe src="" style="margin:0; padding:0; width:100%; height: 100%" frameBorder="0" name="_iframe-' + t.id + '"></iframe>';
			window.frames["_iframe-" + t.id].location.replace(contentsource); //set location of iframe window to specified URL
		} else if (contenttype == "ajax") {
			this.ajax_connect(contentsource, t); //populate window with external contents fetched via Ajax
		}
		t.moveTo("middle", "middle");
		t.contentarea.datatype = contenttype; //store contenttype of current window for future reference
	},

	setupdrag: function(e) {
		var d = dhtmlwindow; //reference dhtml window object
		var t = this._parent; //reference dhtml window div
		d.etarget = this; //remember div mouse is currently held down on ("handle" or "resize" div)
		var e = window.event || e;
		d.initmousex = e.clientX; //store x position of mouse onmousedown
		d.initmousey = e.clientY;
		d.initx = parseInt(t.offsetLeft); //store offset x of window div onmousedown
		d.inity = parseInt(t.offsetTop);
		d.width = parseInt(t.offsetWidth); //store width of window div
		d.contentheight = parseInt(t.contentarea.offsetHeight); //store height of window div's content div
		if (t.contentarea.datatype == "iframe") { //if content of this window div is "iframe"
			t.contentarea.getElementsByTagName("iframe")[0].style.visibility = "hidden"; //hide content div (while window is being dragged)
		}
		document.onmousemove = d.getdistance; //get distance travelled by mouse as it moves
		document.onmouseup = function() {
			if (t.contentarea.datatype == "iframe") { //restore color and visibility of content div onmouseup
				t.contentarea.getElementsByTagName("iframe")[0].style.visibility = "visible";
			}
			d.stop();
		};
		return false;
	},

	getdistance: function(e) {
		var d = dhtmlwindow;
		var etarget = d.etarget;
		var e = window.event || e;
		d.distancex = e.clientX - d.initmousex; //horizontal distance travelled relative to starting point
		d.distancey = e.clientY - d.initmousey;
		if (etarget.className == "drag-handle") //if target element is "handle" div
			d.move(etarget._parent, e);
		else if (etarget.className == "drag-resizearea") //if target element is "resize" div
			d.resize(etarget._parent, e);
		return false; //cancel default dragging behavior
	},

	getviewpoint: function() { //get window viewpoint numbers
		var ie = document.all && !window.opera;
		var domclientWidth = document.documentElement && parseInt(document.documentElement.clientWidth) || 100000; //Preliminary doc width in non IE browsers
		this.standardbody = (document.compatMode == "CSS1Compat") ? document.documentElement : document.body; //create reference to common "body" across doctypes
		this.scroll_top = (ie) ? this.standardbody.scrollTop : window.pageYOffset;
		this.scroll_left = (ie) ? this.standardbody.scrollLeft : window.pageXOffset;
		this.docwidth = (ie) ? this.standardbody.clientWidth : (/Safari/i.test(navigator.userAgent)) ? window.innerWidth : Math.min(domclientWidth, window.innerWidth - 16);
		this.docheight = (ie) ? this.standardbody.clientHeight : window.innerHeight;
	},

	rememberattrs: function(t) { //remember certain attributes of the window when it's minimizing/maximizing or closed, such as dimensions, position on page
		this.getviewpoint(); //Get current window viewpoint numbers
		t.lastx = parseInt((t.style.left || t.offsetLeft)) - dhtmlwindow.scroll_left; //store last known x coord of window just before minimizing/maximizing
		t.lasty = parseInt((t.style.top || t.offsetTop)) - dhtmlwindow.scroll_top; //store last known y coord of window just before minimizing/maximizing
		t.lastwidth = parseInt(t.style.width); //store last known width of window just before minimizing/maximizing or closing
		t.lastheight = parseInt(t.contentarea.style.height); //store last known height of window just before minimizing/maximizing or closing
	},

	move: function(t, e) {
		var d = dhtmlwindow; //reference dhtml window object
		var wb = this.windowBuffer/2;
		var winleft = dhtmlwindow.distancex + dhtmlwindow.initx;
		var wintop = dhtmlwindow.distancey + dhtmlwindow.inity;
		var scroll_top = (document.all) ? this.standardbody.scrollTop : window.pageYOffset;
		var scroll_left = (document.all) ? this.standardbody.scrollLeft : window.pageXOffset;
		if (winleft > (scroll_left + wb) && (winleft + d.width + wb) < (scroll_left + this.docwidth)) {
			t.style.left = winleft + "px";
		}
		if (wintop > (scroll_top + wb) && (wintop + t.offsetHeight + wb) < (scroll_top + this.docheight)) {
			t.style.top = wintop + "px";
		}
	},

	resize: function(t, e) {
		t.style.width = Math.max(dhtmlwindow.width + dhtmlwindow.distancex, 150) + "px";
		t.contentarea.style.height = Math.max(dhtmlwindow.contentheight + dhtmlwindow.distancey, 100) + "px";
	},
	
	resizetocontent: function(t) {
		dhtmlwindow.getviewpoint(); //Get current window viewpoint numbers
		var wb = this.windowBuffer;
		var contentWidth = t.contentarea.getElementsByTagName("iframe")[0].contentWindow.document.body.scrollWidth;
		var contentHeight = t.contentarea.getElementsByTagName("iframe")[0].contentWindow.document.body.scrollHeight;
		var w, h;
		if (parseInt(t.style.width) < (dhtmlwindow.docwidth - 10 - wb) && contentWidth < (dhtmlwindow.docwidth - 10 - wb)) {
			w = parseInt(t.style.width) > contentWidth ? parseInt(t.style.width) : contentWidth;
		} else if (parseInt(t.style.width) < (dhtmlwindow.docwidth - 10 - wb) && contentWidth >= (dhtmlwindow.docwidth - 10 - wb)) {
			w = (dhtmlwindow.docwidth - 10 - wb);
		} else if (parseInt(t.style.width) >= (dhtmlwindow.docwidth - 10 - wb) && contentWidth < (dhtmlwindow.docwidth - 10 - wb)) {
			w = contentWidth;
		} else {
			w = (dhtmlwindow.docwidth - 10 - wb);
		}
		if (parseInt(t.contentarea.style.height) < (dhtmlwindow.docheight - 30 - wb) && contentHeight < (dhtmlwindow.docheight - 30 - wb)) {
			h = parseInt(t.contentarea.style.height) > contentHeight ? parseInt(t.contentarea.style.height) : contentHeight;
		} else if (parseInt(t.contentarea.style.height) < (dhtmlwindow.docheight - 30 - wb) && contentHeight >= (dhtmlwindow.docheight - 30 - wb)) {
			h = (dhtmlwindow.docheight - 30 - wb);
		} else if (parseInt(t.contentarea.style.height) >= (dhtmlwindow.docheight - 30 - wb) && contentHeight < (dhtmlwindow.docheight - 30 - wb)) {
			h = contentHeight;
		} else {
			h = (dhtmlwindow.docheight - 30 - wb);
		}
		if (w < (dhtmlwindow.docwidth - 30 - wb) && h < contentHeight) { //special case
			w += 20;
		}
		t.setSize(w, h); //
		t.moveTo('middle', 'middle');
	},

	enablecontrols: function(e) {
		var d = dhtmlwindow;
		var sourceobj = window.event ? window.event.srcElement : e.target; //Get element within "handle" div mouse is currently on (the controls)
		if (/Minimize/i.test(sourceobj.getAttribute("title"))) //if this is the "minimize" control
			d.minimize(sourceobj, this._parent);
		else if (/Maximize/i.test(sourceobj.getAttribute("title"))) //if this is the "maximize" control
			d.maximize(sourceobj, this._parent);
		else if (/Restore Up/i.test(sourceobj.getAttribute("title"))) //if this is the "restore" control
			d.restoreup(sourceobj, this._parent);
		else if (/Restore Down/i.test(sourceobj.getAttribute("title"))) //if this is the "restore" control
			d.restoredown(sourceobj, this._parent);
		else if (/Close/i.test(sourceobj.getAttribute("title"))) //if this is the "close" control
			if (this._parent.contentarea.getElementsByTagName("iframe")[0].contentWindow.document.getElementById('btCancel') != null) {
				this._parent.contentarea.getElementsByTagName("iframe")[0].contentWindow.document.getElementById('btCancel').click(); // on click of the 'X' click on the cancel event in the dom //d.close(this._parent);
			} else {
				d.close(this._parent);
			}
		return false;
	},

	minimize: function(button, t) {
		if (t.state == "maximized") { //If window is currently maximized
			var maxbutton = t.controls.querySelector('img[title="Restore Down"]');
			dhtmlwindow.restoredown(maxbutton, t);
		}
		dhtmlwindow.rememberattrs(t);
		button.setAttribute("src", dhtmlwindow.imagefiles[3]);
		button.setAttribute("title", "Restore Up");
		t.state = "minimized"; //indicate the state of the window as being "minimized"
		t.contentarea.style.display = "none";
		t.statusarea.style.display = "none";
		if (typeof t.minimizeorder == "undefined") { //stack order of minmized window on screen relative to any other minimized windows
			dhtmlwindow.minimizeorder++; //increment order
			t.minimizeorder = dhtmlwindow.minimizeorder;
		}
		t.style.left = "20px"; //left coord of minmized window
		t.style.width = "200px";
		var windowspacing = t.minimizeorder * 10; //spacing (gap) between each minmized window(s)
		t.style.top = dhtmlwindow.scroll_top + dhtmlwindow.docheight - (t.handle.offsetHeight * t.minimizeorder) - windowspacing - 30 + "px";
	},

	maximize: function(button, t) {
		if (t.state == "minimized") { //If window is currently minimized
			var minbutton = t.controls.querySelector('img[title="Restore Up"]');
			dhtmlwindow.restoreup(minbutton, t);
		}
		dhtmlwindow.rememberattrs(t);
		button.setAttribute("src", dhtmlwindow.imagefiles[3]);
		button.setAttribute("title", "Restore Down");
		t.state = "maximized"; //indicate the state of the window as being "maximized"
		t.setSize(dhtmlwindow.docwidth - 10 - this.windowBuffer, dhtmlwindow.docheight - 30 - this.windowBuffer);
		t.moveTo('middle', 'middle');
	},

	restoreup: function(button, t) {
		dhtmlwindow.getviewpoint();
		button.setAttribute("src", dhtmlwindow.imagefiles[0]);
		button.setAttribute("title", "Minimize");
		t.state = "fullview"; //indicate the state of the window as being "fullview"
		t.style.display = "block";
		t.contentarea.style.display = "block";
		if (t.resizeBool) //if this window is resizable, enable the resize icon
			t.statusarea.style.display = "block";
		t.style.left = parseInt(t.lastx) + dhtmlwindow.scroll_left + "px"; //position window to last known x coord just before minimizing
		t.style.top = parseInt(t.lasty) + dhtmlwindow.scroll_top + "px";
		t.style.width = parseInt(t.lastwidth) + "px";
		t.contentarea.style.height = parseInt(t.lastheight) + "px";
	},

	restoredown: function(button, t) {
		dhtmlwindow.getviewpoint();
		button.setAttribute("src", dhtmlwindow.imagefiles[1]);
		button.setAttribute("title", "Maximize");
		t.state = "fullview"; //indicate the state of the window as being "fullview"
		t.style.left = parseInt(t.lastx) + dhtmlwindow.scroll_left + "px"; //position window to last known x coord just before maximizing
		t.style.top = parseInt(t.lasty) + dhtmlwindow.scroll_top + "px";
		t.style.width = parseInt(t.lastwidth) + "px";
		t.contentarea.style.height = parseInt(t.lastheight) + "px";
	},

	close: function(t) {
		try {
			var closewinbol = t.onclose();
		} catch (err) { //In non IE browsers, all errors are caught, so just run the below
			var closewinbol = true;
		} finally { //In IE, not all errors are caught, so check if variable isn't defined in IE in those cases
			if (typeof closewinbol == "undefined") {
				alert("An error has occured somwhere inside your \"onclose\" event handler");
				var closewinbol = true;
			}
		}
		if (closewinbol) { //if custom event handler function returns true
			if (t.state != "minimized") //if this window isn't currently minimized
				dhtmlwindow.rememberattrs(t); //remember window's dimensions/position on the page before closing
			if (window.frames["_iframe-" + t.id]) //if this is an IFRAME DHTML window
				window.frames["_iframe-" + t.id].location.replace("about:blank");
			else
				t.contentarea.innerHTML = "";
			t.style.display = "none";
			t.isClosed = true; //tell script this window is closed (for detection in t.show())
		}
		return closewinbol;
	},

	setopacity: function(targetobject, value) { //Sets the opacity of targetobject based on the passed in value setting (0 to 1 and in between)
		if (!targetobject)
			return;
		if (targetobject.filters && targetobject.filters[0]) { //IE syntax
			if (typeof targetobject.filters[0].opacity == "number") //IE6
				targetobject.filters[0].opacity = value * 100;
			else //IE 5.5
				targetobject.style.filter = "alpha(opacity=" + value * 100 + ")";
		} else if (typeof targetobject.style.MozOpacity != "undefined") //Old Mozilla syntax
			targetobject.style.MozOpacity = value;
		else if (typeof targetobject.style.opacity != "undefined") //Standard opacity syntax
			targetobject.style.opacity = value;
	},

	setfocus: function(t) { //Sets focus to the currently active window
		this.zIndexvalue++;
		t.style.zIndex = this.zIndexvalue;
		t.isClosed = false; //tell script this window isn't closed (for detection in t.show())
		this.setopacity(this.lastactivet.handle, 0.5); //unfocus last active window
		this.setopacity(t.handle, 1); //focus currently active window
		this.lastactivet = t; //remember last active window
	},

	show: function(t) {
		if (t.isClosed) {
			alert("Window has been closed, so nothing to show. Open/Create the window again.");
			return;
		}
		if (t.lastx) //If there exists previously stored information such as last x position on window attributes (meaning it's been minimized or closed)
			dhtmlwindow.restore(t.controls.firstChild, t); //restore the window using that info
		else
			t.style.display = "block";
		this.setfocus(t);
		t.state = "fullview"; //indicate the state of the window as being "fullview"
	},

	hide: function(t) {
		t.style.display = "none";
	},

	ajax_connect: function(url, t) {
		var page_request = false;
		var bustcacheparameter = "";
		if (window.XMLHttpRequest) // if Mozilla, IE7, Safari etc
			page_request = new XMLHttpRequest();
		else if (window.ActiveXObject) { // if IE6 or below
			try {
				page_request = new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {
				try {
					page_request = new ActiveXObject("Microsoft.XMLHTTP");
				} catch (e) {}
			}
		} else
			return false;
		t.contentarea.innerHTML = this.ajaxloadinghtml;
		page_request.onreadystatechange = function() {
			dhtmlwindow.ajax_loadpage(page_request, t);
		};
		if (this.ajaxbustcache) //if bust caching of external page
			bustcacheparameter = (url.indexOf("?") != -1) ? "&" + new Date().getTime() : "?" + new Date().getTime();
		page_request.open('GET', url + bustcacheparameter, true);
		page_request.send(null);
	},

	ajax_loadpage: function(page_request, t) {
		if (page_request.readyState == 4 && (page_request.status == 200 || window.location.href.indexOf("http") == -1)) {
			t.contentarea.innerHTML = page_request.responseText;
		}
	},

	stop: function() {
		dhtmlwindow.etarget = null; //clean up
		document.onmousemove = null;
		document.onmouseup = null;
	},

	addEvent: function(target, functionref, tasktype) { //assign a function to execute to an event handler (ie: onunload)
		var tasktype = (window.addEventListener) ? tasktype : "on" + tasktype;
		if (target.addEventListener)
			target.addEventListener(tasktype, functionref, false);
		else if (target.attachEvent)
			target.attachEvent(tasktype, functionref);
	},

	cleanup: function() {
		for (var i = 0; i < dhtmlwindow.tobjects.length; i++) {
			dhtmlwindow.tobjects[i].handle._parent = dhtmlwindow.tobjects[i].resizearea._parent = dhtmlwindow.tobjects[i].controls._parent = null;
		}
		window.onload = null;
	}
}; //End dhtmlwindow object

document.write('<div id="dhtmlwindowholder"><span style="display:none">.</span></div>'); //container that holds all dhtml window divs on page
dhtmlwindow.addEvent(window, function() {
	for (var i = 0; i < dhtmlwindow.tobjects.length; i++) {
		var b = dhtmlwindow.tobjects[i];
		if (b.state == "minimized") {
			var minbutton = b.controls.querySelector('img[title="Restore Up"]');
			dhtmlwindow.restoreup(minbutton, b);
			dhtmlwindow.minimize(minbutton, b);
		} else if (b.state == "maximized") {
			var maxbutton = b.controls.querySelector('img[title="Restore Down"]');
			dhtmlwindow.restoredown(maxbutton, b);
			dhtmlwindow.maximize(maxbutton, b);
		} else {
			dhtmlwindow.resizetocontent(b);
		}
	}
}, "resize");
window.onunload = dhtmlwindow.cleanup;