package util
{
	import flash.net.SharedObject;
	import flash.utils.ByteArray;
	
	import mx.controls.Alert;
	
	public class ClientStoreUtil
	{		 
		// Checks if client storage is available for a certain amount of bytes
		public static function clientStoreAvailable(bytes:int):Boolean
		{
				var sharedObject:SharedObject = SharedObject.getLocal("test");
				if(!sharedObject.flush(bytes)){
					
					//Shared objects not allowed
					Alert.show("Please change local memory size to Unlimited in the settings!");
					
					return false;
				} 
					
				sharedObject.clear();
				sharedObject.close();
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
			sharedObject.flush(bytes.length);			
			sharedObject.close();
			
			return true;
		}
		
		
		public static function readFromClient(id:String):Object
		{		
			var sharedObject:SharedObject = SharedObject.getLocal(id);				
			return sharedObject.data.object;			
		}
		
	}
}