package com.itmill.toolkitdraw.tools;

import com.itmill.toolkitdraw.components.PaintCanvas;
import com.itmill.toolkitdraw.tools.Tool.Type;
import com.itmill.toolkitdraw.util.IconFactory;
import com.itmill.toolkitdraw.util.IconFactory.Icons;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;

public class Polygon extends Tool implements ValueChangeListener {

	public Polygon(PaintCanvas canvas){
		this.canvas = canvas;		
		button = new Button();
		button.setData(Type.POLYGON);
		button.setIcon(IconFactory.getIcon(Icons.ICON_POLY));
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
