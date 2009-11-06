package brushes
{
	import elements.Layer;
	
	import flash.geom.Point;
	
	import util.GraphicsUtil;

	public class Pen extends Brush implements IBrush, IUndoableBrush
	{			
		public function Pen(layer:Layer)
		{
			super(this.layer);
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
			currentStroke.sprite.graphics.lineTo(p.x,p.y);						
		}
		
		public function mouseMove(p:Point):void
		{
			currentStroke.points.push(p);
			currentStroke.sprite.graphics.lineTo(p.x, p.y);
		}
		
		public function mouseUp(p:Point):void
		{
			currentStroke.points.push(p);
			currentStroke.size = size;
			currentStroke.color = color;
			currentStroke.alpha = alpha;
			
			strokes.push(currentStroke);
			currentStroke = null;
		}
		
		public function finalize():void
		{
		}
		
		override public function getType():String
		{
			return GraphicsUtil.BRUSH_PEN;
		}
		
	}
}