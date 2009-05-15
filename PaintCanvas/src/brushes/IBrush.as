package brushes
{
	import flash.geom.Point;
	
	import mx.containers.Canvas;
	
	public interface IBrush
	{	
		function processPoint(p:Point):void;	//Mouse move
		function startStroke():void;			//Mouse down
		function endStroke():void;				//Mouse up
		function endTool():void;				//CTRL+Mouse down
		
		function redraw():void;
		function scale(x_ratio:Number, y_ratio:Number):void;
		
		function undo():Boolean;
		function redo():Boolean;
		
		function getColor():Number;
		function setColor(color:Number):void;
		
		function getWidth():Number;
		function setWidth(width:Number):void;
		
		function getType():String;		
		function getCanvas():Canvas;
		function getStrokes():Array;
		
		function getCursor():Class;

	}
}