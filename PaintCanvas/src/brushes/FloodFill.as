package brushes
{
	import flash.display.BitmapData;
	import flash.geom.Point;
	
	import util.GraphicsUtil;

	public class FloodFill extends Brush implements IBrush
	{
		public function FloodFill()
		{
			super();
		}
		
		override public function redraw():void
		{
			if(strokes == null || strokes.length == 0) return;			
			
			for each(var stroke:BrushStroke in strokes)
			{				
				currentStroke = stroke;
								
				// Add the sprite to the layer
				if(layer == null) continue;				
								
				layer.addChild(currentStroke.sprite);
				
				if(stroke.points == null || stroke.points.length == 0)	continue;	
				
				var bd:BitmapData = new BitmapData(layer.width, layer.height, false);
		 		bd.draw(layer);
		 		
		 		bd.floodFill((stroke.points[0] as Point).x, (stroke.points[0] as Point).y, stroke.color);
		 		
		 		currentStroke.sprite.graphics.beginBitmapFill(bd);
		 		currentStroke.sprite.graphics.drawRect(0, 0, layer.width, layer.height);
		 		currentStroke.sprite.graphics.endFill();
				currentStroke.sprite.alpha = stroke.alpha;
			}			
		}
		
		public function mouseDown(p:Point):void
		{
		 	currentStroke = new BrushStroke();
			currentStroke.points.push(p);
			layer.addChild(currentStroke.sprite);
		 	
		 	var bd:BitmapData = new BitmapData(layer.width, layer.height, false);
		 	bd.draw(layer);
		 	
		 	bd.floodFill(p.x, p.y, color);
		 	
		 	currentStroke.sprite.graphics.beginBitmapFill(bd);
		 	currentStroke.sprite.graphics.drawRect(0, 0, layer.width, layer.height);
		 	currentStroke.sprite.graphics.endFill();
		 	currentStroke.sprite.alpha = alpha;
		 	
		 	currentStroke.color = color;
		 	currentStroke.alpha = alpha;
		 	
		 	strokes.push(currentStroke);
		 	currentStroke = null;			
		}
		
		public function mouseMove(p:Point):void
		{
		}
		
		public function mouseUp(p:Point):void
		{
		}
		
		public function finalize():void
		{
		}
		
		override public function getType():String
		{
			return GraphicsUtil.BRUSH_FLOODFILL;
		}
	}
}