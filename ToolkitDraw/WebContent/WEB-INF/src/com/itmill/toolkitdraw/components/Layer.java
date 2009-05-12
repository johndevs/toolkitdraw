package com.itmill.toolkitdraw.components;

import com.itmill.toolkit.ui.CheckBox;
import com.itmill.toolkit.ui.Label;
import com.itmill.toolkit.ui.OrderedLayout;

public class Layer {

	private boolean visible;
	private String name;
	private PaintCanvas canvas;
	
	public PaintCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(PaintCanvas canvas) {
		this.canvas = canvas;
	}

	public Layer(String name, PaintCanvas canvas) {			
		this.name = name;
		visible = true;
		this.canvas = canvas;
	}
	
	public void setVisible(boolean visible){	
		this.visible = visible;
		this.canvas.setLayerVisibility(name, visible);
	}
	
	public boolean getVisible(){
		return visible;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		
		//TODO Call canvas set name function
		this.name = name;
	}

	
}
