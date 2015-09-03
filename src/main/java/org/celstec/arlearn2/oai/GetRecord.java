package org.celstec.arlearn2.oai;


import javax.jdo.PersistenceManager;

import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.LomJDO;

import com.google.appengine.api.datastore.KeyFactory;

public class GetRecord extends OaiVerb {
	
	
	public static String getXmlAsString(OaiParameters op) {
		String returnString = getOaiFirstPart(op);
		returnString += "	<GetRecord>";
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			if (op.getIdentifier() != null) {
                LomJDO l = pm.getObjectById(LomJDO.class, KeyFactory.createKey(LomJDO.class.getSimpleName(), Long.parseLong(op.getIdentifier())));
		
				returnString += getHeader(l);
				if (!l.isDeleted()) {
				returnString += "<metadata>";
				returnString += l.getLom();
				returnString += "</metadata>";
				}
			}

		} finally {
			pm.close();

		}
		returnString += "	</GetRecord>";
		return returnString + getOaiLastPart();
	}

}
