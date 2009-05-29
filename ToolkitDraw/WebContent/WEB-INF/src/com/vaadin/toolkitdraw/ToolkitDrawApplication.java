package com.vaadin.toolkitdraw;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.StreamResource;
import com.vaadin.toolkitdraw.components.ConfirmPopup;
import com.vaadin.toolkitdraw.components.PaintCanvas;
import com.vaadin.toolkitdraw.components.SavePopup;
import com.vaadin.toolkitdraw.demos.SimpleGraphDemo;
import com.vaadin.toolkitdraw.events.ImagePNGRecievedEvent;
import com.vaadin.toolkitdraw.events.ImageXMLRecievedEvent;
import com.vaadin.toolkitdraw.tools.Tool;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;


public class ToolkitDrawApplication extends Application implements ClickListener, ValueChangeListener, SelectedTabChangeListener {

	private Window mainWindow;
	
	private SplitPanel contentWindow;
	
	private SplitPanel subContentWindow;
	
	private TabSheet openFilesTabs;
			
	private MainPanel mainPanel;

	private LeftPanel leftPanel;
	
	private RightPanel rightPanel;
	
	private HorizontalLayout statusbar;
	
	private Map<String,PaintCanvas> openFiles = new HashMap<String, PaintCanvas>();	
	private Map<String, Boolean> savedStatusFiles = new HashMap<String, Boolean>();
	
	private PaintCanvas currentCanvas;
	
	public enum FileType{
		XML, JPG, PNG
	}
	
	@Override
	public void init() {	
		
		setTheme("toolkitdraw");
		
		mainWindow = new Window("IT Mill Toolkit - ToolkitDraw");
		mainWindow.setSizeFull();
		mainWindow.setStyleName("mainwindow");
		setMainWindow(mainWindow);			
			
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setStyleName("mainlayout");
		mainLayout.setSizeFull();
		mainWindow.setLayout(mainLayout);
				
		mainPanel = new MainPanel();		
		mainPanel.setWidth("100%");
		mainPanel.addListener(this);
		mainLayout.addComponent(mainPanel);
		
		HorizontalLayout center = new HorizontalLayout();
		center.setSizeFull();
				
		//Create the open file tabsheet	
		openFilesTabs = new TabSheet();
		openFilesTabs.setStyleName("openfiles");
		openFilesTabs.setSizeFull();
		openFilesTabs.setStyleName("openfiletabsheet");
		openFilesTabs.addListener(this);
		openFilesTabs.setImmediate(true);
	
		currentCanvas = null;
				
		rightPanel = new RightPanel(currentCanvas);
		rightPanel.setWidth("250px");
		rightPanel.setHeight("100%");
				
		leftPanel = new LeftPanel(currentCanvas,Tool.Type.PEN, this);
		leftPanel.setWidth("250px");
		leftPanel.setHeight("100%");
			
		center.addComponent(leftPanel);
		center.addComponent(openFilesTabs);
		center.setExpandRatio(openFilesTabs, 1);
		center.addComponent(rightPanel);
		
		mainLayout.addComponent(center);		
		mainLayout.setExpandRatio(center, 1);		
		
		statusbar = new HorizontalLayout();
		statusbar.setStyleName("statusbar");
		statusbar.setHeight("30px");
		statusbar.setWidth("100%");
		Label statusText = new Label("Application started");
		statusbar.addComponent(statusText);
		mainLayout.addComponent(statusbar);
		
		setImageToolsEnabled(false);
	
		mainWindow.setImmediate(true);
		
	}

	private PaintCanvas addNewFile(){
		
		PaintCanvas canvas = new PaintCanvas("100%","100%",300,400);
		canvas.setComponentBackgroundColor("515151");		
			
		//Calculate next unsaved filename
		int unsaved_counter=1;
		for(String filename : openFiles.keySet()){
			if(filename.startsWith("Unsaved")) unsaved_counter++;
		}
		
		//Put the canvas in the map and create a tab for it
		openFiles.put("Unsaved"+unsaved_counter, canvas);		
		savedStatusFiles.put("Unsaved"+unsaved_counter, false);		
		openFilesTabs.addTab(canvas,"Unsaved"+unsaved_counter,null);
						
		return canvas;
	}
	
	private boolean closeCurrentFile(){
		final PaintCanvas canvas = (PaintCanvas)openFilesTabs.getSelectedTab();
		final String filename = openFilesTabs.getTabCaption(canvas);		
		
		//Check if file has not been saved
		Boolean status = savedStatusFiles.get(filename);
				
		if(status.booleanValue()){
			
			//Select the previous tab
			Iterator<Component> tabs = openFilesTabs.getComponentIterator();
			Component prevObject = tabs.next();
			while(tabs.hasNext()){
				Component o = tabs.next();
				if(o.equals(canvas)) break;
				else prevObject = o;
			}
			
			openFilesTabs.setSelectedTab(prevObject);
			
			//Remove the tab and canvas
			openFiles.remove(filename);
			savedStatusFiles.remove(filename);
			openFilesTabs.removeComponent(canvas);	
			
			if(openFilesTabs.getSelectedTab() != null){
				currentCanvas = (PaintCanvas)openFilesTabs.getSelectedTab();
			}else{
				currentCanvas = null;
				setImageToolsEnabled(false);
			}
			
		}else{
			ConfirmPopup confirm = new ConfirmPopup("Confirm Close"
									,"Are you sure you want to close an unsaved image?",
									mainWindow);
			
			confirm.addListener(new Button.ClickListener(){
				public void buttonClick(ClickEvent event) {
					boolean confirmed = (Boolean)event.getButton().getData();					
					if(confirmed){
						//Select the previous tab
						Iterator<Component> tabs = openFilesTabs.getComponentIterator();
						Component prevObject = tabs.next();
						while(tabs.hasNext()){
							Component o = tabs.next();
							if(o.equals(canvas)) break;
							else prevObject = o;
						}
						
						openFilesTabs.setSelectedTab(prevObject);
						
						//Remove the image
						openFiles.remove(filename);
						savedStatusFiles.remove(filename);
						openFilesTabs.removeComponent(canvas);
						
						if(openFilesTabs.getSelectedTab() != null){
							currentCanvas = (PaintCanvas)openFilesTabs.getSelectedTab();
						}else{
							currentCanvas = null;
							setImageToolsEnabled(false);
						}
					}					
				}				
			});			
			
			confirm.show();
		}		
	
		return status;
	}
	
	private void setImageToolsEnabled(boolean enabled){
		leftPanel.setEnabled(enabled);
		rightPanel.setEnabled(enabled);
		mainPanel.setImageToolsEnabled(enabled);		
		openFilesTabs.setEnabled(enabled);
	}
	
	
	@Override
	public void buttonClick(ClickEvent event) {
		
	
	}

	@Override
	public void valueChange(ValueChangeEvent event) {		
		Object value = event.getProperty().getValue();
				
		if(value instanceof MainPanel.Type){			
			switch((MainPanel.Type)value){
				case UNDO: 	currentCanvas.undo(); 
							setStatusbarText("Undo operation done");
				break;
				
				case REDO: 	currentCanvas.redo(); 
							setStatusbarText("Redo operation done");
				break;
				
				case NEW: 	openFilesTabs.setSelectedTab(addNewFile());						
							setImageToolsEnabled(true);
							leftPanel.setTool(Tool.Type.PEN);
							setStatusbarText("New file opened");
				break;
							
				case CLOSE:{
					boolean unsaved = closeCurrentFile();
					if(unsaved) setStatusbarText("Closing unsaved image?");
					else setStatusbarText("Image closed");					
					break;
				}
				
				case SAVE:{
					final SavePopup pop = new SavePopup("Select filetype","", mainWindow);
					pop.addListener(new Button.ClickListener(){
						public void buttonClick(ClickEvent event) {
							if((Boolean)event.getButton().getData()){
								saveFile(pop.getValue());
							}
						}						
					});
					pop.show();
					break;
				}
					
				//Demos
				case DEMO1:{
						SimpleGraphDemo demo = new SimpleGraphDemo();
						mainWindow.addWindow(demo);
						setStatusbarText("Opening bar graph demo");
				}
			}
		}		
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabs = (TabSheet)event.getSource();
		String filename = tabs.getSelectedTab().getCaption();		
		PaintCanvas canvas = openFiles.get(filename);
		
		//Set the current canvas to the tabs canvas
		this.currentCanvas = canvas;	
		rightPanel.setCanvas(canvas);		
		leftPanel.setCanvas(canvas);
	}
	
	public void setStatusbarText(String text){
		Object label = statusbar.getComponentIterator().next();
		((Label)label).setValue(text);
	}
	
	public void saveFile(FileType type){
		
		if(currentCanvas == null) return;
		
		switch(type){
			case XML:{
				//Listen for the result
				currentCanvas.addListener(new ValueChangeListener(){
					public void valueChange(ValueChangeEvent event) {						
						if(event instanceof ImageXMLRecievedEvent){							
							currentCanvas.removeListener(this);							
							ImageXMLRecievedEvent evnt = (ImageXMLRecievedEvent)event;
							processSavedFile(evnt);
						}									
					}					
				});	
				
				//Request XML image 
				currentCanvas.getImageXML(); 
				break;
			}	
			
			case PNG:{
				//Listen for the result
				currentCanvas.addListener(new ValueChangeListener(){
					public void valueChange(ValueChangeEvent event) {						
						if(event instanceof ImagePNGRecievedEvent){							
							currentCanvas.removeListener(this);							
							ImagePNGRecievedEvent evnt = (ImagePNGRecievedEvent)event;							
							processSavedFile(evnt);
						}									
					}					
				});	
				
				//Request XML image 
				currentCanvas.getImagePNG(); 
				break;				
			}
		}
		
	}
		
	public void processSavedFile(ValueChangeEvent event){
		
		DownloadStream stream = null;
		
		if(event instanceof ImageXMLRecievedEvent){
			System.out.println("XML");
		}
		else if(event instanceof ImagePNGRecievedEvent){			
			ImagePNGRecievedEvent evnt = (ImagePNGRecievedEvent)event;
			ByteArrayInputStream iStream = new ByteArrayInputStream(evnt.getData());
			stream = new DownloadStream(iStream,"Image/PNG","image.png");
		}
				
		if(stream != null){
			mainWindow.showNotification("Image saved");		
			
			final DownloadStream str = stream;
			Resource res = new StreamResource(new StreamResource.StreamSource(){			
				public InputStream getStream() {
					return str.getStream();
				}				
			}, str.getFileName(), this);
			
			mainWindow.open(res);			
		}
		else	
			mainWindow.showNotification("Saving image failed");
		
		
		
	}
	
}
