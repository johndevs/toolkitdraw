package com.itmill.toolkitdraw.tools;

import com.itmill.toolkit.data.Property.ValueChangeEvent;
import com.itmill.toolkit.data.Property.ValueChangeListener;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkitdraw.components.PaintCanvas;
import com.itmill.toolkitdraw.tools.Tool.Type;

public class Polygon extends Tool implements ValueChangeListener {

	public Polygon(PaintCanvas canvas){
		this.canvas = canvas;		
		button = new Button("Poly");
		button.setData(Type.POLYGON);
	}
	
	@Override
	public Type getType(){
		return Type.POLYGON;
	}	
	
	
	
	
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
