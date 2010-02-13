package elements
{
	import brushes.IBrush;
	import brushes.ISelection;
	
	import flash.display.BlendMode;
	import flash.display.Sprite;
	import flash.geom.Rectangle;
	
	import mx.core.UIComponent;
	
	public class Layer extends UIComponent
	{
		private var background:Sprite;
		
		private var identifier:String = "";		
		private var backgroundColor:uint = 0;
		private var backgroundAlpha:Number = 0;
		
		private var brushStack:Array = new Array();
		private var brushUndoStack:Array = new Array();
		private var init:Boolean = false;
				
		public function Layer(identifier:String)
		{
			super();
			this.identifier = identifier;
			this.blendMode = BlendMode.LAYER;
			this.setStyle("paddingTop", 0);
			this.setStyle("paddingBottom",0); 
			this.setStyle("paddingLeft",0); 
			this.setStyle("paddingRight",0);
		}

		override protected function createChildren():void
		{
			this.background = new Sprite();
			this.background.x = 0;
			this.background.y = 0;
			this.addChild(background);
						
			validateDisplayList();
		}

		override protected function updateDisplayList(w:Number, h:Number):void
		{
			super.updateDisplayList(w,h);			
		
			this.background.width = w;
			this.background.height = h;
			this.background.graphics.clear();
			this.background.graphics.beginFill(this.backgroundColor, this.backgroundAlpha);
			this.background.graphics.drawRect(0,0,w,h);
			this.background.graphics.endFill();
											
			for each(var brush:IBrush in brushStack){								
				brush.redraw();				
			}			
					
		}
		
		public function setBackgroundColor(color:uint):void
		{
			this.backgroundColor = color;			
			invalidateDisplayList();
		}
		
		public function getBackgroundColor():uint
		{
			return this.backgroundColor;
		}
		
		public function setBackgroundAlpha(alpha:Number):void
		{
			this.backgroundAlpha = alpha;
			invalidateDisplayList();
		}
		
		public function getBackgroundAlpha():Number
		{
			return this.backgroundAlpha;
		}
		
		/**
		 * This adds a brush to a layer.
		 * Note: This will also set the brushes layer property to this layer!
		 */ 
		public function addBrush(brush:IBrush):void
		{			
			brush.setLayer(this);
			brushStack.push(brush);		
			validateDisplayList();
		}
		
		public function clear():void
		{
			while(brushStack.length > 0){
				var brush:IBrush = brushStack.pop();
				while(brush.undo());
			}				
			
			while(brushUndoStack.length > 0){
				brushUndoStack.pop();
			}
			
			// Ensure that the layer is cleaned
			while(numChildren > 0) removeChildAt(0);
			
			validateDisplayList();
		}
		
		public function getIdentifier():String{
			return this.identifier
		}
		
		public function undo():void
		{
			if(brushStack.length > 0){
				var brush:IBrush = brushStack.pop();
				
				if(!brush.undo()){
					brushUndoStack.push(brushStack.pop());
					undo();				
				} else {
					brushStack.push(brush);
				}
				
				validateDisplayList();	
			}			
		}
		
		public function redo():void
		{
			if(brushStack.length > 0){
				var brush:IBrush = brushStack.pop();
				brushStack.push(brush);
				
				if(!brush.redo()){
					if(brushUndoStack.length > 0){
						var brush2:IBrush = brushUndoStack.pop();
						brush2.redo();
						brushStack.push(brush2);
					}
				}
			}
			
			validateDisplayList();			
		}
		
		public function toXML():XML
		{
			var xml:XML = new XML("<layer></layer>");
			xml.@id = this.identifier;
			xml.@width = this.width;
			xml.@height = this.height;
			xml.@alpha = this.alpha;
			xml.@bgColor = backgroundColor;
			xml.@bgAlpha = backgroundAlpha;			
			
			// Add the brushes
			var counter:int = 0;
			for each(var brush:IBrush in brushStack)
			{	
				//Skip selection brushes
				if(brush is ISelection) continue;
				
				var brushXML:XML = brush.toXML();
				brushXML.@orderNumber = counter;
				xml.appendChild(brushXML);
				counter++;
			}
			
			return xml;
		}	
		
		public function crop(x:int, y:int, width:int, height:int):void
		{
			for(var i:int=0; i<this.numChildren; i++){
				if(getChildAt(i) is Sprite){
					var sprite:Sprite = getChildAt(i) as Sprite;
					sprite.scrollRect = new Rectangle(x,y,width,height);
				}
			}
		}
		
		public function resize(width:int, height:int):void{
			for(var i:int=0; i<this.numChildren; i++){
				background.width = width;
				background.height = height;
				if(getChildAt(i) is Sprite){
					var sprite:Sprite = getChildAt(i) as Sprite;
					sprite.width = width;
					sprite.height = height;
				}
			}
			
			this.width = width;
			this.height = height;
		}
	}
}