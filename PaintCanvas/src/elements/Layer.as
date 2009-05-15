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
				Alert.show("Name was null");
			}else{
				this.name = name;
			}
			
			this.canvas = new Canvas();
			this.canvas.id = this.name;
			this.canvas.width = width;
			this.canvas.height = height;
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
		
		public function setName(name:String):void{ this.name = name;}
		public function getName():String{ return name; }
		
		public function setVisible(visible:Boolean):void
		{ 
			this.visible = visible;
			this.canvas.setVisible(visible);
		}
		public function getVisible():Boolean{return visible; }
		
		public function setBackgroundColor(color:Number):void{ this.backgroundColor = color;}
		public function getBackgroundColor():Number{ return backgroundColor; }
		
		public function setAlpha(alpha:Number):void{this.alpha = alpha;}
		public function getAlpha():Number{ return alpha; }
	}
}