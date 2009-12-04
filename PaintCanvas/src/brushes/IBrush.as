package brushes
{
	import elements.Layer;
	
	import flash.geom.Point;
	
	public interface IBrush
	{
		function redraw():void;
		function mouseDown(p:Point):void;
		function mouseMove(p:Point):void;
		function mouseUp(p:Point):void;
		function finalize():void;
		function getType():String;
		function isFinishable():Boolean;
		
		function setLayer(layer:Layer):void;
		function getLayer():Layer;
		
		function setColor(color:uint):void;
		function getColor():uint;
		
		function setSize(size:uint):void;
		function getSize():uint;
		
		function setAlpha(alpha:Number):void;
		function getAlpha():Number;
		
		function undo():Boolean;
		function redo():Boolean;
		
		function toXML():XML;
		function fromXML(xml:XML):void;
	}
}