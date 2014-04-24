package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import com.google.gwt.core.client.GWT;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceImage;
import org.celstec.arlearn2.portal.client.author.ui.gi.GeneralItemsTab;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.MultipleChoiceImageExtensionEditor;

import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.widgets.Canvas;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

public class MultipleChoiceImageWindow  extends GeneralItemWindow {
	
	MultipleChoiceImageExtensionEditor editor;

    private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

    public MultipleChoiceImageWindow(GeneralItemsTab generalItemsTab, LatLng coordinate) {
		super(generalItemsTab, coordinate);
		setTitle(constants.createMultipleChoiceWithImages());
		setWidth(500);
		setHeight(450);
	}
	
	@Override
	protected GeneralItem createItem() {
		MultipleChoiceImage returnObject = new MultipleChoiceImage();
		editor.saveToBean(returnObject);
		return returnObject;
	}
	
	protected Canvas getMetadataExtensions() {
		editor = new MultipleChoiceImageExtensionEditor();
		return editor;
	}
}
