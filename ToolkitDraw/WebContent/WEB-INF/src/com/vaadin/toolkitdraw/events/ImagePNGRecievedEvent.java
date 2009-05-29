package com.vaadin.toolkitdraw.events;

import com.vaadin.ui.Field.ValueChangeEvent;


public class ImagePNGRecievedEvent extends ValueChangeEvent {

	private byte[] data;
	
	public ImagePNGRecievedEvent(com.vaadin.ui.Field source, byte[] data) {
		super(source);	
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}
}
