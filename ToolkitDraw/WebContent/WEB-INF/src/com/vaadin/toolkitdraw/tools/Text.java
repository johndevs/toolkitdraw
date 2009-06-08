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
	
	private Select font;
	
	public Text(PaintCanvas canvas){
		this.canvas = canvas;
		
		button = new Button();
		button.setData(PaintCanvas.BrushType.TEXT);
		button.setCaption("T");
		//button.setIcon(IconFactory.getIcon(Icons.ICON_SQUARE));
		
		size = new TextField("Font Size");
		size.addListener(this);
		size.setImmediate(true);
		size.setValue(1);
		layout.addComponent(size);		
		
		color = new TextField("Font Color");
		color.addListener(this);
		color.setImmediate(true);
		color.setValue("000000");
		layout.addComponent(color);
		
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		// TODO Auto-generated method stub

	}
	
	public Layout createToolOptions(){	
		return layout;
	}	
		
	public PaintCanvas.BrushType getType(){
		return BrushType.TEXT;
	}

}
