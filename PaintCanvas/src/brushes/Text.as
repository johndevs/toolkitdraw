package brushes
{
	import flash.display.BitmapData;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.containers.Canvas;
	import mx.controls.TextArea;
	import mx.core.ScrollPolicy;
	import mx.graphics.ImageSnapshot;

	public class Text implements IBrush
	{
		protected var canvas:Canvas;
		protected var selection:Canvas;
		protected var text:TextArea;
		
		protected var strokes:Array = new Array;
						
		protected var current_stroke:BrushStroke;
		protected var current_color:Number = 0x0;
		protected var current_width:Number = 1;
		protected var current_text:String = "";
		
		
		protected var startPoint:Point = null;				
		protected var endPoint:Point = null;	
		protected var editing:Boolean = false;
		
		private var data:BitmapData;
		private var x:Number;
		private var y:Number;
		private var w:Number;
		private var h:Number;
		
		public function Text(canvas:Canvas)
		{
			this.canvas = canvas;
		}

		private function keyboard(e:KeyboardEvent):void
		{				
			this.data = ImageSnapshot.captureBitmapData(this.text);				
			this.x = new Number(this.text.x);
			this.y = new Number(this.text.y);
			this.w = new Number(this.text.width);
			this.h = new Number(this.text.height);	
		}
		
		private function mouse(e:MouseEvent):void
		{
			if(e.type == MouseEvent.MOUSE_OVER)
				this.text.setFocus();
			else if(e.type == MouseEvent.MOUSE_OUT)
				this.canvas.setFocus();
		}

		public function processPoint(p:Point):void
		{
			if(!this.editing || selection.numChildren > 0) return; 
			
			if(startPoint == null) startPoint = p;
			endPoint = p;					
			
			if(startPoint.x > endPoint.x)
			{
				selection.x = endPoint.x;				
				selection.width = startPoint.x - endPoint.x;
			} 
			else
			{
				selection.x = startPoint.x;				
				selection.width = endPoint.x - startPoint.x;
			} 
			
			if(startPoint.y > endPoint.y)
			{
				selection.y = endPoint.y;
				selection.height = startPoint.y - endPoint.y;
			}
			else
			{
				selection.y = startPoint.y;
				selection.height = endPoint.y - startPoint.y;
			}		
		}
		
		public function startStroke():void
		{
			if(editing) return;
			
			editing = true;
			current_stroke = new BrushStroke();
			
			selection = new Canvas();
			selection.width = 1;
			selection.height = 1;
			selection.setStyle("backgroundColor","blue");
			selection.alpha = 0.5;
			
			canvas.addChildAt(selection, 0);	
		}
		
		public function endStroke():void
		{				
			if(!editing || text != null) return;
					
			text = new TextArea();
			text.setStyle("fontAntiAliasType","normal");
			
			text.text = current_text;
			this.x = selection.x;
			this.y = selection.y;
			text.width = selection.width;
			this.w = selection.width;
			text.height = selection.height;
			this.h = selection.height;
			text.horizontalScrollPolicy = ScrollPolicy.OFF;
			text.verticalScrollPolicy = ScrollPolicy.OFF;
			text.editable = true;
			text.cacheAsBitmap = true;			
			text.addEventListener(MouseEvent.MOUSE_OVER, mouse);			
			text.addEventListener(MouseEvent.MOUSE_OUT, mouse);
			selection.addChild(text);	
			selection.alpha = 1.0;
							
			text.setFocus();
			
			startPoint = null;
			endPoint = null;											
		}
		
		public function endTool():void
		{
			this.editing = false;					
			
			this.current_text = new String(this.text.text);			
			var d:BitmapData = ImageSnapshot.captureBitmapData(selection);
			
			this.canvas.graphics.beginBitmapFill(d);
			this.canvas.graphics.drawRect(selection.x,selection.y,selection.width,selection.height);
			this.canvas.graphics.endFill();
			
			this.canvas.removeChild(selection);
			this.selection = null;
			this.text = null;
			
		}
		
		public function redraw():void
		{
		}
		
		public function scale(x_ratio:Number, y_ratio:Number):void
		{
		}
		
		public function undo():Boolean
		{
			return false;
		}
		
		public function redo():Boolean
		{
			return false;
		}
		
		public function getColor():Number
		{
			return 0;
		}
		
		public function setColor(color:Number):void
		{
		}
		
		public function getWidth():Number
		{
			return 0;
		}
		
		public function setWidth(width:Number):void
		{
		}
		
		public function getType():String
		{
			return Controller.TEXT;
		}
		
		public function getCanvas():Canvas
		{
			return this.canvas;
		}
		
		public function getStrokes():Array
		{
			return this.strokes;
		}
		
		public function getCursor():Class
		{
			return null;
		}
		
	}
}