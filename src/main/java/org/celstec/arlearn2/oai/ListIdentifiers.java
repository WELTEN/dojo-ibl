package org.celstec.arlearn2.oai;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.LomJDO;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import com.google.appengine.api.datastore.Cursor;

public class ListIdentifiers extends OaiVerb {

	public static String getXmlAsString(OaiParameters op) {
		String returnString = getOaiFirstPart(op);
		returnString += "	<ListIdentifiers>";
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
                returnString += getHeader(l);
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

		returnString += "	</ListIdentifiers>";

		return returnString + getOaiLastPart();
	}
	
	

//	private static String getOaiFirstPart() {
//		String returnString = "";
//		returnString += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
//		returnString += "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
//		returnString += "	xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\">";
//		returnString += "	<responseDate>2010-05-11T15:56:54Z</responseDate>";
//		returnString += "	<request metadataPrefix=\"oai_lom\" verb=\"ListIdentifiers\">"
//				+ Configuration.getBaseUrl() + "</request>";
//		return returnString;
//	}

}
