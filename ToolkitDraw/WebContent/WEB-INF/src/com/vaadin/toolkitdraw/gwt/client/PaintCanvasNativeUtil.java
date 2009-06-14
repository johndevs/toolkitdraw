package com.vaadin.toolkitdraw.gwt.client;

public class PaintCanvasNativeUtil {
	
	/**
	 * Undo last brush stroke
	 * @param id
	 * 		The id of the paintcanvas
	 */
	public static native void undo(String id) /*-{
		var canvas = $wnd.document.getElementById(id);		
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.undo == 'function' && canvas.isReady() ){
			canvas.undo();	
		}else{
			var func = function() { canvas.undo(); };		
			setTimeout(func,1000);
		}				
	}-*/;

	/**
	 * Redo previously undoed brush stroke
	 * @param id
	 * 		The id of the paintcanvas
	 */
	public static native void redo(String id) /*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.redo == 'function' && canvas.isReady() ){
			canvas.redo();	
		}else{
			var func = function() { canvas.redo(); };		
			setTimeout(func,1000);
		}							
	}-*/;

	/**
	 * Set the drawing areas height inside the paintcanvas component
	 * @param id
	 * 		The id of the paintcanvas
	 * @param height
	 * 		The height in pixels
	 */
	public static native void setPaperHeight(String id, int height)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
					
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setPaperHeight == 'function' && canvas.isReady() ){
			canvas.setPaperHeight(height);	
		}else{
			var func = function() { canvas.setPaperHeight(height); };		
			setTimeout(func,1000);
		}							
	}-*/;

	/**
	 * Set the drawing areas width inside the paintcanvas component
	 * @param id
	 * 		The id of the paintcanvas
	 * @param width
	 * 		The width in pixels
	 */
	public static native void setPaperWidth(String id, int width)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setPaperWidth == 'function' && canvas.isReady() ){
			canvas.setPaperWidth(width);
		}else{
			var func = function() { canvas.setPaperWidth(width); };		
			setTimeout(func,1000);
		}				
	}-*/;	

	/**
	 * Display an error message
	 * @param message
	 */
	public static native void error(String message)/*-{
		alert(message);
	}-*/;

	/**
	 * Sets the size of the active brush. 
	 * @param id
	 * 		The id of the paintcanvas
	 * @param s
	 * 		The size in pixels of the brush
	 */
	public static native void setPenSize(String id, double s)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setBrushWidth == 'function' && canvas.isReady() ){
			canvas.setBrushWidth(s);
		}else{
			var func = function() { canvas.setBrushWidth(s); };		
			setTimeout(func,1000);
		}				
	}-*/;	

	/**
	 * Set the color of the active brush.
	 * @param id
	 * 		The id of the paintcanvas
	 * @param color
	 * 		The color in hexadecimal format. For instance FFFFFF(white).
	 */
	public static native void setPenColor(String id, String color)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setBrushColor == 'function' && canvas.isReady() ){
			canvas.setBrushColor(color);
		}else{
			var func = function() { canvas.setBrushColor(color); };		
			setTimeout(func,1000);
		}			
	}-*/;

	/**
	 * Set the currently active brush
	 * @param id
	 * 		The id of the paintcanvas
	 * @param brush
	 * 		The brush id
	 */
	public static native void setBrush(String id, String brush)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setBrush == 'function' && canvas.isReady() ){
			canvas.setBrush(brush);				
		}else{
			var func = function() { canvas.setBrush(brush);	 };		
			setTimeout(func,1000);
		}				
	}-*/;

	/**
	 * Sets the fillcolor of the active brush. 
	 * Not all brushes support this feature and if it is not supported then this will not
	 * do anything. 
	 * @param id
	 * @param color
	 */
	public static native void setFillColor(String id, String color)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setFillColor == 'function' && canvas.isReady() ){
			canvas.setFillColor(color);	
		}else{
			var func = function() { canvas.setFillColor(color);	};		
			setTimeout(func,1000);
		}				
	}-*/;

	/**
	 * Adds a layer to the canvas.
	 * @param id
	 * 		The id of the paintcanvas
	 * @param name
	 * 		The name/id of the layer. This is used to identify the layer and must be unique.
	 * 		The layer name Background is reserved and should not be used
	 */
	public static native void addLayer(String id, String name)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.addNewLayer == 'function' && canvas.isReady() ){
			canvas.addNewLayer(name);
		}else{
			var func = function() { canvas.addNewLayer(name); };		
			setTimeout(func,1000);
		}			
	
	}-*/;

	/**
	 * Set the visibility of the layer
	 * @param id
	 * 		The id of the paintcanvas
	 * @param name
	 * 		The name of the layer
	 * @param visibility
	 * 		The visibility of the layer. true means the layer is visible and false that it is not visble.
	 */
	public static native void setLayerVisibility(String id, String name, boolean visibility)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setLayerVisibility == 'function' && canvas.isReady() ){
			canvas.setLayerVisibility(name,visibility);
		}else{
			var func = function() { canvas.setLayerVisibility(name,visibility); };		
			setTimeout(func,1000);
		}		
	}-*/;

	/**
	 * Select the active layer. All drawing operations are perfomed on the active layer.
	 * @param id
	 * 		The id of the paintcanvas
	 * @param name
	 * 		The name of the layer
	 */
	public static native void selectLayer(String id, String name)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.selectLayer == 'function' && canvas.isReady() ){
			canvas.selectLayer(name);
		}else{
			var func = function() { canvas.selectLayer(name); };		
			setTimeout(func,1000);
		}			
	}-*/;

	/**
	 * Set the background color of the layer
	 * @param id
	 * 		The id of the paintcanvas
	 * @param color
	 * 		The color in hexadecimal format. For instance FFFFFF (white).
	 */
	public static native void setLayerColor(String id, String color)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setLayerBackgroundColor == 'function' && canvas.isReady() ){
			canvas.setLayerBackgroundColor(color);
		}else{
			var func = function() { canvas.setLayerBackgroundColor(color); };		
			setTimeout(func,1000);
		}			
	}-*/;

	/**
	 * Set the transparency of the layer.
	 * @param id
	 * 		The id of the paintcanvas
	 * @param alpha
	 * 		The alpha channel value. Value must be between 0 and 1 where 0 means no 
	 * 		transparency and 1 means full transparency.
	 */
	public static native void setLayerAlpha(String id, double alpha)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setLayerBackgroundAlpha == 'function' && canvas.isReady() ){
			canvas.setLayerBackgroundAlpha(alpha);
		}else{
			var func = function() { canvas.setLayerBackgroundAlpha(alpha); };		
			setTimeout(func,1000);
		}			
	}-*/;

	/**
	 * Requests an XML representation of the image. 
	 * @param id
	 * 		The id of the paintcanvas
	 * @return
	 * 		An XML string with the image represented in XML
	 */
	public static native String getImageXML(String id)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		return canvas.getImageXML();
	}-*/;

	/**
	 * Requests an PNG image from the component
	 * @param id
	 * 		The id of the paintcanvas
	 * @param dpi
	 * 		The dots per inch of the image. 72dpi returns the image
	 *      as seen on the screen.
	 * @return
	 * 		An Base64 encoded PNG image.
	 */
	public static native String getImagePNG(String id, int dpi)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		return canvas.getImagePNG(dpi);
	}-*/;

	/**
	 * Requests an JPG image from the component
	 * @param id
	 * 		The id of the paintcanvas
	 * @param dpi
	 * 		The dots per inch of the image. 72dpi returns the image
	 *      as seen on the screen.
	 * @return
	 * 		An Base64 encoded JPG image.
	 */
	public static native String getImageJPG(String id, int dpi)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		return canvas.getImageJPG(dpi);
	}-*/;

	/**
	 * Sets the component in interactive mode. Interactive mode means the the
	 * user can draw on the canvas using the mouse.
	 * @param id
	 * 		The id of the paintcanvas
	 * @param interactive
	 * 		The state of the component. If this is set to true then the user can
	 *      draw with the mouse on the component. If this is set to false then only
	 *      the server can draw on the component. 		
	 */
	public static native void setInteractive(String id, boolean interactive)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
	
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setInteractive == 'function' && canvas.isReady() ){
			canvas.setInteractive(interactive);
		}else{
			var func = function() { canvas.setInteractive(interactive); };		
			setTimeout(func,1000);
		}				
	}-*/;

	/**
	 * Draw a line on the current layer using the current width and color settings.
	 * @param id
	 * 		The id of the paintcanvas
	 * @param x1
	 * 		The first points x-value in pixels.
	 * @param y1
	 * 		The first points y-value in pixels.
	 * @param x2
	 * 		The second points x-value in pixels.
	 * @param y2
	 * 		The second points y-value in pixels.
	 */
	public static native void drawLine(String id, int x1, int y1, int x2, int y2)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsDrawLine == 'function' && canvas.isReady() ){
			canvas.graphicsDrawLine(x1,y1,x2,y2);
		}else{
			var func = function() { canvas.graphicsDrawLine(x1,y1,x2,y2); };		
		setTimeout(func,1000);
		}		
	}-*/;

	/**
	 * Draws a square on the current layer using the current width and color settings.
	 * @param id
	 * 		The id of the paintcanvas
	 * @param x
	 * 		The x-value of the upper left corner of the square in pixels
	 * @param y
	 * 		The y-value of the upper left corner of the square in pixels
	 * @param width
	 * 		The width of the square in pixels
	 * @param height
	 * 		The height of the square in pixels
	 */
	public static native void drawSquare(String id, int x, int y, int width, int height)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsDrawSquare == 'function' && canvas.isReady() ){
			canvas.graphicsDrawSquare(x,y,width,height);
		}else{
			var func = function() { canvas.graphicsDrawSquare(x,y,width,height); };		
		setTimeout(func,1000);
		}		
	}-*/;

	/**
	 * Clears the current layer of any previous drawings.
	 * @param id
	 * 		The id of the paintcanvas
	 */
	public static native void clear(String id)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsClear == 'function' && canvas.isReady() ){
			canvas.graphicsClear();
		}else{
			var func = function() { canvas.graphicsClear(); };		
		setTimeout(func,1000);
		}		
	}-*/;

	/**
	 * Draws a polygon on the current layer using current color and width settings.
	 * If the last point is not the same as the first point an additional edge will be made connecting
	 * the first point with the last point.
	 * @param id
	 * 		The id of the paintcanvas.
	 * @param x
	 * 		An array of x-values for the polygons corners.
	 * @param y
	 * 		An array of y-values for the polygons corners.
	 */
	public static native void drawPolygon(String id, int[]x, int[]y)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsDrawPolygon == 'function' && canvas.isReady() ){
			canvas.graphicsDrawPolygon(x.slice(), y.slice(), x.length);
		}else{
			var func = function() { canvas.graphicsDrawPolygon(x.slice(), y.slice(), x.length); };		
		setTimeout(func,1000);
		}		
	}-*/;

	/**
	 * Sets the background color of the component
	 * @param id
	 * 		The id of the paintcanvas
	 * @param color
	 * 		The color in hexadecimal format. For instance FFFFFF (white).
	 */
	public static native void setComponentBackground(String id, String color)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");
		
		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setComponentBackgroundColor == 'function' && canvas.isReady() ){
			canvas.setComponentBackgroundColor(color);
		}else{
			var func = function() { canvas.setComponentBackgroundColor(color); };		
		setTimeout(func,1000);
		}		
	}-*/;

	/**
	 * Check state of the component. Returns true if the component has loaded and is ready to be used.
	 * @param id
	 * 		The id of the paintcanvas
	 * @return
	 * 		The state of the component. If the state is true then the component is ready to be used.
	 */
	public static native boolean isReady(String id)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) return false;	
				
		if(typeof canvas.isReady == 'function' && canvas.isReady() ){
			return true;
		}
		
		return false;		
	}-*/;	
	
	/**
	 * Define the native util methods so the flash can find them.
	 * If the package changes then these need to be changed also!!!
	 */
	public static native void defineBridgeMethods()/*-{		
		function PaintCanvasNativeUtil(){
			this.error 	= function(message){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::error(Ljava/lang/String;)(message) };
			this.undo = function(id){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::undo(Ljava/lang/String;)(id); };
			this.redo = function(id){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::redo(Ljava/lang/String;)(id); };
			this.setPaperHeight = function(id, height){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::setPaperHeight(Ljava/lang/String;I)(id,height); };
			this.setPaperWidth = function(id, width){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::setPaperWidth(Ljava/lang/String;I)(id, width); };
			this.setPenSize = function(id, size){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::setPenSize(Ljava/lang/String;D)(id,size); };
			this.setPenColor = function(id, color){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::setPenColor(Ljava/lang/String;Ljava/lang/String;)(id,color); };
			this.setBrush = function(id, brush){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::setBrush(Ljava/lang/String;Ljava/lang/String;)(id,brush); };
			this.setFillColor = function(id, color){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::setFillColor(Ljava/lang/String;Ljava/lang/String;)(id,color); };
			this.addLayer = function(id, name){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::addLayer(Ljava/lang/String;Ljava/lang/String;)(id,name); };
			this.setLayerVisibility = function(id, visibility){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::setLayerVisibility(Ljava/lang/String;Ljava/lang/String;Z)(id,visibility); };
			this.selectLayer = function(id, name){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::selectLayer(Ljava/lang/String;Ljava/lang/String;)(id, name); };
			this.setLayerColor = function(id, color){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::setLayerColor(Ljava/lang/String;Ljava/lang/String;)(id, color); };
			this.setLayerAlpha = function(id, alpha){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::setLayerAlpha(Ljava/lang/String;D)(id,alpha); };
			this.getImageXML = function(id){ return @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::getImageXML(Ljava/lang/String;)(id); };
			this.getImagePNG = function(id, dpi){ return @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::getImagePNG(Ljava/lang/String;I)(id,dpi); };
			this.getImageJPG = function(id, dpi){ return @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::getImageJPG(Ljava/lang/String;I)(id,dpi); };
			this.setInteractive = function(id, interactive){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::setInteractive(Ljava/lang/String;Z)(id,interactive); };
			this.drawLine =  function(id,x1,y1,x2,y2){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::drawLine(Ljava/lang/String;IIII)(id,x1,y1,x2,y2); };
			this.drawSquare = function(id,x,y,width,height){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::drawSquare(Ljava/lang/String;IIII)(id,x,y,width,height); };
			this.clear = function(id){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::clear(Ljava/lang/String;)(id); };
			this.drawPolygon = function(id, x, y){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::drawPolygon(Ljava/lang/String;[I[I)(id,x,y); };
			this.setComponentBackground = function(id, color){ @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::setComponentBackground(Ljava/lang/String;Ljava/lang/String;)(id,color); };
			this.isReady = function(id){ return @com.vaadin.toolkitdraw.gwt.client.PaintCanvasNativeUtil::isReady(Ljava/lang/String;)(id); };
		}
		
		$wnd.PaintCanvasNativeUtil = new PaintCanvasNativeUtil();		
	}-*/;
}
