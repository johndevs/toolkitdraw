package
{
	import brushes.ISelection;
	import brushes.RectangleSelect;
	
	import flash.display.Loader;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.events.TimerEvent;
	import flash.external.ExternalInterface;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.text.Font;
	import flash.utils.ByteArray;
	import flash.utils.Timer;
	
	import mx.core.Application;
	import mx.graphics.ImageSnapshot;
	import mx.graphics.codec.IImageEncoder;
	import mx.graphics.codec.JPEGEncoder;
	import mx.graphics.codec.PNGEncoder;
	import mx.utils.Base64Decoder;
	
	import util.CacheUtil;
	import util.DrawingUtil;
	import util.EventUtil;
	import util.GraphicsUtil;
	import util.LayerUtil;
	
	public class Controller
	{
		private var mouseIsDown:Boolean = false;
		private var clientID:String;
		private var cacheMode:String = "cache-server";
		private var autoCacheMode:String = "cache-client";
		private var isFlashReady:Boolean = false;
		private var isInteractive:Boolean = false;
		private var externalClickListening:Boolean = false;
		private var waitingForImageCache:Boolean = false;
		private var autosaveTimer:Timer = new Timer(10000);
		
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
			
			// Get interactive mode
			isInteractive = Application.application.parameters.interactive;
																
			//Bind to javascript
			if(ExternalInterface != null && ExternalInterface.available)
			{
				//Controller functions
				ExternalInterface.addCallback("undo", 							undo);
				ExternalInterface.addCallback("redo", 							redo);				
				ExternalInterface.addCallback("setInteractive",					setInteractive);
				ExternalInterface.addCallback("setComponentBackgroundColor", 	setApplicationColor);
				ExternalInterface.addCallback("isReady",						isReady);
				ExternalInterface.addCallback("setCacheMode",					setCacheMode);
				ExternalInterface.addCallback("setImageCache",					CacheUtil.imageCacheRecieved);
				ExternalInterface.addCallback("setAutosaveTime",				setAutosaveTimerDelay);
				ExternalInterface.addCallback("setClickListening",				setExternalClickListening);
				ExternalInterface.addCallback("openImage",						openImage);
				
				
				//Filetypes
				ExternalInterface.addCallback("getImageXML",					getImageXML);
				ExternalInterface.addCallback("getImagePNG",					getPNG);
				ExternalInterface.addCallback("getImageJPG",					getJPG);
				
				//Brush functions
				ExternalInterface.addCallback("setBrush", 						GraphicsUtil.setBrush);
				ExternalInterface.addCallback("setBrushColor", 					GraphicsUtil.setBrushColor);
				ExternalInterface.addCallback("setBrushWidth", 					GraphicsUtil.setBrushSize);
				ExternalInterface.addCallback("setBrushAlpha",					GraphicsUtil.setBrushAlpha);									   
		//		ExternalInterface.addCallback("setPaperHeight",	 				setPaperHeight);
		//		ExternalInterface.addCallback("setPaperWidth", 					setPaperWidth);				
				ExternalInterface.addCallback("setFillColor", 					GraphicsUtil.setBrushFillColor);
				ExternalInterface.addCallback("setFillAlpha",					GraphicsUtil.setBrushFillAlpha);
				ExternalInterface.addCallback("setFont",						GraphicsUtil.setBrushFontName);
				ExternalInterface.addCallback("finish",							GraphicsUtil.endBrush);
				
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
				ExternalInterface.addCallback("graphicsClear",					DrawingUtil.clear);
				ExternalInterface.addCallback("graphicsDrawPolygon",			DrawingUtil.drawPolygon);
				ExternalInterface.addCallback("graphicsDrawText",				DrawingUtil.drawText);
				ExternalInterface.addCallback("graphicsDrawImage",				DrawingUtil.drawImage);	
				ExternalInterface.addCallback("graphicsDrawEllipse",			DrawingUtil.drawEllipse);	
				ExternalInterface.addCallback("graphicsFill",					DrawingUtil.fill);
				
				//Selection functions
				ExternalInterface.addCallback("removeSelection",				GraphicsUtil.removeSelection);
				ExternalInterface.addCallback("selectAll",						selectAll);			
				ExternalInterface.addCallback("cropSelected",					crop);	
				
				//Image functions
				// ExternalInterface.addCallback("getRedHistogram",				ImageUtil.getRedHistogram());
				
				//Send available fonts to the server
				var fonts:Array = new Array();
				for each(var font:Font in Font.enumerateFonts(true)) fonts.push(font.fontName);					
				ExternalInterface.call("FlashCanvasNativeUtil.setAvailableFonts", clientID, fonts);													
			}										
		}
	
		public function init():void{
			
			//Set the background color
			setApplicationColor(Application.application.parameters.bgColor);
								
			//Get background layer size
			var width:int = Application.application.parameters.width;
			var height:int = Application.application.parameters.height;
			
			if(width < 0) width = Application.application.width;
			if(height < 0) height = Application.application.height;
				
			//Check cache mode and load from cache if nessecery
			if(cacheMode == CACHE_SERVER){
				CacheUtil.requestCacheFromServer(clientID, cacheReceived);
			} 
			else if(cacheMode == CACHE_CLIENT){
				CacheUtil.requestCacheFromClient(clientID, cacheReceived);
			}
			else if(cacheMode == CACHE_AUTO && CacheUtil.clientCacheAvailable(clientID)){
				CacheUtil.requestCacheFromClient(clientID, cacheReceived);					
			}
			else if(cacheMode == CACHE_AUTO){
				CacheUtil.requestCacheFromServer(clientID, cacheReceived);
			}
			else if(cacheMode == CACHE_NONE){
				// Add the background layer
				LayerUtil.addLayer("Background", width, height, 1, 0xFFFFFF, 0);
			
				// Set the default brush
				GraphicsUtil.setBrush(GraphicsUtil.BRUSH_PEN);	
				
				// Initialization is complete
				initDone();
			}		
								
			// Add mouse listeners if interactive
			// This will be false by default	
			if(isInteractive)
			{
				Application.application.frame.addEventListener(MouseEvent.MOUSE_DOWN, mouseDown);
				Application.application.frame.addEventListener(MouseEvent.MOUSE_MOVE, mouseMove);
				Application.application.frame.addEventListener(MouseEvent.MOUSE_UP, mouseUp);	
			}					
		}
		
		private function cacheReceived(cache:XML):void
		{
			// Initial load
			if(!isFlashReady){
				
				// Check if cache is valid 
				if(!LayerUtil.setImageXML(cache))
				{		
					// The cache could not be read for some reason
					// (This always happens at first initialisation!)
													
					//Get background layer size
					var width:int = Application.application.parameters.width;
					var height:int = Application.application.parameters.height;
			
					if(width < 0) width = Application.application.width;
					if(height < 0) height = Application.application.height;
			
					// Add the background layer
					LayerUtil.addLayer("Background", width, height);
					
					// Set the default brush
					GraphicsUtil.setBrush(GraphicsUtil.BRUSH_PEN);		
				} 		
					
				// Initialization is complete
				initDone();
			}
		}
		
		private function initDone():void
		{
			// Notify server that the canvas is ready
			isFlashReady = true;
			ExternalInterface.call("FlashCanvasNativeUtil.setCanvasReady", clientID);	
			
			// Start autosave and do a save
			autosaveTimer.addEventListener(TimerEvent.TIMER, autosave);
			autosaveTimer.start();
			
			autosave(null);
		}
		
		private function autosave(e:TimerEvent):void
		{
			switch(cacheMode)
			{
				case CACHE_CLIENT:{										
					CacheUtil.sendCacheToClient(clientID, LayerUtil.getImageXML());
					break;
				}
				case CACHE_SERVER:
					CacheUtil.sendCacheToServer(clientID, LayerUtil.getImageXML());
				break;
				
				case CACHE_AUTO:
					var cache:String = LayerUtil.getImageXML();
					if(autoCacheMode == CACHE_CLIENT){
						if(!CacheUtil.sendCacheToClient(clientID, cache))
						{
							autoCacheMode = CACHE_SERVER;
							CacheUtil.sendCacheToServer(clientID, cache);	
						}	
					} else {
						CacheUtil.sendCacheToServer(clientID, cache);	
					}
					
				break;
				
				case CACHE_NONE:
					//Nop
				break;
			}				
											
			autosaveTimer.start();
		}
		
		private function mouseDown(e:MouseEvent):void{	
			// Interactive and null checks
			if(GraphicsUtil.getBrush() != null && LayerUtil.getCurrentLayer() != null && isInteractive)
			{
				// Bound checks
				if(	e.localX <= LayerUtil.getCurrentLayer().width && 
					e.localY <= LayerUtil.getCurrentLayer().height )
				{
					// Selection check
					var selection:ISelection = GraphicsUtil.getCurrentSelection();
					if(	selection == null || 
						GraphicsUtil.getBrush() is ISelection ||
						selection.inSelection(new Point(e.localX, e.localY)))
					{
						// CTRL-key check
						if(e.ctrlKey){
							GraphicsUtil.getBrush().finalize();
							EventUtil.fireBrushEnded(clientID);
						} else {
							GraphicsUtil.getBrush().mouseDown(new Point(e.localX, e.localY));
							EventUtil.fireBrushStartedEvent(clientID);
						}		
					}					
				
				}	
			}	
			
			mouseIsDown = true;
		}
		
		private function mouseMove(e:MouseEvent):void{
			// Interactive and null checks
			if(	mouseIsDown && GraphicsUtil.getBrush() != null && 
				LayerUtil.getCurrentLayer() != null && isInteractive)
			{
				// Bounds check
				if(	e.localX <= LayerUtil.getCurrentLayer().width &&
					 e.localY <= LayerUtil.getCurrentLayer().height)
				{
					//Selection check
					var selection:ISelection = GraphicsUtil.getCurrentSelection();
					if(	selection == null || 
						GraphicsUtil.getBrush() is ISelection ||
						selection.inSelection(new Point(e.localX, e.localY)))
					{
						GraphicsUtil.getBrush().mouseMove(new Point(e.localX, e.localY));	
					}				
				} 
			}				
		}
		
		private function mouseUp(e:MouseEvent):void{
			mouseIsDown = false;
			
			if(GraphicsUtil.getBrush() != null && isInteractive)
				GraphicsUtil.getBrush().mouseUp(new Point(e.localX, e.localY));		
				
			if(externalClickListening)		
				EventUtil.fireClickEvent(clientID, e.localX, e.localY);
			
			if(!GraphicsUtil.getBrush().isFinishable())
				EventUtil.fireBrushEnded(clientID);
			
		}
		
		public function setCacheMode(mode:String):void{
			cacheMode = mode;
		}
		
		/**
		 * Returns true if initialization is complete
		 */ 
		public function isReady():Boolean{
			return isFlashReady;
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
			
				if(!externalClickListening)
					Application.application.frame.addEventListener(MouseEvent.MOUSE_UP, mouseUp);						
								
				isInteractive = true;
				
			} else if(!interactive && isInteractive){
				Application.application.frame.removeEventListener(MouseEvent.MOUSE_DOWN, mouseDown);
				Application.application.frame.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMove);
				
				if(!externalClickListening)
					Application.application.frame.removeEventListener(MouseEvent.MOUSE_UP, mouseUp);			
				
				isInteractive = false;				
			}			
		}	
		
		public function setApplicationColor(color:String):void
		{
			Application.application.setStyle("backgroundColor", color);
			Application.application.setStyle("backgroundGradientColors",[color,color]);
		}	
		
		public function undo():void
		{
			LayerUtil.getCurrentLayer().undo();
		}
		
		public function redo():void
		{
			LayerUtil.getCurrentLayer().redo();
		}
		
		public function setExternalClickListening(on:Boolean):void{
			externalClickListening = on;
			
			if(externalClickListening && !Application.application.frame.hasEventListener(MouseEvent.MOUSE_UP)){				
				Application.application.frame.addEventListener(MouseEvent.MOUSE_UP, mouseUp);	
			} else if(!externalClickListening && !isInteractive && Application.application.frame.hasEventListener(MouseEvent.MOUSE_UP)){
				Application.application.frame.removeEventListener(MouseEvent.MOUSE_UP, mouseUp);	
			}
		}
		
		/**
		 * This returns the image as an XML string.
		 * The information is gathered from the history and layer information
		 */ 
		public function getImageXML():String
		{
			return LayerUtil.getImageXML().toString();		
		}
		
		/**
		 * returns a PNG image in base64 encoding
		 */
		public function getPNG(dpi:int):String{										
			var encoder:IImageEncoder = new PNGEncoder();
			
			//Take the snapshot
			var snapshot:ImageSnapshot = ImageSnapshot.captureImage(Application.application.frame,dpi,encoder,true);

			//Convert image to base 64
			var b64String:String = ImageSnapshot.encodeImageAsBase64(snapshot);

			return b64String;							
		}
				
		/**
		 * returns a JPEG image in base64 encoding
		 */ 
		public function getJPG(dpi:int):String{
			var encoder:IImageEncoder = new JPEGEncoder();
			
			//Take the snapshot
			var snapshot:ImageSnapshot = ImageSnapshot.captureImage(Application.application.frame,dpi,encoder,true);

			//Convert image to base 64
			var b64String:String = ImageSnapshot.encodeImageAsBase64(snapshot);

			return b64String;							
		}		
		
		/**
		 * Set the autosave time interval.
		 * The delay is in seconds.
		 */ 
		public function setAutosaveTimerDelay(delay:Number):void
		{					
			autosaveTimer.stop();
			autosaveTimer.delay = delay*1000;
			autosaveTimer.start();			
		}
		
		public function selectAll():void{
			GraphicsUtil.setBrush(GraphicsUtil.BRUSH_RECTANGLE_SELECT);
			
			GraphicsUtil.getBrush().mouseDown(new Point(0,0));
			GraphicsUtil.getBrush().mouseMove(new Point(Application.application.frame.width, Application.application.frame.height));
			GraphicsUtil.getBrush().mouseUp(new Point(Application.application.frame.width, Application.application.frame.height));
		}
		
		public function crop():void{
			
			if(GraphicsUtil.getCurrentSelection() != null){
				if(GraphicsUtil.getCurrentSelection() is RectangleSelect){
					var area:RectangleSelect = GraphicsUtil.getCurrentSelection() as RectangleSelect;
					var selection:Rectangle = area.getSelection();
					LayerUtil.cropImage(selection.x, selection.y, selection.width, selection.height);
				}
			}			
		}		
		
		public function getClientId():String
		{
			return clientID;
		}
		
		public function setImageWidth(width:int):void{
			
		}
		
		public function setImageHeight(height:int):void{
			
		}
				
		public function openImage(data:String):void{
			var decoder:Base64Decoder = new Base64Decoder();
            decoder.decode(data);
            
            var byteArr:ByteArray;
            byteArr = decoder.toByteArray();
         
         	var loader:Loader = new Loader();
         	loader.contentLoaderInfo.addEventListener(Event.COMPLETE,  function(e:Event):void{
         		
         		// Get width and height of image
         		var width:int = loader.content.width;
         		var height:int = loader.content.height;
         		         		
         		//Resize image to right dimensions
         		LayerUtil.resizeImage(width, height);
         		
         		//Draw the image on to the new image
         		DrawingUtil.drawImageWithLoader(0, 0, width, height, loader, data, 1);         		
         	}); 
         	
         	loader.loadBytes(byteArr);        			
		}
	}
}