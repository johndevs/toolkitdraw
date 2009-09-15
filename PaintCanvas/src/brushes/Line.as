package brushes
{
	import flash.geom.Point;
	
	import mx.containers.Canvas;

	/**
	 * This class represents the line tool
	 */ 
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
			
			selection.graphics.lineStyle(pen.getWidth(),pen.getColor(),pen.getAlpha());
			selection.graphics.moveTo(startPoint.x, startPoint.y);
			selection.graphics.lineTo(endPoint.x, endPoint.y);			
		}
		
		public function startStroke(p:Point):void
		{
			selection = new Canvas();
			selection.width = canvas.width;
			selection.height = canvas.height;		
			canvas.addChild(selection);	
		}
		
		public function endStroke():void
		{
			canvas.removeChild(selection);
			
			pen.startStroke(startPoint);
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
		
		public function getCursor():Class
		{
			return null;
		}
		
		public function endTool():void
		{
			//Nop
		}
		
		public function getAlpha():Number
		{
			return pen.getAlpha();
		}
		
		public function setAlpha(alpha:Number):void
		{
			pen.setAlpha(alpha);
		}
		
		public function getXML():XML
		{
			var brushXML:XML = new XML("<brush></brush>");	
			brushXML.@type = getType();
			brushXML.@color = getColor();
			brushXML.@width = getWidth();	
			
			//generate brush strokes						
			for each(var stroke:BrushStroke in getStrokes())
			{
				var strokeXML:XML = new XML("<stroke></stroke>");
				strokeXML.@color = stroke.color;
				strokeXML.@fill = stroke.fillcolor;
				strokeXML.@alpha = stroke.alpha;
				strokeXML.@width = stroke.width;
				strokeXML.@orderNumber = getStrokes().indexOf(stroke);
				
				//Generate point list
				var points:String = "";
				for each(var p:Point in stroke.points)
					points += p.x+","+p.y+";";
				
				strokeXML.points = points;							
				brushXML.appendChild(strokeXML);							
			}					
			
			return brushXML;
		}
		
		public function setXML(brushXML:XML):void
		{
			if(!brushXML.hasOwnProperty("@type")) return;
										
			var type:String = brushXML.@type;
			if(type != getType()) return;
																				
			if(!brushXML.hasOwnProperty("stroke")) return;
			for each(var strokeXML:XML in brushXML.stroke)
			{
				if(strokeXML.hasOwnProperty("@color"))
					this.setColor(strokeXML.@color);
				if(strokeXML.hasOwnProperty("@width"))
					this.setWidth(strokeXML.@width);
				if(strokeXML.hasOwnProperty("@alpha"))
					this.setAlpha(strokeXML.@alpha);
										
				this.startStroke(null);
				if(!strokeXML.hasOwnProperty("points")) continue;
				var pointsStr:String = strokeXML.points;
				var points:Array = pointsStr.split(";");
			
				for each(var point:String in points)
				{
					var coords:Array = point.split(",");
					if(coords.length != 2) continue;
					
					this.processPoint(new Point(new Number(coords[0]),new Number(coords[1])));
				}
				
				this.endStroke();
			}
			this.endTool();
			
			if(brushXML.hasOwnProperty("@color"))
				this.setColor(brushXML.@color);
			if(brushXML.hasOwnProperty("@width"))
				this.setWidth(brushXML.@width);
			if(brushXML.hasOwnProperty("@alpha"))
				this.setAlpha(brushXML.@alpha);	
		}		
	}
}