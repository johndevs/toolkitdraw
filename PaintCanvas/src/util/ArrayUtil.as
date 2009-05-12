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
		
		
		
	}
}