package fi.jasoft.flashcanvas.client.ui;

import com.google.gwt.user.client.Window;

/**
 * The Class PaintCanvasNativeUtil.
 */
public class FlashCanvasNativeUtil {

	/** The canvases. */
	private static VFlashCanvas[] canvases = new VFlashCanvas[10];

	/**
	 * Undo last brush stroke.
	 *
	 * @param id The id of the paintcanvas
	 */
	public static native boolean undo(String id) /*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.undo == 'function' && canvas.isReady() ){
			canvas.undo();
			return true;
		}else{
			alert("delaying undo");
			var func = function() { canvas.undo(); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Redo previously undoed brush stroke.
	 *
	 * @param id The id of the paintcanvas
	 */
	public static native boolean redo(String id) /*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.redo == 'function' && canvas.isReady() ){
			canvas.redo();
			return true;
		}else{
			alert("delaying redo");
			var func = function() { canvas.redo(); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Set the drawing areas height inside the paintcanvas component.
	 *
	 * @param id The id of the paintcanvas
	 * @param height The height in pixels
	 */
	public static native boolean setPaperHeight(String id, int height)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setPaperHeight == 'function' && canvas.isReady() ){
			canvas.setPaperHeight(height);
			return true;
		}else{
			alert("delaying setPaperHeight");
			var func = function() { canvas.setPaperHeight(height); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Set the drawing areas width inside the paintcanvas component.
	 *
	 * @param id The id of the paintcanvas
	 * @param width The width in pixels
	 */
	public static native boolean setPaperWidth(String id, int width)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setPaperWidth == 'function' && canvas.isReady() ){
			canvas.setPaperWidth(width);
			return true;
		}else{
			alert("delaying setPaperWidth");
			var func = function() { canvas.setPaperWidth(width); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Display an error message.
	 *
	 * @param message the message
	 */
	public static native void error(String message)/*-{
		alert(message);
	}-*/;

	/**
	 * Sets the size of the active brush.
	 *
	 * @param id The id of the paintcanvas
	 * @param s The size in pixels of the brush
	 */
	public static native boolean setPenSize(String id, double s)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setBrushWidth == 'function' && canvas.isReady() ){
			canvas.setBrushWidth(s);
			return true;
		}else{
			alert("delaying setPenSize");
			var func = function() { canvas.setBrushWidth(s); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Set the color of the active brush.
	 *
	 * @param id The id of the paintcanvas
	 * @param color The color in hexadecimal format. For instance FFFFFF(white).
	 */
	public static native boolean setPenColor(String id, String color)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setBrushColor == 'function' && canvas.isReady() ){
			canvas.setBrushColor(color);
			return true;
		}else{
			alert("delaying setPenColor");
			var func = function() { canvas.setBrushColor(color); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Set the currently active brush.
	 *
	 * @param id The id of the paintcanvas
	 * @param brush The brush id
	 */
	public static native boolean setBrush(String id, String brush)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setBrush == 'function' && canvas.isReady() ){
			canvas.setBrush(brush);
			return true;
		}else{
			alert("delaying setBrush");
			var func = function() { canvas.setBrush(brush);	 };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Sets the fillcolor of the active brush.
	 * Not all brushes support this feature and if it is not supported then this will not
	 * do anything.
	 *
	 * @param id the id
	 * @param color the color
	 */
	public static native boolean setFillColor(String id, String color)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setFillColor == 'function' && canvas.isReady() ){
			canvas.setFillColor(color);
			return true;
		}else{
			alert("delaying setFillColor");
			var func = function() { canvas.setFillColor(color);	};
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Sets the fillalpha of the active brush.
	 * Not all brushes support this feature and if it is not supported then this will not
	 * do anything.
	 *
	 * @param id the id
	 * @param color the color
	 */
	public static native boolean setFillAlpha(String id, double alpha)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setFillAlpha == 'function' && canvas.isReady() ){
			canvas.setFillAlpha(alpha);
			return true;
		}else{
			alert("delaying setFillAlpha");
			var func = function() { canvas.setFillAlpha(alpha);	};
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Sets the brush alpha. This has different effects on different brushes
	 *
	 * @param id the id
	 * @param alpha the alpha
	 */
	public static native boolean setPenAlpha(String id, double alpha)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setBrushAlpha == 'function' && canvas.isReady() ){
			canvas.setBrushAlpha(alpha);
			return true;
		}else{
			alert("delaying setPenAlpha");
			var func = function() { canvas.setBrushAlpha(alpha); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Sets the font.
	 *
	 * @param id the id
	 * @param font the font
	 */
	public static native boolean setFont(String id, String font)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setFont == 'function' && canvas.isReady() ){
			canvas.setFont(font);
			return true;
		}else{
			alert("delaying setFont");
			var func = function() { canvas.setFont(font); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Adds a layer to the canvas.
	 *
	 * @param id The id of the paintcanvas
	 * @param name The name/id of the layer. This is used to identify the layer and must be unique.
	 * The layer name Background is reserved and should not be used
	 */
	public static native boolean addLayer(String id, String name)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.addNewLayer == 'function' && canvas.isReady() ){
			canvas.addNewLayer(name);
			return true;
		}else{
			alert("delaying addLayer");
			var func = function() { canvas.addNewLayer(name); };
			setTimeout(func,1000);
			return false;
		}

	}-*/;

	/**
	 * Set the visibility of the layer.
	 *
	 * @param id The id of the paintcanvas
	 * @param name The name of the layer
	 * @param visibility The visibility of the layer. true means the layer is visible and false that it is not visble.
	 */
	public static native boolean setLayerVisibility(String id, String name, boolean visibility)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setLayerVisibility == 'function' && canvas.isReady() ){
			canvas.setLayerVisibility(name,visibility);
			return true;
		}else{
			alert("delaying setLayerVisibility");
			var func = function() { canvas.setLayerVisibility(name,visibility); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Select the active layer. All drawing operations are perfomed on the active layer.
	 *
	 * @param id The id of the paintcanvas
	 * @param name The name of the layer
	 */
	public static native boolean selectLayer(String id, String name)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.selectLayer == 'function' && canvas.isReady() ){
			canvas.selectLayer(name);
			return true;
		}else{
			alert("delaying selectLayer");
			var func = function() { canvas.selectLayer(name); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Set the background color of the layer.
	 *
	 * @param id The id of the paintcanvas
	 * @param color The color in hexadecimal format. For instance FFFFFF (white).
	 */
	public static native boolean setLayerColor(String id, String color)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setLayerBackgroundColor == 'function' && canvas.isReady() ){
			canvas.setLayerBackgroundColor(color);
			return true;
		}else{
			alert("delaying setLayerColor");
			var func = function() { canvas.setLayerBackgroundColor(color); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Set the transparency of the layer.
	 *
	 * @param id The id of the paintcanvas
	 * @param alpha The alpha channel value. Value must be between 0 and 1 where 0 means no
	 * transparency and 1 means full transparency.
	 */
	public static native boolean setLayerAlpha(String id, double alpha)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setLayerBackgroundAlpha == 'function' && canvas.isReady() ){
			canvas.setLayerBackgroundAlpha(alpha);
			return true;
		}else{
			alert("delaying setLayerAlpha");
			var func = function() { canvas.setLayerBackgroundAlpha(alpha); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Moves the selected layer one position upwards
	 * @param id
	 * 		The canvas id
	 * @param name
	 * 		The name of the layer
	 * @return
	 */
	public static native boolean moveLayerUp(String id, String name)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.moveLayerUp == 'function' && canvas.isReady() ){
			canvas.moveLayerUp(name);
			return true;
		}else{
			alert("delaying moveLayerUp");
			var func = function() { canvas.moveLayerUp(name); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Moves the selected layer one position downwards
	 * @param id
	 * 		The canvas id
	 * @param name
	 * 		The name of the layer
	 * @return
	 */
	public static native boolean moveLayerDown(String id, String name)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.moveLayerDown == 'function' && canvas.isReady() ){
			canvas.moveLayerDown(name);
			return true;
		}else{
			alert("delaying moveLayerDown");
			var func = function() { canvas.moveLayerDown(name); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Removes selected layer
	 * @param id
	 * 		The canvas id
	 * @param name
	 * 		The name of the layer
	 * @return
	 */
	public static native boolean removeLayer(String id, String name)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.removeLayer == 'function' && canvas.isReady() ){
			canvas.removeLayer(name);
			return true;
		}else{
			alert("delaying removeLayer");
			var func = function() { canvas.removeLayer(name); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;


	/**
	 * Requests an XML representation of the image.
	 *
	 * @param id The id of the paintcanvas
	 *
	 * @return the image xml
	 *
	 * An XML string with the image represented in XML
	 */
	public static native String getImageXML(String id)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		return canvas.getImageXML();
	}-*/;

	/**
	 * Requests an PNG image from the component.
	 *
	 * @param id The id of the paintcanvas
	 * @param dpi The dots per inch of the image. 72dpi returns the image
	 * as seen on the screen.
	 *
	 * @return the image png
	 *
	 * An Base64 encoded PNG image.
	 */
	public static native String getImagePNG(String id, int dpi)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		return canvas.getImagePNG(dpi);
	}-*/;

	/**
	 * Requests an JPG image from the component.
	 *
	 * @param id The id of the paintcanvas
	 * @param dpi The dots per inch of the image. 72dpi returns the image
	 * as seen on the screen.
	 *
	 * @return the image jpg
	 *
	 * An Base64 encoded JPG image.
	 */
	public static native String getImageJPG(String id, int dpi)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		return canvas.getImageJPG(dpi);
	}-*/;

	/**
	 * Sets the component in interactive mode. Interactive mode means the the
	 * user can draw on the canvas using the mouse.
	 *
	 * @param id The id of the paintcanvas
	 * @param interactive The state of the component. If this is set to true then the user can
	 * draw with the mouse on the component. If this is set to false then only
	 * the server can draw on the component.
	 */
	public static native boolean setInteractive(String id, boolean interactive)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setInteractive == 'function' && canvas.isReady() ){
			canvas.setInteractive(interactive);
			return true;
		}else{
			alert("delaying setInteractive");
			var func = function() { canvas.setInteractive(interactive); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Draw a line on the current layer using the current width and color settings.
	 *
	 * @param id The id of the paintcanvas
	 * @param x1 The first points x-value in pixels.
	 * @param y1 The first points y-value in pixels.
	 * @param x2 The second points x-value in pixels.
	 * @param y2 The second points y-value in pixels.
	 */
	public static native boolean drawLine(String id, int x1, int y1, int x2, int y2)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsDrawLine == 'function' && canvas.isReady() ){
			canvas.graphicsDrawLine(x1,y1,x2,y2);
			return true;
		}else{
			alert("delaying drawLine");
			var func = function() { canvas.graphicsDrawLine(x1,y1,x2,y2); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Draws a square on the current layer using the current width and color settings.
	 *
	 * @param id The id of the paintcanvas
	 * @param x The x-value of the upper left corner of the square in pixels
	 * @param y The y-value of the upper left corner of the square in pixels
	 * @param width The width of the square in pixels
	 * @param height The height of the square in pixels
	 */
	public static native boolean drawSquare(String id, int x, int y, int width, int height)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsDrawSquare == 'function' && canvas.isReady() ){
			canvas.graphicsDrawSquare(x,y,width,height);
			return true;
		}else{
			alert("delaying drawSquare");
			var func = function() { canvas.graphicsDrawSquare(x,y,width,height); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Draws an ellipse on the current layer using the current color
	 *
	 * @param id
	 * 		The id of the canvas
	 * @param x
	 * 		The x-coordinate of the center of the ellipse
	 * @param y
	 * 		The y-coordinate of the center of the ellipse
	 * @param width
	 * 		The width of the ellipse
	 * @param height
	 * 		The height of the ellipse
	 * @return
	 * 		Returns true if the method succeeds
	 */
	public static native boolean drawEllipse(String id, int x, int y, int width, int height)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsDrawEllipse == 'function' && canvas.isReady() ){
			canvas.graphicsDrawEllipse(x,y,width,height);
			return true;
		}else{
			alert("delaying drawEllipse");
			var func = function() { canvas.graphicsDrawEllipse(x,y,width,height); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Clears the current layer of any previous drawings.
	 *
	 * @param id The id of the paintcanvas
	 */
	public static native boolean clear(String id)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsClear == 'function' && canvas.isReady() ){
			canvas.graphicsClear();
			return true;
		}else{
			alert("delaying clear");
			var func = function() { canvas.graphicsClear(); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Draws a polygon on the current layer using current color and width settings.
	 * If the last point is not the same as the first point an additional edge will be made connecting
	 * the first point with the last point.
	 *
	 * @param id The id of the paintcanvas.
	 * @param x An array of x-values for the polygons corners.
	 * @param y An array of y-values for the polygons corners.
	 */
	public static native boolean drawPolygon(String id, int[]x, int[]y)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsDrawPolygon == 'function' && canvas.isReady() ){
			canvas.graphicsDrawPolygon(x.slice(), y.slice(), x.length);
			return true;
		}else{
			alert("delaying drawPolygon");
			var func = function() { canvas.graphicsDrawPolygon(x.slice(), y.slice(), x.length); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Draws text using the current brush settings.
	 *
	 * @param id The id of the paintcanvas
	 * @param x The x-coordinate where the text should be
	 * @param y The y-coordinate where the text should be
	 * @param text the text
	 * @param width the width
	 * @param height the height
	 */
	public static native boolean drawText(String id, String text, int x, int y, int width, int height)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsDrawText == 'function' && canvas.isReady() ){
			canvas.graphicsDrawText(text, x, y, width, height);
			return true;
		}else{
			alert("delaying drawText");
			var func = function() { canvas.graphicsDrawText(text, x, y, width, height); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Draws an image on the current canvas.
	 *
	 * @param id The id of the paintcanvas
	 * @param image The image in base64 encoding
	 * @param x The x-position of the top left corner of the image
	 * @param y The y-position of the top left corner of the image
	 * @param alpha The alpha value of the image
	 */
	public static native boolean drawImage(String id, String image, int x, int y, int w, int h, double alpha)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsDrawImage == 'function' && canvas.isReady() ){
			canvas.graphicsDrawImage(x, y, w, h, image, alpha);
			return true;
		}else{
			alert("delaying drawImage");
			var func = function() { canvas.graphicsDrawImage(x, y, w, h, image, alpha); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	public static native boolean fill(String id, int x, int y) /*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.graphicsFill == 'function' && canvas.isReady() ){
			canvas.graphicsFill(x, y);
			return true;
		}else{
			alert("delaying fill");
			var func = function() { canvas.graphicsFill(x,y); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Sets the background color of the component.
	 *
	 * @param id The id of the paintcanvas
	 * @param color The color in hexadecimal format. For instance FFFFFF (white).
	 */
	public static native boolean setComponentBackground(String id, String color)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setComponentBackgroundColor == 'function' && canvas.isReady() ){
			canvas.setComponentBackgroundColor(color);
			return true;
		}else{
			alert("delaying setComponentBackground");
			var func = function() { canvas.setComponentBackgroundColor(color); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Check state of the component. Returns true if the component has loaded and is ready to be used.
	 *
	 * @param id The id of the paintcanvas
	 *
	 * @return true, if checks if is ready
	 *
	 * The state of the component. If the state is true then the component is ready to be used.
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
	 * Removes the current selection.
	 *
	 * @param id The id of the canvas
	 */
	public static native boolean removeSelection(String id)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.removeSelection == 'function' && canvas.isReady() ){
			canvas.removeSelection();
			return true;
		}else{
			alert("delaying removeSelection");
			var func = function() { canvas.removeSelection(); };
			setTimeout(func,1000);
			return false;
		}

	}-*/;


	/**
	 * Select the whole image.
	 *
	 * @param id The id of the canvas
	 */
	public static native boolean selectAll(String id)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.selectAll == 'function' && canvas.isReady() ){
			canvas.selectAll();
			return true;
		}else{
			alert("delaying selectAll");
			var func = function() { canvas.selectAll(); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Crop the current image to the selection
	 *
	 * @param id the canvas id
	 */
	public static native boolean selectCrop(String id)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.cropSelected == 'function' && canvas.isReady() ){
			canvas.selectAll();
			return true;
		}else{
			alert("delaying selectCrop");
			var func = function() { canvas.cropSelected(); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;


	/**
	 * Sets the cache mode.
	 *
	 * @param id the id
	 * @param mode the mode
	 */
	public static native boolean setCacheMode(String id, String mode)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setCacheMode == 'function' && canvas.isReady() ){
			canvas.setCacheMode(mode);
			return true;
		}else{
			alert("delaying setCacheMode");
			var func = function() { canvas.setCacheMode(mode); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	/**
	 * Sets the image cache.
	 *
	 * @param id the id
	 * @param cache the cache
	 */
	public static native boolean setImageCache(String id, String cache)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setImageCache == 'function' && canvas.isReady() ){
			canvas.setImageCache(cache);
			return true;
		}else{
			var func = function() { canvas.setImageCache(cache); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	public static native boolean isIE()/*-{
		var _app = navigator.appName;
		if (_app == 'Microsoft Internet Explorer') return true;
		else return false;
	}-*/;

	/**
	 * Sets the autosave time
	 * @param id
	 * 		The id of the canvas
	 * @param seconds
	 * 		The amount of seconds to save
	 * @return
	 */
	public static native boolean setAutosaveTime(String id, int seconds)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setAutosaveTime == 'function' && canvas.isReady() ){
			canvas.setAutosaveTime(seconds);
			return true;
		}else{
			var func = function() { canvas.setAutosaveTime(seconds); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;

	public static native boolean setClickListening(String id, boolean on)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.setClickListening == 'function' && canvas.isReady() ){
			canvas.setClickListening(on);
			return true;
		}else{
			var func = function() { canvas.setClickListening(on); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;


	public static native boolean finish(String id)/*-{
		var canvas = $wnd.document.getElementById(id);
		if(canvas == null) alert("Canvas not found!");

		// Check if function exists, if it does not then wait for the plugin to make it available
		if(typeof canvas.finish == 'function' && canvas.isReady() ){
			canvas.finish();
			return true;
		}else{
			var func = function() { canvas.finish(); };
			setTimeout(func,1000);
			return false;
		}
	}-*/;
	
	public static native boolean openImage(String id, String image)/*-{
	var canvas = $wnd.document.getElementById(id);
	if(canvas == null) alert("Canvas not found!");

	// Check if function exists, if it does not then wait for the plugin to make it available
	if(typeof canvas.openImage == 'function' && canvas.isReady() ){
		canvas.openImage(image);
		return true;
	}else{
		var func = function() { canvas.openImage(image); };
		setTimeout(func,1000);
		return false;
	}
}-*/;

	/**
	 * Set the available fonts. This is done by the Flash component and should
	 * not be done manually
	 *
	 * @param id The id of the canvas
	 * @param fonts The available font names
	 */
	public static void setAvailableFonts(String id, String[] fonts){
		getCanvas(id).setFonts(fonts);
	}

	/**
	 * Set the canvas in a ready state. This is done by the Flash component and should
	 * not be not manually
	 *
	 * @param id The id of the canvas which is ready
	 */
	public static void setCanvasReady(String id){
		getCanvas(id).setReady(true);
	}

	/**
	 * Register a new canvas with this utility. This is done automatically by the client
	 * side of the component and should not be done manually.
	 *
	 * @param canvas The canvas which should be registred
	 */
	public static void registerCanvas(VFlashCanvas canvas){
		for(int i=0; i<canvases.length; i++){
			if(canvases[i] == null || canvases[i].getId().equals(canvas.getId())){
				canvases[i] = canvas;
				break;
			}
		}
	}

	/**
	 * Returns a registred canvas.
	 *
	 * @param id The id of the canvas.
	 *
	 * @return the canvas
	 *
	 * A client side canvas implementation.
	 */
	public static VFlashCanvas getCanvas(String id){
		for(int i=0; i<canvases.length; i++){
			if(canvases[i] != null && canvases[i].getId().equals(id))
				return canvases[i];
		}

		return null;
	}

	/**
	 * Fetches the cached image from the server.
	 *
	 * @param id the id
	 */
	public static void getServerCache(String id){
		getCanvas(id).getServerCache();
	}

	public static void setServerCache(String id, String xml){
		getCanvas(id).setServerCache(xml);
	}

	public static void clickEvent(String id, int x, int y){
		getCanvas(id).clickEvent(x, y);
	}

	public static void brushStartEvent(String id){
		getCanvas(id).brushStartEvent();
	}

	public static void brushEndEvent(String id){
		getCanvas(id).brushEndEvent();
	}
	
	public static void brushLoadingCompleteEvent(String id){
		Window.alert("EVENT");
		getCanvas(id).brushLoadingCompleteEvent();
	}

	/**
	 * Define the native util methods so the flash can find them.
	 * If the package changes then these need to be changed also!!!
	 */
	public static native void defineBridgeMethods()/*-{		
                                                   function FlashCanvasNativeUtil(){
                                                   this.error 	= function(message){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::error(Ljava/lang/String;)(message) };
                                                   
                                                   this.undo = function(id){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::undo(Ljava/lang/String;)(id); };
                                                   this.redo = function(id){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::redo(Ljava/lang/String;)(id); };
                                                   
                                                   this.setPaperHeight = function(id, height){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setPaperHeight(Ljava/lang/String;I)(id,height); };
                                                   this.setPaperWidth = function(id, width){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setPaperWidth(Ljava/lang/String;I)(id, width); };
                                                   
                                                   this.setPenSize = function(id, size){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setPenSize(Ljava/lang/String;D)(id,size); };
                                                   this.setPenColor = function(id, color){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setPenColor(Ljava/lang/String;Ljava/lang/String;)(id,color); };
                                                   this.setBrush = function(id, brush){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setBrush(Ljava/lang/String;Ljava/lang/String;)(id,brush); };
                                                   this.setFillColor = function(id, color){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setFillColor(Ljava/lang/String;Ljava/lang/String;)(id,color); };
                                                   this.setFillAlpha = function(id, alpha){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setFillAlpha(Ljava/lang/String;D)(id,alpha); };
                                                   this.setPenAlpha = function(id, alpha){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setPenAlpha(Ljava/lang/String;D)(id,alpha); };
                                                   
                                                   this.addLayer = function(id, name){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::addLayer(Ljava/lang/String;Ljava/lang/String;)(id,name); };
                                                   this.setLayerVisibility = function(id, visibility){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setLayerVisibility(Ljava/lang/String;Ljava/lang/String;Z)(id,visibility); };
                                                   this.selectLayer = function(id, name){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::selectLayer(Ljava/lang/String;Ljava/lang/String;)(id, name); };
                                                   this.setLayerColor = function(id, color){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setLayerColor(Ljava/lang/String;Ljava/lang/String;)(id, color); };
                                                   this.setLayerAlpha = function(id, alpha){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setLayerAlpha(Ljava/lang/String;D)(id,alpha); };
                                                   
                                                   this.getImageXML = function(id){ return @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::getImageXML(Ljava/lang/String;)(id); };
                                                   this.getImagePNG = function(id, dpi){ return @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::getImagePNG(Ljava/lang/String;I)(id,dpi); };
                                                   this.getImageJPG = function(id, dpi){ return @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::getImageJPG(Ljava/lang/String;I)(id,dpi); };
                                                   
                                                   this.setInteractive = function(id, interactive){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setInteractive(Ljava/lang/String;Z)(id,interactive); };
                                                   
                                                   this.drawLine =  function(id,x1,y1,x2,y2){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::drawLine(Ljava/lang/String;IIII)(id,x1,y1,x2,y2); };
                                                   this.drawSquare = function(id,x,y,width,height){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::drawSquare(Ljava/lang/String;IIII)(id,x,y,width,height); };
                                                   this.clear = function(id){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::clear(Ljava/lang/String;)(id); };
                                                   this.drawPolygon = function(id, x, y){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::drawPolygon(Ljava/lang/String;[I[I)(id,x,y); };
                                                   this.fill = function(id, x, y){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::fill(Ljava/lang/String;II)(id,x,y); };
                                                   
                                                   this.setComponentBackground = function(id, color){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setComponentBackground(Ljava/lang/String;Ljava/lang/String;)(id,color); };
                                                   this.isReady = function(id){ return @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::isReady(Ljava/lang/String;)(id); };
                                                   this.setCanvasReady = function(id){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setCanvasReady(Ljava/lang/String;)(id); };
                                                   
                                                   this.drawText = function(id, text, x, y, width, height){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::drawText(Ljava/lang/String;Ljava/lang/String;IIII)(id,text,x,y,width,height); };
                                                   this.removeSelection = function(id){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::removeSelection(Ljava/lang/String;)(id); };
                                                   this.selectAll = function(id){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::selectAll(Ljava/lang/String;)(id); };
                                                   this.setAvailableFonts = function(id, fonts){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setAvailableFonts(Ljava/lang/String;[Ljava/lang/String;)(id, fonts); };
                                                   this.setFont = function(id, font){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setFont(Ljava/lang/String;Ljava/lang/String;)(id, font); };
                                                   
                                                   this.getServerCache = function(id){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::getServerCache(Ljava/lang/String;)(id); };
                                                   this.setServerCache = function(id, xml){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setServerCache(Ljava/lang/String;Ljava/lang/String;)(id, xml); };
                                                   this.setAutosaveTime = function(id, seconds){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::setAutosaveTime(Ljava/lang/String;I)(id, seconds); };
                                                   
                                                   this.clickEvent = function(id, x, y){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::clickEvent(Ljava/lang/String;II)(id, x, y); };
                                                   this.brushStartEvent = function(id){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::brushStartEvent(Ljava/lang/String;)(id); };
                                                   this.brushEndEvent = function(id){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::brushEndEvent(Ljava/lang/String;)(id); };
                                                   this.brushLoadingCompleteEvent = function(id){ @fi.jasoft.flashcanvas.client.ui.FlashCanvasNativeUtil::brushLoadingCompleteEvent(Ljava/lang/String;)(id); };
                                                   
                                                   }
                                                   
                                                   $wnd.FlashCanvasNativeUtil = new FlashCanvasNativeUtil();		
                                                   }-*/;
}
