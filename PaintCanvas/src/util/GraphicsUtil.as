package util
{
	import flash.net.URLRequest;
	
	import mx.core.Application;
	import mx.managers.CursorManager;
	import mx.styles.StyleManager;
   		
	public class GraphicsUtil
	{
		[Embed("/assets/pen.png")]
   		private static var penCursor:Class;	
   		
   		private static var loader:URLLoader;
   		
		
		public static const PEN:String = "pen";	
		public static const NONE:String = "none";	
		
		public static function showCursor(cursor:String):void
   		{
   			switch(cursor)
   			{
   				case PEN:	CursorManager.setCursor(penCursor, CursorManagerPriority.HIGH, 3, 2); break;
   				default: CursorManager.removeAllCursors();
   			}    		
   		}
   		

   		
	}
}