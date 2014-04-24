package org.celstec.arlearn2.oai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.LomJDO;
import org.datanucleus.store.appengine.query.JDOCursorHelper;
import org.jdom.Element;

import com.google.appengine.api.datastore.Cursor;


public class ListRecords extends OaiVerb {
	
//	private static final int amountOfRecords = 10;
//
//	private static Namespace oaiNS = Namespace.getNamespace("oai","http://www.openarchives.org/OAI/2.0/");
//	private static Namespace xsiNS = Namespace.getNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");
	

//	public static Document getXML(String mdPrefix) {
//		Document doc = new Document();
//		Element oaiPmhElement = new Element("OAI-PMH", Configuration.oaiNS);
//		oaiPmhElement.setAttribute("schemaLocation", "http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd", Configuration.xsiNS);
//		doc.setRootElement(oaiPmhElement);
//		setRequest(oaiPmhElement, mdPrefix);
//		Element listRecords = new Element("ListRecords", Configuration.oaiNS);
//		oaiPmhElement.addContent(listRecords);
//		for (Element el: LomManager.getLoms(new Date(0), new Date(System.currentTimeMillis()))) {
//			listRecords.addContent(el);
//		}
//		return doc;
//	}
	

	
//	private static String getOaiFirstPart() {
//		String returnString = "";
//		returnString += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
//		returnString += "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
//		returnString += "	xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\">";
//		returnString += "	<responseDate>2010-05-11T15:56:54Z</responseDate>";
//		returnString += "	<request metadataPrefix=\"oai_lom\" verb=\"ListRecords\">http://sharetec.celstec.org/leraar24-oaipmh/OAIHandler</request>";
//		returnString += "	<ListRecords>";
//		return returnString;
//	}
	
//	private static String getOaiLastPart() {
//		String returnString = "	</ListRecords>";
//		returnString += "</OAI-PMH>";
//		return returnString;
//	}
	
	public static String getXmlAsString(OaiParameters op) {
		String returnString = getOaiFirstPart(op);
		returnString += "	<ListRecords>";

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(LomJDO.class);
			query.setRange(0, Configuration.amountOfRecords);
			List<LomJDO> results = null;
			if (op.getResumptionToken() != null && !op.getResumptionToken().equals("")){
				Cursor cursor = Cursor.fromWebSafeString(op.getResumptionToken());
				Map<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
				Map exParams = setFilters(query, op);
				results = (List<LomJDO>) query.executeWithMap(exParams);
			} else {
				Map exParams = setFilters(query, op);
				results = (List<LomJDO>) query.executeWithMap(exParams);
			}
			
//			List<Lom> results = (List<Lom>) query.execute();
			for (LomJDO l : results) {
				
				returnString += "<record>";
				returnString += getHeader(l);
				if (!l.isDeleted()) {
					returnString += "<metadata>";
					returnString += l.getLom();
					returnString += "</metadata>\n";
				}
				returnString += "</record>\n";
			}
			if (results.size() >= Configuration.amountOfRecords) {
				Cursor cursor = JDOCursorHelper.getCursor(results);	
				String cursorString = cursor.toWebSafeString();
				returnString += "<resumptionToken expirationDate=\"2010-05-11T16:56:55Z\" "+
				"completeListSize=\""+results.size()+"\">"+op.getCursorPrefix()+cursorString+"</resumptionToken>";
			}
		} finally {
			pm.close();

		}
		returnString += "	</ListRecords>";
		return returnString+ getOaiLastPart();
	}
	
//	public static String getXmlAsString(String resumptionToken) {
//		String returnString = getOaiFirstPart();
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			Query query = pm.newQuery(Lom.class);
//			
//			query.setRange(0, Configuration.amountOfRecords);
//			
//			List<Lom> results = (List<Lom>) query.execute();
//			System.out.println(results.size());
////			int lastIndex = 0;
//			for (Lom l : results) {
//				returnString += "<record><header><identifier>oai:ipo.ounl:"+l.getId().getName()+"</identifier>"+
//				"<datestamp>2010-03-25T13:56:59Z"+l.getLastModificationDate()+"</datestamp></header><metadata>";
//				returnString += l.getXmlrep();
//				returnString += "</metadata></record>\n";
////				lastIndex = results.indexOf(l);
//
//			}
////			cursor = JDOCursorHelper.getCursor(results);
////			String cursorString = cursor.toWebSafeString();
////			returnString += "<resumptionToken expirationDate=\"2010-05-11T16:56:55Z\" "+
////			"completeListSize=\""+results.size()+"\">"+cursorString+"</resumptionToken>";
//			if (results.size() <= Configuration.amountOfRecords) {
//				cursor = JDOCursorHelper.getCursor(results);
//				String cursorString = cursor.toWebSafeString();
//				returnString += "<resumptionToken expirationDate=\"2010-05-11T16:56:55Z\" "+
//				"completeListSize=\""+results.size()+"\">"+cursorString+"</resumptionToken>";
//			}
//		} finally {
//			pm.close();
//
//		}
//		return returnString+ getOaiLastPart();
//	}
	
	
	
	public static void setRequest(Element doc, String mdPrefix) {
		Element request = new Element("request", Configuration.oaiNS);
		request.setAttribute("verb", "ListRecords");
		request.setAttribute("metadataPrefix", mdPrefix);
		request.setText("http://localhost:8888/OAI?verb=Identify");
		doc.addContent(request);
	}
}
