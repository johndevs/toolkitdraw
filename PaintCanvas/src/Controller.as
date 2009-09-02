package
{
	import brushes.IBrush;
	import brushes.ISelectableBrush;
	import brushes.Pen;
	
	import elements.Layer;
	
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
	
	import util.CacheUtil;
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
		private var cacheMode:String = "cache-auto";
		private var autosaveTimer:Timer = new Timer(1000);
			
		private var backgroundLayer:Layer;	
		
			
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
		
		public static const CACHE_AUTO:String = "cache-auto";
		public static const CACHE_CLIENT:String  = "cache-client";
		public static const CACHE_SERVER:String  = "cache-server";
		public static const CACHE_NONE:String = "cache-none";
	
		public function Controller()
		{																				
			//Get parameters				
			clientID = Application.application.parameters.id;
			
			//Get cache mode
			cacheMode = Application.application.parameters.cacheMode;
			
			//Notify ClientStoreUtil of controller
			CacheUtil.setController(this);
			
			//No caching
			if(cacheMode == CACHE_NONE) 
				createPage();			
						
			//Client cache mode
			else if(cacheMode == CACHE_CLIENT) 
				createPage(!loadFromCache(CACHE_CLIENT));
			
			//Server cache mode
			else if(cacheMode == CACHE_SERVER)
			{
				if(ExternalInterface.available)
					CacheUtil.requestCacheFromServer(clientID);
				else 
				{
					Alert.show("ExternalInterface not available");
					createPage();	
				}
			}					
			
			else if(cacheMode == CACHE_AUTO)
			{
				//Try client cache
				if(loadFromCache(CACHE_CLIENT)) 
					createPage(false);	
				
				//Try server cache
				else {
					if(ExternalInterface.available)
						CacheUtil.requestCacheFromServer(clientID);
					else 
					{
						Alert.show("ExternalInterface not available");
						createPage();	
					}
				}				
			}
			
			else {
				Alert.show("Unkown cache mode ("+cacheMode+")");
			}		
															
			//Bind to javascript
			if(ExternalInterface != null && ExternalInterface.available)
			{
				//Controller functions
				ExternalInterface.addCallback("undo", 							undo);
				ExternalInterface.addCallback("redo", 							redo);				
				ExternalInterface.addCallback("setInteractive",					setInteractive);
				ExternalInterface.addCallback("setComponentBackgroundColor", 	GraphicsUtil.setApplicationColor);
				ExternalInterface.addCallback("isReady",						isReady);
				ExternalInterface.addCallback("setCacheMode",					setCacheMode);
				ExternalInterface.addCallback("setImageCache",					CacheUtil.cacheFromServerRecieved);
				
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
		}	
		
		public function createPage(newPage:Boolean=true):void
		{				
			LayerUtil.setController(this);			
			LayerUtil.setLayerArray(this.layers);
								
			if(newPage)
			{		
				//Create background layer
				LayerUtil.addNewLayer("Background");
				backgroundLayer = LayerUtil.getCurrentLayer();
																											
				//Chreate the default brush
				var defaultBrush:IBrush = new Pen(backgroundLayer.getCanvas());	
				this.history.push(defaultBrush);
				this.painter = defaultBrush;					
			} 
			
			//Init the utility classes
			DrawingUtil.setController(this);
			DrawingUtil.setPainter(this.painter);
			
			GraphicsUtil.setController(this);
			GraphicsUtil.setPainter(this.painter);
			GraphicsUtil.setHistory(this.history, this.redo_history);
						
			LayerUtil.setPainter(this.painter);
				
			//Set component in interactive mode(user-edit)
			setInteractive(true);	
			
			//Add dropshadow to paper
			Application.application.frame.filters = [new DropShadowFilter()];
			
			var show:Fade = new Fade(Application.application.frame);
			show.alphaFrom = 0;
			show.alphaTo = 1;			
			show.addEventListener(EffectEvent.EFFECT_END, function(e:EffectEvent):void
			{	
				if(cacheMode != CACHE_NONE)
				{										
					//Start the autosave timer
					autosaveTimer.addEventListener(TimerEvent.TIMER, autosave);
					autosaveTimer.start();							
				}							
				
				//Notify the client implementation that the flash has loaded and is ready to recieve commands
				isFlashReady = true;
				ExternalInterface.call("PaintCanvasNativeUtil.setCanvasReady",clientID);
														
			});
			show.play();				
		}
		
		
		public function loadFromCache(cache:String, xml:XML=null):Boolean
		{							
			//Client side cache
			if(cache == CACHE_CLIENT && xml == null)			
				xml = CacheUtil.readFromClient(clientID) as XML;			
					
			if(xml == null) return false;	
			
			var data:Object = ImageConverterUtil.convertFromXML(xml);			
			
			//Data have to have the history and layers info
			if(!data.hasOwnProperty("history") || !data.hasOwnProperty("layers"))
				return false;
								
			//There has to be 1 layer and 1 brush at least
			if(data.history.length == 0 || data.layers.length == 0)
				return false;	
								
			//Add layers to scene
			for each(var layer:Layer in data.layers)
			{
				this.layers.push(layer);
				Application.application.frame.addChild(layer.getCanvas());
				
				if(layer.getName() == "Background")
					backgroundLayer = layer;					
			}
			
			//Add the strokes
			for each(var brush:IBrush in data.history)
			{
				this.history.push(brush);
				this.painter = brush;
			} 
			
			Application.application.frame.width = backgroundLayer.getWidth();
			Application.application.frame.height = backgroundLayer.getHeight();
												
			return true;		
		}
		
						
		/**
		 * Function for setting the height of the paper. 
		 * Height is in pixels.
		 */
		public function setPaperHeight(height:int):void
		{ 				
			if(Application.application == null) 
				return;
			
			if(Application.application.frame == null)
				return;
				
			if(LayerUtil.getCurrentLayer() == null)
				return;
			
			
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
			if(Application.application == null) 
				return;
			
			if(Application.application.frame == null)
				return;
				
			if(LayerUtil.getCurrentLayer() == null)
				return;
									
				
			if(width < 0) width = Application.application.width;
			
			var ratio:Number = width/Application.application.frame.width;
			for each(var brush:IBrush in history)
			{
				if(brush != null) brush.scale(ratio,1.0);
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
			//Check if we have a layer, if not select the Background layer
			if(LayerUtil.getCurrentLayer() == null){
				LayerUtil.selectLayer("Background");
			} 
			
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
			if(LayerUtil.getCurrentLayer() == null){
				LayerUtil.selectLayer("Background");
			} 
		
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
				isInteractive = true;
				
			} else if(!interactive && isInteractive){
				Application.application.frame.removeEventListener(MouseEvent.MOUSE_DOWN, mouseDown);
				Application.application.frame.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMove);
				Application.application.frame.removeEventListener(MouseEvent.MOUSE_UP, mouseUp);			
				
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
				switch(cacheMode)
				{
					case CACHE_CLIENT: 
						CacheUtil.storeOnClient(clientID, ImageConverterUtil.convertToXML(this.history,this.layers));
					break;
					
					case CACHE_SERVER:
						CacheUtil.storeOnServer(clientID, ImageConverterUtil.convertToXML(this.history,this.layers));
					break;
					
					case CACHE_AUTO:
						var client:Boolean = CacheUtil.storeOnClient(clientID, ImageConverterUtil.convertToXML(this.history,this.layers));
						if(!client) CacheUtil.storeOnServer(clientID, ImageConverterUtil.convertToXML(this.history,this.layers));
					break;
					
					case CACHE_NONE:
						//Nop
					break;
				}				
			
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
		
		/**
		 * This controlls what cache is used, default is AUTO where first client cache is tried
		 * then server cache and fallback to none
		 */
		public function setCacheMode(mode:String):void
		{
			this.cacheMode = mode;
		}
		
	}
}