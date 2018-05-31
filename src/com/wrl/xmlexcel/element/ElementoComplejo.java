package com.wrl.xmlexcel.element;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class ElementoComplejo extends ElementoGrafico {
	private Rectangle2D.Double rectangulo;
	private TextLayout textLayout;
	private Color colorTexto;
	private String texto;
	private int x;
	private int y;
	private ArrayList<String> conexiones;
	private Point ptoEnlace;
	
	public ElementoComplejo(String Identificador, 
							Color colorRectangulo, 
							Color colorRellenoRectangulo,
							Color colorTexto,
							String texto,
							int x, 
							int y, 
							int ancho, 
							int alto,
							int tipo,
							BasicStroke grosor){
		
		super(Identificador, colorRectangulo, colorRellenoRectangulo, ELEMENTO_COMPLEJO, grosor);		
		this.colorTexto = colorTexto;
		this.texto = texto;
		this.x = x;
		this.y = y;
		rectangulo = new Rectangle2D.Double(x,y,ancho, alto);
		conexiones = new ArrayList<String>();
		if (tipo == ELEMENTO_COMPLEJO_lISTA_INPUT){
			ptoEnlace = new Point(x+COMPLEJO_ANCHO, y + COMPLEJO_ALTO/2);
		} else {
			ptoEnlace = new Point(x, y + COMPLEJO_ALTO/2);
		}
	}
	
	public ArrayList<String> getConexiones() {
		return conexiones;
	}
	
	public void setConexion(String conexion) {
		conexiones.add(conexion);
	}
	
	public void deleteConexion(String conexion){
		for (int i=0; i<conexiones.size(); i++){
			String conex = conexiones.get(i);
			if (conex.equals(conexion)){
				conexiones.remove(i);
			}
		}
	}

	/******************************************************************************/
	/*  Metodo:  draw                                                             */
	/*  Funcion: Dibujar elemento complejo: Un texto y su bound (rectangulo).     */
	/*  OBJETOS: g2D: Contexto gráfico                                            */
	/*  Fecha: 	 Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	public void draw(Graphics2D g2D) {
		draw(g2D, rectangulo);
		int longitud = texto.length();
		String txt = texto;
		if (longitud > 80){
			txt = texto.substring(0, 20) + " ... <...> ... " + texto.substring(texto.length()-20, texto.length());
		}
		textLayout =  new TextLayout(txt, g2D.getFont(), g2D.getFontRenderContext());
		Color colorPaint = g2D.getBackground();
		if (colorTexto != null)
		{
			colorPaint = colorTexto;
		}
		g2D.setColor(colorPaint);
		textLayout.draw(g2D, x+5, y+15);	
	}
	
	/******************************************************************************/
	/*  Metodo:  contieneAlPuntoXY                                                */
	/*  Funcion: Evalua si el rectangulo contiene al punto o click introducido    */
	/*  OBJETOS:  pto: Punto sobre la vista                                       */
	/*  Fecha: 	  Agosto          Realizado por: William Lopera          		  */
	/******************************************************************************/
	public boolean contieneAlPuntoXY(Point pto){
		return rectangulo.contains(pto);
	}

	@Override
	public String toString() {
//		Point ptoInicial = new Point(x,y);
//		Point ptoFinal = new Point(x+500,y+22);
//		return "Ptoinicial: "+ ptoInicial + " -- PtoFinal: "+ ptoFinal + " -- " + texto;
		return "ElementoComplejo [identificador= " + getIdentificador() + ", texto= "+ texto + ", x=" + x + ", y=" + y + ", conexiones="
				+ conexiones + "]";
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Point getPtoEnlace() {
		return ptoEnlace;
	}

	public void setPtoEnlace(Point ptoEnlace) {
		this.ptoEnlace = ptoEnlace;
	}
}
