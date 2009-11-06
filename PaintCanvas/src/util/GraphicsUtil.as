package util
{
	import brushes.Ellipse;
	import brushes.FloodFill;
	import brushes.IBrush;
	import brushes.Line;
	import brushes.Pen;
	import brushes.Polygon;
	import brushes.Rectangle;
	import brushes.Text;
	import brushes.FilledBrush;
	
	import elements.Layer;
	
	import mx.controls.Alert;
	

	
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
				case BRUSH_PEN:			brush = new Pen(); break;	
				case BRUSH_ELLIPSE: 	brush = new Ellipse(); break;		
				case BRUSH_RECTANGLE:	brush = new Rectangle(); break;	
				case BRUSH_LINE:		brush = new Line(); break;
				case BRUSH_POLYGON:		brush = new Polygon(); break;
				case BRUSH_FLOODFILL:	brush = new FloodFill(); break;
				case BRUSH_TEXT:		brush = new Text(); break;
				default: return;
			}	
			
			if(LayerUtil.getCurrentLayer() == null)
				Alert.show("Cannot add brush, layer is not set!");
			else {
				LayerUtil.getCurrentLayer().addBrush(brush);
				currentBrush = brush;
			}
		}
		
		public static function getBrush():IBrush
		{
			return currentBrush;
		}
		
		public static function setBrushSize(size:int):void
		{
			if(currentBrush != null)
				currentBrush.setSize(size);
		}
		
		public static function setBrushColor(color:uint):void
		{
			if(currentBrush != null)
				currentBrush.setColor(color);
		}
		
		public static function setBrushAlpha(alpha:Number):void
		{
			if(currentBrush != null)
				currentBrush.setAlpha(alpha);
		}
		
		public static function setBrushFillColor(color:uint):void
		{
			if(currentBrush != null && currentBrush is FilledBrush)
			{
				(currentBrush as FilledBrush).setFillColor(color);
			}	
		}
		
		public static function setBrushFillAlpha(alpha:Number):void
		{
			if(currentBrush != null && currentBrush is FilledBrush)
			{
				(currentBrush as FilledBrush).setFillAlpha(alpha);
			}	
		}
		
		public static function setBrushFontName(font:String):void
		{
			if(currentBrush != null && currentBrush is Text)
			{
				(currentBrush as Text).setFontName(font);
			}
		}
		
		public static function createBrushFromXML(xml:XML, layer:Layer):IBrush
		{
			if(layer == null)
				Alert.show("createBrushFromXML: Layer is null");			
			
			var type:String = xml.@type;
			var brush:IBrush = null;	
			switch(type)
			{
				case BRUSH_PEN:			brush = new Pen(); break;	
				case BRUSH_ELLIPSE: 	brush = new Ellipse(); break;		
				case BRUSH_RECTANGLE:	brush = new Rectangle(); break;	
				case BRUSH_LINE:		brush = new Line(); break;
				case BRUSH_POLYGON:		brush = new Polygon(); break;
				case BRUSH_FLOODFILL:	brush = new FloodFill(); break;
				case BRUSH_TEXT:		brush = new Text(); break;
			}
			
			if(brush == null){
				Alert.show("createBrushFromXML: Brush was null");
				return brush;
			}
			
			// Add the strokes to the brush and redraw
			brush.fromXML(xml);
					
			// Add the brush to the layer and set it as teh current brush
			layer.addBrush(brush);
			currentBrush = brush;		
			
			return brush;	
		}		
		

	}
}