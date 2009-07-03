package brushes
{
	import flash.geom.Point;
	
	import mx.containers.Canvas;
	
	import util.SelectionUtil;

	public class RectangleSelect implements ISelectableBrush
	{
		protected var canvas:Canvas;
		protected var selection:Canvas;
		protected var startPoint:Point = null;				
		protected var endPoint:Point = null;	
		
		
		public function RectangleSelect(canvas:Canvas)
		{
			this.canvas = canvas;
		}

		public function processPoint(p:Point):void
		{
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
			selection = new Canvas();
			selection.width = 1;
			selection.height = 1;
			selection.setStyle("backgroundColor","blue");
			selection.alpha = 0.5;
			
			canvas.addChildAt(selection, 0);
		}
		
		public function endStroke():void
		{
			canvas.removeChild(selection);
			
			var arr:Array = new Array();
			arr.push(startPoint);
			arr.push(new Point(endPoint.x, startPoint.y));
			arr.push(endPoint);
			arr.push(new Point(startPoint.x, endPoint.y));
			
			SelectionUtil.setSelection(arr);
			SelectionUtil.showSelection(canvas);
			
			startPoint = null;
			endPoint = null;
		}
		
		public function endTool():void
		{
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
		
		public function getAlpha():Number
		{
			return 0;
		}
		
		public function setAlpha(alpha:Number):void
		{
		}
		
		public function getType():String
		{
			return Controller.RECTANGLE_SELECT;
		}
		
		public function getCanvas():Canvas
		{
			return canvas;
		}
		
		public function getStrokes():Array
		{
			return null;
		}
		
		public function getCursor():Class
		{
			return null;
		}
		
	}
}