package fi.jasoft.toolkitdraw.components;

import java.util.Map;


import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.Reindeer;

import fi.jasoft.toolkitdraw.client.ui.VDetachablePanel;

@ClientWidget(VDetachablePanel.class)
public class DetachablePanel extends Panel {

	public enum PanelState{
		ATTACHED, DETACHED, CLOSED;
	}
	
	private PanelState currentState = PanelState.ATTACHED;
	
	private boolean hideCaption = false;

	public DetachablePanel(){
		super();
		setStyleName(Reindeer.LAYOUT_BLACK);
		addStyleName("v-detachable-panel");
	}
	
	@Override
	public void changeVariables(Object source, Map variables) {
	        super.changeVariables(source, variables);
	        
	        if(variables.containsKey("status")){
	        	String status = variables.get("status").toString();
	        	
	        	if(status.equals("expand")){
	        		int width = Integer.parseInt(variables.get("panelWidth").toString());
		        	int height = Integer.parseInt(variables.get("panelHeight").toString());
	        		detachPanel(width, height);	        		
	        		
	        	}else if(status.equals("close"))
	        		closePanel();
	        }
	}
	
	@Override
	public void paintContent(PaintTarget target) throws PaintException {
 	    	
	    	// Superclass writes any common attributes in the paint target.
	        super.paintContent(target);         
	        
	        target.addAttribute("hideCaption", hideCaption);
	 }
	
	private void hideCaption(){
		hideCaption = true;
		requestRepaint();
	}
	
	private void detachPanel(int width, int height){
		if(currentState == PanelState.ATTACHED){
			if(getParent() instanceof ComponentContainer){								
				
				Window parentWindow = getWindow();
									
				// Remove panel from container
				ComponentContainer container = (ComponentContainer)getParent();
				container.removeComponent(this);
				
				// Hide the caption
				hideCaption();			
				
				// Create the new window
				Window panelWindow = new Window(getCaption());	
				VerticalLayout windowLayout = new VerticalLayout();
				panelWindow.setContent(windowLayout);
				panelWindow.setWidth(width+"px");
				//panelWindow.setHeight(height+"px");
				panelWindow.addComponent(this);
				panelWindow.setResizable(false);
				panelWindow.addListener(new Window.CloseListener() {					
					private static final long serialVersionUID = 1L;
					public void windowClose(CloseEvent e) {
						closePanel();						
					}
				});
										
				// Show window
				parentWindow.addWindow(panelWindow);	
				currentState = PanelState.DETACHED;
			}
		}
	}
	
	private void closePanel(){
		if(currentState == PanelState.ATTACHED){
			// Remove panel from container
			ComponentContainer container = (ComponentContainer)getParent();
			container.removeComponent(this);
		} 		
		
		currentState = PanelState.CLOSED;
	}
	
	public PanelState getState(){
		return currentState;
	}
}
