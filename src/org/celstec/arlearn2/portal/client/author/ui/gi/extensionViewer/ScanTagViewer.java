package org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.ScanTagObject;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.HTMLFlow;

public class ScanTagViewer extends HTMLFlow implements ExtensionViewer {

	public ScanTagViewer() {
		setWidth(230);
		String contents = "";
		setContents(contents);
		setVisibility(Visibility.INHERIT);
	}

	@Override
	public void loadGeneralItem(GeneralItem gi) {
		ScanTagObject yt = (ScanTagObject) gi;
		String contents = "";
		if (yt.getBoolean(ScanTagObject.AUTOLAUNCHQRREADER)){
			contents = "Scanner does  start automatically";
		} else {
			contents = "Scanner does not start automatically";
		}
		setContents(contents);
	}

}
