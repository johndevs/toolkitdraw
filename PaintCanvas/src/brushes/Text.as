package brushes
{
	import flash.display.BitmapData;
	import flash.geom.Matrix;
	import flash.geom.Point;
	
	import mx.controls.TextArea;
	import mx.core.ScrollPolicy;
	import mx.graphics.ImageSnapshot;
	import mx.controls.Alert;
	
	import util.GraphicsUtil;
	import elements.Layer;

	public class Text extends FilledBrush implements IBrush
	{
		private var editing:Boolean = false;
		private var text:TextArea;
		private var fontName:String = "";
		private var sizing:Boolean = false;
		
		public function Text()
		{
			super();	
					
			size = 8;
		}
		
		override public function redraw():void
		{
			// Prevent redraw when editing text
			// This will happen when the layers UpdateDisplayList is triggered
			// after the text box is added to the layer
			if(editing || sizing)
				return;
			
			var savedStrokes:Array = strokes;
			strokes = new Array();
			
			for each(var stroke:TextBrushStroke in savedStrokes)
			{
				mouseDown(stroke.points[0]);
				mouseMove(stroke.points[1]);
				mouseUp(stroke.points[1]);
				
				setText(stroke.text);
				setFontName(stroke.fontName);
				setSize(stroke.size);
				setColor(stroke.color);
				setAlpha(stroke.alpha);				
				setFillColor(stroke.fillColor);
				setFillAlpha(stroke.fillAlpha);
								
				finalize();
			}
		}
		
		public function mouseDown(p:Point):void
		{
			if(editing) return;
			
			sizing = true;
			currentStroke = new TextBrushStroke();
			currentStroke.points.push(p);
			layer.addChild(currentStroke.sprite);
		}
		
		public function mouseMove(p:Point):void
		{
			if(editing || currentStroke == null) return;
			
			var cp:Point = currentStroke.points[0];
			var w:int = Math.round(p.x - cp.x);
			var h:int = Math.round(p.y - cp.y);
			
			currentStroke.sprite.graphics.clear();
			currentStroke.sprite.graphics.beginFill(fillColor, fillAlpha);
			currentStroke.sprite.graphics.lineStyle(1,0xCCCCCC);
			currentStroke.sprite.graphics.drawRect(cp.x, cp.y, w, h);
			currentStroke.sprite.graphics.endFill();
		}
		
		public function mouseUp(p:Point):void
		{
			if(editing || currentStroke == null) return;
			
			var cp:Point = currentStroke.points[0];
			var w:int = Math.round(p.x - cp.x);
			var h:int = Math.round(p.y - cp.y);
			
			currentStroke.sprite.graphics.clear();
						
			currentStroke.points.push(p);
			
			text = new TextArea();
			text.setStyle("backgroundAlpha", fillAlpha);	
			text.setStyle("backgroundColor", fillColor);
			text.setStyle("color", color);
			text.setStyle("fontSize", size);
			text.setStyle("fontFamily", fontName);
			text.alpha = alpha;
			text.text = "";
			text.x = cp.x;
			text.y = cp.y;
			text.width = w;			
			text.height = h;		
			text.horizontalScrollPolicy = ScrollPolicy.OFF;
			text.verticalScrollPolicy = ScrollPolicy.OFF;
			text.editable = true;
			text.cacheAsBitmap = true;	
			
			// This will add the text box to the layer
			// Note: This will trigger a updateDisplayList event
			layer.addChild(text);		
			
			editing = true;			
		}
		
		public function finalize():void
		{
			layer.setFocus();
			text.setStyle("borderStyle","none");
			text.validateNow();
			
			(currentStroke as TextBrushStroke).text = new String(text.text);
			(currentStroke as TextBrushStroke).fontName = fontName;
									
			var bd:BitmapData = ImageSnapshot.captureBitmapData(text);						
			var moveMatrix:Matrix = new Matrix();
			moveMatrix.translate(text.x+1, text.y+1);
			
			currentStroke.sprite.graphics.beginBitmapFill(bd, moveMatrix);
			currentStroke.sprite.graphics.drawRect(text.x+1, text.y+1, text.width, text.height);
			currentStroke.sprite.graphics.endFill();
			
			layer.removeChild(text);
			
			currentStroke.color = color;
			currentStroke.size = size;
			(currentStroke as TextBrushStroke).fillColor = fillColor;
			(currentStroke as TextBrushStroke).fillAlpha = fillAlpha;
			
			strokes.push(currentStroke);
			currentStroke = null;
			editing = false;	
			sizing = false;					
		}
		
		override public function getType():String
		{
			return GraphicsUtil.BRUSH_TEXT;
		}	
		
		override public function setSize(size:uint):void
		{
			this.size = size;
			
			if(this.text != null) 
				this.text.setStyle("fontSize", size);
		}	
		
		override public function setFillColor(color:uint):void
		{
			this.fillColor = color;
						
			if(this.text != null){
				this.text.setStyle("backgroundColor", color);
				this.text.validateNow();
			}
		}
		
		override public function setFillAlpha(alpha:Number):void
		{
			this.fillAlpha = alpha;
			
			if(this.text != null){
				this.text.setStyle("backgroundAlpha", alpha);	
				this.text.validateNow();
			}	
		}
				
		public function setText(text:String):void
		{			
			if(this.text != null){	
				this.text.text = text;
				this.text.validateNow();
			}
		}
				
		override public function setColor(color:uint):void
		{
			this.color = color;	
			
			if(this.text != null) 
				this.text.setStyle("color", color);
		}
		
		public function setFontName(font:String):void
		{
			this.fontName = font;
			
			if(this.text != null)
				this.text.setStyle("fontFamily", fontName);
		}
		
		override public function toXML():XML
		{
			var brushXML:XML = new XML("<brush></brush>");	
			brushXML.@type = getType();
			brushXML.@color = getColor();
			brushXML.@size = getSize();	
			brushXML.@alpha = getAlpha();
			brushXML.@fillalpha = getFillAlpha();
			brushXML.@fillcolor = getFillColor();
						
			for each(var stroke:TextBrushStroke in strokes){
				var strokeXML:XML = new XML("<stroke></stroke>");
				strokeXML.@text = stroke.text;
				strokeXML.@fontName = stroke.fontName;
				strokeXML.@color = stroke.color;
				strokeXML.@alpha = stroke.alpha;
				strokeXML.@size = stroke.size;
				strokeXML.@fillcolor = stroke.fillColor;
				strokeXML.@fillalpha = stroke.fillAlpha;
				strokeXML.@orderNumber = strokes.indexOf(stroke);
								
				var points:String = "";
				for each(var p:Point in stroke.points)
					points += p.x+","+p.y+";";
					
				strokeXML.points = points;							
				brushXML.appendChild(strokeXML);	
			}			
			
			return brushXML;
		}
		
		override public function fromXML(brushXML:XML):void
		{
			if(!brushXML.hasOwnProperty("@type")) return;
			var type:String = brushXML.@type;
			if(type != getType()) return;
			
			if(brushXML.hasOwnProperty("@color"))
				this.setColor(brushXML.@color);
			if(brushXML.hasOwnProperty("@size"))
				this.setSize(brushXML.@size);
			if(brushXML.hasOwnProperty("@alpha"))
				this.setAlpha(brushXML.@alpha);	
			if(brushXML.hasOwnProperty("@fillalpha"))
				this.setFillAlpha(brushXML.@fillalpha);
			if(brushXML.hasOwnProperty("@fillcolor"))
				this.setFillColor(brushXML.@fillcolor);
						
			if(!brushXML.hasOwnProperty("stroke")) return;
			for each(var strokeXML:XML in brushXML.stroke)
			{
				currentStroke = new TextBrushStroke();
				
				if(strokeXML.hasOwnProperty("@color"))
					currentStroke.color = strokeXML.@color;
				if(strokeXML.hasOwnProperty("@size"))
					currentStroke.size = strokeXML.@size;
				if(strokeXML.hasOwnProperty("@alpha"))
					currentStroke.alpha = strokeXML.@alpha;
				if(strokeXML.hasOwnProperty("@fillalpha"))
					(currentStroke as TextBrushStroke).fillAlpha = strokeXML.@fillalpha;
				if(strokeXML.hasOwnProperty("@fillcolor"))
					(currentStroke as TextBrushStroke).fillColor = strokeXML.@fillcolor;
				if(strokeXML.hasOwnProperty("@text"))
					(currentStroke as TextBrushStroke).text = strokeXML.@text;
				if(strokeXML.hasOwnProperty("@fontName"))
					(currentStroke as TextBrushStroke).fontName = strokeXML.@fontName;
														
				if(!strokeXML.hasOwnProperty("points")) continue;
				var pointsStr:String = strokeXML.points;
				var points:Array = pointsStr.split(";");
			
				for each(var point:String in points)
				{
					var coords:Array = point.split(",");
					if(coords.length != 2) continue;
					currentStroke.points.push(new Point(new Number(coords[0]),new Number(coords[1])));
				}
				
				strokes.push(currentStroke);
			}			
		}
		
		override public function isFinishable():Boolean
		{
			return true;
		}		
	}
	
	
}