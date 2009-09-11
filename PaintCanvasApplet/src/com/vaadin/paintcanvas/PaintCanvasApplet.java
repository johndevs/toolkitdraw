package com.vaadin.paintcanvas;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

import netscape.javascript.JSObject;

import com.vaadin.paintcanvas.elements.Layer;
import com.vaadin.paintcanvas.enums.BrushType;
import com.vaadin.paintcanvas.util.GraphicsUtil;
import com.vaadin.paintcanvas.util.LayerUtil;

/**
 * The Class PaintCanvasApplet represents the extenal
 * interface to the applet which the PaintCanvasNativeUtil uses
 * to access the aplte
 */
public class PaintCanvasApplet extends Applet {
    
	private static final long serialVersionUID = 1L;
	
	private MouseHandler mouseHandler;
	
	private Color componentColor = Color.WHITE;
	
	private JSObject window;
		
    @Override
    public void init() {  	    
    	    	
    	
    	//Create the mouse listener
    	this.mouseHandler = new MouseHandler(this);
    	addMouseListener(this.mouseHandler);
    	addMouseMotionListener(this.mouseHandler);
    	addMouseWheelListener(this.mouseHandler);
    	
    	//Set the background color
    	if(getParameter("bgColor") != null){
    		String colorStr = getParameter("bgColor");
    		String red = colorStr.substring(2,4);
    		String green = colorStr.substring(4, 6);
    		String blue = colorStr.substring(6,8);
    		this.componentColor = new Color(Integer.parseInt(red, 16),
    										Integer.parseInt(green, 16),
    										Integer.parseInt(blue, 16));
    		setBackground(this.componentColor);    															
    	}    	
    	
    	//Add the background layer
    	Layer background = LayerUtil.addLayer("Background", true);
    	background.setWidth(300);
    	background.setHeight(400);
    	background.setX((getWidth()-300)/2);
    	background.setY((getHeight()-400)/2);
    	
    	//Add the default tool
    	GraphicsUtil.setBrush(BrushType.PEN);
    }

    @Override
    public void start() {
    	window = JSObject.getWindow(this);
    	
    }

    @Override
    public void stop() {
      
    }

    @Override
    public void destroy() {
        
    }
    
    @Override
    public void paint(Graphics g) {
    	GraphicsUtil.redraw(g);
    }
 
    public void undo(){ 
    	//TODO Create the implementation of this function
    
    }
    
    public void redo(){ 
    	//TODO Create the implementation of this function
    	
    }
    
    public void setPaperHeight(int height){
    	//TODO Create the implementation of this function
    	
    }
    
    public void setPaperWidth(int width){
    	//TODO Create the implementation of this function
    	
    }
    
    public void setBrushWidth(int width){
    	//TODO Create the implementation of this function
    	
    }
    
    public void setBrushColor(String color){
    	//TODO Create the implementation of this function
    
    }
    
    public void setBrush(String brush){
    	//TODO Create the implementation of this function
    	
    }
    
    public void setFillColor(String color){
    	//TODO Create the implementation of this function
    
    }
    
    public void setBrushAlpha(double alpha){
    	//TODO Create the implementation of this function
    	
    }
    
    public void setFont(String font){
    	//TODO Create the implementation of this function
    	
    }
    
    public void addNewLayer(String name){
    	//TODO Create the implementation of this function
    	
    }
    
    public void setLayerVisibility(String name, boolean visibility){
    	//TODO Create the implementation of this function
    	
    }
    
    public void selectLayer(String name){
    	//TODO Create the implementation of this function
    
    }
    
    public void setLayerBackgroundColor(String color){
    	//TODO Create the implementation of this function
   
    }
    
    public void setLayerBackgroundAlpha(double alpha){
    	//TODO Create the implementation of this function
    	
    }
    
    public String getImageXML(){
    	//TODO Create the implementation of this function
    	return "<brush></brush>";
    }
    
    public String getImagePNG(int dpi){
    	//TODO Create the implementation of this function
    	
    	return "";
    }
    
    public String getImageJPG(int dpi){
    	//TODO Create the implementation of this function
    	
    	return "";
    }
    
    public void setInteractive(boolean interactive){
    	//TODO Create the implementation of this function
    	
    }
    
    public void graphicsDrawLine(int x1, int y1, int x2, int y2){
    	//TODO Create the implementation of this function
    	
    }
    
    public void graphicsClear(){
    	//TODO Create the implementation of this function
    	
    }
    
    public void graphicsDrawPolygon(int[] x, int[] y, int length){
    	//TODO Create the implementation of this function
    	
    }
    
    public void graphicsDrawText(String text, int x, int y, int width, int height){
    	//TODO Create the implementation of this function
    	
    }
    
    public void graphicsDrawImage(String image, int x, int y, double alpha){
    	//TODO Create the implementation of this function
    
    }
    
    public void setComponentBackgroundColor(String color){
    	//TODO Create the implementation of this function
    
    }
    
    public boolean isReady(){
    	//TODO Create the implementation of this function
    
    	return true;
    }
    
    public void removeSelection(){
    	//TODO Create the implementation of this function
  
    }
    
    public void selectAll(){
    	//TODO Create the implementation of this function
  
    }
    
    public void setCacheMode(String mode){
    	//TODO Create the implementation of this function
    	
    }
    
    public void setImageCache(String cache){
    	//TODO Create the implementation of this function
    	
    }
    
    public JSObject getWindow(){
    	return window;
    }
}
