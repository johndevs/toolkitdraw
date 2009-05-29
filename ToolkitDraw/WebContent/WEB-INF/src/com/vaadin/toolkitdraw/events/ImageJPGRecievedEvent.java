package com.vaadin.toolkitdraw.events;

import com.vaadin.ui.Field.ValueChangeEvent;

public class ImageJPGRecievedEvent extends ValueChangeEvent {

	private byte[] data;
	
	public ImageJPGRecievedEvent(com.vaadin.ui.Field source, byte[] data) {
		super(source);	
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}
}
