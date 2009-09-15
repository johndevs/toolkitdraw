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
import com.vaadin.toolkitdraw.components.paintcanvas.PaintCanvas.Interactive;
import com.vaadin.toolkitdraw.components.paintcanvas.enums.BrushType;
import com.vaadin.toolkitdraw.tools.Ellipse;
import com.vaadin.toolkitdraw.tools.Fill;
import com.vaadin.toolkitdraw.tools.Line;
import com.vaadin.toolkitdraw.tools.Pen;
import com.vaadin.toolkitdraw.tools.Polygon;
import com.vaadin.toolkitdraw.tools.Select;
import com.vaadin.toolkitdraw.tools.Square;
import com.vaadin.toolkitdraw.tools.Text;
import com.vaadin.toolkitdraw.tools.Tool;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class LeftPanel extends VerticalLayout implements ClickListener {

	private static final long serialVersionUID = 1L;

	private Panel toolPanel;
	
	private Panel optionPanel;
		
	private List<Tool>  tools = new ArrayList<Tool>();			
	private PaintCanvas canvas;
		
	public LeftPanel(PaintCanvas canvas, BrushType selectedTool) {
		super();
		setStyleName("leftpanel");
		setSizeFull();
				
		tools = createToolset(canvas);
		this.canvas = canvas;
	
		//Create the tools tab
		GridLayout toolGrid = new GridLayout(4,4);
		for(Tool tool : tools)
			toolGrid.addComponent(tool.getButton());
		
		//Create the tool panel
		toolPanel = new Panel("Tools",toolGrid);
		toolPanel.setSizeFull();
		addComponent(toolPanel);
		setExpandRatio(toolPanel, 1);
					
		//Create the tool options
		optionPanel = new Panel("Tool Options");	
		optionPanel.setSizeFull();
		addComponent(optionPanel);		
		setExpandRatio(optionPanel, 4);
		
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
		
		Select select = new Select(canvas);
		select.getButton().addListener((ClickListener)this);
		toolset.add(select);
		
		Fill fill = new Fill(canvas);
		fill.getButton().addListener((ClickListener)this);
		toolset.add(fill);
		
		return toolset;
	}
	
	public void setTool(BrushType tool){
		
		if(this.canvas == null){
			System.err.println("No canvas was found!");
			return;
		}
						
		//Get the tool from the toolset and deselect all tools
		Tool selected = null;
		for(Tool t : tools){
			if(t.getType() == tool){
				selected = t;
				t.getButton().removeStyleName("tool-unselected");
				t.getButton().addStyleName("tool-selected");
			} else {				
				t.getButton().removeStyleName("tool-selected");
				t.getButton().addStyleName("tool-unselected");
			}
		}
		
		//Tool is not in toolset
		if(selected == null){
			System.err.println("Tool is not in toolset ("+tool+")");
			return;		
		}
		
		//Set the tool options
		optionPanel.setContent(selected.createToolOptions());
		optionPanel.setCaption(selected.getName());
		
		//Check for interactivity
		Interactive i = this.canvas.getInteractive();
		
		if(i != null){
			i.setBrush(tool);
			selected.sendCurrentSettings();
		}
		
		else System.out.println("ERROR: Canvas is not interactive");
		
	}
	
	@Override
	public void buttonClick(ClickEvent event) {		
		if(event.getButton().getData() instanceof BrushType){
			BrushType type = (BrushType)event.getButton().getData();
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
