package com.vaadin.toolkitdraw.gwt.client;

import com.vaadin.terminal.gwt.client.DefaultWidgetSet;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;


public class PaintCanvasWidgetSet extends DefaultWidgetSet {

	/** Resolves UIDL tag name to widget class. */
    protected Class resolveWidgetType(UIDL uidl) {
        final String tag = uidl.getTag();
        if ("paintcanvas".equals(tag))
            return IPaintCanvas.class;
        // Let the DefaultWidgetSet handle resolution of
        // default widgets
        return super.resolveWidgetType(uidl);
    }
    
    /** Creates a widget instance by its class object. */
    public Paintable createWidget(UIDL uidl) {
        final Class type = resolveWidgetType(uidl);
        if (IPaintCanvas.class == type){
        	
        /*
        	//Default width and height of paper is fullsize
        	String paperWidth = "-1";
        	String paperHeight = "-1";
        	String color = "FF0000";
        	
        	if(uidl.hasVariable("paperWidth"))
        		paperWidth = uidl.getStringVariable("paperWidth");        	
        	
        	if(uidl.hasVariable("paperHeight"))
        		paperHeight = uidl.getStringVariable("paperHeight");     
        	
        	if(uidl.hasVariable("componentColor"))
        		color = uidl.getStringVariable("componentColor");        		
        	
        	String width = "100%";
        	String height = "100%";
        	String url = "";
        	
        	if(uidl.hasVariable("width"))
        		width  =uidl.getStringVariable("width");
        	
        	if(uidl.hasVariable("height"))
        		height = uidl.getStringVariable("height");        	
        	
        	if(uidl.hasVariable("flashurl"))
        		url = uidl.getStringVariable("flashurl");
        	  
        	
        	
        	IPaintCanvas canvas = new IPaintCanvas( width, 
        											height,
        											Integer.parseInt(paperWidth), 
        											Integer.parseInt(paperHeight), 
        											color,
        											uidl.getChildrenAsXML());    
        	*/
        	
        	
        	
        	String url = "/ToolkitDraw/VAADIN/widgetsets/com.vaadin.toolkitdraw.gwt.PaintCanvasWidgetSet/paintcanvas/PaintCanvas.swf";
        	IPaintCanvas canvas = new IPaintCanvas("100%","100%",-1,-1,"FF0000", url);
        	return canvas;        	
        }       
        
        // Let the DefaultWidgetSet handle creation of
        // default widgets
        return super.createWidget(uidl);
    }
}
