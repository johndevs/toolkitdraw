package util
{
	import brushes.*;
	
	import flash.display.Loader;
	import flash.events.Event;
	import flash.geom.Point;
	
	public class DrawingUtil
	{
		public static function drawPolygon(xa:Object, ya:Object, length:int):void
		{
			var color:Number = GraphicsUtil.getBrush().getColor();
			var alpha:Number = GraphicsUtil.getBrush().getAlpha();
			var size:Number = GraphicsUtil.getBrush().getSize();
			var fillcolor:Number = -1;
			var fillalpha:Number = -1;
			
			if(GraphicsUtil.getBrush() is FilledBrush)
			{
				fillcolor = (GraphicsUtil.getBrush() as FilledBrush).getFillColor();
				fillalpha = (GraphicsUtil.getBrush() as FilledBrush).getFillAlpha();
			}
			
			GraphicsUtil.setBrush(GraphicsUtil.BRUSH_POLYGON);
			GraphicsUtil.setBrushColor(color);
			GraphicsUtil.setBrushSize(size);
			GraphicsUtil.setBrushAlpha(alpha);
			
			if(fillcolor >= 0) GraphicsUtil.setBrushFillColor(fillcolor);
			if(fillalpha >= 0) GraphicsUtil.setBrushFillAlpha(fillalpha);
			
			GraphicsUtil.getBrush().mouseDown(new Point(xa[0], ya[0]));
			for(var i:int=1; i<length; i++)
			{				
				GraphicsUtil.getBrush().mouseMove(new Point(xa[i], ya[i]));
				GraphicsUtil.getBrush().mouseUp(new Point(xa[i], ya[i]));
			}
			GraphicsUtil.getBrush().finalize();			
		}
		
		public static function drawSquare(x:int, y:int, width:int, height:int):void
		{	
			var color:Number = GraphicsUtil.getBrush().getColor();
			var alpha:Number = GraphicsUtil.getBrush().getAlpha();
			var size:Number = GraphicsUtil.getBrush().getSize();
			var fillcolor:Number = -1;
			var fillalpha:Number = -1;
			
			if(GraphicsUtil.getBrush() is FilledBrush)
			{
				fillcolor = (GraphicsUtil.getBrush() as FilledBrush).getFillColor();
				fillalpha = (GraphicsUtil.getBrush() as FilledBrush).getFillAlpha();
			}
			
			GraphicsUtil.setBrush(GraphicsUtil.BRUSH_RECTANGLE);
			GraphicsUtil.setBrushColor(color);
			GraphicsUtil.setBrushSize(size);
			GraphicsUtil.setBrushAlpha(alpha);
			
			if(fillcolor >= 0) GraphicsUtil.setBrushFillColor(fillcolor);
			if(fillalpha >= 0) GraphicsUtil.setBrushFillAlpha(fillalpha);
			
			GraphicsUtil.getBrush().mouseDown(new Point(x, y));
			GraphicsUtil.getBrush().mouseMove(new Point(x+width, y+height));
			GraphicsUtil.getBrush().mouseUp(new Point(x+width, y+height));
			GraphicsUtil.getBrush().finalize();
		}
		
		public static function drawText(text:String, x:int, y:int, width:int, height:int):void
		{
			var color:Number = GraphicsUtil.getBrush().getColor();
			var alpha:Number = GraphicsUtil.getBrush().getAlpha();
			var size:Number = GraphicsUtil.getBrush().getSize();
			var fillcolor:Number = -1;
			var fillalpha:Number = -1;
			
			if(GraphicsUtil.getBrush() is FilledBrush)
			{
				fillcolor = (GraphicsUtil.getBrush() as FilledBrush).getFillColor();
				fillalpha = (GraphicsUtil.getBrush() as FilledBrush).getFillAlpha();
			}
			
			GraphicsUtil.setBrush(GraphicsUtil.BRUSH_TEXT);
			GraphicsUtil.setBrushColor(color);
			GraphicsUtil.setBrushSize(size);
			GraphicsUtil.setBrushAlpha(alpha);
						
			if(fillcolor >= 0) GraphicsUtil.setBrushFillColor(fillcolor);
			if(fillalpha >= 0) GraphicsUtil.setBrushFillAlpha(fillalpha);
					
			GraphicsUtil.getBrush().mouseDown(new Point(x, y));
			GraphicsUtil.getBrush().mouseMove(new Point(x+width, y+height));
			GraphicsUtil.getBrush().mouseUp(new Point(x+width, y+height));
			
			(GraphicsUtil.getBrush() as Text).setText(text);
			
			GraphicsUtil.getBrush().finalize();			
		}
		
		public static function drawLine(x1:int, y1:int, x2:int, y2:int):void
		{
			var color:Number = GraphicsUtil.getBrush().getColor();
			var alpha:Number = GraphicsUtil.getBrush().getAlpha();
			var size:Number = GraphicsUtil.getBrush().getSize();
			
			GraphicsUtil.setBrush(GraphicsUtil.BRUSH_LINE);
			GraphicsUtil.setBrushColor(color);
			GraphicsUtil.setBrushSize(size);
			GraphicsUtil.setBrushAlpha(alpha);
			
			GraphicsUtil.getBrush().mouseDown(new Point(x1, y1));
			GraphicsUtil.getBrush().mouseMove(new Point(x2, y2));
			GraphicsUtil.getBrush().mouseUp(new Point(x2, y2));
			GraphicsUtil.getBrush().finalize();			
		}
		
		public static function fill(x:int, y:int):void{
			var color:Number = GraphicsUtil.getBrush().getColor();
			var alpha:Number = GraphicsUtil.getBrush().getAlpha();
			var fillcolor:Number = -1;
			var fillalpha:Number = -1;
			
			GraphicsUtil.setBrush(GraphicsUtil.BRUSH_FLOODFILL);
			GraphicsUtil.setBrushColor(color);
			GraphicsUtil.setBrushAlpha(alpha);
			
			GraphicsUtil.getBrush().mouseDown(new Point(x, y));
			GraphicsUtil.getBrush().mouseUp(new Point(x, y));
			GraphicsUtil.getBrush().finalize();
		}
		
		public static function drawEllipse(x:int, y:int, width:int, height:int):void
		{
			var color:Number = GraphicsUtil.getBrush().getColor();
			var alpha:Number = GraphicsUtil.getBrush().getAlpha();
			var size:Number = GraphicsUtil.getBrush().getSize();
			var fillcolor:Number = -1;
			var fillalpha:Number = -1;
			
			if(GraphicsUtil.getBrush() is FilledBrush)
			{
				fillcolor = (GraphicsUtil.getBrush() as FilledBrush).getFillColor();
				fillalpha = (GraphicsUtil.getBrush() as FilledBrush).getFillAlpha();
			}
			
			GraphicsUtil.setBrush(GraphicsUtil.BRUSH_ELLIPSE);
			GraphicsUtil.setBrushColor(color);
			GraphicsUtil.setBrushSize(size);
			GraphicsUtil.setBrushAlpha(alpha);
			
			if(fillcolor >= 0) GraphicsUtil.setBrushFillColor(fillcolor);
			if(fillalpha >= 0) GraphicsUtil.setBrushFillAlpha(fillalpha);
						
			GraphicsUtil.getBrush().mouseDown(new Point(x-width/2, y-width/2));
			GraphicsUtil.getBrush().mouseMove(new Point(x+width/2, y+height/2));
			GraphicsUtil.getBrush().mouseUp(new Point(x+width/2, y+height/2));
			GraphicsUtil.getBrush().finalize();
		}
		
		public static function clear():void
		{
			LayerUtil.getCurrentLayer().clear();
		}
		
		public static function drawImage(x:int, y:int, width:int, height:int, data:String, alpha:Number):void
		{			
			var previousBrush:IBrush = GraphicsUtil.getBrush();
			GraphicsUtil.setBrush(GraphicsUtil.BRUSH_IMAGE);
			GraphicsUtil.setBrushImage(data, function(e:Event):void{
				GraphicsUtil.getBrush().mouseDown(new Point(x,y));
				GraphicsUtil.getBrush().mouseMove(new Point(x+width, y+height));
				GraphicsUtil.getBrush().mouseUp(new Point(x+width, y+height));
				GraphicsUtil.getBrush().finalize();				
				GraphicsUtil.setBrush(previousBrush.getType());		
			});			
		}
		
		public static function drawImageWithLoader(x:int, y:int, width:int, height:int, data:Loader, dataString:String, alpha:Number):void
		{				
			var previousBrush:IBrush = GraphicsUtil.getBrush();
			GraphicsUtil.setBrush(GraphicsUtil.BRUSH_IMAGE);
			GraphicsUtil.setBrushImageWithLoader(data, dataString);
			GraphicsUtil.getBrush().mouseDown(new Point(x,y));
			GraphicsUtil.getBrush().mouseMove(new Point(x+width, y+height));
			GraphicsUtil.getBrush().mouseUp(new Point(x+width, y+height));
			GraphicsUtil.getBrush().finalize();		
			GraphicsUtil.setBrush(previousBrush.getType());		
		}
		
		
	}
}