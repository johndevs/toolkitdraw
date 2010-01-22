package org.vaadin.toolkitdraw.tools;


import java.awt.Color;

import org.vaadin.toolkitdraw.components.TwinColorPicker;
import org.vaadin.toolkitdraw.components.flashcanvas.FlashCanvas;
import org.vaadin.toolkitdraw.components.flashcanvas.FlashCanvas.Interactive;
import org.vaadin.toolkitdraw.components.flashcanvas.enums.BrushType;
import org.vaadin.toolkitdraw.util.IconFactory;
import org.vaadin.toolkitdraw.util.IconFactory.Icons;

import com.vaadin.colorpicker.ColorSelector;
import com.vaadin.colorpicker.ColorSelector.ColorChangeListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;

public class Line extends Tool implements ValueChangeListener, ColorChangeListener {
			
	private static final long serialVersionUID = 1L;

	private Layout layout = new VerticalLayout();
	
	private Slider size;
	
	private Slider opacity;
	
	private TwinColorPicker colorpicker;
		
	public Line(FlashCanvas canvas){		
		this.canvas = canvas;
		this.layout.setMargin(true);
		
		button = new Button();
		button.setStyleName(Button.STYLE_LINK);
		button.setData(BrushType.LINE);
		button.setIcon(IconFactory.getIcon(Icons.ICON_LINE));
		
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
		return BrushType.LINE;
	}	
	
	@Override
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
	public String getName() {
		return "Line";
	}


	@Override
	public void changed(ColorSelector selector, Color color) {
		colorpicker.selectForegroundColorPicker();	
		canvas.getInteractive().setColor(colorToHex(colorpicker.getColor()));		
	}


	@Override
	public void sendCurrentSettings() {
		if(this.canvas == null) return;
		
		Interactive i = this.canvas.getInteractive();
		
		colorpicker.selectForegroundColorPicker();			
		i.setColor(colorToHex(colorpicker.getColor()));
		
		i.setToolSize(Double.parseDouble(size.getValue().toString()));
		
		i.setAlpha(1.0-Double.parseDouble(opacity.getValue().toString())/100.0);		
	
	}

}
