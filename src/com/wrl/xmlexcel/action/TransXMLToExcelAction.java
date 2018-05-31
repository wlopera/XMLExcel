package com.wrl.xmlexcel.action;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.wrl.xmlexcel.view.MarcoPrincipal;

@SuppressWarnings("serial")
public class TransXMLToExcelAction extends AbstractAction {
	private MarcoPrincipal marcoPrincipal;
	
	public final File DEFAULT_DIRECTORY = new File("C://temp//XMLExcel");
	
	/**
	 * Constructor
	 * @param name: Nombre de la opcion del menu
	 * @param tooltip: Mensaje al usuario
	 */
	public TransXMLToExcelAction(MarcoPrincipal marcoPrincipal, String name, String tooltip, KeyStroke keystroke) {
		super(name);
		if(tooltip != null) { 
			putValue(SHORT_DESCRIPTION, tooltip);
		}
		if(keystroke != null) {
			putValue(ACCELERATOR_KEY, keystroke);
		}
		this.marcoPrincipal = marcoPrincipal;
	}
	
	/**
	 * Constructor por defecto
	 */
	public TransXMLToExcelAction(MarcoPrincipal marcoPrincipal) {
		this(marcoPrincipal, "XML =>  TRANSFORMACIONES => EXCEL", "Abrir Ambiente Transformar XML's", KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
	}
	
	/**
	 * Acion a ejecutar: Permite seleccionar el Arhivo XML a procesar
	 */
	public void actionPerformed(ActionEvent e) {	
		try{
			marcoPrincipal.procesarTransXMLExcel();			
		}catch (Exception ex){
			System.out.println("Error TransXMLToExcelAction: "+ ex.getMessage());
		}
	}
}
