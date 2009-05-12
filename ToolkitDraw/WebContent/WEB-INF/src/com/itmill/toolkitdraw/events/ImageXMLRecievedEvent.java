package com.itmill.toolkitdraw.events;

import com.itmill.toolkit.ui.Field;
import com.itmill.toolkit.ui.Field.ValueChangeEvent;

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
