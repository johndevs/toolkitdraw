package fi.jasoft.toolkitdraw.components;

import com.vaadin.addon.colorpicker.ColorPicker;
import com.vaadin.addon.colorpicker.ColorSelector;
import com.vaadin.addon.colorpicker.ColorPicker.ButtonStyle;
import com.vaadin.addon.colorpicker.ColorPicker.ColorChangeListener;
import com.vaadin.addon.colorpicker.events.ColorChangeEvent;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import fi.jasoft.toolkitdraw.util.IconFactory;
import fi.jasoft.toolkitdraw.util.IconFactory.Icons;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;


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
		
		setWidth("50px");
		setHeight("50px");
		setStyleName("twin-colorpicker");
				
		background = new ColorPicker("Background", Color.WHITE);
		background.addListener(this);
		background.setWidth("33px");
		background.setHeight("30px");
		background.setButtonStyle(ButtonStyle.BUTTON_AREA);
		addComponent(background, "top:15px;left:15px");
		
		foreground = new ColorPicker("Foreground", Color.BLACK);
		foreground.addListener(this);
		foreground.setWidth("33px");
		foreground.setHeight("30px");
		foreground.setButtonStyle(ButtonStyle.BUTTON_AREA);
		addComponent(foreground, "top:0px;left:0px");
		
		resetButton = new Button("", this);	
		resetButton.setIcon(IconFactory.getIcon(Icons.RESET_COLOR_BUTTON));
		resetButton.setStyleName(Button.STYLE_LINK);
		resetButton.setWidth("15px");
		resetButton.setHeight("15px");
		addComponent(resetButton, "top:31px;left:0px");
		
		switchButton = new Button("", this);	
		switchButton.setIcon(IconFactory.getIcon(Icons.SWITCH_COLOR_BUTTON));
		switchButton.setStyleName(Button.STYLE_LINK);
		switchButton.setWidth("15px");
		switchButton.setHeight("15px");
		addComponent(switchButton, "top:0px;left:34px");		
	}

	/* (non-Javadoc)
	 * @see com.vaadin.addon.colorpicker.ColorSelector#addListener(com.vaadin.addon.colorpicker.ColorSelector.ColorChangeListener)
	 */
	public void addListener(ColorChangeListener listener) {
		listeners.add(listener);
		
	}

	/* (non-Javadoc)
	 * @see com.vaadin.addon.colorpicker.ColorSelector#getColor()
	 */
	public Color getColor() {
		if(foregroundSelected)
			return foreground.getColor();
		else
			return background.getColor();		
	}

	/* (non-Javadoc)
	 * @see com.vaadin.addon.colorpicker.ColorSelector#removeListener(com.vaadin.addon.colorpicker.ColorSelector.ColorChangeListener)
	 */
	public void removeListener(ColorChangeListener listener) {
		listeners.remove(listener);
		
	}

	/* (non-Javadoc)
	 * @see com.vaadin.addon.colorpicker.ColorSelector#setColor(java.awt.Color)
	 */
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
	public void buttonClick(ClickEvent event) {
		if(event.getButton() == resetButton){
			foreground.setColor(Color.BLACK);
			background.setColor(Color.WHITE);
			foreground.requestRepaint();
			background.requestRepaint();
			
			colorChanged(new ColorChangeEvent(foreground, Color.BLACK));
			colorChanged(new ColorChangeEvent(background, Color.WHITE));
		} 
		else if(event.getButton() == switchButton){
			Color fgColor = foreground.getColor();
			Color bgColor = background.getColor();
			
			foreground.setColor(bgColor);
			background.setColor(fgColor);
			foreground.requestRepaint();
			background.requestRepaint();
			
			colorChanged(new ColorChangeEvent(foreground, bgColor));
			colorChanged(new ColorChangeEvent(background, fgColor));
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see com.vaadin.addon.colorpicker.ColorPicker.ColorChangeListener#colorChanged(com.vaadin.addon.colorpicker.events.ColorChangeEvent)
	 */
	public void colorChanged(ColorChangeEvent event) {
		for(ColorChangeListener listener :listeners){
			listener.colorChanged(event);
		}				
	}	
}
