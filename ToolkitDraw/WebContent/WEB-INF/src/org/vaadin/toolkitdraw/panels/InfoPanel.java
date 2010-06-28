package org.vaadin.toolkitdraw.panels;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.vaadin.toolkitdraw.Preferences;
import org.vaadin.toolkitdraw.components.DetachablePanel;
import org.vaadin.toolkitdraw.components.flashcanvas.FlashCanvas;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public class InfoPanel extends DetachablePanel {

	private static final long serialVersionUID = 1L;
	private TabSheet tabSheet;
	private Map<String, FlashCanvas> histogramCanvases = new HashMap<String, FlashCanvas>();
	
	private Preferences preferences;
	
	public InfoPanel(Preferences preferences){
		this.preferences = preferences;
		
		setCaption("Image Info");
		setHeight("250px");
		
		createHistograms();
		
		tabSheet = new TabSheet();
		tabSheet.setSizeFull();
		setContent(tabSheet);
	
		for(String caption : histogramCanvases.keySet()){			
			FlashCanvas c = histogramCanvases.get(caption);
			c.setVisible(false);
						
			VerticalLayout layout = new VerticalLayout();
			layout.addComponent(c);
			layout.setComponentAlignment(c, Alignment.MIDDLE_CENTER);
			layout.setCaption(caption);
			
			System.out.println(caption);
			
			tabSheet.addTab(layout);
		}		
	}
	
	private void createHistograms()
	{
		Color bgColor = new Color(	Integer.parseInt("44",16),
				 					Integer.parseInt("44",16),
				 					Integer.parseInt("44",16) );
		
		//Create RGB channel histogram
		FlashCanvas rgb = new FlashCanvas("250px","200px", bgColor);
		;	
				
		//Set the caching mode of the canvas
		rgb.setCacheMode(preferences.getCacheMode());
			
		//Set the plugin to use
		rgb.setPlugin(preferences.getPlugin());
			
		//Set the autosave time
		rgb.setAutosaveTime(preferences.getAutosaveTime());
		
		//Set paper color to background color
		rgb.getGraphics().drawSquare(0, 0, 250, 200, bgColor, bgColor);
						
		String caption = "Histogram";
		histogramCanvases.put(caption, rgb);		
	}
	
	public void setCanvas(FlashCanvas canvas) {		
		if(canvas == null){
			System.err.println("Cannot set null canvas");
			return;
		}
	}	
}
