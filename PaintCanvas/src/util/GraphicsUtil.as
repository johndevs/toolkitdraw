package util
{
	import brushes.*;
	
	import elements.Layer;
	
	import flash.display.Loader;
	
	import mx.controls.Alert;
	import mx.core.Application;
		
	public class GraphicsUtil
	{
		private static var currentBrush:IBrush;
		private static var currentSelection:ISelection;
		
		public static const BRUSH_PEN:String = "Pen";
		public static const BRUSH_ELLIPSE:String = "Ellipse";
		public static const BRUSH_RECTANGLE:String = "Rectangle";
		public static const BRUSH_LINE:String = "Line";
		public static const BRUSH_POLYGON:String = "Polygon";	
		public static const BRUSH_FLOODFILL:String = "Floodfill";	
		public static const BRUSH_TEXT:String = "Text";
		public static const BRUSH_RECTANGLE_SELECT:String = "Rectangle-Select"
		public static const BRUSH_IMAGE:String = "Image";
		
		public static function setBrush(type:String):void
		{		
			var brush:IBrush = null;	
			switch(type)
			{
				case BRUSH_PEN:					brush = new Pen(); break;	
				case BRUSH_ELLIPSE: 			brush = new Ellipse(); break;		
				case BRUSH_RECTANGLE:			brush = new Rectangle(); break;	
				case BRUSH_LINE:				brush = new Line(); break;
				case BRUSH_POLYGON:				brush = new Polygon(); break;
				case BRUSH_FLOODFILL:			brush = new FloodFill(); break;
				case BRUSH_TEXT:				brush = new Text(); break;
				case BRUSH_RECTANGLE_SELECT: 	brush = new RectangleSelect(); break;
				case BRUSH_IMAGE:				brush = new ImageBrush(); break;
				default: return;
			}	
			
			if(LayerUtil.getCurrentLayer() == null)
				Alert.show("Cannot add brush, layer is not set!");
			else {
				LayerUtil.getCurrentLayer().addBrush(brush);
				currentBrush = brush;
				
				if(brush is RectangleSelect)
					currentSelection = (brush as RectangleSelect);
				
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
		
		public static function setBrushImage(data:String, loadedHandler:Function):void
		{
			if(currentBrush != null && currentBrush is ImageBrush)
			{
				(currentBrush as ImageBrush).setImage(data, loadedHandler);
			}
		}
		
		public static function setBrushImageWithLoader(data:Loader, dataString:String):void
		{
			if(currentBrush != null && currentBrush is ImageBrush)
			{
				(currentBrush as ImageBrush).setImageWithLoader(data, dataString);
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
				case BRUSH_IMAGE:		brush = new ImageBrush(); break;
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
		
		public static function getCurrentSelection():ISelection
		{
			return currentSelection;
		}	
		
		public static function removeSelection():void
		{
			if(currentSelection != null)
			{
				currentSelection.removeSelection();
			}
		}
		
		public static function endBrush():void{
			currentBrush.finalize();		
			EventUtil.fireBrushEnded(Application.application.parameters.id);
		}
		

	}
}