package util
{
	import flash.external.ExternalInterface;
	import flash.net.SharedObject;
	import flash.net.SharedObjectFlushStatus;
		
	public class CacheUtil
	{		 	
		private static var callbackFunction:Function = null;
		private static var imageCache:XML = null;
		
		
		public static function sendCacheToServer(id:String, cache:String):void
		{
			ExternalInterface.call("FlashCanvasNativeUtil.setServerCache", id, cache);
		}
		
		public static function requestCacheFromServer(id:String, callback:Function):void
		{
			callbackFunction = callback;
			ExternalInterface.call("FlashCanvasNativeUtil.getServerCache", id);
		}
		
		public static function sendCacheToClient(id:String, cache:String):Boolean
		{
			var so:SharedObject = SharedObject.getLocal(id);
			so.data.cache = cache;
			var flushStatus:String = so.flush();
			return flushStatus == SharedObjectFlushStatus.FLUSHED;
		}
		
		public static function clientCacheAvailable(id:String):Boolean
		{
			var so:SharedObject = SharedObject.getLocal(id);
			var cache:String = so.data.cache;
			return cache != null;
		}
		
		public static function requestCacheFromClient(id:String, callback:Function):void
		{
			callbackFunction = callback;			
			var so:SharedObject = SharedObject.getLocal(id);
			var cache:String = so.data.cache;
			imageCacheRecieved(cache);
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
				ExternalInterface.call("FlashCanvasNativeUtil.error", e.message);				
				if(callbackFunction != null){
					callbackFunction(null);																
					callbackFunction = null;
				}
			}
		}
	}
}