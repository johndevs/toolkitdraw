package com.itmill.toolkitdraw.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.itmill.toolkit.terminal.PaintException;
import com.itmill.toolkit.terminal.PaintTarget;
import com.itmill.toolkit.ui.AbstractField;
import com.itmill.toolkit.ui.Component;
import com.itmill.toolkit.ui.Label;
import com.itmill.toolkitdraw.events.ImageXMLRecievedEvent;

public class PaintCanvas extends AbstractField implements Component{
		
	//The graphics object which can be used to draw to the canvas
	public class Graphics{				
		public void drawLine(int x1, int y1, int x2, int y2){			
			String value = changedVariables.get("graphics-line");
			if(value == null) value = x1+","+y1+","+x2+","+y2;
			else value += ";"+x1+","+y1+","+x2+","+y2;
			
			changedVariables.put("graphics-line", value);			
			if(isImmediate()) requestRepaint();
		}
		
		public void drawSquare(int x, int y, int width, int height){
			String value = changedVariables.get("graphics-square");
			if(value == null) value = x+","+y+","+width+","+height;
			else value += ";"+x+","+y+","+width+","+height;
			
			changedVariables.put("graphics-square", value);			
			if(isImmediate()) requestRepaint();		
		}		
		
		public void clear(){
			changedVariables.put("graphics-clear", "");
			if(isImmediate()) requestRepaint();		
		}
	}
	
	
	private Map<String, String> changedVariables = new HashMap<String, String>();
	
	private List<Layer> layers = new ArrayList<Layer>();	
	
	private Set<ValueChangeListener> valueGetters = new HashSet<ValueChangeListener>();
	
	private String width = "100%";
	
	private String height = "100%";
	
	public PaintCanvas(){	
		setImmediate(true);		
		
		//Add the background layer Layer automaically adds it to this canvas
		Layer background = new Layer("Background", this);
		layers.add(background);
				
		requestRepaint();
	}
	
	public PaintCanvas(String width, String height){				
		this();
		
		setHeight(width);
		setWidth(height);	
		
		requestRepaint();
	}
	
	public PaintCanvas(String width, String height, int paperWidth, int paperHeight){
		this();
		
		setHeight(width);
		setWidth(height);	
		
		setPaperHeight(paperHeight);
		setPaperWidth(paperWidth);		
		
		requestRepaint();		
	}
	
	public PaintCanvas(String width, String height, int paperWidth, int paperHeight, boolean interactive){
		this();
		
		setHeight(width);
		setWidth(height);	
		
		setPaperHeight(paperHeight);
		setPaperWidth(paperWidth);	
		
		setInteractive(true);
		
		requestRepaint();
	}	
	
	
	public Graphics getGraphics(){
		return new Graphics();
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
        
        //IF height and width has not changed then add the current variables
        if(changedVariables.get("height") == null) changedVariables.put("height", height);
        if(changedVariables.get("width") == null) changedVariables.put("width", width);
        
        
        for(String key: changedVariables.keySet()){
        	target.addVariable(this, key, changedVariables.get(key));
        }
        
        changedVariables.clear();       
    }
    
    /** Deserialize changes received from client. */
    public void changeVariables(Object source, Map variables) {
        
    	if(variables.containsKey("getImageXML")){
    		String xml = variables.get("getImageXML").toString();    		
    		
    		ImageXMLRecievedEvent event = new ImageXMLRecievedEvent(this,xml);
    		for(ValueChangeListener listener : valueGetters){
    			listener.valueChange(event);
    		}    		
    	}    
    }
    
    public void undo(){    	
    	changedVariables.put("undo", "true");
    	if(isImmediate()) requestRepaint();
  
    }
    
    public void redo(){
    	changedVariables.put("redo", "true");
    	if(isImmediate()) requestRepaint();
    }
    
    public void setPaperHeight(int h){
    	changedVariables.put("paperHeight", String.valueOf(h));
    	if(isImmediate()) requestRepaint();
    }
    
    public void setPaperWidth(int w){
    	changedVariables.put("paperWidth", String.valueOf(w));
    	if(isImmediate()) requestRepaint();
    }
    
    public void setToolSize(double size){
    	changedVariables.put("penSize", String.valueOf(size));
    	if(isImmediate()) requestRepaint();
    }

    public void setColor(String color){
    	
    	if(color.contains("#")){
    		color.replaceAll("#", "0x");
    	}
    	
    	if(!color.contains("x")){
    		color = "0x"+color;
    	}
    	
    	System.out.println("Color set to "+color);
    	
    	changedVariables.put("penColor", color);
    	if(isImmediate()) requestRepaint();
    }
    
    public void setFillColor(String color){
    	if(color.contains("#")){
    		color.replaceAll("#", "0x");
    	}
    	
    	if(!color.contains("x")){
    		color = "0x"+color;
    	}
    	
    	changedVariables.put("fillColor", color);
    	if(isImmediate()) requestRepaint();    	
    }
    
    public void setBrush(String brush){
    	changedVariables.put("brush", brush);
    	if(isImmediate()) requestRepaint();
    }
    
    public void addLayer(Layer layer){
    	changedVariables.put("newlayer", layer.getName());
    	layers.add(layer);
    	if(isImmediate()) requestRepaint();
    }
    
    public void removeLayer(Layer layer){
    	
    	//Cannot remove background layer
    	if(layer.getName().equals("Background")) return;
    	
    	changedVariables.put("removelayer", layer.getName());
    	layers.remove(layer);
    	if(isImmediate()) requestRepaint();
    }
    
    public void moveLayerUp(String name){
    	
    	//Cannot move background layer
    	if(name.equals("Background")) return;
    	
    	changedVariables.put("layerdown", name);
    	if(isImmediate()) requestRepaint();
    }
    
    public void moveLayerDown(String name){
    	
    	//Cannot move background layer
    	if(name.equals("Background")) return;
    	
    	changedVariables.put("layerup", name);
    	if(isImmediate()) requestRepaint();
    }
    
    public void setLayerVisibility(String name, boolean visible){
    	if(visible) changedVariables.put("showLayer", name);
    	else changedVariables.put("hideLayer", name);    
    	if(isImmediate()) requestRepaint();
    }
    
    public void setActiveLayer(Layer layer){
    	changedVariables.put("activeLayer", layer.getName());
    	if(isImmediate()) requestRepaint();
    }
    
    public List<Layer> getLayers(){
    	return layers;
    }    
    
    @Override
    public void setHeight(String h){
    	height = h;
    	changedVariables.put("height", h);
    	if(isImmediate()) requestRepaint();
   }
    
    @Override
    public void setWidth(String w){
    	width = w;
    	changedVariables.put("width", w);
    	if(isImmediate()) requestRepaint();
    }
    
    public void getImageXML(){
    	changedVariables.put("getImageXML", "");
    	if(isImmediate()) requestRepaint();
    }

    public void addListener(ValueChangeListener listener){
    	valueGetters.add(listener);
    }
    
    public void removeListener(ValueChangeListener listener){
    	valueGetters.remove(listener);
    }
    
    public void setInteractive(boolean interactive){
    	changedVariables.put("interactive", String.valueOf(interactive));
    	if(isImmediate()) requestRepaint();
    }
    
}
