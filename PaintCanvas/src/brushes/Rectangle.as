package brushes
{
	import flash.geom.Point;
	
	import mx.containers.Canvas;

	public class Rectangle implements IFillableBrush
	{
		protected var canvas:Canvas;
		protected var selection:Canvas;
		
		protected var drawing:Boolean = false;
		protected var startPoint:Point = null;				
		protected var endPoint:Point = null;	
			
		protected var current_stroke:BrushStroke;
		protected var fillColor:Number = -1;
		protected var color:Number = 0;
		protected var width:int = 1;
		
		protected var strokes:Array = new Array;
		protected var redo_history:Array = new Array;
		
		public function Rectangle(canvas:Canvas)
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
			
			current_stroke = new BrushStroke();	
			current_stroke.color = color;
			current_stroke.width = width;
			current_stroke.fillcolor = fillColor;
			
			redo_history = new Array;						
		}
		
		public function endStroke():void
		{								
			if(current_stroke != null)
			{
				current_stroke.points.push(startPoint);
				current_stroke.points.push(endPoint);
				strokes.push(current_stroke);
				
				var width:int = startPoint.x-endPoint.x;
				var height:int = startPoint.y-endPoint.y;
				
				canvas.graphics.lineStyle(current_stroke.width, current_stroke.color, current_stroke.alpha);
				
				if(current_stroke.fillcolor != -1)
					canvas.graphics.beginFill(current_stroke.fillcolor);
				
				//Sector 4
				if(width <= 0 && height <= 0)					
						canvas.graphics.drawRect(startPoint.x, startPoint.y, Math.abs(width),Math.abs(height));							
				
				//Sector 3
				else if(width >= 0 && height <= 0)
					canvas.graphics.drawEllipse(endPoint.x, startPoint.y, width, Math.abs(height));
					
				//Sector 2
				else if(width <= 0 && height >= 0)
					canvas.graphics.drawEllipse(startPoint.x, endPoint.y, Math.abs(width), height);
				
				//Sector 1
				else
					canvas.graphics.drawEllipse(endPoint.x, endPoint.y, width, height);	
					
				if(current_stroke.fillcolor != -1)
					canvas.graphics.endFill();					
			}					
			
			canvas.removeChild(selection);		
			current_stroke = null;
			
			startPoint = null;
			endPoint = null;	
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
					canvas.graphics.drawRect(topLeft.x,topLeft.y,Math.abs(width),Math.abs(height));							
				
				//Sector 3
				else if(width >= 0 && height <= 0)
					canvas.graphics.drawRect(bottomRight.x,topLeft.y,width,Math.abs(height));
					
				//Sector 2
				else if(width <= 0 && height >= 0)
					canvas.graphics.drawRect(topLeft.x,bottomRight.y,Math.abs(width),height);
				
				//Sector 1
				else
					canvas.graphics.drawRect(bottomRight.x,bottomRight.y,width,height);				
					
					
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
			return color;
		}
		
		public function setColor(color:Number):void
		{					
			this.color = color;
		}
		
		public function getWidth():Number
		{
			return width;
		}
		
		public function setWidth(width:Number):void
		{						
			this.width = width;
		}
		
		public function getType():String
		{
			return Controller.RECTANGLE;
		}
		
		public function setFillColor(color:Number):void
		{			
			this.fillColor = color;
		}
		
		public function getFillColor():Number
		{
			return this.fillColor;	
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
			return strokes;
		}
		
		public function getCursor():Class
		{
			return null;
		}
		
		public function endTool():void
		{
			
		}
		
		public function getAlpha():Number{
			return 0;	
		}
		
		public function setAlpha(alpha:Number):void
		{
		}
		
		public function getXML():XML
		{
			var brushXML:XML = new XML("<brush></brush>");	
			brushXML.@type = getType();
			brushXML.@color = getColor();
			brushXML.@width = getWidth();	
			brushXML.@fill = getFillColor();
			
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
		
		// TODO Processing of background points
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
				if(strokeXML.hasOwnProperty("@fill"))
					this.setFillColor(strokeXML.@fill);
										
				this.startStroke();
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
			if(brushXML.hasOwnProperty("@fill"))
				this.setFillColor(brushXML.@fill);
		}		
		
	}
}