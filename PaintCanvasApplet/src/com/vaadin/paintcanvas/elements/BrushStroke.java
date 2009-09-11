package com.vaadin.paintcanvas.elements;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class BrushStroke {
	public int x = 0;
	public int y = 0;
	public Color color = Color.BLACK;
	public List<Point> points = new ArrayList<Point>();
}
