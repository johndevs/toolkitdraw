package fi.jasoft.toolkitdraw.tools;

import java.awt.Color;


import com.vaadin.addon.colorpicker.ColorSelector;
import com.vaadin.addon.colorpicker.ColorPicker.ColorChangeListener;
import com.vaadin.addon.colorpicker.events.ColorChangeEvent;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;

import fi.jasoft.flashcanvas.FlashCanvas;
import fi.jasoft.flashcanvas.FlashCanvas.Interactive;
import fi.jasoft.flashcanvas.enums.BrushType;
import fi.jasoft.toolkitdraw.components.TwinColorPicker;
import fi.jasoft.toolkitdraw.util.IconFactory;
import fi.jasoft.toolkitdraw.util.IconFactory.Icons;

public class Fill extends Tool implements ValueChangeListener, ColorChangeListener{
	
	private static final long serialVersionUID = 1L;

	private Layout layout = new VerticalLayout();
					
	private Slider opacity;

	private TwinColorPicker colorpicker;	
		
	public Fill(FlashCanvas canvas){
		
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
	
	public void valueChange(ValueChangeEvent event) {
		if(canvas == null) return;			
		
		if(event.getProperty() == opacity){
			canvas.getInteractive().setAlpha(1.0-Double.parseDouble(event.getProperty().getValue().toString())/100.0);
		}
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

	/*
	 * (non-Javadoc)
	 * @see com.vaadin.addon.colorpicker.ColorPicker.ColorChangeListener#colorChanged(com.vaadin.addon.colorpicker.events.ColorChangeEvent)
	 */
	public void colorChanged(ColorChangeEvent event) {
		colorpicker.selectForegroundColorPicker();	
		canvas.getInteractive().setColor(colorToHex(colorpicker.getColor()));			
	}	
}
