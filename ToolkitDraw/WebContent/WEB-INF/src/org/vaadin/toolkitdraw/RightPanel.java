package org.vaadin.toolkitdraw;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.toolkitdraw.components.flashcanvas.FlashCanvas;
import org.vaadin.toolkitdraw.components.flashcanvas.Layer;
import org.vaadin.toolkitdraw.util.IconFactory;
import org.vaadin.toolkitdraw.util.IconFactory.Icons;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class RightPanel extends VerticalLayout implements Property.ValueChangeListener, ClickListener, ItemClickListener{

	private static final long serialVersionUID = 1L;

	private FlashCanvas canvas;	
	
	private VerticalLayout tab2;
	
	private TextField paperHeight;
	private TextField paperWidth;

	private Table layerTable;
	private Button addLayer;
	private Button removeLayer;
	private Button upLayer;
	private Button downLayer;
	
	private Accordion tabs;
	
	private TabSheet histograms;
	private Map<String, FlashCanvas> histogramCanvases = new HashMap<String, FlashCanvas>();
	
	private Preferences preferences;
	
	public RightPanel(FlashCanvas canvas, Preferences pref){		
		super();
		setStyleName("rightpanel");
		setHeight("100%");
				
		this.canvas = canvas;
		this.preferences = pref;
		
		//Add the histograms
		addComponent(createHistogramsTab());
		
		//Add the tabs
		this.tabs = new Accordion();
		this.tabs.setSizeFull();
		addComponent(this.tabs);
		setExpandRatio(this.tabs, 1);
		
		//Create the layers tab
		this.tabs.addComponent(createLayersTab());
				
		//Create the paper options 
		//this.tabs.addComponent(createPaperOptionsTab());
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
	
	private Component createHistogramsTab()
	{
		createHistograms();		
		
		histograms = new TabSheet();
		histograms.setHeight("250px");
	
		for(String caption : histogramCanvases.keySet()){			
			FlashCanvas c = histogramCanvases.get(caption);
			c.setVisible(false);
						
			VerticalLayout layout = new VerticalLayout();
			layout.addComponent(c);
			layout.setComponentAlignment(c, Alignment.MIDDLE_CENTER);
			layout.setCaption(caption);
			
			histograms.addTab(layout);
		}
		
		return histograms;
	}
		
	private Component createLayersTab()
	{
		tab2 = new VerticalLayout();		
		tab2.setCaption("Layers");	
		tab2.setSizeFull();
			
		HorizontalLayout layerTools = new HorizontalLayout();		
						
		addLayer = new Button();
		addLayer.setDescription("Add layer");
		addLayer.setIcon(IconFactory.getIcon(Icons.PLUS));
		addLayer.setStyleName(Button.STYLE_LINK);
		addLayer.addListener((ClickListener)this);
		layerTools.addComponent(addLayer);
		
		removeLayer = new Button();
		removeLayer.setDescription("Remove layer");
		removeLayer.setIcon(IconFactory.getIcon(Icons.MINUS));
		removeLayer.setStyleName(Button.STYLE_LINK);
		removeLayer.addListener((ClickListener)this);
		layerTools.addComponent(removeLayer);	
		
		upLayer = new Button();
		upLayer.setDescription("Move layer up");
		upLayer.setIcon(IconFactory.getIcon(Icons.UP_ARROW));
		upLayer.setStyleName(Button.STYLE_LINK);
		upLayer.addListener((ClickListener)this);
		layerTools.addComponent(upLayer);
		
		downLayer = new Button();
		downLayer.setHeight("20px");
		downLayer.setDescription("Move layer down");
		downLayer.setIcon(IconFactory.getIcon(Icons.DOWN_ARROW));
		downLayer.setStyleName(Button.STYLE_LINK);
		downLayer.addListener((ClickListener)this);
		layerTools.addComponent(downLayer);	
		
		HorizontalLayout layerToolsContainer = new HorizontalLayout();
		layerToolsContainer.addComponent(layerTools);
		layerToolsContainer.setComponentAlignment(layerTools, Alignment.MIDDLE_LEFT);
		tab2.addComponent(layerToolsContainer);
				
		layerTable = new Table();
		layerTable.setStyleName("layer-table");
		layerTable.setSizeFull();
		layerTable.setSelectable(true);
		layerTable.addListener((ItemClickListener)this);
		layerTable.addContainerProperty("Visible", CheckBox.class, null);		
		layerTable.setColumnWidth("Visible", 30);
		layerTable.setColumnHeader("Visible", "");
		layerTable.addContainerProperty("Name",String.class, "");
		layerTable.setNullSelectionAllowed(false);
								
		tab2.addComponent(layerTable);	
		tab2.setExpandRatio(layerTable, 1);
		
		return tab2;
	}
	
	private void refreshLayers(){
		
		if(this.canvas == null) return;
		
		//Show the histogram
		for(String caption : histogramCanvases.keySet()){			
			FlashCanvas c = histogramCanvases.get(caption);
			c.setVisible(true);
		}
		
		List<Layer> layers = this.canvas.getLayers().getLayers();
		
 		layerTable.removeAllItems();
		
		for(Layer layer : layers){			
			Item item = layerTable.addItem(layer);
					
			//A failue occured
			if(item == null){
				System.out.println("ERROR: Could not add layer"+layer.getName()+" to layer table");
				return;
			}
							
			item.getItemProperty("Name").setValue(layer.getName());
			
			final CheckBox visible = new CheckBox();
			visible.setData(layer);
			visible.setImmediate(true);
			visible.setValue(layer.getVisible());
			visible.addListener(new Button.ClickListener(){
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					if(event.getButton().getValue() == null) return;
					
					boolean state = Boolean.valueOf(event.getButton().getValue().toString());
					Layer layer = (Layer) visible.getData();
					layer.setVisible(state);
				}				
			});
						
			item.getItemProperty("Visible").setValue(visible);			
		}
				
		layerTable.select(canvas.getLayers().getActiveLayer());
		layerTable.requestRepaint();
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		
		if(event.getProperty() == paperWidth){
			canvas.getInteractive().setPaperWidth(Integer.parseInt(event.getProperty().getValue().toString()));
		}
		
		if(event.getProperty() == paperHeight){
			canvas.getInteractive().setPaperHeight(Integer.parseInt(event.getProperty().getValue().toString()));
		}
		
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if(this.canvas == null) return;
		
		if(event.getButton() == addLayer){			
		
			List<Layer> layers = this.canvas.getLayers().getLayers();
												
			//The layer is added automatically to the canvas
			Layer newLayer = new Layer("Layer #"+(layers.size()+1), this.canvas);						
						
			this.canvas.getLayers().addLayer(newLayer);						
			
			refreshLayers();
			
			layerTable.select(newLayer);
			canvas.getLayers().setActiveLayer(newLayer);
			canvas.getInteractive().setBrush(canvas.getInteractive().getCurrentBrush());
			
		}else if(event.getButton() == removeLayer && layerTable.getValue() != null){
			Layer selected = (Layer)layerTable.getValue();
			List<Layer> layers = this.canvas.getLayers().getLayers();
			
			//Background layer cannot be removed
			int idx = layers.indexOf(selected);
			if(idx == 0) return;
			
			//Remove layer
			this.canvas.getLayers().removeLayer(selected);
					
			refreshLayers();
			
			if(layers.size() > idx){
				layerTable.select(layers.get(idx));
				canvas.getLayers().setActiveLayer(layers.get(idx));
			}else{
				layerTable.select(layers.get(idx-1));
				canvas.getLayers().setActiveLayer(layers.get(idx-1));
			}
				
			
		}else if(event.getButton() == upLayer && layerTable.getValue() != null ){
			Layer selected = (Layer)layerTable.getValue();
			List<Layer> layers = this.canvas.getLayers().getLayers();			
			
			//Do not move an element behind the Background element
			int idx = layers.indexOf(selected);
			if(idx <= 1) return;
			
			//Swap the layers in both the table and the image component
			this.canvas.getLayers().moveLayerUp(selected);			
			
			// Refresh the layer listing
			refreshLayers();		
			
			// Select the moved layer
			layerTable.select(selected);
			canvas.getLayers().setActiveLayer(selected);
			
		}else if(event.getButton() == downLayer && layerTable.getValue() != null){
			Layer selected = (Layer)layerTable.getValue();
			List<Layer> layers = this.canvas.getLayers().getLayers();
			
			//Do not move the Background element or the last element
			int idx = layers.indexOf(selected);
			if(idx == layers.size()-1 || idx == 0) return;
			
			//Swap the layers in both the table and the image component
			this.canvas.getLayers().moveLayerDown(selected);			
			
			refreshLayers();
			layerTable.select(selected);
		}		
	}
	
	@Override
	public void itemClick(ItemClickEvent event) {
		if(event.getItemId() == null) return;
		
		Layer layer = (Layer)event.getItemId();
		layer.getCanvas().getLayers().setActiveLayer(layer);
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
		refreshLayers();
	}
	
	public void updateHistogram(){
		
		
		
	}


	

	
}
