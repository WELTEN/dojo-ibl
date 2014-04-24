package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import com.google.gwt.core.client.GWT;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.YoutubeObject;
import org.celstec.arlearn2.portal.client.author.ui.gi.GeneralItemsTab;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.YoutubeObjectEditor;

import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.widgets.Canvas;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

public class YoutubeObjectWindow extends GeneralItemWindow {
	
	YoutubeObjectEditor editor;
    private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);


    public YoutubeObjectWindow(GeneralItemsTab generalItemsTab, LatLng coordinate) {
		super(generalItemsTab, coordinate);
		setTitle(constants.createYoutubeObject());
		setWidth(500);
		setHeight(450);
	}
	
	@Override
	protected GeneralItem createItem() {
		YoutubeObject returnObject = new YoutubeObject();
		editor.saveToBean(returnObject);
		return returnObject;
	}
	
	protected Canvas getMetadataExtensions() {
		editor = new YoutubeObjectEditor();
		return editor;
	}
}
