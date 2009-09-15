package brushes
{
	import flash.display.BitmapData;
	import flash.display.BlendMode;
	import flash.geom.Matrix;
	import flash.geom.Point;
	
	import mx.containers.Canvas;
	import mx.graphics.ImageSnapshot;

	public class Image implements IBrush
	{
		private var canvas:Canvas;
		private var strokes:Array = new Array();
		private var redo_history:Array = new Array();
		private var alpha:Number = 1;
		
		public function Image(canvas:Canvas)
		{
			this.canvas = canvas;
		}
		
		public function getCanvas():Canvas
		{
			return this.canvas;
		}
		
		public function drawImage(anchor:Point, image:BitmapData):void
		{	
			var stroke:BrushStroke = new BrushStroke();
			stroke.data = image;
			stroke.points.push(anchor);
			stroke.alpha = this.alpha;
			strokes.push(stroke);
			
			//Draw the image		
			var c1:Canvas = new Canvas();
			c1.width = image.width;
			c1.height = image.height;
			c1.graphics.beginBitmapFill(image,null,false,false);
			c1.graphics.drawRect(0,0,image.width,image.height);
			c1.graphics.endFill();
			c1.cacheAsBitmap = true;
			
			//Draw the alpha mask
			var c2:Canvas = new Canvas();
			c2.width = c1.width;
			c2.height = c1.height;
			c2.graphics.beginFill(0,alpha);
			c2.graphics.drawRect(0,0,c1.width,c1.height);
			c2.graphics.endFill();
			c2.cacheAsBitmap = true;
			c2.blendMode = BlendMode.ALPHA;
			
			//Add the mask to the original image
			c1.mask = c2;
			c1.validateNow();
			
			var imageWithAlpha:BitmapData = ImageSnapshot.captureBitmapData(c1);
			var matrix:Matrix = new Matrix();
			matrix.translate(anchor.x,anchor.y);
			
			//Draw the image on to the canvas
			canvas.graphics.beginBitmapFill(imageWithAlpha,matrix,false,false);
			canvas.graphics.drawRect(anchor.x,anchor.y,imageWithAlpha.width, imageWithAlpha.height);
			canvas.graphics.endFill();		
		}
						
		public function redraw():void
		{
			for each(var stroke:BrushStroke in strokes)
			{
				var anchor:Point = stroke.points[0];
				var image:BitmapData = BitmapData(stroke.data);
				
				//Draw the image		
				var c1:Canvas = new Canvas();
				c1.width = image.width;
				c1.height = image.height;
				c1.graphics.beginBitmapFill(image,null,false,false);
				c1.graphics.drawRect(0,0,image.width,image.height);
				c1.graphics.endFill();
				c1.cacheAsBitmap = true;
				
				//Draw the alpha mask
				var c2:Canvas = new Canvas();
				c2.width = c1.width;
				c2.height = c1.height;
				c2.graphics.beginFill(0,alpha);
				c2.graphics.drawRect(0,0,c1.width,c1.height);
				c2.graphics.endFill();
				c2.cacheAsBitmap = true;
				c2.blendMode = BlendMode.ALPHA;
				
				//Add the mask to the original image
				c1.mask = c2;
				c1.validateNow();
				
				var imageWithAlpha:BitmapData = ImageSnapshot.captureBitmapData(c1);
				var matrix:Matrix = new Matrix();
				matrix.translate(anchor.x,anchor.y);
				
				//Draw the image on to the canvas
				canvas.graphics.beginBitmapFill(imageWithAlpha,matrix,false,false);
				canvas.graphics.drawRect(anchor.x,anchor.y,imageWithAlpha.width, imageWithAlpha.height);
				canvas.graphics.endFill();					
			}
		}
		
		public function scale(x_ratio:Number, y_ratio:Number):void
		{
			//TODO: implement function
		}
		
		public function undo():Boolean
		{
			if(strokes.length > 0)
			{
				redo_history.push(strokes.pop());
				return true;
			}
			else
			{		
				return false;	
			}			
		}
		
		public function redo():Boolean
		{
			if(redo_history.length > 0)
			{
				strokes.push(redo_history.pop());
				return true;
			}
			else
			{				
				return false;	
			}
		}
		
		public function getAlpha():Number
		{			
			return this.alpha;
		}
		
		public function setAlpha(alpha:Number):void
		{
			this.alpha = alpha;
		}

		public function getType():String
		{
			return Controller.IMAGE;
		}
				
		public function getStrokes():Array
		{
			//TODO: implement function
			return null;
		}
		
		public function getXML():XML
		{
			var brushXML:XML = new XML("<brush></brush>");	
			brushXML.@type = getType();
			
			return brushXML;
		}
		public function setXML(xml:XML):void{ }

		//Functions not used
		public function processPoint(p:Point):void{}		
		public function startStroke(p:Point):void{}	
		public function endStroke():void{}		
		public function endTool():void{}		
		public function getColor():Number{ return 0;}		
		public function setColor(color:Number):void{}		
		public function getWidth():Number{return 0;}		
		public function setWidth(width:Number):void{}						
		public function getCursor():Class{ return null;}		
	}
}