package fi.jasoft.flashcanvas.events;

import java.io.Serializable;

import com.vaadin.ui.Component;

public class ImageUploadEvent implements Serializable {

	public enum ImageType { JPG, PNG, XML }
	
	private final byte[] data;
	
	private final ImageType type;
	
	public ImageUploadEvent(Component source, byte[] data, ImageType type) {
		this.data = data;
		this.type = type;
	}
	
	public byte[] getBytes(){
		return data;
	}
	
	public ImageType getType(){
		return type;
	}


}
