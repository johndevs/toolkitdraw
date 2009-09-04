package com.vaadin.toolkitdraw.components;

import com.vaadin.colorpicker.ColorPicker;
import com.vaadin.colorpicker.ColorSelector;
import com.vaadin.colorpicker.ColorSelector.ColorChangeListener;
import com.vaadin.toolkitdraw.util.IconFactory;
import com.vaadin.toolkitdraw.util.IconFactory.Icons;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class TwinColorPicker.
 */
public class TwinColorPicker extends AbsoluteLayout implements ColorChangeListener, ColorSelector, ClickListener{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The foreground. */
	private ColorPicker foreground;
	
	/** The background. */
	private ColorPicker background;
	
	/** The reset button. */
	private Button resetButton;
	
	/** The switch button. */
	private Button switchButton;
		
	/** The listeners. */
	private Set<ColorChangeListener> listeners = new HashSet<ColorChangeListener>();
	
	/** The foreground selected. */
	private boolean foregroundSelected = true;
	
	/**
	 * Instantiates a new twin color picker.
	 */
	public TwinColorPicker(){
		
		setWidth("100px");
		setHeight("100px");
		setStyleName("twin-colorpicker");
				
		background = new ColorPicker("Background", Color.WHITE);
		background.addListener(this);
		background.setWidth("55px");
		background.setHeight("35px");
		addComponent(background, "top:20px;left:30px");
		
		foreground = new ColorPicker("Foreground", Color.BLACK);
		foreground.addListener(this);
		foreground.setWidth("55px");
		foreground.setHeight("35px");
		addComponent(foreground, "top:0px;left:0px");
		
		resetButton = new Button("", this);	
		resetButton.setIcon(IconFactory.getIcon(Icons.RESET_COLOR_BUTTON));
		resetButton.setStyleName(Button.STYLE_LINK);
		resetButton.setWidth("30px");
		resetButton.setHeight("20px");
		addComponent(resetButton, "top:35px;left:0px");
		
		switchButton = new Button("", this);	
		switchButton.setIcon(IconFactory.getIcon(Icons.SWITCH_COLOR_BUTTON));
		switchButton.setStyleName(Button.STYLE_LINK);
		switchButton.setWidth("30px");
		switchButton.setHeight("20px");
		addComponent(switchButton, "top:0px;left:55px");		
	}

	/* (non-Javadoc)
	 * @see com.vaadin.colorpicker.ColorSelector$ColorChangeListener#changed(com.vaadin.colorpicker.ColorSelector, java.awt.Color)
	 */
	@Override
	public void changed(ColorSelector selector, Color color) {
		for(ColorChangeListener listener :listeners){
			listener.changed(selector, color);
		}		
	}

	/* (non-Javadoc)
	 * @see com.vaadin.colorpicker.ColorSelector#addListener(com.vaadin.colorpicker.ColorSelector.ColorChangeListener)
	 */
	@Override
	public void addListener(ColorChangeListener listener) {
		listeners.add(listener);
		
	}

	/* (non-Javadoc)
	 * @see com.vaadin.colorpicker.ColorSelector#getColor()
	 */
	@Override
	public Color getColor() {
		if(foregroundSelected)
			return foreground.getColor();
		else
			return background.getColor();		
	}

	/* (non-Javadoc)
	 * @see com.vaadin.colorpicker.ColorSelector#removeListener(com.vaadin.colorpicker.ColorSelector.ColorChangeListener)
	 */
	@Override
	public void removeListener(ColorChangeListener listener) {
		listeners.remove(listener);
		
	}

	/* (non-Javadoc)
	 * @see com.vaadin.colorpicker.ColorSelector#setColor(java.awt.Color)
	 */
	@Override
	public void setColor(Color color) {
		if(foregroundSelected){
			foreground.setColor(color);
			foreground.requestRepaint();
		} else{
			background.setColor(color);
			background.requestRepaint();
		}		
	}
	
	/**
	 * Select foreground color picker.
	 */
	public void selectForegroundColorPicker(){
		foregroundSelected = true;
	}
	
	/**
	 * Select background color picker.
	 */
	public void selectBackgroundColorPicker(){
		foregroundSelected = false;
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.Button$ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if(event.getButton() == resetButton){
			foreground.setColor(Color.BLACK);
			background.setColor(Color.WHITE);
			foreground.requestRepaint();
			background.requestRepaint();
		} 
		else if(event.getButton() == switchButton){
			Color fgColor = foreground.getColor();
			Color bgColor = background.getColor();
			
			foreground.setColor(bgColor);
			background.setColor(fgColor);
			foreground.requestRepaint();
			background.requestRepaint();
		}		
	}	
}