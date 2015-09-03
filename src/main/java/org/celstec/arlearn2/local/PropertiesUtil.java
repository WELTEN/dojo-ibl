/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.text.html.HTMLDocument.Iterator;

public class PropertiesUtil {
	
	public static Properties getProperties(File f) {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return props;
	}
	
	public static void writeProperties(Properties props, String file) {
		writeProperties(props, new File(file));
	}
	
	public static void writeProperties(Properties props, File file) {
		PrintWriter pw;
		try {
			pw = new PrintWriter(file);
			java.util.Iterator<Entry<Object, Object>> it = props.entrySet().iterator();
			Vector<String> keyList = new Vector<String>();
			while (it.hasNext()) {
				Map.Entry<java.lang.Object,java.lang.Object> entry = (Map.Entry<java.lang.Object,java.lang.Object>) it
						.next();
				keyList.add((String)entry.getKey());
				
				
			}
			Collections.sort(keyList);
			for (String key: keyList) {
				String entry = (String) props.get(key);
				pw.write(key.replace(":", "\\:")+ " = "+entry+"\n");
			}
//			props.list(pw);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
