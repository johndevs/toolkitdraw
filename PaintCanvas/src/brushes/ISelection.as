package brushes
{
	import flash.geom.Point;
	
	public interface ISelection
	{
		function inSelection(p:Point):Boolean;
		function removeSelection():void;
	}
}