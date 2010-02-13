package brushes
{
	import flash.display.BitmapData;
	import flash.display.Loader;
	import flash.events.Event;
	import flash.geom.Point;
	import flash.utils.ByteArray;
	
	import mx.utils.Base64Decoder;
	
	import util.GraphicsUtil;
	
	public class ImageBrush extends FilledBrush implements IBrush
	{
		private var currentImage:BitmapData;
		private var currentImageString:String;
		private var currentImageLoader:Loader;
		
		private var loading:Boolean = false;
		
		public function ImageBrush()
		{
			super();
		}
		
		override public function redraw():void
		{
			if(strokes == null || strokes.length == 0) return;				
		}
		
		public function setImage(data:String, loadedHandler:Function):void
		{							
			currentImageString = data;
					
			var decoder:Base64Decoder = new Base64Decoder();
            decoder.decode(data);
            
            var byteArr:ByteArray;
            byteArr = decoder.toByteArray();
         
         	currentImageLoader = new Loader();
         	currentImageLoader.contentLoaderInfo.addEventListener(Event.COMPLETE, imageLoaded); 
         	currentImageLoader.contentLoaderInfo.addEventListener(Event.COMPLETE, loadedHandler); 
         	currentImageLoader.loadBytes(byteArr);        	
         	loading = true;
		}
		
		public function setImageWithLoader(data:Loader, dataString:String):void{
			currentImageLoader = data;
			currentImageString = dataString;
			imageLoaded(null);			
		}
		
		private function imageLoaded(event:Event):void
		{			
			currentImage = new BitmapData(currentImageLoader.content.width, currentImageLoader.content.height);
         	currentImage.draw(currentImageLoader);         	
         	loading = false;
		}
		
		public function mouseDown(p:Point):void
		{
			currentStroke = new FilledBrushStroke();
			(currentStroke as FilledBrushStroke).fillImage = currentImageString;			
			currentStroke.points.push(p);
			layer.addChild(currentStroke.sprite);
		}
		
		public function mouseMove(p:Point):void
		{
			var cp:Point = currentStroke.points[0];
			var w:int = Math.round(p.x - cp.x);
			var h:int = Math.round(p.y - cp.y);
			
			currentStroke.sprite.graphics.clear();
			currentStroke.sprite.graphics.beginBitmapFill(currentImage);
			currentStroke.sprite.graphics.drawRect(cp.x, cp.y, w, h);
			currentStroke.sprite.graphics.endFill();
		}
		
		public function mouseUp(p:Point):void
		{
			var cp:Point = currentStroke.points[0];
			var w:int = Math.round(p.x - cp.x);
			var h:int = Math.round(p.y - cp.y);
			
			currentStroke.sprite.graphics.clear();
			currentStroke.sprite.graphics.beginBitmapFill(currentImage);
			currentStroke.sprite.graphics.drawRect(cp.x, cp.y, w, h);
			currentStroke.sprite.graphics.endFill();
			
			currentStroke.color = color;
			currentStroke.size = size;
			currentStroke.alpha = alpha;
			(currentStroke as FilledBrushStroke).fillColor = fillColor;
			(currentStroke as FilledBrushStroke).fillAlpha = fillAlpha;
			
			currentStroke.points.push(p);
			strokes.push(currentStroke);
			currentStroke = null;
		}
		
		public function finalize():void
		{
		}
				
		override public function getType():String
		{
			return GraphicsUtil.BRUSH_IMAGE;
		}
	}
}