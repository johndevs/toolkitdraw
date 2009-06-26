package brushes
{
	public interface IFillableBrush extends IBrush
	{
		function setFillColor(color:Number):void;	
		function getFillColor():Number;		
	}
}