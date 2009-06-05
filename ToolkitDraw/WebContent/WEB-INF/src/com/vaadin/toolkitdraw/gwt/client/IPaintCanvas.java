package com.vaadin.toolkitdraw.gwt.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;



public class IPaintCanvas extends HTML implements Paintable {

	private static String LOCATION = "ToolkitDraw/VAADIN/widgetsets/com.vaadin.toolkitdraw.gwt.PaintCanvasWidgetSet/paintcanvas";
	
	/** The identifier of the embedded Flash plugin **/
	private String id = "";
	
	/** Component identifier in UIDL communications. */   
	private String uidlId;

    /** Reference to the server connection object. */
    private ApplicationConnection client;	
    	    
	public IPaintCanvas(String width, String height, int pageWidth, int pageHeight, String bgColor){		
		super();				
		
		id = DOM.createUniqueId();		
		
		this.getElement().setId(id+"-canvas");
				
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
				"	<param name='flashvars' value=\"id="+id+"&width="+w+"&height="+h+"\" />"+
				"   <param name='wmode' value='transparent' />"+				
				"	<embed id='"+id+"' width='100%' height='98%' flashvars='id="+id+"&width="+w+"&height="+h+"&bgcolor="+bgColor+"' src='"+IPaintCanvas.LOCATION+"/PaintCanvas.swf' wmode='transparent' allowscriptaccess='always' quality='high' play='true' bgcolor='"+bgColor+"'>"+
				"	</embed>"+
				"</object>"	)	;
			
		setSize(width, height);
		setStyleName("paintcanvas");						
	}
		
	private native void undo(String id) /*-{
		var canvas = $wnd.document.getElementById(id);		
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.undo == 'function' && canvas.isReady() ){
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
		if(typeof canvas.redo == 'function' && canvas.isReady() ){
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
		if(typeof canvas.setPaperHeight == 'function' && canvas.isReady() ){
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
		if(typeof canvas.setPaperWidth == 'function' && canvas.isReady() ){
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
		if(typeof canvas.setBrushWidth == 'function' && canvas.isReady() ){
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
		if(typeof canvas.setBrushColor == 'function' && canvas.isReady() ){
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
		if(typeof canvas.setBrush == 'function' && canvas.isReady() ){
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
		if(typeof canvas.setFillColor == 'function' && canvas.isReady() ){
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
		if(typeof canvas.addNewLayer == 'function' && canvas.isReady() ){
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
		if(typeof canvas.setLayerVisibility == 'function' && canvas.isReady() ){
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
		if(typeof canvas.selectLayer == 'function' && canvas.isReady() ){
			canvas.selectLayer(name);
		}else{
			var func = function() { canvas.selectLayer(name); };		
			setTimeout(func,1000);
		}			
	}-*/;
	
	private native void setLayerColor(String id, String color)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setLayerBackgroundColor == 'function' && canvas.isReady() ){
			canvas.setLayerBackgroundColor(color);
		}else{
			var func = function() { canvas.setLayerBackgroundColor(color); };		
			setTimeout(func,1000);
		}			
	}-*/;
	
	private native void setLayerAlpha(String id, double alpha)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setLayerBackgroundAlpha == 'function' && canvas.isReady() ){
			canvas.setLayerBackgroundAlpha(alpha);
		}else{
			var func = function() { canvas.setLayerBackgroundAlpha(alpha); };		
			setTimeout(func,1000);
		}			
	}-*/;
	
	private native String getImageXML(String id)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		return canvas.getImageXML();
	}-*/;
	
	private native String getImagePNG(String id, int dpi)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		return canvas.getImagePNG(dpi);
	}-*/;
	
	private native String getImageJPG(String id, int dpi)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		return canvas.getImageJPG(dpi);
	}-*/;
	
	private native String setInteractive(String id, boolean interactive)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setInteractive == 'function' && canvas.isReady() ){
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
		if(typeof canvas.graphicsDrawLine == 'function' && canvas.isReady() ){
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
		if(typeof canvas.graphicsDrawSquare == 'function' && canvas.isReady() ){
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
		if(typeof canvas.graphicsClear == 'function' && canvas.isReady() ){
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
		if(typeof canvas.graphicsDrawPolygon == 'function' && canvas.isReady() ){
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
		if(typeof canvas.setComponentBackgroundColor == 'function' && canvas.isReady() ){
			canvas.setComponentBackgroundColor(color);
		}else{
			var func = function() { canvas.setComponentBackgroundColor(color); };		
		setTimeout(func,1000);
		}		
	}-*/;
	
	private native boolean isReady(String id)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) return false;	
	
		if(typeof canvas.isReady == 'function' && canvas.isReady() ){
			return true;
		}
		
		return false;		
	}-*/;	
	
	
	private void executeCommand(String id, String command, String value){
		
		//Execute the operations the server wants to do		
		if(command.equals("height")){		
			setHeight(value);
		}
		else if(command.equals("width")){		
			setWidth(value);
		}
		else if(command.equals("undo")) 		undo(id);
		else if(command.equals("redo")) 		redo(id);
		else if(command.equals("newlayer")) 	addLayer(id, value);
		else if(command.equals("paperWidth"))	setPaperWidth(id, Integer.parseInt(value));
		else if(command.equals("paperHeight"))	setPaperHeight(id, Integer.parseInt(value));
		else if(command.equals("penSize")){
			setPenSize(id, Double.parseDouble(value));
		}
		else if(command.equals("penColor"))		setPenColor(id, value);
		else if(command.equals("brush"))		setBrush(id, value);
		else if(command.equals("fillColor"))	setFillColor(id, value);
		else if(command.equals("showLayer"))	setLayerVisibility(id, value, true);
		else if(command.equals("hideLayer"))	setLayerVisibility(id, value, false);
		else if(command.equals("activeLayer"))	selectLayer(id, value);
		else if(command.equals("getImageXML")){
			//Feth the xml from the flash compoent
			String xml = getImageXML(id);
			
			//Send the result back to the server
			client.updateVariable(uidlId, "getImageXML", xml, true);		
		}
		else if(command.equals("getImagePNG")){
			//Fetch the base64 png image from the component
			String base64 = getImagePNG(id, Integer.valueOf(value));
			
			//Send the result back to the server
			client.updateVariable(uidlId, "getImagePNG", base64, true);
		}    	
		
		else if(command.equals("getImageJPG")){
			//Fetch the base64 png image from the component
			String base64 = getImageJPG(id, Integer.valueOf(value));
			
			//Send the result back to the server
			client.updateVariable(uidlId, "getImageJPG", base64, true);
		}    	
		
		else if(command.equals("interactive"))	setInteractive(id, Boolean.valueOf(value));
		else if(command.equals("graphics-clear"))	clear(id);
		else if(command.equals("graphics-line")){
			String[] coords = value.split(",");	
			drawLine(id, 	Integer.valueOf(coords[0]), 
								Integer.valueOf(coords[1]), 
								Integer.valueOf(coords[2]), 
								Integer.valueOf(coords[3]));    			
		}
		else if(command.equals("graphics-square")){
			String[] coords = value.split(",");		
			drawSquare(id, Integer.valueOf(coords[0]), 
								Integer.valueOf(coords[1]), 
								Integer.valueOf(coords[2]), 
								Integer.valueOf(coords[3]));	
		}
		else if(command.equals("graphics-polygon")){
			String[] coords = value.split(";");
			String[] x = coords[0].split(",");
			String[] y = coords[1].split(",");
			
			if(x.length == y.length && x.length > 0){
				int[]xi = new int[x.length];    			
    			int[]yi = new int[y.length];
    			
    			for(int j=0; j<x.length; j++){
    				xi[j] = Integer.parseInt(x[j]);
    				yi[j] = Integer.parseInt(y[j]);
    			}        		
    			
    			drawPolygon(id, xi, yi);
			}   			
		}
		else if(command.equals("componentColor")){
			setComponentBackground(id, value);
		}    		
		else if(command.equals("layercolor")){    		
			setLayerColor(id, value); 
		}
		else if(command.equals("layeralpha")){
			setLayerAlpha(id, Double.parseDouble(value));
		}    		
		
		else	error("No command \""+command+"\" found!");		
	}
	
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
        
      //check that all values came with the transmission
        if(commands.length != values.length){
        	error("Transmission error!");
        	return;
        }
        
        //Check if plugin is ready, this will happen att initial call       
        if(!isReady(this.id)){         	
        	        	
        	//Try to run the commands after one second
        	final UIDL u = uidl;
        	final ApplicationConnection conn = client;
        	Timer t = new Timer(){			
        		public void run() { updateFromUIDL(u, conn); }        		
        	};
        	
        	t.schedule(100);          	
        	
        } else {        	       	
        	
        	//execute commands
        	 for(int i=0; i<commands.length; i++)
        		 executeCommand(this.id, commands[i], values[i]);
        }        
        	
	}
	

}
