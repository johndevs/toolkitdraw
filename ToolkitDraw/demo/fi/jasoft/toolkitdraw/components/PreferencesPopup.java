package fi.jasoft.toolkitdraw.components;

import java.util.Arrays;


import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;
import com.vaadin.ui.themes.Reindeer;

import fi.jasoft.flashcanvas.enums.CacheMode;
import fi.jasoft.flashcanvas.enums.Plugin;
import fi.jasoft.toolkitdraw.Preferences;


public class PreferencesPopup extends Window implements ClickListener{

	private static final long serialVersionUID = 1L;

	private Window parent;
	
	private TabSheet tabs;
	
	private Preferences preferenses;
	
	private Preferences oldValues = new Preferences(); 
	
	private Button ok;
	
	private Button cancel;
	
	/**
	 * Constructor
	 * @param parent
	 * 		The parent window
	 * @param prefs
	 * 		The application preferences
	 */
	public PreferencesPopup(Window parent, Preferences prefs){
		
		this.parent = parent;
		this.preferenses = prefs;
		this.oldValues.copy(prefs);
		
		setCaption("Preferences");
		setWidth("400px");
		setHeight("400px");
		setResizable(false);		
		
		GridLayout layout = new GridLayout(1,2);
		layout.setStyleName(Reindeer.LAYOUT_BLACK);
		layout.addStyleName("preferences-popup-layout");
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
	
	/**
	 * Creates the tabsheet
	 * @return
	 * 		Returns the tabsheet
	 */
	private Component createTabsheet(){
	
		tabs = new TabSheet();
		tabs.setSizeFull();
		
		tabs.addComponent(createGeneralTab());
		tabs.addComponent(createPluginTab());
		
		return tabs;
	}
	
	/**
	 * Create the general tab
	 * @return
	 * 		Returns the general tab layout
	 */
	private Component createGeneralTab(){
		VerticalLayout layout = new VerticalLayout();
		layout.setCaption("General");
		layout.setSizeFull();
		layout.setMargin(true);
		
		//Auto save time in seconds
		final Label autosaveLbl = new Label();
		Slider autosave = new Slider("Autosave time:",0,600);
		autosave.setImmediate(true);		
		autosave.setWidth("90%");
		autosave.addListener(new Property.ValueChangeListener() {	
			private static final long serialVersionUID = 1L;
			public void valueChange(ValueChangeEvent event) {
				int seconds = (int)Double.parseDouble(event.getProperty().getValue().toString());
				preferenses.setAutosaveTime(seconds);
				
				if(seconds == 0){
					autosaveLbl.setValue("Off");
				} else if(seconds == 1){ 
					autosaveLbl.setValue("1 second");
				} else if(seconds < 60){
					autosaveLbl.setValue(seconds+" seconds");
				} else if(seconds == 60){
					autosaveLbl.setValue("1 minute");
				} else if(seconds < 120){
					autosaveLbl.setValue("1 minute and "+(seconds-60)+" seconds");
				} else {
					int min = seconds/60;
					int sec = seconds - min*60;
					
					if(sec == 0){
						autosaveLbl.setValue(min+" minutes");
					} else if(sec == 1){
						autosaveLbl.setValue(min+" minutes and 1 second");
					} else {
						autosaveLbl.setValue(min+" minutes and "+sec+" seconds");
					}
				}				
			}
		});		
		
		try {
			autosave.setValue(preferenses.getAutosaveTime());
		} catch (ValueOutOfBoundsException e) {
			//No panic
		}
		
		HorizontalLayout autosaveLayout = new HorizontalLayout();
		autosaveLayout.addComponent(autosave);
		autosaveLayout.addComponent(autosaveLbl);
		autosaveLayout.setWidth("100%");
		autosaveLayout.setComponentAlignment(autosaveLbl, Alignment.MIDDLE_LEFT);
		autosaveLayout.setComponentAlignment(autosave, Alignment.MIDDLE_LEFT);
		
		layout.addComponent(autosaveLayout);
						
		return layout;
	}
	
	/**
	 * Create the plugin tab
	 * @return
	 */
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

	public void buttonClick(ClickEvent event) {		
		if(event.getButton() == cancel){
			preferenses.copy(oldValues);
			hide();
		} else if(event.getButton() == ok){
			hide();
		}
	}
}
