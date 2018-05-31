package com.wrl.xmlexcel.component;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.wrl.xmlexcel.util.Constantes;
import com.wrl.xmlexcel.view.TransXmlToExcel;

public class MouseControl extends MouseAdapter implements Constantes
{
	private TransXmlToExcel transXmlToExcel;
	
	public MouseControl(TransXmlToExcel transXmlToExcel){
		this.transXmlToExcel = transXmlToExcel;
	}
	
	public void mouseMoved(MouseEvent e) {
		transXmlToExcel.clickMouse(e, MOUSE_MOVED);
    }
	 
	public void mousePressed(MouseEvent e) {
		transXmlToExcel.clickMouse(e, MOUSE_CLICK);
    }
}
