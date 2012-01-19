package fi.jasoft.toolkitdraw.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;

public class ConfirmPopup extends Window implements ClickListener{	
	private static final long serialVersionUID = 1L;

	private Window parent;
	
	private VerticalLayout layout = new VerticalLayout();
	
	private Button ok;
	private Button cancel;
	
	private Label description;
	
	public ConfirmPopup(String caption, String description, Window parent){
		this.parent = parent;
		setModal(true);
		setCaption(caption);
		setWidth("300px");
		setHeight("100px");
		setResizable(false);
		getContent().setStyleName(Reindeer.LAYOUT_BLACK);
		
		layout.setSizeFull();
		layout.setMargin(true);
		
		this.description = new Label(description);
		layout.addComponent(this.description);
		
		HorizontalLayout buttons = new HorizontalLayout();		
			
		ok = new Button("Ok");
		ok.addListener(this);
		ok.setData(true);
		buttons.addComponent(ok);
	
		cancel = new Button("Cancel");
		cancel.addListener(this);
		cancel.setData(false);
		buttons.addComponent(cancel);
		
		layout.addComponent(buttons);
		layout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
		setContent(layout);
	}
	
	public void show(){
		parent.addWindow(this);
		center();
	}
	
	public void hide(){
		parent.removeWindow(this);
	}
	
	public void addListener(ClickListener listener){
		ok.addListener(listener);
		cancel.addListener(listener);
	}
	
	public void removeListener(ClickListener listener){
		ok.removeListener(listener);
		cancel.removeListener(listener);
	}

	public void buttonClick(ClickEvent event) {
		hide();		
	}

}
