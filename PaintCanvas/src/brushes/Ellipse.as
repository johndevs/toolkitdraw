package brushes
{
	import flash.geom.Point;
	
	import mx.containers.Canvas;

	/**
	 * This is the Ellipse tool
	 */ 
	public class Ellipse implements IFillableBrush
	{
		protected var canvas:Canvas;	
		
		protected var drawing:Boolean = false;
		protected var topLeft:Point = null;				
		protected var bottomRight:Point = null;	
		
		protected var selection:Canvas;
		protected var strokes:Array = new Array;
		
		protected var current_stroke:BrushStroke;
		protected var current_color:Number = 0x0;
		protected var current_width:Number = 1;
		protected var current_filled_color:Number = -1;
				
		protected var redo_history:Array = new Array;
		
		/**
		 * Constructor
		 */ 
		public function Ellipse(canvas:Canvas)
		{
			this.canvas = canvas;	
		}

		/**
		 * Process a point.
		 * This is called the the mouse moves 
		 */ 
		public function processPoint(p:Point):void
		{
			if(topLeft == null) topLeft = p;
			bottomRight = p;		
			
			var width:int = topLeft.x-bottomRight.x;
			var height:int = topLeft.y-bottomRight.y;
						
			selection.graphics.clear();
			selection.graphics.beginFill(0x0000FF,0.5);
			
			//Sector 4
			if(width <= 0 && height <= 0)
				selection.graphics.drawEllipse(topLeft.x,topLeft.y,Math.abs(width),Math.abs(height));
			
			//Sector 3
			else if(width >= 0 && height <= 0)
				selection.graphics.drawEllipse(bottomRight.x,topLeft.y,width,Math.abs(height));
				
			//Sector 2
			else if(width <= 0 && height >= 0)
				selection.graphics.drawEllipse(topLeft.x,bottomRight.y,Math.abs(width),height);
			
			//Sector 1
			else
				selection.graphics.drawEllipse(bottomRight.x,bottomRight.y,width,height);
			
				
			selection.graphics.endFill();				
		}
		
		/**
		 * Start a new stroke.
		 * This is called when the mouse button is pressed down inside the image frame
		 */ 
		public function startStroke():void
		{
			//Create a selection on the image
			selection = new Canvas();
			selection.width = 1;
			selection.height = 1;			
			selection.alpha = 0.5;
			
			canvas.addChildAt(selection,0);						
			
			//Create a new stroke
			current_stroke = new BrushStroke();	
			current_stroke.color = current_color;
			current_stroke.width = current_width;
			current_stroke.fillcolor = current_filled_color;
			
			//Reset redo history
			redo_history = new Array;
		}
		
		/**
		 * This ends the currently drawn stroke.
		 * This is called when the mouse button is de-pressed.
		 */ 
		public function endStroke():void
		{
			if(current_stroke != null)
			{
				current_stroke.points.push(topLeft);
				current_stroke.points.push(bottomRight);
				strokes.push(current_stroke);
				
				//Get the width and height of the ellipse				
				var width:int = topLeft.x-bottomRight.x;
				var height:int = topLeft.y-bottomRight.y;
				
				canvas.graphics.lineStyle(current_stroke.width,current_stroke.color,current_stroke.alpha);
				
				if(current_stroke.fillcolor != -1)
					canvas.graphics.beginFill(current_stroke.fillcolor);
				
				//Sector 4
				if(width <= 0 && height <= 0)					
						canvas.graphics.drawEllipse(topLeft.x,topLeft.y,Math.abs(width),Math.abs(height));							
				
				//Sector 3
				else if(width >= 0 && height <= 0)
					canvas.graphics.drawEllipse(bottomRight.x,topLeft.y,width,Math.abs(height));
					
				//Sector 2
				else if(width <= 0 && height >= 0)
					canvas.graphics.drawEllipse(topLeft.x,bottomRight.y,Math.abs(width),height);
				
				//Sector 1
				else
					canvas.graphics.drawEllipse(bottomRight.x,bottomRight.y,width,height);				
					
					
				if(current_stroke.fillcolor != -1)
					canvas.graphics.endFill();	
					
			}		
															
			canvas.removeChild(selection);			
			current_stroke = null;
			
			topLeft = null;
			bottomRight = null;
		}
		
		/**
		 * Redraw the strokes.
		 */ 
		public function redraw():void
		{			
			for each(var stroke:BrushStroke in strokes)
			{					
				current_stroke = stroke;		
				var topLeft:Point = current_stroke.points[0];
				var bottomRight:Point = current_stroke.points[1];
				
				var width:int = topLeft.x-bottomRight.x;
				var height:int = topLeft.y-bottomRight.y;
				
				canvas.graphics.lineStyle(current_stroke.width,current_stroke.color,current_stroke.alpha);
				
				if(current_stroke.fillcolor != -1)
					canvas.graphics.beginFill(current_stroke.fillcolor);
				
				//Sector 4
				if(width <= 0 && height <= 0)					
						canvas.graphics.drawEllipse(topLeft.x,topLeft.y,Math.abs(width),Math.abs(height));							
				
				//Sector 3
				else if(width >= 0 && height <= 0)
					canvas.graphics.drawEllipse(bottomRight.x,topLeft.y,width,Math.abs(height));
					
				//Sector 2
				else if(width <= 0 && height >= 0)
					canvas.graphics.drawEllipse(topLeft.x,bottomRight.y,Math.abs(width),height);
				
				//Sector 1
				else
					canvas.graphics.drawEllipse(bottomRight.x,bottomRight.y,width,height);				
					
					
				if(current_stroke.fillcolor != -1)
					canvas.graphics.endFill();					
				
			}				
			current_stroke = null;
		}
		
		/**
		 * Undo last stroke. 
		 * Returns true if successful.
		 */ 
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
		
		/**
		 * Redo last undone stroke.
		 * Returns true if successful
		 */ 
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
		
		/**
		 * Get current color
		 */ 
		public function getColor():Number
		{
			return current_color;
		}
		
		/**
		 * Set current color
		 */ 
		public function setColor(color:Number):void
		{
			current_color = color;
		}
		
		/**
		 * Get current width
		 */ 
		public function getWidth():Number
		{			
			return current_width;
		}
		
		/**
		 * Set current width of the pencil
		 */ 
		public function setWidth(width:Number):void
		{
			current_width = width;
		}
		
		/**
		 * Get the type of this tool. This is an ellipse.
		 */ 
		public function getType():String
		{
			return Controller.ELLIPSE;
		}
		
		/**
		 * Set the color which the ellipse should be filled with.
		 * If color is null then the ellipse will be transparent.
		 */ 
		public function setFillColor(color:Number):void
		{					
			this.current_filled_color = color;
		}
		
		/**
		 * Get the fill color. A null value represents transparency.
		 */ 
		public function getFillColor():Number
		{
			return this.current_filled_color;	
		}
		
		/**
		 * Scale ALL brushes. This is unreversable.
		 */ 
		public function scale(x_ratio:Number, y_ratio:Number):void
		{
			for each(var stroke:BrushStroke in strokes)
			{
				for each(var p:Point in stroke.points)
				{
					p.x = p.x*x_ratio;
					p.y = p.y*y_ratio;
				}
			}
		}
		
		/**
		 * Return the canvas used by the tool.
		 */ 
		public function getCanvas():Canvas
		{
			return this.canvas;
		}
		
		/**
		 * Returns an array of the strokes done
		 */ 
		public function getStrokes():Array
		{
			return this.strokes;
		}
		
		/**
		 * Get the cursor. NOT USED.
		 */ 
		public function getCursor():Class
		{
			return null;
		}
		
		/**
		 * End the tool. NOT USED.
		 */ 
		public function endTool():void
		{
			
		}
		
		/**
		 * Get the alpha channel value of the tool
		 */ 
		public function getAlpha():Number{
			return 0;	
		}
		
		/**
		 * Set the alpha channel value.
		 * Values between 0-1 are used.
		 */ 
		public function setAlpha(alpha:Number):void
		{
		}
		
		/**
		 * Returns an XML representation of the tools strokes.
		 */ 
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
						
			return brushXML;
		}
	
		/**
		 * Creates strokes out of an XML representation.
		 * Note: This removes all previous strokes.
		 */ 
		public function setXML(brushXML:XML):void
		{
			if(!brushXML.hasOwnProperty("@type")) return;
										
			var type:String = brushXML.@type;
			if(type != getType()) return;
																				
			if(!brushXML.hasOwnProperty("stroke")) return;
			for each(var strokeXML:XML in brushXML.stroke)
			{
				if(strokeXML.hasOwnProperty("@color"))
					this.setColor(strokeXML.@color);
				if(strokeXML.hasOwnProperty("@width"))
					this.setWidth(strokeXML.@width);
				if(strokeXML.hasOwnProperty("@alpha"))
					this.setAlpha(strokeXML.@alpha);
				if(strokeXML.hasOwnProperty("@fill"))
					this.setFillColor(strokeXML.@fill);
										
				this.startStroke();
				if(!strokeXML.hasOwnProperty("points")) continue;
				var pointsStr:String = strokeXML.points;
				var points:Array = pointsStr.split(";");
			
				for each(var point:String in points)
				{
					var coords:Array = point.split(",");
					if(coords.length != 2) continue;
					
					this.processPoint(new Point(new Number(coords[0]),new Number(coords[1])));
				}
				
				this.endStroke();
			}
			this.endTool();
			
			if(brushXML.hasOwnProperty("@color"))
				this.setColor(brushXML.@color);
			if(brushXML.hasOwnProperty("@width"))
				this.setWidth(brushXML.@width);
			if(brushXML.hasOwnProperty("@alpha"))
				this.setAlpha(brushXML.@alpha);	
			if(brushXML.hasOwnProperty("@fill"))
				this.setFillColor(brushXML.@fill);
		}				
	}
}