package org.celstec.arlearn2.oai;

import org.jdom.Namespace;

public class Configuration {
	
	public static final int amountOfRecords = 10;

	public static Namespace oaiNS = Namespace.getNamespace("oai","http://www.openarchives.org/OAI/2.0/");
	public static Namespace xsiNS = Namespace.getNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");
	

	public static String getBaseUrl() {
		return "http://ar-learn.appspot.com/oai";
	}
}
