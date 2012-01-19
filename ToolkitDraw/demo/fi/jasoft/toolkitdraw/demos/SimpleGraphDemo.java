package fi.jasoft.toolkitdraw.demos;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

import fi.jasoft.flashcanvas.FlashCanvas;
import fi.jasoft.flashcanvas.enums.CacheMode;
import fi.jasoft.flashcanvas.events.ImageUploadEvent;
import fi.jasoft.flashcanvas.events.ImageUploadEvent.ImageType;
import fi.jasoft.flashcanvas.events.ImageUploadListener;

public class SimpleGraphDemo extends Window {

	private static final long serialVersionUID = 1L;

	private FlashCanvas canvas;
	
	private Table table;
	
	private Button refresh;
	
	private String[] titles = new String[]{ "Food", "Drinks", "Clothes", "Stuff" };

	private Button save;
	
	private Button close;
	
	public SimpleGraphDemo() {
		
		super("Simple Graph Demo");		
		
		setWidth("600px");
		setHeight("400px");		
		setModal(true);			
		setResizable(false);
						
		GridLayout layout = new GridLayout(2,2);	
		layout.setStyleName(Reindeer.LAYOUT_BLACK);
		setContent(layout);
		
		//Create a paintcanvas 
		canvas = new FlashCanvas("300px","300px",  new Color(51,51,51));	
		
		//Disable drawing on the canvas
		canvas.setInteractive(false);
		
		//Disable caching
		canvas.setCacheMode(CacheMode.NONE);		
		
		//Enable batch mode
		canvas.getGraphics().setBatchMode(true);
		
		canvas.addListener(new ImageUploadListener() {
			public void imageUploaded(ImageUploadEvent event) {
				ByteArrayInputStream iStream = new ByteArrayInputStream(event.getBytes());							
				final DownloadStream stream = new DownloadStream(iStream,"Image/PNG","image"+(new Date()).getTime()+".png");
				Resource res = new StreamResource(new StreamResource.StreamSource(){			
					public InputStream getStream() {
						return stream.getStream();
					}				
				}, stream.getFileName(), getApplication());
				open(res,"img");	
			}
		});
								
		layout.addComponent(canvas,0,0);
		
		//Create a value table
		table = new Table();
		table.addContainerProperty("Title", String.class, "");
		table.addContainerProperty("Value", Integer.class, 0);
		table.setColumnWidth("Title", 190);
		table.setColumnWidth("Value", 70);
		table.setColumnAlignment("Value", Table.ALIGN_CENTER);
		table.setHeight("300px");
		table.setWidth("290px");
		layout.addComponent(table,1,0);
		
		HorizontalLayout buttons = new HorizontalLayout();
		layout.addComponent(buttons, 1,1);
		layout.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);
		buttons.setMargin(true);		
		
		
		save = new Button("Save");
		save.addListener(new Button.ClickListener(){		
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				canvas.getImage(ImageType.PNG, 72);
			}			
		});
				
		buttons.addComponent(save);
		buttons.setComponentAlignment(save, Alignment.MIDDLE_RIGHT);
		
		//Create the refresh button
		refresh = new Button("Refresh");
		refresh.addListener(new Button.ClickListener(){		
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				randomlyCreateValues();												
				renderBarGraph();							
			}			
		});
		
		buttons.addComponent(refresh);
		buttons.setComponentAlignment(refresh, Alignment.MIDDLE_RIGHT);
		
		//Create the close button
		close = new Button("Close");
		close.addListener(new Button.ClickListener() {		
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				setVisible(false);				
			}
		});
		
		buttons.addComponent(close);
		buttons.setComponentAlignment(close, Alignment.MIDDLE_RIGHT);
				
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
						
		//Get the graphics object
		FlashCanvas.Graphics gc = canvas.getGraphics();
					
		// This clears the whole screen
		gc.clear();	
											
		//Draw the bars to the component in one batch
		int counter = 0;
		for(Object id : table.getItemIds()){			
			Item item = table.getItem(id);
			int value = Integer.valueOf((item.getItemProperty("Value").getValue().toString()));
						
			//Draw the front of the bars
			gc.drawSquare(counter*70+20, 280-value*2, 50, value*2,new Color(0xCC, 0x0, 0x0),Color.RED);
			
			//Draw the tops of the bars
			int[] topX = new int[4];
			int[] topY = new int[4];			
			
			topX[0] = counter*70+20;
			topY[0] = 280-value*2;
			topX[1] = topX[0]+10;
			topY[1] = topY[0]-10;
			topX[2] = topX[1]+50;
			topY[2] = topY[1];
			topX[3] = topX[0]+50;
			topY[3] = topY[0];
			
			gc.drawPolygon(topX, topY, new Color(0xCC,0x0,0x0), new Color(0xFF,0x0,0x0));
			
			//Draw the sides of the bars
			topX[0] = counter*70+70;
			topY[0] = 280-value*2;
			topX[1] = topX[0]+10;
			topY[1] = topY[0]-10;
			topX[2] = topX[1];
			topY[2] = topY[1]+value*2;
			topX[3] = topX[0];
			topY[3] = topY[2]+10;
			
			gc.drawPolygon(topX, topY, new Color(0xCC,0x00,0x00), new Color(0xCC,0x0,0x0));			
		
			//Add the values to the top of the bars
			gc.drawText(id.toString(), 
						counter*70+20, 280-value*2, // Coordinates
						100, 100, 					// Text area size
						12, Color.BLACK, 0.0,			// Font properties
						Color.BLACK, 0.0);				// Background properties
			
			counter++;
		}		
													
		//Send the draw intstructions to the client
		gc.sendBatch();			
	}
}
