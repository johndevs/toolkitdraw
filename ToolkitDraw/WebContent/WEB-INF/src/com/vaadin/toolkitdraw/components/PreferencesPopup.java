package com.vaadin.toolkitdraw.components;

import java.util.Arrays;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.toolkitdraw.Preferences;
import com.vaadin.toolkitdraw.components.paintcanvas.enums.CacheMode;
import com.vaadin.toolkitdraw.components.paintcanvas.enums.Plugin;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Select;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;


public class PreferencesPopup extends Window implements ClickListener{

	private Window parent;
	
	private TabSheet tabs;
	
	private Preferences preferenses;
	
	private Preferences oldValues = new Preferences(); 
	
	private Button ok;
	
	private Button cancel;
	
	public PreferencesPopup(Window parent, Preferences prefs){
		
		this.parent = parent;
		this.preferenses = prefs;
		this.oldValues.copy(prefs);
		
		setCaption("Preferences");
		setWidth("400px");
		setHeight("400px");
		
		GridLayout layout = new GridLayout(1,2);
		layout.setStyleName("preferences-popup-layout");
		layout.setSizeFull();
		setContent(layout);
		
		layout.addComponent(createTabsheet());
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSizeFull();
		
		HorizontalLayout btns = new HorizontalLayout();
		ok = new Button("Ok", this);
		ok.setWidth("70px");
		btns.addComponent(ok);
				
		cancel = new Button("Cancel", this);
		cancel.setWidth("70px");
		btns.addComponent(cancel);
		
		buttons.addComponent(btns);
		layout.addComponent(buttons);
		
		layout.setRowExpandRatio(0, 10);
		layout.setRowExpandRatio(1, 1);
	
		buttons.setComponentAlignment(btns, Alignment.MIDDLE_RIGHT);
	}
	
	private Component createTabsheet(){
	
		tabs = new TabSheet();
		tabs.setSizeFull();
		
		tabs.addComponent(createGeneralTab());
		tabs.addComponent(createPluginTab());
		
		return tabs;
	}
	
	private Component createGeneralTab(){
		VerticalLayout layout = new VerticalLayout();
		layout.setCaption("General");
		layout.setSizeFull();
		
		return layout;
	}
	
	private Component createPluginTab(){
		VerticalLayout layout = new VerticalLayout();
		layout.setCaption("Plugin");
		layout.setMargin(true);
		
		Select plugin = new Select("Plugin", Arrays.asList(Plugin.values()));
		plugin.select(preferenses.getPlugin());
		plugin.setNullSelectionAllowed(false);
		plugin.setImmediate(true);
		plugin.addListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			public void valueChange(ValueChangeEvent event) {
				preferenses.setPlugin((Plugin)event.getProperty().getValue());				
			}
		});
		
		layout.addComponent(plugin);
		
		Select cacheMode = new Select("Cache Mode", Arrays.asList(CacheMode.values()));
		cacheMode.select(preferenses.getCacheMode());
		cacheMode.setNullSelectionAllowed(false);
		cacheMode.addListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			public void valueChange(ValueChangeEvent event) {
				preferenses.setCacheMode((CacheMode)event.getProperty().getValue());				
			}
		});
		
		layout.addComponent(cacheMode);		
		
		return layout;
	}
	
	public void show(){
		parent.addWindow(this);
		center();
	}
	
	public void hide(){
		parent.removeWindow(this);
	}

	@Override
	public void buttonClick(ClickEvent event) {		
		if(event.getButton() == cancel){
			preferenses.copy(oldValues);
			hide();
		} else if(event.getButton() == ok){
			hide();
		}
	}
}
