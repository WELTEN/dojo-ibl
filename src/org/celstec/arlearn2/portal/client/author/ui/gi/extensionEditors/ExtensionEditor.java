package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;

public interface ExtensionEditor {

	
	public void saveToBean(GeneralItem gi);
	public boolean validate();

}
