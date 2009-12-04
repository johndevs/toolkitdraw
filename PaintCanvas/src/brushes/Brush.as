package brushes
{
	import elements.Layer;
	
	import flash.geom.Point;
	
	import mx.controls.Alert;

	public class Brush
	{
		protected var layer:Layer;
		protected var strokes:Array = new Array();
		protected var undoneStrokes:Array = new Array();
		protected var currentStroke:BrushStroke;			
		
		protected var color:uint = 0x0;
		protected var size:uint = 1;
		protected var alpha:Number = 1;
				
		public function Brush()
		{
		}
		
		public function setLayer(layer:Layer):void
		{
			if(layer == null) 
				Alert.show("Warning: Setting brush layer to null!");
				
			this.layer = layer;
		}
		
		public function getLayer():Layer
		{
			return this.layer;
		}
		
		public function setColor(color:uint):void
		{
			this.color = color;	
		}
		
		public function getColor():uint
		{
			return this.color;
		}
		
		public function setSize(size:uint):void
		{
			this.size = size;
		}
		
		public function getSize():uint
		{
			return this.size;	
		}
		
		public function setAlpha(alpha:Number):void
		{
			this.alpha = alpha;
		}
		
		public function getAlpha():Number
		{
			return this.alpha;
		}
		
		public function redraw():void{
			// Override this in the actual brush
		}
		
		public function toXML():XML
		{
			var brushXML:XML = new XML("<brush></brush>");	
			brushXML.@type = getType();
			brushXML.@color = getColor();
			brushXML.@size = getSize();	
			brushXML.@alpha = getAlpha();
			
			for each(var stroke:BrushStroke in strokes){
				var strokeXML:XML = new XML("<stroke></stroke>");
				strokeXML.@color = stroke.color;
				strokeXML.@alpha = stroke.alpha;
				strokeXML.@size = stroke.size;
				strokeXML.@orderNumber = strokes.indexOf(stroke);
				
				var points:String = "";
				for each(var p:Point in stroke.points)
					points += p.x+","+p.y+";";
					
				strokeXML.points = points;							
				brushXML.appendChild(strokeXML);	
			}			
			
			return brushXML;
		}
		
		public function fromXML(brushXML:XML):void
		{
			if(!brushXML.hasOwnProperty("@type")) return;
			var type:String = brushXML.@type;
			if(type != getType()) return;
			
			if(brushXML.hasOwnProperty("@color"))
				this.setColor(brushXML.@color);
			
			if(brushXML.hasOwnProperty("@size"))
				this.setSize(brushXML.@size);
			
			if(brushXML.hasOwnProperty("@alpha"))
				this.setAlpha(brushXML.@alpha);	
						
			if(!brushXML.hasOwnProperty("stroke")) return;
			
			for each(var strokeXML:XML in brushXML.stroke)
			{
				currentStroke = new BrushStroke();
				
				if(strokeXML.hasOwnProperty("@color"))
					currentStroke.color = strokeXML.@color;
				
				if(strokeXML.hasOwnProperty("@size"))
					currentStroke.size = strokeXML.@size;
				
				if(strokeXML.hasOwnProperty("@alpha"))
					currentStroke.alpha = strokeXML.@alpha;
										
				if(!strokeXML.hasOwnProperty("points")) continue;
				var pointsStr:String = strokeXML.points;
				var points:Array = pointsStr.split(";");
			
				for each(var point:String in points)
				{
					var coords:Array = point.split(",");
					if(coords.length != 2) continue;
					currentStroke.points.push(new Point(new Number(coords[0]),new Number(coords[1])));
				}
				
				strokes.push(currentStroke);
			}		
			
			redraw();	
		}
		
		public function undo():Boolean
		{ 
			if(strokes.length > 0)
			{
				var stroke:BrushStroke = strokes.pop();				
				layer.removeChild(stroke.sprite);			
				undoneStrokes.push(stroke);										
				return true;
			}
			
			return false;
		}
		
		public function redo():Boolean
		{
			if(undoneStrokes.length > 0)
			{
				var stroke:BrushStroke = undoneStrokes.pop();
				layer.addChild(stroke.sprite);
				strokes.push(stroke);
				return true;
			}	
			
			return false;
		}
		
		public function getType():String
		{
			return "Undefined";	
		}		
		
		public function isFinishable():Boolean
		{
			return false;
		}
		
	}
}