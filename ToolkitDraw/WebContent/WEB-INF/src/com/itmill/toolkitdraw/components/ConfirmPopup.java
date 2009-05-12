package com.itmill.toolkitdraw.components;

import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.HorizontalLayout;
import com.itmill.toolkit.ui.Label;
import com.itmill.toolkit.ui.VerticalLayout;
import com.itmill.toolkit.ui.Window;
import com.itmill.toolkit.ui.Button.ClickEvent;
import com.itmill.toolkit.ui.Button.ClickListener;

public class ConfirmPopup extends Window implements ClickListener{	
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
		
		layout.setSizeFull();
		
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
		setLayout(layout);
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

	@Override
	public void buttonClick(ClickEvent event) {
		hide();		
	}
}
