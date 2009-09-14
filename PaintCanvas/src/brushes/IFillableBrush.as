package brushes
{
	/**
	 * Represents a brush which can be filled with a color.
	 */ 
	public interface IFillableBrush extends IBrush
	{
		function setFillColor(color:Number):void;	
		function getFillColor():Number;		
	}
}