package com.vaadin.toolkitdraw.tools;

import java.awt.Color;

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

public class Fill extends Tool implements ValueChangeListener, ColorChangeListener{
	
	private static final long serialVersionUID = 1L;

	private Layout layout = new VerticalLayout();
				
	private TwinColorPicker colorpicker;
		
	public Fill(PaintCanvas canvas){
		
		this.canvas = canvas;
	
		button = new Button();
		button.setStyleName(Button.STYLE_LINK);
		button.setData(BrushType.FILL);
	//	button.setIcon(IconFactory.getIcon(Icons.ICON_PEN));
		button.setCaption("FILL");
		
		colorpicker = new TwinColorPicker();
		colorpicker.addListener(this);
		layout.addComponent(colorpicker);		
	}
	
	@Override
	public BrushType getType() {
		return BrushType.FILL;
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		//No fields yet..
	}
	
	@Override
	public void changed(ColorSelector selector, Color color) {
		colorpicker.selectForegroundColorPicker();	
		canvas.getInteractive().setColor(colorToHex(colorpicker.getColor()));			
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}
