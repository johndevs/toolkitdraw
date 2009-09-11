package com.vaadin.paintcanvas.enums;

public enum BrushType {
	PEN("pen")
	
	;
	
	private String name;
	private BrushType(String name) {
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
}
