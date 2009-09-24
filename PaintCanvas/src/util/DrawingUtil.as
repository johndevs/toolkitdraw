package util
{
	import brushes.IBrush;
	import brushes.Text;
	
	import flash.geom.Point;
	
	public class DrawingUtil
	{
		private static var painter:IBrush;
		private static var controller:Controller;
		
		public static function setPainter(p:IBrush):void{
			painter = p;	
		}
		
		public static function setController(c:Controller):void{
			controller = c;
		}
		
		public static function drawPolygon(xa:Object, ya:Object, length:int):void
		{
			if(painter.getType() != Controller.POLYGON)
			{
				var color:Number = new Number(painter.getColor());
				var w:Number = new Number(painter.getWidth());	
				
				GraphicsUtil.setBrush(Controller.POLYGON);
				painter.setColor(color);
				painter.setWidth(w);					
			}		
			
			for(var i:int=0; i<length; i++)
			{
				painter.startStroke(new Point(xa[i],ya[i]));							
				painter.processPoint(new Point(xa[i],ya[i]));				
				painter.endStroke();
			}
			
			painter.endTool();
			controller.changeEvent();		
		}
		
		public static function drawSquare(x:int, y:int, width:int, height:int):void
		{			
			if(painter.getType() != Controller.RECTANGLE){
				var color:Number = new Number(painter.getColor());
				var w:Number = new Number(painter.getWidth());
				
				GraphicsUtil.setBrush(Controller.RECTANGLE);
				painter.setColor(color);
				painter.setWidth(w);				
			}				
						
			painter.startStroke(new Point(x,y));
			painter.processPoint(new Point(x,y));
			painter.processPoint(new Point(x+width,y+height));
			painter.endStroke();
			
			controller.changeEvent();
		}
		
		public static function drawText(text:String, x:int, y:int, width:int, height:int):void
		{
			if(painter.getType() != Controller.TEXT){
				var color:Number = new Number(painter.getColor());
				var w:Number = new Number(painter.getWidth());
								
				GraphicsUtil.setBrush(Controller.TEXT);
				painter.setColor(color);
				painter.setWidth(w);						
			}		
				
			Text(painter).setEditable(false);
				
			painter.startStroke(new Point(x,y));
			painter.processPoint(new Point(x,y));
			painter.processPoint(new Point(x+width, y+height));
			painter.endStroke();			
						
			Text(painter).setText(text);			
			painter.endTool();		
			Text(painter).setEditable(true);	
			
			controller.changeEvent();							
		}
		
	
		public static function drawLine(x1:int, y1:int, x2:int, y2:int):void
		{
			GraphicsUtil.setBrush(Controller.LINE);
					
			painter.startStroke(new Point(x1,y1));
			painter.processPoint(new Point(x1,y1));
			painter.processPoint(new Point(x2,y2));
			painter.endStroke();
			
			controller.changeEvent();
		}
		
		//Can only draw JPG and PNG images
		public static function drawImage(img:String, x:int, y:int, alpha:Number):void
		{
			//Image adding not supported yet!
		}	
		
		public static function drawEllipse(x1:int, y1:int, width:int, height:int):void
		{
			if(painter.getType() != Controller.ELLIPSE){
				var color:Number = new Number(painter.getColor());
				var w:Number = new Number(painter.getWidth());
				
				GraphicsUtil.setBrush(Controller.ELLIPSE);
				painter.setColor(color);
				painter.setWidth(w);				
			}				
			
			GraphicsUtil.setBrush(Controller.ELLIPSE);
			
			painter.startStroke(new Point(x1-width/2, y1-height/2));
			painter.processPoint(new Point(x1-width/2, y1-height/2));
			painter.processPoint(new Point(x1,y1));
			painter.endStroke();
			
			controller.changeEvent();
		}
		
	}
}