package org.vaadin.toolkitdraw;

import org.vaadin.toolkitdraw.components.flashcanvas.enums.CacheMode;
import org.vaadin.toolkitdraw.components.flashcanvas.enums.Plugin;

public class Preferences {

	private Plugin plugin = Plugin.FLASH;
	
	private CacheMode cacheMode = CacheMode.SERVER;
	
	private int autosaveTime = 60;
		
	public void copy(Preferences pref){
		this.plugin = pref.getPlugin();
		this.cacheMode = pref.getCacheMode();
		this.autosaveTime = pref.getAutosaveTime();
	}
	
	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	public CacheMode getCacheMode() {
		return cacheMode;
	}

	public void setCacheMode(CacheMode cacheMode) {
		this.cacheMode = cacheMode;
	}
	
	public int getAutosaveTime() {
		return autosaveTime;
	}

	public void setAutosaveTime(int autosaveTime) {
		this.autosaveTime = autosaveTime;
	}

	
}
