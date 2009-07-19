package elements
{
	import mx.containers.Canvas;
	import mx.controls.Alert;
	
	public class Layer
	{
		protected var canvas:Canvas;
		protected var width:int;
		protected var height:int;
		protected var name:String;
		protected var visible:Boolean;
		protected var backgroundColor:Number;
		protected var alpha:Number;
				
		public function Layer(name:String, width:int, height:int)
		{
			if(name == null){
				this.name = "???";				
			}else{
				this.name = name;
			}
									
			this.canvas = new Canvas();
			this.canvas.cacheAsBitmap = true;
			this.canvas.id = this.name;
			
			if(width > 0) this.canvas.width = width;
			else this.canvas.percentWidth = 100;
			
			if(height > 0) this.canvas.height = height;
			else this.canvas.percentHeight = 100;			
						
			this.width = width;
			this.height = height;
			this.visible = true; 
			this.backgroundColor = 0xFFFFFF;
			this.alpha = 1;
		}
		
		public function setCanvas(canvas:Canvas):void{
			Alert.show("Changing layer canvas. This is not advicable!");
			this.canvas = canvas;
			this.canvas.id = this.name;
			this.canvas.width = this.width;
			this.canvas.height = this.height;		
		}
		public function getCanvas():Canvas{ return canvas; }
		
		public function setWidth(width:int):void
		{ 
			this.width = width;
			this.canvas.width = width;
		}
		public function getWidth():int{ return width; }
		
		public function setHeight(height:int):void
		{ 
			this.height = height; 
			this.canvas.height = height;
		}
		public function getHeight():int{ return height; }
		
		public function setName(name:String):void
		{ 
			this.name = name;
			this.canvas.id = name;
		}
		public function getName():String{ return name; }
		
		public function setVisible(visible:Boolean):void
		{ 
			this.visible = visible;
			this.canvas.setVisible(visible);
		}
		public function getVisible():Boolean{return visible; }
		
		public function setBackgroundColor(color:String):void
		{ 
			this.backgroundColor = new Number(color);
			this.canvas.setStyle("backgroundColor",color);
		}
		public function getBackgroundColor():Number{ return backgroundColor; }
		
		public function setAlpha(alpha:Number):void
		{
			this.alpha = alpha;
			this.canvas.alpha = alpha;
		}
		public function getAlpha():Number{ return alpha; }
		
		public function getXML():XML
		{
			var layerXML:XML = new XML("<layer></layer>");
			layerXML.@id = getName();
			layerXML.@visible = getVisible();
			layerXML.@height = getHeight();
			layerXML.@width = getWidth();
			layerXML.@alpha = getAlpha();
			layerXML.@color = getBackgroundColor();			
			
			return layerXML;
		}
		
		public function setXML(layerXML:XML):void
		{ 				
			if(layerXML.hasOwnProperty("@id")) setName(new String(layerXML.@id));						
			if(layerXML.hasOwnProperty("@width")) setWidth(new int(layerXML.@width));
			if(layerXML.hasOwnProperty("@height")) setHeight(new int(layerXML.@height));		
			if(layerXML.hasOwnProperty("@alpha")) setAlpha(new Number(layerXML.@alpha));
			if(layerXML.hasOwnProperty("@color")) setBackgroundColor(new String(layerXML.@color));					
		}
	}
}