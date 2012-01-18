package fi.jasoft.toolkitdraw;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import sun.misc.BASE64Encoder;

import com.vaadin.Application;
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
import fi.jasoft.flashcanvas.enums.CacheMode;
import fi.jasoft.flashcanvas.events.ImageUploadEvent;
import fi.jasoft.flashcanvas.events.ImageUploadListener;
import fi.jasoft.flashcanvas.events.ImageUploadEvent.ImageType;
import fi.jasoft.toolkitdraw.components.ConfirmPopup;
import fi.jasoft.toolkitdraw.components.OpenPopup;
import fi.jasoft.toolkitdraw.components.PreferencesPopup;
import fi.jasoft.toolkitdraw.components.SavePopup;
import fi.jasoft.toolkitdraw.demos.SimpleGraphDemo;
import fi.jasoft.toolkitdraw.demos.TicTacToe;


public class ToolkitDrawApplication extends Application {

	private static final String THEME = "toolkitdraw";
	
	@Override
	public void init() {	
		setMainWindow(new ToolkitDrawWindow());	
		setTheme(THEME);	
	}

}
