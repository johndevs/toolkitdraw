package org.vaadin.toolkitdraw.panels;

import java.util.List;

import org.vaadin.toolkitdraw.components.DetachablePanel;
import org.vaadin.toolkitdraw.tools.Tool;

public class ToolOptionsPanel extends DetachablePanel {

	private static final long serialVersionUID = 1L;
	
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
