package util
{
	import brushes.BrushStroke;
	import brushes.IBrush;
	import brushes.Rectangle;
	
	import elements.Layer;
	
	import flash.geom.Point;
	
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
			
			var counter:int = 0;		
			for each(var layer:Layer in layers)
			{
				if(layer == null) continue;		
				
				//Create the layer node		
				var layerXML:XML = new XML("<layer></layer>");
				layerXML.@id = layer.getName();
				layerXML.@visible = layer.getVisible();
				layerXML.@height = layer.getHeight().toString();
				layerXML.@width = layer.getWidth().toString();
				layerXML.@alpha = layer.getAlpha().toString();
				layerXML.@color = layer.getBackgroundColor();
				layerXML.@orderNumber = counter;				
				
				var brushCounter:int = 0;
				for each(var brush:IBrush in history)
				{										
					//Match brush with layer
					if(layer.getName() == brush.getCanvas().id)
					{
						var brushXML:XML = new XML("<brush></brush>");	
						brushXML.@type = brush.getType();
						brushXML.@color = brush.getColor().toString();
						brushXML.@width = brush.getWidth().toString();
						brushXML.@orderNumber = brushCounter;
						
						//generate brush strokes
						for each(var stroke:BrushStroke in brush.getStrokes())
						{
							var strokeXML:XML = new XML("<stroke></stroke>");
							strokeXML.@color = stroke.color;
							strokeXML.@fill = stroke.fillcolor;
							strokeXML.@alpha = stroke.alpha;
							strokeXML.@width = stroke.width;
							
							//Generate point list
							var points:String = "";
							for each(var p:Point in stroke.points)
								points += p.x+","+p.y+";";
							
							strokeXML.points = points;
							
							brushXML.appendChild(strokeXML);
						}
						
						//Generate background strokes
						if(brush.getType() == Controller.RECTANGLE)
						{
							for each(var backgroundStroke:BrushStroke in Rectangle(brush).getBackgroundStrokes())
							{
								var bstrokeXML:XML = new XML("<backgroundstroke></backgroundstroke>");
								bstrokeXML.@color = backgroundStroke.color;
								bstrokeXML.@fill = backgroundStroke.fillcolor;
								bstrokeXML.@alpha = backgroundStroke.alpha;
								bstrokeXML.@width = backgroundStroke.width;
								
								//Generate point list
								var bpoints:String = "";
								for each(var bp:Point in backgroundStroke.points)
									bpoints += bp.x+","+bp.y+";";
								
								bstrokeXML.points = bpoints;
								
								brushXML.appendChild(bstrokeXML);
							}
						
						}
						
						
						layerXML.appendChild(brushXML);
					}				
				}
				
				doc.appendChild(layerXML);
				counter++;
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
			res.history = new Array();
			res.layers = new Array();
			
			return res;
		}

	}
}