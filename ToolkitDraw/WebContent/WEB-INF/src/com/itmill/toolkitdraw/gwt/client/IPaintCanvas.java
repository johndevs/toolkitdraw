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
	   
	public IPaintCanvas(int pageWidth, int pageHeight, String bgColor){		
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
				"	<embed id='"+id+"' width='100%' height='98%' flashvars='width="+w+"&height="+h+"&bgcolor="+bgColor+"' src='"+IPaintCanvas.LOCATION+"/PaintCanvas.swf' wmode='transparent' allowscriptaccess='always' quality='high' play='true' bgcolor='#FFFFFF'>"+
				"	</embed>"+
				"</object>"	)	;
		
		setSize("100%", "100%");
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
	
	private native void drawPolygon(String id, int[]x, int[]y)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsDrawPolygon == 'function'){
			canvas.graphicsDrawPolygon(x.slice(), y.slice(), x.length);
		}else{
			var func = function() { canvas.graphicsDrawPolygon(x.slice(), y.slice(), x.length); };		
		setTimeout(func,1000);
		}		
	}-*/;

	private native void setComponentBackground(String id, String color)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setComponentBackgroundColor == 'function'){
			canvas.setComponentBackgroundColor(color);
		}else{
			var func = function() { canvas.setComponentBackgroundColor(color); };		
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
     
        String[] commands = uidl.getStringArrayVariable("commands");
        String[] values = uidl.getStringArrayVariable("values");
        
        if(commands.length != values.length){
        	error("Transmission error!");
        }
        
        for(int i=0; i<commands.length; i++)
        {
        	//Execute the operations the server wants to do		
    		if(commands[i].equals("height"))			setHeight(values[i]);
    		else if(commands[i].equals("width"))		setWidth(values[i]);
    		else if(commands[i].equals("undo")) 		undo(this.id);
    		else if(commands[i].equals("redo")) 		redo(this.id);
    		else if(commands[i].equals("newlayer")) 	addLayer(this.id, values[i]);
    		else if(commands[i].equals("paperWidth"))	setPaperWidth(this.id, Integer.parseInt(values[i]));
    		else if(commands[i].equals("paperHeight"))	setPaperHeight(this.id, Integer.parseInt(values[i]));
    		else if(commands[i].equals("penSize")){
    			setPenSize(this.id, Double.parseDouble(values[i]));
    		}
    		else if(commands[i].equals("penColor"))		setPenColor(this.id, values[i]);
    		else if(commands[i].equals("brush"))		setBrush(this.id, values[i]);
    		else if(commands[i].equals("fillColor"))	setFillColor(this.id, values[i]);
    		else if(commands[i].equals("showLayer"))	setLayerVisibility(this.id, values[i], true);
    		else if(commands[i].equals("hideLayer"))	setLayerVisibility(this.id, values[i], false);
    		else if(commands[i].equals("activeLayer"))	selectLayer(this.id, values[i]);
    		else if(commands[i].equals("getImageXML")){
    			//Feth the xml from the flash compoent
    			String xml = getImageXML(this.id);
    			
    			//Send the result back to the server
    			client.updateVariable(uidlId, "getImageXML", xml, true);		
    		}
    		else if(commands[i].equals("interactive"))	setInteractive(this.id, Boolean.valueOf(values[i]));
    		else if(commands[i].equals("graphics-clear"))	clear(this.id);
    		else if(commands[i].equals("graphics-line")){
    			String[] coords = values[i].split(",");	
    			drawLine(this.id, 	Integer.valueOf(coords[0]), 
    								Integer.valueOf(coords[1]), 
    								Integer.valueOf(coords[2]), 
    								Integer.valueOf(coords[3]));    			
    		}
    		else if(commands[i].equals("graphics-square")){
    			String[] coords = values[i].split(",");		
				drawSquare(this.id, Integer.valueOf(coords[0]), 
									Integer.valueOf(coords[1]), 
									Integer.valueOf(coords[2]), 
									Integer.valueOf(coords[3]));	
    		}
    		else if(commands[i].equals("graphics-polygon")){
    			String[] coords = values[i].split(";");
    			String[] x = coords[0].split(",");
    			String[] y = coords[1].split(",");
    			
    			if(x.length == y.length && x.length > 0){
    				int[]xi = new int[x.length];    			
        			int[]yi = new int[y.length];
        			
        			for(int j=0; j<x.length; j++){
        				xi[j] = Integer.parseInt(x[j]);
        				yi[j] = Integer.parseInt(y[j]);
        			}        		
        			
        			drawPolygon(this.id, xi, yi);
    			}   			
    		}
    		else if(commands[i].equals("componentColor")){
    			setComponentBackground(this.id, values[i]);
    		}    		
    		else	error("No command \""+commands[i]+"\" found!");
        }       
	}
	

}
