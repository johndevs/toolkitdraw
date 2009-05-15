package com.itmill.toolkitdraw.tools;

import com.itmill.toolkit.data.Property.ValueChangeListener;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.Layout;
import com.itmill.toolkit.ui.VerticalLayout;
import com.itmill.toolkitdraw.components.PaintCanvas;


public abstract class Tool implements ValueChangeListener{

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
