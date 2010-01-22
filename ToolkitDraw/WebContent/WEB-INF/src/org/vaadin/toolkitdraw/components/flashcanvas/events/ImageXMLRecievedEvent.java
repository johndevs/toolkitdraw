package org.vaadin.toolkitdraw.components.flashcanvas.events;

import com.vaadin.ui.Field;
import com.vaadin.ui.Field.ValueChangeEvent;


public class ImageXMLRecievedEvent extends ValueChangeEvent {
	private static final long serialVersionUID = 1L;
	private String xml = new String();
	
	public ImageXMLRecievedEvent(Field source, String xml) {
		super(source);	
		this.xml = xml;
	}

	public String getXML() {
		return xml;
	}
}
