package com.vaadin.toolkitdraw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.MenuBar.MenuBarImages;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.Command;

public class MainPanel extends MenuBar implements ValueChangeListener, Command{
		
	private List<ValueChangeListener> listeners = new ArrayList<ValueChangeListener>();
	private Map<String, Type> typeMap = new HashMap<String, Type>();
	
	public static enum Type { 
		NEW, SAVE, UNDO, REDO, CLOSE,OPEN,PREFERENCES,
		DEMO1, DEMO2,
		SELECTION_ALL, SELECTION_REMOVE, CROP
	};
	
	public MainPanel(){
		
		setStyleName("menu");
		
		//Map menuitem with a type
		typeMap.put("New", Type.NEW);
		typeMap.put("Open", Type.OPEN);
		typeMap.put("Save", Type.SAVE);
		typeMap.put("Undo", Type.UNDO);
		typeMap.put("Redo", Type.REDO);
		typeMap.put("Close", Type.CLOSE);
		typeMap.put("Simple graph", Type.DEMO1);
		typeMap.put("Tic-Tac-Toe", Type.DEMO2);
		typeMap.put("Select all", Type.SELECTION_ALL);
		typeMap.put("Remove selection", Type.SELECTION_REMOVE);
		typeMap.put("Crop to selection", Type.CROP);
		typeMap.put("Preferences", Type.PREFERENCES);
		
		//Create the file menu
		MenuBar.MenuItem file = addItem("File",null,null);
		MenuBar.MenuItem newFile = file.addItem("New", null,this);
		MenuBar.MenuItem openFile = file.addItem("Open", null, this);
		MenuBar.MenuItem saveFile = file.addItem("Save", null, this);
		MenuBar.MenuItem closeFile = file.addItem("Close", null, this);
		
		//Create the edit menu
		MenuBar.MenuItem edit = addItem("Edit",null,null);
		MenuBar.MenuItem undo = edit.addItem("Undo", null, this);
		MenuBar.MenuItem redo = edit.addItem("Redo", null, this);	
		MenuBar.MenuItem prefs = edit.addItem("Preferences", null, this);
		
		//Create the select menu
		MenuBar.MenuItem select = addItem("Selection",null,null);
		MenuBar.MenuItem selectAll = select.addItem("Select all", null, this);
		MenuBar.MenuItem selectNone  = select.addItem("Remove selection", null, this);
		
		// Hidden until implemented properly
		//MenuBar.MenuItem crop = select.addItem("Crop to selection", null, this);
		
		//Create the demo menu
		MenuBar.MenuItem demos = addItem("Demos", null,null);
		MenuBar.MenuItem simpleGraphDemo = demos.addItem("Simple graph", null, this);
		MenuBar.MenuItem ticTacToeDemo = demos.addItem("Tic-Tac-Toe",null,this);
	}
	
	public void setImageToolsEnabled(boolean enabled){
		
		
	}

	
	public void addListener(ValueChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ClickListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void menuSelected(MenuItem selectedItem) {
		
		final Type type = typeMap.get(selectedItem.getText());
		
		ValueChangeEvent evnt = new Property.ValueChangeEvent(){
			@Override
			public Property getProperty() {				
				return new ObjectProperty(type);				
			}			
		};
		
		valueChange(evnt);
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		for(ValueChangeListener listener : listeners){					
			listener.valueChange(event);
		}
		
	}
}
