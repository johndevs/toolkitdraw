package com.vaadin.toolkitdraw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.catalina.startup.SetAllPropertiesRule;

import com.vaadin.Application;
import com.vaadin.toolkitdraw.MainPanel.Type;
import com.vaadin.toolkitdraw.components.paintcanvas.PaintCanvas;
import com.vaadin.toolkitdraw.tools.Ellipse;
import com.vaadin.toolkitdraw.tools.Line;
import com.vaadin.toolkitdraw.tools.Pen;
import com.vaadin.toolkitdraw.tools.Polygon;
import com.vaadin.toolkitdraw.tools.Square;
import com.vaadin.toolkitdraw.tools.Text;
import com.vaadin.toolkitdraw.tools.Tool;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class LeftPanel extends Accordion implements ClickListener {

	private Layout tab1;
	private Layout tab2;
		
	private List<Tool>  tools = new ArrayList<Tool>();			
	private PaintCanvas canvas;
	
	private Application application;
	
	public LeftPanel(PaintCanvas canvas, PaintCanvas.BrushType selectedTool, Application app) {
		super();
		setStyleName("leftpanel");
		setSizeFull();
		
		application = app;
		tools = createToolset(canvas);
		this.canvas = canvas;
	
		//Create the tools tab
		tab1 = new GridLayout(4,4);	
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
		
		Text text = new Text(canvas);
		text.getButton().addListener((ClickListener)this);
		toolset.add(text);		
		
		return toolset;
	}
	
	public void setTool(PaintCanvas.BrushType tool){
		
		if(this.canvas == null){
			System.err.println("No canvas was found!");
			return;
		}
		
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
		if(selected == null){
			System.err.println("Tool is not in toolset");
			return;		
		}
		
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
			
			case TEXT:		tab2.addComponent(selected.createToolOptions());
							this.canvas.setBrush(PaintCanvas.BrushType.TEXT);
			break;
				
			default:		System.out.println("No tool with the id could be selected");
		}
	}
	
	@Override
	public void buttonClick(ClickEvent event) {		
		if(event.getButton().getData() instanceof PaintCanvas.BrushType){
			PaintCanvas.BrushType type = (PaintCanvas.BrushType)event.getButton().getData();
			setTool(type);			
		}		
	}
	
	public PaintCanvas getCanvas() {
		return canvas;
	}	
	
	public void setCanvas(PaintCanvas canvas) {	
		
		if(canvas == null){
			System.err.println("Cannot set null canvas");
			return;
		}
				
		System.out.println("LeftPanel: Setting new canvas");
		
		this.canvas = canvas;
		
		//Update toolset canvas
		for(Tool tool : tools){
			tool.setCanvas(this.canvas);
		}
	}

	
}
