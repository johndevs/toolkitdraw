package com.vaadin.toolkitdraw.components.paintcanvas.events;

import com.vaadin.ui.Field;
import com.vaadin.ui.Field.ValueChangeEvent;


public class ImageXMLRecievedEvent extends ValueChangeEvent {

	private String xml = new String();
	
	public ImageXMLRecievedEvent(Field source, String xml) {
		super(source);	
		this.xml = xml;
	}

	public String getXML() {
		return xml;
	}
}
