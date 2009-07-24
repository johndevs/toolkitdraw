package com.vaadin.toolkitdraw.components.paintcanvas.enums;

public enum CacheMode {
	
	AUTO("cache-auto"),
	CLIENT("cache-client"),
	SERVER("cache-server"),
	NONE("cache-none");	
	
	
	private final String str;
	private CacheMode(String s) {
		this.str = s;
	}
	public String toString(){
		return str;
	}		
}
