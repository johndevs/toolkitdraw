package util
{
	import brushes.Ellipse;
	import brushes.FloodFill;
	import brushes.IBrush;
	import brushes.Line;
	import brushes.Pen;
	import brushes.Polygon;
	import brushes.Rectangle;
	
	import elements.Layer;
	
	
	public class GraphicsUtil
	{
		private static var currentBrush:IBrush;
		
		public static const BRUSH_PEN:String = "Pen";
		public static const BRUSH_ELLIPSE:String = "Ellipse";
		public static const BRUSH_RECTANGLE:String = "Rectangle";
		public static const BRUSH_LINE:String = "Line";
		public static const BRUSH_POLYGON:String = "Polygon";	
		public static const BRUSH_FLOODFILL:String = "Floodfill";	
		public static const BRUSH_TEXT:String = "Text";
		
		public static function setBrush(type:String):void
		{				
			var brush:IBrush = null;	
			switch(type)
			{
				case BRUSH_PEN:			brush = new Pen(LayerUtil.getCurrentLayer()); break;	
				case BRUSH_ELLIPSE: 	brush = new Ellipse(LayerUtil.getCurrentLayer()); break;		
				case BRUSH_RECTANGLE:	brush = new Rectangle(LayerUtil.getCurrentLayer()); break;	
				case BRUSH_LINE:		brush = new Line(LayerUtil.getCurrentLayer()); break;
				case BRUSH_POLYGON:		brush = new Polygon(LayerUtil.getCurrentLayer()); break;
				case BRUSH_FLOODFILL:	brush = new FloodFill(LayerUtil.getCurrentLayer()); break;
				case BRUSH_TEXT:		brush = new Text(LayerUtil.getCurrentLayer()); break;
				default: return;
			}	
			
			LayerUtil.getCurrentLayer().addBrush(brush);
			currentBrush = brush;
		}
		
		public static function getBrush():IBrush
		{
			return currentBrush;
		}
		
		public static function setBrushSize(size:int):void
		{
			currentBrush.setSize(size);
		}
		
		public static function setBrushColor(color:uint):void
		{
			currentBrush.setColor(color);
		}
		
		public static function setBrushAlpha(alpha:Number):void
		{
			currentBrush.setAlpha(alpha);
		}
		
		public static function setBrushFillColor(color:uint):void
		{
			if(currentBrush is FilledBrush)
			{
				(currentBrush as FilledBrush).setFillColor(color);
			}	
		}
		
		public static function setBrushFillAlpha(alpha:Number):void
		{
			if(currentBrush is FilledBrush)
			{
				(currentBrush as FilledBrush).setFillAlpha(alpha);
			}	
		}
		
		public static function setBrushFontName(font:String):void
		{
			if(currentBrush is Text)
			{
				(currentBrush as Text).setFontName(font);
			}
		}
		
		public static function createBrushFromXML(xml:XML, layer:Layer):IBrush
		{
			var type:String = xml.@type;
			var brush:IBrush = null;	
			switch(type)
			{
				case BRUSH_PEN:			brush = new Pen(LayerUtil.getCurrentLayer()); break;	
				case BRUSH_ELLIPSE: 	brush = new Ellipse(LayerUtil.getCurrentLayer()); break;		
				case BRUSH_RECTANGLE:	brush = new Rectangle(LayerUtil.getCurrentLayer()); break;	
				case BRUSH_LINE:		brush = new Line(LayerUtil.getCurrentLayer()); break;
				case BRUSH_POLYGON:		brush = new Polygon(LayerUtil.getCurrentLayer()); break;
				case BRUSH_FLOODFILL:	brush = new FloodFill(LayerUtil.getCurrentLayer()); break;
				case BRUSH_TEXT:		brush = new Text(LayerUtil.getCurrentLayer()); break;
			}
			
			brush.fromXML(xml);
			layer.addBrush(brush);
			currentBrush = brush;		
			return brush;	
		}		
		

	}
}