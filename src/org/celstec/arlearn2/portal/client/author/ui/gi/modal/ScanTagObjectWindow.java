package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import com.google.gwt.core.client.GWT;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.ScanTagObject;
import org.celstec.arlearn2.portal.client.author.ui.gi.GeneralItemsTab;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.ScanTagEditor;

import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.widgets.Canvas;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

public class ScanTagObjectWindow  extends GeneralItemWindow {
	
	ScanTagEditor editor;

    private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

    public ScanTagObjectWindow(GeneralItemsTab generalItemsTab, LatLng coordinate) {
		super(generalItemsTab, coordinate);
		setTitle(constants.createQrScanner());
		setWidth(500);
		setHeight(450);
	}
	
	@Override
	protected GeneralItem createItem() {
		ScanTagObject returnObject = new ScanTagObject();
		editor.saveToBean(returnObject);
		return returnObject;
	}
	
	protected Canvas getMetadataExtensions() {
		editor = new ScanTagEditor();
		return editor;
	}
}
