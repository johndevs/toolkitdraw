package com.vaadin.toolkitdraw.tools;

import java.awt.Color;

import com.vaadin.colorpicker.ColorSelector;
import com.vaadin.colorpicker.ColorSelector.ColorChangeListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.toolkitdraw.components.TwinColorPicker;
import com.vaadin.toolkitdraw.components.paintcanvas.PaintCanvas;
import com.vaadin.toolkitdraw.components.paintcanvas.PaintCanvas.Interactive;
import com.vaadin.toolkitdraw.components.paintcanvas.enums.BrushType;
import com.vaadin.toolkitdraw.util.IconFactory;
import com.vaadin.toolkitdraw.util.IconFactory.Icons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;

public class Fill extends Tool implements ValueChangeListener, ColorChangeListener{
	
	private static final long serialVersionUID = 1L;

	private Layout layout = new VerticalLayout();
					
	private Slider opacity;

	private TwinColorPicker colorpicker;	
		
	public Fill(PaintCanvas canvas){
		
		this.canvas = canvas;
		this.layout.setMargin(true);
	
		button = new Button();
		button.setStyleName(Button.STYLE_LINK);
		button.setData(BrushType.FILL);
		button.setIcon(IconFactory.getIcon(Icons.ICON_FILL));
		button.setCaption("");
		
		//Create the basic tools
		GridLayout basic = new GridLayout(2,1);
		basic.setColumnExpandRatio(0, 1);
		basic.setColumnExpandRatio(1, 5);
		basic.setWidth("100%");
		basic.setHeight("70px");
				
		colorpicker = new TwinColorPicker();
		colorpicker.addListener(this);
		basic.addComponent(colorpicker,0,0);
		basic.setComponentAlignment(colorpicker, Alignment.MIDDLE_LEFT);
		
		opacity = new Slider("Opacity",0,100);
		opacity.addListener(this);
		opacity.setSizeFull();
		opacity.setImmediate(true);
		try {
			opacity.setValue(0);
		} catch (ValueOutOfBoundsException voobe) {
			//NOP
		}
		basic.addComponent(opacity,1,0);
		basic.setComponentAlignment(opacity, Alignment.MIDDLE_LEFT);
				
		layout.addComponent(basic);			
	}
	
	@Override
	public BrushType getType() {
		return BrushType.FILL;
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		if(canvas == null) return;			
		
		if(event.getProperty() == opacity){
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
		return "Fill area";
	}
	
	public Layout createToolOptions(){	
		return layout;
	}

	@Override
	public void sendCurrentSettings() {
		if(this.canvas == null) return;
		
		Interactive i = this.canvas.getInteractive();
		
		colorpicker.selectForegroundColorPicker();			
		i.setColor(colorToHex(colorpicker.getColor()));		
	}	
}
