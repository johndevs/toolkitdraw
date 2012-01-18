package fi.jasoft.toolkitdraw;

import java.util.ArrayList;
import java.util.List;


import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import fi.jasoft.flashcanvas.FlashCanvas;
import fi.jasoft.flashcanvas.FlashCanvas.Interactive;
import fi.jasoft.flashcanvas.enums.BrushType;
import fi.jasoft.toolkitdraw.panels.ToolOptionsPanel;
import fi.jasoft.toolkitdraw.panels.ToolsPanel;
import fi.jasoft.toolkitdraw.tools.Ellipse;
import fi.jasoft.toolkitdraw.tools.Fill;
import fi.jasoft.toolkitdraw.tools.Line;
import fi.jasoft.toolkitdraw.tools.Pen;
import fi.jasoft.toolkitdraw.tools.Polygon;
import fi.jasoft.toolkitdraw.tools.Select;
import fi.jasoft.toolkitdraw.tools.Square;
import fi.jasoft.toolkitdraw.tools.Text;
import fi.jasoft.toolkitdraw.tools.Tool;

public class LeftPanel extends VerticalLayout implements ClickListener {

	private static final long serialVersionUID = 1L;

	private ToolsPanel toolsPanel;
	
	private ToolOptionsPanel optionPanel;
		
	private List<Tool>  tools = new ArrayList<Tool>();			
	
	private FlashCanvas canvas;
	
	private BrushType currentBrush;
	
	private boolean minimized = false;
		
	public LeftPanel(FlashCanvas canvas, BrushType selectedTool) {
		super();
		setStyleName("leftpanel");
		setSizeFull();
		
		this.canvas = canvas;
				
		tools = createToolset(canvas);
		
		toolsPanel = new ToolsPanel(tools);
		toolsPanel.setWidth("250px");
		addComponent(toolsPanel);
		
		//Create the tool options
		optionPanel = new ToolOptionsPanel(tools);
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
				break;
			} 
		}
		
		//Tool is not in toolset
		if(selected == null){
			System.err.println("Tool is not in toolset ("+tool+")");
			return;		
		}
		
		// Remove anything in the bottom bar
		((ToolkitDrawWindow)getWindow()).getBottomBar().removeAllComponents();
	
		// Select the tool
		toolsPanel.selectTool(selected);
		optionPanel.selectTool(selected);
		
		//Check for interactivity
		Interactive i = this.canvas.getInteractive();
		
		// Send current settings to brush
		if(i != null){
			i.setBrush(tool);
			selected.sendCurrentSettings();
			currentBrush = tool;
		} else {
			System.out.println("ERROR: Canvas is not interactive");
		}
		
	}
	
	public BrushType getSelectedBrush(){
		return currentBrush;
	}
	
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
	
	
	@Override
	public void addComponent(Component c) {		
		if(minimized){
			setWidth("250px");
			minimized = false;
		}
		
		super.addComponent(c);		
		
		if(c == optionPanel)
			setExpandRatio(optionPanel, 1);		
	}
	
	@Override 
	public void removeComponent(Component c) {
		super.removeComponent(c);		
		if(!minimized && !getComponentIterator().hasNext()){
			setWidth("1px");
			minimized = true;			
		}
	}	

	public ToolsPanel getToolsPanel() {
		return toolsPanel;
	}

	public ToolOptionsPanel getToolOptionsPanel() {
		return optionPanel;
	}

}
