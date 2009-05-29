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
        	String width = "-1";
        	String height = "-1";
        	String color = "FFFFFF";
        	
        	if(uidl.hasVariable("paperWidth"))
        		width = uidl.getStringVariable("paperWidth");        	
        	if(uidl.hasVariable("paperHeight"))
        		height = uidl.getStringVariable("paperHeight");     
        	if(uidl.hasVariable("componentColor")){
        		color = uidl.getStringVariable("componentColor");
        		System.out.println(color);
        	}	
        	IPaintCanvas canvas = new IPaintCanvas(Integer.parseInt(width), Integer.parseInt(height), color);        	
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
