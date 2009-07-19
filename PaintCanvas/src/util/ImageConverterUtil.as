package util
{
	import brushes.BrushStroke;
	import brushes.Ellipse;
	import brushes.IBrush;
	import brushes.Image;
	import brushes.Line;
	import brushes.Pen;
	import brushes.Polygon;
	import brushes.Rectangle;
	import brushes.Text;
	
	import elements.Layer;
	
	import flash.geom.Point;
	
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.graphics.ImageSnapshot;
	import mx.graphics.codec.IImageEncoder;
	import mx.graphics.codec.JPEGEncoder;
	import mx.graphics.codec.PNGEncoder;
	
	public class ImageConverterUtil
	{
		/**
		 * returns a PNG image in base64 encoding
		 */
		public static function getPNG(dpi:int):String{										
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
		public static function getJPG(dpi:int):String{
			var encoder:IImageEncoder = new JPEGEncoder();
			
			//Take the snapshot
			var snapshot:ImageSnapshot = ImageSnapshot.captureImage(Application.application.frame,dpi,encoder,true);

			//Convert image to base 64
			var b64String:String = ImageSnapshot.encodeImageAsBase64(snapshot);

			return b64String;							
		}		
		
		/**
		 * This function uses the image history to convert the image to XML
		 * 
		 */ 
		public static function convertToXML(history:Array, layers:Array):XML
		{								
			var doc:XML = new XML("<image></image>");
			
			if(history == null || layers == null)
				return doc;
							
			for each(var layer:Layer in layers)
			{								
				var layerXML:XML = layer.getXML();			
				layerXML.@orderNumber = layers.indexOf(layer);				
								
				for each(var brush:IBrush in history)
				{									
					if(brush.getCanvas() == null)
					{
						Alert.show("Brush canvas was null, skipping brush!");
						continue;
					}
						
					//Match brush with layer
					if(layer.getName() == brush.getCanvas().id)
					{
						var brushXML:XML = brush.getXML();						
						brushXML.@orderNumber = history.indexOf(brush);
					
						layerXML.appendChild(brushXML);
					}							
				}
				
				doc.appendChild(layerXML);				
				
			}		
					
			return doc;
		}
		
		/**
		 * Converts an xml to history/layers information
		 * The returned object has two parameters Object.history and Object.layers
		 * 
		 */ 
		public static function convertFromXML(xml:XML):Object{
					
			var res:Object = new Object();
			var history:Array = new Array();
			var layers:Array = new Array();
			
			res.history = history;
			res.layers = layers;		
			
			if(xml == null) return res;
													
			var layerCounter:int = 0;															
			for each(var layerXML:XML in xml.layer)
			{
				var layer:Layer = new Layer(null,-1,-1);
				layer.setXML(layerXML);
								
				if(layerXML == null || !layerXML.hasOwnProperty("brush")) continue;		
						
				var orderCounter:Number = 0;
				for each(var brushXML:XML in layerXML.brush)
				{								
					if(brushXML == null) continue;
																
					var brush:IBrush;
											
					//If brush type has not been defined, then skip it
					if(!brushXML.hasOwnProperty("@type")) continue;		
					var type:String = new String(brushXML.@type);			
					switch(type)
					{
						case Controller.PEN: 		brush = new Pen(layer.getCanvas()); break;
						case Controller.LINE:		brush = new Line(layer.getCanvas()); break;
						case Controller.ELLIPSE:	brush = new Ellipse(layer.getCanvas()); break;
						case Controller.RECTANGLE:	brush = new Rectangle(layer.getCanvas()); break;
						case Controller.TEXT:		brush = new Text(layer.getCanvas()); break;
						case Controller.IMAGE:		brush = new Image(layer.getCanvas()); break;
						case Controller.POLYGON:	brush = new Polygon(layer.getCanvas()); break;										
					}
					
					if(brush == null)
					{
						Alert.show("Failed loading brush "+brushXML.@type);
						continue;
					}
													
					brush.setXML(brushXML);						
					
					if(brushXML.hasOwnProperty("@orderNumber")) orderCounter = brushXML.@orderNumber;
					else orderCounter++;																
					
					history[orderCounter] = brush;								
				}		
				
				if(layerXML.hasOwnProperty("@orderNumber")) layerCounter = layerXML.@orderNumber;
				else layerCounter++;				
				
				layers[layerCounter] = layer;						
			}			
														
			return res;
		}

	}
}