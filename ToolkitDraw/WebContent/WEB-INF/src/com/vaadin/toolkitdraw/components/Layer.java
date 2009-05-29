package com.vaadin.toolkitdraw.components;

import java.io.Serializable;



public class Layer implements Serializable {

	private boolean visible;
	private String color;
	private double alpha;
	
	private String name;
	private PaintCanvas canvas;
	
	public PaintCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(PaintCanvas canvas) {
		this.canvas = canvas;
	}

	public Layer(String name, PaintCanvas canvas) {			
		this.name = name;
		visible = true;
		this.canvas = canvas;
		
		this.visible = true;
		this.color = "0xFFFFFF";
		this.alpha = 0;
	}
	
	public void setVisible(boolean visible){	
		this.visible = visible;
		this.canvas.setLayerVisibility(name, visible);
	}
	
	public void setColor(String color){
		this.color = color;
		this.canvas.setLayerBackground(this, color, alpha);
	}
	
	public void setAlpha(double alpha){
		this.alpha = alpha;
		this.canvas.setLayerBackground(this, color, alpha);
	}
	
	public boolean getVisible(){
		return visible;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		
		//TODO Call canvas set name function
		this.name = name;
	}

	
}
