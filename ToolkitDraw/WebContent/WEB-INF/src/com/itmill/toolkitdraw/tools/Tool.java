package com.itmill.toolkitdraw.tools;

import com.itmill.toolkitdraw.components.PaintCanvas;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;


public abstract class Tool implements com.vaadin.data.Property.ValueChangeListener{

	protected PaintCanvas canvas;
		
	protected Button button = new Button("Undefined");
		
	public static enum Type { NONE, PEN, SQUARE, ELLIPSE, LINE, POLYGON };
	
	public Layout createToolOptions(){
		return new VerticalLayout();
	}
	
	public Button getButton(){
		return this.button;
	}
	
	public Type getType(){
		return Type.NONE;
	}	
	
	public PaintCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(PaintCanvas canvas) {
		this.canvas = canvas;
	}
	
	
}
