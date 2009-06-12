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

		public function processPoint(p:Point):void
		{
			if(!editing) return; 
			
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
			if(!editing) return;
					
			text = new TextArea();
			text.text = current_text;
			text.x = selection.x;
			text.y = selection.y;
			text.width = selection.width;
			text.height = selection.height;
			text.horizontalScrollPolicy = ScrollPolicy.OFF;
			text.verticalScrollPolicy = ScrollPolicy.OFF;
			text.editable = true;
			text.cacheAsBitmap = true;
			text.addEventListener(KeyboardEvent.KEY_UP, function(e:KeyboardEvent):void{
				var ta:TextArea = TextArea(e.target);				
				if(ta == null) return;
				
				current_text = ta.text;		
					
				data = ImageSnapshot.captureBitmapData(ta);
				
				
				x = ta.x;
				y = ta.y;
				w = ta.width;
				h = ta.height;				
			});
			text.addEventListener(MouseEvent.MOUSE_OVER, function(e:MouseEvent):void{
				text.setFocus();
			});
			text.addEventListener(MouseEvent.MOUSE_OUT, function(e:MouseEvent):void{
				Canvas(canvas.parent).setFocus();
			});
										
			canvas.removeChild(selection);
			canvas.addChild(text);	
							
			text.setFocus();
			
			startPoint = null;
			endPoint = null;											
		}
		
		public function endTool():void
		{
			editing = false;					
														
			canvas.graphics.beginBitmapFill(data);
			canvas.graphics.drawRect(x, y, w, h);
			canvas.graphics.endFill();
			
			canvas.removeChild(text);
			Canvas(canvas.parent).setFocus();
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