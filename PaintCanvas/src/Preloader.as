package
{
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.events.ProgressEvent;
	
	import mx.events.FlexEvent;
	import mx.preloaders.DownloadProgressBar;

	public class Preloader extends DownloadProgressBar
	{
		public function Preloader():void
		{
			super();
			
			// Add an event listener so that we can start drawing once we know we're on the stage
  			addEventListener(Event.ADDED_TO_STAGE, onAddedToStage);  		
		}
		
		//The preloader was added to the stage
		private function onAddedToStage(event:Event):void 
		{ 		  		
		 
		}
		
		// We've made progress... 
		private function progressEventHandler(event:ProgressEvent):void 
		{
  			
		}
		
		// Load complete, fire the Event.COMPLETE event and the framework will carry on from there
		private function initCompleteEventHandler(event:FlexEvent):void 
		{
  			dispatchEvent(new Event(Event.COMPLETE));
		}
		
		// Called by the framework so we can add our event listeners to the various events we're interested in
		override public function set preloader(value:Sprite):void 
		{
			  value.addEventListener(ProgressEvent.PROGRESS, progressEventHandler);
			  value.addEventListener(FlexEvent.INIT_COMPLETE, initCompleteEventHandler);        		 
		}		
	}
}