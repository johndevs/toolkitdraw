package brushes
{
	import flash.geom.Point;
	import elements.Layer;
	
	public class FilledBrush extends Brush
	{
		protected var fillColor:uint = 0;		
		protected var fillAlpha:Number = 0;
		protected var fillImage:String = "";
		
		public function FilledBrush()
		{
			super();
		}
		
		public function setFillColor(color:uint):void
		{
			this.fillColor = color;
		}
		
		public function setFillAlpha(alpha:Number):void
		{
			this.fillAlpha = alpha;
		}
		
		public function setFillImage(data:String):void
		{
			this.fillImage = data;	
		}
		
		public function getFillColor():uint
		{
			return this.fillColor;
		}
		
		public function getFillAlpha():Number
		{
			return this.fillAlpha;
		}
		
		public function getFillImage():String
		{
			return this.fillImage;
		}
		
		override public function toXML():XML
		{
			var brushXML:XML = new XML("<brush></brush>");	
			brushXML.@type = getType();
			brushXML.@color = getColor();
			brushXML.@size = getSize();	
			brushXML.@alpha = getAlpha();
			brushXML.@fillalpha = getFillAlpha();
			brushXML.@fillcolor = getFillColor();
			brushXML.data = getFillImage();
			
			for each(var stroke:FilledBrushStroke in strokes){
				var strokeXML:XML = new XML("<stroke></stroke>");
				strokeXML.@color = stroke.color;
				strokeXML.@alpha = stroke.alpha;
				strokeXML.@size = stroke.size;
				strokeXML.@fillcolor = stroke.fillColor;
				strokeXML.@fillalpha = stroke.fillAlpha;
				strokeXML.@orderNumber = strokes.indexOf(stroke);
				
				var points:String = "";
				for each(var p:Point in stroke.points)
					points += p.x+","+p.y+";";
					
				strokeXML.points = points;		
				strokeXML.data = stroke.fillImage;					
				brushXML.appendChild(strokeXML);	
			}			
			
			return brushXML;
		}
		
		override public function fromXML(brushXML:XML):void
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
			if(brushXML.hasOwnProperty("@fillalpha"))
				this.setFillAlpha(brushXML.@fillalpha);
			if(brushXML.hasOwnProperty("@fillcolor"))
				this.setFillColor(brushXML.@fillcolor);
			
			if(brushXML.hasOwnProperty("data"))
				this.setFillImage(brushXML.data);
						
			if(!brushXML.hasOwnProperty("stroke")) return;
			for each(var strokeXML:XML in brushXML.stroke)
			{
				currentStroke = new FilledBrushStroke();
				
				if(strokeXML.hasOwnProperty("@color"))
					currentStroke.color = strokeXML.@color;
				if(strokeXML.hasOwnProperty("@size"))
					currentStroke.size = strokeXML.@size;
				if(strokeXML.hasOwnProperty("@alpha"))
					currentStroke.alpha = strokeXML.@alpha;
				if(strokeXML.hasOwnProperty("@fillalpha"))
					(currentStroke as FilledBrushStroke).fillAlpha = strokeXML.@fillalpha;
				if(strokeXML.hasOwnProperty("@fillcolor"))
					(currentStroke as FilledBrushStroke).fillColor = strokeXML.@fillcolor;
										
				if(strokeXML.hasOwnProperty("data"))
					(currentStroke as FilledBrushStroke).fillImage = strokeXML.data;						
										
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
		}
		
		
	}
}