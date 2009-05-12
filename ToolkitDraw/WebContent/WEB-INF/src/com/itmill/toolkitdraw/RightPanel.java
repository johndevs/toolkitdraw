package com.itmill.toolkitdraw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sun.awt.ModalityEvent;

import com.itmill.toolkit.data.Item;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.data.Property.ValueChangeEvent;
import com.itmill.toolkit.event.ItemClickEvent;
import com.itmill.toolkit.event.ItemClickEvent.ItemClickListener;
import com.itmill.toolkit.ui.Accordion;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.CheckBox;
import com.itmill.toolkit.ui.GridLayout;
import com.itmill.toolkit.ui.HorizontalLayout;
import com.itmill.toolkit.ui.Label;
import com.itmill.toolkit.ui.Layout;
import com.itmill.toolkit.ui.Panel;
import com.itmill.toolkit.ui.Select;
import com.itmill.toolkit.ui.TabSheet;
import com.itmill.toolkit.ui.Table;
import com.itmill.toolkit.ui.TextField;
import com.itmill.toolkit.ui.VerticalLayout;
import com.itmill.toolkit.ui.Button.ClickEvent;
import com.itmill.toolkit.ui.Button.ClickListener;

import com.itmill.toolkitdraw.components.Layer;
import com.itmill.toolkitdraw.components.PaintCanvas;

public class RightPanel extends Accordion implements Property.ValueChangeListener, ClickListener, ItemClickListener{

	private PaintCanvas canvas;	
	
	private Layout tab1;
	private Layout tab2;
	
	private TextField paperHeight;
	private TextField paperWidth;

	private Table layerTable;
	private Button addLayer;
	private Button removeLayer;
	private Button upLayer;
	private Button downLayer;
	
	public RightPanel(PaintCanvas canvas){
		
		super();
		setStyleName("rightpanel");
		
		this.canvas = canvas;
		
		//Create the paper options
		GridLayout grid = new GridLayout(4,1);
		grid.setCaption("Paper Options");	
		
		grid.addComponent(new Label("Paper dimensions "),0,0);	
		
		paperWidth = new TextField();
		paperWidth.setValue(300);		
		if(this.canvas != null) this.canvas.setPaperWidth(300);
		paperWidth.setWidth("30px");
		paperWidth.addListener((Property.ValueChangeListener)this);
		paperWidth.setImmediate(true);
		grid.addComponent(paperWidth,1,0);
		
		grid.addComponent(new Label("x"),2,0);
		
		paperHeight = new TextField();
		paperHeight.setValue(400);
		if(this.canvas != null) this.canvas.setPaperHeight(400);
		paperHeight.setWidth("30px");
		paperHeight.addListener((Property.ValueChangeListener)this);
		paperHeight.setImmediate(true);
		grid.addComponent(paperHeight,3,0);
		
		tab1 = grid;
		addComponent(tab1);	
		
		//Create the layers tab
		tab2 = new VerticalLayout();		
		tab2.setCaption("Layers");	
			
		HorizontalLayout layerTools = new HorizontalLayout();
		
		addLayer = new Button("Add");
		addLayer.addListener((ClickListener)this);
		layerTools.addComponent(addLayer);
	
		removeLayer = new Button("Del");
		removeLayer.addListener((ClickListener)this);
		layerTools.addComponent(removeLayer);
		
		upLayer = new Button("Up");
		upLayer.addListener((ClickListener)this);
		layerTools.addComponent(upLayer);
		
		downLayer = new Button("Down");
		downLayer.addListener((ClickListener)this);
		layerTools.addComponent(downLayer);
		
		tab2.addComponent(layerTools);
		
		
		layerTable = new Table();
		layerTable.setSizeFull();
		layerTable.setSelectable(true);
		layerTable.addListener((ItemClickListener)this);
		layerTable.addContainerProperty("Visible", CheckBox.class, null);		
		layerTable.setColumnWidth("Visible", 30);
		layerTable.setColumnHeader("Visible", "");
		layerTable.addContainerProperty("Name",String.class, "");		
		
		tab2.addComponent(layerTable);		
		addComponent(tab2);			
	}
	
	
	private void refreshLayers(){
		
		if(this.canvas == null) return;
		
		List<Layer> layers = this.canvas.getLayers();
		
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
				public void buttonClick(ClickEvent event) {
					if(event.getButton().getValue() == null) return;
					
					boolean state = Boolean.valueOf(event.getButton().getValue().toString());
					Layer layer = (Layer) visible.getData();
					layer.setVisible(state);
				}				
			});
						
			item.getItemProperty("Visible").setValue(visible);			
		}
				
		layerTable.requestRepaint();
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		
		if(event.getProperty() == paperWidth){
			canvas.setPaperWidth(Integer.parseInt(event.getProperty().getValue().toString()));
		}
		
		if(event.getProperty() == paperHeight){
			canvas.setPaperHeight(Integer.parseInt(event.getProperty().getValue().toString()));
		}
		
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if(this.canvas == null) return;
		
		if(event.getButton() == addLayer){			
		
			List<Layer> layers = this.canvas.getLayers();
												
			//The layer is added automatically to the canvas
			Layer newLayer = new Layer("Layer #"+(layers.size()+1), this.canvas);						
			this.canvas.addLayer(newLayer);						
			
			refreshLayers();
			
			layerTable.select(newLayer);
			
		}else if(event.getButton() == removeLayer && layerTable.getValue() != null){
			
			
			
			
		}else if(event.getButton() == upLayer && layerTable.getValue() != null ){
			Layer selected = (Layer)layerTable.getValue();
			List<Layer> layers = this.canvas.getLayers();			
			
			//Do not move an element behind the Background element
			int idx = layers.indexOf(selected);
			if(idx <= 1) return;
			
			//Swap the layers in both the table and the image component
			this.canvas.moveLayerUp(selected.getName());
			Collections.swap(layers, idx, idx-1);
			
			refreshLayers();			
			layerTable.select(selected);
			
		}else if(event.getButton() == downLayer && layerTable.getValue() != null){
			Layer selected = (Layer)layerTable.getValue();
			List<Layer> layers = this.canvas.getLayers();
			
			//Do not move the Background element or the last element
			int idx = layers.indexOf(selected);
			if(idx == layers.size()-1 || idx == 0) return;
			
			//Swap the layers in both the table and the image component
			this.canvas.moveLayerDown(selected.getName());
			Collections.swap(layers, idx, idx+1);
			
			refreshLayers();
			layerTable.select(selected);
		}		
	}
	
	@Override
	public void itemClick(ItemClickEvent event) {
		if(event.getItemId() == null) return;
		
		Layer layer = (Layer)event.getItemId();
		layer.getCanvas().setActiveLayer(layer);
	}
	
	public PaintCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(PaintCanvas canvas) {
		
		if(canvas == null) return;
		
		System.out.println("RightPanel: Setting new canvas");		
		
		this.canvas = canvas;
		refreshLayers();
	}


	

	
}