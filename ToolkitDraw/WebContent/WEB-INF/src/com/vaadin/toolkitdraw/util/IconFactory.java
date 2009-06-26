package com.vaadin.toolkitdraw.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;


public class IconFactory implements Serializable {
	
	public static enum Icons{
		ICON_PEN, ICON_SQUARE, ICON_ELLIPSE, ICON_LINE, ICON_POLY, ICON_TEXT,
		DOWN_ARROW, UP_ARROW, PLUS, MINUS
	}
	
	
	private IconFactory(){ 
	}
		
	public static Resource getIcon(Icons icon){	
		Resource res = null;
		
		switch(icon){
			case ICON_PEN:		res = new ThemeResource("icons/penTool.png"); break;
			case ICON_SQUARE:	res = new ThemeResource("icons/squareTool.png"); break;		
			case ICON_ELLIPSE:	res = new ThemeResource("icons/ellipseTool.png"); break;
			case ICON_LINE:		res = new ThemeResource("icons/lineTool.png"); break;
			case ICON_POLY:		res = new ThemeResource("icons/polygonTool.png"); break;
			case ICON_TEXT:		res = new ThemeResource("icons/textTool.png"); break;
			
			case DOWN_ARROW:	res = new ThemeResource("icons/downArrow.png"); break;
			case UP_ARROW:		res = new ThemeResource("icons/upArrow.png"); break;
			case PLUS:			res = new ThemeResource("icons/plus.png"); break;
			case MINUS:			res = new ThemeResource("icons/minus.png"); break;
		}
		
		return res;
	}
	
	
}
