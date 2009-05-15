package brushes
{
	import flash.geom.Point;
	
	import mx.containers.Canvas;
	import mx.controls.Alert;

	public class Square implements IBrush
	{
		protected var canvas:Canvas;
		protected var framePen:Pen;
		protected var backgroundPen:Pen;
		
		protected var drawing:Boolean = false;
		protected var startPoint:Point = null;				
		protected var endPoint:Point = null;	
		
		protected var selection:Canvas;
		protected var fillColor:Number = -1;
		protected var color:Number = 0;
		protected var width:int = 1;
		
		//protected var history:Array = new Array;
		//protected var redohistory:Array = new Array;
		
		public function Square(canvas:Canvas)
		{
			this.canvas = canvas;	
			framePen = new Pen(this.canvas);
			backgroundPen = new Pen(this.canvas);		
		}

		public function processPoint(p:Point):void
		{
			if(startPoint == null) startPoint = p;
			endPoint = p;					
			
			if(startPoint.x > endPoint.x)
			{
				selection.x = endPoint.x;				
				selection.width = startPoint.x - endPoint.x;
			} 
			else
			{
				selection.x = startPoint.x;				
				selection.width = endPoint.x - startPoint.x;
			} 
			
			if(startPoint.y > endPoint.y)
			{
				selection.y = endPoint.y;
				selection.height = startPoint.y - endPoint.y;
			}
			else
			{
				selection.y = startPoint.y;
				selection.height = endPoint.y - startPoint.y;
			}		
		
		}
		
		public function startStroke():void
		{
			selection = new Canvas();
			selection.width = 1;
			selection.height = 1;
			selection.setStyle("backgroundColor","blue");
			selection.alpha = 0.5;
			
			canvas.addChildAt(selection, 0);	
			
			//redohistory = new Array();						
		}
		
		public function endStroke():void
		{
			canvas.removeChild(selection);		
			
		//	Alert.show("Fillcolor: "+fillColor);
						
			//Draw the background
			backgroundPen.setColor(fillColor);
			backgroundPen.setWidth(1);								
			backgroundPen.startStroke();			
			
			if(fillColor >= 0x0)
			{
				var width:int = selection.width;
				var height:int = selection.height;
				var x:int = selection.x;
				var y:int = selection.y;					
							
				for(var h:int=0; h<height; h++)
				{
					backgroundPen.processPoint(new Point(x, y+h));
					backgroundPen.processPoint(new Point(x+width, y+h));
				}					
			}			
			
			backgroundPen.endStroke();
			//this.history.push(background);						
			
			//Draw the border			
			//Alert.show("PenColor: "+color+", PenSize: "+width);
			framePen.setColor(color);
			framePen.setWidth(this.width);								
			framePen.startStroke();		
	
			framePen.processPoint(new Point(startPoint.x, startPoint.y));
			framePen.processPoint(new Point(endPoint.x, startPoint.y));
			framePen.processPoint(new Point(endPoint.x, endPoint.y));
			framePen.processPoint(new Point(startPoint.x, endPoint.y));
			framePen.processPoint(new Point(startPoint.x, startPoint.y));						
			
			framePen.endStroke();
			//this.history.push(frame);
									
			startPoint = null;
			endPoint = null;
		}
		
		public function redraw():void
		{						
			//for each(var p:Pen in this.history){			
			//	p.redraw();
			//}
			
			framePen.redraw();
			backgroundPen.redraw(); 
		}
		
		public function undo():Boolean
		{		
			return framePen.undo() && backgroundPen.undo();
				
			/*
			if(this.history.length >= 2)
			{
				this.redohistory.push(this.history.pop());
				this.redohistory.push(this.history.pop());
				return true;
			} else{
				return false;
			}
			*/			
		}
		
		public function redo():Boolean
		{
			return framePen.redo() && backgroundPen.redo();
			/*
			if(this.redohistory.length >= 2)
			{
				this.history.push(this.redohistory.pop());
				this.history.push(this.redohistory.pop());
				return true;
			} else{
				return false;
			}
			
			*/									
		}
		
		public function getColor():Number
		{
			return color;
		}
		
		public function setColor(color:Number):void
		{					
			this.color = color;
		}
		
		public function getWidth():Number
		{
			return width;
		}
		
		public function setWidth(width:Number):void
		{						
			this.width = width;
		}
		
		public function getType():String
		{
			return Controller.SQUARE;
		}
		
		public function setFillColor(color:Number):void
		{			
			this.fillColor = color;
		}
		
		public function getFillColor():Number
		{
			return this.fillColor;	
		}
		
		public function scale(x_ratio:Number, y_ratio:Number):void
		{
			framePen.scale(x_ratio,y_ratio);
			backgroundPen.scale(x_ratio,y_ratio);
			//for each(var p:Pen in history) p.scale(x_ratio,y_ratio);			
		}
		
		public function getCanvas():Canvas
		{
			return this.canvas;
		}
		
		public function getStrokes():Array
		{
			return framePen.getStrokes();
		}
		
		public function getBackgroundStrokes():Array
		{
			return backgroundPen.getStrokes();
		}
		
		public function getCursor():Class
		{
			return null;
		}
		
		public function endTool():void
		{
			
		}
		
	}
}