package com.itmill.toolkitdraw.components;

import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.HorizontalLayout;
import com.itmill.toolkit.ui.Label;
import com.itmill.toolkit.ui.Select;
import com.itmill.toolkit.ui.VerticalLayout;
import com.itmill.toolkit.ui.Window;
import com.itmill.toolkit.ui.Button.ClickEvent;
import com.itmill.toolkit.ui.Button.ClickListener;
import com.itmill.toolkitdraw.ToolkitDrawApplication.FileType;

public class SavePopup extends Window implements ClickListener{

	private Window parent;
	
	private VerticalLayout layout = new VerticalLayout();
	
	private Button ok;
	private Button cancel;
	private Select filetype;
	
	public SavePopup(String caption, String description, Window parent){
		this.parent = parent;
		setModal(true);
		setWidth("350px");
		setHeight("150px");
		
		setCaption(caption);
		
		layout.setSizeFull();
		
		
		HorizontalLayout fileLayout = new HorizontalLayout();
		fileLayout.setStyleName("file-select-layout");
		Label lbl = new Label();
		lbl.setCaption("Select filetype:");
		fileLayout.addComponent(lbl);
		
		filetype = new Select();
		filetype.setNullSelectionAllowed(false);
		for(FileType type : FileType.values()){
			filetype.addItem(type);
		}
		
		//Select the defualt image format
		filetype.select(FileType.PNG);
		
		fileLayout.addComponent(filetype);
		
		layout.addComponent(fileLayout);
		
		
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
	
	
	
	@Override
	public void buttonClick(ClickEvent event) {
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
}
