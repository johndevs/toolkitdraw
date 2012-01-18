package fi.jasoft.toolkitdraw.client.ui;

import java.util.Map;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.VPanel;
import com.vaadin.ui.ClientWidget;

public class VDetachablePanel extends VPanel{

	protected final String CLASS_LEFT_CAPTION = "left-caption";
	protected final String CLASS_RIGHT_CAPTION = "right-caption";
		
	private Element captionNode;
	
	private Element contentNode;
	
	private Element captionTextWrapper;
	
	private Button expandButton;
	
	private Button closeButton;
	
	private ApplicationConnection client;
	private String uidlId;
	
	private Element controls;
	
	public VDetachablePanel(){
		super();	
		
		// Get the caption node
		NodeList<Node> children = getElement().getChildNodes();
		captionNode = (Element)children.getItem(0).getFirstChild();
		contentNode = (Element)children.getItem(1);
		
		// Encapsulate the caption text
		Element captionText = (Element)captionNode.getChildNodes().getItem(0);
		captionText.setClassName(CLASS_LEFT_CAPTION);
		captionNode.removeChild(captionText);		
		captionTextWrapper = DOM.createDiv();	
		captionTextWrapper.appendChild(captionText);
		captionNode.appendChild(captionTextWrapper);
		
		//Add controls
		// FIXME Hidden controls since they do not work as expected
		/*
		controls = DOM.createDiv();
		controls.setClassName(CLASS_RIGHT_CAPTION);
		
		expandButton = new Button("E");
		controls.appendChild(expandButton.getElement());			
		DOM.sinkEvents(expandButton.getElement(), Event.ONCLICK);
		
		closeButton = new Button("X");		
		controls.appendChild(closeButton.getElement());
		DOM.sinkEvents(closeButton.getElement(), Event.ONCLICK);
		
		captionNode.appendChild(controls);
		*/
	}
	
	
	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		super.updateFromUIDL(uidl, client);
		
		// Ensure correct implementation
        if (client.updateComponent(this, uidl, false)) {
            return;
        }
        
        this.client = client;
       
        // Save the UIDL identifier for the component        
        uidlId = uidl.getId();    
        
        if(uidl.hasAttribute("hideCaption")){
        	boolean hideButtons= uidl.getBooleanAttribute("hideCaption");
        	if(hideButtons){
        		DOM.setStyleAttribute(captionNode, "display", "none");
        	}
        }       
	}
	
	@Override
	public void onBrowserEvent(Event event) { 
		super.onBrowserEvent(event);		 
		if(Element.as(event.getCurrentEventTarget()) == expandButton.getElement()){
			client.updateVariable(uidlId, "status", "expand", false);
			client.updateVariable(uidlId, "panelWidth", contentNode.getOffsetWidth(), false);
			client.updateVariable(uidlId, "panelHeight", contentNode.getOffsetHeight(), true);
		} else if(Element.as(event.getCurrentEventTarget()) == closeButton.getElement()){
			client.updateVariable(uidlId, "status", "close", true);
		}
	}
	
}
