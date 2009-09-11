package com.vaadin.toolkitdraw;

import com.vaadin.toolkitdraw.components.paintcanvas.enums.CacheMode;
import com.vaadin.toolkitdraw.components.paintcanvas.enums.Plugin;

public class Preferences {

	private Plugin plugin = Plugin.FLASH;
	
	private CacheMode cacheMode = CacheMode.SERVER;

	public Preferences(){
		
	}
	
	public void copy(Preferences pref){
		this.plugin = pref.getPlugin();
		this.cacheMode = pref.getCacheMode();
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
	
}
