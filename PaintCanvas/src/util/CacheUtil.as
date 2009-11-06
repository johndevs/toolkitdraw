package util
{
	import flash.external.ExternalInterface;
		
	public class CacheUtil
	{		 	
		private static var callbackFunction:Function = null;
		private static var imageCache:XML = null;
		
		
		public static function sendCacheToServer(id:String, cache:String):void
		{
			ExternalInterface.call("PaintCanvasNativeUtil.setServerCache", id, cache);
		}
		
		public static function requestCacheFromServer(id:String, callback:Function):void
		{
			callbackFunction = callback;
			ExternalInterface.call("PaintCanvasNativeUtil.getServerCache", id);
		}
		
		public static function imageCacheRecieved(cache:String):void
		{
			try{											
				imageCache = new XML(cache);						
				if(callbackFunction != null){
					callbackFunction(imageCache);																
					callbackFunction = null;
				}
			}catch(e:Error){
				//The xml the server returned is not valid XML		
				ExternalInterface.call("PaintCanvasNativeUtil.error", e.message);				
				if(callbackFunction != null){
					callbackFunction(null);																
					callbackFunction = null;
				}
			}
		}
	}
}