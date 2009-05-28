package com.itmill.toolkitdraw.events;

import com.itmill.toolkit.ui.Field;
import com.itmill.toolkit.ui.Field.ValueChangeEvent;

public class ImagePNGRecievedEvent extends ValueChangeEvent {

	private byte[] data;
	
	public ImagePNGRecievedEvent(Field source, byte[] data) {
		super(source);	
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}
}
