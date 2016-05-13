var menuDiv;
var win;
var timingStr = ""; 
var timingFlag = false;

function setInnerHTML(x,menu) {
    if (menu.length > 0) {
		var startTime = new Date();
      	menuDiv = window.frames["mainFrame"].document.getElementById("menuDiv");
		a = new Date();
		timingStr += "Get Div: " + (a-startTime) + " ms";
		
      	if (win && menuDiv && menuDiv.style) {
      	    
			win.populate(menu);
			b = new Date();
        	win.showPopup(x,0);
			c = new Date();
			timingStr += "\n Populate Existing: " + (b-a) + " ms\n Show Popup: " + (c-b) + " ms";
      }
	  else {
//---------------------------------------------------------------------------
//    Create the menuDiv element dynamically :
//---------------------------------------------------------------------------
		var newElement = window.frames["mainFrame"].document.createElement("div");
        newElement.setAttribute("name", "menuDiv");
        newElement.setAttribute("id", "menuDiv");
        window.frames["mainFrame"].document.body.appendChild(newElement);
		c = new Date();
		timingStr += "\n Create MenuDiv Elem: " + (c-a) + " ms";
        with (window.frames["mainFrame"].document.getElementById("menuDiv").style) {
			position = "absolute";
			backgroundColor = "transparent";
			color = "black";
			width = "auto";
			margin = "-1";
			padding = "0";
			fontFamily = "Arial";
			fontSize = "8pt";
			overflow = "visible";
		}
		c1 = new Date();
		timingStr += "\n - Init Styles: " + (c1-c) + " ms";
		//*
        if (document.all) {
          	with (window.frames["mainFrame"].document.styleSheets[0]) {
				c2 = new Date();
				timingStr += "\n - Create With: " + (c2-c1) + " ms";
            	addRule("#menuDiv ul","margin:0; padding:0; list-style-type:none; width:210px; border:3px outset #fcfcfc; background: #eee;");
				c3 = new Date();
				timingStr += "\n - Add Rule: " + (c3-c2) + " ms";
	  		}
		} //*/
	c3 = new Date();
//---------------------------------------------------------------------------
//    Load menu.css stylesheet dynamically :
//---------------------------------------------------------------------------
	    var css = window.frames["mainFrame"].document.createElement('link');
	    css.rel = 'stylesheet';
	    css.type = 'text/css';
	    css.href = '/itsy/css/menu.css';
	    //window.frames["mainFrame"].document.body.appendChild(css);
		c9 = new Date();
		timingStr += "\n - Add link to menu.css: " + (c9-c3) + " ms";
		d = new Date();
        win = new PopupMenu('menuDiv');
		e = new Date();
		win.autoHide();
		if (!win.listenerAttached) {
			win.listenerAttached = true;
			win.attachListener();
		}
		f = new Date();
        win.populate(menu);
		g = new Date();
        win.showPopup(x,0);
		h = new Date();
        menuDiv = window.frames["mainFrame"].document.getElementById("menuDiv");
		h1 = new Date();
		timingStr += "\n Apply Styles: " + (d-c) + " ms\n Create New Menu: " + (e-d) + " ms";
		timingStr += "\n Attach Listener: " + (f-e) + " ms\n Populate: " + (g-f) + " ms";
		timingStr += "\n Show Popup: " + (h-g) + " ms\n Get Div: " + (h1-h) + " ms";
	  }
	  z = new Date();
	  // Run script to convert unordered list to menus
	  buildsubmenus();
	  // if main menu is too wide and the right edge extends off the screen, move it left
	  var clientWidth = parseInt(window.frames["mainFrame"].document.body.clientWidth,10);
	  var elemWidth = parseInt(menuDiv.offsetWidth,10);
	  if ( (getLeftPos(menuDiv) + elemWidth) > clientWidth ) {
		menuDiv.style.left = (clientWidth - elemWidth - 6) + "px";
	  }
	  
		var endTime = new Date();
		timingStr += "\n Build Submenus: "+(endTime-z)+" ms\nTotal: "+(endTime-startTime) + " ms";
		if (timingFlag) alert(timingStr);
		timingStr = "";
    }
}

var urlStr = "";
function goMenu(url) {
	if (url.charAt(0) == "p") {
		var width = 850;
		var height = 560
		if (url.substr(1).indexOf('CallSystemServlet') > -1) {  // Template Maintenance - TemplateManagerWEB
			width = 950;
			height = 800
		}
		urlStr = "window.open('" + url.substr(1) + "','child','scrollbars=yes,toolbar=no,width=" + width + ",height=" + height + ",resizable=yes')";
		if (win && win.hidePopup) win.hidePopup();
	} else {
		window.frames["mainFrame"].document.body.style.pointer = 'wait';
		urlStr = "window.frames[\"mainFrame\"].document.location.href='" + url + "';";
		if (win && win.hidePopup) win.hidePopup();
		win = null;
		menuDiv = null;
	}
	setTimeout("doEval()",130);
}

function doEval() {
	eval(urlStr);
	urlStr="";
}

/* This function will shrink the width of the given DIV until it can no longer
   shrink, or until the height gets larger and then it will increase the size
   slightly. */
function resizeMenu(menuDivObj) {
// When submenus have no spaces, the width can only be reduced so far
	var origHeight = menuDivObj.offsetHeight;
	var prevWidth = 0;
	var delta = 0;
	while (origHeight == menuDivObj.offsetHeight
				&& menuDivObj.offsetWidth != prevWidth) {
		prevWidth = menuDivObj.offsetWidth;
		delta = Math.ceil(menuDivObj.offsetWidth * 0.12);
		menuDivObj.style.width = (menuDivObj.offsetWidth-delta)+"px";
	}
	(delta<12) ? delta=2 : delta=Math.floor(delta/4);
	while (origHeight != menuDivObj.offsetHeight) {
		menuDivObj.style.width = (menuDivObj.offsetWidth+delta)+"px";
	}
	menuDivObj.style.width = (menuDivObj.offsetWidth+5)+"px";
}
    
function getLeftPos(inputObj) {
    var returnValue = inputObj.offsetLeft;
    while((inputObj = inputObj.offsetParent) != null)
    	returnValue += inputObj.offsetLeft;
    return parseInt(returnValue,10);
}
	

//SuckerTree Vertical Menu 1.1 (Nov 8th, 06)
//By Dynamic Drive: http://www.dynamicdrive.com/style/

var menuids=["suckermenu"] //Enter id(s) of SuckerTree UL menus, separated by commas
var ulTagsAr = new Array(); // Array holding all submenus to hide later.
function buildsubmenus() {
	var menuObj = window.frames["mainFrame"].document.getElementById(menuids[0]);
	resizeMenu(menuObj);
	menuObj.style.zIndex = 4;
	ulTagsAr = new Array();
	var clientWidth = parseInt(window.frames["mainFrame"].document.body.clientWidth,10);
	for (var i=0, menuIdsLen = menuids.length; i<menuIdsLen; i++) {
		var ultags=window.frames["mainFrame"].document.getElementById(menuids[i]).getElementsByTagName("ul")
		var ulTagsLen = ultags.length;
	    for (var t=0; t<ulTagsLen; t++){
			resizeMenu(ultags[t]);
			ultags[t].parentNode.getElementsByTagName("a")[0].className="subfolderstyle"
			var myLeft;
			//if this is a first level submenu
			if (ultags[t].parentNode.parentNode.id == menuids[i]) {
			//dynamically position first level submenus to the right of main menu item
				myLeft = ultags[t].parentNode 
			}
			else { //else if this is a sub level submenu (ul)
			//position menu to the right of menu item that activated it
				var myLeft = ultags[t].parentNode.getElementsByTagName("a")[0];
			}
			var leftEdge = parseInt(getLeftPos(myLeft)) + parseInt(myLeft.offsetWidth);
			var elemWidth = parseInt(ultags[t].offsetWidth);
			if ((leftEdge+elemWidth) < clientWidth) {
				ultags[t].style.left = myLeft.offsetWidth + "px" 
			} else {
				// the submenu needs to be placed to the right of the parent UL item
				ultags[t].style.left = (-elemWidth) + "px";
			}
			ultags[t].style.zIndex = t+5;
		    ultags[t].parentNode.onmouseover=function(e){
				this.getElementsByTagName("ul")[0].isMouseOver=true;
		    	this.getElementsByTagName("ul")[0].style.display="block"
				this.style.backgroundColor = "Highlight";
				this.getElementsByTagName("a")[0].style.color = "white"; //"HighlightText";
				this.getElementsByTagName("a")[0].style.backgroundImage = "url(/itsy/images/ArrowPopupSel.gif)";
				hideAllOther(this);
				if(this.toref) window.clearTimeout(this.toref);
		    }
		    ultags[t].parentNode.onmouseout=function(e){
				this.style.backgroundColor = "#eee";
				this.getElementsByTagName("a")[0].style.color = "black";
				this.getElementsByTagName("a")[0].style.backgroundImage = "url(/itsy/images/ArrowPopup.gif)";

				if (this.parentNode.id == "suckermenu") {
			        var saveref=this.getElementsByTagName("ul")[0];
					saveref.isMouseOver=false;
					if(this.toref) window.clearTimeout(this.toref);
						this.toref=window.setTimeout(function(e){
							if(!saveref.isMouseOver) saveref.style.display="none"},240);
				}
				else
					this.getElementsByTagName("ul")[0].style.display="none"
			}
    	}
		for (var t=ulTagsLen-1; t>-1; t--) { 
			//loop through all sub menus again, and use "display:none" to hide menus 
			//(to prevent possible page scrollbars)
			ultags[t].style.visibility="visible"
			ultags[t].style.display="none";
		}
	}
}

function hideAllOther(liobj) {
  var p=liobj.parentNode.childNodes;
  var ulCnt = 0;
  var inx = 0;
  try {
	for (inx=0, pLen=p.length; inx<pLen; inx++) {
	  ulCnt = p[inx].getElementsByTagName("ul").length;
	  if (liobj != p[inx] && "LI"==p[inx].tagName) {
	    for (var jnx=0; jnx<ulCnt; jnx++) {
	      p[inx].getElementsByTagName("ul")[jnx].style.display="none";
	    }
	  }
   	}
  } catch(e) {
    // alert(p[inx].innerHTML)
  }
}