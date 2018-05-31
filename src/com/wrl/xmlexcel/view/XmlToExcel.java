package com.wrl.xmlexcel.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import com.wrl.xmlexcel.service.LeerXML;
import com.wrl.xmlexcel.component.ModeloTabla;


@SuppressWarnings("serial")
public class XmlToExcel extends JPanel {
	private JPanel eastPanel;
	private JPanel westPanel;
	private JPanel southPanel;
	private LeerXML leerXML;	
	private File xmlFile;
	private JTextArea text;
	private ArrayList<String> listInput;
	private ArrayList<String> listOutput;

	/**
     * subirInterfax: Metodo que permite subir la ventana del sistema
     */
	public void init(){
		// Agregar paneles al frame
		setPreferredSize(new Dimension(1200,600));		
		
		setLayout(new BorderLayout());
		
		// Crear Paneles
		westPanel = new JPanel();
		eastPanel = new JPanel();
		southPanel = new JPanel();

		// Agregar area de mensajes
		southPanel.add(agregarTextArea());
		southPanel.setVisible(false);
		
		JScrollPane scroll = new JScrollPane(text);
		southPanel.add(scroll);
		
		add(westPanel, BorderLayout.WEST);
		add(eastPanel, BorderLayout.EAST);
		add(southPanel, BorderLayout.SOUTH);
	}
	
	
	public void cargarFile(File xmlFile, boolean conDato){
		try {
			this.xmlFile = xmlFile;
			leerXML = new LeerXML(xmlFile);
			listInput = leerXML.getXML();
			if (listInput != null){
				String[] columnas = {"XML"};
				
				ModeloTabla tablaXML = new ModeloTabla(columnas, listInput, 500,475, false, this, true);
				JScrollPane scrollPanel = tablaXML.getScrollPaneModelo();
				westPanel.removeAll();
				westPanel.setBorder(BorderFactory.createLoweredBevelBorder());
				westPanel.add(scrollPanel);
			}
			listOutput = leerXML.getTrxsXML(conDato);
			if (listOutput != null){
				Collections.reverse(listOutput);
				int max = getMaxCamposArrayList(listOutput);
				String[] columnas = new String[max];
				
				for (int i=0; i<max; i++){
					columnas[i] = "Tag"+(i+1); 
				}
				ModeloTabla tablaData = new ModeloTabla(columnas, listOutput, 660,450, true, this, false);
				JScrollPane scrollPanel = tablaData.getScrollPaneModelo();
				eastPanel.removeAll();				
				JPanel panel = new JPanel();
				eastPanel.setBorder(BorderFactory.createLoweredBevelBorder());
				eastPanel.setLayout(new BorderLayout());
				panel.add(getBtnExportar());
				eastPanel.add(panel, BorderLayout.NORTH);
				eastPanel.add(scrollPanel, BorderLayout.SOUTH);
			}
			text.setText("SELECCIONE REGISTRO. PARA VER CAMPOS DE LAS TABLAS");
			southPanel.setVisible(true);
			SwingUtilities.updateComponentTreeUI(this);
		}catch (Exception ex){
			System.out.println("Error XMLAbrirAction: "+ ex.getMessage());
		}
	}
	
	public int getMaxCamposArrayList(ArrayList<String> lista){
		int max = 0;		
		for(int i=0; i<lista.size(); i++){
			String[] listado = lista.get(i).split("__&__");
			if (max <listado.length){   
    			max = listado.length;
			}
		}
		return max;
	}
	
	public void setTextTextArea(String txt, ListSelectionEvent event, JTable tableView){
		text.setText(txt);
	}
	
	public JScrollPane agregarTextArea(){
		text = new JTextArea(3,146);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		//text.setEnabled(false);
		text.setDisabledTextColor(Color.BLACK);
		//text.setBackground(Color.CYAN.darker());
		text.setBorder(BorderFactory.createRaisedBevelBorder());
	
		JScrollPane scroll = new JScrollPane(text);
		return scroll;
	}

	public JButton getBtnExportar(){
	  JButton btnExportar = new JButton("Exportar");    
	  btnExportar.setSize(new Dimension(200, 20));
	  btnExportar.addActionListener(new ActionListener() {
	     public void actionPerformed(ActionEvent e) {	        
	        String dateNum = "_"+System.currentTimeMillis();
	        String result = leerXML.writeExcel(listInput, listOutput, "c:\\temp\\"+xmlFile.getName()+dateNum+".xls");
	  		if (result != null ){
	  			setTextTextArea(result, null, null);
	  		} else {
	  			setTextTextArea("No se pudo exportar el archivo", null, null);
	  		}
	     }          
	  });
	return btnExportar;
	}

	public JTextArea getText() {
		return text;
	}

	public void setText(JTextArea text) {
		this.text = text;
	}

	public JPanel getSouthPanel() {
		return southPanel;
	}

	public void setSouthPanel(JPanel southPanel) {
		this.southPanel = southPanel;
	}

	public JPanel getWestPanel() {
		return westPanel;
	}

	public void setWestPanel(JPanel westPanel) {
		this.westPanel = westPanel;
	}

	public JPanel getEastPanel() {
		return eastPanel;
	}

	public void setEastPanel(JPanel eastPanel) {
		this.eastPanel = eastPanel;
	}

	public ArrayList<String> getListInput() {
		return listInput;
	}

	public void setListInput(ArrayList<String> listInput) {
		this.listInput = listInput;
	}

	public ArrayList<String> getListOutput() {
		return listOutput;
	}

	public void setListOutput(ArrayList<String> listOutput) {
		this.listOutput = listOutput;
	}
}
