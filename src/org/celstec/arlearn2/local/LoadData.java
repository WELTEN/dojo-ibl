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
import java.util.Properties;
import java.util.StringTokenizer;


public class LoadData {
	
	public static String root = "/Users/str/frequent/mobileLearning/ARLearn2GoogleProject/ARLearn2Libary/curl/florence/2011/arlearn1/";
	public static String authToken = "";

	
	public static void main(String[] args) {
		System.out.println("test");
		java.util.Properties p = PropertiesUtil.getProperties(new File(root+"props.txt"));
		authToken = p.getProperty("authtoken");
		processProperties(p);
	}

	private static void processProperties(Properties p) {
		StringTokenizer st = new StringTokenizer(p.getProperty("allitems"), ",");
		while (st.hasMoreTokens()) {
		  processGenItem(st.nextToken(), p);
		}

	}

	private static void processGenItem(String id, Properties p) {
		String account = p.getProperty(id+".account");
		String htmlFile = p.getProperty(id+".htmlFile");
		String title = p.getProperty(id+".title");
		String dependsOnAction = p.getProperty(id+".dependsOn.action");
		String dependsOnScope = p.getProperty(id+".dependsOn.scope");
		System.out.println(account+ " "+authToken);
		System.out.println(account+ " "+htmlFile);
		
		publishGenItem(account, htmlFile, title, dependsOnAction, dependsOnScope);
	}

	private static void publishGenItem(String account, String htmlFile, String title, String dependsOnAction, String dependsOnScope) {
		
		
	}
}
