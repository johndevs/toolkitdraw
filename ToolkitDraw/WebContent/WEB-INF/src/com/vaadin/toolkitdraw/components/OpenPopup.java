package com.vaadin.toolkitdraw.components;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.omg.PortableInterceptor.SUCCESSFUL;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;

public class OpenPopup extends Window implements ClickListener, Receiver, Upload.SucceededListener{

	private static final long serialVersionUID = 1L;
	
	private Window parent;
	
	private VerticalLayout layout = new VerticalLayout();
	
	private Upload upload;
	
	private ByteArrayOutputStream recievedImage;
	
	private byte[] image;
	
	private Button ok;
	
	private Button cancel;

	public OpenPopup(String caption, Window parent){
		this.parent = parent;
		setModal(true);
		setCaption(caption);
		setWidth("300px");
		setHeight("100px");
		
		layout.setSizeFull();		
		
		upload = new Upload("File to open",this);
		upload.addListener(this);
		layout.addComponent(upload);
		
		
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
		
		
		setContent(layout);
		
	}
	
	public void show(){
		parent.addWindow(this);
		center();
	}
	
	public void hide(){
		parent.removeWindow(this);
		fireClose();
	}
	
	
	public void buttonClick(ClickEvent event) {
		if(event.getButton() == ok){
			hide();
		}else if(event.getButton() == cancel){
			image = null;
			hide();
		}	
	}

	@Override
	public OutputStream receiveUpload(String filename, String MIMEType) {		
		recievedImage = new ByteArrayOutputStream();	
		return recievedImage;
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		try{
			recievedImage.flush();
			recievedImage.close();				
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		image = recievedImage.toByteArray();			
	}
	
	public byte[] getImage(){
		return image;
	}

}
