package com.vaadin.toolkitdraw.tools;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.toolkitdraw.components.paintcanvas.PaintCanvas;
import com.vaadin.toolkitdraw.components.paintcanvas.enums.BrushType;
import com.vaadin.toolkitdraw.util.IconFactory;
import com.vaadin.toolkitdraw.util.IconFactory.Icons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class Ellipse extends Tool implements ValueChangeListener {
	
	private static final long serialVersionUID = 1L;

	private Layout layout = new VerticalLayout();
	
	private TextField size;
	
	private TextField color;
	
	private TextField fillColor;
	
	public Ellipse(PaintCanvas canvas){
		
		this.canvas = canvas;
		
		button = new Button();
		button.setStyleName(Button.STYLE_LINK);
		button.setData(BrushType.ELLIPSE);
		button.setIcon(IconFactory.getIcon(Icons.ICON_ELLIPSE));
		
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
		
		fillColor = new TextField("Fill Color");
		fillColor.addListener(this);
		fillColor.setImmediate(true);
		fillColor.setValue("");
		layout.addComponent(fillColor);
		
	}
	
	public Layout createToolOptions(){	
		return layout;
	}	
		
	public BrushType getType(){
		return BrushType.ELLIPSE;
	}	
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		if(canvas == null) return;		
		
		if(event.getProperty() == size){							
			canvas.getInteractive().setToolSize(Double.parseDouble(event.getProperty().getValue().toString()));				
		}
		else if(event.getProperty() == color){
			canvas.getInteractive().setColor(String.valueOf(event.getProperty().getValue()));
		}		
		else if(event.getProperty() == fillColor){
			canvas.getInteractive().setFillColor(String.valueOf(event.getProperty().getValue()));
		}
	}

	@Override
	public String getName() {
		return "Ellipse";
	}

}
