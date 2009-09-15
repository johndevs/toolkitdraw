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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;

public class Pen extends Tool implements ValueChangeListener, ColorChangeListener{
	
	private static final long serialVersionUID = 1L;

	private Layout layout = new VerticalLayout();
	
	private Slider size;
	
	private Slider opacity;
		
	private TwinColorPicker colorpicker;
		
	public Pen(PaintCanvas canvas){
		
		this.canvas = canvas;
		this.layout.setMargin(true);
	
		//Create the Pencil button 
		button = new Button();
		button.setStyleName(Button.STYLE_LINK);
		button.setData(BrushType.PEN);
		button.setIcon(IconFactory.getIcon(Icons.ICON_PEN));
		
		//Create the basic tools
		GridLayout basic = new GridLayout(2,2);
		basic.setColumnExpandRatio(0, 1);
		basic.setColumnExpandRatio(1, 5);
		basic.setWidth("100%");
		basic.setHeight("70px");
				
		colorpicker = new TwinColorPicker();
		colorpicker.addListener(this);
		basic.addComponent(colorpicker,0,0,0,1);
		basic.setComponentAlignment(colorpicker, Alignment.MIDDLE_LEFT);
		
		size = new Slider("Size",1,10);
		size.addListener(this);
		size.setSizeFull();
		size.setImmediate(true);		
		try{
			size.setValue(1);
		}catch(ValueOutOfBoundsException voobe){
			//NOP
		}		
		basic.addComponent(size, 1,0);
		
		opacity = new Slider("Opacity",0,100);
		opacity.addListener(this);
		opacity.setSizeFull();
		opacity.setImmediate(true);
		try {
			opacity.setValue(0);
		} catch (ValueOutOfBoundsException voobe) {
			//NOP
		}
		basic.addComponent(opacity,1,1);
				
		layout.addComponent(basic);		
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
		else if(event.getProperty() == opacity){
			canvas.getInteractive().setAlpha(1.0-Double.parseDouble(event.getProperty().getValue().toString())/100.0);
		}
	}

	@Override
	public void changed(ColorSelector selector, Color color) {
		colorpicker.selectForegroundColorPicker();	
		canvas.getInteractive().setColor(colorToHex(colorpicker.getColor()));		
	}

	@Override
	public String getName() {
		return "Pencil";
	}
	
	
}
