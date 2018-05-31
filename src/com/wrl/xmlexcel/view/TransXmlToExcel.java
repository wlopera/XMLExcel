/******************************************************************************/
/*  Clase:   TransXmlToExcel                                                  */
/*  Funcion: Clase principal Parseo de mensajes                               */
/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
/******************************************************************************/
package com.wrl.xmlexcel.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.wrl.xmlexcel.component.ExtensionFilter;
import com.wrl.xmlexcel.component.MouseControl;
import com.wrl.xmlexcel.element.ElementoComplejo;
import com.wrl.xmlexcel.element.ElementoGrafico;
import com.wrl.xmlexcel.element.Linea;
import com.wrl.xmlexcel.util.Constantes;
import com.wrl.xmlexcel.service.LeerXML;

@SuppressWarnings("serial")
public class TransXmlToExcel extends JPanel implements Constantes {
	private ArrayList<ElementoGrafico> origenComplejos;
	private ArrayList<ElementoGrafico> destinoComplejos;
	private ArrayList<ElementoGrafico> elementosLineas;
	private boolean clickActivo;
	private Linea lineaTemp;
	private int identificadorTemp = 1;
	private JTextArea area;
	private JButton botonProcesar;
	private JButton btnBuscarInput; 
	private ElementoComplejo inputComplejo;
	private Componente componente;
	private JScrollPane scroll;
	private JTextField txtEntrada;
	private JTextField txtSalida;
	
	
	/******************************************************************************/
	/*  Metodo:  init                                           				  */
	/*  Funcion: Inicializar vista principal						      		  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	public void init (){	
		this.setBounds(20,20, 1200,615);	
		this.setLayout(new BorderLayout());
		
		// Crear el panel norte de la vista		
		this.add(crearPanelNorth(), BorderLayout.NORTH);
		
		// Crear el panel sur de la vista
		this.add(crearPanelSouth(), BorderLayout.SOUTH);
		
		componente = new Componente();
		// Crear el scroll panel
		crearScrollPanel();
		
		// Cargar Data de Prueba
		elementosLineas = new ArrayList<ElementoGrafico>();
		origenComplejos = new ArrayList<ElementoGrafico>();
		destinoComplejos = new ArrayList<ElementoGrafico>();
		
		// Definir el area de trabajo del scrollPanel
		componente.setPreferredSize(new Dimension(400,getMaximaLongRegistros()));
		this.add(scroll, BorderLayout.CENTER);		
	}
	  
	/******************************************************************************/
	/*  Metodo:  crearPanelNorth                                                  */
	/*  Funcion: Genera el panel norte de la vista                                */
	/*           Filtrar y consultar listas x criterios							  */ 
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private JPanel crearPanelNorth(){
		JPanel panelNorth = new JPanel(new BorderLayout());		
		panelNorth.setPreferredSize(new Dimension(500,130));
		JPanel panelMedio = crearPanelMedio();
		JPanel panelDerecho = crearPanelDerecho();
		JPanel panelSur = crearPanelSur();
		panelNorth.add(panelSur, BorderLayout.SOUTH);

		panelNorth.add(panelMedio, BorderLayout.CENTER);
		panelNorth.add(panelDerecho, BorderLayout.EAST);
		return panelNorth;
	}
	
	
	/******************************************************************************/
	/*  Metodo:  crearPanelDerecho                                                */
	/*  Funcion: Genera el panel Derecho de la vista                              */
	/*           Botones de procesamiento 			  							  */ 
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private JPanel crearPanelDerecho(){
		JPanel panelDerecho = new JPanel(new BorderLayout());
		panelDerecho.setPreferredSize(new Dimension(300,50));
		panelDerecho.setBorder(new TitledBorder(new EtchedBorder(), "Procesamiento: "));
		panelDerecho.setSize(new Dimension(300,50));

		botonProcesar = new JButton("Procesar");
		botonProcesar.setPreferredSize(new Dimension(100, 25));
		botonProcesar.addActionListener(new ActionListener() {
		     public void actionPerformed(ActionEvent e) {	        
		    	 generarDataAProcesar();
		     }          
		  });
		botonProcesar.setEnabled(false);

		JButton botonAyuda = new JButton("Ayuda");
		botonAyuda.setPreferredSize(new Dimension(100, 25));
		botonAyuda.addActionListener(new ActionListener() {
		     public void actionPerformed(ActionEvent e) {	        
		    	String sb = "\nXML's A PROCESAR\n";
    			sb += "  -  Entrada: Elementos origen\n"; 
    			sb += "  -  Salida: Elementos destino\n";
    			sb += "  -  Cargar: mostrar los elementos a parsear.\n";
    			sb += "FILTROS DE BUSQUEDA\n";
    			sb += "  -  Introduzca un Texto a buscar (Elementos origen o destino)\n";
    			sb += "  -  Buscar: seleccionar elemento según el críterio selecionado\n";
    			sb += "UNIR ELEMENTOS\n";
    			sb += "  -  Introduzca un primer click sobre el elemento origen a parsear\n";
    			sb += "  -  Introduzca un segundo click sobre el elemento destino a parsear\n";
    			sb += "ELIMNAR SELECCIÓN DE ELEMENTOS\n";
				sb +="  -  Introduzca reset para eliminar la selección de elementos\n";
    			sb += "BORRAR UNION DE ELEMENTOS\n";
    			sb += "  -  Introduzca reset sobre línea que une dos elementos\n";
    			sb += "  -  Mensaje de confirmación: Aceptar el borrado de la línea\n";
    			sb += "PROCESAR: generar el archivo de Transformaciones de Excel \n\n"; 
		    	JOptionPane.showMessageDialog(null, sb, "Ayuda!", JOptionPane.INFORMATION_MESSAGE);    			
		     }          
		  });
		panelDerecho.add(botonProcesar, BorderLayout.NORTH);
		panelDerecho.add(botonAyuda, BorderLayout.SOUTH);
		
		return panelDerecho;
	}
	
	/******************************************************************************/
	/*  Metodo:  crearPanelMedio                                                  */
	/*  Funcion: Genera el panel central  superior de la vista                    */
	/*           Filtrar y consultar listas x criterios							  */ 
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private JPanel crearPanelMedio(){
		JPanel panelMedio = new JPanel();
		panelMedio.setBorder(new TitledBorder(new EtchedBorder(), "Filtros de Busqueda: "));
		panelMedio.setPreferredSize(new Dimension(400,130));

		//Crear radio buttons - procesar por listado
	    final JRadioButton inputButton = new JRadioButton("Lista Entrada");
	    inputButton.setMnemonic(KeyEvent.VK_I);
	    inputButton.setSelected(true);
		
		final JRadioButton outputButton = new JRadioButton("Lista Salida");
	    outputButton.setMnemonic(KeyEvent.VK_O);
	    outputButton.setSelected(false);

	    //Group the radio buttons.
	    JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(inputButton);
        radioPanel.add(outputButton);
        
	    ButtonGroup group = new ButtonGroup();
	    group.add(inputButton);
	    group.add(outputButton);

		JLabel lblListaInput = new JLabel("Texto a buscar: ");
		final JTextField txtFieldInput = new JTextField();
		txtFieldInput.setPreferredSize(new Dimension(400, 20));
		btnBuscarInput = new JButton("Buscar");
		btnBuscarInput.setSize(new Dimension(200, 20));
		
		btnBuscarInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		    	ElementoComplejo complejoTemp = null;
		    		
		    	if(inputButton.isSelected()){
		    		complejoTemp = getElementosComplejosXLista(origenComplejos, txtFieldInput.getText());
		    	} else {
		    		complejoTemp = getElementosComplejosXLista(destinoComplejos, txtFieldInput.getText());
		    	}
		    	repaint();
		    	if (complejoTemp != null)
		    	{
		    		int y = complejoTemp.getY();  // Movimiento vertical arriba - abajo / Horizontalmente fijo 
		    		scroll.getViewport().setViewPosition(new Point(0,y));
		    	}
		     }          
		});
		btnBuscarInput.setEnabled(false); 
		panelMedio.add(lblListaInput);
		panelMedio.add(txtFieldInput);
		panelMedio.add(radioPanel);
		panelMedio.add(btnBuscarInput);
		return panelMedio;
	}
	
	/******************************************************************************/
	/*  Metodo:  crearPanelSur       	                                          */
	/*  Funcion: Genera el panel Sur de la vista                                  */
	/*           Archivos XML's			 			  							  */ 
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private JPanel crearPanelSur(){
		JPanel panelSur = new JPanel(new BorderLayout());
		panelSur.setBorder(new TitledBorder(new EtchedBorder(), "XML's a Procesar: "));
		panelSur.setPreferredSize(new Dimension(1000,55));		
		txtEntrada = new JTextField();
		txtEntrada.setPreferredSize(new Dimension(450,20));		
		txtSalida = new JTextField();
		txtSalida.setPreferredSize(new Dimension(450,20));
		txtSalida.setSize(new Dimension(200,20));
		
		JPanel panelListaIN = new JPanel();
		//Crear boton - buscar XML entrada
	    final JButton btnListaEntrada = new JButton("Entrada");
	    btnListaEntrada.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame();
				frame.setPreferredSize(new Dimension(300,300));
				JFileChooser chooser = new JFileChooser(DEFAULT_DIRECTORY);
				chooser.setDialogType(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setDialogTitle("Archivo XML de Entrada");
				chooser.setApproveButtonText("Procesar");
				ExtensionFilter xmlFiles = new ExtensionFilter(".xml", "Archivos XML's (*.xml)");
				chooser.addChoosableFileFilter(xmlFiles); // Agregar el filtro
				chooser.setFileFilter(xmlFiles);		  // Filtro por defecto
				int result = chooser.showDialog(frame,null);
				
				if (result == JFileChooser.APPROVE_OPTION) {
					txtEntrada.setText(chooser.getSelectedFile().toString());
				}		    	
		     }          
		});
	    panelListaIN.add(btnListaEntrada);
	    panelListaIN.add(txtEntrada);
		
		JPanel panelListaOUT = new JPanel();
		 //Crear boton - buscar XML salida
	    final JButton btnListaSalida = new JButton("Salida");
	    btnListaSalida.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame();
				frame.setPreferredSize(new Dimension(300,300));
				JFileChooser chooser = new JFileChooser(DEFAULT_DIRECTORY);
				chooser.setDialogType(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setDialogTitle("Archivo XML de Entrada");
				chooser.setApproveButtonText("Procesar");
				ExtensionFilter xmlFiles = new ExtensionFilter(".xml", "Archivos XML's (*.xml)");
				chooser.addChoosableFileFilter(xmlFiles); // Agregar el filtro
				chooser.setFileFilter(xmlFiles);		  // Filtro por defecto
				int result = chooser.showDialog(frame,null);
				
				if (result == JFileChooser.APPROVE_OPTION) {
					txtSalida.setText(chooser.getSelectedFile().toString());
				}		    	
		     }          
		});	    
		JButton botonCargar = new JButton("Cargar");
		botonCargar.setSize(new Dimension(100, 25));
		botonCargar.addActionListener(new ActionListener() {
		     public void actionPerformed(ActionEvent e) {	        
		    	cargarProceso();
		    	btnBuscarInput.setEnabled(true);
		    	botonProcesar.setEnabled(true);
		     }          
		  });
		
	    panelListaOUT.add(btnListaSalida);
	    panelListaOUT.add(txtSalida);
	    
	    panelSur.add(panelListaIN, BorderLayout.WEST);		
		panelSur.add(panelListaOUT, BorderLayout.CENTER);		
		panelSur.add(botonCargar, BorderLayout.EAST);
		return panelSur;
	}
	/******************************************************************************/
	/*  Metodo:  getElementosComplejosXLista                                      */
	/*  Funcion: Permite seleccionar registros por texto contenido en el registro */
	/*  OBJETOS: elementos: LIsta de registros a consultar                        */
	/*           text: Texto a buscar en los registros		         			  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private ElementoComplejo getElementosComplejosXLista(ArrayList<ElementoGrafico> elementos, String text){
		 ElementoComplejo complejoTemp = null;		 
		 for (ElementoGrafico elemento: elementos){
    		 ElementoComplejo complejo = (ElementoComplejo) elemento;
    		 if (complejo.getTexto().toUpperCase().contains(text.toUpperCase())){
    			 if (complejoTemp == null){
    				 complejoTemp = complejo;
    			 }
    			 elemento.setColorRellenoTemp(Color.MAGENTA);		
    			 elemento.setSeleccionado(true);
    			 setMensaje("Elemento: "+ complejo.getTexto(), true);
    		 }
    	 }	
		 return complejoTemp;
	}
	
	/******************************************************************************/
	/*  Metodo:  crearPanelSouth                                                  */
	/*  Funcion: Crea el panel de botones y mensajes al usuario				      */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private JPanel crearPanelSouth(){
		JPanel panel = new JPanel(new BorderLayout());
		area = new JTextArea(8,200);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setDisabledTextColor(Color.BLACK);
		area.setBorder(BorderFactory.createRaisedBevelBorder());		
		JScrollPane scroll = new JScrollPane(area);
		panel.add(scroll, BorderLayout.SOUTH);
		return panel;
	}
	
	/******************************************************************************/
	/*  Metodo:  crearScrollPanel                                                 */
	/*  Funcion:Genera el control scroll Panel asociado a control del mouse.      */
	/*			 Para size scroll  					  							  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private void crearScrollPanel(){
		scroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.getVerticalScrollBar().setBlockIncrement(SCROLL_BLOQUE_INCREMENTO);
		scroll.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIDAD_INCREMENTO);
		scroll.setViewportView(componente);		
		// Asociar el manejo del mouse al scroll
		MouseControl mouse  = new MouseControl(this);
		scroll.addMouseMotionListener(mouse);
		scroll.addMouseListener(mouse);		
	}
	
	/******************************************************************************/
	/*  Metodo:  getMaximaLongRegistros                                           */
	/*  Funcion: Retorna la mayor cantidad de resgitros de la lista. 			  */
	/*			 Para size scroll  					  							  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private int getMaximaLongRegistros(){
		if ( origenComplejos.size() >  destinoComplejos.size()){
			return  origenComplejos.size()*COMPLEJO_DELTA_Y+SCROLL_AJUSTE_SIZE;
		} else {
			return  destinoComplejos.size()*COMPLEJO_DELTA_Y+SCROLL_AJUSTE_SIZE;
		}
	}
	
	/******************************************************************************/
	/*  Metodo:  clickMouse                                                       */
	/*  Funcion: Control de eventos del mouse sobre la vista  					  */
	/*  OBJETOS: e: Eventos del mouse 											  */
	/*           tipoEvento: Tipo de evento (CLICK-PRESS_MOVED...)     			  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	public void clickMouse(MouseEvent e, int tipoEvento){
		// AJUSTE: Delta de variacion de la vista y el scroll 		
		Point pto = e.getPoint();
		Point ptoScroll = scroll.getViewport().getViewPosition();
		pto = new Point(pto.x + ptoScroll.x, pto.y + ptoScroll.y);
		switch (tipoEvento) {
		case MOUSE_CLICK:
			clickActivo = clickActivo ? false : true;			
			if (clickActivo && SwingUtilities.isLeftMouseButton(e)){
				primerPuntoData(pto);
			} else if (!clickActivo && SwingUtilities.isLeftMouseButton(e)){
				segundoPuntoData(pto);
			} else if (SwingUtilities.isRightMouseButton(e)) {
				ElementoGrafico elemento = clickDentroLinea(pto);
				if (elemento != null){
					eliminarlinea(elemento);
				} else {
					mouseReset();
				}
			}			
			break;
		case MOUSE_MOVED:
			if (clickActivo){
				mouseMovePuntoData(pto);
			} else {
				// Permite simular mouse-over sobre la lista de entrada
				selectDataSimulandoMouseOver(origenComplejos, pto);				
			}
			break;
		default:
			break;
		}
	}
	

	/******************************************************************************/
	/*  Metodo:  eliminarlinea  		                                          */
	/*  Funcion: Borra una linea y todas sus conexiones en el modelo		      */
	/*  OBJETOS: pto: Punto de data sobre la vista								  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private void eliminarlinea(ElementoGrafico elemento){
		Color color = elemento.getColor();
		//setMensaje("Linea: elemento: "+ elemento.toString(), true);
		elemento.setColor(Color.MAGENTA);
		elemento.setGrosor(new BasicStroke(3f));
		repaint();
		int resp = JOptionPane.showConfirmDialog(null, "¿Desea eliminar la línea?", "Alerta!", JOptionPane.YES_NO_OPTION);
		if (resp == JOptionPane.YES_OPTION){
			// Eliminar la linea seleccionada
			elementosLineas.remove(elemento);
			
			// Depurar el valor XML de entrada. Eliminar relacion con la linea eliminada
			for (ElementoGrafico rectInicial: origenComplejos){
				if (rectInicial.getIdentificador().equals(((Linea)elemento).getConexionInicial())){
					//setMensaje(rectInicial.toString(), true);
					ElementoComplejo rectInicialComplejo = (ElementoComplejo)rectInicial; 
					rectInicialComplejo.getConexiones().remove(elemento.getIdentificador());
					if (rectInicialComplejo.getConexiones().size() == 0){
						rectInicial.setColorRelleno(null);
					}
				}
			}
			
			// Depurar el valor XML de salida. Eliminar relacion con la linea eliminada
			for (ElementoGrafico rectFinal: destinoComplejos){
				if (rectFinal.getIdentificador().equals(((Linea)elemento).getConexionfinal())){
					//setMensaje(rectFinal.toString(), true);
					ElementoComplejo rectFinalComplejo = (ElementoComplejo)rectFinal; 
					rectFinalComplejo.getConexiones().remove(elemento.getIdentificador());
					rectFinalComplejo.setColorRelleno(null);
				}
			}
			
		} else {
			elemento.setColor(color);
			elemento.setGrosor(null);							
		}	
		repaint();
	}
	
	/******************************************************************************/
	/*  Metodo:  primerPuntoData  		                                          */
	/*  Funcion: Procesar primer punto de data sobre la vista				      */
	/*  OBJETOS: pto: Punto de data sobre la vista								  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private void primerPuntoData(Point pto){		
		ElementoGrafico elemento = clickDentroRectangle(pto, true);
		if (elemento != null){
			String identificador = IDENTIFICADOR_TEMP + identificadorTemp;
			Point puntoEnlace = ((ElementoComplejo)elemento).getPtoEnlace();
			lineaTemp = new Linea(identificador, Color.BLACK, null, puntoEnlace, puntoEnlace, null);
			lineaTemp.setConexionInicial(elemento.getIdentificador());
			//elemento.setColorRelleno(Color.MAGENTA);
			inputComplejo = ((ElementoComplejo)elemento);
			inputComplejo.setColorRellenoTemp(Color.MAGENTA);
			inputComplejo.setConexion(identificador);
			clearSeleccionableValores(origenComplejos, inputComplejo);
			//setMensaje(((ElementoComplejo)elemento).getTexto(), false);			
		} else {
			clickActivo = false;
		}
	}
	
	/******************************************************************************/
	/*  Metodo:  mouseMovePuntoData		                                          */
	/*  Funcion: Procesar los punto de data sobre la vista al mover el mouse	  */
	/*  OBJETOS: pto: Punto de data sobre la vista								  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private void mouseMovePuntoData(Point pto){
		if (lineaTemp != null){
			// Permite simular mouse-over sobre la lista de salida
			selectDataSimulandoMouseOver(destinoComplejos, pto);				
			lineaTemp.setPtoFinal(pto);
			repaint();
		}		
	}

	/******************************************************************************/
	/*  Metodo:  mouseReset		                                          		  */
	/*  Funcion: Resetear el proceso actual de control de linea 				  */
	/*  OBJETOS: pto: Punto de data sobre la vista								  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private void mouseReset(){
		if (lineaTemp != null){
			inputComplejo.setColorRellenoTemp(null);
			// Borrar la conexion a la linea del elemento d ela lista 1.
			String conexion = lineaTemp.getIdentificador();
			inputComplejo.deleteConexion(conexion);
			
			inputComplejo = null;
			lineaTemp = null;
			setMensaje("", false);
		}
		clearSeleccionableValores(origenComplejos, null);
		clearSeleccionableValores(destinoComplejos, null);
		repaint();
	}

	/******************************************************************************/
	/*  Metodo:  segundoPuntoData  		                                          */
	/*  Funcion: Procesar segundo punto de data sobre la vista				      */
	/*  OBJETOS: pto: Punto de data sobre la vista								  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private void segundoPuntoData(Point pto){
		if (lineaTemp != null){
			ElementoGrafico elemento = clickDentroRectangle(pto, false);			
			if (elemento != null){
				Point puntoEnlace = ((ElementoComplejo)elemento).getPtoEnlace();
				lineaTemp.setPtoFinal(puntoEnlace);							
				String identificador = IDENTIFICADOR_TEMP + identificadorTemp++;
				((ElementoComplejo)elemento).setConexion(identificador);
				lineaTemp.setConexionfinal(elemento.getIdentificador());
				elementosLineas.add(lineaTemp);
				inputComplejo.setColorRellenoTemp(null);
				elemento.setColorRellenoTemp(null);
				inputComplejo.setColorRelleno(Color.CYAN.darker());
				elemento.setColorRelleno(Color.CYAN.darker());
				clearSeleccionableValores(destinoComplejos, null);
				inputComplejo = null;
				lineaTemp = null;				
			} else {
				lineaTemp.setPtoFinal(pto);
				clickActivo = true;
			}
			repaint();
		}
	}
	
	/******************************************************************************/
	/*  Metodo:  clickDentroRectangle                                             */
	/*  Funcion: Permite evaluar si el click es dentro de un rectangulo		      */
	/*  OBJETOS: pto: Punto de data sobre la vista								  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private ElementoGrafico clickDentroRectangle(Point pto, boolean input){
		ArrayList<ElementoGrafico> elementos = new ArrayList<ElementoGrafico>();
		if (input){
			elementos = origenComplejos;
		} else {
			elementos = destinoComplejos;
		}
		for (ElementoGrafico elemento: elementos){			
			if( ((ElementoComplejo)elemento).contieneAlPuntoXY(pto)){
				return elemento;
			}
		} 	
		return null;
	}

	/******************************************************************************/
	/*  Metodo:  clickDentroLinea                                                 */
	/*  Funcion: Permite evaluar si el click es dentro de una linea 		      */
	/*			 Seleccionar una linea.											  */
	/*  OBJETOS: pto: Punto de data sobre la vista								  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private ElementoGrafico clickDentroLinea(Point pto){
		for (ElementoGrafico elemento: elementosLineas){			
			if( ((Linea)elemento).contieneAlPuntoXY(pto)){
				return elemento;
			}
		} 	
		return null;
	}
	
	/******************************************************************************/
	/*  Metodo:  setMensaje                                            		      */
	/*  Funcion: Permite enviar mensajes al usuario							      */
	/*  OBJETOS: text: Texto a mostrar en la vista - mensaje al usuario	    	  */
	/*			 agregar: agrega mensajes a la vista sino limpia el mensaje		  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private void setMensaje(String text, boolean agregar){
		if (agregar){
			String textOld = area.getText();
			area.setText(textOld + "\n"+ text);
		} else {
			area.setText(text);
		}
	}
	
//	/******************************************************************************/
//	/*  Metodo:  dummyListaInput                                            	  */
//	/*  Funcion: Valores Dummy de entrada lista 1							      */
//	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
//	/******************************************************************************/
//	private void dummyListaInput(){
//		String[] dataInput ={
//			"<MsgRqHdr><SourceChannelCode/>",
//			"<MsgRqHdr><SupervisorCode/>",
//			"<MsgRqHdr><OperatorCode/>",
//			"<MsgRqHdr><RequestedOperationType/>",
//			"<MsgRqHdr><NetworkTrnInfo><TransactionConsecutive/>",
//			"<MsgRqHdr><NetworkTrnInfo><RegisterNumber/>",
//			"<MsgRqHdr><NetworkTrnInfo><OriginatorName/>",
//			"<MsgRqHdr><NetworkTrnInfo><OperationType/>",
//			"<MsgRqHdr><NetworkTrnInfo><TransactionType/>",
//			"<MsgRqHdr><ApplicantData><Channel/>",
//			"<MsgRqHdr><RecCtrlIn><MaxRec/>",
//			"<MsgRqHdr><ReturnValue/>",
//			"<MsgRqHdr><MessageId/>",
//			"<MsgRqHdr><Priority/>",
//			"<MsgRqHdr><VBProtocol><VBProtocolInd/>",
//			"<MsgRqHdr><VBProtocol><TransactionInd/>",	
//			"<Bill><AllianceBilling><AgreeNum/>",
//			"<Bill><Account><AditionalDesc/>",
//			"<Bill><Account><Bankinfo><BankId/>",
//			"<Bill><Service><SvcCode/>",
//		};
//		origenComplejos = new ArrayList<ElementoGrafico>();
//
//		int x = INPUT_INICIAL_X;
//		int y = INPUT_INICIAL_Y;
//		int ancho = COMPLEJO_ANCHO;
//		int alto = COMPLEJO_ALTO;
//		int deltaX = COMPLEJO_DELTA_X;
//		int deltaY = COMPLEJO_DELTA_Y;
//		for (int i=0;  i<dataInput.length; i++){
//			origenComplejos.add(new ElementoComplejo("INPUT_"+ (i+1), Color.BLUE, null, Color.BLACK, dataInput[i], x, y, ancho, alto, ELEMENTO_COMPLEJO_lISTA_INPUT));
//			x +=deltaX;
//			y +=deltaY;			
//		}
//	}
//	
//	/******************************************************************************/
//	/*  Metodo:  dummyListaOutput                                            	  */
//	/*  Funcion: Valores Dummy de entrada lista 2							      */
//	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
//	/******************************************************************************/
//	private void dummyListaOutput(){
//		String[] dataOutput ={
//			"<readDebt_Rs><Status><StatusType/>",
//			"<readDebt_Rs><Status><StatusCode/>",
//			"<readDebt_Rs><Status><StatusDesc/>",
//			"<readDebt_Rs><Status><ApplicationName/>",
//			"<readDebt_Rs><Status><LineNumber/>",
//			"<readDebt_Rs><Status><AdditionalStatus><StatusType/>",
//			"<readDebt_Rs><Status><AdditionalStatus><StatusCode/>",
//			"<readDebt_Rs><Status><AdditionalStatus><StatusDesc/>",
//			"<readDebt_Rs><Status><AdditionalStatus><ValidationType/>",
//			"<readDebt_Rs><Status><AdditionalStatus><Severity/>",
//			"<readDebt_Rs><Status><AdditionalStatus><LineNumber/>",
//			"<readDebt_Rs><Status><Severity/>",
//			"<readDebt_Rs><Status><StatusInd/>",
//			"<readDebt_Rs><Status><LogId/>",
//			"<readDebt_Rs><Bill><AllianceBilling><AgreeNum/>",
//			"<readDebt_Rs><Bill><Account><AditionalDesc/>",
//			"<readDebt_Rs><Bill><Account><Bankinfo><BankId/>",
//			"<readDebt_Rs><Bill><Service><SvcCode/>",
//			"<readDebt_Rs><Banking><BankAcctTrnRec><TrnSrc/>",
//			"<readDebt_Rs><Banking><BankAcctTrnRec><TrnCod/>" 
//		};
//		destinoComplejos = new ArrayList<ElementoGrafico>();
//
//		int x = OUTPUT_INICIAL_X;
//		int y = OUTPUT_INICIAL_Y;
//		int ancho = COMPLEJO_ANCHO;
//		int alto = COMPLEJO_ALTO;
//		int deltaX = COMPLEJO_DELTA_X;
//		int deltaY = COMPLEJO_DELTA_Y;		
//		for (int i=0;  i<dataOutput.length; i++){
//			destinoComplejos.add(new ElementoComplejo("OUTPUT_"+ (i+1), Color.BLUE, null, Color.BLACK, dataOutput[i], x, y, ancho, alto, ELEMENTO_COMPLEJO_lISTA_OUTPUT));
//			x +=deltaX;
//			y +=deltaY;			
//		}
//	}
	
	/******************************************************************************/
	/*  Metodo:  cargarProceso                                                    */
	/*  Funcion: Carga la data original y reinicia las variables			      */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private void cargarProceso(){
		origenComplejos = getValoresXML(INPUT_INICIAL_X,
									    INPUT_INICIAL_Y,
									    COMPLEJO_ANCHO,
									    COMPLEJO_ALTO,
									    COMPLEJO_DELTA_X,
									    COMPLEJO_DELTA_Y,
									    ELEMENTO_COMPLEJO_lISTA_INPUT,
									    txtEntrada.getText());
		
		destinoComplejos = getValoresXML(OUTPUT_INICIAL_X,
										 OUTPUT_INICIAL_Y,
									     COMPLEJO_ANCHO,
									     COMPLEJO_ALTO,
									     COMPLEJO_DELTA_X,
									     COMPLEJO_DELTA_Y,
									     ELEMENTO_COMPLEJO_lISTA_OUTPUT,
									     txtSalida.getText());
		if (origenComplejos != null && destinoComplejos != null){
			componente.setPreferredSize(new Dimension(400,getMaximaLongRegistros()));
			componente.updateUI();
			elementosLineas = new ArrayList<ElementoGrafico>();
			setMensaje("", false);
			clickActivo = false;
			lineaTemp = null;
			identificadorTemp = 1;
			repaint();
		} else {
			origenComplejos = new ArrayList<ElementoGrafico>();
			destinoComplejos = new ArrayList<ElementoGrafico>();
			elementosLineas = new ArrayList<ElementoGrafico>();
			setMensaje("", false);
			clickActivo = false;
			lineaTemp = null;
			identificadorTemp = 1;
			repaint();
			setMensaje("Archivos o Entrada de datos Invalida ", false);
		}
	}
	
	/******************************************************************************/
	/*  Metodo:  getValoresXML                    		                          */
	/*  Funcion: Genera la lista de valores de un archivo XML   			      */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private ArrayList<ElementoGrafico> getValoresXML(int x, int y, int ancho, int alto, int deltaX, int deltaY, int tipoElemento, String filename){
		File xmlIn = new File(filename);
		LeerXML leerXML = new LeerXML(xmlIn);
		ArrayList<String> lista = leerXML.getTrxsXML(false);
		
		ArrayList<ElementoGrafico> valores = new ArrayList<ElementoGrafico>();
		if (lista != null){
			for (int i=0;  i<lista.size(); i++){
				valores.add(new ElementoComplejo("INPUT_"+ (i+1), Color.BLUE, null, Color.BLACK, lista.get(i).replaceAll("__&__",""), x, y, ancho, alto, tipoElemento, null));
				x +=deltaX;
				y +=deltaY;			
			}
			return valores;
		} else {
			return null;
		}
	}
	
	/******************************************************************************/
	/*  Metodo:  generarDataAProcesar                                             */
	/*  Funcion: Genera la lista de valores a procesar - Generar Excel Trans      */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private void generarDataAProcesar(){
		ArrayList<String> traza = new ArrayList<String>();
		for (ElementoGrafico elemento: origenComplejos){
			ElementoComplejo elInicial = (ElementoComplejo)elemento;
			for (String idLinea: elInicial.getConexiones()){
				ElementoComplejo elFinal = getElementoComplejoConexionFinalXLinea(idLinea);
				if (elFinal != null){
					String valor = elInicial.getTexto() +"__&&__" +elFinal.getTexto();
					traza.add(valor.replace("><", ">__&__<"));
					
				}
			}
		}
		
//		for (String data: traza){
//			setMensaje(data, true);
//		}
		int maximos[] = getMaxCamposDeLista(traza);
		
		crearCabecera(traza, maximos);
		
		LeerXML leerXML = new LeerXML();
		String dateNum = "_"+System.currentTimeMillis();
		
		String resultado = leerXML.createExcel(traza, "c:\\temp\\TRANSFORAMCION_"+dateNum+".xls", maximos[0]);
		setMensaje(resultado, true);
	}
	
	/******************************************************************************/
	/*  Metodo:  getElementoComplejoConexionFinalXLinea               		      */
	/*  Funcion: Retorna elemeno complejo asociado al extremo final de la linea   */
	/*  OBJETOS: identificador: Identificador de la linea   	    	          */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private ElementoComplejo getElementoComplejoConexionFinalXLinea(String identificador){
		for (ElementoGrafico elemento: elementosLineas){
			Linea linea = (Linea)elemento;
			if (linea.getIdentificador().equals(identificador))
			{
				return getElementoComplejoXIdentificador(linea.getConexionfinal());
			}
		}
		return null;
	}
	
	/******************************************************************************/
	/*  Metodo:  getElementoComplejoXIdentificador                   		      */
	/*  Funcion: Retorna elemeno complejo segun su identificador                  */
	/*  OBJETOS: identificador: Identificador del elemento complejo  	    	  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private ElementoComplejo getElementoComplejoXIdentificador(String identificador){
		for (ElementoGrafico elemento: destinoComplejos){
			ElementoComplejo complejo = (ElementoComplejo)elemento;
			if (complejo.getIdentificador().equals(identificador))
			{
				return complejo;
			}
		}
		return null;
	}
	
	/******************************************************************************/
	/*  Metodo:  crearCabecera                                       		      */
	/*  Funcion: Creador de la cabecera de la hoja de excel 				      */
	/*           Evaluando los maximo de cada lista para crear las cabeceras	  */
	/*  OBJETOS: traza: Listado a procesar								    	  */
	/*           maximos: valores de los maximos de cada lista de valores         */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private void crearCabecera(ArrayList<String> traza, int[] maximos){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<maximos[0];i++){
			if (i>0)
			{
				sb.append("__&__");
			}	
			sb.append("TAG" + (i+1));
		}
		sb.append("__&&__");
		for(int i=0;i<maximos[1];i++){
			if (i>0)
			{
				sb.append("__&__");
			}	
			sb.append("TAG" + (i+1));
		}
		traza.add(0, sb.toString());
	}

	/******************************************************************************/
	/*  Metodo:  getMaxCamposDeLista                                   		      */
	/*  Funcion: Evalua los valores maximos de los registros				      */
	/*           retorna el maximo de cada lista para crear las cabeceras		  */
	/*  OBJETOS: lista: Listado a procesar								    	  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	public int[] getMaxCamposDeLista(ArrayList<String> lista){
		int[] max = new int[2];
		for(int i=0; i<lista.size(); i++)
		{
            String[] listado = lista.get(i).split("__&&__");
            String[] dato1 = listado[0].split("__&__");
            for(int j=0; j<dato1.length; j++){            	
            	if (max[0] <dato1.length){   
        			max[0] = dato1.length;
            	}
    		}
            dato1 = listado[1].split("__&__");
            for(int j=0; j<dato1.length; j++){            	
            	if (max[1] <dato1.length){   
            		max[1] = dato1.length;
        		}
            }
        }
		return max;
	}	
	
	/******************************************************************************/
	/*  Metodo:  selectDataSimulandoMouseOver                              		  */
	/*  Funcion: Permite seleccionar valores de las lista y mostrar sus valores   */
	/*           Simulando el efecto Mouse - Over del mouse						  */
	/*  OBJETOS: elementos: Lista de valores a validar y procesar				  */ 
	/* 			 pto: Punto de data sobre la vista								  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private void selectDataSimulandoMouseOver(ArrayList<ElementoGrafico> elementos, Point pto){		
		for (ElementoGrafico elemento: elementos){
			if(((ElementoComplejo)elemento).contieneAlPuntoXY(pto)){
				setMensaje("Valor Origen: " +((ElementoComplejo)elemento).getTexto(), false);
				if (!elemento.isSeleccionado()){
					elemento.setColorRellenoTemp(Color.MAGENTA);
				} else {
					elemento.setColorRellenoTemp(Color.GREEN.darker());
				}
			} else {
				if (!elemento.isSeleccionado()){
					elemento.setColorRellenoTemp(null);
				} else {
					elemento.setColorRellenoTemp(Color.MAGENTA);
				}
			}
		}
		repaint();
	}
	
	/******************************************************************************/
	/*  Metodo:  selectDataSimulandoMouseOver                              		  */
	/*  Funcion: Permite seleccionar valores de las lista y mostrar sus valores   */
	/*           Simulando el efecto Mouse - Over del mouse						  */
	/*  OBJETOS: elementos: Lista de valores a validar y procesar				  */ 
	/* 			 pto: Punto de data sobre la vista								  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	private void clearSeleccionableValores(ArrayList<ElementoGrafico> elementos, ElementoGrafico elementoActual){		
		for (ElementoGrafico elemento: elementos){
			if (!elemento.equals(elementoActual)){
				elemento.setSeleccionado(false);
				elemento.setColorRellenoTemp(null);
			}
		}
		repaint();
	}
	
	/******************************************************************************/
	/*  Clase:  Componente                                    				      */
	/*  Funcion: Clase interna para dibujar los elementos a procesar     		  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	class Componente extends JPanel{
		/******************************************************************************/
		/*  Metodo:  paintComponent                                    				  */
		/*  Funcion: Metodo que permite pintar los elementos en la vista     		  */
		/*  OBJETOS: g: Componente grafico d ela vista 								  */
		/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
		/******************************************************************************/
		public void paintComponent(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;	
			super.paintComponent(g);
			if (origenComplejos != null){
				for (ElementoGrafico elemento: origenComplejos){
					elemento.draw(g2D);
				}
			}
			if (destinoComplejos != null){
				for (ElementoGrafico elemento: destinoComplejos){
					elemento.draw(g2D);
				}
			}
			if (elementosLineas != null){
				for (ElementoGrafico elemento: elementosLineas){
					elemento.draw(g2D);
				}
			}
			if (lineaTemp != null){
				lineaTemp.draw(g2D);
			}
		}  
	}
}


