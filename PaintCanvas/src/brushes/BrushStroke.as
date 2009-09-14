package brushes
{
	import flash.geom.Point;
	
	/**
	 * This class represents a single brush stroke of a tool.
	 * How the values are used is up to the tool.
	 */ 
	public class BrushStroke
	{
		//Default brush properties
		public var color:Number = 0x0;
		public var fillcolor:Number = -1;
		public var alpha:Number = 1.0;
		public var width:Number = 1.0;
		public var points:Array = new Array;
				
		//Text based properties
		public var text:String = "";
		public var fontName:String = "";
	
		//Other date objects
		public var data:Object = new Object();
	}
}