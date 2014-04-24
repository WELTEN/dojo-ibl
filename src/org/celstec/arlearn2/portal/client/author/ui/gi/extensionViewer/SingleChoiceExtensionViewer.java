package org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceAnswer;
import org.celstec.arlearn2.gwtcommonlib.client.objects.SingleChoiceTest;

import com.smartgwt.client.widgets.HTMLFlow;

public class SingleChoiceExtensionViewer extends HTMLFlow implements ExtensionViewer {

	public SingleChoiceExtensionViewer() {
		setWidth(230);

		String contents = "<hr><span class='exampleDropTitle'>Ajax  </span> " + "<b>A</b>synchronous <b>J</b>avaScript <b>A</b>nd <b>X</b>ML (AJAX) is a " + "Web development technique for creating interactive <b>web applications</b>.<hr>";
		setContents(contents);
	}

	@Override
	public void loadGeneralItem(GeneralItem gi) {
		SingleChoiceTest yt = (SingleChoiceTest) gi;
		String contents = "<b>Single choice test:</b><br><ul>";
		for (MultipleChoiceAnswer answer : yt.getAnswers()){
			contents += "<li>"+answer.getString(MultipleChoiceAnswer.ANSWER);	
		}
		
		contents += "</ul>";
		setContents(contents);
	}
}
