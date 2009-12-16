package brushes
{
	import flash.display.Sprite;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import mx.core.Application;
	
	import util.GraphicsUtil;
	
	public class RectangleSelect extends Brush implements IBrush, ISelection
	{
		private var leftTop:Point;
		private var rightBottom:Point;
		private var sprite:Sprite = new Sprite();
		private var selection:flash.geom.Rectangle;
		
		public function RectangleSelect()
		{
			super();			
		}
			
		public function mouseDown(p:Point):void
		{
			leftTop = p;
			selection = new flash.geom.Rectangle();
			layer.addChild(sprite);
		}
		
		public function mouseMove(p:Point):void
		{
			rightBottom = p;
			
			var w:int = Math.round(rightBottom.x - leftTop.x);
			var h:int = Math.round(rightBottom.y - leftTop.y);
						
			this.sprite.graphics.clear();		
			this.sprite.graphics.beginFill(0x0,0.7);
			this.sprite.graphics.drawRect(0,0,Application.application.frame.width, Application.application.frame.height);
			this.sprite.graphics.drawRect(leftTop.x, leftTop.y, w, h);
			this.sprite.graphics.endFill();
					
			selection.x = leftTop.x;
			selection.y = leftTop.y;
			selection.width = w;
			selection.height = h;
		}
		
		public function mouseUp(p:Point):void
		{
			mouseMove(p);
		}
		
		public function finalize():void
		{
		}
		
		override public function getType():String
		{
			return GraphicsUtil.BRUSH_RECTANGLE_SELECT;
		}
		
		public function inSelection(p:Point):Boolean
		{
			if(leftTop == null || rightBottom == null || selection == null)
				return true;
			
			return selection.containsPoint(p);
		}
		
		public function removeSelection():void
		{
			leftTop = null;
			rightBottom = null;
			sprite.graphics.clear();
			selection = null;
			layer.removeChild(sprite);
		}		
		
		public function getSelection():flash.geom.Rectangle{
			return new flash.geom.Rectangle(leftTop.x, leftTop.y, rightBottom.x - leftTop.x, rightBottom.y - leftTop.y);
		}
		
		public function hideSelection():void
		{
			layer.removeChild(sprite);
		}
		
		public function showSelection():void
		{
			layer.addChild(sprite);
		}
		
	}
}