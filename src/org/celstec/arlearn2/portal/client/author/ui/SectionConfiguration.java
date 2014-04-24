package org.celstec.arlearn2.portal.client.author.ui;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.SectionStack;

public class SectionConfiguration extends SectionStack {
	
	public SectionConfiguration() {
		setVisibilityMode(VisibilityMode.MULTIPLE);  
        setAnimateSections(true);  
        setHeight("50%");  
        setOverflow(Overflow.HIDDEN);  
	}
	
	public void add(SectionConfig config) {
		addSection(config);
		
	}

}
