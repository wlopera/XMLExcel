package com.wrl.xmlexcel.component;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class PanelImagen extends javax.swing.JPanel {
	public PanelImagen(Dimension dimension){
		this.setSize(dimension);
	}
	@Override
	public void paintComponent (Graphics g){
		Dimension tamanio = getSize();
		ImageIcon imagenFondo = new ImageIcon("image/fondo.jpg");
		g.drawImage(imagenFondo.getImage(),0,0,tamanio.width, tamanio.height, null);
		setOpaque(false);
		super.paintComponent(g);
	}
}