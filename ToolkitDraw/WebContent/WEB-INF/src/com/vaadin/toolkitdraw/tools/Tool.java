package com.vaadin.toolkitdraw.tools;

import java.awt.Color;
import java.io.Serializable;

import com.vaadin.toolkitdraw.components.paintcanvas.PaintCanvas;
import com.vaadin.toolkitdraw.components.paintcanvas.enums.BrushType;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;


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
	
	
	protected PaintCanvas canvas;
		
	protected Button button = new Button("Undefined");
			
	public Layout createToolOptions(){
		return new VerticalLayout();
	}
	
	public Button getButton(){
		return this.button;
	}
	
	public abstract BrushType getType();
	
	public abstract String getName();
	
	public PaintCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(PaintCanvas canvas) {
		this.canvas = canvas;
	}
	
		
	
}
