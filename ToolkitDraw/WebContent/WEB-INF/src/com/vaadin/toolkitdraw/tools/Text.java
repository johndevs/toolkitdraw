package com.vaadin.toolkitdraw.tools;

import com.sun.org.apache.bcel.internal.generic.Select;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.toolkitdraw.components.paintcanvas.PaintCanvas;
import com.vaadin.toolkitdraw.components.paintcanvas.PaintCanvas.BrushType;
import com.vaadin.toolkitdraw.util.IconFactory;
import com.vaadin.toolkitdraw.util.IconFactory.Icons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class Text extends Tool {

	private Layout layout = new VerticalLayout();
	
	private TextField size;
	
	private TextField color;
	
	private TextField fillColor;
	
	private TextField alpha;
	
	private Select font;
	
	public Text(PaintCanvas canvas){
		this.canvas = canvas;
		
		button = new Button();
		button.setData(PaintCanvas.BrushType.TEXT);		
		button.setIcon(IconFactory.getIcon(Icons.ICON_TEXT));
		
		size = new TextField("Font Size");
		size.addListener(this);
		size.setImmediate(true);
		size.setValue(12);
		layout.addComponent(size);		
		
		color = new TextField("Font Color");
		color.addListener(this);
		color.setImmediate(true);
		color.setValue("000000");
		layout.addComponent(color);
		
		fillColor = new TextField("Background Color");
		fillColor.addListener(this);
		fillColor.setImmediate(true);
		fillColor.setValue("FFFFFF");
		layout.addComponent(fillColor);
		
		alpha = new TextField("Background Alpha");
		alpha.addListener(this);
		alpha.setImmediate(true);
		alpha.setValue("0");
		layout.addComponent(alpha);
		
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
		else if(event.getProperty() == fillColor){
			canvas.setFillColor(String.valueOf(event.getProperty().getValue()));
		}
		else if(event.getProperty() == alpha){
			canvas.setAlpha(Double.parseDouble(event.getProperty().getValue().toString()));
		}
	}
	
	public Layout createToolOptions(){	
		return layout;
	}	
		
	public PaintCanvas.BrushType getType(){
		return BrushType.TEXT;
	}

}
