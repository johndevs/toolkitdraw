package
{
	import brushes.Ellipse;
	import brushes.IBrush;
	import brushes.Line;
	import brushes.Pen;
	import brushes.Polygon;
	import brushes.Square;
	
	import elements.Layer;
	
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.external.ExternalInterface;
	import flash.filters.DropShadowFilter;
	import flash.geom.Point;
	
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.graphics.ImageSnapshot;
	import mx.graphics.codec.IImageEncoder;
	import mx.graphics.codec.JPEGEncoder;
	import mx.graphics.codec.PNGEncoder;
	
	import util.ArrayUtil;
	import util.ImageConverterUtil;
	
	public class Controller
	{
		private var painter:IBrush;
		
		//The current canvas
		private var currentLayer:Layer;	
		private var layers:Array = new Array;		
		
		private var history:Array = new Array;
		private var redo_history:Array = new Array;
		
		private var mouse_is_down:Boolean = false;
		private var isInteractive:Boolean = false;
			
		public static const PEN:String = "Pen"; 
		public static const SQUARE:String = "Square";
		public static const ELLIPSE:String = "Ellipse";
		public static const LINE:String = "Line";
		public static const POLYGON:String = "Polygon";
	
		public function Controller()
		{														
			//Create the default layer
			var backgroundLayer:Layer = new Layer("Background", Application.application.frame.width, Application.application.frame.height);
			currentLayer = backgroundLayer;
			layers.push(backgroundLayer);	
			Application.application.frame.addChild(backgroundLayer.getCanvas());		
			
			//Chreate the default brush
			var defaultBrush:IBrush = new Pen(currentLayer.getCanvas());	
			this.history.push(defaultBrush);
			this.painter = defaultBrush;	
		
			setInteractive(true);	
			
			Application.application.frame.filters = [new DropShadowFilter()];
			Application.application.frame.addEventListener(KeyboardEvent.KEY_DOWN, keyboard);
													
			//Bind to javascript
			if(ExternalInterface != null && ExternalInterface.available)
			{
				//Controller functions
				ExternalInterface.addCallback("undo", 							undo);
				ExternalInterface.addCallback("redo", 							redo);				
				ExternalInterface.addCallback("setInteractive",					setInteractive);
				ExternalInterface.addCallback("setComponentBackgroundColor", 	setApplicationColor);
				
				//Filetypes
				ExternalInterface.addCallback("getImageXML",					getImageXML);
				ExternalInterface.addCallback("getImagePNG",					getPNG);
				ExternalInterface.addCallback("getImageJPG",					getJPG);
				
				//Brush functions
				ExternalInterface.addCallback("setBrush", 						setBrush);
				ExternalInterface.addCallback("setBrushColor", 					setBrushColor);
				ExternalInterface.addCallback("setBrushWidth", 					setBrushWidth);													   
				ExternalInterface.addCallback("setPaperHeight",	 				setPaperHeight);
				ExternalInterface.addCallback("setPaperWidth", 					setPaperWidth);				
				ExternalInterface.addCallback("setFillColor", 					setFillColor);
				
				//Layer functions
				ExternalInterface.addCallback("addNewLayer", 					addNewLayer);
				ExternalInterface.addCallback("removeLayer", 					removeLayer);
				ExternalInterface.addCallback("selectLayer", 					selectLayer);
				ExternalInterface.addCallback("moveLayerUp", 					moveLayerUp);
				ExternalInterface.addCallback("moveLayerDown", 					moveLayerDown);
				ExternalInterface.addCallback("getLayerNames",					getLayerNames);
				ExternalInterface.addCallback("setLayerVisibility", 			setLayerVisibility);	
				ExternalInterface.addCallback("setLayerBackgroundColor", 		setLayerBackgroundColor);
				ExternalInterface.addCallback("setLayerBackgroundAlpha", 		setLayerBackgroundAlpha);
								
				
				//Graphics functions
				ExternalInterface.addCallback("graphicsDrawLine",				drawLine);
				ExternalInterface.addCallback("graphicsDrawSquare",				drawSquare);
				ExternalInterface.addCallback("graphicsClear",					clearCurrentLayer);
				ExternalInterface.addCallback("graphicsDrawPolygon",			drawPolygon);
			}	
			else
			{
				Alert.show("External interface not availble");
				return;	
			}		
		}
			
		//Brush functions			
		private function setBrushColor(color:Number):void{ this.painter.setColor(color); }		
		private function setBrushWidth(width:Number):void
		{ 
			this.painter.setWidth(width); 
		}
				
		private function setFillColor(color:Number):void{
			if( this.painter.getType() == SQUARE ){
				Square(this.painter).setFillColor(color);
			} else if(this.painter.getType() == ELLIPSE){
				Ellipse(this.painter).setFillColor(color);
			} else if(this.painter.getType() == POLYGON){
				Polygon(this.painter).setFillColor(color);
			}			
		}			
						
		//Paper options
		public function setPaperHeight(height:int):void
		{ 				
			var ratio:Number = height/Application.application.frame.height;
			for each(var brush:IBrush in history)
			{
				brush.scale(1.0,ratio);
			}	
						
			Application.application.frame.height=height; 						
			currentLayer.setHeight(height);			
				
			redraw();
		}		
		
		public function setPaperWidth(width:int):void
		{ 			
			var ratio:Number = width/Application.application.frame.width;
			for each(var brush:IBrush in history)
			{
				brush.scale(ratio,1.0);
			}				
						
			Application.application.frame.width=width; 
			currentLayer.setWidth(width);	
			
			redraw();
		}							
						
		//Mouse tracking				
		private function mouseDown(e:MouseEvent):void
		{
			if(currentLayer == null) Alert.show("No layer selected!");
			
			if(e.ctrlKey){
				painter.endTool();		
			}
												
			else if(e.localX < currentLayer.getWidth() && e.localY < currentLayer.getHeight())
			{
				mouse_is_down = true;
				painter.startStroke();
			}
		}
		
		private function mouseMove(e:MouseEvent):void
		{
			if(currentLayer == null) Alert.show("No layer selected!");			
		
			Application.application.stage.focus = currentLayer.getCanvas();
			
			//Only draw when the mouse is down and on the paper
			if(mouse_is_down && e.localX < currentLayer.getWidth() && e.localY < currentLayer.getHeight())
			{
				painter.processPoint(new Point(e.localX, e.localY));
			}					
		}
		
		private function mouseUp(e:MouseEvent):void
		{
			if(mouse_is_down) painter.endStroke();				
			mouse_is_down = false;			
		}
				
		private function keyboard(k:KeyboardEvent):void
		{
			if(k.ctrlKey && String.fromCharCode(k.charCode) != "")
			{
				switch(String.fromCharCode(k.charCode))
				{
					case "s":	Alert.show(getImageXML());	break;
					default:	trace("Warning: CTRL+"+String.fromCharCode(k.charCode)+" unassigned.");
				}
			}
			
			else
			{			
				switch(String.fromCharCode(k.charCode))
				{
					case "u":	undo(); break;			
					case "r":	redo(); break;								
					case "b":	painter.setWidth(painter.getWidth()+1); break;
					case "x":	Alert.show("XML"+getImageXML().toString()); break;
					case "f":	setPaperHeight(-1); break;
					case "p":	setBrush(POLYGON); break;
									
					default:	trace("Warning: "+String.fromCharCode(k.charCode)+" unassigned.");
				}
			}		
		}
		
		private function redraw():void
		{
			if(currentLayer == null) Alert.show("No layer selected!");
						
			currentLayer.getCanvas().graphics.clear();
			
			for each(var brush:IBrush in this.history)
			{											
				brush.redraw();
			}	
		}


		//Undo brush
		public function undo():void
		{			
			var success:Boolean = this.painter.undo();		
			
			if(!success && history.length > 1)
			{
				var p:IBrush = history.pop();
				this.painter = p;
				this.redo_history.push(p);
			}
					
			redraw();			
		}
		
		//Redo brush
		public function redo():void
		{			
			var success:Boolean = this.painter.redo();					
			
			if(!success && redo_history.length > 0)
			{
				var p:IBrush = redo_history.pop();
				this.painter = p;
				this.history.push(p);				
			}		
			
			redraw();
		}	
		
		//Set the brush
		public function setBrush(type:String):void
		{				
			switch(type)
			{
				case PEN:
				{					
					this.history.push(new Pen(currentLayer.getCanvas()));
					this.painter  = this.history[this.history.length-1];					
					break;	
				}
				
				case SQUARE:
				{					
					this.history.push(new Square(currentLayer.getCanvas()));
					this.painter  = this.history[this.history.length-1];		
					break;	
				}
				
				case ELLIPSE:
				{
					this.history.push(new Ellipse(currentLayer.getCanvas()));
					this.painter  = this.history[this.history.length-1];		
					break;
				}
				
				case LINE:
				{
					this.history.push(new Line(currentLayer.getCanvas()));
					this.painter  = this.history[this.history.length-1];		
					break;					
				}
				
				case POLYGON:
				{
					this.history.push(new Polygon(currentLayer.getCanvas()));
					this.painter = this.history[this.history.length-1];
					break;
				}
				
				default:{
					Alert.show("Brush was not found!");
				}				
			}		
			
		}	
		
		
		/**
		 * Layer functions
		 * 
		 * 
		*/
		public function addNewLayer(name:String):void
		{
			//Check if we have a layer with that name
			for each(var layer:Layer in layers){
				if(layer.getName() == name){
					Alert.show("Layer already exists!");
					return;
				}
			}
			
			var width:int  = new int(currentLayer.getWidth());
			var height:int = new int(currentLayer.getHeight());
			
			var newLayer:Layer = new Layer(name,width,height );
			layers.push(newLayer);
			Application.application.frame.addChild(newLayer.getCanvas());
			
			selectLayer(name);
		}
		
		public function removeLayer(name:String):void
		{
			//Prevent removing background layer
			if(name == layers[0].getName()){
				Alert.show("Cannot remove background layer");
				return;
			}
						
			layers = layers.filter(ArrayUtil.isNotLayerFilter(name));
			selectLayer(layers[layers.length-1].getName());			
		}
		
		public function setLayerVisibility(name:String, visible:Boolean):void
		{			
			for each(var layer:Layer in layers){
				if(layer.getName() == name){
					layer.setVisible(visible);
					break;
				}
			}
		}
				
		public function moveLayerUp(name:String):void
		{
			for each(var layer:Layer in layers){
				if(layer.getName() == name){
					
					//Get the current index of the canvas					
					var idx:int = Application.application.getChildIndex(layer.getCanvas());
					
					//Is the child last->then we cant move it anymore
					if(idx == Application.application.frame.numChildren-1)
						return;
						
					//Swap with the layer on top
					Application.application.frame.swapChildrenAt(idx,idx+1);
					
					break;
				}
			}		
		}
		
		public function moveLayerDown(name:String):void
		{
			for each(var layer:Layer in layers){
				if(layer.getName() == name){
					
					//Get the current index of the canvas					
					var idx:int = Application.application.getChildIndex(layer.getCanvas());
					
					//Is the child first->then we cant move it anymore
					if(idx == 0)
						return;
						
					//Swap with the layer behind
					Application.application.frame.swapChildrenAt(idx,idx-1);
					
					break;
				}
			}		
		}
		
		public function selectLayer(name:String):void
		{
			//Search for layer
			for each(var layer:Layer in layers){				
				if(layer.getName() == name){
					
					//Change layer
					currentLayer = layer;		
					currentLayer.getCanvas().setFocus();
					
					//Set the brush for the current layer
					setBrush(this.painter.getType());
							
					return;
				}
			}
			
			//Layer was NOT found
			Alert.show("Layer "+name+" was not found!");
		}
				
		public function getLayerNames():Array
		{	
			var names:Array = new Array;
			for(var i:int=0; i<layers.length; i++){
				var layer:Layer = layers[i];
				names.push(layer.getName());
			}
			return names;			
		}	
		
		public function getImageXML():String
		{
			return ImageConverterUtil.convertToXML(history, layers).toXMLString();			
		}
		
		public function setInteractive(interactive:Boolean):void
		{
			if(interactive && !isInteractive)
			{
				Application.application.frame.addEventListener(MouseEvent.MOUSE_DOWN, mouseDown);
				Application.application.frame.addEventListener(MouseEvent.MOUSE_MOVE, mouseMove);
				Application.application.frame.addEventListener(MouseEvent.MOUSE_UP, mouseUp);			
				Application.application.frame.addEventListener(KeyboardEvent.KEY_DOWN, keyboard);
				isInteractive = true;
				
			} else if(isInteractive){
				Application.application.frame.removeEventListener(MouseEvent.MOUSE_DOWN, mouseDown);
				Application.application.frame.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMove);
				Application.application.frame.removeEventListener(MouseEvent.MOUSE_UP, mouseUp);
				Application.application.frame.removeEventListener(KeyboardEvent.KEY_DOWN, keyboard);
				
				isInteractive = false;				
			}
		}
	
		//Draw functions
		public function drawLine(x1:int, y1:int, x2:int, y2:int):void
		{
			setBrush(LINE);
					
			painter.startStroke();
			painter.processPoint(new Point(x1,y1));
			painter.processPoint(new Point(x2,y2));
			painter.endStroke();
		}
		
		public function drawSquare(x:int, y:int, width:int, height:int):void
		{			
			if(this.painter.getType() != SQUARE){
				var color:Number = new Number(this.painter.getColor());
				var w:Number = new Number(this.painter.getWidth());
				
				setBrush(SQUARE);
				this.painter.setColor(color);
				this.painter.setWidth(w);				
			}				
						
			painter.startStroke();
			painter.processPoint(new Point(x,y));
			painter.processPoint(new Point(x+width,y+height));
			painter.endStroke();
		}
		
		public function clearCurrentLayer():void{
					
			var newHistory:Array = new Array;
						
			for each(var brush:IBrush in this.history)
			{
				if(brush.getCanvas() != currentLayer.getCanvas())
					newHistory.push(brush);					
			}
			
			this.history = newHistory;
			redraw();
		}
						
		public function drawPolygon(xa:Object, ya:Object, length:int):void
		{
			if(this.painter.getType() != POLYGON)
			{
				var color:Number = new Number(this.painter.getColor());
				var w:Number = new Number(this.painter.getWidth());	
				
				setBrush(POLYGON);
				this.painter.setColor(color);
				this.painter.setWidth(w);					
			}		
			
			for(var i:int=0; i<length; i++)
			{
				painter.startStroke();							
				painter.processPoint(new Point(xa[i],ya[i]));				
				painter.endStroke();
			}
			
			painter.endTool();		
		}
		
		public function setApplicationColor(color:String):void
		{
			Application.application.setStyle("backgroundColor", color);
			Application.application.setStyle("backgroundGradientColors",[color,color]);
		}	
		
		public function setLayerBackgroundColor(color:String):void
		{
			if(currentLayer != null){
				currentLayer.setBackgroundColor(color);
			} else {
				Alert.show("Current layer not available");
			}
		}
		
		public function setLayerBackgroundAlpha(alpha:Number):void
		{
			if(currentLayer != null){
				currentLayer.setAlpha(alpha);
			} else {
				Alert.show("Current layer not available");
			}
		}
		
		//returns a PNG image in base64 encoding
		public function getPNG():String{
			var dpi:Number = 0;
			var encoder:IImageEncoder = new PNGEncoder();
			
			//Take the snapshot
			var snapshot:ImageSnapshot = ImageSnapshot.captureImage(Application.application.frame,dpi,encoder,true);

			//Convert image to base 64
			var b64String:String = ImageSnapshot.encodeImageAsBase64(snapshot);

			return b64String;			
		}
		
		public function getJPG():String{
			var dpi:Number = 0;
			var encoder:IImageEncoder = new JPEGEncoder();
			
			//Take the snapshot
			var snapshot:ImageSnapshot = ImageSnapshot.captureImage(Application.application.frame,dpi,encoder,true);

			//Convert image to base 64
			var b64String:String = ImageSnapshot.encodeImageAsBase64(snapshot);

			return b64String;			
		}
		
		
				
	}
}