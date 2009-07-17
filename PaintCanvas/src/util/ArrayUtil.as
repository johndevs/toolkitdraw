package util
{
	import elements.Layer;
	
	public class ArrayUtil
	{
		public static function isNotLayerFilter(name:String):Function{
			return new function(layer:Layer, idx:int, array:Array):Boolean{
				if(layer.getName() != name) return true;				
				else return false;
			};	
		}
		
		public static function getLayerByNameFilter(name:String):Function{
			return new function(layer:Layer, idx:int, array:Array):Boolean{
				if(layer.getName() == name) return true;
				else return false;
			}
		}
		
		/**
		 * This function moves all elements in arr2 to arr1.
		 * All elements previously in arr1 are lost and arr2 is empty after this
		 * operation. Order is preserved.
		 */ 
		public static function assignArray(arr1:Array, arr2:Array):void{
			if(arr1 == null || arr2 == null) return;		
		
			while(arr1.length > 0) arr1.pop();
			while(arr2.length > 0) arr1.push(arr2.pop());
		}
		
		
		
	}
}