package fi.jasoft.toolkitdraw.tools;


import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.flashcanvas.FlashCanvas;
import fi.jasoft.flashcanvas.enums.BrushType;
import fi.jasoft.toolkitdraw.util.IconFactory;
import fi.jasoft.toolkitdraw.util.IconFactory.Icons;

public class Select extends Tool {

	private static final long serialVersionUID = 1L;
	
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
	public Select(FlashCanvas canvas){
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

	public void valueChange(ValueChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return "Select";
	}

	@Override
	public void sendCurrentSettings() {
		// TODO Auto-generated method stub
		
	}

}
