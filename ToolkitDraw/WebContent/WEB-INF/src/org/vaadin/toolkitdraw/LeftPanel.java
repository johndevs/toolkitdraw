package org.vaadin.toolkitdraw;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.toolkitdraw.components.flashcanvas.FlashCanvas;
import org.vaadin.toolkitdraw.components.flashcanvas.FlashCanvas.Interactive;
import org.vaadin.toolkitdraw.components.flashcanvas.enums.BrushType;
import org.vaadin.toolkitdraw.tools.Ellipse;
import org.vaadin.toolkitdraw.tools.Fill;
import org.vaadin.toolkitdraw.tools.Line;
import org.vaadin.toolkitdraw.tools.Pen;
import org.vaadin.toolkitdraw.tools.Polygon;
import org.vaadin.toolkitdraw.tools.Select;
import org.vaadin.toolkitdraw.tools.Square;
import org.vaadin.toolkitdraw.tools.Text;
import org.vaadin.toolkitdraw.tools.Tool;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class LeftPanel extends VerticalLayout implements ClickListener {

	private static final long serialVersionUID = 1L;

	private Panel toolPanel;
	
	private Panel optionPanel;
		
	private List<Tool>  tools = new ArrayList<Tool>();			
	
	private FlashCanvas canvas;
	
	private BrushType currentBrush;
		
	public LeftPanel(FlashCanvas canvas, BrushType selectedTool) {
		super();
		setStyleName("leftpanel");
		setSizeFull();
				
		tools = createToolset(canvas);
		this.canvas = canvas;
	
		//Create the tools tab
		GridLayout toolGrid = new GridLayout(5,4);
		toolGrid.setSpacing(true);
		for(Tool tool : tools)
			toolGrid.addComponent(tool.getButton());
		
		//Create the tool panel
		toolPanel = new Panel("Tools",toolGrid);
		addComponent(toolPanel);
					
		//Create the tool options
		optionPanel = new Panel("Tool Options");	
		optionPanel.setSizeFull();
		addComponent(optionPanel);		
		setExpandRatio(optionPanel, 1);
		
	}	
	
	/**
	 * This function creates the available tools
	 * 
	 * @param canvas
	 * 		The paintcanvas component these tools draw on
	 * @return
	 * 		List of tools
	 */
	private List<Tool> createToolset(FlashCanvas canvas){
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
		ToolkitDrawApplication.getBottomBar().removeAllComponents();
		optionPanel.setContent(selected.createToolOptions());
		optionPanel.setCaption(selected.getName());
		
		//Check for interactivity
		Interactive i = this.canvas.getInteractive();
		
		if(i != null){
			i.setBrush(tool);
			selected.sendCurrentSettings();
			currentBrush = tool;
		}
		
		else System.out.println("ERROR: Canvas is not interactive");
		
	}
	
	public BrushType getSelectedBrush(){
		return currentBrush;
	}
	
	@Override
	public void buttonClick(ClickEvent event) {		
		if(event.getButton().getData() instanceof BrushType){
			BrushType type = (BrushType)event.getButton().getData();
			setTool(type);			
		}		
	}
	
	public FlashCanvas getCanvas() {
		return canvas;
	}	
	
	public void setCanvas(FlashCanvas canvas) {	
		if(canvas == null){
			System.err.println("Cannot set null canvas");
			return;
		}
					
		this.canvas = canvas;
		
		//Update toolset canvas
		for(Tool tool : tools){
			tool.setCanvas(this.canvas);
		}
	}
}