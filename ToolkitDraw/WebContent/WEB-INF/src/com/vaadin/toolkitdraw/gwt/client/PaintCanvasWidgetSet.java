package com.vaadin.toolkitdraw.gwt.client;

import com.google.gwt.core.client.GWT;
import com.vaadin.terminal.gwt.client.ApplicationConfiguration;
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
            IPaintCanvas canvas = new IPaintCanvas();
        	return canvas;        	
        }       
        
        // Let the DefaultWidgetSet handle creation of
        // default widgets
        return super.createWidget(uidl);
    }
}
