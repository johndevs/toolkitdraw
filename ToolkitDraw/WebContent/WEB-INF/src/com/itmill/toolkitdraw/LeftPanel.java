package com.itmill.toolkitdraw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.catalina.startup.SetAllPropertiesRule;

import com.itmill.toolkit.Application;
import com.itmill.toolkit.data.Property.ValueChangeEvent;
import com.itmill.toolkit.data.Property.ValueChangeListener;
import com.itmill.toolkit.terminal.gwt.client.ui.Icon;
import com.itmill.toolkit.ui.Accordion;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.GridLayout;
import com.itmill.toolkit.ui.Label;
import com.itmill.toolkit.ui.Layout;
import com.itmill.toolkit.ui.Panel;
import com.itmill.toolkit.ui.TextField;
import com.itmill.toolkit.ui.VerticalLayout;
import com.itmill.toolkit.ui.Button.ClickEvent;
import com.itmill.toolkit.ui.Button.ClickListener;
import com.itmill.toolkitdraw.MainPanel.Type;
import com.itmill.toolkitdraw.components.PaintCanvas;
import com.itmill.toolkitdraw.tools.Ellipse;
import com.itmill.toolkitdraw.tools.Line;
import com.itmill.toolkitdraw.tools.Pen;
import com.itmill.toolkitdraw.tools.Polygon;
import com.itmill.toolkitdraw.tools.Square;
import com.itmill.toolkitdraw.tools.Tool;

public class LeftPanel extends Accordion implements ClickListener {

	private Layout tab1;
	private Layout tab2;
		
	private List<Tool>  tools = new ArrayList<Tool>();			
	private PaintCanvas canvas;
	
	private Application application;
	
	public LeftPanel(PaintCanvas canvas, Tool.Type selectedTool, Application app) {
		super();
		setStyleName("leftpanel");
		setSizeFull();
		
		application = app;
		tools = createToolset(canvas);
	
		//Create the tools tab
		tab1 = new GridLayout(4,4);
		//tab1.setSizeFull();
		tab1.setCaption("Tools");				
		addComponent(tab1);		
		
		for(Tool tool : tools)
			tab1.addComponent(tool.getButton());
		
		
		//Create the too options
		tab2 = new VerticalLayout();
		tab2.setCaption("Tool Options");	
		tab2.setSizeFull();
		addComponent(tab2);										
	}	
	
	/**
	 * This function creates the available tools
	 * 
	 * @param canvas
	 * 		The paintcanvas component these tools draw on
	 * @return
	 * 		List of tools
	 */
	private List<Tool> createToolset(PaintCanvas canvas){
		List<Tool> toolset = new ArrayList<Tool>();
	
		Pen pen = new Pen(canvas);
		pen.getButton().addListener((ClickListener)this);
		toolset.add(pen);	
		
		Line line = new Line(canvas);
		line.getButton().addListener((ClickListener)this);	
		toolset.add(line);
		
		Square square = new Square(canvas);
		square.getButton().addListener((ClickListener)this);	
		toolset.add(square);
		
		Ellipse ellipse = new Ellipse(canvas);
		ellipse.getButton().addListener((ClickListener)this);
		toolset.add(ellipse);
		
		Polygon poly = new Polygon(canvas);
		poly.getButton().addListener((ClickListener)this);
		toolset.add(poly);
		
		return toolset;
	}
	
	public void setTool(Tool.Type tool){
		
		if(canvas == null) return;
		
		//Get the tool from the toolset and deselect all tools
		Tool selected = null;
		for(Tool t : tools){
			if(t.getType() == tool){
				selected = t;
				t.getButton().setStyleName("tool-selected");
			} else {
				
				t.getButton().setStyleName("tool-unselected");
			}
		}
		
		//Tool is not in toolset
		if(selected == null) return;		
		
		tab2.removeAllComponents();
		
						
		switch(selected.getType()){
		
			case PEN: 		tab2.addComponent(selected.createToolOptions()); 
							this.canvas.setBrush(PaintCanvas.BrushType.PEN);
			break;
							
			case SQUARE:	tab2.addComponent(selected.createToolOptions()); 
							this.canvas.setBrush(PaintCanvas.BrushType.SQUARE);			
			break;
			
			case ELLIPSE:	tab2.addComponent(selected.createToolOptions());
							this.canvas.setBrush(PaintCanvas.BrushType.ELLIPSE);
			break;			
			
			case LINE:		tab2.addComponent(selected.createToolOptions());
							this.canvas.setBrush(PaintCanvas.BrushType.LINE);
			break;			
			
			case POLYGON:	tab2.addComponent(selected.createToolOptions());
							this.canvas.setBrush(PaintCanvas.BrushType.POLYGON);
			break;
				
			default:		System.out.println("No tool with the id could be selected");
		}
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		
		//Tool button events
		if(event.getButton().getData() == Tool.Type.PEN){			
			setTool(Tool.Type.PEN);			
		}
		
		else if(event.getButton().getData() == Tool.Type.SQUARE){
			setTool(Tool.Type.SQUARE);			
		}
		
		else if(event.getButton().getData() == Tool.Type.ELLIPSE){
			setTool(Tool.Type.ELLIPSE);
		}
		
		else if(event.getButton().getData() == Tool.Type.LINE){
			setTool(Tool.Type.LINE);
		}
		
		else if(event.getButton().getData() == Tool.Type.POLYGON){
			setTool(Tool.Type.POLYGON);
		}
	}
	
	public PaintCanvas getCanvas() {
		return canvas;
	}

	
	
	
	public void setCanvas(PaintCanvas canvas) {	
		
		System.out.println("LeftPanel: Setting new canvas");
		
		this.canvas = canvas;
		
		//Update toolset canvas
		for(Tool tool : tools){
			tool.setCanvas(canvas);
		}
	}

	
}
