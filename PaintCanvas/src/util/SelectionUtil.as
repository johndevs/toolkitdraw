package util
{
	import flash.display.BlendMode;
	import flash.geom.Point;
	
	import mx.containers.Canvas;
	import mx.controls.ColorPicker;
	
	public class SelectionUtil
	{
		private static const points:Array = new Array;
		private static var mode:String;
		private static var currentCanvas:Canvas = null;
		private static const selection:Canvas = new Canvas();
		
		public static function setSelection(m:String, pts:Array):void
		{
			//Set mode
			mode = m;	
				
			//Old selection is overwritten
			ArrayUtil.assignArray(points, pts);		
		}
		
		public static function getMode():String{
			return mode;
		}
		
		public static function getPoints():Array{
			return points;
		}
		
		public static function showSelection(canvas:Canvas):void{
						
			//Hide possible previous selection
			SelectionUtil.hideSelection();
			
			//Add the new selection		
			currentCanvas = canvas;
			
			selection.cacheAsBitmap = true;
			selection.width = canvas.width;
			selection.height = canvas.height;
			selection.blendMode = BlendMode.LAYER;		
			
			selection.graphics.clear();
			selection.graphics.beginFill(0, 0.8);
			selection.graphics.drawRect(0,0,selection.width,selection.height);
			selection.graphics.endFill();		
								
			var fg:Canvas = new Canvas();
			fg.cacheAsBitmap = true;
			fg.blendMode = BlendMode.ERASE;
			fg.graphics.clear();
			fg.graphics.beginFill(0xFFFFFF);
			
			if(mode == Controller.RECTANGLE_SELECT)
			{
				var upperLeft:Point = points[0];
				var lowerRight:Point = points[1];				
				fg.graphics.drawRect(upperLeft.x, upperLeft.y, lowerRight.x-upperLeft.x, lowerRight.y-upperLeft.y);
			
				selection.graphics.lineStyle(1,0x0000FF,0.5);
				selection.graphics.drawRect(upperLeft.x-1, upperLeft.y-1, lowerRight.x-upperLeft.x+1, lowerRight.y-upperLeft.y+1);
			}			
			
			fg.graphics.endFill();
			
			selection.addChild(fg);
			
			currentCanvas.addChild(selection);
		}
		
		public static function hideSelection():void{			
			if(currentCanvas != null){
				currentCanvas.removeChild(selection);				
			} 					
			
			selection.removeAllChildren();			
			currentCanvas = null;
		}
	}
}