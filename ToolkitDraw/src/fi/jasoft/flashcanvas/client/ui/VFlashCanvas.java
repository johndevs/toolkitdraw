package fi.jasoft.flashcanvas.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ObjectElement;
import com.google.gwt.dom.client.ParamElement;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.HTML;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VConsole;

public class VFlashCanvas extends HTML implements Paintable {

	/** The identifier of the embedded Flash plugin **/
	private String id = "";
	
	/** Component identifier in UIDL communications. */   
	private String uidlId;
	
    /** Reference to the server connection object. */
    private ApplicationConnection client;	
    
    /** Relative path to the executable Flash **/ 
    public static String SWFPATH = "fi_jasoft_flashcanvas/PaintCanvas.swf";
          
    /** Indicates if the flash has been inited and is ready to be used **/
    private boolean ready = false;
    
    /** Indicates if the initial UIDL has been processed **/
    private boolean init = false;
    
    /** Tracks the transactions **/
    private Long transactionCount = 0L;
    
    public static final String STYLENAME = "v-flashcanvas";
      
    /**
     * Default cnstructor
     */
	public VFlashCanvas(){				
		setStyleName(STYLENAME);
		getElement().getStyle().setOverflow(Overflow.HIDDEN);
		
		//Ensure that the methods are also available in javascript
		FlashCanvasNativeUtil.defineBridgeMethods();
		
		//Register the canvas with the native util
		FlashCanvasNativeUtil.registerCanvas(this);		
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
				
		if(command.equals("undo")) 				FlashCanvasNativeUtil.undo(id);
		else if(command.equals("redo")) 		FlashCanvasNativeUtil.redo(id);
		else if(command.equals("newlayer")) 	FlashCanvasNativeUtil.addLayer(id, value);
		else if(command.equals("paperWidth"))	FlashCanvasNativeUtil.setPaperWidth(id, Integer.parseInt(value));
		else if(command.equals("paperHeight"))	FlashCanvasNativeUtil.setPaperHeight(id, Integer.parseInt(value));
		else if(command.equals("penSize"))		FlashCanvasNativeUtil.setPenSize(id, Double.parseDouble(value));		
		else if(command.equals("penColor"))		FlashCanvasNativeUtil.setPenColor(id, value);
		else if(command.equals("penAlpha"))		FlashCanvasNativeUtil.setPenAlpha(id, Double.parseDouble(value));
		else if(command.equals("brush"))		FlashCanvasNativeUtil.setBrush(id, value);
		else if(command.equals("fillColor"))	FlashCanvasNativeUtil.setFillColor(id, value);
		else if(command.equals("fillAlpha"))	FlashCanvasNativeUtil.setFillAlpha(id, Double.parseDouble(value));
		else if(command.equals("showLayer"))	FlashCanvasNativeUtil.setLayerVisibility(id, value, true);
		else if(command.equals("hideLayer"))	FlashCanvasNativeUtil.setLayerVisibility(id, value, false);
		else if(command.equals("activeLayer"))	FlashCanvasNativeUtil.selectLayer(id, value);
		else if(command.equals("layerup"))		FlashCanvasNativeUtil.moveLayerUp(id, value);
		else if(command.equals("layerdown"))	FlashCanvasNativeUtil.moveLayerDown(id, value);
		else if(command.equals("removelayer"))	FlashCanvasNativeUtil.removeLayer(id, value);
		else if(command.equals("getImageXML")){
			//Feth the xml from the flash compoent
			String xml = FlashCanvasNativeUtil.getImageXML(id);
			
			//Send the result back to the server
			client.updateVariable(uidlId, "getImageXML", xml, true);		
		}
		else if(command.equals("getImagePNG")){
			//Fetch the base64 png image from the component
			String base64 = FlashCanvasNativeUtil.getImagePNG(id, Integer.valueOf(value));
			
			//Send the result back to the server
			client.updateVariable(uidlId, "getImagePNG", base64, true);
		}    	
		
		else if(command.equals("getImageJPG")){
			//Fetch the base64 png image from the component
			String base64 = FlashCanvasNativeUtil.getImageJPG(id, Integer.valueOf(value));
			
			//Send the result back to the server
			client.updateVariable(uidlId, "getImageJPG", base64, true);
		}    	
		
		else if(command.equals("interactive"))	FlashCanvasNativeUtil.setInteractive(id, Boolean.valueOf(value));
		else if(command.equals("graphics-clear"))	FlashCanvasNativeUtil.clear(id);
		else if(command.equals("graphics-line")){
			String[] coords = value.split(",");	
			FlashCanvasNativeUtil.drawLine(id, 	Integer.valueOf(coords[0]), 
												Integer.valueOf(coords[1]), 
												Integer.valueOf(coords[2]), 
												Integer.valueOf(coords[3]));    			
		}
		else if(command.equals("graphics-square")){
			String[] coords = value.split(",");		
			FlashCanvasNativeUtil.drawSquare(id, Integer.valueOf(coords[0]), 
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
    			
    			FlashCanvasNativeUtil.drawPolygon(id, xi, yi);
			}   			
		}
		else if(command.equals("graphics-ellipse")){
			String[] coords = value.split(",");		
			FlashCanvasNativeUtil.drawEllipse(id, Integer.valueOf(coords[0]), 
												Integer.valueOf(coords[1]), 
												Integer.valueOf(coords[2]), 
												Integer.valueOf(coords[3]));			
		}
		else if(command.equals("graphics-fill")){
			String[] coords = value.split(";");
			int x = Integer.parseInt(coords[0]);
			int y = Integer.parseInt(coords[1]);
			String color = coords[2];
			double alpha = Double.parseDouble(coords[3]);
			
			FlashCanvasNativeUtil.setPenColor(id, color);
			FlashCanvasNativeUtil.setPenAlpha(id, alpha);
			FlashCanvasNativeUtil.fill(id,x,y);
		}
		else if(command.equals("componentColor")){
			//this.getElement().setPropertyString("style", "background:"+value);
			//PaintCanvasNativeUtil.setComponentBackground(id, value);
		}    		
		else if(command.equals("layercolor")){    		
			FlashCanvasNativeUtil.setLayerColor(id, value); 
		}
		else if(command.equals("layeralpha")){
			FlashCanvasNativeUtil.setLayerAlpha(id, Double.parseDouble(value));
		}    		
		else if(command.equals("graphics-text")){
			String args[] = value.split(";");
			int height = Integer.parseInt(args[args.length-1]);
			int width = Integer.parseInt(args[args.length-2]);
			int y = Integer.parseInt(args[args.length-3]);
			int x = Integer.parseInt(args[args.length-4]);
			String text = "";
			
			for(int i=0; i<args.length-4; i++)
				text = text + args[i];					
			
			FlashCanvasNativeUtil.drawText(id, text, x, y, width, height); 																
		}
		else if(command.equals("selectionRemove")){
			FlashCanvasNativeUtil.removeSelection(id);
		}
		else if(command.equals("selectionAll")){
			FlashCanvasNativeUtil.selectAll(id);
		}
		else if(command.equals("selectionCrop")){
			FlashCanvasNativeUtil.selectCrop(id);
		}		
		else if(command.equals("setCurrentFont")){
			String fontName = value;
			FlashCanvasNativeUtil.setFont(id, fontName);
		}
		else if(command.equals("graphics-image")){
			int marker1 = value.indexOf(";");
			int marker2 = marker1 + value.substring(marker1+1).indexOf(";") +1;
			int marker3 = marker2 + value.substring(marker2+1).indexOf(";") +1;
			int marker4 = marker3 + value.substring(marker3+1).indexOf(";") +1;
			int marker5 = marker4 + value.substring(marker4+1).indexOf(";") +1;
			
			int x = Integer.parseInt(value.substring(0,marker1));
			int y = Integer.parseInt(value.substring(marker1+1, marker2));
			int w = Integer.parseInt(value.substring(marker2+1, marker3));
			int h = Integer.parseInt(value.substring(marker3+1, marker4));
			double alpha = Double.parseDouble(value.substring(marker4+1,marker5));
			String img = value.substring(marker5+1);
					
			FlashCanvasNativeUtil.drawImage(id, img, x, y, w, h, alpha);
			
			
		}
		else if(command.equals("cache-mode")){
			FlashCanvasNativeUtil.setCacheMode(id, value);
		}				
		else if(command.equals("cache")){		
			
			String val = value.replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"");			
			FlashCanvasNativeUtil.setImageCache(id, val);
		}		
		else if(command.equals("plugin")){
			//Plugin change while editing not supported at this time. 
		}
		else if(command.equals("autosave")){
			int seconds = Integer.parseInt(value);
			FlashCanvasNativeUtil.setAutosaveTime(id, seconds);
		}		
		else if(command.equals("clicklisten")){
			boolean on = Boolean.parseBoolean(value);
			FlashCanvasNativeUtil.setClickListening(id, on);
		}		
		else if(command.equals("finish")){
			FlashCanvasNativeUtil.finish(id);
		}
		else if(command.equals("open-image")){
			FlashCanvasNativeUtil.openImage(id, value);
		}
		
		else	FlashCanvasNativeUtil.error("No command \""+command+"\" found!");		
	}
	
	/**
	 * This method creates the embedded Flash component when loading is complete
	 * 
	 */
	private void createFlashComponent(	String url, String pageWidth, 
										String pageHeight, String bgColor, 
										String cacheMode, boolean interactive){	
		this.getElement().setId(id+"-canvas");	
		
		String pluginCouldNotLoad = 		
			"<p>In order to view this page you need Flash Player 9+ support!</p>"+
	        "<p>"+
	            "<a href=\"http://www.adobe.com/go/getflashplayer\">"+
	                "<img src=\"http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif\" alt=\"Get Adobe Flash player\" />"+
	            "</a>"+
	        "</p>";
		
		setHTML("<div style='outline:none' id='"+id+"'>"+pluginCouldNotLoad+"</div>");
		
		//Embed the flash with SWFObject
		createSWFObject(url,this.id, pageWidth, pageHeight, bgColor, cacheMode, interactive);		
	}
	
	/**
	 * Creates the swf object.
	 * 
	 * @param swfUrl the swf url
	 * @param id the id
	 * @param pageWidth the page width
	 * @param pageHeight the page height
	 * @param bgColor the bg color
	 * @param cacheMode the cache mode
	 */
	private native void createSWFObject(String swfUrl, String id, 
										String pageWidth, String pageHeight, 
										String bgColor, String cacheMode,
										boolean interactive)/*-{
		var flashvars = {};
		flashvars.id = id;
		flashvars.width = pageWidth;
		flashvars.height = pageHeight;
		flashvars.bgColor = bgColor;
		flashvars.cacheMode = cacheMode;
		flashvars.interactive = interactive;
		
		var params = {};
		params.menu = "false";
		params.wmode = "opaque";
		params.movie = swfUrl;
		params.allowScriptAccess = "always";
		params.bgcolor = "#"+bgColor.substr(2,6);
		params.scale = "noborder";
		
		var attributes = {};
		attributes.id = id;
		attributes.name = id;		
		
		$wnd.swfobject.embedSWF(swfUrl, id, "100%", "100%", "9.0.0","expressInstall.swf", flashvars, params, attributes);
	}-*/;
		
	 /**
 	 * This method must be implemented to update the client-side component from
 	 * UIDL data received from server.
 	 * 
 	 * This method is called when the page is loaded for the first time, and
 	 * every time UI changes in the component are received from the server.
 	 * 
 	 * @param uidl the uidl
 	 * @param client the client
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
                      
        // Save the component id
        this.id = uidl.getStringVariable("flashIdentifier");      
        
        //Check if the transaction id is the same as we have
        if(uidl.getLongVariable("transactionId") != transactionCount){
        	
        	// If the transaction id is not the same then a refresh has happend
        	// and we need to initialize the component again
        	client.updateVariable(uidlId, "readyStatus", false, true);   
        	transactionCount = 0L;
        	return;
        }
        
        //Increase the transaction counter by one
        transactionCount++;
                
        //Parse the commands and their values from the UIDL
        if(uidl.hasVariable("commands") && uidl.hasVariable("values")){
        	       	
        	String[] commands = uidl.getStringArrayVariable("commands");
            String[] values = uidl.getStringArrayVariable("values");       
            
            //check that all values came with the transmission
            if(commands.length != values.length){
            	FlashCanvasNativeUtil.error("Transmission error!");
            	return;
            }        
                        
            if(init && ready){                            
	            // execute commands
	            for(int i=0; i<commands.length; i++)
	       		 	executeCommand(commands[i], values[i]);            
            } else {
            	FlashCanvasNativeUtil.error("Plugin not ready!");
            }
        }
        
        // Cache recieived at initialization, pass it on
        else if(uidl.hasAttribute("cache")){
        	executeCommand("cache", uidl.getStringAttribute("cache"));
        }
        
        // The component is initializing, get the init data
        else if(!init){        
        	
           		int pageWidth = uidl.getIntAttribute("pageWidth");
            	int pageHeight = uidl.getIntAttribute("pageHeight");
            	String bgColor = uidl.getStringAttribute("componentColor");
            	String cacheMode = uidl.getStringAttribute("cache-mode");
            	String plugin = uidl.getStringAttribute("plugin");
            	boolean interactive = uidl.getBooleanAttribute("interactive");
            	           	
            	//Use flash plugin
            	if(plugin.equals("plugin-flash")){
            		//Add the SWF path
            		String url = GWT.getModuleBaseURL() + SWFPATH;	       		 
            		        		        		
            		//Initialize the flash component
            		createFlashComponent(url, String.valueOf(pageWidth), String.valueOf(pageHeight), bgColor,cacheMode, interactive);
            		init = true;
            	}
            	
            	//No suitable plugin found
            	else{
            		FlashCanvasNativeUtil.error("No suitable plugin \""+plugin+"\" found!");
            		init = false;
            	}            	
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
		client.updateVariable(this.uidlId, "readyStatus", ready, false);
		client.updateVariable(this.uidlId, "initData", true, true);
	}
	
	/**
	 * Returns Flash component status
	 * @return
	 * 		Returns true if the Flash is ready, else false.
	 */
	public boolean isReady(){
		return this.ready;
	}
	
	/**
	 * This function sets the available fonts
	 * This is called by the PaintCanvasNativeUtil when the Flash sends the fonts
	 * @param fonts
	 */
	public void setFonts(String[] fonts){
		client.updateVariable(this.uidlId, "fontset", fonts, true);
	}
	
	/**
	 * Fetches the cached image(if any) from the server
	 */
	public void getServerCache(){
		client.updateVariable(this.uidlId, "update-cache", true, true);
	}
	
	/**
	 * Sets the cached image on the server
	 */
	public void setServerCache(String xml){				
		client.updateVariable(this.uidlId, "set-cache", new Object[]{xml}, true);
	}
	
	public void clickEvent(int x, int y){
		client.updateVariable(this.uidlId, "click-event",new Object[]{x,y} , true);
	}
	
	public void brushStartEvent(){
		client.updateVariable(this.uidlId, "brush-start-event", true, true);
	}
	
	public void brushEndEvent(){
		client.updateVariable(this.uidlId, "brush-end-event", true, true);
	}
	
	public void brushLoadingCompleteEvent(){
		client.updateVariable(this.uidlId, "brush-loading-complete-event",true, true);
	}
	

}
