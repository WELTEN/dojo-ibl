package org.celstec.arlearn2.oai;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

public class Identify extends OaiVerb {
	private static Namespace oaiNS = Namespace.getNamespace("oai","http://www.openarchives.org/OAI/2.0/");

	
	public static String getXmlAsString(OaiParameters op) {
		String returnString = getOaiFirstPart(op);
		returnString += "	<Identify>\n";
		returnString += "	  <repositoryName>OU iTunes U</repositoryName>\n";
		returnString += "	  <baseURL>"+Configuration.getBaseUrl()+"</baseURL>\n";
		returnString += "	  <protocolVersion>2.0</protocolVersion>\n";
		returnString += "	  <adminEmail>stefaan.ternier@ou.nl</adminEmail>\n";		
		returnString += "	  <earliestDatestamp>2000-01-01T00:00:00Z</earliestDatestamp>\n";		
		returnString += "	  <deletedRecord>no</deletedRecord>\n";		
		returnString += "	  <granularity>YYYY-MM-DDThh:mm:ssZ</granularity>\n";		
		returnString += "	</Identify>\n";
		return returnString + getOaiLastPart();
	}
	
	public static Document getXML() {
		Document doc = new Document();
		Element oaiPmhElement = new Element("OAI-PMH", oaiNS);
		doc.setRootElement(oaiPmhElement);
		setRequest(oaiPmhElement);
		Element identify = new Element("Identify", oaiNS);
		oaiPmhElement.addContent(identify);
		setIdentify(identify);
		return doc;
	}
	
	public static void setRequest(Element doc) {
		Element request = new Element("request", oaiNS);
		request.setAttribute("verb", "Identifier");
		request.setText("http://localhost:8888/OAI?verb=Identify");
		doc.addContent(request);
	}
	
	public static void setIdentify(Element identify) {
		Element repository = new Element("repositoryName", oaiNS);
		repository.setText("OUNL iTunes U");
		identify.addContent(repository);
		Element baseUrl = new Element("baseURL", oaiNS);
		baseUrl.setText("http://localhost:8888/OAI");
		identify.addContent(baseUrl);
		Element protocol = new Element("protocolVersion", oaiNS);
		protocol.setText("2.0");
		identify.addContent(protocol);
		Element adminEmail = new Element("adminEmail", oaiNS);
		adminEmail.setText("stefaan.ternier@ou.nl");
		identify.addContent(adminEmail);
		Element earliest = new Element("earliestDatestamp", oaiNS);
		earliest.setText("2000-01-01T00:00:00Z");
		identify.addContent(earliest);
		Element deletedRecord = new Element("deletedRecord", oaiNS);
		deletedRecord.setText("no");
		identify.addContent(deletedRecord);
		Element granularity = new Element("granularity", oaiNS);
		granularity.setText("YYYY-MM-DDThh:mm:ssZ");
		identify.addContent(granularity);
		
		
	}
	
	
//	<?xml version="1.0" encoding="UTF-8" ?><OAI-PMH xmlns="http://www.openarchives.org/OAI/2.0/" 
	//xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/ 
	//http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd">
	//<responseDate>2010-05-11T09:32:42Z</responseDate><request verb="Identify">http://sharetec.celstec.org/leraar24-oaipmh/OAIHandler</request><Identify><repositoryName>ounl</repositoryName><baseURL>http://sharetec.celstec.org/leraar24-oaipmh/OAIHandler</baseURL><protocolVersion>2.0</protocolVersion><adminEmail>stefaan.ternier@ou.nl</adminEmail><earliestDatestamp>1000-01-01T00:00:00Z</earliestDatestamp><deletedRecord>no</deletedRecord><granularity>YYYY-MM-DDThh:mm:ssZ</granularity><compression>gzip</compression><compression>deflate</compression><description><oai-identifier xmlns="http://www.openarchives.org/OAI/2.0/oai-identifier" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/oai-identifier http://www.openarchives.org/OAI/2.0/oai-identifier.xsd"><scheme>oai</scheme><repositoryIdentifier>oaicat.ariadne.org</repositoryIdentifier><delimiter>:</delimiter><sampleIdentifier>oai:oaicat.ariadne.org:hdl:OCLCNo/ocm00000012</sampleIdentifier></oai-identifier></description>
//		<description><toolkit xsi:schemaLocation="http://oai.dlib.vt.edu/OAI/metadata/toolkit http://oai.dlib.vt.edu/OAI/metadata/toolkit.xsd" xmlns="http://oai.dlib.vt.edu/OAI/metadata/toolkit"><title>OCLC's OAICat Repository Framework</title><author><name>Jeffrey A. Young</name><email>jyoung@oclc.org</email><institution>OCLC</institution></author><version>1.5.49</version><toolkitIcon>http://alcme.oclc.org/oaicat/oaicat_icon.gif</toolkitIcon><URL>http://www.oclc.org/research/software/oai/cat.shtm</URL></toolkit></description></Identify></OAI-PMH>

}
