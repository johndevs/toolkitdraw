package fi.jasoft.toolkitdraw.panels;

import java.util.List;


import fi.jasoft.toolkitdraw.components.DetachablePanel;
import fi.jasoft.toolkitdraw.tools.Tool;

public class ToolOptionsPanel extends DetachablePanel {

	private List<Tool> tools;
	
	public ToolOptionsPanel(List<Tool> tools){
		setCaption("Tool Options");
		
		this.tools = tools;		
	}
	
	public void selectTool(Tool tool){
		if(tools.contains(tool)){
			setContent(tool.createToolOptions());
			setCaption(tool.getName());
		}
	}

}
