package org.celstec.arlearn2.oai;

import org.celstec.arlearn2.jdo.classes.LomJDO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.Query;

//import nl.ounl.itunesu.server.db.Lom;

public abstract class OaiVerb {

	protected static String getOaiFirstPart(OaiParameters op) {
		String returnString = "";
		returnString += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		returnString += "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
		returnString += "	xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\">\n";
		returnString += "	<responseDate>"+OaiDateFormatter.getSingleTonInstance().format(new Date(System.currentTimeMillis()))+"</responseDate>\n";
		returnString += "	<request ";
		if (op.getMetadataPrefix() != null) {
			returnString += "metadataPrefix=\""+op.getMetadataPrefix()+"\" ";
		}
		if (op.getIdentifier() != null) {
			returnString += "identifier=\""+op.getIdentifier()+"\" ";
		}
		if (op.getUntil() != null) {
			returnString += "until=\""+op.getUntil()+"\" ";
		}
		if (op.getFrom() != null) {
			returnString += "from=\""+op.getFrom()+"\" ";
		}
		if (op.getResumptionToken() != null) {
			returnString += "resumptionToken=\""+op.getResumptionToken()+"\" ";
		}
		if (op.getSet() != null) {
			returnString += "set=\""+op.getSet()+"\" ";
		}
		returnString += "verb=\""+op.getVerb()+"\">"
				+ Configuration.getBaseUrl() + "</request>\n";
		return returnString;
	}
	
	protected static String getOaiLastPart() {
		String returnString = "</OAI-PMH>";
		return returnString;
	}
	
	protected static String getHeader(LomJDO l) {
		String returnString = "";
		if (l.isDeleted()) {
			returnString += "<header status =\"deleted\">";
		} else {
			returnString += "<header>";
		}
		returnString += "<identifier>"
			+ l.getGameId()
			+ "</identifier>"
			+ "<datestamp>"
			+ OaiDateFormatter.getSingleTonInstance().format(
					l.getLastModificationDate())
			+ "</datestamp></header>\n";
		return returnString;
	}
	
	private static String addParam(String p, String toAdd) {
		if (!p.equals("")) p += ", ";p+= toAdd;return p;
	}
	
	private static String addFilter(String p, String toAdd) {
		if (!p.equals("")) p += " && ";p+= toAdd;return p;
	}
	
	protected static Map setFilters(Query query, OaiParameters op) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		String declareParam = "";
		String filter = "";
		if (op.getSet() != null) {
			declareParam = addParam(declareParam, "java.lang.String code");
			filter = addFilter(filter, "iTunesCode == code");
			params.put("code", op.getSet());
		}
		if (op.getFromAsDate() != null) {
			declareParam = addParam(declareParam, "java.util.Date fromDate");
			filter = addFilter(filter, "lastModificationDate >= fromDate");
			params.put("fromDate", op.getFromAsDate());
		}
		if (op.getUntilAsDate() != null) {
			declareParam = addParam(declareParam, "java.util.Date untilDate");
			filter = addFilter(filter, "lastModificationDate <= untilDate");
			params.put("untilDate", op.getUntilAsDate());
		}
		if (!filter.equals("")) {
			query.declareParameters(declareParam);
			query.setFilter(filter);
		}
		
		return params;
	}
		
}
