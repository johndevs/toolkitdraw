package com.vaadin.toolkitdraw.tools;

import java.awt.Color;

import com.vaadin.colorpicker.ColorPicker;
import com.vaadin.colorpicker.ColorSelector;
import com.vaadin.colorpicker.ColorSelector.ColorChangeListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.toolkitdraw.components.TwinColorPicker;
import com.vaadin.toolkitdraw.components.paintcanvas.PaintCanvas;
import com.vaadin.toolkitdraw.components.paintcanvas.enums.BrushType;
import com.vaadin.toolkitdraw.util.IconFactory;
import com.vaadin.toolkitdraw.util.IconFactory.Icons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class Pen extends Tool implements ValueChangeListener, ColorChangeListener{
	
	private static final long serialVersionUID = 1L;

	private Layout layout = new VerticalLayout();
	
	private TextField size;
		
	private TwinColorPicker colorpicker;
		
	public Pen(PaintCanvas canvas){
		
		this.canvas = canvas;
	
		button = new Button();
		button.setStyleName(Button.STYLE_LINK);
		button.setData(BrushType.PEN);
		button.setIcon(IconFactory.getIcon(Icons.ICON_PEN));
		
		colorpicker = new TwinColorPicker();
		colorpicker.addListener(this);
		layout.addComponent(colorpicker);
		
		size = new TextField("Size");
		size.addListener(this);
		size.setImmediate(true);
		size.setValue(1);
		layout.addComponent(size);			
	}
	
	public Layout createToolOptions(){	
		return layout;
	}	
		
	public BrushType getType(){
		return BrushType.PEN;
	}

	public void valueChange(ValueChangeEvent event) {		
		
		if(canvas == null) return;
		
		if(event.getProperty() == size){							
			canvas.getInteractive().setToolSize(Double.parseDouble(event.getProperty().getValue().toString()));				
		}			
	}

	@Override
	public void changed(ColorSelector selector, Color color) {
		colorpicker.selectForegroundColorPicker();	
		canvas.getInteractive().setColor(colorToHex(colorpicker.getColor()));		
	}
	
	
}
