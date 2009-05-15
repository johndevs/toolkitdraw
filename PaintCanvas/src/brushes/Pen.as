package brushes
{
	import flash.geom.Point;
	
	import mx.containers.Canvas;
	
	public class Pen implements IBrush
	{
		protected var canvas:Canvas;
		protected var strokes:Array = new Array;
		
		protected var current_stroke:BrushStroke;
		protected var current_color:Number = 0x0;
		protected var current_width:Number = 1;
		
		protected var redo_history:Array = new Array;
		
		[Embed("/assets/pointer.png")]
   		protected static var cursor:Class;	
		
		public function Pen(canvas:Canvas)
		{
			this.canvas = canvas;
		}
		
		public function getType():String
		{
			return Controller.PEN;
		}
				
		public function processPoint(p:Point):void
		{			
			if(current_stroke != null)
			{
				canvas.graphics.lineStyle(current_stroke.width,current_stroke.color,current_stroke.alpha);
				
				if(current_stroke.points.length == 0)
				{					
						canvas.graphics.moveTo(p.x,p.y);
				}
				else 
				{	
					canvas.graphics.lineTo(p.x,p.y);
							
				}
				
				current_stroke.points.push(p);
			}
			else
				trace("Warning: Current stroke not selected, point could not be processed");
		}
		
		public function startStroke():void
		{
			current_stroke = new BrushStroke();
			current_stroke.color = current_color;
			current_stroke.width = current_width;
			
			redo_history = new Array;
		}
		
		public function endStroke():void
		{
			strokes.push(current_stroke);
			current_stroke = null;
		}
		
		public function undo():Boolean
		{
			if(strokes.length > 0)
			{
				redo_history.push(strokes.pop());
				return true;
			}
			else
			{		
				return false;	
			}			
		}
		
		public function redo():Boolean
		{
			if(redo_history.length > 0)
			{
				strokes.push(redo_history.pop());
				return true;
			}
			else
			{				
				return false;	
			}
		}
		
		public function getColor():Number
		{
			return current_color;
		}
		
		public function setColor(color:Number):void
		{
			current_color = color;
		}
		
		public function getWidth():Number
		{
			return current_width;
		}
		
		public function setWidth(width:Number):void
		{					
			current_width = width;
		}
		
		
		
		public function redraw():void
		{
			trace("Redrawing "+strokes.length.toString()+" strokes");
			for each(var stroke:BrushStroke in strokes)
			{
				trace("- Rendering "+stroke.points.length.toString()+" points in stroke");
				
				current_stroke = new BrushStroke();
				current_stroke.color = stroke.color;
				current_stroke.width = stroke.width;
				current_stroke.alpha = stroke.alpha;
								
				for each(var p:Point in stroke.points)
				{
					processPoint(p);					
				}
				current_stroke = null;
			}				
		}
		
		public function scale(x_ratio:Number, y_ratio:Number):void
		{
			for each(var stroke:BrushStroke in strokes)
			{
				for each(var p:Point in stroke.points)
				{
					p.x = p.x*x_ratio;
					p.y = p.y*y_ratio;
				}
			}
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
			return cursor;
		}
		
		public function endTool():void
		{
			
		}
	}
}