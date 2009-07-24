package util
{
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
		 * NOTE: This function clear all previous drawn stuff and created layers 
		 */ 
		public static function convertFromXML(xml:XML):Object
		{
			var result:Object = new Object;
			
			var history:Array = new Array;
			result.history = history;
			
			var layers:Array = new Array;
			result.layers = layers;						
						
			if(!xml.hasOwnProperty("layer")) return result;			
			
			for each (var layerXML:XML in xml.layer)
			{
				if(!layerXML.hasOwnProperty("@id")) continue;
				if(!layerXML.hasOwnProperty("@width")) continue;
				if(!layerXML.hasOwnProperty("@height")) continue;
				if(!layerXML.hasOwnProperty("@orderNumber")) continue;
				
				var layer:Layer = new Layer(layerXML.@id, layerXML.@width, layerXML.@height);
				var layeridx:int = layerXML.@orderNumber;
				layers[layeridx] = layer;
								
				if(!layerXML.hasOwnProperty("brush")) continue;
				for each(var brushXML:XML in layerXML.brush)
				{
					if(!brushXML.hasOwnProperty("@type")) continue;	
					if(!brushXML.hasOwnProperty("@orderNumber")) continue;
						
					var brush:IBrush;
					var type:String = brushXML.@type;
					switch(type as String)
					{
						case Controller.PEN:		brush = new Pen(layer.getCanvas()); break;
						case Controller.LINE:		brush = new Line(layer.getCanvas()); break;
						case Controller.RECTANGLE:	brush = new Rectangle(layer.getCanvas()); break;
						case Controller.POLYGON:	brush = new Polygon(layer.getCanvas()); break;
						case Controller.ELLIPSE:	brush = new Ellipse(layer.getCanvas()); break;
						case Controller.IMAGE:		brush = new Image(layer.getCanvas()); break;
						case Controller.TEXT:		brush = new Text(layer.getCanvas()); break;
					}
					
					if(brush == null){
						Alert.show("brush was null ("+brushXML.@type+")");
						continue;
					}					
					
					var idx:int = brushXML.@orderNumber;
					history[idx] = brush;
					
					brush.setXML(brushXML);
				}				
			}			
			
			return result;
		}
	}
}