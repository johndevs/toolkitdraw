package fi.jasoft.flashcanvas.enums;

public enum Plugin {
	AUTO("plugin-auto", "Automatic"),
	FLASH("plugin-flash", "Flash plugin");
		
	private final String caption;
	private final String id;
	private Plugin(String id, String caption) {
		this.caption = caption;
		this.id = id;
	}
	public String toString(){
		return caption;
	}		
	public String getId(){
		return id;
	}
}
