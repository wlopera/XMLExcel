package com.wrl.xmlexcel.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;

import com.wrl.xmlexcel.component.CrearMenu;
import com.wrl.xmlexcel.component.PanelImagen;

@SuppressWarnings("serial")
public class MarcoPrincipal extends JFrame {
	private CrearMenu menuBar;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MarcoPrincipal marcoPrincipal = new MarcoPrincipal();
		marcoPrincipal.subirInterfax();
	}

	/**
     * subirInterfax: Metodo que permite subir la ventana del sistema
     */
	public void subirInterfax(){
		setTitle("MENSAJES Y TRANSFORMACIONES XML -> EXCEL - SERVICIOS SOA");
		
		// Agregar paneles al frame
		setPreferredSize(new Dimension(1200,600));		
		
		PanelImagen p = new PanelImagen(getSize());
		setContentPane(p);
		
		setLayout(new BorderLayout());
		
		// Agregar Menu
		menuBar = new CrearMenu("Archivo", this);
		setJMenuBar(menuBar);
		
		setVisible(true);
	    pack();
	    setLocationRelativeTo(null);	   
	    
	    ImageIcon img = new ImageIcon("image/icono.jpg");
	    
	    setIconImage(img.getImage());
	    setResizable(false);
	    
	    setDefaultLookAndFeelDecorated(true);
	    try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());			
		} catch (Exception e) {
			System.out.println("Pantalla - Error: "+ e.getMessage());
		}
	    
	    
		// Add a Window closing listener to controlar cerrar vista
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
            	bajarInterfaz();
            }        	
        });
	}
	
	
	public void procesarXMLExcel(File xmlFile, boolean conDato){
		// Instanciar el objeto MlToExcel
		XmlToExcel xmlToExcel = new XmlToExcel();
				
		xmlToExcel.init();
		getContentPane().removeAll();
		setContentPane(xmlToExcel);
	    pack();
	    xmlToExcel.cargarFile(xmlFile, conDato);
	}
	
	public void procesarTransXMLExcel(){
		// Instanciar el objeto TransXmlToExcel
		TransXmlToExcel transXmlToExcel = new TransXmlToExcel();
				
		transXmlToExcel.init();
		getContentPane().removeAll();
		setContentPane(transXmlToExcel);
	    pack();
	}
	
	/**
     * bajarInterfaz:  Metodo que permite cerrar la ventana y el programa
     */
	public void bajarInterfaz()
	{
		this.dispose();
		System.exit(0); 
	}
}
