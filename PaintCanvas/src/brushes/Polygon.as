package brushes
{
	import flash.geom.Point;
	
	import mx.containers.Canvas;

	public class Polygon implements IBrush
	{
		protected var canvas:Canvas;		
		protected var color:Number = 0;
		protected var fillColor:Number = -1;
		protected var width:Number = 1;
	
		protected var strokes:Array = new Array;
		protected var currentStroke:BrushStroke;
		
		protected var selection:Canvas = null;
		protected var nextPoint:Point;
		
		private var redo_history:Array = new Array;
				
		public function Polygon(canvas:Canvas)
		{
			this.canvas = canvas;
		
			currentStroke = new BrushStroke();	
			currentStroke.color = color;
			currentStroke.width = width;
			currentStroke.fillcolor = fillColor;		
		}

		public function processPoint(p:Point):void
		{
			nextPoint = p;
			if(currentStroke.points.length == 0) 
				currentStroke.points.push(p);
			
			selection.graphics.clear();			
			selection.graphics.lineStyle(width, color);
			selection.graphics.beginFill(0x0000FF,0.5);
			
			var start:Point = Point(currentStroke.points[0]);
			
			//Draw previous strokes
		
			selection.graphics.moveTo(start.x,start.y);
			
			for each(var pp:Point in currentStroke.points)
			{
				selection.graphics.lineTo(pp.x,pp.y);			
			}		
			
			selection.graphics.lineTo(p.x,p.y);		
			
			selection.graphics.lineStyle(width, color,0);
			selection.graphics.lineTo(start.x,start.y)	
			
			selection.graphics.endFill();
			
		}
		
		public function startStroke():void
		{
			nextPoint = null;			
		
			if(selection == null)
			{
				selection = new Canvas();
				selection.width = canvas.width;
				selection.height = canvas.height;		
				canvas.addChild(selection);		
			}
		
		}
		
		public function endStroke():void
		{						
			currentStroke.points.push(nextPoint);			
		}
		
		public function redraw():void
		{
			for each(var stroke:BrushStroke in strokes)
			{
				if(fillColor > -1) canvas.graphics.beginFill(stroke.fillcolor,1);
				
				var firstPoint:Point = Point(stroke.points[0]);
				var lastPoint:Point = Point(stroke.points[stroke.points.length-1]);
				
				canvas.graphics.lineStyle(stroke.width,stroke.color);
				canvas.graphics.moveTo(firstPoint.x, firstPoint.y); 
				
				for each(var p:Point in stroke.points)			
					canvas.graphics.lineTo(p.x,p.y);			
			
				if(firstPoint.x != lastPoint.x || firstPoint.y != lastPoint.y)
					canvas.graphics.lineTo(firstPoint.x, firstPoint.y);		
				
				if(fillColor > -1) canvas.graphics.endFill();
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
		
		public function undo():Boolean
		{
			if(strokes.length > 0){
				redo_history.push(strokes.pop());
				return true;
			} else {
				return false;
			}
		}
		
		public function redo():Boolean
		{
			if(redo_history.length > 0){
				strokes.push(redo_history.pop());
				return true;
			} else {
				return false;	
			}			
		}
		
		public function getColor():Number
		{
			return this.color;
		}
		
		public function setColor(color:Number):void
		{
			this.color = color;
		}
		
		public function getWidth():Number
		{
			return this.width;
		}
		
		public function setWidth(width:Number):void
		{
			this.width = width;
		}
		
		public function getFillColor():Number
		{
			return this.fillColor;
		}
		
		public function setFillColor(color:Number):void
		{
			this.fillColor = color;	
		}
		
		public function getType():String
		{
			return Controller.POLYGON;
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
		
		public function endTool():void
		{
			canvas.removeAllChildren();
			selection = null;
			
			//Add the stroke to the strokes
			strokes.push(currentStroke);
			nextPoint = null;
			
			//Draw the polygon
			if(fillColor > -1){
				canvas.graphics.beginFill(fillColor,1);
			} 
			
			var firstPoint:Point = Point(currentStroke.points[0]);
			var lastPoint:Point = Point(currentStroke.points[currentStroke.points.length-1]);
			
			canvas.graphics.lineStyle(width,color);
			canvas.graphics.moveTo(firstPoint.x, firstPoint.y); 
			
			for each(var p:Point in currentStroke.points)
				canvas.graphics.lineTo(p.x,p.y);												
			
			if(firstPoint.x != lastPoint.x || firstPoint.y != lastPoint.y)
				canvas.graphics.lineTo(firstPoint.x, firstPoint.y);			
			
			if(fillColor > -1) canvas.graphics.endFill();			
			
			//Create a new current stroke
			currentStroke = new BrushStroke();	
			currentStroke.color = color;
			currentStroke.width = width;
			currentStroke.fillcolor = fillColor;	
			
			
		}
		
	}
}