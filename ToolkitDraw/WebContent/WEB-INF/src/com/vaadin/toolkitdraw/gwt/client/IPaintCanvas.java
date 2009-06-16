package com.vaadin.toolkitdraw.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;



public class IPaintCanvas extends HTML implements Paintable {

	/** The identifier of the embedded Flash plugin **/
	private String id = "";
	
	/** Component identifier in UIDL communications. */   
	private String uidlId;

    /** Reference to the server connection object. */
    private ApplicationConnection client;	
    
    /** Relative path to the executable Flash **/ 
    public static String SWFPATH = "paintcanvas/PaintCanvas.swf";
    
    /** Indicates if the flash has been inited and is ready to be used **/
    private boolean ready = false;
    
    /** Indicates if the initial UIDL has been processed **/
    private boolean init = false;
    
	public IPaintCanvas(){		
		super();				
		
		this.id = DOM.createUniqueId();			
		this.getElement().setId(id+"-canvas");
		
		setSize("100%", "100%");
		setStyleName("paintcanvas");
		
		//Ensure that the methods are also available in javascript
		PaintCanvasNativeUtil.defineBridgeMethods();
		
		//Register the canvas with the native util
		PaintCanvasNativeUtil.registerCanvas(this);
		
	}
			
	/**
	 * Exectute a command on the canvas.
	 * @param command
	 * 		The command to be executed
	 * @param value
	 * 		The parameters for the command
	 */
	private void executeCommand(String command, String value){
		
		//Execute the operations the server wants to do		
		if(command.equals("height")){		
			setHeight(value);
		}
		else if(command.equals("width")){		
			setWidth(value);
		}
		else if(command.equals("undo")) 		PaintCanvasNativeUtil.undo(id);
		else if(command.equals("redo")) 		PaintCanvasNativeUtil.redo(id);
		else if(command.equals("newlayer")) 	PaintCanvasNativeUtil.addLayer(id, value);
		else if(command.equals("paperWidth"))	PaintCanvasNativeUtil.setPaperWidth(id, Integer.parseInt(value));
		else if(command.equals("paperHeight"))	PaintCanvasNativeUtil.setPaperHeight(id, Integer.parseInt(value));
		else if(command.equals("penSize")){
			PaintCanvasNativeUtil.setPenSize(id, Double.parseDouble(value));
		}
		else if(command.equals("penColor"))		PaintCanvasNativeUtil.setPenColor(id, value);
		else if(command.equals("brush"))		PaintCanvasNativeUtil.setBrush(id, value);
		else if(command.equals("fillColor"))	PaintCanvasNativeUtil.setFillColor(id, value);
		else if(command.equals("showLayer"))	PaintCanvasNativeUtil.setLayerVisibility(id, value, true);
		else if(command.equals("hideLayer"))	PaintCanvasNativeUtil.setLayerVisibility(id, value, false);
		else if(command.equals("activeLayer"))	PaintCanvasNativeUtil.selectLayer(id, value);
		else if(command.equals("getImageXML")){
			//Feth the xml from the flash compoent
			String xml = PaintCanvasNativeUtil.getImageXML(id);
			
			//Send the result back to the server
			client.updateVariable(uidlId, "getImageXML", xml, true);		
		}
		else if(command.equals("getImagePNG")){
			//Fetch the base64 png image from the component
			String base64 = PaintCanvasNativeUtil.getImagePNG(id, Integer.valueOf(value));
			
			//Send the result back to the server
			client.updateVariable(uidlId, "getImagePNG", base64, true);
		}    	
		
		else if(command.equals("getImageJPG")){
			//Fetch the base64 png image from the component
			String base64 = PaintCanvasNativeUtil.getImageJPG(id, Integer.valueOf(value));
			
			//Send the result back to the server
			client.updateVariable(uidlId, "getImageJPG", base64, true);
		}    	
		
		else if(command.equals("interactive"))	PaintCanvasNativeUtil.setInteractive(id, Boolean.valueOf(value));
		else if(command.equals("graphics-clear"))	PaintCanvasNativeUtil.clear(id);
		else if(command.equals("graphics-line")){
			String[] coords = value.split(",");	
			PaintCanvasNativeUtil.drawLine(id, 	Integer.valueOf(coords[0]), 
												Integer.valueOf(coords[1]), 
												Integer.valueOf(coords[2]), 
												Integer.valueOf(coords[3]));    			
		}
		else if(command.equals("graphics-square")){
			String[] coords = value.split(",");		
			PaintCanvasNativeUtil.drawSquare(id, Integer.valueOf(coords[0]), 
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
    			
    			PaintCanvasNativeUtil.drawPolygon(id, xi, yi);
			}   			
		}
		else if(command.equals("componentColor")){
			PaintCanvasNativeUtil.setComponentBackground(id, value);
		}    		
		else if(command.equals("layercolor")){    		
			PaintCanvasNativeUtil.setLayerColor(id, value); 
		}
		else if(command.equals("layeralpha")){
			PaintCanvasNativeUtil.setLayerAlpha(id, Double.parseDouble(value));
		}    		
		
		else	PaintCanvasNativeUtil.error("No command \""+command+"\" found!");		
	}
	
	/**
	 * This method creates the embedded Flash component when loading is complete
	 * 
	 */
	private void createFlashComponent(String url, String pageWidth, String pageHeight, String bgColor){
		
		//Create the embedded object
		//TODO Use SwfObject instead
		setHTML("<object width='100%' height='100%'>"+
				"	<param name='movie' value='"+url+"'>"+
				"	<param name='allowScriptAccess' value='always'/>"+	
				"	<param name='flashvars' value=\"id="+this.id+"&width="+pageWidth+"&height="+pageHeight+"\" />"+
				"   <param name='wmode' value='transparent' />"+				
				"	<embed id='"+id+"' width='100%' height='98%' flashvars='id="+id+"&width="+pageWidth+"&height="+pageHeight+"&bgcolor="+bgColor+"' src='"+url+"' wmode='transparent' allowscriptaccess='always' quality='high' play='true' bgcolor='"+bgColor+"'>"+
				"	</embed>"+
				"</object>")	;
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
        
        //Parse the commands and their values from the UIDL
        String[] commands = uidl.getStringArrayVariable("commands");
        String[] values = uidl.getStringArrayVariable("values");       
        
        //check that all values came with the transmission
        if(commands.length != values.length){
        	PaintCanvasNativeUtil.error("Transmission error!");
        	return;
        }
                
        //check if the plugin has been added
        if(!init){
        
        	//Check that the uidl contains all needed info
        	String pageWidth = null;
        	String pageHeight = null;
        	String bgColor = null;
        	for(int c=0; c<commands.length; c++){
        		if(commands[c].equals("paperWidth"))
        			pageWidth = values[c];
        		else if(commands[c].equals("paperHeight"))
        			pageHeight = values[c];        		
        		else if(commands[c].equals("componentColor"))
        			bgColor = values[c];
        	}
        	
        	//If all info is recieved the init the flash
        	if(pageWidth != null && pageHeight != null && bgColor != null ){
        		//Add the SWF path
        		String url = GWT.getModuleBaseURL() + SWFPATH;	       		           	
            	createFlashComponent(url, pageWidth, pageHeight, bgColor);
            	init = true;       		
        	}       	
        }                
       
        
        //Check if plugin is ready, this will happen at initial call    
        //Ready state is set by the flash when it has completely loaded
        if(!ready){           	
        	
        	//Try to run the commands after one second if the plugin is not ready
        	final UIDL u = uidl;
        	final ApplicationConnection conn = client;
        	Timer t = new Timer(){			
        		public void run() { updateFromUIDL(u, conn); }        		
        	};
        	
        	t.schedule(100);       	
        	
        } else {        
        	
        	//execute commands
        	 for(int i=0; i<commands.length; i++)
        		 executeCommand(commands[i], values[i]);
        }        
        	
	}
	
	/**
	 * Returns the id of this canvas. This can be used to recognice the canvas
	 * @return
	 * 		Unique id of the canvas
	 */
	public String getId(){
		return this.id;
	}
	
	/**
	 * This function set the ready state of the Flash component
	 * This is called by the PaintCanvasNativeUtil when the Flash reports it is ready
	 * @param ready
	 */
	public void setReady(boolean ready){
		this.ready = ready;
		
		client.updateVariable(this.uidlId, "readyStatus", ready, true);
	}
	
	/**
	 * Returns Flash component status
	 * @return
	 * 		Returns true if the Flash is ready, else false.
	 */
	public boolean isReady(){
		return this.ready;
	}
	

}
