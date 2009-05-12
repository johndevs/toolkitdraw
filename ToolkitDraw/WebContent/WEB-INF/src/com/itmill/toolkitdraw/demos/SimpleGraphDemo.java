package com.itmill.toolkitdraw.demos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.ext.typeinfo.HasMetaData;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.itmill.toolkit.data.Item;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.Component;
import com.itmill.toolkit.ui.GridLayout;
import com.itmill.toolkit.ui.HorizontalLayout;
import com.itmill.toolkit.ui.Layout;
import com.itmill.toolkit.ui.Table;
import com.itmill.toolkit.ui.VerticalLayout;
import com.itmill.toolkit.ui.Window;
import com.itmill.toolkit.ui.Button.ClickEvent;
import com.itmill.toolkitdraw.components.PaintCanvas;

public class SimpleGraphDemo extends Window {
	
	private PaintCanvas canvas;
	
	private Table table;
	
	private Button refresh;
	
	private String[] titles = new String[]{ "Food", "Drinks", "Clothes", "Stuff" };
	
	public SimpleGraphDemo() {
		
		super("Simple Graph Demo");		
		
		setWidth("600px");
		setHeight("400px");		
		setModal(true);			
						
		GridLayout layout = new GridLayout(2,2);
		setLayout(layout);	
		
		//Create a paintcanvas 
		canvas = new PaintCanvas();
		canvas.setHeight("300px");
		canvas.setWidth("300px");	
		canvas.setInteractive(false);
		layout.addComponent(canvas,0,0);
		
		//Create a value table
		table = new Table();
		table.addContainerProperty("Title", String.class, "");
		table.addContainerProperty("Value", Integer.class, 0);
		table.setColumnWidth("Title", 200);
		table.setColumnWidth("Value", 70);
		table.setColumnAlignment("Value", Table.ALIGN_CENTER);
		table.setHeight("280px");
		table.setWidth("280px");
		layout.addComponent(table,1,0);
		
		//Create the refresh button
		refresh = new Button("Refresh");
		refresh.addListener(new Button.ClickListener(){
			@Override
			public void buttonClick(ClickEvent event) {
				randomlyCreateValues();
												
				renderBarGraph();							
			}			
		});
		
		layout.addComponent(refresh,0,1);
				
		randomlyCreateValues();
		
		renderBarGraph();
	}
	
	private void populateTable(Map<String, Integer> values){
		
		if(values == null) return;
		
		table.removeAllItems();
		for(String title : values.keySet())
		{
			Item item = table.addItem(title);
			item.getItemProperty("Title").setValue(title);
			item.getItemProperty("Value").setValue(values.get(title));			
		}		
	}
	
	private void randomlyCreateValues(){		
		
		Map<String, Integer> values = new HashMap<String, Integer>();
		
		for(String title : titles){
			int val = new Double(Math.random()*100.0).intValue();
			values.put(title,val);			
		}
		
		populateTable(values);
	}
	
	/**
	 * Renders a bar graph of the values in the table
	 */
	private void renderBarGraph(){			
		
		//Clear the earlier drawings
		canvas.getGraphics().clear();
		
		//Draw the bars to the component
		int counter = 0;
		for(Object id : table.getItemIds()){			
			Item item = table.getItem(id);
			int value = Integer.valueOf((item.getItemProperty("Value").getValue().toString()));
						
			canvas.getGraphics().drawSquare(counter*55+20, 280-value*2, 50, value*2);
			counter++;
		}		
	}
	

}
