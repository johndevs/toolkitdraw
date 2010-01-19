package com.vaadin.toolkitdraw.components.flashcanvas.events;

import com.vaadin.ui.Field.ValueChangeEvent;

public class ImageJPGRecievedEvent extends ValueChangeEvent {
	private static final long serialVersionUID = 1L;
	private byte[] data;
	
	public ImageJPGRecievedEvent(com.vaadin.ui.Field source, byte[] data) {
		super(source);	
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}
}
