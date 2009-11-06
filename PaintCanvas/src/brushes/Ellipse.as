package brushes
{
	import elements.Layer;
	
	import flash.geom.Point;
	
	import util.GraphicsUtil;

	public class Ellipse extends FilledBrush implements IBrush
	{
		public function Ellipse(layer:Layer)
		{
			super(layer);
		}
		
		public function redraw():void
		{
		}
		
		public function mouseDown(p:Point):void
		{
			currentStroke = new FilledBrushStroke();
			currentStroke.points.push(p);
			layer.addChild(currentStroke.sprite);
		}
		
		public function mouseMove(p:Point):void
		{
			var cp:Point = currentStroke.points[0];
			var w:int = Math.round(p.x - cp.x);
			var h:int = Math.round(p.y - cp.y);
			
			currentStroke.sprite.graphics.clear();
			currentStroke.sprite.graphics.beginFill(fillColor, fillAlpha);
			currentStroke.sprite.graphics.lineStyle(size,color,alpha);
			currentStroke.sprite.graphics.drawEllipse(cp.x, cp.y, w, h);
			currentStroke.sprite.graphics.endFill();
		}
		
		public function mouseUp(p:Point):void
		{
			var cp:Point = currentStroke.points[0];
			var w:int = Math.round(p.x - cp.x);
			var h:int = Math.round(p.y - cp.y);
			
			currentStroke.sprite.graphics.clear();
			currentStroke.sprite.graphics.beginFill(fillColor, fillAlpha);
			currentStroke.sprite.graphics.lineStyle(size,color,alpha);
			currentStroke.sprite.graphics.drawEllipse(cp.x, cp.y, w, h);
			currentStroke.sprite.graphics.endFill();
			
			currentStroke.points.push(p);
			currentStroke.color = color;
			currentStroke.size = size;
			currentStroke.alpha = alpha;
			(currentStroke as FilledBrushStroke).fillColor = fillColor;
			(currentStroke as FilledBrushStroke).fillAlpha = fillAlpha;
			
			strokes.push(currentStroke);
			currentStroke = null;
		}
		
		public function finalize():void
		{
		}
		
		override public function getType():String
		{
			return GraphicsUtil.BRUSH_ELLIPSE;
		}
		
	}
}