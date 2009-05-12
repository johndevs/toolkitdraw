package com.itmill.toolkitdraw.tools;

import com.itmill.toolkit.data.Property.ValueChangeEvent;
import com.itmill.toolkit.data.Property.ValueChangeListener;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.Layout;
import com.itmill.toolkit.ui.TextField;
import com.itmill.toolkit.ui.VerticalLayout;
import com.itmill.toolkitdraw.components.PaintCanvas;
import com.itmill.toolkitdraw.tools.Tool.Type;

public class Square extends Tool implements ValueChangeListener{
	
	private Layout layout = new VerticalLayout();
	
	private TextField size;
	
	private TextField color;
	
	private TextField fillColor;
	
	public Square(PaintCanvas canvas){
			
			this.canvas = canvas;
			
			button = new Button("Square");
			button.setData(Type.SQUARE);
			
			size = new TextField("Size");
			size.addListener(this);
			size.setImmediate(true);
			size.setValue(1);
			layout.addComponent(size);		
			
			color = new TextField("Color");
			color.addListener(this);
			color.setImmediate(true);
			color.setValue("000000");
			layout.addComponent(color);
			
			fillColor = new TextField("Fill Color");
			fillColor.addListener(this);
			fillColor.setImmediate(true);
			fillColor.setValue("");
			layout.addComponent(fillColor);
			
		}
		
		public Layout createToolOptions(){	
			return layout;
		}	
			
		public Type getType(){
			return Type.SQUARE;
		}
	
		public void valueChange(ValueChangeEvent event) {		
			if(canvas == null) return;			
			
			if(event.getProperty() == size){							
				canvas.setToolSize(Double.parseDouble(event.getProperty().getValue().toString()));				
			}
			else if(event.getProperty() == color){
				canvas.setColor(String.valueOf(event.getProperty().getValue()));
			}		
			else if(event.getProperty() == fillColor){
				canvas.setFillColor(String.valueOf(event.getProperty().getValue()));
			}
		}
}
