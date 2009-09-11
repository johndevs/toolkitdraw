package com.vaadin.paintcanvas.util;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.paintcanvas.brushes.Pen;
import com.vaadin.paintcanvas.elements.IBrush;
import com.vaadin.paintcanvas.elements.Layer;
import com.vaadin.paintcanvas.enums.BrushType;

public class GraphicsUtil {
	
	private static Map<Layer, List<IBrush>> history = new HashMap<Layer, List<IBrush>>();
	
	private static IBrush currentBrush;
	
	public static void redraw(Graphics g){
		for(Layer layer : LayerUtil.getLayers()){
			layer.draw(g);
			
			List<IBrush> strokes = history.get(layer);
			if(strokes != null){
				for(IBrush brush : strokes){
					g.drawChars(("brush "+strokes.indexOf(brush)).toCharArray(), 0, 50, 0, 0);
					brush.draw(g);
				}
			}
		}
	}
	
	
	public static void setBrush(BrushType type){
		switch(type){
			case PEN:	currentBrush = new Pen(LayerUtil.getCurrentLayer()); break;
		}
		
		if(history.get(LayerUtil.getCurrentLayer()) == null){
			history.put(LayerUtil.getCurrentLayer(), new ArrayList<IBrush>());
		}
		
		history.get(LayerUtil.getCurrentLayer()).add(currentBrush);		
	}
	
	public static IBrush getCurrentBrush(){
		return currentBrush;
	}
	
	
}
