package com.wrl.xmlexcel.component;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ExtensionFilter extends FileFilter {
	private String description; // Filtro descripcion
	private String extension; 	// Filtro extension
	
	public ExtensionFilter(String ext, String descr) {
		extension = ext.toLowerCase(); 	// Almacenar extension en minuscula
		description = descr; 			// Almacenar la descripcion
	}
	
	public boolean accept(File file) {
		return(file.isDirectory()||file.getName().toLowerCase().endsWith(extension));
	}
	public String getDescription() {
		return description;
	}
}
