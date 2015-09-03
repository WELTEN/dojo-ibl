package org.celstec.arlearn2.oai;


import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

import com.google.appengine.api.datastore.KeyFactory;

public class ListMetadataFormats extends OaiVerb {

	
	private static Namespace oaiNS = Namespace.getNamespace("oai","http://www.openarchives.org/OAI/2.0/");
	private static Namespace xsiNS = Namespace.getNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");
	
	public static String getXmlAsString(OaiParameters op) {
		String returnString = getOaiFirstPart(op);
		returnString += "	<ListMetadataFormats>";
		returnString += "	  <metadataPrefix>oai_lom</metadataPrefix>";
		returnString += "	  <schema>http://ltsc.ieee.org/xsd/lomv1.0/lom.xsd</schema>";
		returnString += "	  <metadataNamespace>http://ltsc.ieee.org/xsd/LOM</metadataNamespace>";
		
		returnString += "	</ListMetadataFormats>";
		return returnString + getOaiLastPart();
	}

	public static Document getXML() {
		Document doc = new Document();
		Element oaiPmhElement = new Element("OAI-PMH", oaiNS);
		oaiPmhElement.setAttribute("schemaLocation", "http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd", xsiNS);
		doc.setRootElement(oaiPmhElement);
		setRequest(oaiPmhElement);
		
		Element listMetadataFormats = new Element("ListMetadataFormats", oaiNS);
		oaiPmhElement.addContent(listMetadataFormats);
		addMetadataFormat(listMetadataFormats, "oai_lom", "http://ltsc.ieee.org/xsd/lomv1.0/lom.xsd", "http://ltsc.ieee.org/xsd/LOM");
		return doc;
	}
	
	public static void setRequest(Element doc) {
		Element request = new Element("request", oaiNS);
		request.setAttribute("verb", "ListMetadataFormats");
		request.setText("http://localhost:8888/OAI?verb=Identify");
		doc.addContent(request);
		doc.setNamespace(oaiNS);
	}
	
	public static void addMetadataFormat(Element el, String prefix, String schema, String namespace) {
		Element format = new Element("request", oaiNS);
		el.addContent(format);
		
		Element prefixEl = new Element("metadataPrefix", oaiNS);
		prefixEl.setText(prefix);
		el.addContent(prefixEl);
		
		Element schemaEl = new Element("schema", oaiNS);
		schemaEl.setText(schema);
		el.addContent(schemaEl);
		
		Element namesPaceel = new Element("metadataNamespace", oaiNS);
		namesPaceel.setText(namespace);
		el.addContent(namesPaceel);
	}
}
