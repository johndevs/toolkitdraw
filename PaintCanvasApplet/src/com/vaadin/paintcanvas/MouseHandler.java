package com.vaadin.paintcanvas;

import java.applet.Applet;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.vaadin.paintcanvas.util.GraphicsUtil;

public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {

	private boolean mouseButtonDown = false;
	
	private Applet parent;
	
	public MouseHandler(Applet applet) {
		this.parent = applet;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseButtonDown = true;
		GraphicsUtil.getCurrentBrush().beginStroke();
		this.parent.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseButtonDown = false;
		GraphicsUtil.getCurrentBrush().endBrush();
		this.parent.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(mouseButtonDown){
			GraphicsUtil.getCurrentBrush().processPoint(e.getPoint());		
			this.parent.repaint();
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
	}

}
