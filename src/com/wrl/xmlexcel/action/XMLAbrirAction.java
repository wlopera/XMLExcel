package com.wrl.xmlexcel.action;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.wrl.xmlexcel.component.ExtensionFilter;
import com.wrl.xmlexcel.util.Constantes;
import com.wrl.xmlexcel.view.MarcoPrincipal;

@SuppressWarnings("serial")
public class XMLAbrirAction extends AbstractAction implements Constantes {
	private MarcoPrincipal marcoPrincipal;
	
	/**
	 * Constructor
	 * @param name: Nombre de la opcion del menu
	 * @param tooltip: Mensaje al usuario
	 */
	public XMLAbrirAction(MarcoPrincipal marcoPrincipal, String name, String tooltip, KeyStroke keystroke) {
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
	public XMLAbrirAction(MarcoPrincipal marcoPrincipal) {
		this(marcoPrincipal, "XML => EXCEL - Mensajes", "Abrir archivo XML a parsear Excel", KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
	}
	
	/**
	 * Acion a ejecutar: Permite seleccionar el Arhivo XML a procesar
	 */
	public void actionPerformed(ActionEvent e) {	
		try{
			JFrame frame = new JFrame();
			frame.setPreferredSize(new Dimension(300,300));
			JFileChooser chooser = new JFileChooser(DEFAULT_DIRECTORY);
			chooser.setDialogType(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setDialogTitle("Seleccionar archivo XML");
			chooser.setApproveButtonText("Procesar");
			ExtensionFilter xmlFiles = new ExtensionFilter(".xml", "Archivos XML's (*.xml)");
			chooser.addChoosableFileFilter(xmlFiles); // Agregar el filtro
			chooser.setFileFilter(xmlFiles);		  // Filtro por defecto
			JPanel panel = new JPanel();
			JCheckBox ckeckBox = new JCheckBox("Con Datos");
			panel.add(ckeckBox);
			chooser.setAccessory(panel);
			int result = chooser.showDialog(frame,null);
			
			if (result == JFileChooser.APPROVE_OPTION) {
				marcoPrincipal.procesarXMLExcel(chooser.getSelectedFile(), ckeckBox.isSelected());
			}
			
		}catch (Exception ex){
			System.out.println("Error XMLAbrirAction: "+ ex.getMessage());
		}
	}
}
