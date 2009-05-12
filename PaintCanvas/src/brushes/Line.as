package brushes
{
	import flash.geom.Point;
	
	import mx.containers.Canvas;

	public class Line implements IBrush
	{
		protected var canvas:Canvas;
		protected var pen:Pen;
		
		protected var drawing:Boolean = false;
		protected var startPoint:Point = null;				
		protected var endPoint:Point = null;	
		
		protected var selection:Canvas;
		
		public function Line(canvas:Canvas)
		{
			this.canvas = canvas;
			pen = new Pen(canvas);
		}

		public function processPoint(p:Point):void
		{
			if(startPoint == null) startPoint = p;
			endPoint = p;
			
			selection.graphics.clear();
			
			selection.graphics.lineStyle(pen.getWidth(),pen.getColor());
			selection.graphics.moveTo(startPoint.x, startPoint.y);
			selection.graphics.lineTo(endPoint.x, endPoint.y);			
		}
		
		public function startStroke():void
		{
			selection = new Canvas();
			selection.width = canvas.width;
			selection.height = canvas.height;		
			canvas.addChild(selection);	
		}
		
		public function endStroke():void
		{
			canvas.removeChild(selection);
			
			pen.startStroke();
			pen.processPoint(startPoint);
			pen.processPoint(endPoint);
			pen.endStroke();			
			
			startPoint = null;
			endPoint = null;
		}
		
		public function redraw():void
		{
			pen.redraw();
		}
		
		public function undo():Boolean
		{
			return pen.undo();
		}
		
		public function redo():Boolean
		{
			return pen.redo();
		}
		
		public function getColor():Number
		{
			return pen.getColor();
		}
		
		public function setColor(color:Number):void
		{
			pen.setColor(color);
		}
		
		public function getWidth():Number
		{
			return pen.getWidth();
		}
		
		public function setWidth(width:Number):void
		{
			pen.setWidth(width);
		}
		
		public function getType():String
		{
			return Controller.LINE;
		}
		
		public function scale(x_ratio:Number, y_ratio:Number):void
		{
			pen.scale(x_ratio,y_ratio);
		}
		
		public function getCanvas():Canvas
		{
			return this.canvas;
		}
		
		public function getStrokes():Array
		{
			return pen.getStrokes();
		}
		
	}
}