package com.itmill.toolkitdraw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.data.Property.ValueChangeEvent;
import com.itmill.toolkit.data.Property.ValueChangeListener;
import com.itmill.toolkit.data.util.ObjectProperty;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.CustomComponent;
import com.itmill.toolkit.ui.HorizontalLayout;
import com.itmill.toolkit.ui.MenuBar;
import com.itmill.toolkit.ui.Button.ClickEvent;
import com.itmill.toolkit.ui.Button.ClickListener;
import com.itmill.toolkit.ui.MenuBar.Command;

public class MainPanel extends MenuBar implements ValueChangeListener, Command{
		
	private List<ValueChangeListener> listeners = new ArrayList<ValueChangeListener>();
	private Map<String, Type> typeMap = new HashMap<String, Type>();
	
	public static enum Type { 
		NEW, SAVE, UNDO, REDO, CLOSE,
		DEMO1,
	};
	
	public MainPanel(){
		
		//Map menuitem with a type
		typeMap.put("New", Type.NEW);
		typeMap.put("Save", Type.SAVE);
		typeMap.put("Undo", Type.UNDO);
		typeMap.put("Redo", Type.REDO);
		typeMap.put("Close", Type.CLOSE);
		typeMap.put("Simple graph", Type.DEMO1);
		
		//Create the file menu
		MenuBar.MenuItem file = addItem("File",null,null);
		MenuBar.MenuItem newFile = file.addItem("New", null,this);
		MenuBar.MenuItem saveFile = file.addItem("Save", null, this);
		MenuBar.MenuItem closeFile = file.addItem("Close", null, this);
		
		//Create the edit menu
		MenuBar.MenuItem edit = addItem("Edit",null,null);
		MenuBar.MenuItem undo = edit.addItem("Undo", null, this);
		MenuBar.MenuItem redo = edit.addItem("Redo", null, this);	
		
		//Create the demo menu
		MenuBar.MenuItem demos = addItem("Demos", null,null);
		MenuBar.MenuItem simpleGraphDemo = demos.addItem("Simple graph", null, this);
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
