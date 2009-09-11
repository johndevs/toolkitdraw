package com.vaadin.paintcanvas.elements;

import java.awt.Color;
import java.awt.Graphics;

public class Layer implements IDrawable{

	private String name = "Unknown";
	
	private int width = 0;
	
	private int height = 0;
	
	private Color backgroundColor = Color.WHITE;
	
	private float alpha = 0.0f;
	
	private int x = 0;
	
	private int y = 0;
	
	public Layer(String name){
		this.name = name;
	}	
	
	@Override
	public void draw(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(x, y, width, height);
		
		//Draw shadow if the layer is the background layer
		if(name.equals("Background")){
			g.setColor(Color.BLACK);
			g.drawLine(x+width, y, x+width, y+height);
			g.drawLine(x+width, y+height, x, y+height);
		}
	}

	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
