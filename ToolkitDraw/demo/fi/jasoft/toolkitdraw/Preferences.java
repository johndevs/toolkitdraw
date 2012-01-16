package fi.jasoft.toolkitdraw;

import fi.jasoft.flashcanvas.enums.CacheMode;
import fi.jasoft.flashcanvas.enums.Plugin;

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
