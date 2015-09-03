package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import com.google.gwt.core.client.GWT;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.NarratorItem;
import org.celstec.arlearn2.portal.client.author.ui.gi.GeneralItemsTab;

import com.google.maps.gwt.client.LatLng;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

public class NarratorItemWindow extends GeneralItemWindow {

    private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

    public NarratorItemWindow(GeneralItemsTab generalItemsTab, LatLng coordinate) {
		super(generalItemsTab, coordinate);
		setTitle(constants.createNarratorItem());
		setWidth(500);
		setHeight(450);
	}

	@Override
	protected GeneralItem createItem() {
		NarratorItem ni = new NarratorItem();
		
		return ni;
	}

}
