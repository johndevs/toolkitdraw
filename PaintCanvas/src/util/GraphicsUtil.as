package util
{
	import brushes.Ellipse;
	import brushes.Fill;
	import brushes.IBrush;
	import brushes.IFillableBrush;
	import brushes.Image;
	import brushes.Line;
	import brushes.Pen;
	import brushes.Polygon;
	import brushes.Rectangle;
	import brushes.RectangleSelect;
	import brushes.Text;
	
	import mx.controls.Alert;
	import mx.core.Application;
	
	
	public class GraphicsUtil
	{
		private static var painter:IBrush;
		private static var controller:Controller;
		private static var history:Array;
		private static var redo_history:Array;
		
		public static function setPainter(p:IBrush):void{
			painter = p;	
		}
		
		public static function getPainter():IBrush{
			return painter;
		}
		
		public static function setController(c:Controller):void{
			controller = c;
		}
		
		//Brush functions			
		public static function setBrushColor(color:Number):void
		{ 
			painter.setColor(color); 
			controller.changeEvent();
		}		
		
		public static function setBrushWidth(width:Number):void
		{ 
			painter.setWidth(width);
			controller.changeEvent(); 
		}
		
		public static function setBrushAlpha(alpha:Number):void
		{
			painter.setAlpha(alpha);
			controller.changeEvent();
		}
		
		public static function setHistory(h:Array, r:Array):void
		{
			history = h;
			redo_history = r;
		}
				
		public static function setFillColor(color:Number):void
		{	
			if(painter is IFillableBrush){			
				IFillableBrush(painter).setFillColor(color);
				controller.changeEvent();		
			}			
		}
		
		public static function setApplicationColor(color:String):void
		{
			Application.application.setStyle("backgroundColor", color);
			Application.application.setStyle("backgroundGradientColors",[color,color]);
			controller.changeEvent();
		}			
		
		
		
		//Set the brush
		public static function setBrush(type:String):void
		{				
			var brush:IBrush;
					
			if(LayerUtil.getCurrentLayer() == null){
				Alert.show("setBrush failed! No layer selected.");
				return;
			}
			
			switch(type)
			{
				case Controller.PEN: 		brush = new Pen(LayerUtil.getCurrentLayer().getCanvas()); break;				
				case Controller.RECTANGLE:	brush = new Rectangle(LayerUtil.getCurrentLayer().getCanvas()); break;				
				case Controller.ELLIPSE:	brush = new Ellipse(LayerUtil.getCurrentLayer().getCanvas()); break;				
				case Controller.LINE:		brush = new Line(LayerUtil.getCurrentLayer().getCanvas()); break;				
				case Controller.POLYGON:	brush = new Polygon(LayerUtil.getCurrentLayer().getCanvas()); break;
				case Controller.TEXT:		brush = new Text(LayerUtil.getCurrentLayer().getCanvas()); break;		
				case Controller.IMAGE:		brush = new Image(LayerUtil.getCurrentLayer().getCanvas()); break;
				case Controller.FILL:		brush = new Fill(LayerUtil.getCurrentLayer().getCanvas()); break;
				
				case Controller.RECTANGLE_SELECT:	brush = new RectangleSelect(LayerUtil.getCurrentLayer().getCanvas()); break;	
				
				default:		Alert.show("Brush \""+type+"\" was not found!");
			}		
			
			//Save the selected tool in history
			history.push(brush);
			
			//Set the current painter tool
			painter = history[history.length-1];
			
			//Notify controller of the painter change
			if(controller != null){
				controller.setPainter(painter);						
				controller.changeEvent();
			}
		}	
		
		public static function redraw():void
		{
			if(LayerUtil.getCurrentLayer() == null) 
				Alert.show("Redraw: No layer selected!");
						
			LayerUtil.getCurrentLayer().getCanvas().graphics.clear();
			
			for each(var brush:IBrush in history)
			{					
				if(brush.getCanvas() == LayerUtil.getCurrentLayer().getCanvas())				
					brush.redraw();
			}	
		}
		
		public static function redrawAll():void
		{
			//Erase all previous drawings
			for each(var layerName:String in LayerUtil.getLayerNames())			
				LayerUtil.getLayer(layerName).getCanvas().graphics.clear();
			
			//Redraw all history steps
			for each(var brush:IBrush in history)						
				brush.redraw();			
		}

		public static function setBrushFont(font:String):void
		{
			if(painter.getType() == Controller.TEXT)
			{
				Text(painter).setFont(font);
			}
		}
		
		public static function clearHistory():void
		{
			while(history.length > 0) history.pop();
			while(redo_history.length > 0) redo_history.pop();
		}
		
   		
	}
}