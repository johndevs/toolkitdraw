package brushes
{
	import flash.display.BitmapData;
	import flash.geom.Point;
	import flash.utils.ByteArray;
	
	import mx.containers.Canvas;
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.graphics.ImageSnapshot;

	public class Fill implements IFillableBrush
	{
		protected var canvas:Canvas;
							
		protected var fillcolor:Number = 0x0; 				
		protected var alpha:Number = 0;	
		
		protected var selectedPoint:Point;
		
		protected var strokes:Array = new Array();
		protected var currentStroke:BrushStroke;
				
		public function Fill(canvas:Canvas)
		{
			this.canvas = canvas;			
		}

		public function setFillColor(color:Number):void
		{
			this.fillcolor = color;
		}
		
		public function processPoint(p:Point):void
		{
			selectedPoint = p;
		}
		
		public function getFillColor():Number
		{
			return fillcolor;
		}
		
		private function pointHasBeenDrawn(x:int, y:int):Boolean
		{
			for each(var p2:Point in currentStroke.points)
				if(p2.x == x && p2.y == y) return true;
			
			return false;
		}
		
		private function queueBasedFloodFill(point:Point):void
		{
			//Create a snapshot of the application
		
			var data:BitmapData = ImageSnapshot.captureBitmapData(this.canvas);			
			var width:Number = new Number(data.width);
			var height:Number = new Number(data.height);
				
			//Get pixels
			var pixels:ByteArray = data.getPixels(new flash.geom.Rectangle(0,0,width,height));
			pixels.position = point.y*width + point.x;
			
			//Free memory of the screenshot
			data.dispose();
			
			// 1. Set Q to the empty queue.
			var queue:Array = new Array();	
						
			// 2. If the color of node is not equal to target-color, return.
			Alert.show(pixels.readUnsignedInt()+" == " + currentStroke.color);
			if(pixels.readUnsignedInt() == currentStroke.color) return;
						
			// 3. Add node to the end of Q.
			queue.push(point);			
			
			// 4. While Q is not empty: 
			this.canvas.graphics.lineStyle(1,currentStroke.color, currentStroke.alpha);
			var maxloop:Number = 100;
			while(queue.length > 0)
			{
				var n:Point = queue.shift() as Point;
								
				// 6. If the color of n is equal to target-color, set the color of n to replacement-color.				
				this.canvas.graphics.drawCircle(n.x, n.y, 1);
				
				// 8. If the color of the node to the west of n is target-color, 
				// set the color of that node to replacement-color, 
				// add that node to the end of Q.
				pixels.position = n.y*width + point.x-1;
				Alert.show(pixels.readUnsignedInt()+" == " + currentStroke.color);
				if(n.x-1 >= 0 && !pointHasBeenDrawn(n.x-1, n.y) && pixels.readUnsignedInt() != currentStroke.color)
				{
					this.canvas.graphics.drawCircle(n.x-1, n.y, 1);
					
					var p1:Point = new Point(n.x-1, n.y);
					queue.push(p1);		
					currentStroke.points.push(p1);				
				}
				
				// 9. If the color of the node to the east of n is target-color, 
				// set the color of that node to replacement-color, 
				// add that node to the end of Q.
				pixels.position = n.y*width + point.x+1;
				Alert.show(pixels.readUnsignedInt()+" == " + currentStroke.color);
				if(n.x+1 <= width && !pointHasBeenDrawn(n.x+1, n.y) && pixels.readUnsignedInt() != currentStroke.color)
				{
					this.canvas.graphics.drawCircle(n.x+1, n.y, 1);
					
					var p2:Point = new Point(n.x+1, n.y);
					queue.push(p2);		
					currentStroke.points.push(p2);				
				}
				
				// 10. If the color of the node to the north of n is target-color, 
				// set the color of that node to replacement-color, 
				// add that node to the end of Q.
				pixels.position = (n.y-1)*width + point.x;
				Alert.show(pixels.readUnsignedInt()+" == " + currentStroke.color);
				if(	n.y-1 >= 0 && !pointHasBeenDrawn(n.x, n.y-1) && pixels.readUnsignedInt() != currentStroke.color)
				{
					this.canvas.graphics.drawCircle(n.x, n.y-1, 1);
					
					var p3:Point = new Point(n.x, n.y-1);
					queue.push(p3);		
					currentStroke.points.push(p3);			
				}
				
				// 11. If the color of the node to the south of n is target-color, 
				// set the color of that node to replacement-color, 
				// add that node to the end of Q.
				pixels.position = (n.y+1)*width + point.x+1;
				Alert.show(pixels.readUnsignedInt()+" == " + currentStroke.color);
				if(	n.y+1 <= height && !pointHasBeenDrawn(n.x, n.y+1) && pixels.readUnsignedInt() != currentStroke.color)
				{
					this.canvas.graphics.drawCircle(n.x, n.y+1, 1);
					
					var p4:Point = new Point(n.x, n.y+1);
					queue.push(p4);		
					currentStroke.points.push(p4);					
				}				
				
				maxloop--;
				if(maxloop == 0) break;				
			}
			
		}
		
		public function startStroke(p:Point):void
		{
			//Don't do anything when we push the
			//mouse button down
			
			currentStroke = new BrushStroke();
			currentStroke.color = fillcolor;
			currentStroke.alpha = alpha;
			
			// Flood fill - Change this to another method if not efficient enough.
			// Check wikipedia for more floodfills
			//queueBasedFloodFill(p);			
			
			var data:BitmapData = ImageSnapshot.captureBitmapData(Application.application.frame);		
			data.floodFill(p.x, p.y, fillcolor);
			
			canvas.graphics.beginBitmapFill(data);
			canvas.graphics.drawRect(10,10,data.width,data.height);
			canvas.graphics.endFill();	
		}		
		
		public function endStroke():void
		{
			strokes.push(currentStroke);
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
			brushXML.@alpha = getAlpha();	
			
			return brushXML;
		}
		
		public function setXML(xml:XML):void
		{
		}
		
	}
}