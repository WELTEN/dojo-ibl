package org.celstec.arlearn2.gwtcommonlib.client.datasource.i18;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Constants.DefaultStringValue;

public interface DataSourceConstants extends Constants {

	
	//Game access values
	
	@DefaultStringValue("Is owner")
	String owner();
	@DefaultStringValue("Can edit")
	String write();
	@DefaultStringValue("Can view")
	String read();
}
