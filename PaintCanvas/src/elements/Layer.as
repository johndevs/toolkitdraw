package elements
{
	import mx.containers.Canvas;
	import mx.core.Application;
	
	/**
	 * This class represents an Image layer
	 *
	 *  @author John Ahlroos
	 * 
	 */ 
	public class Layer
	{
		protected var canvas:Canvas;
		protected var width:int;
		protected var height:int;
		protected var name:String;
		protected var visible:Boolean;
		protected var backgroundColor:Number;
		protected var alpha:Number;
				
		/**
		 * The Constructor 
		 */
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
		
		/**
		 * Set the canvas the layer represents
		 * Please use with caution!
		 */ 
		public function setCanvas(canvas:Canvas):void{		
			this.canvas = canvas;
			this.canvas.id = this.name;
			this.canvas.width = this.width;
			this.canvas.height = this.height;		
		}
		
		/**
		 * Get the canvas component the layer represents
		 */ 
		public function getCanvas():Canvas{ 
			return this.canvas; 
		}
		
		/**
		 * Set the width of the layer
		 */ 
		public function setWidth(width:int):void
		{ 
			this.width = width;
			this.canvas.width = width;
		}
		
		/**
		 * Retursn the width of the layer
		 */ 
		public function getWidth():int{ return width; }
		
		/**
		 * Set the height of the layer
		 */ 
		public function setHeight(height:int):void
		{ 
			this.height = height; 
			this.canvas.height = height;
		}
		
		/**
		 * Returns the height of the layer 
		 */ 
		public function getHeight():int{ return height; }
		
		/**
		 * Set the name of the layer. The name should be unique and it
		 * is used to identify the layer.
		 */ 
		public function setName(name:String):void
		{ 
			this.name = name;
			this.canvas.id = name;
		}
		
		/**
		 * Returns the name of the layer
		 */ 
		public function getName():String{ return name; }
		
		/**
		 * Define if the layer should be visible
		 */
		public function setVisible(visible:Boolean):void
		{ 
			this.visible = visible;
			this.canvas.setVisible(visible);
		}
		
		/**
		 * Is the layer visible
		 */ 
		public function getVisible():Boolean{
			return visible; 
		}
		
		/**
		 * Set the background color of the layer. 
		 * An empty string represents no background color
		 */ 
		public function setBackgroundColor(color:String):void
		{ 
			this.backgroundColor = new Number(color);
			Application.application.frame.setStyle("backgroundColor",color);
		}
		
		/**
		 * Get the background color
		 */ 
		public function getBackgroundColor():Number{ 
			return backgroundColor; 
		}
		
		/** 
		 * Set the alpha value of the layer. The layer transparancy can be controlled
		 * with this value.
		 */
		public function setAlpha(alpha:Number):void
		{
			this.alpha = alpha;
			this.canvas.alpha = alpha;
		}
		
		/**
		 * Returns the alpha value of the layer
		 */ 
		public function getAlpha():Number{ return alpha; }
		
		/**
		 * Returns an XML representation of the layer
		 */ 
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
		
		/**
		 * Creates the layer based on XML. XML must use the same format that
		 * getXML() creates.
		 */ 
		public function setXML(layerXML:XML):void
		{ 				
			if(layerXML.hasOwnProperty("@id")) setName(new String(layerXML.@id));						
			if(layerXML.hasOwnProperty("@width")) setWidth(new int(layerXML.@width));
			if(layerXML.hasOwnProperty("@height")) setHeight(new int(layerXML.@height));		
			if(layerXML.hasOwnProperty("@alpha")) setAlpha(new Number(layerXML.@alpha));
			if(layerXML.hasOwnProperty("@color")) setBackgroundColor(new String(layerXML.@color));					
		}
		
		/**
		 * Returns a string representation of the layer
		 */ 
		public function toString():String
		{
			return "Layer: "+name+" "+width+"x"+height+" visible:"+visible+" alpha:"+alpha+" bgColor:"+backgroundColor;
		}
	}
}