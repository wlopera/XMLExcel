package com.wrl.xmlexcel.component;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.wrl.xmlexcel.view.XmlToExcel;

import java.awt.Dimension;
import java.util.ArrayList;

public class ModeloTabla {
	private String[] columnas;
	private Object[][] data;
	private  ArrayList<String> datos;
	private JScrollPane scrollpane;
	private JTable tableView;
	private XmlToExcel xmlToExcel;
	private boolean autoResize;
	
	public ModeloTabla(String[] columnas, ArrayList<String> datos, int ancho, int alto, boolean conSplit, XmlToExcel xmlToExcel, boolean autoResize) {
		this.columnas = columnas;
		this.datos = datos;
		this.xmlToExcel = xmlToExcel;
		this.autoResize = autoResize;
		init(ancho, alto, conSplit);
	}
	
	@SuppressWarnings("serial")
	public void init(int ancho, int alto, boolean conSplit) {
    // Cargar los datos de la tabla
		if (conSplit){
			cargarDatosConSplit();	
		} else {
			cargarDatos();
		}
    
    // Crear el módelo de data
    TableModel dataModel = new AbstractTableModel() {
	// Implementar métodos requeridos por el módelo abstracto
	
    	public int getColumnCount() { return columnas.length; }
    	public int getRowCount() { return data.length;}
    	public Object getValueAt(int row, int col) {return data[row][col];}
    	public String getColumnName(int column) {return columnas[column];}
    	@SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int c) {
    		if (getValueAt(0, c) == null)
    			return new String().getClass();
    		else
    			return getValueAt(0, c).getClass();
    	}
    	public boolean isCellEditable(int row, int col) {return true;}
    	public void setValueAt(Object aValue, int row, int column) {
    		data[row][column] = aValue;
    	}
     };
    
     // Crear la tabla
     tableView = new JTable(dataModel);
    
     // Ajusta el tamaño de la tabla a la ventana
     if (autoResize){
    	 tableView.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
     } else {
    	 tableView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
     }

     // Crea un scroll y activa la ventana
     scrollpane = new JScrollPane(tableView);
     scrollpane.setBorder(new BevelBorder(BevelBorder.LOWERED));
     scrollpane.setPreferredSize(new Dimension(ancho, alto));
     
     tableView.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent event) {
        	 if(!event.getValueIsAdjusting()) {
	        	 StringBuffer sb = new StringBuffer();
	        	 for (int i=0; i<tableView.getColumnCount(); i++)
	        	 {	if (tableView.getValueAt(tableView.getSelectedRow(), i) != null){
	        		 	sb.append(tableView.getValueAt(tableView.getSelectedRow(), i).toString());
	        	 	}
	        	 }	        	 
	        	 xmlToExcel.setTextTextArea(sb.toString().trim(), event, tableView);
        	 }
         }
     });
  }
  
  public JScrollPane getScrollPaneModelo(){
	  return scrollpane;
  }
  
  public JTable getTable(){
	  return tableView; 
  }


/******************************************************************************/
/*  Clase: cargarDatos                                                        */
/*  Función:  Recorre la lista de datos y los parsea como datos para la tabla */
/*  Fecha creación:            Agosto 2016       Autor: William Lopera        */
/******************************************************************************/
    public void cargarDatos(){
    	data = new Object[datos.size()][columnas.length];
    	for (int i=0; i< datos.size(); i++){
    	    for (int j=0; j<columnas.length; j++){   
        			data[i][j] = datos.get(i);
            }
    	}
    }

/******************************************************************************/
/*  Clase: cargarDatosConSplit                                                */
/*  Función:  Recorre la lista de datos y los parsea como datos para la tabla */
/*            luego de realizar split de los datos de entrada                 */
/*  Fecha creación:            Agosto 2016       Autor: William Lopera        */
/******************************************************************************/
    public void cargarDatosConSplit(){
    	data = new Object[datos.size()][columnas.length];
    	for (int i=0; i< datos.size(); i++){
    		String[] lista = datos.get(i).split("__&__");
    	    for (int j=0; j<lista.length; j++){   
        			data[i][j] = lista[j];
            }
    	}
    }
    
} 