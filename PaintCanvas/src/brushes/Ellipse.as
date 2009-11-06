package brushes
{
	import flash.geom.Point;
	
	import util.GraphicsUtil;

	public class Ellipse extends FilledBrush implements IBrush
	{
		public function Ellipse()
		{
			super();
		}
		
		override public function redraw():void
		{
			if(strokes == null || strokes.length == 0) return;			
			
			for each(var stroke:FilledBrushStroke in strokes)
			{				
				currentStroke = stroke;
				
				// Add the sprite to the layer
				if(layer == null) continue;				
								
				layer.addChild(currentStroke.sprite);
				
				if(stroke.points == null || stroke.points.length < 2)	continue;	
				
				currentStroke.sprite.graphics.lineStyle(stroke.size, stroke.color, stroke.alpha);
				currentStroke.sprite.graphics.beginFill(stroke.fillColor, stroke.fillAlpha);
				
				var p1:Point = stroke.points[0];
				var p2:Point = stroke.points[1];
				var w:int = Math.round(p2.x - p1.x);
				var h:int = Math.round(p2.y - p1.y);
				
				currentStroke.sprite.graphics.drawEllipse(p1.x, p1.y, w, h);				
				currentStroke.sprite.graphics.endFill();		
			}
			
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