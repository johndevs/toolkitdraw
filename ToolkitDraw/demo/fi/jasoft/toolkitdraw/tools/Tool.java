package fi.jasoft.toolkitdraw.tools;

import java.awt.Color;
import java.io.Serializable;


import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.flashcanvas.FlashCanvas;
import fi.jasoft.flashcanvas.enums.BrushType;


public abstract class Tool implements com.vaadin.data.Property.ValueChangeListener, Serializable{

	private static final long serialVersionUID = 1L;
	
	public static String colorToHex(Color color){
		String red = Integer.toHexString(color.getRed());
		red = red.length() < 2 ? "0"+red : red;
		
		String green = Integer.toHexString(color.getGreen());
		green = green.length() < 2 ? "0"+green : green;
		
		String blue = Integer.toHexString(color.getBlue());
		blue = blue.length() < 2 ? "0"+blue :blue;
		
		return red+green+blue;
	}
	
	
	protected FlashCanvas canvas;
		
	protected Button button = new Button("Undefined");
			
	public Layout createToolOptions(){
		return new VerticalLayout();
	}
	
	public Button getButton(){
		return this.button;
	}
	
	public abstract BrushType getType();
	
	public abstract String getName();
	
	public abstract void sendCurrentSettings();
	
	public FlashCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(FlashCanvas canvas) {
		this.canvas = canvas;
	}
	
		
	
}
