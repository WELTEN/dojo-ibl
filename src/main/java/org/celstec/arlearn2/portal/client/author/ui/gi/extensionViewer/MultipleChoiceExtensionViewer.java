package org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceAnswer;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceTest;
import org.celstec.arlearn2.gwtcommonlib.client.objects.SingleChoiceTest;

import com.smartgwt.client.widgets.HTMLFlow;

public class MultipleChoiceExtensionViewer extends HTMLFlow implements ExtensionViewer {

	public MultipleChoiceExtensionViewer() {
		setWidth(230);

		String contents = "<hr><span class='exampleDropTitle'>Ajax  </span> " + "<b>A</b>synchronous <b>J</b>avaScript <b>A</b>nd <b>X</b>ML (AJAX) is a " + "Web development technique for creating interactive <b>web applications</b>.<hr>";
		setContents(contents);
	}

	@Override
	public void loadGeneralItem(GeneralItem gi) {
		MultipleChoiceTest yt = (MultipleChoiceTest) gi;
		String contents = "<b>Multiple choice test:</b><ul>";
		for (MultipleChoiceAnswer answer : yt.getAnswers()){
			contents += "<li>"+answer.getString(MultipleChoiceAnswer.ANSWER);	
		}
		
		contents += "</ul>";
		setContents(contents);
	}

}
