package com.itmill.toolkitdraw.tools;

import com.itmill.toolkit.data.Property.ValueChangeEvent;
import com.itmill.toolkit.data.Property.ValueChangeListener;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkitdraw.components.PaintCanvas;
import com.itmill.toolkitdraw.tools.Tool.Type;
import com.itmill.toolkitdraw.util.IconFactory;
import com.itmill.toolkitdraw.util.IconFactory.Icons;

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
