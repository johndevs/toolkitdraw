package com.vaadin.toolkitdraw.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.Select;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.toolkitdraw.components.paintcanvas.PaintCanvas;
import com.vaadin.toolkitdraw.components.paintcanvas.enums.BrushType;
import com.vaadin.toolkitdraw.util.IconFactory;
import com.vaadin.toolkitdraw.util.IconFactory.Icons;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class Text extends Tool {
	
	private static final long serialVersionUID = 1L;

	private Layout layout = new VerticalLayout();
	
	private TextField size;
	
	private TextField color;
	
	private TextField fillColor;
	
	private TextField alpha;
	
	private ComboBox font;
	
	public Text(PaintCanvas canvas){
		this.canvas = canvas;
		
		button = new Button();
		button.setStyleName(Button.STYLE_LINK);
		button.setData(BrushType.TEXT);		
		button.setIcon(IconFactory.getIcon(Icons.ICON_TEXT));
		
		font = new ComboBox("Font type");
		font.addListener(this);
		font.setImmediate(true);
		layout.addComponent(font);
		
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
			canvas.getInteractive().setToolSize(Double.parseDouble(event.getProperty().getValue().toString()));				
		}
		else if(event.getProperty() == color){
			canvas.getInteractive().setColor(String.valueOf(event.getProperty().getValue()));
		}				
		else if(event.getProperty() == fillColor){
			canvas.getInteractive().setFillColor(String.valueOf(event.getProperty().getValue()));
		}
		else if(event.getProperty() == alpha){
			canvas.getInteractive().setAlpha(Double.parseDouble(event.getProperty().getValue().toString()));
		}
		else if(event.getProperty() == font){
			canvas.getInteractive().setFont(event.getProperty().getValue().toString());
		}
	}
	
	public Layout createToolOptions(){	
		
		//Populate the font select
		font.removeAllItems();
		
		//Sort fonts in alphabetical order
		List<String> fontNames = new ArrayList<String>(canvas.getConfiguration().getAvailableFonts());
		Collections.sort(fontNames);
		
		//Add the fonts to the select
		for(String fontName : fontNames)
			font.addItem(fontName);					
		
		return layout;
	}	
		
	public BrushType getType(){
		return BrushType.TEXT;
	}

}
