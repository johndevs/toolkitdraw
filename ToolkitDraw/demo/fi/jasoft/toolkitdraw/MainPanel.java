package fi.jasoft.toolkitdraw;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.Command;

public class MainPanel extends MenuBar implements ValueChangeListener, Command{
	private static final long serialVersionUID = 1L;
	private List<ValueChangeListener> listeners = new ArrayList<ValueChangeListener>();

	public static enum Type { 
		NEW("New"), 
		SAVE("Save"), 
		UNDO("Undo"), 
		REDO("Redo"), 
		CLOSE("Close"),
		OPEN("Open"),
		PREFERENCES("Preferences"),
		
		DEMO1("Simple graph"), 
		DEMO2("Tic-Tac-Toe"),
		
		SELECTION_ALL("Select all"), 
		SELECTION_REMOVE("Remove selection"), 
		CROP("Crop to selection"),
		
		TOOL_WINDOW("Tools"), 
		TOOL_OPTIONS_WINDOW("Tool Options"), 
		IMAGE_INFO_WINDOW("Image Info"), 
		LAYER_WINDOW("Layers");
		
		private String caption;
		private Type(String caption){
			this.caption = caption;
		}
		
		@Override
		public String toString(){
			return caption;
		}
	};
	
	public MainPanel(){
		
		setStyleName("menu");
				
		//Create the file menu
		MenuBar.MenuItem file = addItem("File",null,null);
		@SuppressWarnings("unused")
		MenuBar.MenuItem newFile = file.addItem(Type.NEW.toString(), null,this);
		@SuppressWarnings("unused")
		MenuBar.MenuItem openFile = file.addItem(Type.OPEN.toString(), null, this);
		@SuppressWarnings("unused")
		MenuBar.MenuItem saveFile = file.addItem(Type.SAVE.toString(), null, this);
		@SuppressWarnings("unused")
		MenuBar.MenuItem closeFile = file.addItem(Type.CLOSE.toString(), null, this);
		
		//Create the edit menu
		MenuBar.MenuItem edit = addItem("Edit",null,null);
		@SuppressWarnings("unused")
		MenuBar.MenuItem undo = edit.addItem(Type.UNDO.toString(), null, this);
		@SuppressWarnings("unused")
		MenuBar.MenuItem redo = edit.addItem(Type.REDO.toString(), null, this);	
		@SuppressWarnings("unused")
		MenuBar.MenuItem prefs = edit.addItem(Type.PREFERENCES.toString(), null, this);
		
		//Create the select menu
		MenuBar.MenuItem select = addItem("Selection",null,null);
		@SuppressWarnings("unused")
		MenuBar.MenuItem selectAll = select.addItem(Type.SELECTION_ALL.toString(), null, this);
		@SuppressWarnings("unused")
		MenuBar.MenuItem selectNone  = select.addItem(Type.SELECTION_REMOVE.toString(), null, this);
		
		// Hidden until implemented properly
		//MenuBar.MenuItem crop = select.addItem("Crop to selection", null, this);
		
		//Create the window menu (Hidden until needed)
		/*
		MenuBar.MenuItem window = addItem("Window",null,null);
		@SuppressWarnings("unused")
		MenuBar.MenuItem toolWindow = window.addItem(Type.TOOL_WINDOW.toString(), null, this);
		@SuppressWarnings("unused")
		MenuBar.MenuItem toolOptionsWindow = window.addItem(Type.TOOL_OPTIONS_WINDOW.toString(), null, this);
		@SuppressWarnings("unused")
		MenuBar.MenuItem infoWindow = window.addItem(Type.IMAGE_INFO_WINDOW.toString(), null, this);
		@SuppressWarnings("unused")
		MenuBar.MenuItem layerWindow = window.addItem(Type.LAYER_WINDOW.toString(), null, this);
		*/
		
		//Create the demo menu
		MenuBar.MenuItem demos = addItem("Demos", null,null);
		@SuppressWarnings("unused")
		MenuBar.MenuItem simpleGraphDemo = demos.addItem(Type.DEMO1.toString(), null, this);
		@SuppressWarnings("unused")
		MenuBar.MenuItem ticTacToeDemo = demos.addItem(Type.DEMO2.toString(), null, this);
	}
	
	public void setImageToolsEnabled(boolean enabled){
		
		
	}

	
	public void addListener(ValueChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ClickListener listener) {
		listeners.remove(listener);
	}

	public void menuSelected(MenuItem selectedItem) {
		
		Type selectedType = null;
		String caption = selectedItem.getText();
		for(Type t : Type.values()){
			if(t.toString().equals(caption)){
				selectedType = t;
				break;
			}
		}
		
		if(selectedType != null){
			final Type type = selectedType;		
			ValueChangeEvent evnt = new Property.ValueChangeEvent(){
				private static final long serialVersionUID = 1L;
	
				public Property getProperty() {				
					return new ObjectProperty(type);				
				}			
			};
			
			valueChange(evnt);
		}
	}

	public void valueChange(ValueChangeEvent event) {
		for(ValueChangeListener listener : listeners){					
			listener.valueChange(event);
		}
		
	}
}
