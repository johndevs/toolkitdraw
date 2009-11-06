package util
{
	import elements.Layer;
	
	import mx.controls.Alert;
	import mx.core.Application;
	
	public class LayerUtil
	{
		private static const layers:Array = new Array();
		
		private static var current:Layer;
		
		public static function addNewLayer(identifier:String):Layer{
			return addLayer(identifier, current.width, current.height);			
		}
		
		public static function addLayer(identifier:String, width:int, height:int, alpha:Number=1, backgroundColor:uint=0, backgroundAlpha:Number=0):Layer
		{			
			for each(var l:Layer in layers){
				if(l.getIdentifier() == identifier){
					Alert.show("Layer already exists! ("+identifier+")");
					return l;
				} 
			}
			
			var layer:Layer = new Layer(identifier);
			layer.width = width;
			layer.height = height;			
			layer.setBackgroundAlpha(backgroundAlpha);
			layer.setBackgroundColor(backgroundColor);
			layer.alpha = alpha;
			
			layers.push(layer);
			Application.application.frame.addChild(layer);
			
			if(current == null)
				current = layer;
				
			return layer;
		}
		
		public static function removeLayer(identifier:String):void{
			for each(var layer:Layer in layers){
				if(layer.getIdentifier() == identifier){					
					layers.splice(layers.indexOf(layer),1);
					Application.application.frame.removeChild(layer);							
					break;
				}	
			}			
		}
		
		public static function setCurrentLayer(layer:Layer):void
		{
			if(current != layer){
				current = layer;
			
				//We need to create a new brush for the new layer
				GraphicsUtil.setBrush(GraphicsUtil.getBrush().getType());				
			}			
		}
		
		public static function getCurrentLayer():Layer
		{
			return current;
		}
		
		public static function selectLayer(identifier:String):void
		{
			for each(var layer:Layer in layers)
			{
				if(layer.getIdentifier() == identifier)
				{
					setCurrentLayer(layer);
					break;		
				}
			}
		}
		
		public static function moveLayerUp(identifier:String):void
		{
			for each(var layer:Layer in layers)
			{
				if(layer.getIdentifier() == identifier)
				{
					var idx:int = layers.indexOf(layer);					
					if(idx < 1) return;
										
					var temp:* = layers[idx-1];
					layers[idx-1] = layer;
					layers[idx] = temp;					
					break;
				}
			}
		}
		
		public static function moveLayerDown(identifier:String):void
		{
			for each(var layer:Layer in layers)
			{
				if(layer.getIdentifier() == identifier)
				{
					var idx:int = layers.indexOf(layer);					
					if(idx == layers.length-1) return;
										
					var temp:* = layers[idx+1];
					layers[idx+1] = layer;
					layers[idx] = temp;					
					break;
				}
			}
		}
		
		public static function getLayerNames():Array
		{
			var names:Array = new Array();
			for each(var layer:Layer in layers)
			{
				names.push(layer.getIdentifier());
			}
			
			return names;
		}
		
		public static function setLayerVisibility(identifier:String, visible:Boolean):void
		{
			for each(var layer:Layer in layers)
			{
				if(layer.getIdentifier() == identifier)
				{
					layer.visible = visible;
					break;
				}
			}
		}
		
		public static function removeAllLayers():void
		{
			while(layers.length > 0)
			{
				var layer:Layer = layers.pop();
				Application.application.frame.removeChild(layer);			
			}			
		}
		
		public static function addLayerFromXML(xml:XML):void
		{
			// Create the layer and set it as current
			var layer:Layer = addLayer(xml.@id, xml.@width, xml.@height,  0, 0xFFFFFF, 0);
			
			// Sort brushes by order number
			var brushes:Array = new Array();
			for each(var brushXML:XML in xml.brush)
				brushes[brushXML.@orderNumber] = brushXML;
				
			// Add the brushes to the layer	
			for(var i:int=0; i<brushes.length; i++)	
				GraphicsUtil.createBrushFromXML(brushes[i], layer);			
				
			// Redraw layer	
			layer.invalidateDisplayList();	
		}
		
		public static function setLayerBackgroundColor(color:uint):void
		{
			current.setBackgroundColor(color);
		}
		
		public static function setLayerBackgroundAlpha(alpha:Number):void
		{
			current.setBackgroundAlpha(alpha);
		}
		
		public static function getImageXML():XML
		{
			var xml:XML = new XML("<image></image>");	
			var counter:int = 0;		
			for each(var layer:Layer in layers){
				var layerXML:XML = layer.toXML();
				layerXML.@orderNumber = counter; 
				xml.appendChild(layerXML);
				counter++;
			}			
			return xml;
		}
		
		public static function setImageXML(xml:XML):Boolean
		{		
			if(xml == null) return false;
						
			// Sort the layer by order number into an array
			var layers:Array = new Array();	
			for each (var layerXML:XML in xml.layer)
				layers[layerXML.@orderNumber] = layerXML;
			
			// Remove all layers and generate the new ones
			LayerUtil.removeAllLayers();
			for(var i:int=0; i<layers.length; i++)
				LayerUtil.addLayerFromXML(layers[i]);			
			
			return layers.length > 0;
		}
		

	}
}