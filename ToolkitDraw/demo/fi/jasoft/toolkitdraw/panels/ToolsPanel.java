package fi.jasoft.toolkitdraw.panels;

import java.util.List;


import com.vaadin.ui.GridLayout;

import fi.jasoft.toolkitdraw.components.DetachablePanel;
import fi.jasoft.toolkitdraw.tools.Tool;

public class ToolsPanel extends DetachablePanel {

	private static final long serialVersionUID = 1L;
	
	private static final String SELECTED_STYLE = "tool-selected";
	private static final String UNSELECTED_STYLE = "tool-unselected";

	private GridLayout layout;
	
	private List<Tool> tools;
	
	public ToolsPanel(List<Tool> tools){
		setCaption("Tools");
		
		this.tools = tools;
		
		layout = new GridLayout(4, (int)Math.ceil((double)tools.size()/4.0));
		layout.setSpacing(true);
		layout.setMargin(true);
		
		for(Tool tool : tools)
			layout.addComponent(tool.getButton());
		
		setContent(layout);		
	}	
	
	public void selectTool(Tool tool){
		if(tools.contains(tool)){
			for(Tool t : tools){
				if(t == tool){
					t.getButton().removeStyleName(UNSELECTED_STYLE);
					t.getButton().addStyleName(SELECTED_STYLE);
				} else {				
					t.getButton().removeStyleName(SELECTED_STYLE);
					t.getButton().addStyleName(UNSELECTED_STYLE);
				}
			}			
		}
	}
}
