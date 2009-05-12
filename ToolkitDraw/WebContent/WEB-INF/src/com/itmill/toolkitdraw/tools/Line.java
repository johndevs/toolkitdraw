package com.itmill.toolkitdraw.tools;

import com.itmill.toolkit.data.Property.ValueChangeEvent;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.Layout;
import com.itmill.toolkit.ui.TextField;
import com.itmill.toolkit.ui.VerticalLayout;
import com.itmill.toolkitdraw.components.PaintCanvas;
import com.itmill.toolkitdraw.tools.Tool.Type;

public class Line extends Tool {
			
	private Layout layout = new VerticalLayout();
	
	private TextField size;
	
	private TextField color;
		
	public Line(PaintCanvas canvas){		
		this.canvas = canvas;
		
		button = new Button("Line");
		button.setData(Type.LINE);
		
		size = new TextField("Size");
		size.addListener(this);
		size.setImmediate(true);
		size.setValue(1);
		layout.addComponent(size);		
		
		color = new TextField("Color");
		color.addListener(this);
		color.setImmediate(true);
		color.setValue("000000");
		layout.addComponent(color);		
	}	
	
	
	public Layout createToolOptions(){
		return layout;
	}
	
		
	public Type getType(){
		return Type.LINE;
	}	
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		if(canvas == null) return;
		
		if(event.getProperty() == size){							
			canvas.setToolSize(Double.parseDouble(event.getProperty().getValue().toString()));				
		}
		else if(event.getProperty() == color){
			canvas.setColor(String.valueOf(event.getProperty().getValue()));
		}		
		
	}

}
