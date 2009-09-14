package brushes
{
	import flash.display.BitmapData;
	import flash.geom.Point;
	
	import mx.containers.Canvas;
	import mx.graphics.ImageSnapshot;

	public class Fill implements IFillableBrush
	{
		protected var canvas:Canvas;
							
		protected var fillcolor:Number = 0x0; 				
		protected var alpha:Number = 0;	
		
		protected var selectedPoint:Point;
		
		protected var strokes:Array = new Array();
		protected var currentStroke:BrushStroke;
				
		/**
		 * Constructor
		 */ 		
		public function Fill(canvas:Canvas)
		{
			this.canvas = canvas;			
		}

		/**
		 * Sets the fill color
		 */ 
		public function setFillColor(color:Number):void
		{
			this.fillcolor = color;
		}
		
		/**
		 * Process a point.
		 */ 
		public function processPoint(p:Point):void
		{
			selectedPoint = p;
		}
		
		/**
		 * Returns the fill color.
		 */ 
		public function getFillColor():Number
		{
			return fillcolor;
		}
		
		/**
		 * Start a new fill
		 */ 
		public function startStroke():void
		{
			currentStroke = new BrushStroke();
		}
		
		/**
		 * This method set the color of a pixel and neigbouring pixels
		 */ 
		private function processPixels(	point:Point, 
										data:BitmapData,										
										matchColor:uint, 							
										targetColor:uint,
										processedPixels:Array):void{
						
			if(point.x < 0 || point.x >= data.width)
				return;
			
			//Do not process already processed pixels
			for each(var p:Point in processedPixels){
				if(point.x == p.x && point.y == p.y)
					return;				
			}
			
			//Set the current pixel
			processedPixels.push(point);
			
			//Get the color from the image
			var pixelColor:uint = data.getPixel(point.x, point.y);			
													
			//If it is a match then draw on the mask
			if(pixelColor == matchColor){
				canvas.graphics.moveTo(point.x, point.y);
				canvas.graphics.lineTo(point.x+1, point.y);	
				currentStroke.points.push(point);
			} 
			
			//If another pixel is found then stop
			else {
				return;
			}
						
			//Process the 4 neighbouring pixels			
			processPixels(new Point(point.x-1, point.y), data, matchColor, targetColor, processedPixels);
			//processPixels(new Point(point.x+1, point.y), data, matchColor, targetColor, processedPixels);
			//processPixels(new Point(point.x, point.y-1), data, matchColor, targetColor, processedPixels);
			//processPixels(new Point(point.x, point.y+1), data, matchColor, targetColor, processedPixels);			
		}
		
		public function endStroke():void
		{
			//When the mouse button is released the get the pixel at that position
			if(selectedPoint != null){
				
				//Create a snapshot of the application
				var data:BitmapData = ImageSnapshot.captureBitmapData(this.canvas);
								
				//Get the color from the image
				var matchColor:uint = data.getPixel(selectedPoint.x, selectedPoint.y);							
								
				//Process the pixels
				processPixels(selectedPoint, data, matchColor, fillcolor, new Array());
								
				//Save the points in a brush				
				currentStroke.color = matchColor;
				currentStroke.fillcolor = fillcolor;
				currentStroke.alpha = alpha;
				
				//Add the brush to the strokes
				strokes.push(currentStroke);			
				currentStroke = null;	
			}
		}
		
				
		public function endTool():void
		{
			//Not in use
		}
		
		public function redraw():void
		{
		}
		
		public function scale(x_ratio:Number, y_ratio:Number):void
		{
		}
		
		public function undo():Boolean
		{
			return false;
		}
		
		public function redo():Boolean
		{
			return false;
		}
		
		public function getColor():Number
		{
			return fillcolor;
		}
		
		public function setColor(color:Number):void
		{
			fillcolor = color;
		}
		
		public function getWidth():Number
		{
			return 1;
		}
		
		public function setWidth(width:Number):void
		{
			//Not available
		}
		
		public function getAlpha():Number
		{
			return alpha;
		}
		
		public function setAlpha(alpha:Number):void
		{
			this.alpha = alpha;
		}
		
		public function getType():String
		{
			return Controller.FILL;
		}
		
		public function getCanvas():Canvas
		{
			return this.canvas;
		}
		
		public function getStrokes():Array
		{
			return strokes;
		}
		
		public function getCursor():Class
		{
			return null;
		}
		
		public function getXML():XML
		{
			var brushXML:XML = new XML("<brush></brush>");	
			brushXML.@type = getType();
			brushXML.@color = getColor();
			brushXML.@width = getWidth();	
			
			return brushXML;
		}
		
		public function setXML(xml:XML):void
		{
		}
		
	}
}