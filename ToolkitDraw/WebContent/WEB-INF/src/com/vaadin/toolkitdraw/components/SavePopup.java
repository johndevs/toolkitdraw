package com.vaadin.toolkitdraw.components;


import com.google.gwt.user.client.ui.Grid;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.toolkitdraw.ToolkitDrawApplication.FileType;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class SavePopup extends Window implements ClickListener{

	private Window parent;
	
	private VerticalLayout layout = new VerticalLayout();
	
	private Button ok;
	private Button cancel;
	private Select filetype;
	private TextField dpi;
	
	public SavePopup(String caption, String description, Window parent){
		super();
		
		this.parent = parent;
		setModal(true);
		setWidth("350px");
		setHeight("150px");
		
		setCaption(caption);
		
		layout.setSizeFull();
		
		GridLayout grid = new GridLayout(2,2);
				
		//HorizontalLayout fileLayout = new HorizontalLayout();
		//fileLayout.setStyleName("file-select-layout");
		Label lbl = new Label();
		lbl.setCaption("Select filetype:");
		//fileLayout.addComponent(lbl);
		grid.addComponent(lbl,0,0);
		
		filetype = new Select();
		filetype.setImmediate(true);
		filetype.setNullSelectionAllowed(false);
		for(FileType type : FileType.values()){
			filetype.addItem(type);
		}
		
		filetype.addListener(new ValueChangeListener(){
			public void valueChange(ValueChangeEvent event) {
				FileType type = (FileType)filetype.getValue();
				if(type == FileType.XML){
					dpi.setEnabled(false);
				} else {
					dpi.setEnabled(true);
				}				
			}			
		});
		
		//fileLayout.addComponent(filetype);
		
		//layout.addComponent(fileLayout);
		grid.addComponent(filetype,1,0);
		
		
		//HorizontalLayout dpiLayout = new HorizontalLayout();
		//dpiLayout.setStyleName("file-select-layout");
		Label lbl2 = new Label();
		lbl2.setCaption("Dpi:");
		lbl2.setWidth("100px");
		//dpiLayout.addComponent(lbl2);
		grid.addComponent(lbl2,0,1);
		
		dpi = new TextField();
		dpi.setWidth("100%");
		dpi.setValue("72");
		//dpiLayout.addComponent(dpi);
		
		//layout.addComponent(dpiLayout);
		
		grid.addComponent(dpi,1,1);
		layout.addComponent(grid);
	
		//Select the defualt image format
		filetype.select(FileType.PNG);
		dpi.setEnabled(true);
		
		
		HorizontalLayout buttons = new HorizontalLayout();		
		
		ok = new Button("Ok");
		ok.addListener((ClickListener)this);
		ok.setData(true);
		buttons.addComponent(ok);
	
		cancel = new Button("Cancel");
		cancel.addListener((ClickListener)this);
		cancel.setData(false);
		buttons.addComponent(cancel);		
		
		layout.addComponent(buttons);
		
		setLayout(layout);
	}
	
	
	
	@Override
	public void buttonClick(ClickEvent event) {
		ok.setEnabled(false);
		cancel.setEnabled(false);
		dpi.setEnabled(false);
		filetype.setEnabled(false);
		
		parent.showNotification("Saving file...",Notification.POSITION_BOTTOM_RIGHT);
		
		hide();
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
	
	public FileType getValue(){
		return (FileType) filetype.getValue();
	}
	
	public int getDpi(){		
		return Integer.parseInt(dpi.getValue().toString());		
	}

	
}