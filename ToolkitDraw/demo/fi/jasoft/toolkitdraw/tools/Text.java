package fi.jasoft.toolkitdraw.tools;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import com.vaadin.addon.colorpicker.ColorSelector;
import com.vaadin.addon.colorpicker.ColorPicker.ColorChangeListener;
import com.vaadin.addon.colorpicker.events.ColorChangeEvent;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;

import fi.jasoft.flashcanvas.FlashCanvas;
import fi.jasoft.flashcanvas.FlashCanvas.Interactive;
import fi.jasoft.flashcanvas.enums.BrushType;
import fi.jasoft.toolkitdraw.ToolkitDrawApplication;
import fi.jasoft.toolkitdraw.ToolkitDrawWindow;
import fi.jasoft.toolkitdraw.components.TwinColorPicker;
import fi.jasoft.toolkitdraw.util.IconFactory;
import fi.jasoft.toolkitdraw.util.IconFactory.Icons;

public class Text extends Tool implements ValueChangeListener, ColorChangeListener, ClickListener{
	
	private static final long serialVersionUID = 1L;

	private Layout layout = new VerticalLayout();
	
	private Slider size;
	
	private TwinColorPicker colorpicker;
	
	private Slider backgroundOpacity;
		
	private ComboBox font;
	
	private CheckBox disableFillcolor;
	
	private Button endTool;
	
	public Text(FlashCanvas canvas){
		this.canvas = canvas;
		this.layout.setMargin(true);
		
		button = new Button();
		button.setStyleName(Button.STYLE_LINK);
		button.setData(BrushType.TEXT);		
		button.setIcon(IconFactory.getIcon(Icons.ICON_TEXT));
				
		endTool = new Button("Finish editing", this);
				
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
		
		backgroundOpacity = new Slider("Background Opacity",0,100);
		backgroundOpacity.addListener(this);
		backgroundOpacity.setSizeFull();
		backgroundOpacity.setImmediate(true);
		try {
			backgroundOpacity.setValue(0);
		} catch (ValueOutOfBoundsException voobe) {
			//NOP
		}
		basic.addComponent(backgroundOpacity,1,1);
				
		layout.addComponent(basic);	
		
		//More options
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setWidth("100%");
				
		disableFillcolor = new CheckBox("Filled Background",this);
		disableFillcolor.setImmediate(true);
		disableFillcolor.setValue(false);
		hlayout.addComponent(disableFillcolor);
				
		layout.addComponent(hlayout);		
		
		font = new ComboBox("Font type");
		font.addListener(this);
		font.setImmediate(true);
		layout.addComponent(font);
	}
	
	public void valueChange(ValueChangeEvent event) {
		if(canvas == null) return;			
		
		if(event.getProperty() == size){							
			canvas.getInteractive().setToolSize(Double.parseDouble(event.getProperty().getValue().toString()));				
		}
		else if(event.getProperty() == font){
			if(canvas.getInteractive() != null && event.getProperty().getValue() != null)
				canvas.getInteractive().setFont(event.getProperty().getValue().toString());
		}		
		else if(event.getProperty() == backgroundOpacity){
			canvas.getInteractive().setFillAlpha(1.0-Double.parseDouble(event.getProperty().getValue().toString())/100.0);
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
		
		//Set the initial font to the first font
		font.select(fontNames.get(0));
		
		// Add the finish tool button
		HorizontalLayout bar = ((ToolkitDrawWindow)getButton().getWindow()).getBottomBar();
				
		Label lbl = new Label("Text options:");	
		bar.addComponent(lbl);
		bar.setComponentAlignment(lbl, Alignment.MIDDLE_LEFT);
		
		bar.addComponent(endTool);
		bar.setComponentAlignment(endTool, Alignment.MIDDLE_RIGHT);
			
		return layout;
	}	
		
	public BrushType getType(){
		return BrushType.TEXT;
	}

	@Override
	public String getName() {
		return "Text";
	}

	public void buttonClick(ClickEvent event) {
		if(event.getButton() == disableFillcolor){
			boolean state = event.getButton().booleanValue();
			if(state){
				colorpicker.selectBackgroundColorPicker();
				canvas.getInteractive().setFillColor(colorToHex(colorpicker.getColor()));	
				canvas.getInteractive().setFillAlpha(1.0-Double.parseDouble(backgroundOpacity.getValue().toString())/100.0);
			} else {
				canvas.getInteractive().setFillColor(null);
				canvas.getInteractive().setFillAlpha(1.0-Double.parseDouble(backgroundOpacity.getValue().toString())/100.0);
			}
		}		
		else if(event.getButton() == endTool){
			canvas.getInteractive().finish();
		}
	}

	@Override
	public void sendCurrentSettings() {
		if(this.canvas == null) return;
		
		Interactive i = this.canvas.getInteractive();
		
		colorpicker.selectForegroundColorPicker();			
		i.setColor(colorToHex(colorpicker.getColor()));
		
		i.setToolSize(Double.parseDouble(size.getValue().toString()));		
		i.setAlpha(1.0-Double.parseDouble(backgroundOpacity.getValue().toString())/100.0);	
					
		boolean state = disableFillcolor.booleanValue();
		if(state){
			colorpicker.selectBackgroundColorPicker();
			i.setFillColor(colorToHex(colorpicker.getColor()));	
			i.setFillAlpha(1.0-Double.parseDouble(backgroundOpacity.getValue().toString())/100.0);
		} else {
			i.setFillColor(null);
			i.setFillAlpha(1.0-Double.parseDouble(backgroundOpacity.getValue().toString())/100.0);
		}
		
		i.setFont(font.getValue().toString());
	}

	public void colorChanged(ColorChangeEvent event) {
		colorpicker.selectForegroundColorPicker();	
		canvas.getInteractive().setColor(colorToHex(colorpicker.getColor()));	
		
		if(disableFillcolor.booleanValue()){
			colorpicker.selectBackgroundColorPicker();
			canvas.getInteractive().setFillColor(colorToHex(colorpicker.getColor()));
			canvas.getInteractive().setFillAlpha(1.0-Double.parseDouble(backgroundOpacity.getValue().toString())/100.0);
		}				
	}


}
