package brushes
{
	import flash.geom.Point;
	
	import mx.containers.Canvas;
	import mx.controls.Alert;
	
	public class Pen implements IBrush
	{
		protected var canvas:Canvas;
		protected var strokes:Array = new Array;
		
		protected var current_stroke:BrushStroke;
		protected var current_color:Number = 0x0;
		protected var current_width:Number = 1;
		
		protected var redo_history:Array = new Array;
		
				
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
				Alert.show("Pen: No current stroke!");
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
			return null;
		}
		
		public function endTool():void{ }
		
		public function getAlpha():Number{
			return 0;	
		}
		
		public function setAlpha(alpha:Number):void{ }
		
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
		}		
	}
}