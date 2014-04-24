package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import com.google.gwt.core.client.GWT;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.SingleChoiceTest;
import org.celstec.arlearn2.portal.client.author.ui.gi.GeneralItemsTab;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.SingleChoiceExtensionEditor;

import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.widgets.Canvas;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

public class SingleChoiceTestWindow extends GeneralItemWindow {
	
	SingleChoiceExtensionEditor editor;

    private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

    public SingleChoiceTestWindow(GeneralItemsTab generalItemsTab, LatLng coordinate) {
		super(generalItemsTab, coordinate);
		setTitle(constants.createSingleChoice());
		setWidth(500);
		setHeight(450);
	}
	
	@Override
	protected GeneralItem createItem() {
		SingleChoiceTest returnObject = new SingleChoiceTest();
		editor.saveToBean(returnObject);
		return returnObject;
	}
	
	protected Canvas getMetadataExtensions() {
		editor = new SingleChoiceExtensionEditor();
		return editor;
	}
}