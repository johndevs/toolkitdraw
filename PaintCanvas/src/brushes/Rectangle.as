package brushes
{
	import flash.geom.Point;
	
	import mx.containers.Canvas;

	public class Rectangle implements IFillableBrush
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
		
		public function Rectangle(canvas:Canvas)
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
			return Controller.RECTANGLE;
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
		
		public function getAlpha():Number{
			return 0;	
		}
		
		public function setAlpha(alpha:Number):void
		{
		}
		
		public function getXML():XML
		{
			var brushXML:XML = new XML("<brush></brush>");	
			brushXML.@type = getType();
			brushXML.@color = getColor();
			brushXML.@width = getWidth();	
			brushXML.@fill = getFillColor();
			
			//generate brush strokes						
			for each(var stroke:BrushStroke in getStrokes())
			{
				var strokeXML:XML = new XML("<stroke></stroke>");
				strokeXML.@color = stroke.color;
				strokeXML.@fill = stroke.fillcolor;
				strokeXML.@alpha = stroke.alpha;
				strokeXML.@width = stroke.width;
				strokeXML.@orderNumber = getStrokes().indexOf(stroke);
				
				//Generate point list
				var points:String = "";
				for each(var p:Point in stroke.points)
					points += p.x+","+p.y+";";
				
				strokeXML.points = points;							
				brushXML.appendChild(strokeXML);							
			}		
			
			//Generate background strokes
			for each(var backgroundStroke:BrushStroke in getBackgroundStrokes())
			{
				var bstrokeXML:XML = new XML("<backgroundstroke></backgroundstroke>");
				bstrokeXML.@color = backgroundStroke.color;
				bstrokeXML.@fill = backgroundStroke.fillcolor;
				bstrokeXML.@alpha = backgroundStroke.alpha;
				bstrokeXML.@width = backgroundStroke.width;
				
				//Generate point list
				var bpoints:String = "";
				for each(var bp:Point in backgroundStroke.points)
					bpoints += bp.x+","+bp.y+";";
				
				bstrokeXML.points = bpoints;				
				brushXML.appendChild(bstrokeXML);
			}					
			
			return brushXML;
		}
		
		// TODO Processing of background points
		public function setXML(brushXML:XML):void
		{
			if(brushXML.hasOwnProperty("@color")) setColor(brushXML.@color);
			if(brushXML.hasOwnProperty("@width")) setWidth(brushXML.@width);
			if(brushXML.hasOwnProperty("@fill")) setFillColor(brushXML.@fill);
			
			if(!brushXML.hasOwnProperty("stroke")) return;
								
			for each(var strokeXML:XML in brushXML.stroke)
			{				
				if(strokeXML == null) continue;
				
				if(strokeXML.hasOwnProperty("@color")) setColor(strokeXML.@color);
				if(strokeXML.hasOwnProperty("@alpha")) setAlpha(strokeXML.@alpha);
				if(strokeXML.hasOwnProperty("@width")) setWidth(strokeXML.@width);
				if(strokeXML.hasOwnProperty("@fill"))  setFillColor(strokeXML.@fill);
													
				startStroke();						
					
				if(!strokeXML.hasOwnProperty("points")) continue;								
				for each(var pointXML:String in strokeXML.points)
				{										
					if(pointXML == null) continue;
								
					// Point format is x,y;x,y;..
					var points:Array = pointXML.split(";");
					for each(var point:String in points)
					{
						var coord:Array = point.split(",");
						
						if(coord.length != 2) continue;
						
						processPoint(new Point(coord[0], coord[1]));
					}							
				}		
				
				endStroke();						
			}				
		}		
		
	}
}