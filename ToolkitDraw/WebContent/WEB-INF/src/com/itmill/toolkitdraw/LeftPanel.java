package com.itmill.toolkitdraw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.itmill.toolkit.data.Property.ValueChangeEvent;
import com.itmill.toolkit.data.Property.ValueChangeListener;
import com.itmill.toolkit.ui.Accordion;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.GridLayout;
import com.itmill.toolkit.ui.Label;
import com.itmill.toolkit.ui.Layout;
import com.itmill.toolkit.ui.TextField;
import com.itmill.toolkit.ui.VerticalLayout;
import com.itmill.toolkit.ui.Button.ClickEvent;
import com.itmill.toolkit.ui.Button.ClickListener;
import com.itmill.toolkitdraw.MainPanel.Type;
import com.itmill.toolkitdraw.components.PaintCanvas;
import com.itmill.toolkitdraw.tools.Ellipse;
import com.itmill.toolkitdraw.tools.Line;
import com.itmill.toolkitdraw.tools.Pen;
import com.itmill.toolkitdraw.tools.Square;
import com.itmill.toolkitdraw.tools.Tool;

public class LeftPanel extends Accordion implements ClickListener {

	private Layout tab1;
	private Layout tab2;
		
	private List<Tool>  tools = new ArrayList<Tool>();			
	private PaintCanvas canvas;
	
	
	public LeftPanel(PaintCanvas canvas, Tool.Type selectedTool) {
		super();
		setStyleName("leftpanel");
		setSizeFull();
		
		tools = createToolset(canvas);
	
		//Create the tools tab
		tab1 = new GridLayout(5,5);
		tab1.setSizeFull();
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
		
		return toolset;
	}
	
	public void setTool(Tool.Type tool){
		
		if(canvas == null) return;
		
		//Get the tool from the toolset
		Tool selected = null;
		for(Tool t : tools){
			if(t.getType() == tool){
				selected = t;
				break;
			}
		}
		
		//Tool is not in toolset
		if(selected == null) return;		
		
		tab2.removeAllComponents();
		
		switch(selected.getType()){
		
			case PEN: 		tab2.addComponent(selected.createToolOptions()); 
							this.canvas.setBrush("Pen");
			break;
							
			case SQUARE:	tab2.addComponent(selected.createToolOptions()); 
							this.canvas.setBrush("Square");			
			break;
			
			case ELLIPSE:	tab2.addComponent(selected.createToolOptions());
							this.canvas.setBrush("Ellipse");
			break;			
			
			case LINE:		tab2.addComponent(selected.createToolOptions());
							this.canvas.setBrush("Line");
			break;			
		}
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		
		if(event.getButton().getData() == Tool.Type.PEN){
			setTool(Tool.Type.PEN);			
		}
		
		if(event.getButton().getData() == Tool.Type.SQUARE){
			setTool(Tool.Type.SQUARE);			
		}
		
		if(event.getButton().getData() == Tool.Type.ELLIPSE){
			setTool(Tool.Type.ELLIPSE);
		}
		
		if(event.getButton().getData() == Tool.Type.LINE){
			setTool(Tool.Type.LINE);
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
