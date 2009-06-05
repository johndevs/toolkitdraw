package com.vaadin.toolkitdraw.gwt.client;

import com.vaadin.terminal.gwt.client.DefaultWidgetSet;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;


public class PaintCanvasWidgetSet extends DefaultWidgetSet {

	/** Creates a widget according to its class name. */
    public Paintable createWidget(UIDL uidl)
    {
        final String className = resolveWidgetTypeName(uidl);
        
        if ("com.vaadin.toolkitdraw.gwt.client.IPaintCanvas".equals(className)) 
        {        	
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
        	
        	if(uidl.hasVariable("width"))
        		width  =uidl.getStringVariable("width");
        	
        	if(uidl.hasVariable("height"))
        		height = uidl.getStringVariable("height");        	
        	
        	
        	IPaintCanvas canvas = new IPaintCanvas( width, 
        											height,
        											Integer.parseInt(paperWidth), 
        											Integer.parseInt(paperHeight), color);        	
        	return canvas;
        }     

        // Let the DefaultWidgetSet handle creation of default widgets
        return super.createWidget(uidl);
    }

    /** Resolves UIDL tag name to class name. */
    protected String resolveWidgetTypeName(UIDL uidl) 
    {
        final String tag = uidl.getTag();
        
        if ("paintcanvas".equals(tag)) 
        {
            return "com.vaadin.toolkitdraw.gwt.client.IPaintCanvas";
        }
    
        // Let the DefaultWidgetSet handle resolution of default widgets
        return super.resolveWidgetTypeName(uidl);
    }
	
}
