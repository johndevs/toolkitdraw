package com.vaadin.toolkitdraw.tools;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.toolkitdraw.components.paintcanvas.PaintCanvas;
import com.vaadin.toolkitdraw.components.paintcanvas.enums.BrushType;
import com.vaadin.toolkitdraw.util.IconFactory;
import com.vaadin.toolkitdraw.util.IconFactory.Icons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

public class Select extends Tool {

	private static final long serialVersionUID = 1L;

	private PaintCanvas canvas;
	
	private com.vaadin.ui.Select mode;
	
	private Layout layout = new VerticalLayout();
	
	public enum Modes{
		RECTANGLE("Rectangle Selection"), 
		WAND("Wand Selection"), 
		FILL("Fill Selection");
		
		private final String str;
		private Modes(String s) {
			this.str = s;
		}
		public String toString(){
			return this.str;
		}
	}
	
	@SuppressWarnings("serial")
	public Select(PaintCanvas canvas){
		this.canvas = canvas;
		this.layout.setMargin(true);
		
		//Create the modes
		mode = new com.vaadin.ui.Select("Mode");
		mode.addContainerProperty("Caption", String.class, "");
		mode.setItemCaptionPropertyId("Caption");
		mode.setNullSelectionAllowed(false);		
		
		for(Modes m : Modes.values()){
			Item item = mode.addItem(m);
			item.getItemProperty("Caption").setValue(m.toString());
		}	
		
		mode.select(Modes.RECTANGLE);
		layout.addComponent(mode);
		
		//Create the button
		button = new Button();
		button.setStyleName(Button.STYLE_LINK);
		button.setData(getType());		
		button.setIcon(IconFactory.getIcon(Icons.SELECT));		
		
		mode.addListener(new Property.ValueChangeListener(){
			public void valueChange(ValueChangeEvent event) {
				button.setData(event.getProperty().getValue());				
			}			
		});		
	}
	
	public Layout createToolOptions(){	
		return layout;
	}	
	
	
	@Override
	public BrushType getType() {		
		if(mode.getValue() == Modes.RECTANGLE){
			return BrushType.RECTANGLE_SELECT;
		}
		
		//Generic fallback
		return BrushType.SELECT;
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return "Select";
	}

}
