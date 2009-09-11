package com.vaadin.paintcanvas.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.paintcanvas.elements.Layer;

public class LayerUtil {

	private static List<Layer> layers = new ArrayList<Layer>();
	
	private static Layer currentLayer;
	
	public static Layer addLayer(String name, boolean setCurrent){
		
		Layer newlayer = null;
		
		//Check if we have a layer with the same name
		for(Layer layer : layers){
			if(layer.getName().equals(name))
				newlayer = layer;
				break;
		}
		
		if(newlayer == null){
			newlayer = new Layer(name);
			layers.add(newlayer);
		}
		
		if(setCurrent)
			currentLayer = newlayer;
		
		return newlayer;
	}
	
	public static List<Layer> getLayers(){
		return Collections.unmodifiableList(layers);
	}
	
	public static Layer getCurrentLayer(){
		return currentLayer;
	}
	
}
