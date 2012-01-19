package fi.jasoft.toolkitdraw;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sun.misc.BASE64Encoder;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Window.CloseEvent;

import fi.jasoft.flashcanvas.FlashCanvas;
import fi.jasoft.flashcanvas.enums.BrushType;
import fi.jasoft.flashcanvas.events.ImageUploadEvent;
import fi.jasoft.flashcanvas.events.ImageUploadListener;
import fi.jasoft.flashcanvas.events.ImageUploadEvent.ImageType;
import fi.jasoft.toolkitdraw.components.ConfirmPopup;
import fi.jasoft.toolkitdraw.components.OpenPopup;
import fi.jasoft.toolkitdraw.components.PreferencesPopup;
import fi.jasoft.toolkitdraw.components.SavePopup;
import fi.jasoft.toolkitdraw.demos.SimpleGraphDemo;
import fi.jasoft.toolkitdraw.demos.TicTacToe;

public class ToolkitDrawWindow extends Window 
	implements ClickListener, ValueChangeListener, SelectedTabChangeListener{

	private static final String STYLENAME ="mainwindow";
	
	private static final String STYLE_TOOLKITDRAW = "v-toolkitdraw";
	
	private static final String STYLE_BOTTOM_BAR = STYLE_TOOLKITDRAW+"-bottombar";
	
	private TabSheet openFilesTabs;
	
	private MainPanel mainPanel;

	private LeftPanel leftPanel;
	
	private RightPanel rightPanel;
	
	private HorizontalLayout statusbar;
	
	private Map<String,FlashCanvas> openFiles = new HashMap<String, FlashCanvas>();	
	private Map<String, Boolean> savedStatusFiles = new HashMap<String, Boolean>();
	
	private FlashCanvas currentCanvas;
		
	private Preferences preferences = new Preferences();
	
	private HorizontalLayout bottomBar = new HorizontalLayout();
	
	public ToolkitDrawWindow() {
		setStyleName(STYLENAME);
		setImmediate(true);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setStyleName(STYLE_TOOLKITDRAW);
		mainLayout.setSizeFull();
		setContent(mainLayout);
						
		mainPanel = new MainPanel();		
		mainPanel.setWidth("100%");
		mainPanel.addListener(this);
		mainLayout.addComponent(mainPanel);
		
		HorizontalLayout center = new HorizontalLayout();
		center.setSizeFull();
		
		VerticalLayout centerMiddle = new VerticalLayout();
		centerMiddle.setSizeFull();
				
		//Create the open file tabsheet	
		openFilesTabs = new TabSheet();
		openFilesTabs.setStyleName("openfiles");
		openFilesTabs.setSizeFull();
		openFilesTabs.setStyleName("openfiletabsheet");
		openFilesTabs.addListener((SelectedTabChangeListener)this);
		openFilesTabs.setImmediate(true);
	
		currentCanvas = null;
				
		rightPanel = new RightPanel(currentCanvas, preferences);
		rightPanel.setWidth("250px");
						
		leftPanel = new LeftPanel(currentCanvas, BrushType.PEN);
		leftPanel.setWidth("250px");
		leftPanel.setHeight("100%");
			
		center.addComponent(leftPanel);
						
		center.addComponent(centerMiddle);
		center.setExpandRatio(centerMiddle, 1);
				
		centerMiddle.addComponent(openFilesTabs);
		centerMiddle.setExpandRatio(openFilesTabs, 1);
		
		bottomBar.setVisible(false);
		bottomBar.setWidth("100%");
		bottomBar.setStyleName(STYLE_BOTTOM_BAR);
		centerMiddle.addComponent(bottomBar);		
					
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
	
		
	}
	

	private FlashCanvas addNewFile(){
		
		FlashCanvas canvas = new FlashCanvas("100%","100%",300,400, new Color(51,51,51));					
		
		//Set the caching mode of the canvas
		canvas.setCacheMode(preferences.getCacheMode());
		
		//Set the plugin to use
		canvas.setPlugin(preferences.getPlugin());
		
		//Set the canvas in intactive mode
		canvas.setInteractive(true);
		
		//Set the autosave time
		canvas.setAutosaveTime(preferences.getAutosaveTime());
		
		//Set brush listener
		canvas.addListener(new FlashCanvas.BrushListener() {

			public void brushStart(Component component) {
				if(leftPanel.getSelectedBrush() == BrushType.TEXT ||
					leftPanel.getSelectedBrush() == BrushType.POLYGON){
					enableBottomBar(true);
				}
			}
			
			public void brushEnd(Component component) {
				if(leftPanel.getSelectedBrush() == BrushType.TEXT ||
					leftPanel.getSelectedBrush() == BrushType.POLYGON){
					enableBottomBar(false);
				}
			}

			public void loadingComplete(Component component) {
				System.out.println("Loading complete");
				
			}
		});
		
		canvas.addListener(new ImageUploadListener() {
			public void imageUploaded(ImageUploadEvent event) {
				processSavedFile(event);
			}
		});
						
		//Set background layer to white
		canvas.getLayers().setLayerBackground(canvas.getLayers().getActiveLayer(), "FFFFFF", 1);
				
		//Set the canvas as the current canvas
		leftPanel.setCanvas(canvas);
		rightPanel.setCanvas(canvas);
		this.currentCanvas = canvas;
			
		//Calculate next unsaved filename
		int unsaved_counter=1;
		for(String filename : openFiles.keySet()){
			if(filename.startsWith("Unsaved")) unsaved_counter++;
		}
					
		//Put the canvas in the map and create a tab for it
		openFiles.put("Unsaved"+unsaved_counter, canvas);		
		savedStatusFiles.put("Unsaved"+unsaved_counter, false);		
	
		canvas.setCaption("Unsaved"+unsaved_counter);
		openFilesTabs.addTab(canvas,"Unsaved"+unsaved_counter,null);
							
		return canvas;
	}
	
	private FlashCanvas openFile(){
			
		final OpenPopup selectFile = new OpenPopup("Open file", this);
		selectFile.addListener(new Window.CloseListener(){

			public void windowClose(CloseEvent e) {
																
				//Get the uploaded image
				byte[] bytes = selectFile.getImage();
				
				//No image was selected
				if(bytes == null) return;
								
				//Encode the image to Base64
				BASE64Encoder enc = new BASE64Encoder();
				String encString = enc.encode(bytes);				
				
				//Create a new image
				FlashCanvas canvas = new FlashCanvas(encString);			
								
				//Set the caching mode of the canvas
				canvas.setCacheMode(preferences.getCacheMode());
				
				//Set the plugin to use
				canvas.setPlugin(preferences.getPlugin());
				
				//Set the canvas in intactive mode
				canvas.setInteractive(true);
				
				//Set the autosave time
				canvas.setAutosaveTime(preferences.getAutosaveTime());
				
				//Set the background color of the component
				canvas.setComponentBackgroundColor(new Color(51,51,51));
				
				//Set brush listener
				canvas.addListener(new FlashCanvas.BrushListener() {
					
					public void brushStart(Component component) {
						if(leftPanel.getSelectedBrush() == BrushType.TEXT ||
							leftPanel.getSelectedBrush() == BrushType.POLYGON){
							enableBottomBar(true);
						}
					}
					
					public void brushEnd(Component component) {
						if(leftPanel.getSelectedBrush() == BrushType.TEXT ||
							leftPanel.getSelectedBrush() == BrushType.POLYGON){
							enableBottomBar(false);
						}
					}

					public void loadingComplete(Component component) {
						System.out.println("Loading complete");
						
					}
				});
																							
				//Put the canvas in the map and create a tab for it
				openFiles.put(selectFile.getFilename(), canvas);		
				savedStatusFiles.put(selectFile.getFilename(), true);		
				
				canvas.setCaption(selectFile.getFilename());
				openFilesTabs.addTab(canvas,selectFile.getFilename(),null);		
				openFilesTabs.setSelectedTab(canvas);
				
				//Set the canvas as the current canvas
				leftPanel.setCanvas(canvas);
				rightPanel.setCanvas(canvas);
				currentCanvas = canvas;
				
				leftPanel.setTool(BrushType.PEN);
				setImageToolsEnabled(true);
			}			
		});		
		
		//Show poup
		selectFile.show();
		
		return null;
	}
	
	private boolean closeCurrentFile(){
						
		final FlashCanvas canvas = (FlashCanvas)openFilesTabs.getSelectedTab();
	
		if(canvas == null){
			System.err.println("No canvas");
			return false;
		}		
		
		final String filename = openFilesTabs.getTab(canvas).getCaption();	
		
		if(filename == null){
			System.err.println("No filename");
			return false;			
		}
		
		//Check if file has not been saved
		Boolean status = savedStatusFiles.get(filename);
				
		if(status.booleanValue()){
			
			//Select the previous tab
			Iterator<Component> tabs = (Iterator<Component>)openFilesTabs.getComponentIterator();
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
				currentCanvas = (FlashCanvas)openFilesTabs.getSelectedTab();
				if(currentCanvas == null) System.err.println("Setting canvas to null!");
			}else{
				System.err.println("Setting canvas to null!");
				currentCanvas = null;
				setImageToolsEnabled(false);
			}
			
		}else{
			ConfirmPopup confirm = new ConfirmPopup("Confirm Close"
									,"Are you sure you want to close an unsaved image?",
									this);
			
			confirm.addListener(new Button.ClickListener(){
				private static final long serialVersionUID = 1L;

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
							currentCanvas = (FlashCanvas)openFilesTabs.getSelectedTab();
							if(currentCanvas == null) System.err.println("Setting canvas to null!");
						}else{
							System.err.println("Setting canvas to null!");
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
	
	
	public void buttonClick(ClickEvent event) {
		//TODO
	
	}

	public void valueChange(ValueChangeEvent event) {		
		Object value = event.getProperty().getValue();
				
		if(value instanceof MainPanel.Type){			
			switch((MainPanel.Type)value){
				case UNDO: 	
							if(currentCanvas == null) break;
							currentCanvas.getInteractive().undo(); 
							setStatusbarText("Undo operation done");
				break;
				
				case REDO: 	if(currentCanvas == null) break;
							currentCanvas.getInteractive().redo(); 
							setStatusbarText("Redo operation done");
				break;
				
				case NEW: 	openFilesTabs.setSelectedTab(addNewFile());						
							setImageToolsEnabled(true);
							leftPanel.setTool(BrushType.PEN);
							setStatusbarText("New file opened");
				break;
				
				case OPEN:	openFile();						
							
				break;
							
				case CLOSE:{
					if(currentCanvas == null) break;
					boolean unsaved = closeCurrentFile();
					if(unsaved) setStatusbarText("Closing unsaved image?");
					else setStatusbarText("Image closed");					
					
				}break;
				
				case SAVE:{
					if(currentCanvas == null) break;
					final SavePopup pop = new SavePopup("Select filetype","", this);
					pop.addListener(new Button.ClickListener(){
						private static final long serialVersionUID = 1L;

						public void buttonClick(ClickEvent event) {													
							if((Boolean)event.getButton().getData()){								
								saveFile(pop.getValue(), pop.getDpi());
							}
						}						
					});
					pop.show();
					
				}break;
				
				case PREFERENCES:{
					final PreferencesPopup pop = new PreferencesPopup(this, preferences);
					pop.show();
				}break;
				
				//Selection releted actions
				case SELECTION_REMOVE: 	if(currentCanvas == null) break;
										currentCanvas.getInteractive().removeSelection(); 
				break;
				
				case SELECTION_ALL: 	if(currentCanvas == null) break;
										currentCanvas.getInteractive().selectAll(); 
				break;
				
				case CROP: 				if(currentCanvas == null) break;
										currentCanvas.getInteractive().crop(); 
										setStatusbarText("Cropping done."); 
				break;				
					
				//Demos
				case DEMO1:{
						SimpleGraphDemo demo = new SimpleGraphDemo();
						addWindow(demo);
						setStatusbarText("Opening bar graph demo");
						break;
				}
				
				case DEMO2:{
						TicTacToe demo = new TicTacToe();
						addWindow(demo);
						setStatusbarText("Opening tic-tac-toe");
						break;
				}
				
				//Windows
				case IMAGE_INFO_WINDOW:{
					
				}
				
				case LAYER_WINDOW:{
					
				}
				
				case TOOL_OPTIONS_WINDOW:{
					leftPanel.addComponent(leftPanel.getToolOptionsPanel());
				}
				
				case TOOL_WINDOW:{
					leftPanel.addComponent(leftPanel.getToolsPanel(),0);
				}
			}
		}		
	}

	public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabs = (TabSheet)event.getSource();
		
		String filename = tabs.getSelectedTab().getCaption();
		
		if(filename == null)
			return;
		
		if(!openFiles.containsKey(filename)){
			System.err.println("Could not find open file with filename \""+filename+"\"");
			return;
		}	
		
		FlashCanvas canvas = openFiles.get(filename);
		
		if(canvas == null){
			System.err.println("Canvas was null!");
			return;
		}		
		
		//Set the current canvas to the tabs canvas
		this.currentCanvas = canvas;	
		rightPanel.setCanvas(canvas);		
		leftPanel.setCanvas(canvas);
	}
	
	public void setStatusbarText(String text){		
		Object label = statusbar.getComponentIterator().next();
		((Label)label).setValue(text);
	}
	
	/**
	 * Saves a file
	 * 
	 * @param type
	 * 		The file type of the file
	 * @param dpi
	 * 		The dpi of the image
	 */
	public void saveFile(ImageType type, int dpi){	
		
		if(currentCanvas == null){
			System.err.println("Could not save file, canvas was null");			
			return;
		}
		
		currentCanvas.getImage(type, dpi);
	}
		
	public void processSavedFile(ImageUploadEvent event){		
		DownloadStream stream = null;
		Date now = new Date();		
		String filename = "image";
		
		if(event.getType() == ImageType.XML){
			ByteArrayInputStream iStream = new ByteArrayInputStream(event.getBytes());
			filename = "image"+now.getTime()+".xml";
			stream = new DownloadStream(iStream,"text/XML",filename);
		}
		
		else if(event.getType() == ImageType.PNG){			
			ByteArrayInputStream iStream = new ByteArrayInputStream(event.getBytes());
			filename = "image"+now.getTime()+".png";
			stream = new DownloadStream(iStream,"Image/PNG",filename);
		}
		
		else if(event.getType() == ImageType.JPG){			
			ByteArrayInputStream iStream = new ByteArrayInputStream(event.getBytes());
			filename = "image"+now.getTime()+".jpg";
			stream = new DownloadStream(iStream,"Image/JPG",filename);
		}
				
		if(stream != null){
			showNotification("Image saved");		
			
			final DownloadStream str = stream;
			Resource res = new FileStreamResource(new StreamResource.StreamSource() {				
				public InputStream getStream() {					
					return str.getStream();
				}
			}, filename, getApplication());
						
			open(res);
		}
		else{	
			showNotification("Saving image failed");
		}	
	}	
	
	public HorizontalLayout getBottomBar(){
		return bottomBar;
	}
	
	private void enableBottomBar(boolean enable){
		bottomBar.setVisible(enable);
	}
	
	public BrushType getSelectedBrush(){
		return leftPanel.getSelectedBrush();
	}
	
	public Preferences getPreferences(){
		return preferences;
	}
}
