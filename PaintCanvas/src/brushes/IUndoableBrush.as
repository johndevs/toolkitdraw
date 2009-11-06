package brushes
{
	public interface IUndoableBrush
	{
		function undo():Boolean;
		function redo():Boolean;
	}
}