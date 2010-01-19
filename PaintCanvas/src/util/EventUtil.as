package util
{
	import flash.external.ExternalInterface;
	
	public class EventUtil
	{
		public static function fireBrushStartedEvent(clientID:String):void{
			ExternalInterface.call("FlashCanvasNativeUtil.brushStartEvent", clientID);			
		}
		
		public static function fireBrushEnded(clientID:String):void{
			ExternalInterface.call("FlashCanvasNativeUtil.brushEndEvent", clientID);			
		}
		
		public static function fireClickEvent(clientID:String, x:int, y:int):void{
			ExternalInterface.call("FlashCanvasNativeUtil.clickEvent", clientID, x , y);				
		}

	}
}