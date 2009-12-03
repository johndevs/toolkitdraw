package com.vaadin.toolkitdraw.components.paintcanvas;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

import com.vaadin.toolkitdraw.components.paintcanvas.events.ImageJPGRecievedEvent;
import com.vaadin.toolkitdraw.components.paintcanvas.events.ImagePNGRecievedEvent;
import com.vaadin.toolkitdraw.components.paintcanvas.events.ImageXMLRecievedEvent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;

import com.vaadin.toolkitdraw.components.paintcanvas.enums.BrushType;
import com.vaadin.toolkitdraw.components.paintcanvas.enums.CacheMode;
import com.vaadin.toolkitdraw.components.paintcanvas.enums.Plugin;
import com.vaadin.toolkitdraw.util.XMLUtil;

@SuppressWarnings("unchecked")
public class PaintCanvas extends AbstractField implements Component, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static final String CLASSNAME = ".v-paintcanvas";
		
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
		
		private String color2String(Color c){
			String red = Integer.toHexString(c.getRed());
			String green = Integer.toHexString(c.getGreen());
			String blue = Integer.toHexString(c.getBlue());
			
			red = red.length() < 2 ? "0"+red : red;
			green = green.length() < 2 ? "0"+green : green;
			blue = blue.length() < 2 ? "0"+blue : blue;
			
			return "0x"+red+green+blue;
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
				addToBatch("fillAlpha", "1");
				addToBatch("graphics-square", x+","+y+","+width+","+height);	
			} else {
				//We need to change the brush to a squeare before the operation so 
				//we can set the fillcolor
				addToQueue("brush", BrushType.SQUARE.toString());				
				addToQueue("penSize","1");
				addToQueue("penColor", frameColor);
				addToQueue("fillColor", fillColor);
				addToQueue("fillAlpha", "1");
				addToQueue("graphics-square", x+","+y+","+width+","+height);
			}			
			
			if(isImmediate() && !batchMode) requestRepaint();	
		}
		
		public void drawEllipse(int x, int y, int width, int height){
			if(batchMode) addToBatch("graphics-ellipse",  x+","+y+","+width+","+height);	
			else addToQueue("graphics-ellipse",  x+","+y+","+width+","+height);	
		}
		
		public void drawEllipse(int x, int y, int width, int height, Color frameColor, Color fillColor){
			if(batchMode){
				addToBatch("brush", BrushType.ELLIPSE.toString());
				addToBatch("penSize", "1");
				addToBatch("penColor", color2String(frameColor));
				addToBatch("fillColor", color2String(fillColor));
				addToBatch("fillAlpha", "1");
				addToBatch("graphics-ellipse", x+","+y+","+width+","+height);
			} else {
				addToQueue("brush", BrushType.ELLIPSE.toString());
				addToQueue("penSize", "1");
				addToQueue("penColor", color2String(frameColor));
				addToQueue("fillColor", color2String(fillColor));
				addToQueue("fillAlpha", "1");
				addToQueue("graphics-ellipse", x+","+y+","+width+","+height);
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
		
		public void drawPolygon(int[]x, int[]y, Color color, Color fillColor){
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
		
			if(batchMode){
				addToBatch("brush", BrushType.POLYGON.toString());
				addToBatch("penSize","1");				
				addToBatch("penColor", color2String(color));
				addToBatch("fillColor", color2String(fillColor));	
				addToBatch("fillAlpha", "1");
				addToBatch("graphics-polygon", xStr.toString()+";"+yStr.toString());
			}
			else{
				addToQueue("brush", BrushType.POLYGON.toString());
				addToQueue("penSize","1");				
				addToQueue("penColor", color2String(color));
				addToQueue("fillColor", color2String(fillColor));	
				addToQueue("fillAlpha", "1");
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
		public void drawText(String text, int x, int y, int width, int height, int fontSize, String fontColor, double fontAlpha,  String fillColor, double fillAlpha){
			
			//Do some color string checks
	    	if(fontColor.contains("#")) fontColor = fontColor.replaceAll("#", "0x");
	    	if(fillColor.contains("#")) fillColor = fillColor.replaceAll("#", "0x");
	    	
	    	if(!fontColor.contains("x")) fontColor = "0x"+fontColor;
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
				addToBatch("penColor", fontColor);
				addToBatch("fillColor", fillColor);	
				addToBatch("fillAlpha", String.valueOf(fillAlpha));
				addToBatch("penAlpha", String.valueOf(fontAlpha));
				addToBatch("graphics-text", value.toString());	    		
	    	} else {
	    		addToQueue("brush", BrushType.TEXT.toString());
	    		addToQueue("penSize",String.valueOf(fontSize));				
	    		addToQueue("penColor", fontColor);
	    		addToQueue("fillColor", fillColor);	
	    		addToQueue("fillAlpha", String.valueOf(fillAlpha));
	    		addToQueue("penAlpha", String.valueOf(fontAlpha));
	    		addToQueue("graphics-text", value.toString());	    		    		
	    	}
	    	if(isImmediate() && !batchMode) requestRepaint();				
		}
		
		/**
		 * Draw an image onto the canvas
		 * @param base64EncodedImage
		 * 		The image encoded in base64
		 * @param x
		 * 		The upper left corner x-coordinate
		 * @param y
		 * 		The upper left corner y-coordinate
		 * @param alpha
		 * 		The alpha value of the image
		 */
		public void drawImage(String base64EncodedImage, int x, int y, double alpha){
			
			StringBuilder value = new StringBuilder();
			value.append(x);
			value.append(";");
			value.append(y);
			value.append(";");
			value.append(alpha);
			value.append(";");
			value.append(base64EncodedImage.replaceAll("\r", "").replaceAll("\n", ""));			
			
			if(batchMode){
				addToBatch("graphics-image", value.toString());
			} else {
				addToQueue("graphics-image", value.toString());
			}
			
			if(isImmediate() && !batchMode){			
				requestRepaint();				
			}
		}
	}
		
	/**
	 * The interactive class contains all methods for controlling the canvas component draw
	 * actions. It can be used to set current color, undo, redo etc.
	 * The iteractive object is ONLY available when the canvas is in interactive mode.
	 */
	public class Interactive implements Serializable{

		private static final long serialVersionUID = 1L;
		
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
	    	configuration.setPaperHeight(h);
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
	    	configuration.setPaperWidth(w);
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
	    	if(color == null || color == ""){
	    		addToQueue("fillColor", "-1");
		    	if(isImmediate()) requestRepaint();    	
		    	
	    	} else {
	    		if(color.contains("#")){
		    		color.replaceAll("#", "0x");
		    	}
		    	
		    	if(!color.contains("x")){
		    		color = "0x"+color;
		    	}
		    	
		    	addToQueue("fillColor", color);
		    	if(isImmediate()) requestRepaint();    	
	    	}	    	
	    }
	    
	    /**
	     * Sets the background alpha
	     * 
	     * @param alpha
	     * 		Value between 0 and 1
	     */
	    public void setFillAlpha(double alpha){
	    	addToQueue("fillAlpha", String.valueOf(alpha));
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
	    	 
	    /**
	     * Set the currently used brush
	     * The following brushes are currently supported:
	     * 
	     * @param brush
	     */
	    public void setBrush(BrushType brush){
	    	addToQueue("brush", brush.toString());
	    	if(isImmediate()) requestRepaint();
	    }
	    
	    /**
	     * Removes selection if there is one
	     */
	    public void removeSelection(){
	    	addToQueue("selectionRemove", "");
	    	if(isImmediate()) requestRepaint();
	    }
	    
	    /**
	     * Selects the whole image
	     */
	    public void selectAll(){
	    	addToQueue("selectionAll", "");
	    	if(isImmediate()) requestRepaint();
	    }
	    
	    /**
	     * Set the current font
	     */
	    public void setFont(String fontName){
	    	addToQueue("setCurrentFont", fontName);
	    	if(isImmediate()) requestRepaint();
	    }
	    
	    /**
	     * Crop to selection
	     */
		public void crop(){
			addToQueue("selectionCrop", "");
			if(isImmediate()) requestRepaint();
		}
		
		/**
		 * Finish the finishable tool (CTRL+Right click)
		 */
		public void finish(){
			addToQueue("finish", "");
			if(isImmediate()) requestRepaint();
		}
	    
	}
	
	/**
	 * The layers class contains all the layer functionality like adding layers, modifying 
	 * layers etc.
	 */
	public class Layers implements Serializable{

		private static final long serialVersionUID = 1L;
		
		/**
		 * Add a layer
		 * @param layer
		 * 		The added layer
		 */
		public void addLayer(Layer layer){
	    	addToQueue("newlayer", layer.getName());
	    	layers.add(layer);
	    	if(isImmediate()) requestRepaint();
	    }
	    
		/**
		 * Remove a layer
		 * @param layer
		 * 		The layer to be removed
		 */
	    public void removeLayer(Layer layer){
	    	
	    	//Cannot remove background layer
	    	if(layer.getName().equals("Background")) return;
	    	
	    	addToQueue("removelayer", layer.getName());
	    	layers.remove(layer);
	    	if(isImmediate()) requestRepaint();
	    }
	    
	    /**
	     * Move a layer up
	     * @param layer
	     * 		The layer to be moved
	     */
	    public void moveLayerUp(Layer layer){
	    	
	    	//Cannot move background layer
	    	if(layer.getName().equals("Background")) return;
	    	
	    	addToQueue("layerup", layer.getName());
	    	if(isImmediate()) requestRepaint();
	    }
	    
	    /**
	     * Move a layer down
	     * @param layer
	     * 		The layer to move down
	     */
	    public void moveLayerDown(Layer layer){
	    	
	    	//Cannot move background layer
	    	if(layer.getName().equals("Background")) return;
	    	
	    	addToQueue("layerdown", layer.getName());
	    	if(isImmediate()) requestRepaint();
	    }
	    
	    /**
	     * Set visibility of a layer
	     * @param name
	     * 		The unique name of the layer
	     * @param visible
	     * 		The visibility of the layer
	     */
	    public void setLayerVisibility(String name, boolean visible){
	    	if(visible) addToQueue("showLayer", name);
	    	else addToQueue("hideLayer", name);    
	    	if(isImmediate()) requestRepaint();
	    }
	    
	    /**
	     * Set the active layer. Every draw function is performed on this layer
	     * @param layer
	     * 		The layer to be active
	     */
	    public void setActiveLayer(Layer layer){
	    	currentLayer = layer;
	    	addToQueue("activeLayer", layer.getName());
	    	if(isImmediate()) requestRepaint();
	    }
	    
	    /**
	     * Get the active layer
	     * @return
	     * 		Returns the active layer
	     */
	    public Layer getActiveLayer(){
	    	return currentLayer;
	    }
	    
	    /**
	     * Get a list of the layers
	     * @return
	     */
	    public List<Layer> getLayers(){
	    	return Collections.unmodifiableList(layers);
	    }    
	    
	    /**
	     * Set the background of a layer
	     * @param layer
	     * 		The layer
	     * @param color
	     * 		The background color of the layer
	     * @param alpha
	     * 		The alpha value of the layer
	     */
	    public void setLayerBackground(Layer layer, String color, double alpha){
			if(layer == null || color == null || alpha < 0 || alpha > 1.0) return;
			
			//Do some color string checks
	    	if(color.contains("#")) color = color.replaceAll("#", "0x");	    	
	    	if(!color.contains("x")) color = "0x"+color;
	    	
	    	//Set the color
	    	addToQueue("layercolor", color);
	    	addToQueue("layeralpha", String.valueOf(alpha));
	    	
	    	if(isImmediate()) requestRepaint();
		}
		
	}
	
	public interface ClickListener{
		public void onClick(Component component, int x, int y);
	}
	
	/** The command history is needed when the the session is reloaded(F5) **/
	private Queue<Map<String, String>> commandHistory = new ArrayBlockingQueue<Map<String,String>>(10000);
	
	private Queue<Map<String, String>> changedValues = new ArrayBlockingQueue<Map<String,String>>(1000);
	
	private List<Layer> layers = new ArrayList<Layer>();	
	
	private Set<ValueChangeListener> valueGetters = new HashSet<ValueChangeListener>();
		
	private Graphics graphics = new Graphics();
	
	private Interactive interactive = new Interactive();
	
	private Layers Ilayers = new Layers();
	
	private Config configuration = new Config();
	
	private StringBuilder imageCache = new StringBuilder("<image></image>");
	
	private boolean cacheContentNeeded = false;
	
	private Long transactionCount = 0L;
	
	private Layer currentLayer;
	
	private Set<ClickListener> clickListeners = new HashSet<ClickListener>();
				
	/**
	 * 
	 */
	public PaintCanvas(){	
		this.addStyleName(CLASSNAME);
		
		//Create a random component identifier
		Random r = new Random();
		configuration.setComponentIdentifer(Long.toString(Math.abs(r.nextLong()), 36));
						
		/* Create the background layer which cannot be removed
		 * When creating a new layer it is automatically added the its canvas
		 */
		Layer background = new Layer("Background", this);
		layers.add(background);
		currentLayer = background;
			
		setImmediate(true);		
		requestRepaint();				
	}
	
	/**
	 * Constructor
	 * @param width
	 * 		The width of the canvas
	 * @param height
	 * 		The height of the canvas
	 */
	public PaintCanvas(String width, String height){		
		this.addStyleName(CLASSNAME);
		super.setWidth(width);
		super.setHeight(height);	
		
		//Create a random component identifier
		Random r = new Random();
		configuration.setComponentIdentifer(Long.toString(Math.abs(r.nextLong()), 36));
		
		/* Create the background layer which cannot be removed
		 * When creating a new layer it is automatically added the its canvas
		 */
		Layer background = new Layer("Background", this);
		layers.add(background);
		currentLayer = background;
					
		setImmediate(true);
		requestRepaint();
		
	}
	
	/**
	 * Constructor
	 * @param width
	 * 		The width of the canvas
	 * @param height
	 * 		The height of the canvas
	 * @param color
	 * 		The background color of the canvas	
	 */
	public PaintCanvas(String width, String height, Color color){
		this.addStyleName(CLASSNAME);
		super.setWidth(width);
		super.setHeight(height);
		
		//Create a random component identifier
		Random r = new Random();
		configuration.setComponentIdentifer(Long.toString(Math.abs(r.nextLong()), 36));
		
		/* Create the background layer which cannot be removed
		 * When creating a new layer it is automatically added the its canvas
		 * This layer is created automatically in the flash plugin at initialization
		 */
		Layer background = new Layer("Background", this);
		layers.add(background);
		currentLayer = background;
				
		//Set the background color
		setComponentBackgroundColor(color);		
		
		setImmediate(true);
		requestRepaint();
	}
	
	/**
	 * Constructor
	 * @param width
	 * 		The width of the canvas
	 * @param height
	 * 		The height of the canvas
	 * @param paperWidth
	 * 		The width of the image
	 * @param paperHeight
	 * 		The height of the image
	 */
	public PaintCanvas(String width, String height, int paperWidth, int paperHeight){
		this.addStyleName(CLASSNAME);
		super.setWidth(width);
		super.setHeight(height);
		
		//Create a random component identifier
		Random r = new Random();
		configuration.setComponentIdentifer(Long.toString(Math.abs(r.nextLong()), 36));
			
		/* Create the background layer which cannot be removed
		 * When creating a new layer it is automatically added the its canvas
		 */
		Layer background = new Layer("Background", this);
		layers.add(background);
		currentLayer = background;
		
		//Set the paper height and width
		configuration.setPaperWidth(paperWidth);
		configuration.setPaperHeight(paperHeight);
			
		setImmediate(true);
		requestRepaint();
	}
	
	/**
	 * Constructor
	 * @param width
	 * 		The width of the canvas
	 * @param height
	 * 		The height of the canvas
	 * @param paperWidth
	 * 		The width of the image
	 * @param paperHeight
	 * 		The height of the image
	 * @param color
	 * 		The background color of the image
	 */
	public PaintCanvas(String width, String height, int paperWidth, int paperHeight, Color color){
		this.addStyleName(CLASSNAME);
		super.setWidth(width);
		super.setHeight(height);	
		
		//Create a random component identifier
		Random r = new Random();
		configuration.setComponentIdentifer(Long.toString(Math.abs(r.nextLong()), 36));
		
		/* Create the background layer which cannot be removed
		 * When creating a new layer it is automatically added the its canvas
		 */
		Layer background = new Layer("Background", this);
		layers.add(background);
		currentLayer = background;
				
		//Set the paper height and width(layer size)
		configuration.setPaperWidth(paperWidth);
		configuration.setPaperHeight(paperHeight);
		
		//Set the background color		
		configuration.setComponentColor(color);	
		
		setImmediate(true);
		requestRepaint();		
	}
	
	/**
	 * Constructor
	 * @param width
	 * 		The width of the canvas
	 * @param height
	 * 		The height of the canvas
	 * @param paperWidth
	 * 		The width of the image
	 * @param paperHeight
	 * 		The height of the image
	 * @param interactive
	 * 		Is the canvas interactive
	 */
	public PaintCanvas(String width, String height, int paperWidth, int paperHeight, boolean interactive){
		this.addStyleName(CLASSNAME);
		super.setWidth(width);
		super.setHeight(height);
		
		//Create a random component identifier
		Random r = new Random();
		configuration.setComponentIdentifer(Long.toString(Math.abs(r.nextLong()), 36));
		
		/* Create the background layer which cannot be removed
		 * When creating a new layer it is automatically added the its canvas
		 */
		Layer background = new Layer("Background", this);
		layers.add(background);
		currentLayer = background;
						
		//Set the paper height and width(layer size)
		this.interactive.setPaperHeight(paperHeight);
		this.interactive.setPaperWidth(paperWidth);	
		
		/*
		 * Set the component in interactive mode which means that the user can draw on it
		 * with the mouse
		 */
		configuration.setInteractive(interactive);	
		
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
	public Class<?> getType() {		
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
        
        // Add id and transactionCount to all messages
        target.addVariable(this, "flashIdentifier", configuration.getComponentIdentifer());
        target.addVariable(this, "transactionId", transactionCount);   	          
        transactionCount++;
        
        //If the plugin has been initialized then it is okay to send the commands
        if(configuration.isInitializationComplete()){
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
        }        
        
        //Allow cached content to be sent even if initialization is incomplete
        //This might be needed in the initialization process
        else if(cacheContentNeeded && !configuration.isInitializationComplete()){
        	String cacheContent = XMLUtil.escapeXML(imageCache.toString());    		       	
        	target.addAttribute("cache", cacheContent);
        }
        
        //Else we sent the init data so it can initialize
        else{
        	target.addAttribute("pageWidth", configuration.getPaperWidth());
        	target.addAttribute("pageHeight", configuration.getPaperHeight());        	
        	target.addAttribute("cache-mode", configuration.getCacheMode().getId());           	
        	target.addAttribute("plugin", configuration.getPlugin().getId());
        	target.addAttribute("interactive", configuration.isInteractive());
        	
        	Color bgColor = configuration.getComponentColor();
                	
        	String red = Integer.toHexString(bgColor.getRed());
        	red = red.length() < 2 ? "0"+red : red;
        	
        	String green = Integer.toHexString(bgColor.getGreen());
        	green = green.length() < 2 ? "0"+green : green;
        	
        	String blue = Integer.toHexString(bgColor.getBlue());
        	blue = blue.length() < 2 ? "0"+blue : blue;        	    	
        	
        	target.addAttribute("componentColor", "0x"+red+green+blue);       	        	
        }                  	
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
    	
    	//Send cached image data
    	if(variables.containsKey("update-cache")){   		
    		//cache requested from refresh
    		if(variables.containsKey("init") && (Boolean)variables.get("init") == false && configuration.isInitializationComplete()){
    			cacheContentNeeded = true;
    			configuration.setInitializationComplete(false);
    			requestRepaint();
    		}    		
    		
    		// Cache requested after initialisation but not at a refresh
    		else if(configuration.isInitializationComplete()){    			
    			addToQueue("cache", imageCache.toString());    		
    			requestRepaint();
    		
    		// cache requested at component creation
    		} else {
    			cacheContentNeeded = true;
    			requestRepaint();
    		}
    	}
    	
    	//Plugin is ready event recieved
    	if(variables.containsKey("readyStatus")){    		    		 
    		boolean status = (Boolean)variables.get("readyStatus");    		    		    		
    		if(!status){
    			cacheContentNeeded = false;
    			transactionCount=0L;
    		}
    		
    		configuration.setInitializationComplete(status);      
    		    	
    		//Flush commands
    		requestRepaint();    	
    	}
    	
    	//Send the initialization data
    	if(variables.containsKey("initData")){
    		interactive.setPaperHeight(configuration.getPaperHeight());
    		interactive.setPaperWidth(configuration.getPaperWidth());
    		setComponentBackgroundColor(configuration.getComponentColor());
    		setInteractive(configuration.isInteractive());
    		setCacheMode(configuration.getCacheMode());
    		setPlugin(configuration.getPlugin());
    		configuration.setInitializationComplete(false);    		
    		requestRepaint();    		
    	}   	
    	
    	//Available fonts recieved
    	if(variables.containsKey("fontset")){
    		String[] fonts = (String[])variables.get("fontset");
    		configuration.setAvailableFonts(new HashSet<String>(Arrays.asList(fonts)));
    	}
    	
    	//Cache data recieved
    	if(variables.containsKey("set-cache")){
    		Object[] objects = (Object[])variables.get("set-cache");    		
    		if(objects.length > 0){
        		imageCache = new StringBuilder(objects[0].toString()); 
    		}  		    	
    	}
    	
    	//Click event recieved
    	if(variables.containsKey("click-event")){
    		Object[] coordinates = (Object[])variables.get("click-event");    
    		if(coordinates.length == 2){
       			for(ClickListener listener : clickListeners){
    				listener.onClick(this, Integer.parseInt(coordinates[0].toString()), 
    									   Integer.parseInt(coordinates[1].toString()));
    			}
    		}
    	}
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
    	configuration.setInteractive(interactive);    	
    	addToQueue("interactive", String.valueOf(interactive));
    	if(isImmediate()) requestRepaint();
    }
    
    public Interactive getInteractive() {
    	if(configuration.isInteractive()) 
    		return interactive;
    	else 
    		return null;
	}
    
    public void setComponentBackgroundColor(Color color){    	    	
    	configuration.setComponentColor(color);    
    	
    	String red = Integer.toHexString(color.getRed());
    	red = red.length() < 2 ? "0"+red : red;
    	
    	String green = Integer.toHexString(color.getGreen());
    	green = green.length() < 2 ? "0"+green : green;
    	
    	String blue = Integer.toHexString(color.getBlue());
    	blue = blue.length() < 2 ? "0"+blue : blue;
    	    	
    	addToQueue("componentColor", "0x"+red+green+blue);
    	if(isImmediate()) requestRepaint();
    }        
        
    public Layers getLayers(){
    	return Ilayers;
    }  
    
    public Config getConfiguration(){
    	return configuration;
    }
    
    public void setCacheMode(CacheMode mode){
    	configuration.setCacheMode(mode);
    	addToQueue("cache-mode", mode.getId());
    	if(isImmediate()) requestRepaint();
    }
    
    public void setPlugin(Plugin plugin){
    	configuration.setPlugin(plugin);
    	addToQueue("plugin", plugin.getId());
    	if(isImmediate()) requestRepaint();
    }
    
    public void setAutosaveTime(int seconds){
    	if(seconds < 0) return;
    	configuration.setAutosave(seconds);
    	addToQueue("autosave", String.valueOf(seconds));
    	if(isImmediate()) requestRepaint();
    }
    
    public void addListener(ClickListener listener){   	
    	
    	//Turn on click listening
    	if(clickListeners.size() == 0){
    		addToQueue("clicklisten", "true");
    		requestRepaint();
    	}    		
    	
    	clickListeners.add(listener);
    }
    
    public void removeListener(ClickListener listener){
    	clickListeners.remove(listener);
    	
    	//Turn click listening off
    	if(clickListeners.size() == 0){
    		addToQueue("clicklisten", "false");
    		requestRepaint();
    	}
    }    
}
