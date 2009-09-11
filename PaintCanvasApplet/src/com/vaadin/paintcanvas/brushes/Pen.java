package com.vaadin.paintcanvas.brushes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import netscape.javascript.JSObject;

import com.vaadin.paintcanvas.elements.BrushStroke;
import com.vaadin.paintcanvas.elements.IBrush;
import com.vaadin.paintcanvas.elements.Layer;
import com.vaadin.paintcanvas.enums.BrushType;

public class Pen implements IBrush {

	private Layer layer;
	
	private List<BrushStroke> strokes = new ArrayList<BrushStroke>();
	
	private BrushStroke currentStroke;
	
	private BlockingQueue<Point> pointsNotYetDrawn = new ArrayBlockingQueue<Point>(10);
	
	private Point lastPoint;
	
	public Pen(Layer layer){
		this.layer = layer;
	}
	
	

	@Override
	public void endBrush() {
		// TODO Auto-generated method stub

	}

	@Override
	public void endStroke() {
		currentStroke = null;
	}

	@Override
	public void processPoint(Point p) {
	}

	@Override
	public void draw(Graphics g) {
		g.drawChars("PEN".toCharArray(), 0,50 , 10, 10);
	}

	@Override
	public BrushType getType() {
		return BrushType.PEN;
	}


	@Override
	public void beginStroke() {
		currentStroke = new BrushStroke();
		strokes.add(currentStroke);
	}
}
