package org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.YoutubeObject;

import com.smartgwt.client.widgets.HTMLFlow;

public class YoutubeExtensionViewer extends HTMLFlow implements ExtensionViewer {

	public YoutubeExtensionViewer() {
		setWidth(230);

		String contents = "<hr><span class='exampleDropTitle'>Ajax  </span> " + "<b>A</b>synchronous <b>J</b>avaScript <b>A</b>nd <b>X</b>ML (AJAX) is a " + "Web development technique for creating interactive <b>web applications</b>.<hr>";
		setContents(contents);
	}

	@Override
	public void loadGeneralItem(GeneralItem gi) {
		YoutubeObject yt = (YoutubeObject) gi;
		String contents = "<iframe  src='" + 
		yt.getString(YoutubeObject.YOUTUBE_URL).replace("http://www.youtube.com/watch?v=", "http://www.youtube.com/embed/") + 
		"' frameborder='0' allowfullscreen></iframe>";
//		String contents = "<object width='420' height='315'><param name='movie' value='"+ yt.getString(YoutubeObject.YOUTUBE_URL) +"'></param><param name='allowFullScreen' value='true'></param><param name='allowscriptaccess' value='always'></param><embed src='http://www.youtube.com/v/DjGD8Hx37cI?version=3&amp;hl=en_GB' type='application/x-shockwave-flash' width='420' height='315' allowscriptaccess='always' allowfullscreen='true'></embed></object>";
		setContents(contents);
	}

}
