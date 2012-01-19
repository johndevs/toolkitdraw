package fi.jasoft.flashcanvas;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import javax.xml.bind.DatatypeConverter;


import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Component;

import fi.jasoft.flashcanvas.client.ui.VFlashCanvas;
import fi.jasoft.flashcanvas.enums.BrushType;
import fi.jasoft.flashcanvas.enums.CacheMode;
import fi.jasoft.flashcanvas.enums.Plugin;
import fi.jasoft.flashcanvas.events.ImageUploadEvent;
import fi.jasoft.flashcanvas.events.ImageUploadEvent.ImageType;
import fi.jasoft.flashcanvas.events.ImageUploadListener;
import fi.jasoft.flashcanvas.util.XMLUtil;


/**
 * The FlashCanvas component for creating flash graphics easily. 
 * You can use this for the end users to draw on with different brushes
 * or programmatically draw on the canvas.
 * 
 * @author John Ahlroos, ITMill Oy Ltd 2010
 * @version 0.1
 */
@ClientWidget(VFlashCanvas.class)
public class FlashCanvas extends AbstractComponent{
		
	/**
	 * The graphics class is used to draw on the canvas from the server side
	 * This is mostly used when we want to present some information on the drawing
	 * component.
	 */
	public class Graphics implements Serializable{

		//Are we using the batch mode?
		private boolean batchMode = false;		
		
		private Queue<Map<String, String>> batch = new ArrayBlockingQueue<Map<String,String>>(1000);
		
		/**
		 * Add command into a batch
		 * @param command
		 * 		The command to execute
		 * @param value
		 * 		The value of the command
		 */
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
		 * @param coor
		 * 		The color of the line
		 */
		public void drawLine(int x1, int y1, int x2, int y2, Color color){
			
			if(batchMode){
				addToBatch("penColor", color2String(color));
				addToBatch("graphics-line", x1+","+y1+","+x2+","+y2);
			} else {
				addToQueue("penColor", color2String(color));
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
		
		/**
		 * Draws a square shape
		 * @param x
		 * 		The left upper corner
		 * @param y
		 * 		The left upper corner
		 * @param width
		 * 		The width
		 * @param height
		 * 		The height
		 * @param frameColor
		 * 		The color of the frame
		 * @param fillColor
		 * 		The fill color
		 */
		public void drawSquare(int x, int y, int width, int height, Color frameColor, Color fillColor){		
									
			if(batchMode){
				//We need to change the brush to a squeare before the operation so 
				//we can set the fillcolor
				addToBatch("brush", BrushType.SQUARE.toString());
				addToBatch("penSize","1");				
				addToBatch("penColor", color2String(frameColor));
				addToBatch("fillColor", color2String(fillColor));
				addToBatch("fillAlpha", "1");
				addToBatch("graphics-square", x+","+y+","+width+","+height);	
			} else {
				//We need to change the brush to a squeare before the operation so 
				//we can set the fillcolor
				addToQueue("brush", BrushType.SQUARE.toString());				
				addToQueue("penSize","1");
				addToQueue("penColor", color2String(frameColor));
				addToQueue("fillColor", color2String(fillColor));
				addToQueue("fillAlpha", "1");
				addToQueue("graphics-square", x+","+y+","+width+","+height);
			}			
			
			if(isImmediate() && !batchMode) requestRepaint();	
		}
		
		/**
		 * Draws an ellipse shape
		 * @param x
		 * 		The left upper corner
		 * @param y
		 * 		The left upper corner
		 * @param width
		 * 		The width of the shape
		 * @param height
		 * 		The height of the shape
		 */
		public void drawEllipse(int x, int y, int width, int height){
			if(batchMode) addToBatch("graphics-ellipse",  x+","+y+","+width+","+height);	
			else addToQueue("graphics-ellipse",  x+","+y+","+width+","+height);	
		}
		
		/**
		 * Draws an ellipse shape
		 * @param x
		 * 		The left upper corner
		 * @param y
		 * 		The left upper corner
		 * @param width
		 * 		The with of the shape
		 * @param height
		 * 		The height of the shape
		 * @param frameColor
		 * 		The frame color
		 * @param fillColor
		 * 		The fill color
		 */
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
		 * Draws an poluygon shape along the points given.
		 * If the first and last point is not the same the shape 
		 * will be closes automatically.
		 * @param x
		 * 		An array of x-coordinates
		 * @param y
		 * 		An array of y-coordinates
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
		
		/**
		 * Draws an poluygon shape along the points given.
		 * If the first and last point is not the same the shape 
		 * will be closes automatically.
		 * 
		 * @param x
		 * 		An array of x-coordinates
		 * @param y
		 * 		An array of y-coordinates
		 * @param color
		 * 		The color of the outer border
		 * @param fillColor
		 * 		The fill color
		 */
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
		public void drawText(String text, int x, int y, int width, int height, int fontSize, Color fontColor, double fontAlpha,  Color fillColor, double fillAlpha){
				    		    	
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
				addToBatch("penColor", color2String(fontColor));
				addToBatch("fillColor", color2String(fillColor));	
				addToBatch("fillAlpha", String.valueOf(fillAlpha));
				addToBatch("penAlpha", String.valueOf(fontAlpha));
				addToBatch("graphics-text", value.toString());	    		
	    	} else {
	    		addToQueue("brush", BrushType.TEXT.toString());
	    		addToQueue("penSize",String.valueOf(fontSize));				
	    		addToQueue("penColor", color2String(fontColor));
	    		addToQueue("fillColor", color2String(fillColor));	
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
		public void drawImage(String base64EncodedImage, int x, int y, int width, int height, double alpha){
			
			StringBuilder value = new StringBuilder();
			value.append(x);
			value.append(";");
			value.append(y);
			value.append(";");
			value.append(width);
			value.append(";");
			value.append(height);
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
		
		/**
		 * Bucket fills an area
		 * @param x
		 * 		The point to start filling
		 * @param y	
		 * 		The point to start filling
		 *@param color
		 *		The color of the fill
		 */
		public void fill(int x, int y, Color color, double alpha){
			
			StringBuilder value = new StringBuilder();
			value.append(x);
			value.append(";");
			value.append(y);
			value.append(";");
			value.append(color2String(color));
			value.append(";");
			value.append(alpha);
			
			if(batchMode){				
				addToBatch("graphics-fill", value.toString());
			} else {
				addToQueue("graphics-fill", value.toString());
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
	@SuppressWarnings("serial")
	public class Interactive implements Serializable{

		private BrushType currentBrush;
		
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
	    	currentBrush = brush;
	    	addToQueue("brush", brush.toString());
	    	if(isImmediate()) requestRepaint();
	    }
	    
	    public BrushType getCurrentBrush(){
	    	return currentBrush;
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
	    	
	    	int index = layers.indexOf(layer);
	    	Collections.swap(layers, index, index-1);
	    	
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
	    	
	    	int index = layers.indexOf(layer);
	    	Collections.swap(layers, index, index+1);
	    	
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
	
	/**
	 * The clickListener interface
	 * @author John Ahlroos, ITMill Oy Ltd 2010
	 * @since 0.1
	 */
	public interface ClickListener{
		public void onClick(Component component, int x, int y);
	}
	
	/**
	 * The brush listener interface
	 * @author John Ahlroos, ITMill Oy Ltd 2010
	 * @since 0.1
	 */
	public interface BrushListener{
		public void brushStart(Component component);
		public void brushEnd(Component component);
		public void loadingComplete(Component component);
	}
	
	/** The command history is needed when the the session is reloaded(F5) **/
	private Queue<Map<String, String>> commandHistory = new ArrayBlockingQueue<Map<String,String>>(10000);
	
	private Queue<Map<String, String>> changedValues = new ArrayBlockingQueue<Map<String,String>>(1000);
	
	private List<Layer> layers = new ArrayList<Layer>();	
		
	private Graphics graphics = new Graphics();
	
	private Interactive interactive = new Interactive();
	
	private Layers Ilayers = new Layers();
	
	private Config configuration = new Config();
	
	private StringBuilder imageCache = new StringBuilder("<image></image>");
	
	private boolean cacheContentNeeded = false;
	
	private Long transactionCount = 0L;
	
	private Layer currentLayer;
	
	private final List<ClickListener> clickListeners = new LinkedList<FlashCanvas.ClickListener>();
	private final List<BrushListener> brushListeners = new LinkedList<FlashCanvas.BrushListener>();
	private final List<ImageUploadListener> imageUploadListeners = new LinkedList<ImageUploadListener>();
				
	/**
	 * Default constructor
	 */
	public FlashCanvas(){	
		super.setWidth("100%");
		super.setHeight("100%");		
		
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
	 * @param base64EncodedImage
	 * 		Image encoded in Base64 encoding
	 */
	public FlashCanvas(String base64EncodedImage){
		this();
		
		addToQueue("open-image", base64EncodedImage.replaceAll("\r", "").replaceAll("\n", ""));
		
		requestRepaint();				
	}
	
	/**
	 * Constructor
	 * @param width
	 * 		The width of the canvas
	 * @param height
	 * 		The height of the canvas
	 */
	public FlashCanvas(String width, String height){		
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
	public FlashCanvas(String width, String height, Color color){
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
		background.setColor(FlashCanvas.color2String(color));
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
	public FlashCanvas(String width, String height, int paperWidth, int paperHeight){
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
	public FlashCanvas(String width, String height, int paperWidth, int paperHeight, Color color){
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
	public FlashCanvas(String width, String height, int paperWidth, int paperHeight, boolean interactive){
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
	 * Converts a color into a string representation
	 * @param c
	 * 		The color
	 * @return
	 * 		A CSS color string
	 */
	private static String color2String(Color c){
		String red = Integer.toHexString(c.getRed());
		String green = Integer.toHexString(c.getGreen());
		String blue = Integer.toHexString(c.getBlue());
		
		red = red.length() < 2 ? "0"+red : red;
		green = green.length() < 2 ? "0"+green : green;
		blue = blue.length() < 2 ? "0"+blue : blue;
		
		return "0x"+red+green+blue;
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
    		for(ImageUploadListener listener : imageUploadListeners){
    			listener.imageUploaded(new ImageUploadEvent(this, xml.getBytes(), ImageType.XML));
    		}
    	}
    	    	
    	 //PNG image recieved
    	if(variables.containsKey("getImagePNG")){    		
    		String base64 = variables.get("getImagePNG").toString();
			byte[] data = DatatypeConverter.parseBase64Binary(base64);
    		for(ImageUploadListener listener : imageUploadListeners){
    			listener.imageUploaded(new ImageUploadEvent(this, data, ImageType.PNG));
    		}
    	}    	
    	
    	//JPG image recieved
    	if(variables.containsKey("getImageJPG")){ 
    		String base64 = variables.get("getImageJPG").toString();
    		byte[] data = DatatypeConverter.parseBase64Binary(base64);
    		for(ImageUploadListener listener : imageUploadListeners){
    			listener.imageUploaded(new ImageUploadEvent(this, data, ImageType.JPG));
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
    		setComponentBackgroundColor(configuration.getComponentColor());
    		setInteractive(configuration.isInteractive());
    		setCacheMode(configuration.getCacheMode());
    		setPlugin(configuration.getPlugin());   		
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
    	
    	// Brush start event recieved
    	if(variables.containsKey("brush-start-event")){    		
    		for(BrushListener listener : brushListeners)
    			listener.brushStart(this);
    	}
    	
    	//Brush end event received
    	if(variables.containsKey("brush-end-event")){    	
    		for(BrushListener listener : brushListeners)
    			listener.brushEnd(this);
    	}
    	
    	//Brush loading complete event recieved
    	if(variables.containsKey("brush-loading-complete-event")){
    		for(BrushListener listener : brushListeners)
    			listener.loadingComplete(this);
    	}
    }    
     
	/**
	 * Get the current image. Triggers an ImageUploadEvent when image has been uploaded to server.
	 * 
	 * @param type
	 * 		The image type to convert the image to
	 * @param dpi
	 * 		The dpi of the image (if supported for type)
	 */
	public void getImage(ImageType type, int dpi){
		switch(type){
		case XML: addToQueue("getImageXML", ""); break;
		case JPG : addToQueue("getImageJPG", String.valueOf(dpi)); break;
		case PNG : addToQueue("getImagePNG", String.valueOf(dpi));
		}
		requestRepaint();
	}
    
    /**
     * Adds a brush listener
     * 
     * @param listener
     * 		The listener
     */
    public void addListener(BrushListener listener){
    	brushListeners.add(listener);
    }
    
    
    /**
     * Add image upload listener to listen for image upload events triggered by {@link FlashCanvas#getImage*(int)}

     * @param listener
     * 		The listener to add
     */
    public void addListener (ImageUploadListener listener){
    	if(!imageUploadListeners.contains(listener)){
    		imageUploadListeners.add(listener);
    	}
    }
    
    /**
     * Removes a image uploaded listener
     *  @param listener
     * 		The listener to remove
     */
    public void removeListener(ImageUploadListener listener){
    	imageUploadListeners.remove(listener);
    }
    
    /**
     * Removes a brush listener
     * @param listener
     * 		The listener to remove
     */
    public void removeListener(BrushListener listener){
    	brushListeners.remove(listener);
    }
    
    /**
     * Sets the canvas in interactive state.
     * If true the users can draw on the canvas, if false
     * only the application can draw on the canvas.
     * @param interactive
     */
    public void setInteractive(boolean interactive){
    	configuration.setInteractive(interactive);    	
    	addToQueue("interactive", String.valueOf(interactive));
    	if(isImmediate()) requestRepaint();
    }
    
    /**
     * Alias for setInteractive
     */
    @Override
    public void setReadOnly(boolean readOnly) {
    	setInteractive(!readOnly);
    }
    
    /**
     * Returns true if the canvas is read only.
     */
    @Override
    public boolean isReadOnly(){
    	return configuration.isInteractive();
    }
    
    /**
     * Returns the interactive controls
     * @return
     */
    public Interactive getInteractive() {
    	if(configuration.isInteractive()) 
    		return interactive;
    	else 
    		return null;
	}
    
    /**
     * Sets the background color of the component.
     * This is the color that is shown behind the image if the 
     * image is smaller than the canvas itself. By default the image is
     * the same size as the canvas so this is not visible.
     * 
     * @param color
     * 		The background color
     */
    public void setComponentBackgroundColor(Color color){    	    	
    	configuration.setComponentColor(color);    
    	    	    	
    	addToQueue("componentColor", FlashCanvas.color2String(color));
    	if(isImmediate()) requestRepaint();
    }        
       
    /**
     * Returns the defined layers
     * @return
     * 		The layers of the image
     */
    public Layers getLayers(){
    	return Ilayers;
    }  
    
    /**
     * Returns the configuration of the canvas
     * @return
     * 		The configuration
     */
    public Config getConfiguration(){
    	return configuration;
    }
    
    /**
     * Sets the cache mode of the canvas.
     * The cache mode is used to determine where the image
     * is saved between edits. Default is <b>CacheMode.Server</b>
     * @param mode
     * 		The cache mode 
     */
    public void setCacheMode(CacheMode mode){
    	configuration.setCacheMode(mode);
    	addToQueue("cache-mode", mode.getId());
    	if(isImmediate()) requestRepaint();
    }
    
    /**
     * Set which plugin should be used.
     * <b>At this time only the Flash plugin is supported!</b>
     * @param plugin
     */
    public void setPlugin(Plugin plugin){
    	configuration.setPlugin(plugin);
    	addToQueue("plugin", plugin.getId());
    	if(isImmediate()) requestRepaint();
    }
    
    /**
     * Set the time between autosaves. Default is 1 second
     * @param seconds
     * 		The amount of seconds between autosave
     */
    public void setAutosaveTime(int seconds){
    	if(seconds < 1) return;
    	configuration.setAutosave(seconds);
    	addToQueue("autosave", String.valueOf(seconds));
    	if(isImmediate()) requestRepaint();
    }
    
    /**
     * Adds a click listener
     * @param listener
     * 		The listening component
     */
    public void addListener(ClickListener listener){   	
    	
    	//Turn on click listening
    	if(clickListeners.size() == 0){
    		addToQueue("clicklisten", "true");
    		requestRepaint();
    	}    		
    	
    	clickListeners.add(listener);
    }
    
    /**
     * Removes a click listener
     * @param listener
     * 		The listening component to remove
     */
    public void removeListener(ClickListener listener){
    	clickListeners.remove(listener);
    	
    	//Turn click listening off
    	if(clickListeners.size() == 0){
    		addToQueue("clicklisten", "false");
    		requestRepaint();
    	}
    }    
}
