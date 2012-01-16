package fi.jasoft.toolkitdraw;


import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.flashcanvas.FlashCanvas;
import fi.jasoft.toolkitdraw.panels.InfoPanel;
import fi.jasoft.toolkitdraw.panels.LayerPanel;

public class RightPanel extends VerticalLayout{

	private static final long serialVersionUID = 1L;

	private FlashCanvas canvas;	
	
	private InfoPanel info;
	
	private LayerPanel layers;
		
	public RightPanel(FlashCanvas canvas, Preferences pref){		
		super();
		setStyleName("rightpanel");
		setHeight("100%");
				
		this.canvas = canvas;
		
		info = new InfoPanel(pref);
		addComponent(info);
		
		layers = new LayerPanel(canvas);
		addComponent(layers);
	}

	public FlashCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(FlashCanvas canvas) {
		
		if(canvas == null){
			System.err.println("Cannot set null canvas");
			return;
		}
		
		this.canvas = canvas;
		layers.setCanvas(canvas);
		info.setCanvas(canvas);
	}
	
	@Override 
	public void removeComponent(Component c) {
		super.removeComponent(c);		
		if(!getComponentIterator().hasNext()){
			setWidth("1px");
		}
	}
}
