package com.vaadin.toolkitdraw.tools;

import java.io.Serializable;

import com.vaadin.toolkitdraw.components.paintcanvas.PaintCanvas;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;


public abstract class Tool implements com.vaadin.data.Property.ValueChangeListener, Serializable{

	protected PaintCanvas canvas;
		
	protected Button button = new Button("Undefined");
			
	public Layout createToolOptions(){
		return new VerticalLayout();
	}
	
	public Button getButton(){
		return this.button;
	}
	
	public abstract PaintCanvas.BrushType getType();
	
	public PaintCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(PaintCanvas canvas) {
		this.canvas = canvas;
	}
	
	
}
