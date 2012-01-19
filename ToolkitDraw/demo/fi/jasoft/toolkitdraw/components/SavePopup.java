package fi.jasoft.toolkitdraw.components;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
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
import com.vaadin.ui.themes.Reindeer;

import fi.jasoft.flashcanvas.events.ImageUploadEvent.ImageType;

public class SavePopup extends Window implements ClickListener{
	private static final long serialVersionUID = 1L;

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
		setResizable(false);
		
		layout.setSizeFull();
		layout.setMargin(true);
		layout.setStyleName(Reindeer.LAYOUT_BLACK);
		
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
		for(ImageType type : ImageType.values()){
			filetype.addItem(type);
		}
		
		filetype.addListener(new ValueChangeListener(){
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
				ImageType type = (ImageType)filetype.getValue();
				if(type == ImageType.XML){
					dpi.setEnabled(false);
				} else {
					dpi.setEnabled(true);
				}				
			}			
		});
	
		grid.addComponent(filetype,1,0);
		
		Label lbl2 = new Label();
		lbl2.setCaption("Dpi:");
		lbl2.setWidth("100px");
	
		grid.addComponent(lbl2,0,1);
		
		dpi = new TextField();
		dpi.setWidth("100%");
		dpi.setValue("72");
			
		grid.addComponent(dpi,1,1);
		layout.addComponent(grid);
	
		//Select the defualt image format
		filetype.select(ImageType.PNG);
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
		layout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
		
		setContent(layout);		
	}	

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
	
	public ImageType getValue(){
		return (ImageType) filetype.getValue();
	}
	
	public int getDpi(){		
		return Integer.parseInt(dpi.getValue().toString());		
	}

	
}