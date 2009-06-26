package brushes
{
	import flash.display.BitmapData;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import mx.containers.Canvas;
	import mx.controls.Alert;
	import mx.controls.TextArea;
	import mx.core.ScrollPolicy;
	import mx.graphics.ImageSnapshot;

	public class Text implements IFillableBrush
	{
		protected var canvas:Canvas;
		protected var selection:Canvas;
		protected var text:TextArea;
		
		protected var strokes:Array = new Array;
		protected var redo_history:Array = new Array();
								
		protected var current_color:Number = 0x0;
		protected var current_width:Number = 12;
		protected var current_text:String = "";		
		protected var current_fillColor:Number = 0x0;
		protected var current_fillAlpha:Number = 0;
		
		protected var startPoint:Point = null;				
		protected var endPoint:Point = null;	
		protected var editing:Boolean = false;
		
		private var data:BitmapData;
		private var x:Number;
		private var y:Number;
		private var w:Number;
		private var h:Number;
		
		public function Text(canvas:Canvas)
		{
			this.canvas = canvas;
		}
	
		public function processPoint(p:Point):void
		{
			if(!this.editing || selection.numChildren > 0) return; 
			
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
			if(editing) return;
			
			editing = true;			
			
			selection = new Canvas();
			selection.width = 1;
			selection.height = 1;
			selection.setStyle("backgroundColor","blue");
			selection.alpha = 0.5;
			selection.cacheAsBitmap = true;
			
			canvas.addChildAt(selection, 0);	
		}
		
		public function endStroke():void
		{				
			if(!editing || text != null) return;
					
			text = new TextArea();
			text.setStyle("fontFamily", "Times");	
			text.setStyle("backgroundAlpha", this.current_fillAlpha);	
			text.setStyle("backgroundColor", this.current_fillColor);
			text.setStyle("color",this.current_color);
			text.setStyle("fontSize",this.current_width);
			text.text = "";
			text.x = selection.x;
			text.y = selection.y;
			text.width = selection.width;			
			text.height = selection.height;		
			text.horizontalScrollPolicy = ScrollPolicy.OFF;
			text.verticalScrollPolicy = ScrollPolicy.OFF;
			text.editable = true;
			text.cacheAsBitmap = true;						
			
			this.x = new int(selection.x);
			this.y = new int(selection.y);
			this.w = new int(selection.width);
			this.h = new int(selection.height);			
		
			this.canvas.removeChild(selection);
			this.canvas.addChild(text);						
			text.setFocus();
			
			startPoint = null;
			endPoint = null;				
			selection = null;							
		}
		
		public function endTool():void
		{
			this.editing = false;					
			
			//Get text			
			this.current_text = new String(this.text.text);						
								
			//Capture the whole frame
			var d:BitmapData = ImageSnapshot.captureBitmapData(text);
			
			//Remove borders
			var d2:BitmapData = new BitmapData(d.width-2, d.height-2,true);		
			d2.copyPixels(d,new Rectangle(1,1,d.width-2,d.height-2),new Point(0,0));
																								
			//Paint the screenshot to the canvas
			var m2:Matrix = new Matrix();
			m2.translate(x,y);
			this.canvas.graphics.lineStyle(1,0xFF0000,0);		
			this.canvas.graphics.beginBitmapFill(d2,m2,false,false);
			this.canvas.graphics.drawRect(x, y, d2.width, d2.height);
			this.canvas.graphics.endFill();
			
			this.canvas.removeChild(this.text);			
			this.text = null;			
			
			//Create the stroke
			var stroke:BrushStroke = new BrushStroke();
			
			stroke.points.push(new Point(x,y));
			stroke.points.push(new Point(x+d2.width, y+d2.height));			
			stroke.color = this.current_color;
			stroke.width = this.current_width;
			stroke.text = this.current_text;
			stroke.data = d2;
			
			strokes.push(stroke);			
		}
		
		public function redraw():void
		{
			for each(var stroke:BrushStroke in strokes)
			{
				var sx:Number = Point(stroke.points[0]).x;
				var sy:Number = Point(stroke.points[0]).y;			
				var d:BitmapData = BitmapData(stroke.data);
				
				var matrix:Matrix = new Matrix();
				matrix.translate(sx,sy);
		
				this.canvas.graphics.beginBitmapFill(d,matrix,false,false);
				this.canvas.graphics.drawRect(sx, sy, d.width, d.height);
				this.canvas.graphics.endFill();			
			}			
		}
		
		public function scale(x_ratio:Number, y_ratio:Number):void
		{
			//TODO
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
		
		public function getColor():Number
		{
			return current_color;
		}
		
		public function setColor(color:Number):void
		{
			this.current_color = color;
			
			if(this.text != null) 
				this.text.setStyle("color",this.current_color);
		}
		
		public function getWidth():Number
		{
			return current_width;
		}
		
		public function setWidth(width:Number):void
		{
			this.current_width = width;
			
			if(this.text != null) 
				this.text.setStyle("fontSize",this.current_width);
		}		
		
		public function getType():String
		{
			return Controller.TEXT;
		}
		
		public function getCanvas():Canvas
		{
			return this.canvas;
		}
		
		public function getStrokes():Array
		{
			return this.strokes;
		}
		
		public function getCursor():Class
		{
			return null;
		}
		
		public function setFillColor(color:Number):void
		{			
			this.current_fillColor = color;
			
			if(this.text != null)
				this.text.setStyle("backgroundColor",this.current_fillColor);
		}
		
		public function getFillColor():Number
		{
			return this.current_fillColor;	
		}
		
		public function getAlpha():Number{
			return this.current_fillAlpha;	
		}
		
		public function setAlpha(alpha:Number):void
		{
			this.current_fillAlpha = alpha;
			
			if(this.text != null)
				this.text.setStyle("backgroundAlpha", this.current_fillAlpha);	
		}
		
		public function setText(text:String):void
		{
			if(this.text != null){
				Alert.show(text);
				this.text.text = text;
			}
		}
	}
}