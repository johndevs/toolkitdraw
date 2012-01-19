package fi.jasoft.toolkitdraw.components;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;


import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.themes.Reindeer;

public class OpenPopup extends Window 
	implements ClickListener, Receiver, Upload.SucceededListener, Upload.StartedListener, Upload.ProgressListener{

	private static final long serialVersionUID = 1L;
	
	private Window parent;
	
	private VerticalLayout layout = new VerticalLayout();
	
	private TabSheet tabs = new TabSheet();
	private VerticalLayout tab1 = new VerticalLayout();
	private VerticalLayout tab2 = new VerticalLayout();
	
	private Upload upload;
	private ProgressIndicator uploadProgress = new ProgressIndicator();
	private Label uploadResultState = new Label("");
	
	private TextField download;
	
	private ByteArrayOutputStream recievedImage;
	
	private byte[] image;
	
	private Button ok;
	
	private Button cancel;
	
	private String filename;

	public OpenPopup(String caption, Window parent){
		this.parent = parent;
		setModal(true);
		setCaption(caption);
		setWidth("350px");
		setHeight("230px");
		setResizable(false);
		
		layout.setStyleName(Reindeer.LAYOUT_BLACK);
		layout.setSizeFull();		
				
		tabs.setSizeFull();
		tabs.addComponent(createLocalFileTab());
		tabs.addComponent(createNetworkFileTab());
		layout.addComponent(tabs);
		layout.setExpandRatio(tabs, 1);
		
		HorizontalLayout buttons = new HorizontalLayout();	
		buttons.setSpacing(true);
		buttons.setHeight("30px");
		
		ok = new Button("OK");
		ok.addListener((ClickListener)this);
		ok.setData(true);
		buttons.addComponent(ok);
		buttons.setComponentAlignment(ok, Alignment.MIDDLE_LEFT);
	
		cancel = new Button("Cancel");
		cancel.addListener((ClickListener)this);
		cancel.setData(false);
		buttons.addComponent(cancel);		
		buttons.setComponentAlignment(cancel, Alignment.MIDDLE_LEFT);
		
		layout.addComponent(buttons);	
		layout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
		
		setContent(layout);
		
	}
	
	private Component createLocalFileTab(){
		tab1 = new VerticalLayout();
		tab1.setCaption("Local Image");
		tab1.setMargin(true);
		tab1.setSpacing(true);
		tab1.setWidth("100%");
		
		uploadResultState.setVisible(false);
		tab1.addComponent(uploadResultState);
		
		uploadProgress.setCaption("Uploading image, please wait...");
		uploadProgress.setVisible(false);
		tab1.addComponent(uploadProgress);
		
		upload = new Upload(null,this);
		upload.setImmediate(true);
		upload.setWidth("100%");
		upload.setButtonCaption("Select image file to download");
		upload.addListener((Upload.StartedListener)this);
		upload.addListener((Upload.ProgressListener)this);
		upload.addListener((Upload.SucceededListener)this);
		tab1.addComponent(upload);
		
		return tab1;
	}
	
	private Component createNetworkFileTab(){
		tab2 = new VerticalLayout();
		tab2.setCaption("Network Image");
		tab2.setSizeFull();
		tab2.setMargin(true);
		tab2.setSpacing(true);
		
		download = new TextField("Image URL");
		download.setValue("http://");
		download.setImmediate(true);
		download.setWidth("100%");
		tab2.addComponent(download);
		
		return tab2;
	}
	
	public void show(){
		parent.addWindow(this);
		center();
	}
	
	
	public void buttonClick(ClickEvent event) {
		if(event.getButton() == ok){
			
			if(tabs.getSelectedTab() == tab1){
				close();
				
			} else if(tabs.getSelectedTab() == tab2){
				
				try {
					URL url = new URL(download.getValue().toString());
					
					this.filename = url.getFile().substring(url.getFile().lastIndexOf('/')+1);
				
					DataInputStream byteStream = new DataInputStream(url.openStream());
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					
					try {						
					    while (true) {									    	
					    	// Throws EOF when fully read
					    	byte b = byteStream.readByte();					      
					        out.write(b);							        		
					    }    
					} catch (EOFException e) { 
						image = out.toByteArray();
						byteStream.close();
						out.close();
					
						close();
					}					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
			
		}else if(event.getButton() == cancel){
			image = null;
			close();
		}	
	}

	public OutputStream receiveUpload(String filename, String MIMEType) {		
		recievedImage = new ByteArrayOutputStream();	
		this.filename = filename;
		return recievedImage;
	}

	public void uploadSucceeded(SucceededEvent event) {
		try{
			recievedImage.flush();
			recievedImage.close();				
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		image = recievedImage.toByteArray();		
		
		uploadResultState.setCaption("Upload succeeded. Press OK to open the image.");
		uploadResultState.setVisible(true);
		
		uploadProgress.setVisible(false);
		
		ok.setEnabled(true);
		cancel.setEnabled(true);
	}
	
	public byte[] getImage(){
		return image;
	}
	
	public String getFilename(){
		return this.filename;
	}

	public void uploadStarted(StartedEvent event) {
		upload.setVisible(false);
		
	    uploadProgress.setValue(0f);
	    uploadProgress.setVisible(true);
	    uploadProgress.setPollingInterval(500);     
	    
	    ok.setEnabled(false);
	    cancel.setEnabled(false);
	    tabs.setEnabled(false);
	}

	public void updateProgress(long readBytes, long contentLength) {
		uploadProgress.setValue(new Float(readBytes / (float) contentLength));	
	}

}
