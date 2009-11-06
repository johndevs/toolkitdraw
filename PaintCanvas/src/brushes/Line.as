package brushes
{
	import elements.Layer;
	
	import flash.geom.Point;
	
	import util.GraphicsUtil;

	public class Line extends Brush implements IBrush
	{
		public function Line(layer:Layer)
		{
			super(layer);
		}
		
		public function redraw():void
		{
		}
		
		public function mouseDown(p:Point):void
		{
			currentStroke = new BrushStroke();
			currentStroke.points.push(p);
			layer.addChild(currentStroke.sprite);
			
			currentStroke.sprite.graphics.lineStyle(size,color,alpha);
			currentStroke.sprite.graphics.moveTo(p.x,p.y);
		}
		
		public function mouseMove(p:Point):void
		{
			var cp:Point = currentStroke.points[0];
			
			currentStroke.sprite.graphics.clear();
			currentStroke.sprite.graphics.lineStyle(size,color,alpha);
			currentStroke.sprite.graphics.moveTo(cp.x, cp.y);
			currentStroke.sprite.graphics.lineTo(p.x, p.y);
		}
		
		public function mouseUp(p:Point):void
		{
			var cp:Point = currentStroke.points[0];
			
			currentStroke.sprite.graphics.clear();
			currentStroke.sprite.graphics.lineStyle(size,color,alpha);
			currentStroke.sprite.graphics.moveTo(cp.x, cp.y);
			currentStroke.sprite.graphics.lineTo(p.x, p.y);
			
			currentStroke.points.push(p);
			
			currentStroke.color = color;
			currentStroke.size = size;
			currentStroke.alpha = alpha;
			
			strokes.push(currentStroke);
			currentStroke = null;
		}
		
		public function finalize():void
		{
		}
		
		override public function getType():String
		{
			return GraphicsUtil.BRUSH_LINE;
		}		
	}
}