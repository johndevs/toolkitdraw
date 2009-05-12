package com.itmill.toolkitdraw.gwt.client;


import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;


import com.itmill.toolkit.terminal.gwt.client.ApplicationConnection;
import com.itmill.toolkit.terminal.gwt.client.Paintable;
import com.itmill.toolkit.terminal.gwt.client.UIDL;



public class IPaintCanvas extends HTML implements Paintable {

	private static String LOCATION = "ToolkitDraw/ITMILL/widgetsets/com.itmill.toolkitdraw.gwt.PaintCanvasWidgetSet/paintcanvas";
	private String id = "";
	
	/** Component identifier in UIDL communications. */   
	private String uidlId;

    /** Reference to the server connection object. */
    private ApplicationConnection client;	
	   
	public IPaintCanvas(int pageWidth, int pageHeight){		
		super();		
				
		id = DOM.createUniqueId();
		
		//If either of the heights are negative then make papersize fullscrenn
		String w = String.valueOf(pageWidth);
		String h = String.valueOf(pageHeight);
		if(pageWidth < 0 || pageHeight < 0){
			w = "100%";
			h = "100%";
		}	
		
		setHTML("<object width='100%' height='100%'>"+
				"	<param name='movie' value='"+IPaintCanvas.LOCATION+"/PaintCanvas.swf'>"+
				"	<param name='allowScriptAccess' value='always'/>"+	
				"	<param name='flashvars' value=\"width="+w+"&height="+h+"\" />"+
				"   <param name='wmode' value='transparent' />"+
				"	<embed id='"+id+"' width='100%' height='100%' flashvars='width="+w+"&height="+h+"' src='"+IPaintCanvas.LOCATION+"/PaintCanvas.swf' wmode='transparent' allowscriptaccess='always' quality='high' play='true' bgcolor='#FFFFFF'>"+
				"	</embed>"+
				"</object>"	)	;
		
		setSize("100%", "99%");
		setStyleName("paintcanvas");			
	}
		
	private native void undo(String id) /*-{
		var canvas = $wnd.document.getElementById(id);		
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.undo == 'function'){
			canvas.undo();	
		}else{
			var func = function() { canvas.undo(); };		
			setTimeout(func,1000);
		}				
	}-*/;
	
	private native void redo(String id) /*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.redo == 'function'){
			canvas.redo();	
		}else{
			var func = function() { canvas.redo(); };		
			setTimeout(func,1000);
		}							
	}-*/;
	
	private native void setPaperHeight(String id, int height)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
			
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setPaperHeight == 'function'){
			canvas.setPaperHeight(height);	
		}else{
			var func = function() { canvas.setPaperHeight(height); };		
			setTimeout(func,1000);
		}							
	}-*/;
	
	private native void setPaperWidth(String id, int width)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setPaperWidth == 'function'){
			canvas.setPaperWidth(width);
		}else{
			var func = function() { canvas.setPaperWidth(width); };		
			setTimeout(func,1000);
		}				
	}-*/;	
	
	private native void error(String message)/*-{
		alert(message);
	}-*/;
	
	private native void setPenSize(String id, double s)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setBrushWidth == 'function'){
			canvas.setBrushWidth(s);
		}else{
			var func = function() { canvas.setBrushWidth(s); };		
			setTimeout(func,1000);
		}				
	}-*/;	
	
	private native void setPenColor(String id, String color)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setBrushColor == 'function'){
			canvas.setBrushColor(color);
		}else{
			var func = function() { canvas.setBrushColor(color); };		
			setTimeout(func,1000);
		}			
	}-*/;
	
	private native void setBrush(String id, String brush)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setBrush == 'function'){
			canvas.setBrush(brush);	
		}else{
			var func = function() { canvas.setBrush(brush);	 };		
			setTimeout(func,1000);
		}				
	}-*/;
	
	private native void setFillColor(String id, String color)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setFillColor == 'function'){
			canvas.setFillColor(color);	
		}else{
			var func = function() { canvas.setFillColor(color);	};		
			setTimeout(func,1000);
		}				
	}-*/;
	
	private native void addLayer(String id, String name)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.addNewLayer == 'function'){
			canvas.addNewLayer(name);
		}else{
			var func = function() { canvas.addNewLayer(name); };		
			setTimeout(func,1000);
		}			
	
	}-*/;
	
	private native void setLayerVisibility(String id, String name, boolean visibility)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setLayerVisibility == 'function'){
			canvas.setLayerVisibility(name,visibility);
		}else{
			var func = function() { canvas.setLayerVisibility(name,visibility); };		
			setTimeout(func,1000);
		}		
	}-*/;
	
	private native void selectLayer(String id, String name)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.selectLayer == 'function'){
			canvas.selectLayer(name);
		}else{
			var func = function() { canvas.selectLayer(name); };		
			setTimeout(func,1000);
		}			
	}-*/;
	
	private native String getImageXML(String id)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		return canvas.getImageXML();
	}-*/;
	
	private native String setInteractive(String id, boolean interactive)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setInteractive == 'function'){
			canvas.setInteractive(interactive);
		}else{
			var func = function() { canvas.setInteractive(interactive); };		
			setTimeout(func,1000);
		}				
	}-*/;
	
	private native void drawLine(String id, int x1, int y1, int x2, int y2)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsDrawLine == 'function'){
			canvas.graphicsDrawLine(x1,y1,x2,y2);
		}else{
			var func = function() { canvas.graphicsDrawLine(x1,y1,x2,y2); };		
		setTimeout(func,1000);
		}		
	}-*/;
	
	private native void drawSquare(String id, int x, int y, int width, int height)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsDrawSquare == 'function'){
			canvas.graphicsDrawSquare(x,y,width,height);
		}else{
			var func = function() { canvas.graphicsDrawSquare(x,y,width,height); };		
		setTimeout(func,1000);
		}		
	}-*/;
	
	private native void clear(String id)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsClear == 'function'){
			canvas.graphicsClear();
		}else{
			var func = function() { canvas.graphicsClear(); };		
		setTimeout(func,1000);
		}		
	}-*/;
	
	 /**
    * This method must be implemented to update the client-side component from
    * UIDL data received from server.
    * 
    * This method is called when the page is loaded for the first time, and
    * every time UI changes in the component are received from the server.
    */
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {

		// This call should be made first. Ensure correct implementation,
        // and let the containing layout manage caption, etc.
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        // Save reference to server connection object to be able to send
        // user interaction later
        this.client = client; 
        
        // Save the UIDL identifier for the component        
        uidlId = uidl.getId();        
        
        //Execute the operations the server wants to do		
		if(uidl.hasVariable("undo")) 
			undo(this.id);
		if(uidl.hasVariable("redo")) 
			redo(this.id);
		if(uidl.hasVariable("newlayer")) 
			addLayer(this.id, uidl.getStringVariable("newlayer"));
		if(uidl.hasVariable("width")) 
			setWidth(uidl.getStringVariable("width"));
		if(uidl.hasVariable("height")) 
			setHeight( uidl.getStringVariable("height"));
		if(uidl.hasVariable("paperWidth")) 
			setPaperWidth(this.id, Integer.parseInt(uidl.getStringVariable("paperWidth")));
		if(uidl.hasVariable("paperHeight"))
			setPaperHeight(this.id, Integer.parseInt(uidl.getStringVariable("paperHeight")));
		if(uidl.hasVariable("penSize"))
			setPenSize(this.id, Double.parseDouble(uidl.getStringVariable("penSize")));
		if(uidl.hasVariable("penColor"))
			setPenColor(this.id, uidl.getStringVariable("penColor"));
		if(uidl.hasVariable("brush"))
			setBrush(this.id, uidl.getStringVariable("brush"));
		if(uidl.hasVariable("fillColor"))
			setFillColor(this.id, uidl.getStringVariable("fillColor"));		
		if(uidl.hasVariable("showLayer"))
			setLayerVisibility(this.id,uidl.getStringVariable("showLayer"), true);
		if(uidl.hasVariable("hideLayer"))
			setLayerVisibility(this.id,uidl.getStringVariable("hideLayer"), false);
		if(uidl.hasVariable("activeLayer"))
			selectLayer(this.id, uidl.getStringVariable("activeLayer"));
		if(uidl.hasVariable("getImageXML")){			
			//Feth the xml from the flash compoent
			String xml = getImageXML(this.id);
			
			//Send the result back to the server
			client.updateVariable(uidlId, "getImageXML", xml, true);			
		}
		if(uidl.hasVariable("interactive")){
			boolean i = Boolean.valueOf(uidl.getStringVariable("interactive"));
			setInteractive(this.id, i);
		}
		if(uidl.hasVariable("graphics-clear")){
			clear(this.id);
		}
		if(uidl.hasVariable("graphics-line")){			
			for(String line : uidl.getStringVariable("graphics-line").split(";")){
				String[] values = line.split(",");		
				drawLine(this.id, Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2]), Integer.valueOf(values[3]));	
			}								
		}
		if(uidl.hasVariable("graphics-square")){
			for(String square : uidl.getStringVariable("graphics-square").split(";")){
				String[] values = square.split(",");		
				drawSquare(this.id, Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2]), Integer.valueOf(values[3]));	
			}			
		}
		
			
	}
	

}
