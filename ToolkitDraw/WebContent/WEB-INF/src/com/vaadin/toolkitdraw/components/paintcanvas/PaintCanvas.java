package com.vaadin.toolkitdraw.components.paintcanvas;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.collections.PriorityQueue;


import com.vaadin.Application;
import com.vaadin.terminal.ApplicationResource;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.toolkitdraw.components.paintcanvas.events.ImageJPGRecievedEvent;
import com.vaadin.toolkitdraw.components.paintcanvas.events.ImagePNGRecievedEvent;
import com.vaadin.toolkitdraw.components.paintcanvas.events.ImageXMLRecievedEvent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;

public class PaintCanvas extends AbstractField implements Component, Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * The graphics class is used to draw on the canvas from the server side
	 * This is mostly used when we want to present some information on the drawing
	 * component.
	 */
	public class Graphics implements Serializable{				
		
		private static final long serialVersionUID = 1L;

		//Are we using the batch mode?
		private boolean batchMode = false;		
		
		private Queue<Map<String, String>> batch = new ArrayBlockingQueue<Map<String,String>>(1000);
		
		private void addToBatch(String command, String value){
			Map<String, String> entry = new HashMap<String, String>();
			entry.put(command, value);	
			batch.add(entry);
		}
		
		/**
		 * Set the batch mode which only sends the graphics commands when the batch is sent
		 * @param mode
		 * 		The mode toggle
		 */
		public void setBatchMode(boolean mode){
			batchMode = mode;
		}
		
		/**
		 * Returns the batch mode
		 * @return
		 */
		public boolean getBatchMode(){
			return this.batchMode;
		}
		
		/**
		 * Moves the batched commands to the send queue
		 */
		public void sendBatch(){
			while(!batch.isEmpty()) 
				changedValues.add(batch.poll());
			
			if(isImmediate()) requestRepaint();
		}
				
		/**
		 * This method draws a line between two points. The coordinates are
		 * from the top-left corner.
		 * 
		 * @param x1
		 * 		x-coordinate for the first point		
		 * @param y1
		 * 		y-coordinate for the first point
		 * @param x2
		 * 		x-coordinate for the second point
		 * @param y2
		 * 		y-coordinate for the second point
		 */
		public void drawLine(int x1, int y1, int x2, int y2){				
			if(batchMode) addToBatch("graphics-line", x1+","+y1+","+x2+","+y2);
			else addToQueue("graphics-line", x1+","+y1+","+x2+","+y2);					
					
			if(isImmediate() && !batchMode) requestRepaint();
		}
		
		public void drawLine(int x1, int y1, int x2, int y2, String color){
			//Do some color string checks
	    	if(color.contains("#")) color = color.replaceAll("#", "0x");	    		    	
	    	if(!color.contains("x")) color = "0x"+color;			
			
			if(batchMode){
				addToBatch("penColor", color);
				addToBatch("graphics-line", x1+","+y1+","+x2+","+y2);
			} else {
				addToQueue("penColor", color);
				addToQueue("graphics-line", x1+","+y1+","+x2+","+y2);
			}
			
			if(isImmediate() && !batchMode) requestRepaint();
		}
		
		/**
		 * This method draws a square on the component based on the width and height
		 * given. The position of the square is based on the coordinates given.
		 * 
		 * @param x
		 * 		x-coordinate of the top-left corner of the square
		 * @param y
		 * 		y-coordinate of the top-left corner of the square
		 * @param width
		 * 		The width of the square in pixels
		 * @param height
		 * 		The height of the square in pixels
		 */
		public void drawSquare(int x, int y, int width, int height){
			if(batchMode) addToBatch("graphics-square", x+","+y+","+width+","+height);			
			else addToQueue("graphics-square", x+","+y+","+width+","+height);					
			
			if(isImmediate() && !batchMode) requestRepaint();		
		}		
		
		public void drawSquare(int x, int y, int width, int height, String frameColor, String fillColor){		
			
			//Do some color string checks
	    	if(frameColor.contains("#")) frameColor = frameColor.replaceAll("#", "0x");
	    	if(fillColor.contains("#")) fillColor = fillColor.replaceAll("#", "0x");
	    	
	    	if(!frameColor.contains("x")) frameColor = "0x"+frameColor;
	    	if(!fillColor.contains("x")) fillColor = "0x"+fillColor;
						
			if(batchMode){
				//We need to change the brush to a squeare before the operation so 
				//we can set the fillcolor
				addToBatch("brush", BrushType.SQUARE.toString());
				addToBatch("penSize","1");				
				addToBatch("penColor", frameColor);
				addToBatch("fillColor", fillColor);
				addToBatch("graphics-square", x+","+y+","+width+","+height);	
			} else {
				//We need to change the brush to a squeare before the operation so 
				//we can set the fillcolor
				addToQueue("brush", BrushType.SQUARE.toString());
				
				addToQueue("penSize","1");
				addToQueue("penColor", frameColor);
				addToQueue("fillColor", fillColor);
				addToQueue("graphics-square", x+","+y+","+width+","+height);
			}			
			
			if(isImmediate() && !batchMode) requestRepaint();	
		}
		
		/**
		 * This method clears the current layer of any strokes(empties it)
		 */
		public void clear(){
			if(batchMode) addToBatch("graphics-clear", "");
			else addToQueue("graphics-clear", "");
			
			if(isImmediate() && !batchMode) requestRepaint();		
		}
		
		/**
		 * Draws a polygon
		 */
		
		public void drawPolygon(int[]x, int[]y){
			
			if(x.length == 0 || x.length != y.length)
				return;
			
			StringBuilder xStr = new StringBuilder(String.valueOf(x[0]));
			StringBuilder yStr = new StringBuilder(String.valueOf(y[0]));
			for(int i=1; i<x.length; i++){
				xStr.append(",");
				xStr.append(x[i]);
				yStr.append(",");
				yStr.append(y[i]);
			}
						
			if(batchMode) addToBatch("graphics-polygon", xStr.toString()+";"+yStr.toString());
			else addToQueue("graphics-polygon", xStr.toString()+";"+yStr.toString());
			
			if(isImmediate() && !batchMode) requestRepaint();				
		}
		
		public void drawPolygon(int[]x, int[]y, String color, String fillColor){
			if(x.length == 0 || x.length != y.length)
				return;
			
			//Do some color string checks
	    	if(color.contains("#")) color = color.replaceAll("#", "0x");
	    	if(fillColor.contains("#")) fillColor = fillColor.replaceAll("#", "0x");
	    	
	    	if(!color.contains("x")) color = "0x"+color;
	    	if(!fillColor.contains("x")) fillColor = "0x"+fillColor;
			
			StringBuilder xStr = new StringBuilder(String.valueOf(x[0]));
			StringBuilder yStr = new StringBuilder(String.valueOf(y[0]));
			for(int i=1; i<x.length; i++){
				xStr.append(",");
				xStr.append(x[i]);
				yStr.append(",");
				yStr.append(y[i]);
			}
		
			if(batchMode){
				addToBatch("brush", BrushType.POLYGON.toString());
				addToBatch("penSize","1");				
				addToBatch("penColor", color);
				addToBatch("fillColor", fillColor);				
				addToBatch("graphics-polygon", xStr.toString()+";"+yStr.toString());
			}
			else{
				addToQueue("brush", BrushType.POLYGON.toString());
				addToQueue("penSize","1");				
				addToQueue("penColor", color);
				addToQueue("fillColor", fillColor);					
				addToQueue("graphics-polygon", xStr.toString()+";"+yStr.toString());
			}
			
			if(isImmediate() && !batchMode) requestRepaint();	
		}
		
		/**
		 * Draws some text at the position gived
		 * @param text
		 * 		The string of text to add
		 * @param x
		 * 		The x-coordinate
		 * @param y
		 * 		The y-coordinate
		 * @param color'
		 * 		The color of the text
		 * @param fontSize
		 * 		The size of the text
		 * @param fillColor
		 * 		The background color of the text
		 * @param alpha
		 * 		The transparency of the background
		 */
		public void drawText(String text, int x, int y, int width, int height, String color, int fontSize, String fillColor, double alpha){
			
			//Do some color string checks
	    	if(color.contains("#")) color = color.replaceAll("#", "0x");
	    	if(fillColor.contains("#")) fillColor = fillColor.replaceAll("#", "0x");
	    	
	    	if(!color.contains("x")) color = "0x"+color;
	    	if(!fillColor.contains("x")) fillColor = "0x"+fillColor;
	    		    	
	    	StringBuilder value = new StringBuilder(text);
	    	value.append(";");
	    	value.append(x);
	    	value.append(";");
	    	value.append(y);
	    	value.append(";");
	    	value.append(width);
	    	value.append(";");
	    	value.append(height);
	    		    	
	    	if(batchMode){
	    		addToBatch("brush", BrushType.TEXT.toString());
	    		addToBatch("penSize",String.valueOf(fontSize));				
				addToBatch("penColor", color);
				addToBatch("fillColor", fillColor);	
				addToBatch("penAlpha", String.valueOf(alpha));
				addToBatch("graphics-text", value.toString());	    		
	    	} else {
	    		addToQueue("brush", BrushType.TEXT.toString());
	    		addToQueue("penSize",String.valueOf(fontSize));				
	    		addToQueue("penColor", color);
	    		addToQueue("fillColor", fillColor);	
	    		addToQueue("penAlpha", String.valueOf(alpha));
	    		addToQueue("graphics-text", value.toString());	    		    		
	    	}
	    	if(isImmediate() && !batchMode) requestRepaint();				
		}
		
		
	}
		
	/** The command history is needed when the the session is reloaded(F5) **/
	private Queue<Map<String, String>> commandHistory = new ArrayBlockingQueue<Map<String,String>>(10000);
	
	private Queue<Map<String, String>> changedValues = new ArrayBlockingQueue<Map<String,String>>(1000);
	
	private List<Layer> layers = new ArrayList<Layer>();	
	
	private Set<ValueChangeListener> valueGetters = new HashSet<ValueChangeListener>();
		
	private Graphics graphics = new Graphics();
	
	private boolean initComplete = false;
	
	//Default initialization data
	private int paperHeight = -1;
	private int paperWidth = -1;
	private String componentColor = "000000";
	private boolean interactive = false;
	
	public static enum BrushType{
		PEN("Pen"),
		SQUARE("Square"),
		ELLIPSE("Ellipse"),
		LINE("Line"),
		POLYGON("Polygon"),
		TEXT("Text");
		
		private final String str;
		private BrushType(String s) {
			this.str = s;
		}
		public String toString(){
			return str;
		}		
	}
			
	public PaintCanvas(){	
		
		/* Create the background layer which cannot be removed
		 * When creating a new layer it is automatically added the its canvas
		 */
		Layer background = new Layer("Background", this);
		layers.add(background);
		
		//Set the paper height and width of the component to full
		setPaperHeight(-1);		
		setPaperWidth(-1);
		
		setImmediate(true);		
		requestRepaint();		
		
	}
	
	public PaintCanvas(String width, String height){				
		super.setWidth(width);
		super.setHeight(height);		
		
		/* Create the background layer which cannot be removed
		 * When creating a new layer it is automatically added the its canvas
		 */
		Layer background = new Layer("Background", this);
		layers.add(background);
		
		//Set the paper height and width of the component to full
		setPaperHeight(-1);		
		setPaperWidth(-1);	
		
		setImmediate(true);
		requestRepaint();
		
	}
	
	public PaintCanvas(String width, String height, String color){				
		super.setWidth(width);
		super.setHeight(height);
		
		/* Create the background layer which cannot be removed
		 * When creating a new layer it is automatically added the its canvas
		 */
		Layer background = new Layer("Background", this);
		layers.add(background);
		
		//Set the paper height and width of the component to full
		setPaperHeight(-1);		
		setPaperWidth(-1);	
		
		//Set the background color
		setComponentBackgroundColor(color);		
		
		setImmediate(true);
		requestRepaint();
		
	}
	
	public PaintCanvas(String width, String height, int paperWidth, int paperHeight){
		super.setWidth(width);
		super.setHeight(height);
			
		/* Create the background layer which cannot be removed
		 * When creating a new layer it is automatically added the its canvas
		 */
		Layer background = new Layer("Background", this);
		layers.add(background);
		
		//Set the paper height and width of the component to full
		setPaperHeight(paperHeight);		
		setPaperWidth(paperWidth);	
			
		setImmediate(true);
		requestRepaint();
		
	}
	
	public PaintCanvas(String width, String height, int paperWidth, int paperHeight, String color){
		super.setWidth(width);
		super.setHeight(height);	
		
		/* Create the background layer which cannot be removed
		 * When creating a new layer it is automatically added the its canvas
		 */
		Layer background = new Layer("Background", this);
		layers.add(background);
				
		//Set the paper height and width(layer size)
		setPaperHeight(paperHeight);
		setPaperWidth(paperWidth);		
		
		//Set the background color
		setComponentBackgroundColor(color);
		
		setImmediate(true);
		requestRepaint();
		
	}
	
	public PaintCanvas(String width, String height, int paperWidth, int paperHeight, boolean interactive){
		super.setWidth(width);
		super.setHeight(height);
		
		/* Create the background layer which cannot be removed
		 * When creating a new layer it is automatically added the its canvas
		 */
		Layer background = new Layer("Background", this);
		layers.add(background);
						
		//Set the paper height and width(layer size)
		setPaperHeight(paperHeight);
		setPaperWidth(paperWidth);	
		
		/*
		 * Set the component in interactive mode which means that the user can draw on it
		 * with the mouse
		 */
		setInteractive(interactive);
		
		setImmediate(true);
		requestRepaint();
	
	}	
	
		
	/**
	 * Adds an element to the changed variables sent to the client
	 * 
	 * @param command
	 * 		The command which should be executed on the client
	 * @param value
	 * 		The value of the parameter
	 */
	private void addToQueue(String command, String value){
		Map<String, String> entry = new HashMap<String, String>();
		entry.put(command, value);	
		changedValues.add(entry);
	}
	
	
	
	
	
	/**
	 * This method returns a graphics object which can be used to draw on the canvas
	 * @return
	 * 		Drawable graphics object
	 */
	public Graphics getGraphics(){
		return graphics;
	}	
	
	@Override
	public Class getType() {		
		return PaintCanvas.class;
	}

	@Override
	public String getTag() {		
		return "paintcanvas";
	}
	
	 /** Paint (serialize) the component for the client. */
    public void paintContent(PaintTarget target) throws PaintException {
            	    	  	  	    	
    	// Superclass writes any common attributes in the paint target.
        super.paintContent(target);                                     
           	
        List<String> commands = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
                
        //Add the changed values
        while(!changedValues.isEmpty()){
        	Map<String, String> entry = changedValues.poll();
        	for(String command : entry.keySet()){
        		commands.add(command);
        		values.add(entry.get(command));
        	}
        	commandHistory.add(entry);
        }
        
        target.addVariable(this, "commands", commands.toArray(new String[commands.size()]));
        target.addVariable(this, "values", values.toArray(new String[values.size()]));
            
        //Send update flag. If this is not sent(false) then the flash
        //will refresh itself from the local cache
        if(initComplete){
        	target.addAttribute("init", initComplete);
        }   	
        	
        initComplete = true;
    }
    
    /** Deserialize changes received from client. */
    public void changeVariables(Object source, Map variables) {
            	
    	//XML image recieved
    	if(variables.containsKey("getImageXML")){
    		String xml = variables.get("getImageXML").toString();    		
    		
    		ImageXMLRecievedEvent event = new ImageXMLRecievedEvent(this,xml);
    		for(ValueChangeListener listener : valueGetters){
    			listener.valueChange(event);
    		}    		
    	}
    	    	
    	 //PNG image recieved
    	if(variables.containsKey("getImagePNG")){    		
    		String base64 = variables.get("getImagePNG").toString();
    		byte[] data = null;
    		
    		try{
    			data = new sun.misc.BASE64Decoder().decodeBuffer(base64);
    		}catch(IOException io){
    			System.out.println("Failure converting image from base64 to byte[]");
    		}
    		
    		ImagePNGRecievedEvent event = new ImagePNGRecievedEvent(this,data);
    		for(ValueChangeListener listener : valueGetters){
    			listener.valueChange(event);
    		}    	
    	}    	
    	
    	//JPG image recieved
    	if(variables.containsKey("getImageJPG")){ 
    		        		
    		String base64 = variables.get("getImageJPG").toString();
    		byte[] data = null;
    		
    		try{
    			data = new sun.misc.BASE64Decoder().decodeBuffer(base64);
    		}catch(IOException io){
    			System.out.println("Failure converting image from base64 to byte[]");
    		}
    		
    		ImageJPGRecievedEvent event = new ImageJPGRecievedEvent(this,data);
    		for(ValueChangeListener listener : valueGetters){
    			listener.valueChange(event);
    		}    	
    	}    	
    	
    	//Flash is ready event recieved
    	if(variables.containsKey("readyStatus")){    		
    		initComplete = true;
    	}
    	
    	//Send the initialization data
    	if(variables.containsKey("initData")){
    		System.out.println("Initialization data needed");
    		setPaperHeight(paperHeight);
    		setPaperWidth(paperWidth);
    		setComponentBackgroundColor(componentColor);
    		setInteractive(interactive);
    		initComplete = false;
    		requestRepaint();    		
    	}
    }
    
    /**
     * Undo a previously done brush stroke
     */
    public void undo(){    	
    	addToQueue("undo", "true");    	
    	if(isImmediate()) requestRepaint();  
    }
    
    /**
     * Redo an undone brush stroke.
     * If a brush is undone and then the user draws on the canvas then redo
     * cannot redo the undone brush stroke since the canvas has changed.
     */
    public void redo(){
    	addToQueue("redo", "true");    	
    	if(isImmediate()) requestRepaint();
    }
    
    /**
     * Set the paper height in the drawing component
     * This can be used if we want to create an drawable area which is less than
     * the component size
     * 
     * @param h
     * 		The height of the paper
     */
    public void setPaperHeight(int h){
    	paperHeight = h;
    	addToQueue("paperHeight", String.valueOf(h));    	
    	if(isImmediate()) requestRepaint();
    }
    
    /**
     * Set the paper width in the drawing component.
     * This can be used if we want to create an drawable area which is less than
     * the component size
     * 
     * @param w
     * 		The width of the paper
     */    
    public void setPaperWidth(int w){
    	paperWidth = w;
    	addToQueue("paperWidth", String.valueOf(w));
    	if(isImmediate()) requestRepaint();
    }
    
    /**
     * Sets the pixel size of the currently used brush. How this effects the output
     * depends on the current brush.
     * @param size
     * 		The size of the brush in pixels
     */
    public void setToolSize(double size){
    	addToQueue("penSize", String.valueOf(size));
    	if(isImmediate()) requestRepaint();
    }

    /**
     * Sets the color of the currently user brush.
     * If the brush is a square or ellipse the it is the frame color.
     * 
     * @param color
     * 		The color of the brush in hexadecimal representation
     */		
    public void setColor(String color){
    	
    	if(color.contains("#")){
    		color.replaceAll("#", "0x");
    	}
    	
    	if(!color.contains("x")){
    		color = "0x"+color;
    	}
    	    	
    	addToQueue("penColor", color);
    	if(isImmediate()) requestRepaint();
    }
    
    /**
     * Sets the fill color of the current brush.
     * This is used if the brush supports fillcolor. For example ellipse and square brushes
     * support this
     * 
     * @param color
     * 		The color used to fill the brush in hexadecimal representation
     */
    public void setFillColor(String color){
    	if(color.contains("#")){
    		color.replaceAll("#", "0x");
    	}
    	
    	if(!color.contains("x")){
    		color = "0x"+color;
    	}
    	
    	addToQueue("fillColor", color);
    	if(isImmediate()) requestRepaint();    	
    }
    
    /**
     * Sets the alpha value of the brush
     * @param alpha
     * 		Value between 0 and 1
     */
    public void setAlpha(double alpha){
    	addToQueue("penAlpha", String.valueOf(alpha));
    	if(isImmediate()) requestRepaint();   
    }
    
    
    //TODO Update if necessery
    /**
     * Set the currently used brush
     * The following brushes are currently supported:
     * 
     * @param brush
     */
    public void setBrush(BrushType brush){
    	addToQueue("brush", brush.toString());
    	if(isImmediate()){    	
    		requestRepaint();
    	}
    }
    
    public void addLayer(Layer layer){
    	addToQueue("newlayer", layer.getName());
    	layers.add(layer);
    	if(isImmediate()) requestRepaint();
    }
    
    public void removeLayer(Layer layer){
    	
    	//Cannot remove background layer
    	if(layer.getName().equals("Background")) return;
    	
    	addToQueue("removelayer", layer.getName());
    	layers.remove(layer);
    	if(isImmediate()) requestRepaint();
    }
    
    public void moveLayerUp(String name){
    	
    	//Cannot move background layer
    	if(name.equals("Background")) return;
    	
    	addToQueue("layerdown", name);
    	if(isImmediate()) requestRepaint();
    }
    
    public void moveLayerDown(String name){
    	
    	//Cannot move background layer
    	if(name.equals("Background")) return;
    	
    	addToQueue("layerup", name);
    	if(isImmediate()) requestRepaint();
    }
    
    public void setLayerVisibility(String name, boolean visible){
    	if(visible) addToQueue("showLayer", name);
    	else addToQueue("hideLayer", name);    
    	if(isImmediate()) requestRepaint();
    }
    
    public void setActiveLayer(Layer layer){
    	addToQueue("activeLayer", layer.getName());
    	if(isImmediate()) requestRepaint();
    }
    
    public List<Layer> getLayers(){
    	return layers;
    }    
     
    public void getImageXML(){
    	addToQueue("getImageXML", "");
    	if(isImmediate()) requestRepaint();
    }
    
    public void getImagePNG(int dpi){
    	addToQueue("getImagePNG", String.valueOf(dpi));
    	if(isImmediate()) requestRepaint();
    }
    
    public void getImageJPG(int dpi){
    	addToQueue("getImageJPG", String.valueOf(dpi));
    	if(isImmediate()) requestRepaint();
    }

    public void addListener(ValueChangeListener listener){
    	valueGetters.add(listener);
    }
    
    public void removeListener(ValueChangeListener listener){
    	valueGetters.remove(listener);
    }
    
    public void setInteractive(boolean interactive){
    	this.interactive = interactive;
    	addToQueue("interactive", String.valueOf(interactive));
    	if(isImmediate()) requestRepaint();
    }
    
    public void setComponentBackgroundColor(String color){
    	if(color.contains("#")){
    		color.replaceAll("#", "0x");
    	}
    	
    	if(!color.contains("x")){
    		color = "0x"+color;
    	}
    	
    	componentColor = color;
    	addToQueue("componentColor", color);
    	if(isImmediate()) requestRepaint();
    }
    
    public void setLayerBackground(Layer layer, String color, double alpha){
		if(layer == null || color == null || alpha < 0 || alpha > 1.0) return;
		
		//Do some color string checks
    	if(color.contains("#")) color = color.replaceAll("#", "0x");	    	
    	if(!color.contains("x")) color = "0x"+color;
    	
    	//Select the layer
    	setActiveLayer(layer);
    	
    	//Set the color
    	addToQueue("layercolor", color);
    	
    	if(isImmediate()) requestRepaint();
	}
    
    public boolean isReady(){
    	return initComplete;
    }
    
}
