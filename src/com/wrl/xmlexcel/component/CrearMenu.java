package com.wrl.xmlexcel.component;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.wrl.xmlexcel.action.SalirAction;
import com.wrl.xmlexcel.action.TransXMLToExcelAction;
import com.wrl.xmlexcel.action.XMLAbrirAction;
import com.wrl.xmlexcel.view.MarcoPrincipal;

@SuppressWarnings("serial")
public class CrearMenu extends JMenuBar {
	private JMenu menu;	
	
	// Constructor
	public CrearMenu(String titulo, MarcoPrincipal marcoPrincipal){
		menu = new JMenu(titulo);
		xmltoExcelAction(marcoPrincipal);
		transXmltoExcelAction(marcoPrincipal);
		salirAction(marcoPrincipal);
	}
	
	/**
	 * xmltoExcelAction: Agregar la opcion Abrir XML
	 */
	private void xmltoExcelAction(MarcoPrincipal marcoPrincipal){
		menu.add(new JMenuItem(new XMLAbrirAction(marcoPrincipal)));
		menu.setMnemonic('A'); // Create shortcut
		add(menu); // Add the file menu				
	}
	
	/**
	 * xmltoExcelAction: Agregar la opcion Abrir XML
	 */
	private void transXmltoExcelAction(MarcoPrincipal marcoPrincipal){
		menu.add(new JMenuItem(new TransXMLToExcelAction(marcoPrincipal)));
		menu.setMnemonic('T'); // Create shortcut
		add(menu); // Add the file menu				
	}
	
	/**
	 * agregarAbrirAction: Agregar la opcion Salir del Programa Principal
	 */
	private void salirAction(MarcoPrincipal marcoPrincipal){
		menu.add(new JMenuItem(new SalirAction(marcoPrincipal)));
		menu.setMnemonic('S'); // Create shortcut
		add(menu); // Add the file menu				
	}
}
