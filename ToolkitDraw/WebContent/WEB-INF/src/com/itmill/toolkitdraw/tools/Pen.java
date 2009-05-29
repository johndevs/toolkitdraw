package com.itmill.toolkitdraw.tools;

import java.text.Format;

import com.itmill.toolkitdraw.components.PaintCanvas;
import com.itmill.toolkitdraw.util.IconFactory;
import com.itmill.toolkitdraw.util.IconFactory.Icons;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class Pen extends Tool implements ValueChangeListener{
	
	private Layout layout = new VerticalLayout();
	
	private TextField size;
	
	private TextField color;
		
	public Pen(PaintCanvas canvas){
		
		this.canvas = canvas;
	
		button = new Button();
		button.setData(Type.PEN);
		button.setIcon(IconFactory.getIcon(Icons.ICON_PEN));
		
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
		return Type.PEN;
	}

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
