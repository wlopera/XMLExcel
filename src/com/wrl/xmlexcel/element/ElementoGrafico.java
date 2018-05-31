/******************************************************************************/
/*  Clase:   ElementoGrafico                                                  */
/*  Funcion: Clase de control de tipos de elementos a dubujar en la vista     */
/*  Fecha:   Agosto          Realizado por: William Lopera          		  */
/******************************************************************************/
package com.wrl.xmlexcel.element;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;

import com.wrl.xmlexcel.util.Constantes;

public abstract class ElementoGrafico implements Constantes {
	private String identificador;
	private int tipoElemento = -1;
	private Color color;
	private Color colorRelleno;
	private Color colorRellenoTemp;
	private boolean seleccionado;
	private BasicStroke grosor; 
	
	// Constructor
	public ElementoGrafico(String identificador, Color color, Color colorRelleno, int tipoElemento, BasicStroke grosor){
		this.identificador = identificador;
		this.color = color;
		this.colorRelleno = colorRelleno;
		this.tipoElemento = tipoElemento;
		this.grosor = grosor;
		
	}
	
	/******************************************************************************/
	/*  Metodo:  draw                                                             */
	/*  Funcion: Declaración del método draw que se implementa en cada elemento.  */
	/*  OBJETOS: g2D: Contexto gráfico. 										  */
	/*           view: Vista sobre la que se dibujan los elementos graficos       */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	public abstract void draw(Graphics2D g2D);

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColorRelleno() {
		if (colorRellenoTemp != null){
			return colorRellenoTemp;
		} else {
			return colorRelleno;
		}
	}

	public void setColorRelleno(Color colorRelleno) {
		this.colorRelleno = colorRelleno;
	}
	
	public Color getColorRellenoTemp() {
		return colorRellenoTemp;
	}

	public void setColorRellenoTemp(Color colorRellenoTemp) {
		this.colorRellenoTemp = colorRellenoTemp;
	}

	public int getTipoElemento() {
		return tipoElemento;
	}

	public void setTipoElemento(int tipoElemento) {
		this.tipoElemento = tipoElemento;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public BasicStroke getGrosor() {
		return grosor;
	}

	public void setGrosor(BasicStroke grosor) {
		this.grosor = grosor;
	}

	/******************************************************************************/
	/*  Método:  draw                                                             */
	/*  Función: Dibujar el elemento indicado.                                    */
	/*  OBJETOS: g2D: Contexto gráfico.                                           */
	/*           element: Elemento a ser dibujado.                                */
	/*           view: Vista sobre la que se dibujan los elementos graficos       */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	  protected void draw(Graphics2D g2D, Shape element) {
	    if(g2D != null) {
	    	Stroke stroke = g2D.getStroke();
	    	Color colorPaint = g2D.getBackground();
	    	if (color != null)
	    	{
	    		colorPaint = color;
	    	}
	    	if (grosor != null){
	    		g2D.setStroke(grosor);
	    	}
	    	
	    	g2D.setColor(colorPaint);
		    g2D.draw(element);
	    	if (getColorRelleno() != null)
	    	{
	    		g2D.setColor(getColorRelleno());
			    g2D.fill(element);
	    	}
	    	g2D.setColor(g2D.getBackground());
	    	g2D.setStroke(stroke);
	    }
	  }
}
