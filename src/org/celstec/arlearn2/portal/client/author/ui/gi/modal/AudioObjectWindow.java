package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import com.google.gwt.core.client.GWT;
import org.celstec.arlearn2.gwtcommonlib.client.objects.AudioObject;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.author.ui.gi.GeneralItemsTab;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.AudioExtensionEditor;

import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.widgets.Canvas;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

public class AudioObjectWindow extends GeneralItemWindow {
	
	AudioExtensionEditor editor;
    private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

    public AudioObjectWindow(GeneralItemsTab generalItemsTab, LatLng coordinate) {
		super(generalItemsTab, coordinate);
		setTitle(constants.createAudioObject());
		setWidth(500);
		setHeight(450);
	}
	
	@Override
	protected GeneralItem createItem() {
		AudioObject returnObject = new AudioObject();
		editor.saveToBean(returnObject);
		return returnObject;
	}
	
	protected Canvas getMetadataExtensions() {
		editor = new AudioExtensionEditor();
		return editor;
	}
	
	public boolean validate() {
		return super.validate() && editor.validate();
	}
}
