package fi.jasoft.flashcanvas.enums;

public enum BrushType {
	PEN("Pen"),
	SQUARE("Rectangle"),
	ELLIPSE("Ellipse"),
	LINE("Line"),
	POLYGON("Polygon"),
	TEXT("Text"),
	FILL("Floodfill"),
	
	SELECT("Generic-Select"),
	RECTANGLE_SELECT("Rectangle-Select");
	
	private final String str;
	private BrushType(String s) {
		this.str = s;
	}
	public String toString(){
		return str;
	}		
}
