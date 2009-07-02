package
{
	import brushes.IBrush;
	import brushes.Pen;
	
	import elements.Layer;
	
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.events.TimerEvent;
	import flash.external.ExternalInterface;
	import flash.filters.DropShadowFilter;
	import flash.geom.Point;
	import flash.net.SharedObject;
	import flash.utils.Timer;
	
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.effects.Fade;
	import mx.events.EffectEvent;
	
	import util.DrawingUtil;
	import util.GraphicsUtil;
	import util.ImageConverterUtil;
	import util.LayerUtil;
	
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
		
		public static const HISTORY_OBJECT:String = "PaintCanvas-history";
		public static const LAYERS_OBJECT:String = "PaintCnavas-layers";
		
		public static const RECTANGLE_SELECT:String = "Rectangle-Select";
	
		public function Controller()
		{																				
			//Get parameters				
			clientID = Application.application.parameters.id;
	
			//Try to load previos data from the client cache
			//loadFromClient(clientID);
			
			//No data was loaded, create the default stuff
			if(this.history.length == 0){
				//Create the default layer
				var backgroundLayer:Layer = new Layer("Background", Application.application.frame.width, Application.application.frame.height);
				layers.push(backgroundLayer);	
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
			}			
		
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
				ExternalInterface.addCallback("getImagePNG",					GraphicsUtil.getPNG);
				ExternalInterface.addCallback("getImageJPG",					GraphicsUtil.getJPG);
				
				//Brush functions
				ExternalInterface.addCallback("setBrush", 						GraphicsUtil.setBrush);
				ExternalInterface.addCallback("setBrushColor", 					GraphicsUtil.setBrushColor);
				ExternalInterface.addCallback("setBrushWidth", 					GraphicsUtil.setBrushWidth);
				ExternalInterface.addCallback("setBrushAlpha",					GraphicsUtil.setBrushAlpha);									   
				ExternalInterface.addCallback("setPaperHeight",	 				setPaperHeight);
				ExternalInterface.addCallback("setPaperWidth", 					setPaperWidth);				
				ExternalInterface.addCallback("setFillColor", 					GraphicsUtil.setFillColor);
				
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
				
				
			}	
			else
			{
				Alert.show("External interface not availble");
				return;	
			}		
			
			//Save the current layers to disk
			storeOnClient(clientID);
					
			var show:Fade = new Fade(Application.application.frame);
			show.alphaFrom = 0;
			show.alphaTo = 1;
			show.play();	
			show.addEventListener(EffectEvent.EFFECT_END, function(e:EffectEvent):void
			{
				//Notify the client implementation that the flash has loaded and is ready to recieve commands
				isFlashReady = true;
				ExternalInterface.call("PaintCanvasNativeUtil.setCanvasReady",clientID);	
				
				//Start the autosave timer
				autosaveTimer.addEventListener(TimerEvent.TIMER, autosave);
				autosaveTimer.start();		
			});				
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
				
				//Ensure that all temporary children are removed
				painter.getCanvas().removeAllChildren();
			}
												
			else if(e.localX < LayerUtil.getCurrentLayer().getWidth() && e.localY < LayerUtil.getCurrentLayer().getHeight())
			{
				mouse_is_down = true;
				painter.startStroke();
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
				painter.processPoint(new Point(e.localX, e.localY));
			}					
		}
		
		/**
		 * This function is triggered when the user releases the left mouse button
		 */ 
		private function mouseUp(e:MouseEvent):void
		{
			if(mouse_is_down) painter.endStroke();				
			mouse_is_down = false;			
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
			
			//Call the client side implementation
			//ExternalInterface.call("PaintCanvasNativeUtil.error","SWF says hi");
		}
		
		/**
		 * This stores the image information in the browsers cache so it can be reloaded
		 * if the Flash plugin gets refreshed(F5)
		 */ 
		private function storeOnClient(id:String):void
		{
			var history:SharedObject = SharedObject.getLocal(HISTORY_OBJECT+"-"+id);
			var layers:SharedObject = SharedObject.getLocal(LAYERS_OBJECT+"-"+id);
			
			//Store history data
			history.data.history = this.history;
			history.data.current = this.painter;
			
			//Store layer data
			layers.data.layers = this.layers;
			layers.data.current = LayerUtil.getCurrentLayer();
			
			//Add timestamp
			var ts:Date = new Date();
			history.data.timestamp = ts.toTimeString();
			layers.data.timestamp = ts.toTimeString();
			
			//Save to disk
			history.flush();
			layers.flush();
		}
		
		/**
		 * This loads the image information from the browsers cache if the Flash
		 * plugin is refreshed. The loading overwrites all data and only if all needed
		 * data is found in the cache the image is loaded.
		 */ 
		private function loadFromClient(id:String):void
		{
			var history:SharedObject = SharedObject.getLocal(HISTORY_OBJECT+"-"+id);
			var layers:SharedObject = SharedObject.getLocal(LAYERS_OBJECT+"-"+id);
			
			//Check if we have stored data
			if(history.data.timestamp == null || layers.data.timestamp == null){
				
				Alert.show("Timestamps null!");
				return;
			}
				
				
			//Warn if timestamp is not the same on all data objects
			if(history.data.timestamp != layers.data.timestamp)			
				Alert.show("Warning: Loading data with different timestamp!");	
			
			//Load the data			
			//TODO; this.history = history.data.history;
			
			this.painter = history.data.current;
			GraphicsUtil.setPainter(this.painter);
			DrawingUtil.setPainter(this.painter);
			LayerUtil.setPainter(this.painter);
			
			Alert.show("Loaded "+history.size+" history items!");
			
			//TODO this.layers = layers.data.layers;
			LayerUtil.setCurrentLayer(layers.data.current);
			
			
			Alert.show("Loaded " +layers.size+" layers!");
			
			//Add the background layer
			Application.application.frame.removeAllChildren();
			Application.application.frame.addChild(Layer(this.layers[0]).getCanvas());
			Application.application.frame.width(Layer(this.layers[0]).getWidth());
			Application.application.frame.height(Layer(this.layers[0]).getHeight());
			
			//Redraw the layers
			for each(var layer:Layer in this.layers)
			{
				LayerUtil.selectLayer(layer.getName());
				GraphicsUtil.redraw();
			}			
		}	
		
		/**
		 * This is called by the autosave timer and stores the current image 
		 * in the browsers cache if possible.
		 */ 
		private function autosave(e:TimerEvent):void
		{
			if(hasChanged)
			{
				storeOnClient(clientID);
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