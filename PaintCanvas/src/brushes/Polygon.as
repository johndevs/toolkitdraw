package brushes
{
	import flash.geom.Point;
	
	import util.GraphicsUtil;

	public class Polygon extends FilledBrush implements IBrush
	{
		private var editing:Boolean = false;
		
		public function Polygon()
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
				
				if(stroke.points == null || stroke.points.length == 0)	continue;	
			
				currentStroke.sprite.graphics.lineStyle(stroke.size, stroke.color, stroke.alpha);
				currentStroke.sprite.graphics.beginFill(stroke.fillColor, stroke.fillAlpha);
				
				currentStroke.sprite.graphics.moveTo((stroke.points[0] as Point).x, (stroke.points[0]).y);
				for each(var dp:Point in currentStroke.points)
				{
					currentStroke.sprite.graphics.lineTo(dp.x, dp.y);
				}
				currentStroke.sprite.graphics.lineTo((stroke.points[0] as Point).x, (stroke.points[0]).y);	
				currentStroke.sprite.graphics.endFill();		
			}			
		}
		
		public function mouseDown(p:Point):void
		{
			if(!editing)
			{
				currentStroke = new FilledBrushStroke();
				currentStroke.points.push(p);
				layer.addChild(currentStroke.sprite);
				editing = true;
				
				currentStroke.sprite.graphics.lineStyle(size,color,alpha);
				currentStroke.sprite.graphics.moveTo(p.x,p.y);
			} 
		}
		
		public function mouseMove(p:Point):void
		{
			if(currentStroke == null)
				return;
			
			var cp:Point = currentStroke.points[0];
			
			currentStroke.sprite.graphics.clear();
			currentStroke.sprite.graphics.lineStyle(size, color, alpha);
			currentStroke.sprite.graphics.beginFill(fillColor, fillAlpha);
			
			currentStroke.sprite.graphics.moveTo(cp.x, cp.y);
			for each(var dp:Point in currentStroke.points)
			{
				currentStroke.sprite.graphics.lineTo(dp.x, dp.y);
			}
			
			currentStroke.sprite.graphics.lineTo(p.x, p.y);
			
			currentStroke.sprite.graphics.lineStyle(size, color, 0);
			currentStroke.sprite.graphics.lineTo(cp.x, cp.y)	
						
			currentStroke.sprite.graphics.endFill();			
		}
		
		public function mouseUp(p:Point):void
		{
			if(currentStroke == null)
				return;
			
			var cp:Point = currentStroke.points[0];
			
			currentStroke.sprite.graphics.clear();
			currentStroke.sprite.graphics.lineStyle(size, color,alpha);
			currentStroke.sprite.graphics.beginFill(fillColor, fillAlpha);
			
			currentStroke.sprite.graphics.moveTo(cp.x, cp.y);
			for each(var dp:Point in currentStroke.points)
			{
				currentStroke.sprite.graphics.lineTo(dp.x, dp.y);
			}
			
			currentStroke.sprite.graphics.lineTo(p.x, p.y);
			
			currentStroke.sprite.graphics.lineStyle(size, color, 0);
			currentStroke.sprite.graphics.lineTo(cp.x, cp.y)	
						
			currentStroke.sprite.graphics.endFill();		
				
			currentStroke.points.push(p);		
		}
		
		public function finalize():void
		{
			var cp:Point = currentStroke.points[0];
			
			currentStroke.sprite.graphics.clear();
			currentStroke.sprite.graphics.lineStyle(size, color,alpha);
			currentStroke.sprite.graphics.beginFill(fillColor, fillAlpha);
			
			currentStroke.sprite.graphics.moveTo(cp.x, cp.y);
			for each(var dp:Point in currentStroke.points)
			{
				currentStroke.sprite.graphics.lineTo(dp.x, dp.y);
			}
			currentStroke.sprite.graphics.lineTo(cp.x, cp.y)							
			currentStroke.sprite.graphics.endFill();		
					
			currentStroke.color = color;
			currentStroke.size = size;
			currentStroke.alpha = alpha;
			(currentStroke as FilledBrushStroke).fillColor = fillColor;
			(currentStroke as FilledBrushStroke).fillAlpha = fillAlpha;		
					
			strokes.push(currentStroke);
			currentStroke = null;
			editing = false;
		}
		
		override public function getType():String
		{
			return GraphicsUtil.BRUSH_POLYGON;
		}		
		
		override public function isFinishable():Boolean
		{
			return true;
		}		
	}
}