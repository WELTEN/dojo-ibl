package org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.AudioObject;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.HTMLFlow;

public class AudioExtensionViewer extends HTMLFlow implements ExtensionViewer {

	public AudioExtensionViewer() {
		setWidth(230);

		String contents = "<hr><span class='exampleDropTitle'>Ajax  </span> " + "<b>A</b>synchronous <b>J</b>avaScript <b>A</b>nd <b>X</b>ML (AJAX) is a " + "Web development technique for creating interactive <b>web applications</b>.<hr>";
		setContents(contents);
		setVisibility(Visibility.INHERIT);
	}

	@Override
	public void loadGeneralItem(GeneralItem gi) {
		AudioObject yt = (AudioObject) gi;
		String contents = "<audio controls> <source src='" + yt.getString(AudioObject.AUDIO_FEED) + "' ></audio>";
		setContents(contents);
	}

}