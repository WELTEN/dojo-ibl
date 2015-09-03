package org.celstec.arlearn2.oai;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OaiDateFormatter extends SimpleDateFormat {
	
	private static OaiDateFormatter thisInstance;
	private static SimpleDateFormat thisInstance2;
	
	private OaiDateFormatter() {
		super("yyyy-MM-dd'T'HH:mm:ss'Z'");
	}
	
	public static OaiDateFormatter getSingleTonInstance() {
		if (thisInstance == null) {
			thisInstance2 = new SimpleDateFormat("yyyy-MM-dd");
			thisInstance = new OaiDateFormatter();
		}
		return thisInstance;
	}

	@Override
	public Date parse(String source) throws ParseException {
		// TODO Auto-generated method stub
		try {
		return super.parse(source);
		} catch (ParseException e) {
			return thisInstance2.parse(source);
		}
	}
	
	
}
