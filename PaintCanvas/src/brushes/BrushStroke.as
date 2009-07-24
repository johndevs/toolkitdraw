package brushes
{
	import flash.geom.Point;
	
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