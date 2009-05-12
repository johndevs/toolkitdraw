package com.itmill.toolkitdraw.gwt.client;

import com.itmill.toolkit.terminal.gwt.client.DefaultWidgetSet;
import com.itmill.toolkit.terminal.gwt.client.Paintable;
import com.itmill.toolkit.terminal.gwt.client.UIDL;

public class PaintCanvasWidgetSet extends DefaultWidgetSet {

	/** Creates a widget according to its class name. */
    public Paintable createWidget(UIDL uidl)
    {
        final String className = resolveWidgetTypeName(uidl);
        
        if ("com.itmill.toolkitdraw.gwt.client.IPaintCanvas".equals(className)) 
        {        	
        	//Default width and height of paper is fullsize
        	String width = "-1";
        	String height = "-1";
        	
        	if(uidl.hasVariable("paperWidth"))
        		width = uidl.getStringVariable("paperWidth");        	
        	if(uidl.hasVariable("paperHeight"))
        		height = uidl.getStringVariable("paperHeight");        	
        	
        	IPaintCanvas canvas = new IPaintCanvas(Integer.parseInt(width), Integer.parseInt(height));        	
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
            return "com.itmill.toolkitdraw.gwt.client.IPaintCanvas";
        }
    
        // Let the DefaultWidgetSet handle resolution of default widgets
        return super.resolveWidgetTypeName(uidl);
    }
	

	
}
