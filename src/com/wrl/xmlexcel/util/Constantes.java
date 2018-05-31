/******************************************************************************/
/*  interface:   Constantes                                        			  */
/*  Funcion: Interfaz para control de valores constantes         			  */
/*  Fecha: 	  Agosto          Realizado por: William Lopera          		  */
/******************************************************************************/
package com.wrl.xmlexcel.util;

public interface Constantes {
	// Carpeta por defecto
	String DEFAULT_DIRECTORY		= "C://temp//XMLExcel";
		
    // Control de Mouse
    int MOUSE_CLICK    				= 1;
    int MOUSE_MOVED    				= 2;
    
    // Tipos de elementos graficos
    int ELEMENTO_COMPLEJO 				= 3;
    int ELEMENTO_LINEA		 			= 4;
    int ELEMENTO_COMPLEJO_lISTA_INPUT 	= 5;
    int ELEMENTO_COMPLEJO_lISTA_OUTPUT	= 6;
    
    // Data Lista Input
    int INPUT_INICIAL_X 			= 10;
    int INPUT_INICIAL_Y 			= 10;
    
 // Data Lista Output
    int OUTPUT_INICIAL_X			= 600;
    int OUTPUT_INICIAL_Y			= 10;
    
  // Control elementos complejos
    int COMPLEJO_ANCHO				= 500;
    int COMPLEJO_ALTO				= 20;
    int COMPLEJO_DELTA_X			= 0;
    int COMPLEJO_DELTA_Y			= 22;    
    
  // Parametros para el Scroll
    int SCROLL_AJUSTE_SIZE			= 50;
    int SCROLL_BLOQUE_INCREMENTO	= 200;
    int SCROLL_UNIDAD_INCREMENTO	= 50;
    
  // Identificador Temporal
    String IDENTIFICADOR_TEMP 		="LINEA_";
    
  // Rango de Presicion Punto sobre la linea
    int DELTA_PRECISION            = 4;
    
}

