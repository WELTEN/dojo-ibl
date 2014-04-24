package org.celstec.arlearn2.oai;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

public class OaiParameters {
	
	public final static String IDENTIFY = "Identify";
	public final static String GETRECORD = "GetRecord";
	public final static String LISTRECORDS = "ListRecords";
	public final static String LISTMETADATAFORMATS = "ListMetadataFormats";
	public final static String LISTIDENTIFIERS = "ListIdentifiers";
	
	private static final String VERB = "verb";
	private static final String METADATAPREFIX = "metadataPrefix";
	private static final String IDENTIFIER = "identifier";
	private static final String FROM = "from";
	private static final String UNTIL = "until";
	private static final String SET = "set";
	private static final String RESUMPTIONTOKEN = "resumptionToken";
	
	private String verb;
	private String metadataPrefix;
	private String identifier;
	private Date from;
	private Date until;
	private String set;
	private String resumptionToken;
	
	public OaiParameters(HttpServletRequest req) {
		Map requestParams = req.getParameterMap();
		Iterator<String> it =requestParams.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			if (key.equals(VERB)) setVerb(req.getParameter(key));
			if (key.equals(METADATAPREFIX)) setMetadataPrefix(req.getParameter(key));
			if (key.equals(IDENTIFIER)) setIdentifier(req.getParameter(key));
			if (key.equals(FROM)) setFrom(req.getParameter(key));
			if (key.equals(UNTIL)) setUntil(req.getParameter(key));
			if (key.equals(SET)) setSet(req.getParameter(key));
			if (key.equals(RESUMPTIONTOKEN)) setResumptionToken(req.getParameter(key));
		}
	}
	
	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	public String getMetadataPrefix() {
		return metadataPrefix;
	}
	public void setMetadataPrefix(String metadataPrefix) {
		this.metadataPrefix = metadataPrefix;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getFrom() {
		if (from == null) return null;
		return OaiDateFormatter.getSingleTonInstance().format(from);
	}
	
	public Date getFromAsDate() {
		return from;
	}

	public void setFrom(String from) {
		try {
			this.from = OaiDateFormatter.getSingleTonInstance().parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setFrom(Date d) {
		this.from = d;
	}

	public String getUntil() {
		if (until == null) return null;

		return OaiDateFormatter.getSingleTonInstance().format(until);
	}
	
	public Date getUntilAsDate() {
		return until;
	}

	public void setUntil(String until) {
		try {
			this.until = OaiDateFormatter.getSingleTonInstance().parse(until);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setUntil(Date d) {
		this.until = d;
	}
	
	public String getSet() {
		return set;
	}
	
	public void setSet(String set) {
		this.set = set;
	}
	
	public String getResumptionToken() {
		return resumptionToken;
	}
	
	public void setResumptionToken(String resumptionToken) {
		StringTokenizer st = new StringTokenizer(resumptionToken, ":");
		if (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (!token.equals("*")) setFrom(new Date(Long.parseLong(token)));
		}
		if (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (!token.equals("*")) setUntil(new Date(Long.parseLong(token)));
		}
		if (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (!token.equals("*")) setSet(token);
		}
		if (st.hasMoreTokens()) {
			String token = st.nextToken();
			this.resumptionToken = token;
		}
	}
	
	public String getCursorPrefix() {
		String untilString = (until == null)? "*" : until.getTime()+"";
		String fromString = (from == null)? "*" : from.getTime()+"";
		String setString = (set == null)? "*" : set;
		return fromString+":"+untilString+":"+setString+":";
	}
	
	public boolean isIdentify() {
		return verb.equals(IDENTIFY);
	}
	
	public boolean isListRecords() {
		return verb.equals(LISTRECORDS);
	}
	
	public boolean isListMetadataFormats(){
		return verb.equals(LISTMETADATAFORMATS);
	}
	
	public boolean isListIdentifiers() {
		return verb.equals(LISTIDENTIFIERS);
	}
	
	public boolean isGetRecord() {
		return verb.equals(GETRECORD);
	}

}
