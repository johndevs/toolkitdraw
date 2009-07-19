package
{
	import brushes.IBrush;
	import brushes.ISelectableBrush;
	import brushes.Pen;
	
	import elements.Layer;
	
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.events.TimerEvent;
	import flash.external.ExternalInterface;
	import flash.filters.DropShadowFilter;
	import flash.geom.Point;
	import flash.text.Font;
	import flash.utils.Timer;
	
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.effects.Fade;
	import mx.events.EffectEvent;
	
	import util.ArrayUtil;
	import util.ClientStoreUtil;
	import util.DrawingUtil;
	import util.GraphicsUtil;
	import util.ImageConverterUtil;
	import util.LayerUtil;
	import util.SelectionUtil;
	
	public class Controller
	{
		private var clientID:String;
		private var painter:IBrush;			

		private const layers:Array = new Array;				
		private const history:Array = new Array;
		private const redo_history:Array = new Array;
		
		private var mouse_is_down:Boolean = false;
		private var isInteractive:Boolean = false;
		private var isFlashReady:Boolean = false;
		private var hasChanged:Boolean = false;
		
		private var autosaveTimer:Timer = new Timer(1000);
			
		//Tool constants	
		public static const PEN:String = "Pen"; 
		public static const RECTANGLE:String = "Rectangle";
		public static const ELLIPSE:String = "Ellipse";
		public static const LINE:String = "Line";
		public static const POLYGON:String = "Polygon";
		public static const TEXT:String = "Text";
		public static const IMAGE:String = "Image";
		
		public static const HISTORY_OBJECT:String = "PaintCanvas-history";
		public static const LAYERS_OBJECT:String = "PaintCnavas-layers";
		
		public static const RECTANGLE_SELECT:String = "Rectangle-Select";
	
		public function Controller()
		{																				
			//Get parameters				
			clientID = Application.application.parameters.id;
	
			//Try to load previos data from the client cache
			var loaded:Object = ImageConverterUtil.convertFromXML(ClientStoreUtil.readFromClient(clientID) as XML);
						
			ArrayUtil.assignArray(this.history, loaded.history);								
			ArrayUtil.assignArray(this.layers, loaded.layers);
										
			var backgroundFound:Boolean = false;
			var backgroundLayer:Layer;
			for each(var layer:Layer in this.layers)
			{
				if(layer.getName() == "Background")
				{
					backgroundLayer = layer;
					backgroundFound = true;					
					break;
				}
			}
			
			if(!backgroundFound){				
			
				//Create the default layer
				backgroundLayer = new Layer("Background", Application.application.frame.width, Application.application.frame.height);
				layers.push(backgroundLayer);		
			}
						
			Application.application.frame.width = backgroundLayer.getWidth();
			Application.application.frame.height = backgroundLayer.getHeight();		
			Application.application.frame.addChild(backgroundLayer.getCanvas());	
				
			//Chreate the default brush
			var defaultBrush:IBrush = new Pen(backgroundLayer.getCanvas());	
			this.history.push(defaultBrush);
			this.painter = defaultBrush;		
			
			//Init the utility classes
			DrawingUtil.setController(this);
			DrawingUtil.setPainter(this.painter);
			
			GraphicsUtil.setController(this);
			GraphicsUtil.setPainter(this.painter);
			GraphicsUtil.setHistory(this.history, this.redo_history);
			
			LayerUtil.setController(this);
			LayerUtil.setPainter(this.painter);
			LayerUtil.setLayerArray(this.layers);
			LayerUtil.setCurrentLayer(backgroundLayer);
							
			//Check if we have valid dimensions and if not then set to fullsize
			if(Application.application.parameters.width == null || Application.application.parameters.height == null){
				Application.application.frame.width = Application.application.width;
				Application.application.frame.height = Application.application.height;
			} else {
				Application.application.frame.width = Application.application.parameters.width;
				Application.application.frame.height = Application.application.parameters.height;	
			}	
		
			//Set component in interactive mode(user-edit)
			setInteractive(true);	
			
			//Add dropshadow to paper
			Application.application.frame.filters = [new DropShadowFilter()];
			
			//Add keyboard listener
			//Application.application.frame.addEventListener(KeyboardEvent.KEY_DOWN, keyboard);
						
													
			//Bind to javascript
			if(ExternalInterface != null && ExternalInterface.available)
			{
				//Controller functions
				ExternalInterface.addCallback("undo", 							undo);
				ExternalInterface.addCallback("redo", 							redo);				
				ExternalInterface.addCallback("setInteractive",					setInteractive);
				ExternalInterface.addCallback("setComponentBackgroundColor", 	GraphicsUtil.setApplicationColor);
				ExternalInterface.addCallback("isReady",						isReady);
				
				//Filetypes
				ExternalInterface.addCallback("getImageXML",					getImageXML);
				ExternalInterface.addCallback("getImagePNG",					ImageConverterUtil.getPNG);
				ExternalInterface.addCallback("getImageJPG",					ImageConverterUtil.getJPG);
				
				//Brush functions
				ExternalInterface.addCallback("setBrush", 						GraphicsUtil.setBrush);
				ExternalInterface.addCallback("setBrushColor", 					GraphicsUtil.setBrushColor);
				ExternalInterface.addCallback("setBrushWidth", 					GraphicsUtil.setBrushWidth);
				ExternalInterface.addCallback("setBrushAlpha",					GraphicsUtil.setBrushAlpha);									   
				ExternalInterface.addCallback("setPaperHeight",	 				setPaperHeight);
				ExternalInterface.addCallback("setPaperWidth", 					setPaperWidth);				
				ExternalInterface.addCallback("setFillColor", 					GraphicsUtil.setFillColor);
				ExternalInterface.addCallback("setFont",						GraphicsUtil.setBrushFont);
				
				//Layer functions
				ExternalInterface.addCallback("addNewLayer", 					LayerUtil.addNewLayer);
				ExternalInterface.addCallback("removeLayer", 					LayerUtil.removeLayer);
				ExternalInterface.addCallback("selectLayer", 					LayerUtil.selectLayer);
				ExternalInterface.addCallback("moveLayerUp", 					LayerUtil.moveLayerUp);
				ExternalInterface.addCallback("moveLayerDown", 					LayerUtil.moveLayerDown);
				ExternalInterface.addCallback("getLayerNames",					LayerUtil.getLayerNames);
				ExternalInterface.addCallback("setLayerVisibility", 			LayerUtil.setLayerVisibility);	
				ExternalInterface.addCallback("setLayerBackgroundColor", 		LayerUtil.setLayerBackgroundColor);
				ExternalInterface.addCallback("setLayerBackgroundAlpha", 		LayerUtil.setLayerBackgroundAlpha);
												
				//Graphics functions
				ExternalInterface.addCallback("graphicsDrawLine",				DrawingUtil.drawLine);
				ExternalInterface.addCallback("graphicsDrawSquare",				DrawingUtil.drawSquare);
				ExternalInterface.addCallback("graphicsClear",					LayerUtil.clearCurrentLayer);
				ExternalInterface.addCallback("graphicsDrawPolygon",			DrawingUtil.drawPolygon);
				ExternalInterface.addCallback("graphicsDrawText",				DrawingUtil.drawText);
				ExternalInterface.addCallback("graphicsDrawImage",				DrawingUtil.drawImage);		
				
				//Selection functions
				ExternalInterface.addCallback("removeSelection",				SelectionUtil.hideSelection);
				ExternalInterface.addCallback("selectAll",						SelectionUtil.selectAll);				
				
				//Send available fonts to the server
				var fonts:Array = new Array();
				for each(var font:Font in Font.enumerateFonts(true)) fonts.push(font.fontName);					
				ExternalInterface.call("PaintCanvasNativeUtil.setAvailableFonts", clientID, fonts);	
			}	
			else
			{
				Alert.show("External interface not availble");
				return;	
			}		
								
			var show:Fade = new Fade(Application.application.frame);
			show.alphaFrom = 0;
			show.alphaTo = 1;			
			show.addEventListener(EffectEvent.EFFECT_END, function(e:EffectEvent):void
			{
				//Notify the client implementation that the flash has loaded and is ready to recieve commands
				isFlashReady = true;
				ExternalInterface.call("PaintCanvasNativeUtil.setCanvasReady",clientID);	
				
				//Start the autosave timer
				autosaveTimer.addEventListener(TimerEvent.TIMER, autosave);
				autosaveTimer.start();		
			});
			show.play();						
		}	
						
		/**
		 * Function for setting the height of the paper. 
		 * Height is in pixels.
		 */
		public function setPaperHeight(height:int):void
		{ 				
			//If height <0 then set it to 100%
			if(height < 0) height = Application.application.height;
			
			var ratio:Number = height/Application.application.frame.height;
			for each(var brush:IBrush in history)
			{
				brush.scale(1.0,ratio);
			}	
						
			Application.application.frame.height=height; 						
			
			LayerUtil.getCurrentLayer().setHeight(height);			
				
			GraphicsUtil.redraw();
			changeEvent();
		}		
		
		/**
		 * Function for setting the width of the paper
		 * Width is in pixels.
		 */
		public function setPaperWidth(width:int):void
		{ 			
			if(width < 0) width = Application.application.width;
			
			var ratio:Number = width/Application.application.frame.width;
			for each(var brush:IBrush in history)
			{
				brush.scale(ratio,1.0);
			}				
						
			Application.application.frame.width=width; 
			LayerUtil.getCurrentLayer().setWidth(width);	
			
			GraphicsUtil.redraw();
			changeEvent();
		}							
						
		/**
		 * This function is triggered whe the left mouse button is pressed down.
		 */ 				
		private function mouseDown(e:MouseEvent):void
		{
			if(LayerUtil.getCurrentLayer() == null) Alert.show("No layer selected!");
			
			if(e.ctrlKey){
				painter.endTool();	
				changeEvent();									
			}
												
			//Is the cursor inside paper bounds									
			else if(e.localX < LayerUtil.getCurrentLayer().getWidth() && e.localY < LayerUtil.getCurrentLayer().getHeight())
			{
				//Do we have a selection, if so check its bounds
				if(SelectionUtil.hasSelection() && !(painter is ISelectableBrush)){
					
					var x:int = new int(e.localX);
					var y:int  =new int(e.localY);
					
					if(SelectionUtil.inSelection(x, y)){						
						mouse_is_down = true;
						painter.startStroke();
					} 
				} else {
					mouse_is_down = true;
					painter.startStroke();
				}				
			}
		}
		
		/**
		 * This function is triggered when the mouse moves
		 */ 
		private function mouseMove(e:MouseEvent):void
		{
			if(LayerUtil.getCurrentLayer() == null) Alert.show("No layer selected!");			
		
			Application.application.stage.focus = LayerUtil.getCurrentLayer().getCanvas();
			
			//Only draw when the mouse is down and on the paper
			if(mouse_is_down && e.localX < LayerUtil.getCurrentLayer().getWidth() && 
					e.localY < LayerUtil.getCurrentLayer().getHeight())
			{
				//Do we have a selection, if so check its bounds
				if(SelectionUtil.hasSelection() && !(painter is ISelectableBrush)){
					
					var x:int = new int(e.localX);
					var y:int  =new int(e.localY);
					
					if(SelectionUtil.inSelection(x, y)){						
						painter.processPoint(new Point(e.localX, e.localY));
					} 			
				} else {
					painter.processPoint(new Point(e.localX, e.localY));
				}							
			}					
		}
		
		/**
		 * This function is triggered when the user releases the left mouse button
		 */ 
		private function mouseUp(e:MouseEvent):void
		{
			if(mouse_is_down) painter.endStroke();				
			mouse_is_down = false;		
			changeEvent();	
		}
			
		/**
		 * This function listens to the keyboard events
		 */ 		
		private function keyboard(k:KeyboardEvent):void
		{
			/*
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
			*/		
		}
		
		

		/**
		 * Calling this function undoes the previous brush stroke
		 */ 
		public function undo():void
		{			
			var success:Boolean = this.painter.undo();		
			
			if(!success && history.length > 1)
			{
				var p:IBrush = history.pop();
				this.painter = p;
				this.redo_history.push(p);
			}
					
			GraphicsUtil.redraw();		
			changeEvent();	
		}
		
		/**
		 * This function redoes an undone brushstroke
		 */ 
		public function redo():void
		{			
			var success:Boolean = this.painter.redo();					
			
			if(!success && redo_history.length > 0)
			{
				var p:IBrush = redo_history.pop();
				this.painter = p;
				this.history.push(p);				
			}		
			
			GraphicsUtil.redraw();
			changeEvent();
		}	
		
		
		/**
		 * This returns the image as an XML string.
		 * The information is gathered from the history and layer information
		 */ 
		public function getImageXML():String
		{
			return ImageConverterUtil.convertToXML(history, layers).toXMLString();			
		}
		
		/**
		 * Setting this to true activates the interactive mode which lets the 
		 * user paint on the canvas. If this is set to false then only drawing 
		 * operations can be done on the canvas.
		 */ 
		public function setInteractive(interactive:Boolean):void
		{					
			if(interactive && !isInteractive)
			{
				Application.application.frame.addEventListener(MouseEvent.MOUSE_DOWN, mouseDown);
				Application.application.frame.addEventListener(MouseEvent.MOUSE_MOVE, mouseMove);
				Application.application.frame.addEventListener(MouseEvent.MOUSE_UP, mouseUp);			
				Application.application.frame.addEventListener(KeyboardEvent.KEY_DOWN, keyboard);
				isInteractive = true;
				
			} else if(!interactive && isInteractive){
				Application.application.frame.removeEventListener(MouseEvent.MOUSE_DOWN, mouseDown);
				Application.application.frame.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMove);
				Application.application.frame.removeEventListener(MouseEvent.MOUSE_UP, mouseUp);
				Application.application.frame.removeEventListener(KeyboardEvent.KEY_DOWN, keyboard);
				
				isInteractive = false;				
			}
			
			changeEvent();
		}	
		
		/**
		 * Returns true if initialization is complete
		 */ 
		public function isReady():Boolean{
			return isFlashReady;
		}
		
		/**
		 * This is called if the canvas changes somehow
		 */ 
		public function changeEvent():void
		{
			hasChanged = true;			
		}
						
		/**
		 * This is called by the autosave timer and stores the current image 
		 * in the browsers cache if possible.
		 */ 
		private function autosave(e:TimerEvent):void
		{
			if(hasChanged)
			{				
				ClientStoreUtil.storeOnClient(clientID, ImageConverterUtil.convertToXML(this.history,this.layers));
			
				autosaveTimer.reset();
				autosaveTimer.start();	
				hasChanged = false;
			}					
		}		
		
		/**
		 * IMPORTANT! If the painter is changed outside the controller then this
		 * have to be called with the new painter so the controller always nows about
		 * the current painter.
		 */  
		public function setPainter(p:IBrush):void
		{
			this.painter = p;
			DrawingUtil.setPainter(this.painter);
			GraphicsUtil.setPainter(this.painter);
			LayerUtil.setPainter(this.painter);
		}
		
		
	}
}