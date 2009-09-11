package com.vaadin.paintcanvas.elements;

import java.awt.Component;
import java.awt.Point;

import com.vaadin.paintcanvas.enums.BrushType;

public interface IBrush extends IDrawable{	
	/**
	 * This is called when the mouse button is pressed down
	 */
	public void beginStroke();
	
	/**
	 * This is called when the mouse is moved and the button is pressed
	 * @param p
	 */
	public void processPoint(Point p);
	
	/**
	 * This is called when the mouse button is released
	 */
	public void endStroke();
	
	/**
	 * This is called when end-key is pressed
	 */
	public void endBrush();
	
	public BrushType getType();
}
