package fi.jasoft.flashcanvas.enums;

public enum CacheMode {
	
	AUTO("cache-auto", "Autoselect cache"),
	CLIENT("cache-client", "Use client cache"),
	SERVER("cache-server", "Use server cache"),
	NONE("cache-none", "Disable caching");	
	
	
	private final String caption;
	private final String id;
	private CacheMode(String id, String caption) {
		this.id = id;
		this.caption = caption;
	}
	public String toString(){
		return caption;
	}		
	public String getId(){
		return id;
	}
}
