/**
 * LeerXML: Permite extraer los valores de un archivo XML a un archivo plano
 * autor: William Lopera		@pranical
 * fecha: Julio 2016
 */
package com.wrl.xmlexcel.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class LeerXML {
	private File xmlFile;
	
	public LeerXML(){}
	
	public LeerXML(File xmlFile){
		this.xmlFile = xmlFile;
	}
	
	public static void main(String args[]){
		System.out.println("Iniciando Procesamiento archivo c:/temp/xmlToExcelInput.xml");
		File xmlFile = new File( "c:\\temp\\xmlToExcelInput.xml" );			

		LeerXML leerXML = new LeerXML(xmlFile);
		leerXML.run(true);
	}
	
	@SuppressWarnings("unchecked")
	public void run(boolean conValor){
		try {
			Element rootNode = getRootNode();
			String name = rootNode.getName();
			int sizeChildren = rootNode.getChildren().size();
			if(sizeChildren > 0){
				ArrayList<String> listOutput = new ArrayList<String>();
				getChilder(rootNode.getChildren(), "<"+name+">", listOutput, conValor);
				
				// Mostrar valores por consola
				//showResultByConsole(listOutput);
				
				// Copiar resultados a archivo plano final
				writeFile(listOutput);
				
				// Extraer los valores planos del XML
				ArrayList<String> listInput  = readFile(xmlFile);
				
				// Copiar resultados a hoja de excel final
				System.out.println(writeExcel(listInput, listOutput, "c:\\temp\\xmlToExcelOutput.xls"));				
			}			
		} catch  (FileNotFoundException e){
			System.out.println("Error FileNotFoundException: "+ e.getMessage());
		} catch (JDOMException e) {
			System.out.println("Error JDOMException: "+ e.getMessage());
		} catch (IOException e) {
			System.out.println("Error IOException: "+ e.getMessage());
		}
	}
	
	/**
	 * getRootNode: Obtiene el nodo raiz de un XML
	 * @param xmlFile: ARchivo XML a procesar
	 * @return Elemento raiz
	 * @throws JDOMException
	 * @throws IOException
	 */
	public Element getRootNode() throws JDOMException, IOException{
		 //Se crea un SAXBuilder para poder parsear el archivo
	    SAXBuilder builder = new SAXBuilder();
		 //Se crea el documento a traves del archivo
        Document document = (Document) builder.build( xmlFile );        
        //Se obtiene la raiz 'tables'
        Element rootNode = document.getRootElement();
        return rootNode;
	}
	
	/**
	 * getChilder: Recorre la lista de elementos y genera una lista con los resultados
	 * @param list: Lista inicial
	 * @param valores: Valor de almacenamiento
	 * @param listado: Lista de valores 
	 * @return String de dato 
	 * @throws JDOMException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String getChilder(List<Element> list, String valores, ArrayList<String> listado, boolean conValor) throws JDOMException, IOException{
		for ( int i = 0; i < list.size(); i++ )
        {
        	//Se obtiene los campos de la lista
            Element campo = (Element)list.get( i );
            String name = campo.getName();            
            int sizeChildren = campo.getChildren().size();
    		if (sizeChildren>0){
    				getChilder(campo.getChildren(), valores + "__&__<" + name+ ">", listado, conValor);
			} else if (sizeChildren==0){
				// Si se requiere almacenar el valor
				if (conValor){
					listado.add(i, valores +"__&__<"+  name + "/>"+"__&__"+campo.getValue());
				} else {
					listado.add(i, valores +"__&__<"+  name + "/>");
				}
			}
        }
        return valores;
	}
	
	/**
	 * showResultByConsole: Imprmir los resultados por pantalla
	 * @param listado
	 */
	public void showResultByConsole(ArrayList<String> listado){
		 for(String dato: listado){ 
	        System.out.println(dato);
	     }
	}
	
	/**
	 * writeFile: Imprimir los resultados a un archivo output.txt de salida
	 * @param listado: Lista de valores finales
	 * @throws FileNotFoundException
	 */
    public void writeFile(ArrayList<String> listado)throws FileNotFoundException{
        PrintStream descritor = new PrintStream("c:\\temp\\xmlToExcelOutput.txt");
        for(String dato: listado){ 
        	descritor.println(dato);
        }
        descritor.flush();
        descritor.close();
        System.out.println("\t==> Se genero el archivo c:/temp/xmlToExcelOutput.txt");
    }
    
    /**
     * Crea una hoja Excel y la guarda.
     * 
     * @param args
     */
    @SuppressWarnings("deprecation")
	public String writeExcel(ArrayList<String> listInput, ArrayList<String> listOutput, String filename) {
        try {
            // Se crea el libro
            HSSFWorkbook libro = new HSSFWorkbook();

          // Se crea una hoja dentro del libro para entrada de datos
            HSSFSheet hoja = libro.createSheet("INPUT");          
            
         // Procesar registros de salida
            for(int i=0; i<listInput.size(); i++){ 
            	  // Se crea una fila dentro de la hoja
                HSSFRow fila = hoja.createRow(i);
    	        // Se crea una celda dentro de la fila
    	        HSSFCell celda = fila.createCell((short) 0);
    	        // Se crea el contenido de la celda y se mete en ella.
    	        HSSFRichTextString texto = new HSSFRichTextString(listInput.get(i));
    	        celda.setCellValue(texto);
            }
          	
            // Se crea una hoja dentro del libro
            hoja = libro.createSheet("OUTPUT");                          
            
            // Definir size las primeras 15 columna en 20 
            for (int i=0; i<15; i++)
            	hoja.setColumnWidth(i, 256 * 20);

            HSSFCellStyle style = libro.createCellStyle();
            HSSFFont font = libro.createFont();
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);                 

            
            // Procesar registros de salida
            for(int i=0; i<listOutput.size(); i++){
            	  // Se crea una fila dentro de la hoja
                HSSFRow fila = hoja.createRow(i);
                String[] lista = listOutput.get(i).split("__&__");
                
                for (int j =0; j<lista.length; j++){   
        	        // Se crea una celda dentro de la fila
        	        HSSFCell celda = fila.createCell(j);
        	        if (j < lista.length -1)
        	        	celda.setCellStyle(style);
        	        
        	        // Se crea el contenido de la celda y se mete en ella.
        	        HSSFRichTextString texto = new HSSFRichTextString(lista[j]);
        	        celda.setCellValue(texto);                	
                }
            }

            // Se salva el libro.
            FileOutputStream elFichero = new FileOutputStream(filename);
            libro.write(elFichero);
            elFichero.flush();
            elFichero.close();
            String salida = "Se genero el archivo "+ filename;
            salida += "\n\tCantidad de lineas procesados: "+ listOutput.size();
            return salida; 
        } catch (Exception e) {
            System.out.println("LeerXML - Error: " + e.getMessage());
            return null;
        }
    }
    
    
    /**
     * readFile: Permite recorrer y almacenar los datos del archivo XML en una lista
     * @param archivo
     * @return
     */
    public ArrayList<String> readFile(File archivo) {
    	ArrayList<String> lines = new ArrayList<String>();
        FileReader fr = null;
        BufferedReader br = null;

        try {
           // Apertura del fichero y creacion de BufferedReader para lectura 
           fr = new FileReader (archivo);
           br = new BufferedReader(fr);

           // Lectura del fichero
           String linea;
           while((linea=br.readLine())!=null){
        	   lines.add(linea);
           }          
        }
        catch(Exception e){
        	System.out.println("Error readFile - Exception1: " + e.getMessage());
        }finally{
           // En el finally cerramos el fichero, para asegurarnos
           // que se cierra tanto si todo va bien como si salta 
           // una excepcion.
           try{                    
              if( null != fr ){   
                 fr.close();     
              }                  
           }catch (Exception e2){ 
              System.out.println("Error readFile - Exception2: " + e2.getMessage());
           }
        }
        return lines;
     }
    
    
	public File getXmlFile() {
		return xmlFile;
	}

	public void setXmlFile(File xmlFile) {
		this.xmlFile = xmlFile;
	}

	public ArrayList<String> getXML(){
		try {
			// Extraer los valores planos del XML
			return readFile(xmlFile);
		} catch (Exception e) {
			System.out.println("Error IOException: "+ e.getMessage());
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getTrxsXML(boolean conValor){
		try {
			Element rootNode = getRootNode();
			String name = rootNode.getName();
			int sizeChildren = rootNode.getChildren().size();
			if(sizeChildren > 0){
				ArrayList<String> listOutput = new ArrayList<String>();
				getChilder(rootNode.getChildren(), "<"+name+">", listOutput, conValor);
				listOutput = depurarLista(listOutput);
				return listOutput;
			}			
		} catch (Exception e) {
			System.out.println("Error IOException: "+ e.getMessage());
		}
		return null;
	}
	
	private ArrayList<String> depurarLista(ArrayList<String> lista){
		ArrayList<String> newLista = new ArrayList<String>();
		for (String valor: lista){
			boolean existe = false;
			for (String newValor: newLista){
				if (valor.equalsIgnoreCase(newValor)){
					existe = true;
					break;
				}
			}
			if (!existe){
				newLista.add(valor);
			}
		}
		return newLista;
	}
	

    /**
     * Crea una hoja Excel y la guarda.
     * 
     * @param args
     */
	public String createExcel(ArrayList<String> lista, String filename, int maximo) {
        try {
            // Se crea el libro
            HSSFWorkbook libro = new HSSFWorkbook();
          	
            // Se crea una hoja dentro del libro
            HSSFSheet hoja = libro.createSheet("TRANSFORMACION");                          
            
            // Definir size las primeras 15 columna en 20 
            for (int i=0; i<15; i++)
            	hoja.setColumnWidth(i, 256 * 20);

            HSSFCellStyle style = libro.createCellStyle();
            HSSFFont font = libro.createFont();
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);                 

            // Procesar registros de salida
            for(int i=0; i<lista.size(); i++){
            	// Se crea una fila dentro de la hoja
                HSSFRow fila = hoja.createRow(i);
                String[] listado = lista.get(i).split("__&&__");
                int cont = 0;
                for (int j=0; j<listado.length; j++){
                	String[] datos = listado[j].split("__&__");
	                for (int k=0; k<datos.length; k++){   
	        	        // Se crea una celda dentro de la fila
	        	        HSSFCell celda = fila.createCell(cont++);
	        	        if (k < datos.length -1)
	        	        	celda.setCellStyle(style);	        	        
	        	        // Se crea el contenido de la celda y se mete en ella.
	        	        HSSFRichTextString texto = new HSSFRichTextString(datos[k]);
	        	        celda.setCellValue(texto);                	
	                }
	                // Se completan los valores al maximo
	                for (int v=cont; v<maximo; v++){
	                	// Se crea una celda intermedia 
	        	        HSSFCell celda = fila.createCell(cont++);
	        	        celda.setCellValue(new HSSFRichTextString(""));	
	                }
	                // Se crea una celda intermedia 
        	        HSSFCell celda = fila.createCell(cont++);
        	        // Se crea el contenido de la celda y se mete en ella.
        	        if (i==0 && j==0){ // Evalua si es el primer registro
        	        	celda.setCellValue(new HSSFRichTextString("TRANSFORMADA"));
        	        } else {
        	        	celda.setCellValue(new HSSFRichTextString(""));
        	        }
                }
            }

            // Se salva el libro.
            FileOutputStream elFichero = new FileOutputStream(filename);
            libro.write(elFichero);
            elFichero.flush();
            elFichero.close();
            String salida = "Se genero el archivo "+ filename;
            salida += "\n\tCantidad de lineas procesados: "+ lista.size();
            return salida; 
        } catch (Exception e) {
            System.out.println("LeerXML - Error: " + e.getMessage());
            return null;
        }
    }
}