package
{
	import flash.events.MouseEvent;
	import flash.events.TimerEvent;
	import flash.external.ExternalInterface;
	import flash.geom.Point;
	import flash.text.Font;
	import flash.utils.Timer;
	
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.graphics.ImageSnapshot;
	import mx.graphics.codec.IImageEncoder;
	import mx.graphics.codec.JPEGEncoder;
	import mx.graphics.codec.PNGEncoder;
	
	import util.CacheUtil;
	import util.DrawingUtil;
	import util.GraphicsUtil;
	import util.LayerUtil;
	
	public class Controller
	{
		private var mouseIsDown:Boolean = false;
		private var clientID:String;
		private var cacheMode:String = "cache-server";
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
		//		ExternalInterface.addCallback("graphicsDrawImage",				DrawingUtil.drawImage);	
				ExternalInterface.addCallback("graphicsDrawEllipse",			DrawingUtil.drawEllipse);	
				
				//Selection functions
		//		ExternalInterface.addCallback("removeSelection",				SelectionUtil.hideSelection);
		//		ExternalInterface.addCallback("selectAll",						SelectionUtil.selectAll);			
		//		ExternalInterface.addCallback("cropSelected",					SelectionUtil.cropSelection);	
				
				//Send available fonts to the server
				var fonts:Array = new Array();
				for each(var font:Font in Font.enumerateFonts(true)) fonts.push(font.fontName);					
				ExternalInterface.call("PaintCanvasNativeUtil.setAvailableFonts", clientID, fonts);													
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
			ExternalInterface.call("PaintCanvasNativeUtil.setCanvasReady", clientID);	
			
			// Start autosave and do a save
			autosaveTimer.addEventListener(TimerEvent.TIMER, autosave);
			autosave(null);
		}
		
		private function autosave(e:TimerEvent):void
		{
			switch(cacheMode)
			{
				case CACHE_CLIENT: 
					// TODO
				break;
				
				case CACHE_SERVER:
					CacheUtil.sendCacheToServer(clientID, LayerUtil.getImageXML());
				break;
				
				case CACHE_AUTO:
					// TOOD
				break;
				
				case CACHE_NONE:
					//Nop
				break;
			}				
		
			autosaveTimer.reset();
			autosaveTimer.start();	
		}
		
		private function mouseDown(e:MouseEvent):void{	
			if(e.localX <= LayerUtil.getCurrentLayer().width && e.localY <= LayerUtil.getCurrentLayer().height)
			{
				if(e.ctrlKey){
					GraphicsUtil.getBrush().finalize();
				} else {
					GraphicsUtil.getBrush().mouseDown(new Point(e.localX, e.localY));
				}			
					
				mouseIsDown = true;
			}		
		}
		
		private function mouseMove(e:MouseEvent):void{
			if(mouseIsDown)
			{
				if(e.localX <= LayerUtil.getCurrentLayer().width && e.localY <= LayerUtil.getCurrentLayer().height)
				{
					GraphicsUtil.getBrush().mouseMove(new Point(e.localX, e.localY));
				} 
			}				
		}
		
		private function mouseUp(e:MouseEvent):void{
			mouseIsDown = false;
			GraphicsUtil.getBrush().mouseUp(new Point(e.localX, e.localY));		
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
		
		public function setAutosaveTimerDelay(delay:Number):void
		{
			autosaveTimer.stop();
			autosaveTimer.delay = delay;
			autosaveTimer.start();
		}
	}
}