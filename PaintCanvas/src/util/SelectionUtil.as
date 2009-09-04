package util
{
	import flash.display.BitmapData;
	import flash.display.BlendMode;
	import flash.geom.Point;
	
	import mx.containers.Canvas;
	
	public class SelectionUtil
	{
		private static const points:Array = new Array;		
		private static var currentCanvas:Canvas = null;
		private static const selection:Canvas = new Canvas();
		private static const foreground:Canvas = new Canvas();	
		private static var hitTestBitmap:BitmapData;
		
		/**
		 * Sets the selection
		 */ 
		public static function setSelection(pts:Array):void
		{			
			//Old selection is overwritten
			ArrayUtil.assignArray(points, pts);		
		}	
		
		/**
		 * Returns the currently selected points
		 */ 
		public static function getSelection():Array{
			return points;
		}
		
		/**
		 * Do we have a selection?
		 * 
		 */ 
		public static function hasSelection():Boolean{
			return currentCanvas != null;
		}
		
		/**
		 * Show the currently selected area
		 * 
		 */ 
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
					
			foreground.cacheAsBitmap = true;			
			foreground.blendMode = BlendMode.ERASE;
			foreground.width = selection.width;
			foreground.height = selection.height;
			
			foreground.graphics.clear();			
			foreground.graphics.beginFill(0xFFFFFF);		
			foreground.graphics.moveTo(Point(points[0]).x, Point(points[0]).y);			
			for(var i:int=1; i<points.length; i++)
			{
				foreground.graphics.lineTo(Point(points[i]).x,Point(points[i]).y);							
			}
			foreground.graphics.lineTo(Point(points[0]).x,Point(points[0]).y);													
			foreground.graphics.endFill();				
			
			hitTestBitmap = new BitmapData(foreground.width, foreground.height,true, 0x00000000);					
            hitTestBitmap.draw(foreground);
			
			selection.addChild(foreground);			
			currentCanvas.addChild(selection);
		}
		
		/**
		 * Remove the selection preview
		 */ 
		public static function hideSelection():void{			
			if(currentCanvas != null){
				currentCanvas.removeChild(selection);				
			} 					
			
			selection.removeAllChildren();			
			currentCanvas = null;
		}
		
		/**
		 * Select the whole screen area(paper area)
		 */ 
		public static function selectAll():void{
			hideSelection();
								
			var canv:Canvas = LayerUtil.getCurrentLayer().getCanvas();
			
			var arr:Array = new Array();
			arr.push(new Point(0,0));
			arr.push(new Point(canv.width, 0));
			arr.push(new Point(canv.width, canv.height));
			arr.push(new Point(0,canv.height));						
			SelectionUtil.setSelection(arr);
			
			SelectionUtil.showSelection(canv);
		}
								
		/**
		 * Determines if a coordinate inside or outside the selection
		 */ 		
		public static function inSelection(x:int, y:int):Boolean{							
			var p1:Point = new Point(0,0);
			var p2:Point = new Point(x,y);			
			return hitTestBitmap.hitTest(p1,0xFF,p2);
		}
		
		/**
		 * Resizes the image(paper) to the current selection
		 */  
		public static function cropSelection():void{
			
			
			
		}
		
	}
}