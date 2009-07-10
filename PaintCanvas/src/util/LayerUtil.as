package util
{
	import brushes.IBrush;
	
	import elements.Layer;
	
	import mx.controls.Alert;
	import mx.core.Application;
	
	public class LayerUtil
	{
		private static var painter:IBrush;
		private static var controller:Controller;
		private static var layers:Array;
		private static var currentLayer:Layer;
		private static var history:Array;
		private static var redo_history:Array;
		
		public static function setPainter(p:IBrush):void{
			painter = p;	
		}
		
		public static function setController(c:Controller):void{
			controller = c;
		}
		
		public static function setCurrentLayer(l:Layer):void{
			currentLayer = l;	
		}
		
		public static function getCurrentLayer():Layer{
			return currentLayer;
		}
		
		public static function setLayerArray(a:Array):void{
			layers = a;
		}
		
		public static function setHistory(h:Array, r:Array):void{
			history = h;
			redo_history = r;
		}
		
		public static function addNewLayer(name:String):void
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
			
			LayerUtil.selectLayer(name);
			controller.changeEvent();
		}
		
		public static function removeLayer(name:String):void
		{
			//Prevent removing background layer
			if(name == layers[0].getName()){
				Alert.show("Cannot remove background layer");
				return;
			}
						
			layers = layers.filter(ArrayUtil.isNotLayerFilter(name));
			LayerUtil.selectLayer(layers[layers.length-1].getName());	
			controller.changeEvent();		
		}
		
		public static function setLayerVisibility(name:String, visible:Boolean):void
		{			
			for each(var layer:Layer in layers){
				if(layer.getName() == name){
					layer.setVisible(visible);
					break;
				}
			}
			controller.changeEvent();
		}
				
		public static function moveLayerUp(name:String):void
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
			controller.changeEvent();
		}
		
		public static function moveLayerDown(name:String):void
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
			
			controller.changeEvent();	
		}
		
		public static function selectLayer(name:String):void
		{
			//Search for layer
			for each(var layer:Layer in layers){				
				if(layer.getName() == name){
					
					//Change layer
					currentLayer = layer;		
					currentLayer.getCanvas().setFocus();
					
					//Set the brush for the current layer
					GraphicsUtil.setBrush(painter.getType());
							
					controller.changeEvent();		
					return;
				}
			}
			
			//Layer was NOT found
			Alert.show("Layer "+name+" was not found!");
		}
				
		public static function getLayerNames():Array
		{	
			var names:Array = new Array;
			for(var i:int=0; i<layers.length; i++){
				var layer:Layer = layers[i];
				names.push(layer.getName());
			}
			return names;			
		}	
		
		public static function setLayerBackgroundColor(color:String):void
		{
			if(currentLayer != null){
				currentLayer.setBackgroundColor(color);
				controller.changeEvent();
			} else {
				Alert.show("Current layer not available");
			}
		}
		
		public static function setLayerBackgroundAlpha(alpha:Number):void
		{
			if(currentLayer != null){
				currentLayer.setAlpha(alpha);
				controller.changeEvent();
			} else {
				Alert.show("Current layer not available");
			}
		}
		
		public static function clearCurrentLayer():void{
					
			var newHistory:Array = new Array;
									
			for each(var brush:IBrush in history)
			{			
				if(brush.getCanvas() != currentLayer.getCanvas())
					newHistory.push(brush);					
			}
				
			
			ArrayUtil.assignArray(history, newHistory);			
			GraphicsUtil.redraw();
			
			controller.changeEvent();
		}	
		
		
	}
}