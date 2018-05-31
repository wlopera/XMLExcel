package com.wrl.xmlexcel.action;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.wrl.xmlexcel.view.MarcoPrincipal;

@SuppressWarnings("serial")
public class SalirAction extends AbstractAction {
	MarcoPrincipal marcoPrincipal;
	
	// Constructor
	public SalirAction(MarcoPrincipal marcoPrincipal, String name, String tooltip, KeyStroke keystroke) {
		super(name);
		if(keystroke != null) {
			putValue(ACCELERATOR_KEY, keystroke);
		}
		this.marcoPrincipal = marcoPrincipal;		
	}
	
	/**
	 * Constructor por defecto
	 */
	public SalirAction(MarcoPrincipal marcoPrincipal) {
		this(marcoPrincipal, "Salir", "Salir del programa", KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
	}
	
	// Manejador evento
	public void actionPerformed(ActionEvent e) {
		marcoPrincipal.bajarInterfaz();
	}
}
