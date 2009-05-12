package brushes
{
	import flash.geom.Point;
	
	import mx.containers.Canvas;

	public class Ellipse implements IBrush
	{
		protected var canvas:Canvas;	
		
		protected var drawing:Boolean = false;
		protected var topLeft:Point = null;				
		protected var bottomRight:Point = null;	
		
		protected var selection:Canvas;
		protected var strokes:Array = new Array;
		
		protected var current_stroke:BrushStroke;
		protected var current_color:Number = 0x0;
		protected var current_width:Number = 1;
		protected var current_filled_color:Number = -1;
				
		protected var redo_history:Array = new Array;
		
		public function Ellipse(canvas:Canvas)
		{
			this.canvas = canvas;	
		}

		public function processPoint(p:Point):void
		{
			if(topLeft == null) topLeft = p;
			bottomRight = p;		
			
			var width:int = topLeft.x-bottomRight.x;
			var height:int = topLeft.y-bottomRight.y;
						
			selection.graphics.clear();
			selection.graphics.beginFill(0x0000FF,0.5);
			
			//Sector 4
			if(width <= 0 && height <= 0)
				selection.graphics.drawEllipse(topLeft.x,topLeft.y,Math.abs(width),Math.abs(height));
			
			//Sector 3
			else if(width >= 0 && height <= 0)
				selection.graphics.drawEllipse(bottomRight.x,topLeft.y,width,Math.abs(height));
				
			//Sector 2
			else if(width <= 0 && height >= 0)
				selection.graphics.drawEllipse(topLeft.x,bottomRight.y,Math.abs(width),height);
			
			//Sector 1
			else
				selection.graphics.drawEllipse(bottomRight.x,bottomRight.y,width,height);
			
				
			selection.graphics.endFill();				
		}
		
		public function startStroke():void
		{
			selection = new Canvas();
			selection.width = 1;
			selection.height = 1;			
			selection.alpha = 0.5;
			
			canvas.addChildAt(selection,0);						
			
			current_stroke = new BrushStroke();	
			current_stroke.color = current_color;
			current_stroke.width = current_width;
			current_stroke.fillcolor = current_filled_color;
			
			redo_history = new Array;
		}
		
		public function endStroke():void
		{
			if(current_stroke != null)
			{
				current_stroke.points.push(topLeft);
				current_stroke.points.push(bottomRight);
				strokes.push(current_stroke);
								
				var width:int = topLeft.x-bottomRight.x;
				var height:int = topLeft.y-bottomRight.y;
				
				canvas.graphics.lineStyle(current_stroke.width,current_stroke.color,current_stroke.alpha);
				
				if(current_stroke.fillcolor != -1)
					canvas.graphics.beginFill(current_stroke.fillcolor);
				
				//Sector 4
				if(width <= 0 && height <= 0)					
						canvas.graphics.drawEllipse(topLeft.x,topLeft.y,Math.abs(width),Math.abs(height));							
				
				//Sector 3
				else if(width >= 0 && height <= 0)
					canvas.graphics.drawEllipse(bottomRight.x,topLeft.y,width,Math.abs(height));
					
				//Sector 2
				else if(width <= 0 && height >= 0)
					canvas.graphics.drawEllipse(topLeft.x,bottomRight.y,Math.abs(width),height);
				
				//Sector 1
				else
					canvas.graphics.drawEllipse(bottomRight.x,bottomRight.y,width,height);				
					
					
				if(current_stroke.fillcolor != -1)
					canvas.graphics.endFill();	
					
			}
			else
				trace("Warning: Current stroke not selected, point could not be processed");
															
			canvas.removeChild(selection);			
			current_stroke = null;
			
			topLeft = null;
			bottomRight = null;
		}
		
		public function redraw():void
		{			
			for each(var stroke:BrushStroke in strokes)
			{					
				current_stroke = stroke;		
				var topLeft:Point = current_stroke.points[0];
				var bottomRight:Point = current_stroke.points[1];
				
				var width:int = topLeft.x-bottomRight.x;
				var height:int = topLeft.y-bottomRight.y;
				
				canvas.graphics.lineStyle(current_stroke.width,current_stroke.color,current_stroke.alpha);
				
				if(current_stroke.fillcolor != -1)
					canvas.graphics.beginFill(current_stroke.fillcolor);
				
				//Sector 4
				if(width <= 0 && height <= 0)					
						canvas.graphics.drawEllipse(topLeft.x,topLeft.y,Math.abs(width),Math.abs(height));							
				
				//Sector 3
				else if(width >= 0 && height <= 0)
					canvas.graphics.drawEllipse(bottomRight.x,topLeft.y,width,Math.abs(height));
					
				//Sector 2
				else if(width <= 0 && height >= 0)
					canvas.graphics.drawEllipse(topLeft.x,bottomRight.y,Math.abs(width),height);
				
				//Sector 1
				else
					canvas.graphics.drawEllipse(bottomRight.x,bottomRight.y,width,height);				
					
					
				if(current_stroke.fillcolor != -1)
					canvas.graphics.endFill();					
				
			}				
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
		
		public function getType():String
		{
			return Controller.ELLIPSE;
		}
		
		public function setFillColor(color:Number):void
		{					
			this.current_filled_color = color;
		}
		
		public function getFillColor():Number
		{
			return this.current_filled_color;	
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
	}
}