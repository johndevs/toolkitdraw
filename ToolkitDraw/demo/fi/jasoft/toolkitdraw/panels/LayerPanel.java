package fi.jasoft.toolkitdraw.panels;

import java.util.List;


import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import fi.jasoft.flashcanvas.FlashCanvas;
import fi.jasoft.flashcanvas.Layer;
import fi.jasoft.toolkitdraw.components.DetachablePanel;
import fi.jasoft.toolkitdraw.util.IconFactory;
import fi.jasoft.toolkitdraw.util.IconFactory.Icons;

public class LayerPanel extends DetachablePanel implements ClickListener, ItemClickListener{

	private static final long serialVersionUID = 1L;
	private Table layerTable;
	private Button addLayer;
	private Button removeLayer;
	private Button upLayer;
	private Button downLayer;
	
	private VerticalLayout layout;
	
	private FlashCanvas canvas;
	
	public LayerPanel(FlashCanvas canvas){
		setCaption("Layers");
		
		this.canvas = canvas;
		
		layout = new VerticalLayout();
		setContent(layout);
		
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
		layout.addComponent(layerToolsContainer);
		
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
								
		layout.addComponent(layerTable);	
		layout.setExpandRatio(layerTable, 1);		
	}
	
	public void refreshLayers(){
				
		if(this.canvas == null)
			return;
		
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

	public void buttonClick(ClickEvent event) {
		if(event.getButton() == addLayer){			

			if(this.canvas == null)
				return;
			
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

	public void itemClick(ItemClickEvent event) {
		if(event.getItemId() == null) return;
		
		Layer layer = (Layer)event.getItemId();
		layer.getCanvas().getLayers().setActiveLayer(layer);		
	}
	
	public void setCanvas(FlashCanvas canvas) {		
		if(canvas == null){
			System.err.println("Cannot set null canvas");
			return;
		}
		
		this.canvas = canvas;
		refreshLayers();
	}
}
