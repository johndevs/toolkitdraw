package util
{
	import flash.external.ExternalInterface;
	import flash.net.SharedObject;
	import flash.utils.ByteArray;
	
	import mx.controls.Alert;
	
	public class CacheUtil
	{		 
		private static var controller:Controller;
		
		public static function setController(c:Controller):void
		{
			controller = c;
		}
		
		// Checks if client storage is available for a certain amount of bytes
		public static function clientStoreAvailable(bytes:int):Boolean
		{
				var sharedObject:SharedObject = SharedObject.getLocal("test");
				
				try{
					if(!sharedObject.flush(bytes)){
						
						//Shared objects not allowed
						Alert.show("Please change local memory size to Unlimited in the settings!");
						
						return false;
					} 
						
					sharedObject.clear();
					sharedObject.close();
				}catch(e:Error){
					return false;
				}
				return true;			
		}
		
		public static function storeOnClient(id:String, obj:Object):Boolean
		{
			//Get the size of the object
			var bytes:ByteArray = new ByteArray();
			bytes.writeObject(obj);
			
			//Check if it can be stored
			if(!clientStoreAvailable(bytes.length)){
				return false;
			}
			
			var sharedObject:SharedObject = SharedObject.getLocal(id);
			sharedObject.clear();
			sharedObject.data.object = obj;
					
			sharedObject.data.timestamp = (new Date()).getTime();
		
			try{
				sharedObject.flush(bytes.length);			
			}catch(e:Error){
				return false;
			}			
			
			sharedObject.close();
			
			return true;
		}
		
		public static function storeOnServer(id:String, obj:Object):Boolean
		{				
			ExternalInterface.call("PaintCanvasNativeUtil.setServerCache",id, obj.toString());
			return true;
		}		
		
		public static function readFromClient(id:String):Object
		{		
			var sharedObject:SharedObject = SharedObject.getLocal(id);		
			
			// No catched object
			if(sharedObject.data.object == null) return null;						
										
			return sharedObject.data.object;			
		}
		
		
		public static function requestCacheFromServer(id:String):void
		{
			ExternalInterface.call("PaintCanvasNativeUtil.getServerCache",id);
		}
		
		public static function cacheFromServerRecieved(cache:String):void
		{									
			try{			
				var xml:XML = new XML(cache);							
				var res:Boolean = controller.loadFromCache(Controller.CACHE_SERVER, xml);	
																
				//If parsing failed then create an empty page
				controller.createPage(!res);
					
			}catch(e:Error){
				//The xml the server returned is not valid XML		
				ExternalInterface.call("PaintCanvasNativeUtil.error", e.message);	
				controller.createPage(true);
			}
		}
		
	}
}