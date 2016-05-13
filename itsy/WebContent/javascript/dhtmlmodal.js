// -------------------------------------------------------------------
// DHTML Modal window- By Dynamic Drive, available at: http://www.dynamicdrive.com
// v1.1: April 16th, 08'
// REQUIRES: DHTML Window Widget (v1.01 or higher)
// -------------------------------------------------------------------

if (typeof dhtmlwindow == "undefined")
	alert('ERROR: Modal Window script requires all files from "DHTML Window widget" in order to work!');

var dhtmlmodal = {
	veilstack: 0,
    veilIndexes: [],
	modalWindows: [],

	open: function(t, contenttype, contentsource, title, attr, recalonload) {
		var d = dhtmlwindow; //reference dhtmlwindow object
		this.interVeil = document.getElementById("interVeil"); //Reference "veil" div

		var winHandle = null;
		if (document.getElementById(t) == null) //if window doesn't exist yet, create it
			winHandle = d.init(t); //return reference to dhtml window div
		else
			winHandle= document.getElementById(t);

		if (this.modalWindows.indexOf(winHandle) == -1) {
			this.veilstack++; //var to keep track of how many modal windows are open right now
			this.loadveil();
		}

		if (recalonload == "recal" && d.scroll_top == 0)
			d.addEvent(window, function() {
				dhtmlmodal.adjustveil();
			}, "load");
		var w = d.open(t, contenttype, contentsource, title, attr, recalonload);

		if (this.modalWindows.indexOf(winHandle) == -1) {
			this.modalWindows[this.modalWindows.length] = w;
		}

		w.controls.getElementsByTagName('img')[0].style.visibility = 'hidden';
		w.controls.getElementsByTagName('img')[2].onclick = function() { // on click of the 'X' click on the cancel event in the dom
			if (w.contentarea.getElementsByTagName("iframe")[0].contentWindow.document.getElementById('btCancel') != null) {
				w.contentarea.getElementsByTagName("iframe")[0].contentWindow.document.getElementById('btCancel').click(); // on click of the 'X' click on the cancel event in the dom //d.close(this._parent);
			} else {
				dhtmlmodal.close(w, true);
			}
		}; //OVERWRITE default control action with new one
		w.show = function() {
			dhtmlmodal.show(this);
		}; //OVERWRITE default t.show() method with new one
		w.hide = function() {
			dhtmlmodal.close(this);
		}; //OVERWRITE default t.hide() method with new one
		return w;
	},

	loadveil: function(onscreen) {
		var d = dhtmlwindow;
		d.getviewpoint();
		var highestZ = '';
		if (typeof onscreen == "undefined")
			highestZ = findHighestZIndex();
		this.docheightcomplete = (d.standardbody.offsetHeight > d.standardbody.scrollHeight) ? d.standardbody.offsetHeight : d.standardbody.scrollHeight;
		this.interVeil.style.width = "100%"; //d.docwidth + "px"; //set up veil over page
		this.interVeil.style.height = this.docheightcomplete + "px"; //set up veil over page
		this.interVeil.style.left = 0; //Position veil over page
		this.interVeil.style.top = 0; //Position veil over page
		if (typeof onscreen == "undefined") {
			this.interVeil.style.zIndex = ++highestZ; //Show veil over page
			if (this.veilstack > this.veilIndexes.length) {
				this.veilIndexes[this.veilIndexes.length] = highestZ;
			}
		}
		this.interVeil.style.visibility = "visible"; //Show veil over page
		this.interVeil.style.display = "block"; //Show veil over page
	},

	adjustveil: function() { //function to adjust veil when window is resized
		if (this.interVeil && this.interVeil.style.display == "block") //If veil is currently visible on the screen
			this.loadveil(true); //readjust veil
	},

	closeveil : function(t) { // function to close veil
		if (this.modalWindows.indexOf(t) != -1) {
			this.modalWindows.splice(this.modalWindows.indexOf(t), 1);
			if (this.veilstack > 1) {
				this.veilstack--;
				this.veilIndexes.splice(this.veilIndexes.length - 1, 1);
				if (this.veilIndexes.length == 1) { // this.veilstack--;
					this.interVeil.style.zIndex = this.veilIndexes[this.veilIndexes.length - 1]; // this.interVeil.style.display = "none";
				} else {
					this.interVeil.style.zIndex = this.veilIndexes[this.veilIndexes.length - 2];
				}
			} else {
				this.veilstack--;
				this.veilIndexes.splice(this.veilIndexes.length - 1, 1);
				if (this.veilstack == 0) // if this is the only modal window visible on the screen, and being closed
					this.interVeil.style.display = "none";
			}
		}
	},

	close: function(t, forceclose) { //DHTML modal close function
		t.contentDoc = (t.contentarea.datatype == "iframe") ? window.frames["_iframe-" + t.id].document : t.contentarea; //return reference to modal window DIV (or document object in the case of iframe
		if (typeof forceclose != "undefined")
			t.onclose = function() {
				return true;
			};
		if (dhtmlwindow.close(t)) //if close() returns true
			this.closeveil(t);
	},

	show: function(t) {
		if (this.modalWindows.indexOf(t) == -1) {
			dhtmlmodal.veilstack++;
			dhtmlmodal.loadveil();
			this.modalWindows[this.modalWindows.length] = t;
		}
		dhtmlwindow.show(t);
	}
}; //END object declaration

document.write('<div id="interVeil"></div>');
dhtmlwindow.addEvent(window, function() {
	if (typeof dhtmlmodal != "undefined") dhtmlmodal.adjustveil();
}, "resize");