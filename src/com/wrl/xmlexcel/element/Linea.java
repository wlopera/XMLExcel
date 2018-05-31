/******************************************************************************/
/*  Clase:   Linea                                                 			  */
/*  Funcion: Clase control de objetos  Line2D.Double						  */
/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
/******************************************************************************/
package com.wrl.xmlexcel.element;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;

import com.wrl.xmlexcel.util.Constantes;

public class Linea extends ElementoGrafico implements Constantes {
	private Line2D.Double linea;
	private Point ptoInicial;
	private Point ptoFinal;
	private String conexionInicial;
	private String conexionfinal;
	
	public Linea(String identificador, Color color, Color colorRelleno, Point ptoInicial, Point ptoFinal, BasicStroke grosor){
		super(identificador, color,colorRelleno, ELEMENTO_LINEA, grosor);
		this.ptoInicial = ptoInicial;
		this.ptoFinal = ptoFinal;		
	}
	
	/******************************************************************************/
	/*  Metodo:  draw                                                             */
	/*  Funcion: Dibujar la línea.                                                */
	/*  OBJETOS: g2D: Contexto gráfico                                            */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	public void draw(Graphics2D g2D) {
		// Se llama al método draw de la clase ElementoGrafico
		linea = new Line2D.Double(ptoInicial, ptoFinal);
	    draw(g2D, linea);
	}

	public Point getPtoInicial() {
		return ptoInicial;
	}

	public void setPtoInicial(Point ptoInicial) {		
		this.ptoInicial = ptoInicial;
	}

	public Point getPtoFinal() {
		return ptoFinal;
	}

	public void setPtoFinal(Point ptoFinal) {
		this.ptoFinal = ptoFinal;
				
	}

	public String getConexionInicial() {
		return conexionInicial;
	}

	public void setConexionInicial(String conexionInicial) {
		this.conexionInicial = conexionInicial;
	}

	public String getConexionfinal() {
		return conexionfinal;
	}

	public void setConexionfinal(String conexionfinal) {
		this.conexionfinal = conexionfinal;
	}
	
	/******************************************************************************/
	/*  Metodo:  contieneAlPuntoXY  		                                      */
	/*  Funcion: Realiza un calculo matematico para analizar el valor		      */
	/*  		 de la pendiente. 												  */
	/* 			  - Pendientes iguales: pto sobre la recta (true)	  			  */
	/* 			  - Pendientes diferentes: pto lejos de la recta (false)		  */
	/*  Nota:    Toma un area de precision. Pto cercano al pto sobre la recta     */
	/*  OBJETOS: ptoActual: Punto de data sobre la vista						  */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	public boolean contieneAlPuntoXY(Point ptoActual){
		float pendiente = getPendiente(ptoInicial, ptoFinal); 
		int yMuestra = (int)((pendiente*(ptoActual.getX()-ptoInicial.getX()))+ptoInicial.getY()); 
		if ( ((yMuestra -DELTA_PRECISION) < ptoActual.y) && ((yMuestra +DELTA_PRECISION) > ptoActual.y)){
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Identificador: " + getIdentificador() + 
				" - Linea [linea=" + linea + ", ptoInicial=" + ptoInicial
				+ ", ptoFinal=" + ptoFinal + ", conexionInicial="
				+ conexionInicial + ", conexionfinal=" + conexionfinal + "]";
	}

	private float getPendiente(Point point1, Point point2){
		return (float)((point2.getY()-point1.getY())/(point2.getX()-point1.getX()));	
	}
	
}
