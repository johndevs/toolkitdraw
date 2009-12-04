package util
{
	import flash.external.ExternalInterface;
	
	public class EventUtil
	{
		public static function fireBrushStartedEvent(clientID:String):void{
			ExternalInterface.call("PaintCanvasNativeUtil.brushStartEvent", clientID);			
		}
		
		public static function fireBrushEnded(clientID:String):void{
			ExternalInterface.call("PaintCanvasNativeUtil.brushEndEvent", clientID);			
		}
		
		public static function fireClickEvent(clientID:String, x:int, y:int):void{
			ExternalInterface.call("PaintCanvasNativeUtil.clickEvent", clientID, x , y);				
		}

	}
}