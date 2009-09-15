package com.vaadin.toolkitdraw.tools;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.Select;
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
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;

public class Text extends Tool implements ValueChangeListener, ColorChangeListener, ClickListener{
	
	private static final long serialVersionUID = 1L;

	private Layout layout = new VerticalLayout();
	
	private Slider size;
	
	private TwinColorPicker colorpicker;
	
	private Slider opacity;
	
	private ComboBox font;
	
	private CheckBox disableFillcolor;
	
	public Text(PaintCanvas canvas){
		this.canvas = canvas;
		this.layout.setMargin(true);
		
		button = new Button();
		button.setStyleName(Button.STYLE_LINK);
		button.setData(BrushType.TEXT);		
		button.setIcon(IconFactory.getIcon(Icons.ICON_TEXT));
		
		
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
		
		size = new Slider("Font Size",6,60);
		size.addListener(this);
		size.setSizeFull();
		size.setImmediate(true);		
		try{
			size.setValue(12);
		}catch(ValueOutOfBoundsException voobe){
			//NOP
		}		
		basic.addComponent(size, 1,0);
		
		opacity = new Slider("Background Opacity",0,100);
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
		
		//More options
		disableFillcolor = new CheckBox("Filled Background",this);
		disableFillcolor.setImmediate(true);
		disableFillcolor.setValue(false);
		
		layout.addComponent(disableFillcolor);
		
		font = new ComboBox("Font type");
		font.addListener(this);
		font.setImmediate(true);
		layout.addComponent(font);
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		if(canvas == null) return;			
		
		if(event.getProperty() == size){							
			canvas.getInteractive().setToolSize(Double.parseDouble(event.getProperty().getValue().toString()));				
		}
		else if(event.getProperty() == font){
			canvas.getInteractive().setFont(event.getProperty().getValue().toString());
		}		
		else if(event.getProperty() == opacity){
			canvas.getInteractive().setAlpha(1.0-Double.parseDouble(event.getProperty().getValue().toString())/100.0);
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

	@Override
	public String getName() {
		return "Text";
	}

	@Override
	public void changed(ColorSelector selector, Color color) {
		colorpicker.selectForegroundColorPicker();	
		canvas.getInteractive().setColor(colorToHex(colorpicker.getColor()));	
		
		if(disableFillcolor.booleanValue()){
			colorpicker.selectBackgroundColorPicker();
			canvas.getInteractive().setFillColor(colorToHex(colorpicker.getColor()));				
		}		
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if(event.getButton() == disableFillcolor){
			boolean state = event.getButton().booleanValue();
			if(state){
				colorpicker.selectBackgroundColorPicker();
				canvas.getInteractive().setFillColor(colorToHex(colorpicker.getColor()));				
			} else {
				canvas.getInteractive().setFillColor(null);
			}
		}			
		
	}

}
