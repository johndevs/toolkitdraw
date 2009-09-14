package brushes
{
	import flash.geom.Point;
	
	import mx.containers.Canvas;
	
	public interface IBrush
	{	
		function processPoint(p:Point):void;						// Mouse move
		function startStroke():void;								// Mouse down
		function endStroke():void;									// Mouse up
		function endTool():void;									// CTRL+Mouse down
		
		function redraw():void;										// Redraw the strokes onto the canvas
		function scale(x_ratio:Number, y_ratio:Number):void;		// Scale the strokes
		
		function undo():Boolean;									// Undo last stroke
		function redo():Boolean;									// Redo last stroke
		
		function getColor():Number;									// Get color in use
		function setColor(color:Number):void;						// Set color in use
		
		function getWidth():Number;									// Get the width of the tool in pixels
		function setWidth(width:Number):void;						// Set the width of the tool in pixels
		
		function getAlpha():Number;									// Get the alpha channel value
		function setAlpha(alpha:Number):void;						// Set the alpha channel value (0-1)
		
		function getType():String;									// Returns id of the tool
		function getCanvas():Canvas;								// Returns the canvas the tools draws on
		function getStrokes():Array;								// Get the drawn strokes
		
		function getCursor():Class;									// Deprecated.
		
		function getXML():XML;										// Returns the xml representation of the drawn strokes
		function setXML(xml:XML):void;								// Generates strokes of the given xml.

	}
}