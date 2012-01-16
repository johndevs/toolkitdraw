package fi.jasoft.flashcanvas;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import fi.jasoft.flashcanvas.enums.CacheMode;
import fi.jasoft.flashcanvas.enums.Plugin;


public class Config implements Serializable{

	private static final long serialVersionUID = 1L;

	private CacheMode cacheMode = CacheMode.AUTO;
	
	private Plugin plugin = Plugin.FLASH;

	private String componentIdentifer = "";
	
	private boolean initializationComplete = false;
	
	private boolean interactive = false;
	
	private int paperHeight = -1;
	
	private int paperWidth = -1;
	
	private Color componentColor = Color.WHITE;
	
	private Set<String> availableFonts = new HashSet<String>();
	
	private int autosave = 1;

	public CacheMode getCacheMode() {
		return cacheMode;
	}

	public void setCacheMode(CacheMode cacheMode) {
		this.cacheMode = cacheMode;
	}

	public String getComponentIdentifer() {
		return componentIdentifer;
	}

	public void setComponentIdentifer(String componentIdentifer) {
		this.componentIdentifer = componentIdentifer;
	}

	public boolean isInitializationComplete() {
		return initializationComplete;
	}

	public void setInitializationComplete(boolean initializationComplete) {
		this.initializationComplete = initializationComplete;
	}

	public boolean isInteractive() {
		return interactive;
	}

	public void setInteractive(boolean interactive) {
		this.interactive = interactive;
	}

	public int getPaperHeight() {
		return paperHeight;
	}

	public void setPaperHeight(int paperHeight) {
		this.paperHeight = paperHeight;
	}

	public int getPaperWidth() {
		return paperWidth;
	}

	public void setPaperWidth(int paperWidth) {
		this.paperWidth = paperWidth;
	}

	public Color getComponentColor() {
		return componentColor;
	}

	public void setComponentColor(Color componentColor) {
		this.componentColor = componentColor;
	}

	public Set<String> getAvailableFonts() {
		return availableFonts;
	}

	public void setAvailableFonts(Set<String> availableFonts) {
		this.availableFonts = availableFonts;
	}
	
	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	public int getAutosave() {
		return autosave;
	}

	public void setAutosave(int autosave) {
		this.autosave = autosave;
	}	
}
